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

package com.sun.faces.spi;

import com.sun.faces.util.Util;
import com.sun.faces.util.FacesLogger;

import javax.faces.FacesException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;
import java.util.Enumeration;
import java.util.ArrayList;
import java.util.Arrays;
import java.net.URL;
import java.net.URLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;

/**
 * <p>
 * Base class for service discovery.
 * </p>
 */
final class ServiceFactoryUtils {

    private static final Logger LOGGER = FacesLogger.APPLICATION.getLogger();
    private static final String[] EMPTY_ARRAY = new String[0];


    // ---------------------------------------------------------- Public Methods


    static Object getProviderFromEntry(String entry, Class<?>[] argumentTypes, Object[] arguments) {

        if (entry == null) {
            return null;
        }

        try {
            Class<?> clazz = Util.loadClass(entry, null);
            Constructor c = clazz.getDeclaredConstructor(argumentTypes);
            if (c == null) {
                throw new FacesException("Unable to find constructor accepting arguments: " + Arrays.toString(arguments));
            }
            return c.newInstance(arguments);
        } catch (Exception e) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, e.toString(), e);
            }
            return null;
        }

    }


    static String[] getServiceEntries(String key) {

        List<String> results = null;
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            return EMPTY_ARRAY;
        }

        Enumeration<URL> urls = null;
        String serviceName = "META-INF/services/" + key;
        try {
            urls = loader.getResources(serviceName);
        } catch (IOException ioe) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE,
                           ioe.toString(),
                           ioe);
            }
        }

        if (urls != null) {
            InputStream input = null;
            BufferedReader reader = null;
            while (urls.hasMoreElements()) {
                try {
                    if (results == null) {
                        results = new ArrayList<String>();
                    }
                    URL url = urls.nextElement();
                    URLConnection conn = url.openConnection();
                    conn.setUseCaches(false);
                    input = conn.getInputStream();
                    if (input != null) {
                        try {
                            reader =
                                  new BufferedReader(new InputStreamReader(input,
                                                                           "UTF-8"));
                        } catch (Exception e) {
                            // The DM_DEFAULT_ENCODING warning is acceptable here
                            // because we explicitly *want* to use the Java runtime's
                            // default encoding.
                            reader =
                                  new BufferedReader(new InputStreamReader(input));
                        }
                        for (String line = reader.readLine();
                             line != null;
                             line = reader.readLine()) {
                            results.add(line.trim());
                        }
                    }
                } catch (Exception e) {
                    if (LOGGER.isLoggable(Level.SEVERE)) {
                        LOGGER.log(Level.SEVERE,
                                   "jsf.spi.provider.cannot_read_service",
                                   new Object[]{serviceName});
                        LOGGER.log(Level.SEVERE,
                                   e.toString(),
                                   e);
                    }
                } finally {
                    if (input != null) {
                        try {
                            input.close();
                        } catch (Exception e) {
                            if (LOGGER.isLoggable(Level.FINEST)) {
                                LOGGER.log(Level.FINEST, "Closing stream", e);
                            }
                        }
                    }
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (Exception e) {
                            if (LOGGER.isLoggable(Level.FINEST)) {
                                LOGGER.log(Level.FINEST, "Closing stream", e);
                            }
                        }
                    }
                }
            }
        }

        return ((results != null && !results.isEmpty())
                ? results.toArray(new String[results.size()])
                : EMPTY_ARRAY);

    }

}
