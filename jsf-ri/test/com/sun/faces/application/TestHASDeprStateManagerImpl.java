/*
 * $Id: TestHASDeprStateManagerImpl.java,v 1.4 2006/03/29 22:39:35 rlubke Exp $
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

import org.apache.cactus.WebRequest;
import org.apache.cactus.server.ServletConfigWrapper;
import com.sun.faces.RIConstants;
import com.sun.faces.cactus.ServletFacesTestCase;
import com.sun.faces.util.Util;

import javax.faces.FacesException;
import javax.faces.application.StateManager;
import javax.faces.application.ViewHandler;
import javax.faces.application.StateManager.SerializedView;
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

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import java.io.IOException;
import java.util.ArrayList;


/**
 * This class tests the <code>StateManagerImpl</code> class with deprecated
 * methods only - does not contain any of the replacement methods (such
 * as saveView).
 */
public class TestHASDeprStateManagerImpl extends ServletFacesTestCase {


    public static final String TEST_URI = "/greeting.jsp";
                                                                                                                      
                                                                                                                      
    public static final String ignore[] = {
    };
    
    private Application application = null;


    // ------------------------------------------------------------ Constructors


    public TestHASDeprStateManagerImpl() {

        super("TestHASDeprStateManagerImpl");

    }


    public TestHASDeprStateManagerImpl(String name) {

        super(name);

    }


    // ---------------------------------------------- Methods From FacesTestCase

    public boolean sendResponseToFile() {

        return true;

    }


    public String getExpectedOutputFilename() {

        return "TestViewHandlerImpl_correct";

    }


    public String[] getLinesToIgnore() {

        return ignore;

    }


    // ---------------------------------------------------------- Public Methods


    public void beginRender(WebRequest theRequest) {

        theRequest.setURL("localhost:8080", "/test", "/faces", TEST_URI, null);

    }


    public void setUp() {

        super.setUp();
        ApplicationFactory aFactory =
            (ApplicationFactory) FactoryFinder.getFactory(
                FactoryFinder.APPLICATION_FACTORY);
        application = (ApplicationImpl) aFactory.getApplication();
        application.setViewHandler(new ViewHandlerImpl());
        application.setStateManager(new DeprStateManagerImpl());

    }


    public void testRender() {

        UIViewRoot newView = Util.getViewHandler(getFacesContext()).createView(getFacesContext(), null);
        newView.setViewId(TEST_URI);
        getFacesContext().setViewRoot(newView);
                                                                                                                      
        try {
            ViewHandler viewHandler =
                Util.getViewHandler(getFacesContext());
            viewHandler.renderView(getFacesContext(),
                                   getFacesContext().getViewRoot());
        } catch (IOException e) {
            System.out.println("ViewHandler IOException:" + e);
        } catch (FacesException fe) {
            System.out.println("ViewHandler FacesException: " + fe);
        }
                                                                                                                      
        assertTrue(!(getFacesContext().getRenderResponse()) &&
                   !(getFacesContext().getResponseComplete()));
                                                                                                                      
        assertTrue(verifyExpectedOutput());

    }

}
