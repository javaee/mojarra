/*
 * $Id: Command_HyperlinkTag.java,v 1.42 2003/10/13 22:56:23 jvisvanathan Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.html_basic;


import org.mozilla.util.ParameterCheck;

import javax.faces.component.UIComponent;
import javax.faces.component.UICommand;
import javax.servlet.jsp.JspException;

import com.sun.faces.taglib.BaseComponentTag;
import com.sun.faces.util.Util;


/**
 * This class is the tag handler that evaluates the 
 * <code>command_hyperlink</code> custom tag.
 */

public class Command_HyperlinkTag extends BaseComponentTag
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
    protected String commandname = null;
    protected String commandname_ = null;
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

    public void setCommandName(String newCommandname) {
        ParameterCheck.nonNull(newCommandname);
        commandname_ = newCommandname;
    }
 
    public void setImage(String newImage) {
        image_ = newImage;
    }
    
    public void setActionRef(String newActionRef) {
        actionRef_ = newActionRef;
    }

    public void setImmediate(boolean newImmediate) {
        immediate = newImmediate;
    }

    //
    // General Methods
    //

    public String getRendererType() { 
        return "Hyperlink"; 
    }
    public String getComponentType() { 
        return "CommandHyperlink"; 
    }



    protected void overrideProperties(UIComponent component) {
	super.overrideProperties(component);
	UICommand link = (UICommand) component;
        if (actionRef != null ) {
	    link.setActionRef(actionRef);
	}
        if (action != null ) {
	    link.setAction(action);
	}
        if (value != null) {
            link.setValue(value);
        }
        if (image != null) {
            link.getAttributes().put("image", image);
        }
	link.setImmediate(immediate);
        // set HTML 4. attributes.
        if (shape != null) {
            link.getAttributes().put("shape", shape);
        }
        if (coords != null) {
            link.getAttributes().put("coords", coords);
        }
        if (rel != null) {
            link.getAttributes().put("rel", rel);
        }
        if (rev != null) {
            link.getAttributes().put("rev", rev);
        }
        if (hreflang != null) {
            link.getAttributes().put("hreflang", hreflang);
        }
        if (charset != null) {
            link.getAttributes().put("charset", charset);
        }
    }

    /* Evaluates expressions as necessary */
    private void evaluateExpressions() throws JspException {
        if (commandname_ != null) {
            commandname = Util.evaluateElExpression(commandname_, pageContext);
        }
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

} // end of class Command_HyperlinkTag
