/*
 * $Id: Util.java,v 1.160 2005/05/18 17:34:08 jayashri Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// Util.java

package com.sun.faces.util;

import com.sun.faces.RIConstants;
import com.sun.faces.renderkit.RenderKitImpl;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.FacesMessage;
import javax.faces.application.StateManager;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.el.MethodBinding;
import javax.faces.el.ReferenceSyntaxException;
import javax.el.ValueExpression;
import javax.el.ELContext;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.model.SelectItem;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.ResponseStateManager;
import javax.servlet.ServletContext;
import javax.faces.el.ValueBinding;
import javax.servlet.jsp.jstl.fmt.LocalizationContext;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.text.MessageFormat;

/**
 * <B>Util</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: Util.java,v 1.160 2005/05/18 17:34:08 jayashri Exp $
 */

public class Util extends Object {

    //
    // Private/Protected Constants
    //
    public static final String FACES_LOGGER = "javax.enterprise.resource.jsf.";
    
    public static final String FACES_LOG_STRINGS = 
            "com.sun.faces.LogStrings";
    
    // Log instance for this class
    private static Logger logger;
    static {
        logger = getLogger(FACES_LOGGER);
    }
    
    // README - make sure to add the message identifier constant
    // (ex: Util.CONVERSION_ERROR_MESSAGE_ID) and the number of substitution
    // parameters to test/com/sun/faces/util/TestUtil_messages (see comment there).
 
    /**
     * The message identifier of the {@link FacesMessage} to be created as
     * a result of type conversion error.
     */
    public static final String CONVERSION_ERROR_MESSAGE_ID =
        "com.sun.faces.TYPECONVERSION_ERROR";

    /**
     * The message identifier of the {@link FacesMessage} to be created if
     * there is model update failure.
     */
    public static final String MODEL_UPDATE_ERROR_MESSAGE_ID =
        "com.sun.faces.MODELUPDATE_ERROR";

    public static final String FACES_CONTEXT_CONSTRUCTION_ERROR_MESSAGE_ID =
        "com.sun.faces.FACES_CONTEXT_CONSTRUCTION_ERROR";

    public static final String NULL_COMPONENT_ERROR_MESSAGE_ID =
        "com.sun.faces.NULL_COMPONENT_ERROR";

    public static final String NULL_REQUEST_VIEW_ERROR_MESSAGE_ID =
        "com.sun.faces.NULL_REQUEST_VIEW_ERROR";

    public static final String NULL_RESPONSE_VIEW_ERROR_MESSAGE_ID =
        "com.sun.faces.NULL_RESPONSE_VIEW_ERROR";

    public static final String REQUEST_VIEW_ALREADY_SET_ERROR_MESSAGE_ID =
        "com.sun.faces.REQUEST_VIEW_ALREADY_SET_ERROR";

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
    
    public static final String NOT_NESTED_IN_TYPE_TAG_ERROR_MESSAGE_ID =
        "com.sun.faces.NOT_NESTED_IN_TYPE_TAG_ERROR";

    public static final String NULL_BODY_CONTENT_ERROR_MESSAGE_ID =
        "com.sun.faces.NULL_BODY_CONTENT_ERROR";

    public static final String SAVING_STATE_ERROR_MESSAGE_ID =
        "com.sun.faces.SAVING_STATE_ERROR";

    public static final String RENDERER_NOT_FOUND_ERROR_MESSAGE_ID =
        "com.sun.faces.RENDERER_NOT_FOUND";

    public static final String MAXIMUM_EVENTS_REACHED_ERROR_MESSAGE_ID =
        "com.sun.faces.MAXIMUM_EVENTS_REACHED";

    public static final String NULL_CONFIGURATION_ERROR_MESSAGE_ID =
        "com.sun.faces.NULL_CONFIGURATION";

    public static final String ERROR_OPENING_FILE_ERROR_MESSAGE_ID =
        "com.sun.faces.ERROR_OPENING_FILE";

    public static final String ERROR_REGISTERING_DTD_ERROR_MESSAGE_ID =
        "com.sun.faces.ERROR_REGISTERING_DTD";

    public static final String INVALID_INIT_PARAM_ERROR_MESSAGE_ID =
        "com.sun.faces.INVALID_INIT_PARAM";

    public static final String ERROR_SETTING_BEAN_PROPERTY_ERROR_MESSAGE_ID =
        "com.sun.faces.ERROR_SETTING_BEAN_PROPERTY";

    public static final String ERROR_GETTING_VALUE_BINDING_ERROR_MESSAGE_ID =
        "com.sun.faces.ERROR_GETTING_VALUE_BINDING";

    public static final String ERROR_GETTING_VALUEREF_VALUE_ERROR_MESSAGE_ID =
        "com.sun.faces.ERROR_GETTING_VALUEREF_VALUE";

    public static final String CANT_INTROSPECT_CLASS_ERROR_MESSAGE_ID =
        "com.sun.faces.CANT_INTROSPECT_CLASS";

    public static final String CANT_CONVERT_VALUE_ERROR_MESSAGE_ID =
        "com.sun.faces.CANT_CONVERT_VALUE";

    public static final String INVALID_SCOPE_LIFESPAN_ERROR_MESSAGE_ID =
        "com.sun.faces.INVALID_SCOPE_LIFESPAN";

    public static final String CONVERTER_NOT_FOUND_ERROR_MESSAGE_ID =
        "com.sun.faces.CONVERTER_NOT_FOUND_ERROR";

    public static final String VALIDATOR_NOT_FOUND_ERROR_MESSAGE_ID =
        "com.sun.faces.VALIDATOR_NOT_FOUND_ERROR";

    public static final String CANT_LOAD_CLASS_ERROR_MESSAGE_ID =
        "com.sun.faces.CANT_INSTANTIATE_CLASS";

    public static final String ENCODING_ERROR_MESSAGE_ID =
        "com.sun.faces.ENCODING_ERROR";

