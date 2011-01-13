/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2010 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
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
