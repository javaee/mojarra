/*
 * $Id: UISelectItemBase.java,v 1.2 2003/07/26 17:54:52 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component.base;


import javax.faces.component.UISelectItem;


/**
 * <p><strong>UISelectItemBase</strong> is a convenience base class that
 * implements the default concrete behavior of all methods defined by
 * {@link UISelectItem}.</p>
 */

public class UISelectItemBase extends UIOutputBase implements UISelectItem {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Create a new {@link UISelectItemBase} instance with default property
     * values.</p>
     */
    public UISelectItemBase() {

        super();
        setRendererType(null);

    }


    // -------------------------------------------------------------- Properties


    /**
     * <p>The description for this selection item.</p>
     */
    private String itemDescription = null;


    public String getItemDescription() {

        return (this.itemDescription);

    }


    public void setItemDescription(String itemDescription) {

        this.itemDescription = itemDescription;

    }


    /**
     * <p>The localized label for this selection item.</p>
     */
    private String itemLabel = null;


    public String getItemLabel() {

        return (this.itemLabel);

    }


    public void setItemLabel(String itemLabel) {

        this.itemLabel = itemLabel;

    }


    /**
     * <p>The server value for this selection item.</p>
     */
    private String itemValue = null;


    public String getItemValue() {

        return (this.itemValue);

    }


    public void setItemValue(String itemValue) {

        this.itemValue = itemValue;

    }


}
