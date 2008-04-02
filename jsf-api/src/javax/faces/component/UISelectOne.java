/*
 * $Id: UISelectOne.java,v 1.22 2003/01/23 17:42:49 rkitain Exp $
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
 * <p><strong>UISelectOne</strong> is a {@link UIComponent} that represents
 * the user's choice of a single item from among a discrete set of
 * available options.  The user can modify the selected value.  Optionally,
 * the component can be preconfigured with a currently selected item.  This
 * component is generally rendered as a select box or a group of
 * radio buttons.</p>
 */

public class UISelectOne extends UISelectBase {


    // ------------------------------------------------------- Static Variables


    /**
     * The component type of this {@link UIComponent} subclass.
     */
    public static final String TYPE = "javax.faces.component.UISelectOne";


    // ------------------------------------------------------------- Properties


    /**
     * <p>Return the component type of this <code>UIComponent</code>.</p>
     */
    public String getComponentType() {

        return (TYPE);

    }


    /**
     * <p>Return the local value of the selected item's value.</p>
     */
    public Object getSelectedValue() {

        return (getAttribute("value"));

    }


    /**
     * <p>Set the local value of the selected item's value.</p>
     *
     * @param selectedValue The new selected item's value
     */
    public void setSelectedValue(Object selectedValue) {

        setAttribute("value", selectedValue);

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
        String value =
            context.getServletRequest().getParameter(getClientId(context));
        setValue(value);
        setValid(true);

    }


    /**
     * <p>Render the current value of this component if the value of the 
     * rendered attribute is <code>true</code>. </p>
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
        Object oldValue = currentValue(context);
        if (oldValue == null) {
            oldValue = "";
        }
        String value = getAsString(context, "value", getModelReference());
        Iterator items = getSelectItems(context);

        ResponseWriter writer = context.getResponseWriter();
        writer.write("<select name=\"");
        writer.write(getClientId(context));
        writer.write("\">");
        while (items.hasNext()) {
            SelectItem item = (SelectItem) items.next();
            writer.write("<option value=\"");
            writer.write(item.getValue().toString());
            writer.write("\"");
            if (value.equals(item.getValue())) {
                writer.write(" selected=\"selected\"");
            }
            writer.write(">");
            writer.write(item.getLabel());
            writer.write("</option>");
        }
        writer.write("</select>");

    }


}
