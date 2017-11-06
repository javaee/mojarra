/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.java.net/public/CDDL+GPL_1_1.html
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

package com.sun.faces.config;

import static com.sun.faces.RIConstants.FACES_PREFIX;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.el.ELContext;
import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.ProjectStage;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import com.sun.faces.RIConstants;
import com.sun.faces.config.initfacescontext.NoOpELContext;
import com.sun.faces.config.initfacescontext.NoOpFacesContext;
import com.sun.faces.config.initfacescontext.ServletContextAdapter;
import com.sun.faces.context.ApplicationMap;
import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.Util;

/**
 * A special, minimal implementation of FacesContext used at application initialization time. The ExternalContext
 * returned by this FacesContext only exposes the ApplicationMap.
 */
public class InitFacesContext extends NoOpFacesContext {

    private static Logger LOGGER = FacesLogger.CONFIG.getLogger();
    private static final String INIT_FACES_CONTEXT_ATTR_NAME = RIConstants.FACES_PREFIX + "InitFacesContext";

    private ServletContextAdapter servletContextAdapter;
    private UIViewRoot viewRoot;
    private Map<Object, Object> attributes;

    private ELContext elContext = new NoOpELContext();

    public InitFacesContext(ServletContext servletContext) {
        servletContextAdapter = new ServletContextAdapter(servletContext);
        servletContext.setAttribute(INIT_FACES_CONTEXT_ATTR_NAME, this);
        InitFacesContext.cleanupInitMaps(servletContext);
        
        addServletContextEntryForInitContext(servletContext);
        addInitContextEntryForCurrentThread();
    }

    @Override
    public Map<Object, Object> getAttributes() {
        if (attributes == null) {
            attributes = new HashMap<>();
        }
        
        return attributes;
    }
    
    @Override
    public ExternalContext getExternalContext() {
        return servletContextAdapter;
    }
    
    @Override
    public UIViewRoot getViewRoot() {
        if (viewRoot == null) {
            viewRoot = new UIViewRoot();
            viewRoot.setLocale(Locale.getDefault());
            viewRoot.setViewId(FACES_PREFIX + "xhtml");
        }
        
        return viewRoot;
    }
    
    @Override
    public ELContext getELContext() {
        return elContext;
    }

    public void setELContext(ELContext elContext) {
        this.elContext = elContext;
    }

    @Override
    public Application getApplication() {
        ApplicationFactory factory = (ApplicationFactory) FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        return factory.getApplication();
    }
    
    @Override
    public boolean isProjectStage(ProjectStage stage) {
        if (stage == null) {
            throw new NullPointerException();
        }
        return stage.equals(getApplication().getProjectStage());
    }
    
    @Override
    public void release() {
        
        setCurrentInstance(null);
        if (servletContextAdapter != null) {
            Map<String, Object> applicationMap = servletContextAdapter.getApplicationMap();
            if (applicationMap instanceof ApplicationMap) {
                if (((ApplicationMap) applicationMap).getContext() != null) {
                    applicationMap.remove(INIT_FACES_CONTEXT_ATTR_NAME);
                }
            }
            servletContextAdapter.release();
        }

        if (attributes != null) {
            attributes.clear();
            attributes = null;
        }
        
        elContext = null;
        
        if (viewRoot != null) {
            Map<String, Object> viewMap = viewRoot.getViewMap(false);
            if (viewMap != null) {
                viewMap.clear();
            }
            viewRoot = null;
        }
    }
    
    public void releaseCurrentInstance() {
        removeInitContextEntryForCurrentThread();
        setCurrentInstance(null);
    }

    public void addInitContextEntryForCurrentThread() {
        getThreadInitContextMap().put(Thread.currentThread(), this);
    }
    
    public void removeInitContextEntryForCurrentThread() {
        getThreadInitContextMap().remove(Thread.currentThread());
    }
    
    public void addServletContextEntryForInitContext(ServletContext servletContext) {
        getInitContextServletContextMap().put(this, servletContext);
    }

    public void removeServletContextEntryForInitContext() {
        getInitContextServletContextMap().remove(this);
    }
    
    /**
     * Clean up entries from the threadInitContext and initContextServletContext maps using a ServletContext. First remove
     * entry(s) with matching ServletContext from initContextServletContext map. Then remove entries from threadInitContext
     * map where the entry value(s) match the initFacesContext (associated with the ServletContext).
     * 
     * @param servletContext
     */
    public static void cleanupInitMaps(ServletContext servletContext) {
        
        Map<InitFacesContext, ServletContext> facesContext2ServletContext = getInitContextServletContextMap();
        Map<Thread, InitFacesContext> thread2FacesContext = getThreadInitContextMap();
        
        // First remove entry(s) with matching ServletContext from the initContextServletContext map.
        
        for (Entry<InitFacesContext, ServletContext> facesContext2ServletContextEntry : new ArrayList<>(facesContext2ServletContext.entrySet())) {
            
            if (facesContext2ServletContextEntry.getValue() == servletContext) {
                
                facesContext2ServletContext.remove(facesContext2ServletContextEntry.getKey());
                
                // Then remove entries from the threadInitContext map where the entry value(s) match the initFacesContext 
                // (associated with the ServletContext).
                
                for (Entry<Thread, InitFacesContext> thread2FacesContextEntry : new ArrayList<>(thread2FacesContext.entrySet())) {
                    
                    if (thread2FacesContextEntry.getValue() == facesContext2ServletContextEntry.getKey()) {
                        thread2FacesContext.remove(thread2FacesContextEntry.getKey());
                    }
                    
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    static Map<Thread, InitFacesContext> getThreadInitContextMap() {
        try {
            Field threadMap = FacesContext.class.getDeclaredField("threadInitContext");
            threadMap.setAccessible(true);
            
            return (Map<Thread, InitFacesContext>) threadMap.get(null);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            LOGGER.log(Level.FINEST, "Unable to get (thread, init context) map", e);
        }
        
        return null;
    }

    @SuppressWarnings("unchecked")
    static Map<InitFacesContext, ServletContext> getInitContextServletContextMap() {
        try {
            Field initContextMap = FacesContext.class.getDeclaredField("initContextServletContext");
            initContextMap.setAccessible(true);
            
            return (Map<InitFacesContext, ServletContext>) initContextMap.get(null);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            LOGGER.log(Level.FINEST, "Unable to get (init context, servlet context) map", e);
        }
        
        return null;
    }
    
    public static InitFacesContext getInstance(ServletContext servletContext) {
        InitFacesContext result = (InitFacesContext) servletContext.getAttribute(INIT_FACES_CONTEXT_ATTR_NAME);
        if (result != null) {
            result.addInitContextEntryForCurrentThread();
        }

        return result;
    }
    
    
    
    
    
    
    
    // Cactus / unit test only
    
    public void reInitializeExternalContext(ServletContext sc) {
        assert (Util.isUnitTestModeEnabled());
        servletContextAdapter = new ServletContextAdapter(sc);
    }    

}
