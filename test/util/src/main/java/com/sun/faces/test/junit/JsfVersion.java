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
package com.sun.faces.test.junit;

public enum JsfVersion {

    JSF_2_2_0_M01("2.2.0-m01"),
    JSF_2_2_0_M02("2.2.0-m02"),
    JSF_2_2_0_M03("2.2.0-m03"),
    JSF_2_2_0_M04("2.2.0-m04"),
    JSF_2_2_0_M05("2.2.0-m05"),
    JSF_2_2_0_M06("2.2.0-m06"),
    JSF_2_2_0_M07("2.2.0-m07"),
    JSF_2_2_0_M08("2.2.0-m08"),
    JSF_2_2_0_M09("2.2.0-m09"),
    JSF_2_2_0_M10("2.2.0-m10"),
    JSF_2_2_0_M11("2.2.0-m11"),
    JSF_2_2_0_M12("2.2.0-m12"),
    JSF_2_2_0_M13("2.2.0-m13"),
    JSF_2_2_0_M14("2.2.0-m14"),
    JSF_2_2_0_M15("2.2.0-m15"),
    JSF_2_2_0("2.2.0"),
    JSF_2_2_1("2.2.1"),
    JSF_2_2_2("2.2.2"),
    JSF_2_2_3("2.2.3"),
    JSF_2_2_4("2.2.4"),
    JSF_2_2_5("2.2.5"),
    JSF_2_2_6("2.2.6"),
    JSF_2_2_8("2.2.8"),
    JSF_2_2_9("2.2.9"),
    JSF_2_2_8_01("2.2.8-01"),
    JSF_2_2_8_02("2.2.8-02"),
    JSF_2_2_8_03("2.2.8-03");

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
        for (JsfVersion current : versions) {
            if (current.toString().equals(version)) {
                return current;
            }
        }
        throw new IllegalArgumentException("Unable to determine JSF version");
    }
    /**
     * Stores the version.
     */
    private final String version;
}
