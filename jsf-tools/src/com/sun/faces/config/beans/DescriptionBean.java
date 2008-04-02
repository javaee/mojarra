/*
 * $Id: DescriptionBean.java,v 1.2 2004/01/27 20:13:36 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config.beans;


/**
 * <p>Configuration bean for <code>&lt;description&gt; element.</p>
 */

public class DescriptionBean {


    // -------------------------------------------------------------- Properties


    private String description;
    public String getDescription() { return description; }
    public void setDescription(String description)
    { this.description = description; }


    private String lang;
    public String getLang() { return lang; }
    public void setLang(String lang)
    { this.lang = lang; }


}
