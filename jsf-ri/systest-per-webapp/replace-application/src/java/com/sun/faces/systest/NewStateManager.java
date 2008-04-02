/*
 * $Id: NewStateManager.java,v 1.1 2004/10/18 22:46:03 edburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */



package com.sun.faces.systest;

import javax.faces.application.StateManager;
import javax.faces.application.StateManagerWrapper;

public class NewStateManager extends StateManagerWrapper {

    private StateManager oldStateManager = null;

    public NewStateManager(StateManager oldStateManager) {
	this.oldStateManager = oldStateManager;
    }

    public StateManager getWrapped() {
	return oldStateManager;
    }
}
