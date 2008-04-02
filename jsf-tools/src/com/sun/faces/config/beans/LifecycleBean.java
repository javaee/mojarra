/*
 * $Id: LifecycleBean.java,v 1.2 2004/01/27 20:13:38 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
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
