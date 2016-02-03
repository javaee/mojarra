/*
 * $Id: TestRenderKitFactory.java,v 1.19 2007/04/27 22:02:09 ofung Exp $
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
 * @version $Id: TestRenderKitFactory.java,v 1.19 2007/04/27 22:02:09 ofung Exp $
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
