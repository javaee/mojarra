/*
 * $Id: TestMethodRef.java,v 1.2 2003/12/17 15:15:18 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestMethodRef.java

package com.sun.faces.el;

import com.sun.faces.ServletFacesTestCase;

import javax.faces.el.MethodNotFoundException;

/**
 *
 *  <B>TestMethodRef</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestMethodRef.java,v 1.2 2003/12/17 15:15:18 rkitain Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestMethodRef extends ServletFacesTestCase
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

    public TestMethodRef() {
	super("TestMethodRef");
    }

    public TestMethodRef(String name) {
	super(name);
    }

//
// Class methods
//

//
// General Methods
//


public void testInvalidTrailing() throws Exception {    

    MethodBindingImpl mb = 
	new MethodBindingImpl(getFacesContext().getApplication(),
			      "NewCustomerFormHandler.redLectroidsMmmm", 
			      new Class[0]);

    boolean exceptionThrown = false;
    try {
	mb.invoke(getFacesContext(), new Object[0]);
    }
    catch (MethodNotFoundException e) {
	exceptionThrown = true;
    }
    assertTrue(exceptionThrown);

    mb = new MethodBindingImpl(getFacesContext().getApplication(),
			       "nonexistentBean.redLectroidsMmmm", 
			       new Class[0]);
    exceptionThrown = false;
    try {
	mb.invoke(getFacesContext(), new Object[0]);
    }
    catch (MethodNotFoundException e) {
	exceptionThrown = true;
    }
    assertTrue(exceptionThrown);
}

} // end of class TestMethodRef
