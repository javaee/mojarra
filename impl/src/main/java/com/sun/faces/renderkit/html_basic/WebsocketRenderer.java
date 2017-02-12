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

// CommandScriptRenderer.java

package com.sun.faces.renderkit.html_basic;

import static com.sun.faces.cdi.CdiUtils.getBeanReference;
import static javax.faces.component.behavior.ClientBehaviorContext.createClientBehaviorContext;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.faces.component.UIComponent;
import javax.faces.component.UIWebsocket;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ComponentSystemEventListener;
import javax.faces.event.ListenerFor;
import javax.faces.event.PostAddToViewEvent;

import com.sun.faces.push.WebsocketChannelManager;
import com.sun.faces.push.WebsocketFacesListener;
import com.sun.faces.renderkit.RenderKitUtils;

/**
 * <b>WebsocketRenderer</b> is a class that renders the <code>jsf.push.init()</code> script and decodes any client
 * behaviors triggered by {@link UIWebsocket}.
 *
 * @author Bauke Scholtz
 * @since 2.3
 * @see UIWebsocket
 */
@ListenerFor(systemEventClass=PostAddToViewEvent.class)
public class WebsocketRenderer extends HtmlBasicRenderer implements ComponentSystemEventListener {

    // Constants ------------------------------------------------------------------------------------------------------

    public static final String RENDERER_TYPE = "javax.faces.Websocket";

    private static final String SCRIPT_INIT = "jsf.push.init('%s','%s','%s',%s,%s,%s);";

    // Actions --------------------------------------------------------------------------------------------------------

    /**
     * After adding component to view, subscribe {@link WebsocketFacesListener} if necessary.
     */
    @Override
    public void processEvent(ComponentSystemEvent event) throws AbortProcessingException {
        WebsocketFacesListener.subscribeIfNecessary(event.getFacesContext());
    }

    /**
     * Decode all client behaviors.
     */
    @Override
    public void decode(FacesContext context, UIComponent component) {
        decodeBehaviors(context, component);
    }

    /**
     * Render <code>jsf.push.init()</code> function if necessary.
     */
    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        UIWebsocket websocket = (UIWebsocket) component;

        if (WebsocketFacesListener.isNew(context, websocket)) {
            WebsocketChannelManager websocketChannelManager = getBeanReference(WebsocketChannelManager.class);

            String clientId = websocket.getClientId(context);
            String channel = websocket.getChannel();
            String url = websocketChannelManager.register(context, channel, websocket.getScope(), websocket.getUser());
            String functions = websocket.getOnopen() + "," + websocket.getOnmessage() + "," + websocket.getOnclose();
            String behaviors = getBehaviorScripts(context, websocket);
            boolean connected = websocket.isConnected();

            RenderKitUtils.renderJsfJsIfNecessary(context);

            ResponseWriter writer = context.getResponseWriter();
            writer.startElement("script", component);
            writer.writeAttribute("id", clientId, "id");
            writer.write(String.format(SCRIPT_INIT, clientId, url, channel, functions, behaviors, connected));
            writer.endElement("script");
        }
    }

    // Helpers --------------------------------------------------------------------------------------------------------

    /**
     * Helper to collect all client behavior scripts of websocket into a string representing a JS object.
     */
    private static String getBehaviorScripts(FacesContext context, UIWebsocket websocket) {
        Map<String, List<ClientBehavior>> clientBehaviorsByEvent = websocket.getClientBehaviors();

        if (clientBehaviorsByEvent.isEmpty()) {
            return "{}";
        }

        String clientId = websocket.getClientId(context);
        StringBuilder scripts = new StringBuilder("{");

        for (Entry<String, List<ClientBehavior>> entry : clientBehaviorsByEvent.entrySet()) {
            String event = entry.getKey();
            List<ClientBehavior> clientBehaviors = entry.getValue();
            scripts.append(scripts.length() > 1 ? "," : "").append(event).append(":[");

            for (int i = 0; i < clientBehaviors.size(); i++) {
                scripts.append(i > 0 ? "," : "").append("function(event){");
                scripts.append(clientBehaviors.get(i).getScript(createClientBehaviorContext(context, websocket, event, clientId, null)));
                scripts.append("}");
            }

            scripts.append("]");
        }

        return scripts.append("}").toString();
    }

}