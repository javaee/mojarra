/*
 * $Id: ShipTypeBean.java,v 1.3 2003/12/17 15:15:49 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// ShipTypeBean.java
package fruitstand;

import com.sun.faces.util.Util;



import java.util.Collection;
import java.util.Iterator;


/**
 *
 *  <B>ShipTypeBean</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: ShipTypeBean.java,v 1.3 2003/12/17 15:15:49 rkitain Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class ShipTypeBean extends Object
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

// Attribute Instance Variables

// Relationship Instance Variables
    Collection items = null;
    Object currentItem;

//
// Constructors and Initializers    
//

public ShipTypeBean()
{
    super();
    // Util.parameterNonNull();
    this.init();
}

protected void init()
{
    // super.init();
}

//
// Class methods
//

//
// General Methods
//

public void setShipType(Object toAdd) {
    items = (Collection) toAdd;
}

public Object getShipType() {
    return items;
}

public void setCurrentShipType(Object curShipType) {
    currentItem = curShipType;
}

public Object getCurrentShipType() {
    return currentItem;
}


} // end of class ShipTypeBean
