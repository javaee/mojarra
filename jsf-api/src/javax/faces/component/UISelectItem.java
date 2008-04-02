/*
 * $Id: UISelectItem.java,v 1.11 2003/02/03 22:57:47 craigmcc Exp $
 */

/*
 * Copyright 2002-2003 Sun Microsystems, Inc. All rights reserved.
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
 */

public class UISelectItem extends UIOutput {


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


    public String getComponentType() {

        return (TYPE);

    }


    /**
     * <p>Return <code>true</code> to indicate that no
     * {@link javax.faces.render.Renderer} needs to be associated
     * with this component.</p>
     */
    public boolean getRendersSelf() {

        return (true);

    }


    // ---------------------------------------------------- UIComponent Methods


    /**
     * <p>Override the default behavior and perform no encoding.</p>
     *
     * @param context {@link FacesContext} for the request we are processing
     *
     * @exception IOException if an input/output error occurs while reading
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public void encodeBegin(FacesContext context) throws IOException {

        if (context == null) {
            throw new NullPointerException();
        }

    }


    /**
     * <p>Override the default behavior and perform no encoding.</p>
     *
     * @param context {@link FacesContext} for the request we are processing
     *
     * @exception IOException if an input/output error occurs while reading
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public void encodeChildren(FacesContext context) throws IOException {

        if (context == null) {
            throw new NullPointerException();
        }

    }


    /**
     * <p>Override the default behavior and perform no encoding.</p>
     *
     * @param context {@link FacesContext} for the request we are processing
     *
     * @exception IOException if an input/output error occurs while reading
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public void encodeEnd(FacesContext context) throws IOException {

        if (context == null) {
            throw new NullPointerException();
        }

    }


}
