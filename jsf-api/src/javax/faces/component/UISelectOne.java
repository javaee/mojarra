/*
 * $Id: UISelectOne.java,v 1.25 2003/03/13 01:11:59 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


/**
 * <p><strong>UISelectOne</strong> is a {@link UIComponent} that represents
 * the user's choice of a single item from among a discrete set of
 * available options.  The user can modify the selected value.  Optionally,
 * the component can be preconfigured with a currently selected item, by
 * storing it as the <code>value</code> property of the component.</p>
 *
 * <p>This component is generally rendered as a select box or a group of
 * radio buttons.</p>
 *
 * <p>By default, the <code>rendererType</code> property is set to
 * "<code>Listbox</code>".  This value can be changed by calling the
 * <code>setRendererType()</code> method.</p>
 */

public class UISelectOne extends UISelectBase {


    // ------------------------------------------------------- Static Variables


    /**
     * The component type of this {@link UIComponent} subclass.
     */
    public static final String TYPE = "javax.faces.component.UISelectOne";


    // ----------------------------------------------------------- Constructors


    /**
     * <p>Create a new {@link UISelectOne} instance with default property
     * values.</p>
     */
    public UISelectOne() {

        super();
        setRendererType("Menu");

    }


    // ------------------------------------------------------------- Properties


    public String getComponentType() {

        return (TYPE);

    }


    /**
     * <p>Return the currently selected item, or <code>null</code> if there
     * is no currently selected item.</p>
     */
    public Object getSelectedValue() {

        return (getValue());

    }


    /**
     * <p>Set the currently selected item, or <code>null</code> to indicate
     * that there is no currently selected item.</p>
     *
     * @param selectedItem The new selected item (if any)
     */
    public void setSelectedValue(Object selectedItem) {

        setValue(selectedItem);

    }


}
