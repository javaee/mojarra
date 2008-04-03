/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A very simple, generic, LRU cache implementation.
 * It should be noted that the size of the cache is not strictly enforced,
 * but will be reduced to the specified maximum, if necessary, when the
 * specified clean interval is reached.
 */
public class ConcurrentLRUCache<K,V> {

    private static final Logger LOGGER = FacesLogger.APPLICATION.getLogger();

    /**
     * <p>The default interval that the background thread responsible for
     * pruning old entries from the cache will be run (two minutes).</p>
     */
    public static final int DEFAULT_CLEAN_INTERVAL = 2 * 60000;

    /**
     * <p>The default maximum capacity of this cache (20 items).</p>
     */
    public static final int DEFAULT_MAX_CAPACITY = 20;

    /**
     * The comparitor used by the cleaning thread to sort the entries
     * from oldest to newest.
     */
    private ValueWrapperComparator comparator = new ValueWrapperComparator();

    /* The cache */
    private ConcurrentMap<K,ValueWrapper<V>> cache;

    /*
     * The rough maximum capacity for the cache.  The cache may actually grow
     * beyond this size, but when the background thread is run, the cache will
     * be pruned to this size.
     */
    private int maxCapacity;

    /* cache hits */
    private int cacheHits;

    /* cache misses */
    private int cacheMisses;

    /* cache name */
    private String cacheName;

    /* Timer used to periodically prune the cache */
    java.util.Timer timer;


    // --------------------------------------------------------- Constructors


    /**
     * Constructs a new <code>ConcurrentLRUCache</code> using
     * {@link #DEFAULT_MAX_CAPACITY} and {@link #DEFAULT_CLEAN_INTERVAL}
     * for the capacity and cleaning intervals.
     */
    public ConcurrentLRUCache(String cacheName) {
        this(DEFAULT_MAX_CAPACITY, DEFAULT_CLEAN_INTERVAL, cacheName);
    }


    /**
     * Constructs a new <code>ConcurrentLRUCache</code> using the
     * specified defaults.
     *
     * @param maxCapacity the max capacity of the cache
     * @param cleanInterval the interval, in milliseconds, that the background
     *  thread will be run to prune the cache back to the max capacity
     */
    public ConcurrentLRUCache(int maxCapacity, int cleanInterval, String cacheName) {
        this.maxCapacity = maxCapacity;
        this.cacheName = cacheName;
        cache = new ConcurrentHashMap<K,ValueWrapper<V>>(maxCapacity);
        timer = new java.util.Timer(true);
        timer.schedule(new CleanerTask(), cleanInterval, cleanInterval);
    }


    // -------------------------------------------------------- Public Methods


    /**
     * Adds the specified key/value pair, if it doesn't already exist, to the cache.
     * @param key the key
     * @param value the value
     */
    public void add(K key, V value) {

        if (!cache.containsKey(key)) {
            cache.put(key, new ValueWrapper<V>(value));
        }

    }


    /**
     * @param key the key
     * @return the cached value, if any
     */
    public V get(K key) {

        ValueWrapper<V> wrapper = cache.get(key);
        V value = null;
        if (wrapper != null) {
            wrapper.touch();
            value = wrapper.get();
            cacheHits++;
        } else {
            cacheMisses++;
        }

        return value;

    }


    /**
     * Used for testing
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        ConcurrentLRUCache<String,String> cache = new ConcurrentLRUCache<String,String>(ConcurrentLRUCache.DEFAULT_MAX_CAPACITY, 1, "testCache");
        for (int i = 0; i < 15; i++) {
            System.out.println("adding entry" + i);
            cache.add("key" + i, "value" + i);
        }
        try {
            Thread.sleep(2 * 60000);
        } catch (InterruptedException ie) {
            System.out.println("Interrupted - exiting");
            System.exit(1);
        }

        for (int i = 15; i < 30; i++) {
            System.out.println("adding entry" + i);
            cache.add("key" + i, "value" + i);
        }

        cache.add("key8", "value8");
        cache.get("key8");
        cache.get("key7");

        try {
            Thread.sleep(2 * 60000);
        } catch (InterruptedException ie) {
            System.out.println("Interrupted - exiting");
            System.exit(1);
        }
    }


    // -------------------------------------------------------------- Inner Classes


    /**
     * <p>This task is responsible for pruining entrys from the <code>cache</code> when
     * the size of the cache exceeds the specified maximum.</p>
     */
     private class CleanerTask extends TimerTask {

        public void run() {
            Set<Map.Entry<K,ValueWrapper<V>>> values = cache.entrySet();
            List<Map.Entry<K,ValueWrapper<V>>> list = new ArrayList<Map.Entry<K,ValueWrapper<V>>>();
            list.addAll(values);
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE,
                           "=================================================");
                LOGGER.log(Level.FINE, "Cache name: {0}", cacheName);
                LOGGER.log(Level.FINE, "Max cache size: {0}", maxCapacity);
                LOGGER.log(Level.FINE, "Current cache size: {0}" + list.size());
                LOGGER.log(Level.FINE, "Cache hits: {0}", cacheHits);
                LOGGER.log(Level.FINE, "Cache misses: {0}", cacheMisses);
                LOGGER.log(Level.FINE,
                           "=================================================");
            }
            if (list.size() < maxCapacity) {
                return;
            }
            Collections.sort(list, comparator);
            List<Map.Entry<K,ValueWrapper<V>>> toRemove = list.subList(maxCapacity, list.size());
            for (Map.Entry<K,ValueWrapper<V>> entry : toRemove) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE, "Removing cache item: {0}", entry.getKey());
                }
                cache.remove(entry.getKey());
            }
        }

    }


    /**
     * A simple data wrapper that provides a timestamp, with methods
     * to obtain the wrapped data and update the timestamp.
     * TODO should these be resused?
     */
    private static class ValueWrapper<V> {

        private long timestamp;
        private V value;

        ValueWrapper(V value) {
            this.value = value;
            timestamp = System.nanoTime();
        }

        V get() {
            return value;
        }

        long getTimestamp() {
            return timestamp;
        }

        void touch() {
            timestamp = System.nanoTime();
        }

    }


    /**
     * A <code>Comparator</code> to sort the values from oldest to newest.
     */
    private class ValueWrapperComparator implements Comparator<Map.Entry<K,ValueWrapper<V>>> {

        public int compare(Map.Entry<K,ValueWrapper<V>> vw1, Map.Entry<K,ValueWrapper<V>> vw2) {
            if (vw1.getValue().getTimestamp() > vw2.getValue().getTimestamp()) {
                return -1;
            } else if (vw1.getValue().getTimestamp() == vw2.getValue().getTimestamp()) {
                return 0;
            } else {
                return 1;
            }
        }
    }

}
