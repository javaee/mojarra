/*
 * $Id: ConfigRenderKit.java,v 1.1 2003/09/25 22:22:05 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.generate;

import java.util.Map;
import java.util.HashMap;
import java.util.Collections;


/**
 * <p>Config Bean for a RenderKit instance.</p>
 */
public class ConfigRenderKit extends Object {

    private Map renderers = null;
    public void addRenderer(ConfigRenderer renderer) {
        if (renderers == null) {
            renderers = new HashMap();
        }
        renderers.put(renderer.getRendererType(), renderer);
    }
    public Map getRenderers() {
        if (renderers == null) {
            return (Collections.EMPTY_MAP);
        } else {
            return (this.renderers);
        }
    }

}
