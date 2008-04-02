/*
 * $Id: UIComponentTagBase.java,v 1.3 2005/05/05 20:51:14 edburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.webapp;

import javax.el.ELContext;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.servlet.jsp.tagext.JspTag;

/**
 * <p><strong><code>UIComponentTagBase</code></strong> is the base class
 * for all JSP tags that correspond to a {@link
 * javax.faces.component.UIComponent} instance in the view.  This base
 * class allows a single view to be described in a JSP page consisting
 * of both {@link UIComponentELTag} and {@link UIComponentTag}
 * instances.</p>
 */

public abstract class UIComponentTagBase extends Object implements JspTag {

    /**
     * <p>Return the {@link FacesContext} instance for the current
     * request.  This value will be non-<code>null</code> only from the
     * beginning of <code>doStartTag()</code> through the end of
     * <code>doEndTag()</code> for each tag instance.</p>
     */
    protected abstract FacesContext getFacesContext();

    /**
     * <p>Return the {@link ELContext} for the {@link FacesContext} for
     * this request.</p>
     *
     * <p>This is a convenience for
     * <code>getFacesContext().getELContext()</code>.</p>
     */

    protected ELContext getELContext() {
	FacesContext fc = getFacesContext();
	ELContext result = null;
	if (null != fc) {
	    result = fc.getELContext();
	}
	return result;
    }


    /**
     * <p>Add the component identifier of the specified {@link UIComponent}
     * to the list of component identifiers created or located by nested
     * {@link UIComponentTag}s processing this request.</p>
     *
     * @param child New child whose identifier should be added
     */
    protected abstract void addChild(UIComponent child);

    /**
     * <p>Add the facet name of the specified facet to the list of
     * facet names created or located by nested {@link UIComponentTag}s
     * processing this request.</p>
     *
     * @param name Facet name to be added
     */
    protected abstract void addFacet(String name);

    /**
     * <p>Set the component identifier for the component corresponding
     * to this tag instance.  If the argument begins with {@link
     * javax.faces.component.UIViewRoot#UNIQUE_ID_PREFIX} throw an
     * <code>IllegalArgumentException</code></p>
     *
     * @param id The new component identifier.  This may not start with
     * {@link javax.faces.component.UIViewRoot#UNIQUE_ID_PREFIX}.
     *
     * @exception IllegalArgumentException if the argument is
     * non-<code>null</code> and starts with {@link
     * javax.faces.component.UIViewRoot#UNIQUE_ID_PREFIX}.
     */
    public abstract void setId(String id);

    /**
     * <p>Return the component type for the component that is or will be
     * bound to this tag.  This value can be passed to
     * {@link javax.faces.application.Application#createComponent} to create
     * the {@link UIComponent} instance for this tag.  Subclasses must
     * override this method to return the appropriate value.</p>
     */
    public abstract String getComponentType();

    /**
     * <p>Return the <code>rendererType</code> property that selects the
     * <code>Renderer</code> to be used for encoding this component, or
     * <code>null</code> to ask the component to render itself directly.
     * Subclasses must override this method to return the appropriate value.
     * </p>
     */
    public abstract String getRendererType();

    /**
     * <p>Return the {@link UIComponent} instance that is associated with
     * this tag instance.  This method is designed to be used by tags nested
     * within this tag, and only returns useful results between the
     * execution of <code>doStartTag()</code> and <code>doEndTag()</code>
     * on this tag instance.</p>
     */
    public abstract UIComponent getComponentInstance();

    /**
     * <p>Return <code>true</code> if we dynamically created a new component
     * instance during execution of this tag.  This method is designed to be
     * used by tags nested within this tag, and only returns useful results
     * between the execution of <code>doStartTag()</code> and
     * <code>doEndTag()</code> on this tag instance.</p>
     */
    public abstract boolean getCreated();

    /**
     * <p>Return the index of the next child to be added as a child of
     * this tag.  The default implementation maintains a list of created
     * components and returns the size of the list.</p>
     */

    protected abstract int getIndex();

}
