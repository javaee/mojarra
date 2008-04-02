/*
 * $Id: VerbatimTag.java,v 1.5 2004/01/17 05:20:55 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.jsf_core;

import javax.servlet.jsp.JspException;

import javax.faces.webapp.UIComponentBodyTag;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/**
 * <p>Tag implementation that creates a {@link UIOutput} instance
 * and allows the user to write raw markup.</p>
 *
 */

public class VerbatimTag extends UIComponentBodyTag {


    // ------------------------------------------------------------- Attributes


    private String escape = null;


    public void setEscape(String escape) {

        this.escape = escape;

    }


    // --------------------------------------------------------- Public Methods

    public String getRendererType() { return "Text"; }
    public String getComponentType() { return "Output"; }

    protected void setProperties(UIComponent component) {
	super.setProperties(component);
	if (null != escape) {
	    if (isValueReference(escape)) {
		ValueBinding vb = FacesContext.getCurrentInstance().getApplication().createValueBinding(escape);
		component.setValueBinding("escape", vb);
	    }
	    else {
		boolean _escape = new Boolean(escape).booleanValue();
		component.getAttributes().put("escape", _escape ? Boolean.TRUE : Boolean.FALSE);
	    }
	}
	component.setTransient(true);
    }    

    /**
     * <p>Set the local value of this component to reflect the nested
     * body content of this JSP tag.</p>
     */
    public int doAfterBody() throws JspException {
        if (getBodyContent() != null) {
            String value = getBodyContent().getString().trim();
            if (value != null) {
		UIOutput output = (UIOutput) getComponentInstance();
		output.setValue(value);
            }
        }
        return (getDoAfterBodyValue());
    }



}
