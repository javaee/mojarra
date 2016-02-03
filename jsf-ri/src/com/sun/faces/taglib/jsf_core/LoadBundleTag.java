/*
 * $Id: LoadBundleTag.java,v 1.8.36.2 2007/04/27 21:27:48 ofung Exp $
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

package com.sun.faces.taglib.jsf_core;

import com.sun.faces.util.Util;

import javax.faces.context.FacesContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

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

    private String basename;
    private String basename_;


    /**
     * <p>Set the base name of the <code>ResourceBundle</code> to be
     * loaded.</p>
     */
    public void setBasename(String newBasename) {
        basename_ = newBasename;
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

        // evaluate any VB expression that we were passed
        basename = (String) Util.evaluateVBExpression(basename_);

        if (null == basename || null == var) { // PENDING - i18n
            throw new JspException("null basename or var");
        }

        Map toStore = null;
        FacesContext context = FacesContext.getCurrentInstance();

        final ResourceBundle bundle =
            ResourceBundle.getBundle(basename,
                                     context.getViewRoot().getLocale(),
                                     Util.getCurrentLoader(this));
        if (null == bundle) {
            throw new JspException("null ResourceBundle for " + basename);
        }

        toStore =
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
                    Enumeration keys = bundle.getKeys();
                    Object curObj = null;
                    boolean result = false;
                    while (keys.hasMoreElements()) {
                        curObj = bundle.getObject((String) keys.nextElement());
                        if ((curObj == value) ||
                            ((null != curObj) && curObj.equals(value))) {
                            result = true;
                            break;
                        }
                    }
                    return result;
                }


                public Set entrySet() {
                    HashMap mappings = new HashMap();
                    Enumeration keys = bundle.getKeys();
                    while (keys.hasMoreElements()) {
                        Object key = keys.nextElement();
                        Object value = bundle.getObject((String) key);
                        mappings.put(key, value);
                    }
                    return mappings.entrySet();
                }


                public boolean equals(Object obj) {
                    if ((obj == null) || !(obj instanceof Map)) {
                        return false;
                    }

                    if (entrySet().equals(((Map) obj).entrySet())) {
                        return true;
                    }

                    return false;
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
                    Enumeration keys = bundle.getKeys();
                    result = !keys.hasMoreElements();
                    return result;
                }


                public Set keySet() {
                    Set keySet = new HashSet();
                    Enumeration keys = bundle.getKeys();
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
                    Enumeration keys = bundle.getKeys();
                    while (keys.hasMoreElements()) {
                        keys.nextElement();
                        result++;
                    }
                    return result;
                }


                public java.util.Collection values() {
                    ArrayList result = new ArrayList();
                    Enumeration keys = bundle.getKeys();
                    while (keys.hasMoreElements()) {
                        result.add(
                            bundle.getObject((String) keys.nextElement()));
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

        this.basename = null;
        this.var = null;

    }


}
