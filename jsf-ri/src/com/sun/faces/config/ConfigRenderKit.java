/*
 * $Id: ConfigRenderKit.java,v 1.3 2003/10/06 19:26:52 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config;

import java.util.Map;
import java.util.HashMap;
import java.util.Collections;


/**
 * <p>Config Bean for a RenderKit instance.</p>
 */
public class ConfigRenderKit extends ConfigFeature {

    private String renderKitId = "DEFAULT";
    public String getRenderKitId() {
        return (this.renderKitId);
    }
    public void setRenderKitId(String renderKitId) {
        this.renderKitId = renderKitId;
    }

    private String renderKitClass =
        "com.sun.faces.renderkit.RenderKitImpl";
    public String getRenderKitClass() {
        return (this.renderKitClass);
    }
    public void setRenderKitClass(String renderKitClass) {
        this.renderKitClass = renderKitClass;
    }

    private Map renderers = null;
    public void addRenderer(ConfigRenderer message) {
        if (renderers == null) {
            renderers = new HashMap();
        }
        renderers.put(message.getRendererType(), message);
    }
    public Map getRenderers() {
        if (renderers == null) {
            return (Collections.EMPTY_MAP);
        } else {
            return (this.renderers);
        }
    }

}
