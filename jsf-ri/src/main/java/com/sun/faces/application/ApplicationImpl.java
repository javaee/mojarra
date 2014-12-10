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


import java.beans.*;
import java.lang.reflect.Constructor;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.el.CompositeELResolver;
import javax.el.ELContextListener;
import javax.el.ELException;
import javax.el.ELResolver;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.NavigationHandler;
import javax.faces.application.ResourceHandler;
import javax.faces.application.StateManager;
import javax.faces.application.ViewHandler;
import javax.faces.application.ProjectStage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.behavior.Behavior;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.DateTimeConverter;
import javax.faces.el.MethodBinding;
import javax.faces.el.PropertyResolver;
import javax.faces.el.ReferenceSyntaxException;
import javax.faces.el.ValueBinding;
import javax.faces.el.VariableResolver;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionListener;
import javax.faces.flow.FlowHandler;
import javax.faces.validator.Validator;

import com.sun.faces.RIConstants;
import com.sun.faces.config.WebConfiguration;
import com.sun.faces.config.WebConfiguration.WebContextInitParameter;
import static com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter.DateTimeConverterUsesSystemTimezone;
import static com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter.RegisterConverterPropertyEditors;
import com.sun.faces.el.ELUtils;
import com.sun.faces.el.FacesCompositeELResolver;
import com.sun.faces.el.PropertyResolverImpl;
import com.sun.faces.el.VariableResolverImpl;
import com.sun.faces.util.Cache;
import com.sun.faces.util.Cache.Factory;
import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.MessageUtils;
import com.sun.faces.util.ReflectionUtils;
import com.sun.faces.util.Util;
import java.util.HashSet;

import java.util.LinkedHashSet;

import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.PostAddToViewEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import javax.faces.event.SystemEventListenerHolder;
import javax.faces.event.ExceptionQueuedEventContext;

import java.util.List;
import java.util.TimeZone;
import java.util.LinkedHashMap;

import javax.el.ValueExpression;
import javax.faces.application.Resource;
import javax.faces.render.RenderKit;
import javax.faces.render.Renderer;
import javax.faces.view.ViewDeclarationLanguage;


/**
 * <p><strong>Application</strong> represents a per-web-application
 * singleton object where applications based on JavaServer Faces (or
 * implementations wishing to provide extended functionality) can
 * register application-wide singletons that provide functionality
 * required by JavaServer Faces.
 */
public class ApplicationImpl extends Application {

    // Log instance for this class
    private static final Logger LOGGER = FacesLogger.APPLICATION.getLogger();

    private static final ELContextListener[] EMPTY_EL_CTX_LIST_ARRAY = { };

    private static final Map<String,Class<?>[]> STANDARD_CONV_ID_TO_TYPE_MAP =
         new HashMap<String,Class<?>[]>(8, 1.0f);
    private static final Map<Class<?>,String> STANDARD_TYPE_TO_CONV_ID_MAP =
         new HashMap<Class<?>,String>(16, 1.0f);

    static {
        STANDARD_CONV_ID_TO_TYPE_MAP.put("javax.faces.Byte", new Class[] { Byte.TYPE, Byte.class});
        STANDARD_CONV_ID_TO_TYPE_MAP.put("javax.faces.Boolean", new Class[] { Boolean.TYPE, Boolean.class});
        STANDARD_CONV_ID_TO_TYPE_MAP.put("javax.faces.Character", new Class[] { Character.TYPE, Character.class});
        STANDARD_CONV_ID_TO_TYPE_MAP.put("javax.faces.Short", new Class[] { Short.TYPE, Short.class });
        STANDARD_CONV_ID_TO_TYPE_MAP.put("javax.faces.Integer", new Class[] { Integer.TYPE, Integer.class });
        STANDARD_CONV_ID_TO_TYPE_MAP.put("javax.faces.Long", new Class[] { Long.TYPE, Long.class });
        STANDARD_CONV_ID_TO_TYPE_MAP.put("javax.faces.Float", new Class[] { Float.TYPE, Float.class });
        STANDARD_CONV_ID_TO_TYPE_MAP.put("javax.faces.Double", new Class[] { Double.TYPE, Double.class });
        for (Map.Entry<String,Class<?>[]> entry : STANDARD_CONV_ID_TO_TYPE_MAP.entrySet()) {
            Class<?>[] types = entry.getValue();
            String key = entry.getKey();
            for (Class<?> clazz : types) {
                STANDARD_TYPE_TO_CONV_ID_MAP.put(clazz, key);
            }
        }
    }

    // Relationship Instance Variables

    private ApplicationAssociate associate = null;
    private ProjectStage projectStage;

    private volatile ActionListener actionListener = null;
    private volatile NavigationHandler navigationHandler = null;
    private volatile PropertyResolverImpl propertyResolver = null;
    volatile VariableResolverImpl variableResolver = null;
    private volatile ViewHandler viewHandler = null;
    private volatile ResourceHandler resourceHandler;
    private volatile StateManager stateManager = null;
    private volatile ArrayList<Locale> supportedLocales = null;
    private volatile Locale defaultLocale = null;
    //
    // This map stores reference expression | value binding instance
    // mappings.
    //
    
    //
    // These four maps store store "identifier" | "class name"
    // mappings.
    //
    private ViewMemberInstanceFactoryMetadataMap<String,Object> behaviorMap = null;
    private ViewMemberInstanceFactoryMetadataMap<String,Object> componentMap = null;
    private ViewMemberInstanceFactoryMetadataMap<String,Object> converterIdMap = null;
    private ViewMemberInstanceFactoryMetadataMap<String,Object> validatorMap = null;

    private Map<Class<?>,Object> converterTypeMap = null;
    private Set<String> defaultValidatorIds = null;
    private volatile Map<String,String> defaultValidatorInfo = null;
    private volatile String messageBundle = null;

    private List<ELContextListener> elContextListeners = null;
    CompositeELResolver elResolvers = null;
    FacesCompositeELResolver compositeELResolver = null;
    private final SystemEventHelper systemEventHelper = new SystemEventHelper();
    private final ComponentSystemEventHelper compSysEventHelper = new ComponentSystemEventHelper();
    private boolean passDefaultTimeZone;
    private boolean registerPropertyEditors;
    private TimeZone systemTimeZone;

