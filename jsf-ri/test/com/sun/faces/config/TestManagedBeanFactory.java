/*
 * $Id: TestManagedBeanFactory.java,v 1.2 2003/04/29 20:52:25 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config;

import com.sun.faces.TestBean;
import com.sun.faces.config.ConfigManagedBean;
import com.sun.faces.config.ConfigManagedBeanProperty;
import com.sun.faces.config.ConfigManagedBeanPropertyValue;
import com.sun.faces.config.ManagedBeanFactory;

import org.apache.cactus.ServletTestCase;


/**
 * <p>Unit tests for Managed Bean Factory.</p>
 */

public class TestManagedBeanFactory extends ServletTestCase {



    // ----------------------------------------------------- Instance Variables

    ConfigManagedBean cmb;
    ConfigManagedBeanProperty cmbp;
    ConfigManagedBeanPropertyValue cmbpv;
    ManagedBeanFactory mbf;

    // ----------------------------------------------------------- Constructors


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


    // ------------------------------------------------ Individual Test Methods



    // Test managed bean instantiation
    public void testFull() throws Exception {
        String beanName = "com.sun.faces.TestBean";
        cmb = new ConfigManagedBean();
        cmb.setManagedBeanClass(beanName);
        cmb.setManagedBeanScope("session");

        mbf = new ManagedBeanFactory(cmb);

        //testing with no properties set
        assertNotNull(mbf.newInstance());

        cmbpv = new ConfigManagedBeanPropertyValue();
        cmbpv.setValue("one");

        cmbp = new ConfigManagedBeanProperty();
        cmbp.setPropertyName("one");
        cmbp.setValue(cmbpv);

        cmb.addProperty(cmbp); 

        mbf.setConfigManagedBean(cmb);
        TestBean testBean;

        //testing with a property set
        assertNotNull(testBean = (TestBean) mbf.newInstance());

        //make sure bean instantiated properly. Get property back from bean.
        assertTrue(testBean.getOne().equals("one"));

        //make sure scope is stored properly
        assertTrue(mbf.getScope().equals("session"));

        //FIX_ME: add test cases for property COLLECTIONS
    }
}
