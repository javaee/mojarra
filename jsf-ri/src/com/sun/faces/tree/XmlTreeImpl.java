/*
 * $Id: XmlTreeImpl.java,v 1.7 2002/08/01 21:18:42 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// XmlTreeImpl.java

package com.sun.faces.tree;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.component.UIComponent;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.tree.Tree;
import javax.faces.FactoryFinder;

import javax.servlet.ServletContext;

import com.sun.faces.RIConstants;

/**
 *
 *  <B>XmlTreeImpl</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: XmlTreeImpl.java,v 1.7 2002/08/01 21:18:42 jvisvanathan Exp $
 * 
 * @see	javax.faces.tree.Tree
 *
 */

public class XmlTreeImpl extends SimpleTreeImpl
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

protected String pageUrl = null;

//
// Constructors and Initializers    
//

/**

* PRECONDITION: the ServletContext has been initialized with all the
* required factories.

*/ 

public XmlTreeImpl(ServletContext context, UIComponent newRoot, 
		   String newTreeId, String pageUrl)
{
    super(context, newRoot, newTreeId);
    ParameterCheck.nonNull(pageUrl);

    setPageUrl(pageUrl);
}

//
// Class methods
//

//
// General Methods
//

void setPageUrl(String pageUrl) {
    ParameterCheck.nonNull(pageUrl);
    this.pageUrl = pageUrl;
}

//
// Methods from Tree
//

public String getPageUrl() {
    return pageUrl;
}
// The testcase for this class is TestXmlTreeImpl.java 


} // end of class XmlTreeImpl
