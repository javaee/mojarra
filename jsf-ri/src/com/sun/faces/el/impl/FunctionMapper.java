package com.sun.faces.el.impl;

import java.lang.reflect.Method;

/**
 * <p>The interface to a map between EL function names and methods.</p>
 * 
 * <p>Classes implementing this interface may, for instance, consult 
 * tag library information to resolve the map.</p> 
 */

public interface FunctionMapper {
    /**
     * <p>Resolves the specified local name and prefix into a Java.lang.Method. 
     * Returns null if the prefix and local name are not found.</p>
     * @param prefix the prefix of the function, or "" if no prefix
     * @param localName the short name of the function
     * @return the result of the method mapping.  Null means no entry found.
     */
    public Method resolve(String prefix, String localName);
}
