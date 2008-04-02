/*
 * $Id: TestOldVariableResolver.java,v 1.5 2005/05/06 22:02:03 edburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestOldVariableResolver.java

package com.sun.faces;

import javax.faces.el.VariableResolver;
import javax.faces.el.EvaluationException;
import javax.faces.context.FacesContext;

public class TestOldVariableResolver extends VariableResolver {
   
    public TestOldVariableResolver() {
       
    }
    
    //
    // Relationship Instance Variables
    // 

    // Specified by javax.faces.el.VariableResolver.resolveVariable()
    public Object resolveVariable(FacesContext context, String name)
            throws EvaluationException {
        return null;
    }

}
