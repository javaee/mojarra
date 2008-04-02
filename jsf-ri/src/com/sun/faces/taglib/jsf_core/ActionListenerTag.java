/*
 * $Id: ActionListenerTag.java,v 1.35 2006/12/18 18:58:15 rlubke Exp $
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

import javax.el.ValueExpression;
import javax.faces.component.ActionSource;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionListener;
import javax.faces.event.ActionEvent;
import javax.faces.event.AbortProcessingException;
import javax.faces.webapp.UIComponentClassicTagBase;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.Serializable;

import com.sun.faces.util.MessageUtils;
import com.sun.faces.util.Util;


/**
 * <p>Tag implementation that creates a {@link ActionListener} instance
 * and registers it on the {@link UIComponent} associated with our most
 * immediate surrounding instance of a tag whose implementation class
 * is a subclass of {@link UIComponentClassicTagBase}.  This tag creates no output to
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

        // Locate our parent UIComponentTag
        UIComponentClassicTagBase tag =
             UIComponentClassicTagBase.getParentUIComponentClassicTagBase(pageContext);
        if (tag == null) {
            Object params[] = {this.getClass().getName()};
            throw new JspException(
                 MessageUtils.getExceptionMessageString(
                      MessageUtils.NOT_NESTED_IN_FACES_TAG_ERROR_MESSAGE_ID, params));
        }

        // Nothing to do unless this tag created a component
        if (!tag.getCreated()) {
            return (SKIP_BODY);
        }

        UIComponent component = tag.getComponentInstance();
        if (component == null) {
            throw new JspException(
                 MessageUtils.getExceptionMessageString(MessageUtils.NULL_COMPONENT_ERROR_MESSAGE_ID));
        }
        if (!(component instanceof ActionSource)) {           
            throw new JspException(
                 MessageUtils.getExceptionMessageString(
                      MessageUtils.NOT_NESTED_IN_TYPE_TAG_ERROR_MESSAGE_ID,
                      "actionListener",
                      "javax.faces.component.ActionSource"));
        }

        // If binding is null, type is set and is a literal value,
        // then don't bother wrapping.  Just instantiate and
        // set.
        ActionListener listener;
        if (binding == null && type != null && type.isLiteralText()) {
            try {
                listener = (ActionListener)
                     Util.getListenerInstance(type, null);
            } catch (Exception e) {
                throw new JspException(e.getMessage(), e.getCause());
            }
        } else {
            listener = new BindingActionListener(type, binding);
        }
        
        ((ActionSource) component).addActionListener(listener);

        return (SKIP_BODY);

    }


    /**
     * <p>Release references to any acquired resources.
     */
    public void release() {

        this.type = null;

    }


    // ----------------------------------------------------------- Inner Classes


    private static class BindingActionListener
         implements ActionListener, Serializable {

        private transient ActionListener instance;
        private ValueExpression type;
        private ValueExpression binding;

        // -------------------------------------------------------- Constructors


        public BindingActionListener(ValueExpression type,
                                     ValueExpression binding) {

            this.type = type;
            this.binding = binding;

        }

        // ----------------------------------------- Methods from ActionListener


        /**
         * PENDING
         *
         * @param event The {@link javax.faces.event.ActionEvent} that has occurred
         * @throws javax.faces.event.AbortProcessingException
         *          Signal the JavaServer Faces
         *          implementation that no further processing on the current event
         *          should be performed
         */
        public void processAction(ActionEvent event) throws AbortProcessingException {           

            if (instance == null) {
                instance = (ActionListener)
                     Util.getListenerInstance(type, binding);
            }
            if (instance != null) {
                instance.processAction(event);
            } else {
                if (logger.isLoggable(Level.WARNING)) {
                    logger.log(Level.WARNING,
                               "jsf.core.taglib.action_or_valuechange_listener.null_type_binding",
                               new Object[] {
                                "ActionListener",
                                event.getComponent().getClientId(FacesContext.getCurrentInstance())});
                }
            }
        }
    }
    
}
