/*
 * $Id: ConfigFileTestCase.java,v 1.24 2003/08/06 19:42:13 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config;

import com.sun.faces.application.ApplicationImpl;
import com.sun.faces.application.MessageResourcesImpl;
import com.sun.faces.tree.SimpleTreeImpl;
import com.sun.faces.el.ValueBindingImpl;
import com.sun.faces.RIConstants;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.lang.reflect.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContext;
import javax.faces.FactoryFinder;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.NavigationHandler;
import javax.faces.component.UIComponent;
import javax.faces.application.MessageResources;
import javax.faces.convert.Converter;
import javax.faces.validator.Validator;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;

import org.apache.cactus.ServletTestCase;
import org.apache.cactus.WebRequest;
import org.mozilla.util.Assert;
import com.sun.faces.ServletFacesTestCase;

/**
 * <p>Unit tests for Configuration File processing.</p>
 */

public class ConfigFileTestCase extends ServletFacesTestCase {


    // ----------------------------------------------------- Instance Variables


    // ----------------------------------------------------------- Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public ConfigFileTestCase(String name) {

        super(name);

    }


    // --------------------------------------------------- Overall Test Methods




    // ------------------------------------------------ Individual Test Methods

    public void testMessageResources() {
	// test the default messages
        ApplicationFactory aFactory = (ApplicationFactory)FactoryFinder.getFactory(
        FactoryFinder.APPLICATION_FACTORY);
        ApplicationImpl application = (ApplicationImpl)aFactory.getApplication();
	Map 
	    messageResourcesMap = null,
	    messagesMap = null;
	String apiMessages [] = {
	    "javax.faces.validator.DoubleRangeValidator.LIMIT",
	    "javax.faces.validator.DoubleRangeValidator.MAXIMUM",
	    "javax.faces.validator.DoubleRangeValidator.MINIMUM",
	    "javax.faces.validator.DoubleRangeValidator.TYPE",
	    "javax.faces.validator.LengthValidator.LIMIT",
	    "javax.faces.validator.LengthValidator.MAXIMUM",
	    "javax.faces.validator.LengthValidator.MINIMUM",
	    "javax.faces.component.UIInput.REQUIRED",
	    "javax.faces.validator.LongRangeValidator.LIMIT",
	    "javax.faces.validator.LongRangeValidator.MAXIMUM",
	    "javax.faces.validator.LongRangeValidator.MINIMUM",
	    "javax.faces.validator.LongRangeValidator.TYPE",
	    "javax.faces.validator.StringRangeValidator.LIMIT",
	    "javax.faces.validator.StringRangeValidator.MAXIMUM",
	    "javax.faces.validator.StringRangeValidator.MINIMUM",
	    "javax.faces.validator.StringRangeValidator.TYPE",
	};
	Iterator messageIter = null;

	assertTrue(null != (application.getMessageResources(MessageResources.FACES_API_MESSAGES))); 
	assertTrue(null != (application.getMessageResources(MessageResources.FACES_IMPL_MESSAGES))); 

	MessageResourcesImpl mr = (MessageResourcesImpl)application.getMessageResources(MessageResources.FACES_API_MESSAGES);
	for (int i=0; i<apiMessages.length; i++) {
            Object[] params = new Object[1];
	    assertTrue(null != mr.getMessage(apiMessages[i], params));
	}
    }


    protected void parseConfig(ConfigParser cp,
                                     String resource,
                                     ServletContext context)
        throws Exception {

        InputStream stream = context.getResourceAsStream(resource);
        assertTrue(null != stream);
        cp.parseConfig(stream);
    }



    // Test parsing a full configuration file
    public void testFull() throws Exception {
        ConfigParser cp = new ConfigParser(config.getServletContext());
        ApplicationFactory aFactory = (ApplicationFactory)FactoryFinder.getFactory(
        FactoryFinder.APPLICATION_FACTORY);
        ApplicationImpl application = (ApplicationImpl)aFactory.getApplication();
        parseConfig(cp, "/WEB-INF/faces-config.xml",
                           config.getServletContext());

        // <application>
        assertTrue(application.getActionListener() instanceof com.sun.faces.TestActionListener);
        assertTrue(application.getNavigationHandler() instanceof com.sun.faces.TestNavigationHandler);
        assertTrue(application.getPropertyResolver() instanceof com.sun.faces.TestPropertyResolver);
        assertTrue(application.getVariableResolver() instanceof com.sun.faces.TestVariableResolver);

        // <component>

        Iterator iter = application.getComponentTypes();
        assertTrue(iter.hasNext());
        String cType = null;
        UIComponent comp = null;
        while (iter.hasNext()) {
            cType = (String)iter.next();
            comp = application.getComponent(cType);
            assertNotNull(comp);
        }
        UIComponent command = application.getComponent("Command");
        assertNotNull(command);
        comp = null;
        application.addComponent("fooType", "javax.faces.component.UICommand");
        comp = application.getComponent("fooType");
        assertNotNull(comp);

        // <converter>

        iter = application.getConverterIds();
        assertTrue(iter.hasNext());
        String convId = null;
        Converter conv = null;
        while (iter.hasNext()) {
            convId = (String)iter.next();
            conv = application.getConverter(convId);
            assertNotNull(conv);
        }
        Converter first = application.getConverter("First");
        assertNotNull(first);
        conv = null;
        application.addConverter("fooId", "com.sun.faces.convert.DateConverter");
        conv = application.getConverter("fooId");
        assertNotNull(conv);

        // <validator>

        iter = application.getValidatorIds();
        assertTrue(iter.hasNext());
        String valId = null;
        Validator val = null;
        while (iter.hasNext()) {
            valId = (String)iter.next();
            val = application.getValidator(valId);
            assertNotNull(val);
        }
        Validator second = application.getValidator("Second");
        assertNotNull(second);
        val = null;
        application.addValidator("fooId", "javax.faces.validator.DoubleRangeValidator");
        val = application.getValidator("fooId");
        assertNotNull(val);
    }

    public void testEmpty() throws Exception {
        ConfigParser cp = new ConfigParser(config.getServletContext());
        parseConfig(cp, "/WEB-INF/faces-config-empty.xml",
                           config.getServletContext());
    }

    // Assert that create and stored managed bean is the same as specified in the 
    // config file.
 
    public void testConfigManagedBeanFactory() throws Exception {

        ConfigParser cp = new ConfigParser(config.getServletContext());
        parseConfig(cp, "/WEB-INF/faces-config.xml",
                           config.getServletContext());

        ApplicationFactory aFactory = (ApplicationFactory)FactoryFinder.getFactory(
        FactoryFinder.APPLICATION_FACTORY);
        ApplicationImpl application = (ApplicationImpl)aFactory.getApplication();

        Object bean = application.createAndMaybeStoreManagedBeans(getFacesContext(),
            "SimpleBean");

	// Assert the correct methods have been created in the bean
	// Also assert the value returned from the "get" method matches
	// the one specified in the config file.
	//
	try {
	    Class c = bean.getClass();
	    Method m[] = c.getDeclaredMethods();
	    for (int i=0; i<m.length; i++) {
                Assert.assert_it(m[i].getName().equals("setSimpleProperty") ||
		    m[i].getName().equals("getSimpleProperty"));
		if (m[i].getName().equals("getSimpleProperty")) {
		    Object args[] = null;
	            Object value = m[i].invoke(bean, args);
		    Assert.assert_it(((String)value).equals("Bobby Orr"));
		}
	    }
	} catch (Throwable t) {
	    assertTrue(false);
	}
    }

    public void testNavigationCase() throws Exception {
        ConfigParser cp = new ConfigParser(config.getServletContext());
        parseConfig(cp, "/WEB-INF/faces-config.xml",
                            config.getServletContext());
        ApplicationFactory aFactory = 
            (ApplicationFactory)FactoryFinder.getFactory(
                 FactoryFinder.APPLICATION_FACTORY);
        ApplicationImpl application = (ApplicationImpl)aFactory.getApplication(); 
	NavigationHandler navHandler = application.getNavigationHandler();
	getFacesContext().setTree(new SimpleTreeImpl(getFacesContext(), "/login.jsp"));
        navHandler.handleNavigation(getFacesContext(), "UserBean.login",
	    "success");
        String newTreeId = getFacesContext().getTree().getTreeId();
        assertTrue(newTreeId.equals("/home.jsp"));	
     }

    public void testRenderKit() {
	// test the default renderkit
        ApplicationFactory aFactory = (ApplicationFactory)FactoryFinder.getFactory(
										   FactoryFinder.APPLICATION_FACTORY);
        ApplicationImpl application = (ApplicationImpl)aFactory.getApplication();
        RenderKitFactory renderKitFactory = (RenderKitFactory)
            FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
	Map 
	    renderKitsMap = null,
	    renderersMap = null;
	String defaultRenderers [] = {
	    "Button",
	    "Checkbox",
	    "Data",
	    "Date",
	    "DateTime",
	    "Errors",
	    "Form",
	    "Grid",
	    "Group",
	    "Hidden",
	    "Hyperlink",
	    "Image",
	    "Label",
	    "List",
	    "Listbox",
	    "Menu",
	    "Message",
	    "Number",
	    "Radio",
	    "Secret",
	    "SelectManyCheckbox",
	    "Textarea",
	    "Text",
	    "Time"
	};
	Iterator rendererIter = null;

        Iterator iter = renderKitFactory.getRenderKitIds();
	assertTrue(iter.hasNext());
	RenderKit renderKit = renderKitFactory.getRenderKit("DEFAULT");
	assertTrue(renderKit != null);
	for (int i=0; i<defaultRenderers.length; i++) {
	    assertTrue(null != (renderKit.getRenderer(defaultRenderers[i])));
	}
    }

    /**
     *
     *<p>Test that using the same name for different artifacts works.</p>
     */

    public void testDuplicateNames() throws Exception {
        ConfigParser cp = new ConfigParser(config.getServletContext());
        ApplicationFactory aFactory = (ApplicationFactory)FactoryFinder.getFactory(
        FactoryFinder.APPLICATION_FACTORY);
        ApplicationImpl application = (ApplicationImpl)aFactory.getApplication();
        parseConfig(cp, "config1.xml", config.getServletContext());
    }

    /**
     *
     * <p>Parse a config file that has a managed-bean entry that has an
     * error.  Make sure the expected behavior occurrs.</p>
     *
     */ 

    public void testConversionErrorDuringParse() throws Exception {
        ConfigParser cp = new ConfigParser(config.getServletContext());
        ApplicationFactory aFactory = (ApplicationFactory)FactoryFinder.getFactory(
        FactoryFinder.APPLICATION_FACTORY);
        ApplicationImpl application = (ApplicationImpl)aFactory.getApplication();
	boolean exceptionThrown = false;
	try {
	    parseConfig(cp, "config-with-failing-property-conversion.xml", config.getServletContext());
	}
	catch (RuntimeException re) {
	    exceptionThrown = true;
	}
	assertTrue(exceptionThrown);
    }


}
