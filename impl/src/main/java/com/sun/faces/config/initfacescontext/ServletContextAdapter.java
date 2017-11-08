/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2017 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.faces.config.initfacescontext;

import static com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter.EnableTransitionTimeNoOpFlash;
import static java.lang.Boolean.parseBoolean;
import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.faces.context.ExternalContext;
import javax.faces.context.Flash;
import javax.servlet.ServletContext;

import com.sun.faces.context.ApplicationMap;
import com.sun.faces.context.InitParameterMap;

public class ServletContextAdapter extends ExternalContext {

    private ServletContext servletContext;
    private ApplicationMap applicationMap;
    private InitParameterMap initMap;
    private boolean isEnableTransitionTimeNoOpFlash = EnableTransitionTimeNoOpFlash.getDefaultValue();

    public ServletContextAdapter(ServletContext servletContext) {
        this.servletContext = servletContext;

        String paramValue = servletContext.getInitParameter(EnableTransitionTimeNoOpFlash.getQualifiedName());

        if (paramValue != null) {
            isEnableTransitionTimeNoOpFlash = parseBoolean(paramValue);
        }
    }

    @Override
    public void dispatch(String path) throws IOException {
    }

    public void release() {
        servletContext = null;
        applicationMap = null;
        initMap = null;
    }

    @Override
    public String encodeActionURL(String url) {
        return null;
    }

    @Override
    public String encodeNamespace(String name) {
        return null;
    }

    @Override
    public String encodeResourceURL(String url) {
        return null;
    }

    @Override
    public String encodeWebsocketURL(String url) {
        return null;
    }

    @Override
    public Map<String, Object> getApplicationMap() {
        if (applicationMap == null) {
            applicationMap = new ApplicationMap(servletContext);
        }

        return applicationMap;
    }

    @Override
    public Flash getFlash() {
        if (isEnableTransitionTimeNoOpFlash) {
            return new NoOpFlash();
        } else {
            return super.getFlash();
        }

    }

    @Override
    public String getApplicationContextPath() {
        return servletContext.getContextPath();
    }

    @Override
    public String getAuthType() {
        return null;
    }

    @Override
    public String getMimeType(String file) {
        return servletContext.getMimeType(file);
    }

    @Override
    public Object getContext() {
        return servletContext;
    }

    @Override
    public String getContextName() {
        return servletContext.getServletContextName();
    }

    @Override
    public String getInitParameter(String name) {
        return servletContext.getInitParameter(name);
    }

    @Override
    public Map<String, String> getInitParameterMap() {
        if (initMap == null) {
            initMap = new InitParameterMap(servletContext);
        }

        return initMap;
    }

    @Override
    public String getRemoteUser() {
        return null;
    }

    @Override
    public Object getRequest() {
        return null;
    }

    @Override
    public void setRequest(Object request) {
    }

    @Override
    public String getRequestContextPath() {
        return null;
    }

    @Override
    public Map<String, Object> getRequestCookieMap() {
        return unmodifiableMap(emptyMap());
    }

    @Override
    public Map<String, String> getRequestHeaderMap() {
        return unmodifiableMap(emptyMap());
    }

    @Override
    public Map<String, String[]> getRequestHeaderValuesMap() {
        return unmodifiableMap(emptyMap());
    }

    @Override
    public Locale getRequestLocale() {
        return null;
    }

    @Override
    public Iterator<Locale> getRequestLocales() {
        return null;
    }

    @Override
    public Map<String, Object> getRequestMap() {
        return emptyMap();
    }

    @Override
    public Map<String, String> getRequestParameterMap() {
        return unmodifiableMap(emptyMap());
    }

    @Override
    public Iterator<String> getRequestParameterNames() {
        return Collections.<String>emptyList().iterator();
    }

    @Override
    public Map<String, String[]> getRequestParameterValuesMap() {
        return unmodifiableMap(emptyMap());
    }

    @Override
    public String getRequestPathInfo() {
        return null;
    }

    @Override
    public String getRequestServletPath() {
        return null;
    }

    @Override
    public String getRequestContentType() {
        return null;
    }

    @Override
    public String getResponseContentType() {
        return null;
    }

    @Override
    public int getRequestContentLength() {
        return -1;
    }

    @Override
    public URL getResource(String path) throws MalformedURLException {
        return servletContext.getResource(path);
    }

    @Override
    public InputStream getResourceAsStream(String path) {
        return servletContext.getResourceAsStream(path);
    }

    @Override
    public Set<String> getResourcePaths(String path) {
        return servletContext.getResourcePaths(path);
    }

    @Override
    public Object getResponse() {
        return null;
    }

    @Override
    public void setResponse(Object response) {
    }

    @Override
    public Object getSession(boolean create) {
        return null;
    }

    @Override
    public Map<String, Object> getSessionMap() {
        return emptyMap();
    }

    @Override
    public java.security.Principal getUserPrincipal() {
        return null;
    }

    @Override
    public boolean isUserInRole(String role) {
        return false;
    }

    @Override
    public void log(String message) {
        servletContext.log(message);
    }

    @Override
    public void log(String message, Throwable exception) {
        servletContext.log(message, exception);
    }

    @Override
    public void redirect(String url) throws IOException {
    }

    @Override
    public String getRequestCharacterEncoding() {
        return null;
    }

    @Override
    public void setRequestCharacterEncoding(String requestCharacterEncoding) throws UnsupportedEncodingException {
    }

    @Override
    public String getResponseCharacterEncoding() {
        return null;
    }

    @Override
    public void setResponseCharacterEncoding(String responseCharacterEncoding) {
    }

    @Override
    public void setResponseHeader(String name, String value) {
    }

    @Override
    public void addResponseHeader(String name, String value) {
    }

    @Override
    public String encodePartialActionURL(String url) {
        return null;
    }

    @Override
    public String getRealPath(String path) {
        return servletContext.getRealPath(path);
    }

}
