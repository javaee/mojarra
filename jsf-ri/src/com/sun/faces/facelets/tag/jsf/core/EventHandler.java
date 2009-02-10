/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sun.faces.facelets.tag.jsf.core;

import com.sun.faces.application.ApplicationAssociate;
import com.sun.faces.application.NamedEventManager;
import com.sun.faces.util.Util;
import java.io.IOException;
import java.io.Serializable;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.MethodExpression;
import javax.el.MethodNotFoundException;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ComponentSystemEventListener;
import javax.faces.event.SystemEvent;
import javax.faces.webapp.pdl.facelets.FaceletContext;
import javax.faces.webapp.pdl.facelets.FaceletException;
import javax.faces.webapp.pdl.facelets.tag.TagAttribute;
import javax.faces.webapp.pdl.facelets.tag.TagConfig;
import javax.faces.webapp.pdl.facelets.tag.TagHandler;

/**
 * This is the TagHandler for the f:event tag.
 */
public class EventHandler extends TagHandler {
    protected final TagAttribute type;
    protected final TagAttribute listener;

    public EventHandler(TagConfig config) {
        super(config);
        this.type = this.getRequiredAttribute("type");
        this.listener = this.getRequiredAttribute("listener");
    }

    public void apply(FaceletContext ctx, UIComponent parent) throws IOException, FacesException, FaceletException, ELException {
        Class<? extends SystemEvent> eventClass = getEventClass(ctx);
        if (eventClass != null) {
            parent.subscribeToEvent(eventClass,
                    new DeclarativeSystemEventListener(
                        listener.getMethodExpression(ctx, Object.class, new Class[] { ComponentSystemEvent.class }),
                        listener.getMethodExpression(ctx, Object.class, new Class[] { })));
        }
    }

    protected Class<? extends SystemEvent> getEventClass(FaceletContext ctx) {
        String eventType = (String) this.type.getValueExpression(ctx, String.class).getValue(ctx);
        if (eventType == null) {
            throw new FacesException("Attribute 'type' can not be null");
        }

        return ApplicationAssociate.getInstance(ctx.getFacesContext().getExternalContext())
                .getNamedEventManager().getNamedEvent(eventType);
    }

}


class DeclarativeSystemEventListener implements ComponentSystemEventListener, Serializable {

    private static final long serialVersionUID = 8945415935164238908L;

    private MethodExpression oneArgListener;
    private MethodExpression noArgListener;

    // Necessary for state saving
    public DeclarativeSystemEventListener() {}

    public DeclarativeSystemEventListener(MethodExpression oneArg, MethodExpression noArg) {
        this.oneArgListener = oneArg;
        this.noArgListener = noArg;
    }

    public void processEvent(ComponentSystemEvent event) throws AbortProcessingException {
        final ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        try{
            noArgListener.invoke(elContext, new Object[]{});
        } catch (MethodNotFoundException mnfe) {
            // Attempt to call public void method(ComponentSystemEvent event)
            oneArgListener.invoke(elContext, new Object[]{event});
        }
    }
}