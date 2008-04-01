/*
 * $Id: HtmlBasicRenderKit.java,v 1.35 2002/08/12 23:15:36 eburns Exp $
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
import java.util.Stack;
import java.util.Set;
import java.util.NoSuchElementException;

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
 * @version $Id: HtmlBasicRenderKit.java,v 1.35 2002/08/12 23:15:36 eburns Exp $
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

//
// Instance Variables
//

//
// Ivars used during parsing
//

    /**

    * Used during the Digester parsing.  Accrues HtmlBasicRenderer
    * instances during createAndAccrueRenderer() calls.

    */

    private ArrayList parse_renderersForCurrentComponent = null;

    /**

    * Used during Digester parsing.  Accrues AttributeStruct instances
    * during createAndAccrueRenderer() calls.

    */

    private Stack parse_attrStack;

    private Digester parse_digester = null;

//
// Ivars used during actual client lifetime
//

// Relationship Instance Variables

    /**

    * Keys are String rendererType, values are HtmlBasicRenderer instances

    */

    private Hashtable renderersByRendererType;

    /**

    * Keys are String componentType, values are ArrayList of
    * HtmlBasicRenderer instances.  Currently not used for returning
    * values, but we'll keep it anyway.

    */

    private Hashtable renderersByComponentType;
    private ArrayList componentClasses;

    /** 

    * Used in addComponentClass to test precondition.

    */
 
    private Class uiComponentClass = null;


//
// Constructors and Initializers    
//

    public HtmlBasicRenderKit() {
        super();
	parse_attrStack = new Stack();

	try {
	    uiComponentClass = 
		Util.loadClass("javax.faces.component.UIComponent");
	}
	catch (Throwable e) {
	    Assert.assert_it(false);
	}

	componentClasses = new ArrayList();
        parse_digester = initConfig();
        loadProperties();
    }


//
// Class methods
//

//
// General Methods
//

    private void loadProperties() {
        renderersByRendererType = new Hashtable();
        renderersByComponentType = new Hashtable();

        String fileName = "com/sun/faces/renderkit/html_basic/HtmlBasicRenderKit.xml";
        InputStream in;
        try {
            in = this.getClass().getClassLoader().getResourceAsStream(
                fileName);
        } catch (Throwable t) {
            throw new RuntimeException("Error Opening File:"+fileName);
        }
        try {
            parse_digester.push(this);
            parse_digester.parse(in);
            in.close();
        } catch (Throwable t) {
            throw new IllegalStateException(
                "Unable to parse file:"+t.getMessage());
        }
    }

    private Digester initConfig() {
        Digester parser = new Digester();

        parser.setNamespaceAware(true);
        parser.setValidating(false);
        parser.addCallMethod("*/component", "addRenderersToTable", 1);
        parser.addCallParam("*/component/type", 0);
        parser.addCallMethod("*/renderer", "createAndAccrueRenderer", 2);
        parser.addCallParam("*/renderer/type", 0);
        parser.addCallParam("*/renderer/class", 1);
        parser.addCallMethod("*/renderer/attribute", "storeAttributeData", 4);
        parser.addCallParam("*/renderer/attribute/name", 0);
        parser.addCallParam("*/renderer/attribute/display-name", 1);
        parser.addCallParam("*/renderer/attribute/description", 2);
        parser.addCallParam("*/renderer/attribute/type", 3);

        return parser;
    }

    /**
     * This method is invoked through the Digester as an Xml pattern
     * is encountered.  The value of the "type" (subelement of component)
     * is passed in as a parameter.  <P>

     * It takes the HtmlBasicRenderer instances which were created
     * during previous calls to createAndAccrueRenderer() and stored in
     * parse_renderersForCurrentComponent, and adds them to the
     * renderersByComponentType Hashtable. <P>

     *
     *
     * @param componentType The type of the component.
     */
    public void addRenderersToTable(String componentType) {
        if (parse_renderersForCurrentComponent != null && 
	    parse_renderersForCurrentComponent.size() > 0) {
	    try {
		addComponentClass(Util.loadClass(componentType));
            } 
	    catch (ClassNotFoundException cnf) {
                throw new RuntimeException("Class Not Found:"+
					   cnf.getMessage());
            } 
	    
            renderersByComponentType.put(componentType, 
					 parse_renderersForCurrentComponent);
            parse_renderersForCurrentComponent = new ArrayList();
        }
    }

    /**
     * This method is invoked through the Digester as an Xml pattern
     * is encountered.  The renderer type and renderer class name 
     * from the Xml file are passed in as parameters. <P>

     * Take the arguments and create a new HtmlBasicRenderer instance,
     * which is added: <P>

     * <OL>

     * <LI> to the renderersByType Hashtable

     * <LI> to the parse_renderersForCurrentComponent ArrayList

     * </OL>

     * <P>

     * In addition, this method takes any AttributeStruct instances put
     * on the parse_attrStack during calls to storeAttributeData() and
     * puts calls registerAttribute on the current HtmlBasicRenderer
     * instance.

     *
     *
     * @param rendererType The renderer type.
     * @param className The renderer class name (fully qualified).
     */
    public void createAndAccrueRenderer(String rendererType, String className){
	ParameterCheck.nonNull(rendererType);
	ParameterCheck.nonNull(className);

	HtmlBasicRenderer curRenderer = null;

	// Only add the renderer for this rendererType once
	if (null == (curRenderer = (HtmlBasicRenderer)
		     renderersByRendererType.get(rendererType))){
	    try {
		Class rendererClass = Util.loadClass(className);
		curRenderer = (HtmlBasicRenderer)rendererClass.newInstance();
	    } catch (ClassNotFoundException cnf) {
		throw new RuntimeException("Class Not Found:"+cnf.getMessage());
	    } catch (InstantiationException ie) {
		throw new RuntimeException("Class Instantiation Exception:"+
					   ie.getMessage());
	    } catch (IllegalAccessException ia) {
		throw new RuntimeException("Illegal Access Exception:"+
					   ia.getMessage());
	    }
	    renderersByRendererType.put(rendererType, curRenderer);
	}
	if (parse_renderersForCurrentComponent == null) {
	    parse_renderersForCurrentComponent = new ArrayList();
	}
	
	synchronized(parse_renderersForCurrentComponent) {
	    parse_renderersForCurrentComponent.add(curRenderer);
	}
	
	// handle attribute descriptors
	AttributeStruct struct = null;
	while (!parse_attrStack.isEmpty()) {
	    struct = (AttributeStruct) parse_attrStack.pop();
	    // Only add this attribute if the renderer doesn't have one 
	    // with this name.
	    if (!curRenderer.hasAttributeWithName(struct.name)) {
		curRenderer.registerAttribute(struct.name,
					      struct.displayName,
					      struct.description,
					      struct.typeClassName);
	    }
	}
    }

    public void storeAttributeData(String name, String displayName, 
				  String description, String typeClassName) {
	AttributeStruct struct = new AttributeStruct();
	struct.name = name;
	struct.displayName = displayName;
	struct.description = description;
	struct.typeClassName = typeClassName;
	parse_attrStack.push(struct);
    }

    class AttributeStruct extends Object {
	public String name = null;
	public String displayName = null;
	public String description = null;
	public String typeClassName = null;
    }    


