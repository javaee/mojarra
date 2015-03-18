
/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2015 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.faces.config;

import static com.sun.faces.RIConstants.ANNOTATED_CLASSES;
import static com.sun.faces.config.AnnotationScanner.FACES_ANNOTATION_TYPE;
import static com.sun.faces.config.WebConfiguration.WebContextInitParameter.AnnotationScanPackages;
import com.sun.faces.spi.AnnotationProvider;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletContext;



/*
 * This class is the default implementation of AnnotationProvider
 * referenced by the AnnotationProviderFactory.  Unless someone
 * manualy provides one via in META-INF/services, this is the one
 * that will actually be instantiated and installed into Mojarra.

 * This class actually delegates the real work to one of two kinds of
 * com.sun.faces.config.AnnotationScanner subclasses.
 * JavaClassScanningAnnotationScanner or
 * DelegateToGlassFishAnnotationScanner.
 *
 */

public class DelegatingAnnotationProvider extends AnnotationProvider {

    private AnnotationScanner scanner = null;

    public DelegatingAnnotationProvider(ServletContext sc) {
        super(sc);
    }

    /*
     * Called during annotation scanning.  If we already have a scanner
     * reference, use it.  If not, create a
     * JavaClassScanningAnnotationScanner and use it as the scanner.
     */

    @Override
    public Map<Class<? extends Annotation>, Set<Class<?>>> getAnnotatedClasses(Set<URI> urls) {
        HashMap<Class<? extends Annotation>, Set<Class<?>>> annotatedMap = new HashMap<Class<? extends Annotation>, Set<Class<?>>>();
        createAnnotatedMap(annotatedMap, (Set<Class<?>>) sc.getAttribute(ANNOTATED_CLASSES));
        return annotatedMap;
    }
    
    /*
     * This will only be called if the InjectionProvider offered by the
     * container implements com.sun.faces.spi.AnnotationScanner.  In
     * this case, we know that we can safely use
     * DelegateToGlassFishAnnotationScanner as our scanner reference.
     */
    public void setAnnotationScanner(com.sun.faces.spi.AnnotationScanner containerConnector, Set<String> jarNamesWithoutMetadataComplete) {
        assert(null == scanner);
        DelegateToGlassFishAnnotationScanner impl =
                new DelegateToGlassFishAnnotationScanner(sc);
        impl.setAnnotationScanner(containerConnector, jarNamesWithoutMetadataComplete);
        scanner = impl;
    }

    /**
     * Go over the annotated set and converter it to a hash map.
     *
     * @param annotatedMap
     * @param annotatedSet
     */
    private void createAnnotatedMap(HashMap<Class<? extends Annotation>, Set<Class<?>>> annotatedMap, Set<Class<?>> annotatedSet) {
        if (annotatedSet != null && !annotatedSet.isEmpty()) {
            Iterator<Class<?>> iterator = annotatedSet.iterator();
            
            WebConfiguration webConfig = WebConfiguration.getInstance();
            boolean annotationScanPackagesSet = webConfig.isSet(AnnotationScanPackages);
            String[] annotationScanPackages = null;
            if (annotationScanPackagesSet) {
                annotationScanPackages = webConfig.getOptionValue(AnnotationScanPackages).split("\\s+");
            }
            
            while (iterator.hasNext()) {
                try {
                    Class<?> clazz = iterator.next();
                    Annotation[] annotations = clazz.getAnnotations();
                    for (Annotation annotation : annotations) {
                        Class<? extends Annotation> annoType = annotation.annotationType();
                        if (FACES_ANNOTATION_TYPE.contains(annoType)) {
                            Set<Class<?>> classes = annotatedMap.get(annoType);
                            if (classes == null) {
                                classes = new HashSet<Class<?>>();
                                annotatedMap.put(annoType, classes);
                            }
                            if (annotationScanPackagesSet) {
                                if (matchesAnnotationScanPackages(clazz, annotationScanPackages)) {
                                    classes.add(clazz);
                                }
                            } else {
                                classes.add(clazz);
                            }
                        }
                    }
                } catch (NoClassDefFoundError ncdfe) {
                }
            }
        }
    }
    
    private boolean matchesAnnotationScanPackages(Class clazz, String[] annotationScanPackages) {
        boolean result = false;
        for(int i=0; i<annotationScanPackages.length; i++) {
            String classUrlString = clazz.getProtectionDomain().getCodeSource().getLocation().toString();
            String classPackageName = clazz.getPackage().getName();
            if (classUrlString.contains("WEB-INF/classes")
                    && annotationScanPackages[i].equals("*")) {
                result = true;
            } else if (classPackageName.equals(annotationScanPackages[i])) {
                result = true;
            } else if (annotationScanPackages[i].startsWith("jar:")) {
                String jarName = annotationScanPackages[i].substring(4, annotationScanPackages[i].indexOf(":", 5));
                String jarPackageName = annotationScanPackages[i].substring(annotationScanPackages[i].lastIndexOf(":") + 1);
                if (jarName.equals("*")) {
                    if  (jarPackageName.equals("*")) {
                        result = true;
                    } else if (jarPackageName.equals(classPackageName)) {
                        result = true;
                    }
                } else if (classUrlString.contains(jarName) && jarPackageName.equals("*")) {
                    result = true;
                } else if (classUrlString.contains(jarName) && jarPackageName.equals(classPackageName)) {
                    result = true;
                }
            }
        }
        return result;
    }
}
