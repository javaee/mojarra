/*
 * $Id: TestStateManagerImpl.java,v 1.3 2003/12/22 23:25:59 eburns Exp $
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

        UIViewRoot root = null;
        
        UIComponent comp1 = null;
        
        UIComponent comp2 = null;
        
        UIComponent comp3 = null;
        
        UIComponent facet1 = null;
        
        UIComponent facet2 = null;
        
        // construct a view
        root = new UIViewRoot();
        root.setViewId("/test");
        root.setId("root");
        
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
                
        FacesContext context = getFacesContext();
        HttpSession session = 
            (HttpSession) context.getExternalContext().getSession(false);
        session.setAttribute("/test", root);
        
        
        
        context.setViewRoot(root);
        
        StateManagerImpl stateManager = new StateManagerImpl();
        
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
        root = new UIViewRoot();
        root.setViewId("/test");
        root.setId("root");
        
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
        root = new UIViewRoot();
        root.setViewId("/test");
        root.setId("root");
        
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
	
}
