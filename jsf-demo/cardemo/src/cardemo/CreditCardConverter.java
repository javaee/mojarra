/*
 * $Id: CreditCardConverter.java,v 1.1 2003/01/29 18:46:19 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package cardemo;


import com.sun.faces.RIConstants;
import com.sun.faces.renderkit.FormatPool;
import com.sun.faces.util.Util;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.mozilla.util.Assert;

/**
 * CreditCardConverter Class accepts a Credit Card Number of type String 
 * and strips blanks and <oode>"-"</code> if any from it. It also formats the 
 * CreditCardNumber such a blank space separates every four characters.
 * Blanks and <oode>"-"</code> characters are the expected demiliters 
 * that could be used as part of a CreditCardNumber.
 */
public class CreditCardConverter implements Converter {

    /**
     * Parses the CreditCardNumber and strips any blanks or <oode>"-"</code> 
     * characters from it.
     */
    public Object getAsObject(FacesContext context, UIComponent component,
        String newValue) throws ConverterException {

        String convertedValue = null;
        if ( newValue == null ) {
            return newValue;
        }
        // Since this is only a String to String conversion, this conversion 
        // does not throw ConverterException.
        convertedValue = newValue.trim();
        if ( ((convertedValue.indexOf("-")) != -1) || 
            ((convertedValue.indexOf(" ")) != -1)) {
            char[] input = convertedValue.toCharArray();
            StringBuffer buffer = new StringBuffer(50);
            for ( int i = 0; i < input.length; ++i ) {
                if ( input[i] == '-' || input[i] == ' '  ) {
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
        if ( value == null ) {
            return null;
        }
        // value must be of the type that can be cast to a String.
        try {
            inputVal = (String)value;
        } catch (ClassCastException ce) {
            throw new ConverterException(Util.getExceptionMessage(
                        Util.CONVERSION_ERROR_MESSAGE_ID));
        }

        // insert spaces after every four characters for better    
        // readability if it doesn't already exist.   
        char[] input = inputVal.toCharArray();  
        StringBuffer buffer = new StringBuffer(50);
        for ( int i = 0; i < input.length; ++i ) {
            if ( (i % 4) == 0 && i != 0) {
                if (input[i] != ' ' || input[i] != '-'){ 
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