    public static final String ILLEGAL_IDENTIFIER_LVALUE_MODE_ID =
        "com.sun.faces.ILLEGAL_IDENTIFIER_LVALUE_MODE";

    public static final String VALIDATION_ID_ERROR_ID =
        "com.sun.faces.VALIDATION_ID_ERROR";

    public static final String VALIDATION_EL_ERROR_ID =
        "com.sun.faces.VALIDATION_EL_ERROR";

    public static final String VALIDATION_COMMAND_ERROR_ID =
        "com.sun.faces.VALIDATION_COMMAND_ERROR";

    public static final String CONTENT_TYPE_ERROR_MESSAGE_ID =
        "com.sun.faces.CONTENT_TYPE_ERROR";

    public static final String COMPONENT_NOT_FOUND_IN_VIEW_WARNING_ID =
        "com.sun.faces.COMPONENT_NOT_FOUND_IN_VIEW_WARNING";

    public static final String ILLEGAL_ATTEMPT_SETTING_VIEWHANDLER_ID =
        "com.sun.faces.ILLEGAL_ATTEMPT_SETTING_VIEWHANDLER";

    public static final String ILLEGAL_ATTEMPT_SETTING_STATEMANAGER_ID =
        "com.sun.faces.ILLEGAL_ATTEMPT_SETTING_STATEMANAGER";

    public static final String INVALID_MESSAGE_SEVERITY_IN_CONFIG_ID =
        "com.sun.faces.INVALID_MESSAGE_SEVERITY_IN_CONFIG";

    public static final String CANT_CLOSE_INPUT_STREAM_ID =
        "com.sun.faces.CANT_CLOSE_INPUT_STREAM";

    public static final String DUPLICATE_COMPONENT_ID_ERROR_ID =
        "com.sun.faces.DUPLICATE_COMPONENT_ID_ERROR";

    public static final String FACES_SERVLET_MAPPING_CANNOT_BE_DETERMINED_ID =
        "com.sun.faces.FACES_SERVLET_MAPPING_CANNOT_BE_DETERMINED";

    public static final String ILLEGAL_VIEW_ID_ID =
        "com.sun.faces.ILLEGAL_VIEW_ID";

    public static final String INVALID_EXPRESSION_ID =
        "com.sun.faces.INVALID_EXPRESSION";
    public static final String NULL_FORVALUE_ID =
        "com.sun.faces.NULL_FORVALUE";
    public static final String EMPTY_PARAMETER_ID =
        "com.sun.faces.EMPTY_PARAMETER";
    public static final String ASSERTION_FAILED_ID =
        "com.sun.faces.ASSERTION_FAILED";
    public static final String OBJECT_CREATION_ERROR_ID =
        "com.sun.faces.OBJECT_CREATION_ERROR";
    public static final String CANT_CREATE_CLASS_ERROR_ID = 
        "com.sun.faces.CANT_CREATE_CLASS_ERROR";
    
    public static final String CYCLIC_REFERENCE_ERROR_ID =
        "com.sun.faces.CYCLIC_REFERENCE_ERROR";
    
    public static final String NO_DTD_FOUND_ERROR_ID =
        "com.sun.faces.NO_DTD_FOUND_ERROR";          
    
    public static final String MANAGED_BEAN_CANNOT_SET_LIST_ARRAY_PROPERTY_ID = 
        "com.sun.faces.MANAGED_BEAN_CANNOT_SET_LIST_ARRAY_PROPERTY";
    
    public static final String MANAGED_BEAN_EXISTING_VALUE_NOT_LIST_ID =
        "com.sun.faces.MANAGED_BEAN_EXISTING_VALUE_NOT_LIST";

    public static final String MANAGED_BEAN_CANNOT_SET_MAP_PROPERTY_ID =
        "com.sun.faces.MANAGED_BEAN_CANNOT_SET_MAP_PROPERTY";
    
    public static final String MANAGED_BEAN_TYPE_CONVERSION_ERROR_ID =
        "com.sun.faces.MANAGED_BEAN_TYPE_CONVERSION_ERROR";
    
    public static final String APPLICATION_ASSOCIATE_CTOR_WRONG_CALLSTACK_ID = 
        "com.sun.faces.APPLICATION_ASSOCIATE_CTOR_WRONG_CALLSTACK";
    
    public static final String APPLICATION_ASSOCIATE_EXISTS_ID =
        "com.sun.faces.APPLICATION_ASSOCIATE_EXISTS";

    public static final String OBJECT_IS_READONLY =
        "com.sun.faces.OBJECT_IS_READONLY";

    public static final String APPLICATION_INIT_COMPLETE_ERROR_ID = 
            "com.sun.faces.APPLICATION_INIT_COMPLETE_ERROR_ID";

    public static final String INCORRECT_JSP_VERSION_ID = 
            "com.sun.faces.INCORRECT_JSP_VERSION";
    
    public static final String EL_OUT_OF_BOUNDS_ERROR_ID = 
            "com.sun.faces.OUT_OF_BOUNDS_ERROR";
    public static final String EL_PROPERTY_TYPE_ERROR_ID = 
            "com.sun.faces.PROPERTY_TYPE_ERROR";
    public static final String EL_SIZE_OUT_OF_BOUNDS_ERROR_ID = 
            "com.sun.faces.SIZE_OUT_OF_BOUNDS_ERROR";
    
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
     *
     * @see #renderBooleanPassThruAttributes
     */

