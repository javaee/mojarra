/*
 * $Id: TestPropertyResolver.java,v 1.5 2005/05/06 22:02:03 edburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestPropertyResolver.java

package com.sun.faces;

import javax.faces.el.PropertyResolver;
import javax.faces.el.EvaluationException;
import javax.faces.el.PropertyNotFoundException;

public class TestPropertyResolver extends PropertyResolver {
   
    public TestPropertyResolver(PropertyResolver propertyResolver) {       
    }


    // Specified by javax.faces.el.PropertyResolver.getType(Object,int)
    public Class getType(Object base, int index)  
        throws EvaluationException, PropertyNotFoundException{
        return null;
    }

    // Specified by javax.faces.el.PropertyResolver.getType(Object,String)
    public Class getType(Object base, Object property) {
        return null;
    }

    // Specified by javax.faces.el.PropertyResolver.getValue(Object,int)
    public Object getValue(Object base, int index) {
        return null;
    }

    // Specified by javax.faces.el.PropertyResolver.getValue(Object,String)
    public Object getValue(Object base, Object property) {
        Object result = null;
        return result;
    }

    // Specified by javax.faces.el.PropertyResolver.isReadOnly(Object,int)
    public boolean isReadOnly(Object base, int index) {
        return false;
    }

    // Specified by javax.faces.el.PropertyResolver.isReadOnly(Object,String)
    public boolean isReadOnly(Object base, Object property) {
        return false;
    }

    // Specified by javax.faces.el.PropertyResolver.setValue(Object,int,Object)
    public void setValue(Object base, int index, Object value) {
            
    }

    // Specified by
    // javax.faces.el.PropertyResolver.setValue(Object,String,Object)
    public void setValue(Object base, Object property, Object value) {
    }
    
}
