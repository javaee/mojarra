/*
 * $Id: ConfigApplication.java,v 1.5 2003/10/30 16:14:13 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * <p>Config Bean for Application Rule .</p>
 */
public class ConfigApplication {

    private String actionListener = null;
    private String messageBundle = null;
    private String navigationHandler = null;
    private String propertyResolver = null;
    private String variableResolver = null;
    private String viewHandler = null;

    public String getActionListener() {
        return (this.actionListener);
    }
    public void setActionListener(String actionListener) {
        this.actionListener = actionListener;
    }

    public String getMessageBundle() {
        return (this.messageBundle);
    }
    public void setMessageBundle(String messageBundle) {
        this.messageBundle = messageBundle;
    }

    public String getNavigationHandler() {
        return (this.navigationHandler);
    }
    public void setNavigationHandler(String navigationHandler) {
        this.navigationHandler = navigationHandler;
    }

    public String getPropertyResolver() {
        return (this.propertyResolver);
    }
    public void setPropertyResolver(String propertyResolver) {
        this.propertyResolver = propertyResolver;
    }

    public String getVariableResolver() {
        return (this.variableResolver);
    }
    public void setVariableResolver(String variableResolver) {
        this.variableResolver = variableResolver;
    }

    public String getViewHandler() {
        return (this.viewHandler);
    }
    public void setViewHandler(String viewHandler) {
        this.viewHandler = viewHandler;
    }

    protected String defaultLocale = null;
    public void setDefaultLocale(String newDefaultLocale) {
	defaultLocale = newDefaultLocale;
    }
    
    public String getDefaultLocale() {
	return defaultLocale;
    }

    protected ArrayList supportedLocales;
    public void addSupportedLocale(String localeToAdd) {
	if (null == supportedLocales) {
	    supportedLocales = new ArrayList();
	}
	supportedLocales.add(localeToAdd);
    }

    public List getSupportedLocales() {
	List result = null;
	if (null == supportedLocales) {
	    result = Collections.EMPTY_LIST;
	}
	else {
	    result = supportedLocales;
	}
	return result;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Action Listener:"+getActionListener());
        sb.append("\nMessage Bundle:"+getMessageBundle());
        sb.append("\nNavigation Handler:"+getNavigationHandler());
        sb.append("\nProperty Resolver:"+getPropertyResolver());
        sb.append("\nVariable Resolver:"+getVariableResolver());
        sb.append("\nView Handler:"+getViewHandler());
        return sb.toString();
    }

    
}
