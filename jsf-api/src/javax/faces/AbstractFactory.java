/*
 * $Id: AbstractFactory.java,v 1.2 2002/04/12 23:15:46 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces;

// AbstractFactory.java

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletContext;

/**
 *

 *  <B>AbstractFactory</B> extends AbstractFactoryBase to allow runtime
 *  association of an API class with an implementation class.  <P>

 * <B>Usage:</B> <P>

<CODE><PRE>
    AbstractFactory abstractFactory = new AbstractFactory();
    ObjectManager objectManager = null;
    FacesContext facesContext = null;
    UITextEntry textEntry = null;
    RandomThingy random = null;
    Map randomArgs;
    RandomThingyFactory factory = new RandomThingyFactory();
    String RANDOM = "randomThingyName";

    facesContext =
        abstractFactory.newFacesContext(request, response);
    textEntry = (UITextEntry)
        abstractFactory.newUIComponent(Constants.REF_TEXTENTRY);

    abstractFactory.addFactoryForFacesName(randomThingyFactory, RANDOM);
    randomArgs = new HashMap();
    randomArgs.put("argOne", new Integer(3));
    randomArgs.put("argTwo", new Boolean(false));
    //...
    random = (RandomThingy) abstractFactory.newInstance(RANDOM, randomArgs)
</PRE></CODE><P>

* <B>Algorithm:</B> <P>

* On instantiation, this class creates an in memory store of known
* associations between 1. a String representing a logical class
* identifier (the <B>facesName</B>), and 2. a factory for creating
* instances of that class.  This factory must implement the
* <B>FacesFactory</B> interface.  When
* <CODE>newInstance(facesName,...)</CODE> is called, this store of
* associations is consulted, in concert with
* <CODE>FactoryFinder.find()</CODE>, to find an instance of
* <B>FacesFactory</B> for the given <B>facesName</B>.  This
* implementation happens to cache the <B>FacesFactory</B> instance so
* that future invocations of <CODE>newInstance()</CODE> do not incurr
* the cost of factory lookup.<P>

 *
 * <B>Lifetime And Scope</B> <P> 

 * There is one instance of <B>AbstractFactory</B> per webapp.  This class is
 * <A
 * HREF="http://whatis.techtarget.com/definition/0,,sid9_gci331590,00.html">thread-safe</A>.
 * It is possible to allow this class to re-populate its in memory store
 * of associations by calling <CODE>destroy()</CODE> followed by
 * <CODE>init()</CODE>. <P>

 * <B>Possible future enhancements</B> <P>

 * Currently, the in memory store of known associations is created from
 * hard coded values.  It is desirable to have the implementation read
 * the associations from a file. <P>

 * FactoryFinder could be folded into AbstractFactoryBase. <P>

 *
 * @version $Id: AbstractFactory.java,v 1.2 2002/04/12 23:15:46 eburns Exp $
 * 
 * @see	javax.faces.FacesFactory
 * @see	javax.faces.FactoryFinder#find
 *
 */

public class AbstractFactory extends AbstractFactoryBase
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

    protected HashMap factoryMap;

//
// Constructors and Initializers    
//

    public AbstractFactory() {
	super();
	init();
    }

    /**

    * Initialize this instance. <P>

    * <B>PRECONDITION:</B> this instance is new, or
    * <CODE>destroy()</CODE> has been called on this instance. <P>

    * <B>POSTCONDITION:</B> this instance is ready to service
    * <CODE>newInstance()</CODE> requests <P>

    * @see javax.faces.AbstractFactory
    * @see javax.faces.AbstractFactory#destroy

    */     

    public void init() {
	factoryMap = new HashMap();
	initFactoryMap();
    }

    /**

    * Free any resources associated with this instance so they may be
    * garbage collected. </P>

    * <B>PRECONDITION:</B> You don't want to use this instance any more
    * (until after calling <CODE>init()</CODE> again). <P>

    * <B>POSTCONDITION:</B> <CODE>init()</CODE> must be called before
    * this instance can service any <CODE>newInstance()</CODE> requests. <P>

    */

    public void destroy() {
	factoryMap.clear();
	factoryMap = null;
    }

    // PENDING(edburns): populate this from config file
    
    protected void initFactoryMap() {
	factoryMap.put(Constants.REF_OBJECTMANAGER, 
		       "com.sun.faces.ObjectManagerFactoryImpl");
	factoryMap.put(Constants.REF_FACESCONTEXT,
		       "com.sun.faces.renderkit.html_basic.HtmlBasicFacesContextFactory");
	factoryMap.put(Constants.DEFAULT_MESSAGE_FACTORY_ID,
		       "com.sun.faces.MessageFactoryImpl");
	factoryMap.put(Constants.MESSAGE_LIST_ID,
		       "com.sun.faces.MessageListImpl");
	factoryMap.put(Constants.REF_EVENTQUEUE,
		       "com.sun.faces.EventQueueFactoryImpl");
    }

//
// Class methods
//

//
// General Methods
//

    protected FacesFactory getFactoryForFacesName(String facesName) throws FactoryConfigurationError {
	Object factory;
	FacesFactory result = null;
	String factoryStr;
	boolean putInMap = false;

	// See if we've encountered this facesName before
	if (null == (factory = factoryMap.get(facesName))) {
	    // if not, try to find a factory for this facesName
	    result = (FacesFactory) FactoryFinder.find(facesName, facesName);
	    // mark it so the result is put in the map
	    putInMap = true;
	    return result;
	}

	// If we need to do lazy factory finding
	if (factory instanceof String) {
	    factoryStr = (String) factory;
	    // find the factory based on the value from the map
	    result = (FacesFactory) FactoryFinder.find(factoryStr, factoryStr);
	    // mark it so the result is put in the map
	    putInMap = true;
	}
	else {
	    // we the lazy factory finding has already been done
	    result = (FacesFactory) factory;
	}

	// If we need to put the factory in the map under this facesName
	if (putInMap && null != result) {
	    factoryMap.put(facesName, result);
	}

	return result;
    }

    //
    // Convenience methods for common faces classes
    //

    public final EventQueue newEventQueue() throws FactoryConfigurationError, FacesException {
	return (EventQueue) newInstance(Constants.REF_EVENTQUEUE);
    }

    public final MessageFactory newMessageFactory() throws FactoryConfigurationError, FacesException {
	return (MessageFactory) newInstance(Constants.DEFAULT_MESSAGE_FACTORY_ID);
    }

    public final MessageList newMessageList() throws FactoryConfigurationError, FacesException {
	return (MessageList) newInstance(Constants.MESSAGE_LIST_ID);
    }

    public final FacesContext newFacesContext(ServletRequest req, 
					ServletResponse resp) throws FactoryConfigurationError, FacesException {
	return (FacesContext) newInstance(Constants.REF_FACESCONTEXT, req, 
					  resp);
    }

    // 
    // General methods
    //
	
    /**

    * @see javax.faces.AbstractFactoryBase

    */

    public Object addFactoryForFacesName(FacesFactory factory,
					 String facesName) {
	return factoryMap.put(facesName, factory);
    }

} // end of class AbstractFactory


// The testcase for this class is com.sun.faces.TestAbstractFactory
