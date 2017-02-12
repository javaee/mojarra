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

import static javax.faces.push.PushContext.URI_PREFIX;
import static javax.websocket.CloseReason.CloseCodes.GOING_AWAY;
import static javax.websocket.CloseReason.CloseCodes.VIOLATED_POLICY;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.push.Push;
import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;

/**
 * <p class="changed_added_2_3">
 * This web socket server endpoint handles web socket requests coming from <code>&lt;f:websocket&gt;</code>.
 *
 * @author Bauke Scholtz
 * @see Push
 * @since 2.3
 */
public class WebsocketEndpoint extends Endpoint {

    // Constants ------------------------------------------------------------------------------------------------------

    /** The URI path parameter name of the web socket channel. */
    static final String PARAM_CHANNEL = "channel";

    /** The context-relative URI template where the web socket endpoint should listen on. */
    public static final String URI_TEMPLATE = URI_PREFIX + "/{" + PARAM_CHANNEL + "}";

    private static final Logger logger = Logger.getLogger(WebsocketEndpoint.class.getName());
    private static final CloseReason REASON_UNKNOWN_CHANNEL = new CloseReason(VIOLATED_POLICY, "Unknown channel");
    private static final String ERROR_EXCEPTION = "WebsocketEndpoint: An exception occurred during processing web socket request.";

    // Actions --------------------------------------------------------------------------------------------------------

    /**
     * Add given web socket session to the {@link WebocketSessionManager}. If web socket session is not accepted (i.e. the
     * channel identifier is unknown), then immediately close with reason VIOLATED_POLICY (close code 1008).
     * @param session The opened web socket session.
     * @param config The endpoint configuration.
     */
    @Override
    public void onOpen(Session session, EndpointConfig config) {
        if (WebsocketSessionManager.getInstance().add(session)) { // @Inject in Endpoint doesn't work in Tomcat+Weld/OWB.
            session.setMaxIdleTimeout(0);
        }
        else {
            try {
                session.close(REASON_UNKNOWN_CHANNEL);
            }
            catch (IOException e) {
                onError(session, e);
            }
        }
    }

    /**
     * Delegate exception to onClose.
     * @param session The errored web socket session.
     * @param throwable The cause.
     */
    @Override
    public void onError(Session session, Throwable throwable) {
        if (session.isOpen()) {
            session.getUserProperties().put(Throwable.class.getName(), throwable);
        }
    }

    /**
     * Remove given web socket session from the {@link WebsocketSessionManager}. If there is any exception from onError which was
     * not caused by GOING_AWAY, then log it. Tomcat &lt;= 8.0.30 is known to throw an unnecessary exception when client
     * abruptly disconnects, see also <a href="https://bz.apache.org/bugzilla/show_bug.cgi?id=57489">issue 57489</a>.
     * @param session The closed web socket session.
     * @param reason The close reason.
     */
    @Override
    public void onClose(Session session, CloseReason reason) {
        WebsocketSessionManager.getInstance().remove(session, reason); // @Inject in Endpoint doesn't work in Tomcat+Weld/OWB and CDI.current() during WS close doesn't work in WildFly.

        Throwable throwable = (Throwable) session.getUserProperties().remove(Throwable.class.getName());

        if (throwable != null && reason.getCloseCode() != GOING_AWAY) {
            logger.log(Level.SEVERE, ERROR_EXCEPTION, throwable);
        }
    }

}