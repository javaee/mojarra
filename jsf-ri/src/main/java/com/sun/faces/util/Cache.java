/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2011 Oracle and/or its affiliates. All rights reserved.
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.FacesException;

    /**
     * A concurrent caching mechanism.
     */
public class Cache<K, V> {

            // Log instance for this class
    private static final Logger LOGGER = FacesLogger.UTIL.getLogger();


        /**
     * Factory interface for creating various cacheable objects.
     */
    public interface Factory<K,V> {

        V newInstance(final K arg) throws InterruptedException;

    } // END Factory


        private ConcurrentMap<K,Future<V>> cache =
              new ConcurrentHashMap<K,Future<V>>();
        private Factory<K,V> factory;


        // -------------------------------------------------------- Constructors


        /**
         * Constructs this cache using the specified <code>Factory</code>.
         * @param factory
         */
        public Cache(Factory<K,V> factory) {

            this.factory = factory;

        }


        // ------------------------------------------------------ Public Methods


        /**
         * If a value isn't associated with the specified key, a new
         * {@link java.util.concurrent.Callable} will be created wrapping the <code>Factory</code>
         * specified via the constructor and passed to a {@link java.util.concurrent.FutureTask}.  This task
         * will be passed to the backing ConcurrentMap.  When {@link java.util.concurrent.FutureTask#get()}
         * is invoked, the Factory will return the new Value which will be cached
         * by the {@link java.util.concurrent.FutureTask}.
         *
         * @param key the key the value is associated with
         * @return the value for the specified key, if any
         */
        public V get(final K key) {

            while (true) {
                Future<V> f = cache.get(key);
                if (f == null) {
                    Callable<V> callable = new Callable<V>() {
                        public V call() throws Exception {
                            return factory.newInstance(key);
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
                    f = cache.putIfAbsent(key, ft);
                    if (f == null) {
                        f = ft;
                        ft.run();
                    }
                }
                try {
                    return f.get();
                } catch (CancellationException ce) {
                    if (LOGGER.isLoggable(Level.FINEST)) {
                        LOGGER.log(Level.FINEST,
                                   ce.toString(),
                                   ce);
                    }
                    cache.remove(key);
                } catch (InterruptedException ie) {
                    if (LOGGER.isLoggable(Level.FINEST)) {
                        LOGGER.log(Level.FINEST,
                                   ie.toString(),
                                   ie);
                    }
                    cache.remove(key);
                } catch (ExecutionException ee) {
                    throw new FacesException(ee);
                }
            }

        }

        public V remove(final K key) {
            Future<V> t = cache.remove(key);
            V result = null;

            if (null != t) {
                try {
                    result = t.get();
                } catch (CancellationException ce) {
                    if (LOGGER.isLoggable(Level.FINEST)) {
                        LOGGER.log(Level.FINEST,
                                ce.toString(),
                                ce);
                    }
                } catch (InterruptedException ie) {
                    if (LOGGER.isLoggable(Level.FINEST)) {
                        LOGGER.log(Level.FINEST,
                                ie.toString(),
                                ie);
                    }
                } catch (ExecutionException ee) {
                    throw new FacesException(ee);
                }
            }

            return result;
    }

    } // END Cache
