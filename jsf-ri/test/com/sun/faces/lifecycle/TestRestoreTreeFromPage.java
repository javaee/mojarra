/*
 * $Id: TestRestoreTreeFromPage.java,v 1.1 2002/08/06 18:27:22 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestRestoreTreeFromPage.java

package com.sun.faces.lifecycle;

import org.apache.cactus.WebRequest;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.lifecycle.Phase;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.component.UIComponent;

import javax.faces.tree.TreeFactory;
import javax.faces.tree.Tree;
import com.sun.faces.RIConstants;

import com.sun.faces.ServletFacesTestCase;
import javax.servlet.http.HttpSession;
import javax.faces.render.RenderKitFactory;
import java.util.Locale;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.IOException;
import com.sun.faces.CompareFiles;
import com.sun.faces.FileOutputResponseWriter;

/**
 *
 *  <B>TestReconstituteRequestTreePhase</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestRestoreTreeFromPage.java,v 1.1 2002/08/06 18:27:22 jvisvanathan Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestRestoreTreeFromPage extends ServletFacesTestCase
{
//
// Protected Constants
//

//
// Class Variables
//

//
// Instance Variables
//
public static final String TEST_URI = "/components.jsp";
public static final String  RESTORE_TREE_OUTPUT_FILE = FileOutputResponseWriter.FACES_RESPONSE_ROOT + 
    "RestoreTree_output";
public static final String  RESTORE_TREE_CORRECT_FILE = FileOutputResponseWriter.FACES_RESPONSE_ROOT + 
    "RestoreTree_correct";

// Attribute Instance Variables

// Relationship Instance Variables

//
// Constructors and Initializers    
//

    public TestRestoreTreeFromPage() {
	super("TestRestoreTreeFromPage");
    }

    public TestRestoreTreeFromPage(String name) {
	super(name);
    }

//
// Class methods
//

//
// General Methods
//

public void beginRestoreTreeFromPage(WebRequest theRequest)
{
    theRequest.setURL("localhost:8080", null, null, TEST_URI, null);
    theRequest.addParameter("com.sun.faces.TREE", 
    "rO0ABXNyACFjb20uc3VuLmZhY2VzLnRyZWUuU2ltcGxlVHJlZUltcGzheXFtoEkKqAIAA0wAC3JlbmRlcktpdElkdAASTGphdmEvbGFuZy9TdHJpbmc7TAAEcm9vdHQAI0xqYXZheC9mYWNlcy9jb21wb25lbnQvVUlDb21wb25lbnQ7TAAGdHJlZUlkcQB+AAF4cgAVamF2YXguZmFjZXMudHJlZS5UcmVlPrWmwgkoaEwCAAB4cHQAB0RFRkFVTFRzcgAjY29tLnN1bi5mYWNlcy50cmVlLlNpbXBsZVRyZWVJbXBsJDGZzr1VeEns/QIAAUwABnRoaXMkMHQAI0xjb20vc3VuL2ZhY2VzL3RyZWUvU2ltcGxlVHJlZUltcGw7eHIAJWphdmF4LmZhY2VzLmNvbXBvbmVudC5VSUNvbXBvbmVudEJhc2Vw2MjMoGcw5wIABEwACmF0dHJpYnV0ZXN0ABNMamF2YS91dGlsL0hhc2hNYXA7TAAIY2hpbGRyZW50ABVMamF2YS91dGlsL0FycmF5TGlzdDtMAAhoYW5kbGVyc3EAfgAKTAAKdmFsaWRhdG9yc3EAfgAKeHBwc3IAE2phdmEudXRpbC5BcnJheUxpc3R4gdIdmcdhnQMAAUkABHNpemV4cAAAAAF3BAAAAApzcgAcamF2YXguZmFjZXMuY29tcG9uZW50LlVJRm9ybYTKJXxAIUngAgAAeHEAfgAIc3IAEWphdmEudXRpbC5IYXNoTWFwBQfawcMWYNEDAAJGAApsb2FkRmFjdG9ySQAJdGhyZXNob2xkeHA/QAAAAAAACHcIAAAACwAAAAR0AAtjb21wb25lbnRJZHQACWJhc2ljRm9ybXQADHJlbmRlcmVyVHlwZXQADEZvcm1SZW5kZXJlcnQABnBhcmVudHEAfgALdAAFdmFsdWVxAH4AE3hzcQB+AAwAAAAHdwQAAAAKc3IAIWphdmF4LmZhY2VzLmNvbXBvbmVudC5VSVRleHRFbnRyeciVXbrTnjwNAgAAeHEAfgAIc3EAfgAQP0AAAAAAAAh3CAAAAAsAAAAEcQB+ABJ0AAh1c2VyTmFtZXEAfgAUdAANSW5wdXRSZW5kZXJlcnEAfgAWcQB+AA9xAH4AF3QAEERlZmF1bHRfdXNlcm5hbWV4cHBwc3EAfgAZc3EAfgAQP0AAAAAAAAh3CAAAAAsAAAAEcQB+ABJ0AAhwYXNzd29yZHEAfgAUdAAOU2VjcmV0UmVuZGVyZXJxAH4AFnEAfgAPcQB+ABd0ABBEZWZhdWx0X3Bhc3N3b3JkeHBwcHNyAB9qYXZheC5mYWNlcy5jb21wb25lbnQuVUlDb21tYW5kGG8IkFapi2gCAAB4cQB+AAhzcQB+ABA/QAAAAAAACHcIAAAACwAAAAV0AAVsYWJlbHQABUxvZ2lucQB+ABJ0AAVsb2dpbnEAfgAUdAAOQnV0dG9uUmVuZGVyZXJxAH4AFnEAfgAPcQB+ABdxAH4AKXhwcHBzcQB+ACRzcQB+ABA/QAAAAAAACHcIAAAACwAAAAVxAH4AJ3QACWxpbmsgdGV4dHEAfgASdAAEbGlua3EAfgAUdAARSHlwZXJsaW5rUmVuZGVyZXJxAH4AFnEAfgAPdAAGdGFyZ2V0dAAKaGVsbG8uaHRtbHhwcHBzcgAeamF2YXguZmFjZXMuY29tcG9uZW50LlVJT3V0cHV09U24Sh49nl0CAAB4cQB+AAhzcQB+ABA/QAAAAAAACHcIAAAACwAAAARxAH4AEnQACXVzZXJMYWJlbHEAfgAUdAAMVGV4dFJlbmRlcmVycQB+ABZxAH4AD3EAfgAXdAALT3V0cHV0IFRleHR4cHBwc3IAJWphdmF4LmZhY2VzLmNvbXBvbmVudC5VSVNlbGVjdEJvb2xlYW60h49ycA3cVwIAAHhxAH4ACHNxAH4AED9AAAAAAAAIdwgAAAALAAAABXEAfgAndAAKVmFsaWQgVXNlcnEAfgASdAAJdmFsaWRVc2VycQB+ABR0ABBDaGVja2JveFJlbmRlcmVycQB+ABZxAH4AD3EAfgAXc3IAEWphdmEubGFuZy5Cb29sZWFuzSBygNWc+u4CAAFaAAV2YWx1ZXhwAXhwcHBzcQB+ABlzcQB+ABA/QAAAAAAACHcIAAAACwAAAAZxAH4AEnQAB2FkZHJlc3NxAH4AFHQAEFRleHRBcmVhUmVuZGVyZXJxAH4AFnEAfgAPdAAEcm93c3QAAjEwcQB+ABd0AAhIaSBUaGVyZXQABGNvbHNxAH4ARXhwcHB4cHB4cHBxAH4ABHQAEi9UZXN0U2F2ZVN0YXRlLmpzcA==");

}


public void testRestoreTreeFromPage()
{
    Phase reconstituteTree = new ReconstituteRequestTreePhase(null, 
			Lifecycle.RECONSTITUTE_REQUEST_TREE_PHASE);
    int result = -1;

    try {
	result = reconstituteTree.execute(getFacesContext());
    }
    catch (Throwable e) {
        e.printStackTrace();
	assertTrue(false);
    }
    assertTrue(Phase.GOTO_NEXT == result);

    assertTrue(null != getFacesContext().getRequestTree());
    assertTrue(null != getFacesContext().getRequestTree().getRoot());
    assertTrue(RenderKitFactory.DEFAULT_RENDER_KIT.equals(
           getFacesContext().getRequestTree().getRenderKitId()));
      
    CompareFiles cf = new CompareFiles();
    try {
        FileOutputStream os = new FileOutputStream(RESTORE_TREE_OUTPUT_FILE);
        PrintStream ps = new PrintStream(os);
        com.sun.faces.util.DebugUtil.printTree((getFacesContext().getRequestTree()).getRoot(), ps );
        
	boolean status = cf.filesIdentical(RESTORE_TREE_OUTPUT_FILE, 
                RESTORE_TREE_CORRECT_FILE, null);
        assertTrue(status);
    }
    catch (IOException e) {
	System.out.println(e.getMessage());
	e.printStackTrace();
    }

}

} // end of class TestReconstituteRequestTreePhase
