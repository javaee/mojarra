/*
 * $Id: UISelectItemSub.java,v 1.1 2003/08/28 21:08:56 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component.base;


import javax.faces.component.base.UISelectItemBase;


// Subclass of UISelectItemBase to provide a convenience constructor

public class UISelectItemSub extends UISelectItemBase {

    public UISelectItemSub(String value, String label, String description) {
        super();
        setItemValue(value);
        setItemLabel(label);
        setItemDescription(description);
    }


}
