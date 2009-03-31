/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javax.faces.component;

import com.sun.faces.mock.MockFacesContext;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import javax.faces.event.ValueChangeListener;
import javax.faces.event.FacesListener;
import javax.faces.context.FacesContext;
import javax.faces.convert.DateTimeConverter;
import javax.faces.convert.Converter;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author edburns
 */
public class UIComponentBaseAttachedStateTestCase extends TestCase {
    
    private UIComponentBase component;
    private MockFacesContext facesContext = null;


    public UIComponentBaseAttachedStateTestCase(String arg0) {
        super(arg0);
    }
    
    // Return the tests included in this test case.
    public static Test suite() {
        return (new TestSuite(UIComponentBaseAttachedStateTestCase.class));
    }
    
    public void setUp() throws Exception {
        super.setUp();
        component = new UIOutput();
        facesContext = new MockFacesContext();
    }
    
    public void testAttachedObjectsSet() throws Exception {
        Set<ValueChangeListener> returnedAttachedObjects = null,
                attachedObjects = new HashSet<ValueChangeListener>();
        ValueChangeListener toAdd = new TestValueChangeListener();
        attachedObjects.add(toAdd);
        toAdd = new TestValueChangeListener();
        attachedObjects.add(toAdd);        
        toAdd = new TestValueChangeListener();
        attachedObjects.add(toAdd);        
        Object result = UIComponentBase.saveAttachedState(facesContext, attachedObjects); 
        returnedAttachedObjects = (Set<ValueChangeListener>) 
                UIComponentBase.restoreAttachedState(facesContext, result);
        
    }

    public void testAttachedObjectsStack() throws Exception {
        Stack<ValueChangeListener> returnedAttachedObjects = null,
                attachedObjects = new Stack<ValueChangeListener>();
        ValueChangeListener toAdd = new TestValueChangeListener();
        attachedObjects.add(toAdd);
        toAdd = new TestValueChangeListener();
        attachedObjects.add(toAdd);        
        toAdd = new TestValueChangeListener();
        attachedObjects.add(toAdd);        
        Object result = UIComponentBase.saveAttachedState(facesContext, attachedObjects); 
        returnedAttachedObjects = (Stack<ValueChangeListener>)
                UIComponentBase.restoreAttachedState(facesContext, result);
    }


    // Regression test for bug #907
    public void testAttachedObjectsCount() throws Exception {
        Set<ValueChangeListener> returnedAttachedObjects = null,
                attachedObjects = new HashSet<ValueChangeListener>();
        ValueChangeListener toAdd = new TestValueChangeListener();
        attachedObjects.add(toAdd);
        toAdd = new TestValueChangeListener();
        attachedObjects.add(toAdd);
        toAdd = new TestValueChangeListener();
        attachedObjects.add(toAdd);
        Object result = UIComponentBase.saveAttachedState(facesContext, attachedObjects);
        returnedAttachedObjects = (Set<ValueChangeListener>)
                UIComponentBase.restoreAttachedState(facesContext, result);
        int firstSize = returnedAttachedObjects.size();
        returnedAttachedObjects = (Set<ValueChangeListener>)
                UIComponentBase.restoreAttachedState(facesContext, result);
        int secondSize = returnedAttachedObjects.size();
        assertEquals(firstSize,secondSize);
    }


    public void testFacesListenerState() {

        UIComponent component = new UIOutput();
        TestFacesListener listener = new TestFacesListener();
        listener.setValue("initial");
        component.addFacesListener(listener);
        component.markInitialState();
        assertTrue(component.initialStateMarked());
        assertTrue(listener.initialStateMarked());

        Object state = component.saveState(facesContext);
        assertNull(state);

        component = new UIOutput();
        listener = new TestFacesListener();
        component.addFacesListener(listener);
        listener.setValue("initial");
        component.markInitialState();
        listener.setValue("newvalue");
        state = component.saveState(facesContext);
        assertNotNull(state);

        // verify that state is applied to existing Listener instances.
        component = new UIOutput();
        listener = new TestFacesListener();
        component.addFacesListener(listener);
        listener.setValue("newinitial");
        component.restoreState(facesContext, state);
        assertTrue("newvalue".equals(listener.getValue()));

        // verify listeners are overwritten when using full state saving
        component = new UIOutput();
        listener = new TestFacesListener();
        component.addFacesListener(listener);
        listener.setValue("initial");
        state = component.saveState(facesContext);
        assertNotNull(state);
        listener.setValue("postsave");

        component.restoreState(facesContext, state);
        TestFacesListener l = (TestFacesListener) component.getFacesListeners(TestFacesListener.class)[0];
        assertTrue(l != listener);
        assertTrue("initial".equals(l.getValue()));
        
    }


    // ---------------------------------------------------------- Nested Classes


    public static final class TestFacesListener implements FacesListener, PartialStateHolder {

        private boolean initialState;
        private String value;
        private boolean trans;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            clearInitialState();
            this.value = value;
        }

        public void markInitialState() {
            initialState = true;
        }

        public boolean initialStateMarked() {
            return initialState;
        }

        public void clearInitialState() {
            initialState = false;
        }

        public Object saveState(FacesContext context) {

            return ((!initialState) ? new Object[] { value } : null);

        }

        public void restoreState(FacesContext context, Object state) {

            if (state != null) {
                Object[] values = (Object[]) state;
                value = (String) values[0];
            }

        }

        public boolean isTransient() {
            return trans;
        }

        public void setTransient(boolean trans) {
            this.trans = trans;
        }
    }

}
