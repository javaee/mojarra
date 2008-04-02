/*
 * $Id: ValueChangedListenerTag.java,v 1.7 2003/10/07 20:16:05 horwat Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.jsf_core;


import com.sun.faces.util.Util;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UISelectBoolean;
import javax.faces.component.UISelectOne;
import javax.faces.component.UISelectMany;
import javax.faces.event.ValueChangedListener;
import javax.faces.webapp.UIComponentTag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;


/**
 * <p>Tag implementation that creates a {@link ValueChangedListener} instance
 * and registers it on the {@link UIComponent} associated with our most
 * immediate surrounding instance of a tag whose implementation class
 * is a subclass of {@link UIComponentTag}.  This tag creates no output to the
 * page currently being created.</p>
 *
 * <p>This class may be used directly to implement a generic event handler
 * registration tag (based on the fully qualified Java class name specified
 * by the <code>type</code> attribute), or as a base class for tag instances
 * that support specific {@link ValueChangedListener} subclasses.</p>
 *
 * <p>Subclasses of this class must implement the
 * <code>createValueChangedListener()</code> method, which creates and returns a
 * {@link ValueChangedListener} instance.  Any configuration properties that
 * are required by this {@link ValueChangedListener} instance must have been
 * set by the <code>createValueChangedListener()</code> method.  Generally, 
 * this occurs by copying corresponding attribute values on the tag 
 * instance.</p>
 *
 * <p>This tag creates no output to the page currently being created.  It
 * is used solely for the side effect of {@link ValueChangedListener}
 * creation.</p>
 */

public class ValueChangedListenerTag extends TagSupport {


    // ------------------------------------------------------------- Attributes


    /**
     * <p>The fully qualified class name of the {@link ValueChangedListener}
     * instance to be created.</p>
     */
    private String type = null;
    private String type_ = null;


    /**
     * <p>Set the fully qualified class name of the
     * {@link ValueChangedListener} instance to be created.
     *
     * @param type The new class name
     */
    public void setType(String type) {

        this.type = type;

    }


    // --------------------------------------------------------- Public Methods


    /**
     * <p>Create a new instance of the specified {@link ValueChangedListener}
     * class, and register it with the {@link UIComponent} instance associated
     * with our most immediately surrounding {@link UIComponentTag} instance, if
     * the {@link UIComponent} instance was created by this execution of the
     * containing JSP page.</p>
     *
     * @exception JspException if a JSP error occurs
     */
    public int doStartTag() throws JspException {
        // evaluate any expressions that we were passed
        evaluateExpressions();

        // Locate our parent UIComponentTag
        UIComponentTag tag =
            UIComponentTag.getParentUIComponentTag(pageContext);
        if (tag == null) { 
            throw new JspException(Util.getExceptionMessage(Util.NOT_NESTED_IN_FACES_TAG_ERROR_MESSAGE_ID));
        }

        // Nothing to do unless this tag created a component
        if (!tag.getCreated()) {
            return (SKIP_BODY);
        }

        // Create and register an instance with the appropriate component
        //We need to cast here because addValueChangeListener
        //method does not apply to al components (it is not a method on
        //UIComponent/UIComponentBase).

        ValueChangedListener handler = createValueChangedListener();
        UIComponent component = tag.getComponent();
        if (component == null) {
            throw new JspException(Util.getExceptionMessage(Util.NULL_COMPONENT_ERROR_MESSAGE_ID));
        }
        if (component instanceof UIInput) {
            ((UIInput)component).addValueChangedListener(handler);
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
     * <p>Create and return a new {@link ValueChangedListener} to be registered
     * on our surrounding {@link UIComponent}.</p>
     *
     * @exception JspException if a new instance cannot be created
     */
    protected ValueChangedListener createValueChangedListener()
        throws JspException {

        try {
            Class clazz = Util.loadClass(type, this);
            return ((ValueChangedListener) clazz.newInstance());
        } catch (Exception e) {
            throw new JspException(e);
        }

    }

    /* Evaluates expressions as necessary */
    private void evaluateExpressions() throws JspException {
        if (type_ != null) {
            type = Util.evaluateElExpression(type_, pageContext);
 	}
    }

}
