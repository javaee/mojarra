/*
 * Copyright 2002, 2003 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * 
 * - Redistribution in binary form must reproduce the above
 *   copyright notice, this list of conditions and the following
 *   disclaimer in the documentation and/or other materials
 *   provided with the distribution.
 *    
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *  
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 * DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR
 * RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THIS SOFTWARE OR
 * ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 * SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 * THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 * BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *  
 * You acknowledge that this software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
 */

package components.components;


import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;

import javax.faces.FacesException;
import javax.faces.component.ActionSource;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import javax.faces.event.PhaseId;

/**
 * <p>{@link MapComponent} is a JavaServer Faces component that corresponds
 * to a client-side image map.  It can have one or more children of type
 * {@link AreaComponent}, each representing hot spots, which a user can
 * click on and mouse over.</p>
 *
 * <p>This component is a source of {@link AreaSelectedEvent} events,
 * which are fired whenever the current area is changed.</p>
 */

public class MapComponent extends UIComponentBase
    implements ActionSource {


    // ------------------------------------------------------ Instance Variables


    private String current = null;

    private String action = null;
    private String actionListenerRef = null;
    private String actionRef = null;
    private boolean immediate = false;
    private boolean immediateSet = false;



    // --------------------------------------------------------------Constructors 

    public MapComponent() {
	super();
        addDefaultActionListener(getFacesContext());
    }

    
    // -------------------------------------------------------------- Properties


    /**
     * <p>Return the alternate text label for the currently selected
     * child {@link AreaComponent}.</p>
     */
    public String getCurrent() {
        return (this.current);
    }


    /**
     * <p>Set the alternate text label for the currently selected child.
     * If this is different from the previous value, fire an
     * {@link AreaSelectedEvent} to interested listeners.</p>
     *
     * @param current The new alternate text label
     */
    public void setCurrent(String current) {

        String previous = this.current;
        this.current = current;

        // Fire an {@link AreaSelectedEvent} if appropriate
        if ((previous == null) && (current == null)) {
            return;
        } else if ((previous != null) && (current != null) &&
                   (previous.equals(current))) {
            return;
        } else {
            this.queueEvent(new AreaSelectedEvent(this));
        }

    }

    // -------------------------------------------------- Action Source Methods

    public String getAction() {
	if (this.action != null) {
	    return (this.action);
	}
	ValueBinding vb = getValueBinding("action");
	if (vb != null) {
	    return ((String) vb.getValue(getFacesContext()));
	} else {
	    return (null);
	}
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getActionListenerRef() {
        return (this.actionListenerRef);
    }


    public void setActionListenerRef(String actionListenerRef) {
        this.actionListenerRef = actionListenerRef;
    }

    public String getActionRef() {
        return (this.actionRef);
    }

    public void setActionRef(String actionRef) {
        this.actionRef = actionRef;
    }

    public boolean isImmediate() {
	if (this.immediateSet) {
	    return (this.immediate);
	}
	ValueBinding vb = getValueBinding("immediate");
	if (vb != null) {
	    Boolean value = (Boolean) vb.getValue(getFacesContext());
	    return (value.booleanValue());
	} else {
	    return (this.immediate);
	}
    }

    public void setImmediate(boolean immediate) {
	// if the immediate value is changing.
	if (immediate != this.immediate) {
	    FacesContext context = getFacesContext();
	    // remove the current default action listener
	    removeDefaultActionListener(context);
	    this.immediate = immediate;
	    addDefaultActionListener(context);
	}
	this.immediateSet = true;
    }
    
    public void addActionListener(ActionListener listener) {
        addFacesListener(listener);
    }

    public ActionListener[] getActionListeners() {
        FacesListener fl[] = getFacesListeners(ActionListener.class);
        ActionListener al[] = new ActionListener[fl.length];
        for (int i = 0; i < fl.length; i++) {
            al[i] = (ActionListener) fl[i];
        }
        return (al);
    }

    public void removeActionListener(ActionListener listener) {
        removeFacesListener(listener);
    }

    // ----------------------------------------------------- Event Methods

    private static Class signature[] = { AreaSelectedEvent.class };

    /**
     * Pass the {@link ActionEvent} being broadcast to the method referenced
     * by <code>actionListenerRef</code> (if any).
     *
     * @param event {@link FacesEvent} to be broadcast
     * @param phaseId {@link PhaseId} of the current phase of the
     *  request processing lifecycle
     *
     * @exception AbortProcessingException Signal the JavaServer Faces
     *  implementation that no further processing on the current event
     *  should be performed
     */
    public boolean broadcast(FacesEvent event, PhaseId phaseId)
        throws AbortProcessingException {

        // Perform standard superclass processing
        boolean returnValue = super.broadcast(event, phaseId);

        // Notify the specified action listener method (if any)
        String actionListenerRef = getActionListenerRef();
        if (actionListenerRef != null) {
            if ((isImmediate() &&
                 phaseId.equals(PhaseId.APPLY_REQUEST_VALUES)) ||
                (!isImmediate() &&
                 phaseId.equals(PhaseId.INVOKE_APPLICATION))) {
                FacesContext context = getFacesContext();
                MethodBinding mb =
                    context.getApplication().getMethodBinding
                    (actionListenerRef, signature);
                try {
                    mb.invoke(context, new Object[] { event });
                } catch (InvocationTargetException e) {
                    throw new FacesException(e.getTargetException());
                }
            }
        }

        // Return the flag indicating future interest
        return (returnValue);
    }


    // ----------------------------------------------------- StateHolder Methods


    /**
     * <p>Return the state to be saved for this component.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     */
    public Object saveState(FacesContext context) {
	removeDefaultActionListener(context);
        Object values[] = new Object[7];
        values[0] = super.saveState(context);
        values[1] = current;
        values[2] = action;
        values[3] = actionListenerRef;
        values[4] = actionRef;
        values[5] = immediate ? Boolean.TRUE : Boolean.FALSE;
        values[6] = immediateSet ? Boolean.TRUE : Boolean.FALSE;
        addDefaultActionListener(context);
        return (values);
    }


    /**
     * <p>Restore the state for this component.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     * @param state State to be restored
     *
     * @exception IOException if an input/output error occurs
     */
    public void restoreState(FacesContext context, Object state) {
	removeDefaultActionListener(context);
        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        current = (String) values[1];
        action = (String) values[2];
        actionListenerRef = (String) values[3];
        actionRef = (String) values[4];
        immediate = ((Boolean) values[5]).booleanValue();
        immediateSet = ((Boolean) values[6]).booleanValue();
        addDefaultActionListener(context);
    }
    
    // ----------------------------------------------------- Private Methods

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
