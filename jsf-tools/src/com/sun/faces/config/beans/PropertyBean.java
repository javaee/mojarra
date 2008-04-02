/*
 * $Id: PropertyBean.java,v 1.4 2005/05/05 20:51:35 edburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config.beans;


/**
 * <p>Configuration bean for <code>&lt;property&gt; element.</p>
 */

public class PropertyBean extends FeatureBean {


    // -------------------------------------------------------------- Properties


    private String propertyClass;
    public String getPropertyClass() { return propertyClass; }
    public void setPropertyClass(String propertyClass)
    { this.propertyClass = propertyClass; }


    private String propertyName;
    public String getPropertyName() { return propertyName; }
    public void setPropertyName(String propertyName)
    { this.propertyName = propertyName; }


    private String suggestedValue;
    public String getSuggestedValue() { return suggestedValue; }
    public void setSuggestedValue(String suggestedValue)
    { this.suggestedValue = suggestedValue; }


    // -------------------------------------------------------------- Extensions


    // defaultValue == Non-standard default value (if any)
    private String defaultValue = null;
    public String getDefaultValue() { return defaultValue; }
    public void setDefaultValue(String defaultValue)
    { this.defaultValue = defaultValue; }

    // passThrough == HTML attribute that passes through [default=false]
    private boolean passThrough = false;
    public boolean isPassThrough() { return passThrough; }
    public void setPassThrough(boolean passThrough)
    { this.passThrough = passThrough; }


    // readOnly == Do not generate a property setter [default=false]
    private boolean readOnly = false;
    public boolean isReadOnly() { return readOnly; }
    public void setReadOnly(boolean readOnly)
    { this.readOnly = readOnly; }


    // required == in TLD <attribute>, set required to true [default=false]
    private boolean required = false;
    public boolean isRequired() { return required; }
    public void setRequired(boolean required)
    { this.required = required; }


    // tagAttribute == Generate TLD attribute [default=true]
    private boolean tagAttribute = true;
    public boolean isTagAttribute() { return tagAttribute; }
    public void setTagAttribute(boolean tagAttribute)
    { this.tagAttribute = tagAttribute; }

    // Set to TRUE if property-extension contains method-signature element
    // [default=false]
    private boolean methodExpressionEnabled = false;
    public boolean isMethodExpressionEnabled() {
        return methodExpressionEnabled;
    }
    public void setMethodExpressionEnabled(boolean methodExpressionEnabled) {
        this.methodExpressionEnabled = methodExpressionEnabled;
    }

    private String methodSignature;
    public String getMethodSignature() { return methodSignature; }
    public void setMethodSignature(String methodSignature) {
        if (methodSignature != null) {
            methodSignature = methodSignature.trim();
            if (methodSignature.length() > 0) {
                setMethodExpressionEnabled(true);
                this.methodSignature = methodSignature.trim();
            }
        }
    }

    // value-expression-enabled - if the property can accept ValueExpressions
    // [default=false]
    private boolean valueExpressionEnabled = false;
    public boolean isValueExpressionEnabled() {
        return valueExpressionEnabled;
    }
    public void setValueExpressionEnabled(boolean valueExpressionEnabled) {
        this.valueExpressionEnabled = valueExpressionEnabled;
    }


    // ----------------------------------------------------------------- Methods


}
