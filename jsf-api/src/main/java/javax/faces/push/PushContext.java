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
 * https://glassfish.java.net/public/CDDLGPL_1_1.html
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
package javax.faces.push;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.websocket.RemoteEndpoint.Async;

/**
 * <p class="changed_added_2_3">
 * CDI interface to send a message object to the push socket channel as identified by <code>&#64;</code>{@link Push}.
 * This can be injected via <code>&#64;Push</code> in any container managed artifact in WAR.
 * <pre>
 * &#64;Inject &#64;Push
 * private PushContext channelName;
 * </pre>
 * <p>
 * For detailed usage instructions, see <code>&#64;</code>{@link Push} javadoc.
 * 
 * @since 2.3
 * @see Push
 */
public interface PushContext extends Serializable {

    // Constants ------------------------------------------------------------------------------------------------------

    /** The boolean context parameter name to explicitly enable web socket endpoint during startup. */
    String ENABLE_WEBSOCKET_ENDPOINT_PARAM_NAME = "javax.faces.ENABLE_WEBSOCKET_ENDPOINT";

    /** The integer context parameter name to specify the websocket endpoint port when it's different from HTTP port. */
    String WEBSOCKET_ENDPOINT_PORT_PARAM_NAME = "javax.faces.WEBSOCKET_ENDPOINT_PORT";

    /** The context-relative web socket URI prefix where the endpoint should listen on. */
    String URI_PREFIX = "/javax.faces.push";

    // Actions --------------------------------------------------------------------------------------------------------

    /**
     * Send given message object to the push socket channel as identified by <code>&#64;</code>{@link Push}.
     * The message object will be encoded as JSON and be available as first argument of the JavaScript listener function
     * declared in <code>&lt;f:websocket onmessage&gt;</code>.
     * @param message The push message object.
     * @return The results of the send operation. If it returns an empty set, then there was no open web socket session
     * associated with given socket channel. The returned futures will return <code>null</code> on {@link Future#get()}
     * if the message was successfully delivered and otherwise throw {@link ExecutionException}.
     * @throws IllegalArgumentException If given message object cannot be encoded as JSON.
     * @see Async#sendText(String)
     */
    Set<Future<Void>> send(Object message);

    /**
     * Send given message object to the push socket channel as identified by <code>&#64;</code>{@link Push}, targeted
     * to the given user as identified by <code>&lt;f:websocket user&gt;</code>.
     * The message object will be encoded as JSON and be available as first argument of the JavaScript listener function
     * declared in <code>&lt;f:websocket onmessage&gt;</code>.
     * @param <S> The generic type of the user identifier.
     * @param message The push message object.
     * @param user The user to which the push message object must be delivered to.
     * @return The results of the send operation. If it returns an empty set, then there was no open web socket session
     * associated with given socket channel and user. The returned futures will return <code>null</code> on
     * {@link Future#get()} if the message was successfully delivered and otherwise throw {@link ExecutionException}.
     * @throws IllegalArgumentException If given message object cannot be encoded as JSON.
     * @see Async#sendText(String)
     */
    <S extends Serializable> Set<Future<Void>> send(Object message, S user);

    /**
     * Send given message object to the push socket channel as identified by <code>&#64;</code>{@link Push}, targeted
     * to the given users as identified by <code>&lt;f:websocket user&gt;</code>.
     * The message object will be encoded as JSON and be available as first argument of the JavaScript listener function
     * declared in <code>&lt;f:websocket onmessage&gt;</code>.
     * @param <S> The generic type of the user identifier.
     * @param message The push message object.
     * @param users The users to which the push message object must be delivered to.
     * @return The results of the send operation grouped by user. If it contains an empty set, then there was no open
     * web socket session associated with given socket channel and user. The returned futures will return
     * <code>null</code> on {@link Future#get()} if the message was successfully delivered and otherwise throw
     * {@link ExecutionException}.
     * @throws IllegalArgumentException If given message object cannot be encoded as JSON.
     * @see Async#sendText(String)
     */
    <S extends Serializable> Map<S, Set<Future<Void>>> send(Object message, Collection<S> users);

}
