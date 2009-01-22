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

}
