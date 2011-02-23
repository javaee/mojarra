/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
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


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.Future;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLConnection;
import java.util.Set;
import javax.faces.context.FacesContext;


/**
 * <p><strong class="changed_modified_2_0 changed_modified_2_1">FactoryFinder</strong>
 * implements the standard discovery algorithm for all factory objects
 * specified in the JavaServer Faces APIs.  For a given factory class
 * name, a corresponding implementation class is searched for based on
 * the following algorithm.  Items are listed in order of decreasing
 * search precedence:</p> 

 * <ul> 

 * <li><p>If the JavaServer Faces configuration file bundled into the
 * <code>WEB-INF</code> directory of the webapp contains a
 * <code>factory</code> entry of the given factory class name, that
 * factory is used.<p></li>

 * <li><p>If the JavaServer Faces configuration files named by the
 * <code>javax.faces.CONFIG_FILES</code> <code>ServletContext</code> init
 * parameter contain any <code>factory</code> entries of the given
 * factory class name, those factories are used, with the last one taking
 * precedence.</p></li> 

 * <li><p>If there are any JavaServer Faces configuration files bundled
 * into the <code>META-INF</code> directory of any jars on the
 * <code>ServletContext</code>'s resource paths, the
 * <code>factory</code> entries of the given factory class name in those
 * files are used, with the last one taking precedence.</p></li>

 * <li><p>If a <code>META-INF/services/{factory-class-name}</code>
 * resource is visible to the web application class loader for the
 * calling application (typically as a result of being present in the
 * manifest of a JAR file), its first line is read and assumed to be the
 * name of the factory implementation class to use.</p></li>

 * <li><p>If none of the above steps yield a match, the JavaServer Faces
 * implementation specific class is used.</p></li>

 * </ul>

 * <p>If any of the factories found on any of the steps above happen to
 * have a one-argument constructor, with argument the type being the
 * abstract factory class, that constructor is invoked, and the previous
 * match is passed to the constructor.  For example, say the container
 * vendor provided an implementation of {@link
 * javax.faces.context.FacesContextFactory}, and identified it in
 * <code>META-INF/services/javax.faces.context.FacesContextFactory</code>
 * in a jar on the webapp ClassLoader.  Also say this implementation
 * provided by the container vendor had a one argument constructor that
 * took a <code>FacesContextFactory</code> instance.  The
 * <code>FactoryFinder</code> system would call that one-argument
 * constructor, passing the implementation of
 * <code>FacesContextFactory</code> provided by the JavaServer Faces
 * implementation.</p>

 * <p>If a Factory implementation does not provide a proper one-argument
 * constructor, it must provide a zero-arguments constructor in order to
 * be successfully instantiated.</p>

 * <p>Once the name of the factory implementation class is located, the
 * web application class loader for the calling application is requested
 * to load this class, and a corresponding instance of the class will be
 * created.  A side effect of this rule is that each web application
 * will receive its own instance of each factory class, whether the
 * JavaServer Faces implementation is included within the web
 * application or is made visible through the container's facilities for
 * shared libraries.</p>
 */

public final class FactoryFinder {

    // ----------------------------------------------------------- Constructors


    /**
     * Package-private constructor to disable instantiation of this class.
     */
    FactoryFinder() {
    }

    // ----------------------------------------------------- Manifest Constants


    /**
     * <p>The property name for the
     * {@link javax.faces.application.ApplicationFactory} class name.</p>
     */
    public final static String APPLICATION_FACTORY =
         "javax.faces.application.ApplicationFactory";

    /**
     * <p class="changed_added_2_0">The property name for the {@link
     * javax.faces.context.ExceptionHandlerFactory} class name.</p>
     */
    public final static String EXCEPTION_HANDLER_FACTORY =
         "javax.faces.context.ExceptionHandlerFactory";

    /**
     * <p class="changed_added_2_0">The property name for the {@link
     * javax.faces.context.ExternalContextFactory} class name.</p>
     */
    public final static String EXTERNAL_CONTEXT_FACTORY =
         "javax.faces.context.ExternalContextFactory";

    /**
     * <p>The property name for the
     * {@link javax.faces.context.FacesContextFactory} class name.</p>
     */
    public final static String FACES_CONTEXT_FACTORY =
         "javax.faces.context.FacesContextFactory";

    /**
     * <p class="changed_added_2_1">The property name for the
     * {@link javax.faces.view.facelets.FaceletCacheFactory} class name.</p>
     *
     * @since 2.1
     */
    public final static String FACELET_CACHE_FACTORY =
         "javax.faces.view.facelets.FaceletCacheFactory";

    /**
     * <p class="changed_added_2_0">The property name for the {@link
     * javax.faces.context.PartialViewContextFactory} class name.</p>
     */
    public final static String PARTIAL_VIEW_CONTEXT_FACTORY =
          "javax.faces.context.PartialViewContextFactory";

