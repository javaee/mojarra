/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2013 Oracle and/or its affiliates. All rights reserved.
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


import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * Factory class for creating <code>ConfigurationResourceProvider</code> instances
 * using the Java services discovery mechanism.
 */
public class ConfigurationResourceProviderFactory {


    public enum ProviderType {

        /**
         * ConfigurationResourceProvider type for configuration resources
         * that follow the faces-config DTD/Schema.
         */
        FacesConfig(FacesConfigResourceProvider.SERVICES_KEY),

        /**
         * ConfigurationResourceProvider type for configuration resources
         * that follow the Facelet taglib DTD/Schema.
         */
        FaceletConfig(FaceletConfigResourceProvider.SERVICES_KEY);

        String servicesKey;

        ProviderType(String servicesKey) {
            this.servicesKey = servicesKey;
        }
        
    }


    // ---------------------------------------------------------- Public Methods


    /**
     * @param providerType the type of providers that should be discovered and instantiated.
     *
     * @return an array of all <code>ConfigurationResourceProviders discovered that
     *  match the specified <code>ProviderType</code>.
     */
    public static ConfigurationResourceProvider[] createProviders(ProviderType providerType) {

        String[] serviceEntries = ServiceFactoryUtils.getServiceEntries(providerType.servicesKey);
        List<ConfigurationResourceProvider> providers = new ArrayList<ConfigurationResourceProvider>();
        if (serviceEntries.length > 0) {
            for (String serviceEntry : serviceEntries) {
                try {
                    ConfigurationResourceProvider provider = (ConfigurationResourceProvider)
                          ServiceFactoryUtils.getProviderFromEntry(serviceEntry, null, null);
                    if (provider != null) {
                        if (ProviderType.FacesConfig == providerType) {
                            if (!(provider instanceof FacesConfigResourceProvider)) {
                                throw new IllegalStateException("Expected ConfigurationResourceProvider type to be an instance of FacesConfigResourceProvider");
                            }
                        } else {
                            if (!(provider instanceof FaceletConfigResourceProvider)) {
                                throw new IllegalStateException("Expected ConfigurationResourceProvider type to be an instance of FaceletConfigResourceProvider");
                            }
                        }
                        providers.add(provider);
                    }
                } catch(ClassCastException cce) {
                    // we are going to ignore these for now.
                }
            }
        } else {

            ServiceLoader serviceLoader;

            switch (providerType) {
                case FacesConfig:
                    serviceLoader = ServiceLoader.load(FacesConfigResourceProvider.class);
                break;
                case FaceletConfig:
                    serviceLoader = ServiceLoader.load(FaceletConfigResourceProvider.class);
                break;
                default:
                    throw new UnsupportedOperationException(providerType.servicesKey +
                        " cannot be loaded via ServiceLoader API.");
            }

            Iterator iterator = serviceLoader.iterator();

            while (iterator.hasNext()) {
                providers.add((ConfigurationResourceProvider) iterator.next());
            }
        }

        return providers.toArray(new ConfigurationResourceProvider[providers.size()]);
    }
}
