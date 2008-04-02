/*
 * $Id: LocaleConfigBean.java,v 1.2 2004/01/27 20:13:39 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config.beans;


import java.util.Set;
import java.util.TreeSet;


/**
 * <p>Configuration bean for <code>&lt;locale-config&gt; element.</p>
 */

public class LocaleConfigBean {


    // -------------------------------------------------------------- Properties


    private String defaultLocale;
    public String getDefaultLocale() { return defaultLocale; }
    public void setDefaultLocale(String defaultLocale)
    { this.defaultLocale = defaultLocale; }


    // ------------------------------------------- SupportedLocaleHolder Methods


    private Set supportedLocales = new TreeSet();


    public void addSupportedLocale(String supportedLocale) {
        if (!supportedLocales.contains(supportedLocale)) {
            supportedLocales.add(supportedLocale);
        }
    }


    public String[] getSupportedLocales() {
        String results[] = new String[supportedLocales.size()];
        return ((String[]) supportedLocales.toArray(results));
    }


    public void removeSupportedLocale(String supportedLocale) {
        supportedLocales.remove(supportedLocale);
    }


    // ----------------------------------------------------------------- Methods


}
