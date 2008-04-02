/*
 * $Id: UISelectItem.java,v 1.10 2003/01/17 02:18:08 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.io.IOException;
import javax.faces.context.FacesContext;


/**
 * <p><strong>UISelectItem</strong> is a component that may be nested
 * inside a {@link UISelectMany} or {@link UISelectOne} component, and
 * causes the addition of a {@link SelectItem} instance to the list of
 * available options for the parent component.  The contents of the
 * {@link SelectItem} can be specified in one of the following ways:</p>
 * <ul>
 * <li>The <code>value</code> attribute's value is an instance of
 *     {@link SelectItem}.</li>
 * <li>The <code>modelReference</code> attribute points at a model data
 *     item of type {@link SelectItem}.</li>
 * <li>A new {@link SelectItem} instance is synthesized from the values
 *     of the <code>itemDescription</code>, <code>itemLabel</code>, and
 *     <code>itemValue</code> attributes.</li>
 * </ul>
 *
 * <p>This component has no decode or encode behavior of its own.</p>
 */

public class UISelectItem extends UIComponentBase {


    // ------------------------------------------------------- Static Variables


    /**
     * The component type of this {@link UIComponent} subclass.
     */
    public static final String TYPE = "javax.faces.component.UISelectItem";


    // ------------------------------------------------------------- Attributes


    /**
     * <p>Return the item description for this component (if <code>value</code>
     * and <code>modelReference</code> are <code>null</code>).</p>
     */
    public String getItemDescription() {

        return ((String) getAttribute("itemDescription"));

    }


    /**
     * <p>Set the item description for this component (if <code>value</code>
     * and <code>modelReference</code> are <code>null</code>).</p>
     */
    public void setItemDescription(String itemDescription) {

        setAttribute("itemDescription", itemDescription);

    }


    /**
     * <p>Return the item label for this component (if <code>value</code>
     * and <code>modelReference</code> are <code>null</code>).</p>
     */
    public String getItemLabel() {

        return ((String) getAttribute("itemLabel"));

    }


    /**
     * <p>Set the item label for this component (if <code>value</code>
     * and <code>modelReference</code> are <code>null</code>).</p>
     */
    public void setItemLabel(String itemLabel) {

        setAttribute("itemLabel", itemLabel);

    }


    /**
     * <p>Return the item value for this component (if <code>value</code>
     * and <code>modelReference</code> are <code>null</code>).</p>
     */
    public String getItemValue() {

        return ((String) getAttribute("itemValue"));

    }


    /**
     * <p>Set the item value for this component (if <code>value</code>
     * and <code>modelReference</code> are <code>null</code>).</p>
     */
    public void setItemValue(String itemValue) {

        setAttribute("itemValue", itemValue);

    }


    // ------------------------------------------------------------- Properties


    /**
     * <p>Return the component type of this <code>UIComponent</code>.</p>
     */
    public String getComponentType() {

        return (TYPE);

    }


    /**
     * <p>Return the <code>rendersSelf</code> value for this component.</p>
     */
    public boolean getRendersSelf() {

        return (true);

    }


    /**
     * <p>Set the <code>valid</code> property to <code>true</code>.</p>
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
        setValid(true);

    }


    /**
     * <p>Override the default behavior and take no action.</p>
     *
     * @param context FacesContext for the request we are processing
     *
     * @exception IOException if an input/output error occurs while reading
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public void encodeBegin(FacesContext context) throws IOException {

        if (context == null) {
            throw new NullPointerException();
        }

        // No encoding is performed

    }


    /**
     * <p>Override the default behavior and take no action.</p>
     *
     * @param context FacesContext for the request we are processing
     *
     * @exception IOException if an input/output error occurs while reading
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public void encodeChildren(FacesContext context) throws IOException {

        if (context == null) {
            throw new NullPointerException();
        }

        // No encoding is performed

    }


    /**
     * <p>Override the default behavior and take no action.</p>
     *
     * @param context FacesContext for the request we are processing
     *
     * @exception IOException if an input/output error occurs while reading
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public void encodeEnd(FacesContext context) throws IOException {

        if (context == null) {
            throw new NullPointerException();
        }

        // No encoding is performed

    }


    /**
     * <p>Suppress model updates for this component.</p>
     *
     * @param context FacesContext for the request we are processing
     *
     * @exception IllegalArgumentException if the <code>modelReference</code>
     *  property has invalid syntax for an expression
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public void updateModel(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }

    }


}
