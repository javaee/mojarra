/*
 * $Id: ShipTypeBean.java,v 1.1 2001/12/12 00:24:43 edburns Exp $
 *
 * Copyright 2000-2001 by Sun Microsystems, Inc.,
 * 901 San Antonio Road, Palo Alto, California, 94303, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Sun Microsystems, Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Sun.
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
 * @version $Id: ShipTypeBean.java,v 1.1 2001/12/12 00:24:43 edburns Exp $
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
    currentItem = curShipType;
}

public Object getCurrentShipType() {
    return currentItem;
}


} // end of class ShipTypeBean
