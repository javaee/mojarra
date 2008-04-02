/*
 * $Id: ConfigRenderKit.java,v 1.1 2003/05/02 07:05:50 eburns Exp $
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

    private String messageResourcesId;
    public String getRenderKitId() {
        return (this.messageResourcesId);
    }
    public void setRenderKitId(String messageResourcesId) {
        this.messageResourcesId = messageResourcesId;
    }

    private String messageResourcesClass;
    public String getRenderKitClass() {
        return (this.messageResourcesClass);
    }
    public void setRenderKitClass(String messageResourcesClass) {
        this.messageResourcesClass = messageResourcesClass;
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
