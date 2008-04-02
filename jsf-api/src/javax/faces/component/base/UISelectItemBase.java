/*
 * $Id: UISelectItemBase.java,v 1.4 2003/08/27 00:56:51 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component.base;


import javax.faces.context.FacesContext;
import javax.faces.component.UISelectItem;

import java.io.IOException;


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


    // ----------------------------------------------------- StateHolder Methods


    public Object getState(FacesContext context) {

        Object values[] = new Object[4];
        values[0] = super.getState(context);
        values[1] = itemDescription;
        values[2] = itemLabel;
        values[3] = itemValue;
        return (values);

    }


    public void restoreState(FacesContext context, Object state)
        throws IOException {

        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        itemDescription = (String) values[1];
        itemLabel = (String) values[2];
        itemValue = (String) values[3];

    }


}
