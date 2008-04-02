/*
 * $Id: Util.java,v 1.1 2003/02/13 23:34:21 horwat Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// Util.java

package nonjsp.util;

/**
 *
 * <B>Util</B> is a class ...
 *
 * Copy of com.sun.faces.util.Util in order to remove
 * demo dependancy on RI.
 *
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: Util.java,v 1.1 2003/02/13 23:34:21 horwat Exp $
 * 
 * @see	com.sun.faces.util.Util
 *
 */

public class Util extends Object
{
//
// Protected Constants
//

//
// Class Variables
//

private static long id = 0;


//
// Instance Variables
//

// Attribute Instance Variables

// Relationship Instance Variables

//
// Constructors and Initializers    
//

private Util()
{
    throw new IllegalStateException();
}

//
// Class methods
//
    public static Class loadClass(String name) throws ClassNotFoundException {
	ClassLoader loader =
	    Thread.currentThread().getContextClassLoader();
	if (loader == null) {
	    return Class.forName(name);
	}
	else {
	    return loader.loadClass(name);
	}
    }

    /**
     * Generate a new identifier currently used to uniquely identify
     * components.
     */
    public static synchronized String generateId() {
        if (id == Long.MAX_VALUE) {
            id = 0;
        } else { 
            id++;
        }
        return Long.toHexString(id);
    }

//
// General Methods
//

} // end of class Util
