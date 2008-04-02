/*
 * $Id: UISelectItem.java,v 1.13 2003/03/13 01:11:59 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
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
 * <li>The <code>valueRef</code> attribute points at a model data
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


    // ------------------------------------------------------------- Properties


    public String getComponentType() {

        return (TYPE);

    }


    /**
     * <p>The description for this selection item.</p>
     */
    private String itemDescription = null;


    /**
     * <p>Return the description for this selection item.</p>
     */
    public String getItemDescription() {

        return (this.itemDescription);

    }


    /**
     * <p>Set the description for this selection item.</p>
     *
     * @param itemDescription The new description
     */
    public void setItemDescription(String itemDescription) {

        this.itemDescription = itemDescription;

    }


    /**
     * <p>The localized label for this selection item.</p>
     */
    private String itemLabel = null;


    /**
     * <p>Return the localized label for this selection item.</p>
     */
    public String getItemLabel() {

        return (this.itemLabel);

    }


    /**
     * <p>Set the localized label for this selection item.</p>
     *
     * @param itemLabel The new localized label
     */
    public void setItemLabel(String itemLabel) {

        this.itemLabel = itemLabel;

    }


    /**
     * <p>The server value for this selection item.</p>
     */
    private String itemValue = null;


    /**
     * <p>Return the server value for this selection item.</p>
     */
    public String getItemValue() {

        return (this.itemValue);

    }


    /**
     * <p>Set the server value for this selection item.</p>
     *
     * @param itemValue The new server value
     */
    public void setItemValue(String itemValue) {

        this.itemValue = itemValue;

    }


    /**
     * <p>Return <code>true</code> to indicate that no
     * {@link javax.faces.render.Renderer} needs to be associated
     * with this component.</p>
     */
    public boolean getRendersSelf() {

        return (true);

    }


}
