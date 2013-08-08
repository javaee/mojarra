/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright (c) 1997-2013 Oracle and/or its affiliates. All rights reserved.
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
package com.oracle.faces.cdi;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * A wrapper around the HTTP servlet response so we can encode the cid 
 * parameter when needed.
 * 
 * @author Manfred Riem (manfred.riem@oracle.com)
 */
public class CdiConversationServletResponse extends HttpServletResponseWrapper {

    /**
     * Stores the request.
     */
    private HttpServletRequest request;

    /**
     * Constructor.
     * 
     * @param request the HTTP servlet request.
     * @param response the HTTP servlet response.
     */
    public CdiConversationServletResponse(HttpServletRequest request, HttpServletResponse response) {
        super(response);
        this.request = request;
    }

    /**
     * Encode the redirect URL.
     * 
     * @param url the URL.
     * @return the encoded redirect URL.
     */
    @Override
    public String encodeRedirectURL(String url) {
        return appendConversationIdIfNecessary(super.encodeRedirectURL(url));
    }

    /**
     * Encode the redirect URL.
     * 
     * @param url the URL.
     * @return the encoded redirect URL.
     */
    @Override
    public String encodeRedirectUrl(String url) {
        return encodeRedirectURL(url);
    }

    /**
     * Encode the URL.
     * 
     * @param url the URL.
     * @return the encoded URL.
     */
    @Override
    public String encodeURL(String url) {
        return appendConversationIdIfNecessary(super.encodeURL(url));
    }

    /**
     * Encode the URL.
     * 
     * @param url the URL.
     * @return the encoded URL.
     */
    @Override
    public String encodeUrl(String url) {
        return encodeURL(url);
    }

    /**
     * Append the cid parameter if necessary.
     * 
     * @param url the URL.
     * @return the URL with cid if necesssary.
     */
    public String appendConversationIdIfNecessary(String url) {
        StringBuilder result = new StringBuilder();
        if (request.getAttribute("com.oracle.faces.cdi.ConversationToEnd") == null && 
                request.getAttribute("com.oracle.faces.cdi.ConversationId") != null) {
            if (url.contains("?")) {
                result.append(url);
                result.append("&amp;cid=");
                result.append(request.getAttribute("com.oracle.faces.cdi.ConversationId"));
            } else {
                result.append(url);
                result.append("?cid=");
                result.append(request.getAttribute("com.oracle.faces.cdi.ConversationId"));
            }
        } else {
            result.append(url);
        }
        return result.toString();
    }
}
