/*
 * $Id: UICommandBase.java,v 1.2 2003/07/26 17:54:48 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component.base;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.faces.component.UICommand;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;


/**
 * <p><strong>UICommandBase</strong> is a convenience base class that
 * implements the default concrete behavior of all methods defined by
 * {@link UICommand}.</p>
 */

public class UICommandBase extends UIOutputBase implements UICommand {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Create a new {@link UICommandBase} instance with default property
     * values.</p>
     */
    public UICommandBase() {

        super();
        setRendererType("Button");

    }


    // -------------------------------------------------------------- Properties


    /**
     * <p>The literal outcome value.</p>
     */
    private String action = null;


    public String getAction() {

        return (this.action);

    }


    public void setAction(String action) {

        this.action = action;

    }


    /**
     * <p>The action reference.</p>
     */
    private String actionRef = null;


    public String getActionRef() {

        return (this.actionRef);

    }


    public void setActionRef(String actionRef) {

        this.actionRef = actionRef;

    }


    // ------------------------------------------------ Event Processing Methods


    /**
     * <p>Array of {@link List}s of {@link ActionListener}s registered
     * for particular phases.  The array, as well as the individual
     * elements, are lazily instantiated as necessary.</p>
     */
    protected List listeners[] = null;


    public void addActionListener(ActionListener listener) {

        if (listener == null) {
            throw new NullPointerException();
        }
        if (listeners == null) {
            listeners = new List[PhaseId.VALUES.size()];
        }
        int ordinal = listener.getPhaseId().getOrdinal();
        if (listeners[ordinal] == null) {
            listeners[ordinal] = new ArrayList();
        }
        listeners[ordinal].add(listener);

    }


    public boolean broadcast(FacesEvent event, PhaseId phaseId)
        throws AbortProcessingException {

        if ((event == null) || (phaseId == null)) {
            throw new NullPointerException();
        }
        if (phaseId.equals(PhaseId.ANY_PHASE)) {
            throw new IllegalStateException();
        }
        if (event instanceof ActionEvent) {
            if (listeners == null) {
                return (false);
            }
            ActionEvent aevent = (ActionEvent) event;
            int ordinal = phaseId.getOrdinal();
            broadcast(aevent, listeners[PhaseId.ANY_PHASE.getOrdinal()]);
            broadcast(aevent, listeners[ordinal]);
            for (int i = ordinal + 1; i < listeners.length; i++) {
                if ((listeners[i] != null) && (listeners[i].size() > 0)) {
                    return (true);
                }
            }
            return (false);
        } else {
            throw new IllegalArgumentException();
        }

    }


    /**
     * <p>Broadcast the specified {@link ActionEvent} to the
     * {@link ActionListener}s on the specified list (if any)
     *
     * @param event The {@link ActionEvent} to be broadcast
     * @param list The list of {@link ActionListener}s, or
     *  <code>null</code> for no interested listeners
     */
    protected void broadcast(ActionEvent event, List list) {

        if (list == null) {
            return;
        }
        Iterator listeners = list.iterator();
        while (listeners.hasNext()) {
            ActionListener listener = (ActionListener) listeners.next();
            listener.processAction(event);
        }

    }


    public void removeActionListener(ActionListener listener) {

        if (listener == null) {
            throw new NullPointerException();
        }
        if (listeners == null) {
            return;
        }
        int ordinal = listener.getPhaseId().getOrdinal();
        if (listeners[ordinal] == null) {
            return;
        }
        listeners[ordinal].remove(listener);

    }


}