    private static String[] booleanPassthruAttributes = {
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
     *
     * @see #renderPassThruAttributes
     */
    private static String[][] passthruAttributes = {
        { "accept", null },
        { "accesskey", null },
        { "alt", null },
        { "bgcolor", null },
        { "border", null },
        { "cellpadding", null },
        { "cellspacing", null },
        { "charset", null },
        { "cols", null },
        { "coords", null },
        { "dir", null },
        { "enctype", null },
        { "frame", null },
        { "height", null },
        { "hreflang", null },
        { "lang", "xml" },
        { "longdesc", null },
        { "maxlength", null },
        { "onblur", null },
        { "onchange", null },
        { "onclick", null },
        { "ondblclick", null },
        { "onfocus", null },
        { "onkeydown", null },
        { "onkeypress", null },
        { "onkeyup", null },
        { "onload", null },
        { "onmousedown", null },
        { "onmousemove", null },
        { "onmouseout", null },
        { "onmouseover", null },
        { "onmouseup", null },
        { "onreset", null },
        { "onselect", null },
        { "onsubmit", null },
        { "onunload", null },
        { "rel", null },
        { "rev", null },
        { "rows", null },
        { "rules", null },
        { "shape", null },
        { "size", null },
        { "style", null },
        { "summary", null },
        { "tabindex", null },
        { "target", null },
        { "title", null },
        { "usemap", null },
        { "width", null }
    };

    //NOTE - "type" was deliberately skipped from the list of passthru
    //attrs above All renderers that need this attribute should manually
    //pass it.



//
// Instance Variables
//

// Attribute Instance Variables

// Relationship Instance Variables

//
// Constructors and Initializers    
//

    private Util() {
        throw new IllegalStateException();
    }

//
// Class methods
//
    public static Class loadClass(String name,
                                  Object fallbackClass)
        throws ClassNotFoundException {
        ClassLoader loader = Util.getCurrentLoader(fallbackClass);
        return loader.loadClass(name);
    }


    public static ClassLoader getCurrentLoader(Object fallbackClass) {
        ClassLoader loader =
            Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            loader = fallbackClass.getClass().getClassLoader();
        }
        return loader;
    }
    
    public static Logger getLogger( String loggerName ) {
        return Logger.getLogger(loggerName, FACES_LOG_STRINGS );
    }


    /**
     * Called by the RI to get the IMPL_MESSAGES MessageFactory
     * instance and get a message on it.
     */

    public static synchronized String getExceptionMessageString(String messageId,
                                                          Object[] params) {
        String result = null;

        FacesMessage message = MessageFactory.getMessage(messageId, params);
        if (null != message) {
            result = message.getSummary();
        }


        if (null == result) {
            result = "null MessageFactory";
        } else {
            if ( params != null) {
                result = MessageFormat.format(result, params);
            }
        }
        return result;
    }


    public static synchronized String getExceptionMessageString(String messageId) {
        return Util.getExceptionMessageString(messageId, null);
    }

    public static synchronized FacesMessage getExceptionMessage(String messageId,
								      Object[] params) {
        return MessageFactory.getMessage(messageId, params);
    }


    public static synchronized FacesMessage getExceptionMessage(String messageId) {
        return Util.getExceptionMessage(messageId, null);
    }

    /**
     * Verify the existence of all the factories needed by faces.  Create
     * and install the default RenderKit into the ServletContext. <P>
     *
     * @see javax.faces.FactoryFinder
     */

    public static void verifyFactoriesAndInitDefaultRenderKit(ServletContext context)
        throws FacesException {
        RenderKitFactory renderKitFactory = null;
        LifecycleFactory lifecycleFactory = null;
        FacesContextFactory facesContextFactory = null;
        ApplicationFactory applicationFactory = null;
        RenderKit defaultRenderKit = null;

        renderKitFactory = (RenderKitFactory)
            FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        assert (null != renderKitFactory);

        lifecycleFactory = (LifecycleFactory)
            FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
        assert (null != lifecycleFactory);

        facesContextFactory = (FacesContextFactory)
            FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
        assert (null != facesContextFactory);

        applicationFactory = (ApplicationFactory)
            FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        assert (null != applicationFactory);

        defaultRenderKit =
            renderKitFactory.getRenderKit(null,
                                          RenderKitFactory.HTML_BASIC_RENDER_KIT);
        if (defaultRenderKit == null) {
            // create default renderkit if doesn't exist
            //
            defaultRenderKit = new RenderKitImpl();
            renderKitFactory.addRenderKit(
                RenderKitFactory.HTML_BASIC_RENDER_KIT,
                defaultRenderKit);
        }

        context.setAttribute(RIConstants.HTML_BASIC_RENDER_KIT,
                             defaultRenderKit);

        context.setAttribute(RIConstants.ONE_TIME_INITIALIZATION_ATTR,
                             RIConstants.ONE_TIME_INITIALIZATION_ATTR);
    }


    /**
     * <p>Verifies that the required classes are available on either the
     * ContextClassLoader, or the local ClassLoader.  Currently only
     * checks for the class
     * "javax.servlet.jsp.jstl.fmt.LocalizationContext", which is used
     * for Localization.</p>
     * <p/>
     * <p>The result of the check is saved in the ServletContext
     * attribute RIConstants.HAS_REQUIRED_CLASSES_ATTR.</p>
     * <p/>
     * <p>Algorithm:</p>
     * <p/>
     * <p>Check the ServletContext for the attribute, if found, and the
     * value is false, that means we've checked before, and we don't have
     * the classes, just throw FacesException.  If the value is true,
     * we've checked before and we have the classes, just return.</p>
     */

    public static void verifyRequiredClasses(FacesContext facesContext)
        throws FacesException {
        Map applicationMap = facesContext.getExternalContext()
            .getApplicationMap();
        Boolean result = null;
        String className = "javax.servlet.jsp.jstl.fmt.LocalizationContext";
        Object[] params = {className};

        // Have we checked before?
        if (null != (result = (Boolean)
            applicationMap.get(RIConstants.HAS_REQUIRED_CLASSES_ATTR))) {
            // yes, and the check failed.
            if (Boolean.FALSE == result) {
                throw new
                    FacesException(
                        Util.getExceptionMessageString(
                            Util.MISSING_CLASS_ERROR_MESSAGE_ID, params));
            } else {
                // yes, and the check passed.
                return;
            }
        }

        //
        // We've not checked before, so do the check now!
        //

        try {
            Util.loadClass(className, facesContext);
        } catch (ClassNotFoundException e) {
            applicationMap.put(RIConstants.HAS_REQUIRED_CLASSES_ATTR,
                               Boolean.FALSE);
            throw new FacesException(
                Util.getExceptionMessageString(Util.MISSING_CLASS_ERROR_MESSAGE_ID,
                                         params),
                e);
        }
        applicationMap.put(RIConstants.HAS_REQUIRED_CLASSES_ATTR, Boolean.TRUE);
    }


