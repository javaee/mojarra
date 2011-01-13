/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2010 Sun Microsystems, Inc. All rights reserved.
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

// TestStateContext.java

package com.sun.faces.context;

import com.sun.faces.cactus.ServletFacesTestCase;
import com.sun.faces.context.StateContext;
import com.sun.faces.lifecycle.LifecycleImpl;
import com.sun.faces.util.ComponentStruct;
import com.sun.faces.util.Util;

import javax.faces.application.FacesMessage;
import javax.faces.application.Application;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.el.ELContextListener;
import javax.el.ELContextEvent;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Map;
import java.util.Collections;

import org.apache.cactus.WebRequest;

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
        boolean partial = stateContext.partialStateSaving(ctx, "10");
        assertTrue(partial);
    }

    public void testAddComponent() {
        StateContext stateContext = StateContext.getStateContext(getFacesContext());
        stateContext.startTrackViewModifications();
        UIViewRoot viewRoot = Util.getViewHandler(getFacesContext()).createView(getFacesContext(), null);
        UIOutput output = new UIOutput();
        output.setId("foo");
        viewRoot.getChildren().add(output);
        Map<String,ComponentStruct> added = stateContext.getDynamicAdds();
        assertTrue(added.size() > 0);
    }
}
