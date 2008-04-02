/*
 * $Id: RenderKitFactoryImpl.java,v 1.11 2003/07/08 15:38:33 eburns Exp $
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

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

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
    
    /**
     * Adds the {@link RenderKit} instance to the internal set.
     *
     * @param renderKitId The RenderKit identifier for the RenderKit
     *        that will be added.
     * @param renderKit The RenderKit instance that will be added.
     * @exception NullPointerException if <code>renderKitId</code>
     *  or <code>renderKit</code> arguments are <code>null</code>
     */
    public void addRenderKit(String renderKitId, RenderKit renderKit) {

        if (renderKitId == null || renderKit == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

        if (renderKits.containsKey(renderKitId)) {
            throw new IllegalArgumentException(renderKitId);
        }
        synchronized(renderKits) {
            renderKits.put(renderKitId, renderKit);
        }
    }

    /**
     * Return a {@link RenderKit} instance for the given render
     * kit identifier.  If a {@link RenderKit} instance does not
     * exist for the identifier, create one and add it to the
     * internal table.
     *
     * @param renderKitId A RenderKit identifier.
     * @returns RenderKit A RenderKit instance.
     * @exception NullPointerException if <code>renderKitId</code>
     *  argument is <code>null</code>
     */ 
    public RenderKit getRenderKit(String renderKitId) {

        if (renderKitId == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

        RenderKitImpl renderKit = null;
        this.renderKitId = renderKitId;

        // If an instance already exists, return it.
        //
        synchronized(renderKits) {
            if (renderKits.containsKey(renderKitId)) {
                return ((RenderKit) renderKits.get(renderKitId));
            }
        }

        // Not found, so create an instance of RenderKitImpl.
        //
        renderKit = new RenderKitImpl();

        // Add the newly created renderkit to the table.
        //
        synchronized(renderKits) {
            renderKits.put(renderKitId, renderKit);
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


    /**
     * Return an iteration of render kit identifiers maintained
     * by this factory instance.
     *
     * @returns Iterator The iteration of identfiers.
     */
    public Iterator getRenderKitIds() {
        return (renderKits.keySet().iterator());
    }

}
