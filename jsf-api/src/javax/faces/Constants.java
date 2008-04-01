/*
 * $Id: Constants.java,v 1.15 2002/01/25 18:35:06 visvan Exp $
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
     * The key stored in the servletcontext attributes that enables 
     * the correct scopeKey for a servletContext
     */ 
    public static final String REF_SERVLETCONTEXTINSTANCE = "faces.ServletContextInstance";


    /**
     * The factory used to create the render context.
     */
    public static final String REF_RENDERCONTEXTFACTORY = 
        "faces.RenderContextFactory";

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
     * Name for EventContextFactory
     */
    public static final String REF_EVENTCONTEXTFACTORY = "eventContextFactory";

    /**
     * Name for ObjectAccessorFactory
     */
    public static final String REF_OBJECTACCESSORFACTORY = "objectAccessorFactory";

    /**
     * Name for NavigationHandlerFactory
     */
    public static final String REF_NAVIGATIONHANDLERFACTORY = 
            "navigationHandlerFactory";
    
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

    /**
     * Success indicator after command processing.
     */
    public static final String OUTCOME_SUCCESS = "success";

    /**
     * Failure indicator after command processing.
     */
    public static final String OUTCOME_FAILURE = "failure";

    
}
