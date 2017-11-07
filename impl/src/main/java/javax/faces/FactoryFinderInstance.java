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

package javax.faces;

import static com.sun.faces.util.Util.generateCreatedBy;
import static com.sun.faces.util.Util.getContextClassLoader2;
import static com.sun.faces.util.Util.isAnyNull;
import static com.sun.faces.util.Util.isOneOf;
import static java.text.MessageFormat.format;
import static java.util.Collections.binarySearch;
import static java.util.Collections.unmodifiableMap;
import static java.util.logging.Level.SEVERE;
import static javax.faces.FactoryFinder.APPLICATION_FACTORY;
import static javax.faces.FactoryFinder.CLIENT_WINDOW_FACTORY;
import static javax.faces.FactoryFinder.EXCEPTION_HANDLER_FACTORY;
import static javax.faces.FactoryFinder.EXTERNAL_CONTEXT_FACTORY;
import static javax.faces.FactoryFinder.FACELET_CACHE_FACTORY;
import static javax.faces.FactoryFinder.FACES_CONTEXT_FACTORY;
import static javax.faces.FactoryFinder.FACTORIES_CACHE;
import static javax.faces.FactoryFinder.FLASH_FACTORY;
import static javax.faces.FactoryFinder.FLOW_HANDLER_FACTORY;
import static javax.faces.FactoryFinder.LIFECYCLE_FACTORY;
import static javax.faces.FactoryFinder.PARTIAL_VIEW_CONTEXT_FACTORY;
import static javax.faces.FactoryFinder.RENDER_KIT_FACTORY;
import static javax.faces.FactoryFinder.SEARCH_EXPRESSION_CONTEXT_FACTORY;
import static javax.faces.FactoryFinder.TAG_HANDLER_DELEGATE_FACTORY;
import static javax.faces.FactoryFinder.VIEW_DECLARATION_LANGUAGE_FACTORY;
import static javax.faces.FactoryFinder.VISIT_CONTEXT_FACTORY;
import static javax.faces.ServletContextFacesContextFactory.SERVLET_CONTEXT_FINDER_NAME;
import static javax.faces.ServletContextFacesContextFactory.SERVLET_CONTEXT_FINDER_REMOVAL_NAME;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Logger;

import javax.faces.context.FacesContext;

import com.sun.faces.config.ConfigManager;
import com.sun.faces.spi.InjectionProvider;

final class FactoryFinderInstance {
    
    private static final Logger LOGGER = Logger.getLogger("javax.faces", "javax.faces.LogStrings");
    
    private static final String INJECTION_PROVIDER_KEY = FactoryFinder.class.getPackage().getName() + "INJECTION_PROVIDER_KEY";
    
