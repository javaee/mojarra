/*
 * $Id: TestModelAccessor.java,v 1.3 2002/01/10 22:20:13 edburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestModelAccessor.java

package com.sun.faces;

import junit.framework.TestCase;

import javax.faces.Constants;
import javax.faces.FacesException;
import javax.faces.ModelAccessor;
import javax.faces.ObjectManager;
import javax.faces.RenderContext;
import javax.faces.RenderContextFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;

import com.sun.faces.servlet.FacesServlet;

/**
 *
 *  <B>TestModelAccessor</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestModelAccessor.java,v 1.3 2002/01/10 22:20:13 edburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestModelAccessor extends FacesTestCase {
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

    private User user = null;
    private Address address = null;

    //
    // Constructors and Initializers    
    //

    public TestModelAccessor() {super("TestModelAccessor");}
    public TestModelAccessor(String name) {super(name);}
    //
    // Class methods
    //

    //
    // Methods from TestCase
    //

    //
    // General Methods
    //

    public void testSetGetModelObject() {
        boolean result;

        HttpSession session;
        String modelReference;
        String value;
        Object object = null;
 
        try {

            // use this for debugging
            //com.sun.faces.util.DebugUtil.waitForDebugger();

            result = null != (session = renderContext.getSession());
            assertTrue(result);

            objectManager.bind(ObjectManager.GlobalScope, "user", User.class);
    
            // 1. Use model reference string with '$' format
       
            // Here's the value we're setting
            //
            modelReference = "$user.address.street";
            value = "2340 Linwood Avenue";

            // Set the model object value
            //
            ModelAccessor.setModelObject(renderContext, modelReference, value);
            System.out.println("Model Property Set.");

            // Get the model object value
            //
            object = ModelAccessor.getModelObject(renderContext, modelReference); 
            System.out.println("Returned Object:"+object);
    
            result = value == object;
            System.out.println("Testing Set/Get methods, returned: " + result);
            assertTrue(result);

            // 2. Use model reference string without '$' (literal)

            result = false;

            // Here's the value we're setting
            //
            modelReference = "user";
            value = "2340 Linwood Avenue"; 

            // Set the model object value
            //
            ModelAccessor.setModelObject(renderContext, modelReference, value);
            System.out.println("Model Property Set.");

            // Get the model object value
            //
            object = ModelAccessor.getModelObject(renderContext, 
						  modelReference); 
            System.out.println("Returned Object:"+object);

            result = value == object;
            System.out.println("Testing Set/Get methods, returned: " + result);
            assertTrue(result);

        } catch (Exception e) {
            System.out.println("EXCEPTION:"+e.getMessage());
	    assertTrue(false);
        }
    }
}
