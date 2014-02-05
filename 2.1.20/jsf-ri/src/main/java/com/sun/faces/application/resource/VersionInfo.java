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

/**
 * Metadata pertaining to versions.
 */
public class VersionInfo implements Comparable {

    private String version;
    private String extension;

    // ------------------------------------------------------------ Constructors


    /**
     * Constructs a new VersionInfo instance.
     * @param version the version
     * @param extension the extension (only pertains to versioned resources,
     *  not libraries, and thus may be <code>null</code>)
     */
    public VersionInfo(String version, String extension) {
        this.version = version;
        this.extension = extension;
    }

    
    // ---------------------------------------------------------- Public Methods


    /**
     * @return the version
     */
    public String getVersion() {

        return version;

    }

    /**
     * @return the extension of the resource at processing time, or null
     *  if this version is associated with a library
     */
    public String getExtension() {

        return extension;

    }


    @Override
    public String toString() {

        return version;

    }


    @Override
    public int hashCode() {

        return (version.hashCode() ^ ((extension != null)
                                      ? extension.hashCode()
                                      : 0));

    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null || !(obj instanceof VersionInfo)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        VersionInfo passed = (VersionInfo) obj;
        boolean versionsEqual = this.version.equals(passed.version);
        boolean extensionEqual;
        if (this.extension == null) {
            extensionEqual = (passed.extension == null);
        } else {
            extensionEqual = this.extension.equals(passed.extension);
        }
        return (versionsEqual && extensionEqual);

    }


    // ------------------------------------------------- Methods from Comparable


    public int compareTo(Object o) {
        assert(o instanceof VersionInfo);
        VersionInfo c = (VersionInfo) o;
        return (this.version.compareTo(c.version));
    }
}
