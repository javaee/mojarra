/*
 * $Id: TestUtil_local.java,v 1.5 2004/02/04 23:44:58 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestUtil_local.java

package com.sun.faces.util;

import java.util.Locale;

import junit.framework.TestCase;

/**
 *
 *  <B>TestUtil_local.java</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestUtil_local.java,v 1.5 2004/02/04 23:44:58 ofung Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestUtil_local extends TestCase 
{
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

    public TestUtil_local() {super("TestUtil_local.java");}
    public TestUtil_local(String name) {super(name);}

//
// Class methods
//

//
// General Methods
//


    public void testReplaceOccurrences() {
	assertTrue(((String)Util.replaceOccurrences(" ", " ", "%20")).
		   equals("%20"));
	assertTrue(((String)Util.replaceOccurrences("        ", " ", "%20")).
		   equals("%20%20%20%20%20%20%20%20"));
	assertTrue(((String)Util.replaceOccurrences(" hello", " ", "%20")).
		   equals("%20hello"));
	assertTrue(((String)Util.replaceOccurrences(" hello ", " ", "%20")).
		   equals("%20hello%20"));
	assertTrue(((String)Util.replaceOccurrences("hello ", " ", "%20")).
		   equals("hello%20"));
	assertTrue(((String)Util.replaceOccurrences("hello hello", " ", "%20")).
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
