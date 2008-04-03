/*
 * $Id: ManagedBeanELResolver.java,v 1.17 2007/04/22 21:41:04 rlubke Exp $
 */
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
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
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
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.beans.FeatureDescriptor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

        Object result = null;
        FacesContext facesContext = (FacesContext)
            context.getContext(FacesContext.class);
        ExternalContext externalContext = facesContext.getExternalContext();
        BeanManager manager =
             ApplicationAssociate.getCurrentInstance().getBeanManager();
        String beanName = property.toString();
        if (manager != null && manager.isManaged(beanName)) {
            ELUtils.Scope scope = manager.getBuilder(beanName).getScope();
            // check to see if the bean is already in scope
            switch (scope) {
                case REQUEST:
                    if (externalContext.getRequestMap().containsKey(beanName)) {
                        return null;
                    }
                case SESSION:
                    if (externalContext.getSessionMap().containsKey(beanName)) {
                        return null;
                    }
                case APPLICATION:
                    if (externalContext.getApplicationMap().containsKey(beanName)) {
                        return null;
                    }
            }

            // no bean found in scope.  create a new instance
            result = manager.create(beanName, facesContext);
            context.setPropertyResolved(result != null);
        }
      
        return result;
    }


    public Class<?> getType(ELContext context, Object base, Object property)
        throws ELException {

        if (base == null && property == null) {
            String message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "base and property"); // ?????
            throw new PropertyNotFoundException(message);
        }

        return null;

    }

    public void  setValue(ELContext context, Object base, Object property,
                          Object val) throws ELException {

        if (base == null && property == null) {
            String message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "base and property"); // ?????
            throw new PropertyNotFoundException(message);
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
        BeanManager beanManager =
             ApplicationAssociate.getCurrentInstance().getBeanManager();
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
            
            String description = descriptions.get(loc);
            if (description == null) {
                description = descriptions.get("DEFAULT");
            }
            list.add(Util.getFeatureDescriptor(beanName,
                                               beanName,
                                               description,
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

}
