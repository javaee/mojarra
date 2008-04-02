/*
 * $Id: VariableResolverTestImpl.java,v 1.4 2005/05/06 22:02:04 edburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.application;

import com.sun.faces.TestVariableResolver;
import javax.faces.el.VariableResolver;

public class VariableResolverTestImpl extends TestVariableResolver {

    public VariableResolverTestImpl(VariableResolver root) {
        super(root);   
    }
}
