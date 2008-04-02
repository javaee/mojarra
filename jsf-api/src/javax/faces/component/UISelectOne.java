/*
 * $Id: UISelectOne.java,v 1.29 2003/08/28 21:17:25 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import javax.faces.context.FacesContext;


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


    // ------------------------------------------------------ Manifest Constants


    /**
     * <p>The message identifier of the
     * {@link javax.faces.application.Message} to be created if
     * a value not matching the available options is specified.
     */
    public static final String INVALID_MESSAGE_ID =
        "javax.faces.component.UISelectOne.INVALID";



    // ------------------------------------------------------ Validation Methods


    /**
     * <p>In addition to the standard validation behavior inherited from
     * {@link UIInput}, ensure that any specified value is equal to one of
     * the available options.  If it is not, enqueue an error message
     * and set the <code>valid</code> property to <code>false</code>.</p>
     *
     * @param context The {@link FacesContext} for the current request
     *
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public void validate(FacesContext context);


}
