/*
 * $Id: ValidatorTag.java,v 1.1 2002/07/22 18:27:47 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// ValidatorTag.java

package com.sun.faces.taglib;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.component.UIComponent;
import javax.faces.validator.Validator;
import javax.faces.validator.LengthValidator;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import javax.faces.webapp.FacesTag;

/**

 *  <B>ValidatorTag</B> extends the ValidatorTag in jsf-api and adds the
 *  feature of conveying jsp attributes for stock validations into the
 *  UIComponent instance to which this validator is attached. <P>

 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: ValidatorTag.java,v 1.1 2002/07/22 18:27:47 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class ValidatorTag extends javax.faces.webapp.ValidatorTag
{
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

    protected String maxLengthInt = null;
    protected String minLengthInt = null;


// Relationship Instance Variables

//
// Constructors and Initializers    
//

public ValidatorTag()
{
    super();
}

//
// Class methods
//

//
// Accessors
//

public String getMaxLengthInt()
{
    return maxLengthInt;
}

public void setMaxLengthInt(String newMaxLengthInt)
{
    maxLengthInt = newMaxLengthInt;
}

public String getMinLengthInt()
{
    return minLengthInt;
}

public void setMinLengthInt(String newMinLengthInt)
{
    minLengthInt = newMinLengthInt;
}

//
// General Methods
//

protected void setAttributesIntoComponent(UIComponent component)
{
    try {
	if (null != getMaxLengthInt()) {
	    component.setAttribute(LengthValidator.MAXIMUM_ATTRIBUTE_NAME,
				   new Integer(getMaxLengthInt()));
	}
	if (null != getMinLengthInt()) {
	    component.setAttribute(LengthValidator.MINIMUM_ATTRIBUTE_NAME,
				   new Integer(getMinLengthInt()));
	}
    }
    catch (NumberFormatException e) {
	// PENDING(edburns): do something with this exception
    }
}

//
// Methods from TagSupport
//

public int doStartTag() throws JspException 
{
    int rc;
    Tag tag = getParent();
    while ((tag != null) && !(tag instanceof FacesTag)) {
	tag = tag.getParent();
    }
    if (tag == null) { // FIXME - i18n
	throw new JspException("Not nested in a FacesTag");
    }
    setAttributesIntoComponent(((FacesTag) tag).getComponent());

    rc = super.doStartTag();
    return rc;
}

// Delete this text and replace the below text with the name of the file
// containing the testcase covering this class.  If this text is here
// someone touching this file is poor software engineer.

// The testcase for this class is TestValidatorTag.java 


} // end of class ValidatorTag
