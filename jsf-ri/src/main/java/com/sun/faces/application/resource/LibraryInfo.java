/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
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
 * <code>LibraryInfo</code> is a simple wrapper class for information pertinent to building
 * a complete resource path using a Library and/or Contract.
 * <p>
 */
public class LibraryInfo {

    private String name;
    private VersionInfo version;
    private String localePrefix;
    private String contract;
    private ResourceHelper helper;
    private String path;
    private String nonLocalizedPath;

    /**
     * Constructs a new <code>LibraryInfo</code> using the specified details.
     * @param name the name of the library
     * @param version the version of the library, if any
     * @param contract
     * @param helper the helper class for this resource
     */
    LibraryInfo(String name, VersionInfo version, String localePrefix, String contract, ResourceHelper helper) {
        this.name = name;
        this.version = version;
        this.localePrefix = localePrefix;
        this.contract = contract;
        this.helper = helper;
        initPath();
    }
    
    LibraryInfo(LibraryInfo other, boolean copyLocalePrefix) {
        this.name = other.name;
        this.version = other.version;
        if (copyLocalePrefix) {
            this.contract = other.contract;
            
            // http://java.net/jira/browse/JAVASERVERFACES_SPEC_PUBLIC-548 http://java.net/jira/browse/JAVASERVERFACES-2348
            this.localePrefix = other.localePrefix;
        }
        this.helper = other.helper;
        initPath();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        final LibraryInfo other = (LibraryInfo) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if (this.version != other.version && (this.version == null || !this.version.equals(other.version))) {
            return false;
        }
        if ((this.localePrefix == null) ? (other.localePrefix != null) : !this.localePrefix.equals(other.localePrefix)) {
            return false;
        }
        if ((this.contract == null) ? (other.contract != null) : !this.contract.equals(other.contract)) {
            return false;
        }
        if ((this.path == null) ? (other.path != null) : !this.path.equals(other.path)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 37 * hash + (this.version != null ? this.version.hashCode() : 0);
        hash = 37 * hash + (this.localePrefix != null ? this.localePrefix.hashCode() : 0);
        hash = 37 * hash + (this.contract != null ? this.contract.hashCode() : 0);
        hash = 37 * hash + (this.path != null ? this.path.hashCode() : 0);
        return hash;
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
     * @return the base path of the library.
     */
    public String getPath() {
        return path;
    }
    
    public String getPath(String localePrefix) {
        String result = null;
        if (null == localePrefix) {
            result = nonLocalizedPath;
        } else {
            result = path;
        }
        return result;
    }
    
    /**
     * @return the Locale prefix, if any.
     */
    public String getLocalePrefix() {
        return localePrefix;
    }
    
    /**
     * @return active contract or null
     */
    public String getContract() {
		return contract;
	}

    @Override
    public String toString() {
        return "LibraryInfo{" +
               "name='" + (name != null ? name : "NONE") + '\'' +
               ", version=" + ((version != null) ? version : "NONE") + '\'' +
               ", localePrefix='" + ((localePrefix != null) ? localePrefix : "NONE") + '\'' +
               ", contract='" + ((contract != null) ? contract : "NONE") + '\'' +
               ", path='" + path + '\'' +
               '}';
    }

    // --------------------------------------------------------- Private Methods


    /**
     * Construct the full path to the base directory of the library's resources.
     */
    private void initPath() {

        StringBuilder builder = new StringBuilder(64),
                      noLocaleBuilder = new StringBuilder(64);

        appendBasePath(builder);
        appendBasePath(noLocaleBuilder);

        if (localePrefix != null) {
            builder.append('/').append(localePrefix);
        }
        if (name != null) {
	        builder.append('/').append(name);
	        noLocaleBuilder.append('/').append(name);
        }
        if (version != null) {
            builder.append('/').append(version.getVersion());
            noLocaleBuilder.append('/').append(version.getVersion());
        }
        path = builder.toString();
        nonLocalizedPath = noLocaleBuilder.toString();
    }

    private void appendBasePath(StringBuilder builder) {
        if (contract == null) {
            builder.append(helper.getBaseResourcePath());
        } else {
            builder.append(helper.getBaseContractsPath()).append('/').append(contract);
        }
    }

}
