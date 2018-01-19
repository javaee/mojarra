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

import com.sun.faces.config.DelegatingAnnotationProvider;

import javax.servlet.ServletContext;
import javax.faces.FacesException;
import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * 
 */
public class AnnotationProviderFactory {

    private static final Class<? extends AnnotationProvider> DEFAULT_ANNOTATION_PROVIDER =
       DelegatingAnnotationProvider.class;

    private static final String ANNOTATION_PROVIDER_SERVICE_KEY =
         "com.sun.faces.spi.annotationprovider";


    // ---------------------------------------------------------- Public Methods


    public static AnnotationProvider createAnnotationProvider(ServletContext sc) {
        AnnotationProvider annotationProvider = createDefaultProvider(sc);

        String[] services = ServiceFactoryUtils.getServiceEntries(ANNOTATION_PROVIDER_SERVICE_KEY);
        if (services.length > 0) {
            // only use the first entry...
            Object provider = ServiceFactoryUtils.getProviderFromEntry(services[0],
                new Class[] { ServletContext.class, AnnotationProvider.class }, new Object[] { sc , annotationProvider });
            if (provider == null) {
                provider = ServiceFactoryUtils.getProviderFromEntry(services[0], new Class[] { ServletContext.class }, new Object[] { sc });
            }
            
            if (provider != null) {
                if (!(provider instanceof AnnotationProvider)) {
                    throw new FacesException("Class " + provider.getClass().getName() + " is not an instance of com.sun.faces.spi.AnnotationProvider");
                }
                annotationProvider = (AnnotationProvider)provider;
            }
        }
        else {

            ServiceLoader<AnnotationProvider> serviceLoader = ServiceLoader.load(AnnotationProvider.class);
            Iterator iterator = serviceLoader.iterator();

            if (iterator.hasNext()) {

                AnnotationProvider defaultAnnotationProvider = annotationProvider;
                annotationProvider = (AnnotationProvider) iterator.next();
                annotationProvider.initialize(sc, defaultAnnotationProvider);
            }
        }

        return annotationProvider;
    }


    // --------------------------------------------------------- Private Methods


    private static AnnotationProvider createDefaultProvider(ServletContext sc) {
        AnnotationProvider result = null;
        Constructor c;

        try {
            c = DEFAULT_ANNOTATION_PROVIDER.getDeclaredConstructor(new Class<?>[] { ServletContext.class });
            result = (AnnotationProvider) c.newInstance(sc);
        } catch (Exception e2) {
            throw new FacesException(e2);
        }
        return result;
    }
}
