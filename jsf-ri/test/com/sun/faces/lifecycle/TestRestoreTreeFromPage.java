/*
 * $Id: TestRestoreTreeFromPage.java,v 1.2 2002/09/10 21:02:46 visvan Exp $
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
 * @version $Id: TestRestoreTreeFromPage.java,v 1.2 2002/09/10 21:02:46 visvan Exp $
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
    "value=[Ljava.lang.Object;@18cb3a"
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
    "rO0ABXNyACFjb20uc3VuLmZhY2VzLnRyZWUuU2ltcGxlVHJlZUltcGzheXFtoEkKqAIAA0wAC3JlbmRlcktpdElkdAASTGphdmEvbGFuZy9TdHJpbmc7TAAEcm9vdHQAI0xqYXZheC9mYWNlcy9jb21wb25lbnQvVUlDb21wb25lbnQ7TAAGdHJlZUlkcQB+AAF4cgAVamF2YXguZmFjZXMudHJlZS5UcmVlPrWmwgkoaEwCAAB4cHQAB0RFRkFVTFRzcgAjY29tLnN1bi5mYWNlcy50cmVlLlNpbXBsZVRyZWVJbXBsJDGZzr1VeEns/QIAAUwABnRoaXMkMHQAI0xjb20vc3VuL2ZhY2VzL3RyZWUvU2ltcGxlVHJlZUltcGw7eHIAJWphdmF4LmZhY2VzLmNvbXBvbmVudC5VSUNvbXBvbmVudEJhc2Vw2MjMoGcw5wIABEwACmF0dHJpYnV0ZXN0ABNMamF2YS91dGlsL0hhc2hNYXA7TAAIY2hpbGRyZW50ABVMamF2YS91dGlsL0FycmF5TGlzdDtMAAhoYW5kbGVyc3EAfgAKTAAKdmFsaWRhdG9yc3EAfgAKeHBwc3IAE2phdmEudXRpbC5BcnJheUxpc3R4gdIdmcdhnQMAAUkABHNpemV4cAAAAAF3BAAAAApzcgAcamF2YXguZmFjZXMuY29tcG9uZW50LlVJRm9yba2fwszolcqLAgAAeHEAfgAIc3IAEWphdmEudXRpbC5IYXNoTWFwBQfawcMWYNEDAAJGAApsb2FkRmFjdG9ySQAJdGhyZXNob2xkeHA/QAAAAAAACHcIAAAACwAAAAR0AAtjb21wb25lbnRJZHQACWJhc2ljRm9ybXQADHJlbmRlcmVyVHlwZXQADEZvcm1SZW5kZXJlcnQABnBhcmVudHEAfgALdAAFdmFsdWVxAH4AE3hzcQB+AAwAAAAEdwQAAAAKc3IAH2phdmF4LmZhY2VzLmNvbXBvbmVudC5VSUNvbW1hbmQFceaY5Dr9KAIAAHhxAH4ACHNxAH4AED9AAAAAAAAIdwgAAAALAAAABnQADGNvbW1hbmRDbGFzc3QADmh5cGVybGlua0NsYXNzdAAFbGFiZWx0AAlsaW5rIHRleHRxAH4AEnQABGxpbmtxAH4AFHQAEUh5cGVybGlua1JlbmRlcmVycQB+ABZxAH4AD3QABnRhcmdldHQACmhlbGxvLmh0bWx4cHBwc3IAJWphdmF4LmZhY2VzLmNvbXBvbmVudC5VSVNlbGVjdEJvb2xlYW60h49ycA3cVwIAAHhyAB1qYXZheC5mYWNlcy5jb21wb25lbnQuVUlJbnB1dBYU3Lkb6Aj+AgAAeHEAfgAIc3EAfgAQP0AAAAAAAAh3CAAAAAsAAAAFdAASc2VsZWN0Ym9vbGVhbkNsYXNzcQB+AChxAH4AEnQACXZhbGlkVXNlcnEAfgAUdAAQQ2hlY2tib3hSZW5kZXJlcnEAfgAWcQB+AA9xAH4AF3NyABFqYXZhLmxhbmcuQm9vbGVhbs0gcoDVnPruAgABWgAFdmFsdWV4cAB4cHBwc3IAIWphdmF4LmZhY2VzLmNvbXBvbmVudC5VSVNlbGVjdE9uZYqGFVUh1XPyAgAAeHIAImphdmF4LmZhY2VzLmNvbXBvbmVudC5VSVNlbGVjdEJhc2WUHPvkhAlgVgIAAHhxAH4AJXNxAH4AED9AAAAAAAARdwgAAAAXAAAACXQAJGNvbS5zdW4uZmFjZXMuU0VMRUNUSVRFTVNfQ09ORklHVVJFRHEAfgAxdAAIdGFiaW5kZXh0AAIyMHEAfgASdAANYXBwbGVRdWFudGl0eXEAfgAUdAAPTGlzdGJveFJlbmRlcmVydAAFdGl0bGV0AA9TZWxlY3QgUXVhbnRpdHlxAH4AF3QAATRxAH4AFnEAfgAPdAAJYWNjZXNza2V5dAABTnQABHNpemV0AAE2eHNxAH4ADAAAAAN3BAAAAApzcgAiamF2YXguZmFjZXMuY29tcG9uZW50LlVJU2VsZWN0SXRlbfIXrhteXT2GAgAAeHEAfgAIc3EAfgAQP0AAAAAAAAh3CAAAAAsAAAAFcQB+ABJ0AAEwdAAJaXRlbUxhYmVscQB+AEF0AAhkaXNhYmxlZHQABHRydWVxAH4AFnEAfgAvdAAJaXRlbVZhbHVlcQB+AEF4cHBwc3EAfgA+c3EAfgAQP0AAAAAAAAh3CAAAAAsAAAAFcQB+ABJxAH4AOHEAfgBCcQB+ADhxAH4AFnEAfgAvcQB+AEVxAH4AOHEAfgA2dAAERm91cnhwcHBzcQB+AD5zcQB+ABA/QAAAAAAACHcIAAAACwAAAAVxAH4AEnQAATlxAH4AQnEAfgBLcQB+ABZxAH4AL3EAfgBFcQB+AEtxAH4ANnQABG5pbmV4cHBweHBwc3IAImphdmF4LmZhY2VzLmNvbXBvbmVudC5VSVNlbGVjdE1hbnnnImbn+6BJwQIAAHhxAH4ALnNxAH4AED9AAAAAAAAIdwgAAAALAAAABHEAfgASdAAKTWFueUFwcGxlc3EAfgAUdAAWU2VsZWN0TWFueU1lbnVSZW5kZXJlcnEAfgAWcQB+AA9xAH4AF3VyABNbTGphdmEubGFuZy5PYmplY3Q7kM5YnxBzKWwCAAB4cAAAAAJxAH4AOHQAATd4c3EAfgAMAAAAA3cEAAAACnNxAH4APnNxAH4AED9AAAAAAAAIdwgAAAALAAAABHEAfgAScQB+ADhxAH4AQnQABGZvdXJxAH4AFnEAfgBOcQB+AEVxAH4AOHhwcHBzcQB+AD5zcQB+ABA/QAAAAAAACHcIAAAACwAAAARxAH4AEnEAfgA8cQB+AEJ0AANzaXhxAH4AFnEAfgBOcQB+AEVxAH4APHhwcHBzcQB+AD5zcQB+ABA/QAAAAAAACHcIAAAACwAAAARxAH4AEnEAfgBUcQB+AEJ0AAVzZXZlbnEAfgAWcQB+AE5xAH4ARXEAfgBUeHBwcHhwcHhwcHhwcHEAfgAEdAASL1Rlc3RTYXZlU3RhdGUuanNw");

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
