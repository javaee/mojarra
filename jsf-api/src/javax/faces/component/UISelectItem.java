/*
 * $Id: UISelectItem.java,v 1.16 2003/08/30 00:31:32 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import javax.faces.model.SelectItem;


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

public interface UISelectItem extends UIComponent, ValueHolder {


    // -------------------------------------------------------------- Properties


    /**
     * <p>Return the description for this selection item.</p>
     */
    public String getItemDescription();


    /**
     * <p>Set the description for this selection item.</p>
     *
     * @param itemDescription The new description
     */
    public void setItemDescription(String itemDescription);


    /**
     * <p>Return the localized label for this selection item.</p>
     */
    public String getItemLabel();


    /**
     * <p>Set the localized label for this selection item.</p>
     *
     * @param itemLabel The new localized label
     */
    public void setItemLabel(String itemLabel);


    /**
     * <p>Return the server value for this selection item.</p>
     */
    public String getItemValue();


    /**
     * <p>Set the server value for this selection item.</p>
     *
     * @param itemValue The new server value
     */
    public void setItemValue(String itemValue);


}
