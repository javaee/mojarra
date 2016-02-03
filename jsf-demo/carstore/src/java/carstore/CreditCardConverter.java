/*
 * $Id: CreditCardConverter.java,v 1.1.36.2 2007/04/27 21:27:06 ofung Exp $
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
