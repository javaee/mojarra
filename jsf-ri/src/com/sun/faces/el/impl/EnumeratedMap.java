/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/*
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 1999 The Apache Software Foundation.  All rights 
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer. 
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:  
 *       "This product includes software developed by the 
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Tomcat", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written 
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package com.sun.faces.el.impl;

import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * <p>This is a Map implementation driven by a data source that only
 * provides an enumeration of keys and a getValue(key) method.  This
 * class must be subclassed to implement those methods.
 *
 * <p>Some of the methods may incur a performance penalty that
 * involves enumerating the entire data source.  In these cases, the
 * Map will try to save the results of that enumeration, but only if
 * the underlying data source is immutable.
 * 
 * @author Nathan Abramson - Art Technology Group
 * @version $Change: 181177 $$DateTime: 2001/06/26 08:45:09 $$Author: ofung $
 **/

public abstract class EnumeratedMap
    implements Map {
    //-------------------------------------
    // Member variables
    //-------------------------------------

    Map mMap;

    //-------------------------------------
    public void clear() {
        throw new UnsupportedOperationException();
    }

    //-------------------------------------
    public boolean containsKey(Object pKey) {
        return getValue(pKey) != null;
    }

    //-------------------------------------
    public boolean containsValue(Object pValue) {
        return getAsMap().containsValue(pValue);
    }

    //-------------------------------------
    public Set entrySet() {
        return getAsMap().entrySet();
    }

    //-------------------------------------
    public Object get(Object pKey) {
        return getValue(pKey);
    }

    //-------------------------------------
    public boolean isEmpty() {
        return !enumerateKeys().hasMoreElements();
    }

    //-------------------------------------
    public Set keySet() {
        return getAsMap().keySet();
    }

    //-------------------------------------
    public Object put(Object pKey, Object pValue) {
        throw new UnsupportedOperationException();
    }

    //-------------------------------------
    public void putAll(Map pMap) {
        throw new UnsupportedOperationException();
    }

    //-------------------------------------
    public Object remove(Object pKey) {
        throw new UnsupportedOperationException();
    }

    //-------------------------------------
    public int size() {
        return getAsMap().size();
    }

    //-------------------------------------
    public Collection values() {
        return getAsMap().values();
    }

    //-------------------------------------
    // Abstract methods
    //-------------------------------------
    /**
     *
     * Returns an enumeration of the keys
     **/
    public abstract Enumeration enumerateKeys();

    //-------------------------------------
    /**
     *
     * Returns true if it is possible for this data source to change
     **/
    public abstract boolean isMutable();

    //-------------------------------------
    /**
     *
     * Returns the value associated with the given key, or null if not
     * found.
     **/
    public abstract Object getValue(Object pKey);

    //-------------------------------------
    /**
     *
     * Converts the MapSource to a Map.  If the map is not mutable, this
     * is cached
     **/
    public Map getAsMap() {
        if (mMap != null) {
            return mMap;
        } else {
            Map m = convertToMap();
            if (!isMutable()) {
                mMap = m;
            }
            return m;
        }
    }

    //-------------------------------------
    /**
     *
     * Converts to a Map
     **/
    Map convertToMap() {
        Map ret = new HashMap();
        for (Enumeration e = enumerateKeys(); e.hasMoreElements();) {
            Object key = e.nextElement();
            Object value = getValue(key);
            ret.put(key, value);
        }
        return ret;
    }

    //-------------------------------------
}
