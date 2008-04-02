/*
 * $Id: ConfigFileTestCase.java,v 1.3 2003/04/30 19:05:20 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config;

import com.sun.faces.application.ApplicationImpl;
import com.sun.faces.el.ValueBindingImpl;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.FactoryFinder;
import javax.faces.application.ApplicationFactory;

import org.apache.cactus.ServletTestCase;
import com.sun.faces.ServletFacesTestCase;

/**
 * <p>Unit tests for Configuration File processing.</p>
 */

public class ConfigFileTestCase extends ServletFacesTestCase {


    // The public identifier of our DTD
    protected String CONFIG_DTD_PUBLIC_ID =
        "-//Sun Microsystems, Inc.//DTD JavaServer Faces Config 1.0//EN";


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


    public void tearDown() {
    }


    // ------------------------------------------------ Individual Test Methods



    // Test parsing a full configuration file
    public void testFull() throws Exception {
        ConfigParser cp = new ConfigParser(config.getServletContext());
        base = cp.getConfigBase();

        // <application>
        assertEquals("com.mycompany.MyActionListener",
                     base.getActionListener());
        assertEquals("com.mycompany.MyNavigationHandler",
                     base.getNavigationHandler());
        assertEquals("com.mycompany.MyPropertyResolver",
                     base.getPropertyResolver());
        assertEquals("com.mycompany.MyVariableResolver",
                     base.getVariableResolver());

        // <component>

        Map components = base.getComponents();
        assertNotNull(components);
        ConfigComponent ccomp1 = (ConfigComponent) components.get("Command");
        assertNotNull(ccomp1);
        assertEquals("Command",
                     ccomp1.getComponentType());
        assertEquals("javax.faces.component.UICommand",
                     ccomp1.getComponentClass());
        assertNull(ccomp1.getLargeIcon());
        assertNull(ccomp1.getSmallIcon());
        assertEquals(0, ccomp1.getAttributes().size());
        assertEquals(0, ccomp1.getProperties().size());

        // <converter>

        Map converters = base.getConverters();
        assertNotNull(converters);
        ConfigConverter cc1 = (ConfigConverter) converters.get("First");
        assertNotNull(cc1);
        assertEquals("First",
                     cc1.getConverterId());
        assertEquals("com.mycompany.MyFirstConverter",
                     cc1.getConverterClass());
        assertEquals(1, cc1.getAttributes().size());
        ConfigAttribute cc1a1 =
            (ConfigAttribute) cc1.getAttributes().get("attr1");
        assertNotNull(cc1a1);
        assertEquals("attr1",
                     cc1a1.getAttributeName());
        assertEquals("java.lang.String",
                     cc1a1.getAttributeClass());
        assertEquals(0, cc1.getProperties().size());
        ConfigConverter cc2 = (ConfigConverter) converters.get("Second");
        assertNotNull(cc2);
        assertEquals("Second",
                     cc2.getConverterId());
        assertEquals("com.mycompany.MySecondConverter",
                     cc2.getConverterClass());
        assertEquals(0, cc2.getAttributes().size());
        assertEquals(1, cc2.getProperties().size());
        ConfigProperty cc2p1 =
            (ConfigProperty) cc2.getProperties().get("prop1");
        assertNotNull(cc2p1);
        assertEquals("prop1",
                     cc2p1.getPropertyName());
        assertEquals("java.lang.String",
                     cc2p1.getPropertyClass());

        // <validator>

        Map validators = base.getValidators();
        assertNotNull(validators);
        ConfigValidator cv1 = (ConfigValidator) validators.get("First");
        assertNotNull(cv1);
        assertEquals("First",
                     cv1.getValidatorId());
        assertEquals("com.mycompany.MyFirstValidator",
                     cv1.getValidatorClass());
        assertEquals(1, cv1.getAttributes().size());
        ConfigAttribute cv1a1 =
            (ConfigAttribute) cv1.getAttributes().get("attr1");
        assertNotNull(cv1a1);
        assertEquals("attr1",
                     cv1a1.getAttributeName());
        assertEquals("java.lang.String",
                     cv1a1.getAttributeClass());
        assertEquals(0, cv1.getProperties().size());
        ConfigValidator cv2 = (ConfigValidator) validators.get("Second");
        assertNotNull(cv2);
        assertEquals("Second",
                     cv2.getValidatorId());
        assertEquals("com.mycompany.MySecondValidator",
                     cv2.getValidatorClass());
        assertEquals(0, cv2.getAttributes().size());
        assertEquals(1, cv2.getProperties().size());
        ConfigProperty cv2p1 =
            (ConfigProperty) cv2.getProperties().get("prop1");
        assertNotNull(cv2p1);
        assertEquals("prop1",
                     cv2p1.getPropertyName());
        assertEquals("java.lang.String",
                     cv2p1.getPropertyClass());

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

        Iterator iter = props.keySet().iterator();
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
                    assertTrue(cmpv.getValueCategory()==ConfigManagedBeanPropertyValue.VALUE ||
                        cmpv.getValueCategory()==ConfigManagedBeanPropertyValue.VALUE_REF ||
                        cmpv.getValueCategory()==ConfigManagedBeanPropertyValue.NULL_VALUE);
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
        ConfigParser cp = new ConfigParser("/WEB-INF/faces-config-empty.xml",
            config.getServletContext());
        base = cp.getConfigBase();
    }

    // Assert that create and stored managed bean is the same as specified in the 
    // config file.
 
    public void testConfigManagedBeanFactory() {

        ConfigParser cp = new ConfigParser(config.getServletContext());
        base = cp.getConfigBase();

        ApplicationFactory aFactory = (ApplicationFactory)FactoryFinder.getFactory(
        FactoryFinder.APPLICATION_FACTORY);
        ApplicationImpl application = (ApplicationImpl)aFactory.getApplication();

        Object bean = application.createAndMaybeStoreManagedBeans(getFacesContext(),
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
}
