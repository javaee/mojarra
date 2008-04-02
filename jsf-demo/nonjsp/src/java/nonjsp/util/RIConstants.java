/*
 * $Id: RIConstants.java,v 1.3 2005/12/14 22:27:34 rlubke Exp $
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

package nonjsp.util;

import javax.faces.render.RenderKitFactory;

/**
 * This class contains literal strings used throughout the Faces RI.
 * <p/>
 * Based on com.sun.faces.RIConstants
 *
 * @version $Id: RIConstants.java,v 1.3 2005/12/14 22:27:34 rlubke Exp $
 * @see com.sun.faces.RIConstants
 */
public class RIConstants {

    /** Used to add uniqueness to the names. */
    public final static String FACES_PREFIX = "com.sun.faces.";

    public final static String HTML_BASIC_RENDER_KIT = FACES_PREFIX
                                                       +
                                                       RenderKitFactory
                                                             .HTML_BASIC_RENDER_KIT;

    public final static String FACES_VIEW = FACES_PREFIX + "VIEW";
    public final static String REQUEST_LOCALE = FACES_PREFIX + "LOCALE";

}