    /**
     * <p>Return an Iterator over {@link SelectItem} instances representing the
     * available options for this component, assembled from the set of
     * {@link UISelectItem} and/or {@link UISelectItems} components that are
     * direct children of this component.  If there are no such children, a
     * zero-length array is returned.</p>
     *
     * @param context The {@link FacesContext} for the current request.
     *                If null, the UISelectItems behavior will not work.
     * @throws NullPointerException if <code>context</code>
     *                              is <code>null</code>
     */
    public static Iterator getSelectItems(FacesContext context,
                                          UIComponent component) {

        ArrayList list = new ArrayList();
        Iterator kids = component.getChildren().iterator();
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            if (kid instanceof UISelectItem) {
                Object value = ((UISelectItem) kid).getValue();
                if (value == null) {
                    UISelectItem item = (UISelectItem) kid;
                    list.add(new SelectItem(item.getItemValue(),
                                            item.getItemLabel(),
                                            item.getItemDescription(),
                                            item.isItemDisabled()));
                } else if (value instanceof SelectItem) {
                    list.add(value);
                } else {
                    throw new IllegalArgumentException(Util.getExceptionMessageString(
                        Util.CONVERSION_ERROR_MESSAGE_ID));
                }
            } else if (kid instanceof UISelectItems && null != context) {
                Object value = ((UISelectItems) kid).getValue();
                if (value instanceof SelectItem) {
                    list.add(value);
                } else if (value instanceof SelectItem[]) {
                    SelectItem[] items = (SelectItem[]) value;
                    for (int i = 0; i < items.length; i++) {
                        list.add(items[i]);
                    }
                } else if (value instanceof Collection) {
                    Iterator elements = ((Collection) value).iterator();
                    while (elements.hasNext()) {
                        list.add(elements.next());
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
                } else {
                    throw new IllegalArgumentException(Util.getExceptionMessageString(
                        Util.CONVERSION_ERROR_MESSAGE_ID));
                }
            }
        }
        return (list.iterator());

    }


    /**
     * Return a Locale instance using the following algorithm: <P>
     * <p/>
     * <UL>
     * <p/>
     * <LI>
     * <p/>
     * If this component instance has an attribute named "bundle",
     * interpret it as a model reference to a LocalizationContext
     * instance accessible via FacesContext.getModelValue().
     * <p/>
     * </LI>
     * <p/>
     * <LI>
     * <p/>
     * If FacesContext.getModelValue() returns a LocalizationContext
     * instance, return its Locale.
     * <p/>
     * </LI>
     * <p/>
     * <LI>
     * <p/>
     * If FacesContext.getModelValue() doesn't return a
     * LocalizationContext, return the FacesContext's Locale.
     * <p/>
     * </LI>
     * <p/>
     * </UL>
     */

    public static Locale
        getLocaleFromContextOrComponent(FacesContext context,
                                        UIComponent component) {
        Locale result = null;
        String bundleName = null;
        String bundleAttr = "bundle";

        Util.parameterNonNull(context);
        Util.parameterNonNull(component);

        // verify our component has the proper attributes for bundle.
        if (null !=
            (bundleName = (String) component.getAttributes().get(bundleAttr))) {
            // verify there is a Locale for this localizationContext
            LocalizationContext locCtx = null;
            if (null != (locCtx =
                (javax.servlet.jsp.jstl.fmt.LocalizationContext)
                (Util.getValueExpression(bundleName)).
                getValue(context.getELContext()))) {
                result = locCtx.getLocale();
                assert (null != result);
            }
        }
        if (null == result) {
            result = context.getViewRoot().getLocale();
        }

        return result;
    }


    /**
     * @return true if the component has any passthru attributes
     */
    // PENDING() it would be much more performant to have the tag be
    // aware of the passthru attributes and have it set a
    // "hasPassthruAttributes" attribute to true if the component has
    // any.  

