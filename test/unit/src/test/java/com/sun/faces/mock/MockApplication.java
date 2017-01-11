/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2014 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.faces.mock;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.el.ELContextListener;
import javax.el.ELException;
import javax.el.ELResolver;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.*;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.el.MethodBinding;
import javax.faces.el.PropertyResolver;
import javax.faces.el.ValueBinding;
import javax.faces.el.VariableResolver;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import javax.faces.event.SystemEventListenerHolder;
import javax.faces.validator.Validator;
import javax.servlet.ServletContext;
import com.sun.el.ExpressionFactoryImpl;
import javax.faces.component.search.SearchExpressionHandler;
import javax.faces.component.search.SearchKeywordResolver;

public class MockApplication extends Application {

    private static final Logger LOGGER = Logger.getLogger("MockApplication");
    private final SystemEventHelper systemEventHelper = new SystemEventHelper();
    private final ComponentSystemEventHelper compSysEventHelper = new ComponentSystemEventHelper();

    public MockApplication() {
        addComponent("TestNamingContainer",
                "javax.faces.webapp.TestNamingContainer");
        addComponent("TestComponent", "javax.faces.webapp.TestComponent");
        addComponent("TestInput", "javax.faces.component.UIInput");
        addComponent("TestOutput", "javax.faces.component.UIOutput");
        addConverter("Integer", "javax.faces.convert.IntegerConverter");
        addConverter("javax.faces.Number",
                "javax.faces.convert.NumberConverter");
        addConverter("javax.faces.Long",
                "javax.faces.convert.LongConverter");
        addValidator("Length", "javax.faces.validator.LengthValidator");
        servletContext = new MockServletContext();
    }

    private ServletContext servletContext = null;
    private ActionListener actionListener = null;
    private static boolean processActionCalled = false;

    public ActionListener getActionListener() {
        if (null == actionListener) {
            actionListener = new ActionListener() {
                public void processAction(ActionEvent e) {
                    processActionCalled = true;
                }

                // see if the other object is the same as our
                // anonymous inner class implementation.
                public boolean equals(Object otherObj) {
                    if (!(otherObj instanceof ActionListener)) {
                        return false;
                    }
                    ActionListener other = (ActionListener) otherObj;

                    processActionCalled = false;
                    other.processAction(null);
                    boolean result = processActionCalled;
                    processActionCalled = false;
                    return result;
                }
            };
        }

        return (this.actionListener);
    }

    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    private NavigationHandler navigationHandler = null;

    public NavigationHandler getNavigationHandler() {
        return (this.navigationHandler);
    }

    public void setNavigationHandler(NavigationHandler navigationHandler) {
        this.navigationHandler = navigationHandler;
    }

    private ResourceHandler resourceHandler = new MockResourceHandler();

    @Override
    public ResourceHandler getResourceHandler() {
        return resourceHandler;
    }

    @Override
    public void setResourceHandler(ResourceHandler resourceHandler) {
        this.resourceHandler = resourceHandler;
    }

    private PropertyResolver propertyResolver = null;

    public PropertyResolver getPropertyResolver() {
        if (propertyResolver == null) {
            propertyResolver = new MockPropertyResolver();
        }
        return (this.propertyResolver);
    }

    public void setPropertyResolver(PropertyResolver propertyResolver) {
        this.propertyResolver = propertyResolver;
    }

    public MethodBinding createMethodBinding(String ref, Class params[]) {
        if (ref == null) {
            throw new NullPointerException();
        } else {
            return (new MockMethodBinding(this, ref, params));
        }
    }

    public ValueBinding createValueBinding(String ref) {
        if (ref == null) {
            throw new NullPointerException();
        } else {
            return (new MockValueBinding(this, ref));
        }
    }

    // PENDING(edburns): implement
    public void addELResolver(ELResolver resolver) {
    }

    // PENDING(edburns): implement
    public ELResolver getELResolver() {
        return null;
    }

    private ExpressionFactory expressionFactory = null;

    public ExpressionFactory getExpressionFactory() {
        if (null == expressionFactory) {
            expressionFactory = new ExpressionFactoryImpl();
        }
        return expressionFactory;
    }

