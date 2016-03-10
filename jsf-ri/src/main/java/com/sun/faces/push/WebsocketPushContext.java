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
package com.sun.faces.push;

import static com.sun.faces.push.WebsocketChannelManager.EMPTY_SCOPE;
import static com.sun.faces.push.WebsocketChannelManager.getChannelId;
import static com.sun.faces.push.WebsocketChannelManager.getSessionScope;
import static com.sun.faces.push.WebsocketChannelManager.getViewScope;
import static java.util.Collections.singleton;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.push.Push;
import javax.faces.push.PushContext;

import com.sun.faces.cdi.CdiUtils;

/**
 * <p class="changed_added_2_3">
 * This is a concrete implementation of {@link PushContext} interface which is to be injected by
 * <code>&#64;</code>{@link Push}.
 *
 * @author Bauke Scholtz
 * @see Push
 * @since 2.3
 */
public class WebsocketPushContext implements PushContext {

    // Constants ------------------------------------------------------------------------------------------------------

    private static final long serialVersionUID = 1L;

    // Variables ------------------------------------------------------------------------------------------------------

    private String channel;
    private Map<String, String> sessionScope;
    private Map<String, String> viewScope;
    private WebsocketSessionManager socketSessions;
    private WebsocketUserManager socketUsers;

    // Constructors ---------------------------------------------------------------------------------------------------

    /**
     * Creates a socket push context whereby the mutable map of session and view scope channel identifiers is
     * referenced, so it's still available when another thread invokes {@link #send(Object)} during which the session
     * and view scope is not necessarily active anymore.
     */
    public WebsocketPushContext(String channel, WebsocketSessionManager socketSessions, WebsocketUserManager socketUsers) {
        this.channel = channel;
        boolean hasSession = CdiUtils.isScopeActive(SessionScoped.class);
        sessionScope = hasSession ? getSessionScope() : EMPTY_SCOPE;
        viewScope = hasSession && FacesContext.getCurrentInstance() != null ? getViewScope(false) : EMPTY_SCOPE;
        this.socketSessions = socketSessions;
        this.socketUsers = socketUsers;
    }

    // Actions --------------------------------------------------------------------------------------------------------

    @Override
    public Set<Future<Void>> send(Object message) {
        return socketSessions.send(getChannelId(channel, sessionScope, viewScope), message);
    }

    @Override
    public <S extends Serializable> Set<Future<Void>> send(Object message, S user) {
        return send(message, singleton(user)).get(user);
    }

    @Override
    public <S extends Serializable> Map<S, Set<Future<Void>>> send(Object message, Collection<S> users) {
        Map<S, Set<Future<Void>>> resultsByUser = new HashMap<>(users.size());

        for (S user : users) {
            Set<String> channelIds = socketUsers.getChannelIds(user, channel);
            Set<Future<Void>> results = new HashSet<>(channelIds.size());

            for (String channelId : channelIds) {
                results.addAll(socketSessions.send(channelId, message));
            }

            resultsByUser.put(user, results);
        }

        return resultsByUser;
    }

}