/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2016 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.java.net/public/CDDL+GPL_1_1.html
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
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import javax.faces.context.FacesContext;


/**
 * <p class="changed_added_2_0"><strong
 * class="changed_modified_2_0_rev_a
 * changed_modified_2_2 changed_modified_2_3">ResourceHandler</strong> is the run-time API by
 * which {@link javax.faces.component.UIComponent} and {@link
 * javax.faces.render.Renderer} instances<span
 * class="changed_added_2_2">, and the {@link
 * javax.faces.view.ViewDeclarationLanguage} can reference {@link
 * Resource} instances.</span>  An implementation of this class must be
 * thread-safe.</p>
 *
 * <div class="changed_added_2_0">
 *
 * <p class="javadocSection">Packaging Resources</p>
 *
 * <blockquote>
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
 * <p>relative to the web app root.  <span
 * class="changed_added_2_2">"resources" is the default location, but
 * this location can be changed by the value of the {@link
 * #WEBAPP_RESOURCES_DIRECTORY_PARAM_NAME}
 * <code>&lt;context-param&gt;</code>.</span></p>
 *
 * <p>For the default implementation, resources packaged in the
 * classpath must reside under the JAR entry name</p>

 * <p><code>META-INF/resources/&lt;resourceIdentifier&gt;</code></p>

 * <div class="changed_added_2_2">

 * <p>In the case of Faces Flows packaged
 * within jar files, resources packaged in the classpath must reside
 * under the jar entry name</p>

 * <p><code>META-INF/flows/&lt;resourceIdentifier&gt;</code></p>

 * </div>

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
 * </blockquote>
 *
 * <p class="javadocSection">Encoding Resources</p>
 *
 * <blockquote>
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

 * <p class="changed_added_2_2">This usage of resources does not apply
 * for resources that correspond to VDL resources.</p>
 *
 * </blockquote>
 *
 * <p class="javadocSection">Decoding Resources</p>
 *
 * <blockquote>
 *
 *  <p>During the handling of resource requests, the JSF run-time will
 *  be called upon to decode a resource in such a way as to serve up
 *  the bytes of the resource to the user-agent.  This behavior is
 *  orchestrated by {@link #handleResourceRequest}, which calls {@link
 *  Resource#getInputStream} to obtain bytes of the resource.  See
 *  {@link #handleResourceRequest} for the complete specification.</p>

 * <p class="changed_added_2_2">This usage of resources does not apply
 * for resources that correspond to VDL resources.</p>

 * </blockquote>
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
     * <p class="changed_added_2_3">
     * Resource name of JSF script resource.
     * </p>
     *
     * @since 2.3
     */
    public static final String JSF_SCRIPT_RESOURCE_NAME = "jsf.js";


    /**
     * <p class="changed_added_2_3">
     * Library name of JSF script resource.
     * </p>
     *
     * @since 2.3
     */
    public static final String JSF_SCRIPT_LIBRARY_NAME = "javax.faces";


    /**
     * <p class="changed_added_2_2 changed_modified_2_3">This file must
     * be located in
     * <code>META-INF/contracts/&lt;contractName&gt;/</code> in a jar
     * file that contains a resource library contract, where
     * <code>&lt;contractName&gt;</code> is the name of the contract. If
     * the jar file contains multiple contracts, the marker file must be
     * present in each one. See &#8220;constant field values&#8221; for
     * the name of the file that must be placed at that location.</p>

     * @since 2.2
     */

    public static final String RESOURCE_CONTRACT_XML = "javax.faces.contract.xml";


    /**

     * <p class="changed_added_2_2">If a
     * <code>&lt;context-param&gt;</code> with the param name equal to
     * the value of {@link #WEBAPP_RESOURCES_DIRECTORY_PARAM_NAME}
     * exists, the runtime must interpret its value as a path, relative
     * to the web app root, where resources are to be located.  This
     * param value must not start with a "/", though it may contain "/"
     * characters.  If no such <code>&lt;context-param&gt;</code> exists, or
     * its value is invalid, the value "resources", without the quotes,
     * must be used by the runtime as the value.</p>
     *
     * @since 2.2
     */

    public static final String WEBAPP_RESOURCES_DIRECTORY_PARAM_NAME =
        "javax.faces.WEBAPP_RESOURCES_DIRECTORY";

    /**

     * <p class="changed_added_2_2">If a
     * <code>&lt;context-param&gt;</code> with the param name equal to
     * the value of {@link #WEBAPP_CONTRACTS_DIRECTORY_PARAM_NAME}
     * exists, the runtime must interpret its value as a path, relative
     * to the web app root, where resource library contracts are to be located.  This
     * param value must not start with a "/", though it may contain "/"
     * characters.  If no such <code>&lt;context-param&gt;</code> exists, or
     * its value is invalid, the value "contracts", without the quotes,
     * must be used by the runtime as the value.</p>
     *
     * @since 2.2
     */

    public static final String WEBAPP_CONTRACTS_DIRECTORY_PARAM_NAME =
        "javax.faces.WEBAPP_CONTRACTS_DIRECTORY";

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
     * <p class="changed_added_2_0"><span
     * class="changed_modified_2_2">Create</span> an instance of
     * <code>ViewResource</code> given the argument
     * <code>resourceName</code>.  The content-type of the resource is
     * derived by passing the <em>resourceName</em> to {@link
     * javax.faces.context.ExternalContext#getMimeType}</p>

     * <div class="changed_added_2_0">

     * <p>The algorithm specified in section JSF.2.6.1.4 of the spec
     * prose document <a
     * href="../../../overview-summary.html#prose_document">linked in
     * the overview summary</a> must be executed to create the
     * <code>Resource</code>.  <span class="changed_added_2_2">New
     * requirements were introduced in version 2.2 of the specification.
     * For historical reasons, this method operate correctly when the
     * argument {@code resourceName} is of the form
     * {@code libraryName/resourceName}, even when {@code resourceName}
     * contains '/' characters.  </span></p>

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
     * <p class="changed_added_2_2">Create an instance of <code>Resource</code>
     * given the argument <code>resourceName</code>, which may contain "/"
     * characters.  The {@link javax.faces.view.ViewDeclarationLanguage} calls
     * this method when it needs to load a view from a persistent store, such as
     * a filesystem.  This method is functionality equivalent to
     * {@link #createResource(java.lang.String)}, but all callsites that need
     * to load VDL views must use this method so that classes that want to
     * decorate the <code>ResourceHandler</code> in order to only affect the
     * loading of views may do so without affecting the processing of other
     * kinds of resources, such as scripts and stylesheets.
     * A {@link javax.faces.context.FacesContext} must be present
     * before calling this method.  To preserve compatibility with prior revisions of the
     * specification, a default implementation must be provided that calls
     * {@link #createResource(java.lang.String)}. </p>
     *
     * <div class="changed_added_2_2">
     *
     * <p>The default implementation must look for the resource in the
     * following places, in this order.</p>
     *
     * <ul>
     *
     * <li><p>Considering resource library contracts (at the locations
     * specified in the spec prose document section <em>Resource Library
     * Contracts</em> in the <em>Request Processing Lifecycle</em>
     * chapter).</p></li>
     *
     * <li><p>Considering the web app root.</p></li>
     *
     *  <li><p>Considering faces flows (at the locations specified in
     * the spec prose document section <em>Faces Flows</em> in the
     * <em>Using JSF in Web Applications</em> chapter).</p></li>
     *
     * </ul>
     *
     * <p>Call {@link FacesContext#getResourceLibraryContracts}.  If the
     * result is non-{@code null} and not empty, for each value in the
     * list, treat the value as the name of a resource library contract.
     * If the argument {@code resoureName} exists as a resource in the
     * resource library contract, return it.  Otherwise, return the
     * resource (not in the resource library contract), if found.
     * Otherwise, return {@code null}.</p>
     * </div>
     *
     * @param context the {@link FacesContext} for this request.
     * @param resourceName the name of the resource to be interpreted as a view
     * by the {@link javax.faces.view.ViewDeclarationLanguage}.
     *
     * @throws NullPointerException if <code>resourceName</code> is
     *  {@code null}.
     *
     * @return a newly created {@link ViewResource} instance, suitable
     * for use by the {@link javax.faces.view.ViewDeclarationLanguage}.
     *
     * @since 2.2
     */
    public ViewResource createViewResource(FacesContext context, String resourceName) {
        return context.getApplication().getResourceHandler().createResource(resourceName);
    }
    
    /**
     * <p class="changed_added_2_3">
     *  TODO
     * </p>
     * 
     * @param facesContext The {@link FacesContext} for this request.
     * @param path The path from which to start looking for view resources
     * @param maxDepth the maximum depth of nested directory to visit
     *
     * @return TODO
     * 
     * @since 2.3
     */
    public Stream<String> getViewResources(FacesContext facesContext, String path, int maxDepth, ResourceVisitOption... options) {
        return Stream.empty();
    }
    
    /**
     * <p class="changed_added_2_3">
     *  TODO
     * </p>
     * 
     * @param facesContext The {@link FacesContext} for this request.
     * @param path The path from which to start looking for view resources
     * @param options The options
     *
     * @return TODO
     * 
     * @since 2.3
     */
    public Stream<String> getViewResources(FacesContext facesContext, String path, ResourceVisitOption... options) {
        return Stream.empty();
    }

    /**
     * <p class="changed_added_2_2">Create an instance of
     * <code>Resource</code> given the argument
     * <code>resourceId</code>.  The content-type of the resource is
     * derived by passing the <em>resourceName</em> to {@link
     * javax.faces.context.ExternalContext#getMimeType}</p>
     *
     * <div class="changed_added_2_2">
     *
     * <p>The resource must be identified according to the specification
     * in JSF.2.6.1.3 of the spec prose document <a
     * href="../../../overview-summary.html#prose_document">linked in
     * the overview summary</a>.  New requirements were introduced in
     * version 2.2 of the specification.</p>
     *
     * </div>
     *
     * @param resourceId the resource identifier of the resource.
     *
     * @throws NullPointerException if <code>resourceId</code> is
     *  <code>null</code>.
     *
     * @return a newly created <code>Resource</code> instance, suitable
     * for use in encoding or decoding the named resource.
     *
     * @since 2.2
     */
    public Resource createResourceFromId(String resourceId) {
        return null;
    }

    /**
     * <p class="changed_added_2_0"><span
     * class="changed_modified_2_2">Create</span> an instance of
     * <code>Resource</code> with a resourceName given by the value of
     * the argument <code>resourceName</code> that is a member of the
     * library named by the argument <code>libraryName</code>.  The
     * content-type of the resource is derived by passing the
     * <em>resourceName</em> to {@link
     * javax.faces.context.ExternalContext#getMimeType}.</p>
     *
     * <div class="changed_added_2_0">
     *
     * <p>The algorithm specified in section JSF.2.6.1.4 of the spec
     * prose document <a
     * href="../../../overview-summary.html#prose_document">linked in
     * the overview summary</a> must be executed to create the
     * <code>Resource</code>. <span class="changed_added_2_2">New
     * requirements were introduced in version 2.2 of the
     * specification.</span></p>
     *
     * </div>
     *
     * @param resourceName the name of the resource.
     *
     * @param libraryOrContractName <span class="changed_modified_2_2">the
     * name of the library (or contract) in which this resource
     * resides, may be <code>null</code>. If there is a conflict between
     * the name of a resource library and a resource library contract,
     * the resource library takes precedence.  <span
     * class="changed_modified_2_0_rev_a">May not include relative
     * paths, such as "../".</span></span>
     *
     * @throws NullPointerException if
     * <code>resourceName</code> is <code>null</code>
     *
     * @return a newly created <code>Resource</code> instance, suitable
     * for use in encoding or decoding the named resource.
     */
    public abstract Resource createResource(String resourceName, String libraryOrContractName);

    /**
     * <p class="changed_added_2_0"><span
     * class="changed_modified_2_2">Create</span> an instance of
     * <code>Resource</code> with a <em>resourceName</em> given by the
     * value of the argument <code>resourceName</code> that is a member
     * of the library named by the argument <code>libraryName</code>
     * that claims to have the content-type given by the argument
     * <code>content-type</code>.</p>
     *
     * <div class="changed_added_2_0">
     *
     * <p>The algorithm specified in section JSF.2.6.1.4 of the spec
     * prose document <a
     * href="../../../overview-summary.html#prose_document">linked in
     * the overview summary</a> must be executed to create the
     * <code>Resource</code>. <span class="changed_added_2_2">New
     * requirements were introduced in version 2.2 of the
     * specification.</span></p>
     *
     * </div>
     *
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
     * javax.faces.context.ExternalContext#getMimeType}
     *
     * @throws NullPointerException if
     * <code>resourceName</code> is <code>null</code>.
     *
     * @return a newly created <code>Resource</code> instance, suitable
     * for use in encoding or decoding the named resource.
     */
    public abstract Resource createResource(String resourceName, String libraryName, String contentType);

    /**
     * <p class="changed_added_2_0"><span
     * class="changed_modified_2_2">Return</span> <code>true</code> if
     * the resource library named by the argument
     * <code>libraryName</code> can be found.  <span
     * class="changed_added_2_2">If there is a <code>localePrefix</code>
     * for this application, as defined in {@link #LOCALE_PREFIX}, first
     * look for the library with the prefix.  If no such library is
     * found, look for the library without the prefix.  This allows
     * developers to avoid duplication of files.  For example, consider
     * the case where the developer wants to have a resource library
     * containing a localized image resource and a non-localized script
     * resource.  By checking both locations for the existence of the
     * library, along with other spec changes in section 2.6.1.4, this
     * scenario is enabled.</span></p>
     *
     * @param libraryName the library name.
     * @return <code>true</code> if the library exists, <code>false</code> otherwise.
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
     * @throws IOException when an I/O error occurs.
     */
    public abstract void handleResourceRequest(FacesContext context) throws IOException;

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
     * <p class="changed_added_2_2">Return {@code true} if the argument {@code url}
     * contains the string given by the value of the constant
     * {@link ResourceHandler#RESOURCE_IDENTIFIER}, false otherwise.</p>
     *
     * @param url the url to inspect for the presence of {@link ResourceHandler#RESOURCE_IDENTIFIER}.
     * @return <code>true</code> if this is a resource URL, <code>false</code> otherwise.
     * @throws NullPointerException if the argument url is {@code null}.
     */
    public boolean isResourceURL(String url) {
        boolean result = false;
        if (null == url) {
            throw new NullPointerException("null url");
        }
        result = url.contains(RESOURCE_IDENTIFIER);

        return result;
    }

    /**
     * <p class="changed_added_2_0">Return the <code>renderer-type</code> for a
     * {@link javax.faces.render.Renderer} that is capable of rendering this
     * resource. The default implementation must return values according to the
     * following table.  If no <code>renderer-type</code> can be determined,
     * <code>null</code> must be returned.</p>
     *
     * <table border="1">
     *  <caption>resource name to renderer-type mapping</caption>
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
     *
     * @param resourceName the resource name.
     * @return the renderer type.
     */
    public abstract String getRendererTypeForResourceName(String resourceName);

    /**
     * <p class="changed_added_2_3">
     * Mark the resource as identified by given resource and library name as rendered. The default implementation must
     * ensure that {@link #isResourceRendered(FacesContext, String, String)} will return <code>true</code> when the
     * resource has already been rendered during the render response phase of the current view.
     * </p>
     * @param context The {@link FacesContext} for this request.
     * @param resourceName The name of the resource.
     * @param libraryName The name of the library in which the resource resides, may be <code>null</code>.
     * @since 2.3
     */
    @SuppressWarnings("unchecked")
    public void markResourceRendered(FacesContext context, String resourceName, String libraryName) {
        String resourceIdentifier = libraryName + ":" + resourceName;
        Set<String> resourceIdentifiers = (Set<String>) context.getAttributes().computeIfAbsent(RESOURCE_IDENTIFIER, k -> new HashSet<>());
        resourceIdentifiers.add(resourceIdentifier);
    }

    /**
     * <p class="changed_added_2_3">
     * Returns whether the resource as identified by given resource and library name has been rendered. The default
     * implementation must during the render response phase of the current view return <code>true</code> when the
     * resource has been marked as rendered via {@link #markResourceRendered(FacesContext, String, String)}.
     * </p>
     * @param context The {@link FacesContext} for this request.
     * @param resourceName The name of the resource.
     * @param libraryName The name of the library in which this resource resides, may be <code>null</code>.
     * @return Whether the resource as identified by given resource and library name has been rendered.
     * @since 2.3
     */
    @SuppressWarnings("unchecked")
    public boolean isResourceRendered(FacesContext context, String resourceName, String libraryName) {
        String resourceIdentifier = libraryName + ":" + resourceName;
        Set<String> resourceIdentifiers = (Set<String>) context.getAttributes().get(RESOURCE_IDENTIFIER);
        return resourceIdentifiers != null && resourceIdentifiers.contains(resourceIdentifier);
    }

}