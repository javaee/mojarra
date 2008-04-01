/*
 * $Id: HtmlBasicRenderer.java,v 1.5 2002/08/05 21:22:40 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// HtmlBasicRenderer.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.AttributeDescriptorImpl;
import com.sun.faces.util.Util;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.faces.component.AttributeDescriptor;
import javax.faces.component.UIComponent;
import javax.faces.render.Renderer;
import javax.faces.context.Message;
import javax.faces.context.MessageResources;
import javax.faces.context.FacesContext;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

import java.util.ResourceBundle;
import java.util.MissingResourceException;

/**
 *
 *  <B>HtmlBasicRenderer</B> is a base class for implementing renderers
 *  for HtmlBasicRenderKit.
 * @version
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public abstract class HtmlBasicRenderer extends Renderer {
    //
    // Protected Constants
    //

    //
    // Class Variables
    //

    //
    // Instance Variables
    //

    // Attribute Instance Variables

    // Relationship Instance Variables

    private Hashtable attributeTable;

    //
    // Constructors and Initializers    
    //

    public HtmlBasicRenderer() {
        super();
    }

    //
    // Class methods
    //

    //
    // General Methods
    //
    public void registerAttribute(String name, String displayName, 
			     String description, String typeClassName) {
	Class typeClass = null;
        try {
            typeClass = Util.loadClass(typeClassName);
        } catch (ClassNotFoundException cnf) {
            throw new RuntimeException("Class Not Found:"+cnf.getMessage());
        }
	if (attributeTable == null) {
	    attributeTable = new Hashtable();
	}

        AttributeDescriptorImpl ad = new AttributeDescriptorImpl(name, 
					 displayName, description, typeClass);
        attributeTable.put(name, ad);
    }
	

    //
    // Methods From Renderer
    // FIXME: what if named attriubte doesn't exist? should exception be thrown?
    //
    public AttributeDescriptor getAttributeDescriptor(
        UIComponent component, String name) {

        if (component == null || name == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

	return (AttributeDescriptor)(attributeTable != null? attributeTable.get(name) : null); 
    }

    public AttributeDescriptor getAttributeDescriptor(
        String componentType, String name) {

        if (componentType == null || name == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

	return (AttributeDescriptor)(attributeTable != null? attributeTable.get(name) : null); 
    }

    public Iterator getAttributeNames(UIComponent component) {

        if (component == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_COMPONENT_ERROR_MESSAGE_ID));
        }

        String componentType = component.getComponentType();
        if (!supportsComponentType(componentType)) {
            Object [] params = {componentType}; 
            throw new IllegalArgumentException(Util.getExceptionMessage(
                Util.SUPPORTS_COMPONENT_ERROR_MESSAGE_ID, params));
        }

        return attributeTable != null? attributeTable.keySet().iterator() : emptyIterator();
    }

    public Iterator getAttributeNames(String componentType) {

        if (componentType == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

        if (!supportsComponentType(componentType)) {
            Object [] params = {componentType};
            throw new IllegalArgumentException(Util.getExceptionMessage(
                Util.SUPPORTS_COMPONENT_ERROR_MESSAGE_ID, params));
        }

        return attributeTable != null? attributeTable.keySet().iterator() : emptyIterator();

    }

    private Iterator emptyIterator() {
	return new Iterator() {
	               public boolean hasNext() {return false;}
                       public Object next() {throw new NoSuchElementException();}
                       public void remove() {}
	    };
    }

    public boolean supportsComponentType(UIComponent component) {
        if ( component == null ) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_COMPONENT_ERROR_MESSAGE_ID));
        }     
        return supportsComponentType(component.getComponentType());
    }
    
    public void addConversionErrorMessage( FacesContext facesContext, 
            UIComponent comp, String errorMessage ) {
        Object[] params = new Object[3];
        params[0] = comp.getValue();
        params[1] = comp.getModelReference();
        params[2] = errorMessage; 
        MessageResources resources = Util.getMessageResources();
        Assert.assert_it( resources != null );
        Message msg = resources.getMessage(facesContext, 
                Util.CONVERSION_ERROR_MESSAGE_ID,params);
        facesContext.addMessage(comp, msg);
    }

    /**

    * Look up the attribute named keyAttr in the component's attr set.
    * Use the result as a key into the resource bundle named by the
    * model reference in the component's "bundle" attribute.

    */

    protected String getKeyAndLookupInBundle(FacesContext context,
					     UIComponent component, 
					     String keyAttr) throws MissingResourceException{
	String key = null, bundleName = null, bundleAttr = "bundle";
	ResourceBundle bundle = null;

	ParameterCheck.nonNull(context);
	ParameterCheck.nonNull(component);
	ParameterCheck.nonNull(keyAttr);
	
	// verify our component has the proper attributes for key and bundle.
	if (null == (key = (String) component.getAttribute(keyAttr)) ||
	    null == (bundleName = (String)component.getAttribute(bundleAttr))){
	    throw new MissingResourceException(Util.getExceptionMessage(Util.MISSING_RESOURCE_ERROR_MESSAGE_ID),
					       bundleName, key);
	}
	
	// verify the required Class is loadable
	// PENDING(edburns): Find a way to do this once per ServletContext.
	if (null == Thread.currentThread().getContextClassLoader().
	    getResource("javax.servlet.jsp.jstl.fmt.LocalizationContext")){
	    Object [] params = { "javax.servlet.jsp.jstl.fmt.LocalizationContext" };
	    throw new MissingResourceException(Util.getExceptionMessage(Util.MISSING_CLASS_ERROR_MESSAGE_ID, params), bundleName, key);
	}
	
	// verify there is a ResourceBundle for this modelReference
	javax.servlet.jsp.jstl.fmt.LocalizationContext locCtx = null;
	if (null == (locCtx = (javax.servlet.jsp.jstl.fmt.LocalizationContext) 
		     context.getModelValue(bundleName)) ||
	    null == (bundle = locCtx.getResourceBundle())) {
	    throw new MissingResourceException(Util.getExceptionMessage(Util.MISSING_RESOURCE_ERROR_MESSAGE_ID), bundleName, key);
	}
	
	return bundle.getString(key);
    }

} // end of class HtmlBasicRenderer
