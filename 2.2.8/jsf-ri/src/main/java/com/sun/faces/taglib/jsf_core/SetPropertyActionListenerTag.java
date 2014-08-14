/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package com.sun.faces.taglib.jsf_core;

import javax.el.ValueExpression;
import javax.faces.component.ActionSource;
import javax.faces.component.UIComponent;
import javax.faces.event.ActionListener;
import javax.faces.webapp.UIComponentClassicTagBase;
import javax.faces.webapp.UIComponentELTag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;


import com.sun.faces.util.MessageUtils;


/**
 * <p>Tag implementation that creates a special {@link ActionListener} instance
 * and registers it on the {@link ActionSource} associated with our most
 * immediate surrounding instance of a tag whose implementation class
 * is a subclass of {@link UIComponentClassicTagBase}.  This tag creates no output to
 * the page currently being created.</p>
 * <p/>
 * <p>The ActionListener instance created and installed by this tag has the 
 * following behavior and contract.</p>
 *
 * <ul>
 *
 * <li>Only create and register the <code>ActionListener</code> instance
 * the first time the component for this tag is created</li>
 *
 * <li>The "target" and "value" tag attributes are ValueExpression
 * instances and are stored unevaluated as instance variables of the
 * listener.</li>
 *
 * <li>When the listener executes, call getValue() on the "value"
 * ValueExpression.  Pass the result to a call to setValue() on the
 * "target" ValueExpression</li>
 *
 * </ul>
 *
 * <p>This tag creates no output to the page currently being created.  It
 * is used solely for the side effect of {@link ActionListener}
 * creation.</p>
 */

public class SetPropertyActionListenerTag extends TagSupport {


    // ------------------------------------------------------------- Attributes

    static final long serialVersionUID = 7966883942522780374L;

    /**
     * <p>The target of the value attribute.</p>
     */
    private ValueExpression target = null;

    /**
     * <p>The value that is set into the target attribute.</p>
     */
    private ValueExpression value = null;

    /**
     * <p>Setter for the target attribute</p>
     *
     * @param target The new class name
     */
    public void setTarget(ValueExpression target) {

        this.target = target;

    }

    /*
     * <p>Setter for the value attribute</p>
     *
     * @param value The new value value expression
     *
     * @throws JspException if a JSP error occurs
     */
    public void setValue(ValueExpression value) {
    this.value = value;
    }

    // --------------------------------------------------------- Public Methods


    /**
     * <p>Create a new instance of the {@link ActionListener}
     * class, and register it with the {@link UIComponent} instance associated
     * with our most immediately surrounding {@link UIComponentClassicTagBase}
     * instance.  The behavior of the {@link ActionListener} must conform 
     * to the class description.</p>
     *
     * @throws JspException if a JSP error occurs
     */
    public int doStartTag() throws JspException {

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

        UIComponent component = tag.getComponentInstance();
        if (component == null) {
            throw new JspException(
                MessageUtils.getExceptionMessageString(MessageUtils.NULL_COMPONENT_ERROR_MESSAGE_ID));
        }
        if (!(component instanceof ActionSource)) {
            Object params [] = {"setPropertyActionListener", "javax.faces.component.ActionSource"};
            throw new JspException(
                MessageUtils.getExceptionMessageString(
                    MessageUtils.NOT_NESTED_IN_TYPE_TAG_ERROR_MESSAGE_ID, params));
        }

        ActionListener handler = new SetPropertyActionListenerImpl(target, value);
        ((ActionSource) component).addActionListener(handler);

        return (SKIP_BODY);

    }


    /**
     * <p>Release references to any acquired resources.
     */
    public void release() {

        this.value = null;
        this.target = null;

    }



}
