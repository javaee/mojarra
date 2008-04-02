/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.systest.lifecycle;

import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.context.FacesContext;
import javax.faces.FacesException;
import java.util.Iterator;

public class RenderKitFactoryWrapper extends RenderKitFactory {
    
    private RenderKitFactory oldFactory = null;
    
    public RenderKitFactoryWrapper(RenderKitFactory yourOldFactory) {
	oldFactory = yourOldFactory;
    }
    
    public void addRenderKit(String renderKitId,
			     RenderKit renderKit) {
	oldFactory.addRenderKit(renderKitId, renderKit);
    }

    public RenderKit getRenderKit(FacesContext context, String renderKitId) {
	return oldFactory.getRenderKit(context, renderKitId);
    }

    public Iterator getRenderKitIds() {
	return oldFactory.getRenderKitIds();
    }

    public String toString() {
	return "RenderKitFactoryWrapper";
    }


    
}
