/*
 * $Id: TestVariableResolver.java,v 1.5 2005/05/06 22:02:03 edburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestVariableResolver.java

package com.sun.faces;

import javax.faces.el.VariableResolver;
import javax.faces.el.EvaluationException;
import javax.faces.context.FacesContext;

public class TestVariableResolver extends VariableResolver {
   
    public TestVariableResolver(VariableResolver variableResolver) {
       
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
