/*
 * $Id: UISelectManyBase.java,v 1.3 2003/08/28 21:08:54 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component.base;


import java.util.Iterator;
import javax.faces.application.Message;
import javax.faces.application.MessageResources;
import javax.faces.component.UIInput;
import javax.faces.component.UISelectMany;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;


/**
 * <p><strong>UISelectManyBase</strong> is a convenience base class that
 * implements the default concrete behavior of all methods defined by
 * {@link UISelectMany}.</p>
 */

public class UISelectManyBase extends UIInputBase implements UISelectMany {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Create a new {@link UISelectManyBase} instance with default property
     * values.</p>
     */
    public UISelectManyBase() {

        super();
        setRendererType("Listbox");

    }


    // -------------------------------------------------------------- Properties


    public Object[] getSelectedValues() {

        return ((Object[]) getValue());

    }


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
            Message message =
                context.getApplication().
                getMessageResources(MessageResources.FACES_API_MESSAGES).
                getMessage(context, INVALID_MESSAGE_ID);
            context.addMessage(this, message);
            setValid(false);
        }
        super.validate(context);

    }


}
