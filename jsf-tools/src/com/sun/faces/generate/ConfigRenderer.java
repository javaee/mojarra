/*
 * $Id: ConfigRenderer.java,v 1.1 2003/10/09 16:37:15 eburns Exp $
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
        attributes.put(attribute.getAttributeName(), 
		       attribute.getAttributeClass());
    }
    public Map getAttributes() {
        if (attributes == null) {
            return (Collections.EMPTY_MAP);
        } else {
            return (attributes);
        }
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

}
