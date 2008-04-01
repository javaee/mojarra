/*
 * $Id: SelectOne_OptionListTag.java,v 1.19 2002/07/12 19:44:37 eburns Exp $
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

/**
 *
 *  <B>FacesTag</B> is a base class for most tags in the Faces Tag
 *  library.  Its primary purpose is to centralize common tag functions
 *  to a single base class. <P>
 *
 * @version $Id: SelectOne_OptionListTag.java,v 1.19 2002/07/12 19:44:37 eburns Exp $
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

    protected String itemsmodelreference = null;

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

    public void setItemsmodelreference(String newItemsmodelreference) {
	itemsmodelreference = newItemsmodelreference;
    }

    public String getItemsmodelreference() {
	return itemsmodelreference;
    }

//
// General Methods
//

    public String getLocalRendererType() { return "OptionListRenderer"; }
    public UIComponent createComponent() {
        return (new UISelectOne());
    }

    protected void overrideProperties(UIComponent component) {
	super.overrideProperties(component);
	UISelectOne selectOne = (UISelectOne) component;
	
	if (null == selectOne.getItemsModelReference()) {
	    selectOne.setItemsModelReference(getItemsmodelreference());
	}
    }
//
// Methods from TagSupport
// 


} // end of class SelectOne_OptionListTag
