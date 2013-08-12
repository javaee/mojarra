/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2010 Oracle and/or its affiliates. All rights reserved.
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

/*
 * $Id: ManagedBeanELResolver.java,v 1.19.4.6 2010/04/13 19:38:42 rogerk Exp $
 */
/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2010 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
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

package com.sun.faces.el;

import com.sun.faces.application.ApplicationAssociate;
import com.sun.faces.mgbean.BeanBuilder;
import com.sun.faces.mgbean.BeanManager;
import com.sun.faces.util.MessageUtils;
import com.sun.faces.util.Util;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ELResolver;
import javax.el.PropertyNotFoundException;
import javax.faces.context.FacesContext;
import java.beans.FeatureDescriptor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.faces.context.ExternalContext;

public class ManagedBeanELResolver extends ELResolver {

    public ManagedBeanELResolver() {
    }

    public Object getValue(ELContext context, Object base, Object property)
        throws ELException {
        if (base != null) {
            return null;
        }
        if (property == null) {
            String message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "property");
            throw new PropertyNotFoundException(message);
        }

        return resolveBean(context, property, true);

    }


    public Class<?> getType(ELContext context, Object base, Object property)
        throws ELException {

        if (base == null && property == null) {
            String message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "property");
            throw new PropertyNotFoundException(message);
        }

        return null;

    }

    public void setValue(ELContext context,
                         Object base,
                         Object property,
                         Object val)
    throws ELException {

        if (base == null && property == null) {
            String message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "base and property");
            throw new PropertyNotFoundException(message);
        }

        if (base == null) {
            // create the bean if it doesn't exist.  We won't mark the property
            // as resolved in this case, since the spec requires us to actually
            // wait to set the value until the ScopeAttributeELResolved so that
            // implicit scopes with higher priority than this one have a crack
            // at resolving the bean first
            resolveBean(context, property, false);
        }

    }

    public boolean isReadOnly(ELContext context, Object base, Object property)
        throws ELException {
        if (base != null) {
            return false;
        }
        if (property == null) {
            String message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "property");
            throw new PropertyNotFoundException(message);
        }

        return false;
    }

    public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {

        if (base != null) {
            return null;
        }

        FacesContext facesContext =
            (FacesContext) context.getContext(FacesContext.class);
        BeanManager beanManager = getBeanManager();
        if (beanManager == null || beanManager.getRegisteredBeans().isEmpty()) {
            List<FeatureDescriptor> l = Collections.emptyList();
            return l.iterator();
        }

        Map<String,BeanBuilder> beans = beanManager.getRegisteredBeans();
        List<FeatureDescriptor> list =
             new ArrayList<FeatureDescriptor>(beans.size());
        // iterate over the list of managed beans
        for (Map.Entry<String,BeanBuilder> bean : beans.entrySet()) {
            String beanName = bean.getKey();
            BeanBuilder builder = bean.getValue();
            String loc = Util.getLocaleFromContextOrSystem(facesContext).toString();
            Map<String,String> descriptions = builder.getDescriptions();

            String description = null;
            if (descriptions != null) {
                description = descriptions.get(loc);
                if (description == null) {
                    description = descriptions.get("DEFAULT");
                }
            }
            list.add(Util.getFeatureDescriptor(beanName,
                                               beanName,
                                               (description == null) ? "" : description,
                                               false,
                                               false,
                                               true,
                                               builder.getBeanClass(),
                                               Boolean.TRUE));
        }

        return list.iterator();
    }

    public Class<?> getCommonPropertyType(ELContext context, Object base) {
        if (base != null) {
            return null;
        }
        return Object.class;
    }


    // --------------------------------------------------------- Private Methods


    private static BeanManager getBeanManager() {

        ApplicationAssociate associate = ApplicationAssociate.getCurrentInstance();
        return ((associate != null) ? associate.getBeanManager() : null);

    }

    private Object resolveBean(ELContext context, Object property, boolean markAsResolvedIfCreated) {
        Object result = null;
        BeanManager manager = getBeanManager();
        if (manager != null) {
            String beanName = property.toString();
            BeanBuilder builder = manager.getBuilder(beanName);
            if (builder != null) {
                FacesContext facesContext = (FacesContext)
                    context.getContext(FacesContext.class);

                // JAVASERVERFACES-2989: Make sure to check request, session, and application.
                ExternalContext extContext = facesContext.getExternalContext();
                if (extContext.getRequestMap().containsKey(beanName)) {
                    return null;
                } else if (null != extContext.getSession(false) &&
                           extContext.getSessionMap().containsKey(beanName)) {
                    return null;
                } else if (extContext.getApplicationMap().containsKey(beanName)) {
                    return null;
                }                
                
                result = manager.getBeanFromScope(beanName, builder, facesContext);
                if (result == null) {
                        result = manager.create(beanName, builder, facesContext);
                }
                context.setPropertyResolved(markAsResolvedIfCreated && (result != null));
            }
        }

        return result;
    }


}
