/*
 * $Id: TreeEngineImpl.java,v 1.3 2002/03/15 23:29:49 eburns Exp $
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
import javax.faces.UIPage;

/**
 *

 * @version $Id: TreeEngineImpl.java,v 1.3 2002/03/15 23:29:49 eburns Exp $
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

public TreeNavigator getTreeForURI(RenderContext rc, UIPage root,
				   String requestURI) {
    ParameterCheck.nonNull(rc);
    ParameterCheck.nonNull(root);
    ParameterCheck.nonNull(requestURI);

    TreeNavigator result = null;

    // PENDING(edburns): do something more intelligent than parsing the
    // page each time.  
    if ((null != requestURI) && requestURI.endsWith(".jsp")) {
	requestURI = fixURI(requestURI);
	TreeBuilder treeBuilder = new TreeBuilder(rc, root, requestURI);
	preParser.addJspParseListener(treeBuilder);
	preParser.preParse(requestURI);
	preParser.removeJspParseListener(treeBuilder);
	//treeBuilder.printTree(root, System.out);
	if (null != root) {
	    result = new TreeNavigatorImpl(root);
	}
    }
    return result;
}

// The testcase for this class is TestTreebuilder.java 


} // end of class TreeEngineImpl
