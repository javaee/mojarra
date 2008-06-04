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

/*
 * $Id: ExternalContext.java,v 1.38 2008/01/31 05:19:38 edburns Exp $
 */
 
/*
 * Licensed Material - Property of IBM 
 * (C) Copyright IBM Corp. 2002, 2003 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure 
 * restricted by GSA ADP Schedule Contract with IBM Corp. 
 */

package javax.faces.context;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Principal;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import java.util.Map;



/**
 * <p><span class="changed_modified_2_0">This</span> class allows the
 * Faces API to be unaware of the nature of its containing application
 * environment.  In particular, this class allows JavaServer Faces based
 * appications to run in either a Servlet or a Portlet environment.</p>
 *
 * <p class="changed_modified_2_0">The documentation for this class only
 * specifies the behavior for the <em>Servlet</em> implementation of
 * <code>ExternalContext</code>.  The <em>Portlet</em> implementation of
 * <code>ExternalContext</code> is specified under the revision of the
 * <span style="text-decoration: underline;">Portlet Bridge
 * Specification for JavaServer Faces</span> JSR that corresponds to
 * this version of the JSF specification.  See the Preface of the
 * &quot;prose document&quot;, <a
 * href="../../../overview-summary.html#overview_description">linked
 * from the javadocs</a>, for a reference.</p>

 * <p class="changed_added_2_0">If a reference to an
 * <code>ExternalContext</code> is obtained during application startup
 * time, any method documented as "valid to call this method during
 * application startup" must be supported during application startup
 * time.  The result of calling a method during application startup time
 * that does not have this designation is undefined.</p>


 */

public abstract class ExternalContext {
    

    // ------------------------------------------------------ Manifest Constants


    /**
     * <p>String identifier for BASIC authentication.</p>
     */
    public static final String BASIC_AUTH = "BASIC";


    /**
     * <p>String identifier for CLIENT_CERT authentication.</p>
     */
    public static final String CLIENT_CERT_AUTH = "CLIENT_CERT";


    /**
     * <p>String identifier for DIGEST authentication.</p>
     */
    public static final String DIGEST_AUTH = "DIGEST";


    /**
     * <p>String identifier for FORM authentication.</p>
     */
    public static final String FORM_AUTH = "FORM";



    // ---------------------------------------------------------- Public Methods


    /**
     * <p>Dispatch a request to the specified resource to create output
     * for this response.</p>
     *
     * <p><em>Servlet:</em> This must be accomplished by calling the
     * <code>javax.servlet.ServletContext</code> method
     * <code>getRequestDispatcher(path)</code>, and calling the
     * <code>forward()</code> method on the resulting object.</p>
     *
     * @param path Context relative path to the specified resource,
     *  which must start with a slash ("/") character
     *
     * @throws javax.faces.FacesException thrown if a <code>ServletException</code> occurs
     * @throws IllegalArgumentException if no request dispatcher
     *  can be created for the specified path
     * @throws IOException if an input/output error occurs
     * @throws NullPointerException if <code>path</code>
     *  is <code>null</code>
     */
    public abstract void dispatch(String path)
	throws IOException;


    /**
     * <p>Return the input URL, after performing any rewriting needed to
     * ensure that it will correctly identify an addressable action in the
     * current application.<p>
     *
     * <p><em>Servlet:</em> This must be the value returned by the
     * <code>javax.servlet.http.HttpServletResponse</code> method
     * <code>encodeURL(url)</code>.</p>
     *
     * @param url The input URL to be encoded
     *
     * @throws NullPointerException if <code>url</code>
     *  is <code>null</code>
     */
    public abstract String encodeActionURL(String url);
    

    /**
     * <p>Return the specified name, after prefixing it with a namespace
     * that ensures that it will be unique within the context of a
     * particular page.</p>
     *
     * <p><em>Servlet:</em> The input value must be returned unchanged.</p>
     *
     * @param name Name to be encoded
     *
     * @throws NullPointerException if <code>name</code>
     *  is <code>null</code>
     */
    public abstract String encodeNamespace(String name);


    /**
     * <p>Return the input URL, after performing any rewriting needed to
     * ensure that it will correctly identify an addressable resource in the
     * current application.<p>
     *
     * <p><em>Servlet:</em> This must be the value returned by the
     * <code>javax.servlet.http.HttpServletResponse</code> method
     * <code>encodeURL(url)</code>.</p>
     *
     * @param url The input URL to be encoded
     *
     * @throws NullPointerException if <code>url</code>
     *  is <code>null</code>
     */
    // PENDING(craigmcc) - Currently identical to encodeActionURL()
    public abstract String encodeResourceURL(String url);
    

