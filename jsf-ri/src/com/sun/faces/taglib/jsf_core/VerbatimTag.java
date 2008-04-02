/*
 * $Id: VerbatimTag.java,v 1.15 2006/03/29 22:38:42 rlubke Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

package com.sun.faces.taglib.jsf_core;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.webapp.UIComponentELTag;
import javax.servlet.jsp.JspException;

/**
 * <p>Tag implementation that creates a {@link UIOutput} instance
 * and allows the user to write raw markup.</p>
 */

public class VerbatimTag extends UIComponentELTag {

    // ------------------------------------------------------------- Attributes


    private ValueExpression escape = null;


    /** Holds value of property rendered. */
    private ValueExpression rendered;

    // ----------------------------------------------- Methods From IterationTag


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

    // ---------------------------------------------------------- Public Methods


    public void setEscape(ValueExpression escape) {

        this.escape = escape;

    }


    /**
     * Setter for property rendered.
     *
     * @param rendered New value of property rendered.
     */
    public void setRendered(ValueExpression rendered) {

        this.rendered = rendered;

    }


    public String getComponentType() {

        return "javax.faces.Output";

    }


    public String getRendererType() {

        return "javax.faces.Text";

    }

    // ------------------------------------------------------- Protected Methods


    protected void setProperties(UIComponent component) {

        super.setProperties(component);
        if (null != escape) {
            component.setValueExpression("escape", escape);
        } else {
            component.getAttributes().put("escape", Boolean.FALSE);
        }
        if (null != rendered) {
            component.setValueExpression("rendered", rendered);
        }
        component.setTransient(true);

    }

}
