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
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;


/**
 * <p><strong>FacesEvent</strong> is the base class for user interface and
 * application events that can be fired by {@link UIComponent}s.  Concrete
 * event classes must subclass {@link FacesEvent} in order to be supported
 * by the request processing lifecycle.</p>
 */

public abstract class FacesEvent extends EventObject {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Construct a new event object from the specified source component.</p>
     *
     * @param component Source {@link UIComponent} for this event
     *
     * @throws IllegalArgumentException if <code>component</code> is
     *  <code>null</code>
     */
    public FacesEvent(UIComponent component) {

        super(component);

    }


    // -------------------------------------------------------------- Properties


    /**
     * <p>Return the source {@link UIComponent} that sent this event.
     */
    public UIComponent getComponent() {

        return ((UIComponent) getSource());

    }

    private PhaseId phaseId = PhaseId.ANY_PHASE;

    /**
     * <p>Return the identifier of the request processing phase during
     * which this event should be delivered.  Legal values are the
     * singleton instances defined by the {@link PhaseId} class,
     * including <code>PhaseId.ANY_PHASE</code>, which is the default
     * value.</p>
     */
    public PhaseId getPhaseId() {
	return phaseId;
    }
   
    /**
     * <p>Set the {@link PhaseId} during which this event will be
     * delivered.</p>
     *
     * @throws IllegalArgumentException phaseId is null.
     *
     */ 

    public void setPhaseId(PhaseId phaseId) {
	if (null == phaseId) {
	    throw new IllegalArgumentException();
	}
	this.phaseId = phaseId;
    }


    // ------------------------------------------------- Event Broadcast Methods


    /**
     * <p>Convenience method to queue this event for broadcast at the end
     * of the current request processing lifecycle phase.</p>
     *
     * @throws IllegalStateException if the source component for this
     *  event is not a descendant of a {@link UIViewRoot}
     */
    public void queue() {

        getComponent().queueEvent(this);

    }


    /**
     * <p>Return <code>true</code> if this {@link FacesListener} is an instance
     * of a listener class that this event supports.  Typically, this will be
     * accomplished by an "instanceof" check on the listener class.</p>
     *
     * @param listener {@link FacesListener} to evaluate
     */
    public abstract boolean isAppropriateListener(FacesListener listener);


    /**
     * <p>Broadcast this {@link FacesEvent} to the specified
     * {@link FacesListener}, by whatever mechanism is appropriate.  Typically,
     * this will be accomplished by calling an event processing method, and
     * passing this {@link FacesEvent} as a paramter.</p>
     *
     * @param listener {@link FacesListener} to send this {@link FacesEvent} to
     *
     * @throws AbortProcessingException Signal the JavaServer Faces
     *  implementation that no further processing on the current event
     *  should be performed
     */
    public abstract void processListener(FacesListener listener);


}
