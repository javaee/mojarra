/*
 * $Id: TestMessageResourcesImpl.java,v 1.1 2002/06/25 20:48:02 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestMessageResourcesImpl.java

package com.sun.faces.context;

import com.sun.faces.ServletFacesTestCase;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.context.FacesContext;
import javax.faces.context.MessageResources;
import javax.faces.context.Message;
import com.sun.faces.context.MessageResourcesImpl;
import com.sun.faces.util.Util;
import java.util.Iterator;
import javax.faces.FacesException;

/**
 *
 *  <B>TestMessageResourcesImpl</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestMessageResourcesImpl.java,v 1.1 2002/06/25 20:48:02 jvisvanathan Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestMessageResourcesImpl extends ServletFacesTestCase
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

    public TestMessageResourcesImpl() {super("TestMessageListImpl");}
    public TestMessageResourcesImpl(String name) {super(name);}
    //
    // Class methods
    //

    //
    // Methods from TestCase
    //

    //
    // General Methods
    //
    
    public void testGetMethods() {
        boolean gotException = false;
        Message msg = null;
        
        FacesContext facesContext = getFacesContext();
        Assert.assert_it(facesContext != null );

        MessageResources resources = Util.getMessageResources();
        assertTrue( resources != null );
        
        System.out.println("Testing get methods");
        try {
            msg = resources.getMessage(null, null);
        } catch ( NullPointerException fe) {
            gotException = true;
        }
        assertTrue(gotException);
        gotException = false;
        msg = null;
        
        // if msgId doesn't exist in the resource, faces exception must be
        // thrown.
        try {
            msg = resources.getMessage(facesContext,"MSG01", "param1");
        } catch ( FacesException fe ) {
            gotException = true;
        }    
        assertTrue (gotException);
        
        Object[] params1 = {"JavaServerFaces"};
        msg = resources.getMessage(facesContext, "MSG0001", params1);
        assertTrue ( msg != null );
        assertTrue((msg.getSummary()).equals("JavaServerFaces is not a valid number."));
        
        msg = resources.getMessage( facesContext,"MSG0003", "userId");
        assertTrue ( msg != null );
        assertTrue((msg.getSummary()).equals("userId field cannot be empty."));
        
        msg = resources.getMessage( facesContext, "MSG0004", "userId", "1000","10000");
        assertTrue ( msg != null );
        assertTrue((msg.getDetail()).equals("Value should be between 1000 and 10000.")); 
        assertTrue((msg.getSummary()).equals("userId out of range."));
    }

} // end of class TestMessageListImpl
