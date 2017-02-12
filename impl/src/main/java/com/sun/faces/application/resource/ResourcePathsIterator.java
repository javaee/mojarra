package com.sun.faces.application.resource;

import static com.sun.faces.util.Util.startsWithOneOf;

import java.util.ArrayDeque;
import java.util.Iterator;

import javax.faces.context.ExternalContext;
import javax.servlet.ServletContext;

public class ResourcePathsIterator implements Iterator<String> {
    
    private final int maxDepth;
    private final ExternalContext externalContext;
    private final String[] extensions;
    private final String[] restrictedDirectories;
    
    private final ArrayDeque<String> stack = new ArrayDeque<>();
    
    private String next;
    
    public ResourcePathsIterator(String rootPath, int maxDepth, String[] extensions, String[] restrictedDirectories, ExternalContext externalContext) {
        this.maxDepth = maxDepth;
        this.externalContext = externalContext;
        this.extensions = extensions;
        this.restrictedDirectories = restrictedDirectories;
        visit(rootPath);
    }

    @Override
    public boolean hasNext() {
        if (next != null) {
            return true;
        }
        tryTake();
        
        return next != null;
    }
    
    @Override
    public String next() {
        if (next == null) {
            tryTake();
        }
        String nextReturn = next;
        next = null;
        return nextReturn;
    }
    
    private void visit(String resourcePath) {
        stack.addAll(externalContext.getResourcePaths(resourcePath));
    }
    
    private void tryTake() {
        if (stack.isEmpty()) {
            return;
        }
        
        while (next == null && !stack.isEmpty()) {
            String nextCandidate = stack.removeFirst();
            if (isDirectory(nextCandidate)) {
                if (!startsWithOneOf(nextCandidate, restrictedDirectories) && !directoryExceedsMaxDepth(nextCandidate, maxDepth)) {
                    visit(nextCandidate);
                }
            } else if (isValidCandidate(nextCandidate, extensions)) {
                next = nextCandidate;
            }
            
        }
    }
    
    /**
     * Checks if the given resource path obtained from {@link ServletContext#getResourcePaths(String)} represents a
     * directory.
     *
     * @param resourcePath the resource path to check
     * @return true if the resource path represents a directory, false otherwise
     */
    private static boolean isDirectory(final String resourcePath) {
        return resourcePath.endsWith("/");
    }
    
    private static boolean directoryExceedsMaxDepth(final String resourcePath, final long max) {
        return resourcePath.chars().filter(i -> i == '/').count() > max;
    }
    
    private static boolean isValidCandidate(final String resourcePath, final String[] extensions) {
        if (extensions == null || extensions.length == 0) {
            return true;
        }
        
        for (String extension : extensions) {
            if (resourcePath.endsWith(extension)) {
                return true;
            }
        }
        
        return false;
    }
    
}
