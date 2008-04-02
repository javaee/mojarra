/*
 * $Id: TestManagedBeanFactory.java,v 1.11 2003/12/17 15:15:10 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config;

import java.util.ArrayList;
import java.util.HashMap;

import javax.faces.context.FacesContext;
import javax.faces.FacesException;

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

    public void testPrimitiveProperty() throws Exception {
        //Testing primitive properties
        cmb = new ConfigManagedBean();
        cmb.setManagedBeanClass(beanName);
        cmb.setManagedBeanScope("session");

        boolean testBoolean = true;
        cmbp = new ConfigManagedBeanProperty();
        cmbp.setPropertyName("boolProp");
        cmbpv = new ConfigManagedBeanPropertyValue();
        cmbpv.setValueCategory(ConfigManagedBeanPropertyValue.VALUE);
        cmbpv.setValue((new Boolean(testBoolean)).toString());        
        cmbp.setValue(cmbpv);
        cmb.addProperty(cmbp); 

        byte testByte = 100;
        cmbp = new ConfigManagedBeanProperty();
        cmbp.setPropertyName("byteProp");
        cmbpv = new ConfigManagedBeanPropertyValue();
        cmbpv.setValueCategory(ConfigManagedBeanPropertyValue.VALUE);
        cmbpv.setValue(Byte.toString(testByte));       
        cmbp.setValue(cmbpv);
        cmb.addProperty(cmbp); 

        char testChar = 'z';
        cmbp = new ConfigManagedBeanProperty();
        cmbp.setPropertyName("charProp");
        cmbpv = new ConfigManagedBeanPropertyValue();
        cmbpv.setValueCategory(ConfigManagedBeanPropertyValue.VALUE);
        cmbpv.setValue((new Character(testChar)).toString());      
        cmbp.setValue(cmbpv);
        cmb.addProperty(cmbp); 

        double testDouble = 11.278D;
        cmbp = new ConfigManagedBeanProperty();
        cmbp.setPropertyName("doubleProp");
        cmbpv = new ConfigManagedBeanPropertyValue();
        cmbpv.setValueCategory(ConfigManagedBeanPropertyValue.VALUE);
        cmbpv.setValue(Double.toString(testDouble));        
        cmbp.setValue(cmbpv);
        cmb.addProperty(cmbp); 

        float testFloat = 45.789F;
        cmbp = new ConfigManagedBeanProperty();
        cmbp.setPropertyName("floatProp");
        cmbpv = new ConfigManagedBeanPropertyValue();
        cmbpv.setValueCategory(ConfigManagedBeanPropertyValue.VALUE);
        cmbpv.setValue(Float.toString(testFloat));        
        cmbp.setValue(cmbpv);
        cmb.addProperty(cmbp); 

        int testInt = 42;
        cmbp = new ConfigManagedBeanProperty();
        cmbp.setPropertyName("intProp");
        cmbpv = new ConfigManagedBeanPropertyValue();
        cmbpv.setValueCategory(ConfigManagedBeanPropertyValue.VALUE);
        cmbpv.setValue(Integer.toString(testInt));        
        cmbp.setValue(cmbpv);
        cmb.addProperty(cmbp); 

        long testLong = 3147893289L;
        cmbp = new ConfigManagedBeanProperty();
        cmbp.setPropertyName("longProp");
        cmbpv = new ConfigManagedBeanPropertyValue();
        cmbpv.setValueCategory(ConfigManagedBeanPropertyValue.VALUE);
        cmbpv.setValue(Long.toString(testLong));        
        cmbp.setValue(cmbpv);
        cmb.addProperty(cmbp); 

        short testShort = 25432;
        cmbp = new ConfigManagedBeanProperty();
        cmbp.setPropertyName("shortProp");
        cmbpv = new ConfigManagedBeanPropertyValue();
        cmbpv.setValueCategory(ConfigManagedBeanPropertyValue.VALUE);
        cmbpv.setValue(Short.toString(testShort));        
        cmbp.setValue(cmbpv);
        cmb.addProperty(cmbp); 

        mbf = new ManagedBeanFactory(cmb);

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
	cmb = new ConfigManagedBean();
	cmb.setManagedBeanClass(beanName);
	cmb.setManagedBeanScope("session");

        cmbp = new ConfigManagedBeanProperty();
        cmbp.setPropertyName("indexProperties");

        cmbpv = new ConfigManagedBeanPropertyValue();
        cmbpv.setValueCategory(ConfigManagedBeanPropertyValue.VALUE);
        cmbpv.setValue("foo");
        cmbp.addValue(cmbpv);

        cmbpv = new ConfigManagedBeanPropertyValue();
        cmbpv.setValueCategory(ConfigManagedBeanPropertyValue.VALUE);
        cmbpv.setValue("bar");
        cmbp.addValue(cmbpv);

        cmb.addProperty(cmbp); 

        mbf = new ManagedBeanFactory(cmb);

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
    cmbpv.setValueCategory(ConfigManagedBeanPropertyValue.VALUE);
    cmbpv.setType(Integer.class);
	cmbpv.setValue("23");
    cmbpv.convertValue();
	cmbp.addValue(cmbpv);

	cmb.addProperty(cmbp);

	mbf = new ManagedBeanFactory(cmb);

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
	cmb = new ConfigManagedBean();
	cmb.setManagedBeanClass(beanName);
	cmb.setManagedBeanScope("session");

        cmpm = new ConfigManagedPropertyMap();
        cmpm.setValueCategory(ConfigManagedPropertyMap.VALUE);
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
        cmbpv.setValueCategory(ConfigManagedBeanPropertyValue.VALUE_BINDING);
        cmbpv.setValue("#{TestRefBean.one}");

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

    public void testValueRefScope() throws Exception {
        //Testing value ref scope

        TestBean testBean = new TestBean();
        boolean exceptionThrown = false;

        //testing with:
        //  valueref in application scope
        //  managed bean in session scope
        getFacesContext().getExternalContext().getApplicationMap().put("TestRefBean", testBean);

        ValueBindingImpl valueBinding = 
            new ValueBindingImpl(new ApplicationImpl());

        valueBinding.setRef("TestRefBean.one");
        valueBinding.setValue(getFacesContext(), "one");

	cmb = new ConfigManagedBean();
	cmb.setManagedBeanClass(beanName);
	cmb.setManagedBeanScope("session");

        cmbp = new ConfigManagedBeanProperty();
        cmbp.setPropertyName("one");

        cmbpv = new ConfigManagedBeanPropertyValue();
        cmbpv.setValueCategory(ConfigManagedBeanPropertyValue.VALUE_BINDING);
        cmbpv.setValue("#{TestRefBean.one}");

        cmbp.setValue(cmbpv);
        cmb.addProperty(cmbp); 

        mbf = new ManagedBeanFactory(cmb);

        //testing with an application scope property set in a session scope bean
        assertNotNull(testBean = (TestBean) mbf.newInstance());

	//make sure bean instantiated properly. Get property back from bean.
        assertTrue(testBean.getOne().equals("one"));

        //make sure scope is stored properly
        assertTrue(mbf.getScope().equals("session"));


        //testing with:
        //  valueref in request scope
        //  managed bean in application scope
        getFacesContext().getExternalContext().getRequestMap().put("TestRefBean", testBean);

        valueBinding = new ValueBindingImpl(new ApplicationImpl());

        valueBinding.setRef("TestRefBean.one");
        valueBinding.setValue(getFacesContext(), "one");

	cmb = new ConfigManagedBean();
	cmb.setManagedBeanClass(beanName);
	cmb.setManagedBeanScope("application");

        cmbp = new ConfigManagedBeanProperty();
        cmbp.setPropertyName("one");

        cmbpv = new ConfigManagedBeanPropertyValue();
        cmbpv.setValueCategory(ConfigManagedBeanPropertyValue.VALUE_BINDING);
        cmbpv.setValue("#{TestRefBean.one}");

        cmbp.setValue(cmbpv);
        cmb.addProperty(cmbp); 

        mbf = new ManagedBeanFactory(cmb);

	exceptionThrown = false ;
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
        getFacesContext().getExternalContext().getRequestMap().remove("TestRefBean");

        //testing with:
        //  valueref in session scope
        //  managed bean in no scope
        getFacesContext().getExternalContext().getSessionMap().put("TestRefBean", testBean);

        valueBinding = new ValueBindingImpl(new ApplicationImpl());

        valueBinding.setRef("sessionScope.TestRefBean.one");
        valueBinding.setValue(getFacesContext(), "one");

	cmb = new ConfigManagedBean();
	cmb.setManagedBeanClass(beanName);
	cmb.setManagedBeanScope(null);

        cmbp = new ConfigManagedBeanProperty();
        cmbp.setPropertyName("one");

        cmbpv = new ConfigManagedBeanPropertyValue();
        cmbpv.setValueCategory(ConfigManagedBeanPropertyValue.VALUE_BINDING);
        cmbpv.setValue("#{sessionScope.TestRefBean.one}");

        cmbp.setValue(cmbpv);
        cmb.addProperty(cmbp); 

        mbf = new ManagedBeanFactory(cmb);

	exceptionThrown = false ;
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
    
    
    public void testInvalidPropertyConfiguration() throws Exception {
        // If a ConfigManagedPropertyValue has a value that requires converstion from
        // String (the default type) to another type, say Integer as an example,
        // and the CMPV's value category isn't set to Value, conversion will not
        // take place.  Thus, an error should occur when creating a new instanced
        // of the managed bean.
        
        // no value category set
        cmb = new ConfigManagedBean();
        cmb.setManagedBeanClass(beanName);
        cmb.setManagedBeanScope("session");

        boolean testBoolean = true;
        cmbp = new ConfigManagedBeanProperty();
        cmbp.setPropertyName("boolProp");
        cmbpv = new ConfigManagedBeanPropertyValue();        
        cmbpv.setValue((new Boolean(testBoolean)).toString());
        cmbp.setValue(cmbpv);
        cmb.addProperty(cmbp); 
        
        mbf = new ManagedBeanFactory(cmb);

        boolean exceptionThrown = false;
        try {
            mbf.newInstance();
        } catch (FacesException fe) {
            exceptionThrown = true;    
        }
        assertTrue(exceptionThrown);
        
        
        // value category set to VALUE_BINDING
        cmb = new ConfigManagedBean();
        cmb.setManagedBeanClass(beanName);
        cmb.setManagedBeanScope("session");
      
        cmbp = new ConfigManagedBeanProperty();
        cmbp.setPropertyName("boolProp");
        cmbpv = new ConfigManagedBeanPropertyValue();
        cmbpv.setValueCategory(ConfigManagedBeanPropertyValue.VALUE_BINDING);
        cmbpv.setValue((new Boolean(testBoolean)).toString());
        cmbp.setValue(cmbpv);
        cmb.addProperty(cmbp);

        mbf = new ManagedBeanFactory(cmb);

        exceptionThrown = false;
        try {
            mbf.newInstance();
        } catch (FacesException fe) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
                        
    }

    public void testExceptions() throws Exception {

	cmb = new ConfigManagedBean();
	cmb.setManagedBeanClass("foo");
	cmb.setManagedBeanScope("session");

        cmbp = new ConfigManagedBeanProperty();
        cmbp.setPropertyName("one");

        cmbpv = new ConfigManagedBeanPropertyValue();
        cmbpv.setValue("one");

        cmbp.setValue(cmbpv);
	boolean exceptionThrown = false;
        try {
            cmb.addProperty(cmbp); 
	} catch (FacesException fe) {
	    exceptionThrown = true;
	}
        assertTrue(exceptionThrown);
	exceptionThrown = false;
	try {
	    cmbp = null;
	    cmb.addProperty(cmbp);
	} catch (NullPointerException npe) {
	    exceptionThrown = true;
	}
        assertTrue(exceptionThrown);
    }
}
