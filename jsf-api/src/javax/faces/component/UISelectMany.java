/*
 * $Id: UISelectMany.java,v 1.30 2003/09/12 13:21:19 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import javax.faces.context.FacesContext;


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
 *
 * <p>The {@link javax.faces.render.Renderer} for this component must
 * perform the following logic on <code>decode()</code>:</p>
 *
 * <ul>
 *
 * <p>Obtain the values array from the request.  Obtain the {@link
 * javax.faces.convert.Converter} using the following algorithm:</p>
 *
 * <ul> 
 *
 * <p>If the component has an attached <code>Converter</code>, use
 * it.</p>
 *
 * <p>If not, look at the <code>valueRef</code>.  If there is a
 * <code>valueRef</code>, look at it's type.  <strong>The
 * <code>valueRef</code> for a <code>UISelectMany</code> component must
 * point to something that is an array.</strong> If the type is an array
 * type, use {@link
 * javax.faces.application.Application#createConverter(java.lang.Class)}
 * passing the <code>Class</code> instance for the element type of the
 * array.</p>
 *
 * <p>If for any reason a <code>Converter</code> cannot be found, add a
 * conversion error message to the {@link
 * javax.faces.context.FacesContext}, set the local value to be the
 * values array from the request, set the component's <code>valid</code>
 * state to <code>false</code>, and return.</p>
 *
 * </ul>
 *
 * <p>Use the <code>Converter</code> to convert each element in the
 * values array from the request to the proper type.  If the component
 * has a <code>valueRef</code>, create an array of the expected type to
 * hold the converted values.  If the component does not have a
 * <code>valueRef</code> create an array of <code>Object</code>.  Store
 * the created array as the local value of the component, set the
 * components <code>valid</code> state to <code>true</code> and
 * return.</p>
 *
 * </ul>
 *
 */

public interface UISelectMany extends UIInput {


    // ------------------------------------------------------ Manifest Constants


    /**
     * <p>The message identifier of the
     * {@link javax.faces.application.Message} to be created if
     * a value not matching the available options is specified.
     */
    public static final String INVALID_MESSAGE_ID =
        "javax.faces.component.UISelectMany.INVALID";



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


    // ------------------------------------------------------ Validation Methods


    /**
     * <p>In addition to the standard validation behavior inherited from
     * {@link UIInput}, ensure that any specified values are equal to one of
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
