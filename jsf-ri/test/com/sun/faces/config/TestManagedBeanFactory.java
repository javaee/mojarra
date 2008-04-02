/*
 * $Id: TestManagedBeanFactory.java,v 1.16 2004/02/26 20:34:16 eburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config;

import com.sun.faces.ServletFacesTestCase;
import com.sun.faces.TestBean;
import com.sun.faces.application.ApplicationImpl;
import com.sun.faces.config.beans.ListEntriesBean;
import com.sun.faces.config.beans.ManagedBeanBean;
import com.sun.faces.config.beans.ManagedPropertyBean;
import com.sun.faces.config.beans.MapEntriesBean;
import com.sun.faces.config.beans.MapEntryBean;
import com.sun.faces.el.ValueBindingImpl;

import javax.faces.FacesException;
import javax.faces.el.ValueBinding;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * <p>Unit tests for Managed Bean Factory.</p>
 */

public class TestManagedBeanFactory extends ServletFacesTestCase {


    public static String beanName = "com.sun.faces.TestBean";

    // ----------------------------------------------------- Instance Variables

    ManagedBeanBean bean;
    ManagedPropertyBean property;
    ListEntriesBean listEntries;
    MapEntriesBean mapEntries;
    MapEntryBean mapEntry;
    ManagedBeanFactory mbf;
    TestBean testBean;

    // ----------------------------------------------------------- Constructors


    /**
     * Construct a new instance of this test case.
     */
    public TestManagedBeanFactory() {
        super("TestManagedBeanFactory");
    }


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public TestManagedBeanFactory(String name) {

        super(name);

    }


    // --------------------------------------------------- Overall Test Methods


    // ------------------------------------------------ Individual Test Methods



    // Test managed bean 
    public void testNoProperty() throws Exception {
        //Testing with no properties set
        bean = new ManagedBeanBean();
        bean.setManagedBeanClass(beanName);
        bean.setManagedBeanScope("session");

        mbf = new ManagedBeanFactory(bean);

        assertNotNull(mbf.newInstance());
    }


    public void testSimpleProperty() throws Exception {
        //Testing simple property
        bean = new ManagedBeanBean();
        bean.setManagedBeanClass(beanName);
        bean.setManagedBeanScope("session");

        property = new ManagedPropertyBean();
        property.setPropertyName("one");
        property.setValue("one");

        bean.addManagedProperty(property);

        mbf = new ManagedBeanFactory(bean);

        //testing with a property set
        assertNotNull(testBean = (TestBean) mbf.newInstance());

        //make sure bean instantiated properly. Get property back from bean.
        assertTrue(testBean.getOne().equals("one"));

        //make sure scope is stored properly
        assertTrue(mbf.getScope().equals("session"));
    }


    public void testPrimitiveProperty() throws Exception {
        //Testing primitive properties
        bean = new ManagedBeanBean();
        bean.setManagedBeanClass(beanName);
        bean.setManagedBeanScope("session");

        boolean testBoolean = true;
        property = new ManagedPropertyBean();
        property.setPropertyName("boolProp");
        property.setValue((new Boolean(testBoolean)).toString());
        bean.addManagedProperty(property);

        byte testByte = 100;
        property = new ManagedPropertyBean();
        property.setPropertyName("byteProp");
        property.setValue(Byte.toString(testByte));
        bean.addManagedProperty(property);

        char testChar = 'z';
        property = new ManagedPropertyBean();
        property.setPropertyName("charProp");
        property.setValue((new Character(testChar)).toString());
        bean.addManagedProperty(property);

        double testDouble = 11.278D;
        property = new ManagedPropertyBean();
        property.setPropertyName("doubleProp");
        property.setValue(Double.toString(testDouble));
        bean.addManagedProperty(property);

        float testFloat = 45.789F;
        property = new ManagedPropertyBean();
        property.setPropertyName("floatProp");
        property.setValue(Float.toString(testFloat));
        bean.addManagedProperty(property);

        int testInt = 42;
        property = new ManagedPropertyBean();
        property.setPropertyName("intProp");
        property.setValue(Integer.toString(testInt));
        bean.addManagedProperty(property);

        long testLong = 3147893289L;
        property = new ManagedPropertyBean();
        property.setPropertyName("longProp");
        property.setValue(Long.toString(testLong));
        bean.addManagedProperty(property);

        short testShort = 25432;
        property = new ManagedPropertyBean();
        property.setPropertyName("shortProp");
        property.setValue(Short.toString(testShort));
        bean.addManagedProperty(property);

        mbf = new ManagedBeanFactory(bean);

        //testing with a property set
        assertNotNull(testBean = (TestBean) mbf.newInstance());

        //make sure bean instantiated properly. Get property back from bean.
        assertTrue(testBean.getBoolProp() == testBoolean);
        assertTrue(testBean.getByteProp() == testByte);
        assertTrue(testBean.getCharProp() == testChar);
        assertTrue(testBean.getDoubleProp() == testDouble);
        assertTrue(testBean.getFloatProp() == testFloat);
        assertTrue(testBean.getIntProp() == testInt);
        assertTrue(testBean.getLongProp() == testLong);
        assertTrue(testBean.getShortProp() == testShort);

        //make sure scope is stored properly
        assertTrue(mbf.getScope().equals("session"));
    }


