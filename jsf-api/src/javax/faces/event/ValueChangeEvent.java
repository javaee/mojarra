/*
 * $Id: ValueChangeEvent.java,v 1.8 2005/12/05 16:42:54 edburns Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

package javax.faces.event;


import javax.faces.component.UIComponent;


/**
 * <p>A {@link ValueChangeEvent} is a notification that the local value of
 * the source component has been change as a result of user interface
 * activity.  It is not fired unless validation of the new value was
 * completed successfully.</p>
 */

public class ValueChangeEvent extends FacesEvent {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Construct a new event object from the specified source component,
     * old value, and new value.</p>
     *
     * <p>The default {@link PhaseId} for this event is {@link
     * PhaseId#ANY_PHASE}.</p>
     *
     * @param component Source {@link UIComponent} for this event
     * @param oldValue The previous local value of this {@link UIComponent}
     * @param newValue The new local value of thie {@link UIComponent}
     *
     * @throws IllegalArgumentException if <code>component</code> is
     *  <code>null</code>
     */
    public ValueChangeEvent(UIComponent component,
                             Object oldValue, Object newValue) {

        super(component);
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
     */
    public Object getNewValue() {

        return (this.newValue);

    }


    // ------------------------------------------------- Event Broadcast Methods


    public boolean isAppropriateListener(FacesListener listener) {

        return (listener instanceof ValueChangeListener);

    }

    /**
     * @throws AbortProcessingException {@inheritDoc}
     */ 
    public void processListener(FacesListener listener) {

        ((ValueChangeListener) listener).processValueChange(this);

    }


}
