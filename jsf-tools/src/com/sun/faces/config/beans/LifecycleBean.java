/*
 * $Id: LifecycleBean.java,v 1.3 2004/02/04 23:46:07 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config.beans;


import java.util.ArrayList;
import java.util.List;


/**
 * <p>Configuration bean for <code>&lt;lifecycle&gt; element.</p>
 */

public class LifecycleBean {


    // -------------------------------------------------------------- Properties


    // --------------------------------------------- PhaseListenerHolder Methods


    private List phaseListeners = new ArrayList();


    public void addPhaseListener(String phaseListener) {
        if (!phaseListeners.contains(phaseListener)) {
            phaseListeners.add(phaseListener);
        }
    }


    public String[] getPhaseListeners() {
        String results[] = new String[phaseListeners.size()];
        return ((String[]) phaseListeners.toArray(results));
    }


    public void removePhaseListener(String phaseListener) {
        phaseListeners.remove(phaseListener);
    }


    // ----------------------------------------------------------------- Methods




}
