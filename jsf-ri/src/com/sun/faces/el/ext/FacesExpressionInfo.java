/*
 * $Id: FacesExpressionInfo.java,v 1.2 2003/12/17 15:13:40 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.el.ext;

import com.sun.faces.el.impl.ExpressionInfo;

import javax.faces.el.VariableResolver;
import javax.faces.el.PropertyResolver;
import javax.faces.context.FacesContext;

public class FacesExpressionInfo extends ExpressionInfo {
   
    // PENDING(visvan): this should be a typesafe enum.
    public static final String OPERATION_TYPE_SET = "set";
    public static final String OPERATION_TYPE_GET = "get";
    
    //
    // Instance Variables
    //
    private VariableResolver variableResolver;
    private PropertyResolver propertyResolver;
    private FacesContext facesContext;  
    private Object rValue;
    private String operationType=null;
    private boolean lastSegment = false;

    //
    // General Methods
    //
    
    /**
     * Returns the Faces {@see VariableResolver}.
     * @return the Faces VariableResolver
     */ 
    public VariableResolver getFacesVariableResolver() {
        return variableResolver;
    }

    /**
     * Sets the Faces {@see VariableResolver}.
     * @param variableResolver VariableResolver
     */ 
    public void setFacesVariableResolver(VariableResolver variableResolver) {
        this.variableResolver = variableResolver;
    }

    /**
     * Returns the Faces {@see PropertyResover}.
     * @return the Faces PropertyResolver
     */ 
    public PropertyResolver getPropertyResolver() {
        return propertyResolver;
    }

    /**
     * Sets the Faces {@see PropertyResolver}.
     * @param propertyResolver PropertyResolver
     */ 
    public void setPropertyResolver(PropertyResolver propertyResolver) {
        this.propertyResolver = propertyResolver;
    }

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
     * Returns the rValue of the expression.
     * @return the rValue of the expression.
     */ 
    public Object getRValue() {
        return rValue;
    }

    /**
     * Sets the rValue of the expression.
     * @param rValue the rValue
     */ 
    public void setRValue(Object rValue) {
        this.rValue = rValue;
    }
    
    /**
     * Returns the operation type, could be a set or a get.
     * @return operationType the operationType
     */ 
    public String getOperationType() {
        return operationType;
    }

    /**
     * Sets the operation type, whether its a set or a get.
     * @param opType the operationType
     */ 
    public void setOperationType(String opType) {
        this.operationType = opType;
    }

    public boolean isLastSegment() {
	return lastSegment;
    }

    public void setLastSegment(boolean lastSegment) {
	this.lastSegment = lastSegment;
    }

} // end of class FacesExpressionInfo
