/*
 * $Id: FacesCompositeELResolver.java,v 1.2 2005/08/22 22:10:12 ofung Exp $
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

import java.util.ArrayList;
import java.util.Iterator;

import javax.el.ELResolver;
import javax.el.ELContext;
import javax.el.CompositeELResolver;
import javax.el.ELException;

import javax.faces.context.FacesContext;

/**
 * Maintains an ordered composite list of child <code>ELResolver for JSF</code>.
 *
 */
public class FacesCompositeELResolver extends CompositeELResolver {
    
    public void add(ELResolver elResolver) {                                                                             
        super.add(elResolver);
    }

    public Object getValue(ELContext context, Object base, Object property) 
        throws ELException {
                               
        context.setPropertyResolved(false);                      
        if (FacesContext.getCurrentInstance() == null) {
            return null;
        }                       
        return super.getValue(context, base, property);
    }

    public Class getType(ELContext context, Object base, Object property) 
        throws ELException {
        context.setPropertyResolved(false);
        if (FacesContext.getCurrentInstance() == null) {
            return null;
        }
        return super.getType(context, base, property);
    }

    
    public void setValue(ELContext context, Object base, Object property,
        Object val) throws ELException {
        context.setPropertyResolved(false);
        if (FacesContext.getCurrentInstance() == null) {
            return;
        }
        super.setValue(context, base, property, val);
    }

    
    public boolean isReadOnly(ELContext context, Object base, Object property) 
        throws ELException {
        context.setPropertyResolved(false);
        if (FacesContext.getCurrentInstance() == null) {
            return false;
        }
        return super.isReadOnly(context, base, property);
    }

    
    public Iterator getFeatureDescriptors(ELContext context, Object base) {
        return super.getFeatureDescriptors(context, base);
    }
    
    public Class getCommonPropertyType(ELContext context, Object base) {
        return null;
    }

}

