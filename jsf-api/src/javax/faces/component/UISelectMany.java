/*
 * $Id: UISelectMany.java,v 1.26 2003/04/29 18:51:32 eburns Exp $
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
 * <p>By default, the <code>rendererType</code> property is set to
 * "<code>Listbox</code>".  This value can be changed by calling the
 * <code>setRendererType()</code> method.</p>
 */

public class UISelectMany extends UISelectBase {


    // ------------------------------------------------------- Static Variables


    // ----------------------------------------------------------- Constructors


    /**
     * <p>Create a new {@link UISelectMany} instance with default property
     * values.</p>
     */
    public UISelectMany() {

        super();
        setRendererType("Listbox");

    }


    // ------------------------------------------------------------- Properties

    /**
     * <p>Return the currently selected items, or <code>null</code> if there
     * are no currently selected items.</p>
     */
    public Object[] getSelectedValues() {

        return ((Object[]) getValue());

    }


    /**
     * <p>Set the currently selected items, or <code>null</code> to indicate
     * that there are no currently selected items.</p>
     *
     * @param selectedItemss The new selected items (if any)
     */
    public void setSelectedValues(Object selectedItems[]) {

        setValue(selectedItems);

    }


    // ------------------------------------------------------ Protected Methods


    /**
     * <p>Return <code>true</code> if the new value is different from the
     * previous value. Value comparison must not be sensitive to element order.
     * </p>
     *
     * @param previous old value of this component
     * @param value new value of this component
     */
    protected boolean compareValues(Object previous, Object value) {

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
            count1 = countElementOccurance(oldarray[i], oldarray);
            count2 = countElementOccurance(oldarray[i], newarray);
            if ( count1 != count2 ) {
                valueChanged = true;
                break;
            }     
        }    
        return valueChanged;

    }    

    
    // -------------------------------------------------------- Private Methods


    /**
     * <p>Return the number of occurrances of a particular element in the
     * array.</p>
     *
     * @param element object whose occurrance is to be counted in the array.
     * @param array object representing the old value of this component.
     */
    private int countElementOccurance(Object element, Object[] array) {

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


}
