/*
 * $Id: UICommandBase.java,v 1.8 2003/08/27 22:34:01 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component.base;


import java.util.Iterator;
import java.util.List;
import javax.faces.component.UICommand;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.event.PhaseId;
import java.io.IOException;


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


    /**
     * <p>The immediate flag.</p>
     */
    // PENDING(craigmcc) - not saved as part of the state yet,
    // will be picked up when that mechanism is remodelled
    private boolean immediate = false;


    public boolean isImmediate() {

        return (this.immediate);

    }


    public void setImmediate(boolean immediate) {

        this.immediate = immediate;

    }


    // -------------------------------------------------- Event Listener Methods


    public void addActionListener(ActionListener listener) {

        addFacesListener(listener);

    }


    public void removeActionListener(ActionListener listener) {

        removeFacesListener(listener);

    }


    // ----------------------------------------------------- StateHolder Methods


    public Object getState(FacesContext context) {

        removeDefaultActionListener(context);

        Object values[] = new Object[4];
        values[0] = super.getState(context);
        values[1] = action;
        values[2] = actionRef;
        values[3] = immediate ? Boolean.TRUE : Boolean.FALSE;

        addDefaultActionListener(context);
        return (values);

    }


    public void restoreState(FacesContext context, Object state)
        throws IOException {

        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        action = (String) values[1];
        actionRef = (String) values[2];
        immediate = ((Boolean) values[3]).booleanValue();

        addDefaultActionListener(context);

    }


    // --------------------------------------------------------- Private Methods


    // Add the default action listener
    private void addDefaultActionListener(FacesContext context) {

        ActionListener listener =
            context.getApplication().getActionListener();
        if (immediate) {
            addActionListener(new WrapperActionListener(listener));
        } else {
            addActionListener(listener);
        }

    }


    // Remove the default action listener
    private void removeDefaultActionListener(FacesContext context) {

        if (immediate) {
            if (listeners == null) {
                return;
            }
            List list = listeners[PhaseId.APPLY_REQUEST_VALUES.getOrdinal()];
            if (list == null) {
                return;
            }
            Iterator items = list.iterator();
            while (items.hasNext()) {
                Object item = items.next();
                if (item instanceof WrapperActionListener) {
                    removeActionListener((ActionListener) item);
                    return;
                }
            }
        } else {
            removeActionListener(context.getApplication().getActionListener());
        }

    }



    // ------------------------------------------------ Private Wrapper Listener


    // Wrapper for the default ActionListener that overrides getPhaseId()
    // to return PhaseId.APPLY_REQUEST_VALUES, for "immediate" commands
    private class WrapperActionListener implements ActionListener {

        public WrapperActionListener(ActionListener wrapped) {
            this.wrapped = wrapped;
        }

        private ActionListener wrapped;

        public PhaseId getPhaseId() {
            return (PhaseId.APPLY_REQUEST_VALUES);
        }

        public void processAction(ActionEvent event) {
            wrapped.processAction(event);
        }

    }


}
