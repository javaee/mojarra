package com.sun.faces.application.annotation;

import javax.faces.application.Application;
import javax.faces.event.ListenerFor;
import javax.faces.event.ComponentSystemEventListener;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.event.SystemEventListener;

/**
 * {@link RuntimeAnnotationHandler} responsible for processing {@link ListenerFor} annotations.
 */
class ListenerForHandler implements RuntimeAnnotationHandler {

    private ListenerFor[] listenersFor;


    // ------------------------------------------------------------ Constructors


    public ListenerForHandler(ListenerFor[] listenersFor) {

        this.listenersFor = listenersFor;

    }


    // ------------------------------------------ Methods from AnnotationHandler


    @SuppressWarnings({"UnusedDeclaration"})
    public void apply(FacesContext ctx, Object... params) {

        Object listener;
        UIComponent target;
        if (params.length == 2) {
            // handling @ListenerFor on a Renderer
            listener = params[0];
            target = (UIComponent) params[1];
        } else {
            // handling @ListenerFor on a UIComponent
            listener = params[0];
            target = (UIComponent) params[0];
        }

        if (listener instanceof ComponentSystemEventListener) {
            for (int i = 0, len = listenersFor.length; i < len; i++) {
                    target.subscribeToEvent(listenersFor[i].systemEventClass(),
                                            (ComponentSystemEventListener) listener);
            }
        }
	else if (listener instanceof SystemEventListener) {
	    Class sourceClassValue = null;
	    Application app = ctx.getApplication();
            for (int i = 0, len = listenersFor.length; i < len; i++) {
		sourceClassValue = listenersFor[i].sourceClass();
		if (sourceClassValue == Void.class) {
		    app.subscribeToEvent(listenersFor[i].systemEventClass(), 
					 (SystemEventListener) listener); 
                }
                else {
		    app.subscribeToEvent(listenersFor[i].systemEventClass(), 
					 listenersFor[i].sourceClass(),
					 (SystemEventListener) listener); 
                    
                }
	    }
	}


    }

}
