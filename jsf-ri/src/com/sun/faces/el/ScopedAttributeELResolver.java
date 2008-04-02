package com.sun.faces.el;

/*
 * $Id: ScopedAttributeELResolver.java,v 1.2 2005/07/20 17:03:53 edburns Exp $
 */
/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.beans.FeatureDescriptor;
import java.util.Map.Entry;
import java.util.Set;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import javax.el.ELException;
import javax.el.PropertyNotWritableException;
import javax.el.PropertyNotFoundException;
import javax.el.ELContext;
import javax.el.ELResolver;

import com.sun.faces.el.ELConstants;
import com.sun.faces.util.Util;

public class ScopedAttributeELResolver extends ELResolver {

    public ScopedAttributeELResolver() {
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
        String attribute = (String) property;
        FacesContext facesContext = (FacesContext)
            context.getContext(FacesContext.class);
        ExternalContext ec = facesContext.getExternalContext();
        if (null == (result = ec.getRequestMap().get(attribute))) {
            if (null == (result = ec.getSessionMap().get(attribute))) {
                result = ec.getApplicationMap().get(attribute); 
            }
        }
        if ( result != null) {
            context.setPropertyResolved(true);
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
       
        if (base == null) {
            context.setPropertyResolved(true);
            return Object.class;
        }
        return null;
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
        
        context.setPropertyResolved(true);
        Object result = null;
        
        String attribute = (String) property;
        FacesContext facesContext = (FacesContext) 
            context.getContext(FacesContext.class);
        ExternalContext ec = facesContext.getExternalContext();
        if ((result = ec.getRequestMap().get(attribute)) != null) {
            ec.getRequestMap().put(attribute, val);
        } else if ((result = ec.getSessionMap().get(attribute)) != null) {
            ec.getSessionMap().put(attribute, val);
        } else if ((result = ec.getApplicationMap().get(attribute)) != null) {
            ec.getApplicationMap().put(attribute, val);
        }
        else {
            // if the property doesn't exist in any of the scopes, put it in
            // request scope.
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
        context.setPropertyResolved(true);
        return false;
    }

    public Iterator getFeatureDescriptors(ELContext context, Object base) {

       String attrName = null;
       Object attrValue = null;
       ArrayList list = new ArrayList();
        
       FacesContext facesContext = (FacesContext) 
           context.getContext(FacesContext.class);
       ExternalContext ec = facesContext.getExternalContext();
       
       // add attributes in request scope.
       Set attrs = ec.getRequestMap().entrySet();
       Iterator it = attrs.iterator();
       while (it.hasNext()) {
           Entry entry = (Entry) it.next();
           attrName = (String) entry.getKey();
           attrValue = entry.getValue();
           list.add(Util.getFeatureDescriptor(attrName, attrName, 
           "request scope attribute", false, false, true, attrValue.getClass(), 
           Boolean.TRUE));
       }
       
       // add attributes in session scope.
       attrs = ec.getSessionMap().entrySet();
       it = attrs.iterator();
       while (it.hasNext()) {
           Entry entry = (Entry) it.next();
           attrName = (String) entry.getKey();
           attrValue = entry.getValue();
           list.add(Util.getFeatureDescriptor(attrName, attrName, 
           "session scope attribute", false, false, true, attrValue.getClass(), 
           Boolean.TRUE));
       }
       
       // add attributes in application scope.
       attrs = ec.getApplicationMap().entrySet();
       it = attrs.iterator();
       while (it.hasNext()) {
           Entry entry = (Entry) it.next();
           attrName = (String) entry.getKey();
           attrValue = entry.getValue();
           list.add(Util.getFeatureDescriptor(attrName, attrName, 
           "application scope attribute", false, false, true, attrValue.getClass(), 
           Boolean.TRUE));
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
