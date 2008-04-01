/*
 * $Id: DebugRunner.java,v 1.1 2001/11/22 02:02:17 edburns Exp $
 *
 * Copyright 2000-2001 by Sun Microsystems, Inc.,
 * 901 San Antonio Road, Palo Alto, California, 94303, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Sun Microsystems, Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Sun.
 */

// DebugRunner.java

/**
 *
 *  <B>DebugRunner</B> is the main for running a JUnit test in the debugger.
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: DebugRunner.java,v 1.1 2001/11/22 02:02:17 edburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class DebugRunner extends Object
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

public DebugRunner()
{
    super();
    // ParameterCheck.nonNull();
    this.init();
}

protected void init()
{
    // super.init();
}

//
// Class methods
//

//
// General Methods
//

public static void main(String args[]) {
    if (args.length < 1) {
	System.out.println("Usage: DebugRunner fully.qual.classname.TestSomething");
	System.exit(-1);
    }
    Class testClass = null;
    try {
	testClass = Class.forName(args[0]);
    }
    catch (Exception e) {
	System.out.println("Can't get class for " + args[0]);
	System.exit(-1);
    }
    junit.textui.TestRunner.run(testClass);
}

} // end of class DebugRunner
