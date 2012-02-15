/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2011 Sun Microsystems, Inc. All rights reserved.
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

package com.sun.faces.util;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public abstract class MetadataWrapperMap<K, V> implements Map<K, V> {

    public MetadataWrapperMap(Map<K, V> toWrap) {
        this.wrapped = toWrap;
        metadata = new ConcurrentHashMap<K, Map<Object, Object>>();
    }

    protected Map<K, Map<Object, Object>> getMetadata() {
        return metadata;
    }

    private Map<K, V> wrapped;
    private Map<K, Map<Object, Object>> metadata;

    public void clear() {
	this.wrapped.clear();
    }

    @SuppressWarnings(value="")
    public boolean containsKey(Object key) {
	return this.wrapped.containsKey(key);
    }

    @SuppressWarnings(value="")
    public boolean containsValue(Object value) {
	return this.wrapped.containsValue(value);
    }

    public Set<Map.Entry<K,V>> entrySet() {
	return this.wrapped.entrySet();
    }

    @SuppressWarnings(value="")
    public V get(Object key) {
	return this.wrapped.get(key);
    }


    public boolean isEmpty() {
	return this.wrapped.isEmpty();
    }

    public Set<K> keySet() {
	return this.wrapped.keySet();
    }

    public V put(K key, V value) {
        this.onPut(key, value);
        return this.wrapped.put(key, value);
    }

    protected abstract V onPut(K key, V value);

    public void putAll(Map m) {
	this.wrapped.putAll(m);
    }

    @SuppressWarnings(value="")
    public V remove(Object key) {
	return this.wrapped.remove(key);
    }

    public int size() {
	return this.wrapped.size();
    }

    public Collection<V> values() {
	return this.wrapped.values();
    }


}
