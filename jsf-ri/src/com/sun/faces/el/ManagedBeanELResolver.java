/*
 * $Id: ManagedBeanELResolver.java,v 1.2 2005/05/16 20:16:19 rlubke Exp $
 */
/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.el;

import java.beans.BeanInfo;
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
        if ( base == null && property == null) {
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message + " base " + base + " property " + property;
            throw new PropertyNotFoundException(message);
        }
        
        Object result = null; 
        FacesContext facesContext = (FacesContext)
            context.getContext(FacesContext.class);
	ExternalContext externalContext = facesContext.getExternalContext();
	boolean beanExists = false;
	
	if (externalContext.getRequestMap().containsKey(property)) {
	    beanExists = true;
	}
	else if (externalContext.getSessionMap().containsKey(property)) {
	    beanExists = true;
	}
	else if (externalContext.getApplicationMap().containsKey(property)) {
	    beanExists = true;
	}

	if (beanExists) {
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
        if (base != null) {
            return null;
        }
        if ( base == null && property == null) {
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message + " base " + base + " property " + property;
            throw new PropertyNotFoundException(message);
        }
        context.setPropertyResolved(true);
        return Object.class;
    }

    public void  setValue(ELContext context, Object base, Object property,
        Object val) throws ELException {
        
        if (base != null) {
            return;
        }
        if ( base == null && property == null) {
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message + " base " + base + " property " + property;
            throw new PropertyNotFoundException(message);
        }
            
        Object result = null;
        FacesContext facesContext = 
            (FacesContext) context.getContext(FacesContext.class);
        String attribute = (String) property;
        ExternalContext ec = facesContext.getExternalContext();
        if ((result = ec.getRequestMap().get(attribute)) != null) {
            ec.getRequestMap().put(attribute, val);
            context.setPropertyResolved(true);
        } else if ((result = ec.getSessionMap().get(attribute)) != null) {
            context.setPropertyResolved(true);
            ec.getSessionMap().put(attribute, val);
        } else if ((result = ec.getApplicationMap().get(attribute)) != null) {
            context.setPropertyResolved(true);
            ec.getApplicationMap().put(attribute, val);
        } else {
            ec.getRequestMap().put(attribute, val);
        }
       
    }

    public boolean isReadOnly(ELContext context, Object base, Object property) 
        throws ELException {
        if (base != null) {
            return false;
        }
        if ( base == null && property == null) {
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message + " base " + base + " property " + property;
            throw new PropertyNotFoundException(message);
        }
        // return value will be ignored unless context.propertyResolved is
        // set to true.
        context.setPropertyResolved(true);
        return false;
    }

    public Iterator getFeatureDescriptors(ELContext context, Object base) {
        
        if (base != null) {
            return null;
        }
        ArrayList list = new ArrayList();
        BeanInfo info = null;
       
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
                list.add(getFeatureDescriptor(managedBeanName, 
                    managedBeanName, desc,false, false, true,
                    managedBean.getClass(), Boolean.TRUE));
            }
        }
        return list.iterator();
    }
    
    private FeatureDescriptor getFeatureDescriptor(String name, String
        displayName, String desc, boolean expert, boolean hidden, 
        boolean preferred, Object type, Boolean designTime) {
            
        FeatureDescriptor fd = new FeatureDescriptor();
        fd.setName(name);
        fd.setDisplayName(displayName);
        fd.setShortDescription(desc);
        fd.setExpert(expert);
        fd.setHidden(hidden);
        fd.setPreferred(preferred);
        fd.setValue(ELResolver.TYPE, type);
        fd.setValue(ELResolver.RESOLVABLE_AT_DESIGN_TIME, designTime);
        return fd;
    }

    public Class getCommonPropertyType(ELContext context, Object base) {
        if (base != null) {
            return null;
        }
        return Object.class;
    }

}
