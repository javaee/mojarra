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

package com.sun.faces.vendor;

import com.sun.faces.spi.InjectionProvider;
import com.sun.faces.spi.InjectionProviderException;
import com.sun.faces.util.Util;

import javax.annotation.PreDestroy;
import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.annotation.Annotation;
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


    private static final Logger LOGGER = Util.getLogger(Util.FACES_LOGGER
            + Util.APPLICATION_LOGGER);


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


    private static Method getAnnotatedMethod(Object managedBean,
                                             Class<? extends Annotation> annotation) {

        Class<?> clazz = managedBean.getClass();
        while (!Object.class.equals(clazz)) {

            Method[] methods = clazz.getDeclaredMethods();
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

            clazz = clazz.getSuperclass();
        }

        return null;
    }


}
