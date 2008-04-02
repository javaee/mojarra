/*
 * $Id: TestManagedBeanFactory.java,v 1.3 2003/05/04 21:39:36 horwat Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config;

import java.util.ArrayList;
import java.util.HashMap;

import javax.faces.context.FacesContext;

import com.sun.faces.TestBean;
import com.sun.faces.application.ApplicationImpl;
import com.sun.faces.config.ConfigManagedBean;
import com.sun.faces.config.ConfigManagedBeanProperty;
import com.sun.faces.config.ConfigManagedBeanPropertyValue;
import com.sun.faces.config.ConfigManagedPropertyMap;
import com.sun.faces.config.ManagedBeanFactory;
import com.sun.faces.el.ValueBindingImpl;

import com.sun.faces.ServletFacesTestCase;

/**
 * <p>Unit tests for Managed Bean Factory.</p>
 */

public class TestManagedBeanFactory extends ServletFacesTestCase {


    public static String beanName = "com.sun.faces.TestBean";

    // ----------------------------------------------------- Instance Variables

    ConfigManagedBean cmb;
    ConfigManagedBeanProperty cmbp;
    ConfigManagedBeanPropertyValue cmbpv;
    ConfigManagedPropertyMap cmpm;
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


    /**
     * Set up instance variables required by this test case.
     */
    public void setUp() {
    }

    /**
     * Over-ride method to avoid conflicts
     */
    public void tearDown() {
    }


    // ------------------------------------------------ Individual Test Methods



    // Test managed bean 
    public void testNoProperty() throws Exception {
        //Testing with no properties set
	cmb = new ConfigManagedBean();
	cmb.setManagedBeanClass(beanName);
	cmb.setManagedBeanScope("session");

        mbf = new ManagedBeanFactory(cmb);

        assertNotNull(mbf.newInstance());
    }

    public void testSimpleProperty() throws Exception {
        //Testing simple property
	cmb = new ConfigManagedBean();
	cmb.setManagedBeanClass(beanName);
	cmb.setManagedBeanScope("session");

        cmbp = new ConfigManagedBeanProperty();
        cmbp.setPropertyName("one");

        cmbpv = new ConfigManagedBeanPropertyValue();
        cmbpv.setValue("one");

        cmbp.setValue(cmbpv);
        cmb.addProperty(cmbp); 

        mbf = new ManagedBeanFactory(cmb);

        //testing with a property set
        assertNotNull(testBean = (TestBean) mbf.newInstance());

        //make sure bean instantiated properly. Get property back from bean.
        assertTrue(testBean.getOne().equals("one"));

        //make sure scope is stored properly
        assertTrue(mbf.getScope().equals("session"));
    }

    public void testIndexProperty() throws Exception {
        //Testing indexed properties
	cmb = new ConfigManagedBean();
	cmb.setManagedBeanClass(beanName);
	cmb.setManagedBeanScope("session");

        cmbp = new ConfigManagedBeanProperty();
        cmbp.setPropertyName("indexProperties");

        cmbpv = new ConfigManagedBeanPropertyValue();
        cmbpv.setValue("foo");
        cmbp.addValue(cmbpv);

        cmbpv = new ConfigManagedBeanPropertyValue();
        cmbpv.setValue("bar");
        cmbp.addValue(cmbpv);

        cmb.addProperty(cmbp); 

        mbf = new ManagedBeanFactory(cmb);

        //testing with a property set
        assertNotNull(testBean = (TestBean) mbf.newInstance());

        //make sure bean instantiated properly. Get property back from bean.
        ArrayList properties = (ArrayList) testBean.getIndexProperties();
        assertTrue(properties.get(0).equals("foo"));
        assertTrue(properties.get(1).equals("bar"));

        //make sure scope is stored properly
        assertTrue(mbf.getScope().equals("session"));
    }

