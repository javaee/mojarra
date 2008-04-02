/*
 * $Id: UISelectBooleanTestCase.java,v 1.8 2003/04/29 18:51:54 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
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
 * <p>Test case for the <strong>javax.faces.UISelectBoolean</strong>
 * concrete class.</p>
 */

public class UISelectBooleanTestCase extends UIInputTestCase {


    // ----------------------------------------------------- Instance Variables


    // ---------------------------------------------------------- Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public UISelectBooleanTestCase(String name) {
        super(name);
    }


    // -------------------------------------------------- Overall Test Methods
// This class is necessary since UISelectBoolean isn't a naming container by
// default.
private class UISelectBooleanNamingContainer extends UISelectBoolean implements NamingContainer {
    private UINamingContainer namingContainer = null;

    UISelectBooleanNamingContainer() {
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

        component = new UISelectBooleanNamingContainer();
        component.setComponentId("test");
        attributes = new String[0];
        rendererType = "Checkbox";

    }


    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {

        return (new TestSuite(UISelectBooleanTestCase.class));

    }

    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {

        component = null;

    }


    // ------------------------------------------------ Individual Test Methods


    /**
     * [3.1.7] Attribute/Property Transparency
     */
    public void testAttributePropertyTransparency() {

        UISelectBoolean selectBoolean = (UISelectBoolean) component;

        // Test "selected" property
        assertTrue("selected1", !selectBoolean.isSelected());
        assertTrue("selected2",
                   selectBoolean.getValue() == null);

        selectBoolean.setSelected(true);
        assertTrue("selected3", selectBoolean.isSelected());
        assertTrue("selected4",
                   ((Boolean) selectBoolean.getValue()).booleanValue());
        selectBoolean.setValue(Boolean.FALSE);
        assertTrue("selected5", !selectBoolean.isSelected());
        assertTrue("selected6",
                   !((Boolean) selectBoolean.getValue()).booleanValue());

        // Perform standard tests
        selectBoolean.setValue(null);
        super.testAttributePropertyTransparency();

    }

    public void testFireValueChangeEvents() {

        MockFacesContext facesContext = new MockFacesContext();

        // case 1: previous value null, new value is null;
        // make sure ValueChangedEvent is not fired if new value is same
        // as the old value.
        UISelectBoolean selectBoolean = (UISelectBoolean) component;
        selectBoolean.setPrevious(selectBoolean.currentValue(facesContext));
        selectBoolean.setValue(null);
        selectBoolean.validate(facesContext);
        // ValueChangedEvent should not be fired in this case since the value
        // didn't change.
        Iterator eventsItr = facesContext.getFacesEvents();
        assertTrue(!(eventsItr.hasNext()));

        // case 2: previous value null, new value is false;
        // make sure ValueChangedEvent is fired if new value is different
        // from the old value.
        selectBoolean.setPrevious(selectBoolean.currentValue(facesContext));
        selectBoolean.setValue(Boolean.FALSE);
        selectBoolean.validate(facesContext);

        // ValueChangedEvent should be fired in this case since the value
        // changed
        eventsItr = facesContext.getFacesEvents();
        assertTrue((eventsItr.hasNext()));
        Object eventObj = eventsItr.next();
        // make sure it is an instance of ValueChangedEvent
        assertTrue(eventObj instanceof ValueChangedEvent);

        // case 3: previous value false 
        // new value is true 
        // create a new FacesContext make sure we don't have any events
        // queued from previous test case.
        facesContext = new MockFacesContext();
        selectBoolean.setPrevious(selectBoolean.currentValue(facesContext));
        selectBoolean.setValue(Boolean.TRUE);
        selectBoolean.validate(facesContext);

        // ValueChangedEvent should be fired in this case since the value
        // changed
        eventsItr = facesContext.getFacesEvents();
        assertTrue((eventsItr.hasNext()));
        eventObj = eventsItr.next();
        // make sure it is an instance of ValueChangedEvent
        assertTrue(eventObj instanceof ValueChangedEvent);
    }
}
