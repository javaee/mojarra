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
package com.oracle.faces.cdi;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import javax.el.ELContextListener;
import javax.el.ExpressionFactory;
import javax.enterprise.inject.spi.BeanManager;
import javax.faces.FacesException;
import javax.faces.FacesWrapper;
import javax.faces.application.Application;
import javax.faces.application.NavigationHandler;
import javax.faces.application.StateManager;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.el.MethodBinding;
import javax.faces.el.PropertyResolver;
import javax.faces.el.ReferenceSyntaxException;
import javax.faces.el.ValueBinding;
import javax.faces.el.VariableResolver;
import javax.faces.event.ActionListener;
import javax.faces.validator.Validator;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class CdiApplication extends Application implements FacesWrapper<Application> {

    private Application application;
    private ExpressionFactory expressionFactory;

    public CdiApplication(Application application) {
        this.application = application;
        try {
            /*
             * As specified in the Weld integration guide this ELContext 
             * listener needs to be registered. So we are making sure the 
             * application gets it registered.
             */
            Class clazz = getClass().getClassLoader().loadClass("org.jboss.weld.el.WeldELContextListener");
            ELContextListener contextListener = (ELContextListener) clazz.newInstance();
            this.application.addELContextListener(contextListener);
        } catch (ClassNotFoundException cnfe) {
        } catch (InstantiationException ie) {
        } catch (IllegalAccessException iae) {
        }
    }

    @Override
    public ActionListener getActionListener() {
        return getWrapped().getActionListener();
    }

    @Override
    public void setActionListener(ActionListener listener) {
        getWrapped().setActionListener(listener);
    }

    @Override
    public Locale getDefaultLocale() {
        return getWrapped().getDefaultLocale();
    }

    @Override
    public void setDefaultLocale(Locale locale) {
        getWrapped().setDefaultLocale(locale);
    }

    @Override
    public String getDefaultRenderKitId() {
        return getWrapped().getDefaultRenderKitId();
    }

    @Override
    public void setDefaultRenderKitId(String renderKitId) {
        getWrapped().setDefaultRenderKitId(renderKitId);
    }

    @Override
    public String getMessageBundle() {
        return getWrapped().getMessageBundle();
    }

    @Override
    public void setMessageBundle(String bundle) {
        getWrapped().setMessageBundle(bundle);
    }

    @Override
    public NavigationHandler getNavigationHandler() {
        return getWrapped().getNavigationHandler();
    }

    @Override
    public void setNavigationHandler(NavigationHandler handler) {
        getWrapped().setNavigationHandler(handler);
    }

    @Override
    public PropertyResolver getPropertyResolver() {
        return getWrapped().getPropertyResolver();
    }

    @Override
    public void setPropertyResolver(PropertyResolver resolver) {
        getWrapped().setPropertyResolver(resolver);
    }

    @Override
    public VariableResolver getVariableResolver() {
        return getWrapped().getVariableResolver();
    }

    @Override
    public void setVariableResolver(VariableResolver resolver) {
        getWrapped().setVariableResolver(resolver);
    }

    @Override
    public ViewHandler getViewHandler() {
        return getWrapped().getViewHandler();
    }

    @Override
    public void setViewHandler(ViewHandler handler) {
        getWrapped().setViewHandler(handler);
    }

    @Override
    public StateManager getStateManager() {
        return getWrapped().getStateManager();
    }

    @Override
    public void setStateManager(StateManager manager) {
        getWrapped().setStateManager(manager);
    }

    @Override
    public void addComponent(String componentType, String componentClass) {
        getWrapped().addComponent(componentType, componentClass);
    }

    @Override
    public UIComponent createComponent(String componentType) throws FacesException {
        return getWrapped().createComponent(componentType);
    }

    @Override
    public UIComponent createComponent(ValueBinding componentBinding, FacesContext context, String componentType) throws FacesException {
        return getWrapped().createComponent(componentBinding, context, componentType);
    }

    @Override
    public Iterator<String> getComponentTypes() {
        return getWrapped().getComponentTypes();
    }

    @Override
    public void addConverter(String converterId, String converterClass) {
        getWrapped().addConverter(converterId, converterClass);
    }

    @Override
    public void addConverter(Class<?> targetClass, String converterClass) {
        getWrapped().addConverter(targetClass, converterClass);
    }

    @Override
    public Converter createConverter(String converterId) {
        return getWrapped().createConverter(converterId);
    }

    @Override
    public Converter createConverter(Class<?> targetClass) {
        return getWrapped().createConverter(targetClass);
    }

    @Override
    public Iterator<String> getConverterIds() {
        return getWrapped().getConverterIds();
    }

    @Override
    public Iterator<Class<?>> getConverterTypes() {
        return getWrapped().getConverterTypes();
    }

    @Override
    public MethodBinding createMethodBinding(String ref, Class<?>[] params) throws ReferenceSyntaxException {
        return getWrapped().createMethodBinding(ref, params);
    }

    @Override
    public Iterator<Locale> getSupportedLocales() {
        return getWrapped().getSupportedLocales();
    }

    @Override
    public void setSupportedLocales(Collection<Locale> locales) {
        getWrapped().setSupportedLocales(locales);
    }

    @Override
    public void addValidator(String validatorId, String validatorClass) {
        getWrapped().addValidator(validatorId, validatorClass);
    }

    @Override
    public Validator createValidator(String validatorId) throws FacesException {
        return getWrapped().createValidator(validatorId);
    }

    @Override
    public Iterator<String> getValidatorIds() {
        return getWrapped().getValidatorIds();
    }

    @Override
    public ValueBinding createValueBinding(String ref) throws ReferenceSyntaxException {
        return getWrapped().createValueBinding(ref);
    }

    public Application getWrapped() {
        return this.application;
    }

    /**
     * Get the expression factory.
     * 
     * @return the expression factory.
     */
    @Override
    public ExpressionFactory getExpressionFactory() {
        if (expressionFactory == null) {
            BeanManager beanManager = getBeanManager();
            expressionFactory = beanManager.wrapExpressionFactory(application.getExpressionFactory());
        }
        return expressionFactory;
    }

    /**
     * Get the bean manager.
     * 
     * @return the bean manager.
     */
    private BeanManager getBeanManager() {
        BeanManager result = null;

        try {
            InitialContext initialContext = new InitialContext();
            result = (BeanManager) initialContext.lookup("java:comp/BeanManager");
        } catch (NamingException ne) {
        }
        if (result == null) {
            try {
                InitialContext initialContext = new InitialContext();
                result = (BeanManager) initialContext.lookup("java:comp/env/BeanManager");
            } catch (NamingException ne) {
            }
        }
        return result;
    }
}
