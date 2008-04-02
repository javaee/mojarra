/*
 * $Id: UISelectItemSub.java,v 1.2 2004/02/04 23:38:49 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
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
