/*
 * $Id: UISelectOneTestCase.java,v 1.3 2002/12/17 23:31:00 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.util.Iterator;
import javax.faces.event.FacesEvent;
import javax.faces.validator.Validator;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;
import javax.faces.mock.MockFacesContext;
import javax.faces.context.FacesContext;


/**
 * <p>Test case for the <strong>javax.faces.UISelectOne</strong>
 * concrete class.</p>
 */

public class UISelectOneTestCase extends UISelectBaseTestCase {


    // ----------------------------------------------------- Instance Variables

    // ----------------------------------------------------- Class Variables

    // ---------------------------------------------------------- Constructors

    public UISelectBase newUISelectBaseSubclass() {
	return new UISelectOne();
    }

    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public UISelectOneTestCase(String name) {
        super(name);
    }


    // -------------------------------------------------- Overall Test Methods

    /**
     * Set up instance variables required by this test case.
     */
    public void setUp() {

        component = new UISelectOne();
        component.setComponentId("test");
        attributes = new String[]
            { "componentId", "rendersChildren" };

    }


    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {

        return (new TestSuite(UISelectOneTestCase.class));

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

        assertEquals("componentType", UISelectOne.TYPE,
                     component.getComponentType());

    }


    /**
     * [3.1.7] Attribute/Property Transparency
     */
    public void testAttributePropertyTransparency() {

        super.testAttributePropertyTransparency();
        UISelectOne selectOne = (UISelectOne) component;

        assertNull("selectedValue1", selectOne.getSelectedValue());
        assertNull("selectedValue2", selectOne.getAttribute("value"));
        selectOne.setSelectedValue("foo");
        assertEquals("selectedValue3", "foo", selectOne.getSelectedValue());
        assertEquals("selectedValue4", "foo",
                     (String) selectOne.getAttribute("value"));
        selectOne.setAttribute("value", "bar");
        assertEquals("selectedValue5", "bar", selectOne.getSelectedValue());
        assertEquals("selectedValue6", "bar",
                     (String) selectOne.getAttribute("value"));
        selectOne.setAttribute("value", null);
        assertNull("selectedValue7", selectOne.getSelectedValue());
        assertNull("selectedValue8", selectOne.getAttribute("value"));

    }


    public void testSelectItemNoIds() {
	doSelectItemNoIds();
    }

    public void testSelectItemNoIdsCrazyOrder() {
	doSelectItemNoIdsCrazyOrder();
    }

    public void testSelectItemNoIdsExtraNonNamingContainerRootChild() {
	doSelectItemNoIdsExtraNonNamingContainerRootChild();
    }

    public void testSelectItemNoIdsExtraNamingContainerRootChild() {
	doSelectItemNoIdsExtraNamingContainerRootChild();
    }

    public void testSelectItemWithNamedRoot() {
	doSelectItemWithNamedRoot();
    }

    public void testSelectItemWithNamedSelectBase() {
	doSelectItemWithNamedSelectBase();
    }

    public void testSelectItemWithNamedSelectBaseExtraNamingContainerRootChild() {
	doSelectItemWithNamedSelectBaseExtraNamingContainerRootChild();
    }

    public void testDuplicateSelectItemIdsInNamedParent() {
	doDuplicateSelectItemIdsInNamedParent();
    }

    public void testDuplicateSelectItemIdsInUnnamedParent() {
	doDuplicateSelectItemIdsInUnnamedParent();
    }

    public void testDuplicateSelectBaseIds() {
	doDuplicateSelectBaseIds();
    }

}
