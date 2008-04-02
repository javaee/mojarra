/*
 * $Id: PropertyResolverTestImpl.java,v 1.6 2005/08/09 17:38:29 jayashri Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.application;

import com.sun.faces.TestPropertyResolver;
import javax.faces.el.PropertyResolver;

public class PropertyResolverTestImpl extends TestPropertyResolver{

    PropertyResolver root = null;
    
    public PropertyResolverTestImpl(PropertyResolver root) {
       super(root);
       this.root = root;
    }
    
     public Object getValue(Object base, Object property) {
        if (property.equals("customPRTest2")) {
            return "PropertyResolverTestImpl";
        }
        return root.getValue(base, property);
    }

}
