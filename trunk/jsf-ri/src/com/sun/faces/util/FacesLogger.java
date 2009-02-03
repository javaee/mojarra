/*
 * $Id: FacesLogger.java,v 1.5 2008/02/14 00:32:11 rlubke Exp $
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
 
package com.sun.faces.util;

import java.util.logging.Logger;

/**
 * <p/>
 * An <code>enum</code> of all application <code>Logger</code>s.
 * </p>
 */
public enum FacesLogger {

    APPLICATION("application"),
    RESOURCE("resource"),
    CONFIG("config"),
    CONTEXT("context"),
    LIFECYCLE("lifecycle"),
    MANAGEDBEAN("managedbean"),
    RENDERKIT("renderkit"),
    TAGLIB("taglib"),
    TIMING("timing");

    private static final String LOGGER_RESOURCES
         = "com.sun.faces.LogStrings";
    public static final String FACES_LOGGER_NAME_PREFIX
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

    public Logger getLogger() {
        return Logger.getLogger(loggerName, LOGGER_RESOURCES);
    }

}
