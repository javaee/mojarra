package com.sun.faces.spi;

public interface ThreadContext {
    
    Object getParentWebContext();
    
    void propagateWebContextToChild(Object context);
    
    void clearChildContext();

}
