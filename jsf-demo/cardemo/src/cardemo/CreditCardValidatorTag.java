/*
 * $Id: CreditCardValidatorTag.java,v 1.1 2003/01/29 18:46:20 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package cardemo;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.webapp.ValidatorTag;
import javax.faces.validator.Validator;
import javax.servlet.jsp.JspException;

/**
 *
 *  <B>ValidateLengthTag</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: CreditCardValidatorTag.java,v 1.1 2003/01/29 18:46:20 jvisvanathan Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class CreditCardValidatorTag extends ValidatorTag {
    //
    // Protected Constants
    //

    //
    // Class Variables
    //

    //
    // Instance Variables
    //

    // Attribute Instance Variables
    protected int maximumChar = 0;
    protected int minimumChar = 0;
  
    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public CreditCardValidatorTag() {
        super();
        super.setType("cardemo.CreditCardValidator");
    }

    //
    // Class methods
    //

    //
    // General Methods
    //

    public int getMaximumChar() {
        return maximumChar;
    }

    public void setMaximumChar(int newMaximumChar){
       maximumChar = newMaximumChar;
    }

    public int getMinimumChar(){
        return minimumChar;
    }

    public void setMinimumChar(int newMinimumChar){
        minimumChar = newMinimumChar;
    }
   
    // 
    // Methods from ValidatorTag
    // 

    protected Validator createValidator() throws JspException
    {
        CreditCardValidator result = null;
        result = (CreditCardValidator) super.createValidator();
        Assert.assert_it(null != result);
        
        result.setMaximumChar(getMaximumChar());
        result.setMinimumChar(getMinimumChar());
        return result;
    }

} // end of class CreditCardValidatorTag
