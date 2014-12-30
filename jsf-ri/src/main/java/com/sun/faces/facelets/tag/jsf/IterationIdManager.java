package com.sun.faces.facelets.tag.jsf;

import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import javax.faces.view.facelets.FaceletContext;

public class IterationIdManager {
    
    /**
     * Registers a literal Id with this manager and determines whether the same Id has been seen before
     * @param ctx Facelets Context
     * @param id literal Id
     * @return true if the same Id is already being tracked, false otherwise
     */
    public static boolean registerLiteralId(FaceletContext ctx, String id) {
        Set<String> trackedIds = _getStackOfTrackedIds(ctx).peek();
        
        if (trackedIds == null) {
            return false;
        }
        
        if (trackedIds.contains(id)) {
            return true;
        }
        
        trackedIds.add(id);
        return false;
    }
    
    public static void startIteration(FaceletContext ctx) {
        Deque<Set<String>> stack = _getStackOfTrackedIds(ctx);
        
        // Reuse existing set of Ids if we are already tracking them for the parent iteration
        Set<String> current = stack.peek();
        
        if (current == null) {
           current = new HashSet<String>();
        }
        
        stack.push(current);
    }
    
    public static void stopIteration(FaceletContext ctx) {
        _getStackOfTrackedIds(ctx).pop();
    }
    
    public static void startNamingContainer(FaceletContext ctx) {
        // Push null on the stack to suspend Id tracking
        _getStackOfTrackedIds(ctx).push(null);
    }
    
    public static void stopNamingContainer(FaceletContext ctx) {
        _getStackOfTrackedIds(ctx).pop();
    }
    
    static boolean isIterating(FaceletContext context){
      @SuppressWarnings("unchecked")
      Deque<Set<String>> iterationIds = (Deque<Set<String>>)context.getAttribute(_STACK_OF_TRACKED_IDS);

      return ((iterationIds != null) && (iterationIds.peek() != null));      
    }
    
    private static Deque<Set<String>> _getStackOfTrackedIds(FaceletContext ctx) {
        Deque<Set<String>> stack = (Deque<Set<String>>)ctx.getAttribute(_STACK_OF_TRACKED_IDS);
        if (stack == null) {
            stack = new LinkedList<Set<String>>();
            ctx.setAttribute(_STACK_OF_TRACKED_IDS, stack);
        }
        return stack;
    }
        
    
    private static final String _STACK_OF_TRACKED_IDS = "com.sun.faces.facelets.tag.js._TRACKED_IDS"; 
}
