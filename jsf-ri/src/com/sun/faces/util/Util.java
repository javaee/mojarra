/*
 * $Id: Util.java,v 1.143.6.1.2.10 2007/04/27 21:27:49 ofung Exp $
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

// Util.java

package com.sun.faces.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

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
import javax.faces.el.ValueBinding;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.model.SelectItem;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.ResponseStateManager;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sun.faces.RIConstants;
import com.sun.faces.el.impl.ExpressionEvaluator;
import com.sun.faces.el.impl.ExpressionEvaluatorImpl;
import com.sun.faces.renderkit.RenderKitImpl;

/**
 * <B>Util</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: Util.java,v 1.143.6.1.2.10 2007/04/27 21:27:49 ofung Exp $
 */

public class Util extends Object {

    // Log instance for this class
    protected static Log log = LogFactory.getLog(Util.class);

    /**
     * The parser implementation for handling Faces RE expressions.
     */
    private static final ExpressionEvaluator FACES_EXPRESSION_EVALUATOR =
        new ExpressionEvaluatorImpl(RIConstants.FACES_RE_PARSER);
    
    private static final String RENDER_KIT_IMPL_REQ = 
          RIConstants.FACES_PREFIX + "renderKitImplForRequest";
    
     /**
     * <p>The <code>request</code> scoped attribute to store the
     * {@link javax.faces.webapp.FacesServlet} path of the original
     * request.</p>
     */
    private static final String INVOCATION_PATH =
        RIConstants.FACES_PREFIX + "INVOCATION_PATH";
    
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
    
