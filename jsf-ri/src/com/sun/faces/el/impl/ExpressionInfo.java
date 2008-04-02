/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/*
 * %W% %G%
 */

package com.sun.faces.el.impl;

public class ExpressionInfo {

    private FunctionMapper functionMapper;
    private VariableResolver variableResolver;
    private String expressionString;
    private Class expectedType;

    /**
     * TODO
     * @return
     */
    public FunctionMapper getFunctionMapper() {
        return functionMapper;
    }

    /**
     * TODO
     * @param functionMapper
     */
    public void setFunctionMapper(FunctionMapper functionMapper) {
        this.functionMapper = functionMapper;
    }

    /**
     * TODO
     * @return
     */
    public VariableResolver getVariableResolver() {
        return variableResolver;
    }

    /**
     * TODO
     * @param variableResolver
     */
    public void setVariableResolver(VariableResolver variableResolver) {
        this.variableResolver = variableResolver;
    }

    /**
     * TODO
     * @return
     */
    public String getExpressionString() {
        return expressionString;
    }

    /**
     * TODO
     * @param expressionString
     */
    public void setExpressionString(String expressionString) {
        this.expressionString = expressionString;
    }

    /**
     * TODO
     * @return
     */
    public Class getExpectedType() {
        return expectedType;
    }

    /**
     * TODO
     * @param expectedType
     */
    public void setExpectedType(Class expectedType) {
        this.expectedType = expectedType;
    }
}