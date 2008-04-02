/*
 * $Id: RenderKitFactoryImpl.java,v 1.10 2003/03/13 01:06:30 eburns Exp $
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

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.Rule;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

public class RenderKitFactoryImpl extends RenderKitFactory {

//
// Protected Constants
//
    protected String renderKitId = null;
    protected String className = null;
    protected HashMap renderKits = null;
    protected Digester digester = null;

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
        digester = initConfig();
        renderKits = new HashMap();
        RenderKit renderKit = getRenderKit(
            RenderKitFactory.DEFAULT_RENDER_KIT);
        renderKits.put(RenderKitFactory.DEFAULT_RENDER_KIT, renderKit);
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

        if (RenderKitFactory.DEFAULT_RENDER_KIT.equals(renderKitId)) {
            throw new IllegalArgumentException(renderKitId);
        } else if (renderKits.containsKey(renderKitId)) {
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

        RenderKit renderKit = null;
        this.renderKitId = renderKitId;

        // If an instance already exists, return it.
        //
        synchronized(renderKits) {
            if (renderKits.containsKey(renderKitId)) {
                return ((RenderKit) renderKits.get(renderKitId));
            }
        }

        String fileName = "com/sun/faces/renderkit/RenderKitConfig.xml";
        InputStream in;
	Object [] params;
        try {
            in = this.getClass().getClassLoader().getResourceAsStream(
                fileName);
        } catch (Throwable t) {
	    params = new Object [] { fileName };
            throw new RuntimeException(Util.getExceptionMessage(Util.FILE_NOT_FOUND_ERROR_MESSAGE_ID, params));
        }
        
        try {
            digester.push(this);
            digester.parse(in);
            in.close();
        } catch (Throwable t) {
	    params = new Object [] { t.getMessage() };
            throw new IllegalStateException(Util.getExceptionMessage(Util.CANT_PARSE_FILE_ERROR_MESSAGE_ID, params));
        }

        Assert.assert_it(className != null);

        // Create an instance of the render kit.
        //
        try {
            Class kitClass = Util.loadClass(className, this);
            renderKit = (RenderKit)kitClass.newInstance();
        } catch (ClassNotFoundException cnf) {
	    params = new Object [] { cnf.getMessage() };
            throw new RuntimeException(Util.getExceptionMessage(Util.MISSING_CLASS_ERROR_MESSAGE_ID, params));
        } catch (InstantiationException ie) {
	    params = new Object [] { ie.getMessage() };
            throw new RuntimeException(Util.getExceptionMessage(Util.CANT_INSTANTIATE_CLASS_ERROR_MESSAGE_ID, params));
        } catch (IllegalAccessException ia) {
            throw new RuntimeException(ia.getMessage());
        }

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
     * Method is invoked through Digester when an Xml pattern is
     * recognized from Xml configuration file.
     * 
     * @param renderKitId The Render Kit Identifier
     * @param className The fully qualified class name.
     */ 
    public void setRenderKitClass(String renderKitId, String className) {
        if (renderKitId.equalsIgnoreCase(this.renderKitId)) {
            this.className = className;
        }
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

    private Digester initConfig() {
        Digester digester = new Digester();
        digester.setNamespaceAware(true);
        digester.setValidating(false);
        digester.setUseContextClassLoader(true);
        digester.addCallMethod("renderkit-config/renderkit",
            "setRenderKitClass", 2);
        digester.addCallParam("renderkit-config/renderkit/renderkit-id", 0);
        digester.addCallParam("renderkit-config/renderkit/class", 1);    

        return digester;
    }
}
