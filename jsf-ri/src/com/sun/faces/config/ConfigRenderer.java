/*
 * $Id: ConfigRenderer.java,v 1.1 2003/05/02 07:05:50 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config;


/**
 * <p>Config Bean for a Renderer.</p>
 */
public class ConfigRenderer extends ConfigFeature {

    private String rendererType;
    public String getRendererType() {
        return (this.rendererType);
    }
    public void setRendererType(String rendererType) {
        this.rendererType = rendererType;
    }

    private String rendererClass;
    public String getRendererClass() {
        return (this.rendererClass);
    }
    public void setRendererClass(String rendererClass) {
        this.rendererClass = rendererClass;
    }

}
