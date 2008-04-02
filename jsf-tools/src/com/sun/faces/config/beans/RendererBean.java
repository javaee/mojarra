/*
 * $Id: RendererBean.java,v 1.4 2004/02/04 23:46:09 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config.beans;


import java.util.Map;
import java.util.TreeMap;


/**
 * <p>Configuration bean for <code>&lt;renderer&gt; element.</p>
 */

public class RendererBean extends FeatureBean implements AttributeHolder {


    // -------------------------------------------------------------- Properties


    private String componentFamily;
    public String getComponentFamily() { return componentFamily; }
    public void setComponentFamily(String componentFamily)
    { this.componentFamily = componentFamily; }


    private String rendererClass;
    public String getRendererClass() { return rendererClass; }
    public void setRendererClass(String rendererClass)
    { this.rendererClass = rendererClass; }


    private String rendererType;
    public String getRendererType() { return rendererType; }
    public void setRendererType(String rendererType)
    { this.rendererType = rendererType; }


    // -------------------------------------------------------------- Extensions


    // true == this Renderer returns true for getRendersChildren()
    private boolean rendersChildren = false;
    public boolean isRendersChildren() { return rendersChildren; }
    public void setRendersChildren(boolean rendersChildren)
    { this.rendersChildren = rendersChildren; }

    private String excludeAttributes;
    public String getExcludeAttributes() {
	return excludeAttributes;
    }
    public void setExcludeAttributes(String newExcludeAttributes) {
	excludeAttributes = newExcludeAttributes;
    }



    // Tag name (if it doesn't follow the standard convention)
    private String tagName;
    public String getTagName() { return tagName; }
    public void setTagName(String tagName) { this.tagName = tagName; }


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


    // ----------------------------------------------------------------- Methods


}
