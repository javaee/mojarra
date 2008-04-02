/*
 * $Id: TestViewTag.java,v 1.8 2004/02/06 18:57:13 rlubke Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestViewTag.java

package com.sun.faces.taglib.jsf_core;

import com.sun.faces.JspFacesTestCase;
import com.sun.faces.lifecycle.Phase;
import com.sun.faces.lifecycle.RenderResponsePhase;
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
 * @version $Id: TestViewTag.java,v 1.8 2004/02/06 18:57:13 rlubke Exp $
 * @see	Blah
 * @see	Bloo
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
        UIViewRoot page = new UIViewRoot();
        page.setId("root");
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
        UIViewRoot page = new UIViewRoot();
        page.setId("root");
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
        Locale locale = viewTag.getLocaleFromString("fr-FR");
        assertTrue(locale.equals(new Locale("fr", "FR")));

        locale = viewTag.getLocaleFromString("fr_FR");
        assertTrue(locale.equals(new Locale("fr", "FR")));

        locale = viewTag.getLocaleFromString("fr");
        assertTrue(locale.equals(new Locale("fr")));

        locale = viewTag.getLocaleFromString("testLocale");
        assertTrue(locale.equals(Locale.getDefault()));
    }

} // end of class TestViewTag
