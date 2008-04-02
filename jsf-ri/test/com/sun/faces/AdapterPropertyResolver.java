/*
 * $Id: AdapterPropertyResolver.java,v 1.7 2005/08/22 22:11:04 ofung Exp $
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

// AdapterPropertyResolver.java

package com.sun.faces;

import javax.faces.el.EvaluationException;
import javax.faces.el.PropertyNotFoundException;
import javax.faces.el.PropertyResolver;

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
