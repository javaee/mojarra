/*
 * $Id: HtmlBasicRenderKit.java,v 1.6 2001/11/29 00:12:33 edburns Exp $
 *
 * Copyright 2000-2001 by Sun Microsystems, Inc.,
 * 901 San Antonio Road, Palo Alto, California, 94303, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Sun Microsystems, Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Sun.
 */

// HtmlBasicRenderKit.java

package com.sun.faces.renderkit.html_basic;

import java.util.Iterator;
import java.util.Properties;
import java.io.InputStream;

import javax.servlet.ServletRequest;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

import javax.faces.RenderKit;
import javax.faces.Renderer;
import javax.faces.FacesException;
import javax.faces.ClientCapabilities;
import javax.faces.EventQueue;

/**
 *
 *  <B>HtmlBasicRenderKit</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: HtmlBasicRenderKit.java,v 1.6 2001/11/29 00:12:33 edburns Exp $
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

private static String KIT_NAME = "HTML_3_2_RENDERKIT";

/**

 * used to ensure thread safety.

 */

private static Object lock = KIT_NAME;

//
// Instance Variables
//

// Attribute Instance Variables

// Relationship Instance Variables

private Properties props;

//
// Constructors and Initializers    
//

public HtmlBasicRenderKit()
{
    super();
    loadProperties();
}



//
// Class methods
//

//
// General Methods
//

private void loadProperties() {
    if (null == props) {
	props = new Properties();
    }
	
    if (null == props) {
	return;
    }

    String propsName = HtmlBasicRenderKit.class.getName();
    propsName = propsName.replace('.', '/');
    propsName += ".properties";
    InputStream is = null;

    if (null == propsName) {
	return;
    }

    // stolen from PropertyMessageResources.java in Struts
    try {
	is = this.getClass().getClassLoader().getResourceAsStream(propsName);
	if (is != null) {
	    props.load(is);
	    is.close();
	}
    } catch (Throwable t) {
	System.out.println("Can't open properties: " + t.toString());
	if (is != null) {
	    try {
		is.close();
	    } catch (Throwable u) {
		;
	    }
	}
    }	
}
//
// Methods From RenderKit
//

public String getName() {
    return KIT_NAME;
}

public Iterator getSupportedComponentTypes() {
    Assert.assert_it(null != props);

    Iterator t = new PropertyNameIterator(props, "supportedComponentTypes");
    return t;
}

public Iterator getRendererNamesForComponent(String componentType) {
    ParameterCheck.nonNull(componentType);
    Assert.assert_it(null != props);
    
    Iterator t = new PropertyNameIterator(props, componentType + "Renderer");
    return t;
}

public Renderer getRenderer(String name) throws FacesException {
//    ParameterCheck.nonNull(name);
    
    Class rendererClass;
    Renderer result;

    try {
	rendererClass = Class.forName(name);
	result = (Renderer) rendererClass.newInstance();
    }
    catch (IllegalAccessException e) {
	throw new FacesException("Can't create instance for " + 
				 name + ": " + e.getMessage());
    }
    catch (InstantiationException e) {
	throw new FacesException("Can't create instance for " + 
				 name + ": " + e.getMessage());
    }
    catch (ClassNotFoundException e) {
	throw new FacesException("Can't find class for " + 
				 name + ": " + e.getMessage());
    }
    return result;
}

public void queueEvents(ServletRequest request, EventQueue queue) {
}
									
protected void initialize() {
}

protected void destroy() {
}

    // The test for this class is in TestRenderKit.java

} // end of class HtmlBasicRenderKit

class PropertyNameIterator extends Object implements Iterator {

boolean hasNext;
Properties props;
String propName;
String propValue;
int curIndex = 0;

public PropertyNameIterator(Properties p, String name) {
    ParameterCheck.nonNull(p);
    ParameterCheck.nonNull(name);
    props = p;
    propName = name;

    propValue = props.getProperty(propName);
    hasNext = true;
}

public boolean hasNext() {
    return hasNext;
}
    
public Object next() {
    String result = null;
    int oldIndex = curIndex;
    if (null == propName || null == propValue) {
	hasNext = false;
	return null;
    }
    curIndex = propValue.indexOf(',', oldIndex + 1);
    if (-1 != curIndex) {
	if (0 != oldIndex) {
	    oldIndex++;
	}
	result = propValue.substring(oldIndex, curIndex);
    }
    else {
	if (0 == oldIndex) {
	    oldIndex--;
	}
	result = propValue.substring(oldIndex + 1);
	hasNext = false;
    }
    return result;
}

public void remove() {
}

public String toString() {
    return propValue;
}

} // end of class PropertyNameIterator
