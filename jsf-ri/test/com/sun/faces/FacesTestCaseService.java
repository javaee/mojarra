/*
 * $Id: FacesTestCaseService.java,v 1.42 2005/03/15 20:37:39 edburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// FacesTestCaseService.java

package com.sun.faces;

import com.sun.faces.config.ConfigureListener;
import com.sun.faces.application.ApplicationAssociate;
import com.sun.faces.util.Util;
import org.apache.cactus.server.ServletContextWrapper;

import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.context.ResponseWriter;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.webapp.FacesServlet;
import javax.servlet.ServletContextEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequestWrapper;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;

import com.sun.faces.RIConstants;


/**
 * Subclasses of ServletTestCase and JspTestCase use an instance of this
 * class to handle behavior specific to Faces TestCases.  You may
 * recognize this as using object compositition vs multiple inheritance.
 * <P>
 * <p/>
 * <p/>
 * <B>Lifetime And Scope</B> <P> Same as the JspTestCase or
 * ServletTestCase instance that uses it.
 *
 * @version $Id: FacesTestCaseService.java,v 1.42 2005/03/15 20:37:39 edburns Exp $
 * @see	com.sun.faces.context.FacesContextFactoryImpl
 * @see	com.sun.faces.context.FacesContextImpl
 */

public class FacesTestCaseService extends Object {

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

    public FacesTestCaseService(FacesTestCase newFacesTestCase) {
        facesTestCase = newFacesTestCase;
    }


//
// Class methods
//

//
// General Methods
//

    public FacesContext getFacesContext() {
        return facesContext;
    }


    public FacesContextFactory getFacesContextFactory() {
        return facesContextFactory;
    }


    public void setUp() {
        HttpServletResponse response = null;
        RIConstants.IS_UNIT_TEST_MODE = true;

	// make sure the ApplicationAssociate is aware of the ServletContext
	com.sun.faces.config.StoreServletContext storeSC = 
	    new com.sun.faces.config.StoreServletContext();
	storeSC.setServletContext(facesTestCase.getConfig().getServletContext());

        // make sure the default factories are found, even if they have been
        // cleared before.
	FactoryFinder.releaseFactories();
        FactoryFinder.setFactory(FactoryFinder.APPLICATION_FACTORY,
                                 "com.sun.faces.application.ApplicationFactoryImpl");
        FactoryFinder.setFactory(FactoryFinder.FACES_CONTEXT_FACTORY,
                                 "com.sun.faces.context.FacesContextFactoryImpl");
        FactoryFinder.setFactory(FactoryFinder.LIFECYCLE_FACTORY,
                                 "com.sun.faces.lifecycle.LifecycleFactoryImpl");
        FactoryFinder.setFactory(FactoryFinder.RENDER_KIT_FACTORY,
                                 "com.sun.faces.renderkit.RenderKitFactoryImpl");

	 ApplicationAssociate.clearInstance(storeSC.getServletContextWrapper());
        Util.verifyFactoriesAndInitDefaultRenderKit(
            facesTestCase.getConfig().getServletContext());

        facesContextFactory = (FacesContextFactory)
            FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
        assert (null != facesContextFactory);

        // Since we run using tomcat's deploy targets, we must obtain the
        // absolute path to where we are to write our output files.
        String testRootDir =
            facesTestCase.getConfig().getServletContext().getInitParameter(
                "testRootDir");

        assert (null != testRootDir);
        System.setProperty("testRootDir", testRootDir);

        String jcovFile =
            facesTestCase.getConfig().getServletContext().getInitParameter(
                "jcovFile");

        assert (null != jcovFile);
        System.setProperty("jcovFile", jcovFile);

        // See if the testcase wants to have its output sent to a file.
        if (facesTestCase.sendResponseToFile()) {
            response =
                new FileOutputResponseWrapper(facesTestCase.getResponse());
        } else {
            response = facesTestCase.getResponse();
        }

        LifecycleFactory factory = (LifecycleFactory)
            FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
        assert (null != factory);
        lifecycle = factory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
        assert (null != lifecycle);


        facesContext =
            facesContextFactory.getFacesContext(facesTestCase.getConfig().
                                                getServletContext(),
                                                facesTestCase.getRequest(),
                                                response, lifecycle);
        assert (null != facesContext);

        if (facesTestCase.sendWriterToFile()) {
            ResponseWriter responseWriter = new FileOutputResponseWriter();
            facesContext.setResponseWriter(responseWriter);
        }
        
        TestBean testBean = new TestBean();
        facesContext.getExternalContext().getSessionMap().put("TestBean",
                                                              testBean);

        Iterator paramNames = getFacesContext().getExternalContext()
            .getRequestParameterNames();
        while (paramNames.hasNext()) {
            String curName = (String) paramNames.next();

            System.out.println(curName + "=" +
                               getFacesContext().getExternalContext().
                               getRequestParameterMap().get(curName));
        }

        ConfigureListener configListener = new ConfigureListener();
        ServletContextEvent e =
            new ServletContextEvent(
                facesTestCase.getConfig().getServletContext());

        // make sure this gets called once per ServletContext instance.
        if (null ==
            (facesTestCase.getConfig().getServletContext().
            getAttribute(FacesServlet.CONFIG_FILES_ATTR))) {

            configListener.contextInitialized(e);
        }

	storeSC.setServletContext(facesTestCase.getConfig().getServletContext());

    }


