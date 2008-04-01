/*
 * $Id: Util.java,v 1.21 2002/08/02 22:22:36 rkitain Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// Util.java

package com.sun.faces.util;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.servlet.ServletContext;
import javax.faces.FacesException;
import javax.faces.FactoryFinder;

import javax.faces.render.RenderKitFactory;
import javax.faces.render.RenderKit;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.tree.TreeFactory;
import javax.faces.context.FacesContextFactory;

import javax.faces.FactoryFinder;
import javax.faces.context.MessageResourcesFactory;
import javax.faces.context.MessageResources;
import javax.faces.context.Message;

import javax.faces.component.UISelectItem;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItems;
import javax.faces.component.SelectItem;

import com.sun.faces.RIConstants;
import com.sun.faces.context.MessageResourcesImpl;
import javax.faces.context.FacesContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Iterator;

/**
 *
 *  <B>Util</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: Util.java,v 1.21 2002/08/02 22:22:36 rkitain Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class Util extends Object
{
//
// Protected Constants
//

    /**
     * The message identifier of the {@link Message} to be created as
     * a result of type conversion error.
     */
    public static final String CONVERSION_ERROR_MESSAGE_ID =
        "com.sun.faces.TYPECONVERSION_ERROR";
    
    /**
     * The message identifier of the {@link Message} to be created if
     * there is model update failure.
     */
    public static final String MODEL_UPDATE_ERROR_MESSAGE_ID =
        "com.sun.faces.MODELUPDATE_ERROR";

    public static final String FACES_CONTEXT_CONSTRUCTION_ERROR_MESSAGE_ID = 
	"com.sun.faces.FACES_CONTEXT_CONSTRUCTION_ERROR";

    public static final String NULL_COMPONENT_ERROR_MESSAGE_ID = 
	"com.sun.faces.NULL_COMPONENT_ERROR";

    public static final String NULL_REQUEST_TREE_ERROR_MESSAGE_ID = 
	"com.sun.faces.NULL_REQUEST_TREE_ERROR";

    public static final String NULL_RESPONSE_TREE_ERROR_MESSAGE_ID = 
	"com.sun.faces.NULL_RESPONSE_TREE_ERROR";

    public static final String REQUEST_TREE_ALREADY_SET_ERROR_MESSAGE_ID = 
	"com.sun.faces.REQUEST_TREE_ALREADY_SET_ERROR";
    
    public static final String NULL_MESSAGE_ERROR_MESSAGE_ID = 
	"com.sun.faces.NULL_MESSAGE_ERROR";
    
    public static final String NULL_PARAMETERS_ERROR_MESSAGE_ID = 
	"com.sun.faces.NULL_PARAMETERS_ERROR";
    
    public static final String NAMED_OBJECT_NOT_FOUND_ERROR_MESSAGE_ID = 
	"com.sun.faces.NAMED_OBJECT_NOT_FOUND_ERROR";
    
    public static final String NULL_RESPONSE_STREAM_ERROR_MESSAGE_ID = 
	"com.sun.faces.NULL_RESPONSE_STREAM_ERROR";

    public static final String NULL_RESPONSE_WRITER_ERROR_MESSAGE_ID = 
	"com.sun.faces.NULL_RESPONSE_WRITER_ERROR";

    public static final String NULL_EVENT_ERROR_MESSAGE_ID = 
	"com.sun.faces.NULL_EVENT_ERROR";

    public static final String NULL_HANDLER_ERROR_MESSAGE_ID = 
	"com.sun.faces.NULL_HANDLER_ERROR";

    public static final String NULL_CONTEXT_ERROR_MESSAGE_ID = 
	"com.sun.faces.NULL_CONTEXT_ERROR";

    public static final String NULL_LOCALE_ERROR_MESSAGE_ID =
        "com.sun.faces.NULL_LOCALE_ERROR";

    public static final String SUPPORTS_COMPONENT_ERROR_MESSAGE_ID =
        "com.sun.faces.SUPPORTS_COMPONENT_ERROR";
//
// Class Variables
//

//
// Instance Variables
//
private static long id = 0;

// Attribute Instance Variables

// Relationship Instance Variables

//
// Constructors and Initializers    
//

private Util()
{
    throw new IllegalStateException();
}

