/*
 * $Id: TestMessageFactoryImpl.java,v 1.11 2006/03/29 22:39:48 rlubke Exp $
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

// TestMessageFactoryImpl.java

package com.sun.faces.util;

import com.sun.faces.cactus.ServletFacesTestCase;

import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import java.util.Locale;

/**
 * <B>TestMessageFactoryImpl</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestMessageFactoryImpl.java,v 1.11 2006/03/29 22:39:48 rlubke Exp $
 */

public class TestMessageFactoryImpl extends ServletFacesTestCase {


    // ------------------------------------------------------------ Constructors
    

    public TestMessageFactoryImpl() {

        super("TestMessageFactoryImpl");

    }


    public TestMessageFactoryImpl(String name) {

        super(name);

    }


    // ---------------------------------------------------------- Public Methods

   
    public void setUp() {

        super.setUp();
        UIViewRoot viewRoot = Util.getViewHandler(getFacesContext()).createView(getFacesContext(), null);
        viewRoot.setViewId("viewId");
        getFacesContext().setViewRoot(viewRoot);

    }


    public void testFindCatalog() {

        boolean gotException = false;
        FacesMessage msg = null;

        // if no locale is set, it should use the fall back,
        // JSFMessages.xml
        msg = MessageFactory.getMessage(getFacesContext(), "MSG0003", "userId");
        assertTrue(msg != null);
        assertTrue(
            (msg.getSummary()).equals("'userId' field cannot be empty."));

        // passing an invalid locale should use fall back.
        Locale en_locale = new Locale("eng", "us");
        getFacesContext().getViewRoot().setLocale(en_locale);
        System.out.println("Testing get methods");
        try {
            msg =
                MessageFactory.getMessage(getFacesContext(), "MSG0003",
                                          "userId");
        } catch (Exception fe) {
            gotException = true;
        }
        assertTrue(!gotException);
        gotException = false;
        msg = null;

        en_locale = new Locale("en", "us");
        getFacesContext().getViewRoot().setLocale(en_locale);
        msg = MessageFactory.getMessage(getFacesContext(), "MSG0003", "userId");
        assertTrue(msg != null);
        assertTrue(
            (msg.getSummary()).equals("'userId' field cannot be empty."));
        msg = null;

    }

    
    public void testGetMethods() {

        boolean gotException = false;
        FacesMessage msg = null;

        FacesContext facesContext = getFacesContext();
        assert (facesContext != null);

        System.out.println("Testing get methods");
        try {
            msg = MessageFactory.getMessage((FacesContext) null, (String) null);
        } catch (NullPointerException fe) {
            gotException = true;
        }
        assertTrue(gotException);
        gotException = false;
        msg = null;
        
        // if msgId doesn't exist in the resource, null must be returned
        try {
            msg = MessageFactory.getMessage(facesContext, "MSG01", "param1");
            assertTrue(null == msg);
        } catch (FacesException fe) {
            assertTrue(false);
        }


        Object[] params1 = {"JavaServerFaces"};
        msg = MessageFactory.getMessage(facesContext, "MSG0001", params1);
        assertTrue(msg != null);
        assertTrue(
            (msg.getSummary()).equals(
                "'JavaServerFaces' is not a valid number."));

        msg = MessageFactory.getMessage(facesContext, "MSG0003", "userId");
        assertTrue(msg != null);
        assertTrue(
            (msg.getSummary()).equals("'userId' field cannot be empty."));

        msg =
            MessageFactory.getMessage(facesContext, "MSG0004", "userId",
                                      "1000", "10000");
        assertTrue(msg != null);
        assertTrue(
            (msg.getSummary()).equals(
                "'userId' out of range. Value should be between '1000' and '10000'."));

    }

} // end of class TestMessageListImpl
