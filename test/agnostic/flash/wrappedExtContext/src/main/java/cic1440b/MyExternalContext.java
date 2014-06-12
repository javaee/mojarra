/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2014 Oracle and/or its affiliates. All rights reserved.
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

package cic1440b;

import java.security.Principal;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import javax.faces.context.ExternalContext;
import javax.faces.context.ExternalContextWrapper;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class MyExternalContext extends ExternalContextWrapper {

	private final ExternalContext wrapped;

	public MyExternalContext(ExternalContext wrapped) {
		this.wrapped = wrapped;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ExternalContext getWrapped() {
		return wrapped;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getRequestContextPath() {

		return wrapped.getRequestContextPath();
	}

    @Override
    public Object getRequest() {
        final HttpServletRequest wrappedRequest = (HttpServletRequest) super.getRequest();
        Object result = new MyPortletRequest() {

            public boolean isWindowStateAllowed(Object state) {
                return false;
            }

            public boolean isPortletModeAllowed(Object mode) {
                return false;
            }

            public Object getObject() {
                return this;
            }

            public Object getPreferences() {
                return this;
            }

            public Object getObject(boolean create) {
                return this;
            }

            public String getProperty(String name) {
                return (String) wrappedRequest.getAttribute(name);
            }

            public Enumeration<String> getProperties(String name) {
                return wrappedRequest.getAttributeNames();
            }

            public Enumeration<String> getPropertyNames() {
                return wrappedRequest.getAttributeNames();
            }

            public Object getPortalContext() {
                return this;
            }

            public String getAuthType() {
                return wrappedRequest.getAuthType();
            }

            public String getContextPath() {
                return wrappedRequest.getContextPath();
            }

            public String getRemoteUser() {
                return wrappedRequest.getRemoteUser();
            }

            public Principal getUserPrincipal() {
                return wrappedRequest.getUserPrincipal();
            }

            public boolean isUserInRole(String role) {
                return wrappedRequest.isUserInRole(role);
            }

            public Object getAttribute(String name) {
                return wrappedRequest.getAttribute(name);
            }

            public Enumeration<String> getAttributeNames() {
                return wrappedRequest.getAttributeNames();
            }

            public String getParameter(String name) {
                return wrappedRequest.getParameter(name);
            }

            public Enumeration<String> getParameterNames() {
                return wrappedRequest.getParameterNames();
            }

            public String[] getParameterValues(String name) {
                return wrappedRequest.getParameterValues(name);
            }

            public Map<String, String[]> getParameterMap() {
                return wrappedRequest.getParameterMap();
            }

            public boolean isSecure() {
                return wrappedRequest.isSecure();
            }

            public void setAttribute(String name, Object o) {
                wrappedRequest.setAttribute(name, o);
            }

            public void removeAttribute(String name) {
                wrappedRequest.removeAttribute(name);
            }

            public String getRequestedSessionId() {
                return wrappedRequest.getSession().getId();
            }

            public boolean isRequestedSessionIdValid() {
                return true;
            }

            public String getResponseContentType() {
                return wrappedRequest.getContentType(); // lie
            }

            public Enumeration<String> getResponseContentTypes() {
                return wrappedRequest.getHeaderNames(); // lie
            }

            public Locale getLocale() {
                return wrappedRequest.getLocale();
            }

            public Enumeration<Locale> getLocales() {
                return wrappedRequest.getLocales();
            }

            public String getScheme() {
                return wrappedRequest.getScheme();
            }

            public String getServerName() {
                return wrappedRequest.getServerName();
            }

            public int getServerPort() {
                return wrappedRequest.getServerPort();
            }

            public String getWindowID() {
                return "";
            }

            public Cookie[] getCookies() {
                return wrappedRequest.getCookies();
            }

            public Map<String, String[]> getPrivateParameterMap() {
                return Collections.EMPTY_MAP;
            }

            public Map<String, String[]> getPublicParameterMap() {
                return Collections.EMPTY_MAP;
            }
        };
        
        return result;
    }
    
    
        
        
}
