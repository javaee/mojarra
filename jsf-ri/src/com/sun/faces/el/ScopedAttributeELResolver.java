package com.sun.faces.el;

/*
 * $Id: ScopedAttributeELResolver.java,v 1.13 2007/07/17 23:14:01 rlubke Exp $
 */
/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
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

import java.util.ArrayList;
import java.util.Iterator;
import java.beans.FeatureDescriptor;
import java.util.Map.Entry;
import java.util.Set;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import javax.el.ELException;
import javax.el.PropertyNotFoundException;
import javax.el.ELContext;
import javax.el.ELResolver;

import com.sun.faces.util.Util;
import com.sun.faces.util.MessageUtils;

public class ScopedAttributeELResolver extends ELResolver {

    public ScopedAttributeELResolver() {
    }

    public Object getValue(ELContext context, Object base, Object property)
        throws ELException {
        if (base != null) {
            return null;
        }
        if ( property == null) {
            String message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "base and property"); // ?????
            throw new PropertyNotFoundException(message);
        }
        context.setPropertyResolved(true);
        String attribute = (String) property;
        FacesContext facesContext = (FacesContext)
            context.getContext(FacesContext.class);
        ExternalContext ec = facesContext.getExternalContext();
        Object result;
        if (null == (result = ec.getRequestMap().get(attribute))) {
            if (null == (result = ec.getSessionMap().get(attribute))) {
                result = ec.getApplicationMap().get(attribute);
            }
        }
        
        return result;
    }


    public Class<?> getType(ELContext context, Object base, Object property)
        throws ELException {
        if (base != null) {
            return null;
        }
        if ( property == null) {
            String message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "base and property"); // ?????
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
        if (property == null) {
            String message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "base and property"); // ?????
            throw new PropertyNotFoundException(message);
        }

        context.setPropertyResolved(true);

        String attribute = (String) property;
        FacesContext facesContext = (FacesContext)
            context.getContext(FacesContext.class);
        ExternalContext ec = facesContext.getExternalContext();
        if ((ec.getRequestMap().get(attribute)) != null) {
            ec.getRequestMap().put(attribute, val);
        } else if ((ec.getSessionMap().get(attribute)) != null) {
            ec.getSessionMap().put(attribute, val);
        } else if ((ec.getApplicationMap().get(attribute)) != null) {
            ec.getApplicationMap().put(attribute, val);
        } else {
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
        if (property == null) {
            String message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "base and property"); // ?????
            throw new PropertyNotFoundException(message);
        }
        context.setPropertyResolved(true);
        return false;
    }

    public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {

       ArrayList<FeatureDescriptor> list = new ArrayList<FeatureDescriptor>();

       FacesContext facesContext = (FacesContext)
           context.getContext(FacesContext.class);
       ExternalContext ec = facesContext.getExternalContext();

       // add attributes in request scope.
       Set<Entry<String,Object>> attrs = ec.getRequestMap().entrySet();
        for (Entry<String, Object> entry : attrs) {
            String attrName = entry.getKey();
            Object attrValue = entry.getValue();
            list.add(Util.getFeatureDescriptor(attrName, attrName,
                                               "request scope attribute", false, false, true, attrValue.getClass(),
                                               Boolean.TRUE));
        }

       // add attributes in session scope.
       attrs = ec.getSessionMap().entrySet();
        for (Entry<String, Object> entry : attrs) {
            String attrName = entry.getKey();
            Object attrValue = entry.getValue();
            list.add(Util.getFeatureDescriptor(attrName, attrName,
                                               "session scope attribute", false, false, true, attrValue.getClass(),
                                               Boolean.TRUE));
        }

       // add attributes in application scope.
       attrs = ec.getApplicationMap().entrySet();
       for (Entry<String, Object> entry : attrs) {
            String attrName = entry.getKey();
            Object attrValue = entry.getValue();
            list.add(Util.getFeatureDescriptor(attrName, attrName,
                                               "application scope attribute", false, false, true, attrValue.getClass(),
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
