/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright (c) 1997-2012 Oracle and/or its affiliates. All rights reserved.
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

import com.sun.faces.spi.InjectionProvider;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;

final class FactoryFinderInstance {
    private final Map<String, Object> factories;
    private final Map<String, List<String>> savedFactoryNames;
    private final ReentrantReadWriteLock lock;
    private ServletContextFacesContextFactory servletContextFinder;
    private static final String INJECTION_PROVIDER_KEY = FactoryFinder.class.getPackage().getName() + "INJECTION_PROVIDER_KEY";

    /**
     * <p>The set of JavaServer Faces factory classes for which the factory
     * discovery mechanism is supported.  The entries in this list must be 
     * alphabetically ordered according to the entire string of the
     * *value* of each of the literals, not just
     * the last part of the literal!</p>
     */
    private static final String[] FACTORY_NAMES;
    
    /**
     * <p>Map of Class instances for the our factory names.</p>
     */
    private final static Map<String, Class> FACTORY_CLASSES;
    
    private static final Logger LOGGER;

    static {
        
        Map<String, Class> buildUpFactoryClasses; 
        buildUpFactoryClasses = new HashMap<String, Class>();
        buildUpFactoryClasses.put(FactoryFinder.APPLICATION_FACTORY,
                 javax.faces.application.ApplicationFactory.class);
        buildUpFactoryClasses.put(FactoryFinder.VISIT_CONTEXT_FACTORY,
                 javax.faces.component.visit.VisitContextFactory.class);
        buildUpFactoryClasses.put(FactoryFinder.EXCEPTION_HANDLER_FACTORY,
                 javax.faces.context.ExceptionHandlerFactory.class);
        buildUpFactoryClasses.put(FactoryFinder.EXTERNAL_CONTEXT_FACTORY,
                 javax.faces.context.ExternalContextFactory.class);
        buildUpFactoryClasses.put(FactoryFinder.FACES_CONTEXT_FACTORY,
                 javax.faces.context.FacesContextFactory.class);
        buildUpFactoryClasses.put(FactoryFinder.FLASH_FACTORY,
                 javax.faces.context.FlashFactory.class);
        buildUpFactoryClasses.put(FactoryFinder.PARTIAL_VIEW_CONTEXT_FACTORY,
                 javax.faces.context.PartialViewContextFactory.class);
        buildUpFactoryClasses.put(FactoryFinder.LIFECYCLE_FACTORY,
                 javax.faces.lifecycle.LifecycleFactory.class);
        buildUpFactoryClasses.put(FactoryFinder.CLIENT_WINDOW_FACTORY,
                 javax.faces.lifecycle.ClientWindowFactory.class);
        buildUpFactoryClasses.put(FactoryFinder.RENDER_KIT_FACTORY,
                 javax.faces.render.RenderKitFactory.class);
        buildUpFactoryClasses.put(FactoryFinder.VIEW_DECLARATION_LANGUAGE_FACTORY,
                 javax.faces.view.ViewDeclarationLanguageFactory.class);
        buildUpFactoryClasses.put(FactoryFinder.FACELET_CACHE_FACTORY,
                 javax.faces.view.facelets.FaceletCacheFactory.class);
        buildUpFactoryClasses.put(FactoryFinder.TAG_HANDLER_DELEGATE_FACTORY,
                 javax.faces.view.facelets.TagHandlerDelegateFactory.class);
        buildUpFactoryClasses.put(FactoryFinder.FLOW_HANDLER_FACTORY,
                 javax.faces.flow.FlowHandlerFactory.class);
        FACTORY_CLASSES = Collections.unmodifiableMap(buildUpFactoryClasses);

        FACTORY_NAMES = new String [] {
            FactoryFinder.APPLICATION_FACTORY,
            FactoryFinder.VISIT_CONTEXT_FACTORY,
            FactoryFinder.EXCEPTION_HANDLER_FACTORY,
            FactoryFinder.EXTERNAL_CONTEXT_FACTORY,
            FactoryFinder.FACES_CONTEXT_FACTORY,
            FactoryFinder.FLASH_FACTORY,
            FactoryFinder.FLOW_HANDLER_FACTORY,
            FactoryFinder.PARTIAL_VIEW_CONTEXT_FACTORY,
            FactoryFinder.CLIENT_WINDOW_FACTORY,
            FactoryFinder.LIFECYCLE_FACTORY,
            FactoryFinder.RENDER_KIT_FACTORY,
            FactoryFinder.VIEW_DECLARATION_LANGUAGE_FACTORY,
            FactoryFinder.FACELET_CACHE_FACTORY,
            FactoryFinder.TAG_HANDLER_DELEGATE_FACTORY
        };

        // Optimize performance of validateFactoryName
        Arrays.sort(FACTORY_NAMES);
        
        LOGGER = Logger.getLogger("javax.faces", "javax.faces.LogStrings");
    }
    
    
    // -------------------------------------------------------- Consturctors
    FactoryFinderInstance() {
        lock = new ReentrantReadWriteLock(true);
        factories = new HashMap<String, Object>();
        savedFactoryNames = new HashMap<String, List<String>>();
        for (String name : FACTORY_NAMES) {
            factories.put(name, new ArrayList(4));  // NOPMD
        }
        copyInjectionProviderFromFacesContext();
        servletContextFinder = new ServletContextFacesContextFactory();
    }

