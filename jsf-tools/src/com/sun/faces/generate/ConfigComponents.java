/*
 * $Id: ConfigComponents.java,v 1.1 2003/11/04 18:38:33 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.generate;

import java.util.Map;
import java.util.HashMap;
import java.util.Collections;


/**
 * <p>Config Bean for a Components instance.</p>
 */
public class ConfigComponents extends Object {

    private Map components = null;
    public void addComponent(ConfigComponent component) {
        if (components == null) {
            components = new HashMap();
        }
        components.put(component.getComponentType(), component);
    }
    public Map getComponents() {
        if (components == null) {
            return (Collections.EMPTY_MAP);
        } else {
            return (this.components);
        }
    }
}
