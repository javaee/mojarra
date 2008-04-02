/*
 * $Id: ConfigFactory.java,v 1.2 2003/09/05 18:56:53 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config;


/**
 * <p>Config Bean for Factory Rule .</p>
 */
public class ConfigFactory {

    private String applicationFactory = null;
    private String facesContextFactory = null;
    private String lifecycleFactory = null;
    private String renderKitFactory = null;

    public String getApplicationFactory() {
        return (this.applicationFactory);
    }
    public void setApplicationFactory(String applicationFactory) {
        this.applicationFactory = applicationFactory;
    }

    public String getFacesContextFactory() {
        return (this.facesContextFactory);
    }
    public void setFacesContextFactory(String facesContextFactory) {
        this.facesContextFactory = facesContextFactory;
    }

    public String getLifecycleFactory() {
        return (this.lifecycleFactory);
    }
    public void setLifecycleFactory(String lifecycleFactory) {
        this.lifecycleFactory = lifecycleFactory;
    }

    public String getRenderKitFactory() {
        return (this.renderKitFactory);
    }
    public void setRenderKitFactory(String renderKitFactory) {
        this.renderKitFactory = renderKitFactory;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("ApplicationFactory:"+getApplicationFactory());
        sb.append("\nFacesContextFactory:"+getFacesContextFactory());
        sb.append("\nLifecycleFactory:"+getLifecycleFactory());
        sb.append("\nRenderKitFactory:"+getRenderKitFactory());
        return sb.toString();
    }
}
