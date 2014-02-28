/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2010 Sun Microsystems, Inc. All rights reserved.
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

package com.sun.faces.systest;


import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlScript;
import com.sun.faces.htmlunit.AbstractTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;


public class ResourceLocalePrefixTestCase extends AbstractTestCase {

    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public ResourceLocalePrefixTestCase(String name) {
        super(name);
    }

    // ------------------------------------------------------ Instance Variables

    // ---------------------------------------------------- Overall Test Methods


    /**
     * Set up instance variables required by this test case.
     */
    public void setUp() throws Exception {
        super.setUp();
    }


    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {
        return (new TestSuite(ResourceLocalePrefixTestCase.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }

    // ------------------------------------------------------ Instance Variables

    // ------------------------------------------------- Individual Test Methods


    public void testLocalePrefixes() throws Exception {

        String[] locales = {"en", "de", "fr", "ja"};
        WebClient client = new WebClient();
        HtmlPage page = null;
        for (String locale : locales) {
            System.out.println("Testing locale: " + locale);
            client.addRequestHeader("Accept-Language", locale);
            page = getPage("/faces/test.jsp", client);
            assertTrue(page != null);
            List<HtmlImage> images = new ArrayList<HtmlImage>(2);
            getAllElementsOfGivenClass(page, images, HtmlImage.class);
            assertTrue(images.size() == 2);
            HtmlImage img = images.get(0);
            assertTrue((
                  "/jsf-resource-locale-prefix/faces/javax.faces.resource/duke.gif?loc="
                  + (("ja".equals(locale)
                      ? "en"
                      : locale))).equals(img.getSrcAttribute()));
            img = images.get(1);
            assertTrue((
                  "/jsf-resource-locale-prefix/faces/javax.faces.resource/duke.gif?ln=lib&loc="
                  + (("ja".equals(locale)
                      ? "en"
                      : locale))).equals(img.getSrcAttribute()));
            List<HtmlScript> scripts = new ArrayList<HtmlScript>(1);
        getAllElementsOfGivenClass(page, scripts, HtmlScript.class);
        assertTrue(scripts.size() == 1);
        HtmlScript script = scripts.get(0);
        // The Ajax external script isn't localized - ensure it gets
        // rendered in all cases.
        // This should be true for all non-localized resources
        assertTrue("/jsf-resource-locale-prefix/faces/javax.faces.resource/jsf.js?ln=javax.faces".equals(script.getSrcAttribute()));
            System.out.println("Locale, " + locale + ", PASSED");
        }


    }

}