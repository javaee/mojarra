/*
 * $Id: UISelectItemSub.java,v 1.1 2003/09/25 07:46:12 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import javax.faces.component.UISelectItem;


// Subclass of UISelectItem to provide a convenience constructor

public class UISelectItemSub extends UISelectItem {

    public UISelectItemSub(String value, String label, String description) {
        super();
        setItemValue(value);
        setItemLabel(label);
        setItemDescription(description);
    }


}
