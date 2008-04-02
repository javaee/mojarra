/*
 * $Id: ConfigRenderKit.java,v 1.4 2004/01/20 04:51:44 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config;

import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

import javax.faces.render.RenderKitFactory;


/**
 * <p>Config Bean for a RenderKit instance.</p>
 */
public class ConfigRenderKit extends ConfigFeature {

    private String renderKitId = RenderKitFactory.HTML_BASIC_RENDER_KIT;
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
