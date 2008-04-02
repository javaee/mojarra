/*
 * $Id: AttributeTag.java,v 1.4 2005/05/05 20:51:26 edburns Exp $
 */

/*
 * Copyright 2005 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
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
     * surrounding {@link UIComponentTagBase} instance, if this
     * {@link UIComponent} does not already have a value for the
     * specified attribute name.</p>
     *
     * @exception JspException if a JSP error occurs
     */
    public int doStartTag() throws JspException {

        // Locate our parent UIComponentTagBase
        UIComponentClassicTagBase tag =
            UIComponentELTag.getParentUIComponentClassicTagBase(pageContext);
        if (tag == null) { // PENDING - i18n
            throw new JspException("Not nested in a UIComponentTag");
        }

        // Add this attribute if it is not already defined
        UIComponent component = tag.getComponentInstance();
        if (component == null) { // PENDING - i18n
            throw new JspException("No component associated with UIComponentTag");
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
