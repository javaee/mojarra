/*
 * $Id: UIPanelTestCase.java,v 1.5 2003/03/13 01:12:41 craigmcc Exp $
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
 * <p>Test case for the <strong>javax.faces.UIPanel</strong>
 * concrete class.</p>
 */

public class UIPanelTestCase extends UIOutputTestCase {


    // ----------------------------------------------------- Instance Variables


    // ----------------------------------------------------------- Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public UIPanelTestCase(String name) {
        super(name);
    }


    // --------------------------------------------------- Overall Test Methods
// This class is necessary since UIPanel isn't a naming container by
// default.
private class UIPanelNamingContainer extends UIPanel implements NamingContainer {
    private UINamingContainer namingContainer = null;

    UIPanelNamingContainer() {
	namingContainer = new UINamingContainer();
    }

    public void addComponentToNamespace(UIComponent namedComponent) {
	namingContainer.addComponentToNamespace(namedComponent);
    }
    public void removeComponentFromNamespace(UIComponent namedComponent) {
	namingContainer.removeComponentFromNamespace(namedComponent);
    } 
    public UIComponent findComponentInNamespace(String name) {
	return namingContainer.findComponentInNamespace(name);
    }

    public String generateClientId() {
	return namingContainer.generateClientId();
    }
}



    /**
     * Set up instance variables required by this test case.
     */
    public void setUp() {

        component = new UIPanelNamingContainer();
        component.setComponentId("test");
        attributes = new String[0];

    }


    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {

        return (new TestSuite(UIPanelTestCase.class));

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

        assertEquals("componentType", UIPanel.TYPE,
                     component.getComponentType());

    }


    /**
     * [3.1.10] Renders Children.
     */
    public void testRendersChildren() {

        assertTrue("rendersChildren", component.getRendersChildren());

    }


}
