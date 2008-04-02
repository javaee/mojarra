/*
 * $Id: Util.java,v 1.193 2006/05/26 01:10:37 rlubke Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

// Util.java

package com.sun.faces.util;

import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.StateManager;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.convert.Converter;
import javax.faces.el.ReferenceSyntaxException;
import javax.faces.el.ValueBinding;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import java.beans.FeatureDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Pattern;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.faces.RIConstants;
import com.sun.faces.renderkit.RenderKitImpl;
import com.sun.faces.spi.ManagedBeanFactory.Scope;

/**
 * <B>Util</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: Util.java,v 1.193 2006/05/26 01:10:37 rlubke Exp $
 */

public class Util {

    //
    // Private/Protected Constants
    //
    public static final String FACES_LOGGER = "javax.enterprise.resource.webcontainer.jsf";
    
    public static final String FACES_LOG_STRINGS = 
            "com.sun.faces.LogStrings";        
    
    // Log instance for this class
    private static final Logger LOGGER = getLogger(FACES_LOGGER);

    // README - make sure to add the message identifier constant
    // (ex: Util.CONVERSION_ERROR_MESSAGE_ID) and the number of substitution
    // parameters to test/com/sun/faces/util/TestUtil_messages (see comment there).
 
    // Loggers
    public static final String RENDERKIT_LOGGER = ".renderkit";
    public static final String TAGLIB_LOGGER = ".taglib";
    public static final String APPLICATION_LOGGER = ".application";
    public static final String CONTEXT_LOGGER = ".context";
    public static final String CONFIG_LOGGER = ".config";
    public static final String LIFECYCLE_LOGGER = ".lifecycle";        

    /**
     * Flag that, when true, enables special behavior in the RI to enable
     * unit testing.
     */
    private static boolean unitTestModeEnabled = false;

    /**
     * Flag that enables/disables the core TLV.
     */
    private static boolean coreTLVEnabled = true;

    /**
     * Flag that enables/disables the html TLV.
     */
    private static boolean htmlTLVEnabled = true;
    
    private static final Map<String,Pattern> patternCache = 
          new LRUMap<String,Pattern>(15);
    
     /**
     * <p>The <code>request</code> scoped attribute to store the
     * {@link javax.faces.webapp.FacesServlet} path of the original
     * request.</p>
     */
    private static final String INVOCATION_PATH =
        RIConstants.FACES_PREFIX + "INVOCATION_PATH";

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

    public static void setUnitTestModeEnabled(boolean enabled) {
        unitTestModeEnabled = enabled;
    }

    public static boolean isUnitTestModeEnabled() {
        return unitTestModeEnabled;
    }

    public static void setCoreTLVActive(boolean active) {
        coreTLVEnabled = active;
    }

    public static boolean isCoreTLVActive() {
        return coreTLVEnabled;
    }

    public static void setHtmlTLVActive(boolean active) {
        htmlTLVEnabled = active;
    }

    public static boolean isHtmlTLVActive() {
        return htmlTLVEnabled;
    }


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
        Map<String,Object> applicationMap = facesContext.getExternalContext()
            .getApplicationMap();
        Boolean result = null;
        String className = "javax.servlet.jsp.jstl.fmt.LocalizationContext";
        Object[] params = {className};

