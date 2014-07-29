/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2013 Oracle and/or its affiliates. All rights reserved.
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

public enum JsfVersion {

    JSF_2_1_2("2.1.2"),
    JSF_2_1_3("2.1.3"),
    JSF_2_1_4("2.1.4"),
    JSF_2_1_5("2.1.5"),
    JSF_2_1_6("2.1.6"),
    JSF_2_1_7("2.1.7"),
    JSF_2_1_8("2.1.8"),
    JSF_2_1_9("2.1.9"),
    JSF_2_1_10("2.1.10"),
    JSF_2_1_11("2.1.11"),
    JSF_2_1_12("2.1.12"),
    JSF_2_1_13("2.1.13"),
    JSF_2_1_14("2.1.14"),
    JSF_2_1_15("2.1.15"),
    JSF_2_1_16("2.1.16"),
    JSF_2_1_17("2.1.17"),
    JSF_2_1_18("2.1.18"),
    JSF_2_1_19("2.1.19"),
    JSF_2_1_20("2.1.20"),
    JSF_2_1_21("2.1.21"),
    JSF_2_1_22("2.1.22"),
    JSF_2_1_23("2.1.23"),
    JSF_2_1_24("2.1.24"),
    JSF_2_1_25("2.1.25"),
    JSF_2_1_26("2.1.26"),
    JSF_2_1_27("2.1.27"),
    JSF_2_1_28("2.1.28");

    /**
     * Constructor.
     *
     * @param version the version.
     */
    private JsfVersion(String version) {
        this.version = version;

    }

    /**
     * To string representation.
     *
     * @return the string representation.
     */
    @Override
    public String toString() {
        return version;
    }

    /**
     * From string.
     *
     * @param version the JSF version.
     * @return the JsfVersion
     */
    public static JsfVersion fromString(String version) {
        JsfVersion[] versions = JsfVersion.values();
        for (int i = 0; i < versions.length; i++) {
            if (versions[i].toString().equals(version)) {
                return versions[i];
            }
        }
        throw new IllegalArgumentException("Unable to determine JSF version");
    }
    /**
     * Stores the version.
     */
    private String version;
}