    /**
     * Constructor
     */
    public ApplicationImpl() {
        super();
        associate = new ApplicationAssociate(this);
        componentMap = new ViewMemberInstanceFactoryMetadataMap<String, Object>(new ConcurrentHashMap<String, Object>());
        converterIdMap = new ViewMemberInstanceFactoryMetadataMap<String, Object>(new ConcurrentHashMap<String, Object>());
        converterTypeMap = new ConcurrentHashMap<Class<?>, Object>();
        validatorMap = new ViewMemberInstanceFactoryMetadataMap<String, Object>(new ConcurrentHashMap<String, Object>());
        defaultValidatorIds = new LinkedHashSet<String>();
        behaviorMap = new ViewMemberInstanceFactoryMetadataMap<String, Object>(new ConcurrentHashMap<String, Object>());
        elContextListeners = new CopyOnWriteArrayList<ELContextListener>();
        propertyResolver = new PropertyResolverImpl();
        variableResolver = new VariableResolverImpl();
        elResolvers = new CompositeELResolver();

        FacesContext ctx = FacesContext.getCurrentInstance();
        WebConfiguration webConfig = WebConfiguration.getInstance(ctx.getExternalContext());
        passDefaultTimeZone = webConfig.isOptionEnabled(DateTimeConverterUsesSystemTimezone);
        registerPropertyEditors = webConfig.isOptionEnabled(RegisterConverterPropertyEditors);
        if (passDefaultTimeZone) {
            systemTimeZone = TimeZone.getDefault();
        }
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "Created Application instance ");
        }
    }


    /**
     * @see javax.faces.application.Application#publishEvent(FacesContext, Class, Object)
     */
    @Override
    public void publishEvent(FacesContext context,
                             Class<? extends SystemEvent> systemEventClass,
                             Object source) {

        publishEvent(context, systemEventClass, null, source);

    }


    /**
     * @see javax.faces.application.Application#publishEvent(FacesContext, Class, Object)
     */
    @Override
    public void publishEvent(FacesContext context,
                             Class<? extends SystemEvent> systemEventClass,
                             Class<?> sourceBaseType,
                             Object source) {

        Util.notNull("context", context);
        Util.notNull("systemEventClass", systemEventClass);
        Util.notNull("source", source);
        if (!needsProcessing(context, systemEventClass)) {
            return;
        }
        // source is not compatible with the provided base type.
        // Log a warning that the types are incompatible and return. 
        if (getProjectStage() == ProjectStage.Development
              && sourceBaseType != null
              && !sourceBaseType.isInstance(source)) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING,
                           "jsf.application.publish.event.base_type_mismatch",
                           new Object[] { source.getClass().getName(),
                                          sourceBaseType.getName() });                
            }
            return;
        }

        try {
            // The side-effect of calling invokeListenersFor
            // will create a SystemEvent object appropriate to event/source
            // combination.  This event will be passed on subsequent invocations
            // of invokeListenersFor
            SystemEvent event;

            // Look for and invoke any listeners stored on the source instance.
            event = invokeComponentListenersFor(systemEventClass, source);

            // Look for and invoke any 'view' listeners 
            event = invokeViewListenersFor(context, systemEventClass, event, source);

            // look for and invoke any listeners stored on the application
            // using source type.
            event = invokeListenersFor(systemEventClass,
                                       event,
                                       source,
                                       sourceBaseType,
                                       true);

            // look for and invoke any listeners not specific to the source class
            invokeListenersFor(systemEventClass, event, source, null, false);
        } catch (AbortProcessingException ape) {
            context.getApplication().publishEvent(context,
                                                  ExceptionQueuedEvent.class,
                                                  new ExceptionQueuedEventContext(context, ape));
        }

    }


    /**
     * @see Application#subscribeToEvent(Class, Class, javax.faces.event.SystemEventListener)
     */
    @Override
    public void subscribeToEvent(Class<? extends SystemEvent> systemEventClass,
                                 Class<?> sourceClass,
                                 SystemEventListener listener) {

        Util.notNull("systemEventClass", systemEventClass);
        Util.notNull("listener", listener);

        Set<SystemEventListener> listeners =
              getListeners(systemEventClass, sourceClass);
        listeners.add(listener);

    }


    /**
     * @see Application#subscribeToEvent(Class, javax.faces.event.SystemEventListener)
     */
    @Override
    public void subscribeToEvent(Class<? extends SystemEvent> systemEventClass,
                                 SystemEventListener listener) {

        subscribeToEvent(systemEventClass, null, listener);

    }


    /**
     * @see Application#unsubscribeFromEvent(Class, Class, javax.faces.event.SystemEventListener)
     */
    @Override
    public void unsubscribeFromEvent(Class<? extends SystemEvent> systemEventClass,
                                     Class<?> sourceClass,
                                     SystemEventListener listener) {

        Util.notNull("systemEventClass", systemEventClass);
        Util.notNull("listener", listener);

        Set<SystemEventListener> listeners =
              getListeners(systemEventClass, sourceClass);
        if (listeners != null) {
            listeners.remove(listener);
        }

    }

    /**
     * @see Application#unsubscribeFromEvent(Class, javax.faces.event.SystemEventListener)
     */
    @Override
    public void unsubscribeFromEvent(Class<? extends SystemEvent> systemEventClass,
                                     SystemEventListener listener) {

        unsubscribeFromEvent(systemEventClass, null, listener);

    }


    /**
     * @see javax.faces.application.Application#addELContextListener(javax.el.ELContextListener)
     */
    @Override
    public void addELContextListener(ELContextListener listener) {
        if (listener != null) {
            elContextListeners.add(listener);
        }
    }


    /**
     * @see javax.faces.application.Application#removeELContextListener(javax.el.ELContextListener)
     */
    @Override
    public void removeELContextListener(ELContextListener listener) {
        if (listener != null) {
            elContextListeners.remove(listener);
        }
    }


    /**
     * @see javax.faces.application.Application#getELContextListeners()
     */
    @Override
    public ELContextListener [] getELContextListeners() {
        if (!elContextListeners.isEmpty()) {
            return (elContextListeners.toArray(
                       new ELContextListener[elContextListeners.size()]));
        } else {
            return (EMPTY_EL_CTX_LIST_ARRAY);
        }
    }


    /**
     * @see javax.faces.application.Application#getExpressionFactory()
     */
    @Override
    public ExpressionFactory getExpressionFactory() {
        return associate.getExpressionFactory();
    }

    @Override
    public FlowHandler getFlowHandler() {
        return associate.getFlowHandler();
    }
    
    @Override
    public synchronized void setFlowHandler(FlowHandler flowHandler) {

        Util.notNull("flowHandler", flowHandler);

        associate.setFlowHandler(flowHandler);

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine(MessageFormat.format("set FlowHandler Instance to ''{0}''",
                                             flowHandler.getClass().getName()));
        }
    }

    


    /**
     * @see javax.faces.application.Application#evaluateExpressionGet(javax.faces.context.FacesContext, String, Class)
     */
    @Override
    public <T> T evaluateExpressionGet(FacesContext context,
                                       String expression,
                                       Class<? extends T> expectedType) throws ELException {
        ValueExpression ve = 
          getExpressionFactory().createValueExpression(context.getELContext(), 
                                                       expression,
                                                       expectedType);
        //noinspection unchecked
        return (T)(ve.getValue(context.getELContext()));
    }


    @Override
    public UIComponent createComponent(ValueExpression componentExpression,
                                       FacesContext context,
                                       String componentType)
    throws FacesException {

        Util.notNull("componentExpression", componentExpression);
        Util.notNull("context", context);
        Util.notNull("componentType", componentType);

        return createComponentApplyAnnotations(context,
                                               componentExpression,
                                               componentType,
                                               null,
                                               true);

    }


    /**
     * @see javax.faces.application.Application#getELResolver()
     */
    @Override
    public ELResolver getELResolver() {

        if (compositeELResolver == null) {
            performOneTimeELInitialization();
        }

        return compositeELResolver;

    }


    /**
     * @see javax.faces.application.Application#addELResolver(javax.el.ELResolver)
     */
    @Override
    public void addELResolver(ELResolver resolver) {

        if (associate.hasRequestBeenServiced()) {
            throw new IllegalStateException(
                  MessageUtils.getExceptionMessageString(
                        MessageUtils.ILLEGAL_ATTEMPT_SETTING_APPLICATION_ARTIFACT_ID, "ELResolver"));
        }

        elResolvers.add(resolver);

    }


    /**
     * @see javax.faces.application.Application#getProjectStage() 
     */
    @Override
    public ProjectStage getProjectStage() {
        
        if (projectStage == null) {
            WebConfiguration webConfig =
                  WebConfiguration.getInstance(
                        FacesContext.getCurrentInstance().getExternalContext());
            String value = webConfig.getEnvironmentEntry(WebConfiguration.WebEnvironmentEntry.ProjectStage);
            if (value != null) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE,
                               "ProjectStage configured via JNDI: {0}",
                               value);
                }
            } else {
                value = webConfig.getOptionValue(WebContextInitParameter.JavaxFacesProjectStage);
                if (value != null && LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE,
                       "ProjectStage configured via servlet context init parameter: {0}", 
                       value);
                }
            }
            if (value != null) {
                try {
                    projectStage = ProjectStage.valueOf(value);
                } catch (IllegalArgumentException iae) {
                    if (LOGGER.isLoggable(Level.INFO)) {
                        LOGGER.log(Level.INFO,
                                   "Unable to discern ProjectStage for value {0}.",
                                   value);
                    }
                }
            }
            if (projectStage == null) {
                projectStage = ProjectStage.Production;
            }
            if (projectStage == ProjectStage.Development) {
                subscribeToEvent(PostAddToViewEvent.class, new ValidateComponentNesting());
            }
           
        }
        return projectStage;

    }

    public CompositeELResolver getApplicationELResolvers() {
        return elResolvers;
    }


    /**
     * @see javax.faces.application.Application#getActionListener()
     */
    public ActionListener getActionListener() {
        return actionListener;
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

        Util.notNull("viewHandler", viewHandler);

        if (associate.hasRequestBeenServiced()) {
            throw new IllegalStateException(
                  MessageUtils.getExceptionMessageString(
                        MessageUtils.ILLEGAL_ATTEMPT_SETTING_APPLICATION_ARTIFACT_ID, "ViewHandler"));
        }

        this.viewHandler = viewHandler;
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, MessageFormat.format("set ViewHandler Instance to ''{0}''", viewHandler.getClass().getName()));
        }

    }

    /**
     * @see javax.faces.application.Application#getResourceHandler()
     */
    @Override
    public ResourceHandler getResourceHandler() {

        return resourceHandler;

    }


    /**
     * @see javax.faces.application.Application#setResourceHandler(javax.faces.application.ResourceHandler)
     */
    @Override
    public synchronized void setResourceHandler(ResourceHandler resourceHandler) {

        Util.notNull("resourceHandler", resourceHandler);

        if (associate.hasRequestBeenServiced()) {
            throw new IllegalStateException(
                  MessageUtils.getExceptionMessageString(
                        MessageUtils.ILLEGAL_ATTEMPT_SETTING_APPLICATION_ARTIFACT_ID, "ResourceHandler"));
        }

        this.resourceHandler = resourceHandler;
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE,
                       "set ResourceHandler Instance to ''{0}''",
                       resourceHandler.getClass().getName());
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

        Util.notNull("stateManager", stateManager);

        if (associate.hasRequestBeenServiced()) {
            throw new IllegalStateException(
                  MessageUtils.getExceptionMessageString(
                        MessageUtils.ILLEGAL_ATTEMPT_SETTING_APPLICATION_ARTIFACT_ID, "StateManager"));
        }

        this.stateManager = stateManager;
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, MessageFormat.format("set StateManager Instance to ''{0}''",
                                                        stateManager.getClass().getName()));
        }

    }


    /**
     * @see Application#setActionListener(javax.faces.event.ActionListener)
     */
    public synchronized void setActionListener(ActionListener actionListener) {

        Util.notNull("actionListener", actionListener);

        this.actionListener = actionListener;

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine(MessageFormat.format("set ActionListener Instance to ''{0}''",
                                             actionListener.getClass().getName()));
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

        Util.notNull("navigationHandler", navigationHandler);

        this.navigationHandler = navigationHandler;

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine(MessageFormat.format("set NavigationHandler Instance to ''{0}''",
                                             navigationHandler.getClass().getName()));
        }
    }

    /**
     * @see javax.faces.application.Application#setPropertyResolver(javax.faces.el.PropertyResolver)
     */
    @SuppressWarnings("deprecation")
    public PropertyResolver getPropertyResolver() {
        if (compositeELResolver == null) {
            performOneTimeELInitialization();
        }
        return propertyResolver;
    }


    /**
     * @see javax.faces.application.Application#getResourceBundle(javax.faces.context.FacesContext, String)
     */
    @Override
    public ResourceBundle getResourceBundle(FacesContext context, String var) {

        Util.notNull("context", context);
        Util.notNull("var", var);
        return associate.getResourceBundle(context, var);

    }


    /**
     * @see javax.faces.application.Application#setPropertyResolver(javax.faces.el.PropertyResolver)
     */
    @SuppressWarnings("deprecation")
    public void setPropertyResolver(PropertyResolver resolver) {
        // Throw Illegal State Exception if  a PropertyResolver is set after 
        // a request has been processed.
        if (associate.hasRequestBeenServiced()) {
            throw new IllegalStateException(
                    MessageUtils.getExceptionMessageString(
                        MessageUtils.ILLEGAL_ATTEMPT_SETTING_APPLICATION_ARTIFACT_ID, "PropertyResolver"));
        }
        if (resolver == null) {
            String message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "resolver");
            throw new NullPointerException(message);
        }

        propertyResolver.setDelegate(ELUtils.getDelegatePR(associate, true));
        associate.setLegacyPropertyResolver(resolver);
        propertyResolver = new PropertyResolverImpl();


        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine(MessageFormat.format("set PropertyResolver Instance to ''{0}''", resolver.getClass().getName()));
        }
    }


    /**
     * @see javax.faces.application.Application#createMethodBinding(String, Class[])
     */
    @SuppressWarnings("deprecation")
    public MethodBinding createMethodBinding(String ref, Class<?> params[]) {

        Util.notNull("ref", ref);

        if (!(ref.startsWith("#{") && ref.endsWith("}"))) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine(MessageFormat.format("Expression ''{0}'' does not follow the syntax #{...}", ref));
            }
            throw new ReferenceSyntaxException(ref);
        }
        FacesContext context = FacesContext.getCurrentInstance();
        MethodExpression result;
        try {
            // return a MethodBinding that wraps a MethodExpression.
            if (null == params) {
                params = RIConstants.EMPTY_CLASS_ARGS;
            }
            result =
                  getExpressionFactory().
                        createMethodExpression(context.getELContext(), ref, null,
                                               params);
        } catch (ELException elex) {
            throw new ReferenceSyntaxException(elex);
        }
        return (new MethodBindingMethodExpressionAdapter(result));

    }


    /**
     * @see javax.faces.application.Application#createValueBinding(String)
     */
    @SuppressWarnings("deprecation")
    public ValueBinding createValueBinding(String ref)
    throws ReferenceSyntaxException {

        Util.notNull("ref", ref);
        ValueExpression result;
        FacesContext context = FacesContext.getCurrentInstance();
         // return a ValueBinding that wraps a ValueExpression.
         try {
             result= getExpressionFactory().
                     createValueExpression(context.getELContext(),ref,
                     Object.class);     
         } catch (ELException elex) {
            throw new ReferenceSyntaxException(elex);
         } 
         return (new ValueBindingValueExpressionAdapter(result));

    }


    /**
     * @see javax.faces.application.Application#getVariableResolver()
     */
    @SuppressWarnings("deprecation")
    public VariableResolver getVariableResolver() {       
        if (compositeELResolver == null) {
            performOneTimeELInitialization();
        }

        return variableResolver;
    }


    /**
     * @see javax.faces.application.Application#setVariableResolver(javax.faces.el.VariableResolver)
     */
    @SuppressWarnings("deprecation")
    public void setVariableResolver(VariableResolver resolver) {
        Util.notNull("variableResolver", resolver);

        if (associate.hasRequestBeenServiced()) {
            throw new IllegalStateException(
                    MessageUtils.getExceptionMessageString(
                        MessageUtils.ILLEGAL_ATTEMPT_SETTING_APPLICATION_ARTIFACT_ID, "VariableResolver"));
        }

        variableResolver.setDelegate(ELUtils.getDelegateVR(associate, true));
        associate.setLegacyVariableResolver(resolver);

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine(MessageFormat.format("set VariableResolver Instance to ''{0}''",
                                             variableResolver.getClass().getName()));
        }
    }

    /**
     * @see javax.faces.application.Application#addBehavior(String, String)
     */
    public void addBehavior(String behaviorId, String behaviorClass) {

        Util.notNull("behaviorId", behaviorId);
        Util.notNull("behaviorClass", behaviorClass);

        if (LOGGER.isLoggable(Level.FINE) && behaviorMap.containsKey(behaviorId)) {
            LOGGER.log(Level.FINE,
                       "behaviorId {0} has already been registered.  Replacing existing behavior class type {1} with {2}.",
                       new Object[] { behaviorId, behaviorMap.get(behaviorId), behaviorClass });
        }
        behaviorMap.put(behaviorId, behaviorClass);

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine(MessageFormat.format("added behavior of type ''{0}'' class ''{1}''",
                                             behaviorId,
                                             behaviorClass));
        }

    }

    /**
     * @see javax.faces.application.Application#createBehavior(String)
     */
    public Behavior createBehavior(String behaviorId) throws FacesException {

        Util.notNull("behaviorId", behaviorId);
        Behavior returnVal = (Behavior) newThing(behaviorId, behaviorMap);
        if (returnVal == null) {
            Object[] params = {behaviorId};
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE,
                        "jsf.cannot_instantiate_behavior_error", params);
            }
            throw new FacesException(MessageUtils.getExceptionMessageString(
                MessageUtils.NAMED_OBJECT_NOT_FOUND_ERROR_MESSAGE_ID, params));
        }
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine(MessageFormat.format("created behavior of type ''{0}''",
                                             behaviorId));
        }
        associate.getAnnotationManager().applyBehaviorAnnotations(FacesContext.getCurrentInstance(), returnVal);
        return returnVal;
    }

    /**
     * @see javax.faces.application.Application#getBehaviorIds()
     */
    public Iterator<String> getBehaviorIds() {

        return behaviorMap.keySet().iterator();

    }

    /**
     * @see javax.faces.application.Application#addComponent(java.lang.String, java.lang.String)
     */
    public void addComponent(String componentType, String componentClass) {

        Util.notNull("componentType", componentType);
        Util.notNull("componentType", componentClass);

        if (LOGGER.isLoggable(Level.FINE) && componentMap.containsKey(componentType)) {
            LOGGER.log(Level.FINE,
                       "componentType {0} has already been registered.  Replacing existing component class type {1} with {2}.",
                       new Object[] { componentType, componentMap.get(componentType), componentClass });
        }
        componentMap.put(componentType, componentClass);
        
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine(MessageFormat.format("added component of type ''{0}'' and class ''{1}''",
                                             componentType,
                                             componentClass));
        }
        
    }


    public UIComponent createComponent(String componentType) throws FacesException {

        Util.notNull("componentType", componentType);

        return createComponentApplyAnnotations(FacesContext.getCurrentInstance(),
                                               componentType,
                                               null,
                                               true);

    }


    @Override
    public UIComponent createComponent(FacesContext context, Resource componentResource) throws FacesException {

        // RELEASE_PENDING (rlubke,driscoll) this method needs review.

        Util.notNull("context", context);
        Util.notNull("componentResource", componentResource);

        UIComponent result = null;

        // use the application defined in the FacesContext as we may be calling
        // overriden methods
        Application app = context.getApplication();

        ViewDeclarationLanguage pdl = app.getViewHandler().getViewDeclarationLanguage(context, context.getViewRoot().getViewId());
        BeanInfo componentMetadata = pdl.getComponentMetadata(context,
                                                              componentResource);
        if (null != componentMetadata){
            BeanDescriptor componentBeanDescriptor = componentMetadata.getBeanDescriptor();
            
            // Step 1.  See if the composite component author explicitly
            // gave a componentType as part of the composite component metadata
            ValueExpression ve = (ValueExpression)
                  componentBeanDescriptor.getValue(UIComponent.COMPOSITE_COMPONENT_TYPE_KEY);
            if (null != ve) {
                String componentType = (String) ve.getValue(context.getELContext());
                if (null != componentType && 0 < componentType.length()) {
                    result = app.createComponent(componentType);
                }
            }
        }


        // Step 2. If that didn't work, if a script based resource can be 
        // found for the scriptComponentResource,
        // see if a component can be generated from it
        if (null == result) {
            Resource scriptComponentResource = pdl.getScriptComponentResource(context, componentResource);

            if (null != scriptComponentResource) {
                result = createComponentFromScriptResource(context,
                        scriptComponentResource, componentResource);
            }
        }

        // Step 3. Use the libraryName of the resource as the java package
        // and use the resourceName as the class name.  See
        // if a Java class can be loaded
        if (null == result) {
            String packageName = componentResource.getLibraryName();
            String className = componentResource.getResourceName();
            className = packageName + '.' + className.substring(0, className.lastIndexOf('.'));
            try {
                Class<?> clazz = (Class<?>) componentMap.get(className);
                if (clazz == null) {
                    clazz = Util.loadClass(className, this);
                }
                if (clazz != ComponentResourceClassNotFound.class) {
                    if (!associate.isDevModeEnabled()) {
                        componentMap.put(className, clazz);
                    }
                    result = (UIComponent) clazz.newInstance();
                }
            } catch (ClassNotFoundException ex) {
                if (!associate.isDevModeEnabled()) {
                    componentMap.put(className, ComponentResourceClassNotFound.class);
                }
            } catch (InstantiationException ie) {
                throw new FacesException(ie);
            } catch (IllegalAccessException iae) {
                throw new FacesException(iae);
            } catch (ClassCastException cce) {
                throw new FacesException(cce);
            } catch (Exception otherwise) {
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.log(Level.SEVERE,
                               otherwise.toString(),
                               otherwise);
                }
            }
        }

        // Step 4. Use javax.faces.NamingContainer as the component type
        if (null == result) {
            result = app.createComponent("javax.faces.NamingContainer");
        }

        assert (null != result);

        result.setRendererType("javax.faces.Composite");
        Map<String, Object> attrs = result.getAttributes();
        attrs.put(Resource.COMPONENT_RESOURCE_KEY,
                componentResource);
        attrs.put(UIComponent.BEANINFO_KEY,
                componentMetadata);

        associate.getAnnotationManager().applyComponentAnnotations(context, result);
        pushDeclaredDefaultValuesToAttributesMap(context, componentMetadata, attrs, result);


        return result;
    }


    /*
     * This method makes it so that any cc:attribute elements that have
     * a "default" attribute value have those values pushed into the
     * composite component attribute map so that programmatic access 
     * (as opposed to EL access) will find the attribute values.
     *
     */
    private void pushDeclaredDefaultValuesToAttributesMap(FacesContext context,
            BeanInfo componentMetadata, Map<String, Object> attrs, UIComponent component) {
        PropertyDescriptor[] declaredAttributes = componentMetadata.getPropertyDescriptors();
        Object defaultValue;
        String key;
        Collection<String> attributesWithDeclaredDefaultValues = null;

        PropertyDescriptor[] pd = null;

        for (PropertyDescriptor cur : declaredAttributes) {
            defaultValue = cur.getValue("default");
            if (null != defaultValue) {
                key = cur.getName();
                boolean isLiteralText = false;
                if (defaultValue instanceof ValueExpression) {
                    isLiteralText = ((ValueExpression)defaultValue).isLiteralText();
                    if (isLiteralText) {
                        defaultValue = ((ValueExpression)defaultValue).getValue(context.getELContext());
                    }
                }
                // ensure this attribute is not a method-signature.  method-signature
                // declared default values are handled in retargetMethodExpressions.
                if (null == cur.getValue("method-signature") || null != cur.getValue("type")) {

                    if (null == attributesWithDeclaredDefaultValues) {
                        BeanDescriptor desc = componentMetadata.getBeanDescriptor();
                        attributesWithDeclaredDefaultValues = (Collection<String>)
                                desc.getValue(UIComponent.ATTRS_WITH_DECLARED_DEFAULT_VALUES);
                        if (null == attributesWithDeclaredDefaultValues) {
                            attributesWithDeclaredDefaultValues = new HashSet<String>();
                            desc.setValue(UIComponent.ATTRS_WITH_DECLARED_DEFAULT_VALUES,
                                    attributesWithDeclaredDefaultValues);
                        }
                    }
                    attributesWithDeclaredDefaultValues.add(key);

                    // Only store the attribute if it is literal text.  If it
                    // is a ValueExpression, it will be handled explicitly in
                    // CompositeComponentAttributesELResolver.ExpressionEvalMap.get().
                    // If it is a MethodExpression, it will be dealt with in
                    // retargetMethodExpressions.
                    if (isLiteralText) {
                        try {
                            if (null == pd) {
                                pd = Introspector.getBeanInfo(component.getClass()).getPropertyDescriptors();
                            }
                        } catch (IntrospectionException e) {
                            throw new FacesException(e);
                        }
                        defaultValue = convertValueToTypeIfNecessary(key, defaultValue, pd);
                        attrs.put(key, defaultValue);
                    }
                }
            }
        }
    }
    

        

    @SuppressWarnings("deprecation")
    public UIComponent createComponent(ValueBinding componentBinding,
                                       FacesContext context,
                                       String componentType)
    throws FacesException {

        Util.notNull("componentBinding", componentBinding);
        Util.notNull("context", context);
        Util.notNull("componentType", componentType);

        Object result;
        boolean createOne = false;
        try {
            result = componentBinding.getValue(context);
            if (result != null) {
                createOne = (!(result instanceof UIComponent));
            }

            if (result == null || createOne) {
                result = this.createComponentApplyAnnotations(context,
                                                              componentType,
                                                              null,
                                                              false);
                componentBinding.setValue(context, result);
            }
        } catch (Exception ex) {
            throw new FacesException(ex);
        }

        return (UIComponent) result;

    }


    @Override
    public UIComponent createComponent(ValueExpression componentExpression,
                                       FacesContext context,
                                       String componentType,
                                       String rendererType) {

        Util.notNull("componentExpression", componentExpression);
        Util.notNull("context", context);
        Util.notNull("componentType", componentType);

        return createComponentApplyAnnotations(context,
                                               componentExpression,
                                               componentType,
                                               rendererType,
                                               true);

    }


    @Override
    public UIComponent createComponent(FacesContext context,
                                       String componentType,
                                       String rendererType) {

        Util.notNull("context", context);
        Util.notNull("componentType", componentType);

        return createComponentApplyAnnotations(context,
                                               componentType,
                                               rendererType,
                                               true);

    }

    /**
     * @see javax.faces.application.Application#getComponentTypes()
     */
    public Iterator<String> getComponentTypes() {

        return componentMap.keySet().iterator();

    }


    /**
     * @see javax.faces.application.Application#addConverter(String, String)
     */
    public void addConverter(String converterId, String converterClass) {

        Util.notNull("converterId", converterId);
        Util.notNull("converterClass", converterClass);

        if (LOGGER.isLoggable(Level.FINE) && converterIdMap.containsKey(converterId)) {
            LOGGER.log(Level.FINE,
                       "converterId {0} has already been registered.  Replacing existing converter class type {1} with {2}.",
                       new Object[] { converterId, converterIdMap.get(converterId), converterClass });
        }

        converterIdMap.put(converterId, converterClass);

        Class<?>[] types = STANDARD_CONV_ID_TO_TYPE_MAP.get(converterId);
        if (types != null) {
            for (Class<?> clazz : types) {
                // go directly against map to prevent cyclic method calls
                converterTypeMap.put(clazz, converterClass);
                addPropertyEditorIfNecessary(clazz);
            }
        }
        
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine(MessageFormat.format("added converter of type ''{0}'' and class ''{1}''",
                                             converterId,
                                             converterClass));
        }
    }


    /**
     * @see javax.faces.application.Application#addConverter(Class, String)
     */
    public void addConverter(Class<?> targetClass, String converterClass) {

        Util.notNull("targetClass", targetClass);
        Util.notNull("converterClass", converterClass);

        String converterId = STANDARD_TYPE_TO_CONV_ID_MAP.get(targetClass);
        if (converterId != null) {
            addConverter(converterId, converterClass);
        } else {
            if (LOGGER.isLoggable(Level.FINE) && converterTypeMap
                  .containsKey(targetClass)) {
                LOGGER.log(Level.FINE,
                           "converter target class {0} has already been registered.  Replacing existing converter class type {1} with {2}.",
                           new Object[]{
                                 targetClass.getName(),
                                 converterTypeMap.get(targetClass),
                                 converterClass});
            }
            converterTypeMap.put(targetClass, converterClass);
            addPropertyEditorIfNecessary(targetClass);
        }                
        
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine(MessageFormat.format("added converter of class type ''{0}''", converterClass));
        }
    }
    
    private final String [] STANDARD_BY_TYPE_CONVERTER_CLASSES = {
      "java.math.BigDecimal",
      "java.lang.Boolean",
      "java.lang.Byte",
      "java.lang.Character",
      "java.lang.Double",
      "java.lang.Float",
      "java.lang.Integer",
      "java.lang.Long",
      "java.lang.Short",
      "java.lang.Enum"
    };

    /**
     * Helper method to convert a value to a type as defined in PropertyDescriptor(s)
     * @param name
     * @param value
     * @param pd
     * @return value
     */
    private Object convertValueToTypeIfNecessary(String name, Object value, PropertyDescriptor[] pd) {
        for (PropertyDescriptor aPd : pd) {
            if (aPd.getName().equals(name)) {
                value = getExpressionFactory().coerceToType(value, aPd.getPropertyType());
                break;
            }
        }
        return value;
    }

    /**
     * <p>To enable EL Coercion to use JSF Custom converters, this 
     * method will call <code>PropertyEditorManager.registerEditor()</code>,
     * passing the <code>ConverterPropertyEditor</code> class for the
     * <code>targetClass</code> if the target class is not one of the standard
     * by-type converter target classes.
     * @param targetClass the target class for which a PropertyEditory may or
     *  may not be created
     */
    
    private void addPropertyEditorIfNecessary(Class<?> targetClass) {
        
        if (!registerPropertyEditors) {
            return;
        }

        PropertyEditor editor = PropertyEditorManager.findEditor(targetClass);
        if (null != editor) {
            return;
        }
        String className = targetClass.getName();
        // Don't add a PropertyEditor for the standard by-type converters.
        if (targetClass.isPrimitive()) {
            return;
        }
        for (String standardClass : STANDARD_BY_TYPE_CONVERTER_CLASSES) {
            if (-1 != standardClass.indexOf(className)) {
                return;
            }
        }
        Class<?> editorClass = ConverterPropertyEditorFactory.getDefaultInstance().definePropertyEditorClassFor(targetClass);
        if (editorClass != null) {
            PropertyEditorManager.registerEditor(targetClass, editorClass);
        } else {
        	if (LOGGER.isLoggable(Level.WARNING)) {
        		LOGGER.warning(MessageFormat.format("definePropertyEditorClassFor({0}) returned null.", targetClass.getName()));
        	}
        }
    }

    private void performOneTimeELInitialization() {
        if (null != compositeELResolver) {
            throw new IllegalStateException("Class invariant invalidated: " +
                    "The Application instance's ELResolver is not null " +
                    "and it should be.");
        }
        associate.initializeELResolverChains();
    }


    /**
     * @see javax.faces.application.Application#createConverter(String)
     */
    public Converter createConverter(String converterId) {

        Util.notNull("converterId", converterId);
        Converter returnVal = (Converter) newThing(converterId, converterIdMap);
        if (returnVal == null) {
            Object[] params = {converterId};
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE,
                        "jsf.cannot_instantiate_converter_error", converterId);
            }
            throw new FacesException(MessageUtils.getExceptionMessageString(
                MessageUtils.NAMED_OBJECT_NOT_FOUND_ERROR_MESSAGE_ID, params));
        }
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine(MessageFormat.format("created converter of type ''{0}''", converterId));
        }
        if (passDefaultTimeZone && returnVal instanceof DateTimeConverter) {
            ((DateTimeConverter) returnVal).setTimeZone(systemTimeZone);
        }
        associate.getAnnotationManager().applyConverterAnnotations(FacesContext.getCurrentInstance(), returnVal);
        return returnVal;
    }


    /**
     * @see javax.faces.application.Application#createConverter(Class)
     */
    public Converter createConverter(Class<?> targetClass) {

        Util.notNull("targetClass", targetClass);
        Converter returnVal = (Converter) newConverter(targetClass,
                                                   converterTypeMap,targetClass);
        if (returnVal != null) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine(MessageFormat.format("Created converter of type ''{0}''",
                                                 returnVal.getClass().getName()));
            }
            if (passDefaultTimeZone
                && returnVal instanceof DateTimeConverter) {
                ((DateTimeConverter) returnVal).setTimeZone(systemTimeZone);
            }
            associate.getAnnotationManager().applyConverterAnnotations(FacesContext.getCurrentInstance(), returnVal);
            return returnVal;
        } 

        //Search for converters registered to interfaces implemented by
        //targetClass
        Class<?>[] interfaces = targetClass.getInterfaces();
        if (interfaces != null) {
            for (int i = 0; i < interfaces.length; i++) {
                returnVal = createConverterBasedOnClass(interfaces[i], targetClass);
                if (returnVal != null) {
                   if (LOGGER.isLoggable(Level.FINE)) {
                       LOGGER.fine(MessageFormat.format("Created converter of type ''{0}''",
                                                        returnVal.getClass().getName()));
                    }
                    if (passDefaultTimeZone
                        && returnVal instanceof DateTimeConverter) {
                        ((DateTimeConverter) returnVal)
                              .setTimeZone(systemTimeZone);
                    }
                    associate.getAnnotationManager().applyConverterAnnotations(FacesContext.getCurrentInstance(), returnVal);
                    return returnVal;
                }
            }
        }

        //Search for converters registered to superclasses of targetClass
        Class<?> superclass = targetClass.getSuperclass();
        if (superclass != null) {
            returnVal = createConverterBasedOnClass(superclass, targetClass);
            if (returnVal != null) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine(MessageFormat.format("Created converter of type ''{0}''",
                                                     returnVal.getClass().getName()));
                }
                if (passDefaultTimeZone
                    && returnVal instanceof DateTimeConverter) {
                    ((DateTimeConverter) returnVal).setTimeZone(systemTimeZone);
                }
                associate.getAnnotationManager().applyConverterAnnotations(FacesContext.getCurrentInstance(), returnVal);
                return returnVal;
            }
        }

        return returnVal;
    }

    protected Converter createConverterBasedOnClass(Class<?> targetClass,
            Class<?> baseClass) {
        
        Converter returnVal = (Converter) newConverter(targetClass,
                converterTypeMap, baseClass);
        if (returnVal != null) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine(MessageFormat.format("Created converter of type ''{0}''",
                                                 returnVal.getClass().getName()));
            }
            return returnVal;
        } 

        //Search for converters registered to interfaces implemented by
        //targetClass
        Class<?>[] interfaces = targetClass.getInterfaces();
        if (interfaces != null) {
            for (int i = 0; i < interfaces.length; i++) {
                returnVal = createConverterBasedOnClass(interfaces[i], null);
                if (returnVal != null) {
                   if (LOGGER.isLoggable(Level.FINE)) {
                       LOGGER.fine(MessageFormat.format("Created converter of type ''{0}''",
                                                        returnVal.getClass().getName()));
                    }
                    return returnVal;
                }
            }
        }

        //Search for converters registered to superclasses of targetClass
        Class<?> superclass = targetClass.getSuperclass();
        if (superclass != null) {
            returnVal = createConverterBasedOnClass(superclass, targetClass);
            if (returnVal != null) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine(MessageFormat.format("Created converter of type ''{0}''",
                                                     returnVal.getClass().getName()));
                }
                return returnVal;
            }
        } 
        return returnVal;
    }


    /**
     * @see javax.faces.application.Application#getConverterIds()
     */
    public Iterator<String> getConverterIds() {
       
        return converterIdMap.keySet().iterator();
        
    }


    /**
     * @see javax.faces.application.Application#getConverterTypes()
     */
    public Iterator<Class<?>> getConverterTypes() {
                
        return converterTypeMap.keySet().iterator();
        
    }


    /**
     * @see javax.faces.application.Application#getSupportedLocales()
     */
    public Iterator<Locale> getSupportedLocales() {

            if (null != supportedLocales) {
                return supportedLocales.iterator();
            } else {
                return Collections.<Locale>emptyList().iterator();
            }

    }


    /**
     * @see javax.faces.application.Application#setSupportedLocales(java.util.Collection)
     */
    public synchronized void setSupportedLocales(Collection<Locale> newLocales) {

        Util.notNull("newLocales", newLocales);

        supportedLocales = new ArrayList<Locale>(newLocales);

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, MessageFormat.format("set Supported Locales ''{0}''",
                                                        supportedLocales.toString()));
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

        Util.notNull("locale", locale);

        defaultLocale = locale;

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, (MessageFormat.format("set defaultLocale ''{0}''",
                                                         defaultLocale.getClass().getName())));
        }
    }


    protected String defaultRenderKitId = null;


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
     * @see javax.faces.application.Application#addValidator(String, String)
     */
    public void addValidator(String validatorId, String validatorClass) {

        Util.notNull("validatorId", validatorId);
        Util.notNull("validatorClass", validatorClass);

        if (LOGGER.isLoggable(Level.FINE) && validatorMap.containsKey(validatorId)) {
            LOGGER.log(Level.FINE,
                       "validatorId {0} has already been registered.  Replacing existing validator class type {1} with {2}.",
                       new Object[] { validatorId, validatorMap.get(validatorId), validatorClass });    
        }

        validatorMap.put(validatorId, validatorClass);
        
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine(MessageFormat.format("added validator of type ''{0}'' class ''{1}''",
                                             validatorId,
                                             validatorClass));
        }

    }


    /**
     * @see javax.faces.application.Application#createValidator(String)
     */
    public Validator createValidator(String validatorId) throws FacesException {

        Util.notNull("validatorId", validatorId);
        Validator returnVal = (Validator) newThing(validatorId, validatorMap);
        if (returnVal == null) {
            Object[] params = {validatorId};
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE,
                        "jsf.cannot_instantiate_validator_error", params);
            }
            throw new FacesException(MessageUtils.getExceptionMessageString(
                MessageUtils.NAMED_OBJECT_NOT_FOUND_ERROR_MESSAGE_ID, params));
        }
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine(MessageFormat.format("created validator of type ''{0}''",
                                             validatorId));
        }
        associate.getAnnotationManager().applyValidatorAnnotations(FacesContext.getCurrentInstance(), returnVal);
        return returnVal;
    }


    /**
     * @see javax.faces.application.Application#getValidatorIds()
     */
    public Iterator<String> getValidatorIds() {        
        
        return validatorMap.keySet().iterator();
               
    }

    /**
     * @see javax.faces.application.Application#addDefaultValidatorId(String)
     */
    public synchronized void addDefaultValidatorId(String validatorId) {

        Util.notNull("validatorId", validatorId);
        defaultValidatorInfo = null;
        defaultValidatorIds.add(validatorId);

    }

    /**
     * @see javax.faces.application.Application#getDefaultValidatorInfo() 
     */
    public Map<String,String> getDefaultValidatorInfo() {

        if (defaultValidatorInfo == null) {
            synchronized (this) {
                if (defaultValidatorInfo == null) {
                    defaultValidatorInfo = new LinkedHashMap<String, String>();
                    if (!defaultValidatorIds.isEmpty()) {
                        for (String id : defaultValidatorIds) {
                            String validatorClass;
                            Object result = validatorMap.get(id);
                            if (null != result) {
                                if (result instanceof Class) {
                                    validatorClass = ((Class) result).getName();
                                } else {
                                    validatorClass = result.toString();
                                }
                                defaultValidatorInfo.put(id, validatorClass);
                            }
                        }

                    }
                }
            }
            defaultValidatorInfo =
                  Collections.unmodifiableMap(defaultValidatorInfo);
        }

        return defaultValidatorInfo;

    }

    /**
     * @see javax.faces.application.Application#setMessageBundle(String)
     */
    public synchronized void setMessageBundle(String messageBundle) {
        Util.notNull("messageBundle", messageBundle);

        this.messageBundle = messageBundle;

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, MessageFormat.format("set messageBundle ''{0}''",
                                                        messageBundle));
        }
    }


    /**
     * @see javax.faces.application.Application#getMessageBundle()
     */
    public String getMessageBundle() {
        return messageBundle;
    }
    
    
    /**
     * <p>PRECONDITIONS: the values in the Map are either Strings
     * representing fully qualified java class names, or java.lang.Class
     * instances.</p>
     * <p>ALGORITHM: Look in the argument map for a value for the argument
     * key.  If found, if the value is instanceof String, assume the String
     * specifies a fully qualified java class name and obtain the
     * java.lang.Class instance for that String using Util.loadClass().
     * Replace the String instance in the argument map with the Class
     * instance.  If the value is instanceof Class, proceed.  Assert that the
     * value is either instanceof java.lang.Class or java.lang.String.</p>
     * <p>Now that you have a java.lang.class, call its newInstance and
     * return it as the result of this method.</p>
     *
     * @param key Used to look up the value in the <code>Map</code>.
     * @param map The <code>Map</code> that will be searched.
     * @return The new object instance.
     */
    private Object newThing(String key, ViewMemberInstanceFactoryMetadataMap<String, Object> map) {
        assert (key != null && map != null);

        Object result;
        Class<?> clazz;
        Object value;

        value = map.get(key);
        if (value == null) {
            return null;
        }
        assert (value instanceof String || value instanceof Class);
        if (value instanceof String) {
             String cValue = (String) value;
             try {
               clazz = Util.loadClass(cValue, value);
                if (!associate.isDevModeEnabled()) {
                    map.put(key, clazz);
                } 
                assert (clazz != null);
             } catch (Exception e) {
                 throw new FacesException(e.getMessage(), e);
             }
        } else {
            clazz = (Class) value;
        }
        
        try {
            result = clazz.newInstance();
        } catch (Throwable t) {
            Throwable previousT;
            do {
                previousT = t;
                if (LOGGER.isLoggable(Level.SEVERE)) {
                	LOGGER.log(Level.SEVERE, "Unable to load class: ", t);
                }
            } while (null != (t = t.getCause()));
            t = previousT;
            
            throw new FacesException((MessageUtils.getExceptionMessageString(
                  MessageUtils.CANT_INSTANTIATE_CLASS_ERROR_MESSAGE_ID,
                  clazz.getName())), t);
        }

        return result;
    }
    
    /**
     * <p>The same as newThing except that a single argument constructor
     * that accepts a Class is looked for before calling the no-arg version.</p>
     *
     * <p>PRECONDITIONS: the values in the Map are either Strings
     * representing fully qualified java class names, or java.lang.Class
     * instances.</p>
     * <p>ALGORITHM: Look in the argument map for a value for the argument
     * key.  If found, if the value is instanceof String, assume the String
     * specifies a fully qualified java class name and obtain the
     * java.lang.Class instance for that String using Util.loadClass().
     * Replace the String instance in the argument map with the Class
     * instance.  If the value is instanceof Class, proceed.  Assert that the
     * value is either instanceof java.lang.Class or java.lang.String.</p>
     * <p>Now that you have a java.lang.class, call its newInstance and
     * return it as the result of this method.</p>
     *
     * @param key Used to look up the value in the <code>Map</code>.
     * @param map The <code>Map</code> that will be searched.
     * @param targetClass the target class for the single argument ctor
     * @return The new object instance.
     */
    protected Object newConverter(Class<?> key, Map<Class<?>,Object> map, Class<?> targetClass) {
        assert (key != null && map != null);

        Object result = null;
        Class<?> clazz;
        Object value;

        value = map.get(key);
        if (value == null) {
            return null;
        }
        assert (value instanceof String || value instanceof Class);
        if (value instanceof String) {
            String cValue = (String) value;
             try {
                clazz = Util.loadClass(cValue, value);
                if (!associate.isDevModeEnabled()) {
                    map.put(key, clazz);
                }
                assert (clazz != null);
             } catch (Exception e) {
                 throw new FacesException(e.getMessage(), e);
             }
        } else {
            clazz = (Class) value;
        }
        
        Constructor ctor = 
              ReflectionUtils.lookupConstructor(clazz, Class.class);
        Throwable cause = null;
        if (ctor != null) {
            try {
                result = ctor.newInstance(targetClass);
            } catch (Exception e) {
                cause = e;
            }
        } else {
            try {
                result = clazz.newInstance();
            } catch (Exception e) {
                cause = e;
            }
        }       
        
        if (null != cause) {           
            throw new FacesException((MessageUtils.getExceptionMessageString(
                    MessageUtils.CANT_INSTANTIATE_CLASS_ERROR_MESSAGE_ID, 
                    clazz.getName())), cause);
            
        }
        return result;
    }


    // --------------------------------------------------------- Private Methods

    public static final String THIS_LIBRARY = 
            "com.sun.faces.composite.this.library";

    private UIComponent createComponentFromScriptResource(FacesContext context,
                                                          Resource scriptComponentResource,
                                                          Resource componentResource) {

        UIComponent result = null;

        String className = scriptComponentResource.getResourceName();
        int lastDot = className.lastIndexOf('.');
        className = className.substring(0, lastDot);

        try {

            Class<?> componentClass = (Class<?>) componentMap.get(className);
            if (componentClass == null) {
                componentClass = Util.loadClass(className, this);
            }
            if (!associate.isDevModeEnabled()) {
                componentMap.put(className, componentClass);
            }
            result = (UIComponent) componentClass.newInstance();
        } catch (IllegalAccessException ex) {
        	if (LOGGER.isLoggable(Level.SEVERE)) {
        		LOGGER.log(Level.SEVERE, null, ex);
        	}
        } catch (InstantiationException ex) {
        	if (LOGGER.isLoggable(Level.SEVERE)) {
        		LOGGER.log(Level.SEVERE, null, ex);
        	}
        } catch (ClassNotFoundException ex) {
        	if (LOGGER.isLoggable(Level.SEVERE)) {
        		LOGGER.log(Level.SEVERE, null, ex);
        	}
        }

        if (result != null) {
            // Make sure the resource is there for the annotation processor.
            result.getAttributes().put(Resource.COMPONENT_RESOURCE_KEY, 
                componentResource);
            // In case there are any "this" references, 
            // make sure they can be resolved.
            context.getAttributes().put(THIS_LIBRARY,
                    componentResource.getLibraryName());
            try {
                associate.getAnnotationManager()
                        .applyComponentAnnotations(context, result);
            }
            finally {
                context.getAttributes().remove(THIS_LIBRARY);
            }
        }

        return result;
        
    }


    /**
     * Leveraged by {@link Application#createComponent(String)} and {@link Application#createComponent(javax.faces.context.FacesContext, String, String)}
     * This method will apply any component and render annotations that may be present.
     */
    private UIComponent createComponentApplyAnnotations(FacesContext ctx,
                                                        String componentType,
                                                        String rendererType,
                                                        boolean applyAnnotations) {

        UIComponent c;
        try {
            c = (UIComponent) newThing(componentType, componentMap);
        } catch (Exception ex) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE,
                           "jsf.cannot_instantiate_component_error",
                           componentType);
            }
            throw new FacesException(ex);
        }
        if (c == null) {
            Object[] params = {componentType};
            if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.log(Level.SEVERE,
                               "jsf.cannot_instantiate_component_error",
                               params);
            }
            throw new FacesException(MessageUtils.getExceptionMessageString(
                    MessageUtils.NAMED_OBJECT_NOT_FOUND_ERROR_MESSAGE_ID, params));
        }

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, MessageFormat.format("Created component with component type of ''{0}''",
                                                        componentType));
        }

        if (applyAnnotations) {
            applyAnnotations(ctx, rendererType, c);
        }
        return c;

    }


    /**
     * Leveraged by {@link Application#createComponent(javax.el.ValueExpression, javax.faces.context.FacesContext, String)} and
     * {@link Application#createComponent(javax.el.ValueExpression, javax.faces.context.FacesContext, String, String)}.
     * This method will apply any component and render annotations that may be present.
     */
    private UIComponent createComponentApplyAnnotations(FacesContext ctx,
                                                        ValueExpression componentExpression,
                                                        String componentType,
                                                        String rendererType,
                                                        boolean applyAnnotations) {

        UIComponent c;

        try {
            c = (UIComponent) componentExpression
                  .getValue(ctx.getELContext());
            
            if (c == null) {
                c = this.createComponentApplyAnnotations(ctx,
                                                         componentType,
                                                         rendererType,
                                                         applyAnnotations);
                componentExpression.setValue((ctx.getELContext()), c);
            } else if (applyAnnotations) {
                this.applyAnnotations(ctx, rendererType, c);
            }
        } catch (Exception ex) {
            throw new FacesException(ex);
        }

        return c;

    }


    /**
     * Process any annotations associated with this component/renderer.
     */
    private void applyAnnotations(FacesContext ctx,
                                  String rendererType,
                                  UIComponent c) {

        if (c != null && ctx != null) {
            associate.getAnnotationManager()
                  .applyComponentAnnotations(ctx, c);
            if (rendererType != null) {
                RenderKit rk = ctx.getRenderKit();
                Renderer r = null;
                if (rk != null) {
                    r = rk.getRenderer(c.getFamily(), rendererType);
                    if (r != null) {
                        c.setRendererType(rendererType);
                        associate.getAnnotationManager()
                           .applyRendererAnnotations(ctx, r, c);
                    }
                }
                if ((rk == null || r == null) && LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE,
                       "Unable to create Renderer with rendererType {0} for component with component type of {1}",
                       new Object[] { rendererType, c.getFamily() });
                }
            }
        }
    }

    /**
     * @return the SystemEventListeners that should be used for the
     * provided combination of SystemEvent and source.
     */
    private Set<SystemEventListener> getListeners(Class<? extends SystemEvent> systemEvent,
                                                  Class<?> sourceClass) {

        Set<SystemEventListener> listeners = null;
        EventInfo sourceInfo =
              systemEventHelper.getEventInfo(systemEvent, sourceClass);
        if (sourceInfo != null) {
            listeners = sourceInfo.getListeners();
        }

        return listeners;

    }

    private SystemEvent invokeViewListenersFor(FacesContext ctx,
                                               Class<? extends SystemEvent> systemEventClass,
                                               SystemEvent event,
                                               Object source) {
        SystemEvent result = event;

        if (listenerInvocationGuard.isGuardSet(ctx, systemEventClass)) {
            return result;
        }
        listenerInvocationGuard.setGuard(ctx, systemEventClass);


        UIViewRoot root = ctx.getViewRoot();
        try {
            if (root != null) {
                List<SystemEventListener> listeners = root.getViewListenersForEventClass(systemEventClass);
                if (null == listeners) {
                    return null;
                }

                EventInfo rootEventInfo =
                        systemEventHelper.getEventInfo(systemEventClass,
                        UIViewRoot.class);
                // process view listeners
                result = processListenersAccountingForAdds(listeners,
                        event,
                        source,
                        rootEventInfo);
            }
        } finally {
            listenerInvocationGuard.clearGuard(ctx, systemEventClass);
        }
        return result;

    }

    /*
     * This class encapsulates the behavior to prevent infinite loops
     * when the publishing of one event leads to the queueing of another
     * event of the same type.  Special provision is made to allow the
     * case where this guaring mechanims happens on a per-FacesContext,
     * per-SystemEvent.class type basis.
     */

    private ReentrantLisneterInvocationGuard listenerInvocationGuard = new ReentrantLisneterInvocationGuard();

    private class ReentrantLisneterInvocationGuard {

        public boolean isGuardSet(FacesContext ctx, Class<? extends SystemEvent> systemEventClass) {
            Boolean result;
            Map<Class<? extends SystemEvent>, Boolean> data = getDataStructure(ctx);
            result = data.get(systemEventClass);

            return (null == result ? false : result);
        }

        public void setGuard(FacesContext ctx, Class<? extends SystemEvent> systemEventClass) {
            Map<Class<? extends SystemEvent>, Boolean> data = getDataStructure(ctx);
            data.put(systemEventClass, Boolean.TRUE);

        }

        public void clearGuard(FacesContext ctx, Class<? extends SystemEvent> systemEventClass) {
            Map<Class<? extends SystemEvent>, Boolean> data = getDataStructure(ctx);
            data.put(systemEventClass, Boolean.FALSE);
            
        }

        private Map<Class<? extends SystemEvent>, Boolean> getDataStructure(FacesContext ctx) {
            Map<Class<? extends SystemEvent>, Boolean> result = null;
            Map<Object, Object> ctxMap = ctx.getAttributes();
            final String IS_PROCESSING_LISTENERS_KEY =
                    "com.sun.faces.application.ApplicationImpl.IS_PROCESSING_LISTENERS";

            if (null == (result = (Map<Class<? extends SystemEvent>, Boolean>)
                         ctxMap.get(IS_PROCESSING_LISTENERS_KEY))) {
                result = new HashMap<Class<? extends SystemEvent>, Boolean>(12);
                ctxMap.put(IS_PROCESSING_LISTENERS_KEY, result);
            }

            return result;
        }

    }

    /**
     * @return process any listeners for the specified SystemEventListenerHolder
     *  and return any SystemEvent that may have been created as a side-effect
     *  of processing the listeners.
     */
    private SystemEvent invokeComponentListenersFor(Class<? extends SystemEvent> systemEventClass,
                                                    Object source) {

        if (source instanceof SystemEventListenerHolder) {

            List<SystemEventListener> listeners = ((SystemEventListenerHolder) source).getListenersForEventClass(systemEventClass);
            if (null == listeners) {
                return null;
            }
            EventInfo eventInfo =
                  compSysEventHelper.getEventInfo(systemEventClass,
                                                  source.getClass());
            return processListeners(listeners,
                                    null,
                                    source,
                                    eventInfo);
        }
        return null;

    }

    /**
     * Traverse the <code>List</code> of listeners and invoke any that are relevent
     * for the specified source.
     *
     * @throws javax.faces.event.AbortProcessingException propagated from the listener invocation
     */
    private SystemEvent invokeListenersFor(Class<? extends SystemEvent> systemEventClass,
                                           SystemEvent event,
                                           Object source,
                                           Class<?> sourceBaseType,
                                           boolean useSourceLookup)
    throws AbortProcessingException {

        EventInfo eventInfo = systemEventHelper.getEventInfo(systemEventClass,
                                                             source,
                                                             sourceBaseType,
                                                             useSourceLookup);
        if (eventInfo != null) {
            Set<SystemEventListener> listeners = eventInfo.getListeners();
            event = processListeners(listeners, event, source, eventInfo);
        }

        return event;

    }

    /**
     * Iterate through and invoke the listeners.  If the passed event was
     * <code>null</code>, create the event, and return it.
     */
    private SystemEvent processListeners(Collection<SystemEventListener> listeners,
                                         SystemEvent event,
                                         Object source,
                                         EventInfo eventInfo) {

          if (listeners != null && !listeners.isEmpty()) {
            ArrayList<SystemEventListener> list = 
                    new ArrayList<SystemEventListener>(listeners);
            
            for (SystemEventListener curListener : list) {
                if (curListener != null && curListener.isListenerForSource(source)) {
                    if (event == null) {
                        event = eventInfo.createSystemEvent(source);
                    }
                    assert (event != null);
                    if (event.isAppropriateListener(curListener)) {
                        event.processListener(curListener);
                    }
                }
            }
        }

        return event;

    }

    private SystemEvent processListenersAccountingForAdds(List<SystemEventListener> listeners,
                                         SystemEvent event,
                                         Object source,
                                         EventInfo eventInfo) {

          if (listeners != null && !listeners.isEmpty()) {

              // copy listeners
              // go thru copy completely
              // compare copy to original
              // if original differs from copy, make a new copy.
              // The new copy consists of the original list - processed

              SystemEventListener listenersCopy[] =
                      new SystemEventListener[listeners.size()];
              int i = 0;
              for (i = 0; i < listenersCopy.length; i++) {
                  listenersCopy[i] = listeners.get(i);
              }

              Map<SystemEventListener, Boolean> processedListeners =
                      new HashMap<SystemEventListener, Boolean>(listeners.size());
              boolean processedSomeEvents = false,
                      originalDiffersFromCopy = false;

              do {
                  i = 0;
                  originalDiffersFromCopy = false;
                  if (0 < listenersCopy.length) {
                      for (i = 0; i < listenersCopy.length; i++) {
                          SystemEventListener curListener = listenersCopy[i];
                          if (curListener != null && curListener.isListenerForSource(source)) {
                              if (event == null) {
                                  event = eventInfo.createSystemEvent(source);
                              }
                              assert (event != null);
                              if (!processedListeners.containsKey(curListener)
                                       && event.isAppropriateListener(curListener)) {
                                  processedSomeEvents = true;
                                  event.processListener(curListener);
                                  processedListeners.put(curListener, Boolean.TRUE);
                              }
                          }
                      }
                      if (originalDiffersFromCopy(listeners, listenersCopy)) {
                          originalDiffersFromCopy = true;
                          listenersCopy = copyListWithExclusions(listeners, processedListeners);
                      }
                  }
              } while (originalDiffersFromCopy && processedSomeEvents);
        }

        return event;

    }

    private boolean originalDiffersFromCopy(Collection<SystemEventListener> original,
            SystemEventListener copy[]) {
        boolean foundDifference = false;
        int i = 0, originalLen = original.size(), copyLen = copy.length;

        if (originalLen == copyLen) {
            SystemEventListener originalItem, copyItem;
            Iterator<SystemEventListener> iter = original.iterator();
            while (iter.hasNext() && !foundDifference) {
                originalItem = iter.next();
                copyItem = copy[i++];
                foundDifference = originalItem != copyItem;
            }
        } else {
            foundDifference = true;
        }

        return foundDifference;
    }

    private SystemEventListener [] copyListWithExclusions(Collection<SystemEventListener> original,
            Map<SystemEventListener, Boolean> excludes) {
        SystemEventListener [] result = null,
                temp = new SystemEventListener[original.size()];
        int i = 0;
        for (SystemEventListener cur : original) {
            if (!excludes.containsKey(cur)) {
                temp[i++] = cur;
            }
        }
        result = new SystemEventListener[i];
        System.arraycopy(temp, 0, result, 0, i);

        return result;
    }
    
	private boolean needsProcessing(FacesContext context, Class<? extends SystemEvent> systemEventClass) {
		return context.isProcessingEvents() || ExceptionQueuedEvent.class.isAssignableFrom(systemEventClass);
	}


    // ----------------------------------------------------------- Inner Classes


    /**
     * Utility class for dealing with application events.
     */
    private static class SystemEventHelper {

        private final Cache<Class<? extends SystemEvent>, SystemEventInfo> systemEventInfoCache;


        // -------------------------------------------------------- Constructors


        public SystemEventHelper() {

            systemEventInfoCache =
                  new Cache<Class<? extends SystemEvent>, SystemEventInfo>(
                        new Factory<Class<? extends SystemEvent>, SystemEventInfo>() {
                            public SystemEventInfo newInstance(final Class<? extends SystemEvent> arg)
                                  throws InterruptedException {
                                return new SystemEventInfo(arg);
                            }
                        }
                  );

        }


        // ------------------------------------------------------ Public Methods


        public EventInfo getEventInfo(Class<? extends SystemEvent> systemEventClass,
                                      Class<?> sourceClass) {

            EventInfo info = null;
            SystemEventInfo systemEventInfo = systemEventInfoCache.get(systemEventClass);
            if (systemEventInfo != null) {
                info = systemEventInfo.getEventInfo(sourceClass);
            }

            return info;

        }


        public EventInfo getEventInfo(Class<? extends SystemEvent> systemEventClass,
                                      Object source,
                                      Class<?> sourceBaseType,
                                      boolean useSourceForLookup) {

            Class<?> sourceClass =
                  ((useSourceForLookup) ?
                       ((sourceBaseType != null)
                                          ? sourceBaseType
                                          : source.getClass())
                                        : Void.class);
            return getEventInfo(systemEventClass, sourceClass);

        }


    } // END SystemEventHelper


    /**
     * Utility class for dealing with {@link javax.faces.component.UIComponent} events.
     */
    private static class ComponentSystemEventHelper {

        private Cache<Class<?>,Cache<Class<? extends SystemEvent>,EventInfo>> sourceCache;


        // -------------------------------------------------------- Constructors


        public ComponentSystemEventHelper() {

            // Initialize the 'sources' cache for, ahem, readability...
            // ~generics++
            Factory<Class<?>, Cache<Class<? extends SystemEvent>, EventInfo>> eventCacheFactory =
                  new Factory<Class<?>, Cache<Class<? extends SystemEvent>, EventInfo>>() {
                      public Cache<Class<? extends SystemEvent>, EventInfo> newInstance(
                            final Class<?> sourceClass)
                            throws InterruptedException {
                          Factory<Class<? extends SystemEvent>, EventInfo> eventInfoFactory =
                                new Factory<Class<? extends SystemEvent>, EventInfo>() {
                                    public EventInfo newInstance(final Class<? extends SystemEvent> systemEventClass)
                                          throws InterruptedException {
                                        return new EventInfo(systemEventClass, sourceClass);
                                    }
                                };
                          return new Cache<Class<? extends SystemEvent>, EventInfo>(eventInfoFactory);
                      }
                  };
            sourceCache = new Cache<Class<?>,Cache<Class<? extends SystemEvent>,EventInfo>>(eventCacheFactory);

        }

        // ------------------------------------------------------ Public Methods


        public EventInfo getEventInfo(Class<? extends SystemEvent> systemEvent,
                                      Class<?> sourceClass) {

            Cache<Class<? extends SystemEvent>, EventInfo> eventsCache =
                  sourceCache.get(sourceClass);
            return eventsCache.get(systemEvent);

        }

    } // END ComponentSystemEventHelper


    /**
     * Simple wrapper class for application level SystemEvents.  It provides the
     * structure to map a single SystemEvent to multiple sources which are
     * represented by <code>SourceInfo</code> instances.
     */
    private static class SystemEventInfo {

        private Cache<Class<?>,EventInfo> cache = new Cache<Class<?>,EventInfo>(
              new Factory<Class<?>, EventInfo>() {
                  public EventInfo newInstance(Class<?> arg)
                        throws InterruptedException {
                      return new EventInfo(systemEvent, arg);
                  }
              }
        );
        private Class<? extends SystemEvent> systemEvent;


        // -------------------------------------------------------- Constructors


        private SystemEventInfo(Class<? extends SystemEvent> systemEvent) {

            this.systemEvent = systemEvent;

        }


        // ------------------------------------------------------ Public Methods


        public EventInfo getEventInfo(Class<?> source) {

            Class<?> sourceClass = ((source == null) ? Void.class : source);
            return cache.get(sourceClass);

        }

    } // END SystemEventInfo


    /**
     * Represent a logical association between a SystemEvent and a Source.
     * This call will contain the Listeners specific to this association
     * as well as provide a method to construct new SystemEvents as required.
     */
    private static class EventInfo {
        private Class<? extends SystemEvent> systemEvent;
        private Class<?> sourceClass;
        private Set<SystemEventListener> listeners;
        private Constructor eventConstructor;
        private Map<Class<?>,Constructor> constructorMap;

        // -------------------------------------------------------- Constructors


        public EventInfo(Class<? extends SystemEvent> systemEvent,
                         Class<?> sourceClass) {

            this.systemEvent = systemEvent;
            this.sourceClass = sourceClass;
            this.listeners = new CopyOnWriteArraySet<SystemEventListener>();
            this.constructorMap = new HashMap<Class<?>,Constructor>();
            if (!sourceClass.equals(Void.class)) {
                eventConstructor = getEventConstructor(sourceClass);
            }

        }

        // ------------------------------------------------------ Public Methods


        public Set<SystemEventListener> getListeners() {

            return listeners;

        }


        public SystemEvent createSystemEvent(Object source) {

            Constructor toInvoke = getCachedConstructor(source.getClass());
            if (toInvoke != null) {
                try {
                    return (SystemEvent) toInvoke.newInstance(source);
                } catch (Exception e) {
                    throw new FacesException(e);
                }
            }
            return null;

        }


        // ----------------------------------------------------- Private Methods


        private Constructor getCachedConstructor(Class<?> source) {

            if (eventConstructor != null) {
                return eventConstructor;
            } else {
                Constructor c = constructorMap.get(source);
                if (c == null) {
                    c = getEventConstructor(source);
                    if (c != null) {
                        constructorMap.put(source, c);
                    }
                }
                return c;
            }

        }


        private Constructor getEventConstructor(Class<?> source) {

            Constructor ctor = null;
            try {
                return systemEvent.getDeclaredConstructor(source);
            } catch (NoSuchMethodException ignored) {
                Constructor[] ctors = systemEvent.getConstructors();
                if (ctors != null) {
                    for (Constructor c : ctors) {
                        Class<?>[] params = c.getParameterTypes();
                        if (params.length != 1) {
                            continue;
                        }
                        if (params[0].isAssignableFrom(source)) {
                            return c;
                        }
                    }
                }
                if (eventConstructor == null && LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE,
                        "Unable to find Constructor within {0} that accepts {1} instances.",
                        new Object[] { systemEvent.getName(), sourceClass.getName() });
                }
            }
            return ctor;

        }

    } // END SourceInfo


    private static final class ComponentResourceClassNotFound { }

}
