/*
 * $Id: TestValueBindingImpl.java,v 1.7 2003/05/08 23:13:11 horwat Exp $
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

import com.sun.faces.application.ApplicationImpl;
import com.sun.faces.ServletFacesTestCase;
import com.sun.faces.TestBean;
import com.sun.faces.TestBean.InnerBean;
import com.sun.faces.TestBean.Inner2Bean;

import org.apache.cactus.WebRequest;

import javax.servlet.http.Cookie;

import javax.faces.el.PropertyNotFoundException;
import javax.faces.component.UINamingContainer;

import java.util.Enumeration;
import java.util.Map;

/**
 *
 *  <B>TestValueBindingImpl</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestValueBindingImpl.java,v 1.7 2003/05/08 23:13:11 horwat Exp $
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

    public TestValueBindingImpl() {super("TestValueBindingImpl");}
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
	ValueBindingImpl valueBinding = new ValueBindingImpl(new ApplicationImpl());
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
	ValueBindingImpl valueBinding = new ValueBindingImpl(new ApplicationImpl());
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
    
    public void testGetLastSegment() {
	ValueBindingImpl valueBinding = new ValueBindingImpl(new ApplicationImpl());
	valueBinding.setRef("a.b.c");
	assertTrue(valueBinding.getLastSegment().equals("c"));

	valueBinding.setRef("a[1].b.c");
	assertTrue(valueBinding.getLastSegment().equals("c"));

	valueBinding.setRef("a[1]");
	assertTrue(valueBinding.getLastSegment().equals("[1]"));

	valueBinding.setRef("a[\"1\"]");
	assertTrue(valueBinding.getLastSegment().equals("[\"1\"]"));

	valueBinding.setRef("a[0].b[\"1\"]");
	assertTrue(valueBinding.getLastSegment().equals("[\"1\"]"));

	valueBinding.setRef("a");
	assertTrue(valueBinding.getLastSegment().equals("a"));

	valueBinding.setRef("foo");
	assertTrue(valueBinding.getLastSegment().equals("foo"));

	boolean exceptionThrown = false ;
	try {
	    valueBinding.setRef("a[");
	    valueBinding.getLastSegment();
	}
	catch (PropertyNotFoundException e) {
	    exceptionThrown = true;
	}
	assertTrue(exceptionThrown);

	exceptionThrown = false ;
	try {
	    valueBinding.setRef("apple][");
	    valueBinding.getLastSegment();
	}
	catch (PropertyNotFoundException e) {
	    exceptionThrown = true;
	}
	assertTrue(exceptionThrown);

	exceptionThrown = false ;
	try {
	    valueBinding.setRef("a.");
	    valueBinding.getLastSegment();
	}
	catch (PropertyNotFoundException e) {
	    exceptionThrown = true;
	}
	assertTrue(exceptionThrown);

    }

    public void testHasMultipleSegments() {
	ValueBindingImpl valueBinding = new ValueBindingImpl(new ApplicationImpl());
	valueBinding.setRef("cookie.cookie");
	assertTrue(valueBinding.hasMultipleSegments());
	valueBinding.setRef("cookie.monster");
	assertTrue(valueBinding.hasMultipleSegments());
	valueBinding.setRef("cookie");
	assertTrue(!valueBinding.hasMultipleSegments());
	valueBinding.setRef("cookie[1]");
	assertTrue(valueBinding.hasMultipleSegments());
	valueBinding.setRef("cookie[1].hello");
	assertTrue(valueBinding.hasMultipleSegments());
    }

    public void testStripQuotesIfNecessary() {
	ValueBindingImpl valueBinding = new ValueBindingImpl(new ApplicationImpl());
	assertTrue(valueBinding.stripQuotesIfNecessary("\"hasQuotes\"").equals("hasQuotes"));
	assertTrue(valueBinding.stripQuotesIfNecessary("\"openQuote").equals("\"openQuote"));
	assertTrue(valueBinding.stripQuotesIfNecessary("\'singleQuotes\'").equals("singleQuotes"));
	assertTrue(valueBinding.stripQuotesIfNecessary("endQuote\'").equals("endQuote\'"));
    }

    public void testReadOnly_singleCase() {
	ValueBindingImpl valueBinding = new ValueBindingImpl(new ApplicationImpl());

	// these are mutable Maps
	valueBinding.setRef("applicationScope");
	assertTrue(!valueBinding.isReadOnly(getFacesContext()));
	valueBinding.setRef("sessionScope");
	assertTrue(!valueBinding.isReadOnly(getFacesContext()));
	valueBinding.setRef("requestScope");
	assertTrue(!valueBinding.isReadOnly(getFacesContext()));
	
	// these are immutable Maps
	valueBinding.setRef("param");
	assertTrue(valueBinding.isReadOnly(getFacesContext()));
	valueBinding.setRef("paramValues");
	assertTrue(valueBinding.isReadOnly(getFacesContext()));
	valueBinding.setRef("header");
	assertTrue(valueBinding.isReadOnly(getFacesContext()));
	valueBinding.setRef("headerValues");
	assertTrue(valueBinding.isReadOnly(getFacesContext()));
	valueBinding.setRef("cookie");
	assertTrue(valueBinding.isReadOnly(getFacesContext()));
	valueBinding.setRef("initParam");
	assertTrue(valueBinding.isReadOnly(getFacesContext()));
    }

    public void testReadOnly_multipleCase() {
	ValueBindingImpl valueBinding = new ValueBindingImpl(new ApplicationImpl());

	// these are mutable Maps
	valueBinding.setRef("applicationScope.value");
	assertTrue(!valueBinding.isReadOnly(getFacesContext()));
	valueBinding.setRef("sessionScope.value");
	assertTrue(!valueBinding.isReadOnly(getFacesContext()));
	valueBinding.setRef("requestScope.value");
	assertTrue(!valueBinding.isReadOnly(getFacesContext()));
	
	// these are immutable Maps
	valueBinding.setRef("param.value");
	assertTrue(valueBinding.isReadOnly(getFacesContext()));
	valueBinding.setRef("paramValues.value");
	assertTrue(valueBinding.isReadOnly(getFacesContext()));
	valueBinding.setRef("header.value");
	assertTrue(valueBinding.isReadOnly(getFacesContext()));
	valueBinding.setRef("headerValues.value");
	assertTrue(valueBinding.isReadOnly(getFacesContext()));
	valueBinding.setRef("cookie.value");
	assertTrue(valueBinding.isReadOnly(getFacesContext()));
	valueBinding.setRef("initParam.value");
	assertTrue(valueBinding.isReadOnly(getFacesContext()));

	// tree
	// create a dummy root for the tree.
	UINamingContainer root = new UINamingContainer() {
                public String getComponentType() { return "root"; }
            };
	root.setComponentId("root");
	getFacesContext().setTree(new com.sun.faces.tree.SimpleTreeImpl(getFacesContext(), root, "newTree"));
	valueBinding.setRef("tree.root");
	assertTrue(valueBinding.isReadOnly(getFacesContext()));
	
	TestBean testBean = (TestBean) getFacesContext().getExternalContext().getSessionMap().get("TestBean");
	assertTrue(null != testBean);
	valueBinding.setRef("TestBean.readOnly");
	assertTrue(valueBinding.isReadOnly(getFacesContext()));
	valueBinding.setRef("TestBean.one");
	assertTrue(!valueBinding.isReadOnly(getFacesContext()));

	InnerBean inner = new InnerBean();
	testBean.setInner(inner);
	valueBinding.setRef("TestBean[\"inner\"].customers[1]");
	assertTrue(!valueBinding.isReadOnly(getFacesContext()));


    }

    public void testGetType_singleCase() {
	ValueBindingImpl valueBinding = new ValueBindingImpl(new ApplicationImpl());
	
	// these are mutable Maps
	valueBinding.setRef("applicationScope");
	assertTrue(Map.class.isAssignableFrom(valueBinding.getType(getFacesContext())));
	valueBinding.setRef("sessionScope");
	assertTrue(Map.class.isAssignableFrom(valueBinding.getType(getFacesContext())));
	valueBinding.setRef("requestScope");
	assertTrue(Map.class.isAssignableFrom(valueBinding.getType(getFacesContext())));
	
	// these are immutable Maps
	valueBinding.setRef("param");
	assertTrue(Map.class.isAssignableFrom(valueBinding.getType(getFacesContext())));
	valueBinding.setRef("paramValues");
	assertTrue(Map.class.isAssignableFrom(valueBinding.getType(getFacesContext())));
	valueBinding.setRef("header");
	assertTrue(Map.class.isAssignableFrom(valueBinding.getType(getFacesContext())));
	valueBinding.setRef("headerValues");
	assertTrue(Map.class.isAssignableFrom(valueBinding.getType(getFacesContext())));
	valueBinding.setRef("cookie");
	assertTrue(Map.class.isAssignableFrom(valueBinding.getType(getFacesContext())));
	valueBinding.setRef("initParam");
	assertTrue(Map.class.isAssignableFrom(valueBinding.getType(getFacesContext())));
    }

    public void beginGetType_multipleCase(WebRequest theRequest) {
	populateRequest(theRequest);
    }


    public void testGetType_multipleCase() {
	ValueBindingImpl valueBinding = new ValueBindingImpl(new ApplicationImpl());
	String property = "testValueBindingImpl_property";
	getFacesContext().getExternalContext().getApplicationMap().put(property,
								       property);
	
	getFacesContext().getExternalContext().getSessionMap().put(property,
								   property);
	
	getFacesContext().getExternalContext().getRequestMap().put(property,
								   property);
	
	// these are mutable Maps
	valueBinding.setRef("applicationScope." + property);
	assertTrue(valueBinding.getType(getFacesContext()).getName().equals("java.lang.String"));
	valueBinding.setRef("sessionScope." + property);
	assertTrue(valueBinding.getType(getFacesContext()).getName().equals("java.lang.String"));
	valueBinding.setRef("requestScope." + property);
	assertTrue(valueBinding.getType(getFacesContext()).getName().equals("java.lang.String"));
	
	// these are immutable Maps
	valueBinding.setRef("param." + "ELParam");
	assertTrue(valueBinding.getType(getFacesContext()).getName().equals("java.lang.String"));
	valueBinding.setRef("paramValues.multiparam");
	assertTrue(valueBinding.getType(getFacesContext()).getName().equals("[Ljava.lang.String;"));

	valueBinding.setRef("header.ELHeader");
	assertTrue(valueBinding.getType(getFacesContext()).getName().equals("java.lang.String"));
	valueBinding.setRef("headerValues.multiheader");
	assertTrue(java.util.Enumeration.class.isAssignableFrom(valueBinding.getType(getFacesContext())));
	valueBinding.setRef("cookie.cookie");
	assertTrue(valueBinding.getType(getFacesContext()).getName().equals("javax.servlet.http.Cookie"));
	valueBinding.setRef("initParam.saveStateInClient");
	assertTrue(valueBinding.getType(getFacesContext()).getName().equals("java.lang.String"));

	// tree
	// create a dummy root for the tree.
	UINamingContainer root = new UINamingContainer() {
                public String getComponentType() { return "root"; }
            };
	root.setComponentId("root");
	getFacesContext().setTree(new com.sun.faces.tree.SimpleTreeImpl(getFacesContext(), root, "newTree"));
	valueBinding.setRef("tree.root");
	assertTrue(valueBinding.getType(getFacesContext()).getName().equals("javax.faces.component.UIComponent"));
	
	TestBean testBean = (TestBean) getFacesContext().getExternalContext().getSessionMap().get("TestBean");
	assertTrue(null != testBean);
	valueBinding.setRef("TestBean.readOnly");
	assertTrue(valueBinding.getType(getFacesContext()).getName().equals("java.lang.String"));
	valueBinding.setRef("TestBean.one");
	assertTrue(valueBinding.getType(getFacesContext()).getName().equals("java.lang.String"));

	InnerBean inner = new InnerBean();
	testBean.setInner(inner);
	valueBinding.setRef("TestBean[\"inner\"].customers[1]");
	assertTrue(valueBinding.getType(getFacesContext()).getName().equals("java.lang.String"));

	valueBinding.setRef("TestBean[\"inner\"]");
	assertTrue(valueBinding.getType(getFacesContext()).getName().equals("com.sun.faces.TestBean$InnerBean"));

	int [] intArray = { 1, 2, 3 };
	getFacesContext().getExternalContext().getRequestMap().put("intArray",
								   intArray);
	valueBinding.setRef("requestScope.intArray");
	assertTrue(valueBinding.getType(getFacesContext()).getName().equals("[I"));
    }

    public void testGetScopePositive() {
        ValueBindingImpl valueBinding = new ValueBindingImpl(new ApplicationImpl());
        TestBean testBean = new TestBean();

        getFacesContext().getExternalContext().getApplicationMap().
            put("TestApplicationBean", testBean);
        assertEquals("application", 
            valueBinding.getScope("TestApplicationBean"));
        assertEquals("application", 
            valueBinding.getScope("TestApplicationBean.one"));
        assertEquals("application", 
            valueBinding.getScope("TestApplicationBean.inner.two"));
        assertEquals("application", 
            valueBinding.getScope("applicationScope.TestApplicationBean"));
        assertEquals("application", 
            valueBinding.getScope("applicationScope.TestApplicationBean.inner.two"));

        getFacesContext().getExternalContext().getSessionMap().
            put("TestSessionBean", testBean);
        assertEquals("session", 
            valueBinding.getScope("TestSessionBean"));
        assertEquals("session", 
            valueBinding.getScope("TestSessionBean.one"));
        assertEquals("session", 
            valueBinding.getScope("TestSessionBean.inner.two"));
        assertEquals("session", 
            valueBinding.getScope("sessionScope.TestSessionBean"));
        assertEquals("session", 
            valueBinding.getScope("sessionScope.TestSessionBean.inner.two"));

        getFacesContext().getExternalContext().getRequestMap().
            put("TestRequestBean", testBean);
        assertEquals("request", 
            valueBinding.getScope("TestRequestBean"));
        assertEquals("request", 
            valueBinding.getScope("TestRequestBean.one"));
        assertEquals("request", 
            valueBinding.getScope("TestRequestBean.inner.two"));
        assertEquals("request", 
            valueBinding.getScope("requestScope.TestRequestBean"));
        assertEquals("request", 
            valueBinding.getScope("requestScope.TestRequestBean.inner.two"));

        assertNull(valueBinding.getScope("TestNoneBean"));
        assertNull(valueBinding.getScope("TestNoneBean.one"));
        assertNull(valueBinding.getScope("TestNoneBean.inner.two"));

    }   

    public void testGetScopeNegative() {
        ValueBindingImpl valueBinding = new ValueBindingImpl(new ApplicationImpl());
        String property = null;

        property = "[]";
        assertNull(valueBinding.getScope(property));
        property = "][";
        assertNull(valueBinding.getScope(property));
        property = "";
        assertNull(valueBinding.getScope(property));
        property = null;
        assertNull(valueBinding.getScope(property));
        property = "foo.sessionScope";
        assertNull(valueBinding.getScope(property));
        property = "sessionScope";
        assertNull(valueBinding.getScope(property));
        property = "sessionScope[";
        assertNull(valueBinding.getScope(property));

    }   

} // end of class TestValueBindingImpl