        // Have we checked before?
        if (null != (result = (Boolean)
            applicationMap.get(RIConstants.HAS_REQUIRED_CLASSES_ATTR))) {
            // yes, and the check failed.
            if (Boolean.FALSE.equals(result)) {
                throw new
                    FacesException(
                        MessageUtils.getExceptionMessageString(
                            MessageUtils.MISSING_CLASS_ERROR_MESSAGE_ID, params));
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
                MessageUtils.getExceptionMessageString(MessageUtils.MISSING_CLASS_ERROR_MESSAGE_ID,
                                         params),
                e);
        }
        applicationMap.put(RIConstants.HAS_REQUIRED_CLASSES_ATTR, Boolean.TRUE);
    }
   
    
    /**
     * <p>If the FacesContext has a UIViewRoot, and this UIViewRoot has a Locale,
     * return it.  Otherwise return Locale.getDefault().
     */
    
    public static Locale getLocaleFromContextOrSystem(FacesContext context) {
        Locale result, temp = Locale.getDefault();
        UIViewRoot root = null;
        result = temp;
        if (null != context) {
            if (null != (root = context.getViewRoot())) {
                if (null == (result = root.getLocale())) {
                    result = temp;
                }
            }
        }
        return result;
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

    @SuppressWarnings("Deprecation")
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


    /**
     * This method will return a <code>SessionMap</code> for the current
     * <code>FacesContext</code>.  If the <code>FacesContext</code> argument
     * is null, then one is determined by <code>FacesContext.getCurrentInstance()</code>.
     * The <code>SessionMap</code> will be created if it is null.
     *
     * @param context the FacesContext
     * @return Map The <code>SessionMap</code>
     */
    public static Map<String,Object> getSessionMap(FacesContext context) {
        if (context == null) {
            context = FacesContext.getCurrentInstance();
        }
        return context.getExternalContext().getSessionMap();
    }


    public static Converter getConverterForClass(Class converterClass,
                                                 FacesContext context) {
        if (converterClass == null) {
            return null;
        }
        try {            
            Application application = context.getApplication();
            return (application.createConverter(converterClass));
        } catch (Exception e) {
            return (null);
        }
    }


    public static Converter getConverterForIdentifer(String converterId,
                                                     FacesContext context) {
        if (converterId == null) {
            return null;
        }
        try {            
            Application application = context.getApplication();
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


    public static boolean componentIsDisabled(UIComponent component) {
        Object disabled = null;
        boolean result = false;
        if (null !=
            (disabled = component.getAttributes().get("disabled"))) {
            if (disabled instanceof String) {
                result = ((String) disabled).equalsIgnoreCase("true");
            } else {
                result = disabled.equals(Boolean.TRUE);
            }
        }

        return result;
    }


    public static boolean componentIsDisabledOrReadonly(UIComponent component) {
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
                            if (LOGGER.isLoggable(Level.FINE)) {                                
                                LOGGER.log(Level.FINE,
                                           "jsf.util.no.adapter.ctor.available",
                                           new Object[] {
                                                 clazz.getName(),
                                                 rootType.getName()
                                           });
                            }                           
                        }
                    }
                    if (returnObject == null) {
                        returnObject = clazz.newInstance();
                    }
                }
            } catch (Exception e) {
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    Object[] params = new Object[1];
                    params[0] = className;
                    String msg = MessageUtils.getExceptionMessageString(
                          MessageUtils.CANT_INSTANTIATE_CLASS_ERROR_MESSAGE_ID,
                          params);
                    LOGGER.log(Level.SEVERE, msg, e);
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

    @SuppressWarnings("Deprecation")
    public static String stripBracketsIfNecessary(String expression)
        throws ReferenceSyntaxException {
        assert (null != expression);
        int len = 0;
        // look for invalid expressions
        if ('#' == expression.charAt(0)) {
            if ('{' != expression.charAt(1)) {
                throw new ReferenceSyntaxException(MessageUtils.getExceptionMessageString(
                    MessageUtils.INVALID_EXPRESSION_ID,
                    new Object[]{expression}));
            }
            if ('}' != expression.charAt((len = expression.length()) - 1)) {
                throw new ReferenceSyntaxException(MessageUtils.getExceptionMessageString(
                    MessageUtils.INVALID_EXPRESSION_ID,
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
                MessageUtils.getExceptionMessageString(MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "param"));
        }
    }


    public static void parameterNonEmpty(String param) throws FacesException {
        if (null == param || 0 == param.length()) {
            throw new FacesException(MessageUtils.getExceptionMessageString(MessageUtils.EMPTY_PARAMETER_ID));
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
    @SuppressWarnings("Deprecation")
    public static Scope getScope(String valueBinding,
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
            return Scope.REQUEST;
        }
        if (identifier.equalsIgnoreCase(RIConstants.SESSION_SCOPE)) {
            return Scope.SESSION;
        }
        if (identifier.equalsIgnoreCase(RIConstants.APPLICATION_SCOPE)) {
            return Scope.APPLICATION;
        }

	// handle implicit objects
        if (identifier.equalsIgnoreCase(RIConstants.INIT_PARAM_IMPLICIT_OBJ)) {
	    return Scope.APPLICATION;
        }	
        if (identifier.equalsIgnoreCase(RIConstants.COOKIE_IMPLICIT_OBJ)) {
	    return Scope.REQUEST;
        }	
        if (identifier.equalsIgnoreCase(RIConstants.FACES_CONTEXT_IMPLICIT_OBJ)) {
	    return Scope.REQUEST;
        }	
        if (identifier.equalsIgnoreCase(RIConstants.HEADER_IMPLICIT_OBJ)) {
	    return Scope.REQUEST;
        }	
        if (identifier.equalsIgnoreCase(RIConstants.HEADER_VALUES_IMPLICIT_OBJ)) {
	    return Scope.REQUEST;
        }
        if (identifier.equalsIgnoreCase(RIConstants.PARAM_IMPLICIT_OBJ)) {
	    return Scope.REQUEST;
        }
        if (identifier.equalsIgnoreCase(RIConstants.PARAM_VALUES_IMPLICIT_OBJ)) {
	    return Scope.REQUEST;
        }
        if (identifier.equalsIgnoreCase(RIConstants.VIEW_IMPLICIT_OBJ)) {
	    return Scope.REQUEST;
        }

        //No scope was provided in the expression so check for the 
        //expression in all of the scopes. The expression is the first 
        //segment.

        if (ec.getRequestMap().get(identifier) != null) {
            return Scope.REQUEST;
        }
        if (Util.getSessionMap(context).get(identifier) != null) {
            return Scope.SESSION;
        }
        if (ec.getApplicationMap().get(identifier) != null) {
            return Scope.APPLICATION;
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
    @SuppressWarnings("Deprecation")
    public static List getExpressionsFromString(String expressionString) throws ReferenceSyntaxException {
	if (null == expressionString) {
	    return Collections.EMPTY_LIST;
	}
	List<String> result = new ArrayList<String>();
	int i, j, len = expressionString.length(), cur = 0;
	while (cur < len &&
	       -1 != (i = expressionString.indexOf("#{", cur))) {
	    if (-1 == (j = expressionString.indexOf("}", i + 2))) {
		throw new ReferenceSyntaxException(MessageUtils.getExceptionMessageString(MessageUtils.INVALID_EXPRESSION_ID, new Object[]{expressionString}));
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
		method = response.getClass().getMethod("getContentType", 
                                               RIConstants.EMPTY_CLASS_ARGS);
		if (null != method) {
		    Object obj = method.invoke(response, RIConstants.EMPTY_METH_ARGS);
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
	    Iterator<UIComponent> kids = root.getFacetsAndChildren();
	    while (kids.hasNext() && keepGoing) {
		keepGoing = prefixViewTraversal(context, 
						kids.next(), 
						action);
	    }
	}
	return keepGoing;
    }

    public static interface TreeTraversalCallback {
	public boolean takeActionOnNode(FacesContext context, 
					UIComponent curNode) throws FacesException;
    }
    
    public static FeatureDescriptor getFeatureDescriptor(String name, String
        displayName, String desc, boolean expert, boolean hidden, 
        boolean preferred, Object type, Boolean designTime) {
            
        FeatureDescriptor fd = new FeatureDescriptor();
        fd.setName(name);
        fd.setDisplayName(displayName);
        fd.setShortDescription(desc);
        fd.setExpert(expert);
        fd.setHidden(hidden);
        fd.setPreferred(preferred);
        fd.setValue(ELResolver.TYPE, type);
        fd.setValue(ELResolver.RESOLVABLE_AT_DESIGN_TIME, designTime);
        return fd;
    }

    /** <p>Checks for the existence of a method specified by the "methodName"
     *  argument, on the "instance" argument.</p>
     */
    public static boolean hasDeclaredMethod(Object instance, String methodName) {
        boolean result = false;
        // Look for the presence of the method by method name.
        Class c = instance.getClass();
        Method[] methods = c.getDeclaredMethods();
        
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].getName().equals(methodName)) {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * <p>A slightly more efficient version of 
     * <code>String.split()</code> which caches
     * the <code>Pattern</code>s in an LRUMap instead of
     * creating a new <code>Pattern</code> on each
     * invocation.</p>
     * @param toSplit the string to split
     * @param regex the regex used for splitting
     * @return the result of <code>Pattern.spit(String, int)</code>
     */
    public static String[] split(String toSplit, String regex) {
        Pattern pattern = patternCache.get(regex);
        if (pattern == null) {
            synchronized(patternCache) {
                pattern = patternCache.get(regex);
                if (pattern == null) {
                    pattern = Pattern.compile(regex);
                    patternCache.put(regex, pattern);
                } 
            }
        }
        return pattern.split(toSplit, 0);        
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
            String message = MessageUtils.getExceptionMessageString
                  (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "context");
            throw new NullPointerException(message);
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
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE,
                               "jsf.faces_servlet_mapping_cannot_be_determined_error",
                               new Object[]{servletPath});
                }
            }
        }

        if (mapping != null) {
            extContext.getRequestMap().put(INVOCATION_PATH, mapping);
        }
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE,
                       "URL pattern of the FacesServlet executing the current request "
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
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "servletPath " + servletPath);
            LOGGER.log(Level.FINE, "pathInfo " + pathInfo);
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


} // end of class Util
