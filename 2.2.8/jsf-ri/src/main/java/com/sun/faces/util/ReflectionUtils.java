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

package com.sun.faces.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.beans.PropertyDescriptor;
import java.beans.Introspector;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;

/**
 * <p>A set of utility methods to make working with
 * Classes and Reflection a little easier.</p>
 */
public final class ReflectionUtils {

    private static final Logger LOGGER = FacesLogger.APPLICATION.getLogger();

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
    public static synchronized void clearCache(ClassLoader loader) {

            REFLECTION_CACHE.remove(loader);

    }


    public static synchronized void initCache(ClassLoader loader) {

        if (REFLECTION_CACHE.get(loader) == null) {
            REFLECTION_CACHE.put(loader,
                                 new ConcurrentHashMap<String,MetaData>());
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


    /**
     * <p>Constructs a new object instance based off the
     * provided class name.</p>
     * @param className the class of the object to instantiate
     * @return a new instances of said class
     * @throws InstantiationException if the class cannot be instantiated
     * @throws IllegalAccessException if there is a security violation
     */
    public static Object newInstance(String className)
    throws InstantiationException, IllegalAccessException {

        ClassLoader loader = Util.getCurrentLoader(null);
        if (loader == null) {
            return null;
        }

        return getMetaData(loader, className).lookupClass().newInstance();
        
    }


    /**
     * <p>Obtain a <code>Class</code> instance based on the provided
     * String name.</p>
     * @param className the class to look up
     * @return the <code>Class</code> corresponding to <code>className</code>
     */
    public static Class<?> lookupClass(String className) {

        ClassLoader loader = Util.getCurrentLoader(null);
        if (loader == null) {
            return null;
        }

        return getMetaData(loader, className).lookupClass();
        
    }


    /**
     * @param className the fully qualified class name
     * @param propertyName a JavaBeans property name
     * @return a method suitable for setting a JavaBeans property, or
     *  <code>null</code> if the property doesn't exist or is readonly.
     */
    public static Method lookupWriteMethod(String className, String propertyName) {

        ClassLoader loader = Util.getCurrentLoader(null);
        if (loader == null) {
            return null;
        }

        return getMetaData(loader, className).lookupWriteMethod(propertyName);

    }


    /**
     * @param className the fully qualified class name
     * @param propertyName a JavaBeans property name
     * @return a method suitable for obtaining the value of a JavaBeans property,
     *  or <code>null</code> if the property doesn't exist or can't be read.
     */
    public static Method lookupReadMethod(String className, String propertyName) {


        ClassLoader loader = Util.getCurrentLoader(null);
        if (loader == null) {
            return null;
        }

        return getMetaData(loader, className).lookupReadMethod(propertyName);

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
            initCache(loader);
            cache = REFLECTION_CACHE.get(loader);
        }

        MetaData meta = cache.get(clazz.getName());
        if (meta == null) {
            meta = new MetaData(clazz);
            cache.put(clazz.getName(), meta);
        }
       
        return meta;

    }


    /**
     * <p>Return the <code>MetaData</code> for the specified className.</p>
     *
     * <p>This will check the cache associated with the specified
     * <code>ClassLoader</code>.  If there is no cache hit, then a new
     * <code>MetaData</code> instance will be created and stored.
     *
     * @param loader <code>ClassLoader</code>
     * @param className the class of interest
     * @return a <code>MetaData</code> object for the specified Class
     */
    private static MetaData getMetaData(ClassLoader loader, String className) {

        ConcurrentMap<String, MetaData> cache = REFLECTION_CACHE.get(loader);

        if (cache == null) {
            initCache(loader);
            cache = REFLECTION_CACHE.get(loader);
        }

        MetaData meta = cache.get(className);
        if (meta == null) {
            try {
                Class<?> clazz = Util.loadClass(className, cache);
                meta = new MetaData(clazz);
                cache.put(className, meta);
            } catch (ClassNotFoundException cnfe) {
                return null;
            }
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
        Map<String,HashMap<Integer,Method>> declaredMethods;
        Map<String, PropertyDescriptor> propertyDescriptors;
        Class<?> clazz;


    // ------------------------------------------------------------ Constructors


        /**
         * <p>Constructs a new <code>MetaData</code> instance for the specified
         * class.</p>
         * @param clazz class to construct a new MetaData instance from.
         */
        public MetaData(Class<?> clazz) {

            String name;
            this.clazz = clazz;
            Constructor[] ctors = clazz.getConstructors();
            constructors = new HashMap<Integer,Constructor>(ctors.length, 1.0f);
            for (int i = 0, len = ctors.length; i < len; i++) {
                constructors.put(getKey(ctors[i].getParameterTypes()),
                                 ctors[i]);
            }
            Method[] meths = clazz.getMethods();
            methods = new HashMap<String,HashMap<Integer,Method>>(meths.length, 1.0f);
            for (int i = 0, len = meths.length; i < len; i++) {
                name = meths[i].getName();
                HashMap<Integer,Method> methodsMap = methods.get(name);
                if (methodsMap == null) {
                    methodsMap = new HashMap<Integer,Method>(4, 1.0f);
                    methods.put(name, methodsMap);
                }
                methodsMap.put(getKey(meths[i].getParameterTypes()), meths[i]);
            }
            
            meths = clazz.getDeclaredMethods();
            declaredMethods = new HashMap<String,HashMap<Integer,Method>>(meths.length, 1.0f);
            for (int i = 0, len = meths.length; i < len; i++) {
                name = meths[i].getName();
                HashMap<Integer,Method> declaredMethodsMap = declaredMethods.get(name);
                if (declaredMethodsMap == null) {
                    declaredMethodsMap = new HashMap<Integer,Method>(4, 1.0f);
                    declaredMethods.put(name, declaredMethodsMap);
                }
                declaredMethodsMap.put(getKey(meths[i].getParameterTypes()), meths[i]);
            }

            try {
                BeanInfo info = Introspector.getBeanInfo(clazz);
                PropertyDescriptor[] pds = info.getPropertyDescriptors();
                if (pds != null) {
                    if (propertyDescriptors == null) {
                        propertyDescriptors = new HashMap<String,PropertyDescriptor>(pds.length, 1.0f);
                    }
                    for (PropertyDescriptor pd : pds) {
                        propertyDescriptors.put(pd.getName(), pd);
                    }
                }
            } catch (IntrospectionException ie) {
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.log(Level.SEVERE,
                               ie.toString(),
                               ie);
                }
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

            Map<Integer,Method> map = methods.get(name);
            Integer key = getKey(params);
            Method result = null;
            if ((null == map) || null == (result = map.get(key))) {
                map = declaredMethods.get(name);
                if (null != map) {
                    result = map.get(key);
                }
            }
            return result;

        }


        /**
         * <p>Looks up the class for this MetaData instance.</p>
         * @return the <code>Class</code> for this MetaData instance
         */
        public Class<?> lookupClass() {

            return clazz;

        }


        /**
         * @param propName a JavaBeans property name
         * @return a method suitable for setting a JavaBeans property, or
         *  <code>null</code> if the property doesn't exist or is readonly.
         */
        public Method lookupWriteMethod(String propName) {

            if (propertyDescriptors == null) {
                return null;
            }

            PropertyDescriptor pd = propertyDescriptors.get(propName);
            if (pd != null) {
                return pd.getWriteMethod();
            }
            return null;

        }


        /**
         * @param propName a JavaBeans property name
         * @return a method suitable for obtaining the value of a JavaBeans property,
         *  or <code>null</code> if the property doesn't exist or can't be read.
         */
        public Method lookupReadMethod(String propName) {

            if (propertyDescriptors == null) {
                return null;
            }

            PropertyDescriptor pd = propertyDescriptors.get(propName);
            if (pd != null) {
                return pd.getReadMethod();
            }
            return null;

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
