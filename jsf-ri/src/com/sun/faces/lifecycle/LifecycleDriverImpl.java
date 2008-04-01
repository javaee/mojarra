/*
 * $Id: LifecycleDriverImpl.java,v 1.7 2002/04/15 20:11:02 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// LifecycleDriverImpl.java

package com.sun.faces.lifecycle;

import java.io.IOException;
import java.util.Vector;
import java.util.Iterator;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.sun.faces.util.Util;
import com.sun.faces.treebuilder.TreeEngine;

import javax.faces.Constants;
import javax.faces.ObjectManager;
import javax.faces.AbstractFactory;
import javax.faces.FacesException;
import javax.faces.ConverterManager;
import javax.faces.LifecycleDriver;
import javax.faces.FacesContext;
import javax.faces.TreeNavigator;
import javax.faces.LifecycleStage;
import javax.faces.UIPage;
import javax.faces.UIComponent;

/**
 *

 *  <B>LifecycleDriverImpl</B> Runs thru the faces page lifecycle stages.

 * <B>Lifetime And Scope</B> <P>There is one instance of this per faces
 * webapp.</P>

 *
 * @version $Id: LifecycleDriverImpl.java,v 1.7 2002/04/15 20:11:02 jvisvanathan Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class LifecycleDriverImpl extends Object implements LifecycleDriver
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

protected ServletContext servletContext = null;
protected Vector stages;
protected LifecycleStage disposeStage;

/**

* Allows us to start rendering.  Usually this just calls _jspService().

*/

//
// Constructors and Initializers    
//

public LifecycleDriverImpl()
{
    super();
    stages = new Vector();
}

//
// Class methods
//

//
// Helper Methods
//

/**

* PRECONDITION: called from init(). <P>

* POSTCONDITION: The following instances exist under the respective keys
* and scopes in the ObjectManager. <P>

* <CODE><PRE>
Constants.REF_ABSTRACTFACTORY application
Constants.REF_OBJECTACCESSORFACTORY application
Constants.REF_NAVIGATIONHANDLERFACTORY application
Constants.REF_CONVERTERMANAGER application
Constants.REF_TREEENGINE application
* </PRE></CODE> <P>

*/

protected void initFactories(ObjectManager objectManager)
{
    AbstractFactory abstractFactory = (AbstractFactory)
	objectManager.get(Constants.REF_ABSTRACTFACTORY);
    Assert.assert_it( null != servletContext );
    
    ConverterManager converterManager;

    // Step 1 create an instance of ConverterManager
    // and put it in Application scope.
    converterManager = abstractFactory.newConverterManager(servletContext);
    Assert.assert_it(converterManager != null );
    objectManager.put(servletContext,
            Constants.REF_CONVERTERMANAGER, converterManager);
    
    // Step 2, put the treeEngine in the OM ApplicationScope
    TreeEngine treeEngine = 
	new com.sun.faces.treebuilder.TreeEngineImpl(servletContext);
    Assert.assert_it(null != treeEngine);
    objectManager.put(servletContext, Constants.REF_TREEENGINE, 
		      treeEngine);

    // Step 3 create a default Message Factory and put it in Global scope
    javax.faces.MessageFactory mf = abstractFactory.newMessageFactory();
    Assert.assert_it(null != mf);
    mf.setClassLoader(this.getClass().getClassLoader());
    objectManager.put(servletContext,
            Constants.DEFAULT_MESSAGE_FACTORY_ID, mf);

}

/**

* PRECONDITION: initFactories has returned successfully <P>

* POSTCONDITION: the stages Vector has been initialized with the
* lifecycle stages.

*/

protected void initStages(ObjectManager objectManager)
{
    stages.add(new GenericLifecycleStage(this, "init"));
    stages.add(new RestoreStateLifecycleStage(this, "updateState"));
    stages.add(new ApplyNewValuesLifecycleStage(this, "applyNewValues"));
    stages.add(new ProcessEventsLifecycleStage(this, "processEvents"));
    stages.add(new GenericLifecycleStage(this, "preRender"));
    stages.add(new SaveStateLifecycleStage(this, "saveState"));
    stages.add(new RenderLifecycleStage(this, "render"));
    disposeStage = new GenericLifecycleStage(this, "dispose");
}

//
// General Methods
//

