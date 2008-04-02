/*
 * $Id: TestCarDemo.java,v 1.12 2006/03/07 17:21:03 rlubke Exp $
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

package com.sun.faces.demotest.cardemo;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import java.util.logging.Level;

import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTableDataCell;
import com.sun.faces.demotest.HtmlUnitTestCase;

/**
 * <p>Assumptions: the app is localized for four locales, English,
 * German, French, Spanish.</p>
 */

public class TestCarDemo extends HtmlUnitTestCase {
    
    private static final Logger LOGGER = Logger.getLogger("com.sun.faces.demotest.cardemo");    

    public TestCarDemo() {
    }


    protected ResourceBundle resources = null;
    protected String[] carBundleNames = {
          "carstore.bundles.Jalopy",
          "carstore.bundles.Luxury",
          "carstore.bundles.Roadster",
          "carstore.bundles.SUV"
    };
    protected String[] packageLabelKeys = {
          "Custom",
          "Standard",
          "Performance",
          "Deluxe"
    };

    protected ResourceBundle[] carBundles = null;

    // PENDING: find a way to cause the WebClient's Accept_Charset
    // header to be set so we can test the locale calculation algorithm.

    /**
     * <p>Load the main page.  Assumptions: there are exactly four
     * buttons, in a certain order, to select each locale.  For each
     * button, press it, and call doStoreFront() on the result.</p>
     */

