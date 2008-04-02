/*
 * $Id: RIConstants.java,v 1.18 2002/09/18 23:52:01 rkitain Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces;

import javax.faces.render.RenderKitFactory;

/**
 * This class contains literal strings used throughout the Faces RI.
 */
public class RIConstants {

    public static final String URL_PREFIX = "/faces";
    
    /**
     * Used to add uniqueness to the names.
    */
    public final static String FACES_PREFIX = "com.sun.faces.";

    public final static String DEFAULT_RENDER_KIT = FACES_PREFIX +
	RenderKitFactory.DEFAULT_RENDER_KIT;
    
    public final static String DISABLE_RENDERERS = FACES_PREFIX +
	"DisableRenderers";

    /**

    * If the following name=value pair appears in the request query
    * string, the CreateRequestTreePhase will proceed directly to
    * RenderResponsePhase.

    */

    public final static String INITIAL_REQUEST_NAME = "initialRequest";
    public final static String INITIAL_REQUEST_VALUE = "true";
    
    public final static String FACES_TREE = "com.sun.faces.TREE";
    public final static String REQUEST_LOCALE = "com.sun.faces.LOCALE";
   
    /**

    * The presence of this UIComponent attribute with the value the same
    * as its name indicates that the UIComponent instance has already
    * had its SelectItem "children" configured.

    */ 

    public final static String SELECTITEMS_CONFIGURED = "com.sun.faces.SELECTITEMS_CONFIGURED";

    public final static String IMPL_MESSAGES = "com.sun.faces.IMPL_MESSAGES";

    public static final String SAVESTATE_MARKER = FACES_PREFIX + "saveStateMarker";
    public static final String SAVESTATE_INITPARAM = "saveStateInClient";

    public static final String FORMAT_POOL = "com.sun.faces.renderkit.FormatPool";
    public static final String FACES_LOCALE = "FacesLocale";


}
