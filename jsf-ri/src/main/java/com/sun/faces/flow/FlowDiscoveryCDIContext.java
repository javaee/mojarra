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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.enterprise.context.spi.Context;
import javax.enterprise.context.spi.Contextual;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeShutdown;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.flow.FlowDefinition;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

public class FlowDiscoveryCDIContext implements Context, Serializable {
    
    private static final long serialVersionUID = -7144653402477623609L;
    
    private transient Map<Contextual<?>, FlowDiscoveryInfo> flowBuilders;
        
    // This should be vended from a factory for decoration purposes.
    
    public FlowDiscoveryCDIContext(Map<Contextual<?>, FlowDiscoveryInfo> producers) {
        this.flowBuilders = new ConcurrentHashMap<Contextual<?>, FlowDiscoveryInfo>(producers);
    }
    
    private static final String FLOW_DEFINITION_SCOPE_BEAN_MAP_KEY = FlowDiscoveryCDIContext.class.getName() + "_BEANS";
    private static final String FLOW_DEFINITION_SCOPE_CREATIONAL_MAP_KEY = FlowDiscoveryCDIContext.class.getName() + "_CREATIONAL";
    
    
    private static final String PER_CONTEXT_BEAN_MAP_LIST = FlowDiscoveryCDIContext.class.getPackage().getName() + ".PER_CONTEXT_BEAN_MAP_LIST";
    private static final String PER_CONTEXT_CREATIONAL_LIST = FlowDiscoveryCDIContext.class.getPackage().getName() + ".PER_CONTEXT_CREATIONAL_LIST";

    // -------------------------------------------------------- Private Methods
    
    // <editor-fold defaultstate="collapsed" desc="Private helpers">       
    
    private static Map<Contextual<?>, Object> getFlowScopedBeanMapForCurrentFlow() {

        Map<Contextual<?>, Object> result;
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext extContext = context.getExternalContext();
        Map<String, Object> appMap = extContext.getApplicationMap();
        
        result = (Map<Contextual<?>, Object>) appMap.get(FLOW_DEFINITION_SCOPE_BEAN_MAP_KEY);
        if (null == result) {
            result = new ConcurrentHashMap<Contextual<?>, Object>();
            appMap.put(FLOW_DEFINITION_SCOPE_BEAN_MAP_KEY, result);
            ensureBeanMapCleanupOnContextDestroyed(appMap, FLOW_DEFINITION_SCOPE_BEAN_MAP_KEY);
        }
        
        return result;
    }
    
    private static Map<Contextual<?>, CreationalContext<?>> getFlowScopedCreationalMapForCurrentFlow() {
        Map<Contextual<?>, CreationalContext<?>> result;
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext extContext = context.getExternalContext();
        Map<String, Object> appMap = extContext.getApplicationMap();

        result = (Map<Contextual<?>, CreationalContext<?>>) appMap.get(FLOW_DEFINITION_SCOPE_CREATIONAL_MAP_KEY);
        if (null == result) {
            result = new ConcurrentHashMap<Contextual<?>, CreationalContext<?>>();
            appMap.put(FLOW_DEFINITION_SCOPE_CREATIONAL_MAP_KEY, result);
            ensureCreationalCleanupOnContextDestroyed(appMap, FLOW_DEFINITION_SCOPE_CREATIONAL_MAP_KEY);
        }
        
        return result;

    }
    
    private static void ensureBeanMapCleanupOnContextDestroyed(Map<String, Object> appMap, String flowBeansForClientWindow) {
        List<String> beanMapList = (List<String>) appMap.get(PER_CONTEXT_BEAN_MAP_LIST);
        if (null == beanMapList) {
            beanMapList = new ArrayList<String>();
            appMap.put(PER_CONTEXT_BEAN_MAP_LIST, beanMapList);
        }
        beanMapList.add(flowBeansForClientWindow);
    }
    
    private static void ensureCreationalCleanupOnContextDestroyed(Map<String, Object> appMap, String creationalForClientWindow) {
        List<String> beanMapList = (List<String>) appMap.get(PER_CONTEXT_CREATIONAL_LIST);
        if (null == beanMapList) {
            beanMapList = new ArrayList<String>();
            appMap.put(PER_CONTEXT_CREATIONAL_LIST, beanMapList);
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
    
    // <editor-fold defaultstate="collapsed" desc="Called from code related to flow">
    
    List<FlowDiscoveryInfo> getFlowDefiningClasses() {
        List<FlowDiscoveryInfo> result = null;
        
        if (null != flowBuilders && !flowBuilders.isEmpty()) {
            result = new ArrayList<FlowDiscoveryInfo>();
            for (FlowDiscoveryInfo cur : flowBuilders.values()) {
                result.add(cur);
            }
        } else {
            result = Collections.emptyList();
        }
        
        return result;
    }
    
    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="Called from code not related to flow">       
    
    public static void contextDestroyed(ServletContextEvent hse) {
        ServletContext context = hse.getServletContext();
        
        List<String> beanMapList = (List<String>) context.getAttribute(PER_CONTEXT_BEAN_MAP_LIST);
        if (null != beanMapList) {
            for (String cur : beanMapList) {
                Map<Contextual<?>, Object> beanMap = 
                        (Map<Contextual<?>, Object>) context.getAttribute(cur);
                beanMap.clear();
                context.removeAttribute(cur);
            }
            context.removeAttribute(PER_CONTEXT_BEAN_MAP_LIST);
            beanMapList.clear();
        }
        
        List<String> creationalList = (List<String>) context.getAttribute(PER_CONTEXT_CREATIONAL_LIST);
        if (null != creationalList) {
            for (String cur : creationalList) {
                Map<Contextual<?>, CreationalContext<?>> beanMap = 
                        (Map<Contextual<?>, CreationalContext<?>>) context.getAttribute(cur);
                beanMap.clear();
                context.removeAttribute(cur);
            }
            context.removeAttribute(PER_CONTEXT_CREATIONAL_LIST);
            creationalList.clear();
        }
        
        
    }
    
    // </editor-fold>

    
    // <editor-fold defaultstate="collapsed" desc="spi.Context implementation">       
    
    public <T> T get(Contextual<T> contextual, CreationalContext<T> creational) {
        assertNotReleased();
        
        T result = get(contextual);
        
        if (null == result) {
            Map<Contextual<?>, Object> flowScopedBeanMap = getFlowScopedBeanMapForCurrentFlow();
            Map<Contextual<?>, CreationalContext<?>> creationalMap = getFlowScopedCreationalMapForCurrentFlow();
            
            synchronized (flowScopedBeanMap) {
                result = (T) flowScopedBeanMap.get(contextual);
                if (null == result) {
                    
                    result = contextual.create(creational);
                    if (null != result) {
                        flowScopedBeanMap.put(contextual, result);
                        creationalMap.put(contextual, creational);
                    }
                }
            }
        }
        
        return result;

    }
    
    public <T> T get(Contextual<T> contextual) {
        assertNotReleased();
        
        return (T) getFlowScopedBeanMapForCurrentFlow().get(contextual);
    }
    
    public Class<? extends Annotation> getScope() {
        return FlowDefinition.class;
    }
    
    public boolean isActive() {
        return true;
    }
    
    void beforeShutdown(@Observes final BeforeShutdown event, BeanManager beanManager) {
    }
    
    // </editor-fold>
    
}
