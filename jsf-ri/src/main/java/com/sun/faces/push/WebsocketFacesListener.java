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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.component.UIWebsocket;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialViewContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.PostAddToViewEvent;
import javax.faces.event.PreRenderViewEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import javax.faces.push.Push;

/**
 * <p class="changed_added_2_3">
 * This JSF listener for {@link UIViewRoot} ensures that the necessary JavaScript code to open or close the
 * <code>WebSocket</code> is properly rendered depending on <code>rendered</code> and <code>connected</code> attributes.
 *
 * @author Bauke Scholtz
 * @see Push
 * @since 2.3
 */
public class WebsocketFacesListener implements SystemEventListener {

    // Constants ------------------------------------------------------------------------------------------------------

    private static final String SCRIPT_OPEN = "jsf.push.open('%s');";
    private static final String SCRIPT_CLOSE = "jsf.push.close('%s');";

    // Initialization -------------------------------------------------------------------------------------------------

    public static void subscribeIfNecessary(FacesContext context) {
        UIViewRoot view = context.getViewRoot();
        List<SystemEventListener> listeners = view.getListenersForEventClass(PostAddToViewEvent.class);

        if (listeners == null || !listeners.stream().anyMatch(l -> l instanceof WebsocketFacesListener)) {
            view.subscribeToViewEvent(PreRenderViewEvent.class, new WebsocketFacesListener());
        }
    }

    public static boolean isNew(FacesContext context, UIWebsocket websocket) {
        return getInitializedWebsockets(context).putIfAbsent(websocket.getClientId(context), websocket.isConnected()) == null;
    }

    // Actions --------------------------------------------------------------------------------------------------------

    /**
     * Only listens on {@link UIViewRoot}.
     */
    @Override
    public boolean isListenerForSource(Object source) {
        return source instanceof UIViewRoot;
    }

    /**
     * If the websocket has just switched its <code>rendered</code> or <code>connected</code> attribute, then
     * render either the <code>open()</code> script or the <code>close()</code> script. During an ajax request with
     * partial rendering, it's added as <code>&lt;eval&gt;</code> by partial response writer, else it's just added
     * as a script component with <code>target="body"</code>.
     */
    @Override
    public void processEvent(SystemEvent event) throws AbortProcessingException {
        if (!(event instanceof PreRenderViewEvent)) {
            return;
        }

        FacesContext context = ((ComponentSystemEvent) event).getFacesContext();

        for (Entry<String, Boolean> initializedWebsocket : getInitializedWebsockets(context).entrySet()) {
            String clientId = initializedWebsocket.getKey();
            UIWebsocket websocket = (UIWebsocket) context.getViewRoot().findComponent(clientId);
            boolean connected = websocket.isRendered() && websocket.isConnected();
            boolean previouslyConnected = initializedWebsocket.setValue(connected);

            if (previouslyConnected != connected) {
                String script = String.format(connected ? SCRIPT_OPEN : SCRIPT_CLOSE, clientId);
                PartialViewContext pvc = context.getPartialViewContext();

                if (pvc.isAjaxRequest() && !pvc.isRenderAll()) {
                    context.getPartialViewContext().getEvalScripts().add(script);
                }
                else {
                    UIOutput outputScript = new UIOutput();
                    outputScript.setRendererType("javax.faces.resource.Script");
                    UIOutput content = new UIOutput();
                    content.setValue(script);
                    outputScript.getChildren().add(content);
                    context.getViewRoot().addComponentResource(context, outputScript, "body");
                }
            }
        }
    }

    // Helpers --------------------------------------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    private static Map<String, Boolean> getInitializedWebsockets(FacesContext context) {
        Map<String, Object> viewScope = context.getViewRoot().getViewMap();
        Map<String, Boolean> initializedWebsockets = (Map<String, Boolean>) viewScope.get(WebsocketFacesListener.class.getName());

        if (initializedWebsockets == null) {
            initializedWebsockets = new HashMap<>();
            viewScope.put(WebsocketFacesListener.class.getName(), initializedWebsockets);
        }

        return initializedWebsockets;
    }

}