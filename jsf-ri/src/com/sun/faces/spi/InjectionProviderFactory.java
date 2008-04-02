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

import java.util.logging.Logger;
import java.util.logging.Level;

import com.sun.faces.RIConstants;
import com.sun.faces.util.Util;

/**
 * <p>A factory for creating <code>InjectionProvider</code>
 * instances.</p>
 */
public class InjectionProviderFactory {

    /**
     * <p>Our no-op <code>InjectionProvider</code>.</p>
     */
    private static final InjectionProvider NOOP_PROVIDER =
          new NoopInjectionProvider();

    /**
     * <p>The system property that will be checked for alternate
     * <code>InjectionProvider</code> implementations.</p>
     */
    private static final String INJECTION_PROVIDER_PROPERTY =
          RIConstants.FACES_PREFIX + "InjectionProvider";
    
    
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
    public static InjectionProvider createInstance() {
        String className = System.getProperty(INJECTION_PROVIDER_PROPERTY);
        InjectionProvider provider = NOOP_PROVIDER;
        if (className != null) {            
            try {
                Class<?> clazz = Util.loadClass(className, InjectionProviderFactory.class);
                if (implementsInjectionProvider(clazz)) {
                    provider = (InjectionProvider) clazz.newInstance();
                } else {
                    if (LOGGER.isLoggable(Level.SEVERE)) {
                        LOGGER.log(Level.SEVERE,
                                   "jsf.spi.injection.provider_not_implemented",
                                   new Object[]{ className });
                    }
                }                                                 
            } catch (ClassNotFoundException cnfe) {
                if (LOGGER.isLoggable(Level.SEVERE)) {
                   LOGGER.log(Level.SEVERE,
                                   "jsf.spi.injection.provider_not_found",
                                   new Object[]{ className });
                }
            } catch (InstantiationException ie) {
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.log(Level.SEVERE,
                               "jsf.spi.injection.provider_cannot_instantiate",
                               new Object[]{className});
                    LOGGER.log(Level.SEVERE, "", ie);
                }
            } catch (IllegalAccessException iae) {
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.log(Level.SEVERE,
                               "jsf.spi.injection.provider_cannot_instantiate",
                               new Object[]{className});
                    LOGGER.log(Level.SEVERE, "", iae);
                }
            }
        }
        
        if (provider.getClass() != NoopInjectionProvider.class) {
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.log(Level.INFO,
                           "jsf.spi.injection.provider_configured",
                           new Object[] { className });
            }
        } else {
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.log(Level.INFO,
                           "jsf.spi.injection.no_injection",
                           new Object[] { className });
            }
        }
        return provider;
    }

    /**
     * <p>Determine if the specified class implements the
     * <code>InjectionProvider</code> interfaces.</p>
     * @param clazz the class in question
     * @return <code>true</code> if <code>clazz</code> implements
     *  the <code>InjectionProvider</code> interface
     */
    private static boolean implementsInjectionProvider(Class<?> clazz) {        
        return InjectionProvider.class.isAssignableFrom(clazz);
    }

    /**
     * <p>A no-op implementation of <code>InjectionProvider</code> which will
     * be used when the #INJECTION_PROVIDER_PROPERTY is not specified or
     * is invalid.</p>
     */
    private static final class NoopInjectionProvider implements InjectionProvider {

        /**
         * <p>This is a no-op.</p>
         * @param managedBean target ManagedBean
         */
        public void inject(Object managedBean) { }

        /**
         * <p>This is a no-op.</p>
         * @param managedBean target ManagedBean
         */
        public void invokePreDestroy(Object managedBean) { }
    }

} // END InjectionProviderFactory