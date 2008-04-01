/*
 * $Id: HtmlBasicRenderKit.java,v 1.31 2002/07/17 22:34:03 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// HtmlBasicRenderKit.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.Util;

import org.apache.commons.digester.AbstractObjectCreationFactory;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.Rule;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

import org.xml.sax.Attributes;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.FacesException;
import javax.faces.render.RenderKit;
import javax.faces.render.Renderer;

/**
 *
 *  <B>HtmlBasicRenderKit</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: HtmlBasicRenderKit.java,v 1.31 2002/07/17 22:34:03 jvisvanathan Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class HtmlBasicRenderKit extends RenderKit
{
//
// Protected Constants
//

//
// Class Variables
//

/**

 * used to ensure thread safety.

 */

//
// Instance Variables
//
    private Hashtable rendererTable;
    private ArrayList renderers = null;
    private Hashtable renderersTable;
    private String componentType = null;
    private Digester digester = null;

// Attribute Instance Variables

// Relationship Instance Variables

//
// Constructors and Initializers    
//

    public HtmlBasicRenderKit() {
        super();
        digester = initConfig();
        loadProperties();
    }


//
// Class methods
//

//
// General Methods
//

    private void loadProperties() {
        rendererTable = new Hashtable();
        renderersTable = new Hashtable();

        String fileName = "HtmlBasicRenderKit.xml";
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
    }

    private Digester initConfig() {
        Digester digester = new Digester();

        digester.setNamespaceAware(true);
        digester.setValidating(false);
        digester.addCallMethod("*/component", "setRenderers", 1);
        digester.addCallParam("*/component/type", 0);
        digester.addFactoryCreate("*/renderer", new RendererCreateFactory());
        digester.addCallMethod("*/renderer/attribute", "registerAttribute", 4);
        digester.addCallParam("*/renderer/attribute/name", 0);
        digester.addCallParam("*/renderer/attribute/display-name", 1);
        digester.addCallParam("*/renderer/attribute/description", 2);
        digester.addCallParam("*/renderer/attribute/type", 3);

        return digester;
    }

    /**
     * The factory class which is invoked by the digester to instantiate
     * a Renderer object.  The digester places the created renderer instance 
     * on its stack when this method completes, which is necessary to
     * allow the subsequent digester rules to process the attributes
     * for the renderer.
     */
    private class RendererCreateFactory extends AbstractObjectCreationFactory {
	public Object createObject(org.xml.sax.Attributes attributes) {

            String rendererType = attributes.getValue("type");
	    String rendererClassName = attributes.getValue("classname");

            Assert.assert_it(rendererClassName != null);
	    Assert.assert_it(rendererType != null);

            Renderer renderer = null;

            try {
                Class rendererClass = Util.loadClass(rendererClassName);
                renderer = (Renderer)rendererClass.newInstance();

            } catch (ClassNotFoundException cnf) {
                throw new RuntimeException("Class Not Found:"+
                          cnf.getMessage());

            } catch (InstantiationException ie) {
                throw new RuntimeException("Class Instantiation Exception:"+
                          ie.getMessage());

            } catch (IllegalAccessException ia) {
                throw new RuntimeException("Illegal Access Exception:"+
                          ia.getMessage());
            }

            rendererTable.put(rendererType, renderer);
            if (renderers == null) {
                renderers = new ArrayList();
            }       
            synchronized(renderers) {
                renderers.add(renderer);
            }
	    return renderer;
	}
    }


    /**
     * This method is invoked through the Digester as an Xml pattern
     * is encountered.  The value of the "type" (subelement of component)
     * is passed in as a parameter.
     *
     * @param componentType The type of the component.
     */
    public void setRenderers(String componentType) {
        if (componentType != null) {
            this.componentType = componentType;
        }
        if (renderers != null && renderers.size() > 0) {
            renderersTable.put(this.componentType, renderers);
            renderers = new ArrayList();
        }
    }

//
// Methods From RenderKit
//
    /**
     * This method returns a renderer instance given a renderer type.
     *
     * @param rendererType The renderer type.
     * @returns Renderer A Renderer instance.
     * @throws FacesException If the renderer instance is not found for
     *         the given renderer type.
     */
    public Renderer getRenderer(String rendererType) {

        ParameterCheck.nonNull(rendererType);
        Assert.assert_it(rendererTable != null);

        Renderer renderer = (Renderer)rendererTable.get(rendererType);
        if (renderer == null) {
            throw new FacesException("Renderer Not Found For Renderer Type:"+
                rendererType);
        }

        return renderer;
    }

    /**
     * This method returns an iteration of Renderer instances
     * given a UIComponent instance.
     *
     * @param component The UIComponent instance.
     * @returns Iterator the iteration of Renderer instances.
     */
    public Iterator getRenderers(UIComponent component) {

        ParameterCheck.nonNull(component);

        String componentType = component.getComponentType();
        Assert.assert_it(componentType != null);

        Assert.assert_it(renderersTable != null);
        
        ArrayList renderers = (ArrayList)renderersTable.get(componentType);
        Assert.assert_it(renderers != null);
        
        return renderers.listIterator();
    }

    /**
     * This method returns an iteration of Renderer instances
     * given a component type.
     *
     * @param component The component type.
     * @returns Iterator the iteration of Renderer instances.
     */
    public Iterator getRenderers(String componentType) {

        ParameterCheck.nonNull(componentType);

        Assert.assert_it(renderersTable != null);

        ArrayList renderers = (ArrayList)renderersTable.get(componentType);
        Assert.assert_it(renderers != null);

        return renderers.listIterator();
    }

    /**
     * This method returns an iteration of Renderer types.
     *
     * @returns Iteration The iteration of Renderer types.
     */
    public Iterator getRendererTypes() {

        Assert.assert_it(rendererTable != null);

        return rendererTable.keySet().iterator();
    }
    
   
    public Iterator getRendererTypes(UIComponent component) {
        return null;
    }    

    public void addComponentClass(Class componentClass) {
    }
    
   
    public Iterator getComponentClasses() {
        return null;
    }
    
    
    public void addRenderer(String rendererType, Renderer renderer) {
    }
    
  
    public Iterator getRendererTypes(String componentType) {
        return null;
    }
    
    // The test for this class is in TestRenderKit.java

} // end of class HtmlBasicRenderKit

