/*
 * $Id: HtmlBasicRenderer.java,v 1.12 2002/09/23 20:33:33 rkitain Exp $
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

import javax.faces.FactoryFinder;
import javax.faces.component.AttributeDescriptor;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.component.UIOutput;

import javax.faces.render.Renderer;
import javax.faces.context.Message;
import javax.faces.context.MessageResources;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterFactory;
import javax.faces.convert.ConverterException;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

import java.util.ResourceBundle;
import java.util.MissingResourceException;

import java.io.IOException;

import com.sun.faces.RIConstants;

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

    public boolean hasAttributeWithName(String name) {
	if (null == attributeTable) {
	    return false;
	}
	return (null != attributeTable.get(name));
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
    
    public boolean decode(FacesContext context, UIComponent component) 
            throws IOException {
        Object convertedValue = null;
       
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(
                    Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        
        if (component.getComponentType() == UIOutput.TYPE) {
            // do nothing in output case
            return true;
        }

        String compoundId = component.getCompoundId();
        Assert.assert_it(compoundId != null );
        
        String newValue = context.getServletRequest().getParameter(compoundId);
        try {
            convertedValue = getConvertedValue(context, component, newValue);   
        } catch (IOException ioe) {
            component.setValue(newValue);
            addConversionErrorMessage(context, component, ioe.getMessage());
            return false;
        }    
        component.setValue(convertedValue);
        return true;
    }
    
    /**
     * Simply returns the value. This method needs to be overridden by
     * renderers that need to peform type conversion.
     */
    public Object getConvertedValue(FacesContext context, UIComponent component,
            String newValue) throws IOException {
       return newValue;            
    }         
    
    public void encodeEnd(FacesContext context, UIComponent component) 
            throws IOException {
                
        String currentValue = null;
	StringBuffer buffer = null;
        ResponseWriter writer = null;
	String styleClass = null;
        
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(
                    Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
          
        writer = context.getResponseWriter();
        Assert.assert_it(writer != null );
        
        currentValue = getCurrentValue(context, component);
        // PENDING (visvan) here is where we'd hook in a buffer pooling scheme
        buffer = new StringBuffer(1000);
        getEndTextToRender(context, component, currentValue, buffer);
        writer.write(buffer.toString());
    }
    
    /**
     * Gets value to be rendered and formats it if required. Sets to empty
     * string if value is null.
     */
    protected String getCurrentValue(FacesContext context,UIComponent component) {
        
        String currentValue = null;
        Object currentObj = component.currentValue(context);
        if ( currentObj != null) {
            if (currentObj instanceof String) {
                currentValue = (String)currentObj;
            } else {
                currentValue = getFormattedValue(context, component, currentObj);
            }
        } 
        if (currentValue == null) {
            currentValue = "";
        }
        return currentValue;
    }    
    
    /**
     * Renderers override this method to write appropriate HTML content into
     * the buffer.
     */
    protected void getEndTextToRender(FacesContext context, UIComponent component,
            String currentValue, StringBuffer buffer ) {
        return;
    }
    
    /**
     * Renderers override this method in case output value needs to be
     * formatted
     */
    protected String getFormattedValue(FacesContext context, UIComponent component,
            Object currentValue ) {
        return currentValue.toString();
    }            

    /**
     * This method gets a converter instance.  This method may not
     * apply to all renderers.
     */
    protected Converter getConverter(UIComponent component) {
        Object converter = component.getAttribute("converter");
        if (converter == null) {
            return (null);
        } else if (converter instanceof Converter) {
            return ((Converter) converter);
        }
        ConverterFactory cfactory = (ConverterFactory)
            FactoryFinder.getFactory(FactoryFinder.CONVERTER_FACTORY);
        try {
            return (cfactory.getConverter((String) converter));
        } catch (Exception e) {
            return (null);
        }
    }

} // end of class HtmlBasicRenderer
