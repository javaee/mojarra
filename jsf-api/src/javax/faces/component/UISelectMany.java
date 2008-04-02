/*
 * $Id: UISelectMany.java,v 1.28 2003/07/26 17:54:38 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


/**
 * <p><strong>UISelectMany</strong> is a {@link UIComponent} that represents
 * the user's choice of a zero or more items from among a discrete set of
 * available options.  The user can modify the selected values.  Optionally,
 * the component can be preconfigured with zero or more currently selected
 * items, by storing them as an array in the <code>value</code> property of
 * the component.</p>
 *
 * <p>This component is generally rendered as a select box or a group of
 * checkboxes.</p>
 *
 * <p>By default, the <code>rendererType</code> property must be set to
 * "<code>Listbox</code>".  This value can be changed by calling the
 * <code>setRendererType()</code> method.</p>
 */

public interface UISelectMany extends UIInput {


    // -------------------------------------------------------------- Properties

    /**
     * <p>Return the currently selected values, or <code>null</code> if there
     * are no currently selected values.  This is a typesafe alias for
     * <code>getValue()</code>.</p>
     */
    public Object[] getSelectedValues();


    /**
     * <p>Set the currently selected values, or <code>null</code> to indicate
     * that there are no currently selected values.  This is a typesafe
     * alias for <code>setValue()</code>.</p>
     *
     * @param selectedValues The new selected values (if any)
     */
    public void setSelectedValues(Object selectedValues[]);


}
