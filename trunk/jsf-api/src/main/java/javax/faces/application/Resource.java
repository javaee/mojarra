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

package javax.faces.application;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import javax.faces.context.FacesContext;

/**
 * <p class="changed_added_2_0"><span
 * class="changed_modified_2_1">An</span> instance of
 * <code>Resource</code> is a Java object representation of the artifact
 * that is served up in response to a <i>resource request</i> from the
 * client.  Instances of <code>Resource</code> are normally created and
 * initialized via calls to {@link ResourceHandler#createResource}.  See
 * the documentation for {@link ResourceHandler} for more
 * information.</p>
 *
 * <div class="changed_added_2_0">
 * </div>
 *
 * @since 2.0
 */
public abstract class Resource {
    
    /**
     * <p class="changed_added_2_0">This constant is used as the key in the 
     * component attribute map of a composite component to associate 
     * the component with its <code>Resource</code> instance.  The
     * value for this key is the actual <code>Resource</code> instance.</p>
     * 
     */
    public static final String COMPONENT_RESOURCE_KEY = 
            "javax.faces.application.Resource.ComponentResource";


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

     * <p class="changed_added_2_0">Set the MIME content-type for this
     * resource.  The default implementation performs no validation on
     * the argument.</p>
     * @param contentType the MIME content-type for this resource.  The
     * default implementation must accept <code>null</code> as a
     * parameter.  
     */
    public void setContentType(String contentType) {

        this.contentType = contentType;

    }


    /**
     * <p class="changed_added_2_0">Return the libraryName for this
     * resource.  May be <code>null</code>.  The libraryName for a
     * resource is an optional String that indicates membership in a
     * "resource library".  All resources with the same libraryName
     * belong to the same "resource library".  The "resource library"
     * concept allows disambiguating resources that have the same
     * resourceName.  See {@link ResourceHandler} for more
     * information.</p>
     *
     * @return Return the libraryName for this resource.  May be
     * <code>null</code>.
     */
    public String getLibraryName() {

        return libraryName;

    }


    /**
     * <p class="changed_added_2_0">Set the libraryName for this resource.</p>
     * @param libraryName the libraryName for this resource.  The
     * default implementation must accept <code>null</code> for the
     * <em>libraryName</em>.
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
     * <p class="changed_added_2_0"><span
     * class="changed_modified_2_1">If</span> the current request is a resource
     * request, (that is, {@link ResourceHandler#isResourceRequest}
     * returns <code>true</code>), return an <code>InputStream</code>
     * containing the bytes of the resource.  Otherwise, throw an
     * <code>IOException</code>.</p>
     * @return an <code>InputStream</code> containing the bytes of the
     * resource.</p>
     *
     * <p class="changed_modified_2_1">Any EL expressions present in the
     * resource must be evaluated before serving the bytes of the
     * resource.  Note that due to browser and server caching, EL
     * expressions in a resource file will generally only be evaluated
     * once, when the resource is first served up.  Therefore, using EL
     * expressions that refer to per-request data is not advisable since
     * this data can become stale.</p>
     *
     * @throws IOException if the current request is not a resource request.
     */
    public abstract InputStream getInputStream() throws IOException;


    /**
     * <p class="changed_added_2_0">Returns a mutable
     * <code>Map&lt;String, String&gt;</code> whose entries will be sent
     * as response headers during {@link
     * ResourceHandler#handleResourceRequest}.  The entries in this map
     * must not persist beyond the scope of a single request.  Any
     * modifications made to the map after the resource has been served
     * will be ignored by the run-time.</p>
     *
     * @return a mutable <code>Map&lt;String, String&gt;</code> of
     * headers that will be included with the response.
     */
    public abstract Map<String, String> getResponseHeaders();


    /**
     * <p class="changed_added_2_0">Return a path to this resource such
     * that, when the browser resolves it against the base URI for the
     * view that includes the resource, and issues a GET request to the
     * resultant fully qualified URL, the bytes of the resource are
     * returned in response.</p>
     *
     * <div class="changed_added_2_0">
     *
     * <p>The default implementation must
     * implement the following algorithm.  For discussion, the return
     * result from this method will be called <em>result</em>.</p>
     *
     * <ul>
     *
     * <li><p>Get the context-root for this web application, not ending
     * in slash.  For discussion this will be caled
     * <em>contextRoot</em>.</p></li>
     *
     * <li><p>Discover if the <code>FacesServlet</code> is prefix or
     * extension mapped, and the value of the mapping (including the
     * leading '.'  in the case of extension mapping).  For discussion,
     * this will be <em>facesServletMapping</em>.</p>
     *
     * <p>If prefix mapped, <em>result</em> must be</p>
     *
     * <ul><p><code>result = <em>contextRoot</em> + '/' +
     * <em>facesServletMapping</em> + {@link
     * ResourceHandler#RESOURCE_IDENTIFIER} + '/' + {@link
     * #getResourceName}</code></p></ul>
     *
     * <p>If extension mapped, <em>result</em> must be</p>
     *
     * <ul><p><code>result = <em>contextRoot</em> + {@link
     * ResourceHandler#RESOURCE_IDENTIFIER} + {@link #getResourceName} +
     * <em>facesServletMapping</em></code></p></ul>
     *
     * </li>
     *
     * <li><p>If {@link #getLibraryName} returns non-<code>null</code>,
     * build up a string, called <em>resourceMetaData</em> for
     * discussion, as follows:</p>
     *
     * <ul>
     *
     * <p><code>resourceMetaData = "?ln=" + {@link
     * #getLibraryName}</code></p>
     *
     * </ul>
     *
     * <p>Append <em>resourceMetaData</em> to <em>result</em>.</p>
     *
     * </li>
     *
     * <li><p>Make it portlet safe by passing the result through {@link
     * ViewHandler#getResourceURL}.</p></li>
     *
     * </ul>
     *
     * </div>
     *
     * @return the path to this resource, intended to be included in the
     * encoded view that is sent to the browser when sending a faces
     * response.
     */
    public abstract String getRequestPath();


    /**
     * <p class="changed_added_2_0">Return an actual <code>URL</code>
     * instance that refers to this resource instance.</p>
     *
     * @return Return an actual <code>URL</code> instance that refers to
     * this resource instance.
     */
    public abstract URL getURL();


    /**
     * <p class="changed_added_2_0">Call through to {@link
     * #getRequestPath} and return the result.</p>
     *
     * @return Call through to {@link #getRequestPath} and return the
     * result.
     */
    public String toString() {
        return getRequestPath();
    }


    /**
     * <p class="changed_added_2_0">Return <code>true</code> if the
     * user-agent requesting this resource needs an update.  Returns
     * <code>false</code> otherwise.  
     *
     * @return <code>true</code> or <code>false</code> depending on
     * whether or not the user-agent needs an update of this resource.
     */
    public abstract boolean userAgentNeedsUpdate(FacesContext context);

}
