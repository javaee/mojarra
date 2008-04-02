/*
 * $Id: FactoryBean.java,v 1.4 2004/04/30 14:32:03 eburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config.beans;

import java.util.ArrayList;
import java.util.List;


/**
 * <p>Configuration bean for <code>&lt;factory&gt; element.</p>
 */

public class FactoryBean {


    // -------------------------------------------------------------- Properties


    private List applicationFactories = new ArrayList();
    public List getApplicationFactories() { return applicationFactories; }
    public void addApplicationFactory(String applicationFactory)
    { applicationFactories.add(applicationFactory); }


    private List facesContextFactories = new ArrayList();
    public List getFacesContextFactories() { return facesContextFactories; }
    public void addFacesContextFactory(String facesContextFactory)
    { facesContextFactories.add(facesContextFactory); }


    private List lifecycleFactories = new ArrayList();
    public List getLifecycleFactories() { return lifecycleFactories; }
    public void addLifecycleFactory(String lifecycleFactory)
    { lifecycleFactories.add(lifecycleFactory); }


    private List renderKitFactories = new ArrayList();
    public List getRenderKitFactories() { return renderKitFactories; }
    public void addRenderKitFactory(String renderKitFactory)
    { renderKitFactories.add(renderKitFactory); }


}
