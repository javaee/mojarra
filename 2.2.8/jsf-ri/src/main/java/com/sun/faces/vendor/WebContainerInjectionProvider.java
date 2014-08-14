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

package com.sun.faces.vendor;

import com.sun.faces.spi.InjectionProvider;
import com.sun.faces.spi.InjectionProviderException;
import com.sun.faces.util.FacesLogger;

import javax.annotation.PreDestroy;
import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.annotation.Annotation;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * <p>This <code>InjectionProvider</code> will be used if the
 * <code>PostConstruct</code> and <code>PreDestroy</code> annotations
 * are present, but no specific <code>InjectionProvider</code> has
 * been configured.</p>.
 *
 * <p>It's important to note that this will not provide resource injection.</p>
 */
public class WebContainerInjectionProvider implements InjectionProvider {


    private static final Logger LOGGER = FacesLogger.APPLICATION.getLogger();


    // ------------------------------------------ Methods from InjectionProvider


    public void inject(Object managedBean) throws InjectionProviderException {

        // no-op

    }

    public void invokePreDestroy(Object managedBean)
    throws InjectionProviderException {

        if (managedBean != null) {
            invokeAnnotatedMethod(getAnnotatedMethod(managedBean,
                                                     PreDestroy.class),
                                  managedBean);
        }

    }

    public void invokePostConstruct(Object managedBean)
    throws InjectionProviderException {


        if (managedBean != null) {
            invokeAnnotatedMethod(getAnnotatedMethod(managedBean,
                                                     PostConstruct.class),
                                  managedBean);
        }

    }


    // --------------------------------------------------------- Private Methods


    private static void invokeAnnotatedMethod(Method method, Object managedBean)
    throws InjectionProviderException {

        if (method != null) {
            boolean accessible = method.isAccessible();
            method.setAccessible(true);
            try {
                method.invoke(managedBean);
            } catch (Exception e) {
                throw new InjectionProviderException(e.getMessage(), e);
            } finally {
                method.setAccessible(accessible);
            }
        }

    }
    
    private static class MethodHolder {
        
        private final Method method;

        public MethodHolder(Method method){
            this.method = method;
        }

        public Method getMethod() {
            return method;
        }
    }
    
    private static ConcurrentHashMap<Class, ConcurrentHashMap<Class<? extends Annotation>, MethodHolder>> methodsPerClazz =
            new ConcurrentHashMap<Class, ConcurrentHashMap<Class<? extends Annotation>, MethodHolder>>();


    private static Method getAnnotatedMethod(Object managedBean,
                                             Class<? extends Annotation> annotation) {

        Class<?> clazz = managedBean.getClass();
        while (!Object.class.equals(clazz)) {
            
            ConcurrentHashMap<Class<? extends Annotation>, MethodHolder> methodsMap = methodsPerClazz.get(clazz);
            
            if(methodsMap==null) {
                
                ConcurrentHashMap<Class<? extends Annotation>, MethodHolder> newMethodsMap = 
                        new ConcurrentHashMap<Class<? extends Annotation>, MethodHolder>();
                
                methodsMap = methodsPerClazz.putIfAbsent(clazz, newMethodsMap);
                
                if(methodsMap==null) {
                    methodsMap = newMethodsMap;
                }                           
            }
            
            MethodHolder methodHolder = methodsMap.get(annotation);
            
            if(methodHolder==null) {
                Method[] methods = clazz.getDeclaredMethods();
                Method method = getAnnotatedMethodForMethodArr(methods, annotation);
                
                MethodHolder newMethodHolder = new MethodHolder(method);
                
                methodHolder = methodsMap.putIfAbsent(annotation, newMethodHolder);

                if(methodHolder==null) {
                    methodHolder = newMethodHolder;
                }
            }

            if(methodHolder.getMethod()!=null) {
                return methodHolder.getMethod();
            }

            clazz = clazz.getSuperclass();
        }

        return null;
    }

    private static Method getAnnotatedMethodForMethodArr(Method[] methods, Class<? extends Annotation> annotation) {
        for (Method method : methods) {
            if (method.isAnnotationPresent(annotation)) {
                // validate method
                if (Modifier.isStatic(method.getModifiers())) {
                    if (LOGGER.isLoggable(Level.WARNING)) {
                        LOGGER.log(Level.WARNING,
                                   "jsf.core.web.injection.method_not_static",
                                   new Object[] { method.toString(),
                                                  annotation.getName() });
                    }
                    continue;
                }
                if (!Void.TYPE.equals(method.getReturnType())) {
                    if (LOGGER.isLoggable(Level.WARNING)) {
                        LOGGER.log(Level.WARNING,
                                   "jsf.core.web.injection.method_return_not_void",
                                   new Object[] { method.toString(),
                                                  annotation.getName() });
                    }
                    continue;
                }
                if (method.getParameterTypes().length != 0) {
                    if (LOGGER.isLoggable(Level.WARNING)) {
                        LOGGER.log(Level.WARNING,
                                   "jsf.core.web.injection.method_no_params",
                                   new Object[] { method.toString(),
                                                  annotation.getName() });
                    }
                    continue;
                }
                Class<?>[] exceptions = method.getExceptionTypes();
                if (method.getExceptionTypes().length != 0) {
                    boolean hasChecked = false;
                    for (Class<?> excClass : exceptions) {
                        if (!RuntimeException.class.isAssignableFrom(excClass)) {
                            hasChecked = true;
                            break;
                        }
                    }
                    if (hasChecked) {
                        if (LOGGER.isLoggable(Level.WARNING)) {
                            LOGGER.log(Level.WARNING,
                                 "jsf.core.web.injection.method_no_checked_exceptions",
                                 new Object[]{method.toString(),
                                      annotation.getName()});
                        }
                        continue;
                    }
                }
                // we found a match.
                return method;
            }
        }

        return null;
    }


}
