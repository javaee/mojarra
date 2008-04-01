/*
 * $Id: Constants.java,v 1.22 2002/04/15 20:10:39 jvisvanathan Exp $
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
     * The way Faces Objects are instantiated.
     */
    public static final String REF_ABSTRACTFACTORY = "faces.AbstractFactory";

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
    public static final String REF_FACESCONTEXT = "faces.FacesContext";

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
     * The key stored in the servletcontext attributes that enables 
     * the correct scopeKey for a servletContext
     */ 
    public static final String REF_SERVLETCONTEXTINSTANCE = "faces.ServletContextInstance";


    /**
     * The factory used to create the render context.
     */
    public static final String REF_FACESCONTEXTFACTORY = 
        "faces.RenderContextFactory";

    /**
     * The key for the value property 
     */
    public static final String REF_VALUE = "faces.Value";

    /**
     * The component type name for 'UICommand' components.
     */
    public static final String REF_UICOMMAND = "UICommand";

    /**
     * The component type name for 'UIForm' components.
     */
    public static final String REF_UIFORM = "UIForm";

    /**
     * The component type name for 'UIOutput' components.
     */
    public static final String REF_UIOUTPUT = "UIOutput";

    /**
     * The component type name for 'UISelectBoolean' components.
     */
    public static final String REF_UISELECTBOOLEAN = "UISelectBoolean";

    /**
     * The component type name for 'UISelectOne' components.
     */
    public static final String REF_UISELECTONE = "UISelectOne";

    /**
     * The component type name for 'UITextEntry' components.
     */
    public static final String REF_UITEXTENTRY = "UITextEntry";

    /**
     * Name for formId.
     */
    public static final String REF_UIFORMID = "faces.formId";
    
    /**
     * Name to specify actioncodes in NavigationHandler.
     */
    public static final String REF_FORWARD = "forward";
    public static final String REF_REDIRECT = "redirect";
    public static final String REF_PASS = "pass";
    
    /**
     * Name for ConverterManager
     */
    public static final String REF_CONVERTERMANAGER = "converterManager";

    /**
     * Name for ObjectAccessorFactory
     */
    public static final String REF_OBJECTACCESSOR = "objectAccessor";

    /**
     * Name for NavigationHandlerFactory
     */
    public static final String REF_NAVIGATIONHANDLER = "navigationHandler";
    
     /**
     * Name for NavigationMap
     */
    public static final String REF_NAVIGATIONMAP = "navigationMap";
    
    /**
     * Name for TreeEngine
     */
    public static final String REF_TREEENGINE = "faces.treeEngine";
    
    /**
     * The name of a Renderer.
     */
    public static final String REF_RENDERER_NAME = "faces.RendererName";

    /**
     * Starting string for hidden checkbox field
     */
     public static final String REF_HIDDENCHECKBOX = "facesCheckbox";

    /**
     * The tree navigator
     */
     public static final String REF_TREENAVIGATOR = "faces.TreeNavigator";

    /**
     * The RenderWrapper
     */
     public static final String REF_RENDERWRAPPER = "faces.RenderWrapper";



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

    /**
     * Success indicator after command processing.
     */
    public static final String OUTCOME_SUCCESS = "success";

    /**
     * Failure indicator after command processing.
     */
    public static final String OUTCOME_FAILURE = "failure";
    
    /**
     * Failure indicator after validation processing.
     */
    public static final String OUTCOME_VALIDATION_FAILED = "validationFailed"; 
  
    /**
     * Name for MessageFactory
     */ 
    public final static String DEFAULT_MESSAGE_FACTORY_ID = "faces.DefaultMessageFactory"; 

    /** A reserved ObjectManager id that can be used to the retrieve the
     * central message list for the request. 
     */
     public final static String MESSAGE_LIST_ID = "faces.MessageList";
}
