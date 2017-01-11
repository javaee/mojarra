/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2016 Oracle and/or its affiliates. All rights reserved.
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

package javax.faces.application;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.el.ELContextListener;
import javax.el.ELException;
import javax.el.ELResolver;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.FacesWrapper;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.Behavior;
import javax.faces.component.search.SearchExpressionHandler;
import javax.faces.component.search.SearchKeywordResolver;
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
import javax.faces.flow.FlowHandler;
import javax.faces.validator.Validator;


/**
 * <p class="changed_added_2_0"><span class="changed_modified_2_2">Provides</span> a simple implementation of
 * {@link Application} that can be subclassed by developers wishing
 * to provide specialized behavior to an existing {@link
 * Application} instance.  The default implementation of all methods
 * is to call through to the wrapped {@link Application}.</p>
 *
 * <p class="changed_modified_2_3">Usage: extend this class and push the implementation being wrapped to the
 * constructor and use {@link #getWrapped} to access the instance being wrapped.</p>
 *
 * @since 2.0
 */
public abstract class ApplicationWrapper extends Application implements FacesWrapper<Application> {

    private Application wrapped;
    
    /**
     * @deprecated Use the other constructor taking the implementation being wrapped.
     */
    @Deprecated
    public ApplicationWrapper() {

    }

    /**
     * <p class="changed_added_2_3">If this application has been decorated, 
     * the implementation doing the decorating should push the implementation being wrapped to this constructor.
     * The {@link #getWrapped()} will then return the implementation being wrapped.</p>
     * 
     * @param wrapped The implementation being wrapped.
     * @since 2.3
     */
    public ApplicationWrapper(Application wrapped) {
        this.wrapped = wrapped;
    }
    