    /**
     * <p><span class="changed_modified_2_0">Return</span> a mutable
     * <code>Map</code> representing the application scope attributes
     * for the current application.  The returned <code>Map</code> must
     * implement the entire contract for a modifiable map as described
     * in the JavaDocs for <code>java.util.Map</code>.  Modifications
     * made in the <code>Map</code> must cause the corresponding changes
     * in the set of application scope attributes.  Particularly the
     * <code>clear()</code>, <code>remove()</code>, <code>put()</code>,
     * <code>putAll()</code>, and <code>get()</code> operations must
     * take the appropriate action on the underlying data structure.</p>
     *
     * <p>For any of the <code>Map</code> methods that cause an element
     * to be removed from the underlying data structure, the following
     * action regarding managed-beans must be taken.  If the element to
     * be removed is a managed-bean, and it has one or more public
     * no-argument void return methods annotated with
     * <code>javax.annotation.PreDestroy</code>, each such method must
     * be called before the element is removed from the underlying data
     * structure.  Elements that are not managed-beans, but do happen to
     * have methods with that annotation must not have those methods
     * called on removal.  Any exception thrown by the
     * <code>PreDestroy</code> annotated methods must by caught and not
     * rethrown.  The exception may be logged.</p>

     * <p class="changed_added_2_0">It is valid to call this method
     * during application startup.  If called at startup time, this
     * method returns a <code>Map</code> that is backed by the same
     * container context instance (<code>ServletContext</code> or
     * <code>PortletContext</code>) as the one returned by calling
     * <code>getApplicationMap()</code> on the
     * <code>ExternalContext</code> returned by the
     * <code>FacesContext</code> during an actual request.</p>
     *
     * <p><em>Servlet:</em>  This must be the set of attributes available via
     * the <code>javax.servlet.ServletContext</code> methods
     * <code>getAttribute()</code>, <code>getAttributeNames()</code>,
     * <code>removeAttribute()</code>, and <code>setAttribute()</code>.</p>
     *
     */
    public abstract Map<String, Object> getApplicationMap();


    /**
     * <p>Return the name of the authentication scheme used to authenticate
     * the current user, if any; otherwise, return <code>null</code>.
     * For standard authentication schemes, the returned value will match
     * one of the following constants:
     * <code>BASIC_AUTH</code>, <code>CLIENT_CERT_AUTH</code>,
     * <code>DIGEST_AUTH</code>, or <code>FORM_AUTH</code>.</p>
     *
     * <p><em>Servlet:</em> This must be the value returned by the
     * <code>javax.servlet.http.HttpServletRequest</code> method
     * <code>getAuthType()</code>.</p>
     *
     */
    public abstract String getAuthType();


    /**
     * <p class="changed_added_2_0">Returns the MIME type of the
     * specified file or <code>null</code> if the MIME type is not
     * known.  The MIME type is determined by the container.</p>

     * <p class="changed_added_2_0">It is valid to call this method
     * during application startup.  If called during application
     * startup, this method calls through to the
     * <code>getMimeType()</code> method on the same container
     * context instance (<code>ServletContext</code> or
     * <code>PortletContext</code>) as the one used when calling
     * <code>getMimeType()</code> on the
     * <code>ExternalContext</code> returned by the
     * <code>FacesContext</code> during an actual request.</p>

     * <div class="changed_added_2_0">
 
     * <p><em>Servlet:</em> This must be the value returned by the
     * <code>javax.servlet.ServletContext</code> method
     * <code>getMimeType()</code>.</p>
     *
     * </div>

     *
     * @since 2.0
     */
    public String getMimeType(String file) {

        ExternalContext impl = getDefaultExternalContext();
        if (impl != null) {
            return impl.getMimeType(file);
        }

        throw new UnsupportedOperationException();
        
    }


    /**
     * <p><span class="changed_modified_2_0">Return</span> the
     * application environment object instance for the current
     * appication.</p>

     * <p class="changed_added_2_0">It is valid to call this method
     * during application startup.  If called during application
     * startup, this returns the same container context instance
     * (<code>ServletContext</code> or <code>PortletContext</code>) as
     * the one returned when calling <code>getContext()</code> on the
     * <code>ExternalContext</code> returned by the
     * <code>FacesContext</code> during an actual request.</p>

     *
     * <p><em>Servlet:</em>  This must be the current application's
     * <code>javax.servlet.ServletContext</code> instance.</p>
     *
     */
    public abstract Object getContext();


    /**
     * <p><span class="changed_modified_2_0">Return</span> the value of
     * the specified application initialization parameter (if any).</p>
     *
     * <p><em>Servlet:</em> This must be the result of the
     * <code>javax.servlet.ServletContext</code> method
     * <code>getInitParameter(name)</code>.</p>
     *
     * <p class="changed_added_2_0">It is valid to call this method
     * during application startup.  If called during application
     * startup, this method calls through to the actual container
     * context to return the init parameter value.</p>

     * @param name Name of the requested initialization parameter
     *
     * @throws NullPointerException if <code>name</code>
     *  is <code>null</code>
     */
    public abstract String getInitParameter(String name);


