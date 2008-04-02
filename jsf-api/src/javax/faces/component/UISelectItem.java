/*
 * $Id: UISelectItem.java,v 1.24 2003/11/07 01:23:49 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.io.IOException;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.model.SelectItem;


/**
 * <p><strong>UISelectItem</strong> is a component that may be nested
 * inside a {@link UISelectMany} or {@link UISelectOne} component, and
 * causes the addition of a {@link SelectItem} instance to the list of
 * available options for the parent component.  The contents of the
 * {@link SelectItem} can be specified in one of the following ways:</p>
 * <ul>
 * <li>The <code>value</code> attribute's value is an instance of
 *     {@link SelectItem}.</li>
 * <li>The <code>valueRef</code> attribute points at a model data
 *     item of type {@link SelectItem}.</li>
 * <li>A new {@link SelectItem} instance is synthesized from the values
 *     of the <code>itemDescription</code>, <code>itemLabel</code>, and
 *     <code>itemValue</code> attributes.</li>
 * </ul>
 */

public class UISelectItem extends UIComponentBase implements ValueHolder {


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
    private String itemLabel = null;
    private String itemValue = null;
    private Object value = null;
    private String valueRef = null;


    // -------------------------------------------------------------- Properties


    /**
     * <p>Return the description for this selection item.</p>
     */
    public String getItemDescription() {

	ValueBinding vb = getValueBinding("itemDescription");
	if (vb != null) {
	    return ((String) vb.getValue(getFacesContext()));
	} else {
	    return (this.itemDescription);
	}

    }


    /**
     * <p>Set the description for this selection item.</p>
     *
     * @param itemDescription The new description
     */
    public void setItemDescription(String itemDescription) {

        this.itemDescription = itemDescription;
	setValueBinding("itemDescription", null);

    }


    /**
     * <p>Return the localized label for this selection item.</p>
     */
    public String getItemLabel() {

	ValueBinding vb = getValueBinding("itemLabel");
	if (vb != null) {
	    return ((String) vb.getValue(getFacesContext()));
	} else {
	    return (this.itemLabel);
	}

    }


    /**
     * <p>Set the localized label for this selection item.</p>
     *
     * @param itemLabel The new localized label
     */
    public void setItemLabel(String itemLabel) {

        this.itemLabel = itemLabel;
	setValueBinding("itemLabel", null);

    }


    /**
     * <p>Return the server value for this selection item.</p>
     */
    public String getItemValue() {

	ValueBinding vb = getValueBinding("itemValue");
	if (vb != null) {
	    return ((String) vb.getValue(getFacesContext()));
	} else {
	    return (this.itemValue);
	}

    }


    /**
     * <p>Set the server value for this selection item.</p>
     *
     * @param itemValue The new server value
     */
    public void setItemValue(String itemValue) {

        this.itemValue = itemValue;
	setValueBinding("itemValue", null);

    }


    // -------------------------------------------------- ValueHolder Properties


    public Object getValue() {

        return (this.value);

    }


    public void setValue(Object value) {

        this.value = value;

    }


    public String getValueRef() {

        return (this.valueRef);

    }


    public void setValueRef(String valueRef) {

        this.valueRef = valueRef;

    }


    // ----------------------------------------------------- ValueHolder Methods

    /**
     * @exception EvaluationException {@inheritDoc}
     * @exception NullPointerException {@inheritDoc}  
     */
    public Object currentValue(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }
        Object value = getValue();
        if (value != null) {
            return (value);
        }
        String valueRef = getValueRef();
        if (valueRef != null) {
            Application application = context.getApplication();
            ValueBinding binding = application.getValueBinding(valueRef);
            return (binding.getValue(context));
        }
        return (null);

    }


    // ----------------------------------------------------- StateHolder Methods


    public Object saveState(FacesContext context) {

        Object values[] = new Object[6];
        values[0] = super.saveState(context);
        values[1] = itemDescription;
        values[2] = itemLabel;
        values[3] = itemValue;
        values[4] = value;
        values[5] = valueRef;
        return (values);

    }


    public void restoreState(FacesContext context, Object state) {

        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        itemDescription = (String) values[1];
        itemLabel = (String) values[2];
        itemValue = (String) values[3];
        value = values[4];
        valueRef = (String) values[5];

    }


}
