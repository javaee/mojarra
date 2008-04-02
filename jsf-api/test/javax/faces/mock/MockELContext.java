/*
 * $Id: MockELContext.java,v 1.2 2005/06/01 14:03:29 rlubke Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.mock;

import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.FunctionMapper;
import javax.el.VariableMapper;


public class MockELContext extends ELContext {
 
    private FunctionMapper functionMapper;
    private VariableMapper variableMapper;
    private ELResolver resolver;

    protected MockELContext(ELResolver resolver) {
        this.resolver = resolver;
    }

     public void setFunctionMapper(FunctionMapper fnMapper) {
        functionMapper = fnMapper;
    }

    public FunctionMapper getFunctionMapper() {
        return functionMapper;
    }

    public void setVariableMapper(VariableMapper varMapper) {
        variableMapper = varMapper;
    }

    public VariableMapper getVariableMapper() {
        return variableMapper;
    }

    public ELResolver getELResolver() {
        return resolver;
    }
    
}
