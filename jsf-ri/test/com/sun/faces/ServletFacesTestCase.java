/*
 * $Id: ServletFacesTestCase.java,v 1.5 2003/05/02 03:11:32 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// ServletFacesTestCase.java

package com.sun.faces;

import org.apache.cactus.ServletTestCase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletConfig;

import javax.servlet.jsp.PageContext;

import javax.faces.context.FacesContext;

import java.util.Iterator;

/**
 *

 *  <B>ServletFacesTestCase</B> is a base class that leverages
 *  FacesTestCaseService to add Faces specific behavior to that provided
 *  by cactus.  This class just delegates all method calls to
 *  facesService.
 *
 * @version $Id: ServletFacesTestCase.java,v 1.5 2003/05/02 03:11:32 eburns Exp $
 * 
 * @see	#facesService
 *
 */

public abstract class ServletFacesTestCase extends ServletTestCase implements FacesTestCase
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

    public ServletFacesTestCase() { 
	super("ServletFacesTestCase");
	init();
    }

    public ServletFacesTestCase(String name) { 
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
    return null;
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

public boolean sendWriterToFile() {
    return false;
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

public boolean isMember(String toTest, String [] set) 
{
    return facesService.isMember(toTest, set);
}

public boolean isSubset(String [] subset, Iterator superset) {
    return facesService.isSubset(subset, superset);
}
    

} // end of class ServletFacesTestCase
