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
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Default;
import javax.inject.Named;

/**
 * The Sesssion-scoped conversation manager that is used to keep track of 
 * conversations and their associated @ConversationScoped beans.
 * 
 * @author Manfred Riem (manfred.riem@oracle.com)
 */
@Named("com.oracle.faces.cdi.ConversationScopeManager")
@SessionScoped
@Default
public class CdiConversationScopeManager implements Serializable {

    /**
     * Stores the active conversations.
     */
    private ConcurrentHashMap<String, CdiConversationImpl> activeConversations;

    /**
     * Associate the conversation.
     * 
     * <p>
     *  The conversation has be marked as long-running so we need to add
     *  it to the active conversations.
     * </p>
     */
    public void associateConversation(CdiConversationImpl conversation) {
        if (conversation.getId() != null) {
            if (!activeConversations.isEmpty()) {
                reapConversations();
            }
            activeConversations.put(conversation.getId(), conversation);
        }
    }

    /**
     * Cleanup the given conversation.
     * 
     * @param conversation the conversation.
     */
    public void cleanupConversation(CdiConversationImpl conversation) {
        if (conversation.getId() != null) {
            activeConversations.remove(conversation.getId());
        }

        conversation.destroyBeans();
    }
    
    /**
     * Create a new conversation.
     * 
     * <p>
     *  By default we create a new transient (a.k.a NOT long-running).
     * </p>
     * 
     * @return the new conversation.
     */
    public CdiConversationImpl createConversation() {
        return new CdiConversationImpl(this);
    }
    
    /**
     * Disassociate the conversation.
     * 
     * <p>
     *  The conversation is no longer marked as long-running so we need to
     *  remove it from the active conversations.
     * </p>
     * 
     * @param conversation the conversation.
     */
    public void dissociateConversation(CdiConversationImpl conversation) {
        if (conversation.getId() != null) {
            activeConversations.remove(conversation.getId());
        }
    }

    /**
     * Get the conversation associated with the given conversation id.
     * 
     * @param cid the conversation id.
     * @return the conversation or null if not found.
     */
    public CdiConversationImpl getConversation(String cid) {
        return activeConversations.get(cid);
    }

    /**
     * Generate a new conversation id.
     * 
     * @return the conversation id.
     */
    public String getNewConversationId() {
        String result = UUID.randomUUID().toString();
        while(!isConversationIdAvailable(result)) {
            result = UUID.randomUUID().toString();
        }
        return result;
    }

    /**
     * Verify if the given conversation id is available.
     * 
     * @param cid the conversation id.
     * @return true if available, false otherwise.
     */
    public boolean isConversationIdAvailable(String cid) {
        return !activeConversations.containsKey(cid);
    }

    /**
     * Reap any conversation that have timed-out.
     * 
     * <p>
     *  Note since the manager is session scoped the assumption here is that
     *  there are not going to be that many conversations active for the given
     *  user so we can call this when creating a new conversation to reap any
     *  timed-out conversations.
     * </p>
     */
    private void reapConversations() {
        for (CdiConversationImpl conversation : activeConversations.values()) {
            if (conversation.isTimedOut()) {
                cleanupConversation(conversation);
            }
        }
    }

    /**
     * Initializes the manager.
     */
    @PostConstruct
    public void init() {
        activeConversations = new ConcurrentHashMap<String, CdiConversationImpl>(1);
    }
    
    /**
     * Destroys the manager.
     * 
     * <p>
     *  Cleanup all active conversations and their associated @ConversationScoped beans.
     * </p>
     */
    @PreDestroy 
    public void destroy() {
        for (CdiConversationImpl conversation : activeConversations.values()) {
            cleanupConversation(conversation);
        }
        activeConversations.clear();
    }
}
