/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2013 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
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

import com.sun.faces.RIConstants;
import com.sun.faces.config.WebConfiguration;
import com.sun.faces.io.FastStringWriter;
import com.sun.faces.scripting.groovy.GroovyHelper;

import javax.el.ELResolver;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.StateManager;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.event.AbortProcessingException;
import java.beans.FeatureDescriptor;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.enterprise.inject.spi.BeanManager;
import javax.faces.application.ProjectStage;
import javax.faces.component.UINamingContainer;
import javax.faces.render.ResponseStateManager;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.validation.SchemaFactory;

/**
 * <B>Util</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 */

public class Util {

    
    // Log instance for this class
    private static final Logger LOGGER = FacesLogger.APPLICATION.getLogger();

    // README - make sure to add the message identifier constant
    // (ex: Util.CONVERSION_ERROR_MESSAGE_ID) and the number of substitution
    // parameters to test/com/sun/faces/util/TestUtil_messages (see comment there).

    /**
     * Flag that, when true, enables special behavior in the RI to enable
     * unit testing.
     */
    private static boolean unitTestModeEnabled = false;
    
    /**
     * RegEx patterns
     */
    private static final String patternCacheKey = RIConstants.FACES_PREFIX + "patternCache";


    private Util() {
        throw new IllegalStateException();
    }

    private static Map<String,Pattern> getPatternCache(Map<String, Object> appMap) {
        Map<String,Pattern> result = 
                (Map<String,Pattern>) appMap.get(patternCacheKey);
        if (null == result) {
            result = new LRUMap<String,Pattern>(15);
            appMap.put(patternCacheKey, result);
        }
        
        return result;
    }

    private static Map<String,Pattern> getPatternCache(ServletContext sc) {
        Map<String,Pattern> result =
                (Map<String,Pattern>) sc.getAttribute(patternCacheKey);
        if (null == result) {
            result = new LRUMap<String,Pattern>(15);
            sc.setAttribute(patternCacheKey, result);
        }

        return result;
    }
    
    private static final String CDI_AVAILABLE_PER_APP_KEY = Util.class.getName() + "_CDI_AVAILABLE";
    
    public static boolean isCDIAvailable(Map<String, Object> appMap) {
        boolean result = appMap.containsKey(CDI_AVAILABLE_PER_APP_KEY);
        return result;
    }

    public static boolean isCDIAvailable(ServletContext sc) {
        boolean result = null != sc.getAttribute(CDI_AVAILABLE_PER_APP_KEY);
        return result;
    }
    
    /**
     * Get the CDI bean manager.
     * 
     * @param facesContext the Faces context to consult
     * @return the CDI bean manager.
     */
    public static BeanManager getCdiBeanManager(FacesContext facesContext) {
        BeanManager result = null;
        
        if (facesContext != null && facesContext.getAttributes().containsKey(RIConstants.CDI_BEAN_MANAGER)) {
            result = (BeanManager) facesContext.getAttributes().get(RIConstants.CDI_BEAN_MANAGER);
        } else if (facesContext != null && facesContext.getExternalContext().getApplicationMap().containsKey(RIConstants.CDI_BEAN_MANAGER)) {
            result = (BeanManager) facesContext.getExternalContext().getApplicationMap().get(RIConstants.CDI_BEAN_MANAGER);
        } else {
            try {
                InitialContext initialContext = new InitialContext();
                result = (BeanManager) initialContext.lookup("java:comp/BeanManager");
            }
            catch (NamingException ne) {
                try {
                    InitialContext initialContext = new InitialContext();
                    result = (BeanManager) initialContext.lookup("java:comp/env/BeanManager");
                }
                catch (NamingException ne2) {
                }
            }
            
            if (result == null && facesContext != null) {
                Map<String, Object> applicationMap = facesContext.getExternalContext().getApplicationMap();
                result = (BeanManager) applicationMap.get("org.jboss.weld.environment.servlet.javax.enterprise.inject.spi.BeanManager");
            }
            
            if (result != null && facesContext != null) {
                facesContext.getAttributes().put(RIConstants.CDI_BEAN_MANAGER, result);
                facesContext.getExternalContext().getApplicationMap().put(RIConstants.CDI_BEAN_MANAGER, result);
            }
        }
        
        return result;
    }
    
