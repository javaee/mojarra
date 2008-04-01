/*
 * $Id: JspFacesTestCase.java,v 1.2 2002/06/20 20:20:11 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// JspFacesTestCase.java

package com.sun.faces;

import org.apache.cactus.JspTestCase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletConfig;

import javax.servlet.jsp.PageContext;

import javax.faces.context.FacesContext;

/**
 *

 *  <B>JspFacesTestCase</B> is a base class that leverages
 *  FacesTestCaseService to add Faces specific behavior to that provided
 *  by cactus.  This class just delegates all method calls to
 *  facesService.
 *
 * @version $Id: JspFacesTestCase.java,v 1.2 2002/06/20 20:20:11 jvisvanathan Exp $
 * 
 * @see	#facesService
 *
 */

public abstract class JspFacesTestCase extends JspTestCase implements FacesTestCase
{
//
// Protected Constants
//

public static final String ENTER_CALLED = FacesTestCaseService.ENTER_CALLED;
public static final String EXIT_CALLED = FacesTestCaseService.EXIT_CALLED;
public static final String EMPTY = FacesTestCaseService.EMPTY;


//
// Class Variables
//

//
// Instance Variables
//

// Attribute Instance Variables

// Relationship Instance Variables

/*

* This is the thing you use to get the facesContext and other
* Faces Objects.

*/

protected FacesTestCaseService facesService = null;

//
// Constructors and Initializers    
//

    public JspFacesTestCase() { 
	super("JspFacesTestCase");
	init();
    }

    public JspFacesTestCase(String name) { 
	super(name);
	init();
    }

    protected void init() {
	facesService = new FacesTestCaseService(this);
    }

//
// Class methods
//

//
// Methods from FacesTestCase
//

public ServletConfig getConfig() 
{
    return config;
}

public HttpServletRequest getRequest() 
{
    return request;
}

public HttpServletResponse getResponse()
{
    return response;
}

public PageContext getPageContext()
{
    return pageContext;
}

public boolean sendResponseToFile()
{
    return false;
}

public String getExpectedOutputFilename()
{
    return null;
}

public String [] getLinesToIgnore()
{
    return null;
}

//
// General Methods
//


public void setUp()
{
    facesService.setUp();
}

public void tearDown()
{
    facesService.tearDown();
}

public FacesContext getFacesContext()
{
    return facesService.getFacesContext();
}

public boolean verifyExpectedOutput()
{
    return facesService.verifyExpectedOutput();
}

public boolean sendWriterToFile() {
    return false;
}    

} // end of class JspFacesTestCase
