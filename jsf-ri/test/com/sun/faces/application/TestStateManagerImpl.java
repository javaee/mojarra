
/*
 * $Id: TestStateManagerImpl.java,v 1.21 2007/04/27 22:02:03 ofung Exp $
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

package com.sun.faces.application;
import com.sun.faces.RIConstants;
import com.sun.faces.cactus.ServletFacesTestCase;
import com.sun.faces.cactus.TestingUtil;
import com.sun.faces.util.Util;
import java.util.Map;
import java.util.Locale;
import javax.faces.application.StateManager.SerializedView;

import javax.faces.component.UIComponent;
import javax.faces.component.UIGraphic;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apache.cactus.WebRequest;



/**
 * This class tests the <code>StateManagerImpl</code> class
 * functionality.
 */
public class TestStateManagerImpl extends ServletFacesTestCase {


    //
    // Constructors/Initializers
    //
    public TestStateManagerImpl() {
        super("TestStateManagerImpl");
    }


    public TestStateManagerImpl(String name) {
        super(name);
    }
    
    //
    // Test Methods
    //
    
    // Verify saveSerializedView() throws IllegalStateException
    // if duplicate component id's are detected on non-transient 
    // components.
    public void testDuplicateIdDetectionServer() throws Exception {

        FacesContext context = getFacesContext();
        UIViewRoot root = null;

        UIComponent comp1 = null;

        UIComponent comp2 = null;

        UIComponent comp3 = null;

        UIComponent facet1 = null;

        UIComponent facet2 = null;
        
        // construct a view
        root = Util.getViewHandler(getFacesContext()).createView(context, null); 
        root.setViewId("/test");
        root.setId("root");
        root.setLocale(Locale.US);

        comp1 = new UIInput();
        comp1.setId("comp1");

        comp2 = new UIOutput();
        comp2.setId("comp2");

        comp3 = new UIGraphic();
        comp3.setId("comp3");

        facet1 = new UIOutput();
        facet1.setId("comp4");

        facet2 = new UIOutput();
        facet2.setId("comp2");

        comp2.getFacets().put("facet1", facet1);
        comp2.getFacets().put("facet2", facet2);

        root.getChildren().add(comp1);
        root.getChildren().add(comp2);
        root.getChildren().add(comp3);

        HttpSession session =
            (HttpSession) context.getExternalContext().getSession(false);
        session.setAttribute("/test", root);


        context.setViewRoot(root);

        StateManagerImpl stateManager = (StateManagerImpl) context.getApplication()
            .getStateManager();

        boolean exceptionThrown = false;
        try {
            stateManager.saveSerializedView(context);
        } catch (IllegalStateException ise) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
        
        
        // multiple componentns with a null ID should not
        // trigger an exception
        // construct a view
        root = Util.getViewHandler(getFacesContext()).createView(context, null); 
        root.setViewId("/test");
        root.setId("root");
        root.setLocale(Locale.US);

        comp1 = new UIInput();
        comp1.setId("comp1");

        comp2 = new UIOutput();
        comp2.setId(null);

        comp3 = new UIGraphic();
        comp3.setId(null);

        facet1 = new UIOutput();
        facet1.setId("comp4");

        facet2 = new UIOutput();
        facet2.setId("comp2");

        comp2.getFacets().put("facet1", facet1);
        comp2.getFacets().put("facet2", facet2);

        root.getChildren().add(comp1);
        root.getChildren().add(comp2);
        root.getChildren().add(comp3);

        session.setAttribute("/test", root);

        context.setViewRoot(root);

        exceptionThrown = false;
        try {
            stateManager.saveSerializedView(context);
        } catch (IllegalStateException ise) {
            exceptionThrown = true;
        }
        assertTrue(!exceptionThrown);
        
        // transient components with duplicate ids should 
        // trigger an error condition
        // construct a view
        root = Util.getViewHandler(getFacesContext()).createView(context, null); 
        root.setViewId("/test");
        root.setId("root");
        root.setLocale(Locale.US);

        comp1 = new UIInput();
        comp1.setId("comp1");
        comp1.setTransient(true);

        comp2 = new UIOutput();
        comp2.setId("comp1");
        comp2.setTransient(true);

        comp3 = new UIGraphic();
        comp3.setId("comp3");

        facet1 = new UIOutput();
        facet1.setId("comp4");

        facet2 = new UIOutput();
        facet2.setId("comp2");

        comp2.getFacets().put("facet1", facet1);
        comp2.getFacets().put("facet2", facet2);

        root.getChildren().add(comp1);
        root.getChildren().add(comp2);
        root.getChildren().add(comp3);

        session.setAttribute("/test", root);

        context.setViewRoot(root);

        exceptionThrown = false;
        try {
            stateManager.saveSerializedView(context);
        } catch (IllegalStateException ise) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }


