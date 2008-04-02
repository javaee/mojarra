/*
 * $Id: TestRenderKitFactory.java,v 1.3 2002/11/14 20:50:21 rkitain Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
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

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.ParameterCheck;
import org.apache.cactus.ServletTestCase;

/**
 *
 *  <B>TestRenderKitFactory</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestRenderKitFactory.java,v 1.3 2002/11/14 20:50:21 rkitain Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestRenderKitFactory extends ServletTestCase {
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
        renderKitFactory = new RenderKitFactoryImpl();

        // 1. Verify "getRenderKit" returns the same RenderKit instance
        //    if called multiple times with the same identifier.
        //  
        RenderKit renderKit1 = renderKitFactory.getRenderKit("DEFAULT");
        RenderKit renderKit2 = renderKitFactory.getRenderKit("DEFAULT");
        assertTrue(renderKit1 == renderKit2);

        // 2. Verify "addRenderKit" adds instances.. /
        //      "getRenderKitIds returns iteration..
        //    Should be iteration of "3" because default was added
        //    via RenderKitFactoryImpl constructor call (above).
        //
        renderKitFactory.addRenderKit("Foo", renderKit1);
        renderKitFactory.addRenderKit("Bar", renderKit2);
        Iterator iter = renderKitFactory.getRenderKitIds();
        int i = 0;
        while (iter.hasNext()) {
            iter.next();
            i++;
        }
        assertTrue(i == 3);

        // 3. Verify null parameter exception for "getRenderKit"
        FacesContext context = null;
        boolean except = false;
        try {
            RenderKit renderKit3 = renderKitFactory.getRenderKit("DEFAULT",
                context);
        } catch(NullPointerException npe) {
            except = true;
        }
        assertTrue(except);
    }

    public void testDefaultExists() {
        renderKitFactory = new RenderKitFactoryImpl();
    
        // 1. Verify constructor created "default" renderkit..
        //
        RenderKit renderKit;
        String id = null;
        Iterator iter = renderKitFactory.getRenderKitIds();
        while (iter.hasNext()) {
            id = (String)iter.next();
        }
        assertTrue(id.equals(RenderKitFactory.DEFAULT_RENDER_KIT));
    }
            
    public void testExceptions() {
        renderKitFactory = new RenderKitFactoryImpl();
        RenderKit rKit = null;

        // 1. Verify "IllegalArg" exception which occurs when attempting
        //    to add the default (which already exists...
        //
        boolean thrown = false;
        try {
            rKit = renderKitFactory.getRenderKit(
                RenderKitFactory.DEFAULT_RENDER_KIT);
            renderKitFactory.addRenderKit(RenderKitFactory.DEFAULT_RENDER_KIT,
                rKit);
        } catch (IllegalArgumentException ia) {
            thrown = true;
        }
        assertTrue(thrown);

        // 2. Verify "IllegalArg exception which occurs when attempting
        //    to add the same renderkit id.
        //
        thrown = false;
        try {
            renderKitFactory.addRenderKit("foo", rKit); 
            renderKitFactory.addRenderKit("foo", rKit); 
        } catch (IllegalArgumentException ia) {
            thrown = true;
        }
        assertTrue(thrown);
    }
            
            
} // end of class TestRenderKitFactory
