/*
 * $Id: ActionListenerTag.java,v 1.30 2006/09/05 23:29:27 rlubke Exp $
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

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.component.ActionSource;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionListener;
import javax.faces.webapp.UIComponentClassicTagBase;
import javax.faces.webapp.UIComponentELTag;
import javax.faces.webapp.UIComponentTag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.faces.util.MessageUtils;
import com.sun.faces.util.Util;


/**
 * <p>Tag implementation that creates a {@link ActionListener} instance
 * and registers it on the {@link UIComponent} associated with our most
 * immediate surrounding instance of a tag whose implementation class
 * is a subclass of {@link UIComponentTag}.  This tag creates no output to
 * the page currently being created.</p>
 * <p/>
 * <p>This class may be used directly to implement a generic event handler
 * registration tag (based on the fully qualified Java class name specified
 * by the <code>type</code> attribute), or as a base class for tag instances
 * that support specific {@link ActionListener} subclasses.</p>
 * <p/>
 * <p>Subclasses of this class must implement the
 * <code>createActionListener()</code> method, which creates and returns a
 * {@link ActionListener} instance.  Any configuration properties that
 * are required by this {@link ActionListener} instance must have been
 * set by the <code>createActionListener()</code> method.  Generally,
 * this occurs by copying corresponding attribute values on the tag
 * instance.</p>
 * <p/>
 * <p>This tag creates no output to the page currently being created.  It
 * is used solely for the side effect of {@link ActionListener}
 * creation.</p>
 */

public class ActionListenerTag extends TagSupport {


    // ------------------------------------------------------------- Attributes

    private static final long serialVersionUID = -5222351612904952740L;
    private static final Logger logger = 
            Util.getLogger(Util.FACES_LOGGER + Util.TAGLIB_LOGGER);
    /**
     * <p>The fully qualified class name of the {@link ActionListener}
     * instance to be created.</p>
     */
    private ValueExpression type = null;

    /**
     * <p>The value expression used to create a listener instance and it is 
     * also used to wire up this listener to an {@link ActionListener} property 
     * of a JavaBean class.</p>
     */
    private ValueExpression binding = null;

    /**
     * <p>Set the fully qualified class name of the
     * {@link ActionListener} instance to be created.
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
     * <p>Create a new instance of the specified {@link ActionListener}
     * class, and register it with the {@link UIComponent} instance associated
     * with our most immediately surrounding {@link UIComponentClassicTagBase}
     * instance, if the {@link UIComponent} instance was created by this
     * execution of the containing JSP page.</p>
     *
     * @throws JspException if a JSP error occurs
     */
    public int doStartTag() throws JspException {

        ActionListener handler = null;

        // Locate our parent UIComponentTag
        UIComponentClassicTagBase tag =
            UIComponentELTag.getParentUIComponentClassicTagBase(pageContext);
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
        FacesContext context = FacesContext.getCurrentInstance();

        UIComponent component = tag.getComponentInstance();
        if (component == null) {
            throw new JspException(
                MessageUtils.getExceptionMessageString(MessageUtils.NULL_COMPONENT_ERROR_MESSAGE_ID));
        }
        if (!(component instanceof ActionSource)) {
            Object params [] = {"actionListener", "javax.faces.component.ActionSource"};
            throw new JspException(
                MessageUtils.getExceptionMessageString(
                    MessageUtils.NOT_NESTED_IN_TYPE_TAG_ERROR_MESSAGE_ID, params));
        }
        
        // If "binding" is set, use it to create a listener instance.
        
        if (null != binding) {
	    try {
		handler = (ActionListener)binding.getValue(context.getELContext());
		if (handler != null) {
		    // we ignore the type in this case, even though
		    // it may have been set.
		    ((ActionSource)component).addActionListener(handler);
		    return (SKIP_BODY);
		}
	    } catch (ELException e) {
		throw new JspException(e);
	    }
	}
        // If "type" is set, use it to create the listener
        // instance.  If "type" and "binding" are both set, store the 
        // listener instance in the value of the property represented by
        // the value binding expression.
        if (type != null) {
            handler = createActionListener(context);
            if (handler != null && binding != null) {
		try {
		    binding.setValue(context.getELContext(), handler);
		} catch (ELException e) {
		    throw new JspException(e);
		}
            }
        }
	
        // We need to cast here because addActionListener
        // method does not apply to all components (it is not a method on
        // UIComponent/UIComponentBase).
        if (handler != null) {
            ((ActionSource)component).addActionListener(handler);
        } else {
            if (logger.isLoggable(Level.FINE)) {
                if (binding == null && type == null) {
                    logger.fine("'handler' was not created because both 'binding' and 'type' were null.");
                } else {
                    logger.fine("'handler' was not created.");
                }
            }
        }

               
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
     * <p>Create and return a new {@link ActionListener} to be registered
     * on our surrounding {@link UIComponent}.</p>
     *
     * @throws JspException if a new instance cannot be created
     */
    protected ActionListener createActionListener(FacesContext context)
        throws JspException {

        try {
	    // PENDING(edburns): log potential NPE with a good error
	    // message.
	    String className = 
		type.getValue(context.getELContext()).toString();
	    
            Class clazz = Util.loadClass(className, this);
            return ((ActionListener) clazz.newInstance());
        } catch (Exception e) {
            throw new JspException(e);
        }

    }
}
