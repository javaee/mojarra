/*
 * $Id: IconBean.java,v 1.2 2004/01/27 20:13:37 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config.beans;


/**
 * <p>Configuration bean for <code>&lt;icon&gt; element.</p>
 */

public class IconBean {


    // -------------------------------------------------------------- Properties


    private String lang;
    public String getLang() { return lang; }
    public void setLang(String lang)
    { this.lang = lang; }


    private String largeIcon;
    public String getLargeIcon() { return largeIcon; }
    public void setLargeIcon(String largeIcon)
    { this.largeIcon = largeIcon; }


    private String smallIcon;
    public String getSmallIcon() { return smallIcon; }
    public void setSmallIcon(String smallIcon)
    { this.smallIcon = smallIcon; }


}
