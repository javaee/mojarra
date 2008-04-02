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
 * Copyright 2006 Sun Microsystems Inc. All Rights Reserved
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

    private static final Logger LOGGER = Util.getLogger(Util.FACES_LOGGER 
            + Util.APPLICATION_LOGGER);


    /**
     * <p>Creates a new instance of the class specified by the
     * <code>com.sun.faces.InjectionProvider</code> system property.
     * If this propery is not defined, then a default, no-op, 
     * <code>InjectionProvider</code> will be returned. 
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
        
        String provider = webConfig.getContextInitParameter(
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
