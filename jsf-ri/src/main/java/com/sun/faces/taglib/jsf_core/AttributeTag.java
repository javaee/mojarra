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


import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.webapp.UIComponentELTag;
import javax.faces.webapp.UIComponentClassicTagBase;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.sun.faces.util.MessageUtils;


/**
 * <p>Tag implementation that adds an attribute with a specified name
 * and String value to the component whose tag it is nested inside,
 * if the component does not already contain an attribute with the
 * same name.  This tag creates no output to the page currently
 * being created.</p>
 *
 */

public class AttributeTag extends TagSupport {


    // ------------------------------------------------------------- Attributes


    /**
     * <p>The name of the attribute to be created, if not already present.
     */
    private ValueExpression name = null;


    /**
     * <p>Set the attribute name.</p>
     *
     * @param name The new attribute name
     */
    public void setName(ValueExpression name) {

        this.name = name;

    }


    /**
     * <p>The value to be associated with this attribute, if it is created.</p>
     */
    private ValueExpression value = null;



    /**
     * <p>Set the attribute value.</p>
     *
     * @param value The new attribute value
     */
    public void setValue(ValueExpression value) {

        this.value = value;

    }


    // -------------------------------------------------------- Methods from Tag


    /**
     * <p>Register the specified attribute name and value with the
     * {@link UIComponent} instance associated with our most immediately
     * surrounding {@link UIComponentClassicTagBase} instance, if this
     * {@link UIComponent} does not already have a value for the
     * specified attribute name.</p>
     *
     * @exception JspException if a JSP error occurs
     */
    public int doStartTag() throws JspException {

        // Locate our parent UIComponentTagBase
        UIComponentClassicTagBase tag =
            UIComponentELTag.getParentUIComponentClassicTagBase(pageContext);
        if (tag == null) {
        	String message = MessageUtils.getExceptionMessageString
        	(MessageUtils.NOT_NESTED_IN_UICOMPONENT_TAG_ERROR_MESSAGE_ID);
        	throw new JspException(message);
        }
        
        // Add this attribute if it is not already defined
        UIComponent component = tag.getComponentInstance();
        if (component == null) {
        	String message = MessageUtils.getExceptionMessageString
        	(MessageUtils.NO_COMPONENT_ASSOCIATED_WITH_UICOMPONENT_TAG_MESSAGE_ID);
        	throw new JspException(message);
        }

        FacesContext context = FacesContext.getCurrentInstance();
        ELContext elContext = context.getELContext();

        String nameVal = null;
        Object valueVal = null;
	boolean isLiteral = false;

        if (name != null) {
            nameVal = (String) name.getValue(elContext);
        }

        if (value != null) {
	    if (isLiteral = value.isLiteralText()) {
		valueVal = value.getValue(elContext);
	    }
        }
	
        if (component.getAttributes().get(nameVal) == null) {
	    if (isLiteral) {
		component.getAttributes().put(nameVal, valueVal);
	    }
	    else {
		component.setValueExpression(nameVal, value);
	    }
        }
        return (SKIP_BODY);

    }

    public int doEndTag() throws JspException {
	this.release();
	return (EVAL_PAGE);
    }


    /**
     * <p>Release references to any acquired resources.
     */
    public void release() {

        this.name = null;
        this.value = null;

    } // END release

}
