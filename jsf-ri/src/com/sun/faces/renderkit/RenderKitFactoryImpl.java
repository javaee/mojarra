/*
 * $Id: RenderKitFactoryImpl.java,v 1.17 2004/02/06 18:55:13 rlubke Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.renderkit;

import com.sun.faces.util.Util;

import javax.faces.context.FacesContext;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;

import java.util.HashMap;
import java.util.Iterator;


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
        addRenderKit(HTML_BASIC_RENDER_KIT, new RenderKitImpl());
    }


    public void addRenderKit(String renderKitId, RenderKit renderKit) {

        if (renderKitId == null || renderKit == null) {
            throw new NullPointerException(
                Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

        synchronized (renderKits) {
            renderKits.put(renderKitId, renderKit);
        }
    }


    public RenderKit getRenderKit(FacesContext context, String renderKitId) {

        if (renderKitId == null) {
            throw new NullPointerException(
                Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        //PENDING (rogerk) do something with FacesContext ...
        //
        // If an instance already exists, return it.
        //
        RenderKit renderKit = null;

        synchronized (renderKits) {
            if (renderKits.containsKey(renderKitId)) {
                renderKit = (RenderKit) renderKits.get(renderKitId);
            }
        }

        return renderKit;
    }


    public Iterator getRenderKitIds() {
        return (renderKits.keySet().iterator());
    }

}