//
// Methods From RenderKit
//

    public void addComponentClass(Class componentClass) {
        if (componentClass == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        
	if (!uiComponentClass.isAssignableFrom(componentClass)) {
	    throw new IllegalArgumentException(componentClass.getName() + 
					       " does not implement UIComponent");
	}
	componentClasses.add(componentClass);
    }


    public void addRenderer(String rendererType, Renderer renderer) {
        if (rendererType == null || renderer == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

	if (null != renderersByRendererType.get(rendererType)) {
	    throw new IllegalArgumentException("Renderer for " + rendererType +
					       " already exists");
	}
	renderersByRendererType.put(rendererType, renderer);
    }

    public Iterator getComponentClasses() {
	return componentClasses.iterator();
    }

    /**
     * This method returns a renderer instance given a renderer type.
     *
     * @param rendererType The renderer type.
     * @returns Renderer A Renderer instance.
     * @throws FacesException If the renderer instance is not found for
     *         the given renderer type.
     */
    public Renderer getRenderer(String rendererType) {

        if (rendererType == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

        Assert.assert_it(renderersByRendererType != null);

        Renderer renderer = (Renderer)renderersByRendererType.get(rendererType);
        if (renderer == null) {
            throw new FacesException("Renderer Not Found For Renderer Type:"+
                rendererType);
        }

        return renderer;
    }

    /**
     * This method returns an iteration of Renderer types.
     *
     * @returns Iteration The iteration of Renderer types.
     */
    public Iterator getRendererTypes() {

        Assert.assert_it(renderersByRendererType != null);

        return renderersByRendererType.keySet().iterator();
    }

    public Iterator getRendererTypes(String componentType) {
        if (componentType == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

	Iterator result = null;
	Set rendererTypeKeySet = renderersByRendererType.keySet();

	result = new RendererTypeForComponentTypeIterator(rendererTypeKeySet,
							  componentType);
	
	return result;
    }

    protected class RendererTypeForComponentTypeIterator extends Object implements Iterator {

	String rendererTypes[] = null;
	String componentType = null;
	int curIndex = 0;

	protected RendererTypeForComponentTypeIterator(Set rendererTypeKeySet, 
						      String newComponentType){
	    rendererTypes = new String[rendererTypeKeySet.size()];
	    rendererTypeKeySet.toArray(rendererTypes);
	    componentType = newComponentType;
	    curIndex = 0;
	}

	/**

	* @return the index of the next rendererType that has a Renderer
	* that supports our componentType, -1 if no match.  

	*/
	protected int getNextMatchingIndex() {
	    int i, result = -1;
	    Renderer curRenderer = null;

	    for (i = curIndex; i < rendererTypes.length; i++) {
		curRenderer = (Renderer)
		    renderersByRendererType.get(rendererTypes[i]);
		Assert.assert_it(null != curRenderer);
		
		if (curRenderer.supportsComponentType(componentType)) {
		    result = i;
		    break;
		}
	    }

	    return result;
	}
	
	public boolean hasNext() {
	    boolean result = false;
	    result = (-1 != getNextMatchingIndex());
	    return result;
	}

	public Object next() {
	    int i;
	    Renderer curRenderer = null;
	    Object result = null;

	    if (-1 == (i = getNextMatchingIndex())) {
		throw new NoSuchElementException();
	    }
	    result = rendererTypes[curIndex = i];
	    curIndex++;
	    return result;
	}

	public void remove() {
	    throw new UnsupportedOperationException();
	}
    }

    public Iterator getRendererTypes(UIComponent component) {
        if (component == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

	String componentType = component.getComponentType();
	return this.getRendererTypes(componentType);
    }

    // The test for this class is in TestRenderKit.java

} // end of class HtmlBasicRenderKit

