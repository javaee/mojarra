/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2010-2014 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.faces.test.servlet40.faceletCacheFactory;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.faces.context.FacesContext;
import javax.faces.view.facelets.Facelet;
import javax.faces.view.facelets.FaceletCache;

public final class MDSFaceletCache
        extends FaceletCache {

    public MDSFaceletCache(FaceletCache defaultFaceletCache) {
        _defaultFaceletCache = defaultFaceletCache;
    }

    @Override
    public void setCacheFactories(MemberFactory faceletFactory, MemberFactory viewMetadataFaceletFactory) {
        _defaultFaceletCache.setCacheFactories(faceletFactory, viewMetadataFaceletFactory);
    }

    @Override
    public Object getFacelet(URL url)
            throws IOException {
        if (_isMDSUrl(url)) {
            return _getFacelet(url);
        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            context.getExternalContext().getRequestMap().put("message", "" + System.currentTimeMillis());
            return _defaultFaceletCache.getFacelet(url);
        }
    }

    @Override
    public boolean isFaceletCached(URL url) {
        if (_isMDSUrl(url)) {
            // only way to know if it is cached is to try and get one, we might end up creating the facelet,
            //  but then it is fine because we cache it
            return _getFacelet(url) != null;
        } else {
            return _defaultFaceletCache.isFaceletCached(url);
        }
    }

    @Override
    public Object getViewMetadataFacelet(URL url)
            throws IOException {
        if (_isMDSUrl(url)) {
            return _getViewMetadataFacelet(url);
        } else {
            return _defaultFaceletCache.getViewMetadataFacelet(url);
        }
    }

    @Override
    public boolean isViewMetadataFaceletCached(URL url) {
        if (_isMDSUrl(url)) {
            // only way to know if it is cached is to try and get one, we might
            // end up creating the facelet, but then it is fine because we 
            // cache it
            return _getViewMetadataFacelet(url) != null;
        } else {
            return _defaultFaceletCache.isViewMetadataFaceletCached(url);
        }
    }

    private Object _getFacelet(URL url) {
        return _getOrCreateFacelet(url, Key._FACELET_KEY, _getFaceletFactory());
    }

    private Object _getViewMetadataFacelet(URL url) {
        return _getOrCreateFacelet(url, Key._METADATA_FACELET_KEY, _getMetadataFaceletFactory());
    }

    private Object _getOrCreateFacelet(
            URL url,
            Key key,
            ClientObjectFactory faceletFactory) {
        if (null == url) {
            throw new NullPointerException("The supplied url is null");
        }

        throw new IllegalStateException("Unimplemented");
    }

    private ClientObjectFactory _getFaceletFactory() {
        if (_faceletFactory == null) {
            _faceletFactory = new ADFFaceletsClientObjectFactory(getMemberFactory());
        }

        return _faceletFactory;
    }

    private ClientObjectFactory _getMetadataFaceletFactory() {
        if (_metadataFaceletFactory == null) {
            _metadataFaceletFactory = new ADFFaceletsClientObjectFactory(getMetadataMemberFactory());
        }

        return _metadataFaceletFactory;
    }

    private boolean _isMDSUrl(URL url) {
        return false;
    }

    /**
     * Implementation of ClientObjectFactory that MDS will callback. This
     * implementation delegates to appropriate Facelet instance factory.
     */
    private static class ADFFaceletsClientObjectFactory implements ClientObjectFactory {

        public ADFFaceletsClientObjectFactory(MemberFactory<Facelet> faceletFactory) {
            _faceletFactory = faceletFactory;
        }

        public Object createClientObject(Object mdsObject, Object factoryContext) {
            try {
                return _faceletFactory.newInstance((URL) factoryContext);
            } catch (IOException ioe) {
                _LOG.log(Level.FINE, "FACELET_CREATION_ERROR", ioe);
                return null;
            }
        }

        private final MemberFactory<Facelet> _faceletFactory;
    }

    static private enum Key {

        _FACELET_KEY("facelet"),
        _METADATA_FACELET_KEY("metadataFacelet");

        Key(String keyString) {
            this._keyString = keyString;
        }

        public String getKeyString() {
            return _keyString;
        }

        private final String _keyString;
    };

    private volatile ClientObjectFactory _faceletFactory;
    private volatile ClientObjectFactory _metadataFaceletFactory;

    private static interface ClientObjectFactory {

    }

    private final FaceletCache _defaultFaceletCache;

    // defined in oracle.mds.internal.MDSConstants.MDS_PROTOCOL_FACELETS, 
    // temporarily declare it here until we get it in API package from MDS team.
    //
    private static final Logger _LOG = Logger.getLogger("javax.enterprise.resource.webcontainer.jsf.facelets.factory", "com.sun.faces.LogStrings");
}
