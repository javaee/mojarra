/*
 * $Id: RIConstants.java,v 1.46 2003/10/06 18:11:31 eburns Exp $
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

    /**

    * If the following name=value pair appears in the request query
    * string, the RestoreViewPhase will proceed directly to
    * RenderResponsePhase.

    */

    public final static String INITIAL_REQUEST_NAME = "initialRequest";
    public final static String INITIAL_REQUEST_VALUE = "true";

    public final static String REQUEST_LOCALE = FACES_PREFIX + "LOCALE";

    public final static String FACES_VIEW = FACES_PREFIX + "VIEW";
   
    /**

    * The presence of this UIComponent attribute with the value the same
    * as its name indicates that the UIComponent instance has already
    * had its SelectItem "children" configured.

    */ 

    public final static String SELECTITEMS_CONFIGURED = FACES_PREFIX + "SELECTITEMS_CONFIGURED";

    public final static String IMPL_MESSAGES = FACES_PREFIX + "IMPL_MESSAGES";

    public static final String SAVESTATE_FIELD_MARKER = FACES_PREFIX + 
            "saveStateFieldMarker";
   
    public static final String CONFIG_FILES_INITPARAM = 
        "javax.faces.application.CONFIG_FILES";

    public static final String JSF_RI_CONFIG = 
	"com/sun/faces/jsf-ri-config.xml";

    public final static String FORM_NUMBER_ATTR = FACES_PREFIX +
	"FormNumber";
    
    /**
     * <p>Parser implementation for processing JSF reference expressions.</p>
     */
    public static final String FACES_RE_PARSER = 
        FACES_PREFIX + "el.ext.parser.FacesREParser";
    
    /**
     * <p>ExpressionEvaluator implementation for processing JSP/JSTL-based
     * EL expressions.</p>
     */
    public static final String JSP_EL_EVALUATOR = 
        FACES_PREFIX + "el.impl.ExpressionEvaluatorImpl";

    /**
     * <p>Parser implementation for processing JSP/JSTL-based EL expressions.</p>
     */
    public static final String JSP_EL_PARSER = 
        FACES_PREFIX + "el.impl.parser.ELParserImpl";
    
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

    public static boolean IS_UNIT_TEST_MODE = false;

}
