/*
 * $Id: TestRenderKitFactory.java,v 1.16 2005/10/19 19:51:36 edburns Exp $
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

// TestRenderKitFactory.java

package com.sun.faces.renderkit;

import com.sun.faces.cactus.ServletFacesTestCase;

import javax.faces.FactoryFinder;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;

import java.util.Iterator;

/**
 * <B>TestRenderKitFactory</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestRenderKitFactory.java,v 1.16 2005/10/19 19:51:36 edburns Exp $
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

    public TestRenderKitFactory() {
        super("TestRenderKitFactory");
    }


    public TestRenderKitFactory(String name) {
        super(name);
    }
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
        RenderKit renderKit1 = renderKitFactory.getRenderKit(getFacesContext(),
                                                             RenderKitFactory.HTML_BASIC_RENDER_KIT);
        RenderKit renderKit2 = renderKitFactory.getRenderKit(getFacesContext(),
                                                             RenderKitFactory.HTML_BASIC_RENDER_KIT);
        assertTrue(renderKit1 == renderKit2);

        // 2. Verify "addRenderKit" adds instances.. /
        //
        renderKitFactory.addRenderKit("Foo", renderKit1);
        renderKitFactory.addRenderKit("Bar", renderKit2);

        // Verify renderkit instance replaced with last identifier..
        //
        renderKitFactory.addRenderKit("BarBar", renderKit2);
        RenderKit rkit = renderKitFactory.getRenderKit(getFacesContext(),
                                                       "BarBar");
        assertTrue(rkit != null);
        assertTrue(rkit == renderKit2);

        // 3. Verify "getRenderKit" returns null if
        //    RenderKit not found for renderkitid...
        //
        RenderKit renderKit4 = renderKitFactory.getRenderKit(getFacesContext(),
                                                             "Gamma");
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
            id = (String) iter.next();
            if (id.equals(RenderKitFactory.HTML_BASIC_RENDER_KIT)) {
                exists = true;
                break;
            }
        }
        assertTrue(exists);
    }


    public void testExceptions() {
        renderKitFactory = new RenderKitFactoryImpl();
        RenderKit rKit = null;

        rKit = renderKitFactory.getRenderKit(getFacesContext(),
                                             RenderKitFactory.HTML_BASIC_RENDER_KIT);

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
            rKit = renderKitFactory.getRenderKit(null, null);
        } catch (NullPointerException e2) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        exceptionThrown = false;
        try {
            rKit = renderKitFactory.getRenderKit(getFacesContext(), null);
        } catch (NullPointerException e4) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }


} // end of class TestRenderKitFactory
