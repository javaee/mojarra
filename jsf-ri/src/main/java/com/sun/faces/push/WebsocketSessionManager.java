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

import static com.sun.faces.cdi.CdiUtils.getBeanReference;
import static com.sun.faces.push.WebsocketEndpoint.PARAM_CHANNEL;
import static java.util.Collections.emptySet;
import static javax.websocket.CloseReason.CloseCodes.NORMAL_CLOSURE;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.util.AnnotationLiteral;
import javax.faces.context.FacesContext;
import javax.faces.event.WebsocketEvent;
import javax.faces.event.WebsocketEvent.Closed;
import javax.faces.event.WebsocketEvent.Opened;
import javax.faces.push.Push;
import javax.inject.Inject;
import javax.websocket.CloseReason;
import javax.websocket.Session;

import com.sun.faces.util.Json;
import com.sun.faces.util.Util;

/**
 * <p class="changed_added_2_3">
 * This web socket session manager holds all web socket sessions by their channel identifier.
 *
 * @author Bauke Scholtz
 * @see Push
 * @since 2.3
 */
@ApplicationScoped
public class WebsocketSessionManager {

    // Constants ------------------------------------------------------------------------------------------------------

    private static final CloseReason REASON_EXPIRED = new CloseReason(NORMAL_CLOSURE, "Expired");
    private static final AnnotationLiteral<Opened> SESSION_OPENED = new AnnotationLiteral<Opened>() {
        private static final long serialVersionUID = 1L;
    };
    private static final AnnotationLiteral<Closed> SESSION_CLOSED = new AnnotationLiteral<Closed>() {
        private static final long serialVersionUID = 1L;
    };

    private final ConcurrentMap<String, Collection<Session>> socketSessions = new ConcurrentHashMap<>();

    // Properties -----------------------------------------------------------------------------------------------------

    @Inject
    private WebsocketUserManager socketUsers;

    // Actions --------------------------------------------------------------------------------------------------------

    /**
     * Register given channel identifier.
     * @param channelId The channel identifier to register.
     */
    protected void register(String channelId) {
        if (!socketSessions.containsKey(channelId)) {
            socketSessions.putIfAbsent(channelId, new ConcurrentLinkedQueue<Session>());
        }
    }

    /**
     * Register given channel identifiers.
     * @param channelIds The channel identifiers to register.
     */
    protected void register(Iterable<String> channelIds) {
        for (String channelId : channelIds) {
            register(channelId);
        }
    }

    /**
     * On open, add given web socket session to the mapping associated with its channel identifier and returns
     * <code>true</code> if it's accepted (i.e. the channel identifier is known) and the same session hasn't been added
     * before, otherwise <code>false</code>.
     * @param session The opened web socket session.
     * @return <code>true</code> if given web socket session is accepted and is new, otherwise <code>false</code>.
     */
    protected boolean add(Session session) {
        String channelId = getChannelId(session);
        Collection<Session> sessions = socketSessions.get(channelId);

        if (sessions != null && sessions.add(session)) {
            Serializable user = socketUsers.getUser(getChannel(session), channelId);

            if (user != null) {
                session.getUserProperties().put("user", user);
            }

            fireEvent(session, null, SESSION_OPENED);
            return true;
        }

        return false;
    }

    /**
     * Encode the given message object as JSON and send it to all open web socket sessions associated with given web
     * socket channel identifier.
     * @param channelId The web socket channel identifier.
     * @param message The push message object.
     * @return The results of the send operation. If it returns an empty set, then there was no open session associated
     * with given channel identifier. The returned futures will return <code>null</code> on {@link Future#get()} if the
     * message was successfully delivered and otherwise throw {@link ExecutionException}.
     */
    protected Set<Future<Void>> send(String channelId, Object message) {
        Collection<Session> sessions = (channelId != null) ? socketSessions.get(channelId) : null;

        if (sessions != null && !sessions.isEmpty()) {
            Set<Future<Void>> results = new HashSet<>(sessions.size());
            String json = Json.encode(message);

            for (Session session : sessions) {
                if (session.isOpen()) {
                    send(session, json, results);
                }
            }

            return results;
        }

        return emptySet();
    }

    private void send(Session session, String text, Set<Future<Void>> results) {
        if (session.isOpen()) {
            try {
                results.add(session.getAsyncRemote().sendText(text));
            }
            catch (IllegalStateException e) {
                // Awkward workaround for Tomcat not willing to queue/synchronize asyncRemote().
                // https://bz.apache.org/bugzilla/show_bug.cgi?id=56026
                if (session.getClass().getName().startsWith("org.apache.tomcat.websocket.") && e.getMessage().contains("[TEXT_FULL_WRITING]")) {
                    synchronized (session) {
                        send(session, text, results);
                    }
                }
                else {
                    throw e;
                }
            }
        }
    }

    /**
     * On close, remove given web socket session from the mapping.
     * @param session The closed web socket session.
     * @param reason The close reason.
     */
    protected void remove(Session session, CloseReason reason) {
        Collection<Session> sessions = socketSessions.get(getChannelId(session));

        if (sessions != null && sessions.remove(session)) {
            fireEvent(session, reason, SESSION_CLOSED);
        }
    }

    /**
     * Deregister given channel identifiers and explicitly close all open web socket sessions associated with it.
     * @param channelIds The channel identifiers to deregister.
     */
    protected void deregister(Iterable<String> channelIds) {
        for (String channelId : channelIds) {
            Collection<Session> sessions = socketSessions.get(channelId);

            if (sessions != null) {
                for (Session session : sessions) {
                    if (session.isOpen()) {
                        try {
                            session.close(REASON_EXPIRED);
                        }
                        catch (IOException ignore) {
                            continue;
                        }
                    }
                }
            }
        }
    }

    // Internal -------------------------------------------------------------------------------------------------------

    private static volatile WebsocketSessionManager instance;

    /**
     * Internal usage only. Awkward workaround for it being unavailable via @Inject in endpoint in Tomcat+Weld/OWB.
     */
    static WebsocketSessionManager getInstance() {
        if (instance == null) {
            instance = getBeanReference(WebsocketSessionManager.class);
        }

        return instance;
    }

    // Helpers --------------------------------------------------------------------------------------------------------

    private static String getChannel(Session session) {
        return session.getPathParameters().get(PARAM_CHANNEL);
    }

    private static String getChannelId(Session session) {
        return session.getQueryString();
    }

    private static void fireEvent(Session session, CloseReason reason, AnnotationLiteral<?> qualifier) {
        Serializable user = (Serializable) session.getUserProperties().get("user");
        Util.getCdiBeanManager(FacesContext.getCurrentInstance()).fireEvent(new WebsocketEvent(getChannel(session), user, (reason != null) ? reason.getCloseCode() : null), qualifier);
    }

}