/*
 * $Id: FacesTestCaseService.java,v 1.17 2003/05/13 03:55:44 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// FacesTestCaseService.java

package com.sun.faces;

import java.util.Iterator;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContext;

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
import com.sun.faces.config.ConfigListener;

import org.mozilla.util.Assert;

import java.util.ArrayList;
import java.util.Enumeration;

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
 * @version $Id: FacesTestCaseService.java,v 1.17 2003/05/13 03:55:44 eburns Exp $
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
    facesContext.getExternalContext().getSessionMap().put("TestBean", testBean);
    System.setProperty(RIConstants.DISABLE_RENDERERS, 
		       RIConstants.DISABLE_RENDERERS);

    Iterator paramNames = getFacesContext().getExternalContext().getRequestParameterNames();
    while (paramNames.hasNext()) {
	String curName = (String) paramNames.next();
	
	System.out.println(curName + "=" +
			   getFacesContext().getExternalContext().
			   getRequestParameterMap().get(curName));
    }

    // make sure this gets called once per ServletContext instance.
    if (null == 
	(facesTestCase.getConfig().getServletContext().
	 getAttribute(RIConstants.CONFIG_ATTR))) {
	
	ConfigListener configListener = new ConfigListener();
	ServletContextEvent e = 
	    new ServletContextEvent(facesTestCase.getConfig().getServletContext());
	configListener.contextInitialized(e);
    }
}

public void tearDown()
{
    // make sure this gets called!
    ConfigListener configListener = new ConfigListener();
    ServletContextEvent e = 
	new ServletContextEvent(facesTestCase.getConfig().getServletContext());
    configListener.contextDestroyed(e);
    
    Util.releaseFactoriesAndDefaultRenderKit(facesTestCase.getConfig().getServletContext());
    // make sure session is not null. It will null in case release
    // was invoked.
    if (facesContext.getExternalContext() != null) {
    if ( facesContext.getExternalContext().getSession(true) != null ) {
        facesContext.getExternalContext().getSessionMap().remove("TestBean");
    }    
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

public boolean isMember(String toTest, String [] set) {
    int 
	len = set.length,
	i = 0;
    for (i = 0; i < len; i++) {
	if (set[i].equals(toTest)) {
	    return true;
	}
    }
    return false;
}

/**

* @return true iff every element in subset is present in the iterator.

*/

public boolean isSubset(String [] subset, Iterator superset) {
    int i, len = subset.length;
    boolean [] hits = new boolean[len];
    String cur = null;
    for (i = 0; i < len; i++) {
	hits[i] = false;
    }


    // for each element in the superset, go through the entire subset,
    // marking our "hits" array if there is a match.
    while (superset.hasNext()) {
	cur = (String) superset.next();
	for (i = 0; i < len; i++) {
	    if (cur.equals(subset[i])) {
		hits[i] = true;
	    }
	}
    }

    // if any of the hits array is false, return false;
    for (i = 0; i < len; i++) {
	if (!hits[i]) {
	    return false;
	}
    }
    return true;
}

/**

* <p>This method allows comparing the attribute sets of two
* HttpServletRequests for equality.</p>

* @return true if every attribute in in request1 has an analog in
* request2, with the same value as in request1, and the converse is true
* as well.

*
*/
	
	
public boolean requestsHaveSameAttributeSet(HttpServletRequest request1,
					    HttpServletRequest request2) {
    Enumeration attrNames = request1.getAttributeNames();
    String curAttr;
    Object valA, valB;

    // make sure every name/value pair in request1 is the same
    // name/value pare in request2.

    while (attrNames.hasMoreElements()) {
	curAttr = (String) attrNames.nextElement();
	valA = request1.getAttribute(curAttr);
	valB = request2.getAttribute(curAttr);
	if (null != valA && null != valB) {
	    // if any of the values are not equal, return false
	    if (!valA.equals(valB)) {
		return false;
	    }
	}
	else if (null != valA || null != valB) {
	    // one of the values is null, therefore, not equal, return false
	    return false;
	}
    }

    // make sure every name/value pair in request2 is the same
    // name/value pare in request1.
    attrNames = request2.getAttributeNames();

    while (attrNames.hasMoreElements()) {
	curAttr = (String) attrNames.nextElement();
	valA = request2.getAttribute(curAttr);
	valB = request1.getAttribute(curAttr);
	if (null != valA && null != valB) {
	    // if any of the values are not equal, return false
	    if (!valA.equals(valB)) {
		return false;
	    }
	}
	else if (null != valA || null != valB) {
	    // one of the values is null, therefore, not equal, return false
	    return false;
	}
    }
    return true;
}
    


} // end of class FacesTestCaseService
