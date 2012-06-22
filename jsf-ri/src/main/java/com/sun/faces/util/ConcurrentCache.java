/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2010 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.faces.util;

import java.util.concurrent.ExecutionException;


/**
 * Defines a concurrent cache with a factory for creating new object instances.
 * 
 * This (combined with ExpiringConcurrentCache) offers functionality similar
 * to com.sun.faces.util.Cache.  Two differences:
 *
 * 1. Cache is concrete/assumes a particular implementation.  ConcurrentCache
 *    is abstract/allows subclasses to provide the implementation.  This
 *    facilitates alternative implementations, such as DefaultFaceletCache's
 *    NoCache.
 * 2. ConcurrentCache does not provide remove() as part of its contract, since
 *    remove behavior may be subclass-specific.  For example,
 *    ExpiringConcurentCache automatically removes items by checking for
 *    expiration rather than requiring manual removes.
 *
 * We should consider consolidating Cache and ConcurrentCache + 
 * ExpiringConcurrentCache into a single class hierarchy so that we
 * do not need to duplicate the JCIP scalable result cache code.
 */
public abstract class ConcurrentCache<K, V> {
  
    /**
     * Factory interface for creating various cacheable objects.
     */
    public interface Factory<K,V> {
        public V newInstance(final K arg) throws Exception;
    }
    
    /**
     * Constructs this cache using the specified <code>Factory</code>.
     * 
     * @param f
     */
    public ConcurrentCache(Factory<K,V> f) {
        _f = f;
    }
    
    /**
     * Retrieves a value for the specified key.
     * If the value is not already present in the cache, a new instance will
     * be allocated using the <code>Factory</code> interface
     *
     * @param key the key the value is associated with
     * @return the value for the specified key
     */
    public abstract V get(final K key) throws ExecutionException;
    
    /**
     * Tests whether the cache contains a value for the specified key
     * @param key key to test
     * @return true if the value for the specified key is already cached, false otherwise
     */
    public abstract boolean containsKey(final K key);
    
    /**
     * Retrieves a <code>Factory</code> instance aasociated with this cache
     * @return <code>Factory</code> instance
     */
    protected final Factory<K, V> getFactory() {
        return _f;
    }
    
    
    private final Factory<K, V> _f;
} 
