/*
 * $Id: HtmlBasicFacesContext.java,v 1.2 2002/04/11 22:52:41 eburns Exp $
 */


/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// HtmlBasicFacesContext.java

package com.sun.faces.renderkit.html_basic;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

import javax.faces.Constants;
import javax.faces.AbstractFactory;
import javax.faces.FactoryConfigurationError;
import javax.faces.OutputMethod;
import javax.faces.ObjectManager;
import javax.faces.ObjectAccessor;
import javax.faces.FacesContext;
import javax.faces.RenderKit;
import javax.faces.UIComponent;
import javax.faces.FacesException;
import javax.faces.MessageList;
import javax.faces.FacesEvent;
import javax.faces.EventQueue;
import javax.faces.EventDispatcher;
import javax.faces.NavigationHandler;
import javax.faces.ClientCapabilities;
import com.sun.faces.NavigationHandlerFactory;
import javax.faces.UIForm;
import javax.faces.NavigationMap;
import javax.faces.TreeNavigator;

import javax.servlet.ServletRequest;
import javax.servlet.ServletContext;
import javax.servlet.ServletResponse;

import java.util.Locale;
import java.util.Stack;
import java.util.EventObject;

import com.sun.faces.ObjectAccessorFactory;

/**
 *
 *  <B>HtmlBasicFacesContext</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: HtmlBasicFacesContext.java,v 1.2 2002/04/11 22:52:41 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */


public class HtmlBasicFacesContext extends FacesContext {
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

    private RenderKit renderKit = null;
    private OutputMethod outputMethod = null;
    private ClientCapabilities clientCapabilities = null;

    private ServletRequest request;
    private ServletResponse response;
    private ServletContext servletContext;

    private Stack stack;

    private EventQueue eventQueue = null;
    private String formId = null;
    private NavigationHandler navHandler = null;
    private NavigationHandlerFactory navHandlerFactory = null;
    private MessageList _messageList = null;

    /** 
     * This is the owning reference to the ObjectManager
     */

    private ObjectManager objectManager = null;

    /** 
     * This is the owning reference to the ObjectAccessor
     */

    private ObjectAccessor objectAccessor = null;

    //
    // Constructors and Initializers    
    //

    public HtmlBasicFacesContext(ServletRequest req, ServletResponse res,
            ServletContext sc ) {

        request = req;
        response = res;
        servletContext = sc;

        renderKit = new HtmlBasicRenderKit();
        stack = new Stack();
    }

    //
    // Class methods
    //

    //
    // General Methods
    //
    // Methods from FacesContext.
    //
    public RenderKit getRenderKit() {
        return renderKit;
    }

    public Locale getLocale(){
        return null;
    }

    public ClientCapabilities getClientCapabilities(){
        return null;
    }

    public ObjectManager getObjectManager() {
        if (null == objectManager) {
            // The per-app ObjectManager instance is created in the
            // FacesServlet.init() method, and stored in the
            // ServletContext's attr set.
            objectManager = (ObjectManager)servletContext. 
                getAttribute(Constants.REF_OBJECTMANAGER);
            Assert.assert_it(null != objectManager, 
                             "ObjectManager cannot be null.  Should already be created.");
        }
        return objectManager;
    }

    public ObjectAccessor getObjectAccessor() {
        if (null == objectAccessor) {
            getObjectManager();  // Make sure our lazy objectManager is there
            Assert.assert_it(null != objectManager);

            ObjectAccessorFactory oaFactory = (ObjectAccessorFactory)
                objectManager.get(Constants.REF_OBJECTACCESSORFACTORY);
            Assert.assert_it(null != oaFactory);
            try {
                objectAccessor = oaFactory.newObjectAccessor(this);
            } catch ( FacesException fe ) {
                // PENDING(edburns): log message
            }    
        }
        return objectAccessor;
    }

