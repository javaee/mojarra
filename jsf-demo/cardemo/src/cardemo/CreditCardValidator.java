/*
 * $Id: CreditCardValidator.java,v 1.2 2003/02/21 23:44:26 ofung Exp $
 */

/*
 * Copyright 2002, 2003 Sun Microsystems, Inc. All Rights Reserved.
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

package cardemo;

import javax.faces.FactoryFinder;
import javax.faces.component.AttributeDescriptor;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.Message;
import javax.faces.context.MessageResourcesFactory;
import javax.faces.context.MessageResources;
import javax.faces.validator.Validator;

/**
 * <p><strong>CreditCardValidator</strong> is a Validator that checks
 * the validity of characters in the String representation of the value of the
 * associated component.  The following algorithm is implemented:</p>
 * <ul>
 * <li>Call getValue() to retrieve the current value of the component.
 *     If it is <code>null</code>, exit immediately.  (If null values
 *     should not be allowed, a {RequiredValidator} can be configured
 *     to check for this case.)</li>
 * <li>Make sure that every character in the value lies between 
 *     <code>minimumChar</code> and <code>maximumChar</code> If validation 
 *     failed, add a CREDITCARD_INVALID_MESSAGE_ID message to the FacesContext
 *     for this request.</li> </ul>
 * <code>minimumChar</code> and <code>maximumChar</code> are required
 * for this validator.
 *
 * Validators have to be Serializable, so you can't maintain a reference to 
 * a java.sql.Connection or javax.sql.DataSource inside this class in case 
 * you need to hook upto the database to further verify if the CreditCardNumber is
 * valid. One approach would be to use JNDI-based data source lookups or do
 * this verification in the business tier.
 */

public class CreditCardValidator implements Validator {


    // ----------------------------------------------------- Manifest Constants

    /**
     * <p>The message identifier of the Message to be created if
     * the validation fails.  The message format string for this
     * message may optionally include a <code>{0}</code> placeholder, which
     * will be replaced by the configured minimum character allowed and
     * may optionally include a <code>{1}</code> placeholder, which
     * will be replaced by the configured maximum character allowed.</p>
     */
    public static final String CREDITCARD_INVALID_CHARS_MESSAGE_ID =
        "cardemo.CreditCardValidator.Invalid_Characters";


    //
    // Constructors and Initializers    
    //
    public CreditCardValidator() {
        super();
    }

    /**
     * <p>Construct a CreditCardValidator with the specified preconfigured
     * limits.</p>
     *
     * @param maxchar Maximum Character value to allow.
     * @param minchar Minimum Character value to allow
     *
     */
    public CreditCardValidator(int maxchar, int minchar) {
        super();
        setMaximumChar(maxchar);
        setMinimumChar(minchar);
    }

    // 
    // General Methods
    //
    /**
     * <p>The maximum Character to be enforced by this Validator.
     */
    private int maximumChar = 0;


    /**
     * <p>Return the maximum character to be enforced by this Validator.
     */
    public int getMaximumChar() {

        return (this.maximumChar);

    }


    /**
     * <p>Set the maximum character to be enforced by this Validator.</p>
     *
     * @param maxchar The new maximum character value
     *
     */
    public void setMaximumChar(int maxchar) {

        this.maximumChar = maxchar;
    }

    /**
     * <p>The minimum Character to be enforced by this Validator.
     */
    private int minimumChar = 0;


    /**
     * <p>Return the minimum character to be enforced by this Validator.
     */
    public int getMinimumChar() {

        return (this.minimumChar);

    }

    /**
     * <p>Set the minimum character to be enforced by this Validator.</p>
     *
     * @param minchar The new minimum character value
     *
     */
    public void setMinimumChar(int minchar) {

        this.minimumChar = minchar;
    }

    // 
    // Methods from Validator
    //
    public void validate(FacesContext context, UIComponent component) {

        if ((context == null) || (component == null)) {
            throw new NullPointerException();
        }
        Object value = component.getValue();
        if (value != null) {
            String converted = value.toString();
            char[] input = converted.toCharArray();
            for ( int i = 0; i < input.length; ++i ) {
                int num = Character.getNumericValue(input[i]);
                if (num == -1 ) {
                    component.setValid(false);
                    break;
                }    
                if ( num < minimumChar || num > maximumChar) {
                    Message errMsg = getMessageResources().getMessage(context, 
                            CREDITCARD_INVALID_CHARS_MESSAGE_ID, 
                     (new Object[] { new Integer(minimumChar),
                             new Integer(maximumChar) }));
                    context.addMessage(component, errMsg);
                    component.setValid(false);
                    break;
                }
            }
        }

    }
    
    /**
     * This method will be called before calling facesContext.addMessage, so 
     * message can be localized.
     * <p>Return the {@link MessageResources} instance for the message
     * resources defined by the JavaServer Faces Specification.
     */
    public synchronized MessageResources getMessageResources() {
        MessageResources carResources = null;
	MessageResourcesFactory factory = (MessageResourcesFactory)
	    FactoryFinder.getFactory
	    (FactoryFinder.MESSAGE_RESOURCES_FACTORY);
	carResources = factory.getMessageResources("carResources");
        return (carResources);
    }
}
