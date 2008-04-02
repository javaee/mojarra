/*
 * $Id: FactoryBean.java,v 1.2 2004/01/27 20:13:37 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config.beans;


/**
 * <p>Configuration bean for <code>&lt;factory&gt; element.</p>
 */

public class FactoryBean {


    // -------------------------------------------------------------- Properties


    private String applicationFactory;
    public String getApplicationFactory() { return applicationFactory; }
    public void setApplicationFactory(String applicationFactory)
    { this.applicationFactory = applicationFactory; }


    private String facesContextFactory;
    public String getFacesContextFactory() { return facesContextFactory; }
    public void setFacesContextFactory(String facesContextFactory)
    { this.facesContextFactory = facesContextFactory; }


    private String lifecycleFactory;
    public String getLifecycleFactory() { return lifecycleFactory; }
    public void setLifecycleFactory(String lifecycleFactory)
    { this.lifecycleFactory = lifecycleFactory; }


    private String renderKitFactory;
    public String getRenderKitFactory() { return renderKitFactory; }
    public void setRenderKitFactory(String renderKitFactory)
    { this.renderKitFactory = renderKitFactory; }


}
