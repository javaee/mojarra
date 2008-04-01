/*
 * $Id: FacesContext.java,v 1.3 2002/04/05 19:40:15 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces;

import javax.servlet.ServletRequest;
import javax.servlet.ServletContext;
import javax.servlet.ServletResponse;
import java.util.Locale;
import java.util.EventObject;

/**
 * The class which defines an object representing all contextual
 * information required for processing a Faces request.
 */
public abstract class FacesContext {

    protected FacesContext() {
    }

    /**
     * @return ServletContext object for the web application associated
     *         with this request
     */
    public abstract ServletContext getServletContext();

    /**
     * @return ServletRequest object representing the client request
     */
    public abstract ServletRequest getRequest();

    /**
     * @return ServletResponse object used to write response to the client
     *         request
     */
    public abstract ServletResponse getResponse();
    
    /**
     * The current value of the ClientCapabilities object.
     * @return ClientCapabilities object which characterizes the client
     *         which initiated the request
     */
    public abstract ClientCapabilities getClientCapabilities();

    /**
     * The current value of the render kit object.
     * @return RenderKit object targeted for the client described by
     *         the ClientCapabilities object
     */
    public abstract RenderKit getRenderKit();

    /**
     * The current value of the locale object.  This may be different
     * from the Locale object in the ClientCapabiliites object.
     * @return Locale object respresenting the user's preferred locale
     */
    public abstract Locale getLocale();

    /**
     * The current value of the Object manager object.
     * @return ObjectManager used to manage application objects in scoped
     *         namespace
     */
    public abstract ObjectManager getObjectManager();

    /**
     * The current value of the Object accessor object.
     * @return ObjectAccessor used to resolve object-reference Strings to
     *         objects
     */
    public abstract ObjectAccessor getObjectAccessor();

    /**
     * @return EventQueue object used to post any events originating from client request
     */
    public abstract EventQueue getEventQueue();

    /**
     * @throws NullPointerException if event is null
     * @return EventDispatcher object used to obtain an object which can
     *         dispatched the specified event
     */
    public abstract EventDispatcher getEventDispatcher(EventObject event);

    /**
     * @return NavigationHandler object used to configure the navigational
     *         result of processing events originating from this request
     */
    public abstract NavigationHandler getNavigationHandler();


    /**
     * The current value of the OutputMethod object.
     * @return OutputMethod object to be used to write all rendering 
     *         of user-interface components for the associated request.
     */
    public abstract OutputMethod getOutputMethod();

    /**
     * Returns the ancestor of the component currently at the top
     * of the rendering stack by walking down the stack the specified
     * number of levels.  For example, if <code>1</code> was specified
     * as the level, then the immediate parent on the render stack
     * is returned.
     * @param level the number of levels down the stack to access
     *        the ancestor
     * @return the UIComponent object on the render stack down the
     *         specified number of levels
     */
    public UIComponent peekAtAncestor(int level) {
        return null; //compile
    }

    /**
     * Pushes the specified component on the render stack.  This
     * method is invoked just prior to invoking rendering processing
     * on the specified component.
     * @param c the component to be pushed on the render stack
     * @throws NullPointerException if c is null
     */
    public void pushChild(UIComponent c){
    
    }

    /**
     * Pops the current component off the render stack.  This
     * method is invoked after the specified component and all of
     * its descendents have been rendered.  If
     * there is no component currently on the stack, returns null.
     * @return UIComponent object corresponding to the component
     *         which was most recently rendered from this render context
     */
    public UIComponent popChild() {
        return null; //compile
    }

    /**
     * @return A central message list that can be used by all parts of the
     * request to store messages.
     */
    public MessageList getMessageList() {
        return null;
    }

    /**
     * Set the current value of the OutputMethod object.
     * @param OutputMethod object to be used to write all rendering
     *        of user-interface components for the associated request.
     */
    // PENDING(visvan) check with Aim.
    public abstract void setOutputMethod(OutputMethod outputMethod);

}

