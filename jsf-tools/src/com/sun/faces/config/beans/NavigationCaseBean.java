/*
 * $Id: NavigationCaseBean.java,v 1.2 2004/01/27 20:13:41 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config.beans;


/**
 * <p>Configuration bean for <code>&lt;navigation-case&gt; element.</p>
 */

public class NavigationCaseBean extends FeatureBean {


    // -------------------------------------------------------------- Properties


    private String fromAction = "*";
    public String getFromAction() { return fromAction; }
    public void setFromAction(String fromAction)
    { this.fromAction = fromAction; }


    private String fromOutcome = "*";
    public String getFromOutcome() { return fromOutcome; }
    public void setFromOutcome(String fromOutcome)
    { this.fromOutcome = fromOutcome; }


    private boolean redirect = false;
    public boolean isRedirect() { return redirect; }
    public void setRedirect(boolean redirect) { this.redirect = redirect; }
    public void setRedirectTrue(String dummy) { this.redirect = true; }

    private String toViewId = "*";
    public String getToViewId() { return toViewId; }
    public void setToViewId(String toViewId)
    { this.toViewId = toViewId; }


    // -------------------------------------------------------------- Extensions


    // ----------------------------------------------------------------- Methods


}