    /**
     * <p><span class="changed_modified_2_0">Return</span></span> an
     * immutable <code>Map</code> whose keys are the set of application
     * initialization parameter names configured for this application,
     * and whose values are the corresponding parameter values.  The
     * returned <code>Map</code> must implement the entire contract for
     * an unmodifiable map as described in the JavaDocs for
     * <code>java.util.Map</code>.</p>

     * <p class="changed_added_2_0">It is valid to call this method
     * during application startup.  If called during application
     * startup, this method returns a <code>Map</code> that is backed by
     * the same container context instance (<code>ServletContext</code>
     * or <code>PortletContext</code>) as the one returned by calling
     * <code>getInitParameterMap()</code> on the
     * <code>ExternalContext</code> returned by the
     * <code>FacesContext</code> during an actual request.</p>

     * <p><em>Servlet:</em> This result must be as if it were synthesized
     * by calling the <code>javax.servlet.ServletContext</code>
     * method <code>getInitParameterNames</code>, and putting
     * each configured parameter name/value pair into the result.</p>
     *
     */
    public abstract Map getInitParameterMap();
    

    /**
     * <p>Return the login name of the user making the current request
     * if any; otherwise, return <code>null</code>.</p>
     *
     * <p><em>Servlet:</em> This must be the value returned by the
     * <code>javax.servlet.http.HttpServletRequest</code> method
     * <code>getRemoteUser()</code>.</p>
     *
     */
    public abstract String getRemoteUser();


    /**
     * <p>Return the environment-specific object instance for the current
     * request.</p>
     *
     * <p><em>Servlet:</em>  This must be the current request's
     * <code>javax.servlet.http.HttpServletRequest</code> instance.</p>
     *
     */
    public abstract Object getRequest();

    /**
     * <p>Set the environment-specific request to be returned by
     * subsequent calls to {@link #getRequest}.  This may be used to
     * install a wrapper for the request.</p>
     *
     * <p>The default implementation throws 
     * <code>UnsupportedOperationException</code> and is provided
     * for the sole purpose of not breaking existing applications that extend
     * this class.</p>
     *
     *
     * @since 1.2
     */
    public void setRequest(Object request) {

        ExternalContext impl = getDefaultExternalContext();
        if (impl != null) {
            impl.setRequest(request);
            return;
        }

        throw new UnsupportedOperationException();

    }


    /**
     * <p class="changed_added_2_0">Returns the name of the scheme used
     * to make this request, for example, http, https, or ftp.</p>
     *
     * <div class="changed_added_2_0">
     * <p><em>Servlet:</em> This must be the value returned by the
     * <code>javax.servlet.ServletRequest</code> method
     * <code>getScheme()</code>.</p>
     *
     * <p>The default implementation throws
     * <code>UnsupportedOperationException</code> and is provided for
     * the sole purpose of not breaking existing applications that
     * extend this class.</p>

     * </div>

     * @since 2.0
     */

    public String getRequestScheme() {
	String result;
        ExternalContext impl = getDefaultExternalContext();
        if (impl != null) {
            result = impl.getRequestScheme();
            return result;
        }

        throw new UnsupportedOperationException();
    }

    /**
     * <p class="changed_added_2_0">Returns the host name of the server
     * to which the request was sent.</p>
     *
     * <div class="changed_added_2_0">

     * <p><em>Servlet:</em> This must be the value returned by the
     * <code>javax.servlet.ServletRequest</code> method
     * <code>getServerName()</code>.</p>
     
     * <p>The default implementation throws
     * <code>UnsupportedOperationException</code> and is provided for
     * the sole purpose of not breaking existing applications that
     * extend this class.</p>

     * </div>

     * @since 2.0
     */

    public String getRequestServerName() {
	String result;
        ExternalContext impl = getDefaultExternalContext();
        if (impl != null) {
            result = impl.getRequestServerName();
            return result;
        }

        throw new UnsupportedOperationException();
    }

    /**
     * <p class="changed_added_2_0">Returns the port number to which
     * the request was sent.</p>
     *
     * <div class="changed_added_2_0">

     * <p><em>Servlet:</em> This must be the value returned by the
     * <code>javax.servlet.ServletRequest</code> method
     * <code>getServerPort()</code>.</p>
     
     * <p>The default implementation throws
     * <code>UnsupportedOperationException</code> and is provided for
     * the sole purpose of not breaking existing applications that
     * extend this class.</p>

     * </div>

     * @since 2.0
     */

    public String getRequestServerPort() {
	String result;
        ExternalContext impl = getDefaultExternalContext();
        if (impl != null) {
            result = impl.getRequestServerPort();
            return result;
        }

        throw new UnsupportedOperationException();
    }
    
