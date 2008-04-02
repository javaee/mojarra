/*
 * $Id: NewApplication.java,v 1.5 2006/03/29 22:39:21 rlubke Exp $
 */

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
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */


package com.sun.faces.systest;

import javax.el.ELContextListener;
import javax.el.ELException;
import javax.el.ELResolver;
import javax.faces.FacesException;
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

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;


public class NewApplication extends Application {


    private Application oldApp = null;

    // ------------------------------------------------------------ Constructors


    public NewApplication(Application oldApp) {

        this.oldApp = oldApp;

    }

    // ---------------------------------------------------------- Public Methods


    public void addComponent(String componentType,
                             String componentClass) {

        oldApp.addComponent(componentType, componentClass);

    }


    public void addConverter(Class targetClass,
                             String converterClass) {

        oldApp.addConverter(targetClass, converterClass);

    }


    public void addConverter(String converterId,
                             String converterClass) {

        oldApp.addConverter(converterId, converterClass);

    }


    public void addELContextListener(ELContextListener listener) {

        oldApp.addELContextListener(listener);

    }


    public void addELResolver(ELResolver resolver) {

        oldApp.addELResolver(resolver);

    }


    public void addValidator(String validatorId,
                             String validatorClass) {

        oldApp.addValidator(validatorId, validatorClass);

    }


    public UIComponent createComponent(String componentType)
          throws FacesException {

        return oldApp.createComponent(componentType);

    }


    public UIComponent createComponent(
          javax.el.ValueExpression componentExpression,
          FacesContext context, String componentType) throws FacesException {

        return oldApp
              .createComponent(componentExpression, context, componentType);

    }


    public UIComponent createComponent(ValueBinding componentBinding,
                                       FacesContext context,
                                       String componentType)
          throws FacesException {

        return oldApp.createComponent(componentBinding, context,
                                      componentType);

    }


    public Converter createConverter(Class targetClass) {

        return oldApp.createConverter(targetClass);

    }


    public Converter createConverter(String converterId) {

        return oldApp.createConverter(converterId);

    }


    public MethodBinding createMethodBinding(String ref,
                                             Class params[])
          throws ReferenceSyntaxException {

        return oldApp.createMethodBinding(ref, params);

    }


    public Validator createValidator(String validatorId)
          throws FacesException {

        return oldApp.createValidator(validatorId);

    }


    public ValueBinding createValueBinding(String ref)
          throws ReferenceSyntaxException {

        return oldApp.createValueBinding(ref);

    }


    public Object evaluateExpressionGet(FacesContext context,
                                        String expression, Class expectedType)
          throws ELException {

        return oldApp.evaluateExpressionGet(context, expression, expectedType);

    }


    public ActionListener getActionListener() {

        return oldApp.getActionListener();

    }


    public Iterator getComponentTypes() {

        return oldApp.getComponentTypes();

    }


    public Iterator getConverterIds() {

        return oldApp.getConverterIds();

    }


    public Iterator getConverterTypes() {

        return oldApp.getConverterTypes();

    }


    public Locale getDefaultLocale() {

        return oldApp.getDefaultLocale();

    }


    public String getDefaultRenderKitId() {

        return oldApp.getDefaultRenderKitId();

    }


    public ELContextListener [] getELContextListeners() {

        return oldApp.getELContextListeners();

    }


    public ELResolver getELResolver() {

        return oldApp.getELResolver();

    }


    public javax.el.ExpressionFactory getExpressionFactory() {

        return oldApp.getExpressionFactory();

    }


    public String getMessageBundle() {

        return oldApp.getMessageBundle();

    }


    public NavigationHandler getNavigationHandler() {

        return oldApp.getNavigationHandler();

    }


    public PropertyResolver getPropertyResolver() {

        return oldApp.getPropertyResolver();

    }


    public ResourceBundle getResourceBundle(FacesContext ctx, String name) {

        return oldApp.getResourceBundle(ctx, name);

    }


    public StateManager getStateManager() {

        return oldApp.getStateManager();

    }


    public Iterator getSupportedLocales() {

        return oldApp.getSupportedLocales();

    }


    public Iterator getValidatorIds() {

        return oldApp.getValidatorIds();

    }


    public VariableResolver getVariableResolver() {

        return oldApp.getVariableResolver();

    }


    public ViewHandler getViewHandler() {

        return oldApp.getViewHandler();

    }


    public void removeELContextListener(ELContextListener listener) {

        oldApp.removeELContextListener(listener);

    }


    public void setActionListener(ActionListener listener) {

        oldApp.setActionListener(listener);

    }


    public void setDefaultLocale(Locale locale) {

        oldApp.setDefaultLocale(locale);

    }


    public void setDefaultRenderKitId(String renderKitId) {

        oldApp.setDefaultRenderKitId(renderKitId);

    }


    public void setMessageBundle(String bundle) {

        oldApp.setMessageBundle(bundle);

    }


    public void setNavigationHandler(NavigationHandler handler) {

        oldApp.setNavigationHandler(handler);

    }


    public void setPropertyResolver(PropertyResolver resolver) {

        oldApp.setPropertyResolver(resolver);

    }


    public void setStateManager(StateManager manager) {

        oldApp.setStateManager(manager);

    }


    public void setSupportedLocales(Collection locales) {

        oldApp.setSupportedLocales(locales);

    }


    public void setVariableResolver(VariableResolver resolver) {

        oldApp.setVariableResolver(resolver);

    }


    public void setViewHandler(ViewHandler handler) {

        oldApp.setViewHandler(handler);

    }

}
