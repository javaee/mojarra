/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2014 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.faces.test.junit;

public enum JsfServerExclude {

    GLASSFISH_4_0_1("Glassfish", "4.0.1"),
    GLASSFISH_4_0("Glassfish", "4.0"),
    GLASSFISH_3_1_2_2("Glassfish", "3.1.2.2"),
    TOMCAT_7_0_35("Tomcat", "7.0.35"),
    WEBLOGIC_12_1_4("Weblogic", "12.1.4.0"),
    WEBLOGIC_12_1_3("Weblogic", "12.1.3.0"),
    WEBLOGIC_12_1_2("Weblogic", "12.1.2.0"),
    WEBLOGIC_12_1_1("Weblogic", "12.1.1.0");

    /**
     * Constructor.
     *
     * @param version the version.
     */
    private JsfServerExclude(String name, String version) {
        this.name = name;
        this.version = version;
    }

    /**
     * To string representation.
     *
     * @return the string representation.
     */
    @Override
    public String toString() {
        return name + ":" + version;
    }

    /**
     * From string.
     *
     * @param serverString the server string.
     * @return the JsfServerExclude
     */
    public static JsfServerExclude fromString(String serverString) {
        if (serverString != null) {
            JsfServerExclude[] excludes = JsfServerExclude.values();
            for (JsfServerExclude exclude : excludes) {
                if (serverString.contains(exclude.name) && serverString.contains(exclude.version)) {
                    return exclude;
                }
            }
        }
        return null;
    }

    /**
     * Stores the name.
     */
    private final String name;

    /**
     * Stores the version.
     */
    private final String version;
}
