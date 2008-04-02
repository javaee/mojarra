/*
 * $Id: UISelectMany.java,v 1.46 2004/01/29 03:45:50 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
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
 * "<code>javax.faces.Listbox</code>".  This value can be changed by
 * calling the <code>setRendererType()</code> method.</p>
 *
 * <p>The {@link javax.faces.render.Renderer} for this component must
 * perform the following logic on <code>getConvertedValue()</code>:</p>
 *
 * <ul>
 *
 * <p>Obtain the {@link
 * javax.faces.convert.Converter} using the following algorithm:</p>
 *
 * <ul> 
 *
 * <p>If the component has an attached <code>Converter</code>, use
 * it.</p>
 *
 * <p>If not, look for a {@link ValueBinding} for <code>value</code> (if any).
 * If there is a {@link ValueBinding}, call <code>getType()</code>.
 * <strong>The {@link ValueBinding} must point to something that is
 * an array or a <code>List</code> of <code>String</code>s.</strong>
 * If the type is an array type, use {@link
 * javax.faces.application.Application#createConverter(java.lang.Class)}
 * passing the <code>Class</code> instance for the element type of the
 * array.  If the type is <code>java.util.List</code>, assume the
 * element type is <code>String</code>.</p>
 *
 * <p>If for any reason a <code>Converter</code> cannot be found, assume
 * the type to be a String array.</p>
 *
 * </ul>
 *
 * <p>Use the <code>Converter</code> to convert each element in the
 * values array from the request to the proper type.  If the component
 * has a {@link ValueBinding} for <code>value</code>, create an array
 * of the expected type to hold the converted values.  If the component
 * does not have a {@link ValueBinding} for <code>value</code>, create
 * an array of type <code>Object</code>.  Store the created array
 * as the local value of the component, set the component's <code>valid</code>
 * state to <code>true</code> and return.</p>
 *
 * </ul>
 *
 */

public class UISelectMany extends UIInput {


    // ------------------------------------------------------ Manifest Constants


    /**
     * <p>The standard component type for this component.</p>
     */
    public static final String COMPONENT_TYPE = "javax.faces.SelectMany";


    /**
     * <p>The standard component family for this component.</p>
     */
    public static final String COMPONENT_FAMILY = "javax.faces.SelectMany";


    /**
     * <p>The message identifier of the
     * {@link javax.faces.application.FacesMessage} to be created if
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
        setRendererType("javax.faces.Listbox");

    }


    // -------------------------------------------------------------- Properties


    public String getFamily() {

        return (COMPONENT_FAMILY);

    }


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


    // ---------------------------------------------------------------- Bindings


    /**
     * <p>Return any {@link ValueBinding} set for <code>value</code> if a
     * {@link ValueBinding} for <code>selectedValues</code> is requested;
     * otherwise, perform the default superclass processing for this method.</p>
     *
     * @param name Name of the attribute or property for which to retrieve
     *  a {@link ValueBinding}
     *
     * @exception NullPointerException if <code>name</code>
     *  is <code>null</code>
     */
    public ValueBinding getValueBinding(String name) {

        if ("selectedValues".equals(name)) {
            return (super.getValueBinding("value"));
        } else {
            return (super.getValueBinding(name));
        }

    }


    /**
     * <p>Store any {@link ValueBinding} specified for
     * <code>selectedValues</code> under <code>value</code> instead;
     * otherwise, perform the default superclass processing for this method.</p>
     *
     * @param name Name of the attribute or property for which to set
     *  a {@link ValueBinding}
     * @param binding The {@link ValueBinding} to set, or <code>null</code>
     *  to remove any currently set {@link ValueBinding}
     *
     * @exception NullPointerException if <code>name</code>
     *  is <code>null</code>
     */
    public void setValueBinding(String name, ValueBinding binding) {

        if ("selectedValues".equals(name)) {
            super.setValueBinding("value", binding);
        } else {
            super.setValueBinding(name, binding);
        }

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

        // The arrays may be arrays of primitives;  for simplicity,
        // perform the boxing here.
        if (!(previous instanceof Object[])) {
            previous = toObjectArray(previous);
        }

        if (!(value instanceof Object[])) {
            value = toObjectArray(value);
        }

        // If values are still not of the type Object[], it is perhaps a
        // mistake by the renderers, so return false, so that
        // ValueChangedEvent is not queued in this case.
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

    
    /**
     * Convert an array of primitives to an array of boxed objects.
     * @param primitiveArray object containing the primitive values
     * @return an Object array, or null if the incoming value is not
     * in fact an array at all.
     */
    private Object[] toObjectArray(Object primitiveArray) {
        if (primitiveArray == null) {
            throw new NullPointerException();
        }
        
        if (primitiveArray instanceof Object[]) {
            return (Object[]) primitiveArray;
        }

        if (primitiveArray instanceof List) {
            return ((List) primitiveArray).toArray();
        }
          
        Class clazz = primitiveArray.getClass();
        if (!clazz.isArray()) {
            return null;
        }

        int length = Array.getLength(primitiveArray);
        Object[] array = new Object[length];
        for (int i = 0; i < length; i++) {
            array[i] = Array.get(primitiveArray, i);
        }

        return array;
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

        super.validate(context);

        // Skip validation if it is not necessary
        Object value = getValue();
        if (!isValid() || (value == null)) {
            return;
        }

        // Ensure that the values match one of the available options
        // Don't arrays cast to "Object[]", as we may now be using an array
        // of primitives
        boolean isList = (value instanceof List);
        int length = isList ? ((List) value).size() : Array.getLength(value);
        boolean found = true;
        for (int i = 0; i < length; i++) {
            found = false;
            Iterator items = new SelectItemsIterator(this);
            Object indexValue = isList ? ((List) value).get(i) : Array.get(value, i);
            while (items.hasNext()) {
                SelectItem item = (SelectItem) items.next();
                if (indexValue.equals(item.getValue())) {
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
            FacesMessage message =
                MessageFactory.getMessage(context, INVALID_MESSAGE_ID);
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            context.addMessage(getClientId(context), message);
            setValid(false);
        }
    }


}
