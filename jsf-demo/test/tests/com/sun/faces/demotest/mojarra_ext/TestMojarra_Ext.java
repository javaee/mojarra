/*
 * $Id: TestMojarra_Ext.java,v 1.2 2008/04/02 23:47:54 driscoll Exp $
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
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
package com.sun.faces.demotest.mojarra_ext;

import javax.faces.component.NamingContainer;

import java.util.List;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.sun.faces.demotest.HtmlUnitTestCase;

public class TestMojarra_Ext extends HtmlUnitTestCase {


    /*
     * Check that we get the validation message for incorrect entry, with RegEx.
     * Then, check that a correct entry goes through.
     */
    public void testMojarra_Ext() throws Exception {

        String formName = "ext_form";
        String inputName = "sampleRegex";
        String incorrectValue = "jest";  // will fail, doesn't start with t
        String correctValue = "test";    // will pass, starts with t
        String welcomeTitle = "Test Regex Validator";
        String secondTitle = "Regex Demo";
        String submitButtonName = "submit";
        String validationMessageName = "regexError";

        assertTrue(true);

        HtmlPage greetingPage = accessAppAndGetGreetingJSP();

        assertTrue(greetingPage.getTitleText().equals(welcomeTitle));

        List forms = greetingPage.getForms();
        assertTrue(forms != null);
        assertTrue(forms.size() == 1);

        HtmlForm form = (HtmlForm) forms.get(0);
        assertTrue(form != null);
        assertTrue(form.getIdAttribute().equals(formName));
        assertTrue(stripJsessionInfo(form.getActionAttribute()).equals(
                "/mojarra_ext/faces/welcome.jsp"));


        HtmlTextInput input = (HtmlTextInput) form.getInputByName(
                formName + NamingContainer.SEPARATOR_CHAR + inputName);
        assertTrue(input != null);


        // Set an incorrect value
        input.setValueAttribute(incorrectValue);

        // "click" the submit button to send the value
        HtmlPage resultPage = (HtmlPage) form.submit(
                formName + NamingContainer.SEPARATOR_CHAR + submitButtonName);
        assertTrue(resultPage != null);

        assertTrue(resultPage.getTitleText().equals(welcomeTitle));


        // check for the validation message
        HtmlElement validationElement = resultPage.getHtmlElementById(
                formName + NamingContainer.SEPARATOR_CHAR + validationMessageName);

        assertTrue(validationElement != null);

        String[] validatorMessage = validationElement.asText().split("'");

        assertTrue(validatorMessage.length == 3);

        assertTrue(validatorMessage[1].equals("t.*"));

        // Set a correct value
        input.setValueAttribute(correctValue);

        // "click" the submit button to send the value
        resultPage = (HtmlPage) form.submit(
                formName + NamingContainer.SEPARATOR_CHAR + submitButtonName);
        assertTrue(resultPage != null);

        assertTrue(resultPage.getTitleText().equals(secondTitle));


    }


    private HtmlPage accessAppAndGetGreetingJSP() throws Exception {
        HtmlPage page = (HtmlPage) getInitialPage();
        return page;
    }
    
} // end of class 
    
