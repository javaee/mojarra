/*
 * $Id: TestValueBindingImpl.java,v 1.2 2003/03/28 04:39:21 eburns Exp $
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

import javax.servlet.http.Cookie;

import java.util.Enumeration;

/**
 *
 *  <B>TestValueBindingImpl</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestValueBindingImpl.java,v 1.2 2003/03/28 04:39:21 eburns Exp $
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

    public void populateRequest(WebRequest theRequest) {
	theRequest.addHeader("ELHeader", "ELHeader");
	theRequest.addHeader("multiheader", "1");
	theRequest.addHeader("multiheader", "2");
	theRequest.addParameter("ELParam", "ELParam");
	theRequest.addParameter("multiparam", "one");
	theRequest.addParameter("multiparam", "two");
	theRequest.addCookie("cookie", "monster");
    }
	
    public void beginELGet(WebRequest theRequest) {
	populateRequest(theRequest);
    }

    public void testELGet() {
	TestBean testBean = new TestBean();
	InnerBean newInner, oldInner = new InnerBean();
	testBean.setInner(oldInner);
	ValueBindingImpl valueBinding = new ValueBindingImpl(getFacesContext(),
							     new VariableResolverImpl(),
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

	valueBinding.setRef("TestBean[\"inner\"]");
	result = valueBinding.getValue(getFacesContext());
	assertTrue(result == oldInner);
	
	valueBinding.setRef("TestBean[\"inner\"].customers[1]");
	result = valueBinding.getValue(getFacesContext());
	assertTrue(result instanceof String);
	assertTrue(result.equals("Jerry"));
	
	// try out the implicit objects
	valueBinding.setRef("sessionScope.TestBean.inner");
	result = valueBinding.getValue(getFacesContext());
	assertTrue(result == oldInner);

	valueBinding.setRef("header.ELHeader");
	result = valueBinding.getValue(getFacesContext());
	assertTrue(getFacesContext().getExternalContext().getRequest() == 
		   request);
	assertTrue(request.getHeader("ELHeader").equals("ELHeader"));
	assertTrue(result.equals("ELHeader"));
	
	valueBinding.setRef("param.ELParam");
	result = valueBinding.getValue(getFacesContext());
	assertTrue(result.equals("ELParam"));
	
	String multiparam[] = null;
	valueBinding.setRef("paramValues.multiparam");
	multiparam = (String[])
	    valueBinding.getValue(getFacesContext());
	assertTrue(null != multiparam);
	assertTrue(2 == multiparam.length);
	assertTrue(multiparam[0].equals("one"));
	assertTrue(multiparam[1].equals("two"));

	valueBinding.setRef("headerValues.multiheader");
	Enumeration multiHeader = (Enumeration) 
	    valueBinding.getValue(getFacesContext());
	assertTrue(null != multiHeader);
	int elements = 0;
	while (multiHeader.hasMoreElements()) {
	    elements++;
	    // PENDING(edburns): due to an apparent bug in cactus, multiple
	    // calls to WebRequest.addHeader() still result in all the values
	    // being concatenated together into a comma separated String.
	    String element = (String)multiHeader.nextElement();
	    assertTrue(element.equals("1,2"));
	}
	assertTrue(1 == elements);
	
	valueBinding.setRef("initParam.saveStateInClient");
	result = valueBinding.getValue(getFacesContext());
	assertTrue(null != result);
	assertTrue(result.equals("false") || result.equals("true"));
	
	valueBinding.setRef("cookie.cookie");
	result = valueBinding.getValue(getFacesContext());
	assertTrue(null != result);
	assertTrue(result instanceof Cookie);
	assertTrue(((Cookie) result).getValue().equals("monster"));
	
    }

    public void beginELSet(WebRequest theRequest) {
	populateRequest(theRequest);
    }

    public void testELSet() {
	TestBean testBean = new TestBean();
	InnerBean newInner, oldInner = new InnerBean();
	testBean.setInner(oldInner);
	ValueBindingImpl valueBinding = new ValueBindingImpl(getFacesContext(),
							     new VariableResolverImpl(),
							     new PropertyResolverImpl());
	Object result = null;

	//
	// Set tests
	//

	newInner = new InnerBean();
	valueBinding.setRef("TestBean.inner");
	valueBinding.setValue(getFacesContext(), newInner);
	result = valueBinding.getValue(getFacesContext());
	assertTrue(result == newInner);
	assertTrue(oldInner != newInner);
	
	oldInner = newInner;
	newInner = new InnerBean();
	valueBinding.setRef("TestBean[\"inner\"]");
	valueBinding.setValue(getFacesContext(), newInner);
	result = valueBinding.getValue(getFacesContext());
	assertTrue(result == newInner);
	assertTrue(oldInner != newInner);

	String 
	    oldCustomer0 = null,
	    oldCustomer1 = null,
	    customer0 = null,
	    customer1 = null;
	
	valueBinding.setRef("TestBean[\"inner\"].customers[0]");
	oldCustomer0 = customer0 = (String) valueBinding.getValue(getFacesContext());
	valueBinding.setRef("TestBean[\"inner\"].customers[1]");
	oldCustomer1 = customer1 = (String) valueBinding.getValue(getFacesContext());
	
	valueBinding.setRef("TestBean[\"inner\"].customers[0]");
	valueBinding.setValue(getFacesContext(), "Jerry");
	valueBinding.setRef("TestBean[\"inner\"].customers[1]");
	valueBinding.setValue(getFacesContext(), "Mickey");

	valueBinding.setRef("TestBean[\"inner\"].customers[0]");
	customer0 = (String) valueBinding.getValue(getFacesContext());
	valueBinding.setRef("TestBean[\"inner\"].customers[1]");
	customer1 = (String) valueBinding.getValue(getFacesContext());
	assertTrue(customer0.equals("Jerry"));
	assertTrue(customer1.equals("Mickey"));

	valueBinding.setRef("TestBean[\"inner\"].customers[0]");
	assertTrue(valueBinding.getValue(getFacesContext()) != oldCustomer0);
	valueBinding.setRef("TestBean[\"inner\"].customers[1]");
	assertTrue(valueBinding.getValue(getFacesContext()) != oldCustomer1);
	
	// put in a map to the customers Collection
	Inner2Bean inner2 = new Inner2Bean();
	assertTrue(null == inner2.getNicknames().get("foo"));
	valueBinding.setRef("TestBean[\"inner\"].customers[2]");
	valueBinding.setValue(getFacesContext(), inner2);
	valueBinding.setRef("TestBean[\"inner\"].customers[2]");
	assertTrue(valueBinding.getValue(getFacesContext()) == inner2);    

	valueBinding.setRef("TestBean[\"inner\"].customers[2].nicknames.foo");
	valueBinding.setValue(getFacesContext(), "bar");
	assertTrue(((String) inner2.getNicknames().get("foo")).equals("bar"));
	
	// PENDING(edburns): test set with implicit objects.
	
    }

    public void testReadOnly() {
	// PENDING(edburns): implement read only tests
    }

    public void testType() {
	// PENDING(edburns): implement type tests
    }
    

} // end of class TestValueBindingImpl
