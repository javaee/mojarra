/*
 * $Id: PhaseListenerTag.java,v 1.8 2006/08/24 12:00:12 rogerk Exp $
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

package com.sun.faces.taglib.jsf_core;

import com.sun.faces.util.Util;
import com.sun.faces.util.MessageUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseListener;
import javax.faces.webapp.UIComponentELTag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.tagext.Tag;

import javax.el.ValueExpression;
import javax.el.ELException;

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
    private ValueExpression type = null;

    /**
     * <p>The value binding expression used to create a listener
     * instance and it is also used to wire up this listener to an
     * {@link PhaseListener} property of a JavaBean class.</p>
     */
    private ValueExpression binding= null;

    /**
     * <p>Set the fully qualified class name of the
     * {@link PhaseListener} instance to be created.
     *
     * @param type The new class name
     */
    public void setType(ValueExpression type) {

        this.type = type;

    }

    /*
     * <p>Set the value binding expression  for this listener.</p>
     *
     * @param binding The new value binding expression
     */
    public void setBinding(ValueExpression binding) {
        this.binding = binding;
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
	ValueExpression handlerError = null;

	// find the viewTag
	Tag parent = this;
	UIComponentELTag tag = null;
	while (null != (parent = parent.getParent())) {
	    if (parent instanceof UIComponentELTag) {
		tag = (UIComponentELTag) parent;
	    }
	}

        if (tag == null) {
            Object params [] = {this.getClass().getName()};
            throw new JspException(
                MessageUtils.getExceptionMessageString(
                    MessageUtils.NOT_NESTED_IN_FACES_TAG_ERROR_MESSAGE_ID, params));
        }
        
        // Nothing to do unless this tag created a component
        if (!tag.getCreated()) {
            return (SKIP_BODY);
        }
        
        UIViewRoot viewRoot = (UIViewRoot) tag.getComponentInstance();
        if (viewRoot == null) {
            throw new JspException(
                MessageUtils.getExceptionMessageString(MessageUtils.NULL_COMPONENT_ERROR_MESSAGE_ID));
        }
        
        // If "binding" is set use it to create a listener instance.
        
        FacesContext context = FacesContext.getCurrentInstance();
        if (null != binding) {
	    handlerError = binding;
	    try {
		handler = 
		    (PhaseListener)binding.getValue(context.getELContext());
		if (handler != null) {
		    // we ignore the type in this case, even though
		    // it may have been set.
		    viewRoot.addPhaseListener(handler);
		    return (SKIP_BODY);
		}
	    } catch (ELException e) {
		throw new JspException(e);
	    }
	}
        // If "type" is set, use it to create the listener
        // instance.  

        if (null != type) {
	    handlerError = type;
            handler = createPhaseListener(context);
            if (handler != null) {
		if (binding != null) {
		    // If "type" and "binding" are both set, store the listener
		    // instance in the value of the property represented by the
		    // value binding expression.
		    
		    try {
			binding.setValue(context.getELContext(), handler);
		    } catch (ELException e) {
			throw new JspException(e);
		    }
                }
            }
        }
       
        if (handler == null) {
            Object params [] = {"javax.faces.event.PhaseListener",
				handlerError.getExpressionString()};
            throw new JspException(
                MessageUtils.getExceptionMessageString(
                    MessageUtils.CANT_CREATE_CLASS_ERROR_ID, params));
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
    protected PhaseListener createPhaseListener(FacesContext context)
        throws JspException {
	
        try {
	    String className = 
		type.getValue(context.getELContext()).toString();
	    
            Class clazz = Util.loadClass(className, this);
            return ((PhaseListener) clazz.newInstance());
        } catch (Exception e) {
            throw new JspException(e);
        }

    }
}
