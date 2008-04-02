/*
 * $Id: UISelectItemBase.java,v 1.3 2003/07/28 22:18:47 eburns Exp $
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

    // --------------------------------------------- methods from StateHolder

    public void restoreState(FacesContext context, 
			     Object stateObj) throws IOException {
	Object [] state = (Object []) stateObj;
	String stateStr = (String) state[THIS_INDEX];
	int i = stateStr.indexOf(STATE_SEP), j;
	itemDescription = stateStr.substring(0, i);
	if (itemDescription.equals("null")) {
	    itemDescription = null;
	}

	j = stateStr.indexOf(STATE_SEP, i + STATE_SEP_LEN);
	itemLabel = stateStr.substring(i + STATE_SEP_LEN, j);
	if (itemLabel.equals("null")) {
	    itemLabel = null;
	}

	itemValue = stateStr.substring(j + STATE_SEP_LEN);
	if (itemValue.equals("null")) {
	    itemValue = null;
	}

	super.restoreState(context, state[SUPER_INDEX]);
    }

    public Object getState(FacesContext context) {
	Object superState = super.getState(context);
	Object [] result = new Object[2];
	result[THIS_INDEX] = itemDescription + STATE_SEP + itemLabel + 
	    STATE_SEP + itemValue;
	result[SUPER_INDEX] = superState;
	return result;
    }



}
