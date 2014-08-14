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

package javax.faces.component;

import javax.el.MethodExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.event.*;
import javax.faces.render.Renderer;


/**
 * <p><strong>UICommand</strong> is a {@link UIComponent} that represents
 * a user interface component which, when activated by the user, triggers
 * an application specific "command" or "action".  Such a component is
 * typically rendered as a push button, a menu item, or a hyperlink.</p>
 *
 * <p>When the <code>decode()</code> method of this {@link UICommand}, or
 * its corresponding {@link Renderer}, detects that this control has been
 * activated, it will queue an {@link ActionEvent}.
 * Later on, the <code>broadcast()</code> method will ensure that this
 * event is broadcast to all interested listeners.</p>
 * 
 * <p>Listeners will be invoked in the following order:
 * <ol>
 *  <li>{@link ActionListener}s, in the order in which they were registered.
 *  <li>The "actionListener" {@link MethodExpression} (which will cover
 *  the "actionListener" that was set as a <code>MethodBinding</code>).
 *  <li>The default {@link ActionListener}, retrieved from the
 *      {@link Application} - and therefore, any attached "action"
 *      {@link MethodExpression}.
 * </ol>
 * </p>
 * <p>By default, the <code>rendererType</code> property must be set to
 * "<code>javax.faces.Button</code>".  This value can be changed by calling the
 * <code>setRendererType()</code> method.</p>
 */

