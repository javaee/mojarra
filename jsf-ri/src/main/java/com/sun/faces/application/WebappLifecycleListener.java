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

package com.sun.faces.application;

import com.sun.faces.application.view.ViewScopeManager;
import com.sun.faces.config.InitFacesContext;
import com.sun.faces.config.WebConfiguration;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import com.sun.faces.flow.FlowCDIContext;
import com.sun.faces.io.FastStringWriter;
import com.sun.faces.mgbean.BeanManager;
import com.sun.faces.renderkit.StateHelper;
import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.Util;
import javax.faces.application.Application;
import javax.faces.application.ViewHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

/**
 * <p>Central location for web application lifecycle events.<p>  
 * <p>The main purpose of this class is detect when we
 * should be invoking methods marked with the
 * <code>@PreDestroy</code> annotation.</p>
 */
public class WebappLifecycleListener {

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

        try {
            ServletRequest request = event.getServletRequest();
            for (Enumeration e = request.getAttributeNames(); e.hasMoreElements();) {
                String beanName = (String) e.nextElement();
                handleAttributeEvent(beanName,
                        request.getAttribute(beanName),
                        ELUtils.Scope.REQUEST);
            }
            WebConfiguration config = WebConfiguration.getInstance(event.getServletContext());
            if (config.isOptionEnabled(WebConfiguration.BooleanWebContextInitParameter.EnableAgressiveSessionDirtying)) {
                syncSessionScopedBeans(request);
            }
        } catch (Throwable t) {
            FacesContext context = new InitFacesContext(event.getServletContext());
            ExceptionQueuedEventContext eventContext =
                    new ExceptionQueuedEventContext(context, t);
            context.getApplication().publishEvent(context,
                    ExceptionQueuedEvent.class, eventContext);
            context.getExceptionHandler().handle();
        }
        finally {
            ApplicationAssociate.setCurrentInstance(null);
        }
    }

    /**
     * The request is about to come into scope of the web application.
     *
     * @param event the notification event
     */
    public void requestInitialized(ServletRequestEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext instanceof InitFacesContext) {
            InitFacesContext initFacesContext = (InitFacesContext) facesContext;
            initFacesContext.releaseCurrentInstance();
        }
        ApplicationAssociate.setCurrentInstance(getAssociate());
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
        boolean doCreateToken = true;
        
        // Try to avoid creating the token unless we actually have protected views
        if (null != associate) {
            Application application = associate.getApplication();
            ViewHandler viewHandler = application.getViewHandler();
            doCreateToken = !viewHandler.getProtectedViewsUnmodifiable().isEmpty();
        }

        if (doCreateToken) {
            StateHelper.createAndStoreCryptographicallyStrongTokenInSession(event.getSession());
        }
    }



    /**
     * Notification that a session is about to be invalidated.
     *
     * @param event the notification event
     */
    public void sessionDestroyed(HttpSessionEvent event) {        
        if (activeSessions != null) {
            activeSessions.remove(event.getSession());
        }
                
        if (Util.isCDIAvailable(servletContext)) {
            FlowCDIContext.sessionDestroyed(event);
        }

        ViewScopeManager manager = (ViewScopeManager) servletContext.getAttribute(ViewScopeManager.VIEW_SCOPE_MANAGER);
        if (manager != null) {
            manager.sessionDestroyed(event);
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
        this.applicationAssociate = null;

    }


    public List<HttpSession> getActiveSessions() {
        return new ArrayList<HttpSession>(activeSessions);
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
