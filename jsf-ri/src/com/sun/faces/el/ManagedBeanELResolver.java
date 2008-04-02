/*
 * $Id: ManagedBeanELResolver.java,v 1.10 2005/08/26 15:27:06 rlubke Exp $
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

import java.beans.FeatureDescriptor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Locale;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ELResolver;
import javax.el.PropertyNotFoundException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import com.sun.faces.application.ApplicationAssociate;
import com.sun.faces.config.ManagedBeanFactoryImpl;
import com.sun.faces.config.beans.DescriptionBean;
import com.sun.faces.config.beans.DisplayNameBean;
import com.sun.faces.config.beans.ManagedBeanBean;
import com.sun.faces.util.Util;

public class ManagedBeanELResolver extends ELResolver {

    public ManagedBeanELResolver() {
    }

    public Object getValue(ELContext context, Object base, Object property) 
        throws ELException {
        if (base != null) {
            return null;
        }
        if (property == null) {
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message + " base " + base + " property " + property;
            throw new PropertyNotFoundException(message);
        }
        
        Object result = null; 
        FacesContext facesContext = (FacesContext)
            context.getContext(FacesContext.class);
        ExternalContext externalContext = facesContext.getExternalContext();

        if (externalContext.getRequestMap().containsKey(property)
            || externalContext.getSessionMap().containsKey(property)
            || externalContext.getApplicationMap().containsKey(property)) {
            return null;
        }
       
        // if it's a managed bean, try to create it
        ApplicationAssociate associate = ApplicationAssociate
                    .getInstance(facesContext.getExternalContext());
        if (null != associate) {
            result = associate.createAndMaybeStoreManagedBeans(facesContext, 
                ((String)property));
            if ( result != null) {
                context.setPropertyResolved(true);
            }
        }
        return result;
    }


    public Class getType(ELContext context, Object base, Object property) 
        throws ELException {

        if (base == null && property == null) {
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message + " base " + base + " property " + property;
            throw new PropertyNotFoundException(message);
        }

        return null;

    }

    public void  setValue(ELContext context, Object base, Object property,
        Object val) throws ELException {

        if (base == null && property == null) {
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message + " base " + base + " property " + property;
            throw new PropertyNotFoundException(message);
        }

    }

    public boolean isReadOnly(ELContext context, Object base, Object property) 
        throws ELException {
        if (base != null) {
            return false;
        }
        if (property == null) {
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message + " base " + base + " property " + property;
            throw new PropertyNotFoundException(message);
        }

        return false;
    }

    public Iterator getFeatureDescriptors(ELContext context, Object base) {
        
        if (base != null) {
            return null;
        }

        ArrayList<FeatureDescriptor> list = new ArrayList<FeatureDescriptor>();
       
        FacesContext facesContext = 
            (FacesContext) context.getContext(FacesContext.class);
        ApplicationAssociate associate = 
            ApplicationAssociate.getInstance(facesContext.getExternalContext());
        Map mbMap = associate.getManagedBeanFactoryMap();
        if (mbMap == null) {
            return list.iterator();
        }
        // iterate over the list of managed beans
        for (Iterator i = mbMap.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry entry = (Map.Entry) i.next();
            String managedBeanName = (String) entry.getKey();
            ManagedBeanFactoryImpl managedBeanFactory = (ManagedBeanFactoryImpl)
                entry.getValue();
            ManagedBeanBean managedBean = managedBeanFactory.getManagedBeanBean();
            
            if ( managedBean != null) {
                Locale curLocale = Util.getLocaleFromContextOrSystem(facesContext);
                String locale = curLocale.toString();
                DescriptionBean descBean = managedBean.getDescription(locale);                
                String desc = "";
                descBean = (null != descBean) ? descBean :
                    managedBean.getDescription("");
                if (null != descBean) {
                    // handle the case where the lang or xml:lang attributes
                    // are not specified on the description
                    desc = descBean.getDescription();
                }
                list.add(Util.getFeatureDescriptor(managedBeanName, 
                    managedBeanName, desc, false, false, true,
                    managedBeanFactory.getManagedBeanClass(), Boolean.TRUE));
            }
        }
        return list.iterator();
    }
    
    public Class getCommonPropertyType(ELContext context, Object base) {
        if (base != null) {
            return null;
        }
        return Object.class;
    }

}
