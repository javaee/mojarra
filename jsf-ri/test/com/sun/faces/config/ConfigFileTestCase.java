/*
 * $Id: ConfigFileTestCase.java,v 1.49 2003/12/17 23:26:06 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config;

import com.sun.faces.ServletFacesTestCase;
import com.sun.faces.application.ApplicationImpl;
import org.apache.cactus.WebRequest;
import com.sun.faces.util.Util;

import javax.faces.FactoryFinder;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.NavigationHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.convert.Converter;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.validator.Validator;
import javax.faces.el.ValueBinding;
import javax.servlet.ServletContext;

import java.io.InputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.xml.sax.InputSource;


/**
 * <p>Unit tests for Configuration File processing.</p>
 */

public class ConfigFileTestCase extends ServletFacesTestCase {


    // ----------------------------------------------------- Instance Variables
    List mappings;


    // ----------------------------------------------------------- Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public ConfigFileTestCase(String name) {

        super(name);
        mappings = new ArrayList();
        mappings.add("/faces/*");

    }
    
    public void beginLifecyclePhaseListener(WebRequest theRequest) {
        theRequest.setURL("localhost:8080", "/test", "/faces", null, null);     
    }


    // --------------------------------------------------- Overall Test Methods




    // ------------------------------------------------ Individual Test Methods
    
    // Verify the url-patterns for all javax.faces.webapp.FacesServlet
    // entries are found and massaged.
    public void testWebXmlParser() throws Exception {
        WebXmlParser parser = new WebXmlParser(config.getServletContext());
        List mappings = parser.getFacesServletMappings();

        assertTrue(mappings.contains("/faces"));
        assertTrue(mappings.contains(".jsf"));
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
        ConfigParser cp = new ConfigParser(config.getServletContext(), mappings);
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
            comp = application.createComponent(cType);
            assertNotNull(comp);
        }
        UIComponent command = application.createComponent("Command");
        assertNotNull(command);
        comp = null;
        application.addComponent("fooType", "javax.faces.component.UICommand");
        comp = application.createComponent("fooType");
        assertNotNull(comp);

        // <converter>

        iter = application.getConverterIds();
        assertTrue(iter.hasNext());
        String convId = null;
        Converter conv = null;
        while (iter.hasNext()) {
            convId = (String)iter.next();
            conv = application.createConverter(convId);
            assertNotNull(conv);
        }
        Converter first = application.createConverter("First");
        assertNotNull(first);
        conv = null;
        application.addConverter("fooId", "javax.faces.convert.DateTimeConverter");
        conv = application.createConverter("fooId");
        assertNotNull(conv);

        // <validator>

        iter = application.getValidatorIds();
        assertTrue(iter.hasNext());
        String valId = null;
        Validator val = null;
        while (iter.hasNext()) {
            valId = (String)iter.next();
            val = application.createValidator(valId);
            assertNotNull(val);
        }
        Validator second = application.createValidator("Second");
        assertNotNull(second);
        val = null;
        application.addValidator("fooId", "javax.faces.validator.DoubleRangeValidator");
        val = application.createValidator("fooId");
        assertNotNull(val);

    }

    public void testEmpty() throws Exception {
        ConfigParser cp = new ConfigParser(config.getServletContext(), mappings);
        parseConfig(cp, "/WEB-INF/faces-config-empty.xml",
                           config.getServletContext());
    }

    // Assert that create and stored managed bean is the same as specified in the 
    // config file.
 
    public void testConfigManagedBeanFactory() throws Exception {

        ConfigParser cp = new ConfigParser(config.getServletContext(), mappings);
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
                Util.doAssert(m[i].getName().equals("setSimpleProperty") ||
				 m[i].getName().equals("getSimpleProperty") ||
				 m[i].getName().equals("setIntProperty") ||
				 m[i].getName().equals("getIntProperty") || 
				 m[i].getName().equals("getTrueValue") ||
				 m[i].getName().equals("getFalseValue"));
		if (m[i].getName().equals("getSimpleProperty")) {
		    Object args[] = null;
	            Object value = m[i].invoke(bean, args);
		    Util.doAssert(((String)value).equals("Bobby Orr"));
		}
	    }
	} catch (Throwable t) {
	    assertTrue(false);
	}
    }

    public void testMapAndListPropertyPositive() throws Exception {
        ConfigParser cp = new ConfigParser(config.getServletContext(), mappings);
        ApplicationFactory aFactory = (ApplicationFactory)FactoryFinder.getFactory(
        FactoryFinder.APPLICATION_FACTORY);
        ApplicationImpl application = (ApplicationImpl)aFactory.getApplication();
        parseConfig(cp, "/WEB-INF/faces-config.xml",
                           config.getServletContext());

	ValueBinding valueBinding = 
	    application.createValueBinding("#{NewCustomerFormHandler.claimAmounts}");
	assertNotNull(valueBinding);

	Map claimAmounts = (Map) valueBinding.getValue(getFacesContext());
	assertNotNull(claimAmounts);

	assertNotNull(claimAmounts.get("fire"));
	assertTrue(claimAmounts.get("fire") instanceof Double);
	assertNull(claimAmounts.get("water"));
	assertNull(claimAmounts.get("earthquake"));

	valueBinding = application.createValueBinding("#{NewCustomerFormHandler.allowableValues}");
	assertNotNull(valueBinding);
	
	List list1 = (List) valueBinding.getValue(getFacesContext());
	assertNotNull(list1);

	assertEquals("allowableValues size not as expected", 4, list1.size());
	assertEquals("allowableValues.get(0) not as expected", 
		     new Integer(10), list1.get(0));
	assertEquals("allowableValues.get(1) not as expected", 
		     new Integer(20), list1.get(1));
	assertEquals("allowableValues.get(2) not as expected", 
		     new Integer(60), list1.get(2));
	assertNull("allowableValues.get(3) not as expected", list1.get(3));

	valueBinding = application.createValueBinding("#{NewCustomerFormHandler.firstNames}");
	assertNotNull(valueBinding);
	
	String [] strings = 
	    (String []) valueBinding.getValue(getFacesContext());
	assertNotNull(strings);

	assertEquals("firstNames size not as expected", 5, strings.length);
	assertEquals("firstNames[0] not as expected", "bob", strings[0]);
	assertEquals("firstNames[1] not as expected", "jerry", strings[1]);
	assertEquals("firstNames[2] not as expected", "Thomas", strings[2]);
	assertNull("firstNames[3] not as expected", strings[3]);
	assertNull("firstNames[4] not as expected", strings[4]);

    }

    public void testMapAndListPositive() throws Exception {
        ConfigParser cp = new ConfigParser(config.getServletContext(), mappings);
        ApplicationFactory aFactory = (ApplicationFactory)FactoryFinder.getFactory(
										   FactoryFinder.APPLICATION_FACTORY);
        ApplicationImpl application = (ApplicationImpl)aFactory.getApplication();
        parseConfig(cp, "/WEB-INF/config-lists-and-maps.xml",
		    config.getServletContext());
	
	ValueBinding valueBinding = 
	    application.createValueBinding("#{simpleList}");
	assertNotNull(valueBinding);
	
	List list = (List) valueBinding.getValue(getFacesContext());
	assertNotNull(list);

	assertEquals("simpleList size not as expected", 4, list.size());
	assertEquals("simpleList.get(0) not as expected", 
		     new Integer(10), list.get(0));
	assertEquals("simpleList.get(1) not as expected", 
		     new Integer(20), list.get(1));
	assertEquals("simpleList.get(2) not as expected", 
		     new Integer(60), list.get(2));
	assertNull("simpleList.get(3) not as expected", list.get(3));

	valueBinding = application.createValueBinding("#{objectList}");
	assertNotNull(valueBinding);
	
	list = (List) valueBinding.getValue(getFacesContext());
	assertNotNull(list);

	assertEquals("simpleList size not as expected", 4, list.size());
	assertTrue("simpleList.get(0) not as expected", 
		   list.get(0) instanceof SimpleBean);
	assertTrue("simpleList.get(1) not as expected", 
		   list.get(1) instanceof SimpleBean);
	assertTrue("simpleList.get(2) not as expected", 
		   list.get(2) instanceof SimpleBean);
	assertNull("simpleList.get(3) not as expected", list.get(3));
	

	valueBinding = application.createValueBinding("#{floatMap}");
	assertNotNull(valueBinding);
	
	Map 
	    nestedMap = null,
	    map = (Map) valueBinding.getValue(getFacesContext());
	assertNotNull(map);

	Iterator keys = map.keySet().iterator();
	Float 
	    key1 = new Float(3.1415),
	    key2 = new Float(3.14),
	    key3 = new Float(6.02),
	    key4 = new Float(0.00001);
	Object 	    
	    curKey = null,
	    value = null;

	while (keys.hasNext()) {
	    assertTrue((curKey = keys.next()) instanceof Float);
	    if (null != (value = map.get(curKey))) {
		assertTrue(value instanceof SimpleBean);
	    }
	}

	assertTrue("map.get(key1) not a SimpleBean",
		   map.get(key1) instanceof SimpleBean);
	assertTrue("map.get(key2) not a SimpleBean",
		   map.get(key2) instanceof SimpleBean);
	assertTrue("map.get(key3) not a SimpleBean",
		   map.get(key3) instanceof SimpleBean);
	assertNull("map.get(key4) not null",map.get(key4));

	valueBinding = application.createValueBinding("#{crazyMap}");
	assertNotNull(valueBinding);
	
	map = (Map) valueBinding.getValue(getFacesContext());
	assertNotNull(map);

	keys = map.keySet().iterator();
	while (keys.hasNext()) {
	    assertTrue((curKey = keys.next()) instanceof String);
	    if (null != (value = map.get(curKey))) {
		assertTrue(value instanceof Map);
		nestedMap = (Map) value;
		assertTrue("nestedMap.get(key1) not a SimpleBean",
			   nestedMap.get(key1) instanceof SimpleBean);
		assertTrue("nestedMap.get(key2) not a SimpleBean",
			   nestedMap.get(key2) instanceof SimpleBean);
		assertTrue("nestedMap.get(key3) not a SimpleBean",
			   nestedMap.get(key3) instanceof SimpleBean);
		assertNull("nestedMap.get(key4) not null",
			   nestedMap.get(key4));
	    }
	}
	assertTrue("map.get(one) not a Map",
		   map.get("one") instanceof Map);
	assertTrue("map.get(two) not a Map",
		   map.get("two") instanceof Map);
	assertNull("map.get(three) not null", map.get("three"));



    }

    public void testNavigationCase() throws Exception {
        ConfigParser cp = new ConfigParser(config.getServletContext(), mappings);
        parseConfig(cp, "/WEB-INF/faces-config.xml",
                            config.getServletContext());
        ApplicationFactory aFactory = 
            (ApplicationFactory)FactoryFinder.getFactory(
                 FactoryFinder.APPLICATION_FACTORY);
        ApplicationImpl application = (ApplicationImpl)aFactory.getApplication(); 
	NavigationHandler navHandler = application.getNavigationHandler();
        UIViewRoot page = new UIViewRoot();
        page.setViewId("/login.jsp");
	getFacesContext().setViewRoot(page);
        navHandler.handleNavigation(getFacesContext(), "#{UserBean.login}",
	    "success");
        String newViewId = getFacesContext().getViewRoot().getViewId();
        assertTrue(newViewId.equals("/home.jsp"));	
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
	    "Form",
	    "Grid",
	    "Group",
	    "Hidden",
	    "Link",
	    "Image",
	    "Label",
	    "Listbox",
	    "Menu",
	    "Message",
	    "Radio",
	    "Secret",
	    "CheckboxList",
	    "Textarea",
	    "Text"
	};
	Iterator rendererIter = null;

        Iterator iter = renderKitFactory.getRenderKitIds();
	assertTrue(iter.hasNext());
	RenderKit renderKit = renderKitFactory.getRenderKit(getFacesContext(),
							    "DEFAULT");
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
        ConfigParser cp = new ConfigParser(config.getServletContext(), mappings);
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
        ConfigParser cp = new ConfigParser(config.getServletContext(), mappings);
        ApplicationFactory aFactory = (ApplicationFactory)FactoryFinder.getFactory(
        FactoryFinder.APPLICATION_FACTORY);
        ApplicationImpl application = (ApplicationImpl)aFactory.getApplication();
	boolean exceptionThrown = false;
	try {
	    parseConfig(cp, "config-with-failing-property-conversion.xml", config.getServletContext());
	}
	catch (RuntimeException re) {
	    exceptionThrown = true;
	    assertTrue(-1 != re.getMessage().indexOf("convert"));
	}
	assertTrue(exceptionThrown);
	
    }

    public void testLifecyclePhaseListener() throws Exception {
        final String HANDLED_BEFORE_AFTER = "Handled Before After";
        ConfigParser cp = new ConfigParser(config.getServletContext(), mappings);
        LifecycleFactory lFactory = (LifecycleFactory)FactoryFinder.getFactory(
            FactoryFinder.LIFECYCLE_FACTORY);
        Lifecycle lifecycle = lFactory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
        parseConfig(cp, "config1.xml", config.getServletContext());

        UIViewRoot page = new UIViewRoot();
        page.setViewId("/login.jsp");
	    getFacesContext().setViewRoot(page);
        try {
            System.setProperty(HANDLED_BEFORE_AFTER, "");
            lifecycle.execute(getFacesContext());
        } catch (Exception e) {
            assertTrue(e.getMessage(), false);
        }

        String handledBeforeAfter = System.getProperty(HANDLED_BEFORE_AFTER);
        assertTrue(handledBeforeAfter != null);
        assertTrue(handledBeforeAfter.equals(HANDLED_BEFORE_AFTER));
    }

    public void testFileNotFoundWhenParsing() throws Exception {
        ConfigParser cp = new ConfigParser(config.getServletContext(), mappings);
	// make sure we get a FileNotFoundException on null input for
	// all variants of parseConfig.
	boolean exceptionThrown = false;
	try {
	    cp.parseConfig((InputStream) null);
	}
	catch (FileNotFoundException e) {
	    exceptionThrown = true;
	}
	assertTrue(exceptionThrown);

	exceptionThrown = false;
	try {
	    cp.parseConfig(null, config.getServletContext());
	}
	catch (FileNotFoundException e) {
	    exceptionThrown = true;
	}
	assertTrue(exceptionThrown);

	exceptionThrown = false;
	try {
	    cp.parseConfig((InputSource) null);
	}
	catch (FileNotFoundException e) {
	    exceptionThrown = true;
	}
	assertTrue(exceptionThrown);	
	
    }
}
