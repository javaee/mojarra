/*
 * $Id: PhaseListenerTag.java,v 1.1 2004/12/08 15:10:26 edburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.jsf_core;

import com.sun.faces.util.Util;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.event.PhaseListener;
import javax.faces.webapp.UIComponentTag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.tagext.Tag;


/**
 * <p>Tag implementation that creates a {@link PhaseListener} instance
 * and registers it on the {@link UIViewRoot} associated with our most
 * immediate surrounding instance of a tag whose component
 * is an instance of {@link UIViewRoot}.  This tag creates no output to the
 * page currently being created.</p>
 * <p/>
 */

public class PhaseListenerTag extends TagSupport {


    // ------------------------------------------------------------- Attributes


    /**
     * <p>The fully qualified class name of the {@link PhaseListener}
     * instance to be created.</p>
     */
    private String type = null;
    private String type_ = null;

    /**
     * <p>The value binding expression used to create a listener instance and it is 
     * also used to wire up this listener to an {@link PhaseListener} property 
     * of a JavaBean class.</p>
     */
    private String binding= null;
    private String binding_ = null;

    /**
     * <p>Set the fully qualified class name of the
     * {@link PhaseListener} instance to be created.
     *
     * @param type The new class name
     */
    public void setType(String type) {

        this.type_ = type;

    }

    /*
     * <p>Set the value binding expression  for this listener.</p>
     *
     * @param binding The new value binding expression
     *
     * @throws JspException if a JSP error occurs
     */
    public void setBinding(String binding) 
        throws JspException {
        if (binding != null && !Util.isValueReference(binding)) {
            Object[] params = {binding};
            throw new JspException(
                Util.getExceptionMessageString(Util.INVALID_EXPRESSION_ID, params));
        }
        this.binding_ = binding;
    }

    // --------------------------------------------------------- Public Methods


    /**
     * <p>Create a new instance of the specified {@link PhaseListener}
     * class, and register it with the {@link UIComponent} instance associated
     * with our most immediately surrounding {@link UIComponentTag} instance, if
     * the {@link UIComponent} instance was created by this execution of the
     * containing JSP page.</p>
     *
     * @throws JspException if a JSP error occurs
     */
    public int doStartTag() throws JspException {

        PhaseListener handler = null;

        // Refers to the handler that could not be created.
        String handlerError = null;

	// find the viewTag
	Tag parent = this;
	UIComponentTag tag = null;
	while (null != (parent = parent.getParent())) {
	    if (parent instanceof UIComponentTag) {
		tag = (UIComponentTag) parent;
	    }
	}

        if (tag == null) {
            Object params [] = {this.getClass().getName()};
            throw new JspException(
                Util.getExceptionMessageString(
                    Util.NOT_NESTED_IN_FACES_TAG_ERROR_MESSAGE_ID, params));
        }
        
        // Nothing to do unless this tag created a component
        if (!tag.getCreated()) {
            return (SKIP_BODY);
        }
        
        UIViewRoot viewRoot = (UIViewRoot) tag.getComponentInstance();
        if (viewRoot == null) {
            throw new JspException(
                Util.getExceptionMessageString(Util.NULL_COMPONENT_ERROR_MESSAGE_ID));
        }
        
        // If "binding" is set use it to create a listener instance.
        
        FacesContext context = FacesContext.getCurrentInstance();
        ValueBinding vb = null;
        if (null != binding_) {
            handlerError = binding_;
            vb = Util.getValueBinding(binding_);
            if (vb != null) {
                try {
                    handler = (PhaseListener)vb.getValue(context);
                    if (handler != null) {
			// we ignore the type in this case, even though
			// it may have been set.
                        viewRoot.addPhaseListener(handler);
                        return (SKIP_BODY);
                    }
                } catch (Exception e) {
                    throw new JspException(e);
                }
            }
        }
        // If "type" is set, use it to create the listener
        // instance.  

        if (null != type_) {
            handlerError = type_;
            type = (String) Util.evaluateVBExpression(type_);
            handler = createPhaseListener();
            if (handler != null) {
		if (vb != null) {
		    // If "type" and "binding" are both set, store the listener
		    // instance in the value of the property represented by the
		    // value binding expression.
		    
		    try {
			vb.setValue(context, handler);
		    } catch (Exception e) {
			throw new JspException(e);
		    }
                }
            }
        }
       
        if (handler == null) {
            Object params [] = {"javax.faces.event.PhaseListener",handlerError};
            throw new JspException(
                Util.getExceptionMessageString(
                    Util.CANT_CREATE_CLASS_ERROR_ID, params));
        }
        
        // We need to cast here because addPhaseListener
        // method does not apply to all components (it is not a method on
        // UIComponent/UIComponentBase).
        viewRoot.addPhaseListener(handler);
               
        return (SKIP_BODY);

    }


    /**
     * <p>Release references to any acquired resources.
     */
    public void release() {

        this.type = null;

    }


    // ------------------------------------------------------ Protected Methods


    /**
     * <p>Create and return a new {@link PhaseListener} to be registered
     * on our surrounding {@link UIComponent}.</p>
     *
     * @throws JspException if a new instance cannot be created
     */
    protected PhaseListener createPhaseListener()
        throws JspException {

        try {
            Class clazz = Util.loadClass(type, this);
            return ((PhaseListener) clazz.newInstance());
        } catch (Exception e) {
            throw new JspException(e);
        }

    }
}
