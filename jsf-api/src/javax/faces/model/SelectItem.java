/*
 * $Id: SelectItem.java,v 1.8 2004/06/01 22:14:26 rogerk Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.model;


import java.io.Serializable;
import javax.faces.component.UISelectMany;
import javax.faces.component.UISelectOne;


/**
 * <p><strong>SelectItem</strong> represents a single <em>item</em> in the
 * list of supported <em>items</em> associated with a {@link UISelectMany}
 * or {@link UISelectOne} component.</p>
 */

public class SelectItem implements Serializable {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Construct a <code>SelectItem</code> with no initialized property
     * values.</p>
     */
    public SelectItem() {

        super();

    }


    /**
     * <p>Construct a <code>SelectItem</code> with the specified value.  The
     * <code>label</code> property will be set to the value (converted to a
     * String, if necessary), the <code>description</code> property will be
     * set to <code>null</code>, and the <code>disabled</code> property will
     * be set to <code>false</code>.</p>
     *
     * @param value Value to be delivered to the model if this
     *  item is selected by the user
     *
     * @exception NullPointerException if <code>value</code>
     *  is <code>null</code>
     */
    public SelectItem(Object value) {

        this(value, value.toString(), null, false);

    }


    /**
     * <p>Construct a <code>SelectItem</code> with the specified value and
     * label.  The <code>description</code> property will be set to
     * <code>null</code>, and the <code>disabled</code> property will be
     * set to <code>false</code>.</p>
     *
     * @param value Value to be delivered to the model if this
     *  item is selected by the user
     * @param label Label to be rendered for this item in the response
     *
     * @exception NullPointerException if <code>value</code>
     *  or <code>label</code> <code>null</code>
     */
    public SelectItem(Object value, String label) {

        this(value, label, null, false);

    }


    /**
     * <p>Construct a <code>SelectItem</code> instance with the specified
     * value, label and description.  This <code>disabled</code> property
     * will be set to <code>false</code>.</p>
     *
     * @param value Value to be delivered to the model if this
     *  item is selected by the user
     * @param label Label to be rendered for this item in the response
     * @param description Description of this item, for use in tools
     *
     * @exception NullPointerException if <code>value</code>
     *  or <code>label</code> is <code>null</code>
     */
    public SelectItem(Object value, String label, String description) {

        this(value, label, description, false);

    }


    /**
     * <p>Construct a <code>SelectItem</code> instance with the specified
     * property values.</p>
     *
     * @param value Value to be delivered to the model if this
     *  item is selected by the user
     * @param label Label to be rendered for this item in the response
     * @param description Description of this item, for use in tools
     * @param disabled Flag indicating that this option is disabled
     *
     * @exception NullPointerException if <code>value</code>
     *  or <code>label</code> is <code>null</code>
     */
    public SelectItem(Object value, String label, String description,
                      boolean disabled) {

        super();
        setValue(value);
        setLabel(label);
        setDescription(description);
        setDisabled(disabled);

    }


    // ------------------------------------------------------ Instance Variables


    private String description = null;
    private boolean disabled = false;
    private String label = null;
    private Object value = null;


    // -------------------------------------------------------------- Properties


    /**
     * <p>Return a description of this item, for use in development tools.
     */
    public String getDescription() {

        return (this.description);

    }


    /**
     * <p>Set the description of this item, for use in development tools.</p>
     *
     * @param description The new description
     */
    public void setDescription(String description) {

        this.description = description;

    }


    /**
     * <p>Return the disabled flag for this item, which should modify the
     * rendered output to make this item unavailable for selection by the user
     * if set to <code>true</code>.</p>
     */
    public boolean isDisabled() {

        return (this.disabled);

    }


    /**
     * <p>Set the disabled flag for this item, which should modify the
     * rendered output to make this item unavailable for selection by the user
     * if set to <code>true</code>.</p>
     *
     * @param disabled The new disabled flag
     */
    public void setDisabled(boolean disabled) {

        this.disabled = disabled;

    }


    /**
     * <p>Return the label of this item, to be rendered visibly for the user.
     */
    public String getLabel() {

        return (this.label);

    }


    /**
     * <p>Set the label of this item, to be rendered visibly for the user.
     *
     * @param label The new label
     *
     * @exception NullPointerException if <code>label</code>
     *  is <code>null</code>
     */
    public void setLabel(String label) {

        this.label = label;

    }


    /**
     * <p>Return the value of this item, to be delivered to the model
     * if this item is selected by the user.
     */
    public Object getValue() {

        return (this.value);

    }


    /**
     * <p>Set the value of this item, to be delivered to the model
     * if this item is selected by this user.
     *
     * @param value The new value
     *
     * @exception NullPointerException if <code>value</code> is
     *  <code>null</code>
     */
    public void setValue(Object value) {

        if (value == null) {
            throw new NullPointerException();
        }
        this.value = value;

    }


}
