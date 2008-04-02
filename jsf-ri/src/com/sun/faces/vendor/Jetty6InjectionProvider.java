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

import com.sun.faces.spi.DiscoverableInjectionProvider;
import com.sun.faces.spi.InjectionProviderException;
import org.mortbay.jetty.plus.annotation.InjectionCollection;
import org.mortbay.jetty.plus.annotation.LifeCycleCallbackCollection;
import org.mortbay.jetty.annotations.AnnotationParser;


/**
 * <p>See http://docs.codehaus.org/display/JETTY/Annotations for details on
 * Jetty's supported Annotations.</p>
 */
public class Jetty6InjectionProvider extends DiscoverableInjectionProvider {

    private InjectionCollection injections;
    private LifeCycleCallbackCollection callbacks;


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Construct a new Jetty6InjectionProvider instance.</p>
     */
    public Jetty6InjectionProvider() {

        injections = new InjectionCollection();
        callbacks = new LifeCycleCallbackCollection();

    }


    // ------------------------------ Methods from DiscoverableInjectionProvider


    /**
     * <p>The implementation of this method must perform the following
     * steps:
     * <ul>
     * <li>Inject the supported resources per the Servlet 2.5
     * specification into the provided object</li>
     * </ul>
     * </p>
     * <p>This method <em>must not</em> invoke any methods
     * annotated with <code>@PostConstruct</code>
     *
     * @param managedBean the target managed bean
     * @throws com.sun.faces.spi.InjectionProviderException
     *          if an error occurs during
     *          resource injection
     */
    public void inject(Object managedBean) throws InjectionProviderException {

        AnnotationParser.parseAnnotations(managedBean.getClass(),
                                          null,
                                          injections,
                                          callbacks);
        try {
            injections.inject(managedBean);
        } catch (Exception e) {
            throw new InjectionProviderException(e);
        }
        
    }

    /**
     * <p>The implemenation of this method must invoke any
     * method marked with the <code>@PreDestroy</code> annotation
     * (per the Common Annotations Specification).
     *
     * @param managedBean the target managed bean
     * @throws com.sun.faces.spi.InjectionProviderException
     *          if an error occurs when invoking
     *          the method annotated by the <code>@PreDestroy</code> annotation
     */
    public void invokePreDestroy(Object managedBean)
    throws InjectionProviderException {

        try {
            callbacks.callPreDestroyCallback(managedBean);
        } catch (Exception e) {
            throw new InjectionProviderException(e);
        }

    }

    /**
     * <p>The implemenation of this method must invoke any
     * method marked with the <code>@PostConstruct</code> annotation
     * (per the Common Annotations Specification).
     *
     * @param managedBean the target managed bean
     * @throws com.sun.faces.spi.InjectionProviderException
     *          if an error occurs when invoking
     *          the method annotated by the <code>@PostConstruct</code>
     *          annotation
     */
    public void invokePostConstruct(Object managedBean)
    throws InjectionProviderException {

        try {
            callbacks.callPostConstructCallback(managedBean);       
        } catch (Exception e) {
            throw new InjectionProviderException(e);
        }

    }
}
