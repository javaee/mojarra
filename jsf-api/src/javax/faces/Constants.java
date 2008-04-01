/*
 * $Id: Constants.java,v 1.9 2002/01/10 22:16:31 edburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces;

/**
 * This class contains literal strings used throughout Faces.
 */
public class Constants {

    /**
     * The Faces data structure used to store Faces objects in
     * specified scopes. 
     */
    public static final String REF_OBJECTMANAGER = "faces.ObjectManager";

    /**
     * The event queue used to queue Faces events.
     */
    public static final String REF_EVENTQUEUE = "faces.EventQueue";

    /**
     * The factory used to create the event queue.
     */
    public static final String REF_EVENTQUEUEFACTORY = 
        "faces.EventQueueFactory";

    /**
     * The Faces object which contains resources used for rendering 
     * user interface components.
     */ 
    public static final String REF_RENDERCONTEXT = "faces.RenderContext";

    /**
     * The key stored in the servlet request attributes that enables 
     * the correct scopeKey for a request
     */ 
    public static final String REF_REQUESTINSTANCE = "faces.RequestInstance";

    /**
     * The key stored in the session attributes that enables 
     * the correct scopeKey for a session
     */ 
    public static final String REF_SESSIONINSTANCE = "faces.SessionInstance";

    /**
     * The factory used to create the render context.
     */
    public static final String REF_RENDERCONTEXTFACTORY = 
        "faces.RenderContextFactory";

    /**
     * The component type name for 'WCommand' components.
     */
    public static final String REF_WCOMMAND = "WCommand";

    /**
     * The component type name for 'WForm' components.
     */
    public static final String REF_WFORM = "WForm";

    /**
     * The component type name for 'WOutput' components.
     */
    public static final String REF_WOUTPUT = "WOutput";

    /**
     * The component type name for 'WSelectBoolean' components.
     */
    public static final String REF_WSELECTBOOLEAN = "WSelectBoolean";

    /**
     * The component type name for 'WSelectOne' components.
     */
    public static final String REF_WSELECTONE = "WSelectOne";

    /**
     * The component type name for 'WTextEntry' components.
     */
    public static final String REF_WTEXTENTRY = "WTextEntry";

    /**
     * Name for listeners
     */
    // PENDING ( visvan ) all constants must start with "faces". So
    // need to declare a Faces constant an use it with listeners.
    public static final String REF_FORMLISTENERS = ".formListeners";
    public static final String REF_VALUECHANGELISTENERS = ".valueChangeListeners";
    public static final String REF_COMMANDLISTENERS = ".commandListeners";
    public static final String REF_COMMAND = ".command";

    /**
     * Name for EventDispatcherFactory
     */
    public static final String REF_EVENTDISPATCHERFACTORY = "eventDispatcherFactory";

    /**
     * The name of a Renderer.
     */
    public static final String REF_RENDERER_NAME = "faces.RendererName";

    /**
     * Starting string for hidden checkbox field
     */
     public static final String REF_HIDDENCHECKBOX = "facesCheckbox";

    /**
     * The session attributes key under which our transaction token is
     * stored, if it is used.  Taken from Struts, by Craig McClanahan.
     */
    public static final String TRANSACTION_TOKEN_KEY_SESSION =
        "javax.faces.TOKEN";

    /**
     * The reequest attr under which our transaction token is stored and
     * retrieved.  Taken from Struts, by Craig McClanahan.
     */
    public static final String REQUEST_TOKEN_KEY = "javax.faces.REQUEST_TOKEN";

    
}
