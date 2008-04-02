/*
 * $Id: Command_LinkTag.java,v 1.4 2003/11/09 05:42:03 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.html_basic;


import org.mozilla.util.ParameterCheck;

import javax.faces.component.UIComponent;
import javax.faces.component.UICommand;
import javax.faces.el.ValueBinding;
import javax.servlet.jsp.JspException;

import com.sun.faces.taglib.BaseComponentBodyTag;
import com.sun.faces.util.Util;

import java.io.IOException;

/**
 * This class is the tag handler that evaluates the 
 * <code>command_link</code> custom tag.
 */

public class Command_LinkTag extends BaseComponentBodyTag
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
    protected String actionRef = null;
    protected String immediate = null;

    
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
        commandname = newCommandname;
    }
 
    public void setActionRef(String newActionRef) {
        actionRef = newActionRef;
    }

    public void setImmediate(String newImmediate) {
        immediate = newImmediate;
    }

    //
    // General Methods
    //

    public String getRendererType() { 
        return "Link"; 
    }
    public String getComponentType() { 
        return "CommandLink"; 
    }



    protected void overrideProperties(UIComponent component) {
	super.overrideProperties(component);
	UICommand link = (UICommand) component;
        if (actionRef != null ) {
	    link.setActionRef(actionRef);
	}
        if (action != null ) {
	    if (isValueReference(action)) {
		link.setValueBinding("action", Util.getValueBinding(action));
	    }
	    else {
		link.setAction(action);
	    }
	}
        if (value != null) {
	    if (isValueReference(value)) {
		link.setValueBinding("value", Util.getValueBinding(value));
	    }
	    else {
		link.setValue(value);
	    }
        }
	if (immediate != null) {
	    if (isValueReference(immediate)) {
		link.setValueBinding("immediate", 
				     Util.getValueBinding(immediate));
	    }
	    else {
		link.setImmediate(Boolean.getBoolean(immediate));
	    }
	}
        // set HTML 4. attributes.
        if (shape != null) {
	    if (isValueReference(shape)) {
		link.setValueBinding("shape", 
				     Util.getValueBinding(shape));
	    }
	    else {
		link.getAttributes().put("shape", shape);
	    }
        }
        if (coords != null) {
	    if (isValueReference(coords)) {
		link.setValueBinding("coords", 
				     Util.getValueBinding(coords));
	    }
	    else {
		link.getAttributes().put("coords", coords);
	    }
        }
        if (rel != null) {
	    if (isValueReference(rel)) {
		link.setValueBinding("rel", 
				     Util.getValueBinding(rel));
	    }
	    else {
		link.getAttributes().put("rel", rel);
	    }
        }
        if (rev != null) {
	    if (isValueReference(rev)) {
		link.setValueBinding("rev", 
				     Util.getValueBinding(rev));
	    }
	    else {
		link.getAttributes().put("rev", rev);
	    }
        }
        if (hreflang != null) {
	    if (isValueReference(hreflang)) {
		link.setValueBinding("hreflang", 
				     Util.getValueBinding(hreflang));
	    }
	    else {
		link.getAttributes().put("hreflang", hreflang);
	    }
        }
        if (charset != null) {
	    if (isValueReference(charset)) {
		link.setValueBinding("charset", 
				     Util.getValueBinding(charset));
	    }
	    else {
		link.getAttributes().put("charset", charset);
	    }
        }
        if (type != null) {
	    if (isValueReference(type)) {
		link.setValueBinding("type", 
				     Util.getValueBinding(type));
	    }
	    else {
		link.getAttributes().put("type", type);
	    }
        }
    }

    //
    // Methods from TagSupport
    // 

    public int doEndTag() throws JspException {
	String content = null;     

        try {
            if (null == (bodyContent = getBodyContent())) {
		Object params [] = { this.getClass().getName() };
                throw new JspException(Util.getExceptionMessage(
                        Util.NULL_BODY_CONTENT_ERROR_MESSAGE_ID, params));
            }    
	    content = bodyContent.getString();
            
	    getPreviousOut().write(content);
        } catch (IOException iox) {
            Object [] params = { "session", iox.getMessage() };
            throw new JspException(Util.getExceptionMessage(
                    Util.SAVING_STATE_ERROR_MESSAGE_ID, params));
        }  

	int rc = super.doEndTag();

	return rc;
    }


} // end of class Command_LinkTag
