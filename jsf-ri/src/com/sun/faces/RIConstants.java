/*
 * $Id: RIConstants.java,v 1.84 2006/05/10 20:03:22 rogerk Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
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

    public static final String LOGICAL_VIEW_MAP = FACES_PREFIX +
        "logicalViewMap";

    public static final String ACTUAL_VIEW_MAP = FACES_PREFIX +
        "actualViewMap";

    public static final String SAVED_STATE = FACES_PREFIX + "savedState";       


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

    /*
     * <p>TLV Resource Bundle Location </p>
     */
    public static final String TLV_RESOURCE_LOCATION =
        FACES_PREFIX + "resources.Resources";

    public static final Object NO_VALUE = "";
      
    public static final String CORE_NAMESPACE = 
        "http://java.sun.com/jsf/core";
    public static final String HTML_NAMESPACE = 
        "http://java.sun.com/jsf/html";
    public static final String JSTL_NAMESPACE = 
        "http://java.sun.com/jsp/jstl/core";
    
    public static final Class[] EMPTY_CLASS_ARGS = new Class[0];
    public static final Object[] EMPTY_METH_ARGS = new Object[0];

    //
    // Constructors and Initializers
    //

    private RIConstants() {

        throw new IllegalStateException();
        
    }

    public static final String EL_RESOLVER_CHAIN_TYPE_NAME = FACES_PREFIX + "ELResolverChainType";

    /**
     *<p>ResponseWriter Content Types and Encoding</p>
     */
    public final static String HTML_CONTENT_TYPE = "text/html";
    public final static String XHTML_CONTENT_TYPE = "application/xhtml+xml";
    public final static String APPLICATION_XML_CONTENT_TYPE = "application/xml";
    public final static String TEXT_XML_CONTENT_TYPE = "text/xml";
    public final static String ALL_MEDIA = "*/*";
    public final static String CHAR_ENCODING = "ISO-8859-1";



}
