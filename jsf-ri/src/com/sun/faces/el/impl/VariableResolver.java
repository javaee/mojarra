package com.sun.faces.el.impl;

/**
 * <p>This class is used to customize the way an ExpressionEvaluator resolves 
 * variable references at evaluation time. For example, instances of this class 
 * can implement their own variable lookup mechanisms, or introduce the notion 
 * of "implicit variables" which override any other variables. 
 * An instance of this class should be passed when evaluating an expression.</p>
 * 
 * <p>An instance of this class includes the context against which resolution will happen.</p>
 */

public abstract class VariableResolver {
    /**
     * <p>Resolves the specified variable.  Returns null 
     * if the variable is not found</p>
     * @param variableName the name of the variable to resolve
     * @return the result of the variable resolution, or null 
     *         if the variable cannot be found.
     * @throws ElException if a failure occurred during variable
     *         resolution
     */
    public abstract Object resolve(String variableName) throws ElException;
}
