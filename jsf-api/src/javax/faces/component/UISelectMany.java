/*
 * $Id: UISelectMany.java,v 1.21 2003/01/23 17:42:49 rkitain Exp $
 */

/*
 * Copyright 2002-2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.io.IOException;
import java.util.Iterator;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;


/**
 * <p><strong>UISelectMany</strong> is a {@link UIComponent} that represents
 * the user's choice of a zero or more items from among a discrete set of
 * available options.  The user can modify the selected value.  Optionally,
 * the component can be preconfigured with zero or more currently selected
 * items.  This component is generally rendered as a select box or a group of
 * checkboxes.</p>
 */

public class UISelectMany extends UISelectBase {


    // ------------------------------------------------------- Static Variables


    /**
     * The component type of this {@link UIComponent} subclass.
     */
    public static final String TYPE = "javax.faces.component.UISelectMany";


    // ------------------------------------------------------------- Properties


    /**
     * <p>Return the component type of this <code>UIComponent</code>.</p>
     */
    public String getComponentType() {

        return (TYPE);

    }


    /**
     * <p>Return the local value of the selected item's values.</p>
     */
    public Object[] getSelectedValues() {

        return ((Object[]) getAttribute("value"));

    }


    /**
     * <p>Set the local value of the selected item's values.</p>
     *
     * @param selectedValues The new selected item's value
     */
    public void setSelectedValues(Object selectedValues[]) {

        setAttribute("value", selectedValues);

    }


    // ------------------------------------------- Lifecycle Processing Methods


    /**
     * <p>Decode the new value of this component from the incoming request.</p>
     *
     * @param context FacesContext for the request we are processing
     *
     * @exception IOException if an input/output error occurs while reading
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public void decode(FacesContext context) throws IOException {

        if (context == null) {
            throw new NullPointerException();
        }

        // Delegate to our associated Renderer if needed
        setAttribute(UIInput.PREVIOUS_VALUE, currentValue(context));
        if (getRendererType() != null) {
            super.decode(context);
            return;
        }

        // Perform the default decoding
        String values[] =
            context.getServletRequest().getParameterValues(getClientId(context));
        setValue(values);
        setValid(true);

    }


    /**
     * <p>Render the current value of this component if the value of 
     * the rendered attribute is <code>true</code>. </p>
     *
     * @param context FacesContext for the response we are creating
     *
     * @exception IOException if an input/output error occurs while rendering
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public void encodeEnd(FacesContext context) throws IOException {

        if (context == null) {
            throw new NullPointerException();
        }

        // Delegate to our associated Renderer if needed
        if (getRendererType() != null) {
            super.encodeEnd(context);
            return;
        }
  
        // if rendered is false, do not perform default encoding.
        if (!isRendered()) {
            return;
        }

        // Perform the default encoding
        String values[] = getAsStrings(context, "value", getModelReference());
        Iterator items = getSelectItems(context);

        ResponseWriter writer = context.getResponseWriter();
        writer.write("<select name=\"");
        writer.write(getClientId(context));
        writer.write("\" multiple=\"multiple\">");
        while (items.hasNext()) {
            SelectItem item = (SelectItem) items.next();
            writer.write("<option value=\"");
            writer.write(item.getValue().toString());
            writer.write("\"");
            boolean match = false;
            for (int j = 0; j < values.length; j++) {
                if (values[j].equals(item.getValue())) {
                    match = true;
                    break;
                }
            }
            if (match) {
                writer.write(" selected=\"selected\"");
            }
            writer.write(">");
            writer.write(item.getLabel());
            writer.write("</option>");
        }
        writer.write("</select>");

    }
    
    /**
     * Returns <code>true</code> if the new value is different from the previous
     * value. Value comparison shouldn't be sensitive to element order.
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
    
    /**
     * Returns the number of occurrances of a particular element in the
     * array.
     * @param element object whose occurrance is to be counted in the array.
     * @param array object representing the old value of this component.
     */
    private int countElementOccurance(Object element, Object[] array) {
        int count = 0;
        for ( int i= 0; i < array.length; ++i ) {
            Object arrayElement = array[i];
            if (arrayElement == element) {
                count ++;
            }    
        }    
        return count;
    }    
}
