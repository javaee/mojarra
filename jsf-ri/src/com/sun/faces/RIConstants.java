/*
 * $Id: RIConstants.java,v 1.24 2003/02/07 00:18:08 eburns Exp $
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

    // PENDING(rogerk) this needs to be mentioned in the spec, 
    // under [ConfigFiles-26]
    // EVENT_LIMIT is configured as servlet init param;
    public static final String EVENT_LIMIT = "maxevents";
    public static final int MAX_EVENTS = 100;

    public static final String FORMAT_POOL = "com.sun.faces.renderkit.FormatPool";
    public static final String FACES_LOCALE = "FacesLocale";

    public final static String FORM_NUMBER_ATTR = FACES_PREFIX +
	"FormNumber";

    /**
     * <p>Phase identifier for <em>Reconstitute Request Tree</em>.</p>
     */
    public static final int RECONSTITUTE_REQUEST_TREE_PHASE = 0;


    /**
     * <p>Phase identifier for <em>Apply Request Values</em>.</p>
     */
    public static final int APPLY_REQUEST_VALUES_PHASE = 10;


    /**
     * <p>Phase identifier for <em>Process Validations</em>.</p>
     */
    public static final int PROCESS_VALIDATIONS_PHASE = 20;


    /**
     * <p>Phase identifier for <em>Update Model Values</em>.</p>
     */
    public static final int UPDATE_MODEL_VALUES_PHASE = 30;


    /**
     * <p>Phase identifier for <em>Invoke Application</em>.</p>
     */
    public static final int INVOKE_APPLICATION_PHASE = 40;

    public static final String ELEVALUATOR = "org.apache.taglibs.standard.jstl_el.jstl.ELEvaluator";

    /**
     * <p>Phase identifier for <em>Render Response</em>.</p>
     */
    public static final int RENDER_RESPONSE_PHASE = 50;    

    /**
     * <p>String identifer for <em>bundle attribute.</em>.</p>
     */
    public static final String BUNDLE_ATTR = "com.sun.faces.bundle";

}
