/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt.
 * See the License for the specific language governing
 * permission and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * [Name of File] [ver.__] [Date]
 *
 * Copyright 2006 Sun Microsystems Inc. All Rights Reserved
 */

package com.sun.faces.application;

import com.sun.faces.io.FastStringWriter;
import com.sun.faces.spi.ManagedBeanFactory.Scope;
import com.sun.faces.util.Util;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestAttributeEvent;
import javax.servlet.ServletRequestAttributeListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>Central location for web application lifecycle events.<p>  
 * <p>The main purpose of this class is detect when we
 * should be invoking methods marked with the
 * <code>@PreDestroy</code> annotation.</p>
 */
public class WebappLifecycleListener implements ServletRequestListener,
      HttpSessionListener,
      ServletRequestAttributeListener,
      HttpSessionAttributeListener,
      ServletContextAttributeListener,
      ServletContextListener {

    // Log instance for this class
    private static final Logger LOGGER =
          Util.getLogger(Util.FACES_LOGGER + Util.APPLICATION_LOGGER);
    
    private ServletContext servletContext;
    private ApplicationAssociate applicationAssociate;

    /** The request is about to go out of scope of the web application. */
    public void requestDestroyed(ServletRequestEvent event) {       
        ServletRequest request = event.getServletRequest();
        for (Enumeration e = request.getAttributeNames(); e.hasMoreElements(); ) {
            String beanName = (String)e.nextElement();
            handleAttributeEvent(beanName, 
                                 request.getAttribute(beanName), 
                                 Scope.REQUEST);
        }
    }

    /** The request is about to come into scope of the web application. */
    public void requestInitialized(ServletRequestEvent sre) {
        // not interested in this event
    }

    /**
     * Notification that a session was created.
     *
     * @param se the notification event
     */
    public void sessionCreated(HttpSessionEvent se) {
        // not interested in this event
    }

    /**
     * Notification that a session is about to be invalidated.
     *
     * @param event the notification event
     */
    public void sessionDestroyed(HttpSessionEvent event) {        
        HttpSession session = event.getSession();
        for (Enumeration e = session.getAttributeNames(); e.hasMoreElements(); ) {
            String beanName = (String)e.nextElement();
            handleAttributeEvent(beanName, 
                                 session.getAttribute(beanName), 
                                 Scope.SESSION);
        }
    }

    /**
     * Notification that a new attribute was added to the
     * * servlet request. Called after the attribute is added.
     */
    public void attributeAdded(ServletRequestAttributeEvent srae) {
        // not interested in this event
    }

    /**
     * Notification that an existing attribute has been removed from the
     * * servlet request. Called after the attribute is removed.
     */
    public void attributeRemoved(ServletRequestAttributeEvent event) {
        handleAttributeEvent(event.getName(),
                             event.getValue(),
                             Scope.REQUEST);
    }

    /**
     * Notification that an attribute was replaced on the
     * * servlet request. Called after the attribute is replaced.
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
                                 Scope.REQUEST);
        }
    }

    /** Notification that an attribute has been added to a session. 
     * Called after the attribute is added. 
     */
    public void attributeAdded(HttpSessionBindingEvent se) {
        // not interested in this event
    }

    /** Notification that an attribute has been removed from a session. 
     * Called after the attribute is removed.
     */
    public void attributeRemoved(HttpSessionBindingEvent event) {
        handleAttributeEvent(event.getName(),
                             event.getValue(),
                             Scope.SESSION);
    }

    /** Notification that an attribute has been replaced in a session. 
     * Called after the attribute is replaced. 
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
                                 Scope.SESSION);
        }

    }

    /** Notification that a new attribute was added to the servlet context. 
     * Called after the attribute is added. 
     */
    public void attributeAdded(ServletContextAttributeEvent scab) {

    }

    /** Notification that an existing attribute has been removed from the servlet context. 
     * Called after the attribute is removed. 
     */
    public void attributeRemoved(ServletContextAttributeEvent event) {
        handleAttributeEvent(event.getName(),
                             event.getValue(),
                             Scope.APPLICATION);
    }

    /** Notification that an attribute on the servlet context has been replaced. 
     * Called after the attribute is replaced. 
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
                                 Scope.APPLICATION);
        }
    }

    private void handleAttributeEvent(String beanName,
                                      Object bean,
                                      Scope scope) {

        ApplicationAssociate associate = getAssociate();
        try {
            if (associate != null) {
                associate.handlePreDestroy(beanName, bean, scope);
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
     */
    public void contextInitialized(ServletContextEvent sce) {
        this.servletContext = sce.getServletContext();
    }

    /**
     * Notification that the servlet context is about to be shut down.
     * All servlets and filters have been destroy()ed before any
     * ServletContextListeners are notified of context
     * destruction.
     */
    public void contextDestroyed(ServletContextEvent event) {
        
        for (Enumeration e = servletContext.getAttributeNames(); e.hasMoreElements(); ) {
            String beanName = (String)e.nextElement();
            handleAttributeEvent(beanName, 
                                 servletContext.getAttribute(beanName), 
                                 Scope.APPLICATION);
        }
        
    }


    // --------------------------------------------------------- Private Methods


    private ApplicationAssociate getAssociate() {

        if (applicationAssociate == null) {
            applicationAssociate = ApplicationAssociate.getInstance(servletContext);
        }

        return applicationAssociate;
    }

} // END WebappLifecycleListener