package com.sun.faces.application.annotation;

import java.lang.annotation.Annotation;

/**
 * <p>Instances of this interface are responsible for scanning a <code>Class</code>
 * for a specific annotation types.</p>
 *
 * <p>Scanner implementations <em>must</em> be thread safe.</p>
 */
interface Scanner {

    /**
     * @return the {@link java.lang.annotation.Annotation} this <code>Scanner</code>
     *  is responsible for handling.  <em>NOTE</em>: while a particular <code>Scanner</code>
     *  instance may handle a plural version of an <code>Annotation</code> in
     *  additional to a singular, this method must return the singular version
     *  only.
     */
    public Class<? extends Annotation> getAnnotation();

    
    /**
     * Scan the target class for the {@link java.lang.annotation.Annotation}s this scanner handles.
     * @param clazz the target class
     * @return a new {@link AnnotationHandler} instance capable of processing the
     *  annotations defined on this class.  If no relevant {@link java.lang.annotation.Annotation}s
     *  are found, return <code>null</code>.
     */
    public AnnotationHandler scan(Class<?> clazz);
    
}
