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


package javax.faces.application;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import javax.faces.context.FacesContext;

/**
 * <p class="changed_added_2_0">An instance of <code>Resource</code> is a Java
 * object representation of the artifact that is served up in response
 * to a <i>resource request</i> from the client.  Instances of
 * <code>Resource</code> are normally created and initialized via calls
 * to {@link ResourceHandler@createResource}.  See the documentation for
 * {@link ResourceHandler} for more information.</p>
 *
 * <div class="changed_added_2_0">
 * </div>
 *
 * @since 2.0
 */
public abstract class Resource {


    private String contentType;

    private String libraryName;

    private String resourceName;

    // ---------------------------------------------------------- Public Methods


    /**
     * <p class="changed_added_2_0">Return the MIME content-type for this resource.</p>
     * @return the MIME content-type for this resource.
     */
    public String getContentType() {

        return contentType;

    }


    /**
     * <p class="changed_added_2_0">Set the MIME content-type for this resource.
     * The default implementation performs no validation on the
     * argument.</p>
     * @param contentType the MIME content-type for this resource
     */
    public void setContentType(String contentType) {

        this.contentType = contentType;

    }


    /**
     * <p class="changed_added_2_0">Return the libraryName for this resource.  May
     * be <code>null</code>.  The libraryName for a resource is an
     * optional String that indicates membership in a "resource
     * library".  All resources with the same libraryName belong to the
     * same "resource library".  The "resource library" concept allows
     * disambiguating resources that have the resourceName.  See {@link
     * ResourceHandler} for more information.</p>
     *
     * @return Return the libraryName for this resource.  May be null.
     */
    public String getLibraryName() {

        return libraryName;

    }


    /**
     * <p class="changed_added_2_0">Set the libraryName for this resource.</p>
     * @param libraryName the libraryName for this resource.  May be
     * <code>null</code>.
     */
    public void setLibraryName(String libraryName) {

        this.libraryName = libraryName;

    }


    /**
     * <p class="changed_added_2_0">Return the resourceName for this resource.
     * Will never be null.  All <code>Resource</code> instances must
     * have a resourceName.</p>
     * @return Return the resourceName for this resource.  Will never be
     * null.
     */
    public String getResourceName() {

        return resourceName;

    }


    /**
     * <p class="changed_added_2_0">Set the resourceName for this resource.</p>
     * @param resourceName a non-null String.
     *
     * @throws NullPointerException if argument
     * <code>resourceName</code> is null.
     */
    public void setResourceName(String resourceName) {

	if (null == resourceName) {
	    throw new NullPointerException("PENDING_I18N: All resources must have a non-null resourceName.");
	}

        this.resourceName = resourceName;

    }


    /**
     * <p class="changed_added_2_0">If the current request is a resource request,
     * (that is, {@link ResourceHandler#isResourceRequest} returns
     * <code>true</code>), return an <code>InputStream</code> containing
     * the bytes of the resource.  Otherwise, throw an
     * <code>IOException</code>.</p>
     * @return an <code>InputStream</code> containing the bytes of the
     * resource.
     *
     * @throws IOException if the current request is not a resource request.
     */
    public abstract InputStream getInputStream() throws IOException;


    /**
     * RELEASE_PENDING (edburns,roger) - review docs
     * @return
     */
    public abstract URL getURL();


    /**
     * RELEASE_PENDING (eburns,rogerk) - review docs
     * Returns a Map of response headers to be sent with this resource, or an
     * empty map if no response headers are to be sent.
     * @return
     */
    public abstract Map<String, String> getResponseHeaders();


    /**
     * RELEASE_PENDING (eburns,rogerk) - review docs
     * Return the relative URI for this resource following the algorithm specified
     * in ResourceHandler.  This URI, when resolved against the URI of the view
     * request will yield an absolute URI which will return the actual bytes of
     * the resource.
     * @return
     */
    public abstract String getURI();


    /**
     * RELEASE_PENDING (eburns,rogerk) - review docs
     * Call through to getURI() and return the result.
     * @return
     */
    public String toString() {
        return getURI();
    }


    /**
     * RELEASE_PENDING (eburns,rogerk) - review docs
     * Return true if the user agent requesting this resource needs an update.
     * Returns false otherwise.  If false, the caller should send a 304 not
     * modified to the client.
     * @return
     */
    public abstract boolean userAgentNeedsUpdate(FacesContext context);

}
