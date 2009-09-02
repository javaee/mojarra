/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 *
 * Contributor(s):
 *
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package com.sun.faces.application.resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;

import com.sun.faces.util.Util;
import com.sun.faces.util.FacesLogger;

/**
 * <p>
 * A {@link ResourceHelper} implementation for finding/serving resources
 * found on the classpath within the <code>META-INF/resources directory.
 * </p>
 *
 * @since 2.0
 */
public class ClasspathResourceHelper extends ResourceHelper {


    private static final Logger LOGGER = FacesLogger.RESOURCE.getLogger();

    private static final String BASE_PATH = "META-INF/resources";
    private static final String BASE_PATH_MATCH = "META-INF/resources/";
    private static final String SEPARATOR_CHAR_STR = "/";
    private static final char SEPARATOR_CHAR = '/';
    private static final String WEB_INF_LIB = "/WEB-INF/lib";
    private static final String WEB_INF_CLASSES_RESOURCES = "/WEB-INF/classes/META-INF/resources/";
    private static final String JAR_EXT = ".jar";
    private static final String JAR_PROTOCOL = "jar:";
    private static final String JAR_SEP = "!/";

    private static final String[] MOJARRA_JS_FILES = {
          "javax.faces/",
          "javax.faces/jsf-compressed.js",
          "javax.faces/jsf-uncompressed.js"
    };

    private PathNode resourcePathNodes = new PathNode(null, "/", false);


    // ------------------------------------------------------------ Constructors


    private ClasspathResourceHelper() {

        FacesContext ctx = FacesContext.getCurrentInstance();
        buildNodeTree(ctx, getWebappLibJarFiles(ctx));

    }


    // ---------------------------------------------------------- Public Methods


    public static ClasspathResourceHelper getInstance() {

        return new ClasspathResourceHelper();

    }


    // --------------------------------------------- Methods from ResourceHelper


    /**
     * @see com.sun.faces.application.resource.ResourceHelper#getBaseResourcePath()
     */
    public String getBaseResourcePath() {

        return BASE_PATH;

    }


    /**
     * @see ResourceHelper#getNonCompressedInputStream(ResourceInfo, javax.faces.context.FacesContext)
     */
    protected InputStream getNonCompressedInputStream(ResourceInfo resource, FacesContext ctx)
          throws IOException {

        ClassLoader loader = Util.getCurrentLoader(this.getClass());
        String path = resource.getPath();
        InputStream in = loader.getResourceAsStream(path);
        if (in == null) {
            // try using this class' loader (necessary when running in OSGi)
            in = this.getClass().getClassLoader().getResourceAsStream(path);
        }
        return in;

    }


    /**
     * @see com.sun.faces.application.resource.ResourceHelper#getURL(ResourceInfo, javax.faces.context.FacesContext)
     */
    public URL getURL(ResourceInfo resource, FacesContext ctx) {

        ClassLoader loader = Util.getCurrentLoader(this.getClass());
        URL url = loader.getResource(resource.getPath());
        if (url == null) {
            // try using this class' loader (necessary when running in OSGi)
            url = this.getClass().getClassLoader()
                  .getResource(resource.getPath());
        }
        return url;

    }


    /**
     * @see ResourceHelper#findLibrary(String, String, javax.faces.context.FacesContext)
     */
    public LibraryInfo findLibrary(String libraryName,
                                   String localePrefix,
                                   FacesContext ctx) {

        PathNode n;
        if (localePrefix == null) {
            n = resourcePathNodes;
        } else {
            n = resourcePathNodes.getPathNode(localePrefix);
            if (n == null) {
                return null;
            }
        }

        String[] libraryNodes = Util.split(libraryName, SEPARATOR_CHAR_STR);
        for (String e : libraryNodes) {
            n = n.getPathNode(e);
            if (n == null) {
                return null;
            }
        }


        try {
            List<String> subPaths = getSubPaths(n.getPathNodes());
            if (subPaths.isEmpty()) {
                return new LibraryInfo(libraryName,
                                       null,
                                       localePrefix,
                                       ClasspathResourceHelper.this);
            } else {
                VersionInfo version = getVersion(subPaths, false);
                return new LibraryInfo(libraryName,
                                       version,
                                       localePrefix,
                                       ClasspathResourceHelper.this);
            }
        } catch (Exception e) {
            throw new FacesException(e);
        }

    }


