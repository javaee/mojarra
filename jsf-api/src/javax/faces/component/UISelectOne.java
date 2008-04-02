/*
 * $Id: UISelectOne.java,v 1.27 2003/07/26 17:54:39 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


/**
 * <p><strong>UISelectOne</strong> is a {@link UIComponent} that represents
 * the user's choice of zero or one items from among a discrete set of
 * available options.  The user can modify the selected value.  Optionally,
 * the component can be preconfigured with a currently selected item, by
 * storing it as the <code>value</code> property of the component.</p>
 *
 * <p>This component is generally rendered as a select box or a group of
 * radio buttons.</p>
 *
 * <p>By default, the <code>rendererType</code> property is set to
 * "<code>Menu</code>".  This value can be changed by calling the
 * <code>setRendererType()</code> method.</p>
 */

public interface UISelectOne extends UIInput {


    // -------------------------------------------------------------- Properties


    /**
     * <p>Return the currently selected value, or <code>null</code> if there
     * is no currently selected value.  This is a typesafe alias for
     * <code>getValue()</code>.</p>
     */
    public Object getSelectedValue();


    /**
     * <p>Set the currently selected value, or <code>null</code> to indicate
     * that there is no currently selected value.  This is a typesafe
     * alias for <code>setValue()</code>.</p>
     *
     * @param selectedValue The new selected value (if any)
     */
    public void setSelectedValue(Object selectedValue);


}