//
// Class methods
//
    public static Class loadClass(String name) throws ClassNotFoundException {
	ClassLoader loader =
	    Thread.currentThread().getContextClassLoader();
	if (loader == null) {
	    return Class.forName(name);
	}
	else {
	    return loader.loadClass(name);
	}
    }

    /**
     * Generate a new identifier currently used to uniquely identify
     * components.
     */
    public static synchronized String generateId() {
        if (id == Long.MAX_VALUE) {
            id = 0;
        } else { 
            id++;
        }
        return Long.toHexString(id);
    }

    /**
     * This method will be called before calling facesContext.addMessage, so 
     * message can be localized.
     * <p>Return the {@link MessageResources} instance for the message
     * resources defined by the JavaServer Faces Specification.
     */
    public static synchronized MessageResources getMessageResources() {
        MessageResources resources = null;
	MessageResourcesFactory factory = (MessageResourcesFactory)
	    FactoryFinder.getFactory
	    (FactoryFinder.MESSAGE_RESOURCES_FACTORY);
	resources = factory.getMessageResources
	    (MessageResourcesFactory.FACES_IMPL_MESSAGES);
	
        return (resources);
    }

    /**

    * Called by the RI to get the IMPL_MESSAGES MessageResources
    * instance and get a message on it.  

    */

    public static synchronized String getExceptionMessage(String messageId,
							  Object params[]) {
	String result = null;
	MessageResourcesImpl resources = (MessageResourcesImpl)
	    Util.getMessageResources();

	// As an optimization, we could store the MessageResources
	// instance in the System Properties for subsequent calls to
	// getExceptionMessage().

	if (null != resources) {
	    result = resources.getMessage(messageId, params).getDetail();
	}
	else {
	    result = "null MessageResources";
	}
	return result;
    }

    public static synchronized String getExceptionMessage(String messageId) {
	return Util.getExceptionMessage(messageId, null);
    }
    
    /**

    * Verify the existence of all the factories needed by faces.  Create
    * and install the default RenderKit into the ServletContext. <P>

    * @see javax.faces.FactoryFinder

    */

    public static void verifyFactoriesAndInitDefaultRenderKit(ServletContext context) throws FacesException {
	RenderKitFactory renderKitFactory = null;
	LifecycleFactory lifecycleFactory = null;
	TreeFactory treeFactory = null;
	FacesContextFactory facesContextFactory = null;
	RenderKit defaultRenderKit = null;

	renderKitFactory = (RenderKitFactory)
	    FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
	Assert.assert_it(null != renderKitFactory);

	lifecycleFactory = (LifecycleFactory)
	    FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
	Assert.assert_it(null != lifecycleFactory);

	treeFactory = (TreeFactory)
	    FactoryFinder.getFactory(FactoryFinder.TREE_FACTORY);
	Assert.assert_it(null != treeFactory);

	facesContextFactory = (FacesContextFactory)
	    FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
	Assert.assert_it(null != facesContextFactory);

	defaultRenderKit = 
	    renderKitFactory.getRenderKit(RIConstants.DEFAULT_RENDER_KIT);
	Assert.assert_it(null != defaultRenderKit);
	context.setAttribute(RIConstants.DEFAULT_RENDER_KIT, 
			     defaultRenderKit);
    }

    /** 

    * Release the factories and remove the default RenderKit from the
    * ServletContext.

    */

    public static void releaseFactoriesAndDefaultRenderKit(ServletContext context) throws FacesException {
	FactoryFinder.releaseFactories();

	Assert.assert_it(null != 
		 context.getAttribute(RIConstants.DEFAULT_RENDER_KIT));
	context.removeAttribute(RIConstants.DEFAULT_RENDER_KIT);
    }
			 
    /**
     * <p>Return an Iterator over {@link SelectItem} instances representing the
     * available options for this component, assembled from the set of
     * {@link UISelectItem} and/or {@link UISelectItems} components that are
     * direct children of this component.  If there are no such children, a
     * zero-length array is returned.</p>
     *
     * @param context The {@link FacesContext} for the current request.
     * If null, the UISelectItems behavior will not work.
     *
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public static Iterator getSelectItems(FacesContext context,
					  UIComponent component) {

        ArrayList list = new ArrayList();
        Iterator kids = component.getChildren();
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            if (kid instanceof UISelectItem) {
                UISelectItem item = (UISelectItem) kid;
                list.add(new SelectItem(item.getItemValue(),
                                        item.getItemLabel(),
                                        item.getItemDescription()));
            } else if (kid instanceof UISelectItems && null != context) {
                Object value = kid.currentValue(context);
                if (value instanceof UISelectItem) {
                    list.add(value);
                } else if (value instanceof SelectItem[]) {
                    SelectItem items[] = (SelectItem[]) value;
                    for (int i = 0; i < items.length; i++) {
                        list.add(items[i]);
                    }
                } else if (value instanceof Collection) {
                    Iterator elements = ((Collection) value).iterator();
                    while (elements.hasNext()) {
                        list.add((SelectItem) elements.next());
                    }
                } else if (value instanceof Map) {
                    Iterator keys = ((Map) value).keySet().iterator();
                    while (keys.hasNext()) {
                        Object key = keys.next();
                        if (key == null) {
                            continue;
                        }
                        Object val = ((Map) value).get(key);
                        if (val == null) {
                            continue;
                        }
                        list.add(new SelectItem(val.toString(), key.toString(),
                                                null));
                    }
                }
            }
        }
        return (list.iterator());

    }

//
// General Methods
//

} // end of class Util
