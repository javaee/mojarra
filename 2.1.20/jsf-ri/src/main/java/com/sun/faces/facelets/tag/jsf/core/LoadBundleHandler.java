/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
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
 *
 *
 * This file incorporates work covered by the following copyright and
 * permission notice:
 *
 * Copyright 2005-2007 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sun.faces.facelets.tag.jsf.core;

import com.sun.faces.facelets.tag.TagHandlerImpl;
import com.sun.faces.facelets.tag.jsf.ComponentSupport;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagAttributeException;
import javax.faces.view.facelets.TagConfig;
import java.io.IOException;
import java.util.*;

/**
 * Load a resource bundle localized for the Locale of the current view, and
 * expose it (as a Map) in the request attributes of the current request. <p/>
 * See <a target="_new"
 * href="http://java.sun.com/j2ee/javaserverfaces/1.1_01/docs/tlddocs/f/loadBundle.html">tag
 * documentation</a>.
 * 
 * @author Jacob Hookom
 * @version $Id$
 */
public final class LoadBundleHandler extends TagHandlerImpl {

    private final static class ResourceBundleMap implements Map {
        private final static class ResourceEntry implements Map.Entry {

            protected final String key;

            protected final String value;

            public ResourceEntry(String key, String value) {
                this.key = key;
                this.value = value;
            }

            public Object getKey() {
                return this.key;
            }

            public Object getValue() {
                return this.value;
            }

            public Object setValue(Object value) {
                throw new UnsupportedOperationException();
            }

            public int hashCode() {
                return this.key.hashCode();
            }

            public boolean equals(Object obj) {
                return (obj instanceof ResourceEntry && this.hashCode() == obj
                        .hashCode());
            }
        }

        protected final ResourceBundle bundle;

        public ResourceBundleMap(ResourceBundle bundle) {
            this.bundle = bundle;
        }

        public void clear() {
            throw new UnsupportedOperationException();
        }

        public boolean containsKey(Object key) {
            try {
                bundle.getString(key.toString());
                return true;
            } catch (MissingResourceException e) {
                return false;
            }
        }

        public boolean containsValue(Object value) {
            throw new UnsupportedOperationException();
        }

        public Set entrySet() {
            Enumeration e = this.bundle.getKeys();
            Set s = new HashSet();
            String k;
            while (e.hasMoreElements()) {
                k = (String) e.nextElement();
                s.add(new ResourceEntry(k, this.bundle.getString(k)));
            }
            return s;
        }

        public Object get(Object key) {
        	try {
        		return this.bundle.getObject((String) key);
        	} catch( java.util.MissingResourceException mre ) {
        		return "???"+key+"???";
        	}
        }

        public boolean isEmpty() {
            return false;
        }

        public Set keySet() {
            Enumeration e = this.bundle.getKeys();
            Set s = new HashSet();
            while (e.hasMoreElements()) {
                s.add(e.nextElement());
            }
            return s;
        }

        public Object put(Object key, Object value) {
            throw new UnsupportedOperationException();
        }

        public void putAll(Map t) {
            throw new UnsupportedOperationException();
        }

        public Object remove(Object key) {
            throw new UnsupportedOperationException();
        }

        public int size() {
            return this.keySet().size();
        }

        public Collection values() {
            Enumeration e = this.bundle.getKeys();
            Set s = new HashSet();
            while (e.hasMoreElements()) {
                s.add(this.bundle.getObject((String) e.nextElement()));
            }
            return s;
        }
    }

    private final TagAttribute basename;

    private final TagAttribute var;

    /**
     * @param config
     */
    public LoadBundleHandler(TagConfig config) {
        super(config);
        this.basename = this.getRequiredAttribute("basename");
        this.var = this.getRequiredAttribute("var");
    }

    /**
     * See taglib documentation.
     */
    public void apply(FaceletContext ctx, UIComponent parent)
            throws IOException {
        UIViewRoot root = ComponentSupport.getViewRoot(ctx, parent);
        ResourceBundle bundle = null;
        try {
            String name = this.basename.getValue(ctx);
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            if (root != null && root.getLocale() != null) {
                bundle = ResourceBundle.getBundle(name, root.getLocale(), cl);
            } else {
                bundle = ResourceBundle
                        .getBundle(name, Locale.getDefault(), cl);
            }
        } catch (Exception e) {
            throw new TagAttributeException(this.tag, this.basename, e);
        }
        ResourceBundleMap map = new ResourceBundleMap(bundle);
        FacesContext faces = ctx.getFacesContext();
        faces.getExternalContext().getRequestMap().put(this.var.getValue(ctx),
                map);
    }
}
