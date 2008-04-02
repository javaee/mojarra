/*
 * $Id: ReferencedBeanBean.java,v 1.3 2004/02/04 23:46:09 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
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
