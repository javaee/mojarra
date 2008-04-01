/*
 * $Id: ObjectTableFactoryImpl.java,v 1.1 2001/11/21 00:23:00 edburns Exp $
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

// ObjectTableFactoryImpl.java

package com.sun.faces;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

import javax.servlet.ServletRequest;
import javax.faces.ObjectTableFactory;
import javax.faces.ObjectTable;
import javax.faces.FacesException;

/**
 *
 *  <B>ObjectTableFactoryImpl</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: ObjectTableFactoryImpl.java,v 1.1 2001/11/21 00:23:00 edburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class ObjectTableFactoryImpl extends ObjectTableFactory
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

//
// Constructors and Initializers    
//

public ObjectTableFactoryImpl()
{
    super();
}

//
// Methods from ObjectTableFactory
//

//
// Class methods
//

public ObjectTable newObjectTable() throws FacesException {
    ObjectTable result = new ObjectTableImpl();
    return result;
}

//
// General Methods
//

} // end of class ObjectTableFactoryImpl