    public void setRequest(ServletRequest newReq) {
        ParameterCheck.nonNull(newReq);
        request = newReq;
    }

    public OutputMethod getOutputMethod() {
        return outputMethod;
    }

    public void setOutputMethod(OutputMethod om) {
        ParameterCheck.nonNull(om);
        outputMethod = om;
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public ServletRequest getRequest() {
        return request;
    }

    public UIComponent peekAtAncestor(int level) {
        UIComponent c;
        try {
            c = (UIComponent)stack.get(level);
            return c;
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public void pushChild(UIComponent c){
        stack.push(c);
    }

    public UIComponent popChild() {
        UIComponent c;
        if (stack.empty()) {
            return null;
        } else { 
            c = (UIComponent)stack.pop();
            return c;
        }
    }

    public MessageList getMessageList()
    {
        if (_messageList == null)
        {
            _messageList = MessageList.newInstance();
            _messageList.setLocale(request.getLocale());
            getObjectManager().put(request, Constants.MESSAGE_LIST_ID,
                    _messageList);
        }
        return _messageList;
    }

    public NavigationHandler getNavigationHandler() {

        if (navHandler != null) {
            return navHandler;
        }
        // get the navigationHandler Factory
        navHandlerFactory = (NavigationHandlerFactory)
                    objectManager.get(Constants.REF_NAVIGATIONHANDLERFACTORY);
        Assert.assert_it(null != navHandlerFactory);

        // get NavigationMap from ObjectManager
        // and pass it to constructor. For this we need to look up the
        // navigationMap id in UIForm. UIForm can be obtained from
        // objectManager
        String formId=(String)request.getParameter(Constants.REF_UIFORMID);

        if ( formId != null) {
            TreeNavigator treeNav = (TreeNavigator)objectManager.get(request,
                                                  Constants.REF_TREENAVIGATOR);
            Assert.assert_it(null != treeNav);

            UIForm form_obj = (UIForm) treeNav.findComponentForId(formId);
            if ( form_obj != null ) {
                NavigationMap navMap = form_obj.getNavigationMap(this);
                if ( navMap != null ) {
                    try {
                        navHandler = navHandlerFactory.newNavigationHandler(navMap);
                    } catch (FacesException e) {
                        // PENDING(edburns): log message
                        System.out.println("Exception getEventQueue: " +
                                   e.getMessage());
                        e.printStackTrace();
                        Assert.assert_it(false);
                    }
                }
            }
        }
        return navHandler;
    }

    public EventDispatcher getEventDispatcher(EventObject event) {

        ParameterCheck.nonNull(event);

        EventDispatcher result = null;
        FacesEvent fe = (FacesEvent) event;
        result = (EventDispatcher) fe.getSource();
        Assert.assert_it(result != null);
        return result;
    }

   /**
    * PRECONDITION: AbstractFactory is in ObjectManager.
    *
    * @see javax.faces.EventContext#getEventQueue
    */
    public EventQueue getEventQueue() {
	AbstractFactory abstractFactory;

        if (null == eventQueue) {
            eventQueue = (EventQueue)objectManager.get(request,
                                               Constants.REF_EVENTQUEUE);
            if (eventQueue == null) {
                abstractFactory = (AbstractFactory)
                    objectManager.get(Constants.REF_ABSTRACTFACTORY);
                Assert.assert_it(null != abstractFactory);
                try {
                    eventQueue = abstractFactory.newEventQueue();
                } catch (FactoryConfigurationError e) {
                    // PENDING(edburns): log message
                    System.out.println("Error getEventQueue: " +
                                       e.getMessage());
                    e.printStackTrace();
                    Assert.assert_it(false);
                }
                Assert.assert_it(null != eventQueue);
                objectManager.put(request, Constants.REF_EVENTQUEUE, eventQueue);
            }
        }
        return eventQueue;
    }

    public ServletResponse getResponse() {
        return response;
    }
    

} // end of class HtmlBasicFacesContext
