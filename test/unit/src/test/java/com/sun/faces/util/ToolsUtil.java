/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2014 Oracle and/or its affiliates. All rights reserved.
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

import java.util.ResourceBundle;
import java.util.Locale;
import java.text.MessageFormat;
import java.util.logging.Logger;

/**
 * Various static utility methods.
 */
public class ToolsUtil {

    public static final String FACES_LOGGER = "javax.enterprise.resource.webcontainer.jsf";

    public static final String CONFIG_LOGGER = ".config";

    public static final String BEANS_LOGGER = ".config.beans";

    public static final String RULES_LOGGER = ".config.rules";

    public static final String GENERATE_LOGGER = ".config.generate";

    public static final String FACES_LOG_STRINGS
            = "com.sun.faces.LogStrings";

    public static final String TOOLS_LOG_STRINGS
            = "com.sun.faces.ToolsLogStrings";

    private static final String RESOURCE_BUNDLE_BASE_NAME
            = "com.sun.faces.resources.JsfToolsMessages";

    // --------------------------------------------------- Message Key Constants
    public static final String MANAGED_BEAN_NO_MANAGED_BEAN_NAME_ID
            = "com.sun.faces.MANAGED_BEAN_NO_MANAGED_BEAN_NAME";

    public static final String MANAGED_BEAN_NO_MANAGED_BEAN_CLASS_ID
            = "com.sun.faces.MANAGED_BEAN_NO_MANAGED_BEAN_CLASS";

    public static final String MANAGED_BEAN_NO_MANAGED_BEAN_SCOPE_ID
            = "com.sun.faces.MANAGED_BEAN_NO_MANAGED_BEAN_SCOPE";

    public static final String MANAGED_BEAN_INVALID_SCOPE_ID
            = "com.sun.faces.MANAGED_BEAN_INVALID_SCOPE";

    public static final String MANAGED_BEAN_AS_LIST_CONFIG_ERROR_ID
            = "com.sun.faces.MANAGED_BEAN_AS_LIST_CONFIG_ERROR";

    public static final String MANAGED_BEAN_AS_MAP_CONFIG_ERROR_ID
            = "com.sun.faces.MANAGED_BEAN_AS_MAP_CONFIG_ERROR";

    public static final String MANAGED_BEAN_LIST_PROPERTY_CONFIG_ERROR_ID
            = "com.sun.faces.MANAGED_BEAN_LIST_PROPERTY_CONFIG_ERROR";

    public static final String MANAGED_BEAN_MAP_PROPERTY_CONFIG_ERROR_ID
            = "com.sun.faces.MANAGED_BEAN_MAP_PROPERTY_CONFIG_ERROR";

    public static final String MANAGED_BEAN_PROPERTY_CONFIG_ERROR_ID
            = "com.sun.faces.MANAGED_BEAN_PROPERTY_CONFIG_ERROR";

    public static final String MANAGED_BEAN_NO_MANAGED_PROPERTY_NAME_ID
            = "com.sun.faces.MANAGED_BEAN_NO_MANAGED_PROPERTY_NAME";

    // ---------------------------------------------------------- Public Methods
    public static String getMessage(String messageKey, Object[] params) {

        ResourceBundle bundle
                = ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME,
                        Locale.getDefault(),
                        Thread.currentThread().getContextClassLoader());
        return MessageFormat.format(bundle.getString(messageKey), params);

    } // END getMessage

    public static String getMessage(String messageKey) {

        return getMessage(messageKey, null);

    } // END getMessage

    public static Logger getLogger(String loggerName) {
        return Logger.getLogger(loggerName, FACES_LOG_STRINGS);
    }

    public static Class loadClass(String name,
            Object fallbackClass)
            throws ClassNotFoundException {
        ClassLoader loader = ToolsUtil.getCurrentLoader(fallbackClass);
        return Class.forName(name, false, loader);
    }

    public static ClassLoader getCurrentLoader(Object fallbackClass) {
        ClassLoader loader
                = Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            loader = fallbackClass.getClass().getClassLoader();
        }
        return loader;
    }
}
