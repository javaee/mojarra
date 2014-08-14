/**
 * 
 */
package wlstest.functional.webapp.jsf20.custombeanscope;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author haili Oct 7, 2006 9:57:00 PM
 *
 */
public class CustomBeanScopeSuiteTest {
	public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTest(new CustomBeanScopeTest("testCustomBeanScope"));
    
    //compatibility test
    
    return suite;
  }
}
