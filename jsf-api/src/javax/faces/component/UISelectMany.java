/*
 * $Id: UISelectMany.java,v 1.12 2002/07/30 22:51:36 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
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
        if (getRendererType() != null) {
            super.decode(context);
            return;
        }

        // Perform the default decoding
        String values[] =
            context.getServletRequest().getParameterValues(getCompoundId());
        setValue(values);
        setValid(true);

    }


    /**
     * <p>Render the current value of this component.</p>
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

        // Perform the default encoding
        String values[] = getAsStrings(context, "value", getModelReference());
        Iterator items = getSelectItems(context);

        ResponseWriter writer = context.getResponseWriter();
        writer.write("<select name=\"");
        writer.write(getCompoundId());
        writer.write("\" multiple=\"multiple\">");
        while (items.hasNext()) {
            SelectItem item = (SelectItem) items.next();
            writer.write("<option value=\"");
            writer.write(item.getValue());
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


}
