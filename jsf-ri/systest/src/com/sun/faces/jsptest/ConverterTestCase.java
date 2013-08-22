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

package com.sun.faces.jsptest;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.sun.faces.htmlunit.HtmlUnitFacesTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.util.List;

/**
 * <p>Test that invalid values don't cause valueChangeEvents to occur.</p>
 */

public class ConverterTestCase extends HtmlUnitFacesTestCase {

    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public ConverterTestCase(String name) {
        super(name);
    }

    // ------------------------------------------------------ Instance Variables

    // ---------------------------------------------------- Overall Test Methods


    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {
        return (new TestSuite(ConverterTestCase.class));
    }

    // ------------------------------------------------- Individual Test Methods
    public void testConverter() throws Exception {
        HtmlPage page = getPage("/faces/converter03.jsp");
        List list;
        list = getAllElementsOfGivenClass(page, null,
                HtmlTextInput.class);

        // set the initial value to be 1 for all input fields
        ((HtmlTextInput) list.get(0)).setValueAttribute("1111111111");
        ((HtmlTextInput) list.get(1)).setValueAttribute("1111111111");
        ((HtmlTextInput) list.get(2)).setValueAttribute("1111111111");
        ((HtmlTextInput) list.get(3)).setValueAttribute("1111111111");
        ((HtmlTextInput) list.get(4)).setValueAttribute("99");
        ((HtmlTextInput) list.get(5)).setValueAttribute("4");
        ((HtmlTextInput) list.get(6)).setValueAttribute("12");
        ((HtmlTextInput) list.get(7)).setValueAttribute("7");
        ((HtmlTextInput) list.get(8)).setValueAttribute("10");

        list = getAllElementsOfGivenClass(page, null,
                HtmlSubmitInput.class);
        HtmlSubmitInput button = (HtmlSubmitInput) list.get(0);
        page = (HtmlPage) button.click();
        assertTrue(-1 != page.asText().indexOf("text1 was converted to Object"));
        assertTrue(-1 != page.asText().indexOf("text1 was converted to String"));
        assertTrue(-1 != page.asText().indexOf("text2 was converted to Object"));
        assertTrue(-1 != page.asText().indexOf("text2 was converted to String"));
        assertTrue(-1 != page.asText().indexOf("text3 was converted to Object"));
        assertTrue(-1 != page.asText().indexOf("text3 was converted to String"));
        assertTrue(-1 != page.asText().indexOf("text4 was converted to Object"));
        assertTrue(-1 != page.asText().indexOf("text4 was converted to String"));
        assertTrue(-1 != page.asText().indexOf("text5: '99' could not be understood as a time. Example:"));
        assertTrue(-1 != page.asText().indexOf("4.0"));
        assertTrue(-1 != page.asText().indexOf("12.0"));
        assertTrue(-1 != page.asText().indexOf("7.0"));
        assertTrue(-1 != page.asText().indexOf("10"));
    }

