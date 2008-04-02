/*
 * $Id: FactoryBean.java,v 1.5 2005/08/22 22:12:16 ofung Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
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
