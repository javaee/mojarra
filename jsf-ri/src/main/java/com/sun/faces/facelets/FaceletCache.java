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

package com.sun.faces.facelets;

import java.io.IOException;

import java.net.URL;

@Deprecated
public abstract class FaceletCache<V> {
    
    /**
     * Factory interface for creating Facelets.
     */
    @Deprecated
    public interface InstanceFactory<V> {
        public V newInstance(final URL key) throws IOException;
    }
    
    /**
     * Retrieves a cached Facelet
     * @param url URL for the Facelet being retrieved
     * @return cached Facelet instance, If no instance is available,
     * it will be created using the Facelet InstanceFactory and stored in the cache
     */
    public abstract V getFacelet(URL url) throws IOException;

    /**
     * Determines whether a cached Facelet instance exists for this URL
     * @param url URL for the Facelet
     * @return true if a cached instance exists, false otherwise
     */
    public abstract boolean isFaceletCached(URL url);

    /**
     * Retrieves a cached Metadata Facelet
     * @param url URL for the Metadata Facelet being retrieved
     * @return cached Metadata Facelet instance, If no instance is available,
     * it will be created using the Metadata Facelet InstanceFactory and stored in the cache
     */
    public abstract V getMetadataFacelet(URL url) throws IOException;

    /**
     * Determines whether a cached Metadata Facelet instance exists for this URL
     * @param url URL for the Metadata Facelet
     * @return true if a cached instance exists, false otherwise
     */
    public abstract boolean isMetadataFaceletCached(URL url);
    
    /**
     * Initializes this cache instance.
     * @param faceletFactory <code>InstanceFactory</code> for creating Facelet instances
     * @param metafaceletFactory <code>InstanceFactory</code> for creating Metadata Facelet instances
     */
    public final void init(InstanceFactory<V> faceletFactory, InstanceFactory<V> metafaceletFactory) {
        _faceletFactory = faceletFactory;
        _metafaceletFactory = metafaceletFactory;
    }
    
    /**
     * Retrieves InstanceFactory for creating Facelets
     * @return factory for creating Facelets
     */
    protected final InstanceFactory<V> getFaceletInstanceFactory() {
        return _faceletFactory;
    }
    
    /**
     * Retrieves InstanceFactory for creating Metadata Facelets
     * @return factory for creating MetadataFacelets
     */
    protected final InstanceFactory<V> getMetadataFaceletInstanceFactory() {
        return _metafaceletFactory;
    }

    private InstanceFactory<V> _faceletFactory;
    private InstanceFactory<V> _metafaceletFactory;
}
