/*
 * $Id: SelectOne_OptionListTag.java,v 1.23 2002/08/02 00:11:05 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// SelectOne_OptionListTag.java

package com.sun.faces.taglib.html_basic;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.servlet.jsp.JspException;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectOne;
import javax.faces.context.FacesContext;
import javax.faces.FacesException;

import com.sun.faces.util.Util;

import com.sun.faces.taglib.FacesTag;
import com.sun.faces.RIConstants;

/**
 *
 *  <B>FacesTag</B> is a base class for most tags in the Faces Tag
 *  library.  Its primary purpose is to centralize common tag functions
 *  to a single base class. <P>
 *
 * @version $Id: SelectOne_OptionListTag.java,v 1.23 2002/08/02 00:11:05 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class SelectOne_OptionListTag extends FacesTag
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

// Relationship Instance Variables

//
// Constructors and Initializers    
//

public SelectOne_OptionListTag()
{
    super();
}

//
// Class methods
//

// 
// Accessors
//

//
// General Methods
//

    public String getLocalRendererType() { return "OptionListRenderer"; }
    public UIComponent createComponent() {
        return (new UISelectOne());
    }

//
// Methods from TagSupport
// 

    public int doEndTag() throws JspException {
	UISelectOne component = (UISelectOne) getComponent();

	// This makes sure the no more SelectItems get added to this
	// selectOne instance.
	component.setAttribute(RIConstants.SELECTITEMS_CONFIGURED, 
			       RIConstants.SELECTITEMS_CONFIGURED);
	int rc = super.doEndTag();

	return rc;
    }


} // end of class SelectOne_OptionListTag
