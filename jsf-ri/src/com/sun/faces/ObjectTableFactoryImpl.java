/*
 * $Id: ObjectTableFactoryImpl.java,v 1.2 2001/12/20 22:26:38 ofung Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
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
 * @version $Id: ObjectTableFactoryImpl.java,v 1.2 2001/12/20 22:26:38 ofung Exp $
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
