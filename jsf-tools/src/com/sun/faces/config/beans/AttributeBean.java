/*
 * $Id: AttributeBean.java,v 1.2 2004/01/27 20:13:34 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config.beans;


/**
 * <p>Configuration bean for <code>&lt;attribute&gt; element.</p>
 */

public class AttributeBean extends FeatureBean {


    // -------------------------------------------------------------- Properties


    private String attributeClass;
    public String getAttributeClass() { return attributeClass; }
    public void setAttributeClass(String attributeClass)
    { this.attributeClass = attributeClass; }


    private String attributeName;
    public String getAttributeName() { return attributeName; }
    public void setAttributeName(String attributeName)
    { this.attributeName = attributeName; }


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


    // ----------------------------------------------------------------- Methods


}