    public static boolean hasPassThruAttributes(UIComponent component) {
        if (null == component) {
            return false;
        }

        boolean result = false;
        Map attrs = component.getAttributes();
        if (null == attrs) {
            return false;
        }
        Object attrVal;
        String empty = "";
        for (int i = 0; i < passthruAttributes.length; i++) {
            if (null != (attrVal = attrs.get(passthruAttributes[i][0]))
                &&
                !empty.equals(attrVal)) {
                result = true;
                break;
            }
        }
        if (!result) {
            for (int i = 0; i < booleanPassthruAttributes.length; i++) {
                if (null !=
                    (attrVal = attrs.get(booleanPassthruAttributes[i]))
                    &&
                    !empty.equals(attrVal)) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }


    public static void renderBooleanPassThruAttributes(ResponseWriter writer,
                                                       UIComponent component)
        throws IOException {
        renderBooleanPassThruAttributes(writer, component, null);
    }


    /**
     * Render any boolean "passthru" attributes.
     * <P>
     *
     * @see passthruAttributes
     */
    public static void renderBooleanPassThruAttributes(ResponseWriter writer,
                                                       UIComponent component,
                                                       String[] excludes)
        throws IOException {
        assert (null != writer);
        assert (null != component);

        Object value;
        boolean result;
        boolean skip;

        for (int i = 0, len = booleanPassthruAttributes.length; i < len; i++) {
            skip = false;
            if (null != excludes) {
                for (int j = 0, jLen = excludes.length; j < jLen; j++) {
                    if (null != excludes[j] &&
                        excludes[j].equals(booleanPassthruAttributes[i])) {
                        skip = true;
                        break;
                    }
                }
            }
            if (skip) {
                continue;
            }

            value =
                component.getAttributes().get(booleanPassthruAttributes[i]);
            if (value != null) {
                if (value instanceof Boolean) {
                    result = ((Boolean) value).booleanValue();
                } else {
                    if (!(value instanceof String)) {
                        value = value.toString();
                    }
                    result = (Boolean.valueOf((String) value)).booleanValue();
                }
                //PENDING(rogerk) will revisit "null" param soon..
                if (result) {
                    // NOTE:  render things like readonly="readonly" here
                    writer.writeAttribute(booleanPassthruAttributes[i],
                                          booleanPassthruAttributes[i],
                                          booleanPassthruAttributes[i]);
                    // NOTE:  otherwise render nothing
                }
            }
        }
    }


    public static void renderPassThruAttributes(FacesContext context, 
						ResponseWriter writer,
                                                UIComponent component)
        throws IOException {
        renderPassThruAttributes(context, writer, component, null);
    }


    /**
     * Render any "passthru" attributes, where we simply just output the
     * raw name and value of the attribute.  This method is aware of the
     * set of HTML4 attributes that fall into this bucket.  Examples are
     * all the javascript attributes, alt, rows, cols, etc.  <P>
     *
     * @see passthruAttributes
     */
    public static void renderPassThruAttributes(FacesContext context, 
						ResponseWriter writer,
                                                UIComponent component,
                                                String[] excludes)
        throws IOException {
        assert (null != writer);
        assert (null != component);

        Object value;
        boolean skip;
        for (int i = 0, len = passthruAttributes.length; i < len; i++) {
            skip = false;
            if (null != excludes) {
                for (int j = 0, jLen = excludes.length; j < jLen; j++) {
                    if (null != excludes[j] &&
                        excludes[j].equals(passthruAttributes[i][0])) {
                        skip = true;
                        break;
                    }
                }
            }
            if (skip) {
                continue;
            }

            value = component.getAttributes().get(passthruAttributes[i][0]);
            if (value != null && shouldRenderAttribute(value)) {
                if (!(value instanceof String)) {
                    value = value.toString();
                }
                //PENDING(rogerk) will revisit "null" param soon..
		// if we have no XML prefix
		if (null == passthruAttributes[i][1]) {
		    writer.writeAttribute(passthruAttributes[i][0], value,
					  passthruAttributes[i][0]);
		}
		else {
		    if (null == context) {
			context = FacesContext.getCurrentInstance();
		    }
		    // we have an XML prefix.  
		    if (null != context && 
			null != context.getExternalContext().getRequestMap().get(RIConstants.CONTENT_TYPE_IS_XHTML)) {
			// if we're XHTML, write it out per
			// http://www.w3.org/TR/xhtml1/#C_7
			writer.writeAttribute(passthruAttributes[i][1] + ':' +
					      passthruAttributes[i][0], value,
					      passthruAttributes[i][0]);
		    }
		    writer.writeAttribute(passthruAttributes[i][0], value,
					  passthruAttributes[i][0]);
		}
            }
        }
    }


    /**
     * @return true if and only if the argument
     *         <code>attributeVal</code> is an instance of a wrapper for a
     *         primitive type and its value is equal to the default value for
     *         that type as given in the spec.
     */

    private static boolean shouldRenderAttribute(Object attributeVal) {
        if (attributeVal instanceof Boolean &&
            ((Boolean) attributeVal).booleanValue() ==
            Boolean.FALSE.booleanValue()) {
            return false;
        } else if (attributeVal instanceof Integer &&
            ((Integer) attributeVal).intValue() == Integer.MIN_VALUE) {
            return false;
        } else if (attributeVal instanceof Double &&
            ((Double) attributeVal).doubleValue() == Double.MIN_VALUE) {
            return false;
        } else if (attributeVal instanceof Character &&
            ((Character) attributeVal).charValue() == Character.MIN_VALUE) {
            return false;
        } else if (attributeVal instanceof Float &&
            ((Float) attributeVal).floatValue() == Float.MIN_VALUE) {
            return false;
        } else if (attributeVal instanceof Short &&
            ((Short) attributeVal).shortValue() == Short.MIN_VALUE) {
            return false;
        } else if (attributeVal instanceof Byte &&
            ((Byte) attributeVal).byteValue() == Byte.MIN_VALUE) {
            return false;
        } else if (attributeVal instanceof Long &&
            ((Long) attributeVal).longValue() == Long.MIN_VALUE) {
            return false;
        }
        return true;
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
            } else {
                // it's not a delimiter, just output it.
                result.append(curToken);
            }
        }


        return result.toString();
    }

    public static Object evaluateValueExpression(ValueExpression expression,
                                                 ELContext elContext) {
           if (expression.isLiteralText()) {
               return expression.getExpressionString();
           } else {
               return expression.getValue(elContext);
           }
       }


    public static Object evaluateVBExpression(String expression) {
        if (expression == null || (!isVBExpression(expression))) {
            return expression;
        }
        FacesContext context = FacesContext.getCurrentInstance();
        Object result =
            getValueExpression(expression).getValue(context.getELContext());
        return result;

    }


    public static ValueBinding getValueBinding(String valueRef) {
        ValueBinding vb = null;
        // Must parse the value to see if it contains more than
        // one expression
        FacesContext context = FacesContext.getCurrentInstance();
        vb = context.getApplication().createValueBinding(valueRef);
        return vb;
    }

    public static ValueExpression getValueExpression(String valueRef) {
        ValueExpression ve = null;
        // Must parse the value to see if it contains more than
        // one expression
        FacesContext context = FacesContext.getCurrentInstance();
        ve = context.getApplication().getExpressionFactory().
            createValueExpression(context.getELContext(), valueRef, 
                Object.class);
        return ve;
    }


