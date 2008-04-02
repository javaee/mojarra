/*
 * $Id: Command_ButtonTag.java,v 1.48 2003/10/28 21:00:33 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.html_basic;

import javax.faces.component.UIComponent;
import javax.faces.component.UICommand;
import javax.servlet.jsp.JspException;

import com.sun.faces.taglib.BaseComponentTag;
import com.sun.faces.util.Util;

/**
 * This class is the tag handler that evaluates the <code>command_button</code>
 * custom tag.
 */

public class Command_ButtonTag extends BaseComponentTag
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
    protected String image = null;
    protected String image_ = null;
    protected String actionRef = null;
    protected String actionRef_ = null;
    protected boolean immediate = false;

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //


    //
    // Class methods
    //

    // 
    // Accessors
    //

    public void setActionRef(String newActionRef) {
        actionRef_ = newActionRef;
    }

    public void setImage(String newImage) {
        image_ = newImage;
    }

    public void setImmediate(boolean newImmediate) {
        immediate = newImmediate;
    }


    //
    // General Methods
    //

    public String getRendererType() { return "Button"; }
    public String getComponentType() { return "CommandButton"; }

    protected void overrideProperties(UIComponent component) {
	super.overrideProperties(component);
	UICommand button = (UICommand) component;
        if (actionRef != null ) {
	    button.setActionRef(actionRef);
	}
        if (action != null ) {
	    button.setAction(action);
	}

        if (type != null) {
            button.getAttributes().put("type", type);
        }

        if (value != null) {
            button.setValue(value);
        }

        if (image != null) {
            button.getAttributes().put("image", image);
        }

	button.setImmediate(immediate);
    }

    /* Evaluates expressions as necessary */
    protected void evaluateExpressions() throws JspException {
	super.evaluateExpressions();
        if (image_ != null) {
            image = Util.evaluateElExpression(image_, pageContext);
        }
        if (actionRef_ != null) {
            actionRef = Util.evaluateElExpression(actionRef_, pageContext);
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


} // end of class Command_ButtonTag