    public void tearDown() {
        // make sure this gets called!
        if ( facesTestCase.getConfig().getServletContext() != null ) {
            facesTestCase.getConfig().getServletContext()
                .removeAttribute(RIConstants.HTML_BASIC_RENDER_KIT);
        }
        
        ConfigureListener configListener = new ConfigureListener();
        ServletContextEvent e =
            new ServletContextEvent(
                facesTestCase.getConfig().getServletContext());
        configListener.contextDestroyed(e);

        // make sure session is not null. It will null in case release
        // was invoked.
        try {
            if (facesContext.getExternalContext() != null) {
                if (facesContext.getExternalContext().getSession(true) != null) {
                    facesContext.getExternalContext().getSessionMap().remove(
                        "TestBean");
                }
            }
        } catch (IllegalStateException ie) {
        }
    }

    
    public boolean verifyExpectedOutput() {
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

        if (facesTestCase.sendResponseToFile()) {
            outputFileName = FileOutputResponseWrapper.FACES_RESPONSE_FILENAME;
        } else {
            outputFileName = FileOutputResponseWriter.RESPONSE_WRITER_FILENAME;
        }
        correctFileName = FileOutputResponseWriter.FACES_RESPONSE_ROOT +
            facesTestCase.getExpectedOutputFilename();

        errorMessage = "File Comparison failed: diff -u " + outputFileName +
            " " +
            correctFileName;

        ArrayList ignoreList = null;
        String[] ignore = null;

        if (null != (ignore = facesTestCase.getLinesToIgnore())) {
            ignoreList = new ArrayList();
            for (int i = 0; i < ignore.length; i++) {
                ignoreList.add(ignore[i]);
            }
        }

        try {
            result =
                cf.filesIdentical(outputFileName, correctFileName, ignoreList);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        if (!result) {
            System.out.println(errorMessage);
        }
        System.out.println("VERIFY:" + result);
        return result;
    }


    /**
     * This utiity method searches the specified file (line by line) for the occurence
     * of the specified string.
     */
    public boolean verifyExpectedStringInOutput(String str) {
        boolean exists = false;
        String outputFileName = null;

        // If this testcase doesn't deal with output file(s)
        if (!facesTestCase.sendResponseToFile() &&
            (!facesTestCase.sendWriterToFile()) &&
            (null == facesTestCase.getExpectedOutputFilename())) {
            return true;
        }

        if (facesTestCase.sendResponseToFile()) {
            outputFileName = FileOutputResponseWrapper.FACES_RESPONSE_FILENAME;
        } else {
            outputFileName = FileOutputResponseWriter.RESPONSE_WRITER_FILENAME;
        }

        try {
            File fileToCheck = new File(outputFileName);
            FileReader fileReader = new FileReader(fileToCheck);
            LineNumberReader lineReader = new LineNumberReader(fileReader);
            String line = lineReader.readLine().trim();

            while (null != line) {
                if (line.indexOf(str) >= 0) {
                    exists = true;
                    break;
                }
                line = lineReader.readLine();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return exists;
    }


    public boolean isMember(String toTest, String[] set) {
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

    public boolean isSubset(String[] subset, Iterator superset) {
        int i, len = subset.length;
        boolean[] hits = new boolean[len];
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
     *
     * @return true if every attribute in in request1 has an analog in
     *         request2, with the same value as in request1, and the converse is true
     *         as well.
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
            } else if (null != valA || null != valB) {
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
            } else if (null != valA || null != valB) {
                // one of the values is null, therefore, not equal, return false
                return false;
            }
        }
        return true;
    }


    public void loadFromInitParam(String paramValue) {
        final String paramVal = paramValue;

        // clear out the attr that was set in the servletcontext attr set.
        facesTestCase.getConfig().getServletContext().removeAttribute(
            FacesServlet.CONFIG_FILES_ATTR);
	ApplicationAssociate.clearInstance(facesContext.getExternalContext());
        // clear out the renderKit factory
        FactoryFinder.releaseFactories();

        // work around a bug in cactus where calling
        // config.setInitParameter() doesn't cause
        // servletContext.getInitParameter() to relfect the call.

        ServletContextWrapper sc =
            new ServletContextWrapper(
                facesTestCase.getConfig().getServletContext()) {
                public String getInitParameter(String theName) {
                    if (null != theName &&
                        theName.equals(FacesServlet.CONFIG_FILES_ATTR)) {
                        return paramVal;
                    }
                    return super.getInitParameter(theName);
                }
            };

        ConfigureListener configListener = new ConfigureListener();
        ServletContextEvent e =
            new ServletContextEvent(sc);
        configListener.contextDestroyed(e);
        configListener.contextInitialized(e);
    }

    public Object wrapRequestToHideParameters() {
	Object oldRequest = 
	    getFacesContext().getExternalContext().getRequest();
	
	HttpServletRequest wrapper = 
	    new HttpServletRequestWrapper((HttpServletRequest)oldRequest) {
		public java.util.Enumeration getParameterNames() {
		    return new java.util.Enumeration() {
			    public boolean hasMoreElements() {
				return( false );
			    }
			    
			    public Object nextElement() {
				return new java.util.NoSuchElementException();
			    }
			};
		}
	    };
	getFacesContext().getExternalContext().setRequest(wrapper);
	return oldRequest;
    }

    public void unwrapRequestToShowParameters(Object oldRequest) {
	getFacesContext().getExternalContext().setRequest(oldRequest);
    }


} // end of class FacesTestCaseService