    public void testConverterMessages() throws Exception {
        HtmlPage page = getPage("/faces/converter04.jsp");
        List list;
        list = getAllElementsOfGivenClass(page, null,
                HtmlTextInput.class);

        // set the initial value to be "aaa" for all input fields
        for (int i = 0; i < list.size(); i++) {
            ((HtmlTextInput) list.get(i)).setValueAttribute("aaa");
        }

        list = getAllElementsOfGivenClass(page, null,
                HtmlSubmitInput.class);
        HtmlSubmitInput button = (HtmlSubmitInput) list.get(0);
        page = (HtmlPage) button.click();

        assertTrue(-1 != page.asText().indexOf("form:bd1: 'aaa' must be a signed decimal number."));
        assertTrue(-1 != page.asText().indexOf("form:bd1: 'aaa' must be a signed decimal number consisting of zero or more digits, that may be followed by a decimal point and fraction. Example: 198.23"));
        assertTrue(-1 != page.asText().indexOf("BigDecimal2: 'aaa' must be a signed decimal number."));
        assertTrue(-1 != page.asText().indexOf("BigDecimal2: 'aaa' must be a signed decimal number consisting of zero or more digits, that may be followed by a decimal point and fraction. Example: 198.23"));
        assertTrue(-1 != page.asText().indexOf("form:bi1: 'aaa' must be a number consisting of one or more digits."));
        assertTrue(-1 != page.asText().indexOf("form:bi1: 'aaa' must be a number consisting of one or more digits. Example: 9876"));
        assertTrue(-1 != page.asText().indexOf("BigInteger2: 'aaa' must be a number consisting of one or more digits."));
        assertTrue(-1 != page.asText().indexOf("BigInteger2: 'aaa' must be a number consisting of one or more digits. Example: 9876"));
        assertTrue(-1 != page.asText().indexOf("form:byte1: 'aaa' must be a number between -128 and 127. "));
        assertTrue(-1 != page.asText().indexOf("form:byte1: 'aaa' must be a number between -128 and 127. Example: 254"));
        assertTrue(-1 != page.asText().indexOf("Byte2: 'aaa' must be a number between -128 and 127. "));
        assertTrue(-1 != page.asText().indexOf("Byte2: 'aaa' must be a number between -128 and 127. Example: 254"));
        assertTrue(-1 != page.asText().indexOf("form:date1: 'aaa' could not be understood as a date."));
        assertTrue(-1 != page.asText().indexOf("form:date1: 'aaa' could not be understood as a date. Example:"));
        assertTrue(-1 != page.asText().indexOf("Date2: 'aaa' could not be understood as a date."));
        assertTrue(-1 != page.asText().indexOf("Date2: 'aaa' could not be understood as a date. Example:"));
        assertTrue(-1 != page.asText().indexOf("form:time1: 'aaa' could not be understood as a time."));
        assertTrue(-1 != page.asText().indexOf("form:time1: 'aaa' could not be understood as a time. Example:"));
        assertTrue(-1 != page.asText().indexOf("Time2: 'aaa' could not be understood as a time."));
        assertTrue(-1 != page.asText().indexOf("Time2: 'aaa' could not be understood as a time. Example:"));
        assertTrue(-1 != page.asText().indexOf("form:datetime1: 'aaa' could not be understood as a date and time."));
        assertTrue(-1 != page.asText().indexOf("form:datetime1: 'aaa' could not be understood as a date and time. Example:"));
        assertTrue(-1 != page.asText().indexOf("DateTime2: 'aaa' could not be understood as a date and time."));
        assertTrue(-1 != page.asText().indexOf("DateTime2: 'aaa' could not be understood as a date and time. Example:"));
        assertTrue(-1 != page.asText().indexOf("form:double1: 'aaa' must be a number consisting of one or more digits."));
        assertTrue(-1 != page.asText().indexOf("form:double1: 'aaa' must be a number between 4.9E-324 and 1.7976931348623157E308 Example: 1999999"));
        assertTrue(-1 != page.asText().indexOf("Double2: 'aaa' must be a number consisting of one or more digits."));
        assertTrue(-1 != page.asText().indexOf("Double2: 'aaa' must be a number between 4.9E-324 and 1.7976931348623157E308 Example: 1999999"));
        assertTrue(-1 != page.asText().indexOf("form:float1: 'aaa' must be a number between 1.4E-45 and 3.4028235E38 Example: 2000000000"));
        assertTrue(-1 != page.asText().indexOf("Float2: 'aaa' must be a number consisting of one or more digits."));
        assertTrue(-1 != page.asText().indexOf("Float2: 'aaa' must be a number between 1.4E-45 and 3.4028235E38 Example: 2000000000"));
        assertTrue(-1 != page.asText().indexOf("form:integer1: 'aaa' must be a number between -2147483648 and 2147483647 Example: 9346"));
        assertTrue(-1 != page.asText().indexOf("Integer2: 'aaa' must be a number consisting of one or more digits. "));
        assertTrue(-1 != page.asText().indexOf("Integer2: 'aaa' must be a number between -2147483648 and 2147483647 Example: 9346"));
        assertTrue(-1 != page.asText().indexOf("form:long1: 'aaa' must be a number between -9223372036854775808 to 9223372036854775807 Example: 98765432"));
        assertTrue(-1 != page.asText().indexOf("Long2: 'aaa' must be a number consisting of one or more digits. "));
        assertTrue(-1 != page.asText().indexOf("Long2: 'aaa' must be a number between -9223372036854775808 to 9223372036854775807 Example: 98765432"));
        assertTrue(-1 != page.asText().indexOf("form:number1: 'aaa' could not be understood as a currency value."));
        assertTrue(-1 != page.asText().indexOf("form:number1: 'aaa' could not be understood as a currency value. Example: $99.99"));
        assertTrue(-1 != page.asText().indexOf("Number2: 'aaa' could not be understood as a currency value."));
        assertTrue(-1 != page.asText().indexOf("Number2: 'aaa' could not be understood as a currency value. Example: $99.99"));
        assertTrue(-1 != page.asText().indexOf("form:number3: 'aaa' is not a number."));
        assertTrue(-1 != page.asText().indexOf("form:number3: 'aaa' is not a number. Example: 99"));
        assertTrue(-1 != page.asText().indexOf("Number4: 'aaa' is not a number."));
        assertTrue(-1 != page.asText().indexOf("Number4: 'aaa' is not a number. Example: 99"));
        assertTrue(-1 != page.asText().indexOf("form:number5: 'aaa' could not be understood as a percentage."));
        assertTrue(-1 != page.asText().indexOf("form:number5: 'aaa' could not be understood as a percentage. Example: 75%"));
        assertTrue(-1 != page.asText().indexOf("Number6: 'aaa' could not be understood as a percentage."));
        assertTrue(-1 != page.asText().indexOf("Number6: 'aaa' could not be understood as a percentage. Example: 75%"));
        assertTrue(-1 != page.asText().indexOf("form:number7: 'aaa' is not a number pattern."));
        assertTrue(-1 != page.asText().indexOf("form:number7: 'aaa' is not a number pattern. Example: #,##0.0#"));
        assertTrue(-1 != page.asText().indexOf("Number8: 'aaa' is not a number pattern."));
        assertTrue(-1 != page.asText().indexOf("Number8: 'aaa' is not a number pattern. Example: #,##0.0#"));
        assertTrue(-1 != page.asText().indexOf("form:short1: 'aaa' must be a number between -32768 and 32767 Example: 32456"));
        assertTrue(-1 != page.asText().indexOf("Short2: 'aaa' must be a number consisting of one or more digits. "));
        assertTrue(-1 != page.asText().indexOf("Short2: 'aaa' must be a number between -32768 and 32767 Example: 32456"));
    }

