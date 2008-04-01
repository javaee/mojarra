/*
 * $Id: TestRendererAttributes.java,v 1.1 2002/06/28 22:47:06 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestRendererAttributes.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.renderkit.html_basic.HtmlBasicRenderKit;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;

import java.util.AbstractSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import javax.faces.component.AttributeDescriptor;
import javax.faces.component.UIComponent;
import javax.faces.render.Renderer;

import org.apache.commons.digester.AbstractObjectCreationFactory;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.Rule;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.ParameterCheck;

import org.apache.cactus.ServletTestCase;



/**
 *
 *  <B>TestRendererAttributes</B> is a class which tests the 
 * functionality of the BasicHtmlRenderKit.
 *
 * When this test class is instantiated, it reads the xml configuration
 * file for the render kit and builds a simple tree representation
 * of the kit as follows:
 *     componentTable(Hashtable)
 *        keys:componentType(String)
 *        values:rendererTable(Hashtable)
 *            keys:rendererType(String)
 *            values:attributeTable(Hashtable)
 *                keys:attributeName(String)
 *                values:attributeProperties(Properties)
 * 
 * This hierarchical table derived from the xml file provides the
 * golden view of the render kit's data from which a RenderKit
 * instance can be tested to ensure it properly represents this data.
 * FIXME: use JAXB instead (?)
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestRendererAttributes.java,v 1.1 2002/06/28 22:47:06 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestRendererAttributes extends ServletTestCase {
//
// Protected Constants
//

//
// Class Variables
//

//
// Instance Variables
//
    private Digester digester = null;
    private String xmlRenderKitFileName = null;
    private HtmlBasicRenderKit renderKit = null;
    private Hashtable componentTable = null;
    

// Attribute Instance Variables

// Relationship Instance Variables

//
// Constructors and Initializers    
//

    public TestRendererAttributes() {
	super("TestRendererAttributes");
    }
    public TestRendererAttributes(String name) {
	super(name);

	loadRenderKitFromXML("HtmlBasicRenderKit.xml");
    }
//
// Class methods
//

//
// General Methods
//

   private void loadRenderKitFromXML(String xmlFileName) {
        componentTable = new Hashtable();
	xmlRenderKitFileName = xmlFileName;

        InputStream in;
        try {
            in = this.getClass().getClassLoader().getResourceAsStream(
                xmlFileName);
        } catch (Throwable t) {
            throw new RuntimeException("Error Opening File:"+xmlFileName);
        }
        try {
            digester = initConfig();
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
	Rule popRule = new PopAtEndRule();
        digester.setNamespaceAware(true);
        digester.setValidating(false);
	digester.addRule("*/component", popRule);
        digester.addRule("*/component/type", 
			 new ComponentTypeRule());
        digester.addFactoryCreate("*/component/renderer", 
			 new RendererTypeFactory());
	digester.addRule("*/component/renderer/attribute", popRule);		 
        digester.addRule("*/component/renderer/attribute/name", 
			 new AttributeRule());
        digester.addRule("*/component/renderer/attribute/display-name", 
			 new AttributeDisplayNameRule());
        digester.addRule("*/component/renderer/attribute/description",
			 new AttributeDescriptionRule());
        digester.addRule("*/component/renderer/attribute/type", 
			 new AttributeClassRule());

        return digester;
    }

    private class ComponentTypeRule extends Rule {
	public void body(String body) {
	    Hashtable rendererTable = new Hashtable();
	    componentTable.put(body, rendererTable);
	    System.out.println("componentType: "+body);
	    this.digester.push(rendererTable);
	}
    }

    private class PopAtEndRule extends Rule {
	public void end() {
	    this.digester.pop();
	}
    }
    private class RendererTypeFactory extends AbstractObjectCreationFactory {
	public Object createObject(org.xml.sax.Attributes attributes) {
            String rendererType = attributes.getValue("type");
	    Hashtable rendererTable = (Hashtable)this.digester.peek();
	    Hashtable attributeTable = new Hashtable();
	    rendererTable.put(rendererType, attributeTable);
	    System.out.println("  rendererType: "+rendererType);
	    return attributeTable;
	}
    }
    private class AttributeRule extends Rule {
	public void body(String body) {
	    Hashtable attributeTable = (Hashtable)this.digester.peek();
	    Properties attributeProperties = new Properties();
	    attributeTable.put(body, attributeProperties);
	    System.out.println("    attribute: "+body);
	    this.digester.push(attributeProperties);
	}
    }
    private class AttributeDisplayNameRule extends Rule {
	public void body(String body) {
	    Properties attributeProperties = (Properties)this.digester.peek();
	    attributeProperties.put("displayName", body);
	}
    }
    private class AttributeDescriptionRule extends Rule {
	public void body(String body) {
	    Properties attributeProperties = (Properties)this.digester.peek();
	    attributeProperties.put("description", body);
	}
    }
    private class AttributeClassRule extends Rule {
	public void body(String body) {
	    Properties attributeProperties = (Properties)this.digester.peek();
	    attributeProperties.put("class", body);
	}
    }

    public Hashtable lookupAttributeNames(String rendererType) {
	Iterator components = componentTable.keySet().iterator();
	while (components.hasNext()) {
	    String componentType = (String)components.next();
	    Hashtable rendererTable = (Hashtable)componentTable.get(componentType);
	    Hashtable attributeTable = (Hashtable)rendererTable.get(rendererType);
	    if (attributeTable != null) {
		return attributeTable;
	    }
	}
	return null;
    }
		
	

//
// Test methods
//

    public void testGetAttributeNames() {
        renderKit = new HtmlBasicRenderKit();

        Iterator rendererTypes = renderKit.getRendererTypes();
        while (rendererTypes.hasNext()) {
            String rendererType = (String)rendererTypes.next();
            Renderer renderer = renderKit.getRenderer(rendererType);
	    Hashtable goldenAttrNames = lookupAttributeNames(rendererType);
	    Set clone = ((Hashtable) goldenAttrNames.clone()).keySet();
	    System.out.println(rendererType+" -->");
	    // Test Renderer:getAttributeNames(UIComponent)

	    try {
	        Iterator attrNames = renderer.getAttributeNames((UIComponent)null);
	        verifyMatch(clone, attrNames);
	    } catch (Exception e) {
		assertTrue(rendererType+
			   " attribute names don't match "+
			   xmlRenderKitFileName, false);
	    }

	    clone = ((Hashtable) goldenAttrNames.clone()).keySet();
	    // Test Renderer:getAttributeNames(String)
	    try {
	        Iterator attrNames = renderer.getAttributeNames((String)null);
	        verifyMatch(clone, attrNames);
	    } catch (Exception e) {
		assertTrue(rendererType+
			   " attribute names don't match "+
			   xmlRenderKitFileName+
			   " "+e.getMessage(), false);
	    }
	}
    }

    private void verifyMatch(Set golden, 
			     Iterator set2) throws RuntimeException { 
	while(set2.hasNext()) {
	    Object element = set2.next();
	    System.out.println("element: "+element);
	    if (golden.contains(element)) {
		golden.remove(element);
	    } else {       
		throw new RuntimeException(element+" not found in golden");
	    }
	}
	if (golden.size() > 0) {
	    throw new RuntimeException(" golden contains elements not found");
	}
    }

    

    public static void main(String args[]) {
	TestRendererAttributes test = new TestRendererAttributes();
	test.testGetAttributeNames();
    }
} // end of class TestRendererAttributes
