/*
 * $Id: TestFacesContextImpl_Model.java,v 1.11 2003/02/20 22:49:51 ofung Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestFacesContextImpl_Model.java

package com.sun.faces.context;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.ParameterCheck;
import com.sun.faces.ServletFacesTestCase;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import com.sun.faces.TestBean;
import com.sun.faces.TestBean.InnerBean;
import com.sun.faces.TestBean.Inner2Bean;

/**
 *
 *  <B>TestFacesContextImpl_Model</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestFacesContextImpl_Model.java,v 1.11 2003/02/20 22:49:51 ofung Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestFacesContextImpl_Model extends ServletFacesTestCase
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

    public TestFacesContextImpl_Model() {super("TestBeanAccessor");}
    public TestFacesContextImpl_Model(String name) {super(name);}
//
// Class methods
//

//
// Methods from TestCase
//

//
// General Methods
//

public void testSet()
{
    FacesContext facesContext = getFacesContext();
    System.out.println("Testing setModelValue() with model bean in session ");
    TestBean testBean = new TestBean();
    InnerBean inner = new InnerBean();
    Inner2Bean innerInner = new Inner2Bean();

    (facesContext.getHttpSession()).setAttribute("TestBean", testBean);
    
    // Test one level of nesting
    System.setProperty(TestBean.PROP, TestBean.FALSE);
    facesContext.setModelValue("TestBean.one", "one");
    assertTrue(System.getProperty(TestBean.PROP).equals(TestBean.TRUE));

    System.setProperty(TestBean.PROP, TestBean.FALSE);
    facesContext.setModelValue( "sessionScope.TestBean.inner", inner);
    assertTrue(System.getProperty(TestBean.PROP).equals(TestBean.TRUE));

    // Test two levels of nesting
    System.setProperty(TestBean.PROP, TestBean.FALSE);
    facesContext.setModelValue("sessionScope.TestBean.inner.two", "two");
    assertTrue(System.getProperty(TestBean.PROP).equals(TestBean.TRUE));

    System.setProperty(TestBean.PROP, TestBean.FALSE);
    facesContext.setModelValue("sessionScope.TestBean.inner.inner2", innerInner);
    assertTrue(System.getProperty(TestBean.PROP).equals(TestBean.TRUE));

    // Test three levels of nesting
    System.setProperty(TestBean.PROP, TestBean.FALSE);
    facesContext.setModelValue("sessionScope.TestBean.inner.inner2.three", "three");
    assertTrue(System.getProperty(TestBean.PROP).equals(TestBean.TRUE));
}

public void testSetWithNoCurlyBraces()
{
    FacesContext facesContext = getFacesContext();
    System.out.println("Testing setModelValue() with model bean in request ");
    TestBean testBean = new TestBean();
    InnerBean inner = new InnerBean();
    Inner2Bean innerInner = new Inner2Bean();
    
    (facesContext.getHttpSession()).removeAttribute("TestBean");
    (facesContext.getServletRequest()).setAttribute("TestBean", testBean);
    
    // Test implicit scopes direct access to some scope objects should
    // throw an illegalArgumentException
    boolean gotException =false;
    try {
        facesContext.setModelValue("header.header-one", testBean);
    }catch (IllegalArgumentException iae) {
        gotException = true;
    }    
    assertTrue( gotException);    
    
    // Test one level of nesting
    System.setProperty(TestBean.PROP, TestBean.FALSE);
    facesContext.setModelValue("TestBean.one", "one");
    assertTrue(System.getProperty(TestBean.PROP).equals(TestBean.TRUE));

    System.setProperty(TestBean.PROP, TestBean.FALSE);
    facesContext.setModelValue( "TestBean.inner", inner);
    assertTrue(System.getProperty(TestBean.PROP).equals(TestBean.TRUE));

    // Test two levels of nesting
    System.setProperty(TestBean.PROP, TestBean.FALSE);
    facesContext.setModelValue("TestBean.inner.two", "two");
    assertTrue(System.getProperty(TestBean.PROP).equals(TestBean.TRUE));

    System.setProperty(TestBean.PROP, TestBean.FALSE);
    facesContext.setModelValue("TestBean.inner.inner2", innerInner);
    assertTrue(System.getProperty(TestBean.PROP).equals(TestBean.TRUE));

    // Test three levels of nesting
    System.setProperty(TestBean.PROP, TestBean.FALSE);
    facesContext.setModelValue("TestBean.inner.inner2.three", "three");
    assertTrue(System.getProperty(TestBean.PROP).equals(TestBean.TRUE));
}

public void testGet()
{
    FacesContext facesContext = getFacesContext();
    System.out.println("Testing getModelValue() with model bean in context");
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
    assertTrue( facesContext.getHttpSession() != null );
    
    (facesContext.getServletRequest()).removeAttribute("TestBean");
    (facesContext.getHttpSession()).removeAttribute("TestBean");
    (facesContext.getServletContext()).setAttribute("TestBean", testBean);
 
    // Test zero levels of nesting
    testBeanResult = (TestBean) facesContext.getModelValue("applicationScope.TestBean");
    assertTrue( testBeanResult != null);
    assertTrue(testBeanResult == testBean);
    
    // Test one level of nesting
    result = (String) facesContext.getModelValue("applicationScope.TestBean.one");
    assertTrue(result.equals("one"));

    inner = (InnerBean) facesContext.getModelValue("applicationScope.TestBean.inner");
    assertTrue(null != inner);

    // Test two levels of nesting
    result = (String) facesContext.getModelValue("applicationScope.TestBean.inner.two");
    assertTrue(result.equals("two"));

    inner2 = (Inner2Bean) 
	facesContext.getModelValue("applicationScope.TestBean.inner.inner2");
    assertTrue(null != inner2);

    // Test three levels of nesting
    result = (String) facesContext.getModelValue("applicationScope.TestBean.inner.inner2.three");
    assertTrue(result.equals("three"));
    
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
}

public void testGetWithNoCurlyBraces()
{
    FacesContext facesContext = getFacesContext();
    System.out.println("Testing getModelValue() with model bean in session ");
    assertTrue( facesContext != null );
    TestBean testBeanResult = null, testBean = new TestBean();
    InnerBean inner = new InnerBean();
    Inner2Bean inner2 = new Inner2Bean();
    String result;

    (facesContext.getServletContext()).removeAttribute("TestBean");
    // Init the beans
    testBean.setOne("one");
    inner.setTwo("two");
    inner2.setThree("three");
    inner.setInner2(inner2);
    testBean.setInner(inner);
    
    assertTrue( facesContext != null );
    assertTrue( facesContext.getHttpSession() != null );
    (facesContext.getHttpSession()).setAttribute("TestBean", testBean);

    // Test zero levels of nesting
    testBeanResult = (TestBean) facesContext.getModelValue("TestBean");
    assertTrue(testBeanResult == testBean);
    
    // Test one level of nesting
    result = (String) facesContext.getModelValue("TestBean.one");
    assertTrue(result.equals("one"));

    inner = (InnerBean) facesContext.getModelValue("TestBean.inner");
    assertTrue(null != inner);

    // Test two levels of nesting
    result = (String) facesContext.getModelValue("TestBean.inner.two");
    assertTrue(result.equals("two"));

    inner2 = (Inner2Bean) 
	facesContext.getModelValue("TestBean.inner.inner2");
    assertTrue(null != inner2);

    // Test three levels of nesting
    result = (String) facesContext.getModelValue("TestBean.inner.inner2.three");
    assertTrue(result.equals("three"));
    
    // Test model type
    System.out.println("Testing getModelType()");
    Class classType = null;
    String className = null;

    // Test zero levels of nesting
    classType = facesContext.getModelType("TestBean");
    assertTrue(classType != null);
    className = classType.getName();
    assertTrue(className.equals(testBean.getClass().getName()));

    classType = facesContext.getModelType("TestBean.inner.pin");
    assertTrue(classType != null);
    className = classType.getName();
    assertTrue(className.equals("java.lang.Integer"));
    
    classType = facesContext.getModelType("TestBean.inner.result");
    assertTrue(classType != null);
    className = classType.getName();
    assertTrue(className.equals("java.lang.Boolean"));
    
    classType = facesContext.getModelType("TestBean.one");
    assertTrue(classType != null);
    className = classType.getName();
    assertTrue(className.equals("java.lang.String"));
}

public void testModelObjectSearch() {
    String result = null;
    FacesContext facesContext = getFacesContext();
    assertTrue( facesContext != null );
    TestBean testBean = new TestBean();
    testBean.setOne("one");
    
    (facesContext.getHttpSession()).removeAttribute("TestBean");
    boolean gotException = false;
    try {
	facesContext.getModelType(null);
    }
    catch (NullPointerException e) {
	gotException = true;
    }
    assertTrue(gotException);
    
    gotException = false;
    try {
	facesContext.getModelValue(null);
    }
    catch (NullPointerException e) {
	gotException = true;
    }
    assertTrue(gotException);


    try {
        facesContext.setModelValue(null,null);
    } catch ( Exception e ) {
        gotException = true;
    }    
    assertTrue(gotException);
    // store the bean in various scope and make sure the narrow to broad
    // search works.
    
    // Test Bean in request scope.
    (facesContext.getServletRequest()).setAttribute("TestBean", testBean);
    result = (String) facesContext.getModelValue("${requestScope.TestBean.one}");
    assertTrue(result.equals("one"));
    (facesContext.getServletRequest()).removeAttribute("TestBean");
   
    // Test Bean in session scope.
    (facesContext.getHttpSession()).setAttribute("TestBean", testBean);
    result = (String) facesContext.getModelValue("${sessionScope.TestBean.one}");
    assertTrue(result.equals("one"));
    (facesContext.getHttpSession()).removeAttribute("TestBean");
  
    // Test Bean in ServletContext
    (facesContext.getServletContext()).setAttribute("TestBean", testBean);
    result = (String) facesContext.getModelValue("${applicationScope.TestBean.one}");
    assertTrue(result.equals("one"));
    (facesContext.getServletContext()).removeAttribute("TestBean");
    
}

public void testModelObjectSearchWithNoCurlyFries() {
    String result = null;
    FacesContext facesContext = getFacesContext();
    assertTrue( facesContext != null );
    TestBean testBean = new TestBean();
    testBean.setOne("one");
    
    (facesContext.getServletContext()).removeAttribute("TestBean");
    boolean gotException = false;
    try {
	facesContext.getModelType(null);
    }
    catch (NullPointerException e) {
	gotException = true;
    }
    assertTrue(gotException);
    
    gotException = false;
    try {
	facesContext.getModelValue(null);
    }
    catch (NullPointerException e) {
	gotException = true;
    }
    assertTrue(gotException);


    try {
        facesContext.setModelValue(null,null);
    } catch ( Exception e ) {
        gotException = true;
    }    
    assertTrue(gotException);
    // store the bean in various scope and make sure the narrow to broad
    // search works.
    
    // Test Bean in request scope.
    (facesContext.getServletRequest()).setAttribute("TestBean", testBean);
    result = (String) facesContext.getModelValue("TestBean.one");
    assertTrue(result.equals("one"));
    (facesContext.getServletRequest()).removeAttribute("TestBean");
   
    // Test Bean in session scope.
    (facesContext.getHttpSession()).setAttribute("TestBean", testBean);
    result = (String) facesContext.getModelValue("TestBean.one");
    assertTrue(result.equals("one"));
    (facesContext.getHttpSession()).removeAttribute("TestBean");
  
    // Test Bean in ServletContext
    (facesContext.getServletContext()).setAttribute("TestBean", testBean);
    result = (String) facesContext.getModelValue("TestBean.one");
    assertTrue(result.equals("one"));
    (facesContext.getServletContext()).removeAttribute("TestBean");
}

public void testNullCases() {

    TestBean testBeanFromContext = null, testBean = null;

    assertTrue(null != (testBean = (TestBean)
	       (getFacesContext().getHttpSession()).getAttribute("TestBean")));
    assertTrue(null != (testBeanFromContext = (TestBean)
			(getFacesContext().getModelValue("${TestBean}"))));
    assertTrue(testBean == testBeanFromContext);
	       

    // case 1 -- "TestBean.prop" when the testBean.getProp() method
    // returns null should definitely return null
    testBean.setProp(null);
    try {
	assertTrue(null == getFacesContext().getModelValue("TestBean.prop"));
    }
    catch (Throwable e) {
	assertTrue(false);
    }

    // case 2 -- "TestBean.inner.inner2.nicknames.joebob" (where
    // "nicknames" is a Map and the "joebob" key is missing) should also
    // return null
    Inner2Bean inner2 = new Inner2Bean();
    InnerBean inner = new InnerBean();
    inner.setInner2(inner2);
    testBean.setInner(inner);

    try {
	assertTrue(null == getFacesContext().getModelValue("TestBean.inner.inner2.nicknames.joebob"));
    }
    catch (Throwable e) {
	assertTrue(false);
    }
    

    // case 3 -- "TestBean.inner.inner2.nicknames.joebob" when
    // testBean.getInner().getInner2().getNicknames() returns null is
    // more problematic. Right now, I think this should return
    // FacesException, but ultimately the object manager facilities to
    // automatically create an address will come into play here.
    inner2.setNicknames(null);
    boolean gotException = false;
    try {
	getFacesContext().getModelValue("TestBean.inner.inner2.nicknames.joebob");
    }
    catch (FacesException e) {
	gotException = true;
    }
    assertTrue(gotException);

    // case 3A, this is the same case, but for set.
    gotException = false;
    try {
	getFacesContext().setModelValue("TestBean.inner.inner2.nicknames",
					"joebob");
    }
    catch (FacesException e) {
	gotException = true;
    }
    assertTrue(gotException);


    // case 4 -- "TestBean.inner.inner2" when
    // testBean.getInner() returns null is more problematic. Right
    // now, I think this should return FacesException, but ultimately
    // the object manager facilities to automatically create an address
    // will come into play here.
    gotException = false;
    testBean.setInner(null);
    try {
	getFacesContext().getModelValue("TestBean.inner.inner2");
    }
    catch (FacesException e) {
	gotException = true;
    }
    assertTrue(gotException);

    // case 4A, the same as above, but for set
    gotException = false;
    try {
	getFacesContext().setModelValue("TestBean.inner.inner2", inner2);
    }
    catch (FacesException e) {
	gotException = true;
    }
    assertTrue(gotException);

}


} // end of class TestFacesContextImpl_Model
