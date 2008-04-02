/*
 * $Id: HtmlBasicRenderer.java,v 1.50 2003/08/22 16:50:23 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// HtmlBasicRenderer.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.Util;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.faces.FactoryFinder;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UIInput;
import javax.faces.component.NamingContainer;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.Application;

import javax.faces.render.Renderer;
import javax.faces.application.Message;
import javax.faces.application.MessageResources;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import java.util.ResourceBundle;
import java.util.MissingResourceException;
import java.io.IOException;

import com.sun.faces.RIConstants;

/**
 *
 *  <B>HtmlBasicRenderer</B> is a base class for implementing renderers
 *  for HtmlBasicRenderKit.
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
    }

    public boolean hasAttributeWithName(String name) {
	if (null == attributeTable) {
	    return false;
	}
	return (null != attributeTable.get(name));
    }
	

    //
    // Methods From Renderer
    // PENDING: what if named attriubte doesn't exist? should exception be thrown?
    //
    public Iterator getAttributeNames(UIComponent component) {

        if (component == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_COMPONENT_ERROR_MESSAGE_ID));
        }

        return attributeTable != null? attributeTable.keySet().iterator() : emptyIterator();
    }

    public Iterator getAttributeNames(String componentType) {

        if (componentType == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
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
            UIComponent root = context.getViewRoot();
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

        UIInput uiInput = null;
        if ( component instanceof UIInput) {
            uiInput= (UIInput) component;
        } else {
            // decode needs to be invoked only for components that are
            // instances or subclasses of UIInput.
            return;
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

        Map requestMap = context.getExternalContext().getRequestParameterMap();
	// Don't overwrite the value unless you have to!
	if (requestMap.containsKey(clientId)) {
	    String newValue = (String)requestMap.get(clientId);
	    try {
		convertedValue = getConvertedValue(context, component, newValue);   
	    } catch (ConverterException ce) {
		uiInput.setValue(newValue);
		addConversionErrorMessage(context, component, ce.getMessage());
		uiInput.setValid(false);
		return;
	    }   
	    uiInput.setValue(convertedValue);
	}
     }
    
    /**
     * Simply returns the value. This method needs to be overridden by
     * renderers that need to peform type conversion.
     */
    public Object getConvertedValue(FacesContext context, UIComponent component,
            String newValue) throws ConverterException {
       return newValue;            
    }         
    
    public void encodeEnd(FacesContext context, UIComponent component) 
            throws IOException {
                
        String currentValue = null;
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
        getEndTextToRender(context, component, currentValue);
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
        return currentValue;
    }    
    
    /**
     * Renderers override this method to write appropriate HTML content into
     * the buffer.
     */
    protected void getEndTextToRender(FacesContext context, UIComponent component,
            String currentValue) throws IOException {
        return;
    }
   
    /**
     * This method gets a converter instance.  This method only applies to
     * input and output renderers.
     */
    protected Converter getConverter(String converterId) {
        if (converterId == null) {
            return null;
        }
        try {
	    ApplicationFactory aFactory = 
		(ApplicationFactory)FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
	    Application application = aFactory.getApplication();
            return (application.createConverter(converterId));
        } catch (Exception e) {
            return (null);
        }
    }
    
    /**
     * This method gets a converter instance given a converter 
     * identifier
     */
    protected Converter getConverterForIdentifer(String converterId) {
        if (converterId == null) {
            return null;
        }
        try {
	    ApplicationFactory aFactory = 
		(ApplicationFactory)FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
	    Application application = aFactory.getApplication();
            return (application.createConverter(converterId));
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
            if (component.getId() == null) {
                // Don't call setId() because it checks for
                // uniqueness.  No need.
                clientId = closestContainer.generateClientId();
            } else {
                clientId = component.getId();
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
    
   /**
     * Renderers override this method in case output value needs to be
     * formatted
     */
    protected String getFormattedValue(FacesContext context, UIComponent component,
            Object currentValue ) throws ConverterException {
         String result = null;
        // formatting is supported only for components that support value and 
         // valueRef attributes.
        if ( !(component instanceof UIOutput) ){
             if ( currentValue != null) {
                 result= currentValue.toString();
             } 
             return result;
        }
       
        // if this value is a non-null String use it "as is"
        if ( currentValue != null && currentValue instanceof String) {
            return ((String)currentValue);
        }
         
        String valueRef = ((UIOutput)component).getValueRef();
        Converter converter = null;
        // If there is a converter attribute, use it to to ask application
        // instance for a converter with this identifer.
       
       // PENDING (rlubke) CORRECT IMPLEMENTATION
       // String converterId = component.getConverter();
       // converter = getConverter(converterId);
       
        // if value is null and no converter attribute is specified, then
        // return a zero length String.
        if ( converter == null && currentValue == null) {
            return "";
        }
        // if the value is a non-null, non-String and no converter is
        // specified, try to use one of the standard converters
	if (converter == null && valueRef != null) {
            Class converterType = 
                (Util.getValueBinding(valueRef)).getType(context);

            // if getType returns a type for which we support a default
            // conversion, acquire an appropriate converter instance.
        
            // PENDING (rlubke) CORRECT IMPLEMENTATION
//            converterId = Util.getDefaultConverterForType(
//                     (converterType.getName()));
//            converter = getConverter(converterId);
            
	} else if ( converter == null && valueRef == null ) {
            // if there is no valueRef and converter attribute set, try to acquire
            // a converter using its class type.
        
            // PENDING (rlubke) CORRECT IMPLEMENTATION
            //converterId = currentValue.getClass().getName();
            //converter = getConverter(converterId);
        
            // if there is no default converter available for this identifier,
            // assume the model type to be String.
            if ( converter == null && currentValue != null) {
                result = currentValue.toString();
                return result;
            }
        }
        
        if ( converter != null) {
            result = converter.getAsString(context, component, currentValue);
            
	    return result;
        } else {
            // throw converter exception if no converter can be
            // identified if a valueRef is set and converter could not be
            // identified
            throw new ConverterException(Util.getExceptionMessage(
                    Util.CONVERSION_ERROR_MESSAGE_ID));
        }
    }

} // end of class HtmlBasicRenderer
