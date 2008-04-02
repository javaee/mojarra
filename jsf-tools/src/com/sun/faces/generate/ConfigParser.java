/*
 * $Id: ConfigParser.java,v 1.4 2003/12/17 15:16:36 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.generate;

import java.io.Reader;
import java.io.IOException;
import org.xml.sax.SAXException;
import org.xml.sax.InputSource;
import org.xml.sax.EntityResolver;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Collections;
import java.util.StringTokenizer;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.Rule;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

class ConfigParser extends Object {
    //
    // Protected Constants
    //
    
    // The public identifier of our DTD
    protected static String CONFIG_DTD_PUBLIC_ID =
        "-//Sun Microsystems, Inc.//DTD JavaServer Faces Config 1.0//EN";

    //
    // Class Variables
    //

    static Log log = LogFactory.getLog(ConfigParser.class);



    //
    // Instance Variables
    //
    
    // Attribute Instance Variables
    
    // Relationship Instance Variables

    /**
     * <p>The Digester instance we will use to parse configuration files.</p>
     */
    protected Digester digester = null;

    /**
     * <p>Keys are fully qualified java class names of components</p>
     *
     * <p>Values are <code>List</code> of <code>String</code>, where
     * each element is a renderer type for that component.</p>
     *
     */

    Map renderersByComponentClass = null;

    /**
     * <p>Keys are rendererTypes, values are <code>ConfigRenderer</code>
     * beans</p>
     */
    Map renderersByRendererType = null;

    /**
     * <p>Keys are component types; Values are <code>List</code> of 
     * <code>String</code>, where each element is a renderer type for 
     * that component.</p>
     */
    Map renderersByComponentType = null;

    /**
     * <p>Keys are componentTypes, values are <code>ConfigComponent</code>
     * beans</p>
     */
    Map componentsByComponentType = null;

    /**
     * <p>These components have no associated <code>Renderer</code>.
     */
    List componentsWithNoRenderer = null;

    //
    // Constructors and Initializers    
    //
    
    public ConfigParser() {
	super();
	digester = new Digester();
	try {
	    java.net.URL url = 
		this.getClass().getResource("/web-facesconfig_1_0.dtd");
	    digester.register(CONFIG_DTD_PUBLIC_ID, 
			      replaceOccurrences(url.toExternalForm(),
						 " ", "%20"));
	}
	catch (Exception e) {
	    // if we can't get the DTD, then let's hope it can be
	    // resolved and fetch dynamically.
	}
	digester.setValidating(true);
        configureRules();
	renderersByComponentClass = new HashMap();
	renderersByComponentType = new HashMap();
	renderersByRendererType = new HashMap();
	componentsByComponentType = new HashMap();
	componentsWithNoRenderer = new ArrayList();
    }

    //
    // Class methods
    //

    // 
    // helper methods
    // 

    protected void configureRules() {
        String prefix = "faces-config/render-kit";
	
        digester.addObjectCreate(prefix, 
				 "com.sun.faces.generate.ConfigRenderKit");
	prefix = prefix + "/renderer";
	
        digester.addObjectCreate(prefix, 
				 "com.sun.faces.generate.ConfigRenderer");
        digester.addSetNext(prefix, "addRenderer", 
			    "com.sun.faces.generate.ConfigRenderer");
        digester.addCallMethod(prefix + "/renderer-type",
                               "setRendererType", 0);
        digester.addCallMethod(prefix + "/supported-component-class/component-class",
                               "addComponentClass", 0);

	digester.addObjectCreate(prefix + "/supported-component-type",
            "com.sun.faces.generate.ConfigComponentType");
        digester.addSetNext(prefix + "/supported-component-type", "addComponentType", 
            "com.sun.faces.generate.ConfigComponentType");
        digester.addCallMethod(prefix + "/supported-component-type/component-type",
            "setComponentType", 0);
        digester.addCallMethod(prefix + "/supported-component-type/attribute-name",
            "addAttributeName", 0);

	prefix = prefix + "/attribute";
	digester.addObjectCreate(prefix,
				 "com.sun.faces.generate.ConfigAttribute");
	digester.addSetNext(prefix, "addAttribute",
			    "com.sun.faces.generate.ConfigAttribute");
        digester.addCallMethod(prefix + "/attribute-name",
                               "setAttributeName", 0);
        digester.addCallMethod(prefix + "/attribute-class",
                               "setAttributeClass", 0);
        digester.addCallMethod(prefix + "/description",
                               "setDescription", 0);
        digester.addCallMethod(prefix + "/attribute-extension/tag-attribute",
                               "setTagAttribute", 0);
	
	prefix = "faces-config/render-kit";
	RenderKitRule rRule = new RenderKitRule(this);
	digester.addRule(prefix, rRule);

	// Component Patterns

        prefix = "faces-config";
	
        digester.addObjectCreate(prefix, 
            "com.sun.faces.generate.ConfigComponents");
	prefix = prefix + "/component";
        digester.addObjectCreate(prefix, 
	    "com.sun.faces.generate.ConfigComponent");
        digester.addSetNext(prefix, "addComponent", 
	    "com.sun.faces.generate.ConfigComponent");
        digester.addCallMethod(prefix + "/component-type",
            "setComponentType", 0);
        digester.addCallMethod(prefix + "/component-class",
            "setComponentClass", 0);

	// for nested "property" elements within "component" elements
	//
	prefix = prefix + "/property";
	digester.addObjectCreate(prefix,
            "com.sun.faces.generate.ConfigAttribute");
	digester.addSetNext(prefix, "addProperty",
            "com.sun.faces.generate.ConfigAttribute");
        digester.addCallMethod(prefix + "/property-name",
            "setAttributeName", 0);
        digester.addCallMethod(prefix + "/property-class",
            "setAttributeClass", 0);
        digester.addCallMethod(prefix + "/description",
                               "setDescription", 0);
        digester.addCallMethod(prefix + "/property-extension/tag-attribute",
                               "setTagAttribute", 0);

	// for nested "attribute" elements within "component" elements
	//
	prefix = "faces-config/component/attribute";
	digester.addObjectCreate(prefix,
            "com.sun.faces.generate.ConfigAttribute");
	digester.addSetNext(prefix, "addProperty",
            "com.sun.faces.generate.ConfigAttribute");
        digester.addCallMethod(prefix + "/attribute-name",
            "setAttributeName", 0);
        digester.addCallMethod(prefix + "/attribute-class",
            "setAttributeClass", 0);
        digester.addCallMethod(prefix + "/description",
                               "setDescription", 0);
        digester.addCallMethod(prefix + "/attribute-extension/tag-attribute",
                               "setTagAttribute", 0);

	prefix = "faces-config";
	ComponentsRule cRule = new ComponentsRule(this);
	digester.addRule(prefix, cRule);
    }
    
    //
    // General Methods
    //

    EntityResolver getEntityResolver() {
	return digester.getEntityResolver();
    }

    void setEntityResolver(EntityResolver newResolver) {
	digester.setEntityResolver(newResolver);
    }

    EntityResolver getDefaultEntityResolver() {
	return digester;
    }

    void parseConfig(Reader input) throws IOException, SAXException {        
	digester.clear();
	digester.parse(input);
    }

    void parseConfig(InputSource input) throws IOException, SAXException {   
	digester.clear();
	digester.parse(input);
    }

    List getRendererTypesForClass(String absoluteClass) {
	List result = (List) renderersByComponentClass.get(absoluteClass);
	return result;
    }

    Iterator getRendererTypes() {
	Iterator result = null;
	if (null == renderersByRendererType) {
	    result = Collections.EMPTY_LIST.iterator();
	}
	else {
	    result = renderersByRendererType.keySet().iterator();
	}
	return result;
    }

    List getClassesForRendererType(String rendererType) {
	List result = null;

	if (null == renderersByRendererType) {
	    result = Collections.EMPTY_LIST;
	}
	else {
	    result = ((ConfigRenderer)renderersByRendererType.get(rendererType)).getComponentClasses();
	}
	return result;
    }

    Map getAttributesForRenderer(String rendererType) {
	Map result = null;

	if (null == renderersByRendererType) {
	    result = Collections.EMPTY_MAP;
	}
	else {
	    result = (Map) ((ConfigRenderer)renderersByRendererType.get(rendererType)).getAttributes();
	}
	return result;
    }

    String getRendererAttributeClass(String rendererType, String attributeName) {
	String result = null;
        ConfigRenderer cr = (ConfigRenderer)renderersByRendererType.get(
	    rendererType);
	if (cr == null) {
	    return null;
	}
	return cr.getAttributeClass(attributeName);
    }

    String getRendererAttributeDescription(String rendererType, String attributeName) {
	String result = null;
        ConfigRenderer cr = (ConfigRenderer)renderersByRendererType.get(
	    rendererType);
	if (cr == null) {
	    return null;
	}
	return cr.getAttributeDescription(attributeName);
    }
	    
    String getRendererAttributeTagAttribute(String rendererType, String attributeName) {
	String result = null;
        ConfigRenderer cr = (ConfigRenderer)renderersByRendererType.get(
	    rendererType);
	if (cr == null) {
	    return null;
	}
	return cr.getAttributeTagAttribute(attributeName);
    }

    Map getAttributesForComponent(String componentType) {
	if (null == componentsByComponentType || null == componentType) {
	    return Collections.EMPTY_MAP;
	} 
        ConfigComponent cc = (ConfigComponent)componentsByComponentType.
	    get(componentType);
	if (cc == null) {
	    return Collections.EMPTY_MAP;
	}
	return cc.getProperties();
    }

    Iterator getComponentTypes(String rendererType) {
	List results = new ArrayList();
	if (null == renderersByRendererType) {
	    return Collections.EMPTY_LIST.iterator();
	}
        Iterator iter = ((ConfigRenderer)renderersByRendererType.
            get(rendererType)).getComponentTypes().iterator();
	while (iter.hasNext()) {
	    ConfigComponentType cc = (ConfigComponentType)iter.next();
	    String typeName = cc.getComponentType();
	    results.add(typeName);
	}
	if (results.size() > 0) {
	    return results.iterator();
	} else {
            return Collections.EMPTY_LIST.iterator();
        }
    }

    Iterator getComponentTypes() {
	Iterator result = null;
	if (null == componentsByComponentType) {
	    result = Collections.EMPTY_LIST.iterator();
	}
	else {
	    result = componentsByComponentType.keySet().iterator();
	}
	return result;
    }

    /**
     * <p> Returns a List of the supported attribute names for a component type
     * and renderer type.  This would correspond to the nested 
     * <code><attribute-name></code> element within <code><supported-component-type>
     * </code>.
     */
    List getSupportedAttributeNames(String componentType, String rendererType) {
        if (null == renderersByRendererType) {
	    return Collections.EMPTY_LIST;
	}
        ConfigRenderer renderer = (ConfigRenderer)renderersByRendererType.get(rendererType);
        if (renderer == null) {
	    return Collections.EMPTY_LIST;
	}
	List componentTypes = renderer.getComponentTypes();
	if (componentTypes == Collections.EMPTY_LIST) {
	    return Collections.EMPTY_LIST;
	}
	List result = Collections.EMPTY_LIST;
	Iterator iter = componentTypes.iterator();
        while (iter.hasNext()) {
	    ConfigComponentType cType = (ConfigComponentType)iter.next();
	    if (cType.getComponentType().equals(componentType)) {
	        result = cType.getAttributeNames();
		break;
	    }
	}
	return result;
    }

    String getComponentPropertyClass(String componentType, String propertyName) {
	String result = null;
        ConfigComponent cc = (ConfigComponent)componentsByComponentType.get(
	    componentType);
	if (cc == null) {
	    return null;
	}
	return cc.getPropertyClass(propertyName);
    }

    String getComponentPropertyDescription(String componentType, String propertyName) {
	String result = null;
        ConfigComponent cc = (ConfigComponent)componentsByComponentType.get(
	    componentType);
	if (cc == null) {
	    return null;
	}
	return cc.getPropertyDescription(propertyName);
    }
	    
    String getComponentPropertyTagAttribute(String componentType, String propertyName) {
	String result = null;
        ConfigComponent cc = (ConfigComponent)componentsByComponentType.get(
	    componentType);
	if (cc == null) {
	    return null;
	}
	return cc.getPropertyTagAttribute(propertyName);
    }

    boolean componentHasRenderer(String componentType) {
        boolean result = true;
	if (componentsWithNoRenderer.contains(componentType)) {
	    result = false;
	}
	return result;
    }

    // 
    // Class methods
    //

    /**
     * @return src with all occurrences of "from" replaced with "to".
     */

    public static String replaceOccurrences(String src, 
					    String from,
					    String to) {
	// a little optimization: don't bother with strings that don't
	// have any occurrences to replace.
	if (-1 == src.indexOf(from)) {
	    return src;
	}
	StringBuffer result = new StringBuffer(src.length());
	StringTokenizer toker = new StringTokenizer(src, from, true);
	String curToken = null;
	while (toker.hasMoreTokens()) {
	    // if the current token is a delimiter, replace it with "to"
	    if ((curToken = toker.nextToken()).equals(from)) {
		result.append(to);
	    }
	    else {
		// it's not a delimiter, just output it.
		result.append(curToken);
	    }
	}
	
	
	return result.toString();
    }

    final class RenderKitRule extends Rule {
	ConfigParser parser = null;
	
	public RenderKitRule(ConfigParser newParser) {
	    super();
	    parser = newParser;
	}
	public void end(String namespace, String name) throws Exception {
	    ConfigRenderKit cr = (ConfigRenderKit) this.digester.peek();
	    ConfigRenderer renderer = null;
	    Map renderers = cr.getRenderers();
	    List
		rList = null,
		cClasses = null,
		rendererList = null,
		cTypes = null; // component classes
	    Iterator iter = renderers.keySet().iterator();
	    String 
		className = null,
	        cType = null,
		rType = null;
	    while (iter.hasNext()) {
		rType = (String) iter.next();
		renderer = (ConfigRenderer) renderers.get(rType);
		
		// if this renderer has declared that it renders
		// one or more component classes
		if (null != (cClasses = renderer.getComponentClasses())) {
		    // obtain the (possibly non-existant) List of renderers 
		    // for each class supported by this renderer.
		    for (int i = 0, len = cClasses.size(); i < len; i++) {
			className = (String) cClasses.get(i);
			if (null == (rList = (List)
				     parser.renderersByComponentClass.get(className))){
			    rList = new ArrayList();
			    parser.renderersByComponentClass.put(className,
								 rList);
			}
			rList.add(rType);
		    }
		}

		// Build renderer by component type view..
		//
		if (null != (cTypes = renderer.getComponentTypes())) {
		    for (int i = 0, len = cTypes.size(); i < len; i++) {
			cType = ((ConfigComponentType)cTypes.get(i)).getComponentType();
			if (null == (rendererList = (List)
				     parser.renderersByComponentType.get(cType))){
			    rendererList = new ArrayList();
			    parser.renderersByComponentType.put(cType, rendererList);
			}
			rendererList.add(rType);
		    }
		}

		parser.renderersByRendererType.put(rType, renderer);
	    }
	    return;
	}
    }

    final class ComponentsRule extends Rule {
	ConfigParser parser = null;
	
	public ComponentsRule(ConfigParser newParser) {
	    super();
	    parser = newParser;
	}
	public void end(String namespace, String name) throws Exception {
	    ConfigComponents cc = (ConfigComponents) this.digester.peek();
	    ConfigComponent component = null;
	    Map components = cc.getComponents();

	    String componentType = null;
	    Iterator iter = components.keySet().iterator();
	    while (iter.hasNext()) {
	        componentType = (String)iter.next();
		component = (ConfigComponent)components.get(componentType);
		parser.componentsByComponentType.put(componentType, component);
	    }
	    // Keep track of the components that are not associated with a renderer..
	    //
            Iterator componentTypes = componentsByComponentType.keySet().iterator();
	    while (componentTypes.hasNext()) {
		componentType = (String)componentTypes.next();
	        if (parser.renderersByComponentType.get(componentType) == null) {
		    componentsWithNoRenderer.add(componentType);
		}
	    }
	}
    }

} // end of class ConfigParser
