/*
 * $Id: SelectItemWrapper.java,v 1.3 2003/08/19 19:31:33 rlubke Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.util;

import javax.faces.component.UIComponent;
import javax.faces.model.SelectItem;

/**
  * This class is a wrapper around UISelectItem/UISelectItems and SelectItem bean.
  * This facilitates retrieval of HTML 4.0 attributes (set through tags) at
  * render time.
  */
public class SelectItemWrapper extends Object
{
    //
    // Protected Constants
    //

    //
    // Class Variables
    //

    //
    // Instance Variables
    //
    private UIComponent selectItem;
    private SelectItem selectItemBean;
    
    // Attribute Instance Variables

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public SelectItemWrapper(UIComponent item, SelectItem itemBean ) {
        super();
        this.selectItem = item;
        this.selectItemBean = itemBean;
    }
    
    //
    // Class methods
    //

    //
    // General Methods
    //
    public UIComponent getUISelectItem() {
        return selectItem;
    }
    
    public SelectItem getSelectItem() {
        return selectItemBean;
    }    

} // end of class SelectItenWrapper
