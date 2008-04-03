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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.regex.Pattern;

import javax.faces.context.FacesContext;

import com.sun.faces.util.Util;

/**
 * <p>
 * Implementations of this class contain the knowledge for finding and serving
 * web application resources.
 * <p>
 *
 * @since 2.0
 */
public abstract class ResourceHelper {

    /**
     * This pattern represents a version for a library or resource.
     * Examples:
     *   1.1
     *   1.11
     *   1.11.1.
     *   1.11.1.2
     */
    private static final Pattern VERSION_PATTERN =
          Pattern.compile("^(\\d+)(\\.\\d+)+");


    // ---------------------------------------------------------- Public Methods


    /**
     * @return the base path in which resources will be stored
     */
    public abstract String getBaseResourcePath();


    /**
     * @param resource the resource to obtain an InputStream to
     * @param ctx the {@link javax.faces.context.FacesContext} for the current request
     * @return an <code>InputStream</code> to the resource, or
     *  <code>null</code> if no resource is found
     */
    public abstract InputStream getInputStream(ResourceInfo resource, FacesContext ctx);


    /**
     * @param resource the resource to obtain a URL reference to
     * @param ctx the {@link FacesContext} for the current request
     * @return a URL to the specified resource, otherwise <code>null</code>
     *  if no resource is found
     */
    public abstract URL getURL(ResourceInfo resource, FacesContext ctx);


    /**
     * Search for the specified library/localPrefix combination in an
     * implementation dependent manner.
     * @param libraryName the name of the library
     * @param localePrefix the logicial identifier for a locale specific library.
     *  if no localePrefix is configured, pass <code>null</code>
     * @param ctx the {@link FacesContext} for the current request
     * @return a {@link LibraryInfo} if a matching library based off the inputs
     *  can be found, otherwise returns <code>null</code>
     */
    public abstract LibraryInfo findLibrary(String libraryName,
                                            String localePrefix,
                                            FacesContext ctx);


    /**
     * Search for the specified resource based in the library/localePrefix/resourceName
     * combination in an implementation dependent manner.
     * @param library the library this resource should be a part of.  If the
     *  the resource that is being searched for isn't part of a library, then
     *  pass <code>null</code>
     * @param resourceName the name of the resource that is being searched for
     * @param localePrefix the logicial identifier for a locale specific library.
     *  if no localePrefix is configured, pass <code>null</code>
     * @param ctx the {@link FacesContext} for the current request
     * @return a {@link ResourceInfo} if a matching resource based off the inputs
     *  can be found, otherwise returns <code>null</code>
     */
    public abstract ResourceInfo findResource(LibraryInfo library,
                                              String resourceName,
                                              String localePrefix,
                                              FacesContext ctx);


    /**
     * <p>
     * The default implementation of this method will call through to
     * {@link ResourceHelper#getURL(ResourceInfo, javax.faces.context.FacesContext)}
     * and leverage the URL to obtain the date information of the resource and
     * return the value of <code>URLConnection.getLastModified()</code>
     * </p>
     * @param resource the resource in question
     * @param ctx the {@link FacesContext} for the current request
     * @return the date of the resource in milliseconds (since epoch),
     *  or <code>0</code> if the date cannot be determined
     */
    public long getLastModified(ResourceInfo resource, FacesContext ctx) {
        
        URL url = getURL(resource, ctx);
        long ret;
        try {
            URLConnection con = url.openConnection();
            ret = con.getLastModified();
        } catch (IOException ioe) {
            ret = 0;
        }

        return ((ret >= 0) ? ret : 0);

    }


    // ------------------------------------------------------- Protected Methods


    /**
     * <p>
     * Given a collection of path names:
     * </p>
     * <pre>
     *   1.1, scripts, images, 1.2
     * </pre>
     * <p>
     * this method will pick out the directories that represent a library or
     * resource version and return the latest version found, if any.
     * </p>
     *
     * @param resourcePaths a collection of paths (consisting of single path
     *  elements)
     * @return the latest version or if no version can be detected, otherwise
     *  this method returns <code>null</code>
     */
    protected String getVersion(Collection<String> resourcePaths) {
        List<String> versionedPaths = new ArrayList<String>(resourcePaths.size());
        for (String p : resourcePaths) {
            String vp = getVersion(p);
            if (vp != null) {
                versionedPaths.add(vp);
            }
        }
        String version = null;
        if (!versionedPaths.isEmpty()) {
            Collections.sort(versionedPaths);
            version = versionedPaths.get(versionedPaths.size() - 1);
        }
        return version;
    }


    /**
     * @param pathElement the path element to verify
     * @return <code>true</code> if this path element represents a version
     *  (i.e. matches {@link #VERSION_PATTERN}), otherwise
     *  returns <code>false</code>
     */
    private String getVersion(String pathElement) {

        String[] pathElements = Util.split(pathElement, "/");
        String path = pathElements[pathElements.length - 1];
        return ((VERSION_PATTERN.matcher(path).matches()) ? path : null);

    }

}
