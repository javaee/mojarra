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


import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;


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
     * @throws IllegalArgumentException if the specified binding is not a
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
`    * may be used on most component tag attributes to signal a desire for
     * deferred evaluation of the attribute or property value to be set on
     * the underlying {@link UIComponent}.</p>
     *
     * @param value The value to evaluate
     *
     * @throws NullPointerException if <code>value</code> is
     *  <code>null</code>
     */
    public static boolean isValueReference(String value) {

	if (value == null) {
	    throw new NullPointerException();
	}
	int start = value.indexOf("#{");
	if ((start != -1) && (start < value.indexOf('}', start))) {
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
        UIComponent component;
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

        UIComponentClassicTagBase result =
             getParentUIComponentClassicTagBase(context);
        if (!(result instanceof UIComponentTag)) {
            return new UIComponentTagAdapter(result);
        }
        return ((UIComponentTag) result);

    }


    // --------------------------------------------------------- Private Classes


    /**
     * This adatper exposes a UIComponentClassicTagBase as a UIComponentTag
     * for 1.1 component libraries that rely on UIComponent.getParentUIComponentTag().
     *
     * This will work for most use cases, but there are probably some edge
     * cases out there that we're not aware of.
     */
    private static class UIComponentTagAdapter extends UIComponentTag {

        UIComponentClassicTagBase classicDelegate;

        public UIComponentTagAdapter(UIComponentClassicTagBase classicDelegate) {

            this.classicDelegate = classicDelegate;

        }

        public String getComponentType() {
            return classicDelegate.getComponentType();
        }

        public String getRendererType() {
            return classicDelegate.getRendererType();
        }

        public int doStartTag() throws JspException {
            throw new IllegalStateException();
        }

        public int doEndTag() throws JspException {
            throw new IllegalStateException();
        }

        public UIComponent getComponentInstance() {
            return classicDelegate.getComponentInstance();
        }

        public boolean getCreated() {
            return classicDelegate.getCreated();
        }

        public Tag getParent() {
            return classicDelegate.getParent();
        }

        public void setParent(Tag parent) {
            throw new IllegalStateException();
        }
                
    }


}
