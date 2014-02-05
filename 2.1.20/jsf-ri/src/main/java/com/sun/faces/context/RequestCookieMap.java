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

import java.util.Map;
import java.util.Set;
import java.util.Collections;
import java.util.Collection;
import java.util.Iterator;
import java.util.Enumeration;
import java.util.NoSuchElementException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Cookie;

import com.sun.faces.util.Util;

/**
 * @see javax.faces.context.ExternalContext#getRequestCookieMap()  
 */
public class RequestCookieMap extends BaseContextMap<Object> {

    private final HttpServletRequest request;


    // ------------------------------------------------------------ Constructors


    public RequestCookieMap(HttpServletRequest newRequest) {
        this.request = newRequest;
    }


    // -------------------------------------------------------- Methods from Map


    @Override
    public Object get(Object key) {
        Util.notNull("key", key);

        Cookie[] cookies = request.getCookies();
        if (null == cookies) {
            return null;
        }

        String keyString = key.toString();
        Object result = null;

        for (int i = 0; i < cookies.length; i++) {
            if (cookies[i].getName().equals(keyString)) {
                result = cookies[i];
                break;
            }
        }
        return result;
    }


    @Override
    public Set<Map.Entry<String,Object>> entrySet() {
        return Collections.unmodifiableSet(super.entrySet());
    }


    @Override
    public Set<String> keySet() {
        return Collections.unmodifiableSet(super.keySet());
    }


    @Override
    public Collection<Object> values() {
        return Collections.unmodifiableCollection(super.values());
    }


    @Override
    public boolean equals(Object obj) {
        return !(obj == null ||
                 !(obj.getClass()
                   == ExternalContextImpl
                       .theUnmodifiableMapClass)) && super.equals(obj);
    }


    @Override
    public int hashCode() {
        int hashCode = 7 * request.hashCode();
        for (Iterator i = entrySet().iterator(); i.hasNext(); ) {
            hashCode += i.next().hashCode();
        }
        return hashCode;
    }


    // --------------------------------------------- Methods from BaseContextMap


    protected Iterator<Map.Entry<String,Object>> getEntryIterator() {
        return new EntryIterator(
                new CookieArrayEnumerator(request.getCookies()));
    }


    protected Iterator<String> getKeyIterator() {
        return new KeyIterator(
                new CookieArrayEnumerator(request.getCookies()));
    }


    protected Iterator<Object> getValueIterator() {
        return new ValueIterator(
            new CookieArrayEnumerator(request.getCookies()));
    }


    // ----------------------------------------------------------- Inner Classes


    private static class CookieArrayEnumerator implements Enumeration {

        Cookie[] cookies;
        int curIndex = -1;
        int upperBound;

        public CookieArrayEnumerator(Cookie[] cookies) {
            this.cookies = cookies;
            upperBound = ((this.cookies != null) ? this.cookies.length : -1);
        }

        public boolean hasMoreElements() {
            return (curIndex + 2 <= upperBound);
        }

        public Object nextElement() {
            curIndex++;
            if (curIndex < upperBound) {
                return cookies[curIndex].getName();
            } else {
                throw new NoSuchElementException();
            }
        }
        
    }

} // END RequestCookiesMap