    /**
     *
     * <p>Overrides the name of the character 
     * encoding used in the body of this request.</p>
     *
     * <p>Calling this method after the request has been accessed will have no
     * no effect, unless a <code>Reader</code> or <code>Stream</code> has been
     * obtained from the request, in which case an <code>IllegalStateException</code>
     * is thrown.</p>
     *
     * <p><em>Servlet:</em> This must call through to the
     * <code>javax.servlet.ServletRequest</code> method
     * <code>setCharacterEncoding()</code>.</p>
     *
     * <p>The default implementation throws 
     * <code>UnsupportedOperationException</code> and is provided
     * for the sole purpose of not breaking existing applications that extend
     * this class.</p>
     *
     * @throws java.io.UnsupportedEncodingException if this is not a valid
     * encoding 
     *
     * @since 1.2
     *
     */
    public void setRequestCharacterEncoding(String encoding) throws UnsupportedEncodingException {

        ExternalContext impl = getDefaultExternalContext();
        if (impl != null) {
            impl.setRequestCharacterEncoding(encoding);
            return;
        }

        throw new UnsupportedOperationException();
    }

    /**
     * <p>Returns a String containing the real path for a given virtual
     * path. </p>
     *
     * <p><em>Servlet:</em> This must be the value returned by the
     * <code>javax.servlet.ServletContext</code> method
     * <code>getRealPath()</code>.</p>
     *
     * @param path The context of the requested initialization parameter
     *
     * <p>The default implementation throws 
     * <code>UnsupportedOperationException</code> and is provided
     * for the sole purpose of not breaking existing applications that extend
     * this class.</p>
     * @since 2.0
     */
    public String getRealPath(String path) {
        ExternalContext impl;
        if (null != (impl = (ExternalContext) this.getRequestMap().
                get("com.sun.faces.ExternalContextImpl"))) {
            return impl.getRealPath(path);
        }

        throw new UnsupportedOperationException();
    }


    /**
     * <p>Return the portion of the request URI that identifies the web
     * application context for this request.</p>
     *
     * <p><em>Servlet:</em> This must be the value returned by the
     * <code>javax.servlet.http.HttpServletRequest</code> method
     * <code>getContextPath()</code>.</p>
     *
     */
    public abstract String getRequestContextPath();


    /**
     * <p>Return an immutable <code>Map</code> whose keys are the set of
     * cookie names included in the current request, and whose
     * values (of type <code>javax.servlet.http.Cookie</code>)
     * are the first (or only) cookie for each cookie name
     * returned by the underlying request.  The returned
     * <code>Map</code> must implement the entire contract for an unmodifiable
     * map as described in the JavaDocs for <code>java.util.Map</code>.</p>
     *
     * <p><em>Servlet:</em> This must be the value returned by the
     * <code>javax.servlet.http.HttpServletRequest</code> method
     * <code>getCookies()</code>, unless <code>null</code> was returned,
     * in which case this must be a zero-length array.</p>
     *
     */
    public abstract Map<String, Object> getRequestCookieMap();
    

    /**
     * <p>Return an immutable <code>Map</code> whose keys are the set of
     * request header names included in the current request, and whose
     * values (of type String) are the first (or only) value for each
     * header name returned by the underlying request.  The returned
     * <code>Map</code> must implement the entire contract for an unmodifiable
     * map as described in the JavaDocs for <code>java.util.Map</code>.  In
     * addition, key comparisons must be performed in a case insensitive
     * manner.</p>
     *
     * <p><em>Servlet:</em> This must be the set of headers available via
     * the <code>javax.servlet.http.HttpServletRequest</code> methods
     * <code>getHeader()</code> and <code>getHeaderNames()</code>.</p>
     *
     */
    public abstract Map<String, String> getRequestHeaderMap();
    

    /**
     * <p>Return an immutable <code>Map</code> whose keys are the set of
     * request header names included in the current request, and whose
     * values (of type String[]) are all of the value for each
     * header name returned by the underlying request.  The returned
     * <code>Map</code> must implement the entire contract for an unmodifiable
     * map as described in the JavaDocs for <code>java.util.Map</code>.  In
     * addition, key comparisons must be performed in a case insensitive
     * manner.</p>
     *
     * <p><em>Servlet:</em> This must be the set of headers available via
     * the <code>javax.servlet.http.HttpServletRequest</code> methods
     * <code>getHeaders()</code> and <code>getHeaderNames()</code>.</p>
     *
     */
    public abstract Map<String, String []> getRequestHeaderValuesMap();
    

    /**
     * <p>Return the preferred <code>Locale</code> in which the client
     * will accept content.</p>
     *
     * <p><em>Servlet:</em> This must be the value returned by the
     * <code>javax.servlet.ServletRequest</code> method
     * <code>getLocale()</code>.</p>
     *
     */
    public abstract Locale getRequestLocale();
    