    public void testIndexProperty() throws Exception {
        //Testing indexed properties
        bean = new ManagedBeanBean();
        bean.setManagedBeanClass(beanName);
        bean.setManagedBeanScope("session");

        property = new ManagedPropertyBean();
        property.setPropertyName("indexProperties");
        listEntries = new ListEntriesBean();
        listEntries.addValue("foo");
        listEntries.addValue("bar");
        property.setListEntries(listEntries);

        bean.addManagedProperty(property);

        mbf = new ManagedBeanFactory(bean);

        //testing with a property set
        assertNotNull(testBean = (TestBean) mbf.newInstance());

        //make sure bean instantiated properly. Get property back from bean.
        ArrayList properties = (ArrayList) testBean.getIndexProperties();
        assertTrue(properties.get(5).equals("foo"));
        assertTrue(properties.get(6).equals("bar"));

        //make sure scope is stored properly
        assertTrue(mbf.getScope().equals("session"));
    }


    public void testMapProperty() throws Exception {
        //Testing mapped properties
        bean = new ManagedBeanBean();
        bean.setManagedBeanClass(beanName);
        bean.setManagedBeanScope("session");

        mapEntries = new MapEntriesBean();
        mapEntry = new MapEntryBean();
        mapEntry.setKey("name");
        mapEntry.setValue("Justyna");
        mapEntries.addMapEntry(mapEntry);

        property = new ManagedPropertyBean();
        property.setPropertyName("mapProperty");
        property.setMapEntries(mapEntries);

        bean.addManagedProperty(property);
        mbf = new ManagedBeanFactory(bean);

        //testing with a property set
        assertNotNull(testBean = (TestBean) mbf.newInstance());

        //make sure bean instantiated properly. Get property back from bean.
        HashMap mapProperty = (HashMap)
            testBean.getMapProperty();
        assertTrue(((String) mapProperty.get("name")).equals("Justyna"));

        //make sure scope is stored properly
        assertTrue(mbf.getScope().equals("session"));
    }


    public void testIndexTypeProperty() throws Exception {
        //Testing indexed type properties
        bean = new ManagedBeanBean();
        property = new ManagedPropertyBean();
        bean.setManagedBeanClass(beanName);
        bean.setManagedBeanScope("session");
        property.setPropertyName("indexIntegerProperties");
        listEntries = new ListEntriesBean();
        listEntries.setValueClass("java.lang.Integer");
        listEntries.addValue("23");
        property.setListEntries(listEntries);

        bean.addManagedProperty(property);

        mbf = new ManagedBeanFactory(bean);

        //testing with a property set
        assertNotNull(testBean = (TestBean) mbf.newInstance());

        //make sure bean instantiated properly. Get property back from bean.
        ArrayList properties = (ArrayList) testBean.getIndexIntegerProperties();
        assertTrue(properties.get(1) instanceof Integer);

        Integer integer = new Integer("23");
        assertTrue(properties.get(2).equals(integer));
    }


