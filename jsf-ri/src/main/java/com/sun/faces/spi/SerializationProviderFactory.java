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

import javax.faces.context.ExternalContext;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.ObjectInputStream;
import java.io.InputStream;
import java.io.IOException;

import com.sun.faces.RIConstants;
import com.sun.faces.renderkit.ApplicationObjectInputStream;
import com.sun.faces.config.WebConfiguration;
import com.sun.faces.config.WebConfiguration.WebContextInitParameter;
import com.sun.faces.util.Util;
import com.sun.faces.util.FacesLogger;

/**
 * <p>A factory for creating <code>SerializationProvider</code>
 * instances.</p>
 */
public class SerializationProviderFactory {
    
    /**
     * <p>Our default <code>SerializationProvider</code>.</p>
     */
    private static final SerializationProvider JAVA_PROVIDER =
          new SerializationProviderFactory.JavaSerializationProvider();

    /**
      * <p>The system property that will be checked for alternate
      * <code>SerializationProvider</code> implementations.</p>
      */
     private static final String SERIALIZATION_PROVIDER_PROPERTY =
           RIConstants.FACES_PREFIX + "SerializationProvider";

    private static final Logger LOGGER = FacesLogger.APPLICATION.getLogger();


    /**
     * <p>Creates a new instance of the class specified by the
     * <code>com.sun.faces.InjectionProvider</code> system property.
     * If this propery is not defined, then a default, no-op, 
     * <code>InjectionProvider</code> will be returned.
     * @param extContext the ExternalContext for this application
     * @return an implementation of the <code>InjectionProvider</code>
     *  interfaces
     */
    public static SerializationProvider createInstance(ExternalContext extContext) {
        String providerClass = findProviderClass(extContext);
                
        
        SerializationProvider provider = getProviderInstance(providerClass);

        if (provider.getClass() != JavaSerializationProvider.class) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE,
                           "jsf.spi.serialization.provider_configured",
                           new Object[]{provider.getClass().getName()});
            }
        } 
        return provider;
        
    }
    

    private static SerializationProvider getProviderInstance(String className) {
        SerializationProvider provider = JAVA_PROVIDER;
        if (className != null) {
            try {
                Class<?> clazz = Util.loadClass(className, SerializationProviderFactory.class);
                if (implementsSerializationProvider(clazz)) {
                    provider = (SerializationProvider) clazz.newInstance();
                } else {
                    if (LOGGER.isLoggable(Level.SEVERE)) {
                        LOGGER.log(Level.SEVERE,
                                   "jsf.spi.serialization.provider_not_implemented",
                                   new Object[]{ className });
                    }
                }
            } catch (ClassNotFoundException cnfe) {
                if (LOGGER.isLoggable(Level.SEVERE)) {
                   LOGGER.log(Level.SEVERE,
                              "jsf.spi.serialization.provider_not_found",
                              new Object[]{ className });
                }
            } catch (InstantiationException ie) {
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.log(Level.SEVERE,
                               "jsf.spi.serialization.provider_cannot_instantiate",
                               new Object[]{className});
                    LOGGER.log(Level.SEVERE, "", ie);
                }
            } catch (IllegalAccessException iae) {
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.log(Level.SEVERE,
                               "jsf.spi.serialization.provider_cannot_instantiate",
                               new Object[]{className});
                    LOGGER.log(Level.SEVERE, "", iae);
                }
            }
        } 
       
        return provider;
    }

    /**
     * <p>Determine if the specified class implements the
     * <code>SerializationProvider</code> interfaces.</p>
     * @param clazz the class in question
     * @return <code>true</code> if <code>clazz</code> implements
     *  the <code>SerializationProvider</code> interface
     */
    private static boolean implementsSerializationProvider(Class<?> clazz) {
        return SerializationProvider.class.isAssignableFrom(clazz);
    }
    
    /**
     * Tries to find a provider class in a web context parameter.  If not
     * present it tries to find it as a System property.  If still not found
     * returns null.
     *
     * @param extContext The ExternalContext for this request
     * @return The provider class name specified in the container configuration, 
     *         or <code>null</code> if not found.
     */
    private static String findProviderClass(ExternalContext extContext) {
        
        WebConfiguration webConfig = WebConfiguration.getInstance(extContext);
        
        String provider = webConfig.getOptionValue(
              WebContextInitParameter.SerializationProviderClass);
        
        if (provider != null) {
            return provider;
        } else {
            return System.getProperty(SERIALIZATION_PROVIDER_PROPERTY);
        }
    }

    /**
     * <p>An implementation of <code>SerializationProvider</code> which
     * uses standard Java serialization.</p>
     */
    private static final class JavaSerializationProvider 
          implements SerializationProvider {


        /**
         * <p>Creates a new <code>ObjectOutputStream</code> wrapping the
         * specified <code>destination</code>.</p>
         *
         * @param destination the destination of the serialized Object(s)
         *
         * @return an <code>ObjectOutputStream</code>
         */
        public ObjectOutputStream createObjectOutputStream(
              OutputStream destination) throws IOException {
            return new ObjectOutputStream(destination);
        }

        /**
         * <p>Creates a new <code>ObjectInputStream</code> wrapping the specified
         * <code>source</code>.</p>
         *
         * @param source the source stream from which to read the Object(s)
         *               from
         *
         * @return an <code>ObjectInputStream</code>
         */
        public ObjectInputStream createObjectInputStream(InputStream source)
        throws IOException {
            return new ApplicationObjectInputStream(source);
        }
    }

} // END InjectionProviderFactory
