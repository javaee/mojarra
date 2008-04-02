/*
 * $Id: UIInputTestCase.java,v 1.8 2003/03/13 01:12:40 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangedEvent;
import javax.faces.event.ValueChangedListener;
import javax.faces.validator.Validator;
import javax.faces.mock.MockFacesContext;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * <p>Test case for the <strong>javax.faces.UIInput</strong>
 * concrete class.</p>
 */

public class UIInputTestCase extends UIOutputTestCase {


    // ----------------------------------------------------- Instance Variables


    // ---------------------------------------------------------- Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public UIInputTestCase(String name) {
        super(name);
    }


    // -------------------------------------------------- Overall Test Methods
// This class is necessary since UIInput isn't a naming container by
// default.
private class UIInputNamingContainer extends UIInput implements NamingContainer {
    private UINamingContainer namingContainer = null;

    UIInputNamingContainer() {
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

        component = new UIInputNamingContainer();
        component.setComponentId("test");
        attributes = new String[0];
        rendererType = "Text";

    }


    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {

        return (new TestSuite(UIInputTestCase.class));

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

        assertEquals("componentType", UIInput.TYPE,
                     component.getComponentType());

    }


    /**
     * [3.1.7] Attribute/Property Transparency
     */
    public void testAttributePropertyTransparency() {

        super.testAttributePropertyTransparency();
        UIInput input = (UIInput) component;

    }


    // ----- ValueChangedEvents Tests -----

    public void testValueChangedEvent1() {

        // Add a single listener interested in a single phase
        UIInput input = (UIInput) component;
        ValueChangedEvent event = new ValueChangedEvent(input, "", "");
        TestValueChangedListener listener =
            new TestValueChangedListener(PhaseId.APPLY_REQUEST_VALUES);
        input.addValueChangedListener(listener);

        // Ensure that it is called only once
        assertEquals("Listener has not been called yet",
                     0, listener.getCount());
        input.broadcast(event, PhaseId.RECONSTITUTE_REQUEST);
        input.broadcast(event, PhaseId.APPLY_REQUEST_VALUES);
        input.broadcast(event, PhaseId.PROCESS_VALIDATIONS);
        input.broadcast(event, PhaseId.UPDATE_MODEL_VALUES);
        assertEquals("Listener was called exactly once",
                     1, listener.getCount());

    }


    public void testValueChangedEvent2() {

        // Add a single listener interested in all phases
        UIInput input = (UIInput) component;
        ValueChangedEvent event = new ValueChangedEvent(input, "", "");
        TestValueChangedListener listener =
            new TestValueChangedListener(PhaseId.ANY_PHASE);
        input.addValueChangedListener(listener);

        // Ensure that it is called only once
        assertEquals("Listener has not been called yet",
                     0, listener.getCount());
        input.broadcast(event, PhaseId.RECONSTITUTE_REQUEST);
        input.broadcast(event, PhaseId.APPLY_REQUEST_VALUES);
        input.broadcast(event, PhaseId.PROCESS_VALIDATIONS);
        input.broadcast(event, PhaseId.UPDATE_MODEL_VALUES);
        assertEquals("Listener was called exactly four times",
                     4, listener.getCount());

    }


    public void testValueChangedEvent3() {

        UIInput input = (UIInput) component;
        ValueChangedEvent event = new ValueChangedEvent(input, "", "");

        // No registered listeners at all
        if (input.broadcast(event, PhaseId.RECONSTITUTE_REQUEST)) {
            fail("(1) should have returned false");
        }

        // Retroactively added listener -- too late
        input.addValueChangedListener
                    (new TestValueChangedListener(PhaseId.RECONSTITUTE_REQUEST));
        if (input.broadcast(event, PhaseId.APPLY_REQUEST_VALUES)) {
            fail("(2) should have returned false");
        }

        // Future-phase listener should trigger true return
        input.addValueChangedListener
                    (new TestValueChangedListener(PhaseId.UPDATE_MODEL_VALUES));
        if (!input.broadcast(event, PhaseId.PROCESS_VALIDATIONS)) {
            fail("(3) should have returned true");
        }

        // Last-phase listener should trigger false return
        if (input.broadcast(event, PhaseId.UPDATE_MODEL_VALUES)) {
            fail("(4) should have returned false");
        }

    }
    
    public void testFireValueChangeEvents() {
        MockFacesContext facesContext = new MockFacesContext();
        
        // case 1: previous value null
        // new value is null
        // make sure ValueChangedEvent is not fired if new value is same
        // as the old value.
        UIInput input = (UIInput) component;
        input.setValue(null);
        input.setPrevious(input.currentValue(facesContext));
        input.validate(facesContext);
        
        // ValueChangedEvent should not be fired in this case since the value
        // didn't change.
        Iterator eventsItr = facesContext.getFacesEvents();
        assertTrue(!(eventsItr.hasNext()));
        
        // case 2: previous value null
        // new value is "New Value"
        input.setPrevious(input.currentValue(facesContext));
        input.setValue("New Value");
        input.validate(facesContext);
        // make sure ValueChangedEvent was fired since the value changed
        eventsItr = facesContext.getFacesEvents();
        assertTrue((eventsItr.hasNext()));
        Object eventObj = eventsItr.next();
        // make sure it is an instance of ValueChangedEvent
        assertTrue(eventObj instanceof ValueChangedEvent);
        
        // case 3: previous value "New Value"
        // new value is "New Value"
        // create a new FacesContext make sure we don't have any events 
        // queued from previous test case.
        input.setPrevious(input.currentValue(facesContext));
        facesContext = new MockFacesContext();
        input.setValue("New Value");
        input.validate(facesContext);
        
        // ValueChangedEvent should not be fired in this case since the value
        // didn't change.
        eventsItr = facesContext.getFacesEvents();
        assertTrue(!(eventsItr.hasNext()));
        
        // case 3: previous value "New Value"
        // new value is "Another Value"
        input.setPrevious(input.currentValue(facesContext));
        input.setValue("Another Value");
        input.validate(facesContext);
        // make sure ValueChangedEvent was fired since the value changed
        eventsItr = facesContext.getFacesEvents();
        assertTrue((eventsItr.hasNext()));
        eventObj = eventsItr.next();
        // make sure it is an instance of ValueChangedEvent
        assertTrue(eventObj instanceof ValueChangedEvent);
    }    
    
