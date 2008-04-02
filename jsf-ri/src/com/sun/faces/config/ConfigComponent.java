/*
 * $Id: ConfigComponent.java,v 1.2 2003/04/29 20:51:32 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config;


/**
 * <p>Config Bean for an Component.</p>
 */
public class ConfigComponent extends ConfigFeature {

    private String componentClass;
    public String getComponentClass() {
        return (this.componentClass);
    }
    public void setComponentClass(String componentClass) {
        this.componentClass = componentClass;
    }

    private String componentType;
    public String getComponentType() {
        return (this.componentType);
    }
    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

}
