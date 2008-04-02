/*
 * $Id: PhaseIdTest.java,v 1.2 2004/02/04 23:39:09 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.event;

import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.util.Iterator;

public class PhaseIdTest extends TestCase
{
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

public PhaseIdTest()
{
    super();
}

//
// Class methods
//

//
// General Methods
//

    public void testToString() {
	Iterator valueIter = PhaseId.VALUES.iterator();
	String cur = null;
	while (valueIter.hasNext()) {
	    cur = (String) valueIter.next().toString();
	    System.out.println(cur);
	    assertTrue(cur.length() > 3);
	}
	
    }

} // end of class PhaseIdTest
