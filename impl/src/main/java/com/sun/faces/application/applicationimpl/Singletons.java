/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2017 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.faces.application.applicationimpl;

import static com.sun.faces.util.MessageUtils.ILLEGAL_ATTEMPT_SETTING_APPLICATION_ARTIFACT_ID;
import static com.sun.faces.util.MessageUtils.getExceptionMessageString;
import static com.sun.faces.util.Util.coalesce;
import static com.sun.faces.util.Util.notNull;
import static java.util.logging.Level.FINE;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javax.faces.application.Application;
import javax.faces.application.NavigationHandler;
import javax.faces.application.ResourceHandler;
import javax.faces.application.StateManager;
import javax.faces.application.ViewHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionListener;
import javax.faces.flow.FlowHandler;

import com.sun.faces.application.ApplicationAssociate;
import com.sun.faces.util.FacesLogger;

public class Singletons {
    
    private static final Logger LOGGER = FacesLogger.APPLICATION.getLogger();
    private static final String CONTEXT = "context";
    
    private final ApplicationAssociate associate;
    
    private volatile ActionListener actionListener;
    private volatile NavigationHandler navigationHandler;
    private volatile ViewHandler viewHandler;
    private volatile ResourceHandler resourceHandler;
    private volatile StateManager stateManager;
    
    private volatile ArrayList<Locale> supportedLocales;
    private volatile Locale defaultLocale;
    private volatile String messageBundle;
    
    private String defaultRenderKitId;
    
    public Singletons(ApplicationAssociate applicationAssociate) {
        this.associate = applicationAssociate;
    }
    
    /**
     * @see javax.faces.application.Application#getViewHandler()
     */
    public ViewHandler getViewHandler() {
        return viewHandler;
    }

    /**
     * @see javax.faces.application.Application#setViewHandler(javax.faces.application.ViewHandler)
     */
    public synchronized void setViewHandler(ViewHandler viewHandler) {

        notNull("viewHandler", viewHandler);
        notRequestServiced("ViewHandler");

        this.viewHandler = viewHandler;

        if (LOGGER.isLoggable(FINE)) {
            LOGGER.log(FINE, MessageFormat.format("set ViewHandler Instance to ''{0}''", viewHandler.getClass().getName()));
        }
    }
    
    /**
     * @see javax.faces.application.Application#getResourceHandler()
     */
    public ResourceHandler getResourceHandler() {
        return resourceHandler;
    }

    /**
     * @see javax.faces.application.Application#setResourceHandler(javax.faces.application.ResourceHandler)
     */
    public synchronized void setResourceHandler(ResourceHandler resourceHandler) {

        notNull("resourceHandler", resourceHandler);
        notRequestServiced("ResourceHandler");

        this.resourceHandler = resourceHandler;
        
        if (LOGGER.isLoggable(FINE)) {
            LOGGER.log(FINE, "set ResourceHandler Instance to ''{0}''", resourceHandler.getClass().getName());
        }
    }
    
    /**
     * @see javax.faces.application.Application#getStateManager()
     */
    public StateManager getStateManager() {
        return stateManager;
    }

    /**
     * @see javax.faces.application.Application#setStateManager(javax.faces.application.StateManager)
     */
    public synchronized void setStateManager(StateManager stateManager) {

        notNull("stateManager", stateManager);
        notRequestServiced("StateManager");

        this.stateManager = stateManager;

        if (LOGGER.isLoggable(FINE)) {
            LOGGER.log(FINE, MessageFormat.format("set StateManager Instance to ''{0}''", stateManager.getClass().getName()));
        }
    }
    
    /**
     * @see javax.faces.application.Application#getActionListener()
     */
    public ActionListener getActionListener() {
        return actionListener;
    }
    
    /**
     * @see Application#setActionListener(javax.faces.event.ActionListener)
     */
    public synchronized void setActionListener(ActionListener actionListener) {

        notNull("actionListener", actionListener);

        this.actionListener = actionListener;

        if (LOGGER.isLoggable(FINE)) {
            LOGGER.fine(MessageFormat.format("set ActionListener Instance to ''{0}''", actionListener.getClass().getName()));
        }
    }
    
