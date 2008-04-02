/*
 * $Id: ApplicationBean.java,v 1.2 2004/01/27 20:13:33 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config.beans;


import java.util.ArrayList;
import java.util.List;


/**
 * <p>Configuration bean for <code>&lt;application&gt; element.</p>
 */

// IMPLEMENTATION NOTE:  It is necessary to collect the class names of the
// sub-elements representing pluggable components, so we can chain them together
// if the implementation classes have appropriate constructors.

public class ApplicationBean {


    // -------------------------------------------------------------- Properties


    private LocaleConfigBean localeConfig;
    public LocaleConfigBean getLocaleConfig() { return localeConfig; }
    public void setLocaleConfig(LocaleConfigBean localeConfig)
    { this.localeConfig = localeConfig; }


    private String messageBundle;
    public String getMessageBundle() { return messageBundle; }
    public void setMessageBundle(String messageBundle)
    { this.messageBundle = messageBundle; }

    private String defaultRenderKitId;
    public String getDefaultRenderKitId() { return defaultRenderKitId; }
    public void setDefaultRenderKitId(String defaultRenderKitId)
    { this.defaultRenderKitId = defaultRenderKitId; }

    // -------------------------------------------- ActionListenerHolder Methods


    private List actionListeners = new ArrayList();


    public void addActionListener(String actionListener) {
        if (!actionListeners.contains(actionListener)) {
            actionListeners.add(actionListener);
        }
    }


    public String[] getActionListeners() {
        String results[] = new String[actionListeners.size()];
        return ((String[]) actionListeners.toArray(results));
    }


    public void removeActionListener(String actionListener) {
        actionListeners.remove(actionListener);
    }


    // ----------------------------------------- NavigationHandlerHolder Methods


    private List navigationHandlers = new ArrayList();


    public void addNavigationHandler(String navigationHandler) {
        if (!navigationHandlers.contains(navigationHandler)) {
            navigationHandlers.add(navigationHandler);
        }
    }


    public String[] getNavigationHandlers() {
        String results[] = new String[navigationHandlers.size()];
        return ((String[]) navigationHandlers.toArray(results));
    }


    public void removeNavigationHandler(String navigationHandler) {
        navigationHandlers.remove(navigationHandler);
    }


    // ------------------------------------------ PropertyResolverHolder Methods


    private List propertyResolvers = new ArrayList();


    public void addPropertyResolver(String propertyResolver) {
        if (!propertyResolvers.contains(propertyResolver)) {
            propertyResolvers.add(propertyResolver);
        }
    }


    public String[] getPropertyResolvers() {
        String results[] = new String[propertyResolvers.size()];
        return ((String[]) propertyResolvers.toArray(results));
    }


    public void removePropertyResolver(String propertyResolver) {
        propertyResolvers.remove(propertyResolver);
    }


    // ---------------------------------------------- StateManagerHolder Methods


    private List stateManagers = new ArrayList();


    public void addStateManager(String stateManager) {
        if (!stateManagers.contains(stateManager)) {
            stateManagers.add(stateManager);
        }
    }


    public String[] getStateManagers() {
        String results[] = new String[stateManagers.size()];
        return ((String[]) stateManagers.toArray(results));
    }


    public void removeStateManager(String stateManager) {
        stateManagers.remove(stateManager);
    }


    // ------------------------------------------ VariableResolverHolder Methods


    private List variableResolvers = new ArrayList();


    public void addVariableResolver(String variableResolver) {
        if (!variableResolvers.contains(variableResolver)) {
            variableResolvers.add(variableResolver);
        }
    }


    public String[] getVariableResolvers() {
        String results[] = new String[variableResolvers.size()];
        return ((String[]) variableResolvers.toArray(results));
    }


    public void removeVariableResolver(String variableResolver) {
        variableResolvers.remove(variableResolver);
    }


    // ------------------------------------------ ViewHandlerHolder Methods


    private List viewHandlers = new ArrayList();


    public void addViewHandler(String viewHandler) {
        if (!viewHandlers.contains(viewHandler)) {
            viewHandlers.add(viewHandler);
        }
    }


    public String[] getViewHandlers() {
        String results[] = new String[viewHandlers.size()];
        return ((String[]) viewHandlers.toArray(results));
    }


    public void removeViewHandler(String viewHandler) {
        viewHandlers.remove(viewHandler);
    }


    // ----------------------------------------------------------------- Methods


}
