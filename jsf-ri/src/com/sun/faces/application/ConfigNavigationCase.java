/*
 * $Id: ConfigNavigationCase.java,v 1.3 2004/02/04 23:40:49 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.FacesException;

import com.sun.faces.util.Util;


/**
 * <p>Config Bean for a Navigation Rule .</p>
 */
public class ConfigNavigationCase {

    private String fromViewId = null;
    private String fromAction = null;
    private String fromOutcome = null;
    private String toViewId = null;
    private String key = null;

    public String getFromViewId() {
        return (this.fromViewId);
    }
    public void setFromViewId(String fromViewId) {
        this.fromViewId = fromViewId;
    }

    public String getFromAction() {
        return (this.fromAction);
    }
    public void setFromAction(String fromAction) {
        this.fromAction= fromAction;
    }

    public String getFromOutcome() {
        return (this.fromOutcome);
    }
    public void setFromOutcome(String fromOutcome) {
        this.fromOutcome = fromOutcome;
    }

    public String getToViewId() {
        return (this.toViewId);
    }
    public void setToViewId(String toViewId) {
        this.toViewId = toViewId;
    }

    protected String redirect = null;
    public boolean hasRedirect() {
	return null != redirect;
    }
    public void setRedirect(String redirect) {
	this.redirect = redirect;
    }

    /**
     * The "key" is defined as the combination of
     * <code>from-view-id</code><code>from-action</code>
     * <code>from-outcome</code>.
     */
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("FROM VIEW ID:"+getFromViewId());
        sb.append("\nFROM ACTION:"+getFromAction());
        sb.append("\nFROM OUTCOME:"+getFromOutcome());
        sb.append("\nTO VIEW ID:"+getToViewId());
	sb.append("\nREDIRECT:"+hasRedirect());
        return sb.toString();
    }
}
