/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

// TestMethodRef.java
package com.sun.faces.el;

import com.sun.faces.cactus.ServletFacesTestCase;

import javax.el.MethodExpression;
import javax.el.ELException;
import javax.el.MethodNotFoundException;
import javax.faces.el.PropertyNotFoundException;

/**
 * <B>TestMethodRef </B> is a class ... <p/><B>Lifetime And Scope </B>
 * <P>
 * 
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

//  
//    Because of a minor change in behavior these particular tests will always 
//    fail on GF 3.1.1, but not on GF 3.1.2. Disabling the test on the 2.1 
//    branch. These tests will stil run on 2.2 trunk to assert the proper 
//    behavior (for GF 3.1.2 and upwards).
//
//    public void testNullReference() throws Exception
//    {
//        try
//        {
//            create(null, null);
//            fail();
//        }
//        catch (ELException e) {}
//        catch (Exception exception) {
//            fail();
//        }
//    }
//
//    public void testInvalidMethod() throws Exception
//    {
//        try
//        {
//            create("${foo > 1}", null);
//            fail();
//        }
//        catch (ELException e) {}
//        catch (Exception exeption) {
//            fail();
//        }
//    }
    
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
