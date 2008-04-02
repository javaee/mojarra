/*
 * $Id: VerbatimTag.java,v 1.3 2003/09/24 23:17:37 horwat Exp $
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

/**
 * <p>Tag implementation that creates a {@link UIOutput} instance
 * and allows the user to write raw markup.</p>
 *
 */

public class VerbatimTag extends UIComponentBodyTag {


    // ------------------------------------------------------------- Attributes


    private boolean escape = false;


    public void setEscape(boolean escape) {

        this.escape = escape;

    }


    // --------------------------------------------------------- Public Methods

    public String getRendererType() { return "Text"; }
    public String getComponentType() { return "Output"; }

    protected void overrideProperties(UIComponent component) {
	super.overrideProperties(component);
	component.getAttributes().put("escape", escape ? Boolean.TRUE : Boolean.FALSE);
    }    

    /**
     * <p>Set the local value of this component to reflect the nested
     * body content of this JSP tag.</p>
     */
    public int doAfterBody() throws JspException {
        if (getBodyContent() != null) {
            String value = getBodyContent().getString().trim();
            if (value != null) {
		UIOutput output = (UIOutput) getComponent();
		output.setValue(value);
            }
        }
        return (getDoAfterBodyValue());
    }



}