    private final Map<String, Object> factories = new ConcurrentHashMap<>();
    private final Map<String, List<String>> savedFactoryNames = new ConcurrentHashMap<>();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);
    private final String createdBy;
    
    private ServletContextFacesContextFactory servletContextFinder = new ServletContextFacesContextFactory();

    /**
     * <p>
     * The set of JavaServer Faces factory classes for which the factory discovery mechanism is
     * supported. The entries in this list must be alphabetically ordered according to the entire
     * string of the *value* of each of the literals, not just the last part of the literal!
     * </p>
     */
    private final List<String> factoryNames = asSortedList(
            APPLICATION_FACTORY, 
            VISIT_CONTEXT_FACTORY, 
            EXCEPTION_HANDLER_FACTORY,
            EXTERNAL_CONTEXT_FACTORY, 
            FACES_CONTEXT_FACTORY, 
            FLASH_FACTORY,
            FLOW_HANDLER_FACTORY,
            PARTIAL_VIEW_CONTEXT_FACTORY, 
            CLIENT_WINDOW_FACTORY, 
            LIFECYCLE_FACTORY,
            RENDER_KIT_FACTORY, 
            VIEW_DECLARATION_LANGUAGE_FACTORY, 
            FACELET_CACHE_FACTORY,
            TAG_HANDLER_DELEGATE_FACTORY, 
            SEARCH_EXPRESSION_CONTEXT_FACTORY);

    /**
     * <p>
     * Map of Class instances for our factory names.
     * </p>
     */
    private final Map<String, Class<?>> factoryClasses = buildFactoryClassesMap();
  
    
    // -------------------------------------------------------- Constructors
    
    FactoryFinderInstance(FacesContext facesContext) {
        for (String name : factoryNames) {
            factories.put(name, new ArrayList<>(4)); // NOPMD
        }
        copyInjectionProviderFromFacesContext(facesContext);
        createdBy = generateCreatedBy(facesContext);
    }

    FactoryFinderInstance(FacesContext facesContext, FactoryFinderInstance toCopy) {
        factories.putAll(toCopy.savedFactoryNames);
        copyInjectionProviderFromFacesContext(facesContext);
        createdBy = generateCreatedBy(facesContext);
    }
  
    @Override
    public String toString() {
        return super.toString() + " created by" + createdBy;
    }
    
    

    // ------------------------------------------------------ Package Private Methods
    

    void addFactory(String factoryName, String implementationClassName) {
        validateFactoryName(factoryName);

        Object result = factories.get(factoryName);
        lock.writeLock().lock();
        try {
            if (result instanceof List) {
                TypedCollections.dynamicallyCastList((List<?>) result, String.class).add(0, implementationClassName);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    @SuppressWarnings("unchecked")
    Object getFactory(String factoryName) {
        validateFactoryName(factoryName);
        
        if (factoryName.equals(SERVLET_CONTEXT_FINDER_NAME)) {
            return servletContextFinder;
        }
        
        if (factoryName.equals(SERVLET_CONTEXT_FINDER_REMOVAL_NAME)) {
            try {
                lock.writeLock().lock();
                servletContextFinder = null;
                return null;
            } finally {
                lock.writeLock().unlock();
            }
        }

        Object factoryOrList;
        lock.readLock().lock();
        try {
            factoryOrList = factories.get(factoryName);
            if (!(factoryOrList instanceof List)) {
                return factoryOrList;
            }
        } finally {
            lock.readLock().unlock();
        }
        
        // Factory hasn't been constructed
        lock.writeLock().lock();
        try {
            
            // Double check the current value. Another thread
            // may have completed the initialization by the time
            // this thread entered this block
            factoryOrList = factories.get(factoryName);
            if (!(factoryOrList instanceof List)) {
                return factoryOrList;
            }
            
            savedFactoryNames.put(factoryName, new ArrayList<String>((List<String>) factoryOrList));

            Object factory = getImplementationInstance(getContextClassLoader2(), factoryName, (List<String>) factoryOrList);
            
            if (factory == null) {
                logNoFactory(factoryName);
                
                factory = FACTORIES_CACHE.getFallbackFactory(this, factoryName);
                
                notNullFactory(factoryName, factory);
            }
            
            // Record and return the new instance
            factories.put(factoryName, factory);
            
            return factory;
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    InjectionProvider getInjectionProvider() {
        return (InjectionProvider) factories.get(INJECTION_PROVIDER_KEY);
    }

    void clearInjectionProvider() {
        factories.remove(INJECTION_PROVIDER_KEY);
    }
    
    void releaseFactories() {
        InjectionProvider provider = getInjectionProvider();
        
        if (provider != null) {
            lock.writeLock().lock();
            try {
                for (Entry<String, Object> entry : factories.entrySet()) {
                    Object curFactory = entry.getValue();
                    
                    // If the current entry is not the injectionProvider itself
                    // and the current entry has a non-null value
                    // and the value is not a string...
                    if (!INJECTION_PROVIDER_KEY.equals(entry.getKey()) && curFactory != null && !(curFactory instanceof String)) {
                        try {
                            provider.invokePreDestroy(curFactory);
                        } catch (Exception ex) {
                            logPreDestroyFail(entry.getValue(), ex);
                        }
                    }
                }
            } finally {
                factories.clear();
                lock.writeLock().unlock();
            }

        } else {
            LOGGER.log(SEVERE,
                "Unable to call @PreDestroy annotated methods because no InjectionProvider can be found. Does this container implement the Mojarra Injection SPI?");
        }
    }

    
    Collection<Object> getFactories() {
        return factories.values();
    }
    
    
    //   -------------------------------------------------------- Private methods

    private void copyInjectionProviderFromFacesContext(FacesContext facesContext) {
        InjectionProvider injectionProvider = null;
        if (facesContext != null) {
            injectionProvider = (InjectionProvider) facesContext.getAttributes().get(ConfigManager.INJECTION_PROVIDER_KEY);
        }
        
        if (injectionProvider != null) {
            factories.put(INJECTION_PROVIDER_KEY, injectionProvider);
        } else {
            LOGGER.log(SEVERE,
                "Unable to obtain InjectionProvider from init time FacesContext. Does this container implement the Mojarra Injection SPI?");
        }
    }

    /**
     * <p>
     * Load and return an instance of the specified implementation class using the following
     * algorithm.
     * </p>
     * <p/>
     * <ol>
     * <p/>
     * <li>
     * <p>
     * If the argument <code>implementations</code> list has more than one element, or exactly one
     * element, interpret the last element in the list to be the fully qualified class name of a
     * class implementing <code>factoryName</code>. Instantiate that class and save it for return.
     * If the <code>implementations</code> list has only one element, skip this step.
     * </p>
     * </li>
     * <p/>
     * <li>
     * <p>
     * Look for a resource called <code>/META-INF/services/&lt;factoryName&gt;</code>. If found,
     * interpret it as a properties file, and read out the first entry. Interpret the first entry as
     * a fully qualify class name of a class that implements <code>factoryName</code>. If we have an
     * instantiated factory from the previous step <em>and</em> the implementing class has a one arg
     * constructor of the type for <code>factoryName</code>, instantiate it, passing the
     * instantiated factory from the previous step. If there is no one arg constructor, just
     * instantiate the zero arg constructor. Save the newly instantiated factory for return,
     * replacing the instantiated factory from the previous step.
     * </p>
     * </li>
     * <p/>
     * <li>
     * <p>
     * Treat each remaining element in the <code>implementations</code> list as a fully qualified
     * class name of a class implementing <code>factoryName</code>. If the currentKeyrent element
     * has a one arg constructor of the type for <code>factoryName</code>, instantiate it, passing
     * the instantiated factory from the previous or step iteration. If there is no one arg
     * constructor, just instantiate the zero arg constructor, replacing the instantiated factory
     * from the previous step or iteration.
     * </p>
     * </li>
     * <p/>
     * <li>
     * <p>
     * Return the saved factory
     * </p>
     * </li>
     * <p/>
     * </ol>
     *
     * @param classLoader Class loader for the web application that will be loading the
     *            implementation class
     * @param implementations A List of implementations for a given factory class.
     * @throws FacesException if the specified implementation class cannot be loaded
     * @throws FacesException if an instance of the specified implementation class cannot be
     *             instantiated
     */
    private Object getImplementationInstance(ClassLoader classLoader, String factoryName, List<String> implementations) throws FacesException {

        Object implementation = null;
        String curImplClass;
        int len;

        // step 1.
        if (implementations != null && (1 < (len = implementations.size()) || 1 == len)) {
            curImplClass = implementations.remove(len - 1);
            // since this is the hard coded implementation default,
            // there is no preceding implementation, so don't bother
            // with a non-zero-argument ctor.
            implementation = getImplGivenPreviousImpl(classLoader, factoryName, curImplClass, null);
        }

        // step 2.
        List<String> fromServices = getImplNameFromServices(classLoader, factoryName);
        if (fromServices != null) {
            for (String name : fromServices) {
                implementation = getImplGivenPreviousImpl(classLoader, factoryName, name, implementation);
            }
        }

        // step 3.
        if (implementations != null) {
            for (len = (implementations.size() - 1); 0 <= len; len--) {
                curImplClass = implementations.remove(len);
                implementation = getImplGivenPreviousImpl(classLoader, factoryName, curImplClass, implementation);
            }
        }

        return implementation;
    }

    /**
     * <p>
     * Perform the logic to get the implementation class for the second step of
     * {@link FactoryFinder#getImplementationInstance(ClassLoader, String, java.util.List)}.
     * </p>
     */
    private List<String> getImplNameFromServices(ClassLoader classLoader, String factoryName) {

        // Check for a services definition
        List<String> implementationNames = new ArrayList<>();
        String resourceName = "META-INF/services/" + factoryName;
        
        try {
            Enumeration<URL> resources = classLoader.getResources(resourceName);
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                URLConnection connection = url.openConnection();
                connection.setUseCaches(false);
                
                try (InputStream stream = connection.getInputStream()) {
                    if (stream != null) {
                        implementationNames.add(readLineFromStream(stream));
                    }
                }
            }
        } catch (IOException | SecurityException e) {
            if (LOGGER.isLoggable(SEVERE)) {
                LOGGER.log(SEVERE, e.toString(), e);
            }
        }
        
        return implementationNames;
    }

    /**
     * <p>
     * Implement the decorator pattern for the factory implementation.
     * </p>
     * <p/>
     * <p>
     * If <code>previousImpl</code> is non-<code>null</code> and the class named by the argument
     * <code>implName</code> has a one arg contstructor of type <code>factoryName</code>,
     * instantiate it, passing previousImpl to the constructor.
     * </p>
     * <p/>
     * <p>
     * Otherwise, we just instantiate and return <code>implName</code>.
     * </p>
     *
     * @param classLoader the ClassLoader from which to load the class
     * @param factoryName the fully qualified class name of the factory.
     * @param factoryImplClassName the fully qualified class name of a class that implements the factory.
     * @param previousFactoryImplementation if non-<code>null</code>, the factory instance to be passed to the
     *            constructor of the new factory.
     */
    private Object getImplGivenPreviousImpl(ClassLoader classLoader, String factoryName, String factoryImplClassName, Object previousFactoryImplementation) {
        
        Object factoryImplementation = null;
        
        Class<?> factoryClass = getFactoryClass(factoryName);
        
        if (!isAnyNull(previousFactoryImplementation, factoryClass)) {
            
            // We have a previous factory implementation AND the appropriate one argument ctor.
            
            try {
                factoryImplementation = Class.forName(factoryImplClassName, false, classLoader)
                                             .getConstructor(factoryClass)
                                             .newInstance(previousFactoryImplementation);
                
            } catch (NoSuchMethodException nsme) {
                // fall through to "zero-arg-ctor" case
                factoryClass = null;
            } catch (ClassNotFoundException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                throw new FacesException(factoryImplClassName, e);
            }
        }
        
        if (isAnyNull(previousFactoryImplementation, factoryClass)) {
            
            // We have either no previous factory implementation or no appropriate one argument ctor.
            
            try {
                
                // Since this is the hard coded implementation default, there is no preceding implementation, 
                // so don't bother with a non-zero-argument ctor.
                
                factoryImplementation = Class.forName(factoryImplClassName, false, classLoader)
                                             .newInstance();
                
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                throw new FacesException(factoryImplClassName, e);
            }
        }

        injectImplementation(factoryImplClassName, factoryImplementation);

        return factoryImplementation;
    }

    /**
     * @return the <code>java.lang.Class</code> for the argument factory.
     */
    private Class<?> getFactoryClass(String factoryClassName) {
        return factoryClasses.get(factoryClassName);
    }
    
    private String readLineFromStream(InputStream stream) throws IOException {
        // Deal with systems whose native encoding is possibly
        // different from the way that the services entry was created
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"))) {
            return reader.readLine();
        } catch (UnsupportedEncodingException uee) {
            // The DM_DEFAULT_ENCODING warning is acceptable here
            // because we explicitly *want* to use the Java runtime's
            // default encoding.
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
                return reader.readLine();
            }
        }
    }
    
    private void injectImplementation(String implementationName, Object implementation) {
        if (implementation != null) {
            InjectionProvider provider = getInjectionProvider();
            if (provider != null) {
                try {
                    provider.inject(implementation);
                    provider.invokePostConstruct(implementation);
                } catch (Exception e) {
                    throw new FacesException(implementationName, e);
                }
            } else {
                LOGGER.log(SEVERE,
                    "Unable to inject {0} because no InjectionProvider can be found. Does this container implement the Mojarra Injection SPI?", implementation);
            }
        }
    }
    
    private void logNoFactory(String factoryName) {
        if (LOGGER.isLoggable(SEVERE)) {
            LOGGER.log(SEVERE, format(LOGGER.getResourceBundle().getString("severe.no_factory"), factoryName));
        }
    }
    
    private void logPreDestroyFail(Object factory, Exception ex) {
        if (LOGGER.isLoggable(SEVERE)) {
            LOGGER.log(SEVERE, format("Unable to invoke @PreDestroy annotated methods on {0}.", factory), ex);
        }
    }
    
    private void notNullFactory(String factoryName, Object factory) {
        if (factory == null) {
            throw new IllegalStateException(
                format(LOGGER.getResourceBundle().getString("severe.no_factory_backup_failed"), factoryName));
        }
    }

    private void validateFactoryName(String factoryName) {

        if (factoryName == null) {
            throw new NullPointerException();
        }
        
        if (isOneOf(factoryName, SERVLET_CONTEXT_FINDER_NAME, SERVLET_CONTEXT_FINDER_REMOVAL_NAME)) {
            return;
        }

        if (binarySearch(factoryNames, factoryName) < 0) {
            throw new IllegalArgumentException(factoryName);
        }
    }
    
    private Map<String, Class<?>> buildFactoryClassesMap() {
        Map<String, Class<?>> buildUpFactoryClasses;
        
        buildUpFactoryClasses = new HashMap<>();
        buildUpFactoryClasses.put(APPLICATION_FACTORY, javax.faces.application.ApplicationFactory.class);
        buildUpFactoryClasses.put(VISIT_CONTEXT_FACTORY, javax.faces.component.visit.VisitContextFactory.class);
        buildUpFactoryClasses.put(EXCEPTION_HANDLER_FACTORY, javax.faces.context.ExceptionHandlerFactory.class);
        buildUpFactoryClasses.put(EXTERNAL_CONTEXT_FACTORY, javax.faces.context.ExternalContextFactory.class);
        buildUpFactoryClasses.put(FACES_CONTEXT_FACTORY, javax.faces.context.FacesContextFactory.class);
        buildUpFactoryClasses.put(FLASH_FACTORY, javax.faces.context.FlashFactory.class);
        buildUpFactoryClasses.put(PARTIAL_VIEW_CONTEXT_FACTORY, javax.faces.context.PartialViewContextFactory.class);
        buildUpFactoryClasses.put(LIFECYCLE_FACTORY, javax.faces.lifecycle.LifecycleFactory.class);
        buildUpFactoryClasses.put(CLIENT_WINDOW_FACTORY, javax.faces.lifecycle.ClientWindowFactory.class);
        buildUpFactoryClasses.put(RENDER_KIT_FACTORY, javax.faces.render.RenderKitFactory.class);
        buildUpFactoryClasses.put(VIEW_DECLARATION_LANGUAGE_FACTORY, javax.faces.view.ViewDeclarationLanguageFactory.class);
        buildUpFactoryClasses.put(FACELET_CACHE_FACTORY, javax.faces.view.facelets.FaceletCacheFactory.class);
        buildUpFactoryClasses.put(TAG_HANDLER_DELEGATE_FACTORY, javax.faces.view.facelets.TagHandlerDelegateFactory.class);
        buildUpFactoryClasses.put(FLOW_HANDLER_FACTORY, javax.faces.flow.FlowHandlerFactory.class);
        buildUpFactoryClasses.put(SEARCH_EXPRESSION_CONTEXT_FACTORY, javax.faces.component.search.SearchExpressionContextFactory.class);
        
        return unmodifiableMap(buildUpFactoryClasses);
    }
    
    private List<String> asSortedList(String...names) {
        List<String> list = Arrays.asList(names);
        Collections.sort(list);
        
        return list;
    }

}
