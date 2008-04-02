/*
 * $Id: OutputVerbatimTag.java,v 1.4 2004/02/04 23:42:33 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.systest;


import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.webapp.UIComponentBodyTag;
import javax.servlet.jsp.JspException;


/**
 * <p><code>UIComponentBodyTag</code> for <code>Verbatim</code> rendering.
 * In other words, the body content of this tag is captured and then
 * rendered as the value of this component.</p>
 */

public class OutputVerbatimTag extends UIComponentBodyTag {


    // -------------------------------------------------------------- Attributes


    // ---------------------------------------------------------- Public Methods


    public String getComponentType() {
        return ("Output");
    }


    public String getRendererType() {
        return ("Text");
    }


    // Assign the trimmed body content of this tag as the value of the
    // current component.
    public int doAfterBody() throws JspException {

        // Save the trimmed body content of this tag (if any)
        if (getBodyContent() != null) {
            String value = getBodyContent().getString().trim();
            if (value != null) {
                ((UIOutput) getComponentInstance()).setValue(value);
            }
        }

        // Perform normal superclass processing
        return (super.doAfterBody());

    }


    // ------------------------------------------------------- Protected Methods


}
