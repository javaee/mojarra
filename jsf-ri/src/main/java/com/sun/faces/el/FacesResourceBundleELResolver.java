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
import com.sun.faces.application.ApplicationAssociate;
import com.sun.faces.application.ApplicationResourceBundle;
import com.sun.faces.util.MessageUtils;
import com.sun.faces.util.Util;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ELResolver;
import javax.el.PropertyNotFoundException;
import javax.el.PropertyNotWritableException;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import java.beans.FeatureDescriptor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author edburns
 */
public class FacesResourceBundleELResolver extends ELResolver {

    /** Creates a new instance of FacesResourceBundleELResolver */
    public FacesResourceBundleELResolver() {
    }

    public Object getValue(ELContext context, Object base, Object property) {
        if (null != base) {
            return null;
        }
        if (null == property) {
            String message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "base and property"); // ?????
            throw new PropertyNotFoundException(message);
        }
        FacesContext facesContext = (FacesContext)
           context.getContext(FacesContext.class);
        Application app = facesContext.getApplication();

        ResourceBundle result =
              app.getResourceBundle(facesContext, property.toString());
        if (null != result) {
            context.setPropertyResolved(true);
        }

        return result;
    }



    public Class<?> getType(ELContext context, Object base, Object property)
        throws ELException {

        if (null != base) {
            return null;
        }

        if (null == property) {
            String message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "base and property"); // ?????
            throw new PropertyNotFoundException(message);
        }

        FacesContext facesContext = (FacesContext)
           context.getContext(FacesContext.class);
        Application app = facesContext.getApplication();

        ResourceBundle result =
              app.getResourceBundle(facesContext, property.toString());
        if (null != result) {
            context.setPropertyResolved(true);
            return ResourceBundle.class;
        }

        return null;

    }

    public void  setValue(ELContext context, Object base, Object property,
        Object val) throws ELException {
        String message;

        if (base == null && property == null) {
            message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "base and property"); // ?????
            throw new PropertyNotFoundException(message);
        }

        FacesContext facesContext = (FacesContext)
           context.getContext(FacesContext.class);
        Application app = facesContext.getApplication();

        ResourceBundle result =
              app.getResourceBundle(facesContext, property.toString());
        if (null != result) {
            context.setPropertyResolved(true);
            message = MessageUtils.getExceptionMessageString
                (MessageUtils.OBJECT_IS_READONLY);
            message = message + " base " + base + " property " + property;
            throw new PropertyNotWritableException(message);
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
        FacesContext facesContext = (FacesContext)
           context.getContext(FacesContext.class);
        Application app = facesContext.getApplication();

        ResourceBundle result =
              app.getResourceBundle(facesContext, property.toString());
        if (null != result) {
            context.setPropertyResolved(true);
            return true;
        }

        return false;
    }

    public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {

        if (base != null) {
            return null;
        }

        ArrayList<FeatureDescriptor> list = new ArrayList<FeatureDescriptor>();

        FacesContext facesContext =
            (FacesContext) context.getContext(FacesContext.class);
        ApplicationAssociate associate =
            ApplicationAssociate.getCurrentInstance();
        Map<String, ApplicationResourceBundle> rbMap = associate.getResourceBundles();
        if (rbMap == null) {
            return list.iterator();
        }
        // iterate over the list of managed beans
        for (Iterator<Map.Entry<String,ApplicationResourceBundle>> i = rbMap.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry<String,ApplicationResourceBundle> entry = i.next();
            String var = entry.getKey();
            ApplicationResourceBundle bundle = entry.getValue();
            if ( bundle != null) {
                Locale curLocale = Util.getLocaleFromContextOrSystem(facesContext);

                String description = bundle.getDescription(curLocale);
                String displayName = bundle.getDisplayName(curLocale);

                list.add(Util.getFeatureDescriptor(var,
                    displayName, description, false, false, true,
                    ResourceBundle.class, Boolean.TRUE));
            }
        }
        return list.iterator();
    }
    
    public Class<?> getCommonPropertyType(ELContext context, Object base) {
        if (base != null) {
            return null;
        }
        return String.class;
    }

}
