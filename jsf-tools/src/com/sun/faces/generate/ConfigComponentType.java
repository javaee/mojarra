/*
 * $Id: ConfigComponentType.java,v 1.1 2003/11/04 18:38:33 rkitain Exp $
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
 * <p>Config Bean for a Supported Component Type.</p>
 */
public class ConfigComponentType extends Object {

    private String componentType;
    public String getComponentType() {
        return (this.componentType);
    }
    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    private List attributeNames = null;
    public List getAttributeNames() {
	List result = null;
	if (null == attributeNames) {
	    result = Collections.EMPTY_LIST;
	}
	else {
	    result = attributeNames;
	}
	return result;
    }
    public void addAttributeName(String attributeName) {
	if (null == attributeNames) {
	    attributeNames = new ArrayList();
	}
	attributeNames.add(attributeName);
    }
}
