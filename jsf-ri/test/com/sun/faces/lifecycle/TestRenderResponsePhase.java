/*
 * $Id: TestRenderResponsePhase.java,v 1.76 2004/02/06 18:56:58 rlubke Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestRenderResponsePhase.java

package com.sun.faces.lifecycle;

import com.sun.faces.JspFacesTestCase;
import org.apache.cactus.JspTestCase;
import org.apache.cactus.WebRequest;

import javax.faces.FacesException;
import javax.faces.component.UIViewRoot;

/**
 * <B>TestRenderResponsePhase</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestRenderResponsePhase.java,v 1.76 2004/02/06 18:56:58 rlubke Exp $
 * @see	Blah
 * @see	Bloo
 */

public class TestRenderResponsePhase extends JspFacesTestCase {

//
// Protected Constants
//

    public static final String TEST_URI = "/TestRenderResponsePhase.jsp";


    public String getExpectedOutputFilename() {
        return "RenderResponse_correct";
    }


    public static final String ignore[] = {
    };


    public String[] getLinesToIgnore() {
        return ignore;
    }


    public boolean sendResponseToFile() {
        return true;
    }


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

    public TestRenderResponsePhase() {
        super("TestRenderResponsePhase");
    }


    public TestRenderResponsePhase(String name) {
        super(name);
    }

//
// Class methods
//

//
// General Methods
//


    public void beginHtmlBasicRenderKit(WebRequest theRequest) {
        theRequest.setURL("localhost:8080", "/test", "/faces", TEST_URI, null);
    }


    public void testHtmlBasicRenderKit() {


        boolean result = false;
        String value = null;
        LifecycleImpl lifecycle = new LifecycleImpl();
        Phase renderResponse = new RenderResponsePhase();
        UIViewRoot page = new UIViewRoot();
        page.setId("root");
        page.setViewId(TEST_URI);
        getFacesContext().setViewRoot(page);

        try {
            renderResponse.execute(getFacesContext());
        } catch (FacesException fe) {
            System.out.println(fe.getMessage());
            if (null != fe.getCause()) {
                fe.getCause().printStackTrace();
            } else {
                fe.printStackTrace();
            }
        }
        assertTrue(!(getFacesContext().getRenderResponse()) &&
                   !(getFacesContext().getResponseComplete()));

        assertTrue(verifyExpectedOutput());
    }

} // end of class TestRenderResponsePhase
