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

import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;
import java.util.Collection;
import java.util.Iterator;
import java.util.AbstractSet;
import java.util.AbstractCollection;
import java.util.Enumeration;

/**
 * <p>
 * This is the base Map for all Map instances required by @{link ExternalContext}.
 * </p>
 */
abstract class BaseContextMap<V> extends AbstractMap<String,V> {

    private Set<Map.Entry<String, V>> entrySet;
    private Set<String> keySet;
    private Collection<V> values;


    // -------------------------------------------------------- Methods from Map


    // Supported by maps if overridden
    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }


    // Supported by maps if overridden
    @Override
    public void putAll(Map t) {
        throw new UnsupportedOperationException();
    }


    public Set<Map.Entry<String, V>> entrySet() {
        if (entrySet == null) {
            entrySet = new EntrySet();
        }

        return entrySet;
    }


    @Override
    public Set<String> keySet() {
        if (keySet == null) {
            keySet = new KeySet();
        }

        return keySet;
    }


    @Override
    public Collection<V> values() {
        if (values == null) {
            values = new ValueCollection();
        }

        return values;
    }


    // Supported by maps if overridden
    @Override
    public V remove(Object key) {
        throw new UnsupportedOperationException();
    }


    // ------------------------------------------------------- Protected Methods


    protected boolean removeKey(Object key) {
        return (this.remove(key) != null);
    }


    protected boolean removeValue(Object value) {
        boolean valueRemoved = false;
        if (value == null) {
            return false;
        }
        if (containsValue(value)) {
            for (Iterator i = entrySet().iterator(); i.hasNext(); ) {
                Map.Entry e = (Map.Entry) i.next();
                if (value.equals(e.getValue())) {
                    valueRemoved = (remove(e.getKey()) != null);
                }
            }
        }
        return valueRemoved;
    }


    protected abstract Iterator<Map.Entry<String, V>> getEntryIterator();
    protected abstract Iterator<String> getKeyIterator();
    protected abstract Iterator<V> getValueIterator();


    // ----------------------------------------------------------- Inner Classes


    abstract class BaseSet<E> extends AbstractSet<E> {

        public int size() {
            int size = 0;
            for (Iterator<E> i = iterator(); i.hasNext(); size++) {
                i.next();
            }
            return size;
        }

    }


    class EntrySet extends BaseSet<Map.Entry<String, V>> {

        public Iterator<Map.Entry<String, V>> iterator() {
            return getEntryIterator();
        }

        @Override
        public boolean remove(Object o) {
            return o instanceof Map.Entry
                   && removeKey(((Map.Entry) o).getKey());
        }

    }


    class KeySet extends BaseSet<String> {

        public Iterator<String> iterator() {
            return getKeyIterator();
        }


        @Override
        public boolean contains(Object o) {
            return BaseContextMap.this.containsKey(o);
        }

        @Override
        public boolean remove(Object o) {
            return o instanceof String && removeKey(o);
        }
    }


    class ValueCollection extends AbstractCollection<V> {

        public int size() {
            int size = 0;
            for (Iterator i = iterator(); i.hasNext(); size++) {
                i.next();
            }
            return size;
        }
        
        public Iterator<V> iterator() {
            return getValueIterator();
        }

        @Override
        public boolean remove(Object o) {
            return removeValue(o);
        }
    }


    abstract class BaseIterator<E> implements Iterator<E> {

        protected Enumeration e;
        protected String currentKey;
        protected boolean removeCalled = false;

        BaseIterator(Enumeration e) {
            this.e = e;
        }

        public boolean hasNext() {
            return e.hasMoreElements();
        }

        public String nextKey() {
            removeCalled = false;
            currentKey = (String) e.nextElement();
            return currentKey;
        }
    }


    class EntryIterator extends BaseIterator<Map.Entry<String,V>> {

        EntryIterator(Enumeration e) {
            super(e);
        }

        public void remove() {
            if (currentKey != null && !removeCalled) {
                removeCalled = true;
                removeKey(currentKey);
            } else {
                throw new IllegalStateException();
            }
        }

        public Map.Entry<String,V> next() {
            nextKey();
            return new Entry<V>(currentKey, get(currentKey));
        }
    }


    class KeyIterator extends BaseIterator<String> {

        KeyIterator(Enumeration e) {
            super(e);
        }

        public void remove() {
            if (currentKey != null && !removeCalled) {
                removeCalled = true;
                removeKey(currentKey);
            } else {
                throw new IllegalStateException();
            }
        }

        public String next() {
            return nextKey();
        }
    }


    class ValueIterator extends BaseIterator<V> {

        ValueIterator(Enumeration e) {
            super(e);
        }

        public void remove() {
            if (currentKey != null && !removeCalled) {
                removeCalled = true;
                removeValue(get(currentKey));
            } else {
                throw new IllegalStateException();
            }
        }

        public V next() {
            nextKey();
            return get(currentKey);
        }
    }


    static class Entry<V> implements Map.Entry<String,V> {

        // immutable Entry
        private final String key;
        private final V value;


        Entry(String key, V value) {
            this.key = key;
            this.value = value;
        }


        public String getKey() {
            return key;
        }


        public V getValue() {
            return value;
        }


        // No support of setting the value
        public V setValue(V value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int hashCode() {
            return ((key == null ? 0 : key.hashCode()) ^
                (value == null ? 0 : value.hashCode()));
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof Map.Entry)) {
                return false;
            }

            Map.Entry input = (Map.Entry) obj;
            Object inputKey = input.getKey();
            Object inputValue = input.getValue();

            if (inputKey == key ||
                (inputKey != null && inputKey.equals(key))) {
                if (inputValue == value ||
                    (inputValue != null && inputValue.equals(value))) {
                    return true;
                }
            }
            return false;
        }
    }
    
}
