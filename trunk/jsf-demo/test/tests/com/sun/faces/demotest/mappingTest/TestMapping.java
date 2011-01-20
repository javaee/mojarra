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

package com.sun.faces.demotest.mappingTest;

import javax.faces.component.NamingContainer;

import java.net.URL;
import java.util.Iterator;
import java.util.List;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.sun.faces.demotest.HtmlUnitTestCase;

public class TestMapping extends HtmlUnitTestCase {

    private boolean isPrefix = false;


    /*
     * Click back and forth between greeting.jsp and response.jsp providing
     * guesses that are within 0 - 10.  
     */
    public void testGuessNumber() throws Exception {

        HtmlPage greetingPage = accessAppAndGetGreetingJSP();

        int numberFound = 0;

        // loop through the range of valid guesses
        for (int i = 0; i < 11; i++) {

            assertTrue(greetingPage.getTitleText().equals("Hello"));
            for (HtmlElement element : greetingPage.getAllHtmlChildElements()) {
                if (element.getTagName().equalsIgnoreCase("img")) {
                    assertTrue(element.getAttributeValue("id").equals(
                          "helloForm" +
                          NamingContainer.SEPARATOR_CHAR +
                          "waveImg"));
                    assertTrue(stripJsessionInfo(
                          element.getAttributeValue("src"))
                          .equals(context + "/wave.med.gif"));
                }
            }

            List forms = greetingPage.getForms();
            assertTrue(forms != null);
            assertTrue(forms.size() == 1);

            HtmlForm form = (HtmlForm) forms.get(0);
            assertTrue(form != null);
            assertTrue(form.getIdAttribute().equals("helloForm"));
            if (isPrefix) {
                assertTrue(stripJsessionInfo(form.getActionAttribute()).equals(
                      context + "/guess/greeting.jsp"));
            } else {
                assertTrue(stripJsessionInfo(form.getActionAttribute()).equals(
                      context + "/greeting.faces"));
            }

            HtmlTextInput input = (HtmlTextInput) form.getInputByName(
                  "helloForm" + NamingContainer.SEPARATOR_CHAR + "userNo");
            assertTrue(input != null);

            input.setValueAttribute(Integer.toString(i));

            // "click" the submit button to send our guess
             HtmlSubmitInput submit = (HtmlSubmitInput) getInputContainingGivenId(greetingPage,
                                                                                 "helloForm" + NamingContainer.SEPARATOR_CHAR + "submit");
            HtmlPage resultPage = submit.click();
            assertTrue(resultPage != null);

            assertTrue(resultPage.getTitleText().equals("Guess The Number"));

            for (HtmlElement element : resultPage.getAllHtmlChildElements()) {

                // check to see if we guessed correctly or not
                if (element.getTagName().equalsIgnoreCase("h2")) {
                    if (element.asText().trim().equals("Yay! You got it!")) {
                        numberFound++;
                        System.out.println("Correct number was '" + i + "'");
                        break;
                    } else if (element.asText().trim().equals(
                          "Sorry, " + i + " is incorrect.")) {
                        System.out.println("Incorrect guess for '" + i +
                                           "', going back to guess again.");
                        break;
                    }
                } else if (element.getTagName().equalsIgnoreCase("img")) {
                    assertTrue(element.getAttributeValue("id").equals(
                          "responseForm" +
                          NamingContainer.SEPARATOR_CHAR +
                          "waveImg"));
                    assertTrue(stripJsessionInfo(
                          element.getAttributeValue("src"))
                          .equals(context + "/wave.med.gif"));
                }
            }

            // "click" the back button and submit a new guess
            List forms1 = resultPage.getForms();
            assertTrue(forms1 != null);
            assertTrue(forms1.size() == 1);

            HtmlForm back = (HtmlForm) forms1.get(0);
            assertTrue(back != null);
            assertTrue(back.getIdAttribute().equals("responseForm"));
            if (isPrefix) {
                assertTrue(stripJsessionInfo(back.getActionAttribute()).equals(
                      context + "/guess/response.jsp"));
            } else {
                assertTrue(stripJsessionInfo(back.getActionAttribute()).equals(
                      context + "/response.faces"));
            }

            submit = (HtmlSubmitInput) getInputContainingGivenId(resultPage,
                                                                 "responseForm" + NamingContainer.SEPARATOR_CHAR + "back");
            greetingPage = submit.click();
            assertTrue(greetingPage != null);
        }
        // ensure that there was only one correct guess through the progession 
        // of valid values
        assertTrue(numberFound == 1);
    }


