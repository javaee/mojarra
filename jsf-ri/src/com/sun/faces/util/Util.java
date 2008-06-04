/*
 * $Id: Util.java,v 1.219.2.7 2008/04/17 19:59:21 edburns Exp $
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

import com.sun.faces.RIConstants;

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
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.faces.event.ComponentSystemEventListener;
import javax.faces.event.SystemEventListener;
import javax.faces.event.ListenerFor;
import javax.faces.event.SystemEvent;

/**
 * <B>Util</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: Util.java,v 1.219.2.7 2008/04/17 19:59:21 edburns Exp $
 */

public class Util {

    
    // Log instance for this class
    private static final Logger LOGGER = FacesLogger.APPLICATION.getLogger();

    // README - make sure to add the message identifier constant
    // (ex: Util.CONVERSION_ERROR_MESSAGE_ID) and the number of substitution
    // parameters to test/com/sun/faces/util/TestUtil_messages (see comment there).


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
        // Where to begin...
        // JDK 6 introduced CR 6434149 where one couldn't pass
        // in a literal for an array type ([Ljava.lang.String) and
        // get the Class representation using ClassLoader.loadClass().
        // It was recommended to use Class.forName(String, boolean, ClassLoader)
        // for all ClassLoading requests.
        // HOWEVER, when trying to eliminate the need for .groovy extensions
        // being specified in the faces-config.xml for Groovy-based artifacts,
        // by using a an adapter to the GroovyScriptEngine, I found that the class
        // instance was cached somewhere, so that no matter what change I made,
        // Class.forName() always returned the same instance.  I haven't been
        // able to determine why this happens in the appserver environment
        // as the same adapter in a standalone program works as one might expect.
        // So, for now, if the classname starts with '[', then use Class.forName()
        // to avoid CR 643419 and for all other cases, use ClassLoader.loadClass().
        if (name.charAt(0) == '[') {
            return Class.forName(name, true, loader);
        } else {
            return loader.loadClass(name);
        }
    }


    public static ClassLoader getCurrentLoader(Object fallbackClass) {
        ClassLoader loader =
            Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            loader = fallbackClass.getClass().getClassLoader();
        }
        return loader;
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
        if (null != context) {
            if (null != (root = context.getViewRoot())) {
                if (null == (result = root.getLocale())) {
                    result = temp;
                }
            }
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
        for (int i = 0; i < stacks.length; i++) {
            sb.append(stacks[i].toString()).append('\n');
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

    public static boolean prefixViewTraversal(FacesContext context,
                                              UIComponent root,
                                              TreeTraversalCallback action)
    throws FacesException {
        boolean keepGoing;
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
    
    public static void processListenerForAnnotation(UIComponent source) {
        if (source.getClass().isAnnotationPresent(ListenerFor.class) &&
            source instanceof SystemEventListener) {
            ListenerFor listenerFor = (ListenerFor) 
                    source.getClass().getAnnotation(ListenerFor.class);
            assert(null != listenerFor);
            Class<? extends SystemEvent> eventClass = listenerFor.systemEventClass();
            Class sourceClass = listenerFor.sourceClass();
            source.subscribeToEvent(eventClass, (ComponentSystemEventListener) source);
        }
        
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
    public synchronized static String[] split(String toSplit, String regex) {
        Pattern pattern = patternCache.get(regex);
        if (pattern == null) {
            pattern = Pattern.compile(regex);
            patternCache.put(regex, pattern);
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
              (String) RequestStateManager.get(context, RequestStateManager.INVOCATION_PATH);

        if (mapping == null) {
         
            // first check for javax.servlet.forward.servlet_path
            // and javax.servlet.forward.path_info for non-null
            // values.  if either is non-null, use this
            // information to generate determine the mapping.

            String servletPath = extContext.getRequestServletPath();
            String pathInfo = extContext.getRequestPathInfo();

            mapping = getMappingForRequest(servletPath, pathInfo);
            if (mapping == null) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE,
                               "jsf.faces_servlet_mapping_cannot_be_determined_error",
                               new Object[]{servletPath});
                }
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


} // end of class Util
