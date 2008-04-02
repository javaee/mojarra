/*
 * $Id: FacetTag.java,v 1.11 2004/01/26 20:49:01 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
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


    /**
     * <p>Return <code>EVAL_PAGE</code> to cause the remainder of the
     * current page to be evaluated.</p>
     */
    public int doEndTag() throws JspException {

        return (EVAL_PAGE);

    }


}
