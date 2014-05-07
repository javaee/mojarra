/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.faces.el;

import java.beans.FeatureDescriptor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ELResolver;
import javax.el.PropertyNotFoundException;
import javax.el.PropertyNotWritableException;
import javax.faces.component.UIViewRoot;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import com.sun.faces.util.Util;
import com.sun.faces.util.MessageUtils;
import com.sun.faces.component.CompositeComponentStackManager;
import java.util.HashMap;
import javax.faces.flow.FlowHandler;

public class ImplicitObjectELResolver extends ELResolver implements ELConstants{

    protected static final Map<String, Integer> IMPLICIT_OBJECTS;

    static
    {
        String[] implictNames = new String[]{
        "application", "applicationScope", "cc", "component", "cookie", "facesContext",
        "flash",
        "flowScope",
        "header", "headerValues", "initParam", "param", "paramValues",
        "request", "requestScope", "resource", "session", "sessionScope", 
        "view", "viewScope" };
        int nameCount = implictNames.length;

        Map<String, Integer> implicitObjects = new HashMap<String, Integer>((int) (nameCount * 1.5f));

        for (int nameIndex = 0; nameIndex < nameCount; nameIndex++) {
            implicitObjects.put(implictNames[nameIndex], nameIndex);
        }

        IMPLICIT_OBJECTS = implicitObjects;
  }

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

        Integer index = IMPLICIT_OBJECTS.get(property.toString());

        if (index == null) {
            return null;
        } else {
            FacesContext facesContext = (FacesContext) context.getContext(FacesContext.class);
            ExternalContext extCtx = facesContext.getExternalContext();
	            switch (index) {
                case APPLICATION:
                    context.setPropertyResolved(true);
                    return extCtx.getContext();
                case APPLICATION_SCOPE:
                    context.setPropertyResolved(true);
                    return extCtx.getApplicationMap();
                case COMPOSITE_COMPONENT:
                    // The following five lines violate the specification.
                    // The specification states that the 'cc' implicit object
                    // always evaluates to the current composite component,
                    // however, this isn't desirable behavior when passing
                    // attributes between nested composite components, so we
                    // need to alter the behavior so that the components behave
                    // as the user would expect.
                    /* BEGIN DEVIATION */
                    CompositeComponentStackManager manager =
                          CompositeComponentStackManager.getManager(facesContext);
                    Object o = manager.peek();
                    /* END DEVIATION */
                    if (o == null) {
                        o = UIComponent.getCurrentCompositeComponent(facesContext);
                    }
                    context.setPropertyResolved(o != null);
                    return o;
                case COMPONENT:
                    UIComponent c = UIComponent.getCurrentComponent(facesContext);
                    context.setPropertyResolved(c != null);
                    return c;
                case COOKIE:
                    context.setPropertyResolved(true);
                    return extCtx.getRequestCookieMap();
                case FACES_CONTEXT:
                    context.setPropertyResolved(true);
                    return facesContext;
                case FLASH:
                    context.setPropertyResolved(true);
                    return facesContext.getExternalContext().getFlash();
                case FACES_FLOW:
                    FlowHandler flowHandler = facesContext.getApplication().getFlowHandler();
                    if (null != flowHandler) {
                        Map<Object, Object> flowScope = flowHandler.getCurrentFlowScope();
                        if (null != flowScope) {
                            context.setPropertyResolved(true);
                        }
                        return flowScope;
                    }
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
                case RESOURCE:
                    context.setPropertyResolved(true);
                    return facesContext.getApplication().getResourceHandler();
                case SESSION:
                    context.setPropertyResolved(true);
                    return extCtx.getSession(true);
                case SESSION_SCOPE:
                    context.setPropertyResolved(true);
                    return extCtx.getSessionMap();
                case VIEW:
                    context.setPropertyResolved(true);
                    return facesContext.getViewRoot();
                case VIEW_SCOPE:
                    UIViewRoot root = facesContext.getViewRoot();
                    if (root != null) {
                        context.setPropertyResolved(true);
                        return facesContext.getViewRoot().getViewMap();
                    }
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

        Integer index = IMPLICIT_OBJECTS.get(property.toString());
        if (index != null) {
            throw new PropertyNotWritableException((String) property);
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

        Integer index = IMPLICIT_OBJECTS.get(property.toString());

        if (index != null) {
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

        Integer index = IMPLICIT_OBJECTS.get(property.toString());

        if (index != null) {
            context.setPropertyResolved(true);
        }
        return null;
    }

    @Override
    public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
        if (base != null) {
            return null;
        }
        ArrayList<FeatureDescriptor> list = new ArrayList<FeatureDescriptor>(14);
        list.add(Util.getFeatureDescriptor("application", "application",
                                           "application",false, false, true, Object.class, Boolean.TRUE));
        list.add(Util.getFeatureDescriptor("applicationScope", "applicationScope",
                                           "applicationScope",false, false, true, Map.class, Boolean.TRUE));
        list.add(Util.getFeatureDescriptor("cc", "cc",
                                           "cc",false, false, true, UIComponent.class, Boolean.TRUE));
        list.add(Util.getFeatureDescriptor("component", "component",
                                           "component",false, false, true, UIComponent.class, Boolean.TRUE));
        list.add(Util.getFeatureDescriptor("cookie", "cookie",
                                           "cookie",false, false, true, Map.class, Boolean.TRUE));
        list.add(Util.getFeatureDescriptor("facesContext", "facesContext",
                                           "facesContext",false, false, true, FacesContext.class, Boolean.TRUE));
        list.add(Util.getFeatureDescriptor("flash", "flash",
                                           "flash",false, false, true, Map.class, Boolean.TRUE));
        list.add(Util.getFeatureDescriptor("flowScope", "flowScope",
                                           "flowScope",false, false, true, Map.class, Boolean.TRUE));
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
        list.add(Util.getFeatureDescriptor("resource", "resource",
                                           "resource",false, false, true, Object.class, Boolean.TRUE));
        list.add(Util.getFeatureDescriptor("session", "session",
                                           "session",false, false, true, Object.class, Boolean.TRUE));
        list.add(Util.getFeatureDescriptor("sessionScope", "sessionScope",
                                           "sessionScope",false, false, true, Map.class, Boolean.TRUE));
        list.add(Util.getFeatureDescriptor("viewScope", "viewScope",
                                           "viewScope",false, false, true, Map.class, Boolean.TRUE));

        return list.iterator();

    }

    public Class<?> getCommonPropertyType(ELContext context, Object base) {
        if (base != null) {
            return null;
        }
        return String.class;
    }

}
