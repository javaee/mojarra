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
 */

package com.sun.faces.context;

import java.util.Enumeration;
import java.util.Map;
import java.util.Iterator;

import javax.servlet.ServletContext;

import com.sun.faces.util.Util;

/**
 * @see javax.faces.context.ExternalContext#getApplicationMap()
 */
public class ApplicationMap extends BaseContextMap<Object> {

    private final ServletContext servletContext;


    // ------------------------------------------------------------ Constructors


    public ApplicationMap(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
    
    public Object getContext() {
        return this.servletContext;
    }


    // -------------------------------------------------------- Methods from Map


    @Override
    public void clear() {
        for (Enumeration e = servletContext.getAttributeNames();
             e.hasMoreElements(); ) {
            servletContext.removeAttribute((String) e.nextElement());
        }
    }


    // Supported by maps if overridden
    @Override
    public void putAll(Map t) {
        for (Iterator i = t.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry entry = (Map.Entry) i.next();
            servletContext.setAttribute((String) entry.getKey(),
                                        entry.getValue());
        }
    }


    @Override
    public Object get(Object key) {
        Util.notNull("key", key);
        return servletContext.getAttribute(key.toString());
    }


    @Override
    public Object put(String key, Object value) {
        Util.notNull("key", key);
        Object result = servletContext.getAttribute(key);
        servletContext.setAttribute(key, value);
        return (result);
    }


    @Override
    public Object remove(Object key) {
        if (key == null) {
            return null;
        }
        String keyString = key.toString();
        Object result = servletContext.getAttribute(keyString);
        servletContext.removeAttribute(keyString);
        return (result);
    }


    @Override
    public boolean containsKey(Object key) {
        return (servletContext.getAttribute(key.toString()) != null);
    }


    @Override
    public boolean equals(Object obj) {
        return !(obj == null || !(obj instanceof ApplicationMap))
                   && super.equals(obj);
    }


    @Override
    public int hashCode() {
        int hashCode = 7 * servletContext.hashCode();
        for (Iterator i = entrySet().iterator(); i.hasNext(); ) {
            hashCode += i.next().hashCode();
        }
        return hashCode;
    }


    // --------------------------------------------- Methods from BaseContextMap


    @SuppressWarnings("unchecked")
    protected Iterator<Map.Entry<String, Object>> getEntryIterator() {
        return new EntryIterator(servletContext.getAttributeNames());
    }

    @SuppressWarnings("unchecked")
    protected Iterator<String> getKeyIterator() {
        return new KeyIterator(servletContext.getAttributeNames());
    }

    @SuppressWarnings("unchecked")
    protected Iterator<Object> getValueIterator() {
        return new ValueIterator(servletContext.getAttributeNames());
    }

} // END ApplicationMap
