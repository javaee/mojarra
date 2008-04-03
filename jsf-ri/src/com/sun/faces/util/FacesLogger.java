/*
 * $Id: FacesLogger.java,v 1.1 2007/04/22 21:57:53 rlubke Exp $
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
 
package com.sun.faces.util;

/**
 * <p/>
 * An <code>enum</code> of all application <code>Logger</code>s.
 * </p>
 */
public enum FacesLogger {

    APPLICATION("application"),
    CONFIG("config"),
    CONTEXT("context"),
    LIFECYCLE("lifecycle"),
    MANAGEDBEAN("managedbean"),
    RENDERKIT("renderkit"),
    TAGLIB("taglib"),
    TIMING("timing");

    private static final String LOGGER_RESOURCES
         = "com.sun.faces.LogStrings";
    private static final String FACES_LOGGER_NAME_PREFIX
         = "javax.enterprise.resource.webcontainer.jsf.";
    private String loggerName;


    FacesLogger(String loggerName) {
        this.loggerName = FACES_LOGGER_NAME_PREFIX + loggerName;
    }


    public String getLoggerName() {
        return loggerName;
    }


    public String getResourcesName() {
        return LOGGER_RESOURCES;
    }

}