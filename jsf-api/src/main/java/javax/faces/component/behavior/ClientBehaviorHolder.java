/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
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

package javax.faces.component.behavior;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.Map;

/**
*<p class="changed_added_2_0">The <strong>ClientBehaviorHolder</strong> 
* interface may be implemented by any concrete 
* {@link javax.faces.component.UIComponent} that wishes to support
* client behaviors as defined by {@link ClientBehavior}.</p>
*
* @since 2.0
*/
public interface ClientBehaviorHolder {

    /**
     * <p class="changed_added_2_0">Attaches a {@link ClientBehavior} to 
     * the component implementing this interface for the specified event.
     * Valid event names for a UIComponent implementation are defined by 
     * {@code ClientBehaviorHolder.getEventNames()}.</p>
     *
     * @param eventName the logical name of the client-side event to
     * attach the behavior to.
     * @param  behavior the {@link ClientBehavior} instance to attach
     * for the specified event name.
     *
     * @since 2.0
     */
    public void addClientBehavior(String eventName, ClientBehavior behavior);

    /**
     * <p class="changed_added_2_0">Returns a non-null, unmodifiable 
     * <code>Collection</code> containing the names of the logical 
     * events supported by the component implementing this interface.</p>
     *
     * @since 2.0
     */
    public Collection<String> getEventNames();

    /**
     * <p class="changed_added_2_0">Returns a non-null, unmodifiable
     * <code>Map</code> that contains the the {@link ClientBehavior}s that
     * have been attached to the component implementing this interface.
     * The keys in this <code>Map</code> are event names defined by
     * {@link #getEventNames}.</p>
     *
     * @since 2.0
     */
    public Map<String, List<ClientBehavior>> getClientBehaviors();

    /**
     * <p class="changed_added_2_0">Returns the default event
     * name for this <code>ClientBehaviorHolder</code> implementation.  
     * This must be one of the event names returned by
     * {@link #getEventNames} or null if the component does not have
     * a default event.
     *
     * @since 2.0
     */
    public String getDefaultEventName();
}

 
