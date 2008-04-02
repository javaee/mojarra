/*
 * $Id: AdapterPropertyResolver.java,v 1.4 2004/02/04 23:43:50 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// AdapterPropertyResolver.java

package com.sun.faces;

import javax.faces.el.PropertyResolver;
import javax.faces.el.EvaluationException;
import javax.faces.el.PropertyNotFoundException;

public class AdapterPropertyResolver extends PropertyResolver {
    public AdapterPropertyResolver(PropertyResolver root) {
        this.root = root;
    }

    public PropertyResolver getRoot() {
        return root;
    }

    private PropertyResolver root;

    public Object getValue(Object base, Object name)
        throws EvaluationException, PropertyNotFoundException {
        return root.getValue(base, name);
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
