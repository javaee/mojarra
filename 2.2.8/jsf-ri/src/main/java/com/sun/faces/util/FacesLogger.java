/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
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

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

/**
 * <p/>
 * An <code>enum</code> of all application <code>Logger</code>s.
 * </p>
 */
public enum FacesLogger {

    APPLICATION("application"),
    APPLICATION_VIEW("application.view"),
    RESOURCE("resource"),
    CONFIG("config"),
    CONTEXT("context"),
    FACELETS_COMPILER("facelets.compiler"),
    FACELETS_COMPONENT("facelets.tag.component"),
    FACELETS_EL("facelets.el"),
    FACELETS_META("facelets.tag.meta"),
    FACELETS_COMPOSITION("facelets.tag.ui.composition"),
    FACELETS_DECORATE("facelets.tag.ui.decorate"),
    FACELETS_INCLUDE("facelets.tag.ui.include"),
    FACELETS_FACELET("faclets.facelet"),
    FACELETS_FACTORY("facelets.factory"),
    FLOW("flow"),
    LIFECYCLE("lifecycle"),
    MANAGEDBEAN("managedbean"),
    RENDERKIT("renderkit"),
    TAGLIB("taglib"),
    TIMING("timing"),
    UTIL("util"),
    FLASH("flash");

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
    
    public String interpolateMessage(FacesContext context,
          String messageId,
          Object [] params) {
        String result = null;
        ResourceBundle rb = null;
        UIViewRoot root = context.getViewRoot();
        Locale curLocale;
        ClassLoader loader = Util.getCurrentLoader(this);
        if (null == root) {
            curLocale = Locale.getDefault();
        } else {
            curLocale = root.getLocale();
        }
        try {
            rb = ResourceBundle.getBundle(getResourcesName(), curLocale, loader);
            String message = rb.getString(messageId);
            if (params != null) {
                result = MessageFormat.format(message, params);
            } else {
                result = message;
            }
        } catch (MissingResourceException mre) {
            result = messageId;
        }
        
        return result;
    }

}
