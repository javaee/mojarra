/*
 * $Id: ConfigParser.java,v 1.2 2003/10/14 16:42:34 rkitain Exp $
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
	renderersByRendererType = new HashMap();
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

	prefix = prefix + "/attribute";
	digester.addObjectCreate(prefix,
				 "com.sun.faces.generate.ConfigAttribute");
	digester.addSetNext(prefix, "addAttribute",
			    "com.sun.faces.generate.ConfigAttribute");
        digester.addCallMethod(prefix + "/attribute-name",
                               "setAttributeName", 0);
        digester.addCallMethod(prefix + "/attribute-class",
                               "setAttributeClass", 0);

	
	prefix = "faces-config/render-kit";
	RenderKitRule rRule = new RenderKitRule(this);
	digester.addRule(prefix, rRule);
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
		cClasses = null; // component classes
	    Iterator iter = renderers.keySet().iterator();
	    String 
		className = null,
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

		parser.renderersByRendererType.put(rType, renderer);
	    }
		
	    return;
	}
    }

} // end of class ConfigParser
