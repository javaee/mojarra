/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


/**
 * $Id: SelectMany_CheckboxListTag.java,v 1.15 2003/10/28 21:00:35 eburns Exp $
 *
 * (C) Copyright International Business Machines Corp., 2001,2002
 * The source code for this program is not published or otherwise
 * divested of its trade secrets, irrespective of what has been
 * deposited with the U. S. Copyright Office.   
 */

package com.sun.faces.taglib.html_basic;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectMany;
import javax.servlet.jsp.JspException;

import com.sun.faces.taglib.BaseComponentTag;
import com.sun.faces.util.Util;


/**
 * This class is the tag handler that evaluates the 
 * <code>selectmany_checkboxlist</code> custom tag.
 */

public class SelectMany_CheckboxListTag extends BaseComponentTag
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

    public SelectMany_CheckboxListTag()
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
        return "CheckboxList"; 
    } 
    public String getComponentType() {
        return "SelectManyCheckboxList"; 
    }

    protected void overrideProperties(UIComponent component) {
	super.overrideProperties(component);
	UISelectMany uiSelectMany = (UISelectMany) component;
	
        if (null != layout) {
	    uiSelectMany.getAttributes().put("layout", layout);
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

    public int doEndTag() throws JspException {
        int rc = super.doEndTag();
        return rc;
    }


} // end of class SelectMany_CheckboxListTag
