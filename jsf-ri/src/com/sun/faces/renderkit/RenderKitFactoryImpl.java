/*
 * $Id: RenderKitFactoryImpl.java,v 1.13 2003/12/17 15:13:47 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.renderkit;

import com.sun.faces.util.Util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;

import java.util.HashMap;
import java.util.Iterator;
import javax.faces.FacesException;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.context.FacesContext;

import com.sun.faces.util.Util;


public class RenderKitFactoryImpl extends RenderKitFactory {

//
// Protected Constants
//
    protected String renderKitId = null;
    protected String className = null;
    protected HashMap renderKits = null;

//
// Class Variables
//

// Attribute Instance Variables

// Relationship Instance Variables

//
// Constructors and Initializers
//
    /**
     * Constructor registers default Render kit.
     */
    public RenderKitFactoryImpl() {
        super();
        renderKits = new HashMap();
    }
    
    public void addRenderKit(String renderKitId, RenderKit renderKit) {

        if (renderKitId == null || renderKit == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

        synchronized(renderKits) {
            renderKits.put(renderKitId, renderKit);
        }
    }

    public RenderKit getRenderKit(String renderKitId) {

        if (renderKitId == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

        RenderKit renderKit = null;
        this.renderKitId = renderKitId;

        // If an instance already exists, return it.
        //
        synchronized(renderKits) {
            if (renderKits.containsKey(renderKitId)) {
		renderKit = (RenderKit) renderKits.get(renderKitId);
            }
        }

        return renderKit;
    }

    public RenderKit getRenderKit(String renderKitId, FacesContext context) {

        if (renderKitId == null || context == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        //PENDING (rogerk) do something with FacesContext ...
        //
        return getRenderKit(renderKitId);
    }

    public Iterator getRenderKitIds() {
        return (renderKits.keySet().iterator());
    }

}
