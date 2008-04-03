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
import java.util.logging.Logger;
import java.util.logging.Level;

import javax.faces.context.FacesContext;

import com.sun.faces.util.FacesLogger;

/**
 * <p/>
 * <code>ResourceInfo</code> is a simple wrapper class for information
 * pertainant to building a complete resource path using a Library.
 * <p/>
 */
public class ResourceInfo {

    private static final Logger LOGGER = FacesLogger.RESOURCE.getLogger();
    private static final String COMPRESSED_CONTENT_DIRECTORY =
          "jsf-compressed";

    private String name;
    private VersionInfo version;
    private String localePrefix;
    private ResourceHelper helper;
    private LibraryInfo library;
    private String path;
    private String compressedPath;
    private boolean compressable;


    /**
     * Constructs a new <code>ResourceInfo</code> using the specified details.
     * The {@ResourceHelper} of the resource will be the same as the
     * {@link ResourceHelper} of the {@link LibraryInfo}.
     * @param library the library containing this resource
     * @param name the resource name
     * @param version the version of this resource (if any)
     * @param compressable if this resource should be compressed
     */
    public ResourceInfo(LibraryInfo library, String name, VersionInfo version, boolean compressable) {
        this.name = name;
        this.version = version;
        this.helper = library.getHelper();
        this.library = library;
        this.localePrefix = library.getLocalePrefix();
        this.compressable = compressable;
        initPath();
    }

    /**
     * Constructs a new <code>ResourceInfo</code> using the specified details.
     * @param name the resource name
     * @param version the version of the resource
     * @param localePrefix the locale prefix for this resource (if any)
     * @param helper helper the helper class for this resource
     * @param compressable if this resource should be compressed
     */
    ResourceInfo(String name,
                 VersionInfo version,
                 String localePrefix,
                 ResourceHelper helper,
                 boolean compressable) {
        this.name = name;
        this.version = version;
        this.localePrefix = localePrefix;
        this.helper = helper;
        this.compressable = compressable;
        initPath();
    }

    /**
     * @return return the library name.
     */
    public String getName() {
        return name;
    }

    /**
     * @return return the version of the resource, or <code>null</code> if the
     *         resource isn't versioned.
     */
    public VersionInfo getVersion() {
        return version;
    }

   /**
     * @return return the {@link ResourceHelper} for this resource
     */
    public ResourceHelper getHelper() {
        return helper;
    }

    /**
     * @return the Library associated with this resource, if any.
     */
    public LibraryInfo getLibraryInfo() {
        return library;
    }

    /**
     * @return the Locale prefix, if any.
     */
    public String getLocalePrefix() {
        return localePrefix;   
    }

    /**
     * @return the full path (including the library, if any) of the
     *  resource.
     */
    public String getPath() {
        return path;
    }

    /**
     * @return the path to which the compressed bits for this resource
     *  reside.  If this resource isn't compressable and this method is called,
     *  it will return <code>null</code>
     */
    public String getCompressedPath() {
        return compressedPath;
    }

    /**
     * @return <code>true</code> if this resource should be compressed,
     *  otherwise <code>false</code>
     */
    public boolean isCompressable() {
        return compressable;
    }


    // --------------------------------------------------------- Private Methods


    /**
     * Create the full path to the resource.  If the resource can be compressed,
     * setup the compressedPath ivar so that the path refers to the
     * directory refereneced by the context attribute <code>javax.servlet.context.tempdir</code>.  
     */
    private void initPath() {

        StringBuilder sb = new StringBuilder(32);
        if (library != null) {
            sb.append(library.getPath());
        } else {
            sb.append(helper.getBaseResourcePath());
        }
        if (library == null && localePrefix != null) {
            sb.append('/').append(localePrefix);
        }
        sb.append('/').append(name);
        if (version != null) {
            sb.append('/').append(version.getVersion());
            String extension = version.getExtension();
            if (extension != null) {
                sb.append('.').append(extension);    
            }
        }
        path = sb.toString();

        if (compressable) {
            FacesContext ctx = FacesContext.getCurrentInstance();
            File servletTmpDir = (File) ctx.getExternalContext()
                  .getApplicationMap().get("javax.servlet.context.tempdir");
            if (servletTmpDir == null || !servletTmpDir.isDirectory()) {
                if (LOGGER.isLoggable(Level.INFO)) {
                    // PENDING i18n
                    LOGGER.log(Level.INFO,
                               "File ({0}) referenced by javax.servlet.context.tempdir attribute is null, or was is not a directory.  Compression for {1} will be unavailable.",
                               new Object[]{((servletTmpDir == null)
                                             ? "null"
                                             : servletTmpDir.toString()),
                                            path});
                }
                compressable = false;
            } else {
                String tPath = ((path.charAt(0) == '/') ? path : '/' + path);
                File newDir = new File(servletTmpDir, COMPRESSED_CONTENT_DIRECTORY
                                                      + tPath);
                try {
                    newDir.mkdirs();
                    compressedPath = newDir.getCanonicalPath();
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE,
                               e.toString(),
                               e);
                    compressable = false;
                }
            }
        }
        
    }

}