/*
 * $Id: TestHAStateManagerImpl.java,v 1.3 2005/10/19 19:51:29 edburns Exp $
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

package com.sun.faces.application;

import org.apache.cactus.server.ServletConfigWrapper;
import com.sun.faces.RIConstants;
import com.sun.faces.cactus.ServletFacesTestCase;
import com.sun.faces.util.Util;

import javax.faces.application.StateManager;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIGraphic;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.component.UIForm;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.render.RenderKitFactory;
import javax.servlet.http.HttpSession;
import javax.faces.application.StateManager.SerializedView;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import java.util.ArrayList;


/**
 * This class tests the <code>StateManagerImpl</code> class
 * functionality.
 */
public class TestHAStateManagerImpl extends ServletFacesTestCase {

     public static final String TEST_URI = "/test.jsp";
    //
    // Constructors/Initializers
    //
    public TestHAStateManagerImpl() {
        super("TestStateManagerImpl");
    }


    public TestHAStateManagerImpl(String name) {
        super(name);
    }
    
    private Application application = null;
    
    public void setUp() {
        super.setUp();
        ApplicationFactory aFactory =
            (ApplicationFactory) FactoryFinder.getFactory(
                FactoryFinder.APPLICATION_FACTORY);
        application = (ApplicationImpl) aFactory.getApplication();
        application.setViewHandler(new ViewHandlerImpl());
        application.setStateManager(new StateManagerImpl());
    }
    
    //
    // Test Methods
    //
    
    
    public void testHighAvailabilityStateSaving1() {
       
        // precreate tree and set it in session and make sure the tree is
        // restored from session.
        UIViewRoot root = application.getViewHandler().createView(getFacesContext(), null);
        root.setViewId(TEST_URI);

        UIForm basicForm = new UIForm();
        basicForm.setId("basicForm");
        UIInput userName = new UIInput();

        userName.setId("userName");
        userName.setTransient(true);
        root.getChildren().add(basicForm);
        basicForm.getChildren().add(userName);

        UIPanel panel1 = new UIPanel();
        panel1.setId("panel1");
        basicForm.getChildren().add(panel1);

        UIInput userName1 = new UIInput();
        userName1.setId("userName1");
        panel1.getChildren().add(userName1);

        getFacesContext().setViewRoot(root);

        StateManager stateManager =
            getFacesContext().getApplication().getStateManager();
        stateManager.saveSerializedView(getFacesContext());

        // make sure that the value of viewId attribute in session is an
        // instance of SerializedView.
        Object result = session.getAttribute(TEST_URI);
        assertTrue(result instanceof SerializedView);
        
        root = stateManager.restoreView(getFacesContext(), TEST_URI,
                                 RenderKitFactory.HTML_BASIC_RENDER_KIT);
       
        assertTrue(root != null);
        basicForm = (UIForm) root.findComponent("basicForm");
        assertTrue(basicForm != null);

        userName = (UIInput) basicForm.findComponent("userName");
        assertTrue(userName == null);

        panel1 = (UIPanel) basicForm.findComponent("panel1");
        assertTrue(panel1 != null);

        userName1 = (UIInput) panel1.findComponent("userName1");
        assertTrue(userName1 != null);
    }

}
