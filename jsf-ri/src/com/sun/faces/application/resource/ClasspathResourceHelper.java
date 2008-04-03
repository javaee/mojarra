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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;

import com.sun.faces.util.Util;

/**
 * RELEASE_PENDING (rlubke) document
 *
 * @since 2.0
 */
public class ClasspathResourceHelper extends ResourceHelper {

    private static final ClasspathResourceHelper INSTANCE = new ClasspathResourceHelper();

    private static final String BASE_RESOURCE_PATH = "META-INF/resources";


    // ------------------------------------------------------------ Constructors


    protected ClasspathResourceHelper() { }


    // ---------------------------------------------------------- Public Methods


    public static ClasspathResourceHelper getInstance() {

        return INSTANCE;

    }


    // --------------------------------------------- Methods from ResourceHelper


    /**
     * RELEASE_PENDING (rlubke) document
     * @return
     */
    public String getBaseResourcePath() {

        return BASE_RESOURCE_PATH;

    }


    /**
     * RELEASE_PENDING (rlubke) document
     * @param path
     * @param ctx
     * @return
     */
    public InputStream getInputStream(String path, FacesContext ctx) {

        ClassLoader loader = Util.getCurrentLoader(this.getClass());
        return loader.getResourceAsStream(path);

    }


    /**
     * RELEASE_PENDING (rlubke) document
     * @param path
     * @param ctx
     * @return
     */
    public URL getURL(String path, FacesContext ctx) {

        ClassLoader loader = Util.getCurrentLoader(this.getClass());
        return loader.getResource(path);

    }


    public LibraryInfo findLibrary(String libraryName,
                                   String localePrefix,
                                   FacesContext ctx) {

        ClassLoader loader = Util.getCurrentLoader(this);
        String basePath;
        if (localePrefix == null) {
            basePath = getBaseResourcePath() + '/' + libraryName;
        } else {
            basePath = getBaseResourcePath()
                       + '/'
                       + localePrefix
                       + '/'
                       + libraryName;
        }

        URL basePathURL = loader.getResource(basePath);
        if (basePathURL == null) {
            return null;
        }

        try {
            List<String> subPaths = getSubPaths(basePathURL);
            if (subPaths.isEmpty()) {
                return new LibraryInfo(libraryName, null, this);
            } else {
                String version = getVersion(subPaths);
                return new LibraryInfo(libraryName, version, this);
            }
        } catch (Exception e) {
            throw new FacesException(e);
        }
    }

    public ResourceInfo findResource(LibraryInfo library,
                                     String resourceName,
                                     String localePrefix,
                                     FacesContext ctx) {

        ClassLoader loader = Util.getCurrentLoader(this);
        String basePath;
        if (library != null) {
            basePath = library.getPath() + '/' + resourceName;
        } else {
            if (localePrefix == null) {
                basePath = getBaseResourcePath() + '/' + resourceName;
            } else {
                basePath = getBaseResourcePath()
                           + '/'
                           + localePrefix
                           + '/'
                           + resourceName;
            }
        }

        URL basePathURL = loader.getResource(basePath);
        if (basePathURL == null) {
            return null;
        }

        try {
            List<String> subPaths = getSubPaths(basePathURL);
            if (subPaths.isEmpty()) {
                if (library != null) {
                    return new ResourceInfo(library, resourceName, null);
                } else {
                    return new ResourceInfo(resourceName, null, this);
                }
            } else {
                String version = getVersion(subPaths);
                if (library != null) {
                    return new ResourceInfo(library, resourceName, version);
                } else {
                    return new ResourceInfo(resourceName, version, this);
                }
            }
        } catch (Exception e) {
            throw new FacesException(e);
        }
    }


    // --------------------------------------------------------- Private Methods


    /**
     * RELEASE_PENDING (rlubke) document
     *
     * @param url
     *
     * @return
     */
    private List<String> getSubPaths(URL url)
    throws IOException, URISyntaxException {

        List<String> paths = null;
        if ("jar".equals(url.getProtocol())) {
            // The jar protocol will be used in the following cases:
            //  - web application contains META-INF/resources under
            //    WEB-INF/classes, but the web application isn't exploded
            //    during the deploymnet process
            //  - JAR file containing META-INF/resources which aren't
            //    exploded by the deployment process
            paths = getPathsFromJARUrl(url);
        } else if ("file".equals(url.getProtocol())) {
            paths = getPathsFromFileURL(url);
            // The file protocol appears to be common for the following
            // cases:
            //   - web application is exploded and META-INF/resources
            //     is under WEB-INF/classes
            //   - JARS included with the web application are exploded
            //     by the container
            //  At any rate, if the URL we have represents a directory,
            //  there is a good chance this means the library or resource
            //  versioned.  Return all path element names that are direct
            //  children of the base directory.

        }

        return ((paths == null) ? Collections.<String>emptyList() : paths);

    }


    /**
     * RELEASE_PENDING (rlubke) document
     * @param url
     * @return
     * @throws IOException
     */
    private List<String> getPathsFromJARUrl(URL url) throws IOException {
        List<String> paths = null;
        String urlString = url.toString();
        String pathElement = urlString.substring(urlString.indexOf("!/") + 2);
        URLConnection conn = url.openConnection();
        conn.setUseCaches(false);
        JarURLConnection jconn = (JarURLConnection) conn;
        JarFile jar = jconn.getJarFile();

        // First, check if pathElement represents a directory.  If it doesn't
        // we don't have to do anything further.
        String baseDir = pathElement + '/';
        JarEntry entry = jar.getJarEntry(baseDir);
        if (entry != null && !entry.isDirectory()) {
            return Collections.emptyList();
        }

        // Next, search for all JarEntries that start with pathElement
        // that aren't the pathElement itself and find all of it's direct
        // children (either directories or files) and store them in a list.
        for (Enumeration<JarEntry> e = jar.entries(); e.hasMoreElements();) {
            JarEntry je = e.nextElement();
            String name = je.getName();
            if (name.equals(pathElement) || name.equals(baseDir)) {
                // we're not interested in pathElement itself
                continue;
            }
            if (name.startsWith(pathElement)) {
                String path = name.substring(pathElement.length() + 1);
                if (path.charAt(path.length() - 1) == '/') {
                    path = path.substring(0, path.length() - 1);
                }
                if (path.contains("/")) {
                    // we're dealing with multiple directories - we're not
                    // interested in those
                    continue;
                }

                if (paths == null) {
                    paths = new ArrayList<String>(4);
                }
                paths.add(path);

            }
        }

        return paths;
    }

    /**
     * RELEASE_PENDING (rlubke) document
     * @param url
     * @return
     * @throws IOException
     * @throws URISyntaxException
     */
    private List<String> getPathsFromFileURL(URL url)
    throws IOException, URISyntaxException {

        List<String> paths = null;
        File baseDir = new File(url.toURI());
        if (baseDir.isDirectory()) {
            File[] subDirs = baseDir.listFiles();
            for (File sub : subDirs) {
                if (paths == null) {
                    paths = new ArrayList<String>(subDirs.length);
                }
                paths.add(sub.getName());
            }
        }

        return paths;

    }
}
