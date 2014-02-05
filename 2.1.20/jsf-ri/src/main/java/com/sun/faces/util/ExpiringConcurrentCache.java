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

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.FacesException;

/**
 * This class implements an abstract ConcurrentCache with  objects in the cache potentially expiring.
 * Only non-expired objects will be returned from the cache or considered to be contained in the cache
 * The cache is self-managing, so no remove() method is defined
 */
public final class ExpiringConcurrentCache<K, V> extends ConcurrentCache<K, V> {
    
    /**
     * Interface for checking whether a cached object expired
     */
    public interface ExpiryChecker<K, V>{
        /**
         * Checks whether a cached object expired
         * @param key cache key
         * @param value cached value
         * @return true if the value expired and should be removed from the cache, false otherwise
         */
        public boolean isExpired(K key, V value);
    }
    
    /**
     * Public constructor.
     * @param f used to create new instances of objects that are not already available
     * @param checker used to check whether an object in the cache has expired
     */
    public ExpiringConcurrentCache(Factory<K, V> f, ExpiryChecker<K, V> checker) {
        super(f);
        _checker = checker;
    }
    
    @Override
    public V get(final K key) throws ExecutionException {
        // This method uses a design pattern from "Java concurrency in practice".
        // The pattern ensures that only one thread gets to create an object  missing in the cache,
        // while the all the other threads tring to get it are waiting
        while (true) {
            boolean newlyCached = false;
            
            Future<V> f = _cache.get(key);
            if (f == null) {
                Callable<V> callable = new Callable<V>() {
                    public V call() throws Exception {
                        return getFactory().newInstance(key);
                    }
                };
                FutureTask<V> ft = new FutureTask<V>(callable);
                // here is the real beauty of the concurrent utilities.
                // 1.  putIfAbsent() is atomic
                // 2.  putIfAbsent() will return the value already associated
                //     with the specified key
                // So, if multiple threads make it to this point
                // they will all be calling f.get() on the same
                // FutureTask instance, so this guarantees that the instances
                // that the invoked Callable will return will be created once
                f = _cache.putIfAbsent(key, ft);
                if (f == null) {
                    f = ft;
                    ft.run();
                    newlyCached = true;
                }
            }
            try {
                V obj = f.get();
                if (!newlyCached && _getExpiryChecker().isExpired(key, obj)) {
                    
                    // Note that we are using both key and value in remove() call to ensure
                    // that we are not removing the Future added after expiry check by a different thread
                    _cache.remove(key, f);
                }
                else {
                    return obj;
                }
            } catch (CancellationException ce) {
                if (_LOGGER.isLoggable(Level.SEVERE)) {
                    _LOGGER.log(Level.SEVERE,
                               ce.toString(),
                               ce);
                }
                _cache.remove(key, f);
            } catch (ExecutionException ee) {
                _cache.remove(key, f);
                throw ee;
            } catch (InterruptedException ie) {                 
                throw new FacesException(ie); 
                
            }
        }  
    }
    
    @Override
    public boolean containsKey(final K key) {
        
        Future<V> f = _cache.get(key);

        if (f != null && f.isDone() && !f.isCancelled()) {

            try {
                // Call get() with a 0 timeout to avoid any wait
                V obj = f.get(0, TimeUnit.MILLISECONDS);
                if (_getExpiryChecker().isExpired(key, obj)) {

                    // Note that we are using both key and value in remove() call to ensure
                    // that we are not removing the Future added after expiry check by a different thread
                    _cache.remove(key, f);
                } else {
                    
                    return true;
                }
            } catch (TimeoutException ce) {
                    // do nothing. This just indicates that the object is not yet ready
                } catch (CancellationException ce) {
                if (_LOGGER.isLoggable(Level.SEVERE)) {
                    _LOGGER.log(Level.SEVERE, ce.toString(), ce);
                }
            } catch (InterruptedException ie) {
                throw new FacesException(ie);

            } catch (ExecutionException ee) {
                // Do nothing - the FutureTask will be removed by the thread that called get() on this class
            }
        }
        
        return false;
    }

    


    private ExpiryChecker<K, V> _getExpiryChecker() {
        return _checker;
    }
    
    private final ExpiryChecker<K, V> _checker;
    private final ConcurrentMap<K, Future<V>> _cache = new ConcurrentHashMap<K, Future<V>>();
    
    private static final Logger _LOGGER = FacesLogger.UTIL.getLogger();
}
