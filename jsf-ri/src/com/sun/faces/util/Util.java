/*
 * $Id: Util.java,v 1.57 2003/05/01 02:04:40 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
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
import javax.faces.application.Message;
//import javax.faces.context.MessageResourcesFactory;
import javax.faces.context.MessageResources;


import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.el.ValueBinding;

import javax.faces.component.UISelectItem;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItems;
import javax.faces.component.SelectItem;

import com.sun.faces.RIConstants;
import com.sun.faces.context.MessageResourcesImpl;
import com.sun.faces.application.NavigationConfig;
import javax.faces.context.FacesContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Iterator;
import java.util.Locale;
import java.util.StringTokenizer;

/**
 *
 *  <B>Util</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: Util.java,v 1.57 2003/05/01 02:04:40 rkitain Exp $
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

    // README - make sure to add the message identifier constant
    // (ex: Util.CONVERSION_ERROR_MESSAGE_ID) and the number of substitution
    // parameters to test/com/sun/faces/util/TestUtil_messages (see comment there).
 
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

    public static final String MISSING_RESOURCE_ERROR_MESSAGE_ID =
        "com.sun.faces.MISSING_RESOURCE_ERROR";

    public static final String MISSING_CLASS_ERROR_MESSAGE_ID =
        "com.sun.faces.MISSING_CLASS_ERROR";
    public static final String COMPONENT_NOT_FOUND_ERROR_MESSAGE_ID =
        "com.sun.faces.COMPONENT_NOT_FOUND_ERROR";
    public static final String LIFECYCLE_ID_ALREADY_ADDED_ID =
        "com.sun.faces.LIFECYCLE_ID_ALREADY_ADDED";

    public static final String LIFECYCLE_ID_NOT_FOUND_ERROR_MESSAGE_ID =
        "com.sun.faces.LIFECYCLE_ID_NOT_FOUND";

    public static final String PHASE_ID_OUT_OF_BOUNDS_ERROR_MESSAGE_ID =
        "com.sun.faces.PHASE_ID_OUT_OF_BOUNDS";

    public static final String CANT_CREATE_LIFECYCLE_ERROR_MESSAGE_ID =
        "com.sun.faces.CANT_CREATE_LIFECYCLE_ERROR";

    public static final String ILLEGAL_MODEL_REFERENCE_ID =
        "com.sun.faces.ILLEGAL_MODEL_REFERENCE";
    
     public static final String ATTRIBUTE_NOT_SUPORTED_ERROR_MESSAGE_ID =
         "com.sun.faces.ATTRIBUTE_NOT_SUPORTED";

   public static final String FILE_NOT_FOUND_ERROR_MESSAGE_ID =
         "com.sun.faces.FILE_NOT_FOUND";

   public static final String CANT_PARSE_FILE_ERROR_MESSAGE_ID =
         "com.sun.faces.CANT_PARSE_FILE";

   public static final String CANT_INSTANTIATE_CLASS_ERROR_MESSAGE_ID =
         "com.sun.faces.CANT_INSTANTIATE_CLASS";

   public static final String ILLEGAL_CHARACTERS_ERROR_MESSAGE_ID =
         "com.sun.faces.ILLEGAL_CHARACTERS_ERROR";

   public static final String NOT_NESTED_IN_FACES_TAG_ERROR_MESSAGE_ID =
         "com.sun.faces.NOT_NESTED_IN_FACES_TAG_ERROR";

   public static final String NULL_BODY_CONTENT_ERROR_MESSAGE_ID =
         "com.sun.faces.NULL_BODY_CONTENT_ERROR";

   public static final String SAVING_STATE_ERROR_MESSAGE_ID =
         "com.sun.faces.SAVING_STATE_ERROR";

   public static final String RENDERER_ALREADY_EXISTS_ERROR_MESSAGE_ID =
         "com.sun.faces.RENDERER_ALREADY_EXISTS";

   public static final String RENDERER_NOT_FOUND_ERROR_MESSAGE_ID =
         "com.sun.faces.RENDERER_NOT_FOUND";

   public static final String MAXIMUM_EVENTS_REACHED_ERROR_MESSAGE_ID =
         "com.sun.faces.MAXIMUM_EVENTS_REACHED";

   public static final String NO_ACTION_FROM_ACTIONREF_ERROR_MESSAGE_ID = 
        "com.sun.faces.NO_ACTION_FROM_ACTIONREF";

   public static final String NULL_CONFIGURATION_ERROR_MESSAGE_ID = 
        "com.sun.faces.NULL_CONFIGURATION";

   public static final String ERROR_OPENING_FILE_ERROR_MESSAGE_ID = 
        "com.sun.faces.ERROR_OPENING_FILE";

   public static final String ERROR_REGISTERING_DTD_ERROR_MESSAGE_ID = 
        "com.sun.faces.ERROR_REGISTERING_DTD";

   public static final String INVALID_INIT_PARAM_ERROR_MESSAGE_ID =  
        "com.sun.faces.INVALID_INIT_PARAM";

// README - make sure to add the message identifier constant
// (ex: Util.CONVERSION_ERROR_MESSAGE_ID) and the number of substitution
// parameters to test/com/sun/faces/util/TestUtil_messages (see comment there).

//
// Class Variables
//

    /**

    * This array contains attributes that have a boolean value in JSP,
    * but have have no value in HTML.  For example "disabled" or
    * "readonly". <P>

    * @see renderBooleanPassthruAttributes

    */

    private static String booleanPassthruAttributes[] = {
	"disabled",
	"readonly",
        "ismap"
    };
	
    /**

    * This array contains attributes whose value is just rendered
    * straight to the content.  This array should only contain
    * attributes that require no interpretation by the Renderer.  If an
    * attribute requires interpretation by a Renderer, it should be
    * removed from this array.<P>

    * @see renderPassthruAttributes

    */
    private static String passthruAttributes[] = {
	"accesskey",
	"alt",
        "cols",
        "height",
	"lang",
	"longdesc",
	"maxlength",
	"onblur",
	"onchange",
	"onclick",
	"ondblclick",
	"onfocus",
	"onkeydown",
	"onkeypress",
	"onkeyup",
	"onload",
	"onmousedown",
	"onmousemove",
	"onmouseout",
	"onmouseover",
	"onmouseup",
	"onreset",
	"onselect",
	"onsubmit",
	"onunload",
        "rows",
	"size",
        "tabindex",
        "class",
        "title",
        "style",
        "width",
        "dir",
        "rules",
        "frame",
        "border",
        "cellspacing",
        "cellpadding",
        "summary",
        "bgcolor",
        "usemap",
        "enctype", 
        "acceptcharset", 
        "accept", 
        "target", 
        "onsubmit", 
        "onreset"
    };

