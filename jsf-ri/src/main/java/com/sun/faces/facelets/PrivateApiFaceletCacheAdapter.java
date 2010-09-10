
/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2010 Sun Microsystems, Inc. All rights reserved.
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

package com.sun.faces.facelets;

import com.sun.faces.facelets.FaceletCache.InstanceFactory;
import java.io.IOException;
import java.net.URL;
import javax.faces.view.facelets.FaceletCache;
import javax.faces.view.facelets.FaceletCache.MemberFactory;

public class PrivateApiFaceletCacheAdapter<V> extends FaceletCache<V> {

    private com.sun.faces.facelets.FaceletCache<V> privateApi;

    public PrivateApiFaceletCacheAdapter(com.sun.faces.facelets.FaceletCache<V> privateApi) {
        this.privateApi = privateApi;
    }

    @Override
    public V getFacelet(URL url) throws IOException {
        return privateApi.getFacelet(url);
    }

    @Override
    public V getViewMetadataFacelet(URL url) throws IOException {
        return privateApi.getMetadataFacelet(url);
    }

    @Override
    public boolean isFaceletCached(URL url) {
        return privateApi.isFaceletCached(url);
    }

    @Override
    public boolean isViewMetadataFaceletCached(URL url) {
        return privateApi.isMetadataFaceletCached(url);
    }

    private MemberFactory<V> memberFactory;
    private MemberFactory<V> metadataMemberFactory;

    @Override
    public void setMemberFactories(final MemberFactory<V> faceletFactory, 
            final MemberFactory<V> viewMetadataFaceletFactory) {
        InstanceFactory<V> instanceFactory = new InstanceFactory<V>() {

            public V newInstance(URL key) throws IOException {
                return faceletFactory.newInstance(key);
            }

        };
        InstanceFactory<V> metadataInstanceFactory = new InstanceFactory<V>() {

            public V newInstance(URL key) throws IOException {
                return viewMetadataFaceletFactory.newInstance(key);
            }

        };

        privateApi.init(instanceFactory, metadataInstanceFactory);
    }

    @Override
    public MemberFactory<V> getMemberFactory() {
        return memberFactory;
    }

    @Override
    public MemberFactory<V> getMetadataMemberFactory() {
        return metadataMemberFactory;
    }



}