public class UICommand extends UIComponentBase
    implements ActionSource2 {


    // ------------------------------------------------------ Manifest Constants


    /**
     * <p>The standard component type for this component.</p>
     */
    public static final String COMPONENT_TYPE = "javax.faces.Command";


    /**
     * <p>The standard component family for this component.</p>
     */
    public static final String COMPONENT_FAMILY = "javax.faces.Command";


    /**
     * Properties that are tracked by state saving.
     */
    enum PropertyKeys {
        value,
        immediate,
        methodBindingActionListener,
        actionExpression,
    }


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Create a new {@link UICommand} instance with default property
     * values.</p>
     */
    public UICommand() {

        super();
        setRendererType("javax.faces.Button");

    }


    // -------------------------------------------------------------- Properties


    public String getFamily() {

        return (COMPONENT_FAMILY);

    }


    // ------------------------------------------------- ActionSource/ActionSource2 Properties


    /**
     * {@inheritDoc}
     *
     * @deprecated This has been replaced by {@link #getActionExpression}.
     */
    public MethodBinding getAction() {
        MethodBinding result = null;
        MethodExpression me;

        if (null != (me = getActionExpression())) {
            // if the MethodExpression is an instance of our private
            // wrapper class.
            if (me.getClass().equals(MethodExpressionMethodBindingAdapter.class)) {
                result = ((MethodExpressionMethodBindingAdapter) me).getWrapped();
            } else {
                // otherwise, this is a real MethodExpression.  Wrap it
                // in a MethodBinding.
                result = new MethodBindingMethodExpressionAdapter(me);
            }
        }
        return result;

    }

    /**
     * {@inheritDoc}
     *
     * @deprecated This has been replaced by {@link #setActionExpression(javax.el.MethodExpression)}.
     */
    public void setAction(MethodBinding action) {
        MethodExpressionMethodBindingAdapter adapter;
        if (null != action) {
            adapter = new MethodExpressionMethodBindingAdapter(action);
            setActionExpression(adapter);
        } else {
            setActionExpression(null);
        }
    }
    
    /**
     * {@inheritDoc}
     * @deprecated Use {@link #getActionListeners} instead.
     */
    public MethodBinding getActionListener() {
        return (MethodBinding) getStateHelper().get(PropertyKeys.methodBindingActionListener);
    }

    /**
     * {@inheritDoc}
     * @deprecated This has been replaced by {@link #addActionListener(javax.faces.event.ActionListener)}.
     */
    public void setActionListener(MethodBinding actionListener) {
        getStateHelper().put(PropertyKeys.methodBindingActionListener, actionListener);
    } 

    /**
     * <p>The immediate flag.</p>
     */
    //private Boolean immediate;


    public boolean isImmediate() {

        return (Boolean) getStateHelper().eval(PropertyKeys.immediate, false);

    }


    public void setImmediate(boolean immediate) {

        getStateHelper().put(PropertyKeys.immediate, immediate);

    }



    /**
     * <p>Returns the <code>value</code> property of the
     * <code>UICommand</code>. This is most often rendered as a label.</p>
     */
    public Object getValue() {

        return getStateHelper().eval(PropertyKeys.value);

    }


    /**
     * <p>Sets the <code>value</code> property of the <code>UICommand</code>.
     * This is most often rendered as a label.</p>
     *
     * @param value the new value
     */
    public void setValue(Object value) {

        getStateHelper().put(PropertyKeys.value, value);

    }


    // ---------------------------------------------------- ActionSource / ActionSource2 Methods

    
    public MethodExpression getActionExpression() {
        return (MethodExpression) getStateHelper().get(PropertyKeys.actionExpression);
    }
    
    public void setActionExpression(MethodExpression actionExpression) {
        getStateHelper().put(PropertyKeys.actionExpression, actionExpression);
    }
    
    /** 
     * @throws NullPointerException {@inheritDoc}
     */ 
    public void addActionListener(ActionListener listener) {

        addFacesListener(listener);

    }
    
    public ActionListener[] getActionListeners() {

        ActionListener al[] = (ActionListener [])
        getFacesListeners(ActionListener.class);
        return (al);

    }



    /**
     * @throws NullPointerException {@inheritDoc}
     */ 
    public void removeActionListener(ActionListener listener) {

        removeFacesListener(listener);

    }


    // ----------------------------------------------------- UIComponent Methods


    /**
     * <p>In addition to to the default {@link UIComponent#broadcast}
     * processing, pass the {@link ActionEvent} being broadcast to the
     * method referenced by <code>actionListener</code> (if any),
     * and to the default {@link ActionListener} registered on the
     * {@link javax.faces.application.Application}.</p>
     *
     * @param event {@link FacesEvent} to be broadcast
     *
     * @throws AbortProcessingException Signal the JavaServer Faces
     *  implementation that no further processing on the current event
     *  should be performed
     * @throws IllegalArgumentException if the implementation class
     *  of this {@link FacesEvent} is not supported by this component
     * @throws NullPointerException if <code>event</code> is
     * <code>null</code>
     */
    public void broadcast(FacesEvent event) throws AbortProcessingException {

        // Perform standard superclass processing (including calling our
        // ActionListeners)
        super.broadcast(event);

        if (event instanceof ActionEvent) {
            FacesContext context = getFacesContext();
            
            // Notify the specified action listener method (if any)
            MethodBinding mb = getActionListener();
            if (mb != null) {
                mb.invoke(context, new Object[] { event });
            }

            // Invoke the default ActionListener
            ActionListener listener =
              context.getApplication().getActionListener();
            if (listener != null) {
                listener.processAction((ActionEvent) event);
            }
        }
    }

    /**

     * <p>Intercept <code>queueEvent</code> and take the following
     * action.  If the event is an <code>{@link ActionEvent}</code>,
     * obtain the <code>UIComponent</code> instance from the event.  If
     * the component is an <code>{@link ActionSource}</code> obtain the
     * value of its "immediate" property.  If it is true, mark the
     * phaseId for the event to be
     * <code>PhaseId.APPLY_REQUEST_VALUES</code> otherwise, mark the
     * phaseId to be <code>PhaseId.INVOKE_APPLICATION</code>.  The event
     * must be passed on to <code>super.queueEvent()</code> before
     * returning from this method.</p>

     */

    public void queueEvent(FacesEvent e) {
        UIComponent c = e.getComponent();
        if (e instanceof ActionEvent && c instanceof ActionSource) {
            if (((ActionSource) c).isImmediate()) {
                e.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
            } else {
                e.setPhaseId(PhaseId.INVOKE_APPLICATION);
            }
        }
        super.queueEvent(e);
    }
}
