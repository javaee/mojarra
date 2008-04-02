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
import com.sun.enterprise.Switch;
import com.sun.enterprise.InjectionManager;
import com.sun.enterprise.InjectionException;

/**
 * <p>This <code>InjectionProvider</code> is specific to the
 * GlassFish/SJSAS 9.0 PE/EE application servers.</p>
 */
public class GlassFishInjectionProvider implements InjectionProvider {

    private InjectionManager injectionManager;

    /**
     * <p>Constructs a new <code>GlassFishInjectionProvider</code> instance.</p>
     */
    public GlassFishInjectionProvider() {
        Switch iSwitch = Switch.getSwitch();
        injectionManager = iSwitch.getInjectionManager();
    }

    /**
     * <p>The implementation of this method must perform the following
     * steps:
     * <ul>
     * <li>Inject the supported resources per the Servlet 2.5
     * specification into the provided object</li>
     * <li>Inoke any method marked with the <code>@PreDestroy</code>
     * annotation (per the Common Annotations Specification)</li>
     * </ul>
     * </p>
     *
     * @param managedBean the target managed bean
     */
    public void inject(Object managedBean) throws InjectionProviderException {
        try {
            injectionManager.injectInstance(managedBean);
        } catch (InjectionException ie) {
            throw new InjectionProviderException(ie);
        }
    }

    /**
     * <p>The implemenation of this method must invoke any
     * method marked with the <code>@PreDestroy</code> annotation
     * (per the Common Annotations Specification).
     *
     * @param managedBean the target managed bean
     */
    public void invokePreDestroy(Object managedBean) 
    throws InjectionProviderException {
        try {
            injectionManager.invokeInstancePreDestroy(managedBean);
        } catch (InjectionException ie) {
            throw new InjectionProviderException(ie);           
        }
    }
    
} // END GlassFishInjectionProvider