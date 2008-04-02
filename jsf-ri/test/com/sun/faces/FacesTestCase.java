/*
 * $Id: FacesTestCase.java,v 1.11 2003/02/20 22:49:45 ofung Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// FacesTestCaseJsp.java

package com.sun.faces;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletConfig;
import javax.servlet.jsp.PageContext;

/**

* This interface defines the contract between something that extends a
* cactus TestCase class (JspTestCase or ServletTestCase) and
* FacesTestCaseService.

*/

public interface FacesTestCase
{

public HttpServletRequest getRequest();

public HttpServletResponse getResponse();

public ServletConfig getConfig();

public PageContext getPageContext();

/**

* @return true if the ServletResponse output should be sent to a file

*/

public boolean sendResponseToFile();

/**

* @return the name of the expected output filename for this testcase.

*/

public String getExpectedOutputFilename();

public String [] getLinesToIgnore();

public boolean sendWriterToFile();

} // end of interface FacesTestCase
