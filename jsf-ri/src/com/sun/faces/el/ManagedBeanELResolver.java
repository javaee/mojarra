/*
 * $Id: ManagedBeanELResolver.java,v 1.5 2005/07/20 17:03:53 edburns Exp $
 */
/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.el;

import java.beans.FeatureDescriptor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ELResolver;
import javax.el.PropertyNotFoundException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import com.sun.faces.application.ApplicationAssociate;
import com.sun.faces.config.ManagedBeanFactory;
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

        ArrayList list = new ArrayList();
       
        FacesContext facesContext = 
            (FacesContext) context.getContext(FacesContext.class);
        ApplicationAssociate associate = 
            ApplicationAssociate.getInstance(facesContext.getExternalContext());
        Map mbMap = associate.getManagedBeanFactoriesMap();
        if (mbMap == null) {
            return list.iterator();
        }
        // iterate over the list of managed beans
        for (Iterator i = mbMap.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry entry = (Map.Entry) i.next();
            String managedBeanName = (String) entry.getKey();
            ManagedBeanFactory managedBean = (ManagedBeanFactory)
                entry.getValue();
            if ( managedBean != null) {
                String desc = 
                managedBean.getBeanDescription((
                    facesContext.getViewRoot().getLocale()).toString());
                if (desc == null) {
                    // handle the case where the lang or xml:lang attributes
                    // are not specified on the description
                    desc = managedBean.getBeanDescription("");
                }
                list.add(Util.getFeatureDescriptor(managedBeanName, 
                    managedBeanName, desc,false, false, true,
                    managedBean.getManagedBeanClass(), Boolean.TRUE));
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
