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
                    new DeclarativeSystemEventListener(listener.getMethodExpression(ctx, Object.class, new Class[] { ComponentSystemEvent.class })));
        }
    }

    protected Class<? extends SystemEvent> getEventClass(FaceletContext ctx) {
        Class<? extends SystemEvent> clazz = null;
        String eventType = (String) this.type.getValueExpression(ctx, String.class).getValue(ctx);
        if (eventType == null) {
            throw new FacesException("Attribute 'type' can not be null");
        }
        ApplicationAssociate associate =
                  ApplicationAssociate.getInstance(ctx.getFacesContext().getExternalContext());
        NamedEventManager nem = associate.getNamedEventManager();

        clazz = nem.getNamedEvent(eventType);
        if (clazz == null) {
            try {
                clazz = Util.loadClass(eventType, this);
            } catch (ClassNotFoundException ex) {
                throw new FacesException ("An unknown event type was specified:  " + eventType);
            }
        }

        return clazz;
    }

}


class DeclarativeSystemEventListener implements ComponentSystemEventListener, Serializable {

    private static final long serialVersionUID = 8945415935164238908L;

    private MethodExpression action;

    // Necessary for state saving
    public DeclarativeSystemEventListener() {}

    public DeclarativeSystemEventListener(MethodExpression action) {
        this.action = action;
    }

    public void processEvent(ComponentSystemEvent event) throws AbortProcessingException {
        action.invoke(FacesContext.getCurrentInstance().getELContext(), new Object[]{event});
    }
}