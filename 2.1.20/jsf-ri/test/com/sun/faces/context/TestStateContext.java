/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2012 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
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

// TestStateContext.java

package com.sun.faces.context;

import com.sun.faces.cactus.ServletFacesTestCase;
import com.sun.faces.util.ComponentStruct;
import com.sun.faces.util.Util;
import java.util.List;
import java.util.Locale;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

/**
 * <B>TestStateContext</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 */

public class TestStateContext extends ServletFacesTestCase {

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

    public TestStateContext() {
        super("TestStateContext");
    }


    public TestStateContext(String name) {
        super(name);
    }
//
// Class methods
//

//
// Methods from TestCase
//
    @Override
    public void setUp() {
        super.setUp();
        UIViewRoot viewRoot = Util.getViewHandler(getFacesContext()).createView(getFacesContext(), null);
        viewRoot.setViewId("viewId");
        viewRoot.setLocale(Locale.US);
        getFacesContext().setViewRoot(viewRoot);
    }

//
// General Methods
//

    public void testGetStateContext() {
        StateContext stateContext = StateContext.getStateContext(getFacesContext());
        assertTrue(null != stateContext);
    }

    public void testPartialStateSaving() {
    	  FacesContext ctx = getFacesContext();
        StateContext stateContext = StateContext.getStateContext(ctx);
        boolean partial = stateContext.isPartialStateSaving(ctx, "10");
        assertTrue(partial);
    }

    public void testAddComponent() {
        FacesContext ctx = getFacesContext();
        StateContext stateContext = StateContext.getStateContext(ctx);
        UIViewRoot viewRoot = ctx.getViewRoot();
        assertTrue(viewRoot != null);
        stateContext.startTrackViewModifications(ctx, viewRoot);
        UIOutput output = new UIOutput();
        output.setId("foo");
        viewRoot.getChildren().add(output);
        List<ComponentStruct> added = stateContext.getDynamicActions();
        assertTrue(added.size() > 0);
    }
}
