/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright (c) 1997-2013 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.faces.application.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.spi.Contextual;
import javax.enterprise.context.spi.CreationalContext;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;

/**
 * The manager that deals with CDI ViewScoped beans.
 */
public class ViewScopeContextManager {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = Logger.getLogger(ViewScopeContextManager.class.getName());
    /**
     * Stores the constant to keep track of all the active view scope contexts.
     */
    private static final String ACTIVE_VIEW_CONTEXTS = "com.sun.faces.application.view.activeViewContexts";
    /**
     * Stores the constants to keep track of the active view maps.
     */
    private static final String ACTIVE_VIEW_MAPS = "com.sun.faces.application.view.activeViewMaps";

    /**
     * Clear the current view map using the Faces context.
     *
     * @param facesContext the Faces context.
     */
    public void clear(FacesContext facesContext) {
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.log(Level.FINEST, "Clearing @ViewScoped CDI beans for current view map");
        }
        Map<String, Object> viewMap = facesContext.getViewRoot().getViewMap(false);
        Map<Contextual, ViewScopeContextObject> contextMap = getContextMap(facesContext, false);
        if (contextMap != null) {
            destroyBeans(viewMap, contextMap);
        }
    }
    
    /**
     * Clear the given view map.
     * 
     * @param facesContext the Faces context.
     * @param viewMap the given view map.
     */
    public void clear(FacesContext facesContext, Map<String, Object> viewMap) {
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.log(Level.FINEST, "Clearing @ViewScoped CDI beans for given view map: {0}");
        }
        Map<Contextual, ViewScopeContextObject> contextMap = getContextMap(facesContext, false);
        if (contextMap != null) {
            destroyBeans(viewMap, contextMap);
        }
    }

    /**
     * Create the bean.
     *
     * @param <T> the type.
     * @param facesContext the faces context.
     * @param contextual the contextual.
     * @param creational the creational.
     * @return the value or null if not found.
     */
    public <T> T createBean(FacesContext facesContext, Contextual<T> contextual, CreationalContext<T> creational) {
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.log(Level.FINEST, "Creating @ViewScoped CDI bean using contextual: {0}", contextual);
        }

        T result = contextual.create(creational);

        if (result != null) {
            String name = getName(result);
            facesContext.getViewRoot().getViewMap(true).put(name, result);
            getContextMap(facesContext).put(contextual, new ViewScopeContextObject(contextual, creational, name));
        }

        return result;
    }

    /**
     * Destroy the view scoped beans for the given view and context map.
     *
     * @param viewMap the view map.
     * @param contextMap the context map.
     */
    private void destroyBeans(Map<String, Object> viewMap, Map<Contextual, ViewScopeContextObject> contextMap) {
        ArrayList<String> removalNameList = new ArrayList<String>();

        if (contextMap != null) {
            for (Map.Entry<Contextual, ViewScopeContextObject> entry : contextMap.entrySet()) {
                Contextual contextual = entry.getKey();
                ViewScopeContextObject contextObject = entry.getValue();
                CreationalContext creationalContext = contextObject.getCreationalContext();
                contextual.destroy(viewMap.get(contextObject.getName()), creationalContext);
                removalNameList.add(contextObject.getName());
            }

            Iterator<String> removalNames = removalNameList.iterator();
            while (removalNames.hasNext()) {
                String name = removalNames.next();
                viewMap.remove(name);
            }
        }
    }

    /**
     * Get the value from the view map (or null if not found).
     *
     * @param <T> the type.
     * @param facesContext the faces context.
     * @param contextual the contextual.
     * @return the value or null if not found.
     */
    public <T> T getBean(FacesContext facesContext, Contextual<T> contextual) {
        T result = null;
        Map<Contextual, ViewScopeContextObject> contextMap = getContextMap(facesContext);

        if (contextMap != null) {
            ViewScopeContextObject contextObject = contextMap.get(contextual);

            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.log(Level.FINEST, "Getting value for @ViewScoped bean with name: {0}", contextObject.getName());
            }

            if (contextObject != null) {
                result = (T) facesContext.getViewRoot().getViewMap(true).get(contextObject.getName());
            }
        }

        return result;
    }

    /**
     * Get the context map.
     *
     * @param facesContext the Faces context.
     * @return the context map.
     */
    private Map<Contextual, ViewScopeContextObject> getContextMap(FacesContext facesContext) {
        return getContextMap(facesContext, true);
    }

    /**
     * Get the context map.
     *
     * @param facesContext the Faces context.
     * @param create flag to indicate if we are creating the context map.
     * @return the context map.
     */
    private Map<Contextual, ViewScopeContextObject> getContextMap(FacesContext facesContext, boolean create) {
        Map<Contextual, ViewScopeContextObject> result = null;

        ExternalContext externalContext = facesContext.getExternalContext();
        if (externalContext != null) {
            Map<String, Object> sessionMap = externalContext.getSessionMap();
            Map<Object, Map<Contextual, ViewScopeContextObject>> activeViewScopeContexts =
                    (Map<Object, Map<Contextual, ViewScopeContextObject>>) sessionMap.get(ACTIVE_VIEW_CONTEXTS);
            Map<String, Object> viewMap = facesContext.getViewRoot().getViewMap(false);

            if (activeViewScopeContexts == null && create) {
                synchronized (sessionMap) {
                    activeViewScopeContexts = new ConcurrentHashMap<Object, Map<Contextual, ViewScopeContextObject>>();
                    sessionMap.put(ACTIVE_VIEW_CONTEXTS, activeViewScopeContexts);
                }
            }

            if (activeViewScopeContexts != null && create) {
                synchronized (activeViewScopeContexts) {
                    if (!activeViewScopeContexts.containsKey(System.identityHashCode(viewMap)) && create) {
                        activeViewScopeContexts.put(System.identityHashCode(viewMap),
                                new ConcurrentHashMap<Contextual, ViewScopeContextObject>());
                    }
                }
            }

            if (activeViewScopeContexts != null) {
                result = activeViewScopeContexts.get(System.identityHashCode(viewMap));
            }
        }

        return result;
    }

    /**
     * Get the name of the bean for the given object.
     *
     * @param instance the object.
     * @return the name.
     */
    private String getName(Object instance) {
        String name = instance.getClass().getSimpleName().substring(0, 1).toLowerCase()
                + instance.getClass().getSimpleName().substring(1);

        Named named = instance.getClass().getAnnotation(Named.class);
        if (named != null && named.value() != null && !named.value().trim().equals("")) {
            name = named.value();
        }
        return name;
    }

    /**
     * Called when a session destroyed.
     *
     * @param hse the HTTP session event.
     */
    public void sessionDestroyed(HttpSessionEvent hse) {
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.log(Level.FINEST, "Cleaning up session for CDI @ViewScoped beans");
        }

        HttpSession session = hse.getSession();

        Map<Object, Map<Contextual, ViewScopeContextObject>> activeViewScopeContexts =
                (Map<Object, Map<Contextual, ViewScopeContextObject>>) session.getAttribute(ACTIVE_VIEW_CONTEXTS);

        if (activeViewScopeContexts != null) {
            Map<String, Object> activeViewMaps = (Map<String, Object>) session.getAttribute(ACTIVE_VIEW_MAPS);
            if (activeViewMaps != null) {
                Iterator<Object> activeViewMapsIterator = activeViewMaps.values().iterator();
                while (activeViewMapsIterator.hasNext()) {
                    Map<String, Object> instanceMap = (Map<String, Object>) activeViewMapsIterator.next();
                    Map<Contextual, ViewScopeContextObject> contextMap =
                            activeViewScopeContexts.get(System.identityHashCode(instanceMap));
                    destroyBeans(instanceMap, contextMap);
                }
            }

            activeViewScopeContexts.clear();
            session.removeAttribute(ACTIVE_VIEW_CONTEXTS);
        }
    }
}
