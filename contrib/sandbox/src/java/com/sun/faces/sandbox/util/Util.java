/*
 * $Id: Util.java,v 1.11 2007/07/19 22:10:42 jdlee Exp $
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

//Util.java

package com.sun.faces.sandbox.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.el.ValueBinding;
import javax.faces.render.Renderer;
import javax.servlet.http.HttpServletRequest;

/**
 * <B>Util</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: Util.java,v 1.11 2007/07/19 22:10:42 jdlee Exp $
 */

public class Util {      

    public static final String STATIC_RESOURCE_IDENTIFIER = "/ri_sandbox_static/resource";

    /**
     * This array contains attributes that have a boolean value in JSP,
     * but have have no value in HTML.  For example "disabled" or
     * "readonly". <P>
     */
    private static String booleanPassthruAttributes[] = {
        "disabled",
        "readonly",
        "ismap"
    };

    private static final String INVOCATION_PATH = "com.sun.faces.sandbox.INVOCATION_PATH";
    /**
     * This array contains attributes whose value is just rendered
     * straight to the content.  This array should only contain
     * attributes that require no interpretation by the Renderer.  If an
     * attribute requires interpretation by a Renderer, it should be
     * removed from this array.<P>
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


    public static Object evaluateVBExpression(String expression) {
        if (expression == null || (!isVBExpression(expression))) {
            return expression;
        }
        FacesContext context = FacesContext.getCurrentInstance();
        Object result =
            getValueBinding(expression).getValue(
                    context);
        return result;
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


    public static ClassLoader getCurrentLoader(Object fallbackClass) {
        ClassLoader loader =
            Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            loader = fallbackClass.getClass().getClassLoader();
        }
        return loader;
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
            throw new NullPointerException("The FacesContext was null.");
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
        }

        // if the FacesServlet is mapped to /* throw an 
        // Exception in order to prevent an endless 
        // RequestDispatcher loop
        if ("/*".equals(mapping)) {
            throw new FacesException("The FacesServlet was configured incorrectly");
        }

        if (mapping != null) {
            extContext.getRequestMap().put(INVOCATION_PATH, mapping);
        }
        return mapping;
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

    public static ValueBinding getValueBinding(String valueRef) {
        // Must parse the value to see if it contains more than one expression
        return FacesContext.getCurrentInstance().getApplication().createValueBinding(valueRef);
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


    public static String generateStaticUri(String path) {
        String uri = "";
        FacesContext context = FacesContext.getCurrentInstance();
        String mapping = getFacesMapping(context);
        if (isPrefixMapped(mapping)) {
            uri = "/" + mapping + STATIC_RESOURCE_IDENTIFIER + path;
        } else {
            uri = STATIC_RESOURCE_IDENTIFIER + path + mapping;
        }

        return getAppBaseUrl(context) + uri;
    }

    public static void linkJavascript(ResponseWriter writer, String path, boolean modifyPath) throws IOException {
        // TODO:  Class.forName("some.shale.class"); useShaleStuff(); catch (ClassNotFound) {useOurStuff()};
        if ((path != null) && (path.length() > 0)) {
            if (modifyPath) {
                path = generateStaticUri(path);
            }
            if (!Util.hasResourceBeenRendered("RENDERED"+path)) {
                writer.startElement("script", null);
                writer.writeAttribute("type", "text/javascript", "type");
                writer.writeAttribute("src", path, "src");
    //            writer.writeAttribute("src", generateStaticUri(path), "src");
                writer.endElement("script");
                setResourceAsRendered("RENDERED"+path);
            }
        }
    }

    public static void linkStyleSheet(ResponseWriter writer, String path) throws IOException {
        if ((path != null) && (path.length() > 0)) {
            if (!Util.hasResourceBeenRendered("RENDERED"+path)) {
                writer.startElement("link", null);
                writer.writeAttribute("rel", "stylesheet", "rel");
                writer.writeAttribute("type", "text/css", "type");
                writer.writeAttribute("href", generateStaticUri(path), "href");
                writer.endElement("link");
                setResourceAsRendered("RENDERED"+path);
            }
        }
    }

    /*
    public static XhtmlHelper getXhtmlHelper() {
        if (xhtmlHelper == null) {
            xhtmlHelper = new XhtmlHelper();
        }

        return xhtmlHelper;
    }
    */

    public static Class loadClass(String name, Object fallbackClass) throws ClassNotFoundException {
        return Util.getCurrentLoader(fallbackClass).loadClass(name);
    }


    public static void outputTemplate(Renderer renderer, String path, Map<String, String> fields) throws IOException {
        ResponseWriter writer = FacesContext.getCurrentInstance().getResponseWriter();
        InputStream is = renderer.getClass().getResourceAsStream(path);
        if (is != null) {
            String template = readInString(is);

            for (Map.Entry<String, String> field : fields.entrySet()) {
                template = template.replaceAll("%%%"+field.getKey()+"%%%", field.getValue());
            }

            writer.writeText(template, null);
            is.close();
        }
    }

    public static String readInString(InputStream is) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String ret = null;

        try {
            int count = 0;
            byte[] buffer = new byte[4096];
            while ((count = is.read(buffer)) != -1) {
                if (count > 0) {
                    baos.write(buffer, 0, count);
                }
            }
            ret = baos.toString();

            baos.close();
            is.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        return ret;
    }

    public static void renderBooleanPassThruAttributes(ResponseWriter writer,
            UIComponent component)
    throws IOException {
        renderBooleanPassThruAttributes(writer, component, null);
    }


    /**
     * Render any boolean "passthru" attributes.
     */
    public static void renderBooleanPassThruAttributes(ResponseWriter writer,
            UIComponent component,
            String[] excludes)
    throws IOException {

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
     */
    public static void renderPassThruAttributes(ResponseWriter writer,
            UIComponent component,
            String[] excludes)
    throws IOException {

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

    private Util() {
        throw new IllegalStateException();
    }

    public static String getAppBaseUrl(FacesContext context) {
        String baseUrl = "";
        Object obj = context.getExternalContext().getRequest();
        if (obj instanceof HttpServletRequest ) {
            HttpServletRequest req = (HttpServletRequest )obj;
            baseUrl = req.getScheme() + "://" + req.getServerName() +
                ":" + req.getServerPort() + req.getContextPath();
        }
        
        return baseUrl;
//        return context.getExternalContext().getRequestContextPath();
    }
    
    /**
     * @param context the <code>FacesContext</code> for the current request
     *
     * @return <code>true</code> If the YUI JS and CSS overrides have been rendered
     */
    public static boolean hasResourceBeenRendered(String key) {
        return hasResourceBeenRendered(FacesContext.getCurrentInstance(), key);
    }
    
    public static boolean hasResourceBeenRendered(FacesContext context, String key) {
        return (context.getExternalContext().getRequestMap().get(key) != null);
    }


    /**
     * <p>Set a flag to indicate that the YUI JS and CSS overrides have been rendered
     *
     * @param context the <code>FacesContext</code> of the current request
     */
    public static void setResourceAsRendered(String key) {
        setResourceAsRendered(FacesContext.getCurrentInstance(), key);
    }
    
    @SuppressWarnings("unchecked")
    public static void setResourceAsRendered(FacesContext context, String key) {
        context.getExternalContext().getRequestMap().put(key, Boolean.TRUE);
    }


} // end of class Util