/*
 * $Id: SelectOne_RadioTag.java,v 1.36 2003/10/30 22:15:40 jvisvanathan Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.html_basic;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectOne;
import javax.servlet.jsp.JspException;

import com.sun.faces.util.Util;

/**
 * This class is the tag handler that evaluates the 
 * <code>selectone_radio</code> custom tag.
 */

public class SelectOne_RadioTag extends SelectOne_ListboxTag
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

    protected String layout = null;
    protected String layout_ = null;


    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public SelectOne_RadioTag()
    {
        super();
    }

    //
    // Class methods
    //

    // 
    // Accessors
    //

    public void setLayout(String newLayout) {
	layout_ = newLayout;
    }

    //
    // General Methods
    //

    public String getRendererType() { 
        return "RadioButtonList"; 
    }
    public String getComponentType() { 
        return "SelectOneRadioButtonList"; 
    }

    protected void overrideProperties(UIComponent component) {
	super.overrideProperties(component);
	UISelectOne uiSelectOne = (UISelectOne) component;
	
        if (null != layout) {
	    uiSelectOne.getAttributes().put("layout", layout);
	}
        if (null != enabledClass) {
	    uiSelectOne.getAttributes().put("enabledClass", enabledClass);
	}
        if (null != disabledClass) {
	    uiSelectOne.getAttributes().put("disabledClass", disabledClass);
	}
        if (null != layout) {
	    uiSelectOne.getAttributes().put("layout", layout);
	}
        if (border != Integer.MIN_VALUE) {
	    uiSelectOne.getAttributes().put("border", new Integer(border));
	}
    }

    /* Evaluates expressions as necessary */
    protected void evaluateExpressions() throws JspException {
	super.evaluateExpressions();
        if (layout_ != null) {
            layout = Util.evaluateElExpression(layout_, pageContext);
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



} // end of class SelectOne_RadioTag
