/*
 * $Id: CreditCardConverter.java,v 1.2 2005/08/22 22:08:35 ofung Exp $
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

package carstore;


import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

/**
 * CreditCardConverter Class accepts a Credit Card Number of type String
 * and strips blanks and <oode>"-"</code> if any from it. It also formats the
 * CreditCardNumber such a blank space separates every four characters.
 * Blanks and <oode>"-"</code> characters are the expected demiliters
 * that could be used as part of a CreditCardNumber.
 */
public class CreditCardConverter implements Converter {

    /**
     * <p>The message identifier of the Message to be created if
     * the conversion fails.  The message format string for this
     * message may optionally include a <code>{0}</code> and
     * <code>{1}</code> placeholders, which
     * will be replaced by the object and value.</p>
     */
    public static final String CONVERSION_ERROR_MESSAGE_ID =
        "carstore.Conversion_Error";


    /**
     * Parses the CreditCardNumber and strips any blanks or <oode>"-"</code>
     * characters from it.
     */
    public Object getAsObject(FacesContext context, UIComponent component,
                              String newValue) throws ConverterException {

        String convertedValue = null;
        if (newValue == null) {
            return newValue;
        }
        // Since this is only a String to String conversion, this conversion 
        // does not throw ConverterException.
        convertedValue = newValue.trim();
        if (((convertedValue.indexOf("-")) != -1) ||
            ((convertedValue.indexOf(" ")) != -1)) {
            char[] input = convertedValue.toCharArray();
            StringBuffer buffer = new StringBuffer(50);
            for (int i = 0; i < input.length; ++i) {
                if (input[i] == '-' || input[i] == ' ') {
                    continue;
                } else {
                    buffer.append(input[i]);
                }
            }
            convertedValue = buffer.toString();
        }
        // System.out.println("Converted value " + convertedValue);
        return convertedValue;
    }


    /**
     * Formats the value by inserting space after every four characters
     * for better readability if they don't already exist. In the process
     * converts any <oode>"-"</code> characters into blanks for consistency.
     */
    public String getAsString(FacesContext context, UIComponent component,
                              Object value) throws ConverterException {

        String inputVal = null;
        if (value == null) {
            return null;
        }
        // value must be of the type that can be cast to a String.
        try {
            inputVal = (String) value;
        } catch (ClassCastException ce) {
            FacesMessage errMsg = MessageFactory.getMessage(
                CONVERSION_ERROR_MESSAGE_ID,
                (new Object[]{value, inputVal}));
            throw new ConverterException(errMsg.getSummary());
        }

        // insert spaces after every four characters for better    
        // readability if it doesn't already exist.   
        char[] input = inputVal.toCharArray();
        StringBuffer buffer = new StringBuffer(50);
        for (int i = 0; i < input.length; ++i) {
            if ((i % 4) == 0 && i != 0) {
                if (input[i] != ' ' || input[i] != '-') {
                    buffer.append(" ");
                    // if there any "-"'s convert them to blanks.
                } else if (input[i] == '-') {
                    buffer.append(" ");
                }
            }
            buffer.append(input[i]);
        }
        String convertedValue = buffer.toString();
        // System.out.println("Formatted value " + convertedValue);
        return convertedValue;
    }
}