    FactoryFinderInstance(FactoryFinderInstance toCopy) {
        lock = new ReentrantReadWriteLock(true);
        factories = new HashMap<String, Object>();
        savedFactoryNames = new HashMap<String, List<String>>();
        factories.putAll(toCopy.savedFactoryNames);
        copyInjectionProviderFromFacesContext();
        servletContextFinder = new ServletContextFacesContextFactory();
    }

    private void copyInjectionProviderFromFacesContext() {
        InjectionProvider injectionProvider = null;
        FacesContext context = FacesContext.getCurrentInstance();
        if (null != context) {
            injectionProvider = (InjectionProvider) context.getAttributes().get("com.sun.faces.config.ConfigManager_INJECTION_PROVIDER_TASK");
        }
        if (null != injectionProvider) {
            factories.put(INJECTION_PROVIDER_KEY, injectionProvider);
        } else {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, "Unable to obtain InjectionProvider from init time FacesContext. Does this container implement the Mojarra Injection SPI?");
            }
        }
    }

    /**
     * <p>Load and return an instance of the specified implementation
     * class using the following algorithm.</p>
     * <p/>
     * <ol>
     * <p/>
     * <li><p>If the argument <code>implementations</code> list has
     * more than one element, or exactly one element, interpret the
     * last element in the list to be the fully qualified class name of
     * a class implementing <code>factoryName</code>.  Instantiate that
     * class and save it for return.  If the
     * <code>implementations</code> list has only one element, skip
     * this step.</p></li>
     * <p/>
     * <li><p>Look for a resource called
     * <code>/META-INF/services/&lt;factoryName&gt;</code>.  If found,
     * interpret it as a properties file, and read out the first entry.
     * Interpret the first entry as a fully qualify class name of a
     * class that implements <code>factoryName</code>.  If we have an
     * instantiated factory from the previous step <em>and</em> the
     * implementing class has a one arg constructor of the type for
     * <code>factoryName</code>, instantiate it, passing the
     * instantiated factory from the previous step.  If there is no one
     * arg constructor, just instantiate the zero arg constructor.  Save
     * the newly instantiated factory for return, replacing the
     * instantiated factory from the previous step.</p></li>
     * <p/>
     * <li><p>Treat each remaining element in the
     * <code>implementations</code> list as a fully qualified class name
     * of a class implementing <code>factoryName</code>.  If the currentKeyrent
 element has a one arg constructor of the type for
 <code>factoryName</code>, instantiate it, passing the
     * instantiated factory from the previous or step iteration.  If
     * there is no one arg constructor, just instantiate the zero arg
     * constructor, replacing the instantiated factory from the previous
     * step or iteration.</p></li>
     * <p/>
     * <li><p>Return the saved factory</p></li>
     * <p/>
     * </ol>
     *
     * @param classLoader     Class loader for the web application that will
     *                        be loading the implementation class
     * @param implementations A List of implementations for a given
     *                        factory class.
     * @throws FacesException if the specified implementation class
     *                        cannot be loaded
     * @throws FacesException if an instance of the specified implementation
     *                        class cannot be instantiated
     */
    private Object getImplementationInstance(ClassLoader classLoader,
                                             String factoryName,
                                             List implementations)
    throws FacesException {

        Object result = null;
        String curImplClass;
        int len;

        // step 1.
        if (null != implementations &&
             (1 < (len = implementations.size()) || 1 == len)) {
            curImplClass = (String) implementations.remove(len - 1);
            // since this is the hard coded implementation default,
            // there is no preceding implementation, so don't bother
            // with a non-zero-arg ctor.
            result = getImplGivenPreviousImpl(classLoader, factoryName,
                 curImplClass, null);
        }

        // step 2.
        List<String> fromServices = getImplNameFromServices(classLoader, factoryName);
        if (fromServices != null) {
            for (String name : fromServices) {
                result = getImplGivenPreviousImpl(classLoader,
                                                  factoryName,
                                                  name,
                                                  result);
            }
        }        

        // step 3.
        if (null != implementations) {
            for (len = (implementations.size() - 1); 0 <= len; len--) {
                curImplClass = (String) implementations.remove(len);
                result = getImplGivenPreviousImpl(classLoader, factoryName,
                     curImplClass, result);
            }
        }

        return result;

    }
    

    /**
     * <p>Perform the logic to get the implementation class for the
     * second step of {@link FactoryFinder#getImplementationInstance(ClassLoader, String, java.util.List)}.</p>
     */
    private List<String> getImplNameFromServices(ClassLoader classLoader,
                                                        String factoryName) {

        // Check for a services definition
        List<String> result = null;
        String resourceName = "META-INF/services/" + factoryName;
        InputStream stream;
        BufferedReader reader = null;
        try {
            Enumeration<URL> e = classLoader.getResources(resourceName);
            while (e.hasMoreElements()) {
                URL url = e.nextElement();
                URLConnection conn = url.openConnection();
                conn.setUseCaches(false);
                stream = conn.getInputStream();
                if (stream != null) {
                    // Deal with systems whose native encoding is possibly
                    // different from the way that the services entry was created
                    try {
                        reader =
                              new BufferedReader(new InputStreamReader(stream,
                                                                       "UTF-8"));
                        if (result == null) {
                            result = new ArrayList<String>(3);
                        }
                        result.add(reader.readLine());
                    } catch (UnsupportedEncodingException uee) {
                        // The DM_DEFAULT_ENCODING warning is acceptable here
                        // because we explicitly *want* to use the Java runtime's
                        // default encoding.
                        reader =
                              new BufferedReader(new InputStreamReader(stream));
                    } finally {
                        if (reader != null) {
                            reader.close();
                            reader = null;
                        }
                        if (stream != null) {
                            stream.close();
                            //noinspection UnusedAssignment
                            stream = null;
                        }
                    }

                }
            }
        } catch (IOException e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE,
                           e.toString(),
                           e);
            }
        } catch (SecurityException e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE,
                           e.toString(),
                           e);
            }
        }
        return result;

    }


    /**
     * <p>Implement the decorator pattern for the factory
     * implementation.</p>
     * <p/>
     * <p>If <code>previousImpl</code> is non-<code>null</code> and the
     * class named by the argument <code>implName</code> has a one arg
     * contstructor of type <code>factoryName</code>, instantiate it,
     * passing previousImpl to the constructor.</p>
     * <p/>
     * <p>Otherwise, we just instantiate and return
     * <code>implName</code>.</p>
     *
     * @param classLoader  the ClassLoader from which to load the class
     * @param factoryName  the fully qualified class name of the factory.
     * @param implName     the fully qualified class name of a class that
     *                     implements the factory.
     * @param previousImpl if non-<code>null</code>, the factory
     *                     instance to be passed to the constructor of the new factory.
     */
    private Object getImplGivenPreviousImpl(ClassLoader classLoader,
                                                   String factoryName,
                                                   String implName,
                                                   Object previousImpl) {
        Class clazz;
        Class factoryClass = null;
        Class[] getCtorArg;
        Object[] newInstanceArgs = new Object[1];
        Constructor ctor;
        Object result = null;

        // if we have a previousImpl and the appropriate one arg ctor.
        if ((null != previousImpl) &&
             (null != (factoryClass = getFactoryClass(factoryName)))) {
            try {
                clazz = Class.forName(implName, false, classLoader);
                getCtorArg = new Class[1];
                getCtorArg[0] = factoryClass;
                ctor = clazz.getConstructor(getCtorArg);
                newInstanceArgs[0] = previousImpl;
                result = ctor.newInstance(newInstanceArgs);
            }
            catch (NoSuchMethodException nsme) {
                // fall through to "zero-arg-ctor" case
                factoryClass = null;
            }
            catch (Exception e) {
                throw new FacesException(implName, e);
            }
        }
        if (null == previousImpl || null == factoryClass) {
            // we have either no previousImpl or no appropriate one arg
            // ctor.
            try {
                clazz = Class.forName(implName, false, classLoader);
                // since this is the hard coded implementation default,
                // there is no preceding implementation, so don't bother
                // with a non-zero-arg ctor.
                result = clazz.newInstance();
            } catch (Exception e) {
                throw new FacesException(implName, e);
            }
        }
        
        if (null != result) {
            InjectionProvider provider = getInjectionProvider();
            if (null != provider) {
                try {
                    provider.inject(result);
                    provider.invokePostConstruct(result);
                }
                catch (Exception e) {
                    throw new FacesException(implName, e);
                }
            } else {
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.log(Level.SEVERE, "Unable to inject {0} because no InjectionProvider can be found. Does this container implement the Mojarra Injection SPI?", result);
                }
            }
        }            

        return result;

    }

    /**
     * @return the <code>java.lang.Class</code> for the argument
     *         factory.
     */
    private Class getFactoryClass(String factoryClassName) {

        return FACTORY_CLASSES.get(factoryClassName);

    }

    // ------------------------------------------------------ Package Private Methods
    Collection<Object> getFactories() {
        return factories.values();
    }

    void addFactory(String factoryName, String implementation) {
        validateFactoryName(factoryName);
        
        Object result = factories.get(factoryName);
        lock.writeLock().lock();
        try {
            if (result instanceof List) {
                TypedCollections.dynamicallyCastList((List) result, String.class).add(0, implementation);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    void releaseFactories() {
        InjectionProvider provider = getInjectionProvider();
        if (null != provider) {
            lock.writeLock().lock();
            try {
                for (Map.Entry entry : factories.entrySet()) {
                    Object curFactory = entry.getValue();
                    // If the current entry is not the injectionProvider itself
                    // and the current entry has a non-null value
                    // and the value is not a string...
                    if (!INJECTION_PROVIDER_KEY.equals(entry.getKey()) &&
                            null != curFactory &&
                            !(curFactory instanceof String)) {
                        try {
                            provider.invokePreDestroy(curFactory);
                        } catch (Exception ex) {
                            if (LOGGER.isLoggable(Level.SEVERE)) {
                                String message = MessageFormat.format("Unable to invoke @PreDestroy annotated methods on {0}.", 
                                        entry.getValue());
                                LOGGER.log(Level.SEVERE, message, ex);
                            }
                        }
                    }
                }
            } finally {
                factories.clear();
                lock.writeLock().unlock();
            }
            
        } else {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, "Unable to call @PreDestroy annotated methods because no InjectionProvider can be found. Does this container implement the Mojarra Injection SPI?");
            }
        }
    }

    InjectionProvider getInjectionProvider() {
        InjectionProvider result = (InjectionProvider) factories.get(INJECTION_PROVIDER_KEY);
        return result;
    }

    void clearInjectionProvider() {
        factories.remove(INJECTION_PROVIDER_KEY);
    }

    Object getFactory(String factoryName) {
        validateFactoryName(factoryName);
        if (factoryName.equals(ServletContextFacesContextFactory.SERVLET_CONTEXT_FINDER_NAME)) {
            return servletContextFinder;
        } else if (factoryName.equals(ServletContextFacesContextFactory.SERVLET_CONTEXT_FINDER_REMOVAL_NAME)) {
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
        // factory hasn't been constructed
        lock.writeLock().lock();
        try {
            // double check the current value.  Another thread
            // may have completed the initialization by the time
            // this thread entered this block
            factoryOrList = factories.get(factoryName);
            if (!(factoryOrList instanceof List)) {
                return factoryOrList;
            }
            savedFactoryNames.put(factoryName, new ArrayList((List) factoryOrList));
            ClassLoader cl = getClassLoader();
            
            Object factory = getImplementationInstance(cl, factoryName, (List) factoryOrList);
            if (factory == null) {
                ResourceBundle rb = LOGGER.getResourceBundle();
                String message = rb.getString("severe.no_factory");
                message = MessageFormat.format(message, factoryName);
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.log(Level.SEVERE, message);
                }
                factory = FactoryFinder.FACTORIES_CACHE.getFallbackFactory(this, factoryName);
                if (null == factory) {
                    message = rb.getString("severe.no_factory_backup_failed");
                    message = MessageFormat.format(message, factoryName);
                    throw new IllegalStateException(message);
                }
            }
            // Record and return the new instance
            factories.put(factoryName, factory);
            return factory;
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    private ClassLoader getClassLoader() throws FacesException {

        // J2EE 1.3 (and later) containers are required to make the
        // web application class loader visible through the context
        // class loader of the current thread.
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) {
            throw new FacesException("getContextClassLoader");
        }
        return (cl);

    }
    
    
    private void validateFactoryName(String factoryName) {

        if (factoryName == null) {
            throw new NullPointerException();
        }
        if (factoryName.equals(ServletContextFacesContextFactory.SERVLET_CONTEXT_FINDER_NAME) ||
            factoryName.equals(ServletContextFacesContextFactory.SERVLET_CONTEXT_FINDER_REMOVAL_NAME)) {
            return;
        }
        
        if (Arrays.binarySearch(FACTORY_NAMES, factoryName) < 0) {
            throw new IllegalArgumentException(factoryName);
        }

    }
    
} // END FactoryFinderInstance
