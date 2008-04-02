/*
 * $Id: UISelectManyTestCase.java,v 1.6 2003/01/23 17:42:51 rkitain Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.util.Iterator;
import javax.faces.event.FacesEvent;
import javax.faces.event.ValueChangedEvent;
import javax.faces.mock.MockFacesContext;
import javax.faces.validator.Validator;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * <p>Test case for the <strong>javax.faces.UISelectMany</strong>
 * concrete class.</p>
 */

public class UISelectManyTestCase extends UISelectBaseTestCase {


    // ----------------------------------------------------- Instance Variables


    // ---------------------------------------------------------- Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public UISelectManyTestCase(String name) {
        super(name);
    }


    // -------------------------------------------------- Overall Test Methods

    public UISelectBase newUISelectBaseSubclass() {
	return new UISelectMany();
    }


    /**
     * Set up instance variables required by this test case.
     */
    public void setUp() {

        component = new UISelectMany();
        component.setComponentId("test");
        attributes = new String[]
            { "componentId", "rendersChildren" };

    }


    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {

        return (new TestSuite(UISelectManyTestCase.class));

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

        assertEquals("componentType", UISelectMany.TYPE,
                     component.getComponentType());

    }


    /**
     * [3.1.7] Attribute/Property Transparency
     */
    public void testAttributePropertyTransparency() {

        super.testAttributePropertyTransparency();
        UISelectMany selectMany = (UISelectMany) component;

        assertNull("selectedValues1", selectMany.getSelectedValues());
        assertNull("selectedValues2", selectMany.getAttribute("value"));

        selectMany.setSelectedValues(new String[] { "foo" });
        Object result3[] = selectMany.getSelectedValues();
        assertNotNull("selectedValues3a", result3);
        assertEquals("selectedValues3b", 1, result3.length);
        assertEquals("selectedValues3c", "foo", result3[0]);
        Object result4[] = (String[]) selectMany.getAttribute("value");
        assertNotNull("selectedValues4a", result4);
        assertEquals("selectedValues4b", 1, result4.length);
        assertEquals("selectedValues4c", "foo", result4[0]);

        selectMany.setAttribute("value", new String[] { "bar" });
        Object result5[] = selectMany.getSelectedValues();
        assertNotNull("selectedValues5a", result5);
        assertEquals("selectedValues5b", 1, result5.length);
        assertEquals("selectedValues5c", "bar", result5[0]);
        Object result6[] = (String[]) selectMany.getAttribute("value");
        assertNotNull("selectedValues6a", result6);
        assertEquals("selectedValues6b", 1, result6.length);
        assertEquals("selectedValues6c", "bar", result6[0]);

        selectMany.setAttribute("value", null);
        assertNull("selectedValues7", selectMany.getSelectedValues());
        assertNull("selectedValues8", selectMany.getAttribute("value"));

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
    
    public void testFireValueChangeEvents() {
        
        MockFacesContext facesContext = new MockFacesContext();
       
        // case 1: previous value null, new value is null;
        // make sure ValueChangedEvent is not fired if new value is same
        // as the old value.
        UISelectMany selectMany = (UISelectMany) component;
        selectMany.setAttribute(UIInput.PREVIOUS_VALUE,
            selectMany.currentValue(facesContext));
        selectMany.setValue(null);
        selectMany.validate(facesContext);
        // ValueChangedEvent should not be fired in this case since the value
        // didn't change.
        Iterator eventsItr = facesContext.getFacesEvents();
        assertTrue(!(eventsItr.hasNext()));
        
        // case 2: previous value null, new value is {"one", "two", "one"};
        // make sure ValueChangedEvent is fired if new value is different 
        // from the old value.
        Object selectedValues[] = {"one", "two", "one"};
        selectMany.setAttribute(UIInput.PREVIOUS_VALUE,
            selectMany.currentValue(facesContext));
        selectMany.setValue(selectedValues);
        selectMany.validate(facesContext);
        
        // ValueChangedEvent should be fired in this case since the value
        // changed
        eventsItr = facesContext.getFacesEvents();
        assertTrue((eventsItr.hasNext()));
        Object eventObj = eventsItr.next();
        // make sure it is an instance of ValueChangedEvent
        assertTrue(eventObj instanceof ValueChangedEvent);
        
        // case 3: previous value {"one", "two", "one"}
        // new value is {"one", "one", "two"}
        // create a new FacesContext make sure we don't have any events 
        // queued from previous test case.
        facesContext = new MockFacesContext();
        selectMany.setAttribute(UIInput.PREVIOUS_VALUE,
            selectMany.currentValue(facesContext));
        Object selectedValues2[] = {"one", "one", "two"};
        selectMany.setValue(selectedValues2);
        
        selectMany.validate(facesContext);
        // make sure ValueChangedEvent was not fired.
        eventsItr = facesContext.getFacesEvents();
        assertTrue(!(eventsItr.hasNext()));
        
        // case 4: previous value {{"one", "one", "two"}
        // new value is {"one", "two", "two"}
        selectMany.setAttribute(UIInput.PREVIOUS_VALUE,
            selectMany.currentValue(facesContext));
        Object newValues[] = {"one", "two", "two"};
        selectMany.setValue(newValues);
        selectMany.validate(facesContext);
        
        // ValueChangedEvent should be fired in this case since the value
        // changed
        eventsItr = facesContext.getFacesEvents();
        assertTrue((eventsItr.hasNext()));
        eventObj = eventsItr.next();
        // make sure it is an instance of ValueChangedEvent
        assertTrue(eventObj instanceof ValueChangedEvent);
        
        // case 5: previous value {"one", "two", "two"}
        // new value is {"one", "two", "two", "one"}
        facesContext = new MockFacesContext();
        selectMany.setAttribute(UIInput.PREVIOUS_VALUE,
            selectMany.currentValue(facesContext));
        Object newValues2[] = {"one", "two", "two", "one"};
        selectMany.setValue(newValues2);
        selectMany.validate(facesContext);
        
        // ValueChangedEvent should be fired in this case since the value
        // changed
        eventsItr = facesContext.getFacesEvents();
        assertTrue((eventsItr.hasNext()));
        eventObj = eventsItr.next();
        // make sure it is an instance of ValueChangedEvent
        assertTrue(eventObj instanceof ValueChangedEvent);
    }
}
