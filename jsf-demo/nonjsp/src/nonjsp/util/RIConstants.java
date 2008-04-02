/*
 * $Id: RIConstants.java,v 1.5 2003/08/27 23:49:49 horwat Exp $
 */

/*
 * Copyright 2002, 2003 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * 
 * - Redistribution in binary form must reproduce the above
 *   copyright notice, this list of conditions and the following
 *   disclaimer in the documentation and/or other materials
 *   provided with the distribution.
 *    
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *  
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 * DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR
 * RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THIS SOFTWARE OR
 * ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 * SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 * THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 * BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *  
 * You acknowledge that this software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
 */

package nonjsp.util;

import javax.faces.render.RenderKitFactory;

/**
 * This class contains literal strings used throughout the Faces RI.
 *
 * Copy of com.sun.faces.RIConstants in order to remove
 * demo dependancy on RI.
 *
 * @version $Id: RIConstants.java,v 1.5 2003/08/27 23:49:49 horwat Exp $
 *
 * @see com.sun.faces.RIConstants
 *
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
    
    public final static String FACES_VIEW = FACES_PREFIX + "VIEW";
    public final static String REQUEST_LOCALE = FACES_PREFIX + "LOCALE";
   
    /**

    * The presence of this UIComponent attribute with the value the same
    * as its name indicates that the UIComponent instance has already
    * had its SelectItem "children" configured.

    */ 

    public final static String SELECTITEMS_CONFIGURED = FACES_PREFIX + "SELECTITEMS_CONFIGURED";

    public final static String IMPL_MESSAGES = FACES_PREFIX + "IMPL_MESSAGES";

    public static final String SAVESTATE_MARKER = FACES_PREFIX + "saveStateMarker";
    
    // PENDING(rogerk) this needs to be mentioned in the spec, 
    // under [ConfigFiles-26]
    // EVENT_LIMIT is configured as servlet init param;
    public static final String EVENT_LIMIT = "maxevents";
    public static final int MAX_EVENTS = 100;

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
    public static final String BUNDLE_ATTR = FACES_PREFIX + "bundle";

}
