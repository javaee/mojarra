/*
 * $Id: FacesTestCase.java,v 1.15 2005/06/21 00:55:22 jayashri Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// FacesTestCaseJsp.java

package com.sun.faces;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

/**
 * This interface defines the contract between something that extends a
 * cactus TestCase class (JspTestCase or ServletTestCase) and
 * FacesTestCaseService.
 */

public interface FacesTestCase {

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


    public String[] getLinesToIgnore();


    public boolean sendWriterToFile();
    
    public void setTestRootDir(String testRootDir);
    
    public String getTestRootDir();

} // end of interface FacesTestCase
