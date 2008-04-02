/*
 * $Id: ReferencedBeanBean.java,v 1.2 2004/01/27 20:13:43 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config.beans;


/**
 * <p>Configuration bean for <code>&lt;referenced-bean&gt; element.</p>
 */

public class ReferencedBeanBean extends FeatureBean {


    // -------------------------------------------------------------- Properties


    private String referencedBeanClass;
    public String getReferencedBeanClass() { return referencedBeanClass; }
    public void setReferencedBeanClass(String referencedBeanClass)
    { this.referencedBeanClass = referencedBeanClass; }


    private String referencedBeanName;
    public String getReferencedBeanName() { return referencedBeanName; }
    public void setReferencedBeanName(String referencedBeanName)
    { this.referencedBeanName = referencedBeanName; }


    // -------------------------------------------------------------- Extensions


    // ----------------------------------------------------------------- Methods


}
