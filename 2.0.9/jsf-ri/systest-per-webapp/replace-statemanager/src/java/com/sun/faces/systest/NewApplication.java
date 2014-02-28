/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2010 Sun Microsystems, Inc. All rights reserved.
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


package com.sun.faces.systest;


import javax.el.ELContextListener;
import javax.el.ELException;
import javax.el.ELResolver;
import javax.el.ExpressionFactory;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.ApplicationWrapper;
import javax.faces.application.NavigationHandler;
import javax.faces.application.ProjectStage;
import javax.faces.application.ResourceHandler;
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
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import javax.faces.validator.Validator;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;

public class NewApplication extends ApplicationWrapper {


    private Application oldApp = null;

    public Application getWrapped() {
        return oldApp;
    }


    public NewApplication(Application oldApp) {

        this.oldApp = oldApp;

    }


    public ActionListener getActionListener() {

        return oldApp.getActionListener();

    }

    @Override
    public ProjectStage getProjectStage() {
        return oldApp.getProjectStage();
    }

    @Override
    public void publishEvent(FacesContext ctx,
                             Class<? extends SystemEvent> systemEventClass,
                             Object source) {
        oldApp.publishEvent(ctx, systemEventClass, source);
    }

    @Override
    public void subscribeToEvent(Class<? extends SystemEvent> systemEventClass,
                                 Class<?> sourceClass,
                                 SystemEventListener listener) {
        oldApp.subscribeToEvent(systemEventClass, sourceClass, listener);
    }

    @Override
    public void subscribeToEvent(Class<? extends SystemEvent> systemEventClass,
                                 SystemEventListener listener) {
        oldApp.subscribeToEvent(systemEventClass, listener);
    }

    @Override
    public void unsubscribeFromEvent(Class<? extends SystemEvent> systemEventClass,
                                     Class<?> sourceClass,
                                     SystemEventListener listener) {
        oldApp.unsubscribeFromEvent(systemEventClass,
                                    sourceClass,
                                    listener);
    }

    @Override
    public void unsubscribeFromEvent(Class<? extends SystemEvent> systemEventClass,
                                     SystemEventListener listener) {
        oldApp.unsubscribeFromEvent(systemEventClass, listener);
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


    public void setResourceHandler(ResourceHandler rh) {
        oldApp.setResourceHandler(rh);
    }


    public ResourceHandler getResourceHandler() {
        return oldApp.getResourceHandler();
    }


    public PropertyResolver getPropertyResolver() {

        return oldApp.getPropertyResolver();

    }


    public void setPropertyResolver(PropertyResolver resolver) {

        oldApp.setPropertyResolver(resolver);

    }

    public ELResolver getELResolver() {

        return oldApp.getELResolver();

    }

    public ExpressionFactory getExpressionFactory() {
        return oldApp.getExpressionFactory();
    }

    public void addELContextListener(ELContextListener listener) {
        oldApp.addELContextListener(listener);
    }

    public void removeELContextListener(ELContextListener listener) {
        oldApp.removeELContextListener(listener);
    }

    public void addELResolver(ELResolver resolver) {
        oldApp.addELResolver(resolver);
    }

    public ELContextListener[] getELContextListeners() {
        return oldApp.getELContextListeners();
    }

    public Object evaluateExpressionGet(FacesContext context,
                                        String expression,
                                        Class expectedType) throws ELException {
        return oldApp.evaluateExpressionGet(context, expression, expectedType);
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


}

