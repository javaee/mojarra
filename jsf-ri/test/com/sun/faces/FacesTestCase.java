/*
 * $Id: FacesTestCase.java,v 1.16 2005/08/22 22:11:05 ofung Exp $
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
