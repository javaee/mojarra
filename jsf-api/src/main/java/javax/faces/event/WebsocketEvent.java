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
package javax.faces.event;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.io.Serializable;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Objects;

import javax.enterprise.event.Observes;
import javax.faces.push.Push;
import javax.inject.Qualifier;
import javax.websocket.CloseReason.CloseCode;

/**
 * <p class="changed_added_2_3">
 * This web socket event will be fired when a new <code>&lt;f:websocket&gt;</code> has been
 * <code>&#64;</code>{@link Opened} or <code>&#64;</code>{@link Closed}. An application scoped CDI bean can
 * <code>&#64;</code>{@link Observes} them.
 * <p>
 * For detailed usage instructions, see <code>&#64;</code>{@link Push} javadoc.
 *
 * @see Push
 * @see Opened
 * @see Closed
 * @since 2.3
 */
public final class WebsocketEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String channel;
    private final Serializable user;
    private final CloseCode code;

    public WebsocketEvent(String channel, Serializable user, CloseCode code) {
        this.channel = channel;
        this.user = user;
        this.code = code;
    }

    /**
     * Returns the <code>&lt;f:websocket channel&gt;</code>.
     * @return The web socket channel name.
     */
    public String getChannel() {
        return channel;
    }

    /**
     * Returns the <code>&lt;f:websocket user&gt;</code>, if any.
     * @param <S> The generic type of the user identifier.
     * @return The web socket user identifier, if any.
     * @throws ClassCastException When <code>S</code> is of wrong type.
     */
    @SuppressWarnings("unchecked")
    public <S extends Serializable> S getUser() {
        return (S) user;
    }

    /**
     * Returns the close code.
     * If this returns <code>null</code>, then it was {@link Opened}.
     * If this returns non-<code>null</code>, then it was {@link Closed}.
     * @return The close code.
     */
    public CloseCode getCloseCode() {
        return code;
    }

    @Override
    public int hashCode() {
        return super.hashCode() + Objects.hash(channel, user, code);
    }

    @Override
    public boolean equals(Object other) {
        return other != null && getClass() == other.getClass()
            && Objects.equals(channel, ((WebsocketEvent) other).channel)
            && Objects.equals(user, ((WebsocketEvent) other).user)
            && Objects.equals(code, ((WebsocketEvent) other).code);
    }

    @Override
    public String toString() {
        return String.format("WebsocketEvent[channel=%s, user=%s, closeCode=%s]", channel, user, code);
    }

    /**
     * <p class="changed_added_2_3">
     * Indicates that a <code>&lt;f:websocket&gt;</code> has opened.
     * <p>
     * For detailed usage instructions, see <code>&#64;</code>{@link Push} javadoc.
     *
     * @see Push
     * @since 2.3
     */
    @Qualifier
    @Target(PARAMETER)
    @Retention(RUNTIME)
    @Documented
    public @interface Opened {
        //
    }

    /**
     * <p class="changed_added_2_3">
     * Indicates that a <code>&lt;f:websocket&gt;</code> has closed.
     * <p>
     * For detailed usage instructions, see <code>&#64;</code>{@link Push} javadoc.
     *
     * @see Push
     * @since 2.3
     */
    @Qualifier
    @Target(PARAMETER)
    @Retention(RUNTIME)
    @Documented
    public @interface Closed {
        //
    }

}
