/*
 * $Id: TestApplicationImpl_Config.java,v 1.18 2003/08/25 05:39:54 eburns Exp $
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
import javax.faces.application.ViewHandler;
import javax.faces.el.PropertyResolver;
import javax.faces.el.ReferenceSyntaxException;
import javax.faces.el.VariableResolver;
import javax.faces.event.ActionListener;
import javax.faces.event.PhaseId;
import javax.faces.event.ActionEvent;
import javax.faces.FactoryFinder;
import javax.faces.component.*;
import javax.faces.convert.*;
import javax.faces.validator.Validator;
import javax.faces.validator.LengthValidator;
import javax.faces.application.Message;

import org.mozilla.util.Assert;
import com.sun.faces.ServletFacesTestCase;
import com.sun.faces.TestComponent;
import com.sun.faces.TestConverter;
import com.sun.faces.application.MessageResourcesImpl;
import com.sun.faces.config.*;
import javax.faces.application.MessageResources;
import javax.faces.FacesException;

import java.util.HashMap;
import java.util.Iterator;

/**
 *
 *  <B>TestApplicationImpl_Config</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestApplicationImpl_Config.java,v 1.18 2003/08/25 05:39:54 eburns Exp $
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

    public void testComponentPositive() {
	TestComponent 
	    newTestComponent = null,
	    testComponent = new TestComponent();
	UIComponent uic = null;
	
	// runtime addition
	
	application.addComponent(testComponent.getComponentType(),
				 "com.sun.faces.TestComponent");
	assertTrue(null != (newTestComponent = (TestComponent)
			    application.createComponent(testComponent.getComponentType())));
	assertTrue(newTestComponent != testComponent);

	// built-in components
	assertTrue(null != (uic = application.createComponent("Command")));
	assertTrue(uic instanceof UICommand);
	
	assertTrue(null != (uic = application.createComponent("Form")));
	assertTrue(uic instanceof UIForm);
	
	assertTrue(null != (uic = application.createComponent("Graphic")));
	assertTrue(uic instanceof UIGraphic);
	
	assertTrue(null != (uic = application.createComponent("Input")));
	assertTrue(uic instanceof UIInput);
	
	assertTrue(null != (uic = application.createComponent("NamingContainer")));
	assertTrue(uic instanceof NamingContainer);
	
	assertTrue(null != (uic = application.createComponent("Output")));
	assertTrue(uic instanceof UIOutput);
	
	assertTrue(null != (uic = application.createComponent("Panel")));
	assertTrue(uic instanceof UIPanel);
	
	assertTrue(null != (uic = application.createComponent("Parameter")));
	assertTrue(uic instanceof UIParameter);
	

	assertTrue(null != (uic = application.createComponent("SelectBoolean")));
	assertTrue(uic instanceof UISelectBoolean);
	
	assertTrue(null != (uic = application.createComponent("SelectItem")));
	assertTrue(uic instanceof UISelectItem);
	
	assertTrue(null != (uic = application.createComponent("SelectItems")));
	assertTrue(uic instanceof UISelectItems);
	
	assertTrue(null != (uic = application.createComponent("SelectMany")));
	assertTrue(uic instanceof UISelectMany);
	
	assertTrue(null != (uic = application.createComponent("SelectOne")));
	assertTrue(uic instanceof UISelectOne);
        
    assertTrue(null != (uic = application.createComponent("ViewRoot")));
    assertTrue(uic instanceof UIViewRoot);
	
    }
	
    public void testComponentNegative() {
	boolean exceptionThrown = false;
	
	// componentType/componentClass with non-existent class
	try {
	    application.addComponent("William",
				     "BillyBoy");
	    application.createComponent("William");
	}
	catch (FacesException e) {
	    exceptionThrown = true;
	}
	assertTrue(exceptionThrown);

	// non-existent mapping
	exceptionThrown = false;
	try {
	    application.createComponent("Joebob");
	}
	catch (FacesException e) {
	    exceptionThrown = true;
	}
	assertTrue(exceptionThrown);
	
    }

    public void testGetComponentTypes() {
	Iterator iter = application.getComponentTypes();
	assertTrue(null != iter);
	String standardComponentTypes[] = {
	    "Command",
	    "Form",
	    "Graphic",
	    "Input",
	    "NamingContainer",
	    "Output",
	    "Panel",
	    "Parameter",
	    "SelectBoolean",
	    "SelectItem",
	    "SelectItems",
	    "SelectMany",
	    "SelectOne"
	};
	
	assertTrue(isSubset(standardComponentTypes, iter));
    }

    public void testConverterPositive() {
	TestConverter 
	    newTestConverter = null,
	    testConverter = new TestConverter();
	Converter conv = null;
	
	// runtime addition
	
	application.addConverter(testConverter.getConverterId(),
				 "com.sun.faces.TestConverter");
	assertTrue(null != (newTestConverter = (TestConverter)
			    application.createConverter(testConverter.getConverterId())));
	assertTrue(newTestConverter != testConverter);

	// built-in components
	assertTrue(null != (conv = application.createConverter("DateTime")));
	assertTrue(conv instanceof DateTimeConverter);

	assertTrue(null != (conv = application.createConverter("Number")));
	assertTrue(conv instanceof NumberConverter);

	assertTrue(null != (conv = application.createConverter(java.lang.Boolean.class)));
	assertTrue(conv instanceof BooleanConverter);
	
	assertTrue(null != (conv = application.createConverter(java.lang.Byte.class)));
	assertTrue(conv instanceof ByteConverter);
	
	assertTrue(null != (conv = application.createConverter(java.lang.Character.class)));
	assertTrue(conv instanceof CharacterConverter);
	
	assertTrue(null != (conv = application.createConverter(java.lang.Double.class)));
	assertTrue(conv instanceof DoubleConverter);
	
	assertTrue(null != (conv = application.createConverter(java.lang.Float.class)));
	assertTrue(conv instanceof FloatConverter);
	
	assertTrue(null != (conv = application.createConverter(java.lang.Integer.class)));
	assertTrue(conv instanceof IntegerConverter);
	
	assertTrue(null != (conv = application.createConverter(java.lang.Long.class)));
	assertTrue(conv instanceof LongConverter);
	
	assertTrue(null != (conv = application.createConverter(java.lang.Short.class)));
	assertTrue(conv instanceof ShortConverter);
	
    }
	
    public void testConverterNegative() {
	boolean exceptionThrown = false;
	
	// componentType/componentClass with non-existent class
	try {
	    application.addConverter("William",
				     "BillyBoy");
	    application.createConverter("William");
	}
	catch (FacesException e) {
	    exceptionThrown = true;
	}
	assertTrue(exceptionThrown);

	// non-existent mapping
	exceptionThrown = false;
	try {
	    application.createConverter("Joebob");
	}
	catch (FacesException e) {
	    exceptionThrown = true;
	}
	assertTrue(exceptionThrown);
	
    }

    public void testGetConverterIds() {
	Iterator iter = application.getConverterIds();
	assertTrue(null != iter);
	String standardConverterIds[] = {
	    "DateTime",
	    "Number"
	};

	assertTrue(isSubset(standardConverterIds, iter));
    }

    public void testMessageResorucesPositive() {
	MessageResourcesImpl 
	    newMessageResourcesImpl = null,
	    testMessageResoruces = new MessageResourcesImpl("Foo");
	MessageResources rsrc = null;
	
	// runtime addition
	
	application.addMessageResources("FreshResources",
				 "com.sun.faces.application.MessageResourcesImpl");
	assertTrue(null != (newMessageResourcesImpl = (MessageResourcesImpl)
			    application.getMessageResources("FreshResources")));
	assertTrue(newMessageResourcesImpl != testMessageResoruces);

	// built-in MessageResources
	assertTrue(null != (rsrc = application.getMessageResources(MessageResources.FACES_API_MESSAGES)));
	assertTrue(rsrc instanceof MessageResources);
	Object params[] = new Object[1];
        assertTrue(null != ((MessageResourcesImpl)rsrc).getMessage("javax.faces.validator.DoubleRangeValidator.LIMIT",params));
	assertTrue(null != (rsrc = application.getMessageResources(MessageResources.FACES_IMPL_MESSAGES)));
	assertTrue(rsrc instanceof MessageResources);
        assertTrue(null != ((MessageResourcesImpl)rsrc).getMessage("com.sun.faces.NULL_PARAMETERS_ERROR",params));
    }

    public void testMessageResourcesNegative() {
	boolean exceptionThrown = false;
	
	// componentType/componentClass with non-existent class
	try {
	    application.addMessageResources("William",
				     "BillyBoy");
	    application.getMessageResources("William");
	}
	catch (FacesException e) {
	    exceptionThrown = true;
	}
	assertTrue(exceptionThrown);

	// non-existent mapping
	// make sure an instance got created..
	try {
	    application.getMessageResources("Joebob");
	}
	catch (FacesException e) {
	    assertTrue(false);
	}
        assertTrue(null != application.getMessageResources("Joebob"));	
    }

    public void testGetMessageResourcesIds() {
	Iterator iter = application.getMessageResourcesIds();
	assertTrue(null != iter);
	String standardMessageResourcesIds[] = {
	    MessageResources.FACES_API_MESSAGES,
	    MessageResources.FACES_IMPL_MESSAGES
	};

	assertTrue(isSubset(standardMessageResourcesIds, iter));
    }

    public void testValidatorPositive() {
	Validator 
	    newTestValidator = null,
	    testValidator = new LengthValidator();
	Validator val = null;
	
	// runtime addition
	
	application.addValidator("Billybob",
				 "javax.faces.validator.LengthValidator");
	assertTrue(null != (newTestValidator = (Validator)
			    application.createValidator("Billybob")));
	assertTrue(newTestValidator != testValidator);

	// test standard components
	assertTrue(null != (val = application.createValidator("DoubleRange")));
	assertTrue(val instanceof Validator);
	assertTrue(null != (val = application.createValidator("Length")));
	assertTrue(val instanceof Validator);
	assertTrue(null != (val = application.createValidator("LongRange")));
	assertTrue(val instanceof Validator);

    }
	
    public void testValidatorNegative() {
	boolean exceptionThrown = false;
	
	// componentType/componentClass with non-existent class
	try {
	    application.addValidator("William",
				     "BillyBoy");
	    application.createValidator("William");
	}
	catch (FacesException e) {
	    exceptionThrown = true;
	}
	assertTrue(exceptionThrown);

	// non-existent mapping
	exceptionThrown = false;
	try {
	    application.createValidator("Joebob");
	}
	catch (FacesException e) {
	    exceptionThrown = true;
	}
	assertTrue(exceptionThrown);
	
    }

    public void testGetValidatorIds() {
	Iterator iter = application.getValidatorIds();
	assertTrue(null != iter);
	String standardValidatorIds[] = {
	    "DoubleRange",
	    "Length",
	    "LongRange",
	    "StringRange"	
	};
	
	assertTrue(isSubset(standardValidatorIds, iter));
    }

    public void testUpdateRuntimeComponents() {
	loadFromInitParam("runtime-components.xml");
        ApplicationFactory aFactory = 
	    (ApplicationFactory)FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        application = (ApplicationImpl) aFactory.getApplication();

	ActionListener actionListener = null;
	NavigationHandler navHandler = null;
	PropertyResolver propResolver = null;
	VariableResolver varResolver = null;
	ViewHandler viewHandler = null;
	
	assertTrue(null != (actionListener = 
			    application.getActionListener()));
	assertTrue(actionListener instanceof ActionListenerTestImpl);

	assertTrue(null != (navHandler = 
			    application.getNavigationHandler()));
	assertTrue(navHandler instanceof NavigationHandlerTestImpl);

	assertTrue(null != (propResolver = 
			    application.getPropertyResolver()));
	assertTrue(propResolver instanceof PropertyResolverTestImpl);

	assertTrue(null != (varResolver = 
			    application.getVariableResolver()));
	assertTrue(varResolver instanceof VariableResolverTestImpl);

	assertTrue(null != (viewHandler = 
			    application.getViewHandler()));
	assertTrue(viewHandler instanceof ViewHandlerTestImpl);
    }


} // end of class TestApplicationImpl_Config
