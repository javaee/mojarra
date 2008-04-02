/*
 * $Id: NavigationRuleBean.java,v 1.3 2004/02/04 23:46:08 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config.beans;


import java.util.ArrayList;
import java.util.List;


/**
 * <p>Configuration bean for <code>&lt;navigation-case&gt; element.</p>
 */

public class NavigationRuleBean extends FeatureBean {


    // -------------------------------------------------------------- Properties


    private String fromViewId = "*";
    public String getFromViewId() { return fromViewId; }
    public void setFromViewId(String fromViewId)
    { this.fromViewId = fromViewId; }


    private List navigationCases = new ArrayList();
    public NavigationCaseBean[] getNavigationCases() {
        NavigationCaseBean results[] =
            new NavigationCaseBean[navigationCases.size()];
        return ((NavigationCaseBean[]) navigationCases.toArray(results));
    }


    // -------------------------------------------------------------- Extensions


    // ----------------------------------------------------------------- Methods


    public void addNavigationCase(NavigationCaseBean navigationCase) {
        navigationCases.add(navigationCase);
    }


}
