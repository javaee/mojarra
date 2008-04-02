/*
 * $Id: ValidateRequiredTag.java,v 1.11 2004/04/06 15:02:04 eburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// ValidateRequiredTag.java

package com.sun.faces.taglib.jsf_core;

import javax.faces.webapp.ValidatorTag;
import javax.servlet.jsp.JspException;


/**
 * <B>ValidateRequiredTag</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: ValidateRequiredTag.java,v 1.11 2004/04/06 15:02:04 eburns Exp $
 */

public class ValidateRequiredTag extends ValidatorTag {

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


// Relationship Instance Variables

//
// Constructors and Initializers    
//

    public ValidateRequiredTag() {
        super();
    }

//
// Class methods
//

//
// General Methods
//

    public int doStartTag() throws JspException {
        super.setValidatorId("javax.faces.Required");
	return super.doStartTag();
    }


// 
// Methods from ValidatorTag
// 

} // end of class ValidateRequiredTag
