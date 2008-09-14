package com.sun.faces.application.annotation;

import java.lang.annotation.Annotation;

import javax.faces.event.ListenerFor;
import javax.faces.event.ListenersFor;

import com.sun.faces.util.Util;

/**
 * <code>Scanner</code> implementation responsible for {@link ListenerFor} annotations.
 */
class ListenerForScanner implements Scanner {


    // ---------------------------------------------------- Methods from Scanner


    public Class<? extends Annotation> getAnnotation() {

        return ListenerFor.class;

    }


    public RuntimeAnnotationHandler scan(Class<?> clazz) {

        Util.notNull("clazz", clazz);

        ListenerForHandler handler = null;
        ListenerFor listenerFor = clazz.getAnnotation(ListenerFor.class);
        if (listenerFor != null) {
            handler = new ListenerForHandler(new ListenerFor[] { listenerFor });
        } else {
            ListenersFor listenersFor = clazz.getAnnotation(ListenersFor.class);
            if (listenersFor != null) {
                handler = new ListenerForHandler(listenersFor.value());
            }
        }

        return handler;

    }
    
}