    public void testMapProperty() throws Exception {
        //Testing mapped properties
	cmb = new ConfigManagedBean();
	cmb.setManagedBeanClass(beanName);
	cmb.setManagedBeanScope("session");

        cmpm = new ConfigManagedPropertyMap();
        cmpm.setKey("name");
        cmpm.setValue("Justyna");

        cmbp = new ConfigManagedBeanProperty();
        cmbp.setPropertyName("mapProperty");
        cmbp.addMapEntry(cmpm);

        cmb.addProperty(cmbp); 
        mbf = new ManagedBeanFactory(cmb);

        //testing with a property set
        assertNotNull(testBean = (TestBean) mbf.newInstance());

        //make sure bean instantiated properly. Get property back from bean.
        HashMap mapProperty = (HashMap) 
            testBean.getMapProperty();
        assertTrue(((String)mapProperty.get("name")).equals("Justyna"));

        //make sure scope is stored properly
        assertTrue(mbf.getScope().equals("session"));
    }

    public void testNoBeanCreate() throws Exception {
        cmb = new ConfigManagedBean();
        cmb.setManagedBeanClass(beanName);
        cmb.setManagedBeanCreate("false");

        mbf = new ManagedBeanFactory(cmb);

        //Testing no creation
        assertNull(mbf.newInstance());
    }

    public void testIndexTypeProperty() throws Exception {
	//Testing indexed type properties
        cmb = new ConfigManagedBean();
        cmb.setManagedBeanClass(beanName);
        cmb.setManagedBeanScope("session");

	cmbp = new ConfigManagedBeanProperty();
	cmbp.setPropertyName("indexIntegerProperties");

	cmbpv = new ConfigManagedBeanPropertyValue();
	cmbpv.setValueCategory(ConfigManagedBeanPropertyValue.VALUE_CLASS);
	cmbpv.setValue("java.lang.Integer");
	cmbp.addValue(cmbpv);

	cmbpv = new ConfigManagedBeanPropertyValue();
	cmbpv.setValue("23");
	cmbp.addValue(cmbpv);

	cmb.addProperty(cmbp);

	mbf = new ManagedBeanFactory(cmb);

	//testing with a property set
	assertNotNull(testBean = (TestBean) mbf.newInstance());

	//make sure bean instantiated properly. Get property back from bean.
	ArrayList properties = (ArrayList) testBean.getIndexIntegerProperties();
	assertTrue(properties.get(1) instanceof Integer);

        Integer integer = new Integer("23");
	assertTrue(properties.get(1).equals(integer));
    }

    public void testMapTypeProperty() throws Exception {
        //Testing mapped properties type
	cmb = new ConfigManagedBean();
	cmb.setManagedBeanClass(beanName);
	cmb.setManagedBeanScope("session");

        cmpm = new ConfigManagedPropertyMap();
        cmpm.setKey("name");
        cmpm.setValue("23");

        cmbp = new ConfigManagedBeanProperty();
        cmbp.setPropertyName("mapProperty");
        cmbp.setMapKeyClass("java.lang.String");
        cmbp.setMapValueClass("java.lang.Integer");
        cmbp.addMapEntry(cmpm);

        cmb.addProperty(cmbp); 
        mbf = new ManagedBeanFactory(cmb);

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

/**
 * PENDING: horwat: getFacesContext() is returning a NULL FacesContext making
 * this test non-workable. Must find a way to get a FacesContext.

    public void testValueRefProperty() throws Exception {
        //Testing simple value ref property

        TestBean testBean = new TestBean();
        getFacesContext().getExternalContext().getSessionMap().put("TestRefBean", testBean);

        ValueBindingImpl valueBinding = new 
            ValueBindingImpl(new ApplicationImpl());

        valueBinding.setRef("TestRefBean.one");
        valueBinding.setValue(getFacesContext(), "one");

	cmb = new ConfigManagedBean();
	cmb.setManagedBeanClass(beanName);
	cmb.setManagedBeanScope("session");

        cmbp = new ConfigManagedBeanProperty();
        cmbp.setPropertyName("one");

        cmbpv = new ConfigManagedBeanPropertyValue();
        cmbpv.setValueCategory(ConfigManagedBeanPropertyValue.VALUE_REF);
        cmbpv.setValue("TestRefBean.one");

        cmbp.setValue(cmbpv);
        cmb.addProperty(cmbp); 

        mbf = new ManagedBeanFactory(cmb);

        //testing with a property set
        assertNotNull(testBean = (TestBean) mbf.newInstance());

        //make sure bean instantiated properly. Get property back from bean.
        assertTrue(testBean.getOne().equals("one"));

        //make sure scope is stored properly
        assertTrue(mbf.getScope().equals("session"));

    }

** END testValueRefProperty */

}
