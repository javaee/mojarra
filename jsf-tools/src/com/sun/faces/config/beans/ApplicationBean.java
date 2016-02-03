/*
 * $Id: ApplicationBean.java,v 1.3.28.2 2007/04/27 21:28:32 ofung Exp $
 */

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
