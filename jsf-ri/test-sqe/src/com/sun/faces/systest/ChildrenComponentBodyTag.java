/*
 * $Id: ChildrenComponentBodyTag.java,v 1.1 2005/07/25 18:34:23 rajprem Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.systest;


import javax.faces.webapp.UIComponentBodyTag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;


/**
 * <p><code>UIComponentBodyTag</code> for <code>ChildrenComponent</code>.</p>
 */

public class ChildrenComponentBodyTag extends UIComponentBodyTag {

    private boolean firstPass = true;

    // -------------------------------------------------------------- Attributes


    // ---------------------------------------------------------- Public Methods


    public String getComponentType() {
        return ("ChildrenComponent");
    }


    public String getRendererType() {
        return (null);
    }


    /**
     * <p>Handle the ending of the nested body content for this tag.  The
     * default implementation simply calls <code>getDoAfterBodyValue()</code> to
     * retrieve the flag value to be returned.</p>
     *
     * @throws javax.servlet.jsp.JspException if an error is encountered
     */
    public int doAfterBody() throws JspException {
        if (firstPass) {
            System.out.println("Evaluating body again...");
            BodyContent cont = getBodyContent();
            cont.clearBody();
            firstPass = false;
            return EVAL_BODY_AGAIN;
        }
        else {
            return super.doAfterBody();
        }
    }


    // ------------------------------------------------------- Protected Methods


}
