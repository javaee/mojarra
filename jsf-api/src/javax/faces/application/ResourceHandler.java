/*
 * ResourceHandler.java
 *
 * Created on October 16, 2007, 12:29 PM
 */

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

import javax.faces.context.FacesContext;


/**
 * <p class="changed_added_2_0"><strong>ResourceHandler</strong> is the
 * run-time API by which {@link javax.faces.component.UIComponent} and
 * {@link javax.faces.render.Renderer} instances can reference {@link
 * Resource} instances.</p>
 *
 * <div class="changed_added_2_0">
 *
 * <p class="javadocSection">Terminology and Purpose</p>
 *
 * <ul>
 *
 * <p>Web based distributed applications generally follow a pattern
 * where the user-agent sends several requests to the server with the
 * end goal of displaying a user interface.  Generally, the response to
 * the first request tells the user-agent what additional requests are
 * needed to achieve that goal.  In JSF, the first request and
 * post-backs to the first request are called <em>view requests</em>.
 * Within the scope of a view request, additional requests for scripts,
 * images, stylesheets, and other artifacts, are called <em>resource
 * requests</em>.  The <code>ResourceHandler</code> facility defines how
 * JSF handles resource requests.  Specifically, it defines how
 * resources must be packaged, how the run-time loads these packaged
 * resources and converts them into correctly localized Java object
 * {@link Resource} instances, and how the run-time serves them up to
 * the client in response to resource requests.</p>
 *
 * </ul>
 *
 * <p class="javadocSection">Packaging Resources</p>
 *
 * <ul>
 *
 * <p><code>ResourceHandler</code> defines a path based packaging
 * convention for resources.  The default implementation of
 * <code>ResourceHandle</code> must support packaging resources in the
 * classpath or in the web application root, according to the following
 * specification.  Other implementations of <code>ResourceHandler</code>
 * are free to package resources however they like. For the default
 * implementation, if a resource is packaged in the classpath, it must
 * be under the JAR entry name:</p>
 *
 * <ul><p><code>META-INF/resources/&lt;resourceIdentifier&gt;</code></p></ul>
 *
 * <p>If packaged under the web app root, they must be under the path:</p>
 *
 * <ul><p><code>resources/&lt;resourceIdentifier&gt;</code></p></ul>
 *
 * <p>relative to the web app root.</p>
 *
 * <p>&lt;resourceIdentifier&gt; consists of several segments, specified
 * as follows.</p>
 *
 * <p><ul><code>
 * [localePrefix/][libraryName/][libraryVersion/]resourceName[/resourceVersion]</code></ul>
 * </p>
 *
 * <p>The run-time must enforce the following rules to consider a
 * &lt;resourceIdentifier&gt;s valid.  A &lt;resourceIdentifier&gt; that
 * does not follow these rules must not be considered valid and must be
 * ignored silently.</p>
 *
 * <ul>
 *
 * <li><p>Segments in square brackets [] are optional.</p></li>
 *
 * <li><p>The segments must appear in the order shown above.</p></li>
 *
 * <li><p>If libraryVersion is present, it must be preceded by
 * libraryName</p></li>
 *
 * <li><p>If resourceVersion is present, it must be preceded by
 * resourceName</p></li>
 *
 * <li><p>There must be a '/' between adjacent segments in a
 * &lt;resourceIdentifier&gt;</p></li>
 *
 * <li><p>If libraryVersion or resourceVersion are present, both must be
 * a '.'  separated list of integers, neither starting nor ending with
 * '.'</p></li>
 *
 * </ul>
 *
 * <p>The <a name="validResourceIdentifiers">following</a> examples
 * illustrate the nine valid combinations of the above resource
 * identifier segments.</p>
 *
 * <table border="1">
 *
 * <thead>
 * <tr>
 * <th>localePrefix [optional]</th>
 * <th>libraryName [optional]</th>
 * <th>libraryVersion [optional]</th>
 * <th>resourceName [required]</th>
 * <th>resourceVersion [required]</th>
 * <th>Description</th>
 * <th>actual resourceIdentifier</th>
 * </tr>
 * </thead>
 * <tbody>
 * <tr>
 * <td></td>
 * <td></td>
 * <td></td>
 * <td>duke.gif</td>
 * <td></td>
 *
 * <td>A non-localized, non-versioned image resource called "duke.gif", not
 * in a library</td>
 *
 * <td>duke.gif
 * </td>
 *
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>corporate</td>
 * <td></td>
 * <td>duke.gif</td>
 * <td></td>
 *
 * <td>A non-localized, non-versioned image resource called "duke.gif" in a
 * library called "corporate"</td>
 *
 * <td>corporate/duke.gif
 * </td>
 *
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>corporate</td>
 * <td>2.3</td>
 * <td>duke.gif</td>
 * <td></td>
 *
 * <td>A non-localized, non-versioned image resource called "duke.gif", in
 * version 2.3 of the "corporate" library</td>
 *
 * <td>corporate/2.3/duke.gif
 * </td>
 *
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>basic</td>
 * <td>2.3</td>
 * <td>script.js</td>
 * <td>1.3.4</td>
 *
 * <td>A non-localized, version 1.3.4 script resource called "script.js",
 * in versioned 2.3 library called "basic".</td>
 *
 * <td>basic/2.3/script.js/1.3.4
 * </td>
 *
 * </tr>
 *
 * <tr>
 * <td>de</td>
 * <td></td>
 * <td></td>
 * <td>header.css</td>
 * <td></td>
 *
 * <td>A non-versioned style resource called "header.css" localized for
 * locale "de"</td>
 *
 * <td>de/header.css
 * </td>
 *
 * </tr>
 *
 * <tr>
 * <td>de_AT</td>
 * <td></td>
 * <td></td>
 * <td>footer.css</td>
 * <td>1.4.2</td>
 *
 * <td>Version 1.4.2 of style resource "footer.css", localized for locale "de_AT"</td>
 *
 * <td>de_AT/footer.css/1.4.2
 * </td>
 *
 * </tr>
 *
 * <tr>
 * <td>zh</td>
 * <td>extraFancy</td>
 * <td></td>
 * <td>menu-bar.css</td>
 * <td>2.4</td>
 *
 * <td>Version 2.4 of style resource called, "menu-bar.css" in
 * non-versioned library, "extraFancy", localized for locale "zh"</td>
 *
 * <td>zh/extraFancy/menu-bar.css/2.4
 * </td>
 *
 * </tr>
 *
 * <tr>
 * <td>ja</td>
 * <td>mild</td>
 * <td>0.1</td>
 * <td>ajaxTransaction.js</td>
 * <td></td>
 *
 * <td>Non-versioned script resource called, "ajaxTransaction.js", in
 * version 0.1 of library called "mild", localized for locale "ja"</td>
 *
 * <td>ja/mild/0.1/ajaxTransaction.js
 * </td>
 *
 * </tr>
 *
 * <tr>
 * <td>de_ch</td>
 * <td>grassy</td>
 * <td>1.0</td>
 * <td>bg.png</td>
 * <td>1.0</td>
 *
 * <td>Version 1.0 of image resource called "bg.png", in version 1.0 of
 * library called "grassy" localized for locale "de_ch"</td>
 *
 * <td>de_ch/grassy/1.0/bg.png/1.0
 * </td>
 *
 * </tr>
 *
 * </tbody>
 * </table>
 *
 * <p>Note that when resourceName and resourceVersion are both present
 * the name of the file that contains the bytes of the resource is
 * actually the version number, and that this file resides in a
 * directory named &lt;resourceName&gt;, where &lt;resourceName&gt; is
 * the actual name of the resource.</p>
 *
 * </ul>
 *
 * <p class="javadocSection">Encoding Resources</p>
 *
 * <ul>
 *
 *  <p>During the handling of view requests, the JSF run-time may be
 *  called upon to encode a resource in such a way as to instruct the
 *  user-agent to make a subsequent resource request.  This behavior is
 *  orchestrated by one of the resource renderers
 *  (<code>ScriptRenderer</code>, <code>StylesheetRenderer</code>,
 *  <code>ImageRenderer</code>), which all call {@link Resource#getRequestPath}
 *  to obtain the encoded URI for the resource.  See {@link
 *  Resource#getRequestPath} and the Standard HTML RenderKit specification for
 *  the complete specification.</p>
 *
 * </ul>
 *
 * <p class="javadocSection">Decoding Resources</p>
 *
 * <ul>
 *
 *  <p>During the handling of resource requests, the JSF run-time will
 *  be called upon to decode a resource in such a way as to serve up
 *  the bytes of the resource to the user-agent.  This behavior is
 *  orchestrated by {@link #handleResourceRequest}, which calls {@link
 *  Resource#getInputStream} to obtain bytes of the resource.  See
 *  {@link #handleResourceRequest} for the complete specification.</p>
 *
 * </ul>
 *
 * </div>
 *
 * @since 2.0
 */
