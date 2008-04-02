/*
 * $Id: InputTag.java,v 1.16 2003/10/07 20:15:52 horwat Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.html_basic;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.servlet.jsp.JspException;


import com.sun.faces.taglib.BaseComponentTag;
import com.sun.faces.util.Util;


/**
 * This class acts as the base class for input tags.
 */

public abstract class InputTag extends BaseComponentTag
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
    protected String label = null;
    protected String label_ = null;
    protected boolean required = false;
    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public InputTag()
    {
        super();
    }

    // 
    // Accessors
    //
    
     public void setRequired(boolean newVal) {
	required = newVal;
    }

    public void setLabel(String newLabel)
    {
        label_ = newLabel;
    }

    //
    // General Methods
    //

    protected void overrideProperties(UIComponent component) {
	super.overrideProperties(component);
	UIInput input = (UIInput) component;

        if (readonly) {
	    input.getAttributes().put("readonly", new Boolean(readonly));
	}
	if (size != Integer.MIN_VALUE) {
	    input.getAttributes().put("size", new Integer(size));
	}
	if (maxlength != Integer.MIN_VALUE) {
	    input.getAttributes().put("maxlength", 
				      new Integer(maxlength));
	}
	if (null != alt) {
	    input.getAttributes().put("alt", alt);
	}
        if (null != onselect) {
	    input.getAttributes().put("onselect", onselect);
	}
	if (null != onchange) {
	    input.getAttributes().put("onchange", onchange);
	}
        input.setRequired(required);
    }

    public String getComponentType() { return "Input"; }
    

    /* Evaluates expressions as necessary */
    private void evaluateExpressions() throws JspException {
        if (label_ != null) {
            label = Util.evaluateElExpression(label_, pageContext);
   	}
    }

    //
    // Methods from TagSupport
    //

    public int doStartTag() throws JspException {
        // evaluate any expressions that we were passed
        evaluateExpressions();

        // chain to the parent implementation
        return super.doStartTag();
    }

    
} // end of class Input_DateTag
