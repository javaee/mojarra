/*
 * $Id: TestViewTag.java,v 1.17 2007/01/30 02:32:49 rlubke Exp $
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

// TestViewTag.java

package com.sun.faces.taglib.jsf_core;

import com.sun.faces.cactus.JspFacesTestCase;
import com.sun.faces.lifecycle.Phase;
import com.sun.faces.lifecycle.RenderResponsePhase;
import com.sun.faces.util.Util;
import com.sun.faces.cactus.TestingUtil;

import org.apache.cactus.JspTestCase;
import org.apache.cactus.WebRequest;

import javax.faces.FacesException;
import javax.faces.component.UIViewRoot;
import javax.servlet.ServletRequest;
import javax.servlet.jsp.jstl.core.Config;

import java.util.Locale;

/**
 * <B>TestViewTag</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestViewTag.java,v 1.17 2007/01/30 02:32:49 rlubke Exp $
 */

public class TestViewTag extends JspFacesTestCase {

//
// Protected Constants
//

    public static final String TEST_URI = "/TestViewTag.jsp";
    public static final String TEST_URI2 = "/TestViewTag2.jsp";

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

    public TestViewTag() {
        super("TestViewTag");
    }


    public TestViewTag(String name) {
        super(name);
    }

//
// Class methods
//

//
// General Methods
//

    public void beginViewTag(WebRequest theRequest) {
        theRequest.setURL("localhost:8080", "/test", "/faces", TEST_URI, null);
    }


    public void testViewTag() {
        boolean result = false;
        String value = null;
        Locale expectedLocale = new Locale("ps", "PS");
        Phase renderResponse = new RenderResponsePhase();
        UIViewRoot page = Util.getViewHandler(getFacesContext()).createView(getFacesContext(), null);
        page.setId("root");
        page.setLocale(Locale.US);
        page.setViewId(TEST_URI);
        page.setLocale(Locale.CANADA_FRENCH);
        getFacesContext().setViewRoot(page);

        Config.set((ServletRequest)
            getFacesContext().getExternalContext().getRequest(),
                   Config.FMT_LOCALE, Locale.CANADA_FRENCH);

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
        assertEquals("locale not as expected", expectedLocale,
                     page.getLocale());
        assertEquals("locale not as expected", expectedLocale,
                     Config.get((ServletRequest)
            getFacesContext().getExternalContext().
            getRequest(),
                                Config.FMT_LOCALE));
    }


    public void beginViewTagVB(WebRequest theRequest) {
        theRequest.setURL("localhost:8080", "/test", "/faces", TEST_URI2, null);
    }


    public void testViewTagVB() {
        boolean result = false;
        String value = null;
        Locale expectedLocale = new Locale("ps", "PS", "Traditional");
        request.setAttribute("locale", expectedLocale);
        Phase renderResponse = new RenderResponsePhase();
        UIViewRoot page = Util.getViewHandler(getFacesContext()).createView(getFacesContext(), null);
        page.setId("root");
        page.setLocale(Locale.US);
        page.setViewId(TEST_URI2);
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
        assertEquals("locale not as expected", expectedLocale,
                     page.getLocale());
    }


    public void testGetLocaleFromString() {
        ViewTag viewTag = new ViewTag();
        Locale locale = (Locale) 
            TestingUtil.invokePrivateMethod("getLocaleFromString",
                                            new Class[] { String.class },
                                            new Object[] { "fr-FR" },
                                            ViewTag.class,
                                            viewTag);        
        assertTrue(locale.equals(new Locale("fr", "FR")));

        
        locale = (Locale)
            TestingUtil.invokePrivateMethod("getLocaleFromString",
                                            new Class[] { String.class },
                                            new Object[] { "fr_FR" },
                                            ViewTag.class,
                                            viewTag);
        assertTrue(locale.equals(new Locale("fr", "FR")));

        
        locale = (Locale)
            TestingUtil.invokePrivateMethod("getLocaleFromString",
                                            new Class[] {String.class},
                                            new Object[] {"fr"},
                                            ViewTag.class,
                                            viewTag);
        assertTrue(locale.equals(new Locale("fr", "")));

       
        locale = (Locale)
            TestingUtil.invokePrivateMethod("getLocaleFromString",
                                            new Class[] {String.class},
                                            new Object[] {"testLocale"},
                                            ViewTag.class,
                                            viewTag);
        assertTrue(locale.equals(Locale.getDefault()));
    }

} // end of class TestViewTag
