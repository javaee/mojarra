/*
 * $Id: TestMethodExpressionImpl.java,v 1.1 2005/05/06 22:02:07 edburns Exp $
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
import javax.el.ELContext;
import javax.el.MethodNotFoundException;

/**
 * <B>TestMethodRef </B> is a class ... <p/><B>Lifetime And Scope </B>
 * <P>
 * 
 * @version $Id: TestMethodExpressionImpl.java,v 1.1 2005/05/06 22:02:07 edburns Exp $
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
        catch (NullPointerException ne)
        {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

} // end of class TestMethodRef