    /**
     * <p class="changed_added_2_0">The property name for the {@link
     * javax.faces.component.visit.VisitContextFactory} class name.</p>
     */
    public final static String VISIT_CONTEXT_FACTORY =
         "javax.faces.component.visit.VisitContextFactory";

    /**
     * <p>The property name for the
     * {@link javax.faces.lifecycle.LifecycleFactory} class name.</p>
     */
    public final static String LIFECYCLE_FACTORY =
         "javax.faces.lifecycle.LifecycleFactory";

    /**
     * <p>The property name for the
     * {@link javax.faces.render.RenderKitFactory} class name.</p>
     */
    public final static String RENDER_KIT_FACTORY =
         "javax.faces.render.RenderKitFactory";

    /**
     * <p class="changed_added_2_0">The property name for the {@link
     * javax.faces.view.ViewDeclarationLanguage} class name.</p>
     */
    public final static String VIEW_DECLARATION_LANGUAGE_FACTORY =
         "javax.faces.view.ViewDeclarationLanguageFactory";

    /**
     * <p class="changed_added_2_0">The property name for the {@link
     * javax.faces.view.facelets.TagHandlerDelegate} class name.</p>
     */
    public final static String TAG_HANDLER_DELEGATE_FACTORY =
         "javax.faces.view.facelets.TagHandlerDelegateFactory";

    // ------------------------------------------------------- Static Variables

    private static final FactoryManagerCache FACTORIES_CACHE =
          new FactoryManagerCache();


    /**
     * <p>The set of JavaServer Faces factory classes for which the factory
     * discovery mechanism is supported.  The entries in this list must be 
     * alphabetically ordered according to the entire string of the
     * *value* of each of the literals, not just
     * the last part of the literal!</p>
     */
    private static final String[] FACTORY_NAMES = {
         APPLICATION_FACTORY,
         VISIT_CONTEXT_FACTORY,
         EXCEPTION_HANDLER_FACTORY,
         EXTERNAL_CONTEXT_FACTORY,
         FACES_CONTEXT_FACTORY,
         LIFECYCLE_FACTORY,
         VIEW_DECLARATION_LANGUAGE_FACTORY,
         PARTIAL_VIEW_CONTEXT_FACTORY,
         RENDER_KIT_FACTORY,
         FACELET_CACHE_FACTORY,
         TAG_HANDLER_DELEGATE_FACTORY
    
    };

    /**
     * <p>Map of Class instances for the our factory names.</p>
     */
    private static Map<String, Class> factoryClasses = null;

    private static final Logger LOGGER =
         Logger.getLogger("javax.faces", "javax.faces.LogStrings");

    // Ensure the factory names are sorted.
    //
    static {
        Arrays.sort(FACTORY_NAMES);
    }


    // --------------------------------------------------------- Public Methods


    /**
     * <p><span class="changed_modified_2_0">Create</span> (if
     * necessary) and return a per-web-application instance of the
     * appropriate implementation class for the specified JavaServer
     * Faces factory class, based on the discovery algorithm described
     * in the class description.</p>
     *
     * <p class="changed_added_2_0">The standard factories and wrappers
     * in JSF all implement the interface {@link FacesWrapper}.  If the
     * returned <code>Object</code> is an implementation of one of the
     * standard factories, it must be legal to cast it to an instance of
     * <code>FacesWrapper</code> and call {@link
     * FacesWrapper#getWrapped} on the instance.</p>
     *
     * @param factoryName Fully qualified name of the JavaServer Faces factory
     *                    for which an implementation instance is requested
     * @throws FacesException           if the web application class loader
     *                                  cannot be identified
     * @throws FacesException           if an instance of the configured factory
     *                                  implementation class cannot be loaded
     * @throws FacesException           if an instance of the configured factory
     *                                  implementation class cannot be instantiated
     * @throws IllegalArgumentException if <code>factoryName</code> does not
     *                                  identify a standard JavaServer Faces factory name
     * @throws IllegalStateException    if there is no configured factory
     *                                  implementation class for the specified factory name
     * @throws NullPointerException     if <code>factoryname</code>
     *                                  is null
     */
    public static Object getFactory(String factoryName)
         throws FacesException {

        validateFactoryName(factoryName);

        // Identify the web application class loader
        ClassLoader classLoader = getClassLoader();

        FactoryManager manager =
              FACTORIES_CACHE.getApplicationFactoryManager(classLoader);
        return manager.getFactory(classLoader, factoryName);

    }

