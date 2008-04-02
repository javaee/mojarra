/*
 * $Id: ComponentBean.java,v 1.2 2004/01/27 20:13:35 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config.beans;


import java.util.Map;
import java.util.TreeMap;


/**
 * <p>Configuration bean for <code>&lt;attribute&gt; element.</p>
 */

public class ComponentBean extends FeatureBean
 implements AttributeHolder, PropertyHolder {


    // -------------------------------------------------------------- Properties


    private String componentClass;
    public String getComponentClass() { return componentClass; }
    public void setComponentClass(String componentClass)
    { this.componentClass = componentClass; }


    private String componentType;
    public String getComponentType() { return componentType; }
    public void setComponentType(String componentType)
    { this.componentType = componentType; }


    // -------------------------------------------------------------- Extensions


    // baseComponentType == componentType of base class for this component class
    // (extends UIComponentBase if not specified)
    private String baseComponentType;
    public String getBaseComponentType() { return baseComponentType; }
    public void setBaseComponentType(String baseComponentType)
    { this.baseComponentType = baseComponentType; }


    // componentFamily == default componentFamily for this component class
    // (inherited from baseComponentType if not specified)
    private String componentFamily;
    public String getComponentFamily() { return componentFamily; }
    public void setComponentFamily(String componentFamily)
    { this.componentFamily = componentFamily; }


    // rendererType == default rendererType for this component class
    // (set to null if not specified)
    private String rendererType;
    public String getRendererType() { return rendererType; }
    public void setRendererType(String rendererType)
    { this.rendererType = rendererType; }


    // ------------------------------------------------- AttributeHolder Methods


    private Map attributes = new TreeMap();


    public void addAttribute(AttributeBean descriptor) {
        attributes.put(descriptor.getAttributeName(), descriptor);
    }


    public AttributeBean getAttribute(String name) {
        return ((AttributeBean) attributes.get(name));
    }


    public AttributeBean[] getAttributes() {
        AttributeBean results[] = new AttributeBean[attributes.size()];
        return ((AttributeBean[]) attributes.values().toArray(results));
    }


    public void removeAttribute(AttributeBean descriptor) {
        attributes.remove(descriptor.getAttributeName());
    }


    // ------------------------------------------------- PropertyHolder Methods


    private Map properties = new TreeMap();


    public void addProperty(PropertyBean descriptor) {
        properties.put(descriptor.getPropertyName(), descriptor);
    }


    public PropertyBean getProperty(String name) {
        return ((PropertyBean) properties.get(name));
    }


    public PropertyBean[] getProperties() {
        PropertyBean results[] = new PropertyBean[properties.size()];
        return ((PropertyBean[]) properties.values().toArray(results));
    }


    public void removeProperty(PropertyBean descriptor) {
        properties.remove(descriptor.getPropertyName());
    }


    // ----------------------------------------------------------------- Methods


}
