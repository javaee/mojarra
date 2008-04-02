/*
 * $Id: StateHolderSaverTestCase.java,v 1.6 2005/08/22 22:08:14 ofung Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
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
