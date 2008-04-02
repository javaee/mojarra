/*
 * $Id: ConfigFileTestCase.java,v 1.18 2003/07/07 20:53:18 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config;

import com.sun.faces.application.ApplicationImpl;
import com.sun.faces.el.ValueBindingImpl;
import com.sun.faces.RIConstants;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContext;
import javax.faces.component.UIComponent;
import javax.faces.convert.Converter;
import javax.faces.validator.Validator;
import javax.faces.FactoryFinder;
import javax.faces.application.ApplicationFactory;
import javax.faces.context.MessageResources;
import javax.faces.render.RenderKitFactory;

import org.apache.cactus.ServletTestCase;
import org.apache.cactus.WebRequest;
import com.sun.faces.ServletFacesTestCase;

/**
 * <p>Unit tests for Configuration File processing.</p>
 */

public class ConfigFileTestCase extends ServletFacesTestCase {


    // ----------------------------------------------------- Instance Variables


    protected ConfigBase base = null;


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
	ConfigBase yourBase = application.getAppConfig().getConfigBase();
	ConfigMessageResources messageResources = null;
	ConfigMessage message = null;
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
	    "javax.faces.validator.StringRangeValidator.TYPE"
	};
	Iterator messageIter = null;

	assertTrue(null != (messageResourcesMap = 
			    yourBase.getMessageResources()));
	assertTrue(null != (messageResources = (ConfigMessageResources)
			    messageResourcesMap.get(MessageResources.FACES_API_MESSAGES)));
	assertTrue(null != (messagesMap = messageResources.getMessages()));
	assertTrue(null != (messageIter = messagesMap.keySet().iterator()));
	assertTrue(isSubset(apiMessages, messageIter));
	assertTrue(null != (messageResources = (ConfigMessageResources)
			    messageResourcesMap.get(MessageResources.FACES_IMPL_MESSAGES)));
	
	
    }


    protected ConfigBase parseConfig(ConfigParser cp,
                                     String resource,
                                     ServletContext context)
        throws Exception {

        InputStream stream = context.getResourceAsStream(resource);
        assertTrue(null != stream);
        return (cp.parseConfig(stream));

    }



    // Test parsing a full configuration file
    public void testFull() throws Exception {
        ConfigParser cp = new ConfigParser(config.getServletContext());
        base = parseConfig(cp, "/WEB-INF/faces-config.xml",
                           config.getServletContext());

        // <application>
        assertEquals("com.sun.faces.TestActionListener",
                     base.getActionListener());
        assertEquals("com.sun.faces.TestNavigationHandler",
                     base.getNavigationHandler());
        assertEquals("com.sun.faces.TestPropertyResolver",
                     base.getPropertyResolver());
        assertEquals("com.sun.faces.TestVariableResolver",
                     base.getVariableResolver());

        // <component>

        ApplicationFactory aFactory = (ApplicationFactory)FactoryFinder.getFactory(
        FactoryFinder.APPLICATION_FACTORY);
        ApplicationImpl application = (ApplicationImpl)aFactory.getApplication();
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

        // <managed-beans>

        Map managedBeans = base.getManagedBeans();
        assertNotNull(managedBeans);
        ConfigManagedBean managedBean = 
            (ConfigManagedBean)managedBeans.get("NewCustomerFormHandler");
        assertNotNull(managedBean);
        assertEquals("NewCustomerFormHandler", managedBean.getManagedBeanId());
        assertEquals("com.sun.faces.config.NewCustomerFormHandler", managedBean.getManagedBeanClass());
        assertEquals("session", managedBean.getManagedBeanScope());
        System.out.println("MANAGEDBEAN:"+managedBean.toString());

        Map props = managedBean.getProperties();
        assertTrue(!(props == Collections.EMPTY_MAP));

        // Test Property Value , Values Array , Map Entries

        iter = props.keySet().iterator();
        ConfigManagedBeanProperty cmp = null;
        ConfigManagedBeanPropertyValue cmpv = null;
        ConfigManagedPropertyMap cmpm = null;
        while (iter.hasNext()) {
            cmp = (ConfigManagedBeanProperty)props.get((String)iter.next());
            if (cmp.hasValuesArray()) {
                List list = cmp.getValues();
                for (int i=0;i<list.size();i++) {
                    cmpv = (ConfigManagedBeanPropertyValue)list.get(i);
                    assertTrue(cmpv.getValueCategory()==ConfigManagedBeanPropertyValue.VALUE ||
                        cmpv.getValueCategory()==ConfigManagedBeanPropertyValue.VALUE_REF ||
                        cmpv.getValueCategory()==ConfigManagedBeanPropertyValue.VALUE_CLASS ||
                        cmpv.getValueCategory()==ConfigManagedBeanPropertyValue.NULL_VALUE);
                }
                
            } else if (cmp.hasMapEntries()) {
                List list = cmp.getMapEntries();
                for (int i=0;i<list.size();i++) {
                    cmpm = (ConfigManagedPropertyMap)list.get(i);
                    assertTrue(cmpm.getValueCategory()==ConfigManagedPropertyMap.VALUE ||
                        cmpm.getValueCategory()==ConfigManagedPropertyMap.VALUE_REF ||
                        cmpm.getValueCategory()==ConfigManagedPropertyMap.NULL_VALUE);
                }
            } else {
                cmpv = (ConfigManagedBeanPropertyValue)cmp.getValue();
                assertTrue(cmpv.getValueCategory()==ConfigManagedBeanPropertyValue.VALUE ||
                    cmpv.getValueCategory()==ConfigManagedBeanPropertyValue.VALUE_REF ||
                    cmpv.getValueCategory()==ConfigManagedBeanPropertyValue.VALUE_CLASS ||
                    cmpv.getValueCategory()==ConfigManagedBeanPropertyValue.NULL_VALUE);
            }
        }

        // Test Clone

        ConfigManagedBean managedBean1 = (ConfigManagedBean)managedBeans.get("NewCustomerFormHandler");
        ConfigManagedBean managedBean2 = (ConfigManagedBean)managedBeans.get("NewCustomerFormHandler");
        assertTrue(managedBean1 == managedBean2);

        ConfigManagedBean managedBean3 = (ConfigManagedBean)managedBean2.clone();
        assertTrue(managedBean2 != managedBean3);

        //PENDING include testing of contained objects (cloning)
        
    }

    public void testEmpty() throws Exception {
        ConfigParser cp = new ConfigParser(config.getServletContext());
        base = parseConfig(cp, "/WEB-INF/faces-config-empty.xml",
                           config.getServletContext());
	assertTrue(null != base);
    }

    // Assert that create and stored managed bean is the same as specified in the 
    // config file.
 
    public void testConfigManagedBeanFactory() throws Exception {

        ConfigParser cp = new ConfigParser(config.getServletContext());
        base = parseConfig(cp, "/WEB-INF/faces-config.xml",
                           config.getServletContext());

        ApplicationFactory aFactory = (ApplicationFactory)FactoryFinder.getFactory(
        FactoryFinder.APPLICATION_FACTORY);
        ApplicationImpl application = (ApplicationImpl)aFactory.getApplication();

        Object bean = application.getAppConfig().createAndMaybeStoreManagedBeans(getFacesContext(),
            "SimpleBean");

        Map managedBeans = base.getManagedBeans();
        ConfigManagedBean managedBean = (ConfigManagedBean)managedBeans.get("SimpleBean");
        String className = managedBean.getManagedBeanClass();

        Class clazz = null;
        Object obj = null;
        try {
            clazz = Class.forName(className);
            obj = clazz.newInstance();
        } catch (Throwable t) {
            assertTrue(false);
        }

        assertTrue (clazz.isInstance(obj));
    }

    public void testInitParams() {
	final String paramVal = "config1.xml,config2.xml,/WEB-INF/faces-config.xml";
	ConfigBase configBase = loadFromInitParam(paramVal);
	assertTrue(null != configBase);
	// make sure we have the managed beans from all three config
	// files: 1. the stock RI 2. config1.xml 3. config2.xml
	assertTrue(null != configBase.getManagedBeans().get("NewCustomerFormHandler"));
	assertTrue(null != configBase.getManagedBeans().get("TestBean1"));
	assertTrue(null != configBase.getManagedBeans().get("TestBean2"));
    }

     public void testNavigationCase() throws Exception {
         ConfigParser cp = new ConfigParser(config.getServletContext());
         base = parseConfig(cp, "/WEB-INF/faces-config.xml",
                            config.getServletContext());
         List navCases = base.getNavigationCases();
         assertTrue(!(navCases == Collections.EMPTY_LIST));
         for (int i=0; i< navCases.size(); i++) {
             ConfigNavigationCase cnc = (ConfigNavigationCase)navCases.get(i);
             System.out.println("NAVIGATION CASE:");
             System.out.println(cnc.toString());
         }
     }

    public void testRenderKit() {
	// test the default renderkit
        ApplicationFactory aFactory = (ApplicationFactory)FactoryFinder.getFactory(
										   FactoryFinder.APPLICATION_FACTORY);
        ApplicationImpl application = (ApplicationImpl)aFactory.getApplication();
	ConfigBase yourBase = application.getAppConfig().getConfigBase();
	ConfigRenderKit renderKit = null;
	ConfigRenderer renderer = null;
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

	assertTrue(null != (renderKitsMap = yourBase.getRenderKits()));
	assertTrue(null != (renderKit = (ConfigRenderKit)
			    renderKitsMap.get(RenderKitFactory.DEFAULT_RENDER_KIT)));
	assertTrue(null != (renderersMap = renderKit.getRenderers()));
	assertTrue(null != (rendererIter = renderersMap.keySet().iterator()));
	assertTrue(isSubset(defaultRenderers, rendererIter));
	
    }
}
