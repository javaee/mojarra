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

import static com.sun.faces.cdi.CdiUtils.getBeanInstance;
import static com.sun.faces.push.WebsocketUserManager.getUserChannels;
import static java.util.Collections.emptyMap;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.push.Push;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

/**
 * <p class="changed_added_2_3">
 * This web socket channel manager holds all application and session scoped web socket channel identifiers registered by
 * <code>&lt;f:websocket&gt;</code>.
 *
 * @author Bauke Scholtz
 * @see Push
 * @since 2.3
 */
@SessionScoped
public class WebsocketChannelManager implements Serializable {

    // Constants ------------------------------------------------------------------------------------------------------

    private static final long serialVersionUID = 1L;

    private static final String ERROR_INVALID_SCOPE =
        "f:websocket 'scope' attribute '%s' does not represent a valid scope. It may not be an EL expression and allowed"
            + " values are 'application', 'session' and 'view', case insensitive. The default is 'application'. When"
            + " 'user' attribute is specified, then scope defaults to 'session' and may not be 'application'.";
    private static final String ERROR_DUPLICATE_CHANNEL =
        "f:websocket channel '%s' is already registered on a different scope. Choose an unique channel name for a"
            + " different channel (or shutdown all browsers and restart the server if you were just testing).";

    private static final int ESTIMATED_CHANNELS_PER_APPLICATION = 1;
    private static final int ESTIMATED_CHANNELS_PER_SESSION = 1;
    private static final int ESTIMATED_CHANNELS_PER_VIEW = 1;
    private static final int ESTIMATED_USERS_PER_SESSION = 1;
    static final int ESTIMATED_TOTAL_CHANNELS = ESTIMATED_CHANNELS_PER_APPLICATION + ESTIMATED_CHANNELS_PER_SESSION + ESTIMATED_CHANNELS_PER_VIEW;
    static final Map<String, String> EMPTY_SCOPE = emptyMap();

    private enum Scope {
        APPLICATION, SESSION, VIEW;

        static Scope of(String value, Serializable user) {
            if (value == null) {
                return (user == null) ? APPLICATION : SESSION;
            }

            for (Scope scope : values()) {
                if (scope.name().equalsIgnoreCase(value) && (user == null || scope != APPLICATION)) {
                    return scope;
                }
            }

            throw new IllegalArgumentException(String.format(ERROR_INVALID_SCOPE, value));
        }
    }

    // Properties -----------------------------------------------------------------------------------------------------

    private static final ConcurrentMap<String, String> APPLICATION_SCOPE = new ConcurrentHashMap<>(ESTIMATED_CHANNELS_PER_APPLICATION);
    private final ConcurrentMap<String, String> sessionScope = new ConcurrentHashMap<>(ESTIMATED_CHANNELS_PER_SESSION);
    private final ConcurrentMap<Serializable, String> sessionUsers = new ConcurrentHashMap<>(ESTIMATED_USERS_PER_SESSION);

    @Inject
    private WebsocketSessionManager socketSessions;

    @Inject
    private WebsocketUserManager socketUsers;

    // Actions --------------------------------------------------------------------------------------------------------

    /**
     * Register given channel on given scope and returns the web socket channel identifier.
     * @param context The involved faces context.
     * @param channel The web socket channel.
     * @param scope The web socket scope. Supported values are <code>application</code>, <code>session</code> and
     * <code>view</code>, case insensitive. If <code>null</code>, the default is <code>application</code>.
     * @param user The user object representing the owner of the given channel. If not <code>null</code>, then scope
     * may not be <code>application</code>.
     * @return The web socket URL.
     * @throws IllegalArgumentException When the scope is invalid or when channel already exists on a different scope.
     */
    @SuppressWarnings("unchecked")
    public String register(FacesContext context, String channel, String scope, Serializable user) {
        switch (Scope.of(scope, user)) {
            case APPLICATION: return register(context, null, channel, APPLICATION_SCOPE, sessionScope, getViewScope(false));
            case SESSION: return register(context, user, channel, sessionScope, APPLICATION_SCOPE, getViewScope(false));
            case VIEW: return register(context, user, channel, getViewScope(true), APPLICATION_SCOPE, sessionScope);
            default: throw new UnsupportedOperationException();
        }
    }

    @SuppressWarnings("unchecked")
    private String register(FacesContext context, Serializable user, String channel, Map<String, String> targetScope, Map<String, String>... otherScopes) {
        String url = context.getApplication().getViewHandler().getWebsocketURL(context, channel);

        if (!targetScope.containsKey(channel)) {
            for (Map<String, String> otherScope : otherScopes) {
                if (otherScope.containsKey(channel)) {
                    throw new IllegalArgumentException(String.format(ERROR_DUPLICATE_CHANNEL, channel));
                }
            }

            String channelId = UUID.randomUUID().toString();
            ((ConcurrentMap<String, String>) targetScope).putIfAbsent(channel, channelId);
        }

        String channelId = targetScope.get(channel);

        if (user != null) {
            if (!sessionUsers.containsKey(user)) {
                sessionUsers.putIfAbsent(user, UUID.randomUUID().toString());
                socketUsers.register(user, sessionUsers.get(user));
            }

            socketUsers.addChannelId(sessionUsers.get(user), channel, channelId);
        }

        socketSessions.register(channelId);
        return url + "?" + channelId; 
    }

