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
 *
 *
 * This file incorporates work covered by the following copyright and
 * permission notice:
 *
 * Copyright 2005-2007 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sun.faces.facelets.util;

import com.sun.faces.config.ConfigurationException;
import com.sun.faces.config.WebConfiguration;
import com.sun.faces.config.WebConfiguration.WebContextInitParameter;
import com.sun.faces.util.ReflectionUtils;
import com.sun.faces.util.Util;

import javax.faces.view.facelets.ResourceResolver;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.text.MessageFormat;
import java.util.Arrays;

public class ReflectionUtil {

    private static final String[] PRIMITIVE_NAMES = new String[] { "boolean",
            "byte", "char", "double", "float", "int", "long", "short", "void" };

    private static final Class[] PRIMITIVES = new Class[] { boolean.class,
            byte.class, char.class, double.class, float.class, int.class,
            long.class, short.class, Void.TYPE };

    /**
     * 
     */
    private ReflectionUtil() {
        super();
    }

    public static Class forName(String name) throws ClassNotFoundException {
        if (null == name || "".equals(name)) {
            return null;
        }
        Class c = forNamePrimitive(name);
        if (c == null) {
            if (name.endsWith("[]")) {
                String nc = name.substring(0, name.length() - 2);
                c = Class.forName(nc, false, Thread.currentThread().getContextClassLoader());
                c = Array.newInstance(c, 0).getClass();
            } else {
                c = Class.forName(name, false, Thread.currentThread().getContextClassLoader());
            }
        }
        return c;
    }

    protected static Class forNamePrimitive(String name) {
        if (name.length() <= 8) {
            int p = Arrays.binarySearch(PRIMITIVE_NAMES, name);
            if (p >= 0) {
                return PRIMITIVES[p];
            }
        }
        return null;
    }

    /**
     * Converts an array of Class names to Class types
     * 
     * @param s the array of class names.
     * @return the array of classes.
     * @throws ClassNotFoundException
     */
    public static Class[] toTypeArray(String[] s) throws ClassNotFoundException {
        if (s == null)
            return null;
        Class[] c = new Class[s.length];
        for (int i = 0; i < s.length; i++) {
            c[i] = forName(s[i]);
        }
        return c;
    }

    /**
     * Converts an array of Class types to Class names
     * 
     * @param c the array of classes.
     * @return the array of class names.
     */
    public static String[] toTypeNameArray(Class[] c) {
        if (c == null)
            return null;
        String[] s = new String[c.length];
        for (int i = 0; i < c.length; i++) {
            s[i] = c[i].getName();
        }
        return s;
    }

//    /*
//     * Get a public method form a public class or interface of a given method.
//     * Note that if the base is an instance of a non-public class that
//     * implements a public interface,  calling Class.getMethod() with the base
//     * will not find the method.  To correct this, a version of the
//     * same method must be found in a superclass or interface.
//     **/
//
//    static private Method getMethod(Class cl, String methodName,
//                                    Class[] paramTypes) {
//
//        Method m = null;
//        try {
//            m = cl.getMethod(methodName, paramTypes);
//        } catch (NoSuchMethodException ex) {
//            return null;
//        }
//
//        Class dclass  = m.getDeclaringClass();
//        if (Modifier.isPublic(dclass.getModifiers())) {
//            return m;
//        }
//
//        Class[] intf = dclass.getInterfaces();
//        for (int i = 0; i < intf.length; i++) {
//            m = getMethod(intf[i], methodName, paramTypes);
//            if (m != null) {
//                return m;
//            }
//        }
//        Class c = dclass.getSuperclass();
//        if (c != null) {
//            m = getMethod(c, methodName, paramTypes);
//            if (m != null) {
//                return m;
//            }
//        }
//        return null;
//    }

    protected static final String paramString(Class[] types) {
        if (types != null) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < types.length; i++) {
                sb.append(types[i].getName()).append(", ");
            }
            if (sb.length() > 2) {
                sb.setLength(sb.length() - 2);
            }
            return sb.toString();
        }
        return null;
    }
    
    public static Object decorateInstance(Class clazz,
                                    Class rootType,
                                    Object root) {
        Object returnObject = null;
        try {
            if (isDevModeEnabled()) {
                Class<?>[] interfaces = clazz.getInterfaces();
                if (interfaces != null) {
                    for (Class<?> c : interfaces) {
                        if ("groovy.lang.GroovyObject"
                                .equals(c.getName())) {
                            // all groovy classes will implement this interface
                            returnObject =
                                    createScriptProxy(rootType, clazz.getName(), root);
                            break;
                        }
                    }
                }
            }
            if (returnObject == null) {
                // Look for an adapter constructor if we've got
                // an object to adapt
                if ((rootType != null) && (root != null)) {
                    Constructor construct =
                            ReflectionUtils.lookupConstructor(
                            clazz,
                            rootType);
                    if (construct != null) {
                        returnObject = construct.newInstance(root);
                    }
                }
            }
            if (clazz != null && returnObject == null) {
                returnObject = clazz.newInstance();
            }
        }
        catch (Exception e) {
            throw new ConfigurationException(
                    buildMessage(MessageFormat.format("Unable to create a new instance of ''{0}'': {1}",
                    clazz.getName(),
                    e.toString())), e);
        }
        return returnObject;

    }
    
    public static Object decorateInstance(String className,
                                    Class rootType,
                                    Object root) {
        Class clazz;
        Object returnObject = null;
        if (className != null) {
            try {
                clazz = loadClass(className, returnObject, null);
                if (clazz != null) {
                    returnObject = decorateInstance(clazz, rootType, root);
                }

            } catch (ClassNotFoundException cnfe) {
                throw new ConfigurationException(
                      buildMessage(MessageFormat.format("Unable to find class ''{0}''",
                                                        className)));
            } catch (NoClassDefFoundError ncdfe) {
                throw new ConfigurationException(
                      buildMessage(MessageFormat.format("Class ''{0}'' is missing a runtime dependency: {1}",
                                                        className,
                                                        ncdfe.toString())));
            } catch (ClassCastException cce) {
                throw new ConfigurationException(
                      buildMessage(MessageFormat.format("Class ''{0}'' is not an instance of ''{1}''",
                                                        className,
                                                        rootType)));
            } catch (Exception e) {
                throw new ConfigurationException(
                      buildMessage(MessageFormat.format("Unable to create a new instance of ''{0}'': {1}",
                                                        className,
                                                        e.toString())), e);
            }
        }

        return returnObject;
        
    }
    
    // --------------------------------------------------------- Private Methods


    private static String buildMessage(String cause) {

        return MessageFormat.format("\n  Source Document: {0}\n  Cause: {1}",
                                    "web.xml",
                                    cause);

    }
    
    
    private static Class<?> loadClass(String className,
                                 Object fallback,
                                 Class<?> expectedType)
    throws ClassNotFoundException {

        Class<?> clazz = Util.loadClass(className, fallback);
        if (expectedType != null && !expectedType.isAssignableFrom(clazz)) {
                throw new ClassCastException();
        }
        return clazz;
        
    }
    
    private static boolean isDevModeEnabled() {
        WebConfiguration webconfig = WebConfiguration.getInstance();
        return (webconfig != null
                  && "Development".equals(webconfig.getOptionValue(WebContextInitParameter.JavaxFacesProjectStage)));
    }
    
    private static Object createScriptProxy(Class<?> artifactType,
                                     String scriptName,
                                     Object root) {
        if (ResourceResolver.class.equals(artifactType)) {
            return new ResourceResolverProxy(scriptName, (ResourceResolver) root);
        }
        return null;
    }
    
    
}
