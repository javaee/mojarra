/*
 * $Id: RenderKitBean.java,v 1.2 2004/01/27 20:13:43 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config.beans;


import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * <p>Configuration bean for <code>&lt;render-kit&gt; element.</p>
 */

public class RenderKitBean extends FeatureBean {


    private static final Log log = LogFactory.getLog(RenderKitBean.class);


    // -------------------------------------------------------------- Properties


    private String renderKitClass;
    public String getRenderKitClass() { return renderKitClass; }
    public void setRenderKitClass(String renderKitClass)
    { this.renderKitClass = renderKitClass; }


    private String renderKitId = "HTML_BASIC";
    public String getRenderKitId() { return renderKitId; }
    public void setRenderKitId(String renderKitId)
    { this.renderKitId = renderKitId; }


    // -------------------------------------------------- RendererHolder Methods


    // Key is family + rendererType
    private Map renderers = new TreeMap();


    public void addRenderer(RendererBean descriptor) {
        if (log.isDebugEnabled()) {
            log.debug("addRenderer(" +
                      descriptor.getComponentFamily() + "," +
                      descriptor.getRendererType() + ")");
        }
        renderers.put(descriptor.getComponentFamily() + "|" +
                      descriptor.getRendererType(), descriptor);
    }


    public RendererBean getRenderer(String componentFamily,
                                    String rendererType) {
        return ((RendererBean) renderers.get
                (componentFamily + "|" + rendererType));
    }


    public RendererBean[] getRenderers() {
        RendererBean results[] = new RendererBean[renderers.size()];
        return ((RendererBean[]) renderers.values().toArray(results));
    }


    public void removeRenderer(RendererBean descriptor) {
        renderers.remove(descriptor.getComponentFamily() + "|" +
                         descriptor.getRendererType());
    }


    // -------------------------------------------------------------- Extensions


    // ----------------------------------------------------------------- Methods


}
