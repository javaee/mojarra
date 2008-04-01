/*
 * $Id: UISelectOne.java,v 1.1 2002/05/14 00:41:38 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


/**
 * <p><strong>UISelectOne</strong> is a {@link UIComponent} that represents
 * the user's choice of a single item from among a discrete set of
 * available options.  The user can modify the selected value.  Optionally,
 * the component can be preconfigured with a currently selected item.  This
 * component is generally rendered as a select box or a group of
 * radio buttons.</p>
 *
 * <h3>Properties</h3>
 *
 * <p>Each <code>UISelectOne</code> instance supports the following JavaBean
 * properties to describe its render-independent characteristics:</p>
 * <ul>
 * <li><strong>items</strong> - An array of {@link SelectOne.Item} values
 *     representing the complete set of possible choices.</li>
 * </ul>
 *
 * <p>The <code>value</code> property is used to store the local value the
 * currently selected item (represented by that item's <code>value</code>
 * property), and must be a <code>java.lang.String</code> (as must the
 * model property corresponding to any model reference for this component).
 * </p>
 *
 * <p>For convenience, the local value of the selected item's
 * <code>value</code> is accessible via the <code>getSelectedValue()</code>
 * and <code>setSelectedValue()</code> methods.  The
 * <code>currentValue()</code> method should be used to retrieve the
 * value to be rendered.</p>
 */

public class UISelectOne extends UIComponent {


    // ------------------------------------------------------- Static Variables


    /**
     * The component type of this {@link UIComponent} subclass.
     */
    public static final String TYPE = "SelectOne";


    // ------------------------------------------------------------- Properties


    /**
     * <p>Return the component type of this <code>UIComponent</code>.</p>
     */
    public String getComponentType() {

        return (TYPE);

    }


    /**
     * <p>Return the available items for this component.</p>
     */
    public UISelectOne.Item[] getItems() {

        return ((UISelectOne.Item[]) getAttribute("items"));

    }


    /**
     * <p>Set the available items for this component.</p>
     *
     * @param items The new available items
     */
    public void setItems(UISelectOne.Item items[]) {

        setAttribute("items", items);

    }


    /**
     * <p>Return the local value of the selected item's value.</p>
     */
    public String getSelectedValue() {

        return ((String) getAttribute("value"));

    }


    /**
     * <p>Set the local value of the selected item's value.</p>
     *
     * @param selectedValue The new selected item's value
     */
    public void setSelectedValue(String selectedValue) {

        setAttribute("value", selectedValue);

    }


    // --------------------------------------------------------- Public Methods


    // ----------------------------------------------------------- Nested Class


    /**
     * <p><strong>UISelectOne.Item</strong> encapsulates a single item
     * in the list of items available for selection.
     */
    public static class Item {

        // FIXME-Package-private to allow local modification?
        String value = null;
        String label = null;
        String description = null;

        public Item(String value, String label, String description) {
            this.value = value;
            this.label = label;
            this.description = description;
        }

        public Item(String value, String label) {
            this(value, label, null);
        }

        public Item(String value) {
            this(value, value, null);
        }

        public String getValue() {
            return (this.value);
        }

        public String getLabel() {
            return (this.label);
        }

        public String getDescription() {
            return (this.description);
        }

    }


}
