/*
 * $Id: FacesTestCaseService.java,v 1.10 2003/02/20 22:49:46 ofung Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// FacesTestCaseService.java

package com.sun.faces;

import javax.servlet.http.HttpServletResponse;

import javax.faces.FactoryFinder;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.context.ResponseWriter;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.component.UICommand;
import com.sun.faces.renderkit.html_basic.HtmlBasicRenderKit;
import javax.servlet.jsp.PageContext;

import com.sun.faces.util.Util;
import com.sun.faces.RIConstants;

import org.mozilla.util.Assert;

import java.util.ArrayList;

import java.io.IOException;

/**
 *

 * Subclasses of ServletTestCase and JspTestCase use an instance of this
 * class to handle behavior specific to Faces TestCases.  You may
 * recognize this as using object compositition vs multiple inheritance.
 * <P>

 *
 * <B>Lifetime And Scope</B> <P> Same as the JspTestCase or
 * ServletTestCase instance that uses it.
 *
 * @version $Id: FacesTestCaseService.java,v 1.10 2003/02/20 22:49:46 ofung Exp $
 * 
 * @see	com.sun.faces.context.FacesContextFactoryImpl
 * @see	com.sun.faces.context.FacesContextImpl
 *
 */

public class FacesTestCaseService extends Object
{
//
// Protected Constants
//

/**

* Things used as names and values in the System.Properties table.

*/

public static final String ENTER_CALLED = "enterCalled";
public static final String EXIT_CALLED = "exitCalled";
public static final String EMPTY = "empty";



//
// Class Variables
//

//
// Instance Variables
//

// Attribute Instance Variables

// Relationship Instance Variables

protected FacesTestCase facesTestCase = null;

protected FacesContextFactory facesContextFactory = null;

protected FacesContext facesContext = null;

protected Lifecycle lifecycle = null;

//
// Constructors and Initializers    
//

public FacesTestCaseService(FacesTestCase newFacesTestCase) 
{
    facesTestCase = newFacesTestCase;
}


//
// Class methods
//

//
// General Methods
//

public FacesContext getFacesContext() { return facesContext; }

public FacesContextFactory getFacesContextFactory() 
    { return facesContextFactory; }

public void setUp()
{
    HttpServletResponse response = null;
    Util.verifyFactoriesAndInitDefaultRenderKit(facesTestCase.getConfig().getServletContext());
    
    facesContextFactory = (FacesContextFactory) 
	FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
    Assert.assert_it(null != facesContextFactory);

    // See if the testcase wants to have its output sent to a file.
    if (facesTestCase.sendResponseToFile()) {
	response = new FileOutputResponseWrapper(facesTestCase.getResponse());
    } else {
	response = facesTestCase.getResponse();
    }

    LifecycleFactory factory = (LifecycleFactory)
	FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
    Assert.assert_it(null != factory);
    lifecycle = factory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
    Assert.assert_it(null != lifecycle);


    facesContext = 
	facesContextFactory.getFacesContext(facesTestCase.getConfig().
					    getServletContext(),
					    facesTestCase.getRequest(), 
					    response, lifecycle);
    Assert.assert_it(null != facesContext);
        
    if (facesTestCase.sendWriterToFile()){
        ResponseWriter responseWriter = new FileOutputResponseWriter();
	facesContext.setResponseWriter(responseWriter);
    }    
    
    TestBean testBean = new TestBean();
    (facesContext.getHttpSession()).setAttribute("TestBean", testBean);
    System.setProperty(RIConstants.DISABLE_RENDERERS, 
		       RIConstants.DISABLE_RENDERERS);

    PageContext pageContext = null;
    
    if (null != (pageContext = facesTestCase.getPageContext())) {
	pageContext.setAttribute(FacesContext.FACES_CONTEXT_ATTR, facesContext,
				 PageContext.REQUEST_SCOPE);
    }

    java.util.Enumeration paramNames = getFacesContext().getServletRequest().getParameterNames();
    while (paramNames.hasMoreElements()) {
	String curName = (String) paramNames.nextElement();
	
	System.out.println(curName + "=" +
			   getFacesContext().getServletRequest().getParameter(curName));
    }
}

public void tearDown()
{
    Util.releaseFactoriesAndDefaultRenderKit(facesTestCase.getConfig().getServletContext());
    // make sure session is not null. It will null in case release
    // was invoked.
    if ( facesContext.getHttpSession() != null ) {
        (facesContext.getHttpSession()).removeAttribute("TestBean");
    }    

    PageContext pageContext = null;
    if (null != (pageContext = facesTestCase.getPageContext())) {
	pageContext.removeAttribute(FacesContext.FACES_CONTEXT_ATTR);
    }
}

public boolean verifyExpectedOutput()
{
    boolean result = false;
    CompareFiles cf = new CompareFiles();
    String errorMessage = null;
    String outputFileName = null;
    String correctFileName = null;
    
    // If this testcase doesn't participate in file comparison
    if (!facesTestCase.sendResponseToFile() && 
	(!facesTestCase.sendWriterToFile()) && 
	(null == facesTestCase.getExpectedOutputFilename())) {
	return true;
    }
    
    if (facesTestCase.sendResponseToFile() ) {
        outputFileName = FileOutputResponseWrapper.FACES_RESPONSE_FILENAME;
    } else {
        outputFileName = FileOutputResponseWriter.RESPONSE_WRITER_FILENAME;
    }
    correctFileName = FileOutputResponseWrapper.FACES_RESPONSE_ROOT +
	facesTestCase.getExpectedOutputFilename();
    
    errorMessage = "File Comparison failed: diff -u " + outputFileName + " " + 
        correctFileName;
    
    ArrayList ignoreList = null;
    String [] ignore = null;
    
    if (null != (ignore = facesTestCase.getLinesToIgnore())) {
	ignoreList = new ArrayList();
	for (int i = 0; i < ignore.length; i++) {
	    ignoreList.add(ignore[i]);
	}
    }
    
    try {
	result = cf.filesIdentical(outputFileName, correctFileName,ignoreList);
    }
    catch (IOException e) {
	System.out.println(e.getMessage());
	e.printStackTrace();
    }

    if (!result) {
	System.out.println(errorMessage);
    }
    System.out.println("VERIFY:"+result); 
    return result;
}

} // end of class FacesTestCaseService
