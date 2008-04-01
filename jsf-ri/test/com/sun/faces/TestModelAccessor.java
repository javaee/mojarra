/*
 * $Id: TestModelAccessor.java,v 1.2 2001/12/20 22:26:43 ofung Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestModelAccessor.java

package com.sun.faces;

import junit.framework.TestCase;

import junit.framework.*;
import org.apache.cactus.*;

import javax.faces.Constants;
import javax.faces.FacesException;
import javax.faces.ModelAccessor;
import javax.faces.ObjectTable;
import javax.faces.ObjectTableFactory;
import javax.faces.RenderContext;
import javax.faces.RenderContextFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;

/**
 *
 *  <B>TestModelAccessor</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestModelAccessor.java,v 1.2 2001/12/20 22:26:43 ofung Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestModelAccessor extends ServletTestCase {
    //
    // Protected Constants
    //

    //
    // Class Variables
    //

    private static final int INITIAL_NUM_SCOPES = 3;

    //
    // Instance Variables
    //

    // Attribute Instance Variables

    // Relationship Instance Variables

    private User user = null;
    private Address address = null;
    private ObjectTable objectTable = null;

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

    public void setUp() {
        ObjectTableFactory otf = ObjectTableFactory.newInstance();

        try {
	    otf = ObjectTableFactory.newInstance();
	    otf.newObjectTable();
	    objectTable = ObjectTable.getInstance();
        } catch (Exception e) {
        }
        request.setAttribute(Constants.REF_REQUESTINSTANCE, request);
        HttpSession session = request.getSession();

        session.setAttribute(Constants.REF_SESSIONINSTANCE,
                         session.getId());
    }

    public void tearDown() {
        objectTable = null;
    }

    //
    // General Methods
    //

    public void testSetGetModelObject() {

        boolean result;

        RenderContext context;
        RenderContextFactory factory;
        HttpSession session;
        String modelReference;
        String value;
        Object object = null;
 
        try {

            // use this for debugging
            //com.sun.faces.util.DebugUtil.waitForDebugger();

            factory = RenderContextFactory.newInstance();
            context = factory.newRenderContext(request);
            result = null != context.getSession();
            assertTrue(result);

            session = context.getSession();
            objectTable.put(objectTable.GlobalScope, "user", User.class);
            session.getServletContext().setAttribute(Constants.REF_OBJECTTABLE,
                objectTable);        
    
            // 1. Use model reference string with '$' format
       
            // Here's the value we're setting
            //
            modelReference = "$user.address.street";
            value = "2340 Linwood Avenue";

            // Set the model object value
            //
            ModelAccessor.setModelObject(context, modelReference, value);
            System.out.println("Model Property Set.");

            // Get the model object value
            //
            object = ModelAccessor.getModelObject(context, modelReference); 
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
            ModelAccessor.setModelObject(context, modelReference, value);
            System.out.println("Model Property Set.");

            // Get the model object value
            //
            object = ModelAccessor.getModelObject(context, modelReference); 
            System.out.println("Returned Object:"+object);

            result = value == object;
            System.out.println("Testing Set/Get methods, returned: " + result);
            assertTrue(result);

        } catch (Exception e) {
            System.out.println("EXCEPTION:"+e.getMessage());
        }
    }
}
