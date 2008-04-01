/*
 * $Id: LifecycleDriverImpl.java,v 1.3 2002/03/15 23:29:48 eburns Exp $
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

import com.sun.faces.EventContextFactory;
import com.sun.faces.ObjectAccessorFactory;
import com.sun.faces.ObjectManagerFactory;
import com.sun.faces.NavigationHandlerFactory;
import com.sun.faces.ConverterManagerFactory;
import com.sun.faces.util.Util;
import com.sun.faces.treebuilder.TreeEngine;

import javax.faces.Constants;
import javax.faces.ObjectManager;
import javax.faces.EventQueueFactory;
import javax.faces.RenderContextFactory;
import javax.faces.FacesException;
import javax.faces.ConverterManager;
import javax.faces.LifecycleDriver;
import javax.faces.RenderContext;
import javax.faces.EventContext;
import javax.faces.FacesContext;
import javax.faces.TreeNavigator;
import javax.faces.LifecycleStage;
import javax.faces.UIPage;

/**
 *

 *  <B>LifecycleDriverImpl</B> Runs thru the faces page lifecycle stages.

 * <B>Lifetime And Scope</B> <P>There is one instance of this per faces
 * webapp.</P>

 *
 * @version $Id: LifecycleDriverImpl.java,v 1.3 2002/03/15 23:29:48 eburns Exp $
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
Constants.REF_EVENTQUEUEFACTORY application
Constants.REF_RENDERCONTEXTFACTORY application
Constants.REF_EVENTCONTEXTFACTORY application
Constants.REF_OBJECTACCESSORFACTORY application
Constants.REF_NAVIGATIONHANDLERFACTORY application
Constants.REF_CONVERTERMANAGER application
Constants.REF_TREEENGINE application
* </PRE></CODE> <P>

*/

