package com.sun.faces.application.annotation;

import java.lang.annotation.Annotation;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

import com.sun.faces.util.Util;

/**
 * <code>Scanner</code> implementation responsible for {@link ResourceDependency} annotations.
 */
class ResourceDependencyScanner implements Scanner {


    // ---------------------------------------------------- Methods from Scanner


    public Class<? extends Annotation> getAnnotation() {

        return ResourceDependency.class;

    }


    public RuntimeAnnotationHandler scan(Class<?> clazz) {

        Util.notNull("clazz", clazz);

        ResourceDependencyHandler handler = null;
        ResourceDependency dep = clazz.getAnnotation(ResourceDependency.class);
        if (dep != null) {
            handler = new ResourceDependencyHandler(new ResourceDependency[] { dep });
        } else {
            ResourceDependencies deps = clazz.getAnnotation(ResourceDependencies.class);
            if (deps != null) {
                handler = new ResourceDependencyHandler(deps.value());
            }
        }

        return handler;
        
    }
    
}
