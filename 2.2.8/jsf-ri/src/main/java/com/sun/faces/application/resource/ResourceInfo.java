/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright (c) 1997-2012 Oracle and/or its affiliates. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
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

public class ResourceInfo {
    
    ResourceHelper helper;
    LibraryInfo library;
    ContractInfo contract;
    String libraryName;
    String localePrefix;
    String name;
    String path;
    VersionInfo version;
    boolean doNotCache = false;

    public ResourceInfo(LibraryInfo library, 
            ContractInfo contract,
            String name, 
            VersionInfo version) {
        this.contract = contract;
        this.library = library;
        this.helper = library.getHelper();
        this.localePrefix = library.getLocalePrefix();
        this.name = name;
        this.version = version;
        this.libraryName = library.getName();
        
    }
    
    public ResourceInfo(ContractInfo contract, String name, VersionInfo version, ResourceHelper helper) {
        this.contract = contract;
        this.name = name;
        this.version = version;
        this.helper = helper;
    }
    
    public ResourceInfo(ResourceInfo other, boolean copyLocalePrefix) {
        this.helper = other.helper;
        this.library = new LibraryInfo(other.library, copyLocalePrefix);
        this.libraryName = library.getName();
        if (copyLocalePrefix) {
            this.localePrefix = other.localePrefix;
        }
        this.name = other.name;
        this.path = other.path;
        this.version = other.version;
    }
    
    public void copy(ResourceInfo other) {
        this.helper = other.helper;
        this.library = other.library;
        this.libraryName = other.libraryName;
        this.localePrefix = other.localePrefix;
        this.name = other.name;
        this.path = other.path;
        this.version = other.version;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ResourceInfo other = (ResourceInfo) obj;
        if (this.helper != other.helper && (this.helper == null || !this.helper.equals(other.helper))) {
            return false;
        }
        if (this.library != other.library && (this.library == null || !this.library.equals(other.library))) {
            return false;
        }
        if ((this.libraryName == null) ? (other.libraryName != null) : !this.libraryName.equals(other.libraryName)) {
            return false;
        }
        if ((this.localePrefix == null) ? (other.localePrefix != null) : !this.localePrefix.equals(other.localePrefix)) {
            return false;
        }
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if ((this.path == null) ? (other.path != null) : !this.path.equals(other.path)) {
            return false;
        }
        if (this.version != other.version && (this.version == null || !this.version.equals(other.version))) {
            return false;
        }
        if (this.doNotCache != other.doNotCache) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (this.helper != null ? this.helper.hashCode() : 0);
        hash = 17 * hash + (this.library != null ? this.library.hashCode() : 0);
        hash = 17 * hash + (this.libraryName != null ? this.libraryName.hashCode() : 0);
        hash = 17 * hash + (this.localePrefix != null ? this.localePrefix.hashCode() : 0);
        hash = 17 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 17 * hash + (this.path != null ? this.path.hashCode() : 0);
        hash = 17 * hash + (this.version != null ? this.version.hashCode() : 0);
        hash = 17 * hash + (this.doNotCache ? 1 : 0);
        return hash;
    }

    public boolean isDoNotCache() {
        return doNotCache;
    }

    public void setDoNotCache(boolean doNotCache) {
        this.doNotCache = doNotCache;
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
     * @return return the library name.
     */
    public String getName() {
        return name;
    }

    /**
     * @return the full path (including the library, if any) of the
     *  resource.
     */
    public String getPath() {
        return path;
    }
    
    public String getContract() {
        return (null != contract) ? contract.toString() : null;
    }

    /**
     * @return return the version of the resource, or <code>null</code> if the
     *         resource isn't versioned.
     */
    public VersionInfo getVersion() {
        return version;
    }
    
}
