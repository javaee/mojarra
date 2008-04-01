/*
 * $Id: UISelectOne.java,v 1.7 2002/06/07 20:36:06 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.io.IOException;
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

public class UISelectOne extends UIComponent {


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
     * <p>Return the available items for this component.</p>
     */
    public SelectItem[] getItems() {

        return ((SelectItem[]) getAttribute("items"));

    }


    /**
     * <p>Set the available items for this component.</p>
     *
     * @param items The new available items
     */
    public void setItems(SelectItem items[]) {

        setAttribute("items", items);

    }


    /**
     * <p>Return the model reference expression for the available items
     * for this component.</p>
     */
    public String getItemsModelReference() {

        return ((String) getAttribute("itemsModelReference"));

    }


    /**
     * <p>Set the model reference expression for the available items
     * for this component.</p>
     *
     * @param itemsModelReference The new model reference expression (if any)
     */
    public void setItemsModelReference(String itemsModelReference) {

        setAttribute("itemsModelReference", itemsModelReference);

    }


    /**
     * <p>Return the local value of the selected item's value.</p>
     */
    public String getSelectedValue() {

        return ((String) getAttribute("value"));

    }


    /**
     * <p>Set the local value of the selected item's value.</p>
     *
     * @param selectedValue The new selected item's value
     */
    public void setSelectedValue(String selectedValue) {

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
        String value =
            context.getServletRequest().getParameter(getCompoundId());
        setValue(value);

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
        Object oldValue = currentValue(context);
        if (oldValue == null) {
            oldValue = "";
        }
        String value = oldValue.toString();
        SelectItem items[] = getItems();
        if (items == null) {
            items = (SelectItem[])
                context.getModelValue(getItemsModelReference());
        }
        if (items == null) {
            items = new SelectItem[0];
        }

        ResponseWriter writer = context.getResponseWriter();
        writer.write("<select name=\"");
        writer.write(getCompoundId());
        writer.write("\">");
        for (int i = 0; i < items.length; i++) {
            writer.write("<option value=\"");
            writer.write(items[i].getValue());
            writer.write("\"");
            if (value.equals(items[i].getValue())) {
                writer.write(" selected=\"selected\"");
            }
            writer.write(">");
            writer.write(items[i].getLabel());
            writer.write("</option>");
        }
        writer.write("</select>");

    }


}