public abstract class ResourceHandler {

    /**

     * <p class="changed_added_2_0">{@link Resource#getRequestPath} returns the
     * value of this constant as the prefix of the URI.  {@link
     * #handleResourceRequest(javax.faces.context.FacesContext)} looks for the value of this constant
     * within the request URI to determine if the request is a resource
     * request or a view request.</p>

     */
    public static final String RESOURCE_IDENTIFIER = "/javax.faces.resource";


    // ---------------------------------------------------------- Public Methods
    

    /**
     * <p class="changed_added_2_0">Create an instance of
     * <code>Resource</code> given the argument
     * <code>resourceName</code>.  The content-type of the resource is
     * derived by passing the file extension of
     * <code>resourceName</code> to {@link
     * javax.activation.MimetypesFileTypeMap#getContentType}.</p>
     *
     * @param resourceName the name of the resource.
     *
     * @throws NullPointerException if <code>resourceName</code> is
     *  <code>null</code>.
     *
     * @return a newly created <code>Resource</code> instance, suitable
     * for use in encoding or decoding the named resource.
     */
    public abstract Resource createResource(String resourceName);


    /**
     * <p class="changed_added_2_0">Create an instance of
     * <code>Resource</code> with a resourceName given by the value of
     * the argument <code>resourceName</code> that is a member of the
     * library named by the argument <code>libraryName</code>.  The
     * content-type of the resource is derived by passing the file
     * extension of <code>resourceName</code> to {@link
     * javax.activation.MimetypesFileTypeMap#getContentType}.</p>
     *
     * @param resourceName the name of the resource.
     *
     * @param libraryName the name of the library in which this resource
     * resides (if any).
     *
     * @throws NullPointerException if <code>resourceName</code> is
     *  <code>null</code>
     *
     * @return a newly created <code>Resource</code> instance, suitable
     * for use in encoding or decoding the named resource.
     */
    public abstract Resource createResource(String resourceName,
                                            String libraryName);


