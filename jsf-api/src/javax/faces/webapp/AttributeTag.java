/*
 * $Id: AttributeTag.java,v 1.12 2004/07/29 21:18:05 edburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.webapp;


import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;


/**
 * <p>Tag implementation that adds an attribute with a specified name
 * and String value to the component whose tag it is nested inside,
 * if the component does not already contain an attribute with the
 * same name.  This tag creates no output to the page currently
 * being created.</p>
 */

public class AttributeTag extends TagSupport {


    // ------------------------------------------------------------- Attributes


    /**
     * <p>The name of the attribute to be created, if not already present.
     */
    private String name = null;


    /**
     * <p>Set the attribute name.</p>
     *
     * @param name The new attribute name
     */
    public void setName(String name) {

        this.name = name;

    }


    /**
     * <p>The value to be associated with this attribute, if it is created.</p>
     */
    private String value = null;



    /**
     * <p>Set the attribute value.</p>
     *
     * @param value The new attribute value
     */
    public void setValue(String value) {

        this.value = value;

    }


    // --------------------------------------------------------- Public Methods


    /**
     * <p>Register the specified attribute name and value with the
     * {@link UIComponent} instance associated with our most immediately
     * surrounding {@link UIComponentTag} instance, if this {@link UIComponent}
     * does not already have a value for the specified attribute name.</p>
     *
     * @exception JspException if a JSP error occurs
     */
    public int doStartTag() throws JspException {

        // Locate our parent UIComponentTag
        UIComponentTag tag =
            UIComponentTag.getParentUIComponentTag(pageContext);
        if (tag == null) { // PENDING - i18n
            throw new JspException("Not nested in a UIComponentTag");
        }

        // Add this attribute if it is not already defined
        UIComponent component = tag.getComponentInstance();
        if (component == null) { // PENDING - i18n
            throw new JspException("No component associated with UIComponentTag");
        }
        String nameVal = name;

	FacesContext context = FacesContext.getCurrentInstance();

        if (UIComponentTag.isValueReference(name)) {
            ValueBinding vb =
                context.getApplication().createValueBinding(name);
            nameVal = (String) vb.getValue(context);
        }
        Object valueVal = value;
        if (UIComponentTag.isValueReference(value)) {
            ValueBinding vb =
                context.getApplication().createValueBinding(value);
            valueVal = vb.getValue(context);
        }
        if (component.getAttributes().get(nameVal) == null) {
            component.getAttributes().put(nameVal, valueVal);
        }
        return (SKIP_BODY);

    }


    /**
     * <p>Release references to any acquired resources.
     */
    public void release() {

        this.name = null;
        this.value = null;
    }

}