    /**
     * <p>This method will store the argument
     * <code>factoryName/implName</code> mapping in such a way that
     * {@link #getFactory} will find this mapping when searching for a
     * match.</p>
     * <p/>
     * <p>This method has no effect if <code>getFactory()</code> has
     * already been called looking for a factory for this
     * <code>factoryName</code>.</p>
     * <p/>
     * <p>This method can be used by implementations to store a factory
     * mapping while parsing the Faces configuration file</p>
     *
     * @throws IllegalArgumentException if <code>factoryName</code> does not
     *                                  identify a standard JavaServer Faces factory name
     * @throws NullPointerException     if <code>factoryname</code>
     *                                  is null
     */
    public static void setFactory(String factoryName,
                                  String implName) {

        validateFactoryName(factoryName);

        // Identify the web application class loader
        ClassLoader classLoader = getClassLoader();

        FactoryManager manager =
              FACTORIES_CACHE.getApplicationFactoryManager(classLoader);
        manager.addFactory(factoryName, implName);

    }


    /**
     * <p><span class="changed_modified_2_0">Release</span> any
     * references to factory instances associated with the class loader
     * for the calling web application.  <span
     * class="changed_modified_2_0">This method must be called during of
     * web application shutdown.</span></p>
     *
     * @throws FacesException if the web application class loader
     *                        cannot be identified
     */
    public static void releaseFactories() throws FacesException {

        // Identify the web application class loader
        ClassLoader cl = getClassLoader();

        FACTORIES_CACHE.removeApplicationFactoryManager(cl);

    }


    // -------------------------------------------------------- Private Methods


