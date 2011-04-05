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
import java.io.File;
import java.io.FileWriter;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.util.List;

public class GroovyTestCase extends HtmlUnitFacesTestCase {

    public GroovyTestCase(String name) {
        super(name);
    }

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
        return (new TestSuite(GroovyTestCase.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }


    // ------------------------------------------------------------ Test Methods

    public void testBasicAppFunctionality() throws Exception {

        HtmlPage page = getPage("/hello.jsf");
        HtmlTextInput inputText = (HtmlTextInput) page.getElementById("form:name");
        String val = "" + System.currentTimeMillis();
        inputText.setValueAttribute(val);

        List list = getAllElementsOfGivenClass(page, null,
                HtmlSubmitInput.class);
        HtmlSubmitInput button = (HtmlSubmitInput) list.get(0);
        page = (HtmlPage) button.click();
        String pageAsText = page.asText();
        assertTrue(pageAsText.contains("Hello " + val));
        assertTrue(pageAsText.contains("Happy Birthday"));

        String pageText = page.asXml();
        assertTrue(pageText.contains("<input type=\"hidden\" name=\"javax.faces.ViewState\" id="));
    }

    public void testBasicAppFunctionalityNegative() throws Exception {

        HtmlPage page = getPage("/hello.jsf");
        HtmlTextInput inputText = (HtmlTextInput) page.getElementById("form:name");
        String val = "" + System.currentTimeMillis();
        inputText.setValueAttribute(val);
        inputText = (HtmlTextInput) page.getElementById("form:age");
        inputText.setValueAttribute("-12");

        List list = getAllElementsOfGivenClass(page, null,
                HtmlSubmitInput.class);
        HtmlSubmitInput button = (HtmlSubmitInput) list.get(0);
        page = (HtmlPage) button.click();
        String pageAsText = page.asText();
        assertFalse(pageAsText.contains("Hello " + val));
        assertFalse(pageAsText.contains("Happy Birthday"));
        assertTrue(pageAsText.contains("please enter a valid age between 0 and 65"));

        String pageText = page.asXml();
        assertTrue(pageText.contains("<input type=\"hidden\" name=\"javax.faces.ViewState\" id="));
    }

    public void testRuntimeModifiedUIComponent() throws Exception {
        String val = "" + System.currentTimeMillis();
        HtmlPage page = getPage("/hello.jsf");
        String pageAsText = page.asText();
        // Make sure it does not contain the value
        assertFalse(pageAsText.contains(val));

        // modify the AgeComponent to have an encodeBegin() that outputs the val
        String script = "package hello import javax.faces.component.UIInput; import javax.faces.context.FacesContext; import javax.faces.context.ExternalContext; import javax.faces.context.ResponseWriter;  public class AgeComponent extends UIInput {   public AgeComponent() {     System.out.println(\"AgeComponent initialized...\");   }       public void encodeBegin(FacesContext context) throws IOException {       super.encodeBegin(context);       ExternalContext extContext = context.getExternalContext();       Map<String, String> requestParamMap = extContext.getRequestParameterMap();       ResponseWriter out = context.getResponseWriter();       out.startElement(\"p\", this);       out.writeText(\"[\" + " +
                val +
                "+ this.getClass().getName() + \"]\", this, \"prefix\");       out.endElement(\"p\");           } } ";
        overwriteGroovyClass("hello.AgeComponent", script);
        page = getPage("/hello.jsf");
        pageAsText = page.asText();
        // Make sure it does contain the value
        assertTrue(pageAsText.contains(val));
        
    }

    private void overwriteGroovyClass(String classNameWithoutFileExtension, String script) throws Exception {
        // '-DexplodedWarDir=/Users/edburns/Documents/JavaEE/workareas/mojarra-4HEAD/jsf-ri/systest-per-webapp/build/jsf-groovy'
        String explodedWarDir = System.getProperty("explodedWarDir");
        classNameWithoutFileExtension = classNameWithoutFileExtension.replace(".", File.separator);
        FileWriter fw = new FileWriter(explodedWarDir +
                File.separator +
                "WEB-INF" +
                File.separator +
                "groovy" +
                File.separator +
                classNameWithoutFileExtension +
                ".groovy");
        fw.write(script);
        fw.flush();
        fw.close();
        fw = new FileWriter(explodedWarDir + 
                File.separator + 
                "hello.xhtml", true);
        fw.append("  ");
        fw.flush();
        fw.close();

    }
}
