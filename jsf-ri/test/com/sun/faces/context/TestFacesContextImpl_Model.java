/*
 * $Id: TestFacesContextImpl_Model.java,v 1.1 2002/05/28 18:20:40 jvisvanathan Exp $
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
import org.apache.cactus.ServletTestCase;

import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.ServletRequest;
import javax.servlet.ServletContext;
import com.sun.faces.context.FacesContextImpl;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.FacesException;

/**
 *
 *  <B>TestFacesContextImpl_Model</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestFacesContextImpl_Model.java,v 1.1 2002/05/28 18:20:40 jvisvanathan Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestFacesContextImpl_Model extends ServletTestCase
{
//
// Protected Constants
//

protected static final String PROP = "oneSet";

protected static final String TRUE = "true";
protected static final String FALSE = "false";
//
// Class Variables
//

//
// Instance Variables
//
protected FacesContextImpl facesContext = null;

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
public void setUp() {
    
    ServletContext sc = (request.getSession()).getServletContext();
    try {
        facesContext = new FacesContextImpl(sc,request, response, 
                LifecycleFactory.DEFAULT_LIFECYCLE);
    } catch ( FacesException fe) {
    }
    assertTrue(facesContext != null);
}  

public void testSet()
{
    System.out.println("Testing setModelValue()");
    TestBean testBean = new TestBean();
    InnerBean inner = new InnerBean();
    Inner2Bean innerInner = new Inner2Bean();

    (facesContext.getHttpSession()).setAttribute("TestBean", testBean);
    
    // Test one level of nesting
    System.setProperty(PROP, FALSE);
    facesContext.setModelValue("${TestBean.one}", "one");
    assertTrue(System.getProperty(PROP).equals(TRUE));

    System.setProperty(PROP, FALSE);
    facesContext.setModelValue( "${TestBean.inner}", inner);
    assertTrue(System.getProperty(PROP).equals(TRUE));

    // Test two levels of nesting
    System.setProperty(PROP, FALSE);
    facesContext.setModelValue("${TestBean.inner.two}", "two");
    assertTrue(System.getProperty(PROP).equals(TRUE));

    System.setProperty(PROP, FALSE);
    facesContext.setModelValue("${TestBean.inner.inner2}", innerInner);
    assertTrue(System.getProperty(PROP).equals(TRUE));

    // Test three levels of nesting
    System.setProperty(PROP, FALSE);
    facesContext.setModelValue("${TestBean.inner.inner2.three}", "three");
    assertTrue(System.getProperty(PROP).equals(TRUE));
    
}

public void testGet()
{
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

public static class TestBean extends Object
{

protected String one = null;

public void setOne(String newOne)
{
    one = newOne;
    assertTrue(newOne.equals("one"));
    System.setProperty(PROP, TRUE);
}

public String getOne() 
{
    return one;
}

protected InnerBean inner = null;

public void setInner(InnerBean newInner)
{
    inner = newInner;
    System.setProperty(PROP, TRUE);
}

public InnerBean getInner() 
{
    return inner;
}

}

public static class InnerBean extends Object
{

protected String two = null;
protected Integer pin = null;
protected Boolean result = null; 

public void setTwo(String newTwo)
{
    two = newTwo;
    assertTrue(newTwo.equals("two"));
    System.setProperty(PROP, TRUE);
}

public String getTwo() 
{
    return two;
}

public void setPin(Integer newPin)
{
    pin = newPin;
}

public Integer getPin() 
{
    return pin;
}

public void setResult(Boolean newResult)
{
    result = newResult;
}

public Boolean getResult() 
{
    return result;
}

protected Inner2Bean inner2 = null;

public void setInner2(Inner2Bean newInner2)
{
    inner2 = newInner2;
    System.setProperty(PROP, TRUE);
}

public Inner2Bean getInner2() 
{
    return inner2;
}

}

public static class Inner2Bean extends Object
{

protected String three = null;

public void setThree(String newThree)
{
    three = newThree;
    assertTrue(newThree.equals("three"));
    System.setProperty(PROP, TRUE);
}

public String getThree() 
{
    return three;
}


}


} // end of class TestFacesContextImpl_Model