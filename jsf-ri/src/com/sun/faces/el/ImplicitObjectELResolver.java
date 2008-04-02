/*
 * $Id: ImplicitObjectELResolver.java,v 1.11 2006/09/01 01:22:43 tony_robertson Exp $
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
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ELResolver;
import javax.el.PropertyNotFoundException;
import javax.el.PropertyNotWritableException;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import com.sun.faces.util.Util;
import com.sun.faces.util.MessageUtils;

public class ImplicitObjectELResolver extends ELResolver implements ELConstants{

    static final String[] IMPLICIT_OBJECTS = new String[] {
        "application", "applicationScope", "cookie", "facesContext",
        "header", "headerValues", "initParam", "param", "paramValues",
        "request", "requestScope", "session", "sessionScope", "view" };

    public ImplicitObjectELResolver() {
    }

    public Object getValue(ELContext context,Object base, Object property)
            throws ELException {
        // variable resolution is a special case of property resolution
        // where the base is null.
        if (base != null) {
            return null;
        }
        if (property == null) {
            String message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "property");
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
        if (property == null) {
            String message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "property");
            throw new PropertyNotFoundException(message);
        }

        int index = Arrays.binarySearch(IMPLICIT_OBJECTS, property);
        if (index >= 0) {
            throw new PropertyNotWritableException((String)property);
        }
    }

    public boolean isReadOnly(ELContext context, Object base, Object property)
        throws ELException{
        if (base != null) {
            return false;
        }
        if (property == null) {
            String message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "property");
            throw new PropertyNotFoundException(message);
        }

        int index = Arrays.binarySearch(IMPLICIT_OBJECTS, property);
        if (index >= 0) {
            context.setPropertyResolved(true);
            return true;
        }
        return false;
    }

    public Class<?> getType(ELContext context, Object base, Object property)
        throws ELException {
        if (base != null) {
            return null;
        }
        if (property == null) {
            String message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "property");
            throw new PropertyNotFoundException(message);
        }

        int index = Arrays.binarySearch(IMPLICIT_OBJECTS, property);
        if (index >= 0) {
            context.setPropertyResolved(true);
        }
        return null;
    }

    public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
        if (base != null) {
            return null;
        }
        ArrayList<FeatureDescriptor> list = new ArrayList<FeatureDescriptor>(14);
        list.add(Util.getFeatureDescriptor("application", "application",
                                           "application",false, false, true, Object.class, Boolean.TRUE));
        list.add(Util.getFeatureDescriptor("applicationScope", "applicationScope",
                                           "applicationScope",false, false, true, Map.class, Boolean.TRUE));
        list.add(Util.getFeatureDescriptor("cookie", "cookie",
                                           "cookie",false, false, true, Map.class, Boolean.TRUE));
        list.add(Util.getFeatureDescriptor("facesContext", "facesContext",
                                           "facesContext",false, false, true, FacesContext.class, Boolean.TRUE));
        list.add(Util.getFeatureDescriptor("view", "view",
                                           "root",false, false, true, UIViewRoot.class, Boolean.TRUE));
        list.add(Util.getFeatureDescriptor("header", "header",
                                           "header",false, false, true, Map.class, Boolean.TRUE));
        list.add(Util.getFeatureDescriptor("headerValues", "headerValues",
                                           "headerValues",false, false, true, Map.class, Boolean.TRUE));
        list.add(Util.getFeatureDescriptor("initParam", "initParam",
                                           "initParam",false, false, true, Map.class, Boolean.TRUE));
        list.add(Util.getFeatureDescriptor("param", "param",
                                           "param",false, false, true, Map.class, Boolean.TRUE));
        list.add(Util.getFeatureDescriptor("paramValues", "paramValues",
                                           "paramValues",false, false, true, Map.class, Boolean.TRUE));
        list.add(Util.getFeatureDescriptor("request", "request",
                                           "request",false, false, true, Object.class, Boolean.TRUE));
        list.add(Util.getFeatureDescriptor("requestScope", "requestScope",
                                           "requestScope",false, false, true, Map.class, Boolean.TRUE));
        list.add(Util.getFeatureDescriptor("session", "session",
                                           "session",false, false, true, Object.class, Boolean.TRUE));
        list.add(Util.getFeatureDescriptor("sessionScope", "sessionScope",
                                           "sessionScope",false, false, true, Map.class, Boolean.TRUE));

        return list.iterator();

    }

    public Class<?> getCommonPropertyType(ELContext context, Object base) {
        if (base != null) {
            return null;
        }
        return String.class;
    }

}
