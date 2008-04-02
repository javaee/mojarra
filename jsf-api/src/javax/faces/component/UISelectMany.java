/*
 * $Id: UISelectMany.java,v 1.35 2003/10/30 20:30:14 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.util.Iterator;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;


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
 * point to something that is an array or a <code>java.util.List</code>
 * of <code>String</code>s.</strong> If the type is an array type, use
 * {@link
 * javax.faces.application.Application#createConverter(java.lang.Class)}
 * passing the <code>Class</code> instance for the element type of the
 * array.  If the type is <code>java.util.List</code>, assume the
 * element type is <code>String</code>.</p>
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

public class UISelectMany extends UIInput {


    // ------------------------------------------------------ Manifest Constants


    /**
     * <p>The message identifier of the
     * {@link javax.faces.application.Message} to be created if
     * a value not matching the available options is specified.
     */
    public static final String INVALID_MESSAGE_ID =
        "javax.faces.component.UISelectMany.INVALID";


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Create a new {@link UISelectMany} instance with default property
     * values.</p>
     */
    public UISelectMany() {

        super();
        setRendererType("Listbox");

    }


    // -------------------------------------------------------------- Properties


    /**
     * <p>Return the currently selected values, or <code>null</code> if there
     * are no currently selected values.  This is a typesafe alias for
     * <code>getValue()</code>.</p>
     */
    public Object[] getSelectedValues() {

        return ((Object[]) getValue());

    }


    /**
     * <p>Set the currently selected values, or <code>null</code> to indicate
     * that there are no currently selected values.  This is a typesafe
     * alias for <code>setValue()</code>.</p>
     *
     * @param selectedValues The new selected values (if any)
     */
    public void setSelectedValues(Object selectedValues[]) {

        setValue(selectedValues);

    }


    // --------------------------------------------------------- UIInput Methods


    /**
     * <p>Return <code>true</code> if the new value is different from the
     * previous value. Value comparison must not be sensitive to element order.
     * </p>
     *
     * @param previous old value of this component
     * @param value new value of this component
     */
    protected boolean compareValues(Object previous, Object value) {

        if ((previous == null) && (value != null)) {
            return (true);
        } else if ((previous != null) && (value == null)) {
            return (true);
        } else if ((previous == null) && (value == null)) {
            return (false);
        }

        boolean valueChanged = false;
        Object oldarray[] = null;
        Object newarray[] = null;
        // If values are not of the type Object[], it is perhaps a mistake
        // by the renderers, so return false, so that ValueChangedEvent is not
        // queued in this case.
        if (!(previous instanceof Object[]) || 
              !(value instanceof Object[])) {
              return false;
        }
        oldarray = (Object[]) previous;
        newarray = (Object[])value;
       
        // If we got here then both the arrays cannot be null
        // if their lengths vary, return false.
        if ( oldarray.length != newarray.length) {
            return true;
        }
        
        // make sure every element in the previous array occurs the same
        // number of times in the current array. This should help us
        // to find out the values changed are not. Since we cannot assume
        // the browser will send the elements in the same order everytime,
        // it will not suffice to just compare the element position and position.
        int count1 = 0;
        int count2= 0;
        for ( int i= 0; i < oldarray.length; ++i ) {
            count1 = countElementOccurrence(oldarray[i], oldarray);
            count2 = countElementOccurrence(oldarray[i], newarray);
            if ( count1 != count2 ) {
                valueChanged = true;
                break;
            }     
        }    
        return valueChanged;

    }    

    
    /**
     * <p>Return the number of occurrances of a particular element in the
     * array.</p>
     *
     * @param element object whose occurrance is to be counted in the array.
     * @param array object representing the old value of this component.
     */
    private int countElementOccurrence(Object element, Object[] array) {

        int count = 0;
        for ( int i= 0; i < array.length; ++i ) {
            Object arrayElement = array[i];
            if (arrayElement != null && element != null) {
                if (arrayElement.equals(element)) {
                    count ++;
                }
            }
        }    
        return count;

    }    


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
    public void validate(FacesContext context) {

        Object value = getValue();

        // Skip validation if it is not necessary
        if ((value == null) || !isValid()) {
            super.validate(context);
            return;
        }

        // Ensure that the values match one of the available options
        Object values[] = (Object[]) value;
        boolean found = false;
        for (int i = 0; i < values.length; i++) {
            found = false;
            Iterator items = new SelectItemsIterator(this);
            while (items.hasNext()) {
                SelectItem item = (SelectItem) items.next();
                if (values[i].equals(item.getValue())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                break;
            }
        }

        // Enqueue an error message if an invalid value was specified
        if (!found) {
            context.addMessage(getClientId(context), 
			       MessageFactory.getMessage(context, 
							 INVALID_MESSAGE_ID));
            setValid(false);
        }
        super.validate(context);

    }


}
