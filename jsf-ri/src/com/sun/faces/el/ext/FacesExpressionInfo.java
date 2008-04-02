/*
 * $Id: FacesExpressionInfo.java,v 1.1 2003/08/13 18:10:45 rlubke Exp $
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
   
    //
    // Instance Variables
    //
    private VariableResolver variableResolver;
    private PropertyResolver propertyResolver;
    private FacesContext facesContext;  
    private Object rValue;

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

} // end of class FacesExpressionInfo