    /**
     * When current session scope is about to be destroyed, deregister all session scope channels and explicitly close
     * any open web sockets associated with it to avoid stale websockets. If any, also deregister session users.
     */
    @PreDestroy
    protected void deregisterSessionScope() {
        for (Entry<Serializable, String> sessionUser : sessionUsers.entrySet()) {
            socketUsers.deregister(sessionUser.getKey(), sessionUser.getValue());
        }

        socketSessions.deregister(sessionScope.values());
    }

    // Nested classes -------------------------------------------------------------------------------------------------

    /**
     * This helps the web socket channel manager to hold view scoped web socket channel identifiers registered by
     * <code>&lt;f:websocket&gt;</code>.
     * @author Bauke Scholtz
     * @see WebsocketChannelManager
     * @since 2.3
     */
    @ViewScoped
    public static class ViewScope implements Serializable {

        private static final long serialVersionUID = 1L;
        private ConcurrentMap<String, String> viewScope = new ConcurrentHashMap<>(ESTIMATED_CHANNELS_PER_VIEW);

        /**
         * When current view scope is about to be destroyed, deregister all view scope channels and explicitly close
         * any open web sockets associated with it to avoid stale websockets.
         */
        @PreDestroy
        protected void deregisterViewScope() {
            WebsocketSessionManager.getInstance().deregister(viewScope.values());
        }

    }

    // Internal (static because package private methods in CDI beans are subject to memory leaks) ---------------------

    /**
     * For internal usage only. This makes it possible to remember session scope channel IDs during injection time of
     * {@link WebsocketPushContext} (the CDI session scope is not necessarily active during push send time).
     */
    static Map<String, String> getSessionScope() {
        return getBeanInstance(WebsocketChannelManager.class, true).sessionScope;
    }

    /**
     * For internal usage only. This makes it possible to remember view scope channel IDs during injection time of
     * {@link WebsocketPushContext} (the CDI view scope is not necessarily active during push send time).
     */
    static Map<String, String> getViewScope(boolean create) {
        ViewScope bean = getBeanInstance(ViewScope.class, create);
        return (bean == null) ? EMPTY_SCOPE : bean.viewScope;
    }

    /**
     * For internal usage only. This makes it possible to resolve the session and view scope channel ID during push send
     * time in {@link WebsocketPushContext}.
     */
    static String getChannelId(String channel, Map<String, String> sessionScope, Map<String, String> viewScope) {
        String channelId = viewScope.get(channel);

        if (channelId == null) {
            channelId = sessionScope.get(channel);

            if (channelId == null) {
                channelId = APPLICATION_SCOPE.get(channel);
            }
        }

        return channelId;
    }

    // Serialization --------------------------------------------------------------------------------------------------

    private void writeObject(ObjectOutputStream output) throws IOException {
        output.defaultWriteObject();

        // All of below is just in case server restarts with session persistence or failovers/synchronizes to another server.
        output.writeObject(APPLICATION_SCOPE);
        Map<String, ConcurrentMap<String, Set<String>>> sessionUserChannels = new HashMap<>(sessionUsers.size());

        for (String userId : sessionUsers.values()) {
            sessionUserChannels.put(userId, getUserChannels().get(userId));
        }

        output.writeObject(sessionUserChannels);
    }

    @SuppressWarnings("unchecked")
    private void readObject(ObjectInputStream input) throws IOException, ClassNotFoundException {
        input.defaultReadObject();

        // Below is just in case server restarts with session persistence or failovers/synchronizes from another server.
        APPLICATION_SCOPE.putAll((Map<String, String>) input.readObject());
        Map<String, ConcurrentMap<String, Set<String>>> sessionUserChannels = (Map<String, ConcurrentMap<String, Set<String>>>) input.readObject();

        for (Entry<Serializable, String> sessionUser : sessionUsers.entrySet()) {
            String userId = sessionUser.getValue();
            socketUsers.register(sessionUser.getKey(), userId);
            getUserChannels().put(userId, sessionUserChannels.get(userId));
        }

        // Below awkwardness is because WebsocketChannelManager can't be injected in WebsocketSessionManager (CDI session scope
        // is not necessarily active during WS session). So it can't just ask us for channel IDs and we have to tell it.
        // And, for application scope IDs we make sure they're re-registered after server restart/failover.
        socketSessions.register(sessionScope.values());
        socketSessions.register(APPLICATION_SCOPE.values());
    }

}