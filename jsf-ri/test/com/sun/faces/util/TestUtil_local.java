/*
 * $Id: TestUtil_local.java,v 1.3 2003/09/11 23:13:02 eburns Exp $
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
 * @version $Id: TestUtil_local.java,v 1.3 2003/09/11 23:13:02 eburns Exp $
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

} // end of class TestUtil_local
