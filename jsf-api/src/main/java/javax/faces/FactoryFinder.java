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


import com.sun.faces.spi.InjectionProvider;
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
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;


/**
 * <p><strong class="changed_modified_2_0 changed_modified_2_1 changed_modified_2_2">FactoryFinder</strong>
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
 * factory class name, those injectionProvider are used, with the last one taking
 * precedence.</p></li> 

 * <li><p>If there are any JavaServer Faces configuration files bundled
 * into the <code>META-INF</code> directory of any jars on the
 * <code>ServletContext</code>'s resource paths, the
 * <code>factory</code> entries of the given factory class name in those
 * files are used, with the last one taking precedence.</p></li>

 * <li><p>If a <code>META-INF/services/{factory-class-name}</code>
 * resource is visible to the web application class loader for the
 * calling application (typically as a injectionProvider of being present in the
 * manifest of a JAR file), its first line is read and assumed to be the
 * name of the factory implementation class to use.</p></li>

 * <li><p>If none of the above steps yield a match, the JavaServer Faces
 * implementation specific class is used.</p></li>

 * </ul>

 * <p>If any of the injectionProvider found on any of the steps above happen to
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
     * <p>The property name for the
     * {@link javax.faces.lifecycle.ClientWindowFactory} class name.</p>
     * @since 2.2
     */
    public final static String CLIENT_WINDOW_FACTORY =
         "javax.faces.lifecycle.ClientWindowFactory";

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
     * <p class="changed_added_2_2">The property name for the
     * {@link javax.faces.context.FlashFactory} class name.</p>
     * 
     * @since 2.2
     */
    public final static String FLASH_FACTORY =
         "javax.faces.context.FlashFactory";

    /**
     * <p class="changed_added_2_2">The property name for the
     * {@link javax.faces.flow.FlowHandlerFactory} class name.</p>
     * 
     * @since 2.2
     */
    public final static String FLOW_HANDLER_FACTORY =
         "javax.faces.flow.FlowHandlerFactory";

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

    static final CurrentThreadToServletContext FACTORIES_CACHE;

    private static final Logger LOGGER;

    static {
        FACTORIES_CACHE = new CurrentThreadToServletContext();

        LOGGER = Logger.getLogger("javax.faces", "javax.faces.LogStrings");
    }

    // --------------------------------------------------------- Public Methods


    /**
     * <p><span class="changed_modified_2_0">Create</span> (if
     * necessary) and return a per-web-application instance of the
     * appropriate implementation class for the specified JavaServer
     * Faces factory class, based on the discovery algorithm described
     * in the class description.</p>
     *
     * <p class="changed_added_2_0">The standard injectionProvider and wrappers
     * in JSF all implement the interface {@link FacesWrapper}.  If the
     * returned <code>Object</code> is an implementation of one of the
     * standard injectionProvider, it must be legal to cast it to an instance of
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

        FactoryFinderInstance.validateFactoryName(factoryName);

        // Identify the web application class loader
        ClassLoader classLoader = getClassLoader();

        FactoryFinderInstance manager =
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

        FactoryFinderInstance.validateFactoryName(factoryName);

        // Identify the web application class loader
        ClassLoader classLoader = getClassLoader();

        FactoryFinderInstance manager =
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
        synchronized(FACTORIES_CACHE) {
            // Identify the web application class loader
            ClassLoader cl = getClassLoader();

            if (!FACTORIES_CACHE.applicationMap.isEmpty()) {

                FactoryFinderInstance fm = FACTORIES_CACHE.getApplicationFactoryManager(cl);
                InjectionProvider provider = fm.getInjectionProvider();
                if (null != provider) {
                    Collection factories = null;
                    for (Map.Entry<FactoryManagerCacheKey, FactoryFinderInstance> entry : 
                            FACTORIES_CACHE.applicationMap.entrySet()) {
                        factories = entry.getValue().getFactories();
                        for (Object curFactory : factories) {
                            try {
                                provider.invokePreDestroy(curFactory);
                            } catch (Exception ex) {
                                if (LOGGER.isLoggable(Level.SEVERE)) {
                                    String message = MessageFormat.format("Unable to invoke @PreDestroy annotated methods on {0}.", 
                                            curFactory);
                                    LOGGER.log(Level.SEVERE, message, ex);
                                }
                            }
                        }
                    }
                } else {
                    if (LOGGER.isLoggable(Level.SEVERE)) {
                        LOGGER.log(Level.SEVERE, "Unable to call @PreDestroy annotated methods because no InjectionProvider can be found. Does this container implement the Mojarra Injection SPI?");
                    }
                }
            }

            FACTORIES_CACHE.removeApplicationFactoryManager(cl);
        }
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


    private static void reInitializeFactoryManager() {
        FACTORIES_CACHE.resetSpecialInitializationCaseFlags();
    }


    // ----------------------------------------------------------- Inner Classes


    /**
     * Managed the mappings between a web application and its configured
     * injectionProvider.
     */
    static final class CurrentThreadToServletContext {

        private ConcurrentMap<FactoryManagerCacheKey,FactoryFinderInstance> applicationMap =
              new ConcurrentHashMap<FactoryManagerCacheKey, FactoryFinderInstance>();
        private AtomicBoolean logNullFacesContext = new AtomicBoolean(false);
        private AtomicBoolean logNonNullFacesContext = new AtomicBoolean(false);


        // ------------------------------------------------------ Public Methods
        
        Object getFallbackFactory(ClassLoader cl, FactoryFinderInstance brokenFactoryManager,
                String factoryName) {
            Object result = null;
            for (Map.Entry<FactoryManagerCacheKey,FactoryFinderInstance> cur : applicationMap.entrySet()) {
                if (cur.getKey().getClassLoader().equals(cl) && !cur.getValue().equals(brokenFactoryManager)) {
                    result = cur.getValue().getFactory(cl, factoryName);
                    if (null != result) {
                        break;
                    }
                }
            }
            return result;
        }
        
        private FactoryFinderInstance getApplicationFactoryManager(ClassLoader cl) {
            FactoryFinderInstance result = getApplicationFactoryManager(cl, true);
            return result;
        }

        private FactoryFinderInstance getApplicationFactoryManager(ClassLoader cl, boolean create) {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            boolean isSpecialInitializationCase = detectSpecialInitializationCase(facesContext);

            FactoryManagerCacheKey key = new FactoryManagerCacheKey(facesContext,
                    cl, applicationMap);

            FactoryFinderInstance result = applicationMap.get(key);
            FactoryFinderInstance toCopy = null;
            if (result == null && create) {
                boolean createNewFactoryManagerInstance = false;
                
                if (isSpecialInitializationCase) {
                    // We need to obtain a reference to the correct
                    // FactoryFinderInstance.  Iterate through the data structure 
                    // containing all FactoryFinderInstance instances for this VM.
                    FactoryManagerCacheKey curKey;
                    boolean classLoadersMatchButContextsDoNotMatch = false;
                    boolean foundNoMatchInApplicationMap = true;
                    for (Map.Entry<FactoryManagerCacheKey, FactoryFinderInstance> cur : applicationMap.entrySet()) {
                        curKey = cur.getKey();
                        // If the current FactoryFinderInstance is for a
                        // the same ClassLoader as the current ClassLoader...
                        if (curKey.getClassLoader().equals(cl)) {
                            foundNoMatchInApplicationMap = false;
                            // Check the other descriminator for the
                            // key: the context.  

                            // If the context objects of the keys are
                            // both non-null and non-equal, then *do*
                            // create a new FactoryFinderInstance instance.

                            if ((null != key.getContext() && null != curKey.getContext()) &&
                                (!key.getContext().equals(curKey.getContext()))) {
                                classLoadersMatchButContextsDoNotMatch = true;
                                toCopy = cur.getValue();
                            }
                            else {
                                // Otherwise, use this FactoryFinderInstance
                                // instance.
                                result = cur.getValue();
                            }
                            break;
                        }
                    }
                    // We must create a new FactoryFinderInstance if there was no match
                    // at all found in the applicationMap, or a match was found
                    // and the match is safe to use in this web app
                    createNewFactoryManagerInstance = foundNoMatchInApplicationMap ||
                            (null == result && classLoadersMatchButContextsDoNotMatch);
                } else {
                    createNewFactoryManagerInstance = true;
                }
                
                if (createNewFactoryManagerInstance) {
                    FactoryFinderInstance newResult;
                    if (null != toCopy) {
                        newResult = new FactoryFinderInstance(toCopy);
                    } else {
                        newResult = new FactoryFinderInstance();
                    }
                    result = applicationMap.putIfAbsent(key, newResult);
                    result = (null != result) ? result : newResult;
                }
                
            }
            return result;
        }
        
        /**
         * This method is used to detect the following special initialization case.
         * IF no FactoryFinderInstance can be found for key, 
 AND this call to getApplicationFactoryFinderInstance() *does* have a currentKeyrent FacesContext
 BUT a previous call to getApplicationFactoryFinderInstance *did not* have a currentKeyrent FacesContext
         * 
         * @param facesContext the currentKeyrent FacesContext for this request
         * @return true if the currentKeyrent execution falls into the special initialization case.
         */
        private boolean detectSpecialInitializationCase(FacesContext facesContext) {
            boolean result = false;
            if (null == facesContext) {
                logNullFacesContext.compareAndSet(false, true);
            } else {
                logNonNullFacesContext.compareAndSet(false, true);
            }
            result = logNullFacesContext.get() && logNonNullFacesContext.get();
            
            return result;
        }


        public void removeApplicationFactoryManager(ClassLoader cl) {
            FactoryFinderInstance fm = this.getApplicationFactoryManager(cl, false);
            if (null != fm) {
                fm.clearInjectionProvider();
            }
            
            FacesContext facesContext = FacesContext.getCurrentInstance();
            boolean isSpecialInitializationCase = detectSpecialInitializationCase(facesContext);

            FactoryManagerCacheKey key = new FactoryManagerCacheKey(facesContext,
                    cl, applicationMap);
            
            applicationMap.remove(key);
            if (isSpecialInitializationCase) {
                logNullFacesContext.set(false);
                logNonNullFacesContext.set(false);
            }

        }
        
        public void resetSpecialInitializationCaseFlags() {
            logNullFacesContext.set(false);
            logNonNullFacesContext.set(false);
        }

    } // END FactoryCache

    private static final class FactoryManagerCacheKey {
        // The ClassLoader that is active the first time this key
        // is created.  At startup time, this is assumed to be the 
        // web app ClassLoader
        private ClassLoader cl;
        // I marker that disambiguates the case when multiple
        // web apps have the same web app ClassLoader but different
        // ServletContext instances.  
        private Long marker;
        // The ServletContext corresponding to this marker/ClassLoader pair.
        private Object context;

        private static final String MARKER_KEY = FactoryFinder.class.getName() + "." +
                FactoryManagerCacheKey.class.getSimpleName();
        private static final String INIT_TIME_CL_KEY = MARKER_KEY + ".InitTimeCLKey";

        // <editor-fold defaultstate="collapsed" desc="Constructors and helpers">
        public FactoryManagerCacheKey(FacesContext facesContext, ClassLoader cl,
                Map<FactoryManagerCacheKey,FactoryFinderInstance> factoryMap) {
            ExternalContext extContext = (null != facesContext) ? facesContext.getExternalContext()
                    : null;
            Object servletContext = (null != extContext) ? extContext.getContext() : null;

            if (null == facesContext || null == extContext || null == servletContext) {
                initFromFactoryMap(cl, factoryMap);
            } else {
                initFromAppMap(extContext, cl);
            } 
        }
        
        private void initFromFactoryMap(ClassLoader cl,
                Map<FactoryManagerCacheKey,FactoryFinderInstance> factoryMap) {
            // We don't have a FacesContext.
            // Our only recourse is to inspect the keys of the
            // factoryMap and see if any of them has a classloader
            // equal to our argument cl.
            Set<FactoryManagerCacheKey> keys = factoryMap.keySet();
            FactoryManagerCacheKey match = null;
            
            if (keys.isEmpty()) {
                this.cl = cl;
                this.marker = new Long(System.currentTimeMillis());
            } else {
            
                boolean found = false;
                // For each entry in the factoryMap's keySet...
                for (FactoryManagerCacheKey currentKey : keys) {
                    ClassLoader curCL = cl;
                    // For each ClassLoader in the hierarchy starting 
                    // with the argument ClassLoader...
                    while (!found && null != curCL) {
                        // if the ClassLoader at this level in the hierarchy
                        // is equal to the argument ClassLoader, consider it a match.
                        found = curCL.equals(currentKey.cl);
                        // If it's not a match, try the parent in the ClassLoader
                        // hierarchy.
                        if (!found) {
                            curCL = curCL.getParent();
                        }
                    }
                    // Keep searching for another match to detect an unsupported
                    // deployment scenario.
                    if (found) {
                        if (null != currentKey && null != match) {
                            LOGGER.log(Level.WARNING, "Multiple JSF Applications found on same ClassLoader.  Unable to safely determine which FactoryManager instance to use. Defaulting to first match.");
                            break;
                        }
                        match = currentKey;
                        this.cl = curCL;
                    }
                }
                if (null != match) {
                    this.marker = match.marker;
                    this.context = match.context;
                }
            }
            
        }
        
        private void initFromAppMap(ExternalContext extContext, ClassLoader cl) {
            Map<String, Object> appMap = extContext.getApplicationMap();
            
            Long val = (Long) appMap.get(MARKER_KEY);
            if (null == val) {
                this.marker = new Long(System.currentTimeMillis());
                appMap.put(MARKER_KEY, marker);
                
                // If we needed to create a marker, assume that the
                // argument CL is safe to treat as the web app
                // ClassLoader.  This assumption allows us 
                // to bypass the ClassLoader resolution algorithm
                // in resolveToFirstTimeUsedClassLoader() in all cases
                // except when the TCCL has been replaced.
                appMap.put(INIT_TIME_CL_KEY, new Integer(System.identityHashCode(cl)));
                
            } else {
                this.marker = val;
            }
            this.cl = resolveToFirstTimeUsedClassLoader(cl, extContext);
            this.context = extContext.getContext();
        }
        
       /*
        * Resolve the argument ClassLoader to be the ClassLoader that 
        * was passed in to the ctor the first time a FactoryManagerCacheKey
        * was created for this web app.  
        */

        private ClassLoader resolveToFirstTimeUsedClassLoader(ClassLoader toResolve, ExternalContext extContext) {
            ClassLoader curCL = toResolve;
            ClassLoader resolved = null;
            Map<String, Object> appMap = extContext.getApplicationMap();
            
            // See if the argument curCL already is the web app class loader
            Integer webAppCLHashCode = (Integer) appMap.get(INIT_TIME_CL_KEY);
            boolean found = false;
            if (null != webAppCLHashCode) {
                int toResolveHashCode = System.identityHashCode(curCL);
                while (!found && null != curCL) {
                    found = (toResolveHashCode == webAppCLHashCode);
                    if (!found) {
                        curCL = curCL.getParent();
                        toResolveHashCode = System.identityHashCode(curCL);
                    }
                }
            }
            resolved = found ? curCL : toResolve;
            
            return resolved;
        }
        
        // </editor-fold>
        
        public ClassLoader getClassLoader() {
            return cl;
        }
        
        public Object getContext() {
            return context;
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
            if (this.marker != other.marker && (this.marker == null || !this.marker.equals(other.marker))) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 97 * hash + (this.cl != null ? this.cl.hashCode() : 0);
            hash = 97 * hash + (this.marker != null ? this.marker.hashCode() : 0);
            return hash;
        }


        

    }


}
