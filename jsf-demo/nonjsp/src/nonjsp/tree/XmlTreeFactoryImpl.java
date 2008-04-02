/*
 * $Id: XmlTreeFactoryImpl.java,v 1.3 2003/03/27 19:44:08 jvisvanathan Exp $
 */

/*
 * Copyright 2002, 2003 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * 
 * - Redistribution in binary form must reproduce the above
 *   copyright notice, this list of conditions and the following
 *   disclaimer in the documentation and/or other materials
 *   provided with the distribution.
 *    
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *  
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 * DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR
 * RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THIS SOFTWARE OR
 * ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 * SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 * THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 * BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *  
 * You acknowledge that this software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
 */

// XmlTreeFactoryImpl.java

package nonjsp.tree;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.tree.TreeFactory;
import javax.faces.tree.Tree;
import javax.faces.FacesException;
import javax.faces.component.UINamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import java.util.Iterator;
import java.util.Set;
import java.util.ArrayList;

import java.io.InputStream;
import java.net.URL;

import org.apache.commons.digester.RuleSetBase;
import org.apache.commons.digester.Digester;
import org.apache.commons.logging.impl.SimpleLog;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 *
 * <B>XmlTreeFactoryImpl</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: XmlTreeFactoryImpl.java,v 1.3 2003/03/27 19:44:08 jvisvanathan Exp $
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
    // Log instance for this class
    protected static Log log = LogFactory.getLog(XmlTreeFactoryImpl.class);


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

protected Iterator getTreeIdsFromSuffix(FacesContext facesContext,
					String suffix)
{
    ParameterCheck.nonNull(suffix);

    Iterator resourcePaths = null;
    Set resourceSet = null;
    String curResource = null;
    ArrayList resources = new ArrayList();
    resourceSet = facesContext.getExternalContext().getResourcePaths("/");
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

public Tree getTree(FacesContext facesContext,
		    String treeId) throws FacesException
{
    if (log.isTraceEnabled()) {
        log.trace("treeId: " + treeId);
    }
    Tree result = null;
    InputStream treeInput = null;
    UINamingContainer root = null;
    RuleSetBase ruleSet = null;

    root = new UINamingContainer() {
	public String getComponentType() { return "Root"; }
    };
    root.setComponentId("root");

    if (null == treeId) {
	// PENDING(edburns): need name for default tree
        // PENDING(rogerk) : what to specify for page url
        // (last parameter)????
	result = new SimpleTreeImpl(facesContext, root, "default");
	return result;
    }

    try {
       treeInput = facesContext.getExternalContext().getResourceAsStream(treeId);
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

    digester.push(root);
    try {
	root = (UINamingContainer)digester.parse(treeInput);
    } catch (Throwable e) {
	throw new FacesException("Can't parse stream for " + treeId, e);
    }


    //Print tree for debugging 
    if (log.isDebugEnabled()) {
        printTree(root);
    }

    result = new SimpleTreeImpl(facesContext, root, treeId);

    return result;
}

    private void printTree(UIComponent uic) {
        Iterator kids = uic.getChildren();
        while (kids.hasNext()) {
            printTree((UIComponent) kids.next());
        }
        log.debug("tree: " + uic.getComponentId());
    }

// The testcase for this class is TestXmlTreeFactoryImpl.java 

} // end of class XmlTreeFactoryImpl
