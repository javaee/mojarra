/*
 * $Id: ResourceBundleBean.java,v 1.1 2005/07/19 19:33:20 edburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config.beans;


import java.util.Map;
import java.util.TreeMap;


/**
 * <p>Configuration bean for <code>&lt;attribute&gt; element.</p>
 */

public class ResourceBundleBean extends FeatureBean {
    /**
     * Holds value of property basename.
     */
    private String basename;

    /**
     * Getter for property basename.
     * @return Value of property basename.
     */
    public String getBasename() {

        return this.basename;
    }

    /**
     * Setter for property basename.
     * @param basename New value of property basename.
     */
    public void setBasename(String basename) {

        this.basename = basename;
    }

    /**
     * Holds value of property var.
     */
    private String var;

    /**
     * Getter for property var.
     * @return Value of property var.
     */
    public String getVar() {

        return this.var;
    }

    /**
     * Setter for property var.
     * @param var New value of property var.
     */
    public void setVar(String var) {

        this.var = var;
    }



}
