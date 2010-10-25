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

package javax.faces.view.facelets;

import java.io.IOException;
import java.net.URL;



/**
 * <p class="changed_added_2_1">This API defines the facility by which
 * the Facelets {@link javax.faces.view.ViewDeclarationLanguage}
 * creates and caches instances of Facelets.</p>

 * <p class="changed_added_2_1">The cache handles two different kinds of
 * Facelets: View Facelets and View Metadata Facelets.  The former is
 * the usual Facelet that provides for the construction of a
 * <code>UIComponent</code> tree.  This kind of Facelet is accessed via
 * the {@link #getFacelet} and {@link #isFaceletCached} methods. The
 * latter is a special kind of Facelet that corresponds to {@link
 * javax.faces.view.ViewDeclarationLanguage#getViewMetadata}.  This kind
 * of Facelet is accessed via the {@link #getViewMetadataFacelet} and
 * {@link #isViewMetadataFaceletCached} methods. </p>

 * @since 2.1
 */

public abstract class FaceletCache<V> {

    /**
     * <p class="changed_added_2_1">Factory interface for creating
     * Facelet or View Metadata Facelet instances.</p>
     */
    public interface MemberFactory<V> {

	/**
	 * <p class="changed_added_2_1">Create a Facelet or View
	 * Metadata Facelet (depending on the type of factory this is)
	 * for the argument URL.</p>

	 * @param key the <code>URL</code> that will be used as the key
	 * for the instance being created.
	 *
	 * @throws NullPointerException if argument <code>key</code> is
	 * <code>null</code>.
	 */
        public V newInstance(final URL key) throws IOException;
    }


    /**
     * <p class="changed_added_2_1">Returns a cached Facelet
     * instance. If no instance is available, it will be created using
     * the Facelet {@link MemberFactory} and stored in the cache.</p>

     * @param url <code>URL</code> for the Facelet being retrieved
     * 
     * @throws NullPointerException if argument <code>url</code> is
     * <code>null</code>.

     */

    public abstract V getFacelet(URL url) throws IOException;

    /**

     * <p class="changed_added_2_1">Determines whether a cached Facelet
     * instance exists for this URL.  Returns true if a cached instance
     * exists, false otherwise</p>

     * @param url <code>URL</code> for the Facelet

     * @throws NullPointerException if argument <code>url</code> is
     * <code>null</code>.

     */

    public abstract boolean isFaceletCached(URL url);

    /**
     * <p class="changed_added_2_1">Returns a cached View Metadata
     * Facelet instance. If no instance is available, it will be created
     * using the View Metadata Facelet {@link MemberFactory} and stored
     * in the cache.</p>

     * @param url <code>URL</code> for the View Metadata Facelet being
     * retrieved

     * @throws NullPointerException if argument <code>url</code> is
     * <code>null</code>.

     */

    public abstract V getViewMetadataFacelet(URL url) throws IOException;

    /**
     * <p class="changed_added_2_1">Determines whether a cached View
     * Metadata Facelet instance exists for this URL.  Returns true if a
     * cached instance exists, false otherwise</p>
     * @param url <code>URL</code> for the View Metadata Facelet

     * @throws NullPointerException if argument <code>url</code> is
     * <code>null</code>.
     */

    public abstract boolean isViewMetadataFaceletCached(URL url);


    /**
     * <p class="changed_added_2_1">This must be called by the runtime
     * at startup time, before any requests are serviced, and allows for
     * the <code>FaceletCache</code> implementation to provide the
     * {@link MemberFactory} instances that will be used to create
     * instances of Facelets and View Metadata Facelets. </p>
     *
     * @param faceletFactory the {@link MemberFactory} instance that
     * will be used to create instances of Facelets.

     * @param viewMetadataFaceletFactory the {@link MemberFactory}
     * instance that will be used to create instances of metadata
     * Facelets.

     * @throws NullPointerException if either argument is <code>null</code>

     */
    protected void setMemberFactories(MemberFactory<V> faceletFactory,
            MemberFactory<V> viewMetadataFaceletFactory) {
	if (null == faceletFactory || null == viewMetadataFaceletFactory) {
	    throw new NullPointerException("Neither faceletFactory no viewMetadataFaceletFactory may be null.");
	}

        this.memberFactory = faceletFactory;
        this.viewMetadataMemberFactory = viewMetadataFaceletFactory;
    }

    private MemberFactory<V> memberFactory;
    private MemberFactory<V> viewMetadataMemberFactory;


    /**
     * <p class="changed_added_2_1">Returns the {@link MemberFactory}
     * passed to {@link #setMemberFactories} for the purpose of creating
     * Facelet instance.</p>
     */
    protected MemberFactory<V> getMemberFactory() {
        return memberFactory;
    }

    /**
     * <p class="changed_added_2_1">Returns the {@link MemberFactory}
     * passed to {@link #setMemberFactories} for the purpose of creating
     * View Metadata Facelet instance.</p>
     */
    protected MemberFactory<V> getMetadataMemberFactory() {
        return viewMetadataMemberFactory;
    }

}
