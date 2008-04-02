/*
 * $Id: UINamingContainerTestCase.java,v 1.6 2003/09/25 07:46:09 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.io.IOException;
import java.util.Iterator;
import javax.faces.component.NamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * <p>Unit tests for {@link UINamingContainer}.</p>
 */

public class UINamingContainerTestCase extends UIComponentBaseTestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public UINamingContainerTestCase(String name) {
        super(name);
    }


    // ---------------------------------------------------- Overall Test Methods


    // Set up instance variables required by this test case.
    public void setUp() {
        super.setUp();
        component = new UINamingContainer();
        expectedId = null;
        expectedRendererType = null;
    }


    // Return the tests included in this test case.
    public static Test suite() {
        return (new TestSuite(UINamingContainerTestCase.class));
    }


    // Tear down instance variables required by ths test case
    public void tearDown() {
        super.tearDown();
    }


    // ------------------------------------------------- Individual Test Methods


    // Negative tests for findComponent()
    public void testFindComponentNegative() {

	UINamingContainer
	    containerOne = new UINamingContainer(),
	    containerTwo = new UINamingContainer(),
	    containerThree = new UINamingContainer();
	UIComponent 
	    leafOne = new TestComponent(),
	    leafTwo = new TestComponent(),
	    leafThree = new TestComponent();

	containerOne.setId("containerOne");
	containerTwo.setId("containerTwo");
	containerThree.setId("containerThree");
	
	leafOne.setId("leafOne");
	leafTwo.setId("leafTwo");
	leafThree.setId("leafThree");

	component.getChildren().add(containerOne);
	containerOne.getChildren().add(leafOne);
	containerOne.getChildren().add(containerTwo);
	
	containerTwo.getChildren().add(leafTwo);
	containerTwo.getChildren().add(containerThree);

	containerThree.getChildren().add(leafThree);

	// try invalid expressions
	boolean exceptionThrown = false;
	try {
	    component.findComponent("" + UIComponent.SEPARATOR_CHAR);
	}
	catch (IllegalArgumentException e) {
	    exceptionThrown = true;
	}
	assertTrue("Expected IllegalArgumentException", exceptionThrown);

	exceptionThrown = false;
	try {
	    component.findComponent("containerOne" + UIComponent.SEPARATOR_CHAR);
	}
	catch (IllegalArgumentException e) {
	    exceptionThrown = true;
	}
	assertTrue("Expected IllegalArgumentException", exceptionThrown);
	
	exceptionThrown = false;
	try {
	    component.findComponent("containerOne" + 
				    UIComponent.SEPARATOR_CHAR + 
				    "containerTwo" + 
				    UIComponent.SEPARATOR_CHAR);
	}
	catch (IllegalArgumentException e) {
	    exceptionThrown = true;
	}
	assertTrue("Expected IllegalArgumentException", exceptionThrown);

	exceptionThrown = false;
	try {
	    component.findComponent("containerOne" + UIComponent.SEPARATOR_CHAR + "containerTwo" + UIComponent.SEPARATOR_CHAR + "containerThree" + UIComponent.SEPARATOR_CHAR);
	}
	catch (IllegalArgumentException e) {
	    exceptionThrown = true;
	}
	assertTrue("Expected IllegalArgumentException", exceptionThrown);

	// Throw a non-NamingContainer in the middle
	exceptionThrown = false;
	try {
	    component.findComponent("containerOne" + UIComponent.SEPARATOR_CHAR + "leafOne" + UIComponent.SEPARATOR_CHAR + "containerTwo");
	}
	catch (IllegalArgumentException e) {
	    exceptionThrown = true;
	}
	assertTrue("Expected IllegalArgumentException", exceptionThrown);

	exceptionThrown = false;
	try {
	    component.findComponent("containerTwo" + UIComponent.SEPARATOR_CHAR + "containerOne");
	}
	catch (IllegalArgumentException e) {
	    exceptionThrown = true;
	}
	assertTrue("Unexpected IllegalArgumentException", !exceptionThrown);

	exceptionThrown = false;
	try {
	    component.findComponent("containerOne" + UIComponent.SEPARATOR_CHAR + "containerTwo" + UIComponent.SEPARATOR_CHAR + "leafThree");
	}
	catch (IllegalArgumentException e) {
	    exceptionThrown = true;
	}
	assertTrue("Unexpected IllegalArgumentException", !exceptionThrown);


    }


    // Positive tests for findComponent()
    public void testFindComponentPositive() {

	UINamingContainer
	    containerOne = new UINamingContainer(),
	    containerTwo = new UINamingContainer(),
	    containerThree = new UINamingContainer();
	UIComponent 
	    leafOne = new TestComponent(),
	    leafTwo = new TestComponent(),
	    leafThree = new TestComponent();

	containerOne.setId("containerOne");
	containerTwo.setId("containerTwo");
	containerThree.setId("containerThree");
	
	leafOne.setId("leafOne");
	leafTwo.setId("leafTwo");
	leafThree.setId("leafThree");

	component.getChildren().add(containerOne);
	containerOne.getChildren().add(leafOne);
	containerOne.getChildren().add(containerTwo);
	
	containerTwo.getChildren().add(leafTwo);
	containerTwo.getChildren().add(containerThree);

	containerThree.getChildren().add(leafThree);

	// Test the NamingContainers are found
	assertEquals("find containerOne", containerOne,
		     component.findComponent("containerOne"));
	assertEquals("find containerTwo", containerTwo,
		     component.findComponent("containerOne" + UIComponent.SEPARATOR_CHAR + "containerTwo"));
	assertEquals("find containerThree", containerThree,
		     component.findComponent("containerOne" + UIComponent.SEPARATOR_CHAR + "containerTwo" + UIComponent.SEPARATOR_CHAR + "containerThree"));

	// Test the leaves are found
	assertEquals("find leafOne", leafOne,
		     component.findComponent("containerOne" + UIComponent.SEPARATOR_CHAR + "leafOne"));
	assertEquals("find leafTwo", leafTwo,
		     component.findComponent("containerOne" + UIComponent.SEPARATOR_CHAR + "containerTwo" + UIComponent.SEPARATOR_CHAR + "leafTwo"));
	assertEquals("find leafThree", leafThree,
		     component.findComponent("containerOne" + UIComponent.SEPARATOR_CHAR + "containerTwo" + UIComponent.SEPARATOR_CHAR + "containerThree" + UIComponent.SEPARATOR_CHAR + "leafThree"));

    }


    // Suppress lifecycle tests since we do not have a renderer
    public void testLifecycleManagement() {
    }


    // Test a pristine UINamingContainer instance
    public void testPristine() {

        super.testPristine();
        UINamingContainer namingContainer =
            (UINamingContainer) component;

    }


    // Test setting properties to invalid values
    public void testPropertiesInvalid() throws Exception {

        super.testPropertiesInvalid();
        UINamingContainer namingContainer =
            (UINamingContainer) component;

    }


    // Test setting properties to valid values
    public void testPropertiesValid() throws Exception {

        super.testPropertiesValid();
        UINamingContainer namingContainer =
            (UINamingContainer) component;

    }
}
