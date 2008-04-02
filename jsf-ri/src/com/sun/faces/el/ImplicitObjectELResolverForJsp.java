/*
 * $Id: ImplicitObjectELResolverForJsp.java,v 1.4 2005/07/20 17:03:53 edburns Exp $
 */
/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.el;

import java.beans.FeatureDescriptor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ELResolver;
import javax.el.PropertyNotFoundException;
import javax.el.PropertyNotWritableException;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import com.sun.faces.util.Util;

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
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message + " base " + base + " property " + property;
            throw new PropertyNotFoundException(message);
        }
      
        FacesContext facesContext = 
            (FacesContext)context.getContext(FacesContext.class);
        
        int index = Arrays.binarySearch(IMPLICIT_OBJECTS, property);
        if (index < 0) {
            return null;
        } 
        switch (index) {
            case FACES_CONTEXT:
                context.setPropertyResolved(true);
                return facesContext;
            case VIEW:
                context.setPropertyResolved(true);
                return facesContext.getViewRoot();
            default:
                return null;
        }
       
    }
        
    public Class getType(ELContext context, Object base, Object property) 
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
        
        int index = Arrays.binarySearch(IMPLICIT_OBJECTS, property);
        if (index < 0) {
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
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message + " base " + base + " property " + property;
            throw new PropertyNotFoundException(message);
        }
        
        int index = Arrays.binarySearch(IMPLICIT_OBJECTS, property);
        if (index < 0) {
            return;
        } 
        switch (index) {
            case FACES_CONTEXT:
                throw new PropertyNotWritableException(Util.getExceptionMessageString
                (Util.OBJECT_IS_READONLY, new String[]{"facesContext"}));
            case VIEW:
                throw new PropertyNotWritableException(Util.getExceptionMessageString
                (Util.OBJECT_IS_READONLY, new String[]{"view"}));
            default:
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
        // return value will be ignored unless context.propertyResolved is
        // set to true.
        int index = Arrays.binarySearch(IMPLICIT_OBJECTS, property);
        if (index < 0) {
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

    public Iterator getFeatureDescriptors(ELContext context, Object base) {
        if (base != null) {
            return null;
        }
       
        ArrayList list = new ArrayList(2);
        list.add(Util.getFeatureDescriptor("facesContext", "facesContext",
        "facesContext",false, false, true, FacesContext.class, Boolean.TRUE));
        list.add(Util.getFeatureDescriptor("view", "view",
        "root",false, false, true, UIViewRoot.class, Boolean.TRUE));
        return list.iterator();
       
    }

    public Class getCommonPropertyType(ELContext context, Object base) {
        if (base != null) {
            return null;
        }
        return String.class;
    }
    
}
