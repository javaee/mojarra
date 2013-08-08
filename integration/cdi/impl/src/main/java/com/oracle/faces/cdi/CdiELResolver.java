/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright (c) 1997-2013 Oracle and/or its affiliates. All rights reserved.
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
package com.oracle.faces.cdi;

import java.beans.FeatureDescriptor;
import java.util.Iterator;
import javax.el.ELContext;
import javax.el.ELResolver;
import javax.enterprise.inject.spi.BeanManager;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * The EL resolver that dispatches back into CDI.
 * 
 * @author Manfred Riem (manfred.riem@oracle.com)
 */
public class CdiELResolver extends ELResolver {
    
    /**
     * Stores the CDI bean manager.
     */
    private BeanManager beanManager;

    /**
     * Constructor.
     */
    public CdiELResolver() {
        this.beanManager = getBeanManager();
    }

    /**
     * Get the value.
     * 
     * @param context the EL context.
     * @param base the base.
     * @param property the property.
     * @return the value, or null if not found.
     */
    @Override
    public Object getValue(ELContext context, Object base, Object property) {
        if (beanManager != null) {
            Object result = beanManager.getELResolver().getValue(context, base, property);
            if (context.isPropertyResolved()) {
                return result;
            }
        }
        context.setPropertyResolved(false);
        return null;
    }

    /**
     * Get the type.
     * 
     * @param context the EL context.
     * @param base the base.
     * @param property the property.
     * @return the type, or null if not found.
     */
    @Override
    public Class<?> getType(ELContext context, Object base, Object property) {
        if (beanManager != null) {
            Class<?> result = beanManager.getELResolver().getType(context, base, property);
            if (context.isPropertyResolved()) {
                return result;
            }
        }
        context.setPropertyResolved(false);
        return null;
    }

    /**
     * Set the value.
     * 
     * @param context the EL context.
     * @param base the base.
     * @param property the property.
     * @param value the value to set.
     */
    @Override
    public void setValue(ELContext context, Object base, Object property, Object value) {
        if (beanManager != null) {
            beanManager.getELResolver().setValue(context, base, property, value);
        }
    }

    /**
     * Is the property read only.
     * 
     * @param context the EL context.
     * @param base the base.
     * @param property the property.
     * @return true if read-only, false otherwise.
     */
    @Override
    public boolean isReadOnly(ELContext context, Object base, Object property) {
        if (beanManager != null) {
            boolean result = beanManager.getELResolver().isReadOnly(context, base, property);
            if (context.isPropertyResolved()) {
                return result;
            }
        }
        context.setPropertyResolved(false);
        return true;
    }

    /**
     * Get the feature descriptors.
     * 
     * @param context the EL context.
     * @param base the base.
     * @return the feature descriptors or null if not found.
     */
    @Override
    public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
        if (beanManager != null) {
            Iterator<FeatureDescriptor> iterator = beanManager.getELResolver().getFeatureDescriptors(context, base);
            if (iterator != null) {
                return iterator;
            }
        }
        return null;
    }

    /**
     * Get the common property type.
     * 
     * @param context the EL context.
     * @param base the base.
     * @return the common property type or null if not found.
     */
    @Override
    public Class<?> getCommonPropertyType(ELContext context, Object base) {
        if (beanManager != null) {
            Class<?> result = beanManager.getELResolver().getCommonPropertyType(context, base);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    /**
     * Get the bean manager.
     * 
     * @return the bean manager.
     */
    private BeanManager getBeanManager() {
        BeanManager result = null;
        try {
            InitialContext initialContext = new InitialContext();
            result = (BeanManager) initialContext.lookup("java:comp/BeanManager");
        } catch (NamingException ne) {
        }
        if (result == null) {
            try {
                InitialContext initialContext = new InitialContext();
                result = (BeanManager) initialContext.lookup("java:comp/env/BeanManager");
            } catch (NamingException ne) {
            }
        }
        return result;
    }
}