    /**
     * <p>Return an <code>Iterator</code> over the preferred
     * <code>Locale</code>s specified in the request, in decreasing
     * order of preference.</p>
     *
     * <p><em>Servlet:</em> This must be an <code>Iterator</code>
     * over the values returned by the <code>javax.servlet.ServletRequest</code>
     * method <code>getLocales()</code>.</p>
     *
     */
    public abstract Iterator<Locale> getRequestLocales();


    /**
     * <p>Return a mutable <code>Map</code> representing the request
     * scope attributes for the current application.  The returned
     * <code>Map</code> must implement the entire contract for a
     * modifiable map as described in the JavaDocs for
     * <code>java.util.Map</code>.  Modifications made in the
     * <code>Map</code> must cause the corresponding changes in the set
     * of request scope attributes.  Particularly the
     * <code>clear()</code>, <code>remove()</code>, <code>put()</code>,
     * <code>putAll()</code>, and <code>get()</code> operations must
     * take the appropriate action on the underlying data structure.</p>
     *
     * <p>For any of the <code>Map</code> methods that cause an element
     * to be removed from the underlying data structure, the following
     * action regarding managed-beans must be taken.  If the element to
     * be removed is a managed-bean, and it has one or more public
     * no-argument void return methods annotated with
     * <code>javax.annotation.PreDestroy</code>, each such method must
     * be called before the element is removed from the underlying data
     * structure.  Elements that are not managed-beans, but do happen to
     * have methods with that annotation must not have those methods
     * called on removal.  Any exception thrown by the
     * <code>PreDestroy</code> annotated methods must by caught and not
     * rethrown.  The exception may be logged.</p>
     *
     * <p><em>Servlet:</em>  This must be the set of attributes available via
     * the <code>javax.servlet.ServletRequest</code> methods
     * <code>getAttribute()</code>, <code>getAttributeNames()</code>,
     * <code>removeAttribute()</code>, and <code>setAttribute()</code>.</p>
     *
     */
    public abstract Map<String, Object> getRequestMap();


    /**
     * <p>Return an immutable <code>Map</code> whose keys are the set of
     * request parameters names included in the current request, and whose
     * values (of type String) are the first (or only) value for each
     * parameter name returned by the underlying request.  The returned
     * <code>Map</code> must implement the entire contract for an unmodifiable
     * map as described in the JavaDocs for <code>java.util.Map</code>.</p>
     *
     * <p><em>Servlet:</em> This must be the set of parameters available via
     * the <code>javax.servlet.ServletRequest</code> methods
     * <code>getParameter()</code> and <code>getParameterNames()</code>.</p>
     *
     */
    public abstract Map<String, String> getRequestParameterMap();
    

    /**
     * <p>Return an <code>Iterator</code> over the names of all request
     * parameters included in the current request.</p>
     *
     * <p><em>Servlet:</em> This must be an <code>Iterator</code> over the
     * values returned by the <code>javax.servlet.ServletRequest</code>
     * method <code>getParameterNames()</code>.</p>
     *
     */
    public abstract Iterator<String> getRequestParameterNames();


    /**
     * <p>Return an immutable <code>Map</code> whose keys are the set of
     * request parameters names included in the current request, and whose
     * values (of type String[]) are all of the values for each
     * parameter name returned by the underlying request.  The returned
     * <code>Map</code> must implement the entire contract for an unmodifiable
     * map as described in the JavaDocs for <code>java.util.Map</code>.</p>
     *
     * <p><em>Servlet:</em> This must be the set of parameters available via
     * the <code>javax.servlet.ServletRequest</code> methods
     * <code>getParameterValues()</code> and
     * <code>getParameterNames()</code>.</p>
     *
     */
    public abstract Map<String, String []> getRequestParameterValuesMap();
    

    /**
     * <p>Return the extra path information (if any) included in the
     * request URI; otherwise, return <code>null</code>.</p>
     *
     * <p><em>Servlet:</em> This must be the value returned by the
     * <code>javax.servlet.http.HttpServletRequest</code> method
     * <code>getPathInfo()</code>.</p>
     *
     */
    public abstract String getRequestPathInfo();
    

    /**
     * <p>Return the servlet path information (if any) included in the
     * request URI; otherwise, return <code>null</code>.</p>
     *
     * <p><em>Servlet:</em> This must be the value returned by the
     * <code>javax.servlet.http.HttpServletRequest</code> method
     * <code>getServletPath()</code>.</p>
     *
     */
    public abstract String getRequestServletPath();
    
    /**
     *
     * <p> Return the character encoding currently being used
     * to interpret this request.</p>
     *
     * <p><em>Servlet:</em> This must return the value returned by the
     * <code>javax.servlet.ServletRequest</code> method
     * <code>getCharacterEncoding()</code>.</p>
     *
     * <p>The default implementation throws 
     * <code>UnsupportedOperationException</code> and is provided
     * for the sole purpose of not breaking existing applications that extend
     * this class.</p>
     *
     * @since 1.2
     *
     */
    public String getRequestCharacterEncoding() {

        ExternalContext impl = getDefaultExternalContext();
        if (impl != null) {
            return impl.getRequestCharacterEncoding();
        }
        throw new UnsupportedOperationException();

    }

