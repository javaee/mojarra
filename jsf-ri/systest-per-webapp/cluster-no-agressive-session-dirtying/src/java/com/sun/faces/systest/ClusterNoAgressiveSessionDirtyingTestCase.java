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


import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.sun.faces.htmlunit.HtmlUnitFacesTestCase;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestSuite;


public class ClusterNoAgressiveSessionDirtyingTestCase extends HtmlUnitFacesTestCase {

    public ClusterNoAgressiveSessionDirtyingTestCase(String name) {
        super(name);
    }

    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {
        return (new TestSuite(ClusterNoAgressiveSessionDirtyingTestCase.class));
    }

    
    // ------------------------------------------------------------ Test Methods
    
    public void testSimpleObject() throws Exception {
        List<Integer> instNums = getInstanceNumbers();

        // Get the page from the first instance in the cluster
        // and store a simple string into the session.
        HtmlPage page = getPageFromInstanceN("/faces/session.xhtml", instNums.get(0));
        HtmlTextInput input = (HtmlTextInput) page.getElementById("input");
        String inputValue = "simple session value" + System.currentTimeMillis();
        input.setValueAttribute(inputValue);
        HtmlSubmitInput button = (HtmlSubmitInput) page.getElementById("button");
        button.click();

        // Get the same page from each of the subsequent intstances
        // in the cluster and assert that the session value is the same.
	for (int i = 0; i < instNums.size(); i++) {
	    assertSimpleObjectOutput(instNums.get(i), inputValue);
	}

    }

    public void testComplexObject() throws Exception {
        List<Integer> instNums = getInstanceNumbers();

        // Get the page from the first instance in the cluster
        // and store a simple string into the session.
        HtmlPage page = getPageFromInstanceN("/faces/sessionComplex.xhtml", instNums.get(0));
        HtmlTextInput input = (HtmlTextInput) page.getElementById("input");
        String inputValue = "complex session value";
        input.setValueAttribute(inputValue);
        HtmlSubmitInput button = (HtmlSubmitInput) page.getElementById("button");
        button.click();

        // Get the same page from each of the subsequent intstances
        // in the cluster and assert that the session value is the same.
	for (int i = 0; i < instNums.size(); i++) {
	    assertComplexObjectOutput(instNums.get(i), inputValue);
	}

    }


    private void assertSimpleObjectOutput(int instanceNumber, 
					  final String inputValue) throws Exception {
	HtmlPage page = getPageFromInstanceN("/faces/session.xhtml", 
					     instanceNumber);
	String text = page.asText();
	assertTrue(text.contains(inputValue));
	Thread.sleep(1000);
    }

    private void assertComplexObjectOutput(int instanceNumber, 
					  final String inputValue) throws Exception {
	HtmlPage page = getPageFromInstanceN("/faces/sessionComplex.xhtml", 
					     instanceNumber);
        HtmlTextInput input = (HtmlTextInput) page.getElementById("input");
	String value = input.getValueAttribute();
	assertTrue(value.contains(inputValue));
	assertTrue(inputValue.length() < value.length());

	Thread.sleep(1000);
    }
}

