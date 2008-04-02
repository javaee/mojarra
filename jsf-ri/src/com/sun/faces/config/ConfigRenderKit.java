/*
 * $Id: ConfigRenderKit.java,v 1.2 2003/05/05 23:31:32 craigmcc Exp $
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
        "com.sun.faces.renderkit.html_basic.HtmlBasicRenderKit";
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
