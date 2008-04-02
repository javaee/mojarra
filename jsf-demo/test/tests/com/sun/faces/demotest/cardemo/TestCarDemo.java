/*
 * $Id: TestCarDemo.java,v 1.9 2004/05/12 18:47:36 ofung Exp $
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

package com.sun.faces.demotest.cardemo;

import com.gargoylesoftware.htmlunit.html.*;
import com.sun.faces.demotest.HtmlUnitTestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * <p>Assumptions: the app is localized for four locales, English,
 * German, French, Spanish.</p>
 */

public class TestCarDemo extends HtmlUnitTestCase {

    // Log instance for this class
    private static final Log log = LogFactory.getLog(TestCarDemo.class);


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
            if (log.isTraceEnabled()) {
                log.trace("Running test for language: " + button.asText());
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
                    if (log.isTraceEnabled()) {
                        log.trace("Found description " + description + ".");
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
            if (log.isTraceEnabled()) {
                log.trace("Button text of " + moreButton + " confirmed.");
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

        if (log.isTraceEnabled()) {
            log.trace("No package selected: base price: " + basePrice +
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
                    if (log.isTraceEnabled()) {
                        log.trace("Package: " + label + " currentPrice: " +
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
                        if (log.isTraceEnabled()) {
                            log.trace("Checkbox disabled: " + disabledValue);
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
    
