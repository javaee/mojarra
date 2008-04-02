/*
 * $Id: BaseRenderer.java,v 1.1 2003/02/12 17:59:37 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package components.renderkit;


import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.AttributeDescriptor;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;

import java.util.ResourceBundle;
import java.util.MissingResourceException;



import java.util.Iterator;

/**
 * <p>Convenient base class for <code>Renderer</code> implementations.</p>
 */

public abstract class BaseRenderer extends Renderer {

    /**
     * <p>String identifer for <em>bundle attribute.</em>.</p>
     */
    public static final String BUNDLE_ATTR = "com.sun.faces.bundle";

    public boolean supportsComponentType(UIComponent component) {
        if ( component == null ) {
            throw new NullPointerException();
        }     
        return supportsComponentType(component.getComponentType());
    }

    public Iterator getAttributeNames(String componentType) {
	return null;
    }

    public Iterator getAttributeNames(UIComponent component) {
	return null;
    }

    public AttributeDescriptor getAttributeDescriptor(
        UIComponent component, String name) {
	return null;
    }

    public AttributeDescriptor getAttributeDescriptor(
        String componentType, String name) {
	return null;
    }

    
    /**
     * <p>Return the client-side id for the argument component.</p>
     *
     * <p>The purpose of this method is to give Renderers a chance to
     * define, in a rendering specific way, the client side id for this
     * component.  The client side id should be derived from the
     * component id, if present.  </p>
     *
     * <p>Look up this component's "clientId" attribute.  If non-null,
     * return it.  Get the component id for the argument
     * <code>UIComponent</code>.  If null, generate one using the closest
     * naming container that is an ancestor of this UIComponent, then set
     * the generated id as the componentId of this UIComponent.  Prepend
     * to the component id the component ids of each naming container up
     * to, but not including, the root, separated by the
     * UIComponent.SEPARATOR_CHAR.  In all cases, save the result as the
     * value of the "clientId" attribute.</p>
     *
     * <p>This method must not return null.</p>
     */ 
    public String getClientId(FacesContext context, UIComponent component) {

        // Has a client identifier been generated for this component already?
	String result = null;
	if (null != (result = (String) component.getAttribute("clientId"))) {
	    return result;
	}
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
	    if (null == (result = component.getComponentId())) {
		// Don't call setComponentId() because it checks for
		// uniqueness.  No need.
		component.setAttribute("componentId",
				       result = closestContainer.generateClientId());
	    }

	    // build the client side id
	    containerComponent = (UIComponent) closestContainer;
	    // If this is the root naming container, break
	    if (null != containerComponent.getParent()) {
		result = containerComponent.getClientId(context) +
		    UIComponent.SEPARATOR_CHAR + result;
	    }

	}
	
        // Store the client identifier for future use
	if (null == result) {
	    throw new NullPointerException();
	}
	component.setAttribute("clientId", result);
	return (result);

    }

    protected String getKeyAndLookupInBundle(FacesContext context,
					     UIComponent component, 
					     String keyAttr) throws MissingResourceException{
	String key = null, bundleName = null;
	ResourceBundle bundle = null;

        key = (String) component.getAttribute(keyAttr);
        bundleName = (String)component.getAttribute(BUNDLE_ATTR);

        // if the bundleName is null for this component, it might have
        // been set on the root component.
        if ( bundleName == null ) {
            UIComponent root = context.getTree().getRoot();

            bundleName = (String)root.getAttribute(BUNDLE_ATTR);
        }
	// verify our component has the proper attributes for key and bundle.
	if (null == key || null == bundleName) {
	    throw new MissingResourceException("Can't load JSTL classes", 
					       bundleName, key);
	}
	
	// verify the required Class is loadable
	// PENDING(edburns): Find a way to do this once per ServletContext.
	if (null == Thread.currentThread().getContextClassLoader().
	    getResource("javax.servlet.jsp.jstl.fmt.LocalizationContext")){
	    Object [] params = { "javax.servlet.jsp.jstl.fmt.LocalizationContext" };
	    throw new MissingResourceException("Can't load JSTL classes", 
					       bundleName, key);
	}
	
	// verify there is a ResourceBundle for this modelReference
	javax.servlet.jsp.jstl.fmt.LocalizationContext locCtx = null;
	if (null == (locCtx = (javax.servlet.jsp.jstl.fmt.LocalizationContext) 
		     context.getModelValue(bundleName)) ||
	    null == (bundle = locCtx.getResourceBundle())) {
	    throw new MissingResourceException("Can't load JSTL classes", 
					       bundleName, key);
	}
	
	return bundle.getString(key);
    }



}
