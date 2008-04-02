/*
 * $Id: GlassFishConfigureListener.java,v 1.3 2006/03/29 22:38:31 rlubke Exp $
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

package com.sun.faces.config;

import javax.servlet.ServletContextEvent;

/**
 * <p>Currently, GlassFish will only invoke the ConfigureListener
 * if it detects the FacesServlet defined in the web.xml.  The ConfigureListener
 * currently has logic to bypass configuration if the web.xml has no FacesServlet.
 * So in the case of GlassFish, we're wasting cycles.</p>
 */
public class GlassFishConfigureListener extends ConfigureListener {

    // ---------------------------------------------------------- Public Methods


    /*      
     * Disable web.xml scanning. 
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {

        scanWebXml(false);
        super.contextInitialized(sce);

    }

    // ------------------------------------------------------- Protected Methods


    /*
     * Because of EE 5 requirements, we force XML validation.
     */
    @Override
    protected boolean isFeatureEnabled(Object obj, String paramName) {

        return VALIDATE_XML.equals(paramName) || super
              .isFeatureEnabled(obj, paramName);

    }

}
