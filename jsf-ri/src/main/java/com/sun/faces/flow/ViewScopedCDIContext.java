/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright (c) 1997-2012 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.faces.flow;

import java.io.Serializable;
import java.lang.annotation.Annotation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import javax.enterprise.context.spi.Context;
import javax.enterprise.context.spi.Contextual;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeShutdown;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.flow.ViewScoped;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;

public class ViewScopedCDIContext implements Context, Serializable {
    
    private static final long serialVersionUID = -6245899073989073951L;

    private static final String VIEW_SCOPE_BEAN_MAP_KEY = ViewScopedCDIContext.class.getName() + "_BEANS";
    private static final String VIEW_SCOPE_CREATIONAL_MAP_KEY = ViewScopedCDIContext.class.getName() + "_CREATIONAL";
    
    private static final String PER_SESSION_BEAN_MAP_LIST = ViewScopedCDIContext.class.getPackage().getName() + ".PER_SESSION_BEAN_MAP_LIST";
    private static final String PER_SESSION_CREATIONAL_LIST = ViewScopedCDIContext.class.getPackage().getName() + ".PER_SESSION_CREATIONAL_LIST";
    
    // This should be vended from a factory for decoration purposes.
    
    public ViewScopedCDIContext() {
    }
    
    // -------------------------------------------------------- Private Methods
    
    // <editor-fold defaultstate="collapsed" desc="Private helpers">       
    private static Map<Contextual<?>, Object> getViewScopedBeanMapForCurrentView() {

        return getViewScopedBeanMapForCurrentView(true);
    }
    
    private static Map<Contextual<?>, Object> getViewScopedBeanMapForCurrentView(boolean create) {

        Map<Contextual<?>, Object> result = null;
        FacesContext context = FacesContext.getCurrentInstance();
        if (null == context) {
            return null;
        }
        
        UIViewRoot viewRoot = context.getViewRoot();
        
        if (null != viewRoot) {
            // We obtain a reference to the sessionMap, but this is not used to
            // store the bean instances marked with @ViewScoped.  Rather, 
            // the existence of the sessionMap is used to determine if we have any @ViewScoped beans
            // in the first place.
            Map<String, Object> viewMap = viewRoot.getViewMap(create);
            if (null != viewMap) { 
                // The @ViewScoped bean instances themselves are stored in session scope.
                ExternalContext extContext = context.getExternalContext();
                Map<String, Object> sessionMap = extContext.getSessionMap();
                
                result = (Map<Contextual<?>, Object>) sessionMap.get(VIEW_SCOPE_BEAN_MAP_KEY);
                if (null == result && create) {
                    result = new ConcurrentHashMap<Contextual<?>, Object>();
                    sessionMap.put(VIEW_SCOPE_BEAN_MAP_KEY, result);
                    ensureBeanMapCleanupOnSessionDestroyed(sessionMap, VIEW_SCOPE_BEAN_MAP_KEY);
                }
            }
        }
        
        return result;
    }

    private static Map<Contextual<?>, CreationalContext<?>> getViewScopedCreationalMapForCurrentView() {
        return getViewScopedCreationalMapForCurrentView(true);
    }    

    private static Map<Contextual<?>, CreationalContext<?>> getViewScopedCreationalMapForCurrentView(boolean create) {
        Map<Contextual<?>, CreationalContext<?>> result;
        FacesContext context = FacesContext.getCurrentInstance();
        if (null == context) {
            return null;
        }
        
        ExternalContext extContext = context.getExternalContext();
        Map<String, Object> sessionMap = extContext.getSessionMap();

        result = (Map<Contextual<?>, CreationalContext<?>>) sessionMap.get(VIEW_SCOPE_CREATIONAL_MAP_KEY);
        if (null == result && create) {
            result = new ConcurrentHashMap<Contextual<?>, CreationalContext<?>>();
            sessionMap.put(VIEW_SCOPE_CREATIONAL_MAP_KEY, result);
            ensureCreationalCleanupOnSessionDestroyed(sessionMap, VIEW_SCOPE_CREATIONAL_MAP_KEY);
        }
        
        return result;

    }
    
    private static void ensureBeanMapCleanupOnSessionDestroyed(Map<String, Object> sessionMap, String flowBeansForClientWindow) {
        List<String> beanMapList = (List<String>) sessionMap.get(PER_SESSION_BEAN_MAP_LIST);
        if (null == beanMapList) {
            beanMapList = new ArrayList<String>();
            sessionMap.put(PER_SESSION_BEAN_MAP_LIST, beanMapList);
        }
        beanMapList.add(flowBeansForClientWindow);
    }
    
