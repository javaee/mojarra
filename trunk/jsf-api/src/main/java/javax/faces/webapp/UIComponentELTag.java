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

package javax.faces.webapp;


import javax.el.ELException;
import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;


/**
 * <p>{@link UIComponentELTag} specializes its superclass to allow for
 * properties that take their values from EL API expressions.</p>
 *
 * <p>This tag is designed for use with Faces version 1.2 and JSP
 * version 2.1 containers.</p>
 *
 */

public abstract class UIComponentELTag extends UIComponentClassicTagBase implements Tag {


    // ------------------------------------------------------------- Attributes


    /**
     * <p>The value binding expression (if any) used to wire up this component
     * to a {@link UIComponent} property of a JavaBean class.</p>
     */
    private ValueExpression binding = null;


    /**
     * <p>Set the value expression for our component.</p>
     *
     * @param binding The new value expression
     *
     * @throws JspException if an error occurs
     */
    public void setBinding(ValueExpression binding) throws JspException {
	this.binding = binding;
    }

    protected boolean hasBinding() {
	return null != binding;
    }

    /**
     * <p>An override for the rendered attribute associated with our
     * {@link UIComponent}.</p>
     */
    private ValueExpression rendered = null;


    /**
     * <p>Set an override for the rendered attribute.</p>
     *
     * @param rendered The new value for rendered attribute
     */
    public void setRendered(ValueExpression rendered) {
        this.rendered = rendered;
    }

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


    // ------------------------------------------------------------ Tag Methods



    /**
     * <p>Release any resources allocated during the execution of this
     * tag handler.</p>
     */
    public void release() {

	this.binding = null;
        this.rendered = null;
	super.release();
    }


    // ------------------------------------------------------- Protected Methods

    /**
     * <p>Override properties and attributes of the specified component,
     * if the corresponding properties of this tag handler instance were
     * explicitly set.  This method must be called <strong>ONLY</strong>
     * if the specified {@link UIComponent} was in fact created during
     * the execution of this tag handler instance, and this call will occur
     * <strong>BEFORE</strong> the {@link UIComponent} is added to
     * the view.</p>
     *
     * <p>Tag subclasses that want to support additional set properties
     * must ensure that the base class <code>setProperties()</code>
     * method is still called.  A typical implementation that supports
     * extra properties <code>foo</code> and <code>bar</code> would look
     * something like this:</p>
     * <pre>
     * protected void setProperties(UIComponent component) {
     *   super.setProperties(component);
     *   if (foo != null) {
     *     component.setAttribute("foo", foo);
     *   }
     *   if (bar != null) {
     *     component.setAttribute("bar", bar);
     *   }
     * }
     * </pre>
     *
     * <p>The default implementation overrides the following properties:</p>
     * <ul>
     * <li><code>rendered</code> - Set if a value for the
     *     <code>rendered</code> property is specified for
     *     this tag handler instance.</li>
     * <li><code>rendererType</code> - Set if the <code>getRendererType()</code>
     *     method returns a non-null value.</li>
     * </ul>
     *
     * @param component {@link UIComponent} whose properties are to be
     *  overridden
     */
    protected void setProperties(UIComponent component) {
        // The "id" property is explicitly set when components are created
        // so it does not need to be set here
        if (rendered != null) {
            if (rendered.isLiteralText()) {
                try {
                    component.setRendered(Boolean.valueOf(rendered.getExpressionString())
                            .booleanValue());
                } catch (ELException e) {
                    throw new FacesException(e);
                }
            } else {
                component.setValueExpression("rendered", rendered);
            }
        }
        if (getRendererType() != null) {
            component.setRendererType(getRendererType());
        }

    }


    /**
     * <p>Create and return a new child component of the type returned by
     * calling <code>getComponentType()</code>.  If this {@link UIComponentELTag}
     * has a non-null <code>binding</code> attribute, this is done by
     * call {@link Application#createComponent} with the {@link ValueExpression}
     * created for the <code>binding</code> attribute, and the
     * {@link ValueExpression} will be stored on the component.  Otherwise,
     * {@link Application#createComponent} is called with only 
     * the component type.  Finally, initialize the components id
     * and other properties.
     * </p>
     * @param context {@link FacesContext} for the current request
     * @param newId id of the component
     */
    protected UIComponent createComponent(FacesContext context, String newId) throws JspException {
        UIComponent component;
        Application application = context.getApplication();
        if (binding != null) {
            component = application.createComponent(binding, context,
                                                    getComponentType());
	    component.setValueExpression("binding", binding);
        } else {
            component = application.createComponent(getComponentType());
        }

        component.setId(newId);
        setProperties(component);

        return component;
    }

}