private static long id = 0;


//
// Instance Variables
//

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
    public static Class loadClass(String name, 
				  Object fallbackClass) throws ClassNotFoundException {
	ClassLoader loader =
	    Thread.currentThread().getContextClassLoader();
	if (loader == null) {
	    loader = fallbackClass.getClass().getClassLoader();
	}
	return loader.loadClass(name);
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
/* FIXME
	MessageResourcesFactory factory = (MessageResourcesFactory)
	    FactoryFinder.getFactory
	    (FactoryFinder.MESSAGE_RESOURCES_FACTORY);
	resources = factory.getMessageResources
	    (MessageResourcesFactory.FACES_IMPL_MESSAGES);
*/
	
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
	ApplicationFactory applicationFactory = null;
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

	applicationFactory = (ApplicationFactory)
	    FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
	Assert.assert_it(null != applicationFactory);

	defaultRenderKit = 
	    renderKitFactory.getRenderKit(RIConstants.DEFAULT_RENDER_KIT);
	Assert.assert_it(null != defaultRenderKit);
	context.setAttribute(RIConstants.DEFAULT_RENDER_KIT, 
			     defaultRenderKit);

	// PENDING(rogerk): need a cleaner way to do this
	context.setAttribute(RIConstants.NAVIGATION_CONFIG_ATTR, 
			     new NavigationConfig(context));
	context.setAttribute(RIConstants.ONE_TIME_INITIALIZATION_ATTR,
			     RIConstants.ONE_TIME_INITIALIZATION_ATTR);
    }

    /**

    * <p>Verifies that the required classes are available on either the
    * ContextClassLoader, or the local ClassLoader.  Currently only
    * checks for the class
    * "javax.servlet.jsp.jstl.fmt.LocalizationContext", which is used
    * for Localization.</p>  

    * <p>The result of the check is saved in the ServletContext
    * attribute RIConstants.HAS_REQUIRED_CLASSES_ATTR.</p>

    * <p>Algorithm:</p>

    * <p>Check the ServletContext for the attribute, if found, and the
    * value is false, that means we've checked before, and we don't have
    * the classes, just throw FacesException.  If the value is true,
    * we've checked before and we have the classes, just return.</p>

    */

    public static void verifyRequiredClasses(FacesContext facesContext) throws FacesException {
        Map applicationMap = facesContext.getExternalContext().getApplicationMap();
	Boolean result = null;
	String className = "javax.servlet.jsp.jstl.fmt.LocalizationContext";
	Object [] params = {className};

	// Have we checked before?
	if (null != (result = (Boolean)
            applicationMap.get(RIConstants.HAS_REQUIRED_CLASSES_ATTR))) {
	    // yes, and the check failed.
	    if (Boolean.FALSE == result) {
		throw new 
		    FacesException(Util.getExceptionMessage(Util.MISSING_CLASS_ERROR_MESSAGE_ID, params));
	    }
	    else {
		// yes, and the check passed.
		return;
	    }
	}

	//
	// We've not checked before, so do the check now!
	// 

	try {
	    Util.loadClass(className, facesContext);
	}
	catch (ClassNotFoundException e) {
	    applicationMap.put(RIConstants.HAS_REQUIRED_CLASSES_ATTR, Boolean.FALSE);
	    throw new FacesException(Util.getExceptionMessage(Util.MISSING_CLASS_ERROR_MESSAGE_ID, params), e);
	}
	applicationMap.put(RIConstants.HAS_REQUIRED_CLASSES_ATTR, Boolean.TRUE);
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
     * <p>Return an Iterator over {@link SelectItemWrppaer} instances representing the
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
    public static Iterator getSelectItemWrappers(FacesContext context,
					  UIComponent component) {

        ArrayList list = new ArrayList();
        Iterator kids = component.getChildren();
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            if (kid instanceof UISelectItem) {
                Object value = ((UISelectItem)kid).currentValue(context);
                if ( value == null ) {
                    UISelectItem item = (UISelectItem) kid;
                    list.add(new SelectItemWrapper( kid,
                                        new SelectItem(item.getItemValue(),
                                        item.getItemLabel(),
                                        item.getItemDescription())));
                } else if ( value instanceof SelectItem){
                    list.add(new SelectItemWrapper(kid,
                            ((SelectItem)value)));
                } else {
                    throw new IllegalArgumentException(Util.getExceptionMessage(
                        Util.CONVERSION_ERROR_MESSAGE_ID));
                }
            } else if (kid instanceof UISelectItems && null != context) {
                Object value = ((UISelectItems)kid).currentValue(context);
                if (value instanceof SelectItem) {
                    SelectItem item = (SelectItem) kid;
                    list.add(new SelectItemWrapper( kid, item));
                } else if (value instanceof SelectItem[]) {
                    SelectItem items[] = (SelectItem[]) value;
                    for (int i = 0; i < items.length; i++) {
                        list.add(new SelectItemWrapper(kid, items[i]));
                    }
                } else if (value instanceof Collection) {
                    Iterator elements = ((Collection) value).iterator();
                    while (elements.hasNext()) {
                        list.add(new SelectItemWrapper(kid, (SelectItem) elements.next()));
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
                        list.add(new SelectItemWrapper( kid, 
                            new SelectItem(val.toString(), key.toString(),null)));
                    }
                } else {
                    throw new IllegalArgumentException(Util.getExceptionMessage(
                        Util.CONVERSION_ERROR_MESSAGE_ID));
                }
            }
        }
        return (list.iterator());

    }

    /**

    * Return a Locale instance using the following algorithm: <P>

     	<UL>

	<LI>

	If this component instance has an attribute named "bundle",
	interpret it as a model reference to a LocalizationContext
	instance accessible via FacesContext.getModelValue().

	</LI>

	<LI>

	If FacesContext.getModelValue() returns a LocalizationContext
	instance, return its Locale.

	</LI>

	<LI>

	If FacesContext.getModelValue() doesn't return a
	LocalizationContext, return the FacesContext's Locale.

	</LI>

	</UL>



    */

    public static Locale 
	getLocaleFromContextOrComponent(FacesContext context,
					UIComponent component) {
	Locale result = null;
	String bundleName = null, bundleAttr = "bundle";
	
	ParameterCheck.nonNull(context);
	ParameterCheck.nonNull(component);
	
	// verify our component has the proper attributes for bundle.
	if (null != (bundleName = (String)component.getAttribute(bundleAttr))){
	    // verify there is a Locale for this localizationContext
	    javax.servlet.jsp.jstl.fmt.LocalizationContext locCtx = null;
	    if (null != (locCtx = 
			 (javax.servlet.jsp.jstl.fmt.LocalizationContext) 
                         (Util.getValueBinding(bundleName)).getValue(context))) {
                result = locCtx.getLocale();
		Assert.assert_it(null != result);
	    }
	}
	if (null == result) {
	    result = context.getLocale();
	}

	return result;
    }


    /**

    * Render any boolean "passthru" attributes.  
    * <P>

    * @see passthruAttributes

    */

    public static String renderBooleanPassthruAttributes(FacesContext context,
						       UIComponent component) {
	int i = 0, len = booleanPassthruAttributes.length;
	String value;
	boolean thisIsTheFirstAppend = true;
	StringBuffer renderedText = new StringBuffer();

	for (i = 0; i < len; i++) {
	    if (null != (value = (String) 
		      component.getAttribute(booleanPassthruAttributes[i]))) {
		if (thisIsTheFirstAppend) {
		    // prepend ' '
		    renderedText.append(' ');
		    thisIsTheFirstAppend = false;
		}
		if (Boolean.valueOf(value).booleanValue()) {
		    renderedText.append(booleanPassthruAttributes[i] + ' ');
		}
	    }
	}
	
	return renderedText.toString();
    }

    /**

    * Render any "passthru" attributes, where we simply just output the
    * raw name and value of the attribute.  This method is aware of the
    * set of HTML4 attributes that fall into this bucket.  Examples are
    * all the javascript attributes, alt, rows, cols, etc.  <P>

    * @return the rendererd attributes as specified in the component.
    * Padded with leading and trailing ' '.  If there are no passthru
    * attributes in the component, return the empty String.

    * @see passthruAttributes

    */

    public static String renderPassthruAttributes(FacesContext context,
						  UIComponent component) {
	int i = 0, len = passthruAttributes.length;
	String value;
	boolean thisIsTheFirstAppend = true;
	StringBuffer renderedText = new StringBuffer();

	for (i = 0; i < len; i++) {
	    if (null != (value = (String) 
			 component.getAttribute(passthruAttributes[i]))) {
		if (thisIsTheFirstAppend) {
		    // prepend ' '
		    renderedText.append(' ');
		    thisIsTheFirstAppend = false;
		}
		renderedText.append(passthruAttributes[i] + "=\"" + value + 
				    "\" ");
	    }
	}
	
	return renderedText.toString();
    }

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
    
    public static ValueBinding getValueBinding(String valueRef) {
        ApplicationFactory factory = (ApplicationFactory)
                FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        Application application = factory.getApplication();
        ValueBinding binding = application.getValueBinding(valueRef);
        return binding;
    }         

//
// General Methods
//

} // end of class Util
