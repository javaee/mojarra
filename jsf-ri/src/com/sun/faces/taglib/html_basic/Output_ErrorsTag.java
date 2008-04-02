/*
 * $Id: Output_ErrorsTag.java,v 1.16 2003/10/07 20:15:53 horwat Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.html_basic;

import com.sun.faces.taglib.BaseComponentTag;
import com.sun.faces.util.Util;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.servlet.jsp.JspException;


/**
 *  <B>Output_ErrorsTag</B> is a convenience tag allowing content
 *  authors flexible error reporting.  See the tld for
 *  attribute description.
 */

public class Output_ErrorsTag extends BaseComponentTag {

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
    protected String color = null;
    protected String color_ = null;

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public Output_ErrorsTag() {
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

    public void setColor(String newColor) {
        color_ = newColor;
    }

    //
    // General Methods
    //

    public String getRendererType() {
        return "Errors"; 
    }
    public String getComponentType() {
        return "Output"; 
    }

    protected void overrideProperties(UIComponent component) {
        super.overrideProperties(component);       
        if (null != forValue) {
            component.getAttributes().put("for", forValue);
        }
        if (null != color) {
            component.getAttributes().put("color", color);
        }
    }

    /* Evaluates expressions as necessary */
    private void evaluateExpressions() throws JspException {
        if (forValue_ != null) {
            forValue = Util.evaluateElExpression(forValue_, pageContext);
   	}
        if (color_ != null) {
            color = Util.evaluateElExpression(color_, pageContext);
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



} // end of class Output_ErrorsTag
