/*
 * $Id: TestMapping.java,v 1.9 2006/03/07 08:27:25 srinivas635 Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

package com.sun.faces.demotest.mappingTest;

import javax.faces.component.NamingContainer;

import java.net.URL;
import java.util.Iterator;
import java.util.List;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
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
            for (Iterator iter = greetingPage.getAllHtmlChildElements();
                 iter.hasNext();) {
                HtmlElement element = (HtmlElement) iter.next();
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
            HtmlPage resultPage = (HtmlPage) form.submit(
                  "helloForm" + NamingContainer.SEPARATOR_CHAR + "submit");
            assertTrue(resultPage != null);

            assertTrue(resultPage.getTitleText().equals("Guess The Number"));

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

        HtmlPage failed = (HtmlPage) guessForm.submit(
              "helloForm" + NamingContainer.SEPARATOR_CHAR + "submit");
        assertTrue(failed != null);
        assertTrue(failed.getTitleText().equals("Hello"));
        for (Iterator iter = failed.getAllHtmlChildElements(); iter.hasNext();)
        {
            HtmlElement element = (HtmlElement) iter.next();
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

        failed =
              (HtmlPage) guessForm.submit(
                    "helloForm" + NamingContainer.SEPARATOR_CHAR + "submit");
        assertTrue(failed != null);
        assertTrue(failed.getTitleText().equals("Hello"));
        for (Iterator iter = failed.getAllHtmlChildElements(); iter.hasNext();)
        {
            HtmlElement element = (HtmlElement) iter.next();
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
    
