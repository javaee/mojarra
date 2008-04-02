/*
 * $Id: TestFacesMessage.java,v 1.1 2007/01/27 18:16:58 rogerk Exp $
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

// TestFacesMessage.java

package com.sun.faces.application;
import com.sun.faces.cactus.ServletFacesTestCase;
import com.sun.faces.util.Util;

import java.io.*;

import javax.faces.application.FacesMessage;


/**
 *
 *  <B>TestFacesMessage</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestFacesMessage.java,v 1.1 2007/01/27 18:16:58 rogerk Exp $
 */

/**
 * This class tests the <code>FacesMessage</code> class
 * functionality.  It uses the xml configuration file:
 * <code>web/test/WEB-INF/faces-navigation.xml</code>.
 */
public class TestFacesMessage extends ServletFacesTestCase {
    
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
    
    public TestFacesMessage() {
        super("TestFacesMessage");
    }
    
    
    public TestFacesMessage(String name) {
        super(name);
    }
//
// Class methods
//
    
//
// Methods from TestCase
//
    
//
// General Methods
//
    
    public void testSerializeable() {
        FacesMessage message = null;
        
        // Case 0 (nothing)
        message = new FacesMessage();
        persistAndCheck(message);

        // Case 1 (summary)
        message = new FacesMessage("This is a bad error.");
        persistAndCheck(message);
        
        // Case 2 (summary & detail)
        message = new FacesMessage("This is a bad error.", "This is a really bad error.");
        persistAndCheck(message);
        
        // Case 3 (severity, summary & detail)
        message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "This is a bad error.",
                "This is a really bad error.");
        persistAndCheck(message);
    }
    
    private void persistAndCheck(FacesMessage message) {
        FacesMessage message1 = null;
        String mSummary, mSummary1 = null;
        String mDetail, mDetail1 = null;
        String severity, severity1 = null;
        ByteArrayOutputStream bos = null;
        ByteArrayInputStream bis = null;
        
        mSummary = message.getSummary();
        mDetail = message.getDetail();
        severity = message.getSeverity().toString();
        
        try {
            bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(message);
            oos.close();
            byte[] bytes = bos.toByteArray();
            InputStream in = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(in);
            message1 = (FacesMessage)ois.readObject();
            ois.close();
            mSummary1 = message1.getSummary();
            mDetail1 = message1.getDetail();
            severity1 = message1.getSeverity().toString();
            if (null != mSummary1) {
                assertTrue(mSummary1.equals(mSummary));
            } else {
                assertTrue(mSummary == null);
            }
            if (null != mDetail1) {
                assertTrue(mDetail1.equals(mDetail));
            } else {
                assertTrue(mDetail == null);
            }
            if (null != severity1) {
                assertTrue(severity1.equals(severity));
            } else {
                assertTrue(severity == null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        } 
    }
    
} // end of class TestFacesMessage