    /**
     *
     * <p>Return the MIME Content-Type for this request.  If not
     * available, return <code>null</code>.</p>
     *
     * <p><em>Servlet:</em> This must return the value returned by the
     * <code>javax.servlet.ServletRequest</code> method
     * <code>getContentType()</code>.</p>
     *
     * <p>The default implementation throws 
     * <code>UnsupportedOperationException</code> and is provided
     * for the sole purpose of not breaking existing applications that extend
     * this class.</p>
     *
     * @since 1.2
     */
    public String getRequestContentType() {

        ExternalContext impl = getDefaultExternalContext();
        if (impl != null) {
            return impl.getRequestContentType();
        }

        throw new UnsupportedOperationException();

    }

    /**
     *
     * <p>Returns the name of the character encoding (MIME charset) used for 
     * the body sent in this response. </p>
     *
     * <p><em>Servlet:</em> This must return the value returned by the
     * <code>javax.servlet.ServletResponse</code> method
     * <code>getCharacterEncoding()</code>.</p>
     *
     * <p>The default implementation throws 
     * <code>UnsupportedOperationException</code> and is provided
     * for the sole purpose of not breaking existing applications that extend
     * this class.</p>
     *
     * @since 1.2
     */
    public String getResponseCharacterEncoding() {

        ExternalContext impl = getDefaultExternalContext();
        if (impl != null) {
            return impl.getResponseCharacterEncoding();
        }

        throw new UnsupportedOperationException();

    }

    
    /**
     *
     * <p>Return the MIME Content-Type for this response.  If not
     * available, return <code>null</code>.</p>
     *
     * <p><em>Servlet:</em> This must return the value returned by the
     * <code>javax.servlet.ServletResponse</code> method
     * <code>getContentType()</code>.</p>
     *
     * <p>The default implementation throws 
     * <code>UnsupportedOperationException</code> and is provided
     * for the sole purpose of not breaking existing applications that extend
     * this class.</p>
     *
     * @since 1.2
     */
    public String getResponseContentType() {

        ExternalContext impl = getDefaultExternalContext();
        if (impl != null) {
            return impl.getResponseContentType();
        }

        throw new UnsupportedOperationException();

    }

    

    /**
     * <p><span class="changed_modified_2_0">Return</span> a
     * <code>URL</code> for the application resource mapped to the
     * specified path, if it exists; otherwise, return
     * <code>null</code>.</p>
     *
     * <p class="changed_added_2_0">It is valid to call this method
     * during application startup.  If called during application
     * startup, this method calls through to the
     * <code>getResource()</code> method on the same container
     * context instance (<code>ServletContext</code> or
     * <code>PortletContext</code>) as the one used when calling
     * <code>getResource()</code> on the
     * <code>ExternalContext</code> returned by the
     * <code>FacesContext</code> during an actual request.</p>

     * <p><em>Servlet:</em> This must be the value returned by the
     * <code>javax.servlet.ServletContext</code> method
     * <code>getResource(path)</code>.</p>
     *
     * @param path The path to the requested resource, which must
     *  start with a slash ("/" character
     *
     * @throws MalformedURLException if the specified path
     *  is not in the correct form
     * @throws NullPointerException if <code>path</code>
     *  is <code>null</code>
     */
    public abstract URL getResource(String path) throws MalformedURLException;


    /**
     * <p><span class="changed_modified_2_0">Return</span> an
     * <code>InputStream</code> for an application resource mapped to
     * the specified path, if it exists; otherwise, return
     * <code>null</code>.</p>

     * <p class="changed_added_2_0">It is valid to call this method
     * during application startup.  If called during application
     * startup, this method calls through to the
     * <code>getResourceAsStream()</code> method on the same container
     * context instance (<code>ServletContext</code> or
     * <code>PortletContext</code>) as the one used when calling
     * <code>getResourceAsStream()</code> on the
     * <code>ExternalContext</code> returned by the
     * <code>FacesContext</code> during an actual request.</p>
     *
     * <p><em>Servlet:</em> This must be the value returned by the
     * <code>javax.servlet.ServletContext</code> method
     * <code>getResourceAsStream(path)</code>.</p>
     *
     * @param path The path to the requested resource, which must
     *  start with a slash ("/" character
     *
     * @throws NullPointerException if <code>path</code>
     *  is <code>null</code>
     */
    public abstract InputStream getResourceAsStream(String path);


