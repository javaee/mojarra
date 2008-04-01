/*
 * $Id: DebugRunner.java,v 1.3 2001/12/20 21:05:11 edburns Exp $
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
 * @version $Id: DebugRunner.java,v 1.3 2001/12/20 21:05:11 edburns Exp $
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

/**

* PRECONDITION: testClass is a subclass of junit.framework.TestCase

* POSTCONDITION: if testClass is a subclass of
* org.apache.cactus.ServletTestCase, return true.

*/

private static boolean isServletTestCase(Class testClass) {
    Object testCase = null;
    boolean result = false;
    try {
	testCase = testClass.newInstance();
    }
    catch (Exception e) {
    }
    if (org.apache.cactus.ServletTestCase.class.isInstance(testCase)) {
	result = true;
    }
    return result;
}

private static void startTomcat() {
    String serverXml = null;
    if (null == System.getProperty("catalina.home")) {
	System.setProperty("catalina.home", "D:\\tomcat4.0");
    }
    if (null == (serverXml = System.getProperty("server.xml"))) {
	serverXml = "D:\\Projects\\J2EE\\workareas\\jsf-ea1\\jsf-ri\\build\\test\\servers\\tomcat40\\conf\\server.xml";
    }
    final String [] args = {"-config", serverXml, "start"};
    Thread runner = new Thread(new Runnable() {
	    public void run() {
		org.apache.catalina.startup.Bootstrap.main(args);
	    }
	});
    runner.start();
}

private static void stopTomcat() {
    String [] args = {"stop"};
    org.apache.catalina.startup.Bootstrap.main(args);
}

public static void main(String args[]) {
    boolean isServlet = false;
    if (args.length < 1) {
	System.out.println("Usage: DebugRunner fully.qual.classname.TestSomething\n\n" +
			   "If argument is cactus test, you must have following System properties defined:\n\n" +
			   "\tcatalina.home [D:\\tomcat4.0]\n\n" +
			   "\tserver.xml, given as the -config argument. " +
			   "[D:\\Projects\\J2EE\\workareas\\jsf-ri\\build\\test\\servers\\tomcat40\\conf\\server.xml]\n\n" + 
			   "\tYou must also have the following jars in your classpath:\n" +
			   "\tcactus.jar, bootstrap.jar, catalina.jar, junit.jar");
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
    if (isServlet = isServletTestCase(testClass)) {
	startTomcat();
    }
    junit.textui.TestRunner.run(testClass);
    if (isServlet) {
	stopTomcat();
    }
}

} // end of class DebugRunner
