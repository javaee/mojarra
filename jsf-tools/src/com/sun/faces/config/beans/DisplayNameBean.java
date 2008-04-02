/*
 * $Id: DisplayNameBean.java,v 1.2 2004/01/27 20:13:36 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
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
