/*
 * $Id: TestGuessNumber.java,v 1.7 2003/12/17 15:20:15 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
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
        for (int i = 0; i < 11; i++) {
          
            assertTrue(greetingPage.getTitleText().equals("Hello"));
            for (Iterator iter = greetingPage.getAllHtmlChildElements(); iter.hasNext();) {
                HtmlElement element = (HtmlElement) iter.next();
                if (element.getTagName().equalsIgnoreCase("img")) {
                    assertTrue(element.getAttributeValue("id").equals("helloForm" + NamingContainer.SEPARATOR_CHAR + "waveImg"));                    
                    assertTrue(stripJsessionInfo(element.getAttributeValue("src")).equals("/jsf-guessNumber/wave.med.gif"));
                }
            }    
            
            List forms = greetingPage.getAllForms();
            assertTrue(forms != null);
            assertTrue(forms.size() == 1);

            HtmlForm form = (HtmlForm) forms.get(0);
            assertTrue(form != null);
            assertTrue(form.getIdAttribute().equals("helloForm"));
            assertTrue(stripJsessionInfo(form.getActionAttribute()).equals("/jsf-guessNumber/guess/greeting.jsp"));
            
            HtmlTextInput input = (HtmlTextInput) form.getInputByName("helloForm" + NamingContainer.SEPARATOR_CHAR + "userNo");
            assertTrue(input != null);
            
            input.setValueAttribute(Integer.toString(i));
            
            // "click" the submit button to send our guess
            HtmlPage resultPage = (HtmlPage) form.submit("helloForm" + NamingContainer.SEPARATOR_CHAR + "submit");
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
                    } else if (element.asText().trim().equals("Sorry, " + i + " is incorrect.")) {
                        System.out.println("Incorrect guess for '" + i + "', going back to guess again.");
                        break;
                    }
                } else if (element.getTagName().equalsIgnoreCase("img")) {
                    assertTrue(element.getAttributeValue("id").equals("responseForm" + NamingContainer.SEPARATOR_CHAR + "waveImg"));
                    assertTrue(stripJsessionInfo(element.getAttributeValue("src")).equals("/jsf-guessNumber/wave.med.gif"));
                }
            } 
            
            
                // "click" the back button and submit a new guess
            List forms1 = resultPage.getAllForms();
            assertTrue(forms1 != null);
            assertTrue(forms1.size() == 1);

            HtmlForm back = (HtmlForm) forms1.get(0);
            assertTrue(back != null);
            assertTrue(back.getIdAttribute().equals("responseForm"));
            assertTrue(stripJsessionInfo(back.getActionAttribute()).equals("/jsf-guessNumber/guess/response.jsp"));

            greetingPage = (HtmlPage) back.submit("responseForm" + NamingContainer.SEPARATOR_CHAR + "back");
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
        HtmlPage greetingPage = accessAppAndGetGreetingJSP();
        HtmlForm guessForm = (HtmlForm) greetingPage.getAllForms().get(0);
        assertTrue(guessForm != null);              
        
        HtmlPage resultPage = (HtmlPage) guessForm.submit("helloForm" + NamingContainer.SEPARATOR_CHAR + "submit");
        assertTrue(resultPage != null);
        
        for (Iterator iter = resultPage.getAllHtmlChildElements(); iter.hasNext(); ) {
            HtmlElement element = (HtmlElement) iter.next();
            if (element.getTagName().equalsIgnoreCase("h2")) {
                if (element.getTagName().equalsIgnoreCase("font")) {
                    assertTrue(element.getAttributeValue("color").equals("RED"));
                    assertTrue(element.asText().trim().equals(
                                    "Conversion Error setting value '' for 'UserNumberBean.userNumber'."));
                }                
            }
        }        
    }
    
    /* 
     * Confirm a validation error is returned when the input value is outside
     * the specified range of 0 and 10.
     */ 
    public void testGuessNumberInvalidInputRange() throws Exception {
        HtmlPage greetingPage = accessAppAndGetGreetingJSP();
        HtmlForm guessForm = (HtmlForm) greetingPage.getAllForms().get(0);
        assertTrue(guessForm != null);
        
        HtmlTextInput input = (HtmlTextInput) guessForm.getInputByName("helloForm" + NamingContainer.SEPARATOR_CHAR + "userNo");
        assertTrue(input != null);

        input.setValueAttribute(Integer.toString(-1));
        
        HtmlPage failed = (HtmlPage) guessForm.submit("helloForm" + NamingContainer.SEPARATOR_CHAR + "submit");
        assertTrue(failed != null);
        assertTrue(failed.getTitleText().equals("Hello"));
        for (Iterator iter = failed.getAllHtmlChildElements(); iter.hasNext();) {
            HtmlElement element = (HtmlElement) iter.next();
            if (element.getTagName().equalsIgnoreCase("font")) {
                assertTrue(element.getAttributeValue("color").equals("RED"));
                assertTrue(element.asText().trim().startsWith("Validation Error"));
            }
        }  
        
        guessForm = (HtmlForm) failed.getAllForms().get(0);
        assertTrue(guessForm != null);
        
        input = (HtmlTextInput) guessForm.getInputByName("helloForm" + NamingContainer.SEPARATOR_CHAR + "userNo");
        assertTrue(input != null);

        input.setValueAttribute(Integer.toString(11));
        
        failed = (HtmlPage) guessForm.submit("helloForm" + NamingContainer.SEPARATOR_CHAR + "submit");
        assertTrue(failed != null);
        assertTrue(failed.getTitleText().equals("Hello"));
        for (Iterator iter = failed.getAllHtmlChildElements(); iter.hasNext();) {
            HtmlElement element = (HtmlElement) iter.next();
            if (element.getTagName().equalsIgnoreCase("font")) {
                assertTrue(element.getAttributeValue("color").equals("RED"));
                assertTrue(element.asText().trim().startsWith("Validation Error:"));
            }
        }  
        
    }
    
    private HtmlPage accessAppAndGetGreetingJSP() throws Exception {
        
        HtmlPage page = (HtmlPage) getInitialPage();
        return page;
    }

} // end of class DemoTest01
    
