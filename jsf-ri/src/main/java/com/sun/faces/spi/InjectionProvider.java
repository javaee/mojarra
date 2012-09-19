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

package com.sun.faces.spi;

/**
 * <p>This interface defines an integration point for Java EE vendors.
 * Each vendor will need to provide an implementation of this interface
 * which will provide the JSF implementation the necessary hooks to 
 * perform resource injection.</p>  
 * 
 * <p>The implementation of this interface *must* be thread-safe and must
 * provider either a no-arg constructor, or a constructor accepting
 * a <code>ServletContext</code> instance.</p>
 */
public interface InjectionProvider {

    /**
     * <p>The implementation of this method must perform the following
     * steps:
     *    <ul>
     *        <li>Inject the supported resources per the Servlet 2.5
     *           specification into the provided object</li>        
     *    </ul>
     * </p>
     * <p>This method <em>must not</em> invoke any methods
     * annotated with <code>@PostConstruct</code>
     * @param managedBean the target managed bean
     * @throws InjectionProviderException if an error occurs during 
     *  resource injection
     */
    public void inject(Object managedBean) throws InjectionProviderException;

    
    /**
     * <p>The implemenation of this method must invoke any
     * method marked with the <code>@PreDestroy</code> annotation
     * (per the Common Annotations Specification).
     * @param managedBean the target managed bean
     * @throws InjectionProviderException if an error occurs when invoking
     *  the method annotated by the <code>@PreDestroy</code> annotation
     */
    public void invokePreDestroy(Object managedBean) 
    throws InjectionProviderException;
    
    
    /**
     * <p>The implemenation of this method must invoke any
     * method marked with the <code>@PostConstruct</code> annotation
     * (per the Common Annotations Specification).
     * @param managedBean the target managed bean
     * @throws InjectionProviderException if an error occurs when invoking
     *  the method annotated by the <code>@PostConstruct</code> annotation
     */
    public void invokePostConstruct(Object managedBean) 
    throws InjectionProviderException;
    
}
