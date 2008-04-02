/*
 * $Id: TestELResolver.java,v 1.1 2005/05/06 22:02:02 edburns Exp $
 */
/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces;

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

public class TestELResolver extends ELResolver {

    public TestELResolver() {
    }

    public Object getValue(ELContext context,Object base, Object property) 
            throws ELException {
        return null;
    }

    public void  setValue(ELContext context, Object base, Object property,
        Object val) throws ELException {
       
    }

    public boolean isReadOnly(ELContext context, Object base, Object property) 
        throws ELException{
          
        return false;	
    }

    public Class getType(ELContext context, Object base, Object property) 
        throws ELException {
        return Object.class;
    } 

    public Iterator getFeatureDescriptors(ELContext context, Object base) {
        return null;
    }

    public Class getCommonPropertyType(ELContext context, Object base) {
        if (base != null) {
            return null;
        }
        return String.class;
    }
}
