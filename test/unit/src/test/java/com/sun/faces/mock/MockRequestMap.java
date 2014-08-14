/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2014 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.faces.mock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletRequest;

final class MockRequestMap implements Map {

    public MockRequestMap(ServletRequest request) {
        this.request = request;
    }

    private ServletRequest request = null;

    public void clear() {
        Iterator keys = keySet().iterator();
        while (keys.hasNext()) {
            request.removeAttribute((String) keys.next());
        }
    }

    public boolean containsKey(Object key) {
        return (request.getAttribute(key(key)) != null);
    }

    public boolean containsValue(Object value) {
        if (value == null) {
            return (false);
        }
        Enumeration keys = request.getAttributeNames();
        while (keys.hasMoreElements()) {
            Object next = request.getAttribute((String) keys.nextElement());
            if (next == value) {
                return (true);
            }
        }
        return (false);
    }

    public Set entrySet() {
        Set set = new HashSet();
        Enumeration keys = request.getAttributeNames();
        while (keys.hasMoreElements()) {
            set.add(request.getAttribute((String) keys.nextElement()));
        }
        return (set);
    }

    public boolean equals(Object o) {
        return (request.equals(o));
    }

    public Object get(Object key) {
        return (request.getAttribute(key(key)));
    }

    public int hashCode() {
        return (request.hashCode());
    }

    public boolean isEmpty() {
        return (size() < 1);
    }

    public Set keySet() {
        Set set = new HashSet();
        Enumeration keys = request.getAttributeNames();
        while (keys.hasMoreElements()) {
            set.add(keys.nextElement());
        }
        return (set);
    }

    public Object put(Object key, Object value) {
        if (value == null) {
            return (remove(key));
        }
        String skey = key(key);
        Object previous = request.getAttribute(skey);
        request.setAttribute(skey, value);
        return (previous);
    }

    public void putAll(Map map) {
        Iterator keys = map.keySet().iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            request.setAttribute(key, map.get(key));
        }
    }

    public Object remove(Object key) {
        String skey = key(key);
        Object previous = request.getAttribute(skey);
        request.removeAttribute(skey);
        return (previous);
    }

    public int size() {
        int n = 0;
        Enumeration keys = request.getAttributeNames();
        while (keys.hasMoreElements()) {
            keys.nextElement();
            n++;
        }
        return (n);
    }

    public Collection values() {
        List list = new ArrayList();
        Enumeration keys = request.getAttributeNames();
        while (keys.hasMoreElements()) {
            list.add(request.getAttribute((String) keys.nextElement()));
        }
        return (list);
    }

    private String key(Object key) {
        if (key == null) {
            throw new IllegalArgumentException();
        } else if (key instanceof String) {
            return ((String) key);
        } else {
            return (key.toString());
        }
    }

}
