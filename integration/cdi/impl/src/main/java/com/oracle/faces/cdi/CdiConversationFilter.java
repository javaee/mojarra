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

import java.io.IOException;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A servlet filter that makes it possible to cleanup a transient conversation.
 * 
 * @author Manfred Riem (manfred.riem@oracle.com)
 */
public class CdiConversationFilter implements Filter {

    /**
     * Stores the conversation scope manager.
     */
    @Inject CdiConversationScopeManager manager;
    
    /**
     * Initialize the filter.
     * 
     * @param filterConfig the filter configuration.
     * @throws ServletException when an initialization error occurs.
     */
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    /**
     * Do filter processing.
     * 
     * @param request the request.
     * @param response the response.
     * @param chain the filter chain.
     * @throws IOException when an I/O error occurs.
     * @throws ServletException when a servlet/filter error occurs.
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (response instanceof HttpServletResponse) {
            chain.doFilter(request, new CdiConversationServletResponse(
                    (HttpServletRequest) request, (HttpServletResponse) response));

            /*
             * If the "com.oracle.faces.cdi.ConversationToEnd" attribute is set 
             * we need to cleanup this particular conversation. Note the value 
             * of the attribute is the actual conversation that needs to be 
             * cleaned up.
             */
            if (request.getAttribute("com.oracle.faces.cdi.ConversationToEnd") != null) {
                manager.cleanupConversation((CdiConversationImpl) request.getAttribute("com.oracle.faces.cdi.ConversationToEnd"));
                request.removeAttribute("com.oracle.faces.cdi.ConversationToEnd");
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    /**
     * Destroy the filter.
     */
    public void destroy() {
    }
}
