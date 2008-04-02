/*
 * $Id: PropertyResolverTestImpl.java,v 1.5 2005/05/06 22:02:03 edburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.application;

import com.sun.faces.TestPropertyResolver;
import javax.faces.el.PropertyResolver;

public class PropertyResolverTestImpl extends TestPropertyResolver{

    public PropertyResolverTestImpl(PropertyResolver root) {
        super(root);
    }

}
