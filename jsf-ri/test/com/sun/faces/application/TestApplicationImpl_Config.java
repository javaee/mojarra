/*
 * $Id: TestApplicationImpl_Config.java,v 1.1 2003/05/01 06:20:44 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestApplicationImpl_Config.java

package com.sun.faces.application;

import com.sun.faces.application.ApplicationFactoryImpl;
import com.sun.faces.application.ApplicationImpl;
import com.sun.faces.application.NavigationHandlerImpl;
import com.sun.faces.el.PropertyResolverImpl;
import com.sun.faces.el.VariableResolverImpl;

import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.NavigationHandler;
import javax.faces.el.PropertyResolver;
import javax.faces.el.ReferenceSyntaxException;
import javax.faces.el.VariableResolver;
import javax.faces.event.ActionListener;
import javax.faces.event.PhaseId;
import javax.faces.event.ActionEvent;
import javax.faces.FactoryFinder;
import javax.faces.component.*;

import org.mozilla.util.Assert;
import com.sun.faces.ServletFacesTestCase;
import com.sun.faces.TestComponent;

/**
 *
 *  <B>TestApplicationImpl_Config</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestApplicationImpl_Config.java,v 1.1 2003/05/01 06:20:44 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestApplicationImpl_Config extends ServletFacesTestCase {
//
// Protected Constants
//
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

    public TestApplicationImpl_Config() {super("TestApplicationImpl_Config");}
    public TestApplicationImpl_Config(String name) {super(name);}
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
	

    //
    // Test Config related methods
    //

    public void testAddComponentPositive() {
	TestComponent 
	    newTestComponent = null,
	    testComponent = new TestComponent();
	UIComponent uic = null;
	
	// runtime addition
	
	application.addComponent(testComponent.getComponentType(),
				 "com.sun.faces.TestComponent");
	assertTrue(null != (newTestComponent = (TestComponent)
			    application.getComponent(testComponent.getComponentType())));
	assertTrue(newTestComponent != testComponent);

	// built-in components
	assertTrue(null != (uic = application.getComponent("Command")));
	assertTrue(uic instanceof UICommand);
	
	assertTrue(null != (uic = application.getComponent("Form")));
	assertTrue(uic instanceof UIForm);
	
	assertTrue(null != (uic = application.getComponent("Graphic")));
	assertTrue(uic instanceof UIGraphic);
	
	assertTrue(null != (uic = application.getComponent("Input")));
	assertTrue(uic instanceof UIInput);
	
	assertTrue(null != (uic = application.getComponent("NamingContainer")));
	assertTrue(uic instanceof UINamingContainer);
	
	assertTrue(null != (uic = application.getComponent("Output")));
	assertTrue(uic instanceof UIOutput);
	
	assertTrue(null != (uic = application.getComponent("Panel")));
	assertTrue(uic instanceof UIPanel);
	
	assertTrue(null != (uic = application.getComponent("Parameter")));
	assertTrue(uic instanceof UIParameter);
	

	assertTrue(null != (uic = application.getComponent("SelectBoolean")));
	assertTrue(uic instanceof UISelectBoolean);
	
	assertTrue(null != (uic = application.getComponent("SelectItem")));
	assertTrue(uic instanceof UISelectItem);
	
	assertTrue(null != (uic = application.getComponent("SelectItems")));
	assertTrue(uic instanceof UISelectItems);
	
	assertTrue(null != (uic = application.getComponent("SelectMany")));
	assertTrue(uic instanceof UISelectMany);
	
	assertTrue(null != (uic = application.getComponent("SelectOne")));
	assertTrue(uic instanceof UISelectOne);
	
    }
	

} // end of class TestApplicationImpl_Config
