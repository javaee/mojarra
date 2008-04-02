/*
 * $Id: FacetTag.java,v 1.4 2003/01/23 22:21:30 craigmcc Exp $
 */

/*
 * Copyright 2002-2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.webapp;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;


/**
 * <p><strong>FacetTag</strong> is the JSP mechanism for denoting a
 * UIComponent is to be added as a facet to its parent.</p>

 * <p>The <strong>FacetTag</strong> must have one and only one tag
 * child.  This child must be a <code>FacesTag</code> instance
 * representing a single <code>UIComponent</code> instance.</p>
 *
 * </p>
 */

public class FacetTag extends TagSupport {

    // ------------------------------------------------- Instance Variables

    protected int children = 0;


    // ------------------------------------------------------------- Properties

    /**

    * The name of this facet.  This will be used as the facet name for
    * our <code>FacesTag</code> child in our <code>FacesTag</code>
    * parent's facet list.

    */ 

    protected String name = null;

    public String getName()
    {
	return name;
    }
    
    public void setName(String newName)
    {
	name = newName;
    }

    // ------------------------------------------------------------ Tag Methods


    public void release() {

        super.release();
        this.name = null;
    }

    public int doStartTag() throws JspException {

        return (EVAL_BODY_INCLUDE);

    }

    public int doEndTag() throws JspException {

        children = 0;
        return (EVAL_PAGE);

    }

    // ----------------------------------------------------- General Methods

    /**

    * Cause a JspException to be thrown if we have more than one {@link
    * FacesTag} child.

    * @exception JspException if we have more than one {@link FacesTag}
    * child.

    */

    public void verifySingleChild() throws JspException {
	if (++children > 1) {
	    throw new JspException("Facet may have only one child");
	}
    }

}
