/*
 * $Id: ConfigApplication.java,v 1.3 2003/08/22 22:30:01 eburns Exp $
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

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Action Listener:"+getActionListener());
        sb.append("\nNavigation Handler:"+getNavigationHandler());
        sb.append("\nProperty Resolver:"+getPropertyResolver());
        sb.append("\nVariable Resolver:"+getVariableResolver());
        sb.append("\nView Handler:"+getViewHandler());
        return sb.toString();
    }
}
