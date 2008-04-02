/*
 * $Id: UINamingContainerTestCase.java,v 1.3 2003/03/13 01:12:41 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.util.Iterator;
import javax.faces.event.FacesEvent;
import javax.faces.validator.Validator;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * <p>Test case for the <strong>javax.faces.UINamingContainer</strong>
 * concrete class.</p>
 */

public class UINamingContainerTestCase extends UIComponentTestCase {


    // ----------------------------------------------------- Instance Variables


    // ---------------------------------------------------------- Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public UINamingContainerTestCase(String name) {
        super(name);
    }


    // -------------------------------------------------- Overall Test Methods

    /**
     * Set up instance variables required by this test case.
     */
    public void setUp() {

        component = new UINamingContainer();
        component.setComponentId("test");
        attributes = new String[0];

    }


    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {

        return (new TestSuite(UINamingContainerTestCase.class));

    }

    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {

        component = null;

    }


    // ------------------------------------------------ Individual Test Methods


    /**
     * [3.1.1] Component Type.
     */
    public void testComponentType() {

        assertEquals("componentType", UINamingContainer.TYPE,
                     component.getComponentType());

    }


    public void testFindComponent() {

	UINamingContainer 
	    containerOne = new UINamingContainer(),
	    containerTwo = new UINamingContainer(),
	    containerThree = new UINamingContainer();
	UIInput 
	    leafOne = new UIInput(),
	    leafTwo = new UIInput(),
	    leafThree = new UIInput();

	containerOne.setComponentId("containerOne");
	containerTwo.setComponentId("containerTwo");
	containerThree.setComponentId("containerThree");
	
	leafOne.setComponentId("leafOne");
	leafTwo.setComponentId("leafTwo");
	leafThree.setComponentId("leafThree");

	component.addChild(containerOne);
	containerOne.addChild(leafOne);
	containerOne.addChild(containerTwo);
	
	containerTwo.addChild(leafTwo);
	containerTwo.addChild(containerThree);

	containerThree.addChild(leafThree);

	// Test the NamingContainers are found
	assertEquals("find containerOne", containerOne,
		     component.findComponent("containerOne"));
	assertEquals("find containerTwo", containerTwo,
		     component.findComponent("containerOne.containerTwo"));
	assertEquals("find containerThree", containerThree,
		     component.findComponent("containerOne.containerTwo.containerThree"));

	// Test the leaves are found
	assertEquals("find leafOne", leafOne,
		     component.findComponent("containerOne.leafOne"));
	assertEquals("find leafTwo", leafTwo,
		     component.findComponent("containerOne.containerTwo.leafTwo"));
	assertEquals("find leafThree", leafThree,
		     component.findComponent("containerOne.containerTwo.containerThree.leafThree"));
	

    }

    public void testFindComponentNegative() {

	UINamingContainer 
	    containerOne = new UINamingContainer(),
	    containerTwo = new UINamingContainer(),
	    containerThree = new UINamingContainer();
	UIInput 
	    leafOne = new UIInput(),
	    leafTwo = new UIInput(),
	    leafThree = new UIInput();

	containerOne.setComponentId("containerOne");
	containerTwo.setComponentId("containerTwo");
	containerThree.setComponentId("containerThree");
	
	leafOne.setComponentId("leafOne");
	leafTwo.setComponentId("leafTwo");
	leafThree.setComponentId("leafThree");

	component.addChild(containerOne);
	containerOne.addChild(leafOne);
	containerOne.addChild(containerTwo);
	
	containerTwo.addChild(leafTwo);
	containerTwo.addChild(containerThree);

	containerThree.addChild(leafThree);

	// try invalid expressions
	boolean exceptionThrown = false;
	try {
	    component.findComponent(".");
	}
	catch (IllegalArgumentException e) {
	    exceptionThrown = true;
	}
	assertTrue("Expected IllegalArgumentException", exceptionThrown);

	exceptionThrown = false;
	try {
	    component.findComponent("containerOne.");
	}
	catch (IllegalArgumentException e) {
	    exceptionThrown = true;
	}
	assertTrue("Expected IllegalArgumentException", exceptionThrown);
	
	exceptionThrown = false;
	try {
	    component.findComponent("containerOne.containerTwo.");
	}
	catch (IllegalArgumentException e) {
	    exceptionThrown = true;
	}
	assertTrue("Expected IllegalArgumentException", exceptionThrown);

	exceptionThrown = false;
	try {
	    component.findComponent("containerOne.containerTwo.containerThree.");
	}
	catch (IllegalArgumentException e) {
	    exceptionThrown = true;
	}
	assertTrue("Expected IllegalArgumentException", exceptionThrown);

	// Throw a non-NamingContainer in the middle
	exceptionThrown = false;
	try {
	    component.findComponent("containerOne.leafOne.containerTwo");
	}
	catch (IllegalArgumentException e) {
	    exceptionThrown = true;
	}
	assertTrue("Expected IllegalArgumentException", exceptionThrown);

	exceptionThrown = false;
	try {
	    component.findComponent("containerTwo.containerOne");
	}
	catch (IllegalArgumentException e) {
	    exceptionThrown = true;
	}
	assertTrue("Unexpected IllegalArgumentException", !exceptionThrown);

	exceptionThrown = false;
	try {
	    component.findComponent("containerOne.containerTwo.leafThree");
	}
	catch (IllegalArgumentException e) {
	    exceptionThrown = true;
	}
	assertTrue("Unexpected IllegalArgumentException", !exceptionThrown);


    }


}
