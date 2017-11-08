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

package com.sun.faces.push;

import static com.sun.faces.cdi.CdiUtils.getBeanInstance;
import static java.util.Collections.emptySet;
import static java.util.Collections.synchronizedSet;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.push.Push;

/**
 * <p class="changed_added_2_3">
 * This web socket user manager holds all web socket users registered by <code>&lt;f:websocket&gt;</code>.
 *
 * @author Bauke Scholtz
 * @see Push
 * @since 2.3
 */
@ApplicationScoped
public class WebsocketUserManager {

    // Constants ------------------------------------------------------------------------------------------------------

    private static final int ESTIMATED_USER_CHANNELS_PER_APPLICATION = 1;
    private static final int ESTIMATED_USER_CHANNELS_PER_SESSION = 1;
    private static final int ESTIMATED_SESSIONS_PER_USER = 2;
    private static final int ESTIMATED_CHANNELS_IDS_PER_USER = ESTIMATED_SESSIONS_PER_USER * ESTIMATED_USER_CHANNELS_PER_APPLICATION * ESTIMATED_USER_CHANNELS_PER_SESSION;

    // Properties -----------------------------------------------------------------------------------------------------

    private final ConcurrentMap<String, ConcurrentMap<String, Set<String>>> userChannels = new ConcurrentHashMap<>();
    private final ConcurrentMap<Serializable, Set<String>> applicationUsers = new ConcurrentHashMap<>();

    // Actions --------------------------------------------------------------------------------------------------------

    /**
     * Register application user based on given user and session based user ID.
     * @param user The user.
     * @param userId The session based user ID.
     */
    protected void register(Serializable user, String userId) {
        synchronized (applicationUsers) {
            if (!applicationUsers.containsKey(user)) {
                applicationUsers.putIfAbsent(user, synchronizedSet(new HashSet<String>(ESTIMATED_SESSIONS_PER_USER)));
            }

            applicationUsers.get(user).add(userId);
        }
    }

    /**
     * Add user channel ID associated with given session based user ID and channel name.
     * @param userId The session based user ID.
     * @param channel The channel name.
     * @param channelId The channel identifier.
     */
    protected void addChannelId(String userId, String channel, String channelId) {
        if (!userChannels.containsKey(userId)) {
            userChannels.putIfAbsent(userId, new ConcurrentHashMap<String, Set<String>>(ESTIMATED_USER_CHANNELS_PER_APPLICATION));
        }

        ConcurrentMap<String, Set<String>> channelIds = userChannels.get(userId);

        if (!channelIds.containsKey(channel)) {
            channelIds.putIfAbsent(channel, synchronizedSet(new HashSet<String>(ESTIMATED_USER_CHANNELS_PER_SESSION)));
        }

        channelIds.get(channel).add(channelId);
    }

    /**
     * Resolve the user associated with given channel name and ID.
     * @param channel The channel name.
     * @param channelId The channel identifier.
     * @return The user associated with given channel name and ID.
     */
    protected Serializable getUser(String channel, String channelId) {
        for (Entry<Serializable, Set<String>> applicationUser : applicationUsers.entrySet()) {
            for (String userId : applicationUser.getValue()) { // "Normally" this contains only 1 entry, so it isn't that inefficient as it looks like.
                if (getApplicationUserChannelIds(userId, channel).contains(channelId)) {
                    return applicationUser.getKey();
                }
            }
        }

        return null;
    }

    /**
     * Resolve the user-specific channel IDs associated with given user and channel name.
     * @param user The user.
     * @param channel The channel name.
     * @return The user-specific channel IDs associated with given user and channel name.
     */
    protected Set<String> getChannelIds(Serializable user, String channel) {
        Set<String> channelIds = new HashSet<>(ESTIMATED_CHANNELS_IDS_PER_USER);
        Set<String> userIds = applicationUsers.get(user);

        if (userIds != null) {
            for (String userId : userIds) {
                channelIds.addAll(getApplicationUserChannelIds(userId, channel));
            }
        }

        return channelIds;
    }

    /**
     * Deregister application user associated with given user and session based user ID.
     * @param user The user.
     * @param userId The session based user ID.
     */
    protected void deregister(Serializable user, String userId) {
        userChannels.remove(userId);

        synchronized (applicationUsers) {
            Set<String> userIds = applicationUsers.get(user);
            userIds.remove(userId);

            if (userIds.isEmpty()) {
                applicationUsers.remove(user);
            }
        }
    }

    // Internal (static because package private methods in CDI beans are subject to memory leaks) ---------------------

    /**
     * For internal usage only. This makes it possible to save and restore user specific channels during server
     * restart/failover in {@link WebsocketChannelManager}.
     */
    static ConcurrentMap<String, ConcurrentMap<String, Set<String>>> getUserChannels() {
        return getBeanInstance(WebsocketUserManager.class, true).userChannels;
    }

    // Helpers --------------------------------------------------------------------------------------------------------

    private Set<String> getApplicationUserChannelIds(String userId, String channel) {
        Map<String, Set<String>> channels = userChannels.get(userId);

        if (channels != null) {
            Set<String> channelIds = channels.get(channel);

            if (channelIds != null) {
                return channelIds;
            }
        }

        return emptySet();
    }

}
