/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2010 Oracle and/or its affiliates. All rights reserved.
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


import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.sun.faces.htmlunit.HtmlUnitFacesTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;




/**
 * <p>Make sure that an application that replaces the ApplicationFactory
 * but uses the decorator pattern to allow the existing ApplicationImpl
 * to do the bulk of the requests works.</p>
 */

public class ReplaceVariableResolverAndAddELResolverProgrammaticallyTestCase extends HtmlUnitFacesTestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public ReplaceVariableResolverAndAddELResolverProgrammaticallyTestCase(String name) {
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
        return (new TestSuite(ReplaceVariableResolverAndAddELResolverProgrammaticallyTestCase.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }


    // ------------------------------------------------------ Instance Variables



    // ------------------------------------------------- Individual Test Methods

    /**
     *
     * <p>Verify that the bean is successfully resolved</p>
     */

    public void testReplaceVariableResolverAndAddELResolverProgrammatically() throws Exception {
	HtmlPage page = getPage("/faces/test.jsp");
	assertTrue(-1 != page.asText().indexOf("Invoking the variable resolver chain: success."));
	assertTrue(-1 != page.asText().indexOf("Invoking the variable resolver directly: success."));
	assertTrue(-1 != page.asText().indexOf("Invoking the EL resolver directly: true."));
	assertTrue(-1 != page.asText().indexOf("result: isReadOnly invoked directly."));
	assertTrue(-1 != page.asText().indexOf("Invoking the EL resolver via chain: true."));
	assertTrue(-1 != page.asText().indexOf("result: isReadOnly invoked thru chain."));
        HtmlSubmitInput button = (HtmlSubmitInput) getInputContainingGivenId(page, "reload");
        page = (HtmlPage) button.click();
        String text = page.asXml();
        text = text.replaceAll(":[0-9]*\\)", "\\)");
        text = text.replaceAll("com.sun.faces.", "");
        text = text.replaceAll("toString() invocation", "");

        String [] orderedListOfStringsToFindInPage = {
            "FacesELResolverForFaces",
            "el.ImplicitObjectELResolver.getValue(ImplicitObjectELResolver.java)",
            "el.VariableResolverChainWrapper.getValue(VariableResolverChainWrapper.java)",
            "systest.NewVariableResolver.resolveVariable(NewVariableResolver.java)",
            "systest.NewELResolver.getValue(NewELResolver.java)",
            "el.ManagedBeanELResolver.resolveBean(ManagedBeanELResolver.java)",
            "el.FacesResourceBundleELResolver.getValue(FacesResourceBundleELResolver.java)",
            "el.ScopedAttributeELResolver.getValue(ScopedAttributeELResolver.java)",
            "FacesELResolverForJsp",
            "systest.NewVariableResolver.resolveVariable(NewVariableResolver.java)",
            "systest.NewELResolver.getValue(NewELResolver.java)"
        };
        boolean [] foundFlags = new boolean[orderedListOfStringsToFindInPage.length];
        int i,j;
        for (i = 0; i < foundFlags.length; i++) {
            foundFlags[i] = false;
        }
        String [] textSplitOnSpace = text.split(" ");
        j = 0;
        for (i = 0; i < textSplitOnSpace.length &&
                    j < orderedListOfStringsToFindInPage.length; i++) {
            if (textSplitOnSpace[i].contains(orderedListOfStringsToFindInPage[j])) {
                foundFlags[j++] = true;
            }
        }
        for (i = 0; i < foundFlags.length; i++) {
            if (!foundFlags[i]) {
                fail("Unable to find " + orderedListOfStringsToFindInPage[i] +
                     " at expected order in ELResolver chain");
            }
        }
    }

}