    public Object evaluateExpressionGet(FacesContext context,
            String expression,
            Class expectedType) throws ELException {
        ValueExpression ve = getExpressionFactory().createValueExpression(context.getELContext(), expression, expectedType);
        return ve.getValue(context.getELContext());
    }

    private VariableResolver variableResolver = null;

    public VariableResolver getVariableResolver() {
        if (variableResolver == null) {
            variableResolver = new MockVariableResolver();
        }
        return (this.variableResolver);
    }

    public void setVariableResolver(VariableResolver variableResolver) {
        this.variableResolver = variableResolver;
    }

    private ViewHandler viewHandler = null;

    public ViewHandler getViewHandler() {
        if (null == viewHandler) {
            viewHandler = new MockViewHandler();
        }
        return (this.viewHandler);
    }

    public void setViewHandler(ViewHandler viewHandler) {
        this.viewHandler = viewHandler;
    }

    private StateManager stateManager = null;

    public StateManager getStateManager() {
        if (null == stateManager) {
            stateManager = new MockStateManager();
        }
        return (this.stateManager);
    }

    public void setStateManager(StateManager stateManager) {
        this.stateManager = stateManager;
    }

    private Map components = new HashMap();

    public void addComponent(String componentType, String componentClass) {
        components.put(componentType, componentClass);
    }

    public UIComponent createComponent(String componentType) {
        String componentClass = (String) components.get(componentType);
        try {
            Class clazz = Class.forName(componentClass);
            return ((UIComponent) clazz.newInstance());
        } catch (Exception e) {
            throw new FacesException(e);
        }
    }

    public UIComponent createComponent(ValueBinding componentBinding,
            FacesContext context,
            String componentType)
            throws FacesException {
        throw new FacesException(new UnsupportedOperationException());
    }

    public UIComponent createComponent(ValueExpression componentExpression,
            FacesContext context,
            String componentType)
            throws FacesException {
        throw new FacesException(new UnsupportedOperationException());
    }

    public Iterator getComponentTypes() {
        return (components.keySet().iterator());
    }

    private Map converters = new HashMap();

    public void addConverter(String converterId, String converterClass) {
        converters.put(converterId, converterClass);
    }

    public void addConverter(Class targetClass, String converterClass) {
        throw new UnsupportedOperationException();
    }

    public Converter createConverter(String converterId) {
        String converterClass = (String) converters.get(converterId);
        try {
            Class clazz = Class.forName(converterClass);
            return ((Converter) clazz.newInstance());
        } catch (Exception e) {
            throw new FacesException(e);
        }
    }

    public Converter createConverter(Class targetClass) {
        throw new UnsupportedOperationException();
    }

    public Iterator getConverterIds() {
        return (converters.keySet().iterator());
    }

    public Iterator getConverterTypes() {
        throw new UnsupportedOperationException();
    }

    private String messageBundle = null;

    public void setMessageBundle(String messageBundle) {
        this.messageBundle = messageBundle;
    }

    public String getMessageBundle() {
        return messageBundle;
    }

    private Map validators = new HashMap();

    public void addValidator(String validatorId, String validatorClass) {
        validators.put(validatorId, validatorClass);
    }

    public Validator createValidator(String validatorId) {
        String validatorClass = (String) validators.get(validatorId);
        try {
            Class clazz = Class.forName(validatorClass);
            return ((Validator) clazz.newInstance());
        } catch (Exception e) {
            throw new FacesException(e);
        }
    }

    public Iterator getValidatorIds() {
        return (validators.keySet().iterator());
    }

    public Iterator getSupportedLocales() {
        return Collections.EMPTY_LIST.iterator();
    }

    public void setSupportedLocales(Collection newLocales) {
    }

    public void addELContextListener(ELContextListener listener) {
        // PENDING(edburns): maybe implement
    }

    public void removeELContextListener(ELContextListener listener) {
        // PENDING(edburns): maybe implement
    }

