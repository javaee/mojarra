/*
 * $Id: Output_LabelTag.java,v 1.15 2003/10/28 21:00:34 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.html_basic;

import javax.faces.component.UIComponent;
import javax.servlet.jsp.JspException;

import com.sun.faces.taglib.BaseComponentTag;
import com.sun.faces.util.Util;


/**
 * This class is the tag handler that evaluates the 
 * <code>output_label</code> custom tag.
 */

public class Output_LabelTag extends BaseComponentTag
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

    protected String forValue = null;
    protected String forValue_ = null;


    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public Output_LabelTag()
    {
        super();
    }

    //
    // Class methods
    //

    // 
    // Accessors
    //

    public void setFor(String newForValue)
    {
        forValue_ = newForValue;
    }

    //
    // General Methods
    //

    public String getRendererType() { 
        return "Label"; 
    }
    public String getComponentType() { 
        return "OutputLabel"; 
    }

    protected void overrideProperties(UIComponent component) {
	super.overrideProperties(component);

	if (null != forValue) {
	    // PENDING(edburns): We should do something more intelligent
	    // here.  For now, however, we'll just store what we get
	    // from JSP.
	    component.getAttributes().put("for",forValue);
	}
    }
    
    /* Evaluates expressions as necessary */
    protected void evaluateExpressions() throws JspException {
	super.evaluateExpressions();
        if (forValue_ != null) {
            forValue = Util.evaluateElExpression(forValue_, pageContext);
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



} // end of class Output_LabelTag