    @Override
    public Application getWrapped() {
        return wrapped;
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#getActionListener} on the
     * wrapped {@link Application} object.</p>
     */
    @Override
    public ActionListener getActionListener() {
        return getWrapped().getActionListener();
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#setActionListener(javax.faces.event.ActionListener)}
     * on the wrapped {@link Application} object.</p>
     */
    @Override
    public void setActionListener(ActionListener listener) {
        getWrapped().setActionListener(listener);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#getDefaultLocale} on the
     * wrapped {@link Application} object.</p>
     */
    @Override
    public Locale getDefaultLocale() {
        return getWrapped().getDefaultLocale();
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#setDefaultLocale(java.util.Locale)} on the
     * wrapped {@link Application} object.</p>
     */
    @Override
    public void setDefaultLocale(Locale locale) {
        getWrapped().setDefaultLocale(locale);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#getDefaultRenderKitId} on the
     * wrapped {@link Application} object.</p>
     */
    @Override
    public String getDefaultRenderKitId() {
        return getWrapped().getDefaultRenderKitId();
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#addDefaultValidatorId(String)} on the
     * wrapped {@link Application} object.</p>
     */
    @Override
    public void addDefaultValidatorId(String validatorId) {
        getWrapped().addDefaultValidatorId(validatorId);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#getDefaultValidatorInfo} on the
     * wrapped {@link Application} object.</p>
     */
    @Override
    public Map<String, String> getDefaultValidatorInfo() {
        return getWrapped().getDefaultValidatorInfo();
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#setDefaultRenderKitId(String)} on the
     * wrapped {@link Application} object.</p>
     */
    @Override
    public void setDefaultRenderKitId(String renderKitId) {
        getWrapped().setDefaultRenderKitId(renderKitId);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#getMessageBundle} on the
     * wrapped {@link Application} object.</p>
     */
    @Override
    public String getMessageBundle() {
        return getWrapped().getMessageBundle();
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#setMessageBundle(String)} on the
     * wrapped {@link Application} object.</p>
     */
    @Override
    public void setMessageBundle(String bundle) {
        getWrapped().setMessageBundle(bundle);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#getNavigationHandler} on the
     * wrapped {@link Application} object.</p>
     */
    @Override
    public NavigationHandler getNavigationHandler() {
        return getWrapped().getNavigationHandler();
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#setNavigationHandler(NavigationHandler)} on the
     * wrapped {@link Application} object.</p>
     */
    @Override
    public void setNavigationHandler(NavigationHandler handler) {
        getWrapped().setNavigationHandler(handler);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#getPropertyResolver} on the
     * wrapped {@link Application} object.</p>
     * 
     * @deprecated See superclass for alternative.
     */
    @Override
    @Deprecated
    public PropertyResolver getPropertyResolver() {
        return getWrapped().getPropertyResolver();
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#setPropertyResolver(javax.faces.el.PropertyResolver)} on the
     * wrapped {@link Application} object.</p>
     * 
     * @deprecated See superclass for alternative.
     */
    @Override
    @Deprecated
    public void setPropertyResolver(PropertyResolver resolver) {
        getWrapped().setPropertyResolver(resolver);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#getVariableResolver} on the
     * wrapped {@link Application} object.</p>
     * 
     * @deprecated See superclass for alternative.
     */
    @Override
    @Deprecated
    public VariableResolver getVariableResolver() {
        return getWrapped().getVariableResolver();
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#setVariableResolver(javax.faces.el.VariableResolver)} on the
     * wrapped {@link Application} object.</p>
     * 
     * @deprecated See superclass for alternative.
     */
    @Override
    @Deprecated
    public void setVariableResolver(VariableResolver resolver) {
        getWrapped().setVariableResolver(resolver);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#getViewHandler} on the
     * wrapped {@link Application} object.</p>
     */
    @Override
    public ViewHandler getViewHandler() {
        return getWrapped().getViewHandler();
    }

    /**
     * <p class="changed_added_2_0"><span class="changed_modified_2_2">The</span> default behavior of this method
     * is to call {@link Application#setViewHandler(ViewHandler)} on the
     * wrapped {@link Application} object.</p>
     * 
     * @throws IllegalStateException <span class="changed_modified_2_2">if this method is called after
     * at least one request has been processed by the
     * <code>Lifecycle</code> instance for this application.
     * @throws NullPointerException if <code>manager</code>
     *  is <code>null</code></span>
 */
    @Override
    public void setViewHandler(ViewHandler handler) {
        getWrapped().setViewHandler(handler);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#getStateManager} on the
     * wrapped {@link Application} object.</p>
     */
    @Override
    public StateManager getStateManager() {
        return getWrapped().getStateManager();
    }

    /**
     * <p class="changed_added_2_0"><span class="changed_modified_2_2">The</span> default behavior of this method
     * is to call {@link Application#setStateManager(StateManager)} on the
     * wrapped {@link Application} object.</p>
     * 
     * @throws IllegalStateException <span class="changed_added_2_2">if this method is called after
     * at least one request has been processed by the
     * <code>Lifecycle</code> instance for this application.
     * @throws NullPointerException if <code>manager</code>
     *  is <code>null</code></span>
     */
    @Override
    public void setStateManager(StateManager manager) {
        getWrapped().setStateManager(manager);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#addComponent(String, String)} on the
     * wrapped {@link Application} object.</p>
     */
    @Override
    public void addComponent(String componentType, String componentClass) {
        getWrapped().addComponent(componentType, componentClass);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#createComponent(String)} on the
     * wrapped {@link Application} object.</p>
     */
    @Override
    public UIComponent createComponent(String componentType)
          throws FacesException {
        return getWrapped().createComponent(componentType);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#createComponent(javax.faces.el.ValueBinding, javax.faces.context.FacesContext, String)}
     * on the wrapped {@link Application} object.</p>
     * 
     * @deprecated See superclass for alternative.
     */
    @Override
    @Deprecated
    public UIComponent createComponent(ValueBinding componentBinding,
                                       FacesContext context,
                                       String componentType)
          throws FacesException {
        return getWrapped().createComponent(componentBinding,
                                            context,
                                            componentType);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#getComponentTypes} on the
     * wrapped {@link Application} object.</p>
     */
    @Override
    public Iterator<String> getComponentTypes() {
        return getWrapped().getComponentTypes();
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#addConverter(String, String)} on the
     * wrapped {@link Application} object.</p>
     */
    @Override
    public void addConverter(String converterId, String converterClass) {
        getWrapped().addConverter(converterId, converterClass);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#addConverter(Class, String)} on the
     * wrapped {@link Application} object.</p>
     */
    @Override
    public void addConverter(Class<?> targetClass, String converterClass) {
        getWrapped().addConverter(targetClass, converterClass);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#createConverter(String)} on the
     * wrapped {@link Application} object.</p>
     */
    @Override
    public Converter createConverter(String converterId) {
        return getWrapped().createConverter(converterId);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#createConverter(Class)} on the
     * wrapped {@link Application} object.</p>
     */
    @Override
    public Converter createConverter(Class<?> targetClass) {
        return getWrapped().createConverter(targetClass);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#getConverterIds} on the
     * wrapped {@link Application} object.</p>
     */
    @Override
    public Iterator<String> getConverterIds() {
        return getWrapped().getConverterIds();
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#getConverterTypes} on the
     * wrapped {@link Application} object.</p>
     */
    @Override
    public Iterator<Class<?>> getConverterTypes() {
        return getWrapped().getConverterTypes();
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#createMethodBinding(String, Class[])} on the
     * wrapped {@link Application} object.</p>
     * 
     * @deprecated See superclass for alternative.
     */
    @Override
    @Deprecated
    public MethodBinding createMethodBinding(String ref, Class<?>[] params)
          throws ReferenceSyntaxException {
        return getWrapped().createMethodBinding(ref, params);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#getSupportedLocales} on the
     * wrapped {@link Application} object.</p>
     */
    @Override
    public Iterator<Locale> getSupportedLocales() {
        return getWrapped().getSupportedLocales();
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#setSupportedLocales(java.util.Collection)} on the
     * wrapped {@link Application} object.</p>
     */
    @Override
    public void setSupportedLocales(Collection<Locale> locales) {
        getWrapped().setSupportedLocales(locales);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#addBehavior(String, String)} on the
     * wrapped {@link Application} object.</p>
     */
    @Override
    public void addBehavior(String behaviorId, String behaviorClass) {
        getWrapped().addBehavior(behaviorId, behaviorClass);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#createBehavior(String)} on the
     * wrapped {@link Application} object.</p>
     */
    @Override
    public Behavior createBehavior(String behaviorId) throws FacesException {
        return getWrapped().createBehavior(behaviorId);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#getBehaviorIds} on the
     * wrapped {@link Application} object.</p>
     */
    @Override
    public Iterator<String> getBehaviorIds() {
        return getWrapped().getBehaviorIds();
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#addValidator(String, String)} on the
     * wrapped {@link Application} object.</p>
     */
    @Override
    public void addValidator(String validatorId, String validatorClass) {
        getWrapped().addValidator(validatorId, validatorClass);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#createValidator(String)} on the
     * wrapped {@link Application} object.</p>
     */
    @Override
    public Validator createValidator(String validatorId) throws FacesException {
        return getWrapped().createValidator(validatorId);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#getValidatorIds} on the
     * wrapped {@link Application} object.</p>
     */
    @Override
    public Iterator<String> getValidatorIds() {
        return getWrapped().getValidatorIds();
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#createValueBinding(String)} on the
     * wrapped {@link Application} object.</p>
     */
    @Override
    public ValueBinding createValueBinding(String ref)
          throws ReferenceSyntaxException {
        return getWrapped().createValueBinding(ref);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#getResourceHandler} on the
     * wrapped {@link Application} object.</p>
     */
    @Override
    public ResourceHandler getResourceHandler() {
        return getWrapped().getResourceHandler();
    }

    /**
     * <p class="changed_added_2_0"><span class="changed_modified_2_2">The</span> default behavior of this method
     * is to call {@link Application#setResourceHandler(ResourceHandler)} on the
     * wrapped {@link Application} object.</p>
     * 
     * @throws IllegalStateException <span class="changed_added_2_2">if this method is called after
     * at least one request has been processed by the
     * <code>Lifecycle</code> instance for this application.
     * @throws NullPointerException if <code>resourceHandler</code>
     *  is <code>null</code></span>
     */
    @Override
    public void setResourceHandler(ResourceHandler resourceHandler) {
        getWrapped().setResourceHandler(resourceHandler);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#getResourceBundle(javax.faces.context.FacesContext, String)} on the
     * wrapped {@link Application} object.</p>
     */
    @Override
    public ResourceBundle getResourceBundle(FacesContext ctx, String name) {
        return getWrapped().getResourceBundle(ctx, name);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#getProjectStage} on the
     * wrapped {@link Application} object.</p>
     */
    @Override
    public ProjectStage getProjectStage() {
        return getWrapped().getProjectStage();
    }

    /**
     * <p class="changed_added_2_0"><span class="changed_modified_2_2">The</span> default behavior of this method
     * is to call {@link Application#addELResolver(javax.el.ELResolver)} on the
     * wrapped {@link Application} object.</p>
     * 
     * @throws IllegalStateException <span
     * class="changed_added_2_2">if called after the first
     * request to the {@link javax.faces.webapp.FacesServlet} has been
     * serviced.</span>
     */
    @Override
    public void addELResolver(ELResolver resolver) {
        getWrapped().addELResolver(resolver);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#getELResolver} on the
     * wrapped {@link Application} object.</p>
     */
    @Override
    public ELResolver getELResolver() {
        return getWrapped().getELResolver();
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#createComponent(javax.el.ValueExpression, javax.faces.context.FacesContext, String)}
     * on the wrapped {@link Application} object.</p>
     */
    @Override
    public UIComponent createComponent(ValueExpression componentExpression,
                                       FacesContext context,
                                       String componentType)
          throws FacesException {
        return getWrapped().createComponent(componentExpression, context, componentType);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#createComponent(javax.el.ValueExpression, javax.faces.context.FacesContext, String, String)}
     * on the wrapped {@link Application} object.</p>
     */
    @Override
    public UIComponent createComponent(ValueExpression componentExpression,
                                       FacesContext context,
                                       String componentType,
                                       String rendererType) {
        return getWrapped().createComponent(componentExpression, context, componentType, rendererType);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#createComponent(javax.faces.context.FacesContext, String, String)}
     * on the wrapped {@link Application} object.</p>
     */
    @Override
    public UIComponent createComponent(FacesContext context,
                                       String componentType,
                                       String rendererType) {
        return getWrapped().createComponent(context, componentType, rendererType);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#createComponent(javax.faces.context.FacesContext, Resource)}
     * on the wrapped {@link Application} object.</p>
     */
    @Override
    public UIComponent createComponent(FacesContext context,
                                       Resource componentResource) {
        return getWrapped().createComponent(context, componentResource);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#getExpressionFactory} on the
     * wrapped {@link Application} object.</p>
     */
    @Override
    public ExpressionFactory getExpressionFactory() {
        return getWrapped().getExpressionFactory();
    }

    @Override
    public FlowHandler getFlowHandler() {
        return getWrapped().getFlowHandler();
    }

    @Override
    public void setFlowHandler(FlowHandler newHandler) {
        super.setFlowHandler(newHandler);
    }
    
    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#evaluateExpressionGet(javax.faces.context.FacesContext, String, Class)}
     * on the wrapped {@link Application} object.</p>
     * 
     * @deprecated See superclass for alternative.
     */
    @Override
    @Deprecated
    public <T> T evaluateExpressionGet(FacesContext context,
                                       String expression,
                                       Class<? extends T> expectedType)
          throws ELException {
        return getWrapped().evaluateExpressionGet(context, expression, expectedType);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#addELContextListener(javax.el.ELContextListener)} on the
     * wrapped {@link Application} object.</p>
     */
    @Override
    public void addELContextListener(ELContextListener listener) {
        getWrapped().addELContextListener(listener);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#removeELContextListener(javax.el.ELContextListener)} on the
     * wrapped {@link Application} object.</p>
     */
    @Override
    public void removeELContextListener(ELContextListener listener) {
        getWrapped().removeELContextListener(listener);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#getELContextListeners} on the
     * wrapped {@link Application} object.</p>
     */
    @Override
    public ELContextListener[] getELContextListeners() {
        return getWrapped().getELContextListeners();
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#publishEvent(javax.faces.context.FacesContext, Class, Object)}
     * on the wrapped {@link Application} object.</p>
     */
    @Override
    public void publishEvent(FacesContext context,
                             Class<? extends SystemEvent> systemEventClass,
                             Object source) {
        getWrapped().publishEvent(context, systemEventClass, source);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#publishEvent(javax.faces.context.FacesContext, Class, Class, Object)}
     * on the wrapped {@link Application} object.</p>
     */
    @Override
    public void publishEvent(FacesContext context,
                             Class<? extends SystemEvent> systemEventClass,
                             Class<?> sourceBaseType,
                             Object source) {
        getWrapped().publishEvent(context,
                                  systemEventClass,
                                  sourceBaseType,
                                  source);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#subscribeToEvent(Class, Class, javax.faces.event.SystemEventListener)} on the
     * wrapped {@link Application} object.</p>
     */
    @Override
    public void subscribeToEvent(Class<? extends SystemEvent> systemEventClass,
                                 Class<?> sourceClass,
                                 SystemEventListener listener) {
        getWrapped().subscribeToEvent(systemEventClass, sourceClass, listener);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#subscribeToEvent(Class, javax.faces.event.SystemEventListener)} on the
     * wrapped {@link Application} object.</p>
     */
    @Override
    public void subscribeToEvent(Class<? extends SystemEvent> systemEventClass,
                                 SystemEventListener listener) {
        getWrapped().subscribeToEvent(systemEventClass, listener);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#unsubscribeFromEvent(Class, Class, javax.faces.event.SystemEventListener)} on the
     * wrapped {@link Application} object.</p>
     */
    @Override
    public void unsubscribeFromEvent(Class<? extends SystemEvent> systemEventClass,
                                     Class<?> sourceClass,
                                     SystemEventListener listener) {
        getWrapped().unsubscribeFromEvent(systemEventClass, sourceClass, listener);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#unsubscribeFromEvent(Class, javax.faces.event.SystemEventListener)} on the
     * wrapped {@link Application} object.</p>
     */
    @Override
    public void unsubscribeFromEvent(Class<? extends SystemEvent> systemEventClass,
                                     SystemEventListener listener) {
        getWrapped().unsubscribeFromEvent(systemEventClass, listener);
    }

    @Override
    public SearchExpressionHandler getSearchExpressionHandler() {
        return getWrapped().getSearchExpressionHandler();
    }
    
    @Override
    public void setSearchExpressionHandler(SearchExpressionHandler searchExpressionHandler) {
        getWrapped().setSearchExpressionHandler(searchExpressionHandler);
    }
 
    @Override
    public void addSearchKeywordResolver(SearchKeywordResolver resolver) {
        getWrapped().addSearchKeywordResolver(resolver);
    }
    
    @Override
    public SearchKeywordResolver getSearchKeywordResolver() {
        return getWrapped().getSearchKeywordResolver();
    }
}
