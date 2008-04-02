/*
 * $Id: TestApplicationImpl.java,v 1.7 2003/08/22 19:25:11 rlubke Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestApplicationImpl.java

package com.sun.faces.application;

import com.sun.faces.application.ApplicationFactoryImpl;
import com.sun.faces.application.ApplicationImpl;
import com.sun.faces.application.NavigationHandlerImpl;
import com.sun.faces.el.PropertyResolverImpl;
import com.sun.faces.el.VariableResolverImpl;

import javax.faces.component.UIComponent;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.NavigationHandler;
import javax.faces.el.PropertyResolver;
import javax.faces.el.ReferenceSyntaxException;
import javax.faces.el.VariableResolver;
import javax.faces.el.ValueBinding;
import javax.faces.event.ActionListener;
import javax.faces.event.PhaseId;
import javax.faces.event.ActionEvent;
import javax.faces.FactoryFinder;
import javax.faces.FacesException;

import org.mozilla.util.Assert;
import com.sun.faces.JspFacesTestCase;
import com.sun.faces.TestComponent;

/**
 *
 *  <B>TestApplicationImpl</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestApplicationImpl.java,v 1.7 2003/08/22 19:25:11 rlubke Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestApplicationImpl extends JspFacesTestCase {
//
// Protected Constants
//
    public static final String HANDLED_ACTIONEVENT1 = "handledValueEvent1";
    public static final String HANDLED_ACTIONEVENT2 = "handledValueEvent2";

//
// Class Variables
//

//
// Instance Variables
//
    private ApplicationImpl application = null;

// Attribute Instance Variables

// Relationship Instance Variables

//
// Constructors and Initializers    
//

    public TestApplicationImpl() {super("TestApplicationImpl");}
    public TestApplicationImpl(String name) {super(name);}
//
// Class methods
//

//
// General Methods
//

    public void setUp() {
	super.setUp();
        ApplicationFactory aFactory = 
	    (ApplicationFactory)FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        application = (ApplicationImpl) aFactory.getApplication();
    }
	

    public void testAccessors() {

        // 1. Verify "getActionListener" returns the same ActionListener
        //    instance if called multiple times.
        //
        ActionListener actionListener1 = new ValidActionListener();
        application.setActionListener(actionListener1);
        ActionListener actionListener2 = application.getActionListener();
        ActionListener actionListener3 = application.getActionListener();
        assertTrue((actionListener1 == actionListener2) && 
                   (actionListener1 == actionListener3));

        // 2. Verify "getNavigationHandler" returns the same NavigationHandler
        //    instance if called multiple times.
        //
        NavigationHandler navigationHandler1 = new NavigationHandlerImpl();
        application.setNavigationHandler(navigationHandler1);
        NavigationHandler navigationHandler2 = application.getNavigationHandler();
        NavigationHandler navigationHandler3 = application.getNavigationHandler();
        assertTrue((navigationHandler1 == navigationHandler2) && 
                   (navigationHandler1 == navigationHandler3));

        // 3. Verify "getPropertyResolver" returns the same PropertyResolver
        //    instance if called multiple times.
        //
        PropertyResolver propertyResolver1 = new PropertyResolverImpl();
        application.setPropertyResolver(propertyResolver1);
        PropertyResolver propertyResolver2 = application.getPropertyResolver();
        PropertyResolver propertyResolver3 = application.getPropertyResolver();
        assertTrue((propertyResolver1 == propertyResolver2) && 
                   (propertyResolver1 == propertyResolver3));

        // 4. Verify "getVariableResolver" returns the same VariableResolver
        //    instance if called multiple times.
        //
        VariableResolver variableResolver1 = new VariableResolverImpl();
        application.setVariableResolver(variableResolver1);
        VariableResolver variableResolver2 = application.getVariableResolver();
        VariableResolver variableResolver3 = application.getVariableResolver();
        assertTrue((variableResolver1 == variableResolver2) && 
                   (variableResolver1 == variableResolver3));

    }

    public void testExceptions() {
        boolean thrown;

        // 1. Verify NullPointer exception which occurs when attempting
        //    to set a null ActionListener
        //
        thrown = false;
        try {
            application.setActionListener(null);
        } catch (NullPointerException e) {
            thrown = true;
        }
        assertTrue(thrown);

        // 2. Verify NullPointer exception which occurs when attempting
        //    to set an ActionListener with the wrong PhaseId
        //
        ActionListener actionListener = new InvalidActionListener();
        thrown = false;
        try {
            application.setActionListener(actionListener);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        assertTrue(thrown);

        // 3. Verify NullPointer exception which occurs when attempting
        //    to set a null NavigationHandler
        //
        thrown = false;
        try {
            application.setNavigationHandler(null);
        } catch (NullPointerException e) {
            thrown = true;
        }
        assertTrue(thrown);

        // 4. Verify NullPointer exception which occurs when attempting
        //    to set a null PropertyResolver
        //
        thrown = false;
        try {
            application.setPropertyResolver(null);
        } catch (NullPointerException e) {
            thrown = true;
        }
        assertTrue(thrown);

        // 5. Verify NullPointer exception which occurs when attempting
        //    to get a ValueBinding with a null ref
        //
        thrown = false;
        try {
            application.getValueBinding(null);
        } catch (NullPointerException e) {
            thrown = true;
        }
        assertTrue(thrown);

        // 6. Verify NullPointer exception which occurs when attempting
        //    to set a null VariableResolver
        //
        thrown = false;
        try {
            application.setVariableResolver(null);
        } catch (NullPointerException e) {
            thrown = true;
        }
        assertTrue(thrown);
       
        thrown = false;
        try {
            application.getValueBinding("improper expression");
        } catch (ReferenceSyntaxException e) {
            thrown = true;
        }
        assertTrue(thrown);
        
        thrown = false;
        try {
            application.getValueBinding("improper\texpression");
        } catch (ReferenceSyntaxException e) {
            thrown = true;
        }
        assertTrue(thrown);
        
        thrown = false;
        try {
            application.getValueBinding("improper\rexpression");
        } catch (ReferenceSyntaxException e) {
            thrown = true;
        }
        assertTrue(thrown);
        
        thrown = false;
        try {
            application.getValueBinding("improper\nexpression");
        } catch (ReferenceSyntaxException e) {
            thrown = true;
        }
        assertTrue(thrown);

        thrown = false;
        try {
            application.getValueBinding("proper[\"a key\"]");
        } catch (ReferenceSyntaxException e) {
            thrown = true;
        }
        assertTrue(!thrown); 
        
        try {
            application.getValueBinding("proper[\"a { } key\"]");
        } catch (ReferenceSyntaxException e) {
            thrown = true;
        }
        assertTrue(!thrown);
        
        try {
            application.getValueBinding("bean.a{indentifer");
        } catch (ReferenceSyntaxException e) {
            thrown = true;
        }
        assertTrue(thrown);
        
        thrown = false;
        try {
            application.getValueBinding("bean['invalid'");            
        } catch (ReferenceSyntaxException e) {
            thrown = true;
        }
        assertTrue(thrown);
        
        thrown = false;
        try {
            application.getValueBinding("bean[[\"invalid\"]].foo");
        } catch (ReferenceSyntaxException e) {
            thrown = true;
        }
        assertTrue(thrown);
        
        thrown = false;
        try {
            application.getValueBinding("bean[\"[a\"]");
        } catch (ReferenceSyntaxException e) {
            thrown = true;
        }
        assertTrue(!thrown);
        
        try {
            application.getValueBinding("bean[\".a\"]");
        } catch (ReferenceSyntaxException e) {
            thrown = true;
        }
        assertTrue(!thrown);             
    }

            

    public class InvalidActionListener implements ActionListener {
        public PhaseId getPhaseId() {
	    return PhaseId.ANY_PHASE;
        }

        public void processAction(ActionEvent event) {
	    System.setProperty(HANDLED_ACTIONEVENT1, HANDLED_ACTIONEVENT1);
        }
    }

    public class ValidActionListener implements ActionListener {
        public PhaseId getPhaseId() {
	    return PhaseId.INVOKE_APPLICATION;
        }

        public void processAction(ActionEvent event) {
	    System.setProperty(HANDLED_ACTIONEVENT2, HANDLED_ACTIONEVENT2);
        }
    }

    //
    // Test Config related methods
    //

    public void testAddComponentPositive() {
	TestComponent 
	    newTestComponent = null,
	    testComponent = new TestComponent();
	
	
	application.addComponent(testComponent.getComponentType(),
				 "com.sun.faces.TestComponent");
	assertTrue(null != (newTestComponent = (TestComponent)
			    application.createComponent(testComponent.getComponentType())));
	assertTrue(newTestComponent != testComponent);
	
    }

    public void testGetComponentWithRefNegative() {
	ValueBinding valueBinding = null;
	boolean exceptionThrown = false;
	UIComponent result = null;
	getFacesContext().getExternalContext().getSessionMap().put("TAIBean",
								   this);
	assertTrue(null != (valueBinding = 
		    application.getValueBinding("sessionScope.TAIBean")));

	try {
	    result = application.createComponent(valueBinding, getFacesContext(),
					      "notreached");
	    assertTrue(false);
	}
	catch (FacesException e) {
	    exceptionThrown = true;
	}
	assertTrue(exceptionThrown);
    }
	

} // end of class TestApplicationImpl
