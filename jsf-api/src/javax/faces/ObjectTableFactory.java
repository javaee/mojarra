/*
 * $Id: ObjectTableFactory.java,v 1.1 2001/11/21 00:17:44 edburns Exp $
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

// ObjectTableFactory.java

package javax.faces;

import javax.servlet.ServletRequest;

/**
 *

 *  <B>ObjectTableFactory</B> Creates ObjectTable instances using
 *  the pluggable implementation scheme defined in the spec. <P>

 *
 * <B>Lifetime And Scope</B> <P>

 * There should be one instance of ObjectTableFactory per app. <P></P>

 * It is created and used like this: <P></P>

<CODE><PRE>
    RenderKit kit = null;
    ObjectTable context;
    ObjectTableFactory factory;
    Renderer renderer;
    
    try {
	factory = ObjectTableFactory.newInstance();
	context = factory.newObjectTable(servletRequest);
	kit = context.getRenderKit();
    }
    catch (Exception e) {
	System.out.println("Exception getting factory!!! " + e.getMessage());
    }

</PRE></CODE>

 *
 * @version $Id: ObjectTableFactory.java,v 1.1 2001/11/21 00:17:44 edburns Exp $
 * 
 * @see	javax.faces.ObjectTable
 *
 */

public abstract class ObjectTableFactory extends Object
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

protected ObjectTableFactory()
{
    super();
}

//
// Class methods
//

public static ObjectTableFactory newInstance() throws FactoryConfigurationError  {
    try {
	return (ObjectTableFactory) FactoryFinder.find(
	   /* The default property name according to the JSFaces spec */
	   "javax.faces.ObjectTableFactory",
	   /* The fallback implementation class name */
	   "com.sun.faces.ObjectTableFactoryImpl");
    } catch (FactoryFinder.ConfigurationError e) {
	throw new FactoryConfigurationError(e.getException(),
					    e.getMessage());
    }
}


//
// Abstract Methods 
//

/**
  * @throws FacesException if any of these objects could not be
  *         created.
  */

public abstract ObjectTable newObjectTable() throws FacesException;

} // end of class ObjectTableFactory
