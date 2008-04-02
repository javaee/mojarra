/*
 * $Id: TestELResolver.java,v 1.4 2006/03/29 23:04:37 rlubke Exp $
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
