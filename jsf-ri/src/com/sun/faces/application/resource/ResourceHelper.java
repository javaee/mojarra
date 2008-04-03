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

import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Pattern;

import javax.faces.context.FacesContext;

import com.sun.faces.util.Util;

/**
 * RELEASE_PENDING (rlubke)
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
     * RELEASE_PENDING (rlubke) document
     * @return
     */
    public abstract String getBaseResourcePath();


    /**
     * RELEASE_PENDING (rlubke) document
     * @param ctx
     * @param path
     * @return
     */
    public abstract InputStream getInputStream(String path, FacesContext ctx);


    /**
     * RELEASE_PENDING (rlubke) document
     * @param ctx
     * @param path
     * @return
     */
    public abstract URL getURL(String path, FacesContext ctx);


    /**
     * RELEASE_PENDING (rlubke) document
     * @param libraryName
     * @param localePrefix
     * @param ctx
     * @return
     */
    public abstract LibraryInfo findLibrary(String libraryName,
                                            String localePrefix,
                                            FacesContext ctx);


    /**
     * RELEASE_PENDING (rlubke) document
     * @param library
     * @param resourceName
     * @param localePrefix
     * @param ctx
     * @return
     */
    public abstract ResourceInfo findResource(LibraryInfo library,
                                              String resourceName,
                                              String localePrefix,
                                              FacesContext ctx);


    // ------------------------------------------------------- Protected Methods


    /**
     * RELEASE_PENDING (rlubke) document
     * @param resourcePaths
     * @return
     */
    protected String getVersion(Collection<String> resourcePaths) {
        List<String> versionedPaths = new ArrayList<String>(resourcePaths.size());
        for (String p : resourcePaths) {
            String ver = getDirectoryVersion(p);
            if (ver != null) {
                versionedPaths.add(ver);
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
     * RELEASE_PENDING (rlubke) document
     *
     * @param pathElement
     *
     * @return
     */
    private String getDirectoryVersion(String pathElement) {

        String[] pathElements = Util.split(pathElement, "/");
        String path = pathElements[pathElements.length - 1];
        return (VERSION_PATTERN.matcher(path).matches() ? path : null);

    }

}
