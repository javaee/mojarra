/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
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
 */

public class TestMessageFactoryImpl extends ServletFacesTestCase {

    //
    // Protected Constants
    //

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

    public TestMessageFactoryImpl() {
        super("TestMessageFactoryImpl");
    }


    public TestMessageFactoryImpl(String name) {
        super(name);
    }
    //
    // Class methods
    //

    //
    // Methods from TestCase
    //
    public void setUp() {
        super.setUp();
        UIViewRoot viewRoot = Util.getViewHandler(getFacesContext()).createView(getFacesContext(), null);
        viewRoot.setViewId("viewId");
        viewRoot.setLocale(Locale.US);
        getFacesContext().setViewRoot(viewRoot);
    }
    
    //
    // General Methods
    //
    
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

} // end of class TestMessageListImpl