    /**
     * <p class="changed_added_2_0">Create an instance of
     * <code>Resource</code> with a resourceName given by the value of
     * the argument <code>resourceName</code> that is a member of the
     * library named by the argument <code>libraryName</code> that
     * claims to have the content-type given by the argument
     * <code>content-type</code>.</p>
     *
     * @param resourceName the name of the resource.
     *
     * @param libraryName the name of the library in which this resource
     * resides (if any).
     *
     * @param contentType the mime content that this
     * <code>Resource</code> instance will return from {@link
     * Resource#getContentType}.  If the value is null, The
     * content-type of the resource is derived by passing the file
     * extension of <code>resourceName</code> to {@link
     * javax.activation.MimetypesFileTypeMap#getContentType}.</p>
     *
     * @throws NullPointerException if <code>resourceName</code> is
     *  <code>null</code>.
     *
     * @return a newly created <code>Resource</code> instance, suitable
     * for use in encoding or decoding the named resource.
     */
    public abstract Resource createResource(String resourceName,
                                            String libraryName,
                                            String contentType);


    /**
     * <p class="changed_added_2_0">This method specifieds the contract
     * for satisfying resource requests.  This method is called from
     * {@link javax.faces.webapp.FacesServlet#service} after that method
     * determines the current request is a resource request by calling
     * {@link #isResourceRequest}.  Thus, <code>createResource</code>
     * may assume that the current request is a resource request.</p>
     *
     * @param context the {@link javax.faces.context.FacesContext} for this
     * request
     */
    public abstract void handleResourceRequest(FacesContext context)
    throws IOException;


    /**
     * <p class="changed_added_2_0">Return <code>true</code> if the
     * current request is a resource request.  This method is called by
     * {@link ViewHandler#renderView} to determine if this request is a
     * view request or a resource request.  This must return
     * <code>false</code> if the viewId ends in the string ".class".</p>
     *
     * @param context the {@link javax.faces.context.FacesContext} for this
     * request
     * @return <code>true</code> if the current request is a safe resource request,
     * <code>false</code> otherwise.
     */
    public abstract boolean isResourceRequest(FacesContext context);

}
