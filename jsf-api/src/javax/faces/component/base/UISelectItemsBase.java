/*
 * $Id: UISelectItemsBase.java,v 1.2 2003/07/26 17:54:52 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component.base;


import javax.faces.component.UISelectItems;


/**
 * <p><strong>UISelectItemsBase</strong> is a convenience base class that
 * implements the default concrete behavior of all methods defined by
 * {@link UISelectItems}.</p>
 */

public class UISelectItemsBase extends UIOutputBase implements UISelectItems {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Create a new {@link UISelectItemsBase} instance with default property
     * values.</p>
     */
    public UISelectItemsBase() {

        super();
        setRendererType(null);

    }


}
