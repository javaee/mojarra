/*
 * $Id: TestGenericPhaseImpl.java,v 1.10 2003/02/20 22:49:56 ofung Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestGenericPhaseImpl.java

package com.sun.faces.lifecycle;

import org.apache.cactus.WebRequest;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.ApplicationHandler;
import javax.faces.lifecycle.Phase;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIInput;

import java.util.Iterator;

import com.sun.faces.ServletFacesTestCase;
import com.sun.faces.CompareFiles;
import com.sun.faces.FileOutputResponseWrapper;
import com.sun.faces.lifecycle.LifecycleCallback;
import com.sun.faces.RIConstants;
import java.io.PrintStream;
import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;


/**
 *
 *  <B>TestGenericPhaseImpl</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestGenericPhaseImpl.java,v 1.10 2003/02/20 22:49:56 ofung Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestGenericPhaseImpl extends ServletFacesTestCase
{
//
// Protected Constants
//

public static final String TEST_URI = "/components.jsp";

public static final String OUTPUT_FILENAME = 
    FileOutputResponseWrapper.FACES_RESPONSE_ROOT + "GenericPhase_out";

public static final String CORRECT_OUTPUT_FILENAME = 
    FileOutputResponseWrapper.FACES_RESPONSE_ROOT + "GenericPhase_correct";

//
// Class Variables
//

//
// Instance Variables
//

// Attribute Instance Variables

// Relationship Instance Variables

//
// Constructors and Initializers    
//

    public TestGenericPhaseImpl() {super("TestGenericPhaseImpl");}
    public TestGenericPhaseImpl(String name) {super(name);}

//
// Class methods
//

//
// General Methods
//

public void beginExecute(WebRequest theRequest)
{
    theRequest.setURL("localhost:8080", null, null, TEST_URI, null);
    theRequest.addParameter("userName", "jerry");
}

public void testExecute()
{
    int result = -1;
    final PrintStream out;
    File file = null;
    FileOutputStream fs = null;

    try {
        file = new File ( OUTPUT_FILENAME );
        fs = new FileOutputStream(file);
    } catch ( Exception e ) {
        assertTrue(false);
    }
    out = new PrintStream(fs);

    Phase
        reconstituteTree = new ReconstituteRequestTreePhase(null,
            RIConstants.RECONSTITUTE_REQUEST_TREE_PHASE);
    try {
        result = reconstituteTree.execute(getFacesContext());
    }
    catch (Throwable e) {
        e.printStackTrace();
        assertTrue(false);
    }
    assertTrue(Phase.GOTO_NEXT == result);
    assertTrue(null != getFacesContext().getTree());

    // 2. Add components to tree
    //
    UIComponent root = getFacesContext().getTree().getRoot();
    UIForm basicForm = new UIForm();
    basicForm.setComponentId("basicForm");
    UIInput userName = new UIInput();
    userName.setComponentId("userName");
    root.addChild(basicForm);
    basicForm.addChild(userName);

    Phase applyValues = new ApplyRequestValuesPhase(null, 
        RIConstants.APPLY_REQUEST_VALUES_PHASE) {
            public int execute(FacesContext facesContext) 
                throws FacesException {
	        int rc = -1;
	        rc = super.execute(facesContext);
	        this.setLifecycleCallback(new LifecycleCallback() {
		    public int takeActionOnComponent(FacesContext context,
                        UIComponent component) throws FacesException {
		        out.println("GenericPhaseImpl: takeActionOnComponent: "+
		            component.getComponentId());
		       return Phase.GOTO_NEXT;
		   }
		 });

	     rc = traverseTreeInvokingCallback(facesContext);

	     return rc;
	 }
	};

    try {
	result = applyValues.execute(getFacesContext());
    }
    catch (Throwable e) {
	System.out.println("Throwable: " + e.getMessage());
	e.printStackTrace();
	assertTrue(false);
    }
    assertTrue(Phase.GOTO_NEXT == result);

    out.close();

    try {
	CompareFiles cf = new CompareFiles();
	assertTrue(cf.filesIdentical(OUTPUT_FILENAME, CORRECT_OUTPUT_FILENAME, 
				     null));
    } catch (Throwable e ) {
	System.out.println("Throwable: " + e.getMessage());
	assertTrue(false);
    }


}

} // end of class TestGenericPhaseImpl