    public void testMapTypeProperty() throws Exception {
        //Testing mapped properties type
        bean = new ManagedBeanBean();
        bean.setManagedBeanClass(beanName);
        bean.setManagedBeanScope("session");

        mapEntries = new MapEntriesBean();
        mapEntries.setKeyClass("java.lang.String");
        mapEntries.setValueClass("java.lang.Integer");

        mapEntry = new MapEntryBean();
        mapEntry.setKey("name");
        mapEntry.setValue("23");
        mapEntries.addMapEntry(mapEntry);

        property = new ManagedPropertyBean();
        property.setPropertyName("mapProperty");
        property.setMapEntries(mapEntries);

        bean.addManagedProperty(property);
        mbf = new ManagedBeanFactory(bean);

        //testing with a property set
        assertNotNull(testBean = (TestBean) mbf.newInstance());

        //make sure bean instantiated properly. Get property back from bean.
        HashMap mapProperty = (HashMap)
            testBean.getMapProperty();

        assertTrue(mapProperty.get("name") instanceof Integer);

        Integer integer = new Integer("23");
        assertTrue(mapProperty.get("name").equals(integer));

        //make sure scope is stored properly
        assertTrue(mbf.getScope().equals("session"));
    }


    public void testValueRefProperty() throws Exception {
        //Testing simple value ref property

        TestBean testBean = new TestBean();
        getFacesContext().getExternalContext().getSessionMap().put(
            "TestRefBean", testBean);

        ValueBindingImpl valueBinding = new
            ValueBindingImpl(new ApplicationImpl());

        valueBinding.setRef("TestRefBean.one");
        valueBinding.setValue(getFacesContext(), "one");

        bean = new ManagedBeanBean();
        bean.setManagedBeanClass(beanName);
        bean.setManagedBeanScope("session");

        property = new ManagedPropertyBean();
        property.setPropertyName("one");
        property.setValue("#{TestRefBean.one}");
        bean.addManagedProperty(property);

        mbf = new ManagedBeanFactory(bean);

        //testing with a property set
        assertNotNull(testBean = (TestBean) mbf.newInstance());

        //make sure bean instantiated properly. Get property back from bean.
        assertTrue(testBean.getOne().equals("one"));

        //make sure scope is stored properly
        assertTrue(mbf.getScope().equals("session"));

    }


    public void testValueRefScope() throws Exception {
        //Testing value ref scope

        TestBean testBean = new TestBean();
        boolean exceptionThrown = false;

        //testing with:
        //  valueref in application scope
        //  managed bean in session scope
        getFacesContext().getExternalContext().getApplicationMap().put(
            "TestRefBean", testBean);

        ValueBindingImpl valueBinding =
            new ValueBindingImpl(new ApplicationImpl());

        valueBinding.setRef("TestRefBean.one");
        valueBinding.setValue(getFacesContext(), "one");

        bean = new ManagedBeanBean();
        bean.setManagedBeanClass(beanName);
        bean.setManagedBeanScope("session");

        property = new ManagedPropertyBean();
        property.setPropertyName("one");
        property.setValue("#{TestRefBean.one}");
        bean.addManagedProperty(property);

        mbf = new ManagedBeanFactory(bean);

        //testing with an application scope property set in a session scope bean
        assertNotNull(testBean = (TestBean) mbf.newInstance());

        //make sure bean instantiated properly. Get property back from bean.
        assertTrue(testBean.getOne().equals("one"));

        //make sure scope is stored properly
        assertTrue(mbf.getScope().equals("session"));


        //testing with:
        //  valueref in request scope
        //  managed bean in application scope
        getFacesContext().getExternalContext().getRequestMap().put(
            "TestRefBean", testBean);

        valueBinding = new ValueBindingImpl(new ApplicationImpl());

        valueBinding.setRef("TestRefBean.one");
        valueBinding.setValue(getFacesContext(), "one");

        bean = new ManagedBeanBean();
        bean.setManagedBeanClass(beanName);
        bean.setManagedBeanScope("application");

        property = new ManagedPropertyBean();
        property.setPropertyName("one");
        property.setValue("#{TestRefBean.one}");

        bean.addManagedProperty(property);

        mbf = new ManagedBeanFactory(bean);

        exceptionThrown = false;
        try {
            mbf.newInstance();
            fail("Should have thrown FacesException");
        } catch (FacesException ex) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        //make sure scope is stored properly
        assertTrue(mbf.getScope().equals("application"));

        //cleanup
        getFacesContext().getExternalContext().getRequestMap().remove(
            "TestRefBean");

        //testing with:
        //  valueref in session scope
        //  managed bean in no scope
        getFacesContext().getExternalContext().getSessionMap().put(
            "TestRefBean", testBean);

        valueBinding = new ValueBindingImpl(new ApplicationImpl());

        valueBinding.setRef("sessionScope.TestRefBean.one");
        valueBinding.setValue(getFacesContext(), "one");

        bean = new ManagedBeanBean();
        bean.setManagedBeanClass(beanName);
        bean.setManagedBeanScope(null);

        property = new ManagedPropertyBean();
        property.setPropertyName("one");
        property.setValue("#{sessionScope.TestRefBean.one}");
        bean.addManagedProperty(property);

        mbf = new ManagedBeanFactory(bean);

        exceptionThrown = false;
        try {
            mbf.newInstance();
            fail("Should have thrown FacesException");
        } catch (FacesException ex) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        //make sure scope is stored properly
        assertTrue(mbf.getScope() == null);
    }