/**

* PRECONDITION: This method is called once per LifecycleDriverImpl
* instance. <P>

* POSTCONDITION: An ObjectManager instance exists and is in the
* ServletContext attr set under the key Constants.REF_OBJECTMANAGER.
* The postCondition of initFactories() is met.

*/

public void init(ServletContext newServletContext) throws ServletException
{
    ParameterCheck.nonNull(newServletContext);
    
    // init must only be called once per LifecycleDriverImpl instance.
    Assert.assert_it(null == servletContext);
    
    servletContext = newServletContext;

    // Uncomment this to cause the program to hang forever, until
    // you go in the debugger and un-hang it.
    // com.sun.faces.util.DebugUtil.waitForDebugger();
    
    ObjectManager objectManager;
    AbstractFactory abstractFactory;
    
    Assert.assert_it(null != servletContext);
    
    // Step 1: Create the singleton ObjectManager instance and put it
    // in the servletContext.
    
    objectManager = (ObjectManager) servletContext.getAttribute(
								Constants.REF_OBJECTMANAGER);
    // The objectManager must not exist at this point.  It is an error
    // if it does exist.
    Assert.assert_it(null == objectManager);

    // Step 1.5 create the AbstractFactory
    abstractFactory = new AbstractFactory();
    Assert.assert_it(null != abstractFactory);

    
    // create the ObjectManager
    try {
	objectManager = (ObjectManager)
	    abstractFactory.newInstance(Constants.REF_OBJECTMANAGER,
					servletContext);
    } catch (FacesException e) {
	throw new ServletException(e.getMessage());
    }
    Assert.assert_it(null != objectManager);
    
    // Store the ObjectManager in the servlet context
    // (application scope).
    //
    servletContext.setAttribute(Constants.REF_OBJECTMANAGER, objectManager);

    // Put the AbstractFactory in global scope
    objectManager.put(servletContext,
		      Constants.REF_ABSTRACTFACTORY, abstractFactory);

    initFactories(objectManager);
    initStages(objectManager);
}

public void destroy()
{
    Assert.assert_it(null != servletContext);

    stages.clear();
    servletContext.removeAttribute(Constants.REF_OBJECTMANAGER);
}

//
// Methods from LifecycleDriver
//

/**

* PRECONDITION: FacesContext is properly initialized for this request. <P>

* POSTCONDITION: TreeNavigator is fully initialized and is a complete
* tree from the JSP page addressed by the FacesContext.

*/

public TreeNavigator wireUp(FacesContext facesContext, UIPage root) throws IOException
{
    TreeNavigator result = null;
    ObjectManager objectManager = facesContext.getObjectManager();
    HttpServletRequest request =(HttpServletRequest)facesContext.getRequest();
    String requestURI = request.getRequestURI();
    TreeEngine treeEng = (TreeEngine) 
	objectManager.get(Constants.REF_TREEENGINE);
    Assert.assert_it(null != treeEng);

    result = treeEng.getTreeForURI(facesContext, root, requestURI);
    if (null == result) {
	throw new IOException("Can't get tree for: " + requestURI);
    }
    objectManager.put(facesContext.getRequest(), 
		      Constants.REF_TREENAVIGATOR, result);

    return result;
}

public void executeLifecycle(FacesContext facesContext, 
			     TreeNavigator treeNav) throws FacesException, IOException
{
    Iterator life = stages.iterator();
    boolean keepGoing = true;

    try {
	while (keepGoing && life.hasNext()) {
	    treeNav.reset();
	    keepGoing = ((LifecycleStage)life.next()).execute(facesContext, treeNav);
	}
    }
    finally {
	treeNav.reset();
	disposeStage.execute(facesContext, treeNav);
    }
	    
}

boolean traverseTreeInvokingMethod(FacesContext facesContext, TreeNavigator treeNav,
				String cycleName)
{
    boolean result = true;
    UIComponent next = null;

    while (null != (next = treeNav.getNextStart())) {
        if (cycleName.equals("init")) {
            next.init(facesContext);
        } else if (cycleName.equals("preRender")) {
            next.preRender(facesContext);
        } else if (cycleName.equals("dispose")) {
            next.dispose(facesContext);
        } 
    }
    treeNav.reset();
    return result;
}


// The testcase for this class is TestLifecycleDriverImpl.java 


} // end of class LifecycleDriverImpl
