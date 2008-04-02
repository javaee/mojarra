/*
 * $Id: TestUtil_local.java,v 1.2 2003/08/08 23:34:48 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestUtil_local.java

package com.sun.faces.util;

import junit.framework.TestCase;

/**
 *
 *  <B>TestUtil_local.java</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestUtil_local.java,v 1.2 2003/08/08 23:34:48 eburns Exp $
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

    public void testGetDefaultConverterForType() {
        String expectedConverters[][] = {
	    {"java.util.Date", "Date"},
	    {"java.lang.Boolean", "Boolean"},
	    {"java.lang.Byte", "Number"},
	    {"java.lang.Double", "Number"},
	    {"java.lang.Float", "Number"},
	    {"java.lang.Integer", "Number"},
	    {"java.lang.Short", "Number"},
	    {"java.lang.Long", "Number"},
	    {"byte", "Number"},
	    {"double", "Number"},
	    {"float", "Number"},
	    {"int", "Number"},
	    {"long", "Number"},
	    {"short", "Number"}
	};
	int i, len = expectedConverters[0].length;
	String result = null;
	for (i = 0; i < len; i++) {
	    result = Util.getDefaultConverterForType(expectedConverters[i][0]);
	    assertTrue(null != result);
	    assertTrue(result.equals(expectedConverters[i][1]));
	}
	assertTrue(null == Util.getDefaultConverterForType("yoyodyne"));
    }


} // end of class TestUtil_local
