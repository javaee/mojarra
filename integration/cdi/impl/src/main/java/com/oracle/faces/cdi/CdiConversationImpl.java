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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.spi.Contextual;
import javax.enterprise.context.spi.CreationalContext;

/**
 * The internal implementation of a CDI conversation.
 * 
 * @author Manfred Riem (manfred.riem@oracle.com)
 */
public class CdiConversationImpl implements Conversation, Serializable {
    /**
     * Stores the context map for @ConversationScoped beans.
     */
    private ConcurrentHashMap<Contextual, CdiConversationScopeObject> contextMap = 
            new ConcurrentHashMap<Contextual, CdiConversationScopeObject>();

    /**
     * Stores the conversation scope manager.
     */
    private CdiConversationScopeManager manager;
    
    /**
     * Stores the conversation id.
     */
    private String id;
    
    /**
     * Stores the transient flag.
     */
    private boolean transientFlag;
    
    /**
     * Stores the timeout (in milliseconds).
     */
    private long timeout;
    
    /**
     * Stores the last active time (in milliseconds).
     */
    private long lastActiveTime;

    /**
     * Constructor.
     * 
     * @param manager the conversation scope manager.
     */
    public CdiConversationImpl(CdiConversationScopeManager manager) {
        this.manager = manager;
        this.timeout = 600000;
        this.id = null;
        this.transientFlag = true;
        this.lastActiveTime = System.currentTimeMillis();
    }

    /**
     * Begin the conversation.
     */
    public void begin() {
        if (transientFlag) {
            this.transientFlag = false;
            this.id = manager.getNewConversationId();
            manager.associateConversation(this);
        } else {
            throw new IllegalStateException("Conversation already marked long-running");
        }
    }

    /**
     * Begin the conversation.
     * 
     * @param id the conversation id.
     */
    public void begin(String id) {
        if (transientFlag) {
            this.transientFlag = false;
            if (manager.isConversationIdAvailable(id)) {
                this.id = id;
                manager.associateConversation(this);
            } else {
                throw new IllegalArgumentException("Specified conversation id already exists");
            }
        } else {
            throw new IllegalStateException("Conversation already marked long-running");
        }
    }

    /**
     * End the conversation.
     */
    public void end() {
        if (!transientFlag) {
            this.transientFlag = true;
            manager.dissociateConversation(this);
        } else {
            throw new IllegalStateException("Conversation is already marked transient");
        }
    }

    /**
     * Get the conversation id.
     * 
     * @return the conversation id.
     */
    public String getId() {
        return this.id;
    }

    /**
     * Get the timeout.
     * 
     * @return the timeout.
     */
    public long getTimeout() {
        return this.timeout;
    }

    /**
     * Set the timeout.
     * 
     * @param timeout the timeout.
     */
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    /**
     * Is the conversation transient?
     * 
     * @return true if it is, false otherwise.
     */
    public boolean isTransient() {
        return this.transientFlag;
    }

    /**
     * Is the conversation timed out?
     * 
     * @return true if it is, false otherwise.
     */
    public boolean isTimedOut() {
        if (lastActiveTime + timeout < System.currentTimeMillis()) {
            return true;
        }        
        return false;
    }

    /**
     * Mark the conversation as used.
     * 
     * <p>
     *  Every API method called on the conversation marks the last active time.
     *  So to keep a conversation from timing out the developer can either call
     *  API methods, or set the timeout manually.
     * </p>
     */
    public void mark() {
        lastActiveTime = System.currentTimeMillis();
    }

    /**
     * Get the value from the context map (or null if not found).
     *
     * @param <T> the type.
     * @param contextual the contextual.
     * @return the value or null if not found.
     */
    public <T> T getBean(Contextual<T> contextual) {
        T result = null;

        if (contextMap != null) {
            CdiConversationScopeObject contextObject = contextMap.get(contextual);

            if (contextObject != null) {
                result = (T) contextObject.getInstance();
            }
        }

        return result;
    }

    /**
     * Create the bean.
     *
     * @param <T> the type.
     * @param contextual the contextual.
     * @param creational the creational.
     * @return the value or null if not found.
     */
    public <T> T createBean(Contextual<T> contextual, CreationalContext<T> creational) {
        T result = contextual.create(creational);

        if (result != null) {
            contextMap.put(contextual, new CdiConversationScopeObject(contextual, creational, result));
        }

        return result;
    }

    /**
     * Destroy the associated @ConversationScoped beans.
     */
    public void destroyBeans() {
        for (Map.Entry<Contextual, CdiConversationScopeObject> entry : contextMap.entrySet()) {
            CdiConversationScopeObject cdiObject = entry.getValue();
            cdiObject.getContextual().destroy(cdiObject.getInstance(), cdiObject.getCreationalContext());
        }
        contextMap.clear();
    }
}
