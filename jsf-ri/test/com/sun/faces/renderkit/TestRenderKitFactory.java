/*
 * $Id: TestRenderKitFactory.java,v 1.8 2003/12/17 15:15:34 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestRenderKitFactory.java

package com.sun.faces.renderkit;

import com.sun.faces.renderkit.RenderKitFactoryImpl;

import java.util.Iterator;

import javax.faces.context.FacesContext;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.FacesException;
import javax.faces.FactoryFinder;

import com.sun.faces.util.Util;



import com.sun.faces.ServletFacesTestCase;

/**
 *
 *  <B>TestRenderKitFactory</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestRenderKitFactory.java,v 1.8 2003/12/17 15:15:34 rkitain Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestRenderKitFactory extends ServletFacesTestCase {
//
// Protected Constants
//

//
// Class Variables
//

//
// Instance Variables
//
    private RenderKitFactoryImpl renderKitFactory = null;

// Attribute Instance Variables

// Relationship Instance Variables

//
// Constructors and Initializers    
//

    public TestRenderKitFactory() {super("TestRenderKitFactory");}
    public TestRenderKitFactory(String name) {super(name);}
//
// Class methods
//

//
// General Methods
//

    public void testFactory() {
        RenderKitFactory renderKitFactory = (RenderKitFactory)
            FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);

        // 1. Verify "getRenderKit" returns the same RenderKit instance
        //    if called multiple times with the same identifier.
        //  
        RenderKit renderKit1 = renderKitFactory.getRenderKit("DEFAULT");
        RenderKit renderKit2 = renderKitFactory.getRenderKit("DEFAULT");
        assertTrue(renderKit1 == renderKit2);

        // 2. Verify "addRenderKit" adds instances.. /
        //
        renderKitFactory.addRenderKit("Foo", renderKit1);
        renderKitFactory.addRenderKit("Bar", renderKit2);

	// Verify renderkit instance replaced with last identifier..
	//
	renderKitFactory.addRenderKit("BarBar", renderKit2);
	RenderKit rkit = renderKitFactory.getRenderKit("BarBar");
	assertTrue(rkit != null);
	assertTrue(rkit == renderKit2);

        // 3. Verify "getRenderKit" returns null if
        //    RenderKit not found for renderkitid...
        //
        RenderKit renderKit4 = renderKitFactory.getRenderKit("Gamma");
	assertTrue(renderKit4 == null);
    }

    public void testDefaultExists() {
        RenderKitFactory renderKitFactory = (RenderKitFactory)
            FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
    
        // 1. Verify "default" renderkit..
        //
        RenderKit renderKit;
        String id = null;
        Iterator iter = renderKitFactory.getRenderKitIds();
        boolean exists = false; 
        while (iter.hasNext()) {
            id = (String)iter.next();
            if (id.equals(RenderKitFactory.DEFAULT_RENDER_KIT)) {
                exists=true;
                break;
            }
        }
        assertTrue(exists);
    }
            
    public void testExceptions() {
        renderKitFactory = new RenderKitFactoryImpl();
        RenderKit rKit = null;

        rKit = renderKitFactory.getRenderKit("DEFAULT");

	// Verify NPE for "addRenderKit"
	//
        boolean exceptionThrown = false;
	try {
	    renderKitFactory.addRenderKit(null, rKit);
	    exceptionThrown = false;
	} catch (NullPointerException e) {
	    exceptionThrown = true;
	}
	assertTrue(exceptionThrown);
	exceptionThrown = false;
	try {
	    renderKitFactory.addRenderKit("foo", null);
	    exceptionThrown = false;
	} catch (NullPointerException e1) {
	    exceptionThrown = true;
	}
	assertTrue(exceptionThrown);
	
        // Verify null parameter exception for "getRenderKit"
	//
        exceptionThrown = false;
        try {
            rKit = renderKitFactory.getRenderKit(null);
        } catch(NullPointerException e2) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        exceptionThrown = false;
        try {
            rKit = renderKitFactory.getRenderKit(null, getFacesContext());
        } catch(NullPointerException e3) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
        exceptionThrown = false;
        try {
            rKit = renderKitFactory.getRenderKit("foo", null);
        } catch(NullPointerException e4) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }
            
            
} // end of class TestRenderKitFactory