    /**
     * @see ResourceHelper#findResource(LibraryInfo, String, String, boolean, javax.faces.context.FacesContext)
     */
    public ResourceInfo findResource(LibraryInfo library,
                                     String resourceName,
                                     String localePrefix,
                                     boolean compressable,
                                     FacesContext ctx) {

        PathNode n = resourcePathNodes;
        if (library != null) {
            String[] libraryNodes = Util.split(library.getPath(), SEPARATOR_CHAR_STR);
            for (int i = 2, len = libraryNodes.length; i < len; i++) {
                n = n.getPathNode(libraryNodes[i]);
                if (n == null) {
                    return null;
                }
            }
        } else {
            if (localePrefix == null) {
                n = resourcePathNodes;
            } else {
                n = resourcePathNodes.getPathNode(localePrefix);
                if (n == null) {
                    return null;
                }
            }
        }
        String[] resourceNodes = Util.split(resourceName, SEPARATOR_CHAR_STR);
        for (String e : resourceNodes) {
            n = n.getPathNode(e);
            if (n == null) {
                return null;
            }
        }

        ResourceInfo value;
        try {
            List<String> subPaths = getSubPaths(n.getPathNodes());
            if (subPaths.isEmpty()) {
                if (library != null) {
                    value = new ResourceInfo(library,
                                             resourceName,
                                             null,
                                             compressable,
                                             resourceSupportsEL(resourceName,
                                                                ctx));
                } else {
                    value = new ResourceInfo(resourceName,
                                             null,
                                             localePrefix,
                                             ClasspathResourceHelper.this,
                                             compressable,
                                             resourceSupportsEL(resourceName,
                                                                ctx));
                }
            } else {
                VersionInfo version = getVersion(subPaths, true);
                if (version == null) {
                    if (LOGGER.isLoggable(Level.WARNING)) {
                        LOGGER.log(Level.WARNING,
                                   "jsf.application.resource.unable_to_determine_resource_version",
                                   resourceName);
                    }
                    return null;
                } else {
                    if (library != null) {
                        value = new ResourceInfo(library,
                                                 resourceName,
                                                 version,
                                                 compressable,
                                                 resourceSupportsEL(
                                                       resourceName,
                                                       ctx));
                    } else {
                        value = new ResourceInfo(resourceName,
                                                 version,
                                                 localePrefix,
                                                 ClasspathResourceHelper.this,
                                                 compressable,
                                                 resourceSupportsEL(
                                                       resourceName,
                                                       ctx));
                    }
                }
            }
        } catch (Exception e) {
            throw new FacesException(e);
        }
        if (value.isCompressable()) {
            value = handleCompression(value);
        }
        return value;

    }


    // --------------------------------------------------------- Private Methods


    private void buildNodeTree(FacesContext ctx, Set<JarFile> jarFiles) {

        if (jarFiles != null && !jarFiles.isEmpty()) {
            for (JarFile jarFile : jarFiles) {
                for (Enumeration<JarEntry> e = jarFile.entries(); e
                      .hasMoreElements();) {
                    JarEntry je = e.nextElement();
                    String eName = je.getName();
                    if (!eName.startsWith(BASE_PATH_MATCH)
                        || eName.length() <= 19) {
                        continue;
                    }
                    String pathName = eName.substring(19);
                    processPath(pathName, !je.isDirectory(), resourcePathNodes);
                }
            }
        }

        // special handling for mojarra scripts
        // If we add more scripts, then MOJARRA_JS_FILES needs to be updated
        // appropriately
        for (String mojarraJS : MOJARRA_JS_FILES) {
            processPath(mojarraJS,
                        mojarraJS.charAt(mojarraJS.length() - 1) == SEPARATOR_CHAR,
                        resourcePathNodes);
        }

        // add PathNode instances for WEB-INF/classes/META-INF/resources/
        buildPathNodesWebInfClasses(ctx.getExternalContext(),
                                    WEB_INF_CLASSES_RESOURCES,
                                    resourcePathNodes);

    }