    public static MethodBinding createMethodBinding(String methodRef,
                                                    Class[] params) {
        ApplicationFactory factory = (ApplicationFactory)
            FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        Application application = factory.getApplication();
        MethodBinding binding = application.createMethodBinding(methodRef,
                                                                params);
        return binding;
    }


    public static MethodBinding createConstantMethodBinding(String outcome) {
        return new ConstantMethodBinding(outcome);
    }


    /**
     * This method will return a <code>SessionMap</code> for the current
     * <code>FacesContext</code>.  If the <code>FacesContext</code> argument
     * is null, then one is determined by <code>FacesContext.getCurrentInstance()</code>.
     * The <code>SessionMap</code> will be created if it is null.
     *
     * @param context the FacesContext
     * @return Map The <code>SessionMap</code>
     */
    public static Map getSessionMap(FacesContext context) {
        if (context == null) {
            context = FacesContext.getCurrentInstance();
        }
        return context.getExternalContext().getSessionMap();
    }


    public static Converter getConverterForClass(Class converterClass) {
        if (converterClass == null) {
            return null;
        }
        try {
            ApplicationFactory aFactory =
                (ApplicationFactory) FactoryFinder.getFactory(
                    FactoryFinder.APPLICATION_FACTORY);
            Application application = aFactory.getApplication();
            return (application.createConverter(converterClass));
        } catch (Exception e) {
            return (null);
        }
    }


    public static Converter getConverterForIdentifer(String converterId) {
        if (converterId == null) {
            return null;
        }
        try {
            ApplicationFactory aFactory =
                (ApplicationFactory) FactoryFinder.getFactory(
                    FactoryFinder.APPLICATION_FACTORY);
            Application application = aFactory.getApplication();
            return (application.createConverter(converterId));
        } catch (Exception e) {
            return (null);
        }
    }

    /*
     * Determine whether String is a value binding expression or not.
     */
    public static boolean isVBExpression(String expression) {
        if (null == expression) {
            return false;
        }
        int start = 0;
        //check to see if attribute has an expression
        if (((start = expression.indexOf("#{")) != -1) &&
            (start < expression.indexOf('}'))) {
            return true;
        }
        return false;
    }


    /*
     * Determine whether String is a mixed value binding expression or not.
     */
    public static boolean isMixedVBExpression(String expression) {
        if (null == expression) {
            return false;
        }
        // if it doesn't start and end with delimiters
        if (!(expression.startsWith("#{") && expression.endsWith("}"))) {
            // see if it has some inside.
            return isVBExpression(expression);
        }
        return false;
    }


    public static StateManager getStateManager(FacesContext context)
        throws FacesException {
        return (context.getApplication().getStateManager());
    }


    public static ViewHandler getViewHandler(FacesContext context)
        throws FacesException {
        // Get Application instance
        Application application = context.getApplication();
        assert (application != null);

        // Get the ViewHandler
        ViewHandler viewHandler = application.getViewHandler();
        assert (viewHandler != null);

        return viewHandler;
    }


    public static ResponseStateManager getResponseStateManager(FacesContext context, String renderKitId)
        throws FacesException {

        assert (null != renderKitId);
        assert (null != context);

        RenderKitFactory renderKitFactory = (RenderKitFactory)
            FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        assert (null != renderKitFactory);

        RenderKit renderKit = renderKitFactory.getRenderKit(context, renderKitId);
        if ( renderKit == null) {
            if ( logger.isLoggable(Level.SEVERE)) {
                logger.log(Level.SEVERE, 
                        "Renderkit could not loaded for renderKitId " 
                        + renderKitId);
            }
        }
        assert (null != renderKit);

        return renderKit.getResponseStateManager();

    }

    public static RenderKit getCurrentRenderKit(FacesContext context) {
	RenderKitFactory renderKitFactory = (RenderKitFactory)
	    FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
	RenderKit renderKit = 
	    renderKitFactory.getRenderKit(context, 
				      context.getViewRoot().getRenderKitId());
	return renderKit;
    }


    public static boolean componentIsDisabledOnReadonly(UIComponent component) {
        Object disabledOrReadonly = null;
        boolean result = false;
        if (null !=
            (disabledOrReadonly = component.getAttributes().get("disabled"))) {
            if (disabledOrReadonly instanceof String) {
                result = ((String) disabledOrReadonly).equalsIgnoreCase("true");
            } else {
                result = disabledOrReadonly.equals(Boolean.TRUE);
            }
        }
        if ((result == false) &&
            null !=
            (disabledOrReadonly = component.getAttributes().get("readonly"))) {
            if (disabledOrReadonly instanceof String) {
                result = ((String) disabledOrReadonly).equalsIgnoreCase("true");
            } else {
                result = disabledOrReadonly.equals(Boolean.TRUE);
            }
        }

        return result;
    }


    public static Object createInstance(String className) {
        return createInstance(className, null, null);
    }


    public static Object createInstance(String className,
                                        Class rootType,
                                        Object root) {
        Class clazz = null;
        Object returnObject = null;
        if (className != null) {
            try {
                clazz = Util.loadClass(className, returnObject);
                if (clazz != null) { 
                    // Look for an adapter constructor if we've got
                    // an object to adapt
                    if ((rootType != null) && (root != null)) {
                        try {
                            Class[] parameterTypes = new Class[]{rootType};
                            Constructor construct =
                                clazz.getConstructor(parameterTypes);
                            Object[] parameters = new Object[]{root};
                            returnObject = construct.newInstance(parameters);
                        } catch (NoSuchMethodException nsme) {
                            // OK - there's no adapter constructor
                        }
                    }

                    if (returnObject == null) {
                        returnObject = clazz.newInstance();
                    }
                }
            } catch (Exception e) {
                Object[] params = new Object[1];
                params[0] = className;                
                String msg = Util.getExceptionMessageString(
                    Util.CANT_INSTANTIATE_CLASS_ERROR_MESSAGE_ID, params);
                if ( logger.isLoggable(Level.SEVERE)) {
                    logger.log(Level.SEVERE, msg, e);
                }
            }
        }
        return returnObject;
    }

