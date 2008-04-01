/*
 * $Id: TestAbstractFactory.java,v 1.4 2002/04/15 20:11:03 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestAbstractFactory.java

package com.sun.faces;

import javax.faces.AbstractFactory;
import javax.faces.FactoryConfigurationError;
import javax.faces.MessageFactory;
import javax.faces.MessageList;
import javax.faces.FacesContext;
import javax.faces.EventQueue;
import javax.faces.FacesException;
import javax.faces.Constants;
import javax.faces.NavigationMap;
import javax.faces.NavigationHandler;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import javax.faces.ObjectAccessor;
import javax.faces.ConverterManager;

import java.util.HashMap;
import java.util.Properties;

import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;

/**
 *
 *  <B>TestAbstractFactory</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestAbstractFactory.java,v 1.4 2002/04/15 20:11:03 jvisvanathan Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestAbstractFactory extends FacesTestCase
{
//
// Protected Constants
//

    protected static final String WIDGET_CLASSNAME = "com.sun.faces.WidgetFactory";

//
// Class Variables
//

//
// Instance Variables
//

// Attribute Instance Variables

// Relationship Instance Variables

    protected AbstractFactory abstractFactory;

//
// Constructors and Initializers    
//

    public TestAbstractFactory() {super("TestAbstractFactory");}
    public TestAbstractFactory(String name) {super(name);}
//
// Class methods
//

//
// Methods from TestCase
//

public void setUp()
{
    abstractFactory = new AbstractFactory();
    assertTrue(null != abstractFactory);
}

public void tearDown()
{
    abstractFactory.destroy();
}



//
// General Methods
//

public void testFactoryLookup()
{
    AbstractFactory abstractFactory = new AbstractFactory();
    WidgetFactory.Widget widget = null;
    HashMap widgetMap = new HashMap();
    boolean result = false;

    // local vars for dealing with the properties file: faces.properties
    Properties props = new Properties();
    File propsFile = null, tempFile = null;
    FileOutputStream newPropsFos;
    String javah=System.getProperty( "java.home" );
    String configFile = javah + File.separator +
	"lib" + File.separator + "faces.properties";
    


    // Use a new factory

    // Method one, a system property
    System.setProperty(WIDGET_CLASSNAME, WIDGET_CLASSNAME);

    widgetMap.put("paramOne", new Object());
    widgetMap.put("paramTwo", new Integer(1));
    widgetMap.put("paramThree", new Boolean(false));
    widgetMap.put("paramFour", "filename");

    widget = (WidgetFactory.Widget) abstractFactory.newInstance(WIDGET_CLASSNAME, widgetMap);
    result = null != widget;
    assertTrue(result);
    System.getProperties().remove(WIDGET_CLASSNAME);

    // Method two, the file java.home/lib/faces.properties
    props.setProperty(WIDGET_CLASSNAME, WIDGET_CLASSNAME);

    try {
	propsFile = new File(configFile);
	if (propsFile.exists()) {
	    // rename the props file
	    tempFile = new File(configFile + ".orig");
	    assertTrue(propsFile.renameTo(tempFile));
	}
	newPropsFos = new FileOutputStream(propsFile);
	props.store(newPropsFos, "temporary faces.properties");
	newPropsFos.close();
    }
    catch (IOException e) {
	assertTrue(false);
    }

    widget = (WidgetFactory.Widget) abstractFactory.newInstance(WIDGET_CLASSNAME, widgetMap);
    result = null != widget;
    assertTrue(result);

    // delete the temporary props file and rename the old one, if present
    assertTrue(null != propsFile);
    assertTrue(propsFile.delete());
    if (null != tempFile) {
	assertTrue(tempFile.renameTo(propsFile));
    }

    // I can't test Method three.  
    
}

public void testHardCodedMethods()
{
    MessageFactory messageFactory = null;
    MessageList messageList = null;
    FacesContext facesContext = null;
   
    ConverterManager converterManager = null;
    NavigationHandler navHandler = null;
    ObjectAccessor objectAccessor = null;
    EventQueue eventQueue = null;

    boolean result;

    eventQueue = abstractFactory.newEventQueue();
    assertTrue(null != eventQueue);

    messageFactory = abstractFactory.newMessageFactory();
    assertTrue(null != messageFactory);

    
    ServletContext servletContext = request.getSession().getServletContext();
    converterManager = abstractFactory.newConverterManager(servletContext);
    assertTrue(null != converterManager);
    
    NavigationMap navMap = new NavigationMapImpl();
    navHandler = abstractFactory.newNavigationHandler(navMap);
    assertTrue(null != navHandler);
    
    facesContext = abstractFactory.newFacesContext(request, response);
    assertTrue(null != facesContext);
    
    objectAccessor = abstractFactory.newObjectAccessor(facesContext);
    assertTrue(null != objectAccessor);
    
    messageList = abstractFactory.newMessageList();
    assertTrue(null != messageList);

}

public void testExceptions()
{
    boolean result;

    result = false;
    try {
	// Use the wrong constructor arguments
	abstractFactory.newInstance(Constants.REF_FACESCONTEXT);
    }
    catch (FacesException e) {
	result = true;
    }
    assertTrue(result);

    result = false;
    try {
	// Use a bogus factory
	abstractFactory.newInstance("BOGUS_STRING");
    }
    catch (FactoryConfigurationError e) {
	result = true;
    }
    assertTrue(result);
}

public void testNewFactory()
{
    boolean result;
    WidgetFactory factory = new WidgetFactory();
    WidgetFactory.Widget widget = null;
    HashMap widgetMap = new HashMap();
    final String WIDGET = "WIDGET";

    abstractFactory.addFactoryForFacesName(factory, WIDGET);

    widgetMap.put("paramOne", new Object());
    widgetMap.put("paramTwo", new Integer(1));
    widgetMap.put("paramThree", new Boolean(false));
    widgetMap.put("paramFour", "filename");

    widget = (WidgetFactory.Widget) abstractFactory.newInstance(WIDGET, 
								widgetMap);
    result = null != widget;
    assertTrue(result);
}



} // end of class TestAbstractFactory
