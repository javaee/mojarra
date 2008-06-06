/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
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

package com.sun.faces.application;

import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestAttributeEvent;
import javax.servlet.ServletRequestEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;

import com.sun.faces.el.ELUtils;
import com.sun.faces.io.FastStringWriter;
import com.sun.faces.mgbean.BeanManager;
import com.sun.faces.util.FacesLogger;
import java.util.Map;
import com.sun.faces.application.resource.ResourceCache;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.SystemEvent;
import javax.faces.event.ViewMapCreatedEvent;
import javax.faces.event.ViewMapCreatedEvent;
import javax.faces.event.ViewMapDestroyedEvent;
import javax.faces.event.ViewMapListener;

/**
 * <p>Central location for web application lifecycle events.<p>  
 * <p>The main purpose of this class is detect when we
 * should be invoking methods marked with the
 * <code>@PreDestroy</code> annotation.</p>
 */
public class WebappLifecycleListener implements ViewMapListener {

    // Log instance for this class
    private static final Logger LOGGER = FacesLogger.APPLICATION.getLogger();
    
    private ServletContext servletContext;
    private ApplicationAssociate applicationAssociate;
    private List<HttpSession> activeSessions;


    // ------------------------------------------------------------ Constructors


    public WebappLifecycleListener() { }

    public WebappLifecycleListener(ServletContext servletContext) {

        this.servletContext = servletContext;

    }


    // ---------------------------------------------------------- Public Methods

    /**
     * The request is about to go out of scope of the web application.
     *
     * @param event the notification event
     */
    public void requestDestroyed(ServletRequestEvent event) {       
        ServletRequest request = event.getServletRequest();
        for (Enumeration e = request.getAttributeNames(); e.hasMoreElements(); ) {
            String beanName = (String)e.nextElement();
            handleAttributeEvent(beanName, 
                                 request.getAttribute(beanName), 
                                 ELUtils.Scope.REQUEST);
        }
        syncSessionScopedBeans(request);
        ApplicationAssociate.setCurrentInstance(null);
    }

    /**
     * The request is about to come into scope of the web application.
     *
     * @param event the notification event
     */
    public void requestInitialized(ServletRequestEvent event) {
        ApplicationAssociate.setCurrentInstance(getAssociate());
    }

    public boolean isListenerForSource(Object component) {
        return (component instanceof UIViewRoot);
    }

    public void processEvent(SystemEvent event)
          throws AbortProcessingException {
        if (event instanceof ViewMapCreatedEvent) {

        } else if (event instanceof ViewMapDestroyedEvent) {
            Map<String, Object> viewMap =
                  ((UIViewRoot) event.getSource()).getViewMap(false);
            if (viewMap != null && viewMap.size() != 0) {
                for (Map.Entry<String, Object> cur : viewMap.entrySet()) {
                    handleAttributeEvent(cur.getKey(), cur.getValue(),
                                         ELUtils.Scope.VIEW);
                }
            }
        }
    }

    /**
     * Notfication that a session has been created.
     * @param event the notification event
     */
    public void sessionCreated(HttpSessionEvent event) {
        ApplicationAssociate associate = getAssociate();
        // PENDING this should only create a new list if in dev mode
         if (associate != null && associate.isDevModeEnabled()) {
            if (activeSessions == null) {
                activeSessions = new ArrayList<HttpSession>();
            }
            activeSessions.add(event.getSession());
        }
    }



    /**
     * Notification that a session is about to be invalidated.
     *
     * @param event the notification event
     */
    public void sessionDestroyed(HttpSessionEvent event) {        
        HttpSession session = event.getSession();
        if (activeSessions != null) {
            activeSessions.remove(event.getSession());
        }
        for (Enumeration e = session.getAttributeNames(); e.hasMoreElements(); ) {
            String beanName = (String)e.nextElement();
            handleAttributeEvent(beanName, 
                                 session.getAttribute(beanName), 
                                 ELUtils.Scope.SESSION);
        }
    }

    /**
     * Notification that an existing attribute has been removed from the
     * servlet request. Called after the attribute is removed.
     * @param event the notification event
     */
    public void attributeRemoved(ServletRequestAttributeEvent event) {
        handleAttributeEvent(event.getName(),
                             event.getValue(),
                             ELUtils.Scope.REQUEST);
    }

    /**
     * Notification that an attribute was replaced on the
     * servlet request. Called after the attribute is replaced.
     *
     * @param event the notification event
     */
    public void attributeReplaced(ServletRequestAttributeEvent event) {
        String attrName = event.getName();
        Object newValue = event.getServletRequest().getAttribute(attrName);

        // perhaps a bit paranoid, but since the javadocs are a bit vague,
        // only handle the event if oldValue and newValue are not the
        // exact same object
        //noinspection ObjectEquality
        if (event.getValue() != newValue) {
            handleAttributeEvent(attrName,
                                 event.getValue(),
                                 ELUtils.Scope.REQUEST);
        }
    }
    

    /**
     * Notification that an attribute has been removed from a session.
     * Called after the attribute is removed.
     *
     * @param event the nofication event
     */
    public void attributeRemoved(HttpSessionBindingEvent event) {
        handleAttributeEvent(event.getName(),
                             event.getValue(),
                             ELUtils.Scope.SESSION);
    }

