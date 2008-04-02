/*
 * $Id: TestLifecycleImpl_initial.java,v 1.31 2006/03/29 22:39:45 rlubke Exp $
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

// TestLifecycleImpl_initial.java

package com.sun.faces.lifecycle;

import com.sun.faces.cactus.JspFacesTestCase;
import org.apache.cactus.WebRequest;

import javax.faces.FacesException;

import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletRequest;

/**
 * <B>TestLifecycleImpl_initial</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestLifecycleImpl_initial.java,v 1.31 2006/03/29 22:39:45 rlubke Exp $
 */

public class TestLifecycleImpl_initial extends JspFacesTestCase {

    public static final String TEST_URI = "/greeting.jsp";
    public static final String ignore[] = {
    };


    // ------------------------------------------------------------ Constructors


    public TestLifecycleImpl_initial() {

        super("TestLifecycleImpl_initial");

    }


    public TestLifecycleImpl_initial(String name) {

        super(name);

    }


    // ---------------------------------------------- Methods From FacesTestCase


    public boolean sendResponseToFile() {

        return true;

    }


    public String getExpectedOutputFilename() {

        return "TestLifecycleImpl_initial_correct";

    }


    public String[] getLinesToIgnore() {

        return ignore;

    }


    // ---------------------------------------------------------- Public Methods


    public void beginExecuteInitial(WebRequest theRequest) {

        initWebRequest(theRequest);

    }


    public void testExecuteInitial() {

        boolean result = false;
        LifecycleImpl life = new LifecycleImpl();

	Object oldRequest = facesService.wrapRequestToHideParameters();

        try {
            life.execute(getFacesContext());
	    facesService.unwrapRequestToShowParameters(oldRequest);
            life.render(getFacesContext());
        } catch (FacesException e) {
            System.err.println("Root Cause: " + e.getCause());
            if (null != e.getCause()) {
                e.getCause().printStackTrace();
            } else {
                e.printStackTrace();
            }

            assertTrue(e.getMessage(), false);
        }

        assertTrue(verifyExpectedOutput());

    }


    // ------------------------------------------------------- Protected Methods


    protected void initWebRequest(WebRequest theRequest) {

        theRequest.setURL("localhost:8080", "/test", "/faces", TEST_URI, null);

    }

} // end of class TestLifecycleImpl_initial