    public void testDuplicateIdDetectionClient() throws Exception {
        StateManagerImpl wrapper =
            new StateManagerImpl() {
                public boolean isSavingStateInClient(FacesContext context) {
                    return true;
                }
            };
        getFacesContext().getApplication().setStateManager(wrapper);
        testDuplicateIdDetectionServer();
    }

    public static void resetStateManagerRequestIdSerialNumber(FacesContext context) {
	//((StateManagerImpl) Util.getStateManager(context)).requestIdSerial = 0;
        StateManagerImpl stateManager = 
              ((StateManagerImpl) Util.getStateManager(context));
        TestingUtil.setPrivateField("reqeustIdSerial",
                                    Integer.TYPE,
                                    stateManager,
                                    0);
        
	
    }
    
    public void beginMultiWindowSaveServer(WebRequest theRequest) {
        theRequest.addParameter("javax.faces.ViewState", "j_id1:j_id2");
    }

    public void testMultiWindowSaveServer() throws Exception {
        StateManagerImpl wrapper =
            new StateManagerImpl() {
                public boolean isSavingStateInClient(FacesContext context) {
                    return false;
                }
            };
        getFacesContext().getApplication().setStateManager(wrapper);
        
        // build the tree
        
        UIViewRoot root, newRoot = null;

        UIComponent comp1 = null;

        UIComponent comp2 = null;

        UIComponent comp3 = null;

        UIComponent facet1 = null;

        UIComponent facet2 = null;
        
        // construct a view
        root = Util.getViewHandler(getFacesContext()).createView(getFacesContext(), null); 
        root.setViewId("/test");
        root.setId("root");
        root.setLocale(Locale.US);

        comp1 = new UIInput();
        comp1.setId("comp1");

        comp2 = new UIOutput();
        comp2.setId("comp2");

        comp3 = new UIGraphic();
        comp3.setId("comp3");

        facet1 = new UIOutput();
        facet1.setId("comp4");

        comp2.getFacets().put("facet1", facet1);

        root.getChildren().add(comp1);
        root.getChildren().add(comp2);
        root.getChildren().add(comp3);
        
        getFacesContext().setViewRoot(root);
        root.getAttributes().put("checkThisValue", "checkThisValue");
        
        SerializedView viewState = wrapper.saveSerializedView(getFacesContext());
        
        // See that the Logical View and Actual View maps are correctly created
        Map sessionMap = getFacesContext().getExternalContext().getSessionMap();
        assertTrue(sessionMap.containsKey(RIConstants.LOGICAL_VIEW_MAP));
        assertTrue(((Map)sessionMap.get(RIConstants.LOGICAL_VIEW_MAP)).containsKey("j_id1"));
        
        newRoot = wrapper.restoreView(getFacesContext(), "test", "HTML_BASIC");
        assertNotNull(newRoot);
        assertEquals(root.getAttributes().get("checkThisValue"),
                     newRoot.getAttributes().get("checkThisValue"));
        assertNotNull(getFacesContext().getExternalContext().getRequestMap().get(RIConstants.LOGICAL_VIEW_MAP));
        assertEquals(getFacesContext().getExternalContext().getRequestMap().get(RIConstants.LOGICAL_VIEW_MAP), 
                     "j_id1");
        
        
    }
    
}
