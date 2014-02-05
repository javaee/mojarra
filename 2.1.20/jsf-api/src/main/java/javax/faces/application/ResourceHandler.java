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

import javax.faces.context.FacesContext;


/**
 * <p class="changed_added_2_0"><strong class="changed_modified_2_0_rev_a">ResourceHandler</strong> is the
 * run-time API by which {@link javax.faces.component.UIComponent} and
 * {@link javax.faces.render.Renderer} instances can reference {@link
 * Resource} instances.  An implementation of this class must be thread-safe.</p>
 *
 * <div class="changed_added_2_0">
 *
 * <p class="javadocSection">Packaging Resources</p>
 *
 * <ul>
 *
 *  <p>ResourceHandler defines a path based packaging convention for
 *  resources.  The default implementation of
 *  <code>ResourceHandler</code> must support packaging resources in the
 *  classpath or in the web application root. See section JSF.2.6.1 of the
 *  spec prose document <a
 *  href="../../../overview-summary.html#prose_document">linked in the
 *  overview summary</a> for the normative specification of packaging
 *  resources.</p>

 * <p>Briefly, The default implementation must support packaging
 * resources in the web application root under the path</p>
 *
 * <p><code>resources/&lt;resourceIdentifier&gt;</code></p>
 *
 * <p>relative to the web app root.</p>
 *
 * <p>For the default implementation, resources packaged in the
 * classpath must reside under the JAR entry name</p>
 *
 * <p><code>META-INF/resources/&lt;resourceIdentifier&gt;</code></p>

 * <p><code>&lt;resourceIdentifier&gt;</code> consists of several
 * segments, specified as follows.</p>

 * <p><code>[localePrefix/][libraryName/][libraryVersion/]resourceName[/resourceVersion]</code></p>

 * <p class="changed_modified_2_0_rev_a">None of the segments in the
 * resourceIdentifier may be relative paths, such as
 * &#8216;../otherLibraryName&#8217;.  The implementation is not
 * required to support the <code>libraryVersion</code> and
 * <code>resourceVersion</code> segments for the JAR packaging case.</p>

 * <p>Note that <em>resourceName</em> is the only required segment.</p>
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


    /**
     * <p class="changed_added_2_0">The name of a key within the
     * application message bundle named by the return from {@link
     * Application#getMessageBundle} whose value is the locale prefix
     * used to find a packaged resource to return from {@link
     * #createResource} (or one of its variants).
     */

    public static final String LOCALE_PREFIX = 
	"javax.faces.resource.localePrefix";



    /**
     * <p class="changed_added_2_0">The <code>ServletContext</code> init
     * parameter consulted by the {@link #handleResourceRequest} to tell
     * which kinds of resources must never be served up in response to a
     * resource request.  The value of this parameter is a single space
     * separated list of file extensions, including the leading '.'
     * character (without the quotes).  If not specified, the default
     * value given in the value of the {@link
     * #RESOURCE_EXCLUDES_DEFAULT_VALUE} constant is used.  If manually
     * specified, the given value entirely overrides the default one and
     * does not supplement it.  </p>
     */
    public static final String RESOURCE_EXCLUDES_PARAM_NAME =
          "javax.faces.RESOURCE_EXCLUDES";

    /**
     * <p class="changed_added_2_0 changed_modified_2_1">The default value for the {@link
     * #RESOURCE_EXCLUDES_PARAM_NAME} init param.</p>
     */
    public static final String RESOURCE_EXCLUDES_DEFAULT_VALUE =
          ".class .jsp .jspx .properties .xhtml .groovy";



    // ---------------------------------------------------------- Public Methods
    

    /**
     * <p class="changed_added_2_0">Create an instance of
     * <code>Resource</code> given the argument
     * <code>resourceName</code>.  The content-type of the resource is
     * derived by passing the <em>resourceName</em> to {@link
     * javax.faces.context.ExternalContext#getMimeType}</p>

     * <div class="changed_added_2_0">

     * <p>The algorithm specified in section JSF.2.6.1.4 of the spec prose
     * document <a
     * href="../../../overview-summary.html#prose_document">linked in
     * the overview summary</a> must be executed to create the
     * <code>Resource</code></p>

     * </div>

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
     * content-type of the resource is derived by passing the
     * <em>resourceName</em> to
     * {@link javax.faces.context.ExternalContext#getMimeType}.</p>
     *
     * <div class="changed_added_2_0">

     * <p>The algorithm specified in section JSF.2.6.1.4 of the spec prose
     * document <a
     * href="../../../overview-summary.html#prose_document">linked in
     * the overview summary</a> must be executed to create the
     * <code>Resource</code></p>

     * </div>

     * @param resourceName the name of the resource.
     *
     * @param libraryName the name of the library in which this resource
     * resides, may be <code>null</code>. <span
     * class="changed_modified_2_0_rev_a">May not include relative
     * paths, such as "../".</span>
     *
     * @throws <code>NullPointerException</code> if
     * <code>resourceName</code> is <code>null</code>
     *
     * @return a newly created <code>Resource</code> instance, suitable
     * for use in encoding or decoding the named resource.
     */
    public abstract Resource createResource(String resourceName,
                                            String libraryName);


    /**
     * <p class="changed_added_2_0">Create an instance of
     * <code>Resource</code> with a <em>resourceName</em> given by the
     * value of the argument <code>resourceName</code> that is a member
     * of the library named by the argument <code>libraryName</code>
     * that claims to have the content-type given by the argument
     * <code>content-type</code>.</p>
     *
     * <div class="changed_added_2_0">

     * <p>The algorithm specified in section JSF.2.6.1.4 of the spec prose
     * document <a
     * href="../../../overview-summary.html#prose_document">linked in
     * the overview summary</a> must be executed to create the
     * <code>Resource</code></p>

     * </div>

     * @param resourceName the name of the resource.
     *
     * @param libraryName the name of the library in which this resource
     * resides, may be <code>null</code>.  <span
     * class="changed_modified_2_0_rev_a">May not include relative
     * paths, such as "../".</span>
     *
     * @param contentType the mime content that this
     * <code>Resource</code> instance will return from {@link
     * Resource#getContentType}.  If the value is <code>null</code>, The
     * content-type of the resource is derived by passing the
     * <em>resourceName</em> to {@link
     * javax.faces.context.ExternalContext#getMimeType}</p>
     *
     * @throws <code>NullPointerException</code> if
     * <code>resourceName</code> is <code>null</code>.
     *
     * @return a newly created <code>Resource</code> instance, suitable
     * for use in encoding or decoding the named resource.
     */
    public abstract Resource createResource(String resourceName,
                                            String libraryName,
                                            String contentType);
    
    /**
     * <p class="changed_added_2_0">Return <code>true</code> if the 
     * resource library named by the argument <code>libraryName</code>
     * can be found.</p>
     *
     * @since 2.0
     * 
     */
    
    public abstract boolean libraryExists(String libraryName);


    /**
     * <p class="changed_added_2_0">This method specifies the contract
     * for satisfying resource requests.  This method is called from
     * {@link javax.faces.webapp.FacesServlet#service} after that method
     * determines the current request is a resource request by calling
     * {@link #isResourceRequest}.  Thus, <code>handleResourceRequest</code>
     * may assume that the current request is a resource request.</p>
     *
     * <div class="changed_added_2_0">
     *
     * <p>The default implementation must implement an algorithm
     * semantically identical to the following algorithm.</p>
     *
     * For discussion, in all cases when a status code is to be set,
     * this spec talks only using the Servlet API, but it is understood
     * that in a portlet environment the appropriate equivalent API must
     * be used.
     *
     * <ul>
     *
     * <li><p>If the <em>resourceIdentifier</em> ends with any of the
     * extensions listed in the value of the {@link
     * #RESOURCE_EXCLUDES_PARAM_NAME} init parameter,
     * <code>HttpServletRequest.SC_NOT_FOUND</code> must be passed to
     * <code>HttpServletResponse.setStatus()</code>, then
     * <code>handleResourceRequest</code> must immediately return.</p></li>
     *
     * <li><p>Extract the <em>resourceName</em> from the
     * <em>resourceIdentifier</em> by taking the substring of
     * <em>resourceIdentifier</em> that starts at <code>{@link
     * #RESOURCE_IDENTIFIER}.length() + 1</code> and goes to the end of
     * <em>resourceIdentifier</em>.  If no <em>resourceName</em> can be
     * extracted, <code>HttpServletRequest.SC_NOT_FOUND</code> must be
     * passed to <code>HttpServletResponse.setStatus()</code>, then
     * <code>handleResourceRequest</code> must immediately return.</p></li>
     *
     * <li><p>Extract the <em>libraryName</em> from the request by
     * looking in the request parameter map for an entry under the key
     * "ln", without the quotes.  If found, use its value as the
     * <em>libraryName</em>.</p></li>
     *
     * <li><p>If <em>resourceName</em> and <em>libraryName</em> are
     * present, call {@link #createResource(String, String)} to create
     * the <code>Resource</code>.  If only <em>resourceName</em> is
     * present, call {@link #createResource(String)} to create the
     * <code>Resource</code>.  If the <code>Resource</code> cannot be
     * successfully created,
     * <code>HttpServletRequest.SC_NOT_FOUND</code> must be passed to
     * <code>HttpServletResponse.setStatus()</code>, then
     * <code>handleResourceRequest</code> must immediately return.</p></li>
     *
     * <li><p>Call {@link Resource#userAgentNeedsUpdate}.  If this
     * method returns false,
     * <code>HttpServletRequest.SC_NOT_MODIFIED</code> must be passed to
     * <code>HttpServletResponse.setStatus()</code>, then
     * <code>handleResourceRequest</code> must immediately return.</p></li>
     *
     * <li><p>Pass the result of {@link Resource#getContentType} to
     * <code>HttpServletResponse.setContentType.</code> </p></li>
     *
     * <li><p>Call {@link Resource#getResponseHeaders}.  For each entry
     * in this <code>Map</code>, call
     * <code>HttpServletResponse.setHeader()</code>, passing the key as
     * the first argument and the value as the second argument.</p></li>
     *
     * <li><p>Call {@link Resource#getInputStream} and serve up the
     * bytes of the resource to the response.</p></li>
     *
     * <li><p>Call <code>HttpServletResponse.setContentLength()</code>
     * passing the byte count of the resource.</p></li>
     *
     * <li><p>If an <code>IOException</code> is thrown during any of the
     * previous steps, log a descriptive, localized message, including
     * the <em>resourceName</em> and <em>libraryName</em> (if present).
     * Then, <code>HttpServletRequest.SC_NOT_FOUND</code> must be passed
     * to <code>HttpServletResponse.setStatus()</code>, then
     * <code>handleResourceRequest</code> must immediately return.</p></li>
     *
     * <li><p>In all cases in this method, any streams, channels,
     * sockets, or any other IO resources must be closed before this
     * method returns.</p></li>
     *
     * </ul>
     *
     * </div>
     *
     * @param context the {@link javax.faces.context.FacesContext} for this
     * request
     */
    public abstract void handleResourceRequest(FacesContext context)
    throws IOException;


    /**
     * <p class="changed_added_2_0">Return <code>true</code> if the
     * current request is a resource request.  This method is called by
     * {@link javax.faces.webapp.FacesServlet#service} to determine if
     * this request is a <em>view request</em> or a <em>resource
     * request</em>.</p>
     *
     * @param context the {@link javax.faces.context.FacesContext} for this
     * request
     * @return <code>true</code> if the current request is a resource
     * request, <code>false</code> otherwise.
     */
    public abstract boolean isResourceRequest(FacesContext context);
    
    /**
     * <p class="changed_added_2_0">Return the <code>renderer-type</code> for a 
     * {@link javax.faces.render.Renderer} that is capable of rendering this 
     * resource. The default implementation must return values according to the
     * following table.  If no <code>renderer-type</code> can be determined,
     * <code>null</code> must be returned.</p> 
     * 
     * <table border="1">
     * 
     * <tr>
     * 
     * <th>example resource name</th>
     * 
     * <th>renderer-type</th>
     * 
     * </tr>
     * 
     * <tr>
     * 
     * <td>mycomponent.js</td>
     * 
     * <td><code>javax.faces.resource.Script</code></td>
     * 
     * </tr>
     * 
     * <tr>
     * 
     * <td>mystyle.css</td>
     * 
     * <td><code>javax.faces.resource.Stylesheet</code></td>
     * 
     * </tr>
     * 
     * </table>
     */
    
    public abstract String getRendererTypeForResourceName(String resourceName);
    

}