    /**
     * @see javax.faces.application.Application#getNavigationHandler()
     */
    public NavigationHandler getNavigationHandler() {
        return navigationHandler;
    }

    /**
     * @see javax.faces.application.Application#setNavigationHandler(javax.faces.application.NavigationHandler)
     */
    public synchronized void setNavigationHandler(NavigationHandler navigationHandler) {

        notNull("navigationHandler", navigationHandler);

        this.navigationHandler = navigationHandler;

        if (LOGGER.isLoggable(FINE)) {
            LOGGER.fine(MessageFormat.format("set NavigationHandler Instance to ''{0}''", navigationHandler.getClass().getName()));
        }
    }
    
    public FlowHandler getFlowHandler() {
        return associate.getFlowHandler();
    }

    public synchronized void setFlowHandler(FlowHandler flowHandler) {

        notNull("flowHandler", flowHandler);

        associate.setFlowHandler(flowHandler);

        if (LOGGER.isLoggable(FINE)) {
            LOGGER.fine(MessageFormat.format("set FlowHandler Instance to ''{0}''", flowHandler.getClass().getName()));
        }
    }
    
    /**
     * @see javax.faces.application.Application#getSupportedLocales()
     */
    public Iterator<Locale> getSupportedLocales() {
        return coalesce(supportedLocales, Collections.<Locale>emptyList()).iterator();
    }

    /**
     * @see javax.faces.application.Application#setSupportedLocales(java.util.Collection)
     */
    public synchronized void setSupportedLocales(Collection<Locale> newLocales) {

        notNull("newLocales", newLocales);

        supportedLocales = new ArrayList<>(newLocales);

        if (LOGGER.isLoggable(FINE)) {
            LOGGER.log(FINE, MessageFormat.format("set Supported Locales ''{0}''", supportedLocales.toString()));
        }

    }

    /**
     * @see javax.faces.application.Application#getDefaultLocale()
     */
    public Locale getDefaultLocale() {
        return defaultLocale;
    }

    /**
     * @see javax.faces.application.Application#setDefaultLocale(java.util.Locale)
     */
    public synchronized void setDefaultLocale(Locale locale) {

        notNull("locale", locale);

        defaultLocale = locale;

        if (LOGGER.isLoggable(FINE)) {
            LOGGER.log(FINE, MessageFormat.format("set defaultLocale ''{0}''", defaultLocale.getClass().getName()));
        }
    }
    
    /**
     * @see javax.faces.application.Application#setMessageBundle(String)
     */
    public synchronized void setMessageBundle(String messageBundle) {
        notNull("messageBundle", messageBundle);

        this.messageBundle = messageBundle;

        if (LOGGER.isLoggable(FINE)) {
            LOGGER.log(FINE, MessageFormat.format("set messageBundle ''{0}''", messageBundle));
        }
    }

    /**
     * @see javax.faces.application.Application#getMessageBundle()
     */
    public String getMessageBundle() {
        return messageBundle;
    }

    /**
     * @see javax.faces.application.Application#getDefaultRenderKitId()
     */
    public String getDefaultRenderKitId() {
        return defaultRenderKitId;
    }

    /**
     * @see javax.faces.application.Application#setDefaultRenderKitId(String)
     */
    public void setDefaultRenderKitId(String renderKitId) {
        defaultRenderKitId = renderKitId;
    }
    
    /**
     * @see javax.faces.application.Application#getResourceBundle(javax.faces.context.FacesContext,
     *      String)
     */
    public ResourceBundle getResourceBundle(FacesContext context, String var) {

        notNull(CONTEXT, context);
        notNull("var", var);

        return associate.getResourceBundle(context, var);
    }
    
    
    
    
    private void notRequestServiced(String artifactId) {
        if (associate.hasRequestBeenServiced()) {
            throw new IllegalStateException(getExceptionMessageString(ILLEGAL_ATTEMPT_SETTING_APPLICATION_ARTIFACT_ID, artifactId));
        }
    }

}