    /**
     * <p><span class="changed_modified_2_0">Return</span> the
     * <code>Set</code> of resource paths for all application resources
     * whose resource path starts with the specified argument.</p>
     *
     * <p class="changed_added_2_0">It is valid to call this method
     * during application startup.  If called during application
     * startup, this method calls through to the
     * <code>getResourcePaths()</code> method on the same container
     * context instance (<code>ServletContext</code> or
     * <code>PortletContext</code>) as the one used when calling
     * <code>getResourcePaths()</code> on the
     * <code>ExternalContext</code> returned by the
     * <code>FacesContext</code> during an actual request.</p>

     * <p><em>Servlet:</em> This must be the value returned by the
     * <code>javax.servlet.ServletContext</code> method
     * <code>getResourcePaths(path).</code></p>
     *
     * @param path Partial path used to match resources, which must
     *  start with a slash ("/") character
     *
     * @throws NullPointerException if <code>path</code>
     *  is <code>null</code>
     */
    public abstract Set<String> getResourcePaths(String path);


    /**
     * <p>Return the environment-specific object instance for the current
     * response.</p>
     *
     * <p><em>Servlet:</em>  This is the current request's
     * <code>javax.servlet.http.HttpServletResponse</code> instance.</p>
     *
     */
    public abstract Object getResponse();

    /**
     * <p>Set the environment-specific response to be returned by
     * subsequent calls to {@link #getResponse}.  This may be used to
     * install a wrapper for the response.</p>
     *
     * <p>The default implementation throws 
     * <code>UnsupportedOperationException</code> and is provided
     * for the sole purpose of not breaking existing applications that extend
     * this class.</p>
     *
     *
     * @since 1.2
     */
    public void setResponse(Object response) {

        ExternalContext impl = getDefaultExternalContext();
        if (impl != null) {
            impl.setResponse(response);
            return;
        }
        
        throw new UnsupportedOperationException();

    }
    
    
    /**
     *
     * <p>Sets the character encoding (MIME charset) of the response being sent 
     * to the client, for example, to UTF-8.</p>
     *
     * <p><em>Servlet:</em> This must call through to the
     * <code>javax.servlet.ServletResponse</code> method
     * <code>setCharacterEncoding()</code>.</p>
     *
     * <p>The default implementation throws 
     * <code>UnsupportedOperationException</code> and is provided
     * for the sole purpose of not breaking existing applications that extend
     * this class.</p>
     *
     *
     * @since 1.2
     *
     */
    public void setResponseCharacterEncoding(String encoding) {

        ExternalContext impl = getDefaultExternalContext();
        if (impl != null) {
            impl.setResponseCharacterEncoding(encoding);
            return;
        }
        
        throw new UnsupportedOperationException();

    }
    

    /**
     * <p class="changed_added_2_0">Sets the content type of the
     * response being sent to the client, if the response has not been
     * committed yet.</p>
     *
     * <div class="changed_added_2_0">

     * <p><em>Servlet:</em> This must call
     * <code>setContentType()</code> on the underlying
     * <code>javax.servlet.ServletResponse</code> instance.</p>

     * <p>The default implementation throws
     * <code>UnsupportedOperationException</code> and is provided for
     * the sole purpose of not breaking existing applications that
     * extend this class.</p>

     * </div>

     * @since 2.0
     */

    public void setResponseContentType(String contentType) {
        ExternalContext impl = getDefaultExternalContext();
        if (impl != null) {
            impl.setResponseContentType(contentType);
            return;
        }

        throw new UnsupportedOperationException();
    }


    /**
     * <p>If the <code>create</code> parameter is <code>true</code>,
     * create (if necessary) and return a session instance associated
     * with the current request.  If the <code>create</code> parameter
     * is <code>false</code> return any existing session instance
     * associated with the current request, or return <code>null</code> if
     * there is no such session.</p>
     *
     * <p><em>Servlet:</em> This must return the result of calling
     * <code>getSession(create)</code> on the underlying
     * <code>javax.servlet.http.HttpServletRequest</code> instance.</p>
     *
     * @param create Flag indicating whether or not a new session should be
     *  created if there is no session associated with the current request
     */
    public abstract Object getSession(boolean create);


