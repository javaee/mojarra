/*
 * $Id: TreeEngineImpl.java,v 1.2 2002/03/08 22:16:09 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TreeEngine.java

package com.sun.faces.treebuilder;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.servlet.ServletContext;

import org.apache.jasper_hacked.compiler.PreParser;

import javax.faces.UIComponent;
import javax.faces.RenderContext;
import javax.faces.TreeNavigator;

/**
 *

 * @version $Id: TreeEngineImpl.java,v 1.2 2002/03/08 22:16:09 eburns Exp $
 * 
 * @see	com.sun.faces.treebuilder.TreeEngine

 *
 */

public class TreeEngineImpl extends Object implements TreeEngine
{
//
// Protected Constants
//

//
// Class Variables
//

//
// Instance Variables
//

// Attribute Instance Variables

// Relationship Instance Variables

private ServletContext servletContext;
private PreParser preParser = null;

//
// Constructors and Initializers    
//

public TreeEngineImpl(ServletContext newServletContext)
{
    ParameterCheck.nonNull(newServletContext);
    servletContext = newServletContext;
    preParser = new PreParser(servletContext);
}

//
// Class methods
//

//
// helper methods
//

/**

* Strips off the first part of the path. <P>

* PRECONDITION: requestURI is at least a filename.

*/

public String fixURI(String requestURI) {
    String result = requestURI;
    int i = requestURI.indexOf("/");
    if (-1 != i && requestURI.length() > i) {
	requestURI = requestURI.substring(i + 1);
	i = requestURI.indexOf("/");
	if (-1 != i && requestURI.length() > i) {
	    result = requestURI.substring(i);
	}
    } 
    else {
	// This uri doesn't have a leading slash.  Make sure it does.
	result = "/" + requestURI;
    }
    return result;
}
    
//
// General Methods
//

public TreeNavigator getTreeForURI(RenderContext rc, String requestURI) {
    ParameterCheck.nonNull(rc);

    UIComponent root = null;
    TreeNavigator result = null;

    // PENDING(edburns): do something more intelligent than parsing the
    // page each time.  
    if ((null != requestURI) && requestURI.endsWith(".jsp")) {
	requestURI = fixURI(requestURI);
	TreeBuilder treeBuilder = new TreeBuilder(rc, requestURI);
	preParser.addJspParseListener(treeBuilder);
	preParser.preParse(requestURI);
	preParser.removeJspParseListener(treeBuilder);
	root = treeBuilder.getRoot();
	// treeBuilder.printTree(root, System.out);
	if (null != root) {
	    result = new TreeNavigatorImpl(root);
	}
    }
    return result;
}

// Delete this text and replace the below text with the name of the file
// containing the testcase covering this class.  If this text is here
// someone touching this file is poor software engineer.

// The testcase for this class is TestTreeEngine.java 


} // end of class TreeEngineImpl
