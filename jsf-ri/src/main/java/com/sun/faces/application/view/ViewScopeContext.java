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

import com.sun.faces.mgbean.BeanManager;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ContextNotActiveException;
import javax.enterprise.context.spi.Context;
import javax.enterprise.context.spi.Contextual;
import javax.enterprise.context.spi.CreationalContext;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;

/**
 * The CDI context for CDI ViewScoped beans.
 */
public class ViewScopeContext implements Context, Serializable {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = Logger.getLogger(ViewScopeContext.class.getName());
    /**
     * Stores the serial version UID.
     */
    private static final long serialVersionUID = -6245899073989073951L;
    /**
     * Stores the constant to keep track of all the active view scope contexts.
     */
    private static final String ACTIVE_VIEW_SCOPE_CONTEXTS = "com.sun.faces.application.view.activeViewScopeContexts";

    /**
     * Constructor.
     */
    public ViewScopeContext() {
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.log(Level.FINEST, "Creating ViewScope CDI context");
        }
    }

    /**
     * Assert the context is active, otherwise throw ContextNotActiveException.
     */
    @SuppressWarnings({"FinalPrivateMethod"})
    private final void assertNotReleased() {
        if (!isActive()) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, "Trying to access ViewScope CDI context while it is not active");
            }
            throw new ContextNotActiveException();
        }
    }

    /**
     * Clear the ViewScoped CDI beans.
     *
     * <p> This method will remove any ViewScoped CDI bean from the current view
     * map and cause PreDestroy to be invoked upon each of those beans. </p>
     *
     * <p> ASSUMPTION: this method needs to be called before the old style
     * ViewScoped beans are cleaned up. </p>
     */
    public static void clearViewScopedBeans() {
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.log(Level.FINEST, "Clearing @ViewScoped CDI beans");
        }
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext != null) {
            UIViewRoot viewRoot = facesContext.getViewRoot();
            if (viewRoot != null) {
                Map<String, Object> instanceMap = viewRoot.getViewMap(false);
                if (instanceMap != null) {
                    Map<Contextual, ViewScopeContextObject> contextMap = getContextMap(instanceMap, false);
                    if (contextMap != null) {
                        destroyViewScopedBeans(instanceMap, contextMap);
                    }
                }
            }
        }
    }

    /**
     * Destroy the view scoped beans for the given instance and context map.
     *
     * @param instanceMap the instance map.
     * @param contextMap the context map.
     */
    private static void destroyViewScopedBeans(Map<String, Object> instanceMap, Map<Contextual, ViewScopeContextObject> contextMap) {
        ArrayList<String> removalNameList = new ArrayList<String>();

        if (contextMap != null) {
            for (Map.Entry<Contextual, ViewScopeContextObject> entry : contextMap.entrySet()) {
                Contextual contextual = entry.getKey();
                ViewScopeContextObject contextObject = entry.getValue();
                CreationalContext creationalContext = contextObject.getCreationalContext();
                contextual.destroy(instanceMap.get(contextObject.getName()), creationalContext);
                removalNameList.add(contextObject.getName());
            }

            Iterator<String> removalNames = removalNameList.iterator();
            while (removalNames.hasNext()) {
                String name = removalNames.next();
                instanceMap.remove(name);
            }
        }
    }

    /**
     * Get the ViewScoped bean for the given contextual.
     *
     * @param <T> the type.
     * @param contextual the contextual.
     * @return the view scoped bean, or null if not found.
     */
    @Override
    public <T> T get(Contextual<T> contextual) {
        assertNotReleased();

        T result = null;

        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext != null) {
            UIViewRoot viewRoot = facesContext.getViewRoot();
            if (viewRoot != null) {
                Map<String, Object> viewMap = viewRoot.getViewMap(false);
                if (viewMap != null) {
                    result = getFromInstanceMap(viewMap, contextual);
                }
            }
        }

        return result;
    }

    /**
     * Get the existing instance of the ViewScoped bean for the given contextual
     * or create a new one.
     *
     * @param <T> the type.
     * @param contextual the contextual.
     * @param creational the creational.
     * @return the instance.
     * @throws ContextNotActiveException when the context is not active.
     */
    @Override
    public <T> T get(Contextual<T> contextual, CreationalContext<T> creational) {
        assertNotReleased();

        T result = get(contextual);

        if (result == null) {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            if (facesContext != null) {
                UIViewRoot viewRoot = facesContext.getViewRoot();
                if (viewRoot != null) {
                    Map<String, Object> instanceMap = viewRoot.getViewMap();
                    if (instanceMap != null) {
                        Map<Contextual, ViewScopeContextObject> contextMap = getContextMap(instanceMap);
                        synchronized (instanceMap) {
                            result = getFromInstanceMap(instanceMap, contextual);
                            if (result == null) {
                                if (LOGGER.isLoggable(Level.FINEST)) {
                                    LOGGER.log(Level.FINEST, "Creating bean for @ViewScoped bean with name: {0}",
                                            contextual.toString());
                                }

                                result = contextual.create(creational);

                                if (LOGGER.isLoggable(Level.FINEST)) {
                                    LOGGER.log(Level.FINEST, "Created bean: {0} for @ViewScoped bean with name: {1}",
                                            new Object[]{result, contextual.toString()});
                                }

                                if (result != null) {
                                    String name = getName(result);
                                    instanceMap.put(name, result);
                                    contextMap.put(contextual, new ViewScopeContextObject(contextual, creational, name));
                                }
                            }
                        }
                    }
                }
            }
        }

        return result;
    }

    /**
     * Get the context map.
     *
     * @param instanceMap the instance map.
     * @return the context map.
     */
    private static Map<Contextual, ViewScopeContextObject> getContextMap(Map<String, Object> instanceMap) {
        return getContextMap(instanceMap, true);
    }

    /**
     * Get the context map.
     *
     * @param instanceMap the instance map.
     * @param create flag to indicate if we are creating the context map.
     * @return the context map.
     */
    private static Map<Contextual, ViewScopeContextObject> getContextMap(Map<String, Object> instanceMap, boolean create) {
        Map<Contextual, ViewScopeContextObject> result = null;

        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext != null) {
            ExternalContext externalContext = facesContext.getExternalContext();
            if (externalContext != null) {
                Map<String, Object> sessionMap = externalContext.getSessionMap();
                Map<Object, Map<Contextual, ViewScopeContextObject>> activeViewScopeContexts =
                        (Map<Object, Map<Contextual, ViewScopeContextObject>>) sessionMap.get(ACTIVE_VIEW_SCOPE_CONTEXTS);

                if (activeViewScopeContexts == null && create) {
                    synchronized (sessionMap) {
                        activeViewScopeContexts = new ConcurrentHashMap<Object, Map<Contextual, ViewScopeContextObject>>();
                        sessionMap.put(ACTIVE_VIEW_SCOPE_CONTEXTS, activeViewScopeContexts);
                    }
                }

                if (activeViewScopeContexts != null && create) {
                    synchronized (activeViewScopeContexts) {
                        if (!activeViewScopeContexts.containsKey(System.identityHashCode(instanceMap)) && create) {
                            activeViewScopeContexts.put(System.identityHashCode(instanceMap), new ConcurrentHashMap<Contextual, ViewScopeContextObject>());
                        }
                    }
                }
                
                if (activeViewScopeContexts != null) {
                    result = activeViewScopeContexts.get(System.identityHashCode(instanceMap));
                }
            }
        }
        
        return result;
    }

    /**
     * Get the value from the instance view map (or null if not found).
     *
     * @param <T> the type.
     * @param instanceMap the instance map.
     * @param contextual the contextual.
     * @return the value or null if not found.
     */
    private <T> T getFromInstanceMap(Map<String, Object> instanceMap, Contextual<T> contextual) {
        T result = null;

        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.log(Level.FINEST, "Getting value for @ViewScoped bean with name: {0}", contextual.toString());
        }

        Map<Contextual, ViewScopeContextObject> contextMap = getContextMap(instanceMap);
        if (contextMap != null) {
            ViewScopeContextObject contextObject = contextMap.get(contextual);
            if (contextObject != null) {
                result = (T) instanceMap.get(contextObject.getName());
            }
        }

        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.log(Level.FINEST, "Found value: {0} for @ViewScoped bean with name: {1}",
                    new Object[]{result, contextual.toString()});
        }

        return result;
    }

    /**
     * Get the name of the bean for the given object.
     *
     * @param object the object.
     * @return the name.
     */
    private String getName(Object object) {
        String name = object.getClass().getSimpleName().substring(0, 1).toLowerCase()
                + object.getClass().getSimpleName().substring(1);

        Named named = object.getClass().getAnnotation(Named.class);
        if (named != null && named.value() != null && !named.value().trim().equals("")) {
            name = named.value();
        }
        return name;
    }

    /**
     * Get the class of the scope object.
     *
     * @return the class.
     */
    @Override
    public Class<? extends Annotation> getScope() {
        return ViewScoped.class;
    }

    /**
     * Determine if the context is active.
     *
     * @return true if there is a view root, false otherwise.
     */
    @Override
    public boolean isActive() {
        boolean result = false;

        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext != null) {
            UIViewRoot viewRoot = facesContext.getViewRoot();
            if (viewRoot != null) {
                result = true;
            }
        }

        return result;
    }

    /**
     * Called as part of session cleanup.
     *
     * <p> See WebappLifecycleListener for more information. </p>
     *
     * <p> ASSUMPTION: this method needs to be called before the old style
     * ViewScoped beans are cleaned up as part of the session cleanup. </p>
     *
     * @param hse the event.
     */
    public static void sessionDestroyed(HttpSessionEvent hse) {
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.log(Level.FINEST, "Cleaning up session for @ViewScoped CDI beans");
        }

        HttpSession session = hse.getSession();

        Map<Object, Map<Contextual, ViewScopeContextObject>> activeViewScopeContexts =
                (Map<Object, Map<Contextual, ViewScopeContextObject>>) session.getAttribute(ACTIVE_VIEW_SCOPE_CONTEXTS);

        if (activeViewScopeContexts != null) {
            Map<String, Object> activeViewMaps = (Map<String, Object>) session.getAttribute("com.sun.faces.activeViewMaps");
            if (activeViewMaps != null) {
                Iterator<Object> activeViewMapsIterator = activeViewMaps.values().iterator();
                while(activeViewMapsIterator.hasNext()) {
                    Map<String, Object> instanceMap = (Map<String, Object>) activeViewMapsIterator.next();
                    Map<Contextual, ViewScopeContextObject> contextMap = activeViewScopeContexts.get(System.identityHashCode(instanceMap));
                    destroyViewScopedBeans(instanceMap, contextMap);
                }
            }

            activeViewScopeContexts.clear();
            session.removeAttribute(ACTIVE_VIEW_SCOPE_CONTEXTS);
        }
    }
}
