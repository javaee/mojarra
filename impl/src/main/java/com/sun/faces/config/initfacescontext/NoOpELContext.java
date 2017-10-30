package com.sun.faces.config.initfacescontext;

import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.FunctionMapper;
import javax.el.VariableMapper;

public class NoOpELContext extends ELContext {
    
    @Override
    public ELResolver getELResolver() {
        return null;
    }

    @Override
    public FunctionMapper getFunctionMapper() {
        return null;
    }

    @Override
    public VariableMapper getVariableMapper() {
        return null;
    }
}
