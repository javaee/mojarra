/*
 * $Id: LoadBundleTag.java,v 1.16 2007/02/27 23:10:22 rlubke Exp $
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

package com.sun.faces.taglib.jsf_core;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.sun.faces.util.Util;
import com.sun.faces.util.MessageUtils;
import com.sun.faces.el.ELUtils;

/**
 * <p>Tag action that loads the specified ResourceBundle as a Map into
 * the request scope of the current {@link
 * javax.faces.context.FacesContext}.</p>
 * <p/>
 * <p>The user is discouraged from using multiple dot syntax in their
 * resource bundle keys.  For example, for the bundle loaded under the
 * var <code>msgs</code>, this key: <code>index.page.title</code> is
 * discouraged.  If your application requires this syntax for resource
 * bundle keys, they may be referred to in the page with a syntax like
 * this: <code>#{msgs["index.page.title"]}.</code></p>
 */

public class LoadBundleTag extends TagSupport {


    // ------------------------------------------------------------- Attributes

    private ValueExpression basenameExpression;


    /**
     * <p>Set the base name of the <code>ResourceBundle</code> to be
     * loaded.</p>
     */
    public void setBasename(ValueExpression basename) {
        this.basenameExpression = basename;
    }


    private String var;


    /**
     * <p>Set the name of the attribute in the request scope under which
     * to store the <code>ResourceBundle</code> <code>Map</code>.</p>
     */

    public void setVar(String newVar) {
        var = newVar;
    }



    // --------------------------------------------------------- Public Methods


    /**
     * <p>Load the <code>ResourceBundle</code> named by our
     * <code>basename</code> property.</p>  Wrap it in an immutable
     * <code>Map</code> implementation and store the <code>Map</code> in
     * the request attr set of under the key given by our
     * <code>var</code> property.
     *
     * @throws JspException if a JSP error occurs
     */
    public int doStartTag() throws JspException {

        FacesContext context = FacesContext.getCurrentInstance();

        // evaluate any VB expression that we were passed
        String basename;

        basename = (String)
            ELUtils.evaluateValueExpression(basenameExpression,
                                         context.getELContext());


        if (null == basename) {
        	String message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "baseName");
        	throw new NullPointerException(message);
        }
        if (null == var) {
        	String message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "baseName");
        	throw new NullPointerException(message);
        }

        final ResourceBundle bundle =
            ResourceBundle.getBundle(basename,
                                     context.getViewRoot().getLocale(),
                                     Util.getCurrentLoader(this));
        if (null == bundle) {
            throw new JspException("null ResourceBundle for " + basename);
        }

        Map toStore =
            new Map() {
                // this is an immutable Map

                // Do not need to implement for immutable Map
                public void clear() {
                    throw new UnsupportedOperationException();
                }


                public boolean containsKey(Object key) {
                    boolean result = false;
                    if (null != key) {
                        result = (null != bundle.getObject(key.toString()));
                    }
                    return result;
                }


                public boolean containsValue(Object value) {
                    Enumeration<String> keys = bundle.getKeys();
                    Object curObj = null;
                    boolean result = false;
                    while (keys.hasMoreElements()) {
                        curObj = bundle.getObject(keys.nextElement());
                        if ((curObj == value) ||
                            ((null != curObj) && curObj.equals(value))) {
                            result = true;
                            break;
                        }
                    }
                    return result;
                }


                public Set entrySet() {
                    HashMap<String,Object> mappings = new HashMap<String, Object>();
                    Enumeration<String> keys = bundle.getKeys();
                    while (keys.hasMoreElements()) {
                        String key = keys.nextElement();
                        Object value = bundle.getObject(key);
                        mappings.put(key, value);
                    }
                    return mappings.entrySet();
                }


                public boolean equals(Object obj) {
                    if ((obj == null) || !(obj instanceof Map)) {
                        return false;
                    }

                    return entrySet().equals(((Map) obj).entrySet());

                }


                public Object get(Object key) {
                    if (null == key) {
                        return null;
                    }
                    Object result = null;
                    try {
                        result = bundle.getObject(key.toString());
                    } catch (MissingResourceException e) {
                        result = "???" + key + "???";
                    }
                    return result;
                }


                public int hashCode() {
                    return bundle.hashCode();
                }


                public boolean isEmpty() {
                    boolean result = true;
                    Enumeration<String> keys = bundle.getKeys();
                    result = !keys.hasMoreElements();
                    return result;
                }


                public Set keySet() {
                    Set<String> keySet = new HashSet<String>();
                    Enumeration<String> keys = bundle.getKeys();
                    while (keys.hasMoreElements()) {
                        keySet.add(keys.nextElement());
                    }
                    return keySet;
                }


                // Do not need to implement for immutable Map
                public Object put(Object k, Object v) {
                    throw new UnsupportedOperationException();
                }


                // Do not need to implement for immutable Map
                public void putAll(Map t) {
                    throw new UnsupportedOperationException();
                }


                // Do not need to implement for immutable Map
                public Object remove(Object k) {
                    throw new UnsupportedOperationException();
                }


                public int size() {
                    int result = 0;
                    Enumeration<String> keys = bundle.getKeys();
                    while (keys.hasMoreElements()) {
                        keys.nextElement();
                        result++;
                    }
                    return result;
                }


                public java.util.Collection values() {
                    ArrayList<Object> result = new ArrayList<Object>();
                    Enumeration<String> keys = bundle.getKeys();
                    while (keys.hasMoreElements()) {
                        result.add(
                            bundle.getObject(keys.nextElement()));
                    }
                    return result;
                }
            };

        context.getExternalContext().getRequestMap().put(var, toStore);

        return (EVAL_BODY_INCLUDE);

    }


    /**
     * <p>Release references to any acquired resources.
     */
    public void release() {

        this.basenameExpression = null;
        this.var = null;

    }


}
