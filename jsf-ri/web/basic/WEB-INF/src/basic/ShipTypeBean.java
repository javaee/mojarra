/*
 * $Id: ShipTypeBean.java,v 1.3 2002/01/25 18:45:20 visvan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// ShipTypeBean.java
package basic;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.ParameterCheck;

import java.util.Collection;
import java.util.Iterator;


/**
 *
 *  <B>ShipTypeBean</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: ShipTypeBean.java,v 1.3 2002/01/25 18:45:20 visvan Exp $
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
    // ParameterCheck.nonNull();
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
    System.out.println("set current ship type " + curShipType);
    currentItem = curShipType;
}

public Object getCurrentShipType() {
    return currentItem;
}


} // end of class ShipTypeBean
