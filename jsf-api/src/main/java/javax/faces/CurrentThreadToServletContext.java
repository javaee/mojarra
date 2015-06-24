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

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

// ----------------------------------------------------------- Inner Classes
final class CurrentThreadToServletContext {
    ConcurrentMap<FactoryManagerCacheKey, FactoryFinderInstance> applicationMap = new ConcurrentHashMap<FactoryManagerCacheKey, FactoryFinderInstance>();
    private AtomicBoolean logNullFacesContext = new AtomicBoolean(false);
    private AtomicBoolean logNonNullFacesContext = new AtomicBoolean(false);
    // Bug 20458755: This instance provides a method to look up the current FacesContext
    // that bypasses the additional check for the InitFacesContext introduced
    // by the fix for 20458755
    private final ServletContextFacesContextFactory servletContextFacesContextFactory;
    
    private static final Logger LOGGER;
    
    static {
        LOGGER = Logger.getLogger("javax.faces", "javax.faces.LogStrings");
    }

    CurrentThreadToServletContext() {
        servletContextFacesContextFactory = new ServletContextFacesContextFactory();
    }
    
    // ------------------------------------------------------ Public Methods
    Object getFallbackFactory(FactoryFinderInstance brokenFactoryManager, String factoryName) {
        Object result = null;
        ClassLoader cl = getClassLoader();        
        for (Map.Entry<FactoryManagerCacheKey, FactoryFinderInstance> cur : applicationMap.entrySet()) {
            if (cur.getKey().getClassLoader().equals(cl) && !cur.getValue().equals(brokenFactoryManager)) {
                result = cur.getValue().getFactory(factoryName);
                if (null != result) {
                    break;
                }
            }
        }
        return result;
    }

    FactoryFinderInstance getApplicationFactoryManager() {
        ClassLoader cl = getClassLoader();
        FactoryFinderInstance result = getApplicationFactoryManager(cl, true);
        return result;
    }
    
    FactoryFinderInstance getApplicationFactoryManager(boolean create) {
        ClassLoader cl = getClassLoader();
        FactoryFinderInstance result = getApplicationFactoryManager(cl, create);
        return result;
    }
    
    private FactoryFinderInstance getApplicationFactoryManager(ClassLoader cl, boolean create) {
        FacesContext facesContext = servletContextFacesContextFactory.getFacesContextWithoutServletContextLookup();
        boolean isSpecialInitializationCase = detectSpecialInitializationCase(facesContext);
        FactoryManagerCacheKey key = new FactoryManagerCacheKey(facesContext, cl, applicationMap);
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
                        if ((null != key.getContext() && null != curKey.getContext()) && (!key.getContext().equals(curKey.getContext()))) {
                            classLoadersMatchButContextsDoNotMatch = true;
                            toCopy = cur.getValue();
                        } else {
                            // Otherwise, use this FactoryFinderInstance
                            // instance.
                            result = cur.getValue();
                        }
                        break;
                    }
                }
                // We must create a new FactoryFinderInstance if there was no matchingKey
                // at all found in the applicationMap, or a matchingKey was found
                // and the matchingKey is safe to use in this web app
                createNewFactoryManagerInstance = foundNoMatchInApplicationMap || (null == result && classLoadersMatchButContextsDoNotMatch);
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
    
    /*
     * Uses the FactoryManagerCacheKey system to find the ServletContext 
     * associated with the current ClassLoader, if any.
     */
    Object getServletContextForCurrentClassLoader() {
        Object result;
        FactoryManagerCacheKey key = new FactoryManagerCacheKey(null, getClassLoader(), applicationMap);
        result = key.getContext();
        
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

    void removeApplicationFactoryManager() {
        ClassLoader cl = getClassLoader();
        FactoryFinderInstance fm = this.getApplicationFactoryManager(cl, false);
        if (null != fm) {
            fm.clearInjectionProvider();
        }
        FacesContext facesContext = servletContextFacesContextFactory.getFacesContextWithoutServletContextLookup();
        boolean isSpecialInitializationCase = detectSpecialInitializationCase(facesContext);
        FactoryManagerCacheKey key = new FactoryManagerCacheKey(facesContext, cl, applicationMap);
        applicationMap.remove(key);
        if (isSpecialInitializationCase) {
            logNullFacesContext.set(false);
            logNonNullFacesContext.set(false);
        }
    }

    void resetSpecialInitializationCaseFlags() {
        logNullFacesContext.set(false);
        logNonNullFacesContext.set(false);
    }
    
    /**
     * <p>Identify and return the class loader that is associated with the
     * calling web application.</p>
     *
     * @throws FacesException if the web application class loader
     *                        cannot be identified
     */
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
        FactoryManagerCacheKey(FacesContext facesContext, ClassLoader cl,
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
            FactoryManagerCacheKey matchingKey = null;
            
            if (keys.isEmpty()) {
                this.cl = cl;
                this.marker = new Long(System.currentTimeMillis());
            } else {
            
                // For each entry in the factoryMap's keySet...
                for (FactoryManagerCacheKey currentKey : keys) {
                    assert(null != currentKey.cl);
                    ClassLoader matchingCL = findMatchConsideringParentClassLoader(cl, currentKey.cl);
                    // If there is a match...
                    if (null != matchingCL) {
                        // If the match was found on a previous iteration...
                        if (null != matchingKey) {
                            LOGGER.log(Level.WARNING, "Multiple JSF Applications found on same ClassLoader.  Unable to safely determine which FactoryManager instance to use. Defaulting to first match.");
                            break;
                        }
                        matchingKey = currentKey;
                        this.cl = matchingCL;
                    }
                }
                if (null != matchingKey) {
                    this.marker = matchingKey.marker;
                    this.context = matchingKey.context;
                }
            }
            
        }

        private ClassLoader findMatchConsideringParentClassLoader(ClassLoader argumentCL, ClassLoader currentKeyCL) {
            ClassLoader curCL = argumentCL;
            // For each ClassLoader in the hierarchy starting 
            // with the argument ClassLoader...
            while (null != curCL) {
                // if the ClassLoader at this level in the hierarchy
                // is equal to the argument ClassLoader, consider it a matchingKey.
                if (curCL.equals(currentKeyCL)) {
                    return curCL;
                } else {
                    // If it's not a matchingKey, try the parent in the ClassLoader
                    // hierarchy.
                    curCL = curCL.getParent();
                } 
            }
            return null;
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
        
        ClassLoader getClassLoader() {
            return cl;
        }
        
        Object getContext() {
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
    
    

} // END FactoryCache