    // W3C XML specification refers to IETF RFC 1766 for language code
    // structure, therefore the value for the xml:lang attribute should
    // be in the form of language or language-country or
    // language-country-variant.

    public static Locale getLocaleFromString(String localeStr)
        throws IllegalArgumentException {
        // length must be at least 2.
        if (null == localeStr || localeStr.length() < 2) {
            throw new IllegalArgumentException("Illegal locale String: " +
                                               localeStr);
        }

        Locale result = null;
        String
            lang = null,
            country = null,
            variant = null;
        char[] seps = {
            '-',
            '_'
        };
        int
            i = 0,
            j = 0;

        // to have a language, the length must be >= 2
        if ((localeStr.length() >= 2) &&
            (-1 == (i = indexOfSet(localeStr, seps, 0)))) {
            // we have only Language, no country or variant
            if (2 != localeStr.length()) {
                throw new
                    IllegalArgumentException("Illegal locale String: " +
                                             localeStr);
            }
            lang = localeStr.toLowerCase();
        }

        // we have a separator, it must be either '-' or '_'
        if (-1 != i) {
            lang = localeStr.substring(0, i);
            // look for the country sep.
            // to have a country, the length must be >= 5
            if ((localeStr.length() >= 5) &&
                (-1 == (j = indexOfSet(localeStr, seps, i + 1)))) {
                // no further separators, length must be 5
                if (5 != localeStr.length()) {
                    throw new
                        IllegalArgumentException("Illegal locale String: " +
                                                 localeStr);
                }
                country = localeStr.substring(i + 1);
            }
            if (-1 != j) {
                country = localeStr.substring(i + 1, j);
                // if we have enough separators for language, locale,
                // and variant, the length must be >= 8.
                if (localeStr.length() >= 8) {
                    variant = localeStr.substring(j + 1);
                } else {
                    throw new
                        IllegalArgumentException("Illegal locale String: " +
                                                 localeStr);
                }
            }
        }
        if (null != variant && null != country && null != lang) {
            result = new Locale(lang, country, variant);
        } else if (null != lang && null != country) {
            result = new Locale(lang, country);
        } else if (null != lang) {
            result = new Locale(lang, "");
        }
        return result;
    }


    /**
     * @return starting at <code>fromIndex</code>, the index of the
     *         first occurrence of any substring from <code>set</code> in
     *         <code>toSearch</code>, or -1 if no such match is found
     */

    public static int indexOfSet(String str, char[] set,
                                 int fromIndex) {
        int result = -1;
        char[] toSearch = str.toCharArray();
        for (int i = fromIndex, len = toSearch.length; i < len; i++) {
            for (int j = 0, innerLen = set.length; j < innerLen; j++) {
                if (toSearch[i] == set[j]) {
                    result = i;
                    break;
                }
            }
            if (-1 != result) {
                break;
            }
        }
        return result;
    }


    public static String stripBracketsIfNecessary(String expression)
        throws ReferenceSyntaxException {
        assert (null != expression);
        int len = 0;
        // look for invalid expressions
        if ('#' == expression.charAt(0)) {
            if ('{' != expression.charAt(1)) {
                throw new ReferenceSyntaxException(Util.getExceptionMessageString(
                    Util.INVALID_EXPRESSION_ID,
                    new Object[]{expression}));
            }
            if ('}' != expression.charAt((len = expression.length()) - 1)) {
                throw new ReferenceSyntaxException(Util.getExceptionMessageString(
                    Util.INVALID_EXPRESSION_ID,
                    new Object[]{expression}));
            }
            expression = expression.substring(2, len - 1);
        }
        return expression;
    }
    
    //
    // General Methods
    //