    /*
     * Validate the proper response is returned when no input is provided
     * to the form for guessing duke's number
     */
    public void testGuessNumberNullInput() throws Exception {
        int numberFound = 0;
        HtmlPage greetingPage = accessAppAndGetGreetingJSP();
        HtmlForm guessForm = (HtmlForm) greetingPage.getForms().get(0);
        assertTrue(guessForm != null);

        HtmlSubmitInput submit = (HtmlSubmitInput) getInputContainingGivenId(greetingPage,
                                                                             "helloForm" + NamingContainer.SEPARATOR_CHAR + "submit");
        HtmlPage resultPage = submit.click();
        assertTrue(resultPage != null);
        for (HtmlElement element : resultPage.getAllHtmlChildElements()) {
            if (element.asText().trim().equals("Sorry, null is incorrect.")) {
                numberFound++;
                System.out.println("Incorrect guess 'null'.");
                break;
            }
        }
        assertTrue(numberFound == 1);
    }


    /*
     * Confirm a validation error is returned when the input value is outside
     * the specified range of 0 and 10.
     */
    public void testGuessNumberInvalidInputRange() throws Exception {
        boolean testFailed = false;
        HtmlPage greetingPage = accessAppAndGetGreetingJSP();
        HtmlForm guessForm = (HtmlForm) greetingPage.getForms().get(0);
        assertTrue(guessForm != null);

        HtmlTextInput input = (HtmlTextInput) guessForm.getInputByName(
              "helloForm" + NamingContainer.SEPARATOR_CHAR + "userNo");
        assertTrue(input != null);

        input.setValueAttribute(Integer.toString(-1));

         HtmlSubmitInput submit = (HtmlSubmitInput) getInputContainingGivenId(greetingPage,
                                                                                 "helloForm" + NamingContainer.SEPARATOR_CHAR + "submit");
        HtmlPage failed = submit.click();
        assertTrue(failed != null);
        assertTrue(failed.getTitleText().equals("Hello"));
        for (HtmlElement element : failed.getAllHtmlChildElements()) {
            if (element.getTagName().equalsIgnoreCase("span")) {
                testFailed = true;
                assertTrue(element.getAttributeValue("style").startsWith(
                      "color: red;"));
                assertTrue(
                      element.asText().trim().startsWith("Validation Error"));
            }
        }
        // make sure validation error occurred
        assertTrue(testFailed == true);
        testFailed = false;

        guessForm = (HtmlForm) failed.getForms().get(0);
        assertTrue(guessForm != null);

        input =
              (HtmlTextInput) guessForm.getInputByName(
                    "helloForm" + NamingContainer.SEPARATOR_CHAR + "userNo");
        assertTrue(input != null);

        input.setValueAttribute(Integer.toString(11));
        submit = (HtmlSubmitInput) getInputContainingGivenId(failed,
                                                                              "helloForm" + NamingContainer.SEPARATOR_CHAR + "submit");
        failed = submit.click();
        assertTrue(failed != null);
        assertTrue(failed.getTitleText().equals("Hello"));
        for (HtmlElement element : failed.getAllHtmlChildElements()) {
            if (element.getTagName().equalsIgnoreCase("span")) {
                testFailed = true;
                assertTrue(element.getAttributeValue("style").startsWith(
                      "color: red;"));
                assertTrue(
                      element.asText().trim().startsWith("Validation Error"));
            }
        }
        // make sure validation error occurred
        assertTrue(testFailed == true);

    }


    private HtmlPage accessAppAndGetGreetingJSP() throws Exception {
        HtmlPage page;

        WebClient client = new WebClient();
        client.setRedirectEnabled(true);
        if (context.indexOf("prefix") > 0) {
            isPrefix = true;
            // fetch page using prefix mapping
            page = (HtmlPage) client.getPage(new URL("http://" + host + ":" +
                                                     port +
                                                     context +
                                                     "/guess/greeting.jsp"));
        } else {
            // fetch page using extension mapping
            isPrefix = false;
            page = (HtmlPage) client.getPage(new URL("http://" + host + ":" +
                                                     port +
                                                     context +
                                                     "/greeting.faces"));
        }
        return page;
    }

} // end of class DemoTest01
    
