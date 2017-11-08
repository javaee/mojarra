/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2017 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.faces.config.manager.tasks;

import static com.sun.faces.RIConstants.ANNOTATED_CLASSES;
import static java.util.Collections.emptySet;

import java.lang.annotation.Annotation;
import java.net.URI;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import javax.servlet.ServletContext;

import com.sun.faces.config.InitFacesContext;
import com.sun.faces.config.manager.spi.FilterClassesFromFacesInitializerAnnotationProvider;
import com.sun.faces.spi.AnnotationProvider;
import com.sun.faces.spi.AnnotationProviderFactory;
import com.sun.faces.util.Timer;

/**
 * Scans the class files within a web application returning a <code>Set</code> of classes that have been annotated with
 * a standard Faces annotation (possibly limited to the annotations that denote configurable elements)
 */
public class FindAnnotatedConfigClasses implements Callable<Map<Class<? extends Annotation>, Set<Class<?>>>> {

    private InitFacesContext facesContext;
    private AnnotationProvider provider;
    private ProvideMetadataToAnnotationScanTask metadataGetter;
    private Set<Class<?>> annotatedSet;

    // -------------------------------------------------------- Constructors

    @SuppressWarnings("unchecked")
    public FindAnnotatedConfigClasses(ServletContext servletContext, InitFacesContext facesContext, ProvideMetadataToAnnotationScanTask metadataGetter) {
        this.facesContext = facesContext;
        this.provider = AnnotationProviderFactory.createAnnotationProvider(servletContext);
        this.metadataGetter = metadataGetter;
        this.annotatedSet = (Set<Class<?>>) servletContext.getAttribute(ANNOTATED_CLASSES);
    }

    // ----------------------------------------------- Methods from Callable

    @Override
    public Map<Class<? extends Annotation>, Set<Class<?>>> call() throws Exception {

        Timer t = Timer.getInstance();
        if (t != null) {
            t.startTiming();
        }

        // We are executing on a different thread.
        facesContext.addInitContextEntryForCurrentThread();
        Set<URI> scanUris = null;
        com.sun.faces.spi.AnnotationScanner annotationScanner = metadataGetter.getAnnotationScanner();

        // This is where we discover what kind of InjectionProvider we have.
        if (provider instanceof FilterClassesFromFacesInitializerAnnotationProvider && annotationScanner != null) {
            // This InjectionProvider is capable of annotation scanning *and* injection.
            
            // Note that DelegatingAnnotationProvider itself doesn't use the provided scanner, but just uses the classes
            // that were already scanned by the ServletContainerInitializer and stored there.
            
            ((FilterClassesFromFacesInitializerAnnotationProvider) provider).setAnnotationScanner(annotationScanner, metadataGetter.getJarNames(annotatedSet));
            scanUris = emptySet();
        } else {
            // This InjectionProvider is capable of annotation scanning only
            scanUris = metadataGetter.getAnnotationScanURIs(annotatedSet);
        }

        // Note that DelegatingAnnotationProvider itself ignores the scanUris and directly gets the classes from the
        // ServletContext where they were stored by the ServletContainerInitializer
        
        Map<Class<? extends Annotation>, Set<Class<?>>> annotatedClasses = provider.getAnnotatedClasses(scanUris);

        if (t != null) {
            t.stopTiming();
            t.logResult("Configuration annotation scan complete.");
        }

        return annotatedClasses;
    }

} // END AnnotationScanTask
