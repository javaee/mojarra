/*
 * $Id: TestMethodRef.java,v 1.13 2006/03/29 23:04:53 rlubke Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

// TestMethodRef.java
package com.sun.faces.el;

import com.sun.faces.cactus.ServletFacesTestCase;

import javax.faces.el.EvaluationException;
import javax.faces.el.MethodBinding;
import javax.faces.el.MethodNotFoundException;
import javax.faces.el.ReferenceSyntaxException;

/**
 * <B>TestMethodRef </B> is a class ... <p/><B>Lifetime And Scope </B>
 * <P>
 * 
 * @version $Id: TestMethodRef.java,v 1.13 2006/03/29 23:04:53 rlubke Exp $
 */

public class TestMethodRef extends ServletFacesTestCase
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

    public TestMethodRef()
    {
        super("TestMethodRef");
    }

    public TestMethodRef(String name)
    {
        super(name);
    }

    //
    // Class methods
    //

    //
    // General Methods
    //
    protected MethodBinding create(String ref, Class[] params) throws Exception
    {
        return (getFacesContext().getApplication().createMethodBinding(ref, params));
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
            create("#{foo > 1}", null);
            fail();
        }
        catch (ReferenceSyntaxException rse) {}
        catch (Exception e) { fail("Should have thrown a ReferenceSyntaxException"); }
    }
    
    public void testLiteralReference() throws Exception
    {
        try
        {
            create("some.method", null);
            fail();
        }
        catch (ReferenceSyntaxException rse) {}
        catch (Exception e) { fail("Should have thrown a ReferenceSyntaxException"); }
    }

    public void testInvalidTrailing() throws Exception
    {
        MethodBinding mb = this.create(
                "#{NewCustomerFormHandler.redLectroidsMmmm}", new Class[0]);

        boolean exceptionThrown = false;
        try
        {
            mb.invoke(getFacesContext(), new Object[0]);
        }
        catch (MethodNotFoundException e)
        {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        mb = this.create("#{nonexistentBean.redLectroidsMmmm}", new Class[0]);
        
        // page 80 of the EL Spec, since nonexistentBean is null, the target
        // method is never reached and should catch a PropertyNotFoundException
        // and rethrow as a MethodNotFoundException
        exceptionThrown = false;
        try
        {
            mb.invoke(getFacesContext(), new Object[0]);
        }
        catch (MethodNotFoundException e)
        {
            exceptionThrown = true;
        }
        catch (EvaluationException e) {
            //TODO remove once adaptor is fixed
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

} // end of class TestMethodRef
