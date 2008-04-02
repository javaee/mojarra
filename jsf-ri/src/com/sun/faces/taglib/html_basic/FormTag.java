/*
 * $Id: FormTag.java,v 1.51 2003/10/19 14:54:46 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.html_basic;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.servlet.jsp.JspException;

import com.sun.faces.taglib.BaseComponentTag;
import com.sun.faces.RIConstants;
import com.sun.faces.util.Util;


/**
 * This class is the tag handler that evaluates the <code>form</code> custom tag.
 */

public class FormTag extends BaseComponentTag
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

    public FormTag()
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

    public String getRendererType() { 
        return "Form"; 
    }
    public String getComponentType() { 
        return "Form"; 
    }

    protected void overrideProperties(UIComponent component) {
	super.overrideProperties(component);
        
        // action, method, enctype, acceptcharset, accept, target, onsubmit, 
        // onreset
        if (onsubmit != null ) {
            component.getAttributes().put("onsubmit", onsubmit); 
        }
        if (onreset != null ) {
            component.getAttributes().put("onreset", onreset); 
        }
        if (action != null ) {
            component.getAttributes().put("action", action); 
        }
        if (method != null ) {
            component.getAttributes().put("method", method); 
        }
        if (enctype != null ) {
            component.getAttributes().put("enctype", enctype); 
        }
        if (accept != null ) {
            component.getAttributes().put("accept", accept); 
        }
        if (target != null ) {
            component.getAttributes().put("target", target); 
        }
        if (acceptcharset != null ) {
            component.getAttributes().put("acceptcharset", acceptcharset); 
        }        
    }

    //
    // Methods from TagSupport
    //

    public int doStartTag() throws JspException {

        // chain to the parent implementation
        return super.doStartTag();
    }


} // end of class FormTag
