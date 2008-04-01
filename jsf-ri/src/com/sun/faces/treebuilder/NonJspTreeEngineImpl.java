/*
 * $Id: NonJspTreeEngineImpl.java,v 1.1 2002/04/25 22:42:21 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// NonJspTreeEngine.java

package com.sun.faces.treebuilder;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.servlet.ServletContext;

import org.apache.jasper_hacked.compiler.PreParser;

import javax.faces.UIComponent;
import javax.faces.FacesContext;
import javax.faces.TreeNavigator;
import javax.faces.UIPage;
import javax.faces.FacesFactory;
import javax.faces.FacesException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletContext;

import java.util.Map;
import java.io.InputStream;

/**
 *

 * @version $Id: NonJspTreeEngineImpl.java,v 1.1 2002/04/25 22:42:21 eburns Exp $
 * 
 * @see	com.sun.faces.treebuilder.TreeEngine

 *
 */

public class NonJspTreeEngineImpl extends Object implements TreeEngine
{
//
// Protected Constants
//

protected static final String TREE_SUFFIX = ".uiml";

//
// Class Variables
//

//
// Instance Variables
//

// Attribute Instance Variables

// Relationship Instance Variables

private ServletContext servletContext;
private BuildComponentFromTag componentBuilder = null;

//
// Constructors and Initializers    
//

public NonJspTreeEngineImpl(ServletContext newServletContext)
{
    ParameterCheck.nonNull(newServletContext);
    servletContext = newServletContext;
}

//
// Class methods
//

//
// helper methods
//

//
// General Methods
//

public TreeNavigator getTreeForURI(FacesContext fc, UIPage root,
				   String requestURI) {
    ParameterCheck.nonNull(fc);
    ParameterCheck.nonNull(root);
    ParameterCheck.nonNull(requestURI);

    TreeNavigator result = null;
    InputStream treeInput = null;

    // PENDING(edburns): do something more intelligent than parsing the
    // page each time.  
    if ((null != requestURI) && requestURI.endsWith(TREE_SUFFIX)) {
	// turn the requestURI into an InputStream
	NonJspTreeBuilder treeBuilder = new NonJspTreeBuilder(componentBuilder,
							      fc, root,
							      treeInput);
	treeBuilder.printTree(root, System.out);
	if (null != root) {
	    result = new TreeNavigatorImpl(root);
	}
    }
    return result;
}

public static class Factory extends Object implements FacesFactory
{

//
// Methods from FacesFactory
//

public Object newInstance(String facesName, ServletRequest req, ServletResponse res) throws FacesException
{
    throw new FacesException("Can't create TreeEngine from request and response.");
}

public Object newInstance(String facesName, ServletContext ctx) throws FacesException
{
    return new NonJspTreeEngineImpl(ctx);
}

public Object newInstance(String facesName) throws FacesException
{
    throw new FacesException("Can't create TreeEngine from nothing.");
}

public Object newInstance(String facesName, Map args) throws FacesException
{
    throw new FacesException("Can't create TreeEngine from map.");
}


}

// The testcase for this class is TestTreebuilder.java 


} // end of class NonJspTreeEngineImpl
