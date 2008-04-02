/*
 * $Id: TestStateManagerImpl.java,v 1.1 2003/10/03 17:43:40 rlubke Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.application;

import com.sun.faces.ServletFacesTestCase;

import javax.faces.component.UIViewRoot;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.component.UIGraphic;
import javax.faces.context.FacesContext;
import javax.faces.application.ViewHandler;
import javax.servlet.http.HttpSession;


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
    public void testDuplicateIdDetection() throws Exception {
        
        
        // construct a view
        UIViewRoot root = new UIViewRoot();
        root.setViewId("/test");
        root.setId("root");
        
        UIComponent comp1 = new UIInput();
        comp1.setId("comp1");
        
        UIComponent comp2 = new UIOutput();
        comp2.setId("comp2");
        
        UIComponent comp3 = new UIGraphic();
        comp3.setId("comp3");
        
        UIComponent facet1 = new UIOutput();
        facet1.setId("comp4");
        
        UIComponent facet2 = new UIOutput();
        facet2.setId("comp2");
        
        comp2.getFacets().put("facet1", facet1);
        comp2.getFacets().put("facet2", facet2);
        
        root.getChildren().add(comp1);
        root.getChildren().add(comp2);
        root.getChildren().add(comp3);
                
        FacesContext context = getFacesContext();
        HttpSession session = 
            (HttpSession) context.getExternalContext().getSession(false);
        session.setAttribute("/test", root);
        
        
        
        context.setViewRoot(root);
        
        ViewHandler viewHandler = new ViewHandlerImpl();
        
        boolean exceptionThrown = false;
        try {
            viewHandler.getStateManager().saveSerializedView(context);
        } catch (IllegalStateException ise) {
            exceptionThrown = true;
        }        
        assertTrue(exceptionThrown);
        
        
        // multiple componentns with a null ID should not
        // trigger an exception
        comp2.setId(null);
        comp3.setId(null);
        
        exceptionThrown = false;
        try {
            viewHandler.getStateManager().saveSerializedView(context);
        } catch (IllegalStateException ise) {
            exceptionThrown = true;
        }
        assertTrue(!exceptionThrown);
        
        // transient components with duplicate ids should 
        // trigger an error condition
        comp2.setId("comp2");
        comp2.setTransient(true);
        comp3.setId("comp1");
        comp3.setTransient(true);
        
        exceptionThrown = false;
        try {
            viewHandler.getStateManager().saveSerializedView(context);
        } catch (IllegalStateException ise) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }
}