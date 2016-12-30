package com.sun.faces.application.resource;

import java.util.ArrayDeque;
import java.util.Iterator;

import javax.faces.context.ExternalContext;

public class ResourcePathsIterator implements Iterator<String> {
    
    private final int maxDepth;
    private final ExternalContext externalContext;
    private final ArrayDeque<String> stack = new ArrayDeque<>();
    private String next;
    
    public ResourcePathsIterator(String rootPath, int maxDepth, ExternalContext externalContext) {
        this.maxDepth = maxDepth;
        this.externalContext = externalContext;
    }

    @Override
    public boolean hasNext() {
        return false;
    }
    
    @Override
    public String next() {
        // TODO Auto-generated method stub
        return null;
    }
    
    private void visit(String path) {
        stack.addAll(externalContext.getResourcePaths(path));
    }
    
    private void tryTake() {
        if (stack.isEmpty()) {
            return;
        }
        
        while (next == null && !stack.isEmpty()) {
            
        }
        
    }
    
}
