/*
 * $Id: TestMethodRef.java,v 1.14 2007/04/27 22:02:06 ofung Exp $
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
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

import javax.faces.el.EvaluationException;
import javax.faces.el.MethodBinding;
import javax.faces.el.MethodNotFoundException;
import javax.faces.el.ReferenceSyntaxException;

/**
 * <B>TestMethodRef </B> is a class ... <p/><B>Lifetime And Scope </B>
 * <P>
 * 
 * @version $Id: TestMethodRef.java,v 1.14 2007/04/27 22:02:06 ofung Exp $
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