    public void testBroadCast() {
        // testBroadcast method that takes a ValueChangedEvent and List
        // as parameters
        ArrayList listenerList = null;
        ValueChangedEvent event = null;
        UIInput input = (UIInput) component;
        
        // null parameters are allowed and should not result in exception
        try {
            input.broadcast(event, listenerList);
        } catch (Exception e ) {
            assertTrue(false);
        }
        
        listenerList = new ArrayList(4);
        event = new ValueChangedEvent(input, "", "");
        TestValueChangedListener listener1 =
            new TestValueChangedListener(PhaseId.RECONSTITUTE_REQUEST);
        TestValueChangedListener listener2 =
            new TestValueChangedListener(PhaseId.PROCESS_VALIDATIONS);
        TestValueChangedListener listener3 =
            new TestValueChangedListener(PhaseId.UPDATE_MODEL_VALUES);
        TestValueChangedListener listener4 =
            new TestValueChangedListener(PhaseId.ANY_PHASE);

        listenerList.add(listener1);
        listenerList.add(listener2);
        listenerList.add(listener3);
        listenerList.add(listener4);
        input.broadcast(event, listenerList);
        assertEquals("Listener was called exactly once",
                     1, listener1.getCount());
        assertEquals("Listener was called exactly once",
                     1, listener2.getCount());
        assertEquals("Listener was called exactly once",
                     1, listener3.getCount());
        assertEquals("Listener was called exactly once",
                     1, listener4.getCount());
    }    
    
    public void testAddRemoveValueChangeListeners() {
        UIInput input = (UIInput) component;
       
        TestValueChangedListener listener1 =
            new TestValueChangedListener(PhaseId.RECONSTITUTE_REQUEST);
        TestValueChangedListener listener2 =
            new TestValueChangedListener(PhaseId.PROCESS_VALIDATIONS);
        TestValueChangedListener listener3 =
            new TestValueChangedListener(PhaseId.UPDATE_MODEL_VALUES);
        TestValueChangedListener listener4 =
            new TestValueChangedListener(PhaseId.ANY_PHASE);
        TestValueChangedListener listener5 =
            new TestValueChangedListener(PhaseId.PROCESS_VALIDATIONS);
        TestValueChangedListener listener6 =
            new TestValueChangedListener(PhaseId.UPDATE_MODEL_VALUES);
        
        // test addValueChangedListener
        // if the listener is null, make sure a null ptr exception is thrown
        boolean gotException=false;
        try {
            input.addValueChangedListener(null);
        } catch (Exception e ) {
            gotException = true;
        }
        assertTrue(gotException);
        
        // test addValueChangedListener works correctly if same listener 
        // instance is added more than once.
        input.addValueChangedListener(listener1);
        input.addValueChangedListener(listener2);
        input.addValueChangedListener(listener3);
        input.addValueChangedListener(listener2);
        input.addValueChangedListener(listener3);
        input.addValueChangedListener(listener4);
        input.addValueChangedListener(listener5);
        input.addValueChangedListener(listener6);
     
        assertTrue((getListenerIndex(input, listener1)) == 0);
        assertTrue((getListenerIndex(input, listener2)) == 0);
        assertTrue((getListenerIndex(input, listener3)) == 0);
        assertTrue((getListenerIndex(input, listener4)) == 0);
        assertTrue((getListenerIndex(input, listener5)) == 2);
        assertTrue((getListenerIndex(input, listener6)) == 2);
        
        // test removeValueChangedListener
        // if the listener is null, make sure a null ptr exception is thrown
        gotException=false;
        try {
            input.removeValueChangedListener(null);
        } catch (Exception e ) {
            gotException = true;
        }
        assertTrue(gotException);
        
        input.removeValueChangedListener(listener1);
        input.removeValueChangedListener(listener2);
        input.removeValueChangedListener(listener3);
        input.removeValueChangedListener(listener4);
        input.removeValueChangedListener(listener5);
        input.removeValueChangedListener(listener6);
     
        assertTrue((getListenerIndex(input, listener1)) == -1);
        // listener2 and listener3 should still exist since two instances
        // were added
        assertTrue((getListenerIndex(input, listener2)) == 0);
        assertTrue((getListenerIndex(input, listener3)) == 0);
        assertTrue((getListenerIndex(input, listener4)) == -1);
        assertTrue((getListenerIndex(input, listener5)) == -1);
        assertTrue((getListenerIndex(input, listener6)) == -1);
        
        input.removeValueChangedListener(listener2);
        input.removeValueChangedListener(listener3);
        assertTrue((getListenerIndex(input, listener2)) == -1);
        assertTrue((getListenerIndex(input, listener3)) == -1);
        
    }    
    
    protected int getListenerIndex(UIInput input,
            TestValueChangedListener listener) {
        int ordinal = listener.getPhaseId().getOrdinal();
        return (input.listeners[ordinal].indexOf(listener));
    }     
}
