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
package com.sun.faces.facelets.tag.jsf.core;

import static com.sun.faces.cdi.CdiUtils.getBeanReference;
import static com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter.EnableWebsocketEndpoint;
import static javax.faces.push.PushContext.ENABLE_WEBSOCKET_ENDPOINT_PARAM_NAME;

import java.io.IOException;
import java.io.Serializable;
import java.util.regex.Pattern;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.event.PostAddToViewEvent;
import javax.faces.event.PreRenderViewEvent;
import javax.faces.event.SystemEventListener;
import javax.faces.push.Push;
import javax.faces.push.PushContext;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagConfig;

import com.sun.faces.config.WebConfiguration;
import com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter;
import com.sun.faces.facelets.tag.TagHandlerImpl;
import com.sun.faces.push.WebsocketChannelManager;
import com.sun.faces.push.WebsocketEndpoint;
import com.sun.faces.push.WebsocketFacesListener;
import com.sun.faces.push.WebsocketPushContext;

/**
 * <p class="changed_added_2_3">
 * The <code>&lt;f:websocket&gt;</code> tag opens an one-way (server to client) web socket based push connection in client
 * side which can be reached from server side via {@link PushContext} interface injected in any CDI/container managed
 * artifact via <code>&#64;</code>{@link Push} annotation.
 *
 * @author Bauke Scholtz
 * @see Push
 * @see PushContext
 * @see WebsocketEndpoint
 * @see WebsocketScope
 * @see WebsocketEventListener
 * @see WebsocketManager
 * @see WebsocketPushContext
 * @see BooleanWebContextInitParameter#EnableWebsocketEndpoint
 * @since 2.3
 */
public class WebsocketHandler extends TagHandlerImpl {

    // Constants ------------------------------------------------------------------------------------------------------

    private static final Pattern PATTERN_CHANNEL_NAME = Pattern.compile("[\\w.-]+");

    private static final String ERROR_ENDPOINT_NOT_ENABLED =
        "f:websocket endpoint is not enabled."
            + " You need to set web.xml context param '" + ENABLE_WEBSOCKET_ENDPOINT_PARAM_NAME + "' with value 'true'.";
    private static final String ERROR_INVALID_CHANNEL =
        "f:websocket 'channel' attribute '%s' does not represent a valid channel name. It may not be an EL expression and"
            + " it may only contain alphanumeric characters, hyphens, underscores and periods.";
    private static final String ERROR_INVALID_USER =
        "f:websocket 'user' attribute '%s' does not represent a valid user identifier. It must implement Serializable and"
            + " preferably have low memory footprint. Suggestion: use #{request.remoteUser} or #{someLoggedInUser.id}.";

    // Properties -----------------------------------------------------------------------------------------------------

    private final TagAttribute port;
    private final TagAttribute channel;
    private final TagAttribute scope;
    private final TagAttribute user;
    private final TagAttribute onopen;
    private final TagAttribute onmessage;
    private final TagAttribute onclose;
    private final TagAttribute connected;

    // Constructors ---------------------------------------------------------------------------------------------------

    /**
     * The tag constructor. It will extract the attributes.
     * @param config The tag config.
     */
    public WebsocketHandler(TagConfig config) {
        super(config);
        port = getAttribute("port");
        channel = getRequiredAttribute("channel");
        scope = getAttribute("scope");
        user = getAttribute("user");
        onopen = getAttribute("onopen");
        onmessage = getRequiredAttribute("onmessage");
        onclose = getAttribute("onclose");
        connected = getAttribute("connected");
    }

    // Actions --------------------------------------------------------------------------------------------------------

    /**
     * Check if endpoint is registered, validate channel name, compose the functions, extract <code>connected</code>
     * value expression and create {@link WebsocketEventListener}. Finally subscribe that websocket event listener to
     * {@link PostAddToViewEvent} and {@link PreRenderViewEvent} of {@link UIViewRoot}.
     * @throws IllegalStateException When the websocket endpoint is not registered.
     * @throws IllegalArgumentException When the websocket channel name is invalid.
     */
    @Override
    public void apply(FaceletContext context, UIComponent parent) throws IOException {
        if (!ComponentHandler.isNew(parent)) {
            return;
        }

        if (!WebConfiguration.getInstance().isOptionEnabled(EnableWebsocketEndpoint)) {
            throw new IllegalStateException(ERROR_ENDPOINT_NOT_ENABLED);
        }

        Integer portNumber = (port != null) ? (Integer) port.getValueExpression(context, Integer.class).getValue(context) : null;
        String channelName = getChannelName(context, channel);
        String channelId = getChannelId(context, channelName, scope, user);
        String onopenFunction = (onopen != null) ? onopen.getValue(context) : null;
        String onmessageFunction = onmessage.getValue(context);
        String oncloseFunction = (onclose != null) ? onclose.getValue(context) : null;
        String functions = onopenFunction + "," + onmessageFunction + "," + oncloseFunction;
        ValueExpression connectedExpression = (connected != null) ? connected.getValueExpression(context, Boolean.class) : null;

        SystemEventListener listener = new WebsocketFacesListener(portNumber, channelName, channelId, functions, connectedExpression);
        UIViewRoot view = context.getFacesContext().getViewRoot();
        view.subscribeToViewEvent(PostAddToViewEvent.class, listener);
        view.subscribeToViewEvent(PreRenderViewEvent.class, listener);
    }

    // Helpers --------------------------------------------------------------------------------------------------------

    private static String getChannelName(FaceletContext context, TagAttribute channel) {
        String channelName = channel.isLiteral() ? channel.getValue(context) : null;

        if (channelName == null || !PATTERN_CHANNEL_NAME.matcher(channelName).matches()) {
            throw new IllegalArgumentException(String.format(ERROR_INVALID_CHANNEL, channelName));
        }

        return channelName;
    }

    private static String getChannelId(FaceletContext context, String channelName, TagAttribute scope, TagAttribute user) {
        Object userObject = (user != null) ? user.getObject(context) : null;

        if (userObject != null && !(userObject instanceof Serializable)) {
            throw new IllegalArgumentException(String.format(ERROR_INVALID_USER, userObject));
        }

        WebsocketChannelManager socketChannels = getBeanReference(WebsocketChannelManager.class);
        String scopeName = (scope == null) ? null : scope.isLiteral() ? scope.getValue(context) : "";
        return socketChannels.register(channelName, scopeName, (Serializable) userObject);
    }

}