/*
 * $Id: UISelectOne.java,v 1.23 2003/02/03 22:57:48 craigmcc Exp $
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
 * the component can be preconfigured with a currently selected item, by
 * storing it as the <code>value</code> property of the component.</p>
 *
 * <p>This component is generally rendered as a select box or a group of
 * radio buttons.</p>
 */

public class UISelectOne extends UISelectBase {


    // ------------------------------------------------------- Static Variables


    /**
     * The component type of this {@link UIComponent} subclass.
     */
    public static final String TYPE = "javax.faces.component.UISelectOne";


    // ------------------------------------------------------------- Properties


    public String getComponentType() {

        return (TYPE);

    }


    /**
     * <p>Return the currently selected item, or <code>null</code> if there
     * is no currently selected item.</p>
     */
    public Object getSelectedValue() {

        return (getAttribute("value"));

    }


    /**
     * <p>Set the currently selected item, or <code>null</code> to indicate
     * that there is no currently selected item.</p>
     *
     * @param selectedItem The new selected item (if any)
     */
    public void setSelectedValue(Object selectedItem) {

        setAttribute("value", selectedItem);

    }


    // ---------------------------------------------------- UIComponent Methods


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
