/*
 * $Id: ChildrenComponentBodyTag.java,v 1.2 2005/08/22 22:11:35 ofung Exp $
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
