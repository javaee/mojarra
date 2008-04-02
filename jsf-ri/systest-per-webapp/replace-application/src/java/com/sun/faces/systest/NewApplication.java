/*
 * $Id: NewApplication.java,v 1.6 2006/03/29 23:04:31 rlubke Exp $
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

import java.util.Iterator;
import java.util.Collection;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.NavigationHandler;
import javax.faces.application.ViewHandler;
import javax.faces.application.StateManager;
import javax.faces.component.ActionSource;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.el.MethodBinding;
import javax.faces.el.PropertyResolver;
import javax.faces.el.ReferenceSyntaxException;
import javax.faces.el.ValueBinding;
import javax.faces.el.VariableResolver;
import javax.faces.event.ActionListener;
import javax.faces.render.RenderKit;
import javax.faces.validator.Validator;

import javax.el.ELContextListener;
import javax.el.ELException;
import javax.el.ELResolver;


public class NewApplication extends Application {
    
    private Application oldApp = null;

    public NewApplication(Application oldApp) {
	this.oldApp = oldApp;
    }

    public ActionListener getActionListener() {
	return oldApp.getActionListener();
    }


    public void setActionListener(ActionListener listener) {
	oldApp.setActionListener(listener);
    }


    public Locale getDefaultLocale() {
	return oldApp.getDefaultLocale();
    }


    public void setDefaultLocale(Locale locale) {
	oldApp.setDefaultLocale(locale);
    }


    public String getDefaultRenderKitId() {
	return oldApp.getDefaultRenderKitId();
    }


    public void setDefaultRenderKitId(String renderKitId) {
	oldApp.setDefaultRenderKitId(renderKitId);
    }
	


    public String getMessageBundle() {
	return oldApp.getMessageBundle();
    }


    public void setMessageBundle(String bundle) {
	oldApp.setMessageBundle(bundle);
    }


    public NavigationHandler getNavigationHandler() {
	return oldApp.getNavigationHandler();
    }


    public void setNavigationHandler(NavigationHandler handler) {
	oldApp.setNavigationHandler(handler);
    }


    public PropertyResolver getPropertyResolver() {
	return oldApp.getPropertyResolver();
    }


    public void setPropertyResolver(PropertyResolver resolver) {
	oldApp.setPropertyResolver(resolver);
    }


    public VariableResolver getVariableResolver() {
	return oldApp.getVariableResolver();
    }


    public void setVariableResolver(VariableResolver resolver) {
	oldApp.setVariableResolver(resolver);
    }


    public ViewHandler getViewHandler() {
	return oldApp.getViewHandler();
    }


    public void setViewHandler(ViewHandler handler) {
	oldApp.setViewHandler(handler);
    }



    public StateManager getStateManager() {
	return oldApp.getStateManager();
    }


    public void setStateManager(StateManager manager) {
	oldApp.setStateManager(manager);
    }

    public ResourceBundle getResourceBundle(FacesContext ctx, String name) {
        return oldApp.getResourceBundle(ctx, name);
    }


    // ------------------------------------------------------- Object Factories


    public void addComponent(String componentType,
			     String componentClass) {
	oldApp.addComponent(componentType, componentClass);
    }


    public UIComponent createComponent(String componentType)
        throws FacesException {
	return oldApp.createComponent(componentType);
    }


    public UIComponent createComponent(ValueBinding componentBinding,
                                                FacesContext context,
                                                String componentType)
	throws FacesException {
	return oldApp.createComponent(componentBinding, context, 
				      componentType);
    }


    public Iterator getComponentTypes() {
	return oldApp.getComponentTypes();
    }


    public void addConverter(String converterId, 
			     String converterClass) {
	oldApp.addConverter(converterId, converterClass);
    }


    public void addConverter(Class targetClass,
			     String converterClass) {
	oldApp.addConverter(targetClass, converterClass);
    }


    public Converter createConverter(String converterId) {
	return oldApp.createConverter(converterId);
    }


    public Converter createConverter(Class targetClass) {
	return oldApp.createConverter(targetClass);
    }


    public Iterator getConverterIds() {
	return oldApp.getConverterIds();
    }

    
    public Iterator getConverterTypes() {
	return oldApp.getConverterTypes();
    }


    public MethodBinding createMethodBinding(String ref,
                                                      Class params[])
        throws ReferenceSyntaxException {
	return oldApp.createMethodBinding(ref, params);
    }


    public Iterator getSupportedLocales() {
	return oldApp.getSupportedLocales();
    }


    public void setSupportedLocales(Collection locales) {
	oldApp.setSupportedLocales(locales);
    }


    public void addValidator(String validatorId, 
			     String validatorClass) {
	oldApp.addValidator(validatorId, validatorClass);
    }


    public Validator createValidator(String validatorId)
        throws FacesException {
	return oldApp.createValidator(validatorId);
    }


    public Iterator getValidatorIds() {
	return oldApp.getValidatorIds();
    }


    public ValueBinding createValueBinding(String ref)
        throws ReferenceSyntaxException {
	return oldApp.createValueBinding(ref);
    }
    
    public ELContextListener [] getELContextListeners() {
        return oldApp.getELContextListeners();
    }
    
    public void addELContextListener(ELContextListener listener) {
        oldApp.addELContextListener(listener);
    } 
    
    public void removeELContextListener(ELContextListener listener) {
        oldApp.removeELContextListener(listener);
    }
    
     public Object evaluateExpressionGet(FacesContext context, 
        String expression, Class expectedType) throws ELException {
        return oldApp.evaluateExpressionGet(context, expression, expectedType);
    }
    
     public javax.el.ExpressionFactory getExpressionFactory() {
        return oldApp.getExpressionFactory();
     }
     
    public UIComponent createComponent(javax.el.ValueExpression componentExpression,
        FacesContext context, String componentType) throws FacesException {
        return oldApp.createComponent(componentExpression, context, componentType);
    } 
    
    public void addELResolver(ELResolver resolver) {
       oldApp.addELResolver(resolver);
    }
    
    public ELResolver getELResolver() {
        return oldApp.getELResolver();
    }

}
