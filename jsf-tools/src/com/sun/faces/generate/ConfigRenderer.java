/*
 * $Id: ConfigRenderer.java,v 1.2 2003/11/04 18:38:33 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.generate;

import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

/**
 * <p>Config Bean for a Renderer.</p>
 */
public class ConfigRenderer extends Object {
    /**
     * <p>Keys are attribute names, values are attribute classes.</p>
     */
    private Map attributes = null;
    public void addAttribute(ConfigAttribute attribute) {
        if (attributes == null) {
            attributes = new HashMap();
        }
        attributes.put(attribute.getAttributeName(), attribute); 
    }
    public Map getAttributes() {
        if (attributes == null) {
            return (Collections.EMPTY_MAP);
        } else {
            return (attributes);
        }
    }
    public ConfigAttribute getAttribute(String attributeName) {
        return (ConfigAttribute)attributes.get(attributeName);
    }
    public String getAttributeClass(String attributeName) {
        ConfigAttribute attribute = (ConfigAttribute)attributes.get(attributeName);
	if (attribute == null) {
	    return null;
	}
	return attribute.getAttributeClass();
    }
    public String getAttributeDescription(String attributeName) {
        ConfigAttribute attribute = (ConfigAttribute)attributes.get(attributeName);
	if (attribute == null) {
	    return null;
	}
	return attribute.getDescription();
    }
    public String getAttributeTagAttribute(String attributeName) {
        ConfigAttribute attribute = (ConfigAttribute)attributes.get(attributeName);
	if (attribute == null) {
	    return null;
	}
	return attribute.getTagAttribute();
    }

    private String rendererType;
    public String getRendererType() {
        return (this.rendererType);
    }
    public void setRendererType(String rendererType) {
        this.rendererType = rendererType;
    }

    private List componentClasses = null;
    public List getComponentClasses() {
	List result = null;
	if (null == componentClasses) {
	    result = Collections.EMPTY_LIST;
	}
	else {
	    result = componentClasses;
	}
	return result;
    }
    public void addComponentClass(String componentClass) {
	if (null == componentClasses) {
	    componentClasses = new ArrayList();
	}
	componentClasses.add(componentClass);
    }

    private List componentTypes = null;
    public List getComponentTypes() {
	List result = null;
	if (null == componentTypes) {
	    result = Collections.EMPTY_LIST;
	}
	else {
	    result = componentTypes;
	}
	return result;
    }
    public void addComponentType(ConfigComponentType componentType) {
	if (null == componentTypes) {
	    componentTypes = new ArrayList();
	}
	componentTypes.add(componentType);
    }
}
