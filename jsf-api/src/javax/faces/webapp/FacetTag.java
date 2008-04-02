/*
 * $Id: FacetTag.java,v 1.8 2003/05/02 05:04:55 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.webapp;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;


/**
 * <p><strong>FacetTag</strong> is the JSP mechanism for denoting a
 * {@link javax.faces.component.UIComponent} is to be added as a
 * <code>facet</code> to the component associated with its parent.</p>
 *
 * <p>A <strong>FacetTag</strong> must have one and only one tag
 * child.  This child must be a {@link UIComponentTag} instance representing
 * a single {@link javax.faces.component.UIComponent} instance.</p>
 */

public class FacetTag extends TagSupport {


    // ------------------------------------------------- Instance Variables


    /**
     * <p>The number of child component tags that have been nested
     * inside this tag.</p>
     */
    protected int children = 0;


    // ------------------------------------------------------------- Properties


    /**
     * The name of this facet.  This will be used as the facet name for
     * our <code>UIComponentTag</code> child in our <code>UIComponentTag</code>
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


}
