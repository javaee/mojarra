/*
 * $Id: FacetTag.java,v 1.16 2005/08/22 22:08:11 ofung Exp $
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

package javax.faces.webapp;


import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;


/**
 * <p><strong>FacetTag</strong> is the JSP mechanism for denoting a
 * {@link javax.faces.component.UIComponent} is to be added as a
 * <code>facet</code> to the component associated with its parent.</p>
 *
 * <p>A <strong>FacetTag</strong> must have one and only one
 * child.  This child must be a {@link UIComponentTag} instance representing
 * a single {@link javax.faces.component.UIComponent} instance.</p>
 */

public class FacetTag extends TagSupport {


    // -------------------------------------------------------------- Properties


    /**
     * <p>The name of this facet.  This will be used as the facet name for
     * our <code>UIComponentTag</code> child in our <code>UIComponentTag</code>
     * parent's facet list.</p>
     */ 
    private String name = null;


    /**
     * <p>Return the name to be assigned to this facet.</p>
     */
    public String getName() {

	return (name);

    }
    

    /**
     * <p>Set the name to be assigned to this facet.</p>
     *
     * @param name The new facet name
     */
    public void setName(String name) {

	this.name = name;

    }


    // ------------------------------------------------------------- Tag Methods


    /**
     * <p>Release any resources allocated by this tag instance.
     */
    public void release() {

        super.release();
        this.name = null;

    }


    /**
     * <p>Return <code>EVAL_BODY_INCLUDE</code> to cause nested body
     * content to be evaluated.</p>
     */
    public int doStartTag() throws JspException {

        return (EVAL_BODY_INCLUDE);

    }

}
