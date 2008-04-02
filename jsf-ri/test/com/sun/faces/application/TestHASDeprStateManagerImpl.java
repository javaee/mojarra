/*
 * $Id: TestHASDeprStateManagerImpl.java,v 1.1 2005/08/10 13:35:39 rogerk Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.application;

import org.apache.cactus.WebRequest;
import org.apache.cactus.server.ServletConfigWrapper;
import com.sun.faces.RIConstants;
import com.sun.faces.ServletFacesTestCase;
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
                                                                                                                      
    public String getExpectedOutputFilename() {
        return "TestViewHandlerImpl_correct";
    }
                                                                                                                      
                                                                                                                      
    public static final String ignore[] = {
    };
                                                                                                                      
    public String[] getLinesToIgnore() {
        return ignore;
    }

    public boolean sendResponseToFile() {
        return true;
    }

    //
    // Constructors/Initializers
    //
    public TestHASDeprStateManagerImpl() {
        super("TestHASDeprStateManagerImpl");
    }


    public TestHASDeprStateManagerImpl(String name) {
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
        application.setStateManager(new DeprStateManagerImpl());
    }
    
    //
    // Test Methods
    //
    
    public void beginRender(WebRequest theRequest) {
        theRequest.setURL("localhost:8080", "/test", "/faces", TEST_URI, null);
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
