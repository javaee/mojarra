/*
 * $Id: Util.java,v 1.2 2005/08/22 22:09:24 ofung Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

// Util.java

package nonjsp.util;

/**
 * <B>Util</B> is a class ...
 *
 * Copy of com.sun.faces.util.Util in order to remove
 * demo dependancy on RI.
 *
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: Util.java,v 1.2 2005/08/22 22:09:24 ofung Exp $
 * @see	com.sun.faces.util.Util
 */

public class Util extends Object {

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

    private Util() {
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
        } else {
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
