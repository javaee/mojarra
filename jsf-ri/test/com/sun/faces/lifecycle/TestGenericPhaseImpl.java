/*
 * $Id: TestGenericPhaseImpl.java,v 1.1 2002/06/01 00:58:23 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
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
import javax.faces.lifecycle.PhaseListener;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;

import java.util.Iterator;

import com.sun.faces.FacesContextTestCase;
import com.sun.faces.CompareFiles;
import com.sun.faces.lifecycle.LifecycleCallback;

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
 * @version $Id: TestGenericPhaseImpl.java,v 1.1 2002/06/01 00:58:23 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestGenericPhaseImpl extends FacesContextTestCase
{
//
// Protected Constants
//

public static final String TEST_URI_XUL = "/Faces_Basic.xul";

public static final String PATH_ROOT = "./build/test/servers/tomcat40/webapps/test/";

public static final String OUTPUT_FILENAME = PATH_ROOT +
    "GenericPhase_out";

public static final String CORRECT_OUTPUT_FILENAME = PATH_ROOT +
    "GenericPhase_correct";

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
    theRequest.addParameter("tree", TEST_URI_XUL);
}

public void testExecute()
{
    int result = -1;
    final PrintStream out;
    File file = null;
    FileOutputStream fs = null;

    Phase createTree = null;

    try {
        file = new File ( OUTPUT_FILENAME );
	fs = new FileOutputStream(file);
    } catch ( Exception e ) {
        assertTrue(false);
    }
    out = new PrintStream(fs);

    createTree = new CreateRequestTreePhase(null, 
					 Lifecycle.CREATE_REQUEST_TREE_PHASE) {
         public int execute(FacesContext facesContext) throws FacesException {
	     int rc = -1;
	     // this builds the tree into the facesContext
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
	result = createTree.execute(facesContext);
    }
    catch (Throwable e) {
	System.out.println("Throwable: " + e.getMessage());
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