protected void initFactories(ObjectManager objectManager)
{
    EventQueueFactory eqFactory;
    RenderContextFactory rcFactory;
    EventContextFactory ecFactory;
    ObjectAccessorFactory oaFactory;
    NavigationHandlerFactory nhFactory;
    ConverterManagerFactory cmFactory;
    ConverterManager converterManager;

    Assert.assert_it(null != servletContext);

    // Step 2: Create the EventQueueFactory and put it in the
    // ObjectManager in Global Scope.
    
    eqFactory = (EventQueueFactory)
	objectManager.get(Constants.REF_EVENTQUEUEFACTORY);
    
    // The EventQueueFactory must not exist at this point.  It is an
    // error if it does exist.
    Assert.assert_it(null == eqFactory);
    
    eqFactory = EventQueueFactory.newInstance();
    Assert.assert_it(null != eqFactory);
    objectManager.put(servletContext,
		      Constants.REF_EVENTQUEUEFACTORY, eqFactory);
    
    // Step 3: Create the RenderContextFactory and put it in the
    // ObjectManager in GlobalScope
    
    rcFactory = (RenderContextFactory)objectManager.get(
							Constants.REF_RENDERCONTEXTFACTORY);
    // The RenderContextFactory must not exist at this point.  It is an
    // error if it does exist.
    Assert.assert_it(null == rcFactory);
    
    rcFactory = RenderContextFactory.newInstance();
    Assert.assert_it(null != rcFactory);
    objectManager.put(servletContext,
		      Constants.REF_RENDERCONTEXTFACTORY, rcFactory);
    
    // Step 4: Create the EventContextFactory and put it in the
    // ObjectManager in GlobalScope
    ecFactory = (EventContextFactory)objectManager.get(
						       Constants.REF_EVENTCONTEXTFACTORY);
    // The EventContextFactory must not exist at this point.  It is an
    // error if it does exist.
    Assert.assert_it(null == ecFactory);
    
    ecFactory = EventContextFactory.newInstance();
    Assert.assert_it(null != ecFactory);
    objectManager.put(servletContext,
		      Constants.REF_EVENTCONTEXTFACTORY, ecFactory);
    
    // Step 5: Create the ObjectAccessorFactory and put it in the
    // ObjectManager in GlobalScope
    oaFactory = (ObjectAccessorFactory)objectManager.get(
							 Constants.REF_OBJECTACCESSORFACTORY);
    // The ObjectAccessorFactory must not exist at this point.  It is an
    // error if it does exist.
    Assert.assert_it(null == oaFactory);
    
    oaFactory = ObjectAccessorFactory.newInstance();
    Assert.assert_it(null != oaFactory);
    objectManager.put(servletContext,
		      Constants.REF_OBJECTACCESSORFACTORY, oaFactory);
    
    // Step 6 create an instance of navigationHandlerFactory
    // and put it in Application scope.
    nhFactory = (NavigationHandlerFactory)objectManager.get(
							    Constants.REF_NAVIGATIONHANDLERFACTORY);
    
    // The NavigationHandlerFactory must not exist at this point.  It is an
    // error if it does exist.
    Assert.assert_it(null == nhFactory);
    
    nhFactory = NavigationHandlerFactory.newInstance();
    Assert.assert_it(null != nhFactory);
    objectManager.put(servletContext,
		      Constants.REF_NAVIGATIONHANDLERFACTORY, nhFactory);

    // Step 7 create an instance of ConverterManager
    // and put it in Application scope.
    converterManager = (ConverterManager)objectManager.get(
							   Constants.REF_CONVERTERMANAGER);
    
    // The converterManager must not exist at this point.  It is an
    // error if it does exist.
    // PENDING(visvan)  ConverterManager should be request scoped
    // to avoid threading issues. 
    Assert.assert_it(null == converterManager);
    
    cmFactory = ConverterManagerFactory.newInstance();
    Assert.assert_it(null != cmFactory);
    
    converterManager = cmFactory.newConverterManager(servletContext);
    objectManager.put(servletContext,
		      Constants.REF_CONVERTERMANAGER, converterManager);
    
    
    // Step 8, put the treeEngine in the OM ApplicationScope
    TreeEngine treeEngine = 
	new com.sun.faces.treebuilder.TreeEngineImpl(servletContext);
    Assert.assert_it(null != treeEngine);
    objectManager.put(servletContext, Constants.REF_TREEENGINE, 
		      treeEngine);

    // Step 9 create a default Message Factory and put it in Global scope
    javax.faces.MessageFactory mf = javax.faces.MessageFactory.newInstance();
    mf.setClassLoader(this.getClass().getClassLoader());
    objectManager.put(servletContext,
            com.sun.faces.MessageListImpl.DEFAULT_MESSAGE_FACTORY_ID, mf);

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
    ObjectManagerFactory omFactory;
    
    Assert.assert_it(null != servletContext);
    
    // Step 1: Create the singleton ObjectManager instance and put it
    // in the servletContext.
    
    objectManager = (ObjectManager) servletContext.getAttribute(
								Constants.REF_OBJECTMANAGER);
    // The objectManager must not exist at this point.  It is an error
    // if it does exist.
    Assert.assert_it(null == objectManager);
    
    // create the ObjectManager
    try {
	omFactory = ObjectManagerFactory.newInstance();
	objectManager = omFactory.newObjectManager(servletContext);
    } catch (FacesException e) {
	throw new ServletException(e.getMessage());
    }
    Assert.assert_it(null != objectManager);
    
    // Store the ObjectManager in the servlet context
    // (application scope).
    //
    servletContext.setAttribute(Constants.REF_OBJECTMANAGER, objectManager);

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

public TreeNavigator wireUp(FacesContext ctx, UIPage root) throws IOException
{
    TreeNavigator result = null;
    RenderContext renderContext = ctx.getRenderContext();
    ObjectManager objectManager = renderContext.getObjectManager();
    HttpServletRequest request =(HttpServletRequest)renderContext.getRequest();
    String requestURI = request.getRequestURI();
    TreeEngine treeEng = (TreeEngine) 
	objectManager.get(Constants.REF_TREEENGINE);
    Assert.assert_it(null != treeEng);

    result = treeEng.getTreeForURI(renderContext, root, requestURI);
    if (null == result) {
	throw new IOException("Can't get tree for: " + requestURI);
    }
    objectManager.put(renderContext.getRequest(), 
		      Constants.REF_TREENAVIGATOR, result);

    return result;
}

public void executeLifecycle(FacesContext ctx, 
			     TreeNavigator treeNav) throws FacesException, IOException
{
    Iterator life = stages.iterator();
    boolean keepGoing = true;

    try {
	while (keepGoing && life.hasNext()) {
	    treeNav.reset();
	    keepGoing = ((LifecycleStage)life.next()).execute(ctx, treeNav);
	}
    }
    finally {
	treeNav.reset();
	disposeStage.execute(ctx, treeNav);
    }
	    
}

boolean traverseTreeInvokingMethod(FacesContext ctx, TreeNavigator treeNav,
				String cycleName)
{
    boolean result = true;
    return result;
}


// The testcase for this class is TestLifecycleDriverImpl.java 


} // end of class LifecycleDriverImpl
