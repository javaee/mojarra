/*
 * $Id: TestObjectAccessor.java,v 1.4 2002/04/17 19:19:42 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestObjectAccessor.java

package com.sun.faces;

import javax.servlet.http.HttpSession;

import javax.faces.ObjectManager;
import javax.faces.ObjectAccessor;

/**
 *
 *  <B>TestObjectAccessor</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestObjectAccessor.java,v 1.4 2002/04/17 19:19:42 jvisvanathan Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestObjectAccessor extends FacesTestCase
{
//
// Protected Constants
//

//
// Class Variables
//

private static final int INITIAL_NUM_SCOPES = 3;

//
// Instance Variables
//

// Attribute Instance Variables

// Relationship Instance Variables

//
// Constructors and Initializers    
//

    public TestObjectAccessor() {super("TestObjectAccessor");}
    public TestObjectAccessor(String name) {super(name);}
//
// Class methods
//

//
// Methods from TestCase
//

//
// General Methods
//

    public void testSetGetObject() {
        boolean result;

        String modelReference;
        String value;
        Object object = null;
	ObjectAccessor objectAccessor = null;

	result = null != (objectAccessor = facesContext.getObjectAccessor());
	System.out.println("Get an objectAccessor from the facesContext: " + 
			   result);
	assertTrue(result);
 
        try {

            // use this for debugging
            //com.sun.faces.util.DebugUtil.waitForDebugger();

            objectManager.bind(ObjectManager.GlobalScope, "user", User.class);
    
            // 1. Use model reference string with '$' format
       
            // Here's the value we're setting
            //
            modelReference = "${user.address.street}";
            value = "2340 Linwood Avenue";

            // Set the model object value
            //
            objectAccessor.setObject(request, modelReference, value);
            System.out.println("Model Property Set.");

            // Get the model object value
            //
            object = objectAccessor.getObject(request, modelReference); 
            System.out.println("Returned Object:"+object);
    
            result = value == object;
            System.out.println("Testing Set/Get methods, returned: " + result);
            assertTrue(result);

            // 2. Use model reference string that doesn't refer to a nested
            // property.

            result = false;

            // Here's the value we're setting
            //
            modelReference = "${user}";
            value = "2340 Linwood Avenue"; 

            // Set the model object value
            //
            objectAccessor.setObject(request, modelReference, value);
            System.out.println("Model Property Set.");

            // Get the model object value
            //
            object = objectAccessor.getObject(request, modelReference); 
            System.out.println("Returned Object:"+object);

            result = value == object;
            System.out.println("Testing Set/Get methods, returned: " + result);
            assertTrue(result);

        } catch (Exception e) {
            System.out.println("EXCEPTION:"+e.getMessage());
	    assertTrue(false);
        }
    }

} // end of class TestObjectAccessor
