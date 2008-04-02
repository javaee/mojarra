/*
 * $Id: ServletMappingBean.java,v 1.2 2004/02/04 23:40:53 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config;

/**
 * This bean contains the <code>servlet-name</code> and <code>
 * url-pattern</code> information for exactly one occurance of the
 * <code>servlet-mapping</code> element in the web application
 * deployment descriptor.
 */
public class ServletMappingBean extends Object {

    /**
     * <p>The <code>web-app/servlet-mapping/servlet-name</code>
     * element.</p>
     */ 
    private String servletName;
    
    public String getServletName() {
        return servletName;
    }


    public void setServletName(String servletName) {
        this.servletName = servletName;
    }


    /**
     * <p>The <code>web-app/servlet-mapping/url-pattern</code>
     * element.</p>
     */ 
    private String urlPattern;
    
    public String getUrlPattern() {
        return urlPattern;
    }


    public void setUrlPattern(String urlPattern) {
        this.urlPattern = urlPattern;
    }

} // end of class ServletMappingBean
