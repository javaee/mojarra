/*
 * $Id: TestValueBindingImpl_Model.java,v 1.5 2004/02/04 23:44:21 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestValueBindingImpl_Model.java

package com.sun.faces.el;

import com.sun.faces.util.Util;



import com.sun.faces.ServletFacesTestCase;
import com.sun.faces.TestBean;
import com.sun.faces.TestBean.InnerBean;
import com.sun.faces.TestBean.Inner2Bean;

import org.apache.cactus.WebRequest;

import javax.servlet.http.Cookie;

import java.util.Enumeration;
import javax.faces.context.FacesContext;
import javax.faces.el.PropertyNotFoundException;

import com.sun.faces.application.ApplicationImpl;

/**
 *
 *  <B>TestValueBindingImpl_Model</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestValueBindingImpl_Model.java,v 1.5 2004/02/04 23:44:21 ofung Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestValueBindingImpl_Model extends ServletFacesTestCase
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
    ValueBindingImpl valueBinding = null;

//
// Constructors and Initializers    
//

    public TestValueBindingImpl_Model() {super("TestValueBindingImpl");}
    public TestValueBindingImpl_Model(String name) {super(name);}
//
// Class methods
//

//
// Methods from TestCase
//

//
// General Methods
//

    public void setUp() {
	super.setUp();
	valueBinding = new ValueBindingImpl(new ApplicationImpl());
    }

    public void tearDown() {
	valueBinding = null;
	super.tearDown();
    }
	
    public void testSet() {
	FacesContext facesContext = getFacesContext();
	System.out.println("Testing setValue() with model bean in session ");
	TestBean testBean = new TestBean();
	InnerBean inner = new InnerBean();
	Inner2Bean innerInner = new Inner2Bean();
	
	getFacesContext().getExternalContext().getSessionMap().put("TestBean", 
								   testBean);
    
	// Test one level of nesting
	System.setProperty(TestBean.PROP, TestBean.FALSE);
	valueBinding.setRef("TestBean.one");
	valueBinding.setValue(getFacesContext(), "one");
	assertTrue(System.getProperty(TestBean.PROP).equals(TestBean.TRUE));
	
	System.setProperty(TestBean.PROP, TestBean.FALSE);
	valueBinding.setRef("sessionScope.TestBean.inner");
	valueBinding.setValue(getFacesContext(), inner);
	assertTrue(System.getProperty(TestBean.PROP).equals(TestBean.TRUE));

	// Test two levels of nesting
	System.setProperty(TestBean.PROP, TestBean.FALSE);
	valueBinding.setRef("sessionScope.TestBean.inner.two");
	valueBinding.setValue(getFacesContext(), "two");
	assertTrue(System.getProperty(TestBean.PROP).equals(TestBean.TRUE));
	
	System.setProperty(TestBean.PROP, TestBean.FALSE);
	valueBinding.setRef("sessionScope.TestBean.inner.inner2");
	valueBinding.setValue(getFacesContext(), innerInner);
	assertTrue(System.getProperty(TestBean.PROP).equals(TestBean.TRUE));

	// Test three levels of nesting
	System.setProperty(TestBean.PROP, TestBean.FALSE);
	valueBinding.setRef("sessionScope.TestBean.inner.inner2.three");
	valueBinding.setValue(getFacesContext(), "three");
	assertTrue(System.getProperty(TestBean.PROP).equals(TestBean.TRUE));
    }    

    public void testSetNull() {
	FacesContext facesContext = getFacesContext();
	System.out.println("Testing setValue() with model bean in session with null rValues");
	TestBean testBean = new TestBean();
	InnerBean inner = new InnerBean();
	Inner2Bean innerInner = new Inner2Bean();
	
	getFacesContext().getExternalContext().getSessionMap().put("TestBean", 
								   testBean);
    
	// Test one level of nesting
	valueBinding.setRef("TestBean.one");
	valueBinding.setValue(getFacesContext(), null);
	assertTrue(testBean.getOne() == null);
	
        System.setProperty(TestBean.PROP, TestBean.FALSE);
	valueBinding.setRef("sessionScope.TestBean.inner");
	valueBinding.setValue(getFacesContext(), inner);
	assertTrue(System.getProperty(TestBean.PROP).equals(TestBean.TRUE));
        
	valueBinding.setRef("sessionScope.TestBean.inner");
	valueBinding.setValue(getFacesContext(), null);
	assertTrue(testBean.getInner() == null);

	// Test two levels of nesting
	valueBinding.setRef("sessionScope.TestBean.inner.two");
	valueBinding.setValue(getFacesContext(), null);
	assertTrue(testBean.getInner().getTwo() == null);
	
	System.setProperty(TestBean.PROP, TestBean.FALSE);
	valueBinding.setRef("sessionScope.TestBean.inner.inner2");
	valueBinding.setValue(getFacesContext(), innerInner);
	assertTrue(System.getProperty(TestBean.PROP).equals(TestBean.TRUE));

        valueBinding.setRef("sessionScope.TestBean.inner.inner2");
	valueBinding.setValue(getFacesContext(), null);
	assertTrue(testBean.getInner().getInner2() == null);
        
	// Test three levels of nesting
	valueBinding.setRef("sessionScope.TestBean.inner.inner2.three");
	valueBinding.setValue(getFacesContext(), null);
	assertTrue(testBean.getInner().getInner2().getThree() == null);
    }
    
    public void testSetWithNoCurlyBraces() {
	FacesContext facesContext = getFacesContext();
	System.out.println("Testing setValue() with model bean in request ");
	TestBean testBean = new TestBean();
	InnerBean inner = new InnerBean();
	Inner2Bean innerInner = new Inner2Bean();
	
	facesContext.getExternalContext().getSessionMap().remove("TestBean");
	facesContext.getExternalContext().getRequestMap().put("TestBean", 
							      testBean);
	
	// Test implicit scopes direct access to some scope objects should
	// throw an illegalArgumentException
	boolean gotException =false;
	try {
	    valueBinding.setRef("header.header-one");
	    valueBinding.setValue(getFacesContext(), testBean);
	}catch (PropertyNotFoundException pnf) {
	    gotException = true;
	}    
	assertTrue( gotException);    
	
	// Test one level of nesting
	System.setProperty(TestBean.PROP, TestBean.FALSE);
	valueBinding.setRef("TestBean.one");
	valueBinding.setValue(getFacesContext(), "one");
	assertTrue(System.getProperty(TestBean.PROP).equals(TestBean.TRUE));
	
	System.setProperty(TestBean.PROP, TestBean.FALSE);
	valueBinding.setRef("requestScope.TestBean.inner");
	valueBinding.setValue(getFacesContext(), inner);
	assertTrue(System.getProperty(TestBean.PROP).equals(TestBean.TRUE));

	// Test two levels of nesting
	System.setProperty(TestBean.PROP, TestBean.FALSE);
	valueBinding.setRef("requestScope.TestBean.inner.two");
	valueBinding.setValue(getFacesContext(), "two");
	assertTrue(System.getProperty(TestBean.PROP).equals(TestBean.TRUE));
	
	System.setProperty(TestBean.PROP, TestBean.FALSE);
	valueBinding.setRef("requestScope.TestBean.inner.inner2");
	valueBinding.setValue(getFacesContext(), innerInner);
	assertTrue(System.getProperty(TestBean.PROP).equals(TestBean.TRUE));

	// Test three levels of nesting
	System.setProperty(TestBean.PROP, TestBean.FALSE);
	valueBinding.setRef("requestScope.TestBean.inner.inner2.three");
	valueBinding.setValue(getFacesContext(), "three");
	assertTrue(System.getProperty(TestBean.PROP).equals(TestBean.TRUE));
    }

    public void testGet() {
	FacesContext facesContext = getFacesContext();
	System.out.println("Testing getValue() with model bean in context");
	assertTrue( facesContext != null );
	TestBean testBeanResult = null, testBean = new TestBean();
	InnerBean inner = new InnerBean();
	Inner2Bean inner2 = new Inner2Bean();
	String result;
	
	// Init the beans
	testBean.setOne("one");
	inner.setTwo("two");
	inner2.setThree("three");
	inner.setInner2(inner2);
	testBean.setInner(inner);
	
	assertTrue( facesContext != null );
	assertTrue(facesContext.getExternalContext().getSession(false) != null );

	facesContext.getExternalContext().getRequestMap().remove("TestBean");
	facesContext.getExternalContext().getSessionMap().remove("TestBean");
	facesContext.getExternalContext().getApplicationMap().put("TestBean", 
								  testBean);
	
	// Test zero levels of nesting
	valueBinding.setRef("applicationScope.TestBean");
	testBeanResult = (TestBean) valueBinding.getValue(getFacesContext());
	assertTrue( testBeanResult != null);
	assertTrue(testBeanResult == testBean);
	
	// Test one level of nesting
	valueBinding.setRef("applicationScope.TestBean.one");
	result = (String) valueBinding.getValue(getFacesContext());
	assertTrue(result.equals("one"));
	
	valueBinding.setRef("applicationScope.TestBean.inner");
	inner = (InnerBean) valueBinding.getValue(getFacesContext());
	assertTrue(null != inner);
	
	// Test two levels of nesting
	valueBinding.setRef("applicationScope.TestBean.inner.two");
	result = (String) valueBinding.getValue(getFacesContext());
	assertTrue(result.equals("two"));
	
	valueBinding.setRef("applicationScope.TestBean.inner.inner2");
	inner2 = (Inner2Bean) 
	    valueBinding.getValue(getFacesContext());
	assertTrue(null != inner2);
	
	// Test three levels of nesting
	valueBinding.setRef("applicationScope.TestBean.inner.inner2.three");
	result = (String) valueBinding.getValue(getFacesContext());
	assertTrue(result.equals("three"));

    }

    public void testModelType() {
	/***************** PENDING(edburns):
	
	// Test model type
	System.out.println("Testing getModelType()");
	Class classType = null;
	String className = null;
	
	// Test zero levels of nesting
	classType = facesContext.getModelType("applicationScope.TestBean");
	assertTrue(classType != null);
	className = classType.getName();
	assertTrue(className.equals(testBean.getClass().getName()));
	
	classType = facesContext.getModelType("applicationScope.TestBean.inner.pin");
	assertTrue(classType != null);
	className = classType.getName();
	assertTrue(className.equals("java.lang.Integer"));
	
	classType = facesContext.getModelType("applicationScope.TestBean.inner.result");
	assertTrue(classType != null);
	className = classType.getName();
	assertTrue(className.equals("java.lang.Boolean"));
	
	classType = facesContext.getModelType("applicationScope.TestBean.one");
	assertTrue(classType != null);
	className = classType.getName();
	assertTrue(className.equals("java.lang.String"));
	*********************/
    }
    
} // end of class TestValueBindingImpl_Model
