/*
 * $Id: UISelectMany.java,v 1.1 2002/05/22 21:37:02 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.io.IOException;
import java.io.PrintWriter;
import javax.faces.context.FacesContext;


/**
 * <p><strong>UISelectMany</strong> is a {@link UIComponent} that represents
 * the user's choice of a zero or more items from among a discrete set of
 * available options.  The user can modify the selected value.  Optionally,
 * the component can be preconfigured with zero or more currently selected
 * items.  This component is generally rendered as a select box or a group of
 * checkboxes.</p>
 */

public class UISelectMany extends UIComponent {


    // ------------------------------------------------------- Static Variables


    /**
     * The component type of this {@link UIComponent} subclass.
     */
    public static final String TYPE = "SelectMany";


    // ------------------------------------------------------------- Properties


    /**
     * <p>Return the component type of this <code>UIComponent</code>.</p>
     */
    public String getComponentType() {

        return (TYPE);

    }


    /**
     * <p>Return the available items for this component.</p>
     */
    public UISelectItem[] getItems() {

        return ((UISelectItem[]) getAttribute("items"));

    }


    /**
     * <p>Set the available items for this component.</p>
     *
     * @param items The new available items
     */
    public void setItems(UISelectItem items[]) {

        setAttribute("items", items);

    }


    /**
     * <p>Return the model reference expression for the available items
     * for this component.</p>
     */
    public String getItemsModel() {

        return ((String) getAttribute("itemsModel"));

    }


    /**
     * <p>Set the model reference expression for the available items
     * for this component.</p>
     *
     * @param itemsModel The new model reference expression (if any)
     */
    public void setItemsModel(String itemsModel) {

        setAttribute("itemsModel", itemsModel);

    }


    /**
     * <p>Return the local value of the selected item's value.</p>
     */
    public String[] getSelectedValues() {

        return ((String[]) getAttribute("value"));

    }


    /**
     * <p>Set the local value of the selected item's value.</p>
     *
     * @param selectedValues The new selected item's value
     */
    public void setSelectedValues(String selectedValues[]) {

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
        String values[] =
            context.getServletRequest().getParameterValues(getCompoundId());
        setValue(values);

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
    public void encodeBegin(FacesContext context) throws IOException {

        if (context == null) {
            throw new NullPointerException();
        }
        String values[] = (String[]) currentValue(context);
        if (values == null) {
            values = new String[0];
        }
        UISelectItem items[] = getItems();
        if (items == null) {
            items = (UISelectItem[]) context.getModelValue(getItemsModel());
        }
        if (items == null) {
            items = new UISelectItem[0];
        }

        PrintWriter writer = context.getServletResponse().getWriter();
        writer.print("<select name=\"");
        writer.print(getCompoundId());
        writer.print("\" multiple=\"multiple\">");
        for (int i = 0; i < items.length; i++) {
            writer.print("<option value=\"");
            writer.print(items[i].getValue());
            writer.print("\"");
            boolean match = false;
            for (int j = 0; j < values.length; j++) {
                if (values[j].equals(items[i].getValue())) {
                    match = true;
                    break;
                }
            }
            if (match) {
                writer.print(" selected=\"selected\"");
            }
            writer.print(">");
            writer.print(items[i].getLabel());
            writer.print("</option>");
        }
        writer.print("</select>");

    }


}
