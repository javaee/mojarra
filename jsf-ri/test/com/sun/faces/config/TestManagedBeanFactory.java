/*
 * $Id: TestManagedBeanFactory.java,v 1.31 2006/03/29 23:04:46 rlubke Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

package com.sun.faces.config;

import com.sun.faces.cactus.ServletFacesTestCase;
import com.sun.faces.TestBean;
import com.sun.faces.config.beans.ListEntriesBean;
import com.sun.faces.config.beans.ManagedBeanBean;
import com.sun.faces.config.beans.ManagedPropertyBean;
import com.sun.faces.config.beans.MapEntriesBean;
import com.sun.faces.config.beans.MapEntryBean;
import com.sun.faces.spi.ManagedBeanFactory.Scope;

import javax.el.ValueExpression;

import javax.faces.FacesException;

import com.sun.faces.util.Util;
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
    ManagedBeanFactoryImpl mbf;
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

        mbf = new ManagedBeanFactoryImpl(bean);

        assertNotNull(mbf.newInstance(getFacesContext()));

	bean.setManagedBeanScope("request");
	mbf.setManagedBeanBean(bean);
        assertTrue(mbf.getScope() == Scope.REQUEST);
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

        mbf = new ManagedBeanFactoryImpl(bean);

        //testing with a property set
        assertNotNull(testBean = (TestBean) mbf.newInstance(getFacesContext()));

        //make sure bean instantiated properly. Get property back from bean.
        assertTrue(testBean.getOne().equals("one"));

        //make sure scope is stored properly
        assertTrue(mbf.getScope() == Scope.SESSION);
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

        mbf = new ManagedBeanFactoryImpl(bean);

        //testing with a property set
        assertNotNull(testBean = (TestBean) mbf.newInstance(getFacesContext()));

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
        assertTrue(mbf.getScope() == Scope.SESSION);
    }
    
    public void testSimpleNumericProperty() throws Exception {
        // If a property value is "" ensure numeric properties
        // are set to 0.
        bean = new ManagedBeanBean();
        bean.setManagedBeanClass(beanName);
        bean.setManagedBeanScope("session");
                
        property = new ManagedPropertyBean();
        property.setPropertyName("byteProp");
        property.setValue("");
        bean.addManagedProperty(property);
        
        property = new ManagedPropertyBean();
        property.setPropertyName("charProp");
        property.setValue("");
        bean.addManagedProperty(property);
              
        property = new ManagedPropertyBean();
        property.setPropertyName("doubleProp");
        property.setValue("");
        bean.addManagedProperty(property);
      
        property = new ManagedPropertyBean();
        property.setPropertyName("floatProp");
        property.setValue("");
        bean.addManagedProperty(property);
       
        property = new ManagedPropertyBean();
        property.setPropertyName("intProp");
        property.setValue("");
        bean.addManagedProperty(property);
       
        property = new ManagedPropertyBean();
        property.setPropertyName("longProp");
        property.setValue("");
        bean.addManagedProperty(property);
        
        property = new ManagedPropertyBean();
        property.setPropertyName("shortProp");
        property.setValue("");
        bean.addManagedProperty(property);
        
        mbf = new ManagedBeanFactoryImpl(bean);
               
        assertNotNull(testBean = (TestBean) mbf.newInstance(getFacesContext()));
        
        mbf = new ManagedBeanFactoryImpl(bean);      
        assertTrue(testBean.getByteProp() == 0); 
        assertTrue(testBean.getCharProp() == 0);
        assertTrue(testBean.getDoubleProp() == 0);
        assertTrue(testBean.getFloatProp() == 0);
        assertTrue(testBean.getIntProp() == 0);
        assertTrue(testBean.getLongProp() == 0);
        assertTrue(testBean.getShortProp() == 0);
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

        ManagedPropertyBean property2 = new ManagedPropertyBean();
        property2.setPropertyName("indexPropertiesNull");
        property2.setListEntries(listEntries);

        bean.addManagedProperty(property);
        bean.addManagedProperty(property2);

        mbf = new ManagedBeanFactoryImpl(bean);

        //testing with a property set
        assertNotNull(testBean = (TestBean) mbf.newInstance(getFacesContext()));

        //make sure bean instantiated properly. Get property back from bean.
        ArrayList properties = (ArrayList) testBean.getIndexProperties();
        assertTrue(properties.get(5).equals("foo"));
        assertTrue(properties.get(6).equals("bar"));

        // setter shouldn't be called if bean getter returns List
        assertTrue(!testBean.getListSetterCalled());

        // setter should be called if bean getter returned null
        assertTrue(testBean.getListNullSetterCalled());

        //make sure scope is stored properly
        assertTrue(mbf.getScope() == Scope.SESSION);
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

        ManagedPropertyBean property2 = new ManagedPropertyBean();
        property2.setPropertyName("mapPropertyNull");
        property2.setMapEntries(mapEntries);

        bean.addManagedProperty(property);
        bean.addManagedProperty(property2);
        mbf = new ManagedBeanFactoryImpl(bean);

        //testing with a property set
        assertNotNull(testBean = (TestBean) mbf.newInstance(getFacesContext()));

        //make sure bean instantiated properly. Get property back from bean.
        HashMap mapProperty = (HashMap)
            testBean.getMapProperty();
        assertTrue(((String) mapProperty.get("name")).equals("Justyna"));

        // setter shouldn't be called if bean getter returns Map
        assertTrue(!testBean.getMapPropertySetterCalled());

        // setter should be called if bean getter returned null
        assertTrue(testBean.getMapPropertyNullSetterCalled());

        //make sure scope is stored properly
        assertTrue(mbf.getScope() == Scope.SESSION);
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

        mbf = new ManagedBeanFactoryImpl(bean);

        //testing with a property set
        assertNotNull(testBean = (TestBean) mbf.newInstance(getFacesContext()));

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
        mbf = new ManagedBeanFactoryImpl(bean);

        //testing with a property set
        assertNotNull(testBean = (TestBean) mbf.newInstance(getFacesContext()));

        //make sure bean instantiated properly. Get property back from bean.
        HashMap mapProperty = (HashMap)
            testBean.getMapProperty();

        assertTrue(mapProperty.get("name") instanceof Integer);

        Integer integer = new Integer("23");
        assertTrue(mapProperty.get("name").equals(integer));

        //make sure scope is stored properly
        assertTrue(mbf.getScope() == Scope.SESSION);
    }


    public void testValueRefProperty() throws Exception {
        //Testing simple value ref property

        TestBean testBean = new TestBean();
        getFacesContext().getExternalContext().getSessionMap().put(
            "TestRefBean", testBean);

        ValueExpression valueExpression = Util.getValueExpression("#{TestRefBean.one}");
        valueExpression.setValue(getFacesContext().getELContext(), "one");

        bean = new ManagedBeanBean();
        bean.setManagedBeanClass(beanName);
        bean.setManagedBeanScope("session");

        property = new ManagedPropertyBean();
        property.setPropertyName("one");
        property.setValue("#{TestRefBean.one}");
        bean.addManagedProperty(property);

        mbf = new ManagedBeanFactoryImpl(bean);

        //testing with a property set
        assertNotNull(testBean = (TestBean) mbf.newInstance(getFacesContext()));

        //make sure bean instantiated properly. Get property back from bean.
        assertTrue(testBean.getOne().equals("one"));

        //make sure scope is stored properly
        assertTrue(mbf.getScope() == Scope.SESSION);

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

        ValueExpression valueExpression =
            Util.getValueExpression("#{TestRefBean.one}");
        valueExpression.setValue(getFacesContext().getELContext(), "one");

        bean = new ManagedBeanBean();
        bean.setManagedBeanClass(beanName);
        bean.setManagedBeanScope("session");

        property = new ManagedPropertyBean();
        property.setPropertyName("one");
        property.setValue("#{TestRefBean.one}");
        bean.addManagedProperty(property);

        mbf = new ManagedBeanFactoryImpl(bean);

        //testing with an application scope property set in a session scope bean
        assertNotNull(testBean = (TestBean) mbf.newInstance(getFacesContext()));

        //make sure bean instantiated properly. Get property back from bean.
        assertTrue(testBean.getOne().equals("one"));

        //make sure scope is stored properly
        assertTrue(mbf.getScope() == Scope.SESSION);


        //testing with:
        //  valueref in request scope
        //  managed bean in application scope
        getFacesContext().getExternalContext().getRequestMap().put(
            "TestRefBean", testBean);

        valueExpression = Util.getValueExpression("#{TestRefBean.one}");
        valueExpression.setValue(getFacesContext().getELContext(), "one");

        bean = new ManagedBeanBean();
        bean.setManagedBeanClass(beanName);
        bean.setManagedBeanScope("application");

        property = new ManagedPropertyBean();
        property.setPropertyName("one");
        property.setValue("#{TestRefBean.one}");

        bean.addManagedProperty(property);

        mbf = new ManagedBeanFactoryImpl(bean);

        exceptionThrown = false;
        try {
            mbf.newInstance(getFacesContext());
            fail("Should have thrown FacesException");
        } catch (FacesException ex) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        //make sure scope is stored properly
        assertTrue(mbf.getScope() == Scope.APPLICATION);

        //cleanup
        getFacesContext().getExternalContext().getRequestMap().remove(
            "TestRefBean");

        //testing with:
        //  valueref in session scope
        //  managed bean in no scope
        getFacesContext().getExternalContext().getSessionMap().put(
            "TestRefBean", testBean);

        valueExpression = Util.getValueExpression("#{sessionScope.TestRefBean.one}");
        valueExpression.setValue(getFacesContext().getELContext(), "one");

        bean = new ManagedBeanBean();
        bean.setManagedBeanClass(beanName);
        bean.setManagedBeanScope(null);

        property = new ManagedPropertyBean();
        property.setPropertyName("one");
        property.setValue("#{sessionScope.TestRefBean.one}");
        bean.addManagedProperty(property);

        mbf = new ManagedBeanFactoryImpl(bean);

        exceptionThrown = false;
        try {
            mbf.newInstance(getFacesContext());
            fail("Should have thrown FacesException");
        } catch (FacesException ex) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        //make sure scope is stored properly
        assertTrue(mbf.getScope() == Scope.NONE);
    }
    
    public void testNoneScope() throws Exception {
        //Testing value ref scope
        TestBean testBean = new TestBean();
        boolean exceptionThrown = false;

        //  valueref in request scope
        //  managed bean in none scope
        // this should fail
        getFacesContext().getExternalContext().getRequestMap().put(
            "TestRefBean", testBean);

        ValueExpression valueExpression = Util.getValueExpression("#{TestRefBean.one}");
        valueExpression.setValue(getFacesContext().getELContext(), "one");

        bean = new ManagedBeanBean();
        bean.setManagedBeanClass(beanName);
        bean.setManagedBeanScope("none");

        property = new ManagedPropertyBean();
        property.setPropertyName("one");
        property.setValue("#{TestRefBean.one}");

        bean.addManagedProperty(property);

        mbf = new ManagedBeanFactoryImpl(bean);

        exceptionThrown = false;
        try {
            mbf.newInstance(getFacesContext());
            fail("Should have thrown FacesException");
        } catch (FacesException ex) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        //make sure scope is stored properly
        assertTrue(mbf.getScope() == Scope.NONE);

        //cleanup
        getFacesContext().getExternalContext().getRequestMap().remove(
            "TestRefBean");

        //  valueref in none scope
        //  managed bean in none scope
        // this should pass
        ValueExpression valueExpression1 = 
        Util.getValueExpression("#{testBean.customerBean.name}");
        exceptionThrown = false;
        try {
            valueExpression1.getValue(getFacesContext().getELContext());
        } catch (FacesException ex) {
            exceptionThrown = true;
        }
        assertTrue(!exceptionThrown);
      
    }

    public void testMixedBean() throws Exception {
        ValueExpression vb =
            Util.getValueExpression(
                "#{mixedBean}");
        TestBean bean = (TestBean) vb.getValue(getFacesContext().getELContext());
        assertEquals("mixed value Bobby Orr", bean.getProp());

        vb =
            Util.getValueExpression(
                "#{mixedBean.prop}");
        assertEquals(bean.getProp(), (String) vb.getValue(getFacesContext().getELContext()));
    } 

    public void testMixedBeanNegative() throws Exception {
        ValueExpression vb =
            Util.getValueExpression(
                "#{threeBeanSaladNegative}");
	boolean exceptionThrown = false;
	try {
	    TestBean bean = (TestBean) vb.getValue(getFacesContext().getELContext());
	    assertTrue(false);
	}
	catch (FacesException pnfe) {
	    exceptionThrown = true;
	}

	assertTrue(exceptionThrown);
    }

    public void testMixedBeanPositive() throws Exception {
        ValueExpression vb =
            Util.getValueExpression(
                "#{threeBeanSaladPositive}");
        TestBean bean = (TestBean) vb.getValue(getFacesContext().getELContext());
        assertEquals("request request session session none none", 
		     bean.getProp());

        vb =
            Util.getValueExpression(
                "#{threeBeanSaladPositive.prop}");
        assertEquals(bean.getProp(), (String) vb.getValue(getFacesContext().getELContext()));
    } 


    public void testConstructorException() {
        // constructor of this bean throws ann exception. Make sure the
        // exception is not swallowed.
        ValueExpression valueExpression1 = 
        Util.getValueExpression("#{exceptionBean.one}");
        boolean exceptionThrown = false;
        try {
            valueExpression1.getValue(getFacesContext().getELContext());
        } catch (FacesException ex) {
            exceptionThrown = true;
            assertTrue((ex.getMessage().
                indexOf("TestConstructorException Passed")) != -1);
        }   
        assertTrue(exceptionThrown);
        
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
