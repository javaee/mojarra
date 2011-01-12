/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2011 Oracle and/or its affiliates. All rights reserved.
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

import com.sun.faces.spi.DiscoverableInjectionProvider;
import com.sun.faces.spi.InjectionProviderException;
import org.mortbay.jetty.plus.annotation.InjectionCollection;
import org.mortbay.jetty.plus.annotation.LifeCycleCallbackCollection;
import org.mortbay.jetty.annotations.AnnotationParser;
import org.mortbay.jetty.webapp.WebAppContext;



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

        AnnotationParser.parseAnnotations((WebAppContext) WebAppContext.getCurrentWebAppContext(),
                                          managedBean.getClass(),
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