    public void testMixedBean() throws Exception {
        ValueBinding vb =
            getFacesContext().getApplication().createValueBinding(
                "#{mixedBean}");
        TestBean bean = (TestBean) vb.getValue(getFacesContext());
        assertEquals("mixed value Bobby Orr", bean.getProp());

        vb =
            getFacesContext().getApplication().createValueBinding(
                "#{mixedBean.prop}");
        assertEquals(bean.getProp(), (String) vb.getValue(getFacesContext()));
    }
	
    /************* PENDING(edburns): rewrite to exercise new edge case
     * detection.

     public void testInvalidPropertyConfiguration() throws Exception {
     // If a ConfigManagedPropertyValue has a value that requires
     // converstion from String (the default type) to another type,
     // say Integer as an example, and the CMPV's value category
     // isn't set to Value, conversion will not take place.  Thus, an
     // error should occur when creating a new instanced of the
     // managed bean.

     // no value category set
     bean = new ManagedBeanBean();
     bean.setManagedBeanClass(beanName);
     bean.setManagedBeanScope("session");

     boolean testBoolean = true;
     property = new ManagedPropertyBean();
     property.setPropertyName("boolProp");
     propertyv = new ConfigManagedBeanPropertyValue();
     propertyv.setValue((new Boolean(testBoolean)).toString());
     property.setValue(propertyv);
     bean.addManagedProperty(property);

     mbf = new ManagedBeanFactory(bean);

     boolean exceptionThrown = false;
     try {
     mbf.newInstance();
     } catch (FacesException fe) {
     exceptionThrown = true;
     }
     assertTrue(exceptionThrown);


     // value category set to VALUE_BINDING
     bean = new ManagedBeanBean();
     bean.setManagedBeanClass(beanName);
     bean.setManagedBeanScope("session");

     property = new ManagedPropertyBean();
     property.setPropertyName("boolProp");
     propertyv = new ConfigManagedBeanPropertyValue();
     propertyv.setValueCategory(ConfigManagedBeanPropertyValue.VALUE_BINDING);
     propertyv.setValue((new Boolean(testBoolean)).toString());
     property.setValue(propertyv);
     bean.addManagedProperty(property);

     mbf = new ManagedBeanFactory(bean);

     exceptionThrown = false;
     try {
     mbf.newInstance();
     } catch (FacesException fe) {
     exceptionThrown = true;
     }
     assertTrue(exceptionThrown);

     }

     public void testExceptions() throws Exception {

     bean = new ManagedBeanBean();
     bean.setManagedBeanClass("foo");
     bean.setManagedBeanScope("session");

     property = new ManagedPropertyBean();
     property.setPropertyName("one");

     propertyv = new ConfigManagedBeanPropertyValue();
     propertyv.setValue("one");

     property.setValue(propertyv);
     boolean exceptionThrown = false;
     try {
     bean.addManagedProperty(property);
     } catch (FacesException fe) {
     exceptionThrown = true;
     }
     assertTrue(exceptionThrown);
     exceptionThrown = false;
     try {
     property = null;
     bean.addManagedProperty(property);
     } catch (NullPointerException npe) {
     exceptionThrown = true;
     }
     assertTrue(exceptionThrown);
     }
     ***********/

}