    /**
     * <p>Return a mutable <code>Map</code> representing the session
     * scope attributes for the current application.  The returned
     * <code>Map</code> must implement the entire contract for a
     * modifiable map as described in the JavaDocs for
     * <code>java.util.Map</code>.  Modifications made in the
     * <code>Map</code> must cause the corresponding changes in the set
     * of session scope attributes.  Particularly the
     * <code>clear()</code>, <code>remove()</code>, <code>put()</code>,
     * and <code>get()</code> operations must take the appropriate
     * action on the underlying data structure.  Accessing attributes
     * via this <code>Map</code> must cause the creation of a session
     * associated with the current request, if such a session does not
     * already exist.</p>
     *
     * <p>For any of the <code>Map</code> methods that cause an element
     * to be removed from the underlying data structure, the following
     * action regarding managed-beans must be taken.  If the element to
     * be removed is a managed-bean, and it has one or more public
     * no-argument void return methods annotated with
     * <code>javax.annotation.PreDestroy</code>, each such method must
     * be called before the element is removed from the underlying data
     * structure.  Elements that are not managed-beans, but do happen to
     * have methods with that annotation must not have those methods
     * called on removal.  Any exception thrown by the
     * <code>PreDestroy</code> annotated methods must by caught and not
     * rethrown.  The exception may be logged.</p>
     *
     * <p><em>Servlet:</em>  This must be the set of attributes available via
     * the <code>javax.servlet.http.HttpServletSession</code> methods
     * <code>getAttribute()</code>, <code>getAttributeNames()</code>,
     * <code>removeAttribute()</code>, and <code>setAttribute()</code>.</p>
     *
     */
    public abstract Map<String, Object> getSessionMap();


    /**
     * <p>Return the <code>Principal</code> object containing the name of
     * the current authenticated user, if any; otherwise, return
     * <code>null</code>.</p>
     *
     * <p><em>Servlet:</em> This must be the value returned by the
     * <code>javax.servlet.http.HttpServletRequest</code> method
     * <code>getUserPrincipal()</code>.</p>
     *
     */
    public abstract Principal getUserPrincipal();


    /**
     * <p>Return <code>true</code> if the currently authenticated user is
     * included in the specified role.  Otherwise, return <code>false</code>.
     * </p>
     *
     * <p><em>Servlet:</em> This must be the value returned by the
     * <code>javax.servlet.http.HttpServletRequest</code> method
     * <code>isUserInRole(role)</code>.</p>
     *
     * @param role Logical role name to be checked
     *
     * @throws NullPointerException if <code>role</code>
     *  is <code>null</code>
     */
    public abstract boolean isUserInRole(String role);


    /**
     * <p><span class="changed_modified_2_0">Log</span> the specified
     * message to the application object.</p>

     * <p class="changed_added_2_0">It is valid to call this method
     * during application startup.  If called during application
     * startup, this calls the <code>log()</code> method on the same
     * container context instance (<code>ServletContext</code> or
     * <code>PortletContext</code>) as the one used during a call to
     * <code>log()</code> on the <code>ExternalContext</code> returned
     * by the <code>FacesContext</code> during an actual request.</p>

     *
     * <p><em>Servlet:</em> This must be performed by calling the
     * <code>javax.servlet.ServletContext</code> method
     * <code>log(String)</code>.</p>
     *
     * @param message Message to be logged
     *
     * @throws NullPointerException if <code>message</code>
     *  is <code>null</code>
     */
    public abstract void log(String message);


    /**
     * <p><span class="changed_modified_2_0">Log</span> the specified
     * message and exception to the application object.</p>

     * <p class="changed_added_2_0">It is valid to call this method
     * during application startup.  If called during application
     * startup, this calls the <code>log()</code> method on the same
     * container context instance (<code>ServletContext</code> or
     * <code>PortletContext</code>) as the one used when calling
     * <code>log()</code> on the <code>ExternalContext</code> returned
     * by the <code>FacesContext</code> during an actual request.</p>
     *
     * <p><em>Servlet:</em> This must be performed by calling the
     * <code>javax.servlet.ServletContext</code> method
     * <code>log(String,Throwable)</code>.</p>
     *
     * @param message Message to be logged
     * @param exception Exception to be logged
     *
     * @throws NullPointerException if <code>message</code>
     *  or <code>exception</code> is <code>null</code>
     */
    public abstract void log(String message, Throwable exception);


    /**
     * <p>Redirect a request to the specified URL, and cause the
     * <code>responseComplete()</code> method to be called on the
     * {@link FacesContext} instance for the current request.</p>
     *
     * <p><em>Servlet:</em> This must be accomplished by calling the
     * <code>javax.servlet.http.HttpServletResponse</code> method
     * <code>sendRedirect()</code>.</p>
     *
     * @param url Absolute URL to which the client should be redirected
     *
     * @throws IllegalArgumentException if the specified url is relative
     * @throws IllegalStateException if, in a portlet environment,
     *  the current response object is a <code>RenderResponse</code>
     *  instead of an <code>ActionResponse</code>
     * @throws IllegalStateException if, in a servlet environment,
     *  the current response has already been committed
     * @throws IOException if an input/output error occurs
     */
    public abstract void redirect(String url)
	throws IOException;


    // --------------------------------------------------------- Private Methods

    private ExternalContext getDefaultExternalContext() {
        ExternalContext extCtx = null;
        Map m = (Map) getRequestMap().get("com.sun.faces.util.RequestStateManager");
        if (m != null) {
            extCtx = (ExternalContext) m.get("com.sun.faces.ExternalContextImpl");
        }
        return extCtx;

    }
}
