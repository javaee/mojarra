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

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.PropertyNotFoundException;
import javax.el.PropertyNotWritableException;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import com.sun.faces.util.Util;
import com.sun.faces.util.MessageUtils;

public class ImplicitObjectELResolverForJsp extends ImplicitObjectELResolver {

    public ImplicitObjectELResolverForJsp() {
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
        
        if (index == null)
        {
          return null;
        }
 
        FacesContext facesContext =
          (FacesContext)context.getContext(FacesContext.class);
       
        switch (index.intValue()) {
            case FACES_CONTEXT:
                context.setPropertyResolved(true);
                return facesContext;
            case FLASH:
                context.setPropertyResolved(true);
                return facesContext.getExternalContext().getFlash();
            case VIEW:
                context.setPropertyResolved(true);
                return facesContext.getViewRoot();
            case VIEW_SCOPE:
                context.setPropertyResolved(true);
                return facesContext.getViewRoot().getViewMap();
            case RESOURCE:
                context.setPropertyResolved(true);
                return facesContext.getApplication().getResourceHandler();
            default:
                return null;
        }

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
        
        if (index == null) {
            return null;
        }
        switch (index) {
            case FACES_CONTEXT:
            case VIEW:
                context.setPropertyResolved(true);
                return null;
            default:
                return null;
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
        if (index == null) {
            return;
        }
        switch (index) {
            case FACES_CONTEXT:
                throw new PropertyNotWritableException(MessageUtils.getExceptionMessageString
                (MessageUtils.OBJECT_IS_READONLY, "facesContext"));
            case VIEW:
                throw new PropertyNotWritableException(MessageUtils.getExceptionMessageString
                (MessageUtils.OBJECT_IS_READONLY, "view"));
            default:
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
        // return value will be ignored unless context.propertyResolved is
        // set to true.
        Integer index = IMPLICIT_OBJECTS.get(property.toString());
        if (index == null) {
            return false;
        }
        switch (index) {
            case FACES_CONTEXT:
                context.setPropertyResolved(true);
                return true;
            case VIEW:
                context.setPropertyResolved(true);
                return true;
            default:
                return false;
        }
    }

    public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
        if (base != null) {
            return null;
        }

        ArrayList<FeatureDescriptor> list = new ArrayList<FeatureDescriptor>(2);
        list.add(Util.getFeatureDescriptor("facesContext", "facesContext",
                                           "facesContext",false, false, true, FacesContext.class, Boolean.TRUE));
        list.add(Util.getFeatureDescriptor("view", "view",
                                           "root",false, false, true, UIViewRoot.class, Boolean.TRUE));
        return list.iterator();

    }

    public Class<?> getCommonPropertyType(ELContext context, Object base) {
        if (base != null) {
            return null;
        }
        return String.class;
    }

}
