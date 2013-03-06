/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sun.faces.application.annotation;

import java.lang.annotation.Annotation;

/**
 * <code>Scanner</code> implementation responsible for {@link EJB}
 * annotations.
 * 
 * <p>
 *  Note this will delegate down to the EJBScanner so we can fail gracefully
 *  when CDI is not available.
 * </p>
 */
public class DelegatedEJBScanner implements Scanner {

    private Scanner delegate;
    
    public DelegatedEJBScanner() {
        try {
            delegate = new EJBScanner();
        } catch(Throwable throwable) {
            throwable.printStackTrace(System.err);
        }
    }

    /**
     * Delegate to the actual EJB scanner.
     * 
     * @return the annotation.
     */
    @Override
    public Class<? extends Annotation> getAnnotation() {
        if (delegate != null) {
            return delegate.getAnnotation();
        }
        return null;
    }

    /**
     * Delegate to the actual EBJ scanner.
     * 
     * @param clazz the class.
     * @return the runtime annotation handler.
     */
    @Override
    public RuntimeAnnotationHandler scan(Class<?> clazz) {
        if (delegate != null) {
            return delegate.scan(clazz);
        }
        return null;
    }
    
}
