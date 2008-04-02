/*
 * $Id: PhaseIdTest.java,v 1.4 2005/08/22 22:08:22 ofung Exp $
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