    /**
     * Notification that an attribute has been replaced in a session.
     * Called after the attribute is replaced.
     *
     * @param event the notification event
     */
    public void attributeReplaced(HttpSessionBindingEvent event) {
        HttpSession session = event.getSession();
        String attrName = event.getName();
        Object newValue = session.getAttribute(attrName);

        // perhaps a bit paranoid, but since the javadocs are a bit vague,
        // only handle the event if oldValue and newValue are not the
        // exact same object
        //noinspection ObjectEquality
        if (event.getValue() != newValue) {
            handleAttributeEvent(attrName,
                                 event.getValue(),
                                 ELUtils.Scope.SESSION);
        }

    }


    /**
     * Notification that an existing attribute has been removed from the servlet context.
     * Called after the attribute is removed.
     *
     * @param event the notification event
     */
    public void attributeRemoved(ServletContextAttributeEvent event) {
        handleAttributeEvent(event.getName(),
                             event.getValue(),
                             ELUtils.Scope.APPLICATION);
    }

    /**
     * Notification that an attribute on the servlet context has been replaced.
     * Called after the attribute is replaced.
     *
     * @param event the notification event
     */
    public void attributeReplaced(ServletContextAttributeEvent event) {
        ServletContext context = event.getServletContext();
        String attrName = event.getName();
        Object newValue = context.getAttribute(attrName);

        // perhaps a bit paranoid, but since the javadocs are a bit vague,
        // only handle the event if oldValue and newValue are not the
        // exact same object
        //noinspection ObjectEquality
        if (event.getValue() != newValue) {
            handleAttributeEvent(attrName,
                                 event.getValue(),
                                 ELUtils.Scope.APPLICATION);
        }
    }

    private void handleAttributeEvent(String beanName,
                                      Object bean,
                                      ELUtils.Scope scope) {

        ApplicationAssociate associate = getAssociate();
        try {
            if (associate != null) {
                BeanManager beanManager = associate.getBeanManager();
                if (beanManager != null && beanManager.isManaged(beanName)) {
                    beanManager.destroy(beanName, bean);
                }              
            }
        } catch (Exception e) {
            String className = e.getClass().getName();
            String message = e.getMessage();            
            if (message == null) {
                message = "";
            }
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.log(Level.INFO,
                           "jsf.config.listener.predestroy.error",
                           new Object[]{
                                 className,
                                 beanName,
                                 scope,
                                 message});
            }
            if (LOGGER.isLoggable(Level.FINE)) {
                FastStringWriter writer = new FastStringWriter(128);
                e.printStackTrace(new PrintWriter(writer));
                LOGGER.fine(writer.toString());
            }
        }

    } // END handleAttributeEvent

    /**
     * Notification that the web application initialization
     * process is starting.
     * All ServletContextListeners are notified of context
     * initialization before any filter or servlet in the web
     * application is initialized.
     *
     * @param event the notification event
     */
    public void contextInitialized(ServletContextEvent event) {
        if (this.servletContext == null) {
            this.servletContext = event.getServletContext();
        }
    }

    /**
     * Notification that the servlet context is about to be shut down.
     * All servlets and filters have been destroy()ed before any
     * ServletContextListeners are notified of context
     * destruction.
     *
     * @param event the nofication event
     */
    public void contextDestroyed(ServletContextEvent event) {
        
        for (Enumeration e = servletContext.getAttributeNames(); e.hasMoreElements(); ) {
            String beanName = (String)e.nextElement();
            handleAttributeEvent(beanName, 
                                 servletContext.getAttribute(beanName), 
                                 ELUtils.Scope.APPLICATION);
        }
        // HACK - we'll improve this when the event system is in place
        if (applicationAssociate != null) {
            ResourceCache cache = applicationAssociate.getResourceCache();
            if (cache != null) {
                cache.shutdown();
            }
        }
        this.applicationAssociate = null;

    }


    public List<HttpSession> getActiveSessions() {
        return activeSessions;
    }


    // --------------------------------------------------------- Private Methods


    private ApplicationAssociate getAssociate() {

        if (applicationAssociate == null) {
            applicationAssociate = ApplicationAssociate.getInstance(servletContext);
        }

        return applicationAssociate;
    }


    /**
     * This method ensures that session scoped managed beans will be
     * synchronized properly in a clustered environment.
     *
     * @param request the current <code>ServletRequest</code>
     */
    private void syncSessionScopedBeans(ServletRequest request) {

        if (request instanceof HttpServletRequest) {
            HttpSession session = ((HttpServletRequest) request).getSession(false);
            if (session != null) {
                ApplicationAssociate associate = getAssociate();
                if (associate == null) {
                    return;
                }
                BeanManager manager = associate.getBeanManager();
                if (manager != null) {
                    for (Enumeration e = session.getAttributeNames();
                         e.hasMoreElements();) {
                        String name = (String) e.nextElement();
                        if (manager.isManaged(name)) {
                            session
                                  .setAttribute(name, session.getAttribute(name));
                        }
                    }
                }
            }
        }

    }

} // END WebappLifecycleListener
