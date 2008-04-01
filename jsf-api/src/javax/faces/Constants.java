/*
 * $Id: Constants.java,v 1.2 2001/12/04 01:06:25 edburns Exp $
 *
 * Copyright 2000-2001 by Sun Microsystems, Inc.,
 * 901 San Antonio Road, Palo Alto, California, 94303, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Sun Microsystems, Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Sun.
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
    public static final String REF_OBJECTTABLE = "faces.ObjectTable";

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
    
}
