/*
 * $Id: FacesResourceBundleELResolver.java,v 1.11 2007/04/22 21:41:04 rlubke Exp $
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
        ResourceBundle result = null;
        FacesContext facesContext = (FacesContext)
           context.getContext(FacesContext.class);
        Application app = facesContext.getApplication();

        result = app.getResourceBundle(facesContext, property.toString());
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

        ResourceBundle result = null;
        FacesContext facesContext = (FacesContext)
           context.getContext(FacesContext.class);
        Application app = facesContext.getApplication();

        result = app.getResourceBundle(facesContext, property.toString());
        if (null != result) {
            context.setPropertyResolved(true);
            return ResourceBundle.class;
        }

        return null;

    }

    public void  setValue(ELContext context, Object base, Object property,
        Object val) throws ELException {
        String message = null;

        if (base == null && property == null) {
            message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "base and property"); // ?????
            throw new PropertyNotFoundException(message);
        }

        ResourceBundle result = null;
        FacesContext facesContext = (FacesContext)
           context.getContext(FacesContext.class);
        Application app = facesContext.getApplication();

        result = app.getResourceBundle(facesContext, property.toString());
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
        ResourceBundle result = null;
        FacesContext facesContext = (FacesContext)
           context.getContext(FacesContext.class);
        Application app = facesContext.getApplication();

        result = app.getResourceBundle(facesContext, property.toString());
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