    public static void setCDIAvailable(ServletContext sc, Object beanManager) {
        sc.setAttribute(CDI_AVAILABLE_PER_APP_KEY, beanManager);
        }
    
    public static boolean isCdiOneOneOrGreater() {

        // The following try/catch is a hack to discover
        // if CDI 1.1 or greater is available
        boolean result = false;
        try {
            Class.forName("javax.enterprise.context.Initialized");
            result = true;
        } catch (ClassNotFoundException ignored) {
            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.log(Level.FINEST, "Dected CDI 1.0", ignored);
            }
        }
        return result;
    }

    /**
     * <p>
     * Convenience method for determining if the request associated
     * with the specified <code>FacesContext</code> is a PortletRequest
     * submitted by the JSR-301 bridge.
     * </p>
     * @param context the <code>FacesContext</code> associated with
     *  the request.
     */
    public static boolean isPortletRequest (FacesContext context) {
        return (context.getExternalContext().getRequestMap().get("javax.portlet.faces.phase") != null);
    }
    

    /**
     * <p>Factory method for creating the varius JSF listener
     *  instances that may be referenced by <code>type</code>
     *  or <code>binding</code>.</p>
     * <p>If <code>binding</code> is not <code>null</code>
     * and the evaluation result is not <code>null</code> return
     * that instance.  Otherwise try to instantiate an instances
     * based on <code>type</code>.</p>
     * 
     * @param type the <code>Listener</code> type
     * @param binding a <code>ValueExpression</code> which resolves
     *  to a <code>Listener</code> instance
     * @return a <code>Listener</code> instance based off the provided
     *  <code>type</code> and <binding>
     */
    public static Object getListenerInstance(ValueExpression type,
                                             ValueExpression binding) {

        FacesContext faces = FacesContext.getCurrentInstance();
        Object instance = null;
        if (faces == null) {
            return null;
        }
        if (binding != null) {
            instance = binding.getValue(faces.getELContext());
        }
        if (instance == null && type != null) {
            try {
                instance = ReflectionUtils.newInstance(((String) type.getValue(faces.getELContext())));
            } catch (Exception e) {
                throw new AbortProcessingException(e.getMessage(), e);
            }

            if (binding != null) {
                binding.setValue(faces.getELContext(), instance);
            }
        }

        return instance;

    }

    public static void setUnitTestModeEnabled(boolean enabled) {
        unitTestModeEnabled = enabled;
    }

    public static boolean isUnitTestModeEnabled() {
        return unitTestModeEnabled;
    }

    public static TransformerFactory createTransformerFactory() {
         ClassLoader cl = Thread.currentThread().getContextClassLoader();
         TransformerFactory factory;
         try {
             Thread.currentThread().setContextClassLoader(Util.class.getClassLoader());
             factory = TransformerFactory.newInstance();
         } finally {
             Thread.currentThread().setContextClassLoader(cl);
         }
         return factory;
     }

    public static SAXParserFactory createSAXParserFactory() {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        SAXParserFactory factory;
        try {
            Thread.currentThread().setContextClassLoader(Util.class.getClassLoader());
            factory = SAXParserFactory.newInstance();
        } finally {
            Thread.currentThread().setContextClassLoader(cl);
        }
        return factory;
    }

    public static DocumentBuilderFactory createDocumentBuilderFactory() {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        DocumentBuilderFactory factory;
        try {
            Thread.currentThread().setContextClassLoader(Util.class.getClassLoader());
            factory = DocumentBuilderFactory.newInstance();
        } finally {
            Thread.currentThread().setContextClassLoader(cl);
        }
        return factory;
    }

    public static SchemaFactory createSchemaFactory(String uri) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        SchemaFactory factory;
        try {
            Thread.currentThread().setContextClassLoader(Util.class.getClassLoader());
            factory = SchemaFactory.newInstance(uri);
        } finally {
            Thread.currentThread().setContextClassLoader(cl);
        }
        return factory;
    }


    public static Class loadClass(String name,
                                  Object fallbackClass)
        throws ClassNotFoundException {
        ClassLoader loader = Util.getCurrentLoader(fallbackClass);
        
        String[] primitiveNames = { "byte", "short", "int", "long", "float", "double", "boolean", "char" };
        Class[] primitiveClasses = { byte.class, short.class, int.class, long.class, float.class, double.class, boolean.class, char.class };
        
        for(int i=0; i<primitiveNames.length; i++) {
            if (primitiveNames[i].equals(name)) {
                return primitiveClasses[i];
            }
        }
        // Where to begin...
        // JDK 6 introduced CR 6434149 where one couldn't pass
        // in a literal for an array type ([Ljava.lang.String) and
        // get the Class representation using ClassLoader.loadClass().
        // It was recommended to use Class.forName(String, boolean, ClassLoader)
        // for all ClassLoading requests.
        // HOWEVER, when trying to eliminate the need for .groovy extensions
        // being specified in the faces-config.xml for Groovy-based artifacts,
        // by using an adapter to the GroovyScriptEngine, I found that the class
        // instance was cached somewhere, so that no matter what change I made,
        // Class.forName() always returned the same instance.  I haven't been
        // able to determine why this happens in the appserver environment
        // as the same adapter in a standalone program works as one might expect.
        // So, for now, if the classname starts with '[', then use Class.forName()
        // to avoid CR 643419 and for all other cases, use ClassLoader.loadClass().
        if (loader.getClass() == com.sun.faces.scripting.groovy.GroovyHelperImpl.MojarraGroovyClassLoader.class) {
            if (name.charAt(0) == '[') {
                return Class.forName(name, true, loader);
            } else {
                return loader.loadClass(name);
            }
        }        
        return Class.forName(name, true, loader);
    }


    public static ClassLoader getCurrentLoader(Object fallbackClass) {
        ClassLoader loader = getContextClassLoader();
        if (loader == null) {
            loader = fallbackClass.getClass().getClassLoader();
        }
        return loader;
    }

    private static ClassLoader getContextClassLoader() {
        if (System.getSecurityManager() == null) {
            return Thread.currentThread().getContextClassLoader();
        } else {
            return (ClassLoader)
                java.security.AccessController.doPrivileged(
                    new java.security.PrivilegedAction() {
                        public java.lang.Object run() {
                            return Thread.currentThread().getContextClassLoader();
                        }
                    });
        }
    }


    public static String removeAllButLastSlashPathSegment(String input) {
        // Trim the leading lastSlash, if any.
        if (input.charAt(0) == '/') {
            input = input.substring(1);
        }
        int len = input.length();
        // Trim the trailing lastSlash, if any.
        if (input.charAt(len - 1) == '/') {
            input = input.substring(0, len - 1);
        }
        
        // Trim any path segments that remain, leaving only the 
        // last path segment.
        int slash = input.lastIndexOf("/");
        
        // Do we have a "/"?
        if (-1 != slash) {
            input = input.substring(slash + 1);
        }
        
        return input;
    }
    
    public static String removeAllButNextToLastSlashPathSegment(String input) {
        // Trim the leading lastSlash, if any.
        if (input.charAt(0) == '/') {
            input = input.substring(1);
        }
        int len = input.length();
        // Trim the trailing lastSlash, if any.
        if (input.charAt(len - 1) == '/') {
            input = input.substring(0, len - 1);
        }
        
        // Trim any path segments that remain, leaving only the 
        // last path segment.
        int lastSlash = input.lastIndexOf("/");
        
        // Do we have a "/"?
        if (-1 != lastSlash) {
        
            int startOrPreviousSlash = input.lastIndexOf("/", lastSlash - 1);
            startOrPreviousSlash = (-1 == startOrPreviousSlash) ? 0 : startOrPreviousSlash;
            
            input = input.substring(startOrPreviousSlash, lastSlash);
        }
        
        return input;
    }
    
    public static String removeLastPathSegment(String input) {
        int slash = input.lastIndexOf("/");
        
        // Do we have a "/"?
        if (-1 != slash) {
            input = input.substring(0, slash);
        }
        
        return input;
    }

    /*
     * Apply a series of conventions to the component to arrive at a potential
     * flowId, which may or may not correspond to an actual defined flow.
     * 
     * Assume the target is a UIViewRoot.
     * 
     * Get its viewId.
     * 
     * Strip the leading lastSlash, if any.
     * 
     * If the viewId has multiple segments assume the next to last is the flowId.
     * 
     * Otherwise, remove the extension and assume the result is the flowId.
     * 
     * 
     */
    public static String getFlowIdFromComponent(FacesContext context, UIComponent target) {
        String result = "";
        if (target instanceof javax.faces.component.UIViewRoot) {
            result = Util.removeAllButNextToLastSlashPathSegment(((javax.faces.component.UIViewRoot)target).getViewId());
            
            int dot = result.indexOf(".");
            if (-1 != dot) {
                result = result.substring(0, dot);
            }
        }
        
        return result;
    }

    

    public static void notNull(String varname, Object var) {

        if (var == null) {
            throw new NullPointerException(
                  MessageUtils.getExceptionMessageString(
                      MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, varname));
        }
        
    }


    /**
     * @param context the <code>FacesContext</code> for the current request
     * @return the Locale from the UIViewRoot, the the value of Locale.getDefault()
     */
    public static Locale getLocaleFromContextOrSystem(FacesContext context) {
        Locale result, temp = Locale.getDefault();
        UIViewRoot root;
        result = temp;
        if (null != context && null != (root = context.getViewRoot()) && null == (result = root.getLocale())) {
            result = temp;
        }
        return result;
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


    public static StateManager getStateManager(FacesContext context)
        throws FacesException {
        return (context.getApplication().getStateManager());
    }
    
    public static Class getTypeFromString(String type) throws ClassNotFoundException {
        Class result;
        if (type.equals("byte")) {
            result = Byte.TYPE;
        } else if (type.equals("short")) {
            result = Short.TYPE;
        } else if (type.equals("int")) {
            result = Integer.TYPE;
        } else if (type.equals("long")) {
            result = Long.TYPE;
        } else if (type.equals("float")) {
            result = Float.TYPE;
        } else if (type.equals("double")) {
            result = Double.TYPE;
        } else if (type.equals("boolean")) {
            result = Boolean.TYPE;
        } else if (type.equals("char")) {
            result = Character.TYPE;
        } else if (type.equals("void")) {
            result = Void.TYPE;
        } else {
            if (type.indexOf('.') == -1) {
                type = "java.lang." + type;
            }
            result = Util.loadClass(type, Void.TYPE);
        }

        return result;
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

        return (Boolean.valueOf(String.valueOf(component.getAttributes().get("disabled"))));

    }


    public static boolean componentIsDisabledOrReadonly(UIComponent component) {
        return Boolean.valueOf(String.valueOf(component.getAttributes().get("disabled"))) || Boolean.valueOf(String.valueOf(component.getAttributes().get("readonly")));
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
        
        try {
            Method method = Locale.class.getMethod("forLanguageTag", String.class);
            if (method != null) {
                result = (Locale) method.invoke(null, localeStr);
            }
        } catch(Throwable throwable) {
            // if we are NOT running JavaSE 7 we end up here and we will 
            // default to the previous way of determining the Locale below.
        }

        if (result == null || result.getLanguage().equals("")) {
            String lang = null;
            String country = null;
            String variant = null;
            char[] seps = {
                '-',
                '_'
            };
            int inputLength = localeStr.length();
            int i = 0;
            int j = 0;

            // to have a language, the length must be >= 2
            if ((inputLength >= 2) &&
                ((i = indexOfSet(localeStr, seps, 0)) == -1)) {
                // we have only Language, no country or variant
                if (2 != localeStr.length()) {
                    throw new
                        IllegalArgumentException("Illegal locale String: " +
                                                 localeStr);
                }
                lang = localeStr.toLowerCase();
            }

            // we have a separator, it must be either '-' or '_'
            if (i != -1) {
                lang = localeStr.substring(0, i);
                // look for the country sep.
                // to have a country, the length must be >= 5
                if ((inputLength >= 5) &&
                    ((j = indexOfSet(localeStr, seps, i + 1)) == -1)) {
                    // no further separators, length must be 5
                    if (inputLength != 5) {
                        throw new
                            IllegalArgumentException("Illegal locale String: " +
                                                     localeStr);
                    }
                    country = localeStr.substring(i + 1);
                }
                if (j != -1) {
                    country = localeStr.substring(i + 1, j);
                    // if we have enough separators for language, locale,
                    // and variant, the length must be >= 8.
                    if (inputLength >= 8) {
                        variant = localeStr.substring(j + 1);
                    } else {
                        throw new
                            IllegalArgumentException("Illegal locale String: " +
                                                     localeStr);
                    }
                }
            }
            if (variant != null && country != null && lang != null) {
                result = new Locale(lang, country, variant);
            } else if (lang != null && country != null) {
                result = new Locale(lang, country);
            } else if (lang != null) {
                result = new Locale(lang, "");
            }
        }
        
        return result;
    }

    /**
     * @param str local string
     * @param set the substring
     * @param fromIndex starting index
     * @return starting at <code>fromIndex</code>, the index of the
     *         first occurrence of any substring from <code>set</code> in
     *         <code>toSearch</code>, or -1 if no such match is found
     */
    public static int indexOfSet(String str, char[] set, int fromIndex) {
        int result = -1;
        for (int i = fromIndex, len = str.length(); i < len; i++) {
            for (int j = 0, innerLen = set.length; j < innerLen; j++) {
                if (str.charAt(i) == set[j]) {
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


    /**
     * <p>Leverage the Throwable.getStackTrace() method to produce a String
     * version of the stack trace, with a "\n" before each line.</p>
     *
     * @param e the Throwable to obtain the stacktrace from
     *
     * @return the String representation ofthe stack trace obtained by calling
     *         getStackTrace() on the passed in exception.  If null is passed
     *         in, we return the empty String.
     */
    public static String getStackTraceString(Throwable e) {
        if (null == e) {
            return "";
        }

        StackTraceElement[] stacks = e.getStackTrace();
        StringBuffer sb = new StringBuffer();
        for (StackTraceElement stack : stacks) {
            sb.append(stack.toString()).append('\n');
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
     * @param response the current response
     * @return the content type of the response
     */
    public static String getContentTypeFromResponse(Object response) {
        String result = null;
        if (null != response) {           

            try {
                Method method = ReflectionUtils.lookupMethod(
                      response.getClass(),
                      "getContentType",
                      RIConstants.EMPTY_CLASS_ARGS
                );
                if (null != method) {
                    Object obj =
                          method.invoke(response, RIConstants.EMPTY_METH_ARGS);
                    if (null != obj) {
                        result = obj.toString();
                    }
                }
            } catch (Exception e) {
                throw new FacesException(e);
            }
        }
        return result;
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
   

    /**
     * <p>A slightly more efficient version of 
     * <code>String.split()</code> which caches
     * the <code>Pattern</code>s in an LRUMap instead of
     * creating a new <code>Pattern</code> on each
     * invocation.</p>
     * @param appMap the Application Map
     * @param toSplit the string to split
     * @param regex the regex used for splitting
     * @return the result of <code>Pattern.spit(String, int)</code>
     */
    public synchronized static String[] split(Map<String, Object> appMap, String toSplit, String regex) {
        Map<String, Pattern> patternCache = getPatternCache(appMap);
        Pattern pattern = patternCache.get(regex);
        if (pattern == null) {
            pattern = Pattern.compile(regex);
            patternCache.put(regex, pattern);
        }
        return  pattern.split(toSplit, 0);
    }

     public synchronized static String[] split(ServletContext sc,
             String toSplit, String regex) {
        Map<String, Pattern> patternCache = getPatternCache(sc);
        Pattern pattern = patternCache.get(regex);
        if (pattern == null) {
            pattern = Pattern.compile(regex);
            patternCache.put(regex, pattern);
        }
        return  pattern.split(toSplit, 0);
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
              (String) RequestStateManager.get(context, RequestStateManager.INVOCATION_PATH);

        if (mapping == null) {
         
            // first check for javax.servlet.forward.servlet_path
            // and javax.servlet.forward.path_info for non-null
            // values.  if either is non-null, use this
            // information to generate determine the mapping.

            String servletPath = extContext.getRequestServletPath();
            String pathInfo = extContext.getRequestPathInfo();

            mapping = getMappingForRequest(servletPath, pathInfo);
            if (mapping == null && LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE,
                        "jsf.faces_servlet_mapping_cannot_be_determined_error",
                        new Object[]{servletPath});
            }
        }
        
        // if the FacesServlet is mapped to /* throw an 
        // Exception in order to prevent an endless 
        // RequestDispatcher loop
        //if ("/*".equals(mapping)) {
        //    throw new FacesException(MessageUtils.getExceptionMessageString(
        //          MessageUtils.FACES_SERVLET_MAPPING_INCORRECT_ID));
        //}

        if (mapping != null) {
            RequestStateManager.set(context,
                                    RequestStateManager.INVOCATION_PATH,
                                    mapping);
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
     * @see javax.servlet.http.HttpServletRequest#getServletPath()
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

    public static boolean isSpecialAttributeName(String name) {
        boolean isSpecialAttributeName = name.equals("action")  ||
                            name.equals("actionListener") || name.equals("validator")
                            || name.equals("valueChangeListener");
        return isSpecialAttributeName;
    }


    /**
     * @param ctx the {@link FacesContext} for the current request
     * @param viewToRender the {@link UIViewRoot} to check
     * @return <code>true</code> if the {@link FacesContext} attributes map
     *  contains a reference to the {@link UIViewRoot}'s view ID
     */
    public static boolean isViewPopulated(FacesContext ctx, UIViewRoot viewToRender) {

        return ctx.getAttributes().containsKey(viewToRender);

    }


    /**
     * <p>
     * Flag the specified {@link UIViewRoot} as populated.
     * </p>
     * @param ctx the {@link FacesContext} for the current request
     * @param viewToRender the {@link UIViewRoot} to mark as populated
     */
    public static void setViewPopulated(FacesContext ctx,
                                        UIViewRoot viewToRender) {

        ctx.getAttributes().put(viewToRender, Boolean.TRUE);

    }


    /**
     * Utility method to validate ID uniqueness for the tree represented
     * by <code>component</code>.
     */
    public static void checkIdUniqueness(FacesContext context,
                                          UIComponent component,
                                          Set<String> componentIds) {

        boolean uniquenessCheckDisabled = false;
        
        if (context.isProjectStage(ProjectStage.Production)) {
            WebConfiguration config = WebConfiguration.getInstance(context.getExternalContext());
            uniquenessCheckDisabled = config.isOptionEnabled(
                WebConfiguration.BooleanWebContextInitParameter.DisableIdUniquenessCheck);
        }
        
        if (!uniquenessCheckDisabled) {
        
            // deal with children/facets that are marked transient.
            for (Iterator<UIComponent> kids = component.getFacetsAndChildren();
                kids.hasNext();) {

                UIComponent kid = kids.next();
                // check for id uniqueness
                String id = kid.getClientId(context);
                if (componentIds.add(id)) {
                    checkIdUniqueness(context, kid, componentIds);
                } else {
                    if (LOGGER.isLoggable(Level.SEVERE)) {
                        LOGGER.log(Level.SEVERE,
                                   "jsf.duplicate_component_id_error",
                                   id);


                        FastStringWriter writer = new FastStringWriter(128);
                        DebugUtil.simplePrintTree(context.getViewRoot(), id, writer);
                        LOGGER.severe(writer.toString());
                    }

                    String message =
                          MessageUtils.getExceptionMessageString(
                                MessageUtils.DUPLICATE_COMPONENT_ID_ERROR_ID, id);
                    throw new IllegalStateException(message);
                }
            }
        }
    }

    static public boolean classHasAnnotations(Class<?> clazz) {
        if (clazz != null) {
            while (clazz != Object.class) {
                Field[] fields = clazz.getDeclaredFields();
                if (fields != null) {
                    for (Field field : fields) {
                        if (field.getAnnotations().length > 0) {
                            return true;
                        }
                    }
                }

                Method[] methods = clazz.getDeclaredMethods();
                if (methods != null) {
                    for (Method method : methods) {
                        if (method.getDeclaredAnnotations().length > 0) {
                            return true;
                        }
                    }
                }

                clazz = clazz.getSuperclass();
            }
        }

        return false;
    }
    
    public static String getViewStateId(FacesContext context) {
        String result = null;
        final String viewStateCounterKey = "com.sun.faces.util.ViewStateCounterKey";
        Map<Object, Object> contextAttrs = context.getAttributes();
        Integer counter = (Integer) contextAttrs.get(viewStateCounterKey);
        if (null == counter) {
            counter = Integer.valueOf(0);
        }
        
        char sep = UINamingContainer.getSeparatorChar(context);
        UIViewRoot root = context.getViewRoot();
        result = root.getContainerClientId(context) + sep + 
                ResponseStateManager.VIEW_STATE_PARAM + sep +
                + counter;
        contextAttrs.put(viewStateCounterKey, ++counter);
        
        return result;
    }

    public static String getClientWindowId(FacesContext context) {
        String result = null;
        final String clientWindowIdCounterKey = "com.sun.faces.util.ClientWindowCounterKey";
        Map<Object, Object> contextAttrs = context.getAttributes();
        Integer counter = (Integer) contextAttrs.get(clientWindowIdCounterKey);
        if (null == counter) {
            counter = Integer.valueOf(0);
        }
        
        char sep = UINamingContainer.getSeparatorChar(context);
        result = context.getViewRoot().getContainerClientId(context) + sep + 
                ResponseStateManager.CLIENT_WINDOW_PARAM + sep + counter;
        contextAttrs.put(clientWindowIdCounterKey, ++counter);
        
        return result;
    }

    private static final String FACES_CONTEXT_ATTRIBUTES_DOCTYPE_KEY = Util.class.getName() + "_FACES_CONTEXT_ATTRS_DOCTYPE_KEY";
    
    public static void saveDOCTYPEToFacesContextAttributes(String DOCTYPE) {
        FacesContext context = FacesContext.getCurrentInstance();
        if (null == context) {
            return;
        }
        Map<Object, Object> attrs = context.getAttributes();
        attrs.put(FACES_CONTEXT_ATTRIBUTES_DOCTYPE_KEY, DOCTYPE);
        
    }
    
    public static String getDOCTYPEFromFacesContextAttributes(FacesContext context) {
        if (null == context) {
            return null;
        }
        Map<Object, Object> attrs = context.getAttributes();
        return (String) attrs.get(FACES_CONTEXT_ATTRIBUTES_DOCTYPE_KEY);
    }
    
    private static final String FACES_CONTEXT_ATTRIBUTES_XMLDECL_KEY = Util.class.getName() + "_FACES_CONTEXT_ATTRS_XMLDECL_KEY";
    
    public static void saveXMLDECLToFacesContextAttributes(String XMLDECL) {
        FacesContext context = FacesContext.getCurrentInstance();
        if (null == context) {
            return;
        }
        Map<Object, Object> attrs = context.getAttributes();
        attrs.put(FACES_CONTEXT_ATTRIBUTES_XMLDECL_KEY, XMLDECL);
        
    }
    
    public static String getXMLDECLFromFacesContextAttributes(FacesContext context) {
        if (null == context) {
            return null;
        }
        Map<Object, Object> attrs = context.getAttributes();
        return (String) attrs.get(FACES_CONTEXT_ATTRIBUTES_XMLDECL_KEY);
    }
    
    public static long getLastModified(URL url) {
        long lastModified;
        URLConnection conn;
        InputStream is = null;

        try {
            conn = url.openConnection();

            if (conn instanceof JarURLConnection) { 
                /*
                 * Note this is a work around for JarURLConnection since the
                 * getLastModified method is buggy. See JAVASERVERFACES-2725
                 * and JAVASERVERFACES-2734.
                 */
                JarURLConnection jarUrlConnection = (JarURLConnection) conn; 
                URL jarFileUrl = jarUrlConnection.getJarFileURL(); 
                URLConnection jarFileConnection = jarFileUrl.openConnection(); 
                lastModified = jarFileConnection.getLastModified(); 
                jarFileConnection.getInputStream().close(); 
            }
            else { 
                is = conn.getInputStream(); 
                lastModified = conn.getLastModified(); 
            } 
        } catch (Exception e) { 
            throw new FacesException("Error Checking Last Modified for " + url, e);
        } finally {
            if (is != null) {
                try { 
                    is.close();
                } catch (Exception e) {
                    if (LOGGER.isLoggable(Level.FINEST)) {
                        LOGGER.log(Level.FINEST, "Closing stream", e);
                    }
                }
            }
        }
        return lastModified;
    }

} // end of class Util
