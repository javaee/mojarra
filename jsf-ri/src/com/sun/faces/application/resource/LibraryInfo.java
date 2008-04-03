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

/**
 * <p>
 * <code>LibraryInfo</code> is a simple wrapper class for information pertainant to building
 * a complete resource path using a Library.
 * <p>
 */
public class LibraryInfo {

    private String name;
    private String version;
    private ResourceHelper helper;
    private String path;

    /**
     * Constructs a new <code>LibraryInfo</code> using the specified details.
     * @param name the name of the library
     * @param version the version of the library, if any
     * @param helper the helper class for this resource
     */
    LibraryInfo(String name, String version, ResourceHelper helper) {
        this.name = name;
        this.version = version;
        this.helper = helper;
        initPath();
    }

    /**
     * @return return the library name.
     */
    public String getName() {
        return name;
    }

    /**
     * @return return the version of the library, or <code>null</code>
     *  if the library isn't versioned.
     */
    public String getVersion() {
        return version;
    }

    /**
     * @return return the {@link ResourceHelper} for this resource
     */
    public ResourceHelper getHelper() {
        return helper;
    }

    /**
     * @return the base path of the library.
     */
    public String getPath() {
        return path;
    }


    // --------------------------------------------------------- Private Methods


    /**
     * Construct the full path to the base directory of the library's resources.
     */
    private void initPath() {

        StringBuilder sb = new StringBuilder(64);
        sb.append(helper.getBaseResourcePath());
        sb.append('/').append(name);
        if (version != null) {
            sb.append('/').append(version);
        }
        path = sb.toString();
        
    }

}
