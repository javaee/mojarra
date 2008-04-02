/*
 * $Id: UIComponentTag.java,v 1.55 2005/04/21 18:55:31 edburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.webapp;


import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

import java.util.List;

/**
 * <p>{@link UIComponentTag} is the base class for all JSP custom
 * actions that correspond to user interface components in a page that is
 * rendered by JavaServer Faces.</p>
 *
 * <p>In this version of the specification, <code>UIComponentTag</code>
 * extends {@link UIComponentClassicTagBase} to add properties that use
 * the Faces 1.1 Expression Language.</p>
 *
 * @deprecated Use of this class has been replaced with {@link
 * UIComponentELTag}, which extends
 * <code>UIComponentClassicTagBase</code> to add properties that use the
 * EL API introduced as part of JSP 2.1.
 */

public abstract class UIComponentTag extends UIComponentClassicTagBase implements Tag {

    // ------------------------------------------------------------- Properties

    /**
     * <p>The value binding expression (if any) used to wire up this component
     * to a {@link UIComponent} property of a JavaBean class.</p>
     */
    private String binding = null;


    /**
     * <p>Set the value binding expression for our component.</p>
     *
     * @param binding The new value binding expression
     *
     * @exception IllegalArgumentException if the specified binding is not a
     * valid value binding expression.
     */
    public void setBinding(String binding) throws JspException {
	if (!isValueReference(binding)) {
	    throw new IllegalArgumentException();
        }

	this.binding = binding;
    }

    protected boolean hasBinding() {
	return null != binding;
    }



    /**
     * <p>An override for the rendered attribute associated with our
     * {@link UIComponent}.</p>
     */
    private String rendered = null;


    /**
     * <p>Set an override for the rendered attribute.</p>
     *
     * @param rendered The new value for rendered attribute
     */
    public void setRendered(String rendered) {

        this.rendered = rendered;

    }


    /**
     * <p>Flag indicating whether or not rendering should occur.</p>
     */
    private boolean suppressed = false;


    protected boolean isSuppressed() {

        return (suppressed);

    }


    /**
     * <p>Return <code>true</code> if the specified value conforms to the
     * syntax requirements of a value binding expression.  Such expressions
`     * may be used on most component tag attributes to signal a desire for
     * deferred evaluation of the attribute or property value to be set on
     * the underlying {@link UIComponent}.</p>
     *
     * @param value The value to evaluate
     *
     * @exception NullPointerException if <code>value</code> is
     *  <code>null</code>
     */
    public static boolean isValueReference(String value) {

	if (value == null) {
	    throw new NullPointerException();
	}
        if ((value.indexOf("#{") != -1) &&
            (value.indexOf("#{") < value.indexOf('}'))) {
            return true;
        }
        return false;

    }

    // ------------------------------------------ Methods from Tag

    /**
     * <p>Release any resources allocated during the execution of this
     * tag handler.</p>
     */
    public void release() {
	
	this.suppressed = false;
	this.binding = null;
        this.rendered = null;
	super.release();
    }


    // ----------------  Concrete Implementations of methods from superclass

    /**
     * @param component {@inheritDoc} 
     */
    protected void setProperties(UIComponent component) {
        // The "id" property is explicitly set when components are created
        // so it does not need to be set here
        if (rendered != null) {
	    if (isValueReference(rendered)) {
		ValueBinding vb =
		    getFacesContext().getApplication().createValueBinding(rendered);
		component.setValueBinding("rendered", vb);
	    } else {
		component.setRendered(Boolean.valueOf(rendered).booleanValue());
	    }
        }
	if (getRendererType() != null) {
	    component.setRendererType(getRendererType());
	}

    }





    /**
     * <p>Implement <code>createComponent</code> using Faces 1.1 EL
     * API.</p>
     * 
     * @param context {@inheritDoc} 
     * @param newId {@inheritDoc}
     */
    protected UIComponent createComponent(FacesContext context, String newId) {
        UIComponent component = null;
        Application application = context.getApplication();
        if (binding != null) {
            ValueBinding vb = application.createValueBinding(binding);
            component = application.createComponent(vb, context,
                                                    getComponentType());
	    component.setValueBinding("binding", vb);
        } else {
            component = application.createComponent(getComponentType());
        }

        component.setId(newId);
        setProperties(component);

        return component;
    }


    // Tag tree navigation

    /**
     * <p>Locate and return the nearest enclosing {@link UIComponentTag}
     * if any; otherwise, return <code>null</code>.</p>
     *
     * @param context <code>PageContext</code> for the current page
     */
    public static UIComponentTag getParentUIComponentTag(PageContext context) {

	// PENDING(): this method may have to wrap the returned thing in
	// an instance of UIComponentTag if it is not an instance of
	// UIComponentTag.

	UIComponentClassicTagBase result = 
	    getParentUIComponentClassicTagBase(context);
	return ((UIComponentTag)result);

    }




}