    public ELContextListener[] getELContextListeners() {
        // PENDING(edburns): maybe implement
        return (ELContextListener[]) java.lang.reflect.Array.newInstance(ELContextListener.class,
                0);
    }

    public Locale getDefaultLocale() {
        return Locale.getDefault();
    }

    public void setDefaultLocale(Locale newLocale) {
    }

    public String getDefaultRenderKitId() {
        return null;
    }

    public void setDefaultRenderKitId(String renderKitId) {
    }

    public ResourceBundle getResourceBundle(FacesContext ctx, String name) {
        return null;
    }

    /**
     * <p class="changed_added_2_0">If there are one or more listeners for
     * events of the type represented by <code>systemEventClass</code>, call
     * those listeners, passing <code>source</code> as the source of the event.
     * The implementation should be as fast as possible in determining whether
     * or not a listener for the given <code>systemEventClass</code> and
     * <code>source</code> has been installed, and should return immediately
     * once such a determination has been made. The implementation of
     * <code>publishEvent</code> must honor the requirements stated in
     * {@link #subscribeToEvent} regarding the storage and retrieval of listener
     * instances.</p>
     *
     * <div class="changed_added_2_0">
     *
     * <p>
     * The default implementation must implement an algorithm semantically
     * equivalent to the following to locate listener instances and to invoke
     * them.</p>
     *
     * <ul>
     *
     * <li><p>
     * If the <code>source</code> argument implements {@link
     * javax.faces.event.SystemEventListenerHolder}, call {@link
     * javax.faces.event.SystemEventListenerHolder#getListenersForEventClass} on
     * it, passing the <code>systemEventClass</code> argument. If the list is
     * not empty, perform algorithm
     * <em>traverseListenerList</em> on the list.</p></li>
     *
     * <li><p>
     * If any <code>Application</code> level listeners have been installed by
     * previous calls to {@link
     * #subscribeToEvent(Class, Class,
     *     javax.faces.event.SystemEventListener)}, perform algorithm
     * <em>traverseListenerList</em> on the list.</p></li>
     *
     * <li><p>
     * If any <code>Application</code> level listeners have been installed by
     * previous calls to {@link
     * #subscribeToEvent(Class, javax.faces.event.SystemEventListener)}, perform
     * algorithm <em>traverseListenerList</em> on the list.</p></li>
     *
     * </ul>
     *
     * <p>
     * If the act of invoking the <code>processListener</code> method causes an
     * {@link javax.faces.event.AbortProcessingException} to be thrown,
     * processing of the listeners must be aborted.</p>
     *
     * RELEASE_PENDING (edburns,rogerk) it may be prudent to specify how the
     * abortprocessingexception should be handled. Logged or thrown?
     *
     * <p>
     * Algorithm <em>traverseListenerList</em>: For each listener in the
     * list,</p>
     *
     * <ul>
     *
     * <li><p>
     * Call {@link
     * javax.faces.event.SystemEventListener#isListenerForSource}, passing the
     * <code>source</code> argument. If this returns <code>false</code>, take no
     * action on the listener.</p></li>
     *
     * <li><p>
     * Otherwise, if the event to be passed to the listener instances has not
     * yet been constructed, construct the event, passing <code>source</code> as
     * the argument to the one-argument constructor that takes an
     * <code>Object</code>. This same event instance must be passed to all
     * listener instances.</p></li>
     *
     * <li><p>
     * Call {@link javax.faces.event.SystemEvent#isAppropriateListener}, passing
     * the listener instance as the argument. If this returns
     * <code>false</code>, take no action on the listener.</p></li>
     *
     * <li><p>
     * Call {@link javax.faces.event.SystemEvent#processListener}, passing the
     * listener instance. </p></li>
     *
     * </ul>
     * </div>
     *
     * @param systemEventClass The <code>Class</code> of event that is being
     * published.
     * @param source The source for the event of type
     * <code>systemEventClass</code>.
     *
     * @throws NullPointerException if either <code>systemEventClass</code> or
     * <code>source</code> is <code>null</code>
     *
     * @since 2.0
     */
    public void publishEvent(FacesContext context,
            Class<? extends SystemEvent> systemEventClass,
            Object source) {

        if (systemEventClass == null) {
            throw new NullPointerException("systemEventClass");
        }
        if (source == null) {
            throw new NullPointerException("source");
        }

        try {
            // The side-effect of calling invokeListenersFor
            // will create a SystemEvent object appropriate to event/source
            // combination.  This event will be passed on subsequent invocations
            // of invokeListenersFor
            SystemEvent event;

            // Look for and invoke any listeners stored on the source instance.
            event = invokeComponentListenersFor(systemEventClass, source);

            // look for and invoke any listeners stored on the application
            // using source type.
            event = invokeListenersFor(systemEventClass, event, source, true);

            // look for and invoke any listeners not specific to the source class
            invokeListenersFor(systemEventClass, event, source, false);
        } catch (AbortProcessingException ape) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE,
                        ape.getMessage(),
                        ape);
            }
        }
    }

    /**
     * <p class="changed_added_2_0">Install the listener instance referenced by
     * argument <code>listener</code> into the application as a listener for
     * events of type <code>systemEventClass</code> that originate from objects
     * of type <code>sourceClass</code>.</p>
     *
     * <div class="changed_added_2_0">
     *
     * <p>
     * If argument <code>sourceClass</code> is non-<code>null</code>,
     * <code>sourceClass</code> and <code>systemEventClass</code> must be used
     * to store the argument <code>listener</code> in the application in such a
     * way that the <code>listener</code> can be quickly looked up by the
     * implementation of
     * {@link javax.faces.application.Application#publishEvent} given
     * <code>systemEventClass</code> and an instance of the <code>Class</code>
     * referenced by <code>sourceClass</code>. If argument
     * <code>sourceClass</code> is <code>null</code>, the <code>listener</code>
     * must be discoverable by the implementation of
     * {@link javax.faces.application.Application#publishEvent} given only
     * <code>systemEventClass</code>.
     * </p>
     *
     * </div>
     *
     * @param systemEventClass the <code>Class</code> of event for which
     * <code>listener</code> must be fired.
     *
     * @param sourceClass the <code>Class</code> of the instance which causes
     * events of type <code>systemEventClass</code> to be fired. May be
     * <code>null</code>.
     *
     * @param listener the implementation of {@link
     * javax.faces.event.SystemEventListener} whose {@link
     * javax.faces.event.SystemEventListener#processEvent} method must be called
     * when events of type <code>systemEventClass</code> are fired.
     *
     * @throws <code>NullPointerException</code> if any combination of
     * <code>systemEventClass</code>, or <code>listener</code> are
     * <code>null</code>.
     *
     * @since 2.0
     */
    public void subscribeToEvent(Class<? extends SystemEvent> systemEventClass,
            Class<?> sourceClass,
            SystemEventListener listener) {

        if (systemEventClass == null) {
            throw new NullPointerException("systemEventClass");
        }
        if (listener == null) {
            throw new NullPointerException("listener");
        }

        Set<SystemEventListener> listeners
                = getListeners(systemEventClass, sourceClass);
        listeners.add(listener);

    }

    /**
     * <p class="changed_added_2_0">Install the listener instance referenced by
     * argument <code>listener</code> into application as a listener for events
     * of type <code>systemEventClass</code>. The default implementation simply
     * calls through to
     * {@link #subscribeToEvent(Class, Class, javax.faces.event.SystemEventListener)}
     * passing <code>null</code> as the <code>sourceClass</code> argument</p>
     *
     * @param systemEventClass the <code>Class</code> of event for which
     * <code>listener</code> must be fired.
     *
     * @param listener the implementation of {@link
     * javax.faces.event.SystemEventListener} whose {@link
     * javax.faces.event.SystemEventListener#processEvent} method must be called
     * when events of type <code>systemEventClass</code> are fired.
     *
     * @throws <code>NullPointerException</code> if any combination of
     * <code>systemEventClass</code>, or <code>listener</code> are
     * <code>null</code>.
     *
     * @since 2.0
     */
    public void subscribeToEvent(Class<? extends SystemEvent> systemEventClass,
            SystemEventListener listener) {

        subscribeToEvent(systemEventClass, null, listener);

    }

    /**
     * <p class="changed_added_2_0">Remove the listener instance referenced by
     * argument <code>listener</code> from the application as a listener for
     * events of type <code>systemEventClass</code> that originate from objects
     * of type <code>sourceClass</code>. See {@link
     * #subscribeToEvent(Class, Class,
     * javax.faces.event.SystemEventListener)} for the specification of how the
     * listener is stored, and therefore, how it must be removed.</p>
     *
     * @param systemEventClass the <code>Class</code> of event for which
     * <code>listener</code> must be fired.
     *
     * @param sourceClass the <code>Class</code> of the instance which causes
     * events of type <code>systemEventClass</code> to be fired. May be
     * <code>null</code>.
     *
     * @param listener the implementation of {@link
     * javax.faces.event.SystemEventListener} to remove from the internal data
     * structure.
     *
     * @throws <code>NullPointerException</code> if any combination of
     * <code>context</code>, <code>systemEventClass</code>, or
     * <code>listener</code> are <code>null</code>.
     */
    public void unsubscribeFromEvent(Class<? extends SystemEvent> systemEventClass,
            Class<?> sourceClass,
            SystemEventListener listener) {

        if (systemEventClass == null) {
            throw new NullPointerException("systemEventClass");
        }
        if (listener == null) {
            throw new NullPointerException("listener");
        }

        Set<SystemEventListener> listeners
                = getListeners(systemEventClass, sourceClass);
        if (listeners != null) {
            listeners.remove(listener);
        }

    }

    /**
     * <p class="changed_added_2_0">Remove the listener instance referenced by
     * argument <code>listener</code> from the application as a listener for
     * events of type <code>systemEventClass</code>. The default implementation
     * simply calls through to
     * {@link #unsubscribeFromEvent(Class, javax.faces.event.SystemEventListener)}
     * passing <code>null</code> as the <code>sourceClass</code> argument</p>
     *
     * @param systemEventClass the <code>Class</code> of event for which
     * <code>listener</code> must be fired.
     *
     * @param listener the implementation of {@link
     * javax.faces.event.SystemEventListener} to remove from the internal data
     * structure.
     *
     * @throws <code>NullPointerException</code> if any combination of
     * <code>context</code>, <code>systemEventClass</code>, or
     * <code>listener</code> are <code>null</code>.
     */
    public void unsubscribeFromEvent(Class<? extends SystemEvent> systemEventClass,
            SystemEventListener listener) {

        unsubscribeFromEvent(systemEventClass, null, listener);

    }

    /**
     * @return the SystemEventListeners that should be used for the provided
     * combination of SystemEvent and source.
     */
    private Set<SystemEventListener> getListeners(Class<? extends SystemEvent> systemEvent,
            Class<?> sourceClass) {

        Set<SystemEventListener> listeners = null;
        EventInfo sourceInfo
                = systemEventHelper.getEventInfo(systemEvent, sourceClass);
        if (sourceInfo != null) {
            listeners = sourceInfo.getListeners();
        }

        return listeners;

    }

    /**
     * @return process any listeners for the specified SystemEventListenerHolder
     * and return any SystemEvent that may have been created as a side-effect of
     * processing the listeners.
     */
    private SystemEvent invokeComponentListenersFor(Class<? extends SystemEvent> systemEventClass,
            Object source) {

        if (source instanceof SystemEventListenerHolder) {
            EventInfo eventInfo
                    = compSysEventHelper.getEventInfo(systemEventClass,
                            source.getClass());
            return processListeners(((SystemEventListenerHolder) source).getListenersForEventClass(systemEventClass),
                    null,
                    source,
                    eventInfo);
        }
        return null;

    }

    /**
     * Traverse the <code>List</code> of listeners and invoke any that are
     * relevent for the specified source.
     *
     * @throws javax.faces.event.AbortProcessingException propagated from the
     * listener invocation
     */
    private SystemEvent invokeListenersFor(Class<? extends SystemEvent> systemEventClass,
            SystemEvent event,
            Object source,
            boolean useSourceLookup)
            throws AbortProcessingException {

        EventInfo eventInfo = systemEventHelper.getEventInfo(systemEventClass,
                source,
                useSourceLookup);
        if (eventInfo != null) {
            Set<SystemEventListener> listeners = eventInfo.getListeners();
            event = processListeners(listeners, event, source, eventInfo);
        }

        return event;

    }

    /**
     * Iterate through and invoke the listeners. If the passed event was
     * <code>null</code>, create the event, and return it.
     */
    private SystemEvent processListeners(Collection<SystemEventListener> listeners,
            SystemEvent event,
            Object source,
            EventInfo eventInfo) {

        if (listeners != null && !listeners.isEmpty()) {
            for (SystemEventListener curListener : listeners) {
                if (curListener.isListenerForSource(source)) {
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

    /**
     * Utility class for dealing with application events.
     */
    private static class SystemEventHelper {

        private final Cache<Class<? extends SystemEvent>, SystemEventInfo> systemEventInfoCache;

        // -------------------------------------------------------- Constructors
        public SystemEventHelper() {

            systemEventInfoCache
                    = new Cache<Class<? extends SystemEvent>, SystemEventInfo>(
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
                boolean useSourceForLookup) {

            Class<?> sourceClass
                    = ((useSourceForLookup) ? source.getClass() : Void.class);
            return getEventInfo(systemEventClass, sourceClass);

        }

    } // END SystemEventHelper

    /**
     * Utility class for dealing with {@link javax.faces.component.UIComponent}
     * events.
     */
    private static class ComponentSystemEventHelper {

        private Cache<Class<?>, Cache<Class<? extends SystemEvent>, EventInfo>> sourceCache;

        // -------------------------------------------------------- Constructors
        public ComponentSystemEventHelper() {

            // Initialize the 'sources' cache for, ahem, readability...
            // ~generics++
            Factory<Class<?>, Cache<Class<? extends SystemEvent>, EventInfo>> eventCacheFactory
                    = new Factory<Class<?>, Cache<Class<? extends SystemEvent>, EventInfo>>() {
                        public Cache<Class<? extends SystemEvent>, EventInfo> newInstance(
                                final Class<?> sourceClass)
                        throws InterruptedException {
                            Factory<Class<? extends SystemEvent>, EventInfo> eventInfoFactory
                            = new Factory<Class<? extends SystemEvent>, EventInfo>() {
                                public EventInfo newInstance(final Class<? extends SystemEvent> systemEventClass)
                                throws InterruptedException {
                                    return new EventInfo(systemEventClass, sourceClass);
                                }
                            };
                            return new Cache<Class<? extends SystemEvent>, EventInfo>(eventInfoFactory);
                        }
                    };
            sourceCache = new Cache<Class<?>, Cache<Class<? extends SystemEvent>, EventInfo>>(eventCacheFactory);

        }

        // ------------------------------------------------------ Public Methods
        public EventInfo getEventInfo(Class<? extends SystemEvent> systemEvent,
                Class<?> sourceClass) {

            Cache<Class<? extends SystemEvent>, EventInfo> eventsCache
                    = sourceCache.get(sourceClass);
            return eventsCache.get(systemEvent);

        }

    } // END ComponentSystemEventHelper

    /**
     * Simple wrapper class for application level SystemEvents. It provides the
     * structure to map a single SystemEvent to multiple sources which are
     * represented by <code>SourceInfo</code> instances.
     */
    private static class SystemEventInfo {

        private Cache<Class<?>, EventInfo> cache = new Cache<Class<?>, EventInfo>(
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
     * Represent a logical association between a SystemEvent and a Source. This
     * call will contain the Listeners specific to this association as well as
     * provide a method to construct new SystemEvents as required.
     */
    private static class EventInfo {

        private Class<? extends SystemEvent> systemEvent;
        private Class<?> sourceClass;
        private Set<SystemEventListener> listeners;
        private Constructor eventConstructor;
        private Map<Class<?>, Constructor> constructorMap;

        // -------------------------------------------------------- Constructors
        public EventInfo(Class<? extends SystemEvent> systemEvent,
                Class<?> sourceClass) {

            this.systemEvent = systemEvent;
            this.sourceClass = sourceClass;
            this.listeners = new CopyOnWriteArraySet<SystemEventListener>();
            this.constructorMap = new HashMap<Class<?>, Constructor>();
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
                if (eventConstructor == null) {
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.log(Level.FINE,
                                "Unable to find Constructor within {0} that accepts {1} instances.",
                                new Object[]{systemEvent.getName(), sourceClass.getName()});
                    }
                }
            }
            return ctor;

        }

    } // END SourceInfo

    /**
     * Factory interface for creating various cacheable objects.
     */
    private interface Factory<K, V> {

        V newInstance(final K arg) throws InterruptedException;

    } // END Factory

    /**
     * A concurrent caching mechanism.
     */
    private static final class Cache<K, V> {

        private ConcurrentMap<K, Future<V>> cache
                = new ConcurrentHashMap<K, Future<V>>();
        private Factory<K, V> factory;

        // -------------------------------------------------------- Constructors
        /**
         * Constructs this cache using the specified <code>Factory</code>.
         *
         * @param factory
         */
        public Cache(Factory<K, V> factory) {

            this.factory = factory;

        }

        // ------------------------------------------------------ Public Methods
        /**
         * If a value isn't associated with the specified key, a new
         * {@link java.util.concurrent.Callable} will be created wrapping the
         * <code>Factory</code> specified via the constructor and passed to a
         * {@link java.util.concurrent.FutureTask}. This task will be passed to
         * the backing ConcurrentMap. When
         * {@link java.util.concurrent.FutureTask#get()} is invoked, the Factory
         * will return the new Value which will be cached by the
         * {@link java.util.concurrent.FutureTask}.
         *
         * @param key the key the value is associated with
         * @return the value for the specified key, if any
         */
        public V get(final K key) {

            while (true) {
                Future<V> f = cache.get(key);
                if (f == null) {
                    Callable<V> callable = new Callable<V>() {
                        public V call() throws Exception {
                            return factory.newInstance(key);
                        }
                    };
                    FutureTask<V> ft = new FutureTask<V>(callable);
                    // here is the real beauty of the concurrent utilities.
                    // 1.  putIfAbsent() is atomic
                    // 2.  putIfAbsent() will return the value already associated
                    //     with the specified key
                    // So, if multiple threads make it to this point
                    // they will all be calling f.get() on the same
                    // FutureTask instance, so this guarantees that the instances
                    // that the invoked Callable will return will be created once
                    f = cache.putIfAbsent(key, ft);
                    if (f == null) {
                        f = ft;
                        ft.run();
                    }
                }
                try {
                    return f.get();
                } catch (CancellationException ce) {
                    if (LOGGER.isLoggable(Level.FINEST)) {
                        LOGGER.log(Level.FINEST,
                                ce.toString(),
                                ce);
                    }
                    cache.remove(key);
                } catch (InterruptedException ie) {
                    if (LOGGER.isLoggable(Level.FINEST)) {
                        LOGGER.log(Level.FINEST,
                                ie.toString(),
                                ie);
                    }
                    cache.remove(key);
                } catch (ExecutionException ee) {
                    throw new FacesException(ee);
                }
            }
        }

    } // END Cache
    
    private SearchExpressionHandler searchExpressionHandler;
    private SearchKeywordResolver searchKeywordResolver;
    
    @Override
    public SearchExpressionHandler getSearchExpressionHandler() {
        return searchExpressionHandler;
    }
    
    @Override
    public void setSearchExpressionHandler(SearchExpressionHandler searchExpressionHandler) {
        this.searchExpressionHandler = searchExpressionHandler;
    }
    
    @Override
    public SearchKeywordResolver getSearchKeywordResolver() {
        return searchKeywordResolver;
    }

    public void setSearchKeywordResolver(SearchKeywordResolver searchKeywordResolver) {
        this.searchKeywordResolver = searchKeywordResolver;
    }
}
