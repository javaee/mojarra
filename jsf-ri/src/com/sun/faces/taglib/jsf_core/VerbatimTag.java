/*
 * $Id: VerbatimTag.java,v 1.11 2005/04/21 18:55:38 edburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.jsf_core;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;
import javax.servlet.jsp.JspException;

/**
 * <p>Tag implementation that creates a {@link UIOutput} instance
 * and allows the user to write raw markup.</p>
 */

public class VerbatimTag extends UIComponentTag {


    // ------------------------------------------------------------- Attributes


    private String escape = null;


    public void setEscape(String escape) {
        this.escape = escape;
    }


    // --------------------------------------------------------- Public Methods


    public String getRendererType() {
        return "javax.faces.Text";
    }


    public String getComponentType() {
        return "javax.faces.Output";
    }


    protected void setProperties(UIComponent component) {

        super.setProperties(component);
        if (null != escape) {
            if (isValueReference(escape)) {
                ValueBinding vb =
                    FacesContext.getCurrentInstance().getApplication().
                    createValueBinding(escape);
                component.setValueBinding("escape", vb);
            } else {
                boolean _escape = Boolean.valueOf(escape).booleanValue();
                component.getAttributes().put
                    ("escape", _escape ? Boolean.TRUE : Boolean.FALSE);
            }
        } else {
            component.getAttributes().put("escape", Boolean.FALSE);
        }
        component.setTransient(true);

    }


    /**
     * <p>Set the local value of this component to reflect the nested
     * body content of this JSP tag.</p>
     */
    public int doAfterBody() throws JspException {

        if (getBodyContent() != null) {
            String value = getBodyContent().getString();
            if (value != null) {
                UIOutput output = (UIOutput) getComponentInstance();
                output.setValue(value);
		getBodyContent().clearBody();
            }
        }
        return (getDoAfterBodyValue());

    }


}