    private PathNode processPath(String path, boolean leaf, PathNode pathNode) {

        String[] pElements = Util.split(path, SEPARATOR_CHAR_STR);
        PathNode currentNode = pathNode;
        for (String pElement : pElements) {
            PathNode node = currentNode.getPathNode(pElement);
            if (node == null) {

                PathNode n = new PathNode(currentNode, pElement, !leaf);
                currentNode.addPathNode(pElement, n);
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("ADD RESOURCE NODE: Parent Node ["
                                + currentNode.name
                                + "], new node path ["
                                + pElement
                                + "], leaf ["
                                + leaf
                                + ']');
                    LOGGER.fine("NODE PATH: " + n.getPath());
                }
                currentNode = n;
            } else {
                currentNode = node;
            }
        }
        return currentNode;

    }



    private void buildPathNodesWebInfClasses(ExternalContext extContext,
                                             String basePath,
                                             PathNode pathNode) {

        Set<String> subPaths = extContext.getResourcePaths(basePath);
        if (subPaths != null && !subPaths.isEmpty()) {
            for (String path : subPaths) {
                boolean leaf = !(path.charAt(path.length() - 1) == SEPARATOR_CHAR);
                String mPath;
                if (leaf) {
                    mPath = path.substring(path.lastIndexOf('/') + 1);
                } else {
                    mPath = path.substring(basePath.length(), path.length() - 1);
                }
                PathNode n = processPath(mPath, leaf, pathNode);
                if (!leaf) {
                    buildPathNodesWebInfClasses(extContext, path, n);
                }
            }
        }

    }



    private Set<JarFile> getWebappLibJarFiles(FacesContext ctx) {

        ExternalContext extContext = ctx.getExternalContext();
        Set<String> elements = extContext.getResourcePaths(WEB_INF_LIB);
        Set<JarFile> appJars = null;
        if (elements != null && !elements.isEmpty()) {
            for (String element : elements) {
                if (element.charAt(element.length() - 1) == SEPARATOR_CHAR
                    || !element.endsWith(JAR_EXT)) {
                    continue;
                }
                try {
                    URL url = extContext.getResource(element);
                    StringBuilder sb = new StringBuilder(32);
                    sb.append(JAR_PROTOCOL).append(url.toString()).append(JAR_SEP);
                    url = new URL(sb.toString());
                    JarFile jarFile =
                          ((JarURLConnection) url.openConnection())
                                .getJarFile();
                    if (appJars == null) {
                        appJars = new HashSet<JarFile>(elements.size());
                    }
                    appJars.add(jarFile);
                } catch (MalformedURLException mue) {
                    throw new FacesException(mue);
                } catch (IOException ioe) {
                    throw new FacesException(ioe);
                }
            }
        }
        return appJars;

    }


    private List<String> getSubPaths(Collection<PathNode> pathNodes)
          throws IOException {

        List<String> paths = new ArrayList<String>(pathNodes.size());
        for (PathNode n : pathNodes) {
            paths.add(n.getName());
        }

        return paths;

    }


    // ------------------------------------------------------ Nested Classes


    private final class PathNode {

        private TreeMap<String, PathNode> subpathNodes = new TreeMap<String, PathNode>();
        private PathNode parent;
        private String name;
        private boolean leaf;


        // ---------------------------------------------------- Constructors


        PathNode(PathNode parent, String name, boolean leaf) {

            if (name == null) {
                throw new IllegalArgumentException();
            }
            if (!leaf) {
                subpathNodes = new TreeMap<String, PathNode>();
            }
            this.name = name;
            this.parent = parent;
            this.leaf = leaf;

        }


        // -------------------------------------------------- Public Methods


        private String getName() {
            return name;
        }


        private void addPathNode(String name, PathNode n) {

            if (name == null || n == null) {
                throw new IllegalArgumentException();
            }

            subpathNodes.put(name, n);

        }


        private PathNode getPathNode(String name) {

            if (name == null) {
                throw new IllegalArgumentException();
            }

            return subpathNodes.get(name);

        }


        private Collection<PathNode> getPathNodes() {

            return subpathNodes.values();

        }


        private String getPath() {

            StringBuilder sb = new StringBuilder(32);
            sb.append(SEPARATOR_CHAR).append(name);
            PathNode p = parent;
            while (true) {
                if (p.parent == null) {
                    break;
                }
                sb.insert(0, p.name);
                sb.insert(0, SEPARATOR_CHAR);
                p = p.parent;
            }
            return sb.toString();

        }

        @Override
        public String toString() {
            return "PathNode{" +
                   "parent=" + parent +
                   ", name='" + name + '\'' +
                   ", leaf=" + leaf +
                   '}';
        }
        
    } // END PathNode

}
