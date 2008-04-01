/*
 * $Id: TestFacesContextImpl_Model.java,v 1.4 2002/08/02 01:17:39 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
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
 * @version $Id: TestFacesContextImpl_Model.java,v 1.4 2002/08/02 01:17:39 eburns Exp $
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
    System.out.println("Testing setModelValue()");
    TestBean testBean = new TestBean();
    InnerBean inner = new InnerBean();
    Inner2Bean innerInner = new Inner2Bean();

    (facesContext.getHttpSession()).setAttribute("TestBean", testBean);
    
    // Test one level of nesting
    System.setProperty(TestBean.PROP, TestBean.FALSE);
    facesContext.setModelValue("${TestBean.one}", "one");
    assertTrue(System.getProperty(TestBean.PROP).equals(TestBean.TRUE));

    System.setProperty(TestBean.PROP, TestBean.FALSE);
    facesContext.setModelValue( "${TestBean.inner}", inner);
    assertTrue(System.getProperty(TestBean.PROP).equals(TestBean.TRUE));

    // Test two levels of nesting
    System.setProperty(TestBean.PROP, TestBean.FALSE);
    facesContext.setModelValue("${TestBean.inner.two}", "two");
    assertTrue(System.getProperty(TestBean.PROP).equals(TestBean.TRUE));

    System.setProperty(TestBean.PROP, TestBean.FALSE);
    facesContext.setModelValue("${TestBean.inner.inner2}", innerInner);
    assertTrue(System.getProperty(TestBean.PROP).equals(TestBean.TRUE));

    // Test three levels of nesting
    System.setProperty(TestBean.PROP, TestBean.FALSE);
    facesContext.setModelValue("${TestBean.inner.inner2.three}", "three");
    assertTrue(System.getProperty(TestBean.PROP).equals(TestBean.TRUE));
    
}

public void testGet()
{
    FacesContext facesContext = getFacesContext();
    System.out.println("Testing getModelValue()");
    assertTrue( facesContext != null );
    TestBean testBean = new TestBean();
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
    (facesContext.getHttpSession()).setAttribute("TestBean", testBean);
    
    // Test one level of nesting
    result = (String) facesContext.getModelValue("${TestBean.one}");
    assertTrue(result.equals("one"));

    inner = (InnerBean) facesContext.getModelValue("${TestBean.inner}");
    assertTrue(null != inner);

    // Test two levels of nesting
    result = (String) facesContext.getModelValue("${TestBean.inner.two}");
    assertTrue(result.equals("two"));

    inner2 = (Inner2Bean) 
	facesContext.getModelValue("${TestBean.inner.inner2}");
    assertTrue(null != inner2);

    // Test three levels of nesting
    result = (String) facesContext.getModelValue("${TestBean.inner.inner2.three}");
    assertTrue(result.equals("three"));
    
    // Test model type
    System.out.println("Testing getModelType()");
    Class classType = null;
    String className = null;
    classType = facesContext.getModelType("${TestBean.inner.pin}");
    assertTrue(classType != null);
    className = classType.getName();
    assertTrue(className.equals("java.lang.Integer"));
    
    classType = facesContext.getModelType("${TestBean.inner.result}");
    assertTrue(classType != null);
    className = classType.getName();
    assertTrue(className.equals("java.lang.Boolean"));
    
    classType = facesContext.getModelType("${TestBean.one}");
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
    result = (String) facesContext.getModelValue("${TestBean.one}");
    assertTrue(result.equals("one"));
    (facesContext.getServletRequest()).removeAttribute("TestBean");
   
    // Test Bean in session scope.
    (facesContext.getHttpSession()).setAttribute("TestBean", testBean);
    result = (String) facesContext.getModelValue("${TestBean.one}");
    assertTrue(result.equals("one"));
    (facesContext.getHttpSession()).removeAttribute("TestBean");
  
    // Test Bean in ServletContext
    (facesContext.getServletContext()).setAttribute("TestBean", testBean);
    result = (String) facesContext.getModelValue("${TestBean.one}");
    assertTrue(result.equals("one"));
    (facesContext.getServletContext()).removeAttribute("TestBean");
    
    // make sure we get an exception if bean doesn't exist
    gotException = false;
    try {
        result = (String) facesContext.getModelValue("${TestBean.one}");
    } catch ( FacesException fe) {
        gotException = true;
    }    
    assertTrue(gotException);
}    

} // end of class TestFacesContextImpl_Model
