/*
 * $Id: TestFacesContextFactoryImpl.java,v 1.16 2007/04/27 22:02:05 ofung Exp $
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

// TestFacesContextFactoryImpl.java

package com.sun.faces.context;

import com.sun.faces.cactus.ServletFacesTestCase;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;

/**
 * <B>TestFacesContextFactoryImpl</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestFacesContextFactoryImpl.java,v 1.16 2007/04/27 22:02:05 ofung Exp $
 */

public class TestFacesContextFactoryImpl extends ServletFacesTestCase {

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

    public TestFacesContextFactoryImpl() {
        super("TestFacesContextFactory");
    }


    public TestFacesContextFactoryImpl(String name) {
        super(name);
    }
//
// Class methods
//

//
// Methods from TestCase
//

//
// General Methods
//

    public void testCreateMethods() {
        boolean gotException = false;
        FacesContext facesContext = null;
        FacesContextFactoryImpl facesContextFactory = null;

        // create FacesContextFactory.
        facesContextFactory = new FacesContextFactoryImpl();

        try {
            facesContext = facesContextFactory.getFacesContext(null, null, null,
                                                               null);
        } catch (FacesException fe) {
            gotException = true;
        } catch (NullPointerException ee) {
            gotException = true;
        }
        assertTrue(gotException);

        LifecycleFactory factory = (LifecycleFactory)
            FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
        assertTrue(null != factory);
        Lifecycle lifecycle =
            factory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
        assertTrue(null != lifecycle);

        gotException = false;
        try {
            facesContext = facesContextFactory.getFacesContext(
                config.getServletContext(),
                request,
                response,
                lifecycle);
        } catch (FacesException fe) {
            gotException = true;
        }
        assertTrue(gotException == false);

    }

} // end of class TestFacesContextFactoryImpl
