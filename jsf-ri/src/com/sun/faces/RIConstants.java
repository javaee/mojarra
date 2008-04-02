/*
 * $Id: RIConstants.java,v 1.36 2003/05/08 23:13:02 horwat Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
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
    
    public final static String FACES_TREE = FACES_PREFIX + "TREE";
    public final static String REQUEST_LOCALE = FACES_PREFIX + "LOCALE";
   
    /**

    * The presence of this UIComponent attribute with the value the same
    * as its name indicates that the UIComponent instance has already
    * had its SelectItem "children" configured.

    */ 

    public final static String SELECTITEMS_CONFIGURED = FACES_PREFIX + "SELECTITEMS_CONFIGURED";

    public final static String IMPL_MESSAGES = FACES_PREFIX + "IMPL_MESSAGES";

    public static final String SAVESTATE_MARKER = FACES_PREFIX + "saveStateMarker";
    public static final String SAVESTATE_INITPARAM = 
	FACES_PREFIX + "saveStateInClient";

    public static final String CONFIG_FILES_INITPARAM = 
        "javax.faces.application.CONFIG_FILES";

    public static final String JSF_RI_CONFIG = 
	"com/sun/faces/jsf-ri-config.xml";

    // PENDING(rogerk) this needs to be mentioned in the spec, 
    // under [ConfigFiles-26]
    // EVENT_LIMIT is configured as servlet init param;
    public static final String EVENT_LIMIT = "maxevents";
    public static final int MAX_EVENTS = 100;

    public static final String FORMAT_POOL = FACES_PREFIX + "renderkit.FormatPool";
    public static final String FACES_LOCALE = "FacesLocale";

    public final static String FORM_NUMBER_ATTR = FACES_PREFIX +
	"FormNumber";

    public static final String ELEVALUATOR = "org.apache.taglibs.standard.jstl_el.jstl.ELEvaluator";

    /**
     * <p>String identifer for <em>bundle attribute.</em>.</p>
     */
    public static final String BUNDLE_ATTR = FACES_PREFIX + "bundle";

    /**
     * <p>The name of the attribute in the ServletContext's attr set
     * used to store the result of the check for the ability to load the
     * required classes for the Faces RI.</p>
     */
    public static final String HAS_REQUIRED_CLASSES_ATTR = FACES_PREFIX + "HasRequiredClasses";

    /**

    * <p>Used in resolveVariable to mark immutable maps.</p>

    */
    
    public static final String IMMUTABLE_MARKER = 
	FACES_PREFIX + "IMMUTABLE";

    public static final String CONFIG_ATTR = FACES_PREFIX + "ConfigBase";

    public static final String VALIDATE_XML = FACES_PREFIX + "validateXml";

    public static final String ONE_TIME_INITIALIZATION_ATTR = 
	FACES_PREFIX + "OneTimeInitialization";

    public static final String APPLICATION = "application";
    public static final String APPLICATION_SCOPE = "applicationScope";
    public static final String SESSION = "session";
    public static final String SESSION_SCOPE = "sessionScope";
    public static final String REQUEST = "request";
    public static final String REQUEST_SCOPE = "requestScope";



}