    private static void ensureCreationalCleanupOnSessionDestroyed(Map<String, Object> sessionMap, String creationalForClientWindow) {
        List<String> beanMapList = (List<String>) sessionMap.get(PER_SESSION_CREATIONAL_LIST);
        if (null == beanMapList) {
            beanMapList = new ArrayList<String>();
            sessionMap.put(PER_SESSION_CREATIONAL_LIST, beanMapList);
        }
        beanMapList.add(creationalForClientWindow);
    }
    
    
    
    @SuppressWarnings({"FinalPrivateMethod"})
    private final void assertNotReleased() {
        if (!isActive()) {
            throw new IllegalStateException();
        }
    }
    
    // </editor-fold>    
    
    // <editor-fold defaultstate="collapsed" desc="Called from code not related to flow">       
    
    /*
     * Called from WebappLifecycleListener.sessionDestroyed()
     */
    
    public static void sessionDestroyed(HttpSessionEvent hse) {
        HttpSession session = hse.getSession();
        clearViewScopedBeans();
        
        List<String> beanMapList = (List<String>) session.getAttribute(PER_SESSION_BEAN_MAP_LIST);
        if (null != beanMapList) {
            for (String cur : beanMapList) {
                Map<Contextual<?>, Object> beanMap = 
                        (Map<Contextual<?>, Object>) session.getAttribute(cur);
                beanMap.clear();
                session.removeAttribute(cur);
            }
            session.removeAttribute(PER_SESSION_BEAN_MAP_LIST);
            beanMapList.clear();
        }
        
        List<String> creationalList = (List<String>) session.getAttribute(PER_SESSION_CREATIONAL_LIST);
        if (null != creationalList) {
            for (String cur : creationalList) {
                Map<Contextual<?>, CreationalContext<?>> beanMap = 
                        (Map<Contextual<?>, CreationalContext<?>>) session.getAttribute(cur);
                beanMap.clear();
                session.removeAttribute(cur);
            }
            session.removeAttribute(PER_SESSION_CREATIONAL_LIST);
            creationalList.clear();
        }
        
        
    }
    
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Called from code related to flow">  
    
    public static void clearViewScopedBeans() {
        Map<Contextual<?>, Object> viewScopedBeanMap = getViewScopedBeanMapForCurrentView(false);
        if (null == viewScopedBeanMap) {
            return;
        }
        
        Map<Contextual<?>, CreationalContext<?>> creationalMap = getViewScopedCreationalMapForCurrentView(false);
        if (null == creationalMap) {
            return;
        }
        
        List<Contextual<?>> viewScopedBeansToRemove = new ArrayList<Contextual<?>>();
        
        for (Entry<Contextual<?>, Object> entry : viewScopedBeanMap.entrySet()) {
            Contextual owner = entry.getKey();
            Object bean = entry.getValue();
            CreationalContext creational = creationalMap.get(owner);
            
            owner.destroy(bean, creational);
            viewScopedBeansToRemove.add(owner);
        }
        
        for (Contextual<?> cur : viewScopedBeansToRemove) {
            viewScopedBeanMap.remove(cur);
            creationalMap.remove(cur);
        }
        
    }

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="spi.Context implementation">       
    
    public <T> T get(Contextual<T> contextual, CreationalContext<T> creational) {
        assertNotReleased();
        
        T result = get(contextual);
        
        if (null == result) {
            Map<Contextual<?>, Object> viewScopedBeanMap = getViewScopedBeanMapForCurrentView();
            Map<Contextual<?>, CreationalContext<?>> creationalMap = getViewScopedCreationalMapForCurrentView();
            
            synchronized (viewScopedBeanMap) {
                result = (T) viewScopedBeanMap.get(contextual);
                if (null == result) {
                    
                    result = contextual.create(creational);
                    
                    if (null != result) {
                        viewScopedBeanMap.put(contextual, result);
                        creationalMap.put(contextual, creational);
                    }
                }
            }
        }
        
        return result;

    }
    
    public <T> T get(Contextual<T> contextual) {
        assertNotReleased();
        
        return (T) getViewScopedBeanMapForCurrentView().get(contextual);
    }
    
    public Class<? extends Annotation> getScope() {
        return ViewScoped.class;
    }
    
    public boolean isActive() {
        boolean result = false;
        
        FacesContext context = FacesContext.getCurrentInstance();
        if (null != context) {
            UIViewRoot viewRoot = context.getViewRoot();
            
            if (null != viewRoot) {
                result = true;
            }
        }
        return result;
    }
    
    void beforeShutdown(@Observes final BeforeShutdown event, BeanManager beanManager) {
    }
    
    // </editor-fold>
    
}
