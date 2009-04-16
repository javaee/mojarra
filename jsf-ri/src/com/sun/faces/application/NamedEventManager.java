/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sun.faces.application;

import com.sun.faces.util.Util;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.faces.FacesException;
import javax.faces.event.PostAddToViewEvent;
import javax.faces.event.PostValidateEvent;
import javax.faces.event.PreValidateEvent;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.PreRenderComponentEvent;
import javax.faces.event.PreRenderViewEvent;
import javax.faces.event.SystemEvent;

/**
 * Note: New, relevant spec'd ComponentSystemEvents must be added to the constructor
 */
public class NamedEventManager {

    private Map<String, Class<? extends SystemEvent>> namedEvents =
            new ConcurrentHashMap<String, Class<? extends SystemEvent>>();
    private Map<String, Set<Class<? extends SystemEvent>>> duplicateNames =
             new ConcurrentHashMap<String, Set<Class<? extends SystemEvent>>>();

    public NamedEventManager() {
        namedEvents.put("javax.faces.event.PreRenderComponent", PreRenderComponentEvent.class);
        namedEvents.put("javax.faces.event.PreRenderView", PreRenderViewEvent.class);
        namedEvents.put("javax.faces.event.PostAddToView", PostAddToViewEvent.class);
        namedEvents.put("javax.faces.event.PreValidate", PreValidateEvent.class);
        namedEvents.put("javax.faces.event.PostValidate", PostValidateEvent.class);
        namedEvents.put("preRenderComponent", PreRenderComponentEvent.class);
        namedEvents.put("preRenderView", PreRenderViewEvent.class);
        namedEvents.put("postAddToView", PostAddToViewEvent.class);
        namedEvents.put("preValidate", PreValidateEvent.class);
        namedEvents.put("postValidate", PostValidateEvent.class);
    }

    public void addNamedEvent(String name, Class<? extends SystemEvent> event) {
        namedEvents.put(name, event);
    }

    public Class<? extends SystemEvent> getNamedEvent(String name) {
        Class<? extends SystemEvent> namedEvent = namedEvents.get(name);
        if (namedEvent == null) {
            try {
                namedEvent = Util.loadClass(name, this);
            } catch (ClassNotFoundException ex) {
                throw new FacesException ("An unknown event type was specified:  " + name, ex);
            }
        }
        if (!ComponentSystemEvent.class.isAssignableFrom(namedEvent)) {
                throw new ClassCastException();
        }

        return namedEvent;
    }

    public void addDuplicateName(String name, Class<? extends SystemEvent> event) {
        Class<? extends SystemEvent> registeredEvent = namedEvents.remove(name);
        Set<Class<? extends SystemEvent>> events = duplicateNames.get(name);
        if (events == null) {
            events = new HashSet<Class<? extends SystemEvent>>();
            duplicateNames.put(name, events);
        }
        events.add(event);
        if (registeredEvent != null) {
            events.add(registeredEvent);
        }
    }

    public boolean isDuplicateNamedEvent(String name) {
        return (namedEvents.get(name) != null) || (duplicateNames.get(name) != null);
    }
}
