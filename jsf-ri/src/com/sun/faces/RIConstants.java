/*
 * $Id: RIConstants.java,v 1.91 2007/04/22 21:40:57 rlubke Exp $
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


    /**
     * Used to add uniqueness to the names.
     */
    public final static String FACES_PREFIX = "com.sun.faces.";

    public final static String HTML_BASIC_RENDER_KIT = FACES_PREFIX +
        RenderKitFactory.HTML_BASIC_RENDER_KIT;       

    public static final String SAVESTATE_FIELD_DELIMITER = "~";
    public static final String SAVESTATE_FIELD_MARKER = 
          SAVESTATE_FIELD_DELIMITER 
           + FACES_PREFIX 
           + "saveStateFieldMarker" 
           + SAVESTATE_FIELD_DELIMITER;

    public static final String LOGICAL_VIEW_MAP = FACES_PREFIX +
        "logicalViewMap";

    public static final String ACTUAL_VIEW_MAP = FACES_PREFIX +
        "actualViewMap";

    public static final String SAVED_STATE = FACES_PREFIX + "savedState";          

    /**
     * <p>The name of the attribute in the ServletContext's attr set
     * used to store the result of the check for the ability to load the
     * required classes for the Faces RI.</p>
     */
    public static final String HAS_REQUIRED_CLASSES_ATTR = FACES_PREFIX +
        "HasRequiredClasses";   

    public static final String ONE_TIME_INITIALIZATION_ATTR =
        FACES_PREFIX + "OneTimeInitialization";    

    /**
     * Request attribute containing a Set of clientIds that have messages pending
     * display when rendering response.
     */
    public static final String CLIENT_ID_MESSAGES_NOT_DISPLAYED = FACES_PREFIX +
    	"clientIdMessagesNotDisplayed";

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
    
    public static final Class[] EMPTY_CLASS_ARGS = new Class[0];
    public static final Object[] EMPTY_METH_ARGS = new Object[0];
    
    public static final String EL_RESOLVER_CHAIN_TYPE_NAME = FACES_PREFIX + "ELResolverChainType";

    /**
     *<p>ResponseWriter Content Types and Encoding</p>
     */
    public static final String HTML_CONTENT_TYPE = "text/html";
    public static final String XHTML_CONTENT_TYPE = "application/xhtml+xml";
    public static final String APPLICATION_XML_CONTENT_TYPE = "application/xml";
    public static final String TEXT_XML_CONTENT_TYPE = "text/xml";
    public static final String ALL_MEDIA = "*/*";
    public static final String CHAR_ENCODING = "ISO-8859-1";
    public static final String SUN_JSF_JS_URI = "com_sun_faces_sunjsf.js";
    public static final String DEFAULT_LIFECYCLE = 
          FACES_PREFIX + "DefaultLifecycle";
    public static final String DEFAULT_STATEMANAGER =
          FACES_PREFIX + "DefaultStateManager";


    private RIConstants() {

        throw new IllegalStateException();
        
    }

}