    public void testEnumConverter() throws Exception {
        HtmlPage page = getPage("/faces/enum-converter.jsp");
        List list;

        // Case 0, invalid data in both text fields
        list = getAllElementsOfGivenClass(page, null,
                HtmlTextInput.class);
        for (int i = 0; i < list.size(); i++) {
            ((HtmlTextInput) list.get(i)).setValueAttribute("aoeuoeuoe");
        }

        list = getAllElementsOfGivenClass(page, null,
                HtmlSubmitInput.class);
        HtmlSubmitInput button = (HtmlSubmitInput) list.get(0);
        page = (HtmlPage) button.click();

        assertTrue(-1 != page.asText().indexOf("suit: 'aoeuoeuoe' must be convertible to an enum. suit: 'aoeuoeuoe' must be convertible to an enum from the enum that contains the constant 'aoeuoeuoe'."));
        assertTrue(-1 != page.asText().indexOf("color: 'aoeuoeuoe' must be convertible to an enum. color: 'aoeuoeuoe' must be convertible to an enum from the enum that contains the constant 'aoeuoeuoe'."));

        // Case 1, valid Suit, invalid color
        list = getAllElementsOfGivenClass(page, null,
                HtmlTextInput.class);
        ((HtmlTextInput) list.get(0)).setValueAttribute("Hearts");

        list = getAllElementsOfGivenClass(page, null,
                HtmlSubmitInput.class);
        button = (HtmlSubmitInput) list.get(0);
        page = (HtmlPage) button.click();

        assertTrue(-1 == page.asText().indexOf("suit:"));
        assertTrue(-1 != page.asText().indexOf("color: 'aoeuoeuoe' must be convertible to an enum. color: 'aoeuoeuoe' must be convertible to an enum from the enum that contains the constant 'aoeuoeuoe'."));

        // Case 2, valid Suit, valid color
        list = getAllElementsOfGivenClass(page, null,
                HtmlTextInput.class);
        ((HtmlTextInput) list.get(1)).setValueAttribute("Blue");

        list = getAllElementsOfGivenClass(page, null,
                HtmlSubmitInput.class);
        button = (HtmlSubmitInput) list.get(0);
        page = (HtmlPage) button.click();

        assertTrue(-1 == page.asText().indexOf("suit:"));
        assertTrue(-1 == page.asText().indexOf("color:"));

        list = getAllElementsOfGivenClass(page, null,
                HtmlSubmitInput.class);
        button = (HtmlSubmitInput) list.get(0);
        page = (HtmlPage) button.click();

        // Case 3, invalid suit, valid color
        list = getAllElementsOfGivenClass(page, null,
                HtmlTextInput.class);
        ((HtmlTextInput) list.get(0)).setValueAttribute("aoeuoeuoe");

        list = getAllElementsOfGivenClass(page, null,
                HtmlSubmitInput.class);
        button = (HtmlSubmitInput) list.get(0);
        page = (HtmlPage) button.click();

        assertTrue(-1 != page.asText().indexOf("suit: 'aoeuoeuoe' must be convertible to an enum. suit: 'aoeuoeuoe' must be convertible to an enum from the enum that contains the constant 'aoeuoeuoe'."));
        assertTrue(-1 == page.asText().indexOf("color:"));


    }
}
