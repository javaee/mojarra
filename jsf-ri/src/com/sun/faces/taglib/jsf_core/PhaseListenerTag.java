/*
 * $Id: PhaseListenerTag.java,v 1.13 2006/12/18 18:58:15 rlubke Exp $
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

import com.sun.faces.util.MessageUtils;
import com.sun.faces.util.Util;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.faces.webapp.UIComponentELTag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>Tag implementation that creates a {@link PhaseListener} instance
 * and registers it on the {@link UIViewRoot} associated with our most
 * immediate surrounding instance of a tag whose component
 * is an instance of {@link UIViewRoot}.  This tag creates no output to the
 * page currently being created.</p>
 * <p/>
 */

public class PhaseListenerTag extends TagSupport {


    private static final Logger logger =
         Util.getLogger(Util.FACES_LOGGER + Util.TAGLIB_LOGGER);


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
    private ValueExpression binding = null;

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
     * with our most immediately surrounding {@link UIComponentELTag} instance, if
     * the {@link UIComponent} instance was created by this execution of the
     * containing JSP page.</p>
     *
     * @throws JspException if a JSP error occurs
     */
    public int doStartTag() throws JspException {

        // find the viewTag
        Tag parent = this;
        UIComponentELTag tag = null;
        while (null != (parent = parent.getParent())) {
            if (parent instanceof UIComponentELTag) {
                tag = (UIComponentELTag) parent;
            }
        }

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

        UIViewRoot viewRoot = (UIViewRoot) tag.getComponentInstance();
        if (viewRoot == null) {
            throw new JspException(
                 MessageUtils.getExceptionMessageString(MessageUtils.NULL_COMPONENT_ERROR_MESSAGE_ID));
        }

        // If binding is null, type is set and is a literal value,
        // then don't bother wrapping.  Just instantiate and
        // set.
        PhaseListener listener;
        if (binding == null && type != null && type.isLiteralText()) {
            try {
                listener = (PhaseListener)
                    Util.getListenerInstance(type, null);
            } catch (Exception e) {
                throw new JspException(e.getMessage(), e.getCause());
            }
        } else {
            listener = new BindingPhaseListener(type, binding);            
        }
        viewRoot.addPhaseListener(listener);

        return (SKIP_BODY);

    }


    /**
     * <p>Release references to any acquired resources.
     */
    public void release() {

        this.type = null;

    }


// ----------------------------------------------------------- Inner Classes


    private static class BindingPhaseListener
         implements PhaseListener, Serializable {

        private transient PhaseListener instance;
        private ValueExpression type;
        private ValueExpression binding;

        // -------------------------------------------------------- Constructors


        public BindingPhaseListener(ValueExpression type,
                                    ValueExpression binding) {

            this.type = type;
            this.binding = binding;

        }

        // ------------------------------------------ Methods from PhaseListener


        /**
         * <p>Handle a notification that the processing for a particular
         * phase has just been completed.</p>
         */
        public void afterPhase(PhaseEvent event) {

            PhaseListener listener = getPhaseListener();
            if (listener != null) {
                listener.afterPhase(event);
            }

        }

        /**
         * <p>Handle a notification that the processing for a particular
         * phase of the request processing lifecycle is about to begin.</p>
         */
        public void beforePhase(PhaseEvent event) {

            PhaseListener listener = getPhaseListener();
            if (listener != null) {
                listener.beforePhase(event);
            }

        }

        /**
         * <p>Return the identifier of the request processing phase during
         * which this listener is interested in processing {@link javax.faces.event.PhaseEvent}
         * events.  Legal values are the singleton instances defined by the
         * {@link javax.faces.event.PhaseId} class, including <code>PhaseId.ANY_PHASE</code>
         * to indicate an interest in being notified for all standard phases.</p>
         */
        public PhaseId getPhaseId() {

            PhaseListener listener = getPhaseListener();
            if (listener != null) {
                return listener.getPhaseId();
            }

            return null;

        }

        /**
         * <p>Invoked when the value change described by the specified
         * {@link javax.faces.event.ValueChangeEvent} occurs.</p>
         *
         * @return a <code>PhaseListener</code> instance
         * @throws javax.faces.event.AbortProcessingException
         *          Signal the JavaServer Faces
         *          implementation that no further processing on the current event
         *          should be performed
         */
        public PhaseListener getPhaseListener() throws AbortProcessingException {
            if (instance == null) {
                instance = (PhaseListener)
                     Util.getListenerInstance(type, binding);
            }
            if (instance != null) {
                return instance;
            } else {
                 if (logger.isLoggable(Level.WARNING)) {
                    // PENDING i18n
                    logger.warning("PhaseListener will not be processed - " +
                         "both 'binding' and 'type' are null");
                }
                return null;
            }
        }
    }

}