    /**
     * <p>Identify and return the class loader that is associated with the
     * calling web application.</p>
     *
     * @throws FacesException if the web application class loader
     *                        cannot be identified
     */
    private static ClassLoader getClassLoader() throws FacesException {

        // J2EE 1.3 (and later) containers are required to make the
        // web application class loader visible through the context
        // class loader of the current thread.
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) {
            throw new FacesException("getContextClassLoader");
        }
        return (cl);

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
     * of a class implementing <code>factoryName</code>.  If the current
     * element has a one arg constructor of the type for
     * <code>factoryName</code>, instantiate it, passing the
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
    private static Object getImplementationInstance(ClassLoader classLoader,
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
    private static List<String> getImplNameFromServices(ClassLoader classLoader,
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
    private static Object getImplGivenPreviousImpl(ClassLoader classLoader,
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
        return result;

    }


    /**
     * @return the <code>java.lang.Class</code> for the argument
     *         factory.
     */
    private static Class getFactoryClass(String factoryClassName) {

        if (null == factoryClasses) {
            factoryClasses = new HashMap<String, Class>(FACTORY_NAMES.length);
            factoryClasses.put(APPLICATION_FACTORY,
                 javax.faces.application.ApplicationFactory.class);
            factoryClasses.put(EXCEPTION_HANDLER_FACTORY,
                 javax.faces.context.ExceptionHandlerFactory.class);
            factoryClasses.put(EXTERNAL_CONTEXT_FACTORY,
                 javax.faces.context.ExternalContextFactory.class);
            factoryClasses.put(FACES_CONTEXT_FACTORY,
                 javax.faces.context.FacesContextFactory.class);
            factoryClasses.put(VISIT_CONTEXT_FACTORY,
                 javax.faces.component.visit.VisitContextFactory.class);
            factoryClasses.put(LIFECYCLE_FACTORY,
                 javax.faces.lifecycle.LifecycleFactory.class);
            factoryClasses.put(PARTIAL_VIEW_CONTEXT_FACTORY,
                 javax.faces.context.PartialViewContextFactory.class);
            factoryClasses.put(RENDER_KIT_FACTORY,
                 javax.faces.render.RenderKitFactory.class);
            factoryClasses.put(VIEW_DECLARATION_LANGUAGE_FACTORY,
                 javax.faces.view.ViewDeclarationLanguageFactory.class);
            factoryClasses.put(TAG_HANDLER_DELEGATE_FACTORY,
                 javax.faces.view.facelets.TagHandlerDelegateFactory.class);
        }
        return factoryClasses.get(factoryClassName);

    }


    /**
     * Ensure the provided factory name is valid.
     */
    private static void validateFactoryName(String factoryName) {

        if (factoryName == null) {
            throw new NullPointerException();
        }
        if (Arrays.binarySearch(FACTORY_NAMES, factoryName) < 0) {
            throw new IllegalArgumentException(factoryName);
        }

    }


    // ----------------------------------------------------------- Inner Classes


    /**
     * Managed the mappings between a web application and its configured
     * factories.
     */
    private static final class FactoryManagerCache {

        private ConcurrentMap<FactoryManagerCacheKey,Future<FactoryManager>> applicationMap =
              new ConcurrentHashMap<FactoryManagerCacheKey, Future<FactoryManager>>();


        // ------------------------------------------------------ Public Methods


        private FactoryManager getApplicationFactoryManager(ClassLoader cl) {

            FactoryManagerCacheKey key = new FactoryManagerCacheKey(cl, applicationMap);

            while (true) {
                Future<FactoryManager> factories = applicationMap.get(key);
                if (factories == null) {
                    Callable<FactoryManager> callable =
                          new Callable<FactoryManager>() {
                              public FactoryManager call()
                                    throws Exception {
                                  return new FactoryManager();
                              }
                          };

                    FutureTask<FactoryManager> ft =
                          new FutureTask<FactoryManager>(callable);
                    factories = applicationMap.putIfAbsent(key, ft);
                    if (factories == null) {
                        factories = ft;
                        ft.run();
                    }
                }

                try {
                    return factories.get();
                } catch (CancellationException ce) {
                    if (LOGGER.isLoggable(Level.FINEST)) {
                        LOGGER.log(Level.FINEST,
                                   ce.toString(),
                                   ce);
                    }
                    applicationMap.remove(key);
                } catch (InterruptedException ie) {
                    if (LOGGER.isLoggable(Level.FINEST)) {
                        LOGGER.log(Level.FINEST,
                                   ie.toString(),
                                   ie);
                    }
                    applicationMap.remove(key);
                } catch (ExecutionException ee) {
                    throw new FacesException(ee);
                }

            }

        }


        public void removeApplicationFactoryManager(ClassLoader cl) {
            FactoryManagerCacheKey key = new FactoryManagerCacheKey(cl, applicationMap);

            applicationMap.remove(key);

        }

    } // END FactoryCache

    private static final class FactoryManagerCacheKey {
        private ClassLoader cl;
        private Object context;

        private static final String KEY = FactoryFinder.class.getName() + "." +
                FactoryManagerCacheKey.class.getSimpleName();

        public FactoryManagerCacheKey(ClassLoader cl,
                Map<FactoryManagerCacheKey,Future<FactoryManager>> factoryMap) {
            this.cl = cl;
            FacesContext facesContext = FacesContext.getCurrentInstance();
            if (null != facesContext) {
                Map<String, Object> appMap = facesContext.getExternalContext().getApplicationMap();
                Object val = appMap.get(KEY);
                if (null == val) {
                    context = new Long(System.currentTimeMillis());
                    appMap.put(KEY, context);
                } else {
                    context = val;
                }
            } else {
                // We don't have a FacesContext.
                // Our only recourse is to inspect the keys of the
                // factoryMap and see if any of them has a classloader
                // equal to our argument cl.
                Set<FactoryManagerCacheKey> keys = factoryMap.keySet();
                FactoryManagerCacheKey match = null;
                for (FactoryManagerCacheKey cur : keys) {
                    if (this.cl.equals(cur.cl)) {
                        match = cur;
                        if (null != match) {
                            LOGGER.log(Level.WARNING, "Multiple JSF Applications found on same ClassLoader.  Unable to safely determine which FactoryManager instance to use. Defaulting to first match.");
                        }
                    }
                }
                if (null != match) {
                    this.context = match.context;
                }
            }
        }
        
        private FactoryManagerCacheKey() {}

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final FactoryManagerCacheKey other = (FactoryManagerCacheKey) obj;
            if (this.cl != other.cl && (this.cl == null || !this.cl.equals(other.cl))) {
                return false;
            }
            if (this.context != other.context && (this.context == null || !this.context.equals(other.context))) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 97 * hash + (this.cl != null ? this.cl.hashCode() : 0);
            hash = 97 * hash + (this.context != null ? this.context.hashCode() : 0);
            return hash;
        }


        

    }


    /**
     * Maintains the factories for a single web application.
     */
    private static final class FactoryManager {

        private final Map<String,Object> factories;
        private final ReentrantReadWriteLock lock;


        // -------------------------------------------------------- Consturctors


        public FactoryManager() {
            factories = new HashMap<String,Object>();
            for (String name : FACTORY_NAMES) {
                factories.put(name, new ArrayList(4));
            }
            lock = new ReentrantReadWriteLock(true);
        }


        // ------------------------------------------------------ Public Methods


        public void addFactory(String factoryName, String implementation) {

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


        public Object getFactory(ClassLoader cl, String factoryName) {

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
                Object factory = getImplementationInstance(cl,
                                                           factoryName,
                                                           (List) factoryOrList);

                if (factory == null) {
                    ResourceBundle rb = LOGGER.getResourceBundle();
                    String message = rb.getString("severe.no_factory");
                    message = MessageFormat.format(message, factoryName);
                    throw new IllegalStateException(message);
                }

                // Record and return the new instance
                factories.put(factoryName, factory);
                return (factory);
            } finally {
                lock.writeLock().unlock();
            }
        }

    } // END FactoryManager


}
