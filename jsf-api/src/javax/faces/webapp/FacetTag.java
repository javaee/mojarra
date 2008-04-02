/*
 * $Id: FacetTag.java,v 1.1 2003/01/17 01:12:00 eburns Exp $
 */

/*
 * Copyright 2002-2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.webapp;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;


/**
 * <p><strong>FacetTag</strong> is the JSP mechanism for denoting a
 * UIComponent is to be added as a facet to its parent.</p>

 * <p>The <strong>FacetTag</strong> must have one and only one tag
 * child.  This child must be a <code>FacesTag</code> instance
 * representing a single <code>UIComponent</code> instance.</p>
 *
 * </p>
 */

public abstract class FacetTag extends BodyTagSupport {

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


    /**
     * <p>Release any resources allocated during the execution of this
     * tag handler.</p>
     */
    public void release() {

        super.release();
        this.name = null;
    }

    // ----------------------------------------------------- General Methods

    public void verifySingleChild() throws JspException {
	if (++children > 1) {
	    throw new JspException("Facet may have only one child");
	}
    }

}
