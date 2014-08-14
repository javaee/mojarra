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

package javax.faces.event;

import java.util.EventObject;

/**
 * <p><strong class="changed_added_2_0 changed_modified_2_2">SystemEvent</strong> 
 * is the base class for non-application specific events that can be fired by
 * arbitrary objects.</p>
 *
 * @since 2.0
 */
public abstract class SystemEvent extends EventObject {

    private static final long serialVersionUID = 2696415667461888462L;


    // ------------------------------------------------------------ Constructors


    /**
     * <p class="changed_added_2_0">Pass the argument
     * <code>source</code> to the superclass constructor.</p>

     * @param source the <code>source</code> reference to be
     * passed to the superclass constructor.
     *
     * @throws <code>IllegalArgumentException</code> if the argument is
     * <code>null</code>.
     */
    public SystemEvent(Object source) {
        super(source);
    }


    // ---------------------------------------------------------- Public Methods


    /**
     * <p><span class="changed_modified_2_2">Return</span> <code>true</code> 
     * if this {@link FacesListener} is an
     * instance of a the appropriate listener class that this event
     * supports. <span class="changed_added_2_2">The default implementation returns true if the listener
     * is a {@link ComponentSystemEventListener}.</span></p>
     *
     * @param listener {@link FacesListener} to evaluate
     */
    public boolean isAppropriateListener(FacesListener listener) {

        return (listener instanceof SystemEventListener);

    }


    /**
     * <p>Broadcast this event instance to the specified
     * {@link FacesListener}, by whatever mechanism is appropriate.  Typically,
     * this will be accomplished by calling an event processing method, and
     * passing this instance as a paramter.</p>
     *
     * @param listener {@link FacesListener} to send this {@link FacesEvent} to
     *
     * @throws AbortProcessingException Signal the JavaServer Faces
     *  implementation that no further processing on the current event
     *  should be performed
     */
    public void processListener(FacesListener listener) {

        ((SystemEventListener) listener).processEvent(this);

    }
}
