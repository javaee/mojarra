/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2018 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://javaee.github.io/glassfish/LICENSE
 * See the License for the specific
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


import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * <p class="changed_modified_2_3">A {@link ValueChangeEvent} is a notification 
 * that the local value of the source component has been change as a result of 
 * user interface activity.  It is not fired unless validation of the new value 
 * was completed successfully.</p>
 */
public class ValueChangeEvent extends FacesEvent {

    private static final long serialVersionUID = 2455861757565618446L;

    /**
     * <p class="changed_removed_2_3">Construct a new event object from the 
     * specified source component, old value, and new value.</p>
     *
     * <p>The default {@link PhaseId} for this event is {@link
     * PhaseId#ANY_PHASE}.</p>
     *
     * @param component Source {@link UIComponent} for this event
     * @param oldValue The previous local value of this {@link UIComponent}
     * @param newValue The new local value of thie {@link UIComponent}
     * @throws IllegalArgumentException if <code>component</code> is
     *  <code>null</code>
     */
    public ValueChangeEvent(UIComponent component,
                             Object oldValue, Object newValue) {
        super(component);
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    /**
     * <p class="changed_added_2_3">Construct a new event object from the 
     * Faces context, specified source component, old value and new value.</p>
     *
     * <p>The default {@link PhaseId} for this event is {@link
     * PhaseId#ANY_PHASE}.</p>
     *
     * @param facesContext the Faces context.
     * @param component Source {@link UIComponent} for this event
     * @param oldValue The previous local value of this {@link UIComponent}
     * @param newValue The new local value of thie {@link UIComponent}
     * @throws IllegalArgumentException if <code>component</code> is
     *  <code>null</code>
     */
    public ValueChangeEvent(FacesContext facesContext, UIComponent component,
                             Object oldValue, Object newValue) {
        super(facesContext, component);
        this.oldValue = oldValue;
        this.newValue = newValue;
    }
    
    // -------------------------------------------------------------- Properties


    /**
     * <p>The previous local value of the source {@link UIComponent}.</p>
     */
    private Object oldValue = null;


    /**
     * <p>Return the previous local value of the source {@link UIComponent}.
     * </p>
     * 
     * @return the previous local value
     */
    public Object getOldValue() {

        return (this.oldValue);

    }


    /**
     * <p>The current local value of the source {@link UIComponent}.</p>
     */
    private Object newValue = null;


    /**
     * <p>Return the current local value of the source {@link UIComponent}.
     * </p>
     * 
     * @return the current local value
     */
    public Object getNewValue() {

        return (this.newValue);

    }


    // ------------------------------------------------- Event Broadcast Methods


    @Override
    public boolean isAppropriateListener(FacesListener listener) {

        return (listener instanceof ValueChangeListener);

    }

    /**
     * @throws AbortProcessingException {@inheritDoc}
     */ 
    @Override
    public void processListener(FacesListener listener) {

        ((ValueChangeListener) listener).processValueChange(this);

    }


}
