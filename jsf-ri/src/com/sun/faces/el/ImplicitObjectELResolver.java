/*
 * $Id: ImplicitObjectELResolver.java,v 1.1 2005/05/05 20:51:22 edburns Exp $
 */
/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.el;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.beans.FeatureDescriptor;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.component.UIViewRoot;

import javax.el.ELException;
import javax.el.PropertyNotWritableException;
import javax.el.PropertyNotFoundException;
import javax.el.PropertyNotFoundException;
import javax.el.ELContext;
import javax.el.ELResolver;

import com.sun.faces.el.ELConstants;
import com.sun.faces.util.Util;

public class ImplicitObjectELResolver extends ELResolver implements ELConstants{

    public ImplicitObjectELResolver() {
    }

    public Object getValue(ELContext context,Object base, Object property) 
            throws ELException {
        // variable resolution is a special case of property resolution
        // where the base is null.
        if (base != null) {
            return null;
        }
        if ( base == null && property == null) {
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message + " base " + base + " property " + property;
            throw new PropertyNotFoundException(message);
        }
      
        FacesContext facesContext = 
            (FacesContext) context.getContext(FacesContext.class);
        ExternalContext extCtx = facesContext.getExternalContext();
        int index = Arrays.binarySearch(IMPLICIT_OBJECTS, property);
        if (index < 0) {
            return null;
        } else {
            switch (index) {
                case APPLICATION:
                    context.setPropertyResolved(true);
                    return extCtx.getContext();
                case APPLICATION_SCOPE:
                    context.setPropertyResolved(true);
                    return extCtx.getApplicationMap();
                case COOKIE:
                    context.setPropertyResolved(true);
                    return extCtx.getRequestCookieMap();
                case FACES_CONTEXT:
                    context.setPropertyResolved(true);
                    return facesContext;
                case HEADER:
                    context.setPropertyResolved(true);
                    return extCtx.getRequestHeaderMap();
                case HEADER_VALUES:
                    context.setPropertyResolved(true);
                    return extCtx.getRequestHeaderValuesMap();
                case INIT_PARAM:
                    context.setPropertyResolved(true);
                    return extCtx.getInitParameterMap();
                case PARAM:
                    context.setPropertyResolved(true);
                    return extCtx.getRequestParameterMap();
                case PARAM_VALUES:
                    context.setPropertyResolved(true);
                    return extCtx.getRequestParameterValuesMap();
                case REQUEST:
                    context.setPropertyResolved(true);
                    return extCtx.getRequest();
                case REQUEST_SCOPE:
                    context.setPropertyResolved(true);
                    return extCtx.getRequestMap();
                case SESSION:
                    context.setPropertyResolved(true);
                    return extCtx.getSession(true);
                case SESSION_SCOPE:
                    context.setPropertyResolved(true);
                    return extCtx.getSessionMap();
                case VIEW:
                    context.setPropertyResolved(true);
                    return facesContext.getViewRoot();
                default:
                    return null;
            }
        }
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
        
        int index = Arrays.binarySearch(IMPLICIT_OBJECTS, property);
        if ((base == null) && (index > 0)) {
            throw new PropertyNotWritableException((String)property);
        }
    }

    public boolean isReadOnly(ELContext context, Object base, Object property) 
        throws ELException{
        if (base != null) {
            return false;
        }
        if ( base == null && property == null) {
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message + " base " + base + " property " + property;
            throw new PropertyNotFoundException(message);
        }
        
        int index = Arrays.binarySearch(IMPLICIT_OBJECTS, property);
        if ((base == null) && (index > 0)) {
            context.setPropertyResolved(true);
            return true;
        }                          
        return false;	
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
        
        int index = Arrays.binarySearch(IMPLICIT_OBJECTS, property);
        if ((base == null) && (index > 0)) {
            context.setPropertyResolved(true);
        }
        return null;
    } 

    public Iterator getFeatureDescriptors(ELContext context, Object base) {
        if (base != null) {
            return null;
        }
        ArrayList list = new ArrayList(14);
        list.add(getFeatureDescriptor("application", "application",
            "application",false, false, true, Object.class, Boolean.TRUE));
        list.add(getFeatureDescriptor("applicationScope", "applicationScope",
            "applicationScope",false, false, true, Map.class, Boolean.TRUE));
        list.add(getFeatureDescriptor("cookie", "cookie",
            "cookie",false, false, true, Map.class, Boolean.TRUE));
        list.add(getFeatureDescriptor("facesContext", "facesContext",
            "facesContext",false, false, true, FacesContext.class, Boolean.TRUE));
        list.add(getFeatureDescriptor("view", "view",
            "root",false, false, true, UIViewRoot.class, Boolean.TRUE));
        list.add(getFeatureDescriptor("header", "header",
            "header",false, false, true, Map.class, Boolean.TRUE));
        list.add(getFeatureDescriptor("headerValues", "headerValues",
            "headerValues",false, false, true, Map.class, Boolean.TRUE));
        list.add(getFeatureDescriptor("initParam", "initParam",
            "initParam",false, false, true, Map.class, Boolean.TRUE));
        list.add(getFeatureDescriptor("param", "param",
            "param",false, false, true, Map.class, Boolean.TRUE));
        list.add(getFeatureDescriptor("paramValues", "paramValues",
            "paramValues",false, false, true, Map.class, Boolean.TRUE));
        list.add(getFeatureDescriptor("request", "request",
            "request",false, false, true, Object.class, Boolean.TRUE));
        list.add(getFeatureDescriptor("requestScope", "requestScope",
            "requestScope",false, false, true, Map.class, Boolean.TRUE));
        list.add(getFeatureDescriptor("session", "session",
            "session",false, false, true, Object.class, Boolean.TRUE));
        list.add(getFeatureDescriptor("sessionScope", "sessionScope",
            "sessionScope",false, false, true, Map.class, Boolean.TRUE));
        
        return list.iterator();
       
    }

    public Class getCommonPropertyType(ELContext context, Object base) {
        if (base != null) {
            return null;
        }
        return String.class;
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

}
