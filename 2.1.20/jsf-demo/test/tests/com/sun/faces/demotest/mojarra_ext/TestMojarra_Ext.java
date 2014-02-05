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

package com.sun.faces.demotest.mojarra_ext;

import javax.faces.component.NamingContainer;

import java.util.List;

import com.gargoylesoftware.htmlunit.html.*;
import com.sun.faces.demotest.HtmlUnitTestCase;

public class TestMojarra_Ext extends HtmlUnitTestCase {


    /*
     * Check that we get the validation message for incorrect entry, with RegEx.
     * Then, check that a correct entry goes through.
     */
    public void testMojarra_Ext_Regex() throws Exception {

        String firstPageName = "/mojarra_ext/faces/welcome.jsp";
        String formName = "regex_form";
        String inputName = "sampleRegex";
        String incorrectValue = "jest";  // will fail, doesn't start with t
        String correctValue = "test";    // will pass, starts with t
        String welcomeTitle = "Test Regex Validator";
        String secondTitle = "Credit Card Demo";
        String submitButtonName = "submit";
        String validationMessageName = "regexError";

        HtmlPage greetingPage = (HtmlPage) getInitialPage();

        assertTrue(greetingPage.getTitleText().equals(welcomeTitle));

        List forms = greetingPage.getForms();
        assertTrue(forms != null);
        assertTrue(forms.size() == 1);

        HtmlForm form = (HtmlForm) forms.get(0);
        assertTrue(form != null);
        assertTrue(form.getIdAttribute().equals(formName));
        assertTrue(stripJsessionInfo(form.getActionAttribute()).equals(
                firstPageName));


        HtmlTextInput input = (HtmlTextInput) form.getInputByName(
                formName + NamingContainer.SEPARATOR_CHAR + inputName);
        assertTrue(input != null);


        // Set an incorrect value
        input.setValueAttribute(incorrectValue);

        // "click" the submit button to send the value
        HtmlSubmitInput submit = (HtmlSubmitInput) getInputContainingGivenId(greetingPage,
                                                                             formName + NamingContainer.SEPARATOR_CHAR + submitButtonName);
        assertNotNull(submit);
        HtmlPage resultPage = submit.click();

        assertTrue(resultPage.getTitleText().equals(welcomeTitle));


        // check for the validation message
        HtmlElement validationElement = resultPage.getHtmlElementById(
                formName + NamingContainer.SEPARATOR_CHAR + validationMessageName);

        assertTrue(validationElement != null);

        String[] validatorMessage = validationElement.asText().split("'");

        assertTrue(validatorMessage.length == 3);

        assertTrue(validatorMessage[1].equals("t.*"));

        // Set a correct value
        input = (HtmlTextInput) getInputContainingGivenId(resultPage, formName + NamingContainer.SEPARATOR_CHAR + inputName);
        input.setValueAttribute(correctValue);

        // "click" the submit button to send the value
        submit = (HtmlSubmitInput) getInputContainingGivenId(resultPage,
                                                             formName + NamingContainer.SEPARATOR_CHAR + submitButtonName);
        resultPage = submit.click();
        assertTrue(resultPage != null);

        assertTrue(resultPage.getTitleText().equals(secondTitle));

    }
    public void testMojarra_Ext_CreditCard() throws Exception {

        String initialPage = "/faces/landing.jsp";
        String firstPageName = "/mojarra_ext/faces/landing.jsp";
        String formName = "credit_card_form";
        String inputName = "sampleCreditCard";        
        String incorrectValue1 = "4111 1111 1111 1112";
        String incorrectError1 = "Not a valid credit card number.";
        String incorrectValue2 = "4111x1111x1111x1111";
        String incorrectError2 = "Invalid characters in value";
        String correctValue = "4111 1111 1111 1111";    
        String welcomeTitle = "Credit Card Demo";
        String secondTitle = "Test Regex Validator";
        String submitButtonName = "submit";
        String validationMessageName = "creditcardError";

        HtmlPage greetingPage = (HtmlPage) getPage(initialPage);

        assertTrue(greetingPage.getTitleText().equals(welcomeTitle));

        List forms = greetingPage.getForms();
        assertTrue(forms != null);
        assertTrue(forms.size() == 1);

        HtmlForm form = (HtmlForm) forms.get(0);
        assertTrue(form != null);
        assertTrue(form.getIdAttribute().equals(formName));
        assertTrue(stripJsessionInfo(form.getActionAttribute()).equals(
                firstPageName));


        HtmlTextInput input = (HtmlTextInput) form.getInputByName(
                formName + NamingContainer.SEPARATOR_CHAR + inputName);
        assertTrue(input != null);
        
        // Set first incorrect value
        input.setValueAttribute(incorrectValue1);

        // "click" the submit button to send the value
        HtmlSubmitInput submit = (HtmlSubmitInput) getInputContainingGivenId(greetingPage,
                                                                             formName + NamingContainer.SEPARATOR_CHAR + submitButtonName);
        HtmlPage resultPage = submit.click();
        assertTrue(resultPage != null);

        assertTrue(resultPage.getTitleText().equals(welcomeTitle));


        // check for the validation message
        HtmlElement validationElement = resultPage.getHtmlElementById(
                formName + NamingContainer.SEPARATOR_CHAR + validationMessageName);

        assertTrue(validationElement != null);

        assertTrue(validationElement.asText().contains(incorrectError1));

        input = (HtmlTextInput) getInputContainingGivenId(resultPage,  formName + NamingContainer.SEPARATOR_CHAR + inputName);
        // Set second incorrect value
        input.setValueAttribute(incorrectValue2);

        // "click" the submit button to send the value
        submit = (HtmlSubmitInput) getInputContainingGivenId(resultPage,
                                                             formName + NamingContainer.SEPARATOR_CHAR + submitButtonName);
        resultPage = submit.click();
        assertTrue(resultPage != null);

        assertTrue(resultPage.getTitleText().equals(welcomeTitle));

        // check for the validation message
        validationElement = resultPage.getHtmlElementById(
                formName + NamingContainer.SEPARATOR_CHAR + validationMessageName);

        assertTrue(validationElement != null);

        assertTrue(validationElement.asText().contains(incorrectError2));
        
        input = (HtmlTextInput) getInputContainingGivenId(resultPage,  formName + NamingContainer.SEPARATOR_CHAR + inputName);
        // Set a correct value
        input.setValueAttribute(correctValue);

        // "click" the submit button to send the value
        submit = (HtmlSubmitInput) getInputContainingGivenId(resultPage,
                                                             formName + NamingContainer.SEPARATOR_CHAR + submitButtonName);
        resultPage = submit.click();
        assertTrue(resultPage != null);

        assertTrue(resultPage.getTitleText().equals(secondTitle));

    }
    
} // end of class 
    
