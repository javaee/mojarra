/*
 * $Id: NewViewHandler.java,v 1.1 2004/10/18 22:46:04 edburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */



package com.sun.faces.systest;

import javax.faces.application.ViewHandler;
import javax.faces.application.ViewHandlerWrapper;

public class NewViewHandler extends ViewHandlerWrapper {

    private ViewHandler oldViewHandler = null;

    public NewViewHandler(ViewHandler oldViewHandler) {
	this.oldViewHandler = oldViewHandler;
    }

    public ViewHandler getWrapped() {
	return oldViewHandler;
    }
}