    public void testCarDemo() throws Exception {

        // for each of the language links run the test
        HtmlPage page = (HtmlPage) getInitialPage();
        List buttons = getAllElementsOfGivenClass(page, null,
                                                  HtmlSubmitInput.class);
        HtmlSubmitInput button = null;
        int i, j = 0;
        Locale[] locales = {
              Locale.ENGLISH,
              Locale.GERMAN,
              Locale.FRENCH,
              new Locale("es", "")
        };

        for (i = 0; i < locales.length; i++) {
            resources = ResourceBundle.getBundle("carstore.bundles.Resources",
                                                 locales[i]);
            carBundles =
                  new ResourceBundle[carBundleNames.length];
            for (j = 0; j < carBundleNames.length; j++) {
                carBundles[j] =
                      ResourceBundle.getBundle(carBundleNames[j], locales[i]);
            }

            button = (HtmlSubmitInput) buttons.get(i);
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Running test for language: " + button.asText());
            }
            doStoreFront((HtmlPage) button.click());
        }
    }


    /**
     * <p>Assumptions: there are exactly four buttons on this page, one
     * for each car model.</p>
     * <p/>
     * <p>Verify that all of the expected cars have their descriptions
     * on the page.</p>
     * <p/>
     * <p>Verify that the text of the "more" button is properly
     * localized.</p>
     * <p/>
     * <p>Press the button for each model and execute doCarDetail() on
     * the result.</p>
     */

    public void doStoreFront(HtmlPage storeFront) throws Exception {
        HtmlSubmitInput button = null;
        HtmlTableDataCell cell = null;
        String
              description = null,
              moreButton = null;
        Iterator iter = null;
        boolean found = false;
        int i;

        assertNotNull(storeFront);

        List
              cells = getAllElementsOfGivenClass(storeFront, null,
                                                 HtmlTableDataCell.class),
              buttons = getAllElementsOfGivenClass(storeFront, null,
                                                   HtmlSubmitInput.class);

        // verify the expected descriptions are present

        for (i = 0; i < carBundles.length; i++) {
            iter = cells.iterator();
            description = carBundles[i].getString("description").trim();
            while (iter.hasNext()) {
                cell = (HtmlTableDataCell) iter.next();
                if (-1 != cell.asText().indexOf(description)) {
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.fine("Found description " + description + ".");
                    }
                    found = true;
                    break;
                }
            }
        }
        assertTrue("Did not find description: " + description, found);

        iter = buttons.iterator();
        moreButton = resources.getString("moreButton").trim();
        while (iter.hasNext()) {
            button = (HtmlSubmitInput) iter.next();
            assertTrue(-1 !=
                       button.asText().indexOf(moreButton));
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Button text of " + moreButton + " confirmed.");
            }
            doCarDetail((HtmlPage) button.click());
        }

    }


    /**
     * <p>Assumptions: Each of the package buttons causes an increase in
     * base price over the previous button, in order.</p>
     */

    public void doCarDetail(HtmlPage carDetail) throws Exception {
        assertNotNull(carDetail);
        int
              previousPrice = 0,
              basePrice = getNumberNearLabel("basePriceLabel", carDetail),
              currentPrice = getNumberNearLabel("yourPriceLabel", carDetail);
        List buttons = getAllElementsOfGivenClass(carDetail, null,
                                                  HtmlSubmitInput.class);
        HtmlSubmitInput button = null;
        int i = 0;
        String label = null;
        Iterator iter = null;

        assertEquals(basePrice, currentPrice);

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("No package selected: base price: " + basePrice +
                      " current price: " + currentPrice);
        }

        // press each of the package buttons and see that the price
        // increases for each one.
        for (i = 0; i < packageLabelKeys.length; i++) {
            previousPrice = currentPrice;
            iter = buttons.iterator();
            label = resources.getString(packageLabelKeys[i]).trim();
            while (iter.hasNext()) {
                button = (HtmlSubmitInput) iter.next();
                // if this is the button we're looking for
                if (-1 != (button.asText().indexOf(label))) {
                    // press it
                    carDetail = (HtmlPage) button.click();
                    // resample yourPrice
                    currentPrice = getNumberNearLabel("yourPriceLabel",
                                                      carDetail);
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.fine("Package: " + label + " currentPrice: " +
                                  currentPrice);
                    }

                    assertTrue(previousPrice < currentPrice);
                    break;
                }
            }
        }

        // press the "performance" button and see that some of the
        // checkboxes are disabled.
        HtmlCheckBoxInput checkbox = null;
        List checkboxes = null;
        Iterator checkboxIter = null;
        boolean foundDisabled = false;
        String disabledValue = null;
        iter = buttons.iterator();
        while (iter.hasNext()) {
            button = (HtmlSubmitInput) iter.next();
            // 2 is the index for the "performance" label
            label = resources.getString(packageLabelKeys[2]).trim();
            // if this is the button we're looking for
            if (-1 != (button.asText().indexOf(label))) {
                // press it
                carDetail = (HtmlPage) button.click();
                checkboxes =
                      getAllElementsOfGivenClass(carDetail, null,
                                                 HtmlCheckBoxInput.class);
                // verify that at least one of the checkboxes are
                // disabled.
                checkboxIter = checkboxes.iterator();
                while (checkboxIter.hasNext()) {
                    checkbox = (HtmlCheckBoxInput) checkboxIter.next();
                    if (null != (disabledValue =
                          checkbox.getDisabledAttribute())) {
                        if (LOGGER.isLoggable(Level.FINE)) {
                            LOGGER.fine("Checkbox disabled: " + disabledValue);
                        }
                        foundDisabled = true;
                    }
                    break;
                }
            }
            if (foundDisabled) {
                break;
            }
        }
        assertTrue(foundDisabled);
    }


    protected int getNumberNearLabel(String label, HtmlPage page) {
        List cells;
        Iterator iter = null;
        HtmlTableDataCell cell = null;
        String yourPrice = null;
        int result = Integer.MIN_VALUE;

        cells = getAllElementsOfGivenClass(page, null,
                                           HtmlTableDataCell.class);
        iter = cells.iterator();
        yourPrice = resources.getString(label).trim();
        // look in the current or next cell for the price data.
        while (iter.hasNext()) {
            cell = (HtmlTableDataCell) iter.next();
            if (-1 != cell.asText().indexOf(yourPrice)) {
                if (Integer.MIN_VALUE !=
                    (result = extractNumberFromText(cell.asText().trim()))) {
                    return result;
                }
                // try the next cell
                cell = (HtmlTableDataCell) iter.next();
                if (Integer.MIN_VALUE !=
                    (result = extractNumberFromText(cell.asText().trim()))) {
                    return result;
                }
            }
        }
        return Integer.MIN_VALUE;
    }


    protected int extractNumberFromText(String content) {
        char[] chars = null;
        chars = content.toCharArray();
        String number = null;
        int i, j;
        for (i = 0; i < chars.length; i++) {
            if (Character.isDigit(chars[i])) {
                for (j = i; j < chars.length; j++) {
                    if (Character.isWhitespace(chars[j])) {
                        break;
                    }
                }
                number = content.substring(i, j);
                return Integer.valueOf(number).intValue();
            }
        }
        return Integer.MIN_VALUE;
    }


} // end of class DemoTest01
    
