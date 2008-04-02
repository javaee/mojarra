/*
 * $Id: Util.java,v 1.1 2006/12/13 20:35:47 jdlee Exp $
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
 * Copyright 2006 Sun Microsystems Inc. All Rights Reserved
 */

// Util.java

package com.sun.faces.sandbox.util;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.el.ValueBinding;
import java.io.IOException;
import java.util.Map;

/**
 * <B>Util</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: Util.java,v 1.1 2006/12/13 20:35:47 jdlee Exp $
 */

public class Util {      

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

    private Util() {
        throw new IllegalStateException();
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

} // end of class Util

