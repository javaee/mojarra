/*
 * $Id: ConfigComponent.java,v 1.1 2003/11/04 18:38:32 rkitain Exp $
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
 * <p>Config Bean for a Component.</p>
 */
public class ConfigComponent extends Object {
    /**
     * <p>Keys are property names, values are <code>ConfigAttirbute</code>
     * beans.</p>
     */
    private Map properties = null;
    public void addProperty(ConfigAttribute property) {
        if (properties == null) {
            properties = new HashMap();
        }
        properties.put(property.getAttributeName(), property);
    }
    public Map getProperties() {
        if (properties == null) {
            return (Collections.EMPTY_MAP);
        } else {
            return (properties);
        }
    }
    public ConfigAttribute getProperty(String propertyName) {
        return (ConfigAttribute)properties.get(propertyName);
    }
    public String getPropertyClass(String propertyName) {
        ConfigAttribute property = (ConfigAttribute)properties.get(propertyName);
	if (property == null) {
	    return null;
	}
	return property.getAttributeClass();
    }
    public String getPropertyDescription(String propertyName) {
        ConfigAttribute property = (ConfigAttribute)properties.get(propertyName);
	if (property == null) {
	    return null;
	}
	return property.getDescription();
    }
    public String getPropertyTagAttribute(String propertyName) {
        ConfigAttribute property = (ConfigAttribute)properties.get(propertyName);
	if (property == null) {
	    return null;
	}
	return property.getTagAttribute();
    }

    private String componentType;
    public String getComponentType() {
        return (this.componentType);
    }
    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }
    private String componentClass;
    public String getComponentClass() {
        return (this.componentClass);
    }
    public void setComponentClass(String componentClass) {
        this.componentClass = componentClass;
    }
}
