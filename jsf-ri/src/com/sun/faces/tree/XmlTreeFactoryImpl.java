/*
 * $Id: XmlTreeFactoryImpl.java,v 1.4 2002/06/12 23:51:09 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// XmlTreeFactoryImpl.java

package com.sun.faces.tree;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.tree.TreeFactory;
import javax.faces.tree.Tree;
import javax.faces.FacesException;
import javax.faces.component.UIComponentBase;

import javax.servlet.ServletContext;

import java.util.Iterator;
import java.util.Set;
import java.util.ArrayList;

import java.io.InputStream;
import java.net.URL;


import org.apache.commons.digester.RuleSetBase;
import org.apache.commons.digester.Digester;
import org.apache.commons.logging.impl.SimpleLog;

/**
 *
 *  <B>XmlTreeFactoryImpl</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: XmlTreeFactoryImpl.java,v 1.4 2002/06/12 23:51:09 jvisvanathan Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class XmlTreeFactoryImpl extends TreeFactory
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

//PENDING(rogerk) maybe config file?
/**
  * Should we use a validating XML parser to read the configuration file?
  */
protected boolean validate = false;

/**
 * The set of public identifiers, and corresponding resource names, for
 * the versions of the configuration file DTDs that we know about.  There
 * <strong>MUST</strong> be an even number of Strings in this list!
 * Only used if you are validating against DTD.  Could be read from config
 * file instead.
 */
protected String registrations[] = {
    "-//UIT//DTD UIML 2.0 Draft//EN",
    "UIML2_0d.dtd"
};



// Relationship Instance Variables

protected XmlDialectProvider dialectProvider = null;

//
// Constructors and Initializers    
//

public XmlTreeFactoryImpl()
{
    super();

    dialectProvider = new XulDialectProvider();
}

//
// Class methods
//

//
// General Methods
//

protected void setXmlDialectProvider(XmlDialectProvider provider)
{
    ParameterCheck.nonNull(provider);

    dialectProvider = provider;
}

/**

* @return an Iterator over the resources in the current web app that
* have the given file suffix.

*/

protected Iterator getTreeIdsFromSuffix(ServletContext servletContext,
					String suffix)
{
    ParameterCheck.nonNull(suffix);

    Iterator resourcePaths = null;
    Set resourceSet = null;
    String curResource = null;
    ArrayList resources = new ArrayList();

    resourceSet = servletContext.getResourcePaths("/");
    resourcePaths = resourceSet.iterator();
    while (resourcePaths.hasNext()) {
	curResource = (String) resourcePaths.next();
	if (null != curResource && -1 != curResource.indexOf(suffix)) {
	    resources.add(curResource);
	}
    }
    
    return resources.iterator();

}

//
// Abstract Methods, implemented by subclass
//

//
// Methods from TreeFactory
//

public Tree createTree(ServletContext servletContext,
		       String treeId) throws FacesException
{
    Tree result = null;
    InputStream treeInput = null;
    UIComponentBase root = null;
    RuleSetBase ruleSet = null;

    root = new UIComponentBase() {
	public String getComponentType() { return "Root"; }
    };
    root.setComponentId("root");

    if (null == treeId) {
	// PENDING(edburns): need name for default tree
	result = new XmlTreeImpl(servletContext, root, "default");
	return result;
    }

    try {
	treeInput = 
	    servletContext.getResourceAsStream(treeId);
	if (null == treeInput) {
	    throw new NullPointerException();
	}
    }
    catch (Throwable e) {
	throw new FacesException("Can't get stream for " + treeId, e);
    }

    // PENDING(edburns): can this digester instance be maintained as an
    // ivar?

    Digester digester = new Digester();

    // SimpleLog implements the Log interface (from commons.logging).
    // This replaces deprecated "Digester.setDebug" method.
    // PENDING(rogerk) Perhaps the logging level should be configurable..
    // For debugging, you can set the log level to 
    // "SimpleLog.LOG_LEVEL_DEBUG". 
    //
    SimpleLog sLog = new SimpleLog("digesterLog");
    sLog.setLevel(SimpleLog.LOG_LEVEL_ERROR);
    digester.setLogger(sLog);

    digester.setNamespaceAware(true);
    
    digester.setValidating(validate);

    ruleSet = dialectProvider.getRuleSet();

    Assert.assert_it(null != ruleSet);

    digester.addRuleSet(ruleSet);

    if (validate) {
	for (int i = 0; i < registrations.length; i += 2) {
	    URL url = this.getClass().getResource(registrations[i+1]);
	    if (url != null) {
		digester.register(registrations[i], url.toString());
	    }
	}
    }

    // push the topmost component onto the stack so we have
    // access to it after parsing is complete.
    //
    digester.push(root);
    
    try {
	root = (UIComponentBase)digester.parse(treeInput);
    } catch (Throwable e) {
	throw new FacesException("Can't parse stream for " + treeId, e);
    }

    result = new XmlTreeImpl(servletContext, root, treeId);

    return result;
}

public Iterator getTreeIds(ServletContext context)
{
    Assert.assert_it(null != dialectProvider);
    return getTreeIdsFromSuffix(context, dialectProvider.getSuffix());
}

// The testcase for this class is TestXmlTreeFactoryImpl.java 


} // end of class XmlTreeFactoryImpl
