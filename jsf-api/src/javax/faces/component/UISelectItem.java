/*
 * $Id: UISelectItem.java,v 1.36 2005/05/05 20:51:04 edburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.io.IOException;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.FacesException;
import javax.el.ELException;
import javax.el.ValueExpression;



/**
 * <p><strong>UISelectItem</strong> is a component that may be nested
 * inside a {@link UISelectMany} or {@link UISelectOne} component, and
 * causes the addition of a {@link SelectItem} instance to the list of
 * available options for the parent component.  The contents of the
 * {@link SelectItem} can be specified in one of the following ways:</p>
 * <ul>
 * <li>The <code>value</code> attribute's value is an instance of
 *     {@link SelectItem}.</li>
 * <li>The associated {@link javax.el.ValueExpression} points at a model data
 *     item of type {@link SelectItem}.</li>
 * <li>A new {@link SelectItem} instance is synthesized from the values
 *     of the <code>itemDescription</code>, <code>itemDisabled</code>,
 *     <code>itemLabel</code>, and <code>itemValue</code> attributes.</li>
 * </ul>
 */

public class UISelectItem extends UIComponentBase {


    // ------------------------------------------------------ Manifest Constants


    /**
     * <p>The standard component type for this component.</p>
     */
    public static final String COMPONENT_TYPE = "javax.faces.SelectItem";


    /**
     * <p>The standard component family for this component.</p>
     */
    public static final String COMPONENT_FAMILY = "javax.faces.SelectItem";


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Create a new {@link UISelectItem} instance with default property
     * values.</p>
     */
    public UISelectItem() {

        super();
        setRendererType(null);

    }


    // ------------------------------------------------------ Instance Variables


    private String itemDescription = null;
    private boolean itemDisabled = false;
    private boolean itemDisabledSet = false;
    private String itemLabel = null;
    private Object itemValue = null;
    private Object value = null;


    // -------------------------------------------------------------- Properties


    public String getFamily() {

        return (COMPONENT_FAMILY);

    }


    /**
     * <p>Return the description for this selection item.</p>
     */
    public String getItemDescription() {

	if (this.itemDescription != null) {
	    return (this.itemDescription);
	}
	ValueExpression ve = getValueExpression("itemDescription");
	if (ve != null) {
	    try {
		return ((String) ve.getValue(getFacesContext().getELContext()));
	    }
	    catch (ELException e) {
		throw new FacesException(e);
	    }
	} else {
	    return (null);
	}

    }


    /**
     * <p>Set the description for this selection item.</p>
     *
     * @param itemDescription The new description
     */
    public void setItemDescription(String itemDescription) {

        this.itemDescription = itemDescription;

    }

    /**
     * <p>Return the disabled setting for this selection item.</p>
     */
    public boolean isItemDisabled() {

	if (this.itemDisabledSet) {
	    return (this.itemDisabled);
	}
	ValueExpression ve = getValueExpression("itemDisabled");
	if (ve != null) {
	    try {
		return (Boolean.TRUE.equals(ve.getValue(getFacesContext().getELContext())));
	    }
	    catch (ELException e) {
		throw new FacesException(e);
	    }
	} else {
	    return (this.itemDisabled);
	}

    }

    /**
     * <p>Set the disabled value for this selection item.</p>
     *
     * @param itemDisabled The new disabled flag
     */
    public void setItemDisabled(boolean itemDisabled) {

        this.itemDisabled = itemDisabled;
        this.itemDisabledSet = true;

    }

    /**
     * <p>Return the localized label for this selection item.</p>
     */
    public String getItemLabel() {

	if (this.itemLabel != null) {
	    return (this.itemLabel);
	}
	ValueExpression ve = getValueExpression("itemLabel");
	if (ve != null) {
	    try {
		return ((String) ve.getValue(getFacesContext().getELContext()));
	    }
	    catch (ELException e) {
		throw new FacesException(e);
	    }
	} else {
	    return (null);
	}

    }


    /**
     * <p>Set the localized label for this selection item.</p>
     *
     * @param itemLabel The new localized label
     */
    public void setItemLabel(String itemLabel) {

        this.itemLabel = itemLabel;

    }


    /**
     * <p>Return the server value for this selection item.</p>
     */
    public Object getItemValue() {

	if (this.itemValue != null) {
	    return (this.itemValue);
	}
	ValueExpression ve = getValueExpression("itemValue");
	if (ve != null) {
	    try {
		return ve.getValue(getFacesContext().getELContext());
	    }
	    catch (ELException e) {
		throw new FacesException(e);
	    }
	} else {
	    return (null);
	}

    }


    /**
     * <p>Set the server value for this selection item.</p>
     *
     * @param itemValue The new server value
     */
    public void setItemValue(Object itemValue) {

        this.itemValue = itemValue;

    }



    /**
     * <p>Returns the <code>value</code> property of the
     * <code>UISelectItem</code>.</p>
     */
    public Object getValue() {

	if (this.value != null) {
	    return (this.value);
	}
	ValueExpression ve = getValueExpression("value");
	if (ve != null) {
	    try {
		return (ve.getValue(getFacesContext().getELContext()));
	    }
	    catch (ELException e) {
		throw new FacesException(e);
	    }
	} else {
	    return (null);
	}

    }


    /**
     * <p>Sets the <code>value</code> property of the
     * <code>UISelectItem</code>.</p>
     * 
     * @param value the new value
     */
    public void setValue(Object value) {

        this.value = value;

    }


    // ----------------------------------------------------- StateHolder Methods


    public Object saveState(FacesContext context) {

        Object values[] = new Object[7];
        values[0] = super.saveState(context);
        values[1] = itemDescription;
        values[2] = itemDisabled ? Boolean.TRUE : Boolean.FALSE;
        values[3] = itemDisabledSet ? Boolean.TRUE : Boolean.FALSE;
        values[4] = itemLabel;
        values[5] = itemValue;
        values[6] = value;
        return (values);

    }


    public void restoreState(FacesContext context, Object state) {

        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        itemDescription = (String) values[1];
        itemDisabled = ((Boolean) values[2]).booleanValue();
        itemDisabledSet = ((Boolean) values[3]).booleanValue();
        itemLabel = (String) values[4];
        itemValue = values[5];
        value = values[6];

    }


}
