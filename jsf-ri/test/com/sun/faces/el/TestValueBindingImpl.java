/*
 * $Id: TestValueBindingImpl.java,v 1.1 2003/03/24 19:45:38 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestValueBindingImpl.java

package com.sun.faces.el;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.ParameterCheck;

import com.sun.faces.ServletFacesTestCase;
import com.sun.faces.TestBean;
import com.sun.faces.TestBean.InnerBean;
import com.sun.faces.TestBean.Inner2Bean;

import org.apache.cactus.WebRequest;

/**
 *
 *  <B>TestValueBindingImpl</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestValueBindingImpl.java,v 1.1 2003/03/24 19:45:38 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestValueBindingImpl extends ServletFacesTestCase
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

    public TestValueBindingImpl() {super("TestFacesContext");}
    public TestValueBindingImpl(String name) {super(name);}
//
// Class methods
//

//
// Methods from TestCase
//

//
// General Methods
//

public void beginEL(WebRequest theRequest) 
{
    theRequest.addHeader("ELHeader", "ELHeader");
    theRequest.addHeader("multiheader", "1");
    theRequest.addHeader("multiheader", "2");
    theRequest.addParameter("ELParam", "ELParam");
    theRequest.addParameter("multiparam", "one");
    theRequest.addParameter("multiparam", "two");
    theRequest.addCookie("cookie", "monster");
}

    public void testEl() {
	TestBean testBean = new TestBean();
	InnerBean newInner, oldInner = new InnerBean();
	testBean.setInner(oldInner);
	ValueBindingImpl valueBinding = new ValueBindingImpl(new VariableResolverImpl(),
							     new PropertyResolverImpl());
	Object result = null;

	//
	// Get tests
	//
	
	// Simple tests, verify that bracket and dot operators work
	valueBinding.setRef("TestBean.inner");
	getFacesContext().getExternalContext().getSessionMap().put("TestBean", 
								   testBean);
	result = valueBinding.getValue(getFacesContext());
	assertTrue(result == oldInner);
	
    }


} // end of class TestValueBindingImpl
