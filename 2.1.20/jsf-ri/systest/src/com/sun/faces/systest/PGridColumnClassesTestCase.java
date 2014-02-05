
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

package com.sun.faces.systest;

import com.sun.faces.systest.jsp.interweaving.*;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.sun.faces.htmlunit.HtmlUnitFacesTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;



public class PGridColumnClassesTestCase extends HtmlUnitFacesTestCase {


    public PGridColumnClassesTestCase(String name) {
        super(name);
    }

    public static Test suite() {
        return (new TestSuite(PGridColumnClassesTestCase.class));
    }

    public void test01() throws Exception {
        HtmlPage page = getPage("/faces/standard/pgridcolumnclasses.jsp");
        String xml = page.asXml();
        String xmlWithoutWhitespace = xml.replaceAll("\\s{1,100}", "");
        assertTrue(xmlWithoutWhitespace.contains("<html><head><title>pgridcolumnclasses.jsp</title><styletype=\"text/css\">.b1{background-color:red;}.b2{background-color:green;}.b3{background-color:blue;}.b4{background-color:burlywood;}.b5{background-color:darkolivegreen;}.b6{background-color:darkviolet;}.b7{background-color:skyblue;}</style></head><body><table><tbody><tr><tdclass=\"b1\">c1</td><tdclass=\"b2\">c2</td><td>c3</td><td>c4</td><td>c5</td><td>c6</td></tr><tr><tdclass=\"b1\">c1_1</td><tdclass=\"b2\">c2_1</td><td>c3_1</td><td>c4_1</td><td>c5_1</td><td>c6_1</td></tr></tbody></table><table><tbody><tr><tdclass=\"b1\">c1</td><tdclass=\"b2\">c2</td><tdclass=\"b3\">c3</td><tdclass=\"b4\">c4</td><td>c5</td><td>c6</td></tr><tr><tdclass=\"b1\">c1_1</td><tdclass=\"b2\">c2_1</td><tdclass=\"b3\">c3_1</td><tdclass=\"b4\">c4_1</td><td>c5_1</td><td>c6_1</td></tr></tbody></table><table><tbody><tr><tdclass=\"b1\">c1</td><tdclass=\"b2\">c2</td><tdclass=\"b3\">c3</td><td>c4</td><td>c5</td><td>c6</td></tr><tr><tdclass=\"b1\">c1_1</td><tdclass=\"b2\">c2_1</td><tdclass=\"b3\">c3_1</td><td>c4_1</td><td>c5_1</td><td>c6_1</td></tr></tbody></table><table><tbody><tr><tdclass=\"b1\">c1</td><td>c2</td><td>c3</td><td>c4</td><td>c5</td><td>c6</td></tr><tr><tdclass=\"b1\">c1_1</td><td>c2_1</td><td>c3_1</td><td>c4_1</td><td>c5_1</td><td>c6_1</td></tr></tbody></table><table><tbody><tr><td>c1</td><td>c2</td><td>c3</td><td>c4</td><td>c5</td><td>c6</td></tr><tr><td>c1_1</td><td>c2_1</td><td>c3_1</td><td>c4_1</td><td>c5_1</td><td>c6_1</td></tr></tbody></table><table><tbody><tr><tdclass=\"b1\">c1</td><tdclass=\"b2\">c2</td><tdclass=\"b3\">c3</td><tdclass=\"b4\">c4</td><tdclass=\"b5\">c5</td><tdclass=\"b6\">c6</td></tr><tr><tdclass=\"b1\">c1_1</td><tdclass=\"b2\">c2_1</td><tdclass=\"b3\">c3_1</td><tdclass=\"b4\">c4_1</td><tdclass=\"b5\">c5_1</td><tdclass=\"b6\">c6_1</td></tr></tbody></table><table><tbody><tr><td>c1</td><tdclass=\"b2\">c2</td><td>c3</td><td>c4</td><tdclass=\"b5\">c5</td><tdclass=\"b6\">c6</td></tr><tr><td>c1_1</td><tdclass=\"b2\">c2_1</td><td>c3_1</td><td>c4_1</td><tdclass=\"b5\">c5_1</td><tdclass=\"b6\">c6_1</td></tr></tbody></table></body></html>"));
    }
}
