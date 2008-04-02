/*
 * $Id: RIConstants.java,v 1.63 2004/05/04 19:55:00 rlubke Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
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

    public final static String HTML_BASIC_RENDER_KIT = FACES_PREFIX +
        RenderKitFactory.HTML_BASIC_RENDER_KIT;

    /**
     * If the following name=value pair appears in the request query
     * string, the RestoreViewPhase will proceed directly to
     * RenderResponsePhase.
     */

    public final static String INITIAL_REQUEST_NAME = "initialRequest";
    public final static String INITIAL_REQUEST_VALUE = "true";

    public final static String FACES_VIEW = FACES_PREFIX + "VIEW";

    /**
     * The presence of this UIComponent attribute with the value the same
     * as its name indicates that the UIComponent instance has already
     * had its SelectItem "children" configured.
     */

    public final static String SELECTITEMS_CONFIGURED = FACES_PREFIX +
        "SELECTITEMS_CONFIGURED";

    public final static String IMPL_MESSAGES = FACES_PREFIX + "IMPL_MESSAGES";

    public static final String SAVESTATE_FIELD_MARKER = FACES_PREFIX +
        "saveStateFieldMarker";

    public static final String JSF_RI_CONFIG =
        "com/sun/faces/jsf-ri-config.xml";

    public static final String JSF_RI_STANDARD =
        "com/sun/faces/standard-html-renderkit.xml";

    public final static String FORM_NUMBER_ATTR = FACES_PREFIX +
        "FormNumber";

    /**
     * <p>Parser implementation for processing JSF reference expressions.</p>
     */
    public static final String FACES_RE_PARSER =
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
    public static final String HAS_REQUIRED_CLASSES_ATTR = FACES_PREFIX +
        "HasRequiredClasses";

    /**
     * <p>Used in resolveVariable to mark immutable maps.</p>
     */

    public static final String IMMUTABLE_MARKER =
        FACES_PREFIX + "IMMUTABLE";

    public static final String CONFIG_ATTR = FACES_PREFIX + "ConfigBase";

    public static final String VALIDATE_XML = FACES_PREFIX + "validateXml";

    public static final String VERIFY_OBJECTS = FACES_PREFIX + "verifyObjects";

    public static final String ONE_TIME_INITIALIZATION_ATTR =
        FACES_PREFIX + "OneTimeInitialization";

    public static final String APPLICATION = "application";
    public static final String APPLICATION_SCOPE = "applicationScope";
    public static final String SESSION = "session";
    public static final String SESSION_SCOPE = "sessionScope";
    public static final String REQUEST = "request";
    public static final String REQUEST_SCOPE = "requestScope";
    public static final String NONE = "NONE";
    public static final String COOKIE_IMPLICIT_OBJ = "cookie";
    public static final String FACES_CONTEXT_IMPLICIT_OBJ = "facesContext";
    public static final String HEADER_IMPLICIT_OBJ = "header";
    public static final String HEADER_VALUES_IMPLICIT_OBJ = "headerValues";
    public static final String INIT_PARAM_IMPLICIT_OBJ = "initParam";
    public static final String PARAM_IMPLICIT_OBJ = "param";
    public static final String PARAM_VALUES_IMPLICIT_OBJ = "paramValues";
    public static final String VIEW_IMPLICIT_OBJ = "view";

    public static boolean IS_UNIT_TEST_MODE = false;

    /*
     * <p>TLV Resource Bundle Location </p>
     */
    public static final String TLV_RESOURCE_LOCATION =
        FACES_PREFIX + "resources.Resources";

    public static final Object NO_VALUE = new String();

    private static boolean htmlTLVActive = false;

    public static final String ENABLE_HTML_TLV = FACES_PREFIX + 
	"enableHtmlTagLibValidator";
    
    public static boolean isHtmlTagLibValidatorActive() {
	return htmlTLVActive;
    }

    public static void setHtmlTagLibValidatorActive(boolean state) {
	htmlTLVActive = state;
    }

    //
    // Constructors and Initializers
    //

    private RIConstants() {

        throw new IllegalStateException();
        
    }


}
