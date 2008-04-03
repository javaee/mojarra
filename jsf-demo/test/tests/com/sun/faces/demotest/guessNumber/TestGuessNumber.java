/*
 * $Id: TestGuessNumber.java,v 1.17 2008/04/02 23:08:16 driscoll Exp $
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

package com.sun.faces.demotest.guessNumber;

import javax.faces.component.NamingContainer;

import java.util.Iterator;
import java.util.List;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.sun.faces.demotest.HtmlUnitTestCase;

public class TestGuessNumber extends HtmlUnitTestCase {


    /*
     * Click back and forth between greeting.jsp and response.jsp providing
     * guesses that are within 0 - 10.  
     */
    public void testGuessNumber() throws Exception {

        HtmlPage greetingPage = accessAppAndGetGreetingJSP();

        int numberFound = 0;

        // loop through the range of valid guesses
        for (int i = 1; i < 11; i++) {

            assertTrue(greetingPage.getTitleText().equals("Hello"));
            boolean foundImage = false;
            for (Iterator iter = greetingPage.getAllHtmlChildElements();
                 iter.hasNext();) {
                HtmlElement element = (HtmlElement) iter.next();
                if (element.getTagName().equalsIgnoreCase("img")) {
                    if (element.getAttributeValue("id").equals(
                          "helloForm" +
                          NamingContainer.SEPARATOR_CHAR +
                          "waveImg")) {
                        foundImage = true;
                        assertTrue(stripJsessionInfo(
                            element.getAttributeValue("src"))
                            .equals("/jsf-guessNumber/guess/javax.faces.resource/images/wave.med.gif"));
                    }
                }
            }
            assertTrue(foundImage);

            List forms = greetingPage.getForms();
            assertTrue(forms != null);
            assertTrue(forms.size() == 1);

            HtmlForm form = (HtmlForm) forms.get(0);
            assertTrue(form != null);
            assertTrue(form.getIdAttribute().equals("helloForm"));
            assertTrue(stripJsessionInfo(form.getActionAttribute()).equals(
                  "/jsf-guessNumber/guess/greeting.jsp"));

            HtmlTextInput input = (HtmlTextInput) form.getInputByName(
                  "helloForm" + NamingContainer.SEPARATOR_CHAR + "userNo");
            assertTrue(input != null);

            input.setValueAttribute(Integer.toString(i));

            // "click" the submit button to send our guess
            HtmlPage resultPage = (HtmlPage) form.submit(
                  "helloForm" + NamingContainer.SEPARATOR_CHAR + "submit");
            assertTrue(resultPage != null);

            assertTrue(resultPage.getTitleText().equals("Guess The Number"));

            foundImage = false;
            for (Iterator iter = resultPage.getAllHtmlChildElements();
                 iter.hasNext();) {
                HtmlElement element = (HtmlElement) iter.next();

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
                    if (element.getAttributeValue("id").equals(
                        "responseForm" +
                        NamingContainer.SEPARATOR_CHAR +
                        "waveImg")) {
                        foundImage = true;
                        assertTrue(stripJsessionInfo(
                            element.getAttributeValue("src"))
                            .equals("/jsf-guessNumber/guess/javax.faces.resource/images/wave.med.gif"));
                    }
                }
            }
            assertTrue(foundImage);

            // "click" the back button and submit a new guess
            List forms1 = resultPage.getForms();
            assertTrue(forms1 != null);
            assertTrue(forms1.size() == 1);

            HtmlForm back = (HtmlForm) forms1.get(0);
            assertTrue(back != null);
            assertTrue(back.getIdAttribute().equals("responseForm"));
            assertTrue(stripJsessionInfo(back.getActionAttribute()).equals(
                  "/jsf-guessNumber/guess/response.jsp"));

            greetingPage =
                  (HtmlPage) back.submit(
                        "responseForm"
                        + NamingContainer
                              .SEPARATOR_CHAR
                        + "back");
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

        HtmlPage resultPage = (HtmlPage) guessForm.submit(
              "helloForm" + NamingContainer.SEPARATOR_CHAR + "submit");
        assertTrue(resultPage != null);

        for (Iterator iter = resultPage.getAllHtmlChildElements();
             iter.hasNext();) {
            HtmlElement element = (HtmlElement) iter.next();
            if (element.asText().trim()
                  .equals("Sorry, null is incorrect. Try a larger number.")) {
                numberFound++;
                System.out.println("Incorrect guess 'null'.");
                break;
            }
        }
        // mkae sure error was encountered.
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

        input.setValueAttribute(Integer.toString(-1234));

        HtmlPage failed = (HtmlPage) guessForm.submit(
              "helloForm" + NamingContainer.SEPARATOR_CHAR + "submit");
        assertTrue(failed != null);
        assertTrue(failed.getTitleText().equals("Hello"));
        for (Iterator iter = failed.getAllHtmlChildElements(); iter.hasNext();)
        {
            HtmlElement element = (HtmlElement) iter.next();
            if (element.getTagName().equalsIgnoreCase("span")) {
                if (element.getAttributeValue("id").equals("helloForm" +
                    NamingContainer.SEPARATOR_CHAR + "errors1")) {
                    testFailed = true;
                    assertTrue(element.getAttributeValue("style").startsWith(
                      "color: red;"));
                    assertTrue(
                      element.asText().trim().contains("Validation Error"));
                }
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

        failed =
              (HtmlPage) guessForm.submit(
                    "helloForm" + NamingContainer.SEPARATOR_CHAR + "submit");
        assertTrue(failed != null);
        assertTrue(failed.getTitleText().equals("Hello"));
        for (Iterator iter = failed.getAllHtmlChildElements(); iter.hasNext();)
        {
            HtmlElement element = (HtmlElement) iter.next();
            if (element.getTagName().equalsIgnoreCase("span")) {
                if (element.getAttributeValue("id").equals("helloForm" +
                    NamingContainer.SEPARATOR_CHAR + "errors1")) {
                    testFailed = true;
                    assertTrue(element.getAttributeValue("style").startsWith(
                      "color: red;"));
                    assertTrue(
                      element.asText().trim().contains("Validation Error"));
                }
            }
        }
        // make sure validation error occurred
        assertTrue(testFailed == true);

    }


    private HtmlPage accessAppAndGetGreetingJSP() throws Exception {
        HtmlPage page = (HtmlPage) getInitialPage();
        return page;
    }

} // end of class DemoTest01
    