    public static final String COMMAND_LINK_NO_FORM_MESSAGE_ID =
        "com.sun.faces.COMMAND_LINK_NO_FORM_MESSAGE";


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
     *
     * @see renderPassthruAttributes
     */
    private static String passthruAttributes[] = {
        "accept",
        "accesskey",
        "alt",
        "bgcolor",
        "border",
        "cellpadding",
        "cellspacing",
        "charset",
        "cols",
        "coords",
        "dir",
        "enctype",
        "frame",
        "height",
        "hreflang",
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
        "rel",
        "rev",
        "rows",
        "rules",
        "shape",
        "size",
        "style",
        "summary",
        "tabindex",
        "target",
        "title",
        "usemap",
        "width"
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


    /**
     * Called by the RI to get the IMPL_MESSAGES MessageFactory
     * instance and get a message on it.
     */

    public static synchronized String getExceptionMessageString(String messageId,
                                                          Object params[]) {
        String result = null;

        FacesMessage message = MessageFactory.getMessage(messageId, params);
        if (null != message) {
            result = message.getSummary();
        }


        if (null == result) {
            result = "null MessageFactory";
        }
        return result;
    }


    public static synchronized String getExceptionMessageString(String messageId) {
        return Util.getExceptionMessageString(messageId, null);
    }

    public static synchronized FacesMessage getExceptionMessage(String messageId,
								      Object params[]) {
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
        Util.doAssert(null != renderKitFactory);

        lifecycleFactory = (LifecycleFactory)
            FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
        Util.doAssert(null != lifecycleFactory);

        facesContextFactory = (FacesContextFactory)
            FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
        Util.doAssert(null != facesContextFactory);

        applicationFactory = (ApplicationFactory)
            FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        Util.doAssert(null != applicationFactory);

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
     * <p>Return an Iterator over {@link SelectItemWrapper} instances representing the
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
                    SelectItem items[] = (SelectItem[]) value;
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
        String bundleName = null, bundleAttr = "bundle";

        Util.parameterNonNull(context);
        Util.parameterNonNull(component);

        // verify our component has the proper attributes for bundle.
        if (null !=
            (bundleName = (String) component.getAttributes().get(bundleAttr))) {
            // verify there is a Locale for this localizationContext
            javax.servlet.jsp.jstl.fmt.LocalizationContext locCtx = null;
            if (null != (locCtx =
                (javax.servlet.jsp.jstl.fmt.LocalizationContext)
                (Util.getValueBinding(bundleName)).getValue(context))) {
                result = locCtx.getLocale();
                Util.doAssert(null != result);
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
        int i = 0;
        Object attrVal;
        String empty = "";
        for (i = 0; i < passthruAttributes.length; i++) {
            if (null != (attrVal = attrs.get(passthruAttributes[i]))
                &&
                !empty.equals(attrVal)) {
                result = true;
                break;
            }
        }
        if (!result) {
            for (i = 0; i < booleanPassthruAttributes.length; i++) {
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
        Util.doAssert(null != writer);
        Util.doAssert(null != component);

        int i = 0, len = booleanPassthruAttributes.length, j,
            jLen = (null != excludes ? excludes.length : 0);
        Object value = null;
        boolean result;
        boolean skip = false;
        for (i = 0; i < len; i++) {
            skip = false;
            if (null != excludes) {
                for (j = 0; j < jLen; j++) {
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
                    result = (new Boolean((String) value)).booleanValue();
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


    public static void renderPassThruAttributes(ResponseWriter writer,
                                                UIComponent component)
        throws IOException {
        renderPassThruAttributes(writer, component, null);
    }


    /**
     * Render any "passthru" attributes, where we simply just output the
     * raw name and value of the attribute.  This method is aware of the
     * set of HTML4 attributes that fall into this bucket.  Examples are
     * all the javascript attributes, alt, rows, cols, etc.  <P>
     *
     * @see passthruAttributes
     */
    public static void renderPassThruAttributes(ResponseWriter writer,
                                                UIComponent component,
                                                String[] excludes)
        throws IOException {
        Util.doAssert(null != writer);
        Util.doAssert(null != component);

        int i = 0, len = passthruAttributes.length, j,
            jLen = (null != excludes ? excludes.length : 0);
        Object value = null;
        boolean skip = false;
        for (i = 0; i < len; i++) {
            skip = false;
            if (null != excludes) {
                for (j = 0; j < jLen; j++) {
                    if (null != excludes[j] &&
                        excludes[j].equals(passthruAttributes[i])) {
                        skip = true;
                        break;
                    }
                }
            }
            if (skip) {
                continue;
            }

            value = component.getAttributes().get(passthruAttributes[i]);
            if (value != null && shouldRenderAttribute(value)) {
                if (!(value instanceof String)) {
                    value = value.toString();
                }
                //PENDING(rogerk) will revisit "null" param soon..
                writer.writeAttribute(passthruAttributes[i], value,
                                      passthruAttributes[i]);
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


    public static Object evaluateVBExpression(String expression) {
        if (expression == null || (!isVBExpression(expression))) {
            return expression;
        }
        FacesContext context = FacesContext.getCurrentInstance();
        Object result =
            context.getApplication().createValueBinding(expression).getValue(
                context);
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


    public static Converter getConverterForClass(Class converterClass,
                                                 FacesContext facesContext) {
        if (converterClass == null) {
            return null;
        }
        try {
            Application application = facesContext.getApplication();
            return (application.createConverter(converterClass));
        } catch (Exception e) {
            return (null);
        }
    }


    public static Converter getConverterForIdentifer(String converterId,
                                                     FacesContext facesContext) {
        if (converterId == null) {
            return null;
        }
        try {
            Application application = facesContext.getApplication();
            return (application.createConverter(converterId));
        } catch (Exception e) {
            return (null);
        }
    }


    /**
     * <p>Return the single {@link ExpressionEvaluator} instance.</p>
     *
     * @return an ExpressionEvaluator
     */
    public static ExpressionEvaluator getExpressionEvaluator() {
        return FACES_EXPRESSION_EVALUATOR;
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
        Util.doAssert(application != null);

        // Get the ViewHandler
        ViewHandler viewHandler = application.getViewHandler();
        Util.doAssert(viewHandler != null);

        return viewHandler;
    }


    public static ResponseStateManager getResponseStateManager(FacesContext context,
                                                               String renderKitId)
        throws FacesException {
        RenderKit renderKit = context.getRenderKit();

        if (renderKit == null) {
            // check request scope for a RenderKitFactory implementation
            Map requestMap = context.getExternalContext().getRequestMap();
            RenderKitFactory factory = (RenderKitFactory)
                  requestMap.get(RENDER_KIT_IMPL_REQ);
            if (factory != null) {                
                renderKit = factory.getRenderKit(context, renderKitId);
            } else {                
                factory = (RenderKitFactory) 
                      FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
                if (factory == null) {
                    throw new IllegalStateException();
                } else {
                    requestMap.put(RENDER_KIT_IMPL_REQ, factory);
                }
                renderKit = factory.getRenderKit(context, renderKitId);
            }
        }
        
        return renderKit.getResponseStateManager();
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
                if (log.isErrorEnabled()) {
                    log.error(msg + ":" + className + ":exception:" +
                              e.getMessage());
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
        Util.doAssert(null != expression);
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

    private static boolean assertEnabled = true;


    public static void doAssert(boolean cond) throws FacesException {
        if (assertEnabled && !cond) {
            throw new FacesException(getExceptionMessageString(ASSERTION_FAILED_ID));
        }
    }


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
     * @param valueBinding the expression
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
     * @result a List of expressions from the expressionString
     *
     * @param expressionString the expression string, with delimiters
     * intact.
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
        int segmentIndex = valueBinding.indexOf(".");
        int bracketIndex = valueBinding.indexOf("[");

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
     * <p>Leverage the Throwable.printStackTrace() method to produce a
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

	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	PrintStream ps = new PrintStream(baos);
	e.printStackTrace(ps);
	return baos.toString();
    }



    /**
     * <p>Returns the URL pattern of the
     * {@link javax.faces.webapp.FacesServlet} that
     * is executing the current request.  If there are multiple
     * URL patterns, the value returned by
     * <code>HttpServletRequest.getServletPath()</code> and
     * <code>HttpServletRequest.getPathInfo()</code> is
     * used to determine which mapping to return.</p>
     * If no mapping can be determined, it most likely means
     * that this particular request wasn't dispatched through
     * the {@link javax.faces.webapp.FacesServlet}.
     *
     * @param context the {@link FacesContext} of the current request
     *
     * @return the URL pattern of the {@link javax.faces.webapp.FacesServlet}
     *         or <code>null</code> if no mapping can be determined
     *
     * @throws NullPointerException if <code>context</code> is null
     */
    public static String getFacesMapping(FacesContext context) {

        if (context == null) {
            throw new NullPointerException("context must not be null");
        }

        // Check for a previously stored mapping   
        ExternalContext extContext = context.getExternalContext();
        String mapping =
              (String) extContext.getRequestMap().get(INVOCATION_PATH);

        if (mapping == null) {

            Object request = extContext.getRequest();
            String servletPath = null;
            String pathInfo = null;

            // first check for javax.servlet.forward.servlet_path
            // and javax.servlet.forward.path_info for non-null
            // values.  if either is non-null, use this
            // information to generate determine the mapping.

            if (request instanceof HttpServletRequest) {
                servletPath = extContext.getRequestServletPath();
                pathInfo = extContext.getRequestPathInfo();
            }


            mapping = getMappingForRequest(servletPath, pathInfo);
            if (mapping == null) {
                if (log.isTraceEnabled()) {
                    log.trace("JSF Servlet mapping cannot be determined. "+servletPath);
                }
            }
        }

        if (mapping != null) {
            extContext.getRequestMap().put(INVOCATION_PATH, mapping);
        }
        if (log.isTraceEnabled()) {
            log.trace("URL pattern of the FacesServlet executing the current request " 
            		+ mapping);
        }
        return mapping;
    }

    /**
     * <p>Return the appropriate {@link javax.faces.webapp.FacesServlet} mapping
     * based on the servlet path of the current request.</p>
     *
     * @param servletPath the servlet path of the request
     * @param pathInfo    the path info of the request
     *
     * @return the appropriate mapping based on the current request
     *
     * @see HttpServletRequest#getServletPath()
     */
    private static String getMappingForRequest(String servletPath, String pathInfo) {

        if (servletPath == null) {
            return null;
        }
        if (log.isTraceEnabled()) {
            log.trace("servletPath " + servletPath);
            log.trace("pathInfo " + pathInfo);
        }
        // If the path returned by HttpServletRequest.getServletPath()
        // returns a zero-length String, then the FacesServlet has
        // been mapped to '/*'.
        if (servletPath.length() == 0) {
            return "/*";
        }

        // presence of path info means we were invoked
        // using a prefix path mapping
        if (pathInfo != null) {
            return servletPath;
        } else if (servletPath.indexOf('.') < 0) {
            // if pathInfo is null and no '.' is present, assume the
            // FacesServlet was invoked using prefix path but without
            // any pathInfo - i.e. GET /contextroot/faces or
            // GET /contextroot/faces/
            return servletPath;
        } else {
            // Servlet invoked using extension mapping
            return servletPath.substring(servletPath.lastIndexOf('.'));
        }
    }
    
    
    /**
     * <p>Returns true if the provided <code>url-mapping</code> is
     * a prefix path mapping (starts with <code>/</code>).</p>
     *
     * @param mapping a <code>url-pattern</code>
     * @return true if the mapping starts with <code>/</code>
     */
    public static boolean isPrefixMapped(String mapping) {
        return (mapping.charAt(0) == '/');
    }
    

    /**
     * <p>Replaces all occurrences of <code>-</code> with <code>$_</code>.</p>
     * 
     * @param origIdentifier the original identifer that needs to be
     *  'ECMA-ized'
     * @return an ECMA valid identifer
     */
    public static String createValidECMAIdentifier(String origIdentifier) {
		StringBuffer newIdentifier = new StringBuffer(origIdentifier);
		int currentIndex = newIdentifier.indexOf("-", 0);
		while(currentIndex != -1) {
			newIdentifier.replace(currentIndex, currentIndex+1, "$_");
			currentIndex = newIdentifier.indexOf("-", currentIndex);
		}
        return newIdentifier.toString();
    }
    
} // end of class Util
