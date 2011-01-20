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

package javax.faces.context;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.*;
import java.net.URL;
import java.net.MalformedURLException;
import java.security.Principal;

import javax.faces.FacesWrapper;

/**
 * <p class="changed_added_2_0">Provides a simple implementation of 
 * {@link ExternalContext} that can
 * be subclassed by developers wishing to provide specialized behavior
 * to an existing {@link ExternalContext} instance.  The default
 * implementation of all methods is to call through to the wrapped
 * {@link ExternalContext} instance.</p>
 *
 * <p class="changed_added_2_0">Usage: extend this class and override 
 * {@link #getWrapped} to
 * return the instance being wrapping.</p>
 *
 * @since 2.0
 */
public abstract class ExternalContextWrapper extends ExternalContext implements FacesWrapper<ExternalContext> {


    // ----------------------------------------------- Methods from FacesWrapper


    /**
     * @return the wrapped {@link ExternalContext} instance
     * @see javax.faces.FacesWrapper#getWrapped()
     */
    public abstract ExternalContext getWrapped();


    // -------------------------------------------- Methods from ExternalContext


    /**
     * <p>The default behavior of this method is to
     * call {@link ExternalContext#dispatch(String)}
     * on the wrapped {@link ExternalContext} object.</p>
     *
     * @see javax.faces.context.ExternalContext#dispatch(String)
     */
    public void dispatch(String path) throws IOException {
        getWrapped().dispatch(path);
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ExternalContext#encodeActionURL(String)}
     * on the wrapped {@link ExternalContext} object.</p>
     *
     * @see javax.faces.context.ExternalContext#encodeActionURL(String)
     */
    public String encodeActionURL(String url) {
        return getWrapped().encodeActionURL(url);
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ExternalContext#encodeNamespace(String)}
     * on the wrapped {@link ExternalContext} object.</p>
     *
     * @see javax.faces.context.ExternalContext#encodeNamespace(String)
     */
    public String encodeNamespace(String name) {
        return getWrapped().encodeNamespace(name);
    }

    @Override
    public String encodePartialActionURL(String url) {
        return getWrapped().encodePartialActionURL(url);
    }
    
    
    /**
     * <p>The default behavior of this method is to
     * call {@link ExternalContext#encodeResourceURL(String)}
     * on the wrapped {@link ExternalContext} object.</p>
     *
     * @see javax.faces.context.ExternalContext#encodeResourceURL(String)
     */
    public String encodeResourceURL(String url) {
        return getWrapped().encodeResourceURL(url);
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ExternalContext#getApplicationMap}
     * on the wrapped {@link ExternalContext} object.</p>
     *
     * @see javax.faces.context.ExternalContext#getApplicationMap()
     */
    public Map<String, Object> getApplicationMap() {
        return getWrapped().getApplicationMap();
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ExternalContext#getAuthType}
     * on the wrapped {@link ExternalContext} object.</p>
     *
     * @see javax.faces.context.ExternalContext#getAuthType()
     */
    public String getAuthType() {
        return getWrapped().getAuthType();
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ExternalContext#getContext}
     * on the wrapped {@link ExternalContext} object.</p>
     *
     * @see javax.faces.context.ExternalContext#getContext()
     */
    public Object getContext() {
        return getWrapped().getContext();
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ExternalContext#getInitParameter(String)}
     * on the wrapped {@link ExternalContext} object.</p>
     *
     * @see javax.faces.context.ExternalContext#getInitParameter(String)
     */
    public String getInitParameter(String name) {
        return getWrapped().getInitParameter(name);
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ExternalContext#getInitParameterMap}
     * on the wrapped {@link ExternalContext} object.</p>
     *
     * @see javax.faces.context.ExternalContext#getInitParameterMap()
     */
    public Map getInitParameterMap() {
        return getWrapped().getInitParameterMap();
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ExternalContext#getRemoteUser}
     * on the wrapped {@link ExternalContext} object.</p>
     *
     * @see javax.faces.context.ExternalContext#getRemoteUser()
     */
    public String getRemoteUser() {
        return getWrapped().getRemoteUser();
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ExternalContext#getRequest}
     * on the wrapped {@link ExternalContext} object.</p>
     *
     * @see javax.faces.context.ExternalContext#getRequest()
     */
    public Object getRequest() {
        return getWrapped().getRequest();
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ExternalContext#getRequestContextPath}
     * on the wrapped {@link ExternalContext} object.</p>
     *
     * @see javax.faces.context.ExternalContext#getRequestContextPath() 
     */
    public String getRequestContextPath() {
        return getWrapped().getRequestContextPath();
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ExternalContext#getRequestCookieMap}
     * on the wrapped {@link ExternalContext} object.</p>
     *
     * @see javax.faces.context.ExternalContext#getRequestCookieMap()
     */
    public Map<String, Object> getRequestCookieMap() {
        return getWrapped().getRequestCookieMap();
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ExternalContext#getRequestHeaderMap}
     * on the wrapped {@link ExternalContext} object.</p>
     *
     * @see javax.faces.context.ExternalContext#getRequestHeaderMap()
     */
    public Map<String, String> getRequestHeaderMap() {
        return getWrapped().getRequestHeaderMap();
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ExternalContext#getRequestHeaderValuesMap}
     * on the wrapped {@link ExternalContext} object.</p>
     *
     * @see javax.faces.context.ExternalContext#getRequestHeaderValuesMap()
     */
    public Map<String, String[]> getRequestHeaderValuesMap() {
        return getWrapped().getRequestHeaderValuesMap();
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ExternalContext#getRequestLocale}
     * on the wrapped {@link ExternalContext} object.</p>
     *
     * @see javax.faces.context.ExternalContext#getRequestLocale() 
     */
    public Locale getRequestLocale() {
        return getWrapped().getRequestLocale();
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ExternalContext#getRequestLocales}
     * on the wrapped {@link ExternalContext} object.</p>
     *
     * @see javax.faces.context.ExternalContext#getRequestLocales()
     */
    public Iterator<Locale> getRequestLocales() {
        return getWrapped().getRequestLocales();
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ExternalContext#getRequestMap}
     * on the wrapped {@link ExternalContext} object.</p>
     *
     * @see javax.faces.context.ExternalContext#getRequestMap() 
     */
    public Map<String, Object> getRequestMap() {
        return getWrapped().getRequestMap();
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ExternalContext#getRequestParameterMap}
     * on the wrapped {@link ExternalContext} object.</p>
     *
     * @see javax.faces.context.ExternalContext#getRequestParameterMap()
     */
    public Map<String, String> getRequestParameterMap() {
        return getWrapped().getRequestParameterMap();
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ExternalContext#getRequestParameterNames}
     * on the wrapped {@link ExternalContext} object.</p>
     *
     * @see javax.faces.context.ExternalContext#getRequestParameterNames()
     */
    public Iterator<String> getRequestParameterNames() {
        return getWrapped().getRequestParameterNames();
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ExternalContext#getRequestParameterValuesMap}
     * on the wrapped {@link ExternalContext} object.</p>
     *
     * @see javax.faces.context.ExternalContext#getRequestParameterValuesMap()
     */
    public Map<String, String[]> getRequestParameterValuesMap() {
        return getWrapped().getRequestParameterValuesMap();
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ExternalContext#getRequestPathInfo}
     * on the wrapped {@link ExternalContext} object.</p>
     *
     * @see javax.faces.context.ExternalContext#getRequestPathInfo()
     */
    public String getRequestPathInfo() {
        return getWrapped().getRequestPathInfo();
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ExternalContext#getRequestServletPath}
     * on the wrapped {@link ExternalContext} object.</p>
     *
     * @see javax.faces.context.ExternalContext#getRequestServletPath()
     */
    public String getRequestServletPath() {
        return getWrapped().getRequestServletPath();
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ExternalContext#getResource(String)}
     * on the wrapped {@link ExternalContext} object.</p>
     *
     * @see javax.faces.context.ExternalContext#getResource(String) 
     */
    public URL getResource(String path) throws MalformedURLException {
        return getWrapped().getResource(path);
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ExternalContext#getResourceAsStream(String)}
     * on the wrapped {@link ExternalContext} object.</p>
     *
     * @see javax.faces.context.ExternalContext#getResourceAsStream(String)
     */
    public InputStream getResourceAsStream(String path) {
        return getWrapped().getResourceAsStream(path);
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ExternalContext#getResourcePaths(String)}
     * on the wrapped {@link ExternalContext} object.</p>
     *
     * @see javax.faces.context.ExternalContext#getResourcePaths(String)
     */
    public Set<String> getResourcePaths(String path) {
        return getWrapped().getResourcePaths(path);
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ExternalContext#getResponse}
     * on the wrapped {@link ExternalContext} object.</p>
     *
     * @see javax.faces.context.ExternalContext#getResponse()
     */
    public Object getResponse() {
        return getWrapped().getResponse();
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ExternalContext#getSession(boolean)}
     * on the wrapped {@link ExternalContext} object.</p>
     *
     * @see javax.faces.context.ExternalContext#getSession(boolean)
     */
    public Object getSession(boolean create) {
        return getWrapped().getSession(create);
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ExternalContext#getAuthType}
     * on the wrapped {@link ExternalContext} object.</p>
     *
     * @see javax.faces.context.ExternalContext#getAuthType()
     */
    public Map<String, Object> getSessionMap() {
        return getWrapped().getSessionMap();
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ExternalContext#getUserPrincipal}
     * on the wrapped {@link ExternalContext} object.</p>
     *
     * @see javax.faces.context.ExternalContext#getUserPrincipal()
     */
    public Principal getUserPrincipal() {
        return getWrapped().getUserPrincipal();
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ExternalContext#isUserInRole(String)}
     * on the wrapped {@link ExternalContext} object.</p>
     *
     * @see javax.faces.context.ExternalContext#isUserInRole(String)
     */
    public boolean isUserInRole(String role) {
        return getWrapped().isUserInRole(role);
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ExternalContext#log(String)}
     * on the wrapped {@link ExternalContext} object.</p>
     *
     * @see javax.faces.context.ExternalContext#log(String)
     */
    public void log(String message) {
        getWrapped().log(message);
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ExternalContext#log(String, Throwable)}
     * on the wrapped {@link ExternalContext} object.</p>
     *
     * @see javax.faces.context.ExternalContext#log(String, Throwable)
     */
    public void log(String message, Throwable exception) {
        getWrapped().log(message, exception);
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ExternalContext#redirect(String)}
     * on the wrapped {@link ExternalContext} object.</p>
     *
     * @see javax.faces.context.ExternalContext#redirect(String)
     */
    public void redirect(String url) throws IOException {
        getWrapped().redirect(url);
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ExternalContext#addResponseCookie(String, String, Map)}
     * on the wrapped {@link ExternalContext} object.</p>
     *
     * @see javax.faces.context.ExternalContext#addResponseCookie(String, String, Map)
     */
    @Override
    public void addResponseCookie(String name,
                                  String value,
                                  Map<String, Object> properties) {
        getWrapped().addResponseCookie(name, value, properties);
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ExternalContext#getMimeType(String)}
     * on the wrapped {@link ExternalContext} object.</p>
     *
     * @see javax.faces.context.ExternalContext#getMimeType(String)
     */
    @Override
    public String getMimeType(String file) {
        return getWrapped().getMimeType(file);
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ExternalContext#getContextName}
     * on the wrapped {@link ExternalContext} object.</p>
     *
     * @see javax.faces.context.ExternalContext#getContextName()
     */
    @Override
    public String getContextName() {
        return getWrapped().getContextName();
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ExternalContext#setRequest(Object)}
     * on the wrapped {@link ExternalContext} object.</p>
     *
     * @see javax.faces.context.ExternalContext#setRequest(Object)
     */
    @Override
    public void setRequest(Object request) {
        getWrapped().setRequest(request);
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ExternalContext#getRequestScheme}
     * on the wrapped {@link ExternalContext} object.</p>
     *
     * @see javax.faces.context.ExternalContext#getRequestScheme()
     */
    @Override
    public String getRequestScheme() {
        return getWrapped().getRequestScheme();
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ExternalContext#getRequestServerName}
     * on the wrapped {@link ExternalContext} object.</p>
     *
     * @see javax.faces.context.ExternalContext#getRequestServerName()
     */
    @Override
    public String getRequestServerName() {
        return getWrapped().getRequestServerName();
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ExternalContext#getRequestServerPort}
     * on the wrapped {@link ExternalContext} object.</p>
     *
     * @see javax.faces.context.ExternalContext#getRequestServerPort()
     */
    @Override
    public int getRequestServerPort() {
        return getWrapped().getRequestServerPort();
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ExternalContext#setRequestCharacterEncoding(String)}
     * on the wrapped {@link ExternalContext} object.</p>
     *
     * @see javax.faces.context.ExternalContext#setRequestCharacterEncoding(String)
     */
    @Override
    public void setRequestCharacterEncoding(String encoding)
    throws UnsupportedEncodingException {
        getWrapped().setRequestCharacterEncoding(encoding);
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ExternalContext#getRealPath(String)}
     * on the wrapped {@link ExternalContext} object.</p>
     *
     * @see javax.faces.context.ExternalContext#getRealPath(String)
     */
    @Override
    public String getRealPath(String path) {
        return getWrapped().getRealPath(path);
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ExternalContext#getRequestCharacterEncoding}
     * on the wrapped {@link ExternalContext} object.</p>
     *
     * @see javax.faces.context.ExternalContext#getRequestCharacterEncoding()
     */
    @Override
    public String getRequestCharacterEncoding() {
        return getWrapped().getRequestCharacterEncoding();
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ExternalContext#getRequestContentType}
     * on the wrapped {@link ExternalContext} object.</p>
     *
     * @see javax.faces.context.ExternalContext#getRequestContentType()
     */
    @Override
    public String getRequestContentType() {
        return getWrapped().getRequestContentType();
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ExternalContext#getRequestContentLength}
     * on the wrapped {@link ExternalContext} object.</p>
     *
     * @see javax.faces.context.ExternalContext#getRequestContentLength()
     */
    @Override
    public int getRequestContentLength() {
        return getWrapped().getRequestContentLength();
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ExternalContext#getResponseCharacterEncoding}
     * on the wrapped {@link ExternalContext} object.</p>
     *
     * @see javax.faces.context.ExternalContext#getResponseCharacterEncoding()
     */
    @Override
    public String getResponseCharacterEncoding() {
        return getWrapped().getResponseCharacterEncoding();
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ExternalContext#getResponseContentType}
     * on the wrapped {@link ExternalContext} object.</p>
     *
     * @see javax.faces.context.ExternalContext#getResponseContentType()
     */
    @Override
    public String getResponseContentType() {
        return getWrapped().getResponseContentType();
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ExternalContext#setResponse(Object)}
     * on the wrapped {@link ExternalContext} object.</p>
     *
     * @see javax.faces.context.ExternalContext#setResponse(Object)
     */
    @Override
    public void setResponse(Object response) {
        getWrapped().setResponse(response);
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ExternalContext#getResponseOutputStream}
     * on the wrapped {@link ExternalContext} object.</p>
     *
     * @see javax.faces.context.ExternalContext#getResponseOutputStream()
     */
    @Override
    public OutputStream getResponseOutputStream() throws IOException {
        return getWrapped().getResponseOutputStream();
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ExternalContext#getResponseOutputWriter}
     * on the wrapped {@link ExternalContext} object.</p>
     *
     * @see javax.faces.context.ExternalContext#getResponseOutputWriter()
     */
    @Override
    public Writer getResponseOutputWriter() throws IOException {
        return getWrapped().getResponseOutputWriter();
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ExternalContext#getResponseCharacterEncoding}
     * on the wrapped {@link ExternalContext} object.</p>
     *
     * @see javax.faces.context.ExternalContext#getResponseCharacterEncoding()
     */
    @Override
    public void setResponseCharacterEncoding(String encoding) {
        getWrapped().setResponseCharacterEncoding(encoding);
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ExternalContext#setResponseContentType(String)}
     * on the wrapped {@link ExternalContext} object.</p>
     *
     * @see javax.faces.context.ExternalContext#setResponseContentType(String)
     */
    @Override
    public void setResponseContentType(String contentType) {
        getWrapped().setResponseContentType(contentType);
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ExternalContext#invalidateSession}
     * on the wrapped {@link ExternalContext} object.</p>
     *
     * @see javax.faces.context.ExternalContext#invalidateSession()
     */
    @Override
    public void invalidateSession() {
        getWrapped().invalidateSession();
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ExternalContext#setResponseHeader(String,String)}
     * on the wrapped {@link ExternalContext} object.</p>
     *
     * @see javax.faces.context.ExternalContext#setResponseHeader(String,String)
     */
    @Override
    public void setResponseHeader(String name, String value) {
        getWrapped().setResponseHeader(name, value);
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ExternalContext#addResponseHeader(String,String)}
     * on the wrapped {@link ExternalContext} object.</p>
     *
     * @see javax.faces.context.ExternalContext#addResponseHeader(String,String)
     */
    @Override
    public void addResponseHeader(String name, String value) {
        getWrapped().addResponseHeader(name, value);
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ExternalContext#setResponseBufferSize(int)}
     * on the wrapped {@link ExternalContext} object.</p>
     *
     * @see javax.faces.context.ExternalContext#setResponseBufferSize(int)
     */
    @Override
    public void setResponseBufferSize(int size) {
        getWrapped().setResponseBufferSize(size);   
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ExternalContext#getResponseBufferSize()}
     * on the wrapped {@link ExternalContext} object.</p>
     *
     * @see javax.faces.context.ExternalContext#getResponseBufferSize()
     */
    @Override
    public int getResponseBufferSize() {
        return getWrapped().getResponseBufferSize();
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ExternalContext#isResponseCommitted()}
     * on the wrapped {@link ExternalContext} object.</p>
     *
     * @see javax.faces.context.ExternalContext#isResponseCommitted()
     */
    @Override
    public boolean isResponseCommitted() {
        return getWrapped().isResponseCommitted();
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ExternalContext#responseReset()}
     * on the wrapped {@link ExternalContext} object.</p>
     *
     * @see javax.faces.context.ExternalContext#responseReset()
     */
    @Override
    public void responseReset() {
        getWrapped().responseReset();
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ExternalContext#responseSendError(int,String)}
     * on the wrapped {@link ExternalContext} object.</p>
     *
     * @see javax.faces.context.ExternalContext#responseSendError(int,String)
     */
    @Override
    public void responseSendError(int statusCode, String message) throws IOException {
        getWrapped().responseSendError(statusCode, message);
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ExternalContext#setResponseStatus(int)}
     * on the wrapped {@link ExternalContext} object.</p>
     *
     * @see javax.faces.context.ExternalContext#setResponseStatus(int)
     */
    @Override
    public void setResponseStatus(int statusCode) {
        getWrapped().setResponseStatus(statusCode);    
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link javax.faces.context.ExternalContext#responseFlushBuffer()}
     * on the wrapped {@link ExternalContext} object.</p>
     *
     * @see javax.faces.context.ExternalContext#responseFlushBuffer()
     */
    @Override
    public void responseFlushBuffer() throws IOException {
        getWrapped().responseFlushBuffer();
    }


    /**
     * <p>The default behavior of this method is to
     * call {@link javax.faces.context.ExternalContext#setResponseContentLength(int)}
     * on the wrapped {@link ExternalContext} object.</p>
     *
     * @see javax.faces.context.ExternalContext#setResponseContentLength(int)
     */
    @Override
    public void setResponseContentLength(int length) {
        getWrapped().setResponseContentLength(length);
    }


    /**
     * <p>The default behavior of this method is to
     * call {@link javax.faces.context.ExternalContext#encodeBookmarkableURL(String, java.util.Map)}
     * on the wrapped {@link ExternalContext} object.</p>
     *
     * @see javax.faces.context.ExternalContext#encodeBookmarkableURL(String, java.util.Map)
     */
    @Override
    public String encodeBookmarkableURL(String baseUrl, Map<String, List<String>> parameters) {
        return getWrapped().encodeBookmarkableURL(baseUrl, parameters);
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link javax.faces.context.ExternalContext#encodeRedirectURL(String, java.util.Map)}
     * on the wrapped {@link ExternalContext} object.</p>
     *
     * @see javax.faces.context.ExternalContext#encodeRedirectURL(String, java.util.Map)
     */
    @Override
    public String encodeRedirectURL(String baseUrl, Map<String, List<String>> parameters) {
        return getWrapped().encodeRedirectURL(baseUrl, parameters);
    }


    /**
     * <p>The default behavior of this method is to
     * call {@link ExternalContext#getFlash()} on the wrapped {@link ExternalContext}
     * object.</p?
     *
     * @see javax.faces.context.ExternalContext#getFlash()
     */
    @Override
    public Flash getFlash() {
        return getWrapped().getFlash();
    }
    
}
