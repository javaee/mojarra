/*
 * $Id: TestPropertyResolver.java,v 1.6 2005/08/09 17:38:28 jayashri Exp $
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
   
    PropertyResolver root = null;
    public TestPropertyResolver(PropertyResolver propertyResolver) {       
        root = propertyResolver;
    }
    
    // Specified by javax.faces.el.PropertyResolver.getValue(Object,String)
    public Object getValue(Object base, Object property) {
         if (property.equals("customPRTest1")) {
            return "TestPropertyResolver";
         }
         return root.getValue(base, property);
    }

    public Object getValue(Object base, int index)
        throws EvaluationException, PropertyNotFoundException {
        return root.getValue(base, index);
    }


    public void setValue(Object base, Object name, Object value)
        throws EvaluationException, PropertyNotFoundException {
        root.setValue(base, name, value);
    }


    public void setValue(Object base, int index, Object value)
        throws EvaluationException, PropertyNotFoundException {
        root.setValue(base, index, value);
    }


    public boolean isReadOnly(Object base, Object name)
        throws PropertyNotFoundException {
        return root.isReadOnly(base, name);
    }


    public boolean isReadOnly(Object base, int index)
        throws PropertyNotFoundException {
        return root.isReadOnly(base, index);
    }


    public Class getType(Object base, Object name)
        throws PropertyNotFoundException {
        return root.getType(base, name);
    }


    public Class getType(Object base, int index)
        throws PropertyNotFoundException {
        return root.getType(base, index);
    }
    
}
