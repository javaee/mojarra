/*
 * $Id: HtmlBasicRenderer.java,v 1.34 2003/03/21 23:24:01 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// HtmlBasicRenderer.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.AttributeDescriptorImpl;
import com.sun.faces.util.Util;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.faces.FactoryFinder;
import javax.faces.FacesException;
import javax.faces.component.AttributeDescriptor;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.component.UIInput;
import javax.faces.component.NamingContainer;
import javax.faces.el.ValueBinding;

import javax.faces.render.Renderer;
import javax.faces.component.UIInput;
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
            typeClass = Util.loadClass(typeClassName, this);
        } catch (ClassNotFoundException cnf) {
	    Object [] params = { cnf.getMessage() };
            throw new RuntimeException(Util.getExceptionMessage(Util.MISSING_RESOURCE_ERROR_MESSAGE_ID, params));
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
        return (getAttributeDescriptor(component.getComponentType(), name));
    }

    public AttributeDescriptor getAttributeDescriptor(
        String componentType, String name) {

        if (componentType == null || name == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

        if (!supportsComponentType(componentType)) {
            Object [] params = {componentType}; 
            throw new IllegalArgumentException(Util.getExceptionMessage(
                Util.SUPPORTS_COMPONENT_ERROR_MESSAGE_ID, params));
        }
        if (!hasAttributeWithName(name)) {
            Object [] params = {name, componentType}; 
            throw new IllegalArgumentException(Util.getExceptionMessage(
                Util.ATTRIBUTE_NOT_SUPORTED_ERROR_MESSAGE_ID, params));
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
        UIOutput uiOutput = null;
        if ( comp instanceof UIOutput) {
            uiOutput= (UIOutput) comp;
            params[0] = uiOutput.getValue();
            params[1] = uiOutput.getValueRef();
        }
        params[2] = errorMessage; 
        MessageResources resources = Util.getMessageResources();
        Assert.assert_it( resources != null );
        Message msg = resources.getMessage(facesContext, 
                Util.CONVERSION_ERROR_MESSAGE_ID,params);
        facesContext.addMessage(comp, msg);
    }

    public void addGenericErrorMessage(FacesContext facesContext,
				       UIComponent component,
				       String messageId, String param) {
        MessageResources resources = Util.getMessageResources();
        Assert.assert_it( resources != null );
	Object[] params = new Object[3];
	params[0] = param;
        Message msg = resources.getMessage(facesContext, messageId, params);
        facesContext.addMessage(component, msg);
    }

    /**

    * Look up the attribute named keyAttr in the component's attr set.
    * Use the result as a key into the resource bundle named by the
    * model reference in the component's "bundle" attribute.

    */

    protected String getKeyAndLookupInBundle(FacesContext context,
					     UIComponent component, 
					     String keyAttr) throws MissingResourceException{
	String key = null, bundleName = null;
	ResourceBundle bundle = null;

	ParameterCheck.nonNull(context);
	ParameterCheck.nonNull(component);
	ParameterCheck.nonNull(keyAttr);

        key = (String) component.getAttribute(keyAttr);
        bundleName = (String)component.getAttribute(RIConstants.BUNDLE_ATTR);

        // if the bundleName is null for this component, it might have
        // been set on the root component.
        if ( bundleName == null ) {
            UIComponent root = context.getTree().getRoot();
            Assert.assert_it(root != null);
            bundleName = (String)root.getAttribute(RIConstants.BUNDLE_ATTR);
        }
	// verify our component has the proper attributes for key and bundle.
	if (null == key || null == bundleName) {
	    throw new MissingResourceException(Util.getExceptionMessage(
                Util.MISSING_RESOURCE_ERROR_MESSAGE_ID),bundleName, key);
	}
	
	// verify the required Class is loadable
	try {
	    Util.verifyRequiredClasses(context);
	}  
	catch (FacesException e) {
	    Object [] params = { "javax.servlet.jsp.jstl.fmt.LocalizationContext" };
	    throw new MissingResourceException(Util.getExceptionMessage(Util.MISSING_CLASS_ERROR_MESSAGE_ID, params), bundleName, key);
	}
	    
	// verify there is a ResourceBundle in scoped namescape.
	javax.servlet.jsp.jstl.fmt.LocalizationContext locCtx = null;
	if (null == (locCtx = (javax.servlet.jsp.jstl.fmt.LocalizationContext) 
            (Util.getValueBinding(bundleName)).getValue(context)) ||
	    null == (bundle = locCtx.getResourceBundle())) {
	    throw new MissingResourceException(Util.getExceptionMessage(Util.MISSING_RESOURCE_ERROR_MESSAGE_ID), bundleName, key);
	}
	
	return bundle.getString(key);
    }
    
    public void decode(FacesContext context, UIComponent component) 
            throws IOException {

        Object convertedValue = null;
       
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(
                    Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        
        if ((UIOutput.TYPE.equals(component.getComponentType()))) {
            // do nothing in output case
            return;
        }

        UIInput uiInput = null;
        if ( component instanceof UIInput) {
            uiInput= (UIInput) component;
        }
        String clientId = component.getClientId(context);
        Assert.assert_it(clientId != null );
        
        // set previous value = current value (converted if necessary)
        // we should convert because we want to compare the converted
        // previous (current) value with the converted new value;
        // ex: we don't want to compare "48%" with 0.48;
        
        Object curValue = uiInput.currentValue(context);
        if (curValue instanceof String) {
            try {
                Object convertedCurrentValue = 
                    getConvertedValue(context, component,
                    (String)curValue);
                curValue = convertedCurrentValue;
            } catch (ConverterException ce) {
            }
        }
        setPreviousValue(component, curValue);

        Map requestMap = context.getExternalContext().getRequestMap();
        String newValue = (String)requestMap.get(clientId);
        try {
            convertedValue = getConvertedValue(context, component, newValue);   
        } catch (ConverterException ce) {
            uiInput.setValue(newValue);
            addConversionErrorMessage(context, component, ce.getMessage());
            component.setValid(false);
            return;
        }    
        uiInput.setValue(convertedValue);
        component.setValid(true);
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
        
        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            return;
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
        
        UIOutput uiOutput = null;
        if ( component instanceof UIOutput) {
            uiOutput= (UIOutput) component;
        }
        
        String currentValue = null;
        Object currentObj = uiOutput.currentValue(context);
        if ( currentObj != null) {
            currentValue = getFormattedValue(context, component, currentObj);
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
        String converterId = component.getConverter();
        if (converterId == null) {
            return (null);
        }
        ConverterFactory cfactory = (ConverterFactory)
            FactoryFinder.getFactory(FactoryFinder.CONVERTER_FACTORY);
        try {
            return (cfactory.getConverter(converterId));
        } catch (Exception e) {
            return (null);
        }
    }

    public String getClientId(FacesContext context, UIComponent component){
	String clientId = null;
	NamingContainer closestContainer = null;
	UIComponent containerComponent = component;

        // Search for an ancestor that is a naming container
        while (null != (containerComponent = 
                        containerComponent.getParent())) {
            if (containerComponent instanceof NamingContainer) {
                closestContainer = (NamingContainer) containerComponent;
                break;
            }
        }

        // If none is found, see if this is a naming container
        if (null == closestContainer && component instanceof NamingContainer) {
            closestContainer = (NamingContainer) component;
        }

        if (null != closestContainer) {

            // If there is no componentId, generate one and store it
            if (component.getComponentId() == null) {
                // Don't call setComponentId() because it checks for
                // uniqueness.  No need.
                clientId = closestContainer.generateClientId();
            } else {
                clientId = component.getComponentId();
            }

            // build the client side id
            containerComponent = (UIComponent) closestContainer;

            // If this is the root naming container, break
            if (null != containerComponent.getParent()) {
                clientId = containerComponent.getClientId(context) +
                    UIComponent.SEPARATOR_CHAR + clientId;
            }
        }

        if (null == clientId) {
	    throw new NullPointerException();
	}
	return (clientId);
    }

    /**
     * Renderers override this method to store the previous value
     * of the associated component.
     */
    protected void setPreviousValue(UIComponent component, Object value) {
    }

} // end of class HtmlBasicRenderer
