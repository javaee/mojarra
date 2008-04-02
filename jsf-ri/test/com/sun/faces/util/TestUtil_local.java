/*
 * $Id: TestUtil_local.java,v 1.8 2005/08/22 22:11:27 ofung Exp $
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

// TestUtil_local.java

package com.sun.faces.util;

import junit.framework.TestCase;

import java.util.Locale;

/**
 * <B>TestUtil_local.java</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestUtil_local.java,v 1.8 2005/08/22 22:11:27 ofung Exp $
 */

public class TestUtil_local extends TestCase {

//
// Protected Constants
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

    public TestUtil_local() {
        super("TestUtil_local.java");
    }


    public TestUtil_local(String name) {
        super(name);
    }

//
// Class methods
//

//
// General Methods
//


    public void testReplaceOccurrences() {
        assertTrue(((String) Util.replaceOccurrences(" ", " ", "%20")).
                   equals("%20"));
        assertTrue(((String) Util.replaceOccurrences("        ", " ", "%20")).
                   equals("%20%20%20%20%20%20%20%20"));
        assertTrue(((String) Util.replaceOccurrences(" hello", " ", "%20")).
                   equals("%20hello"));
        assertTrue(((String) Util.replaceOccurrences(" hello ", " ", "%20")).
                   equals("%20hello%20"));
        assertTrue(((String) Util.replaceOccurrences("hello ", " ", "%20")).
                   equals("hello%20"));
        assertTrue(((String) Util.replaceOccurrences("hello hello", " ", "%20")).
                   equals("hello%20hello"));

    }


    public void testGetLocaleFromString() {
        Locale result = null;

        // positive tests
        assertNotNull(result = Util.getLocaleFromString("ps"));
        assertNotNull(result = Util.getLocaleFromString("tg_AF"));
        assertNotNull(result = Util.getLocaleFromString("tk_IQ-Traditional"));
        assertNotNull(result = Util.getLocaleFromString("tk-IQ_Traditional"));

    }

} // end of class TestUtil_local
