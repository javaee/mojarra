/*
 * $Id: TestRenderKitFactory.java,v 1.6 2003/07/08 15:38:48 eburns Exp $
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

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.ParameterCheck;

import com.sun.faces.ServletFacesTestCase;

/**
 *
 *  <B>TestRenderKitFactory</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestRenderKitFactory.java,v 1.6 2003/07/08 15:38:48 eburns Exp $
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

        // 3. Verify null parameter exception for "getRenderKit"
        FacesContext context = null;
        boolean except = false;
        try {
            RenderKit renderKit3 = renderKitFactory.getRenderKit("DEFAULT", null);
        } catch(NullPointerException npe) {
            except = true;
        }
        assertTrue(except);

        // 4. Verify "getRenderKit" returns RenderKitImpl if
        //    RenderKit not found for renderkitid...
        //
        RenderKit renderKit4 = renderKitFactory.getRenderKit("Gamma");
        Assert.assert_it(renderKit4 instanceof RenderKitImpl);

        

        
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
