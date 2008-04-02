/*
 * $Id: UICommand.java,v 1.30 2003/01/17 00:26:46 craigmcc Exp $
 */

/*
 * Copyright 2002-2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.event.FacesEvent;
import javax.faces.event.FormEvent;
import javax.faces.event.PhaseId;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * <p><strong>UICommand</strong> is a {@link UIComponent} that represents
 * a user interface component which, when activated by the user, triggers
 * an application specific "command" or "action".  Such a component is
 * typically rendered as a push button, a menu item, or a hyperlink.</p>
 *
 * <p>When the <code>decode()</code> method of this {@link UICommand}, or
 * its corresponding <code>Renderer</code>, detects that this control has
 * been activated, it will queue an {@link ActionEvent}.  Later on,
 * the <code>broadcast()</code> method will ensure that this event is
 * broadcast to all interested listeners.</p>
 */

public class UICommand extends UIComponentBase {


    // ------------------------------------------------------- Static Variables


    /**
     * The component type of this {@link UIComponent} subclass.
     */
    public static final String TYPE = "javax.faces.component.UICommand";


    // ------------------------------------------------------------- Properties


    /**
     * <p>Return the command name associated with this command.</p>
     */
    public String getCommandName() {

        return ((String) getAttribute("value"));

    }


    /**
     * <p>Set the command name for this <code>UICommand</code>.</p>
     *
     * @param commandName The new command name
     */
    public void setCommandName(String commandName) {

        setAttribute("value", commandName);

    }

    
    /**
     * <p>Return the component type of this <code>UIComponent</code>.</p>
     */
    public String getComponentType() {

        return (TYPE);

    }


    // ------------------------------------------- Lifecycle Processing Methods


    /**
     * <p>Enqueue a {@link FormEvent} event to the application identifying
     * the form submission that has occurred, along with the command name
     * of the {@link UICommand} that caused the form to be submitted, if any.
     * </p>
     *
     * @param context FacesContext for the request we are processing
     *
     * @exception IOException if an input/output error occurs while reading
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public void decode(FacesContext context) throws IOException {

        if (context == null) {
            throw new NullPointerException();
        }

        // Delegate to our associated Renderer if needed
        if (getRendererType() != null) {
            super.decode(context);
            return;
        }

        // Was our command the one that caused this submission?
        setValid(true);
        String value = context.getServletRequest().
            getParameter(getClientId(context));
        if (value == null) {
            return;
        }

        // Fire an ActionEvent for broadcast to interested listeners
        fireActionEvent(context);

        // Construct and enqueue a FormEvent for the application
        String commandName = (String) currentValue(context);
        String formName = null;
        UIComponent parent = getParent();
        while (parent != null) {
            if (parent instanceof UIForm) {
                formName = (String) parent.currentValue(context);
                break;
            }
            parent = parent.getParent();
        }
        if (formName == null) {
            return; // Not nested in a form
        }
        context.addApplicationEvent
            (new FormEvent(this, formName, commandName));

    }


    /**
     * <p>Render the current value of this component as an HTML submit
     * button, if the value of the rendered attribute is <code>true</code>. </p>
     *
     * @param context FacesContext for the response we are creating
     *
     * @exception IOException if an input/output error occurs while rendering
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public void encodeEnd(FacesContext context) throws IOException {

        if (context == null) {
            throw new NullPointerException();
        }

        // Delegate to our associated Renderer if needed
        if (getRendererType() != null) {
            super.encodeEnd(context);
            return;
        }

        // if rendered is false, do not perform default encoding.
        if (!isRendered()) {
            return;
        }

        // Perform default encoding
        ResponseWriter writer = context.getResponseWriter();
        writer.write("<input type=\"submit\"");
        Object currentValue = currentValue(context);
        if (currentValue != null) {
            writer.write(" name=\"");
            writer.write(getClientId(context));
            writer.write("\"");
            writer.write(" value=\"");
            writer.write(currentValue.toString());
            writer.write("\"");
        }
        writer.write(">");

    }


    /**
     * <p>Suppress model updates for this component.</p>
     *
     * @param context FacesContext for the request we are processing
     *
     * @exception IllegalArgumentException if the <code>modelReference</code>
     *  property has invalid syntax for an expression
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public boolean updateModel(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }
        return (true);

    }


    // ----------------------------------------------- Event Processing Methods


    /**
     * <p>Array of {@link List}s of {@link ActionListener}s registered
     * for particular phases.  The array, as well as the individual
     * elements, are lazily instantiated as necessary.</p>
     */
    protected List listeners[] = null;


    /**
     * <p>Add a new {@link ActionListener} to the set of listeners interested
     * in being notified when {@link ActionEvent}s occur.</p>
     *
     * @param listener The {@link ActionListener} to be added
     *
     * @exception NullPointerException if <code>listener</code>
     *  is <code>null</code>
     */
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


    /**
     * <p>Broadcast the specified {@link FacesEvent} to all registered
     * event listeners who have expressed an interest in events of this
     * type, for the specified {@link PhaseId}.  The order in which
     * registered listeners are notified is implementation dependent.</p>
     *
     * <p>After all interested listeners have been notified, return
     * <code>false</code> if this event does not have any listeners
     * interested in this event in future phases of the request processing
     * lifecycle.  Otherwise, return <code>true</code>.</p>
     *
     * @param event The {@link FacesEvent} to be broadcast
     * @param phaseId The {@link PhaseId} of the current phase of the
     *  request processing lifecycle
     *
     * @exception AbortProcessingException Signal the JavaServer Faces
     *  implementation that no further processing on the current event
     *  should be performed
     * @exception IllegalArgumentException if the implementation class
     *  of this {@link FacesEvent} is not supported by this component
     * @exception IllegalStateException if PhaseId.ANY_PHASE is passed
     *  for the phase identifier
     * @exception NullPointerException if <code>event</code> or
     *  <code>phaseId</code> is <code>null</code>
     */
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


    /**
     * <p>Queue an {@link ActionEvent} for processing during the next
     * event processing cycle.</p>
     *
     * @param context The {@link FacesContext} for the current request
     */
    public void fireActionEvent(FacesContext context) {

        context.addFacesEvent(new ActionEvent(this, getCommandName()));

    }


    /**
     * <p>Remove an existing {@link ActionListener} (if any) from the set of
     * listeners interested in being notified when {@link ActionEvent}s
     * occur.</p>
     *
     * @param listener The {@link ActionListener} to be removed
     *
     * @exception NullPointerException if <code>listener</code>
     *  is <code>null</code>
     */
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
