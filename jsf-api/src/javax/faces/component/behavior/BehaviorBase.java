/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 *
 * Contributor(s):
 *
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

import java.util.ArrayList;
import java.util.List;

import javax.faces.component.PartialStateHolder;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.BehaviorEvent;
import javax.faces.event.BehaviorListener;

/**
 * <p class="changed_added_2_0"><strong>BehaviorBase</strong> is a
 * convenience base class that provides a default implementation of the 
 * {@link Behavior} contract.  It also provides behavior listener registration 
 * and state saving support.</p>
 * </p>
 *
 * @since 2.0
 */
public class BehaviorBase implements Behavior, PartialStateHolder {
	
 /**
     * <p>Our {@link javax.faces.event.BehaviorListener}s.  This data
     * structure is lazily instantiated as necessary.</p>
     */
    private List<BehaviorListener> listeners;

    // Flag indicating a desire to not participate in state saving.
    private boolean transientFlag = false;

    // Flag indicating that initial state has been marked.
    private boolean initialState = false;

    /**
     * <p class="changed_added_2_0">Default implementation of 
     * {@link Behavior#broadcast}.  Delivers the specified 
     * {@link BehaviorEvent} to all registered {@link BehaviorListener} 
     * event listeners who have expressed an interest in events of 
     * this type.  Listeners are called in the order in which they were 
     * registered (added).</p>
     *
     * @param event The {@link BehaviorEvent} to be broadcast
     *
     * @throws AbortProcessingException Signal the JavaServer Faces
     *  implementation that no further processing on the current event
     *  should be performed
     * @throws IllegalArgumentException if the implementation class
     *  of this {@link BehaviorEvent} is not supported by this component
     * @throws NullPointerException if <code>event</code> is
     * <code>null</code>
     *
     * @since 2.0
     */
    public void broadcast(BehaviorEvent event)
        throws AbortProcessingException {

        if (null != listeners) {
            for (BehaviorListener listener : listeners) {
                if (event.isAppropriateListener(listener)) {
                    event.processListener(listener);
                }
            }
        }
    }

    /**
     * <p class="changed_added_2_0">Implementation of
     * {@link javax.faces.component.StateHolder#isTransient}.
     */
    public boolean isTransient() {
        return transientFlag;
    }

    /**
     * <p class="changed_added_2_0">Implementation of
     * {@link javax.faces.component.StateHolder#setTransient}.
     */
    public void setTransient(boolean transientFlag) {
        this.transientFlag = transientFlag;
    }

    /**
     * <p class="changed_added_2_0">Implementation of
     * {@link javax.faces.component.StateHolder#saveState}.
     */
    public Object saveState(FacesContext context) {

        // If initial state has been marked, we don't need to
        // save any state.
        if (initialStateMarked()) {
            return null;
        }

        // At the moment, the only state we need to save is our listeners
        return UIComponentBase.saveAttachedState(context, listeners);
    }

    /**
     * <p class="changed_added_2_0">Implementation of
     * {@link javax.faces.component.StateHolder#restoreState}.
     */
    @SuppressWarnings("unchecked")
    public void restoreState(FacesContext context, Object state) {

        if (state != null) {

            // Unchecked cast from Object to List<BehaviorListener>
            listeners = (List<BehaviorListener>)UIComponentBase.restoreAttachedState(context, state);

            // If we saved state last time, save state again next time.
            clearInitialState();
        }
    }

  
    /**
     * <p class="changed_added_2_0">Implementation of
     * {@link javax.faces.component.PartialStateHolder#markInitialState}.
     */
    public void markInitialState() {
        // temporary 'fix' until we can correct behavior save/restore
        initialState = false;
    }

    /**
     * <p class="changed_added_2_0">Implementation of
     * {@link javax.faces.component.PartialStateHolder#initialStateMarked}.
     */
    public boolean initialStateMarked() {
        return initialState;
    }

    /**
     * <p class="changed_added_2_0">Clears the initial state flag, causing
     * the behavior to revert from partial to full state saving.</p>
     */
    protected void clearInitialState() {
        initialState = false;
    }

    /**
     * <p class="changed_added_2_0">Add the specified {@link BehaviorListener} 
     * to the set of listeners registered to receive event notifications 
     * from this {@link Behavior}.
     * It is expected that {@link Behavior} classes acting as event sources
     * will have corresponding typesafe APIs for registering listeners of the
     * required type, and the implementation of those registration methods
     * will delegate to this method.  For example:</p>
     * <pre>
     * public class AjaxBehaviorEvent extends BehaviorEvent { ... }
     *
     * public interface AjaxBehaviorListener extends BehaviorListener {
     *   public void processAjaxBehavior(FooEvent event);
     * }
     *
     * public class AjaxBehavior extends ClientBehaviorBase {
     *   ...
     *   public void addAjaxBehaviorListener(AjaxBehaviorListener listener) {
     *     addBehaviorListener(listener);
     *   }
     *   public void removeAjaxBehaviorListener(AjaxBehaviorListener listener) {
     *     removeBehaviorListener(listener);
     *   }
     *   ...
     * }
     * </pre>
     *
     * @param listener The {@link BehaviorListener} to be registered
     *
     * @throws NullPointerException if <code>listener</code>
     *  is <code>null</code>
     *
     * @since 2.0
     */
    protected void addBehaviorListener(BehaviorListener listener) {

        if (listener == null) {
            throw new NullPointerException();
        }
        if (listeners == null) {
            //noinspection CollectionWithoutInitialCapacity
            listeners = new ArrayList<BehaviorListener>();
        }
        listeners.add(listener);

        clearInitialState();
    }

    /**
     * <p class="changed_added_2_0">Remove the specified 
     * {@link BehaviorListener} from the set of listeners
     * registered to receive event notifications from this 
     * {@link Behavior}.
     *
     * @param listener The {@link BehaviorListener} to be deregistered
     * @throws NullPointerException if <code>listener</code>
     *                              is <code>null</code>
     *
     * @since 2.0
     */
    protected void removeBehaviorListener(BehaviorListener listener) {

        if (listener == null) {
            throw new NullPointerException();
        }
        if (listeners == null) {
            return;
        }
        listeners.remove(listener);

        clearInitialState();
    }
}
