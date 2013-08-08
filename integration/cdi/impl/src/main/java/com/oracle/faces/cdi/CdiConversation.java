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

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Alternative;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

/**
 * The request scoped part of a CDI conversation.
 *
 * @author Manfred Riem (manfred.riem@oracle.com)
 */
@RequestScoped
@Named("javax.enterprise.context.conversation")
@Alternative
public class CdiConversation implements Conversation, Serializable {

    /**
     * Stores the manager.
     */
    @Inject
    private CdiConversationScopeManager manager;
    /**
     * Stores the HTTP servlet request.
     */
    private HttpServletRequest request;
    /**
     * Stores the internal conversation.
     */
    private CdiConversationImpl conversation;

    /**
     * Initializes the request scoped part of a CDI conversation.
     */
    @PostConstruct
    public void init() {
        /*
         * If we could inject the HttpServletRequest here we could get rid of
         * the JSF call. Now just using the public API to get to the 
         * HttpServletRequest.
         */
        request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        if (request.getParameter("cid") != null) {
            conversation = manager.getConversation(request.getParameter("cid"));
            if (conversation != null) {
                request.setAttribute("com.oracle.faces.cdi.ConversationId", conversation.getId());
                conversation.mark();
            }
        }
        if (conversation == null) {
            conversation = manager.createConversation();
            request.setAttribute("com.oracle.faces.cdi.ConversationToEnd", conversation);
        }
    }

    /**
     * Begin the conversation.
     */
    public void begin() {
        conversation.begin();
        conversation.mark();
        request.setAttribute("com.oracle.faces.cdi.ConversationId", conversation.getId());
        request.removeAttribute("com.oracle.faces.cdi.ConversationToEnd");
    }

    /**
     * Begin the conversation.
     * 
     * @param id the conversation id.
     */
    public void begin(String id) {
        conversation.begin(id);
        conversation.mark();
        request.setAttribute("com.oracle.faces.cdi.ConversationId", conversation.getId());
        request.removeAttribute("com.oracle.faces.cdi.ConversationToEnd");
    }

    /**
     * End the conversation.
     */
    public void end() {
        conversation.end();
        request.removeAttribute("com.oracle.faces.cdi.ConversationId");
        request.setAttribute("com.oracle.faces.cdi.ConversationToEnd", conversation);
    }

    /**
     * Get the id.
     * 
     * @return the id.
     */
    public String getId() {
        conversation.mark();
        return conversation.getId();
    }

    /**
     * Get the timeout.
     * 
     * @return the timeout
     */
    public long getTimeout() {
        conversation.mark();
        return conversation.getTimeout();
    }

    /**
     * Set the timeout.
     * 
     * @param timeout the timeout. 
     */
    public void setTimeout(long timeout) {
        conversation.mark();
        conversation.setTimeout(timeout);
    }

    /**
     * Is the conversation transient?
     * 
     * @return true if it is, false otherwise.
     */
    public boolean isTransient() {
        conversation.mark();
        return conversation.isTransient();
    }

    /**
     * Get the associated internal conversation.
     * 
     * @return the internal conversation.
     */
    public CdiConversationImpl getConversation() {
        return this.conversation;
    }
}
