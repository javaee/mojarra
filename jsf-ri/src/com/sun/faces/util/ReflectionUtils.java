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
 * Copyright 2006 Sun Microsystems Inc. All Rights Reserved
 */

package com.sun.faces.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * <p>Cache system for reflection objects.</p>
 */
public final class ReflectionUtils {


    /**
     * <p>Cache</p>
     */
    private static final Map<ClassLoader, ConcurrentMap<String, MetaData>> REFLECTION_CACHE =
          new WeakHashMap<ClassLoader,ConcurrentMap<String, MetaData>>();


    // ------------------------------------------------------------ Constructors


    private ReflectionUtils() { }


    // ---------------------------------------------------------- Public Methods


    /**
     * <p>Clears the cache for the specified <code>ClassLoader</code>.</p>
     * <p>This method <em>MUST</em> be called when <code>ConfigureListener
     * .contextDestroyed()</code> is called.</p>
     * @param loader the <code>ClassLoader</code> whose associated cache
     *  should be cleared
     */
    public static void clearCache(ClassLoader loader) {

        synchronized(REFLECTION_CACHE) {
            REFLECTION_CACHE.remove(loader);
        }

    }


    /**
     * <p>Returns the <code>Constructor</code> appropriate to the specified
     * Class and parameters.</p>
     * @param clazz the Class of interest
     * @param params the parameters for the constructor of the provided Class
     * @return a Constructor that can be invoked with the specified parameters
     */
    public static Constructor lookupConstructor(
          Class<?> clazz,
          Class<?>... params) {

        ClassLoader loader = Util.getCurrentLoader(clazz);
        if (loader == null) {
            return null;
        }
        
        return getMetaData(loader, clazz).lookupConstructor(params);
        
    }
    
    /**
     * <p>Returns the <code>Method</code> appropriate to the specified
     * Class, method name, and parameters.</p>
     * @param clazz the Class of interest
     * @param methodName the name of the method
     * @param params the parameters for the specified method
     * @return a Method that can be invoked with the specified parameters
     */
    public static Method lookupMethod(
          Class<?> clazz,
          String methodName,
          Class<?>... params) {

        ClassLoader loader = Util.getCurrentLoader(clazz);
        if (loader == null) {
            return null;
        }        
        
        return getMetaData(loader, clazz).lookupMethod(methodName, params);
        
    }


    // --------------------------------------------------------- Private Methods


    /**
     * <p>Return the <code>MetaData</code> for the specified Class.</p>
     * 
     * <p>This will check the cache associated with the specified
     * <code>ClassLoader</code>.  If there is no cache hit, then a new
     * <code>MetaData</code> instance will be created and stored.
     * 
     * @param loader <code>ClassLoader</code>
     * @param clazz the Class of interest
     * @return a <code>MetaData</code> object for the specified Class
     */
    private static MetaData getMetaData(ClassLoader loader, Class<?> clazz) {

        ConcurrentMap<String, MetaData> cache = REFLECTION_CACHE.get(loader);
        if (cache == null) {
            synchronized (REFLECTION_CACHE) {
                cache = REFLECTION_CACHE.get(loader);
                if (cache == null) {
                    cache = new ConcurrentHashMap<String, MetaData>();
                    REFLECTION_CACHE.put(loader, cache);
                }
            }
        }

        MetaData meta = cache.get(clazz.getName());
        if (meta == null) {
            meta = new MetaData(clazz);
            cache.put(clazz.getName(), meta);
        }
       
        return meta;

    }


    /**
     * <p>MetaData contains lookup methods for <code>Constructor</code>s and
     * <code>Method</code>s of a particular Class.
     */
    private static final class MetaData {


        Map<Integer,Constructor> constructors;
        Map<String,HashMap<Integer,Method>> methods;


    // ------------------------------------------------------------ Constructors


        /**
         * <p>Constructs a new <code>MetaData</code> instance for the specified
         * class.</p>
         * @param clazz class to construct a new MetaData instance from.
         */
        public MetaData(Class<?> clazz) {

            Constructor[] ctors = clazz.getConstructors();
            constructors = new HashMap<Integer,Constructor>(ctors.length, 1.0f);
            for (int i = 0, len = ctors.length; i < len; i++) {
                constructors.put(getKey(ctors[i].getParameterTypes()),
                                 ctors[i]);
            }
            Method[] meths = clazz.getMethods();
            methods = new HashMap<String,HashMap<Integer,Method>>(meths.length, 1.0f);
            for (int i = 0, len = meths.length; i < len; i++) {
                String name = meths[i].getName();
                HashMap<Integer,Method> methodsMap = methods.get(name);
                if (methodsMap == null) {
                    methodsMap = new HashMap<Integer,Method>(4, 1.0f);
                    methods.put(name, methodsMap);
                }
                methodsMap.put(getKey(meths[i].getParameterTypes()), meths[i]);
            }

        }


    // ---------------------------------------------------------- Public Methods


        /**
         * <p>Looks up a <code>Constructor</code> based off the specified
         * <code>params</code>.</p>
         * @param params constructor parameters
         * @return the <code>Constructor</code> appropriate to the specified 
         *  parameters or <code>null</code>
         */
        public Constructor lookupConstructor(Class<?>... params) {

            return constructors.get(getKey(params));

        }


        /**
         * <p>Looks up a <code>Method</code> based off the specified method
         * name and <code>params</code>.</p>
         * @param name the name of the <cod>Method</code>
         * @param params the <code>Method</code> parameters
         * @return the <code>Method</code> appropriate to the specified 
         *  name and parameters or <code>null</code>
         */
        public Method lookupMethod(String name, Class<?>... params) {

            Map<Integer,Method> methodsMap = methods.get(name);
            return methodsMap.get(getKey(params));

        }


    // --------------------------------------------------------- Private Methods


        /**
         * Return a hashcode of all the class parameters.
         * @param params the parameters to a <code>Constructor</code> or
         *  a <code>Method</code> instance
         * @return the result of <code>Arrays.deepHashCode</code>
         */
        private static Integer getKey(Class<?>... params) {

            return Arrays.deepHashCode(params);

        }

    }

} // END ReflectionUtils