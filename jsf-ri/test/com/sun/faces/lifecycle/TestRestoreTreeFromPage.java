/*
 * $Id: TestRestoreTreeFromPage.java,v 1.3 2002/09/20 21:14:41 jvisvanathan Exp $
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
import java.util.ArrayList;
import java.util.List;
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
 * @version $Id: TestRestoreTreeFromPage.java,v 1.3 2002/09/20 21:14:41 jvisvanathan Exp $
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
public static final String TEST_URI = "/TestSaveState.jsp";
public static final String  RESTORE_TREE_OUTPUT_FILE = FileOutputResponseWriter.FACES_RESPONSE_ROOT + 
    "RestoreTree_output";
public static final String  RESTORE_TREE_CORRECT_FILE = FileOutputResponseWriter.FACES_RESPONSE_ROOT + 
    "RestoreTree_correct";

public static final String ignore[] = {
  "value=[Ljava.lang.Object;@54d66b"
};
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
    "rO0ABXNyACFjb20uc3VuLmZhY2VzLnRyZWUuU2ltcGxlVHJlZUltcGzheXFtoEkKqAIAA0wAC3JlbmRlcktpdElkdAASTGphdmEvbGFuZy9TdHJpbmc7TAAEcm9vdHQAI0xqYXZheC9mYWNlcy9jb21wb25lbnQvVUlDb21wb25lbnQ7TAAGdHJlZUlkcQB+AAF4cgAVamF2YXguZmFjZXMudHJlZS5UcmVlPrWmwgkoaEwCAAB4cHQAB0RFRkFVTFRzcgAjY29tLnN1bi5mYWNlcy50cmVlLlNpbXBsZVRyZWVJbXBsJDGZzr1VeEns/QIAAUwABnRoaXMkMHQAI0xjb20vc3VuL2ZhY2VzL3RyZWUvU2ltcGxlVHJlZUltcGw7eHIAJWphdmF4LmZhY2VzLmNvbXBvbmVudC5VSUNvbXBvbmVudEJhc2VLmeHIbzQDogIABEwACmF0dHJpYnV0ZXN0ABNMamF2YS91dGlsL0hhc2hNYXA7TAAIY2hpbGRyZW50ABVMamF2YS91dGlsL0FycmF5TGlzdDtMAAhoYW5kbGVyc3EAfgAKTAAKdmFsaWRhdG9yc3EAfgAKeHBwc3IAE2phdmEudXRpbC5BcnJheUxpc3R4gdIdmcdhnQMAAUkABHNpemV4cAAAAAF3BAAAAApzcgAcamF2YXguZmFjZXMuY29tcG9uZW50LlVJRm9yba2fwszolcqLAgAAeHEAfgAIc3IAEWphdmEudXRpbC5IYXNoTWFwBQfawcMWYNEDAAJGAApsb2FkRmFjdG9ySQAJdGhyZXNob2xkeHA/QAAAAAAACHcIAAAACwAAAAR0AAtjb21wb25lbnRJZHQACWJhc2ljRm9ybXQADHJlbmRlcmVyVHlwZXQADEZvcm1SZW5kZXJlcnQABnBhcmVudHEAfgALdAAFdmFsdWVxAH4AE3hzcQB+AAwAAAAFdwQAAAAKc3IAHWphdmF4LmZhY2VzLmNvbXBvbmVudC5VSUlucHV0q8kW2FmUtRgCAAB4cQB+AAhzcQB+ABA/QAAAAAAACHcIAAAACwAAAAZ0AC1qYXZheC5mYWNlcy52YWxpZGF0b3IuTGVuZ3RoVmFsaWRhdG9yLk1BWElNVU10AAIxMHQALWphdmF4LmZhY2VzLnZhbGlkYXRvci5MZW5ndGhWYWxpZGF0b3IuTUlOSU1VTXQAATZxAH4AEnQACHVzZXJOYW1lcQB+ABR0AAxUZXh0UmVuZGVyZXJxAH4AFnEAfgAPcQB+ABd0AANKU0Z4cHBzcQB+AAwAAAACdwQAAAAKc3IAJWphdmF4LmZhY2VzLnZhbGlkYXRvci5MZW5ndGhWYWxpZGF0b3ITmN0DktNpjwIABEkAB21heGltdW1aAAptYXhpbXVtU2V0SQAHbWluaW11bVoACm1pbmltdW1TZXR4cgAjamF2YXguZmFjZXMudmFsaWRhdG9yLlZhbGlkYXRvckJhc2WJ4Bs3HAV7BQIAAHhwAAAAAAAAAAAAAHNyACdqYXZheC5mYWNlcy52YWxpZGF0b3IuUmVxdWlyZWRWYWxpZGF0b3I36MvQ5If0/gIAAHhxAH4AJXhzcgAfamF2YXguZmFjZXMuY29tcG9uZW50LlVJQ29tbWFuZPTrEssUCDfTAgAAeHEAfgAIc3EAfgAQP0AAAAAAAAh3CAAAAAsAAAAGdAAMY29tbWFuZENsYXNzdAAOaHlwZXJsaW5rQ2xhc3N0AAVsYWJlbHQACWxpbmsgdGV4dHEAfgASdAAEbGlua3EAfgAUdAARSHlwZXJsaW5rUmVuZGVyZXJxAH4AFnEAfgAPdAAGdGFyZ2V0dAAKaGVsbG8uaHRtbHhwcHBzcgAlamF2YXguZmFjZXMuY29tcG9uZW50LlVJU2VsZWN0Qm9vbGVhbiiDmwtDyLEhAgAAeHEAfgAZc3EAfgAQP0AAAAAAAAh3CAAAAAsAAAAFdAASc2VsZWN0Ym9vbGVhbkNsYXNzcQB+ADdxAH4AEnQACXZhbGlkVXNlcnEAfgAUdAAQQ2hlY2tib3hSZW5kZXJlcnEAfgAWcQB+AA9xAH4AF3NyABFqYXZhLmxhbmcuQm9vbGVhbs0gcoDVnPruAgABWgAFdmFsdWV4cAB4cHBwc3IAIWphdmF4LmZhY2VzLmNvbXBvbmVudC5VSVNlbGVjdE9uZYr0Fd9uYLMSAgAAeHIAImphdmF4LmZhY2VzLmNvbXBvbmVudC5VSVNlbGVjdEJhc2WUHPvkhAlgVgIAAHhxAH4AGXNxAH4AED9AAAAAAAARdwgAAAAXAAAACXQAJGNvbS5zdW4uZmFjZXMuU0VMRUNUSVRFTVNfQ09ORklHVVJFRHEAfgBAdAAIdGFiaW5kZXh0AAIyMHEAfgASdAANYXBwbGVRdWFudGl0eXEAfgAUdAAPTGlzdGJveFJlbmRlcmVydAAFdGl0bGV0AA9TZWxlY3QgUXVhbnRpdHlxAH4AF3QAATRxAH4AFnEAfgAPdAAJYWNjZXNza2V5dAABTnQABHNpemV0AAEzeHNxAH4ADAAAAAN3BAAAAApzcgAiamF2YXguZmFjZXMuY29tcG9uZW50LlVJU2VsZWN0SXRlbWQCox8xJbjDAgAAeHEAfgAIc3EAfgAQP0AAAAAAAAh3CAAAAAsAAAAFcQB+ABJ0AAEwdAAJaXRlbUxhYmVscQB+AFB0AAhkaXNhYmxlZHQABHRydWVxAH4AFnEAfgA+dAAJaXRlbVZhbHVlcQB+AFB4cHBwc3EAfgBNc3EAfgAQP0AAAAAAAAh3CAAAAAsAAAAFcQB+ABJxAH4AR3EAfgBRcQB+AEdxAH4AFnEAfgA+cQB+AFRxAH4AR3EAfgBFdAAERm91cnhwcHBzcQB+AE1zcQB+ABA/QAAAAAAACHcIAAAACwAAAAVxAH4AEnQAATlxAH4AUXEAfgBacQB+ABZxAH4APnEAfgBUcQB+AFpxAH4ARXQABG5pbmV4cHBweHBwc3IAImphdmF4LmZhY2VzLmNvbXBvbmVudC5VSVNlbGVjdE1hbnlGLmInuaTTUwIAAHhxAH4APXNxAH4AED9AAAAAAAAIdwgAAAALAAAABXEAfgASdAAKTWFueUFwcGxlc3EAfgBKdAABMXEAfgAUdAAMTWVudVJlbmRlcmVycQB+ABZxAH4AD3EAfgAXdXIAE1tMamF2YS5sYW5nLk9iamVjdDuQzlifEHMpbAIAAHhwAAAAAnEAfgBHdAABN3hzcQB+AAwAAAADdwQAAAAKc3EAfgBNc3EAfgAQP0AAAAAAAAh3CAAAAAsAAAAEcQB+ABJxAH4AR3EAfgBRdAAEZm91cnEAfgAWcQB+AF1xAH4AVHEAfgBHeHBwcHNxAH4ATXNxAH4AED9AAAAAAAAIdwgAAAALAAAABHEAfgAScQB+AB9xAH4AUXQAA3NpeHEAfgAWcQB+AF1xAH4AVHEAfgAfeHBwcHNxAH4ATXNxAH4AED9AAAAAAAAIdwgAAAALAAAABHEAfgAScQB+AGRxAH4AUXQABXNldmVucQB+ABZxAH4AXXEAfgBUcQB+AGR4cHBweHBweHBweHBwcQB+AAR0ABIvVGVzdFNhdmVTdGF0ZS5qc3A=");

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
        
        List ignoreList = new ArrayList();
	for (int i = 0; i < ignore.length; i++) {
	    ignoreList.add(ignore[i]);
	}
	boolean status = cf.filesIdentical(RESTORE_TREE_OUTPUT_FILE, 
                RESTORE_TREE_CORRECT_FILE, ignoreList);
        assertTrue(status);
    }
    catch (IOException e) {
	System.out.println(e.getMessage());
	e.printStackTrace();
    }

}

} // end of class TestReconstituteRequestTreePhase
