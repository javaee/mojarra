/*
 * $Id: StateHolderSaverTestCase.java,v 1.3 2004/01/27 20:30:03 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

import javax.faces.convert.IntegerConverter;


public class StateHolderSaverTestCase extends UIComponentBaseTestCase {


    // ------------------------------------------------------ Instance Variables

    // ------------------------------------------------------------ Constructors


    // Construct a new instance of this test case.
    public StateHolderSaverTestCase(String name) {
        super(name);
    }


    // ---------------------------------------------------- Overall Test Methods

    // Return the tests included in this test case.
    public static Test suite() {

        return (new TestSuite(StateHolderSaverTestCase.class));

    }

    // ------------------------------------------------- Individual Test Methods

    public void testLifecycleManagement() { }


    public void testChildrenRecursive() {}


    public void testComponentReconnect() {}


    public void testComponentRemoval() {}


    public void testStateHolder() throws Exception {}


    public void testValueBindings() {}


    public void testImplementsStateHolder() throws Exception {
	StateHolderSaver saver = null;
	UIInput 
	    postSave,
	    preSave = new UIInput();
	preSave.setId("id1");
	preSave.setRendererType(null);
	
	saver = new StateHolderSaver(facesContext, preSave);
	postSave = (UIInput) saver.restore(facesContext);
	assertEquals(postSave.getId(), preSave.getId());
    }

    public void testImplementsSerializable() throws Exception {
	StateHolderSaver saver = null;
	String 
	    preSave = "hello",
	    postSave = null;

	saver = new StateHolderSaver(facesContext, preSave);
	postSave = (String) saver.restore(facesContext);
	assertTrue(preSave.equals(postSave));
    }

    public void testImplementsNeither() throws Exception {
	StateHolderSaver saver = null;
	IntegerConverter  
	    preSave = new IntegerConverter(),
	    postSave = null;

	saver = new StateHolderSaver(facesContext, preSave);
	postSave = (IntegerConverter) saver.restore(facesContext);
	assertTrue(true); // lack of ClassCastException
    }


}
