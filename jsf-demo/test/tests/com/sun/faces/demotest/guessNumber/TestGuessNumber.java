/*
 * $Id: TestGuessNumber.java,v 1.12 2004/11/09 17:37:50 rlubke Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * 
 * - Redistribution in binary form must reproduce the above
 *   copyright notice, this list of conditions and the following
 *   disclaimer in the documentation and/or other materials
 *   provided with the distribution.
 *    
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *  
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 * DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR
 * RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THIS SOFTWARE OR
 * ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 * SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 * THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 * BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *  
 * You acknowledge that this software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
 */

package com.sun.faces.demotest.guessNumber;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.sun.faces.demotest.HtmlUnitTestCase;

import javax.faces.component.NamingContainer;

import java.util.Iterator;
import java.util.List;

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
            for (Iterator iter = greetingPage.getAllHtmlChildElements(); iter.hasNext();) {
                HtmlElement element = (HtmlElement) iter.next();
                if (element.getTagName().equalsIgnoreCase("img")) {
                    assertTrue(element.getAttributeValue("id").equals("helloForm" +
                                                                      NamingContainer.SEPARATOR_CHAR +
                                                                      "waveImg"));
                    assertTrue(stripJsessionInfo(
                        element.getAttributeValue("src"))
                               .equals("/jsf-guessNumber/wave.med.gif"));
                }
            }

            List forms = greetingPage.getAllForms();
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

            for (Iterator iter = resultPage.getAllHtmlChildElements(); iter.hasNext();) {
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
                    assertTrue(element.getAttributeValue("id").equals("responseForm" +
                                                                      NamingContainer.SEPARATOR_CHAR +
                                                                      "waveImg"));
                    assertTrue(stripJsessionInfo(
                        element.getAttributeValue("src"))
                               .equals("/jsf-guessNumber/wave.med.gif"));
                }
            }


            // "click" the back button and submit a new guess
            List forms1 = resultPage.getAllForms();
            assertTrue(forms1 != null);
            assertTrue(forms1.size() == 1);

            HtmlForm back = (HtmlForm) forms1.get(0);
            assertTrue(back != null);
            assertTrue(back.getIdAttribute().equals("responseForm"));
            assertTrue(stripJsessionInfo(back.getActionAttribute()).equals(
                "/jsf-guessNumber/guess/response.jsp"));

            greetingPage =
                (HtmlPage) back.submit(
                    "responseForm" + NamingContainer.SEPARATOR_CHAR + "back");
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
        HtmlForm guessForm = (HtmlForm) greetingPage.getAllForms().get(0);
        assertTrue(guessForm != null);

        HtmlPage resultPage = (HtmlPage) guessForm.submit(
            "helloForm" + NamingContainer.SEPARATOR_CHAR + "submit");
        assertTrue(resultPage != null);

        for (Iterator iter = resultPage.getAllHtmlChildElements(); iter.hasNext();) {
            HtmlElement element = (HtmlElement) iter.next();
            if (element.asText().trim().equals("Sorry, 0 is incorrect. Try a larger number.")) {
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
        HtmlForm guessForm = (HtmlForm) greetingPage.getAllForms().get(0);
        assertTrue(guessForm != null);

        HtmlTextInput input = (HtmlTextInput) guessForm.getInputByName(
            "helloForm" + NamingContainer.SEPARATOR_CHAR + "userNo");
        assertTrue(input != null);

        input.setValueAttribute(Integer.toString(-1234));

        HtmlPage failed = (HtmlPage) guessForm.submit(
            "helloForm" + NamingContainer.SEPARATOR_CHAR + "submit");
        assertTrue(failed != null);
        assertTrue(failed.getTitleText().equals("Hello"));
        for (Iterator iter = failed.getAllHtmlChildElements(); iter.hasNext();) {
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

        guessForm = (HtmlForm) failed.getAllForms().get(0);
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
        for (Iterator iter = failed.getAllHtmlChildElements(); iter.hasNext();) {
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

        HtmlPage page = (HtmlPage) getInitialPage();
        return page;
    }

} // end of class DemoTest01
    
