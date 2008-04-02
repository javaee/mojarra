/*
 * $Id: ValueChangeListenerTag.java,v 1.9 2004/12/02 18:42:24 rogerk Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.jsf_core;


import com.sun.faces.util.Util;

import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.event.ValueChangeListener;
import javax.faces.webapp.UIComponentTag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;


/**
 * <p>Tag implementation that creates a {@link ValueChangeListener} instance
 * and registers it on the {@link UIComponent} associated with our most
 * immediate surrounding instance of a tag whose implementation class
 * is a subclass of {@link UIComponentTag}.  This tag creates no output to the
 * page currently being created.</p>
 * <p/>
 * <p>This class may be used directly to implement a generic event handler
 * registration tag (based on the fully qualified Java class name specified
 * by the <code>type</code> attribute), or as a base class for tag instances
 * that support specific {@link ValueChangeListener} subclasses.</p>
 * <p/>
 * <p>Subclasses of this class must implement the
 * <code>createValueChangeListener()</code> method, which creates and returns a
 * {@link ValueChangeListener} instance.  Any configuration properties that
 * are required by this {@link ValueChangeListener} instance must have been
 * set by the <code>createValueChangeListener()</code> method.  Generally,
 * this occurs by copying corresponding attribute values on the tag
 * instance.</p>
 * <p/>
 * <p>This tag creates no output to the page currently being created.  It
 * is used solely for the side effect of {@link ValueChangeListener}
 * creation.</p>
 */

public class ValueChangeListenerTag extends TagSupport {


    // ------------------------------------------------------------- Attributes


    /**
     * <p>The fully qualified class name of the {@link ValueChangeListener}
     * instance to be created.</p>
     */
    private String type = null;
    private String type_ = null;

    /**
     * <p>The value binding expression used to create a listener instance and it is 
     * also used to wire up this listener to an {@link ValueChangeListener} property 
     * of a JavaBean class.</p>
     */
    private String binding= null;
    private String binding_ = null;

    /**
     * <p>Set the fully qualified class name of the
     * {@link ValueChangeListener} instance to be created.
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
     * <p>Create a new instance of the specified {@link ValueChangeListener}
     * class, and register it with the {@link UIComponent} instance associated
     * with our most immediately surrounding {@link UIComponentTag} instance, if
     * the {@link UIComponent} instance was created by this execution of the
     * containing JSP page.</p>
     *
     * @throws JspException if a JSP error occurs
     */
    public int doStartTag() throws JspException {

        ValueChangeListener handler = null;
        
        // Refers to the handler that could not be created.
        String handlerError = null;
        
        // Locate our parent UIComponentTag
        UIComponentTag tag =
            UIComponentTag.getParentUIComponentTag(pageContext);
        if (tag == null) {
            Object params [] = {this.getClass().getName()};
            throw new JspException(
                Util.getExceptionMessageString(
                    Util.NOT_NESTED_IN_FACES_TAG_ERROR_MESSAGE_ID));
        }

        // Nothing to do unless this tag created a component
        if (!tag.getCreated()) {
            return (SKIP_BODY);
        }

        UIComponent component = tag.getComponentInstance();
        if (component == null) {
            throw new JspException(
                Util.getExceptionMessageString(Util.NULL_COMPONENT_ERROR_MESSAGE_ID));
        }
        if (!(component instanceof EditableValueHolder)) {
            Object params [] = {this.getClass().getName()};
            throw new JspException(
                Util.getExceptionMessageString(
                    Util.NOT_NESTED_IN_TYPE_TAG_ERROR_MESSAGE_ID, params));
        }
        
        // If "binding" is set, use it to create a listener instance.

        FacesContext context = FacesContext.getCurrentInstance();
        ValueBinding vb = null;
        if (binding_ != null) {
            handlerError = binding_;
            vb = Util.getValueBinding(binding_);
            if (vb != null) {
                try {
                    handler = (ValueChangeListener)vb.getValue(context);
                    if (handler != null) {
                        ((EditableValueHolder)component).addValueChangeListener(handler);
                        return (SKIP_BODY);
                    }
                } catch (Exception e) {
                    throw new JspException(e);
                }
            }
        } 
        // If "type" is set, use it to create the listener
        // instance.  If "type" and "binding" are both set, store the 
        // listener instance in the value of the property represented by
        // the value binding expression.    
        if (type_ != null) {
            handlerError = type_;
            type = (String) Util.evaluateVBExpression(type_);
            handler = createValueChangeListener();
            if (handler != null) {
                if (vb != null) {
                    try {
                        vb.setValue(context, handler);
                    } catch (Exception e) {
                        throw new JspException(e);
                    }
                }
            }
        }
              
        if (handler == null) {
            Object params [] = {"javax.faces.event.ValueChangeListener", handlerError};
            throw new JspException(
                Util.getExceptionMessageString(
                    Util.CANT_CREATE_CLASS_ERROR_ID, params));
        }
        
        // We need to cast here because addValueChangeListener
        // method does not apply to all components (it is not a method on
        // UIComponent/UIComponentBase).
        ((EditableValueHolder) component).addValueChangeListener(handler);

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
     * <p>Create and return a new {@link ValueChangeListener} to be registered
     * on our surrounding {@link UIComponent}.</p>
     *
     * @throws JspException if a new instance cannot be created
     */
    protected ValueChangeListener createValueChangeListener()
        throws JspException {

        try {
            Class clazz = Util.loadClass(type, this);
            return ((ValueChangeListener) clazz.newInstance());
        } catch (Exception e) {
            throw new JspException(e);
        }

    }

}
