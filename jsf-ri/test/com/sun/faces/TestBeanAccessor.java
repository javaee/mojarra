/*
 * $Id: TestBeanAccessor.java,v 1.1 2002/04/15 20:57:51 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestBeanAccessor.java

package com.sun.faces;

import javax.faces.ObjectAccessor;
import javax.faces.ObjectManager;

/**
 *
 *  <B>TestBeanAccessor</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestBeanAccessor.java,v 1.1 2002/04/15 20:57:51 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestBeanAccessor extends FacesTestCase
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

// Attribute Instance Variables

// Relationship Instance Variables

//
// Constructors and Initializers    
//

    public TestBeanAccessor() {super("TestBeanAccessor");}
    public TestBeanAccessor(String name) {super(name);}
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
    ObjectAccessor objectAccessor = facesContext.getObjectAccessor();
    ObjectManager objectManager = facesContext.getObjectManager();
    TestBean testBean = new TestBean();
    InnerBean inner = new InnerBean();
    Inner2Bean innerInner = new Inner2Bean();

    objectManager.put(request, "TestBean", testBean);
    
    // Test one level of nesting
    System.setProperty(PROP, FALSE);
    objectAccessor.setObject(request, "$TestBean.one", "one");
    assertTrue(System.getProperty(PROP).equals(TRUE));

    System.setProperty(PROP, FALSE);
    objectAccessor.setObject(request, "$TestBean.inner", inner);
    assertTrue(System.getProperty(PROP).equals(TRUE));

    // Test two levels of nesting
    System.setProperty(PROP, FALSE);
    objectAccessor.setObject(request, "$TestBean.inner.two", "two");
    assertTrue(System.getProperty(PROP).equals(TRUE));

    System.setProperty(PROP, FALSE);
    objectAccessor.setObject(request, "$TestBean.inner.inner2", 
			     innerInner);
    assertTrue(System.getProperty(PROP).equals(TRUE));

    // Test three levels of nesting
    System.setProperty(PROP, FALSE);
    objectAccessor.setObject(request, "$TestBean.inner.inner2.three", 
			     "three");
    assertTrue(System.getProperty(PROP).equals(TRUE));
    
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

protected Inner2Bean inner2 = null;

public void setInner2(Inner2Bean newInner2)
{
    inner2 = newInner2;
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


} // end of class TestBeanAccessor
