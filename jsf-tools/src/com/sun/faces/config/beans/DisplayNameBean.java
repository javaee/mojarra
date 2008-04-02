/*
 * $Id: DisplayNameBean.java,v 1.3 2004/02/04 23:46:07 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config.beans;


/**
 * <p>Configuration bean for <code>&lt;display-name&gt; element.</p>
 */

public class DisplayNameBean {


    // -------------------------------------------------------------- Properties


    private String displayName;
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName)
    { this.displayName = displayName; }


    private String lang;
    public String getLang() { return lang; }
    public void setLang(String lang)
    { this.lang = lang; }


}
