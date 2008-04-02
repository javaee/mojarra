/*
 * $Id: Output_LinkTag.java,v 1.4 2004/01/08 21:21:37 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.html_basic;




import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.servlet.jsp.JspException;

import com.sun.faces.taglib.BaseComponentBodyTag;
import com.sun.faces.util.Util;

import java.io.IOException;

/**
 * This class is the tag handler that evaluates the 
 * <code>output_link</code> custom tag.
 */

public class Output_LinkTag extends BaseComponentBodyTag
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
        return "Link"; 
    }
    public String getComponentType() { 
        return "OutputLink"; 
    }



    protected void setProperties(UIComponent component) {
	super.setProperties(component);
	UIOutput link = (UIOutput) component;
        // set HTML 4. attributes.
        if (value != null) {
	    if (isValueReference(value)) {
		link.setValueBinding("value", Util.getValueBinding(value));
	    }
	    else {
		link.setValue(value);
	    }
        }

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


} // end of class Output_LinkTag
