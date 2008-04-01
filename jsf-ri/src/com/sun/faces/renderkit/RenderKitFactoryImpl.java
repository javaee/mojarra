/*
 * $Id: RenderKitFactoryImpl.java,v 1.3 2002/06/21 00:31:22 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
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
     */
    public void addRenderKit(String renderKitId, RenderKit renderKit) {

        ParameterCheck.nonNull(renderKitId);
        ParameterCheck.nonNull(renderKit);

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
     */ 
    public RenderKit getRenderKit(String renderKitId) {

        ParameterCheck.nonNull(renderKitId);

        RenderKit renderKit = null;
        this.renderKitId = renderKitId;

        // If an instance already exists, return it.
        //
        synchronized(renderKits) {
            if (renderKits.containsKey(renderKitId)) {
                return ((RenderKit) renderKits.get(renderKitId));
            }
        }

        String fileName = "RenderKitConfig.xml";
        InputStream in;
        try {
            in = this.getClass().getClassLoader().getResourceAsStream(
                fileName);
        } catch (Throwable t) {
            throw new RuntimeException("Error Opening File:"+fileName);
        }
        
        try {
            digester.push(this);
            digester.parse(in);
            in.close();
        } catch (Throwable t) {
            throw new IllegalStateException(
                "Unable to parse file:"+t.getMessage());
        }

        Assert.assert_it(className != null);

        // Create an instance of the render kit.
        //
        try {
            Class kitClass = Util.loadClass(className);
            renderKit = (RenderKit)kitClass.newInstance();
        } catch (ClassNotFoundException cnf) {
            throw new RuntimeException("Class Not Found:"+cnf.getMessage());
        } catch (InstantiationException ie) {
            throw new RuntimeException("Class Instantiation Exception:"+
                ie.getMessage());
        } catch (IllegalAccessException ia) {
            throw new RuntimeException("Illegal Access Exception:"+
                ia.getMessage());
        }

        // Add the newly created renderkit to the table.
        //
        synchronized(renderKits) {
            renderKits.put(renderKitId, renderKit);
        }

        return renderKit;
    }

    public RenderKit getRenderKit(String renderKitId, FacesContext context) {
	Assert.assert_it(false, "PENDING(): fixme");
	return null;
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
        digester.addCallMethod("renderkit-config/renderkit",
            "setRenderKitClass", 2);
        digester.addCallParam("renderkit-config/renderkit/renderkit-id", 0);
        digester.addCallParam("renderkit-config/renderkit/class", 1);    

        return digester;
    }
}