    public static void parameterNonNull(Object param) throws FacesException {
        if (null == param) {
            throw new FacesException(
                getExceptionMessageString(NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
    }


    public static void parameterNonEmpty(String param) throws FacesException {
        if (null == param || 0 == param.length()) {
            throw new FacesException(getExceptionMessageString(EMPTY_PARAMETER_ID));
        }
    }

    /**
     * <p>This method is used by the ManagedBeanFactory to ensure that
     * properties set by an expression point to an object with an
     * accepted lifespan.</p>
     *
     * <p>get the scope of the expression. Return <code>null</code> if
     * it isn't scoped</p> 
     *
     * <p>For example, the expression:
     * <code>sessionScope.TestBean.one</code> should return "session" 
     * as the scope.</p>
     *
     * @param ValueExpression the expression
     *
     * @param outString an allocated String Array into which we put the
     * first segment.
     *
     * @return the scope of the expression
     */
    public static String getScope(String valueBinding,
				  String [] outString) throws ReferenceSyntaxException {
        if (valueBinding == null || 0 == valueBinding.length()) {
            return null;
        }
	valueBinding = stripBracketsIfNecessary(valueBinding);
	
        int segmentIndex = getFirstSegmentIndex(valueBinding);

        //examine first segment and see if it is a scope
        String identifier = valueBinding;

        if (segmentIndex > 0) {
            //get first segment designated by a "." or "["
            identifier = valueBinding.substring(0, segmentIndex);            
        }

        //check to see if the identifier is a named scope.

        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();
	
	if (null != outString) {
	    outString[0] = identifier;
	}
        if (identifier.equalsIgnoreCase(RIConstants.REQUEST_SCOPE)) {
            return RIConstants.REQUEST;
        }
        if (identifier.equalsIgnoreCase(RIConstants.SESSION_SCOPE)) {
            return RIConstants.SESSION;
        }
        if (identifier.equalsIgnoreCase(RIConstants.APPLICATION_SCOPE)) {
            return RIConstants.APPLICATION;
        }

	// handle implicit objects
        if (identifier.equalsIgnoreCase(RIConstants.INIT_PARAM_IMPLICIT_OBJ)) {
	    return RIConstants.APPLICATION;
        }	
        if (identifier.equalsIgnoreCase(RIConstants.COOKIE_IMPLICIT_OBJ)) {
	    return RIConstants.REQUEST;
        }	
        if (identifier.equalsIgnoreCase(RIConstants.FACES_CONTEXT_IMPLICIT_OBJ)) {
	    return RIConstants.REQUEST;
        }	
        if (identifier.equalsIgnoreCase(RIConstants.HEADER_IMPLICIT_OBJ)) {
	    return RIConstants.REQUEST;
        }	
        if (identifier.equalsIgnoreCase(RIConstants.HEADER_VALUES_IMPLICIT_OBJ)) {
	    return RIConstants.REQUEST;
        }
        if (identifier.equalsIgnoreCase(RIConstants.PARAM_IMPLICIT_OBJ)) {
	    return RIConstants.REQUEST;
        }
        if (identifier.equalsIgnoreCase(RIConstants.PARAM_VALUES_IMPLICIT_OBJ)) {
	    return RIConstants.REQUEST;
        }
        if (identifier.equalsIgnoreCase(RIConstants.VIEW_IMPLICIT_OBJ)) {
	    return RIConstants.REQUEST;
        }

        //No scope was provided in the expression so check for the 
        //expression in all of the scopes. The expression is the first 
        //segment.

        if (ec.getRequestMap().get(identifier) != null) {
            return RIConstants.REQUEST;
        }
        if (Util.getSessionMap(context).get(identifier) != null) {
            return RIConstants.SESSION;
        }
        if (ec.getApplicationMap().get(identifier) != null) {
            return RIConstants.APPLICATION;
        }

        //not present in any scope
        return null;
    }

    /**
     * @param expressionString the expression string, with delimiters
     * intact.
     *
     * @return a List of expressions from the expressionString
     */

    public static List getExpressionsFromString(String expressionString) throws ReferenceSyntaxException {
	if (null == expressionString) {
	    return Collections.EMPTY_LIST;
	}
	List result = new ArrayList();
	int i, j, len = expressionString.length(), cur = 0;
	while (cur < len &&
	       -1 != (i = expressionString.indexOf("#{", cur))) {
	    if (-1 == (j = expressionString.indexOf("}", i + 2))) {
		throw new ReferenceSyntaxException(Util.getExceptionMessageString(Util.INVALID_EXPRESSION_ID, new Object[]{expressionString}));
	    }
	    cur = j + 1;
	    result.add(expressionString.substring(i, cur));
	}
	return result;
    }

    /**
     * <p/>
     * The the first segment of a String tokenized by a "." or "["
     *
     * @return index of the first occurrence of . or [
     */
    private static int getFirstSegmentIndex(String valueBinding) {
        int segmentIndex = valueBinding.indexOf('.');
        int bracketIndex = valueBinding.indexOf('[');

        //there is no "." in the valueBinding so take the bracket value
        if (segmentIndex < 0) {
            segmentIndex = bracketIndex;
        } else {
            //if there is a bracket proceed
            if (bracketIndex > 0) {
                //if the bracket index is before the "." then
                //get the bracket index
                if (segmentIndex > bracketIndex) {
                    segmentIndex = bracketIndex;
                }
            }
        }
        return segmentIndex;
    }

    /**
     * <p>Leverage the Throwable.getStackTrace() method to produce a
     * String version of the stack trace, with a "\n" before each
     * line.</p>
     *
     * @return the String representation ofthe stack trace obtained by
     * calling getStackTrace() on the passed in exception.  If null is
     * passed in, we return the empty String.
     */ 

    public static String getStackTraceString(Throwable e) {
	if (null == e) {
	    return "";
	}
	
	StackTraceElement[] stacks = e.getStackTrace();
	StringBuffer sb = new StringBuffer();
	for (int i = 0; i < stacks.length; i++) {
	    sb.append(stacks[i].toString() + "\n");
	}
	return sb.toString();
    }

    /**
     * <p>PRECONDITION: argument <code>response</code> is non-null and
     * has a method called <code>getContentType</code> that takes no
     * arguments and returns a String, with no side-effects.</p>
     *
     * <p>This method allows us to get the contentType in both the
     * servlet and portlet cases, without introducing a compile-time
     * dependency on the portlet api.</p>
     *
     */ 

    public static String getContentTypeFromResponse(Object response) {
	String result = null;
	if (null != response) {
	    Method method = null;

	    try {
		method = response.getClass().getMethod("getContentType", null);
		if (null != method) {
		    Object obj = method.invoke(response, null);
		    if (null != obj) {
			result = obj.toString();
		    }
		}
	    }
	    catch (NoSuchMethodException nsme) {
		throw new FacesException(nsme);
	    }
	    catch (IllegalAccessException iae) {
		throw new FacesException(iae);
	    }
	    catch (IllegalArgumentException iare) {
		throw new FacesException(iare);
	    }
	    catch (InvocationTargetException ite) {
		throw new FacesException(ite);
	    }
	    catch (SecurityException e) {
		throw new FacesException(e);
	    }
	}
	return result;
    }		

    public static boolean prefixViewTraversal(FacesContext context,
					      UIComponent root,
					      TreeTraversalCallback action) throws FacesException {
	boolean keepGoing = false;
	if (keepGoing = action.takeActionOnNode(context, root)) {
	    Iterator kids = root.getFacetsAndChildren();
	    while (kids.hasNext() && keepGoing) {
		keepGoing = prefixViewTraversal(context, 
						(UIComponent) kids.next(), 
						action);
	    }
	}
	return keepGoing;
    }

    public static interface TreeTraversalCallback {
	public boolean takeActionOnNode(FacesContext context, 
					UIComponent curNode) throws FacesException;
    }


} // end of class Util
