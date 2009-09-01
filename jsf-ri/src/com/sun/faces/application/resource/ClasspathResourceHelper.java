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
import java.io.FileOutputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;

import com.sun.faces.util.Util;
import com.sun.faces.util.FacesLogger;
import com.sun.faces.config.WebConfiguration;
import static com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter.EnableClasspathVersioning;

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

    private ResourceHelper delegate;


    // ------------------------------------------------------------ Constructors


    private ClasspathResourceHelper() {

        FacesContext ctx = FacesContext.getCurrentInstance();
        WebConfiguration webConfig =
              WebConfiguration.getInstance(ctx.getExternalContext());
        if (webConfig.isOptionEnabled(EnableClasspathVersioning)) {
            delegate = new VersionedClasspathResourceDelegate();
        } else {
            delegate = new NonVersionedClasspathResourceDelegate();
        }

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

        return delegate.getBaseResourcePath();

    }


    /**
     * @see ResourceHelper#getNonCompressedInputStream(ResourceInfo, javax.faces.context.FacesContext)
     */
    protected InputStream getNonCompressedInputStream(ResourceInfo resource, FacesContext ctx)
    throws IOException {

        return delegate.getNonCompressedInputStream(resource, ctx);

    }


    /**
     * @see com.sun.faces.application.resource.ResourceHelper#getURL(ResourceInfo, javax.faces.context.FacesContext)
     */
    public URL getURL(ResourceInfo resource, FacesContext ctx) {

        return delegate.getURL(resource, ctx);

    }


    /**
     * @see ResourceHelper#findLibrary(String, String, javax.faces.context.FacesContext)
     */
    public LibraryInfo findLibrary(String libraryName,
                                   String localePrefix,
                                   FacesContext ctx) {

        return delegate.findLibrary(libraryName, localePrefix, ctx);

    }


    /**
     * @see ResourceHelper#findResource(LibraryInfo, String, String, boolean, javax.faces.context.FacesContext)
     */
    public ResourceInfo findResource(LibraryInfo library,
                                     String resourceName,
                                     String localePrefix,
                                     boolean compressable,
                                     FacesContext ctx) {

        return delegate.findResource(library,
                                     resourceName,
                                     localePrefix,
                                     compressable,
                                     ctx);

    }


    // ---------------------------------------------------------- Nested Classes


    private final class NonVersionedClasspathResourceDelegate extends ResourceHelper {

        private static final String BASE_RESOURCE_PATH = "META-INF/resources";


        // -------------------------------------------------------- Constructors


        NonVersionedClasspathResourceDelegate() {}


        // ----------------------------------------- Methods from ResourceHelper

        /**
         * @see com.sun.faces.application.resource.ResourceHelper#getBaseResourcePath()
         */
        public String getBaseResourcePath() {

            return BASE_RESOURCE_PATH;

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

            try {
                return new LibraryInfo(libraryName,
                                       null,
                                       localePrefix,
                                       ClasspathResourceHelper.this);
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

            ResourceInfo value;
            try {
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
            } catch (Exception e) {
                throw new FacesException(e);
            }
            if (value.isCompressable()) {
                value = handleCompression(value);
            }
            return value;

        }

    } // END VersionedClasspathResourceDelegate


    private final class VersionedClasspathResourceDelegate extends ResourceHelper {


        private final String basePath;
        private final String[] MOJARRA_JS_FILES = {
              "META-INF/resources/javax.faces/jsf-compressed.js",
              "META-INF/resources/javax.faces/jsf-uncompressed.js"
        };

        // -------------------------------------------------------- Constructors


        VersionedClasspathResourceDelegate() {

            FacesContext ctx = FacesContext.getCurrentInstance();
            File basePathFile = explodeResources(ctx, getWebappLibJarFileURLS(ctx));
            try {
                basePath = basePathFile.getCanonicalPath();
            } catch (IOException ioe) {
                throw new FacesException(ioe); // should handle this better
            }

        }


        // ----------------------------------------- Methods from ResourceHelper


        @Override
        public String getBaseResourcePath() {
            return basePath;
        }


        @Override
        public URL getURL(ResourceInfo resource, FacesContext ctx) {

            File f = new File(resource.getPath());
            try {
                return f.toURI().toURL();
            } catch (MalformedURLException mue) {
                throw new FacesException(mue);
            }

        }

        @Override
        protected InputStream getNonCompressedInputStream(ResourceInfo info, FacesContext ctx)
              throws IOException {
            
            return getURL(info, ctx).openStream();

        }

        /**
         * @see ResourceHelper#findLibrary(String, String, javax.faces.context.FacesContext)
         */
        public LibraryInfo findLibrary(String libraryName,
                                       String localePrefix,
                                       FacesContext ctx) {

            String basePath;
            if (localePrefix == null) {
                basePath = getBaseResourcePath()
                             + File.separatorChar
                             + libraryName;
            } else {
                basePath = getBaseResourcePath()
                             + File.separatorChar
                             + localePrefix
                             + File.separatorChar
                             + libraryName;
            }

            File basePathDir = new File(basePath);
            if (!basePathDir.exists()) {
                return null;
            }

            try {
                List<String> subPaths = getSubPaths(basePathDir);
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

            String basePath;
            String resName = resourceName.replace('/', File.separatorChar);
            if (library != null) {
                basePath = library.getPath() + File.separatorChar + resName;
            } else {
                if (localePrefix == null) {
                    basePath = getBaseResourcePath()
                                 + File.separatorChar
                                 + resName;
                } else {
                    basePath = getBaseResourcePath() +
                               + File.separatorChar
                               + localePrefix
                               + File.separatorChar
                               + resName;
                }
            }

            File basePathDir = new File(basePath);
            if (!basePathDir.exists()) {
                return null;
            }

            ResourceInfo value;
            try {
                List<String> subPaths = getSubPaths(basePathDir);
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


        // ----------------------------------------------------- Private Methods


        private File explodeResources(FacesContext ctx, Set<JarFile> jarFiles) {

            File tmpDir = (File) ctx.getExternalContext().getApplicationMap()
                  .get("javax.servlet.context.tempdir");

            if (tmpDir == null) {
                throw new FacesException("Unable to support versioned classpath resources as the servlet container temp directory as identified by 'javax.servlet.context.tempdir' is null.");
            }

            File javaxResourcesDir = new File(tmpDir, "javax.faces.resources");
            deleteDir(javaxResourcesDir);
            mkdirs(javaxResourcesDir);

            // allocate 16kb for copying resources
            byte[] buf = createBuffer(16);

            copyWebappResources(jarFiles, javaxResourcesDir, buf);

            copyWebInfClassesMetaInfResources(ctx.getExternalContext(),
                                              javaxResourcesDir,
                                              "/WEB-INF/classes/META-INF/resources/",
                                              buf);

            copyMojarraResources(javaxResourcesDir, buf);

            return javaxResourcesDir;

        }

        
        private void copyWebappResources(Set<JarFile> jarFiles, File javaxResourcesDir, byte[] buf) {

            if (jarFiles != null && !jarFiles.isEmpty()) {
                for (JarFile jar : jarFiles) {
                    for (Enumeration<JarEntry> entries = jar.entries(); entries .hasMoreElements();) {
                        JarEntry e = entries.nextElement();
                        if (e.getName().startsWith("META-INF/resources")) {
                            String entryName = e.getName().substring(18);
                            if (entryName.length() == 1) {
                                continue;
                            }
                            File f = new File(javaxResourcesDir, entryName);
                            if (e.isDirectory()) {
                                mkdirs(f);
                            } else {
                                try {
                                    writeBytes(f, jar.getInputStream(e), buf);
                                } catch (IOException ioe) {
                                    throw new FacesException(ioe);
                                }
                            }
                        }
                    }
                }
            }
        }


        // special handling for Mojarra ajax javascripts
        private void copyMojarraResources(File javaxResourcesDir, byte[] buf) {

            File f = new File(javaxResourcesDir, "javax.faces");
            mkdirs(f);
            for (int i = 0, len = MOJARRA_JS_FILES.length; i < len; i++) {
                String jsPath = MOJARRA_JS_FILES[i];
                String fname = jsPath.substring(jsPath.lastIndexOf('/') + 1);
                File ff = new File(f, fname);
                InputStream in = this.getClass().getClassLoader().getResourceAsStream(jsPath);
                writeBytes(ff, in, buf);
            }

        }


        private void copyWebInfClassesMetaInfResources(ExternalContext extContext,
                                                       File base,
                                                       String basePath,
                                                       byte[] buf) {
            Set<String> subPaths = extContext.getResourcePaths(basePath);
            if (subPaths != null && !subPaths.isEmpty()) {
                for (String path : subPaths) {

                    if (path.charAt(path.length() - 1) == '/') {
                        String name = path.substring(basePath.length(), path.length() - 1);
                        File f = new File(base, name);
                        mkdirs(f);
                        copyWebInfClassesMetaInfResources(extContext,
                                                          f,
                                                          path,
                                                          buf);
                    } else {
                        String name = path.substring(path.lastIndexOf('/') + 1);
                        File f = new File(base, name);
                        writeBytes(f, extContext.getResourceAsStream(path), buf);
                    }
                }
            }

        }


        private void mkdirs(File f) {

            if (!f.mkdirs()) {
                throw new FacesException("Unable to create "
                                         + f.toString()
                                         + " temp directory.");
            }

        }


        private void writeBytes(File f, InputStream in, byte[] buf) {

            FileOutputStream fout = null;
            try {
                fout = new FileOutputStream(f);
                for (int read = in.read(buf); read != -1; read = in.read(buf)) {
                    fout.write(buf, 0, read);
                }
                fout.flush();
            } catch (Exception e) {
                throw new FacesException(e);
            } finally {
                if (fout != null) {
                    try {
                        fout.close();
                    } catch (IOException ioe) {
                        // ignored
                    }
                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException ioe) {
                        // ignored
                    }
                }
            }

        }


        private byte[] createBuffer(int kb) {

            return new byte[1024 * kb];

        }


        private boolean deleteDir(File dir) {
            if (dir.exists() && dir.isDirectory()) {
                String[] children = dir.list();
                for (int i = 0, len = children.length; i < len; i++) {
                    boolean success = deleteDir(new File(dir, children[i]));
                    if (!success) {
                        return false;
                    }
                }
            }

            return dir.delete();
        }


        private Set<JarFile> getWebappLibJarFileURLS(FacesContext ctx) {

            ExternalContext extContext = ctx.getExternalContext();
            Set<String> elements = extContext.getResourcePaths("/WEB-INF/lib");
            Set<JarFile> appJars = null;
            if (elements != null && !elements.isEmpty()) {
                for (String element : elements) {
                    if (element.charAt(element.length() - 1) == '/'
                        || !element.endsWith(".jar")) {
                        continue;
                    }
                    try {
                        URL url = extContext.getResource(element);
                        StringBuilder sb = new StringBuilder(32);
                        sb.append("jar:").append(url.toString()).append("!/");
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


        private List<String> getSubPaths(File basePath) throws IOException {

            List<String> paths = null;
            if (basePath.isDirectory()) {
                File[] subDirs = basePath.listFiles();
                for (File sub : subDirs) {
                    if (paths == null) {
                        paths = new ArrayList<String>(subDirs.length);
                    }
                    paths.add(sub.getName());
                }
            }

            return ((paths == null) ? Collections.<String>emptyList() : paths);

        }

    } // END VersionedClasspathResourceHelperDelegate

}
