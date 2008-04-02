/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/*
 * %W% %G%
 */

package com.sun.faces.el.impl;

import javax.faces.context.FacesContext;
import javax.faces.el.VariableResolver;
import javax.faces.el.PropertyResolver;

public class ExpressionInfo {

    private FacesContext facesContext;
    private FunctionMapper functionMapper;
    private VariableResolver variableResolver;
    private PropertyResolver propertyResolver;
    private String expressionString;
    private Class expectedType;

    /**
     * Returns the {@see FacesContext}.
     * @return the FacesContext
     */ 
    public FacesContext getFacesContext() {
        return facesContext;
    }

    /**
     * Sets the {@see FacesContext}.
     * @param facesContext FacesContext
     */ 
    public void setFacesContext(FacesContext facesContext) {
        this.facesContext = facesContext;
    }

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

    public PropertyResolver getPropertyResolver() {
        return propertyResolver;
    }

    public void setPropertyResolver(PropertyResolver propertyResolver) {
        this.propertyResolver = propertyResolver;
    }

    /**
     * Returns the expression string with the "${}" delimiters that
     * the JSP 2.0 evaluator requires. An alternative would be to
     * modify the parser, but I think that can wait until a merged
     * JSF/JSP evaluator is designed.
     *
     * @return
     */
    public String getExpressionString() {
        return "${" + expressionString + "}";
    }

    /**
     * TODO
     * @param expressionString
     */
    public void setExpressionString(String expressionString) {
        int index;
        //ExpressionString may contain more than one expression.
        //If it does, change delimeters to ones recognized by the
        //JSP parser.
        while ((index = expressionString.indexOf("#{")) != -1) {
            StringBuffer buf = new StringBuffer();
            buf.append(expressionString.substring(0, index));
            buf.append("$");
            buf.append(expressionString.substring(index + 1));
            expressionString = buf.toString();
        }
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
