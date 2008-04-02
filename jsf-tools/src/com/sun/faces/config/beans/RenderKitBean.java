/*
 * $Id: RenderKitBean.java,v 1.4 2005/07/27 21:59:14 edburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config.beans;


import com.sun.faces.util.ToolsUtil;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 * <p>Configuration bean for <code>&lt;render-kit&gt; element.</p>
 */

public class RenderKitBean extends FeatureBean {


    private static final Logger logger = ToolsUtil.getLogger(ToolsUtil.FACES_LOGGER +
            ToolsUtil.BEANS_LOGGER);


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
        if (logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE, "addRenderer(" +
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
