/*
 * $Id: TestMethodExpressionImpl.java,v 1.3 2005/06/13 02:47:43 jhook Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestMethodRef.java
package com.sun.faces.el;

import com.sun.faces.ServletFacesTestCase;

import javax.el.MethodExpression;
import javax.el.ELException;
import javax.el.MethodNotFoundException;
import javax.faces.el.PropertyNotFoundException;

/**
 * <B>TestMethodRef </B> is a class ... <p/><B>Lifetime And Scope </B>
 * <P>
 * 
 * @version $Id: TestMethodExpressionImpl.java,v 1.3 2005/06/13 02:47:43 jhook Exp $
 */

public class TestMethodExpressionImpl extends ServletFacesTestCase
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

    public TestMethodExpressionImpl()
    {
        super("TestMethodExpression");
    }

    public TestMethodExpressionImpl(String name)
    {
        super(name);
    }

    //
    // Class methods
    //

    //
    // General Methods
    //
    protected MethodExpression create(String ref, Class[] params) throws Exception
    {
        return (getFacesContext().getApplication().getExpressionFactory().
            createMethodExpression(getFacesContext().getELContext(),ref, null, params));
    }
    
    public void testNullReference() throws Exception
    {
        try
        {
            create(null, null);
            fail();
        }
        catch (NullPointerException npe) {}
        catch (Exception e) { fail("Should have thrown an NPE"); };
    }
    
    public void testInvalidMethod() throws Exception
    {
        try
        {
            create("${foo > 1}", null);
            fail();
        }
        catch (ELException ee) {
            fail("Should have thrown a NullPointerException"); 
        }
        catch (NullPointerException npe) { }
    }
    
    public void testLiteralReference() throws Exception
    {
        boolean exceptionThrown = false;
        try
        {
            create("some.method", null);
        }
        catch (NullPointerException ee) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    public void testInvalidTrailing() throws Exception
    {
        MethodExpression mb = this.create(
                "#{NewCustomerFormHandler.redLectroidsMmmm}", new Class[0]);

        boolean exceptionThrown = false;
        try
        {
            mb.invoke(getFacesContext().getELContext(), new Object[0]);
        }
        catch (MethodNotFoundException me)
        {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        mb = this.create("#{nonexistentBean.redLectroidsMmmm}", new Class[0]);
       
        exceptionThrown = false;
        try
        {
            mb.invoke(getFacesContext().getELContext(), new Object[0]);
        }
        catch (PropertyNotFoundException ne)
        {
            exceptionThrown = true;
        }
        catch (ELException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

} // end of class TestMethodRef
