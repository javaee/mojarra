/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package javax.faces.component;

import javax.el.MethodExpression;
import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextWrapper;
import javax.faces.el.MethodBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PreRenderViewEvent;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.view.ViewMetadata;
import javax.faces.webapp.FacesServlet;

/**

 * <p class="changed_added_2_2"><strong>UIViewAction</strong> represents
 * a method invocation that occurs during the request processing
 * lifecycle, usually in response to an initial request, not a
 * postback.</p>

 * <div class="changed_added_2_2">

 * <p>The {@link javax.faces.view.ViewDeclarationLanguage}
 * implementation must cause an instance of this component to be placed
 * in the view for each occurrence of an <code>&lt;f:viewAction
 * /&gt;</code> element placed inside of an <code>&lt;f:metadata
 * /&gt;</code> element.  The user must place <code>&lt;f:metadata
 * /&gt;</code> within the <code>UIViewRoot</code>.</p>

 * <p>Because this class implements {@link ActionSource2}, any actions
 * that one would normally take on a component that implements
 * <code>ActionSource2</code>, such as {@link UICommand}, are valid for
 * instances of this class.  Instances of this class participate in the
 * regular JSF lifecycle, including on Ajax requests.</p>

 * <p>The purpose of this component is to provide a light-weight
 * front-controller solution for executing code upon the loading of a
 * JSF view to support the integration of system services, content
 * retrieval, view management, and navigation. This functionality is
 * especially useful for non-faces (initial) requests.</p>

 * <p>The most common use case for this component is to take actions
 * necessary for a particular view, often with the help of one or more
 * {@link UIViewParameter}s.</p>

 * The {@link NavigationHandler} is consulted after the action is invoked to carry out the navigation case that matches the
 * action signature and outcome. If a navigation case is matched, or the response is marked complete by the action, subsequent
 * {@link UIViewAction} components associated with the current view are short-circuited. The lifecycle then advances
 * appropriately.
 * </p>
 * <p/>
 * <p>
 * It's important to note that the full component tree is not built before the UIViewAction components are processed on an
 * non-faces (initial) request. Rather, the component tree only contains the {@link ViewMetadata}, an important part of the
 * optimization of this component and what sets it apart from a {@link PreRenderViewEvent} listener.
 * </p>
 *
 * </div>

 * @since 2.2
 */
public class UIViewAction extends UIComponentBase implements ActionSource2 {

    // ------------------------------------------------------ Manifest Constants

    /**
     * <p>
     * The standard component type for this component.
     * </p>
     */
    public static final String COMPONENT_TYPE = "javax.faces.ViewAction";

    /**
     * <p>
     * The standard component family for this component.
     * </p>
     */
    public static final String COMPONENT_FAMILY = "javax.faces.ViewAction";

    /**
     * Properties that are tracked by state saving.
     */
    enum PropertyKeys {

        onPostback, actionExpression, immediate, phase, ifAttr("if");
        private String name;

        PropertyKeys() {
        }

        PropertyKeys(final String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name != null ? name : super.toString();
        }
    }

    // ------------------------------------------------------------ Constructors

    /**
     * <p>
     * Create a new {@link UIViewAction} instance with default property values.
     * </p>
     */
    public UIViewAction() {
        super();
        setRendererType(null);
    }

    // -------------------------------------------------------------- Properties
    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    /**
     * {@inheritDoc}
     *
     * @since 2.2
     */
    @Deprecated
    public MethodBinding getAction() {
        MethodBinding result = null;
        MethodExpression me;

        if (null != (me = getActionExpression())) {
            result = new MethodBindingMethodExpressionAdapter(me);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     *
     * @since 2.2
     */
    @Deprecated
    @SuppressWarnings("deprecation")
    public void setAction(final MethodBinding action) {
    }

    /**
     * {@inheritDoc}
     *
     * @since 2.2
     */
    @Deprecated
    @SuppressWarnings("deprecation")
    public MethodBinding getActionListener() {
        throw new UnsupportedOperationException("Not supported.");
    }

    /**
     * {@inheritDoc}
     *
     * @since 2.2
     */
    @Deprecated
    @SuppressWarnings("deprecation")
    public void setActionListener(final MethodBinding actionListener) {
        throw new UnsupportedOperationException("Not supported.");
    }

    /**
     * {@inheritDoc}
     *
     * @since 2.2
     */
    public boolean isImmediate() {
        return (Boolean) getStateHelper().eval(PropertyKeys.immediate, false);
    }

    /**
     * {@inheritDoc}
     *
     * @since 2.2
     */
    public void setImmediate(final boolean immediate) {
        getStateHelper().put(PropertyKeys.immediate, immediate);
    }

    /**
     * <p class="changed_added_2_2">Returns the name of the lifecycle
     * phase in which the action is to be queued.</p>
     *
     * @since 2.2
     */
    public String getPhase() {
        PhaseId myPhaseId = (PhaseId) getStateHelper().eval(PropertyKeys.phase);
        String result = null;
        if (null != myPhaseId) {
            result = myPhaseId.getName();
        }
        return result;
    }

    /**
     * <p class="changed_added_2_2">Attempt to set the lifecycle phase
     * in which this instance will queue its {@link ActionEvent}.  Pass
     * the argument <code>phase</code> to {@link
     * PhaseId#phaseIdValueOf}.  If the result is not one of the
     * following values, <code>FacesException</code> must be thrown.</p>
     *
     * <ul>

     * <li><p>{@link PhaseId#APPLY_REQUEST_VALUES}</p></li>
     * <li><p>{@link PhaseId#PROCESS_VALIDATIONS}</p></li>
     * <li><p>{@link PhaseId#UPDATE_MODEL_VALUES}</p></li>
     * <li><p>{@link PhaseId#INVOKE_APPLICATION}</p></li>

     * </ul>

     * <p>If set, this value takes precedence over the immediate flag.</p>

     * @since 2.2
     */

    public void setPhase(final String phase) {
        PhaseId myPhaseId = PhaseId.phaseIdValueOf(phase);
        if (PhaseId.ANY_PHASE.equals(myPhaseId) || 
            PhaseId.RESTORE_VIEW.equals(myPhaseId) || 
            PhaseId.RENDER_RESPONSE.equals(myPhaseId)) {
            throw new FacesException("View actions cannot be executed in specified phase: [" + myPhaseId.toString() + "]");
        }
        getStateHelper().put(PropertyKeys.phase, myPhaseId);
    }

    private PhaseId getPhaseId() {
        PhaseId myPhaseId = (PhaseId) getStateHelper().eval(PropertyKeys.phase);
        return myPhaseId;
    }

    /**
     * {@inheritDoc}
     *
     * @since 2.2
     */
    @Override
    public void addActionListener(final ActionListener listener) {
    }

    /**
     * {@inheritDoc}
     *
     * @since 2.2
     */
    @Override
    public ActionListener[] getActionListeners() {
        return new ActionListener[0];
    }

    /**
     * {@inheritDoc}
     *
     * @since 2.2
     */
    @Override
    public void removeActionListener(final ActionListener listener) {
        throw new UnsupportedOperationException("Not supported.");
    }

    /**
     * {@inheritDoc}
     *
     * @since 2.2
     */
    @Override
    public MethodExpression getActionExpression() {
        return (MethodExpression) getStateHelper().get(PropertyKeys.actionExpression);
    }

    /**
     * {@inheritDoc}
     *
     * @since 2.2
     */
    @Override
    public void setActionExpression(final MethodExpression actionExpression) {
        getStateHelper().put(PropertyKeys.actionExpression, actionExpression);
    }

    /**
     * <p class="changed_added_2_2">If <code>true</code> this
     * component will operate on postback.</p>

     * @since 2.2
     */
    public boolean isOnPostback() {
        return (Boolean) getStateHelper().eval(PropertyKeys.onPostback, false);
    }

    /**
     * <p class="changed_added_2_2">Controls whether or not this
     * component operates on postback.</p>

     * @since 2.2
     */
    public void setOnPostback(final boolean onPostback) {
        getStateHelper().put(PropertyKeys.onPostback, onPostback);
    }

    /**
     * <p class="changed_added_2_2">Return <code>true</code> if this
     * component should take the actions specified in the {@link
     * #decode} method.</p>
     * 
     * @since 2.2
     */

    public boolean isIf() {
        return (Boolean) getStateHelper().eval(PropertyKeys.ifAttr, true);
    }

    /**
     * <p class="changed_added_2_2">Sets the <code>if</code> property
     * of this component.</p>
     *
     * @param condition the new value of the property.
     *
     * @since 2.2
     */
    public void setIf(final boolean condition) {
        getStateHelper().put(PropertyKeys.ifAttr, condition);
    }

    // ----------------------------------------------------- UIComponent Methods

    /**
     * <p class="changed_added_2_2">Enable the method invocation
     * specified by this component instance to return a value that
     * performs navigation, similar in spirit to {@link
     * UICommand#broadcast}.</p>

     * <div class="changed_added_2_2">

     * <p>Take no action and return immediately if any of the following
     * conditions are true.</p>

     * <ul>

     * <li><p>The response has already been marked as complete.</p></li>

     * <li><p>The current <code>UIViewRoot</code> is different from the
     * event's source's v<code>UIViewRoot</code>.</p></li>

     * </ul>

     * <p>Save a local reference to the current
     * <code>UIViewRoot</code>.</p>

     * <p>Obtain the {@link ActionListener} from the {@link
     * javax.faces.application.Application}.  Wrap the current {@link
     * FacesContext} in an implementation of {@link
     * javax.faces.context.FacesContextWrapper} that overrides the
     * {@link FacesContext#renderResponse} method to take no action.
     * Set the current <code>FacesContext</code> to be the
     * <code>FacesContextWrapper</code> instance.  Invoke {@link
     * ActionListener#processAction}.  The specification requires the
     * implementation of <code>processAction()</code> to call
     * <code>renderResponse()</code> but this will have no effect due to
     * the wrapping.  Restore the original
     * <code>FacesContext</code>, and discard the wrapper.</p>

     * <p>If the response has been marked as complete during the
     * invocation of <code>processAction()</code>, take no further
     * action and return.  Otherwise, compare the
     * <code>UIViewRoot</code> saved before invocation of
     * <code>processAction()</code> with the one on the
     * <code>FacesContext</code> after the invocation.  If the two
     * <code>UIViewRoot</code>s are the same, call {@link
     * FacesContext#renderResponse} and return.  Otherwise, execute the
     * lifecycle on the new <code>UIViewRoot</code> PENDING(edburns).
     * Obviously need more details here.</p>

     * </div>
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
     * 
     * @since 2.2
     */
    @Override
    public void broadcast(final FacesEvent event) throws AbortProcessingException {

        super.broadcast(event);

        FacesContext context = getFacesContext();
        if (!(event instanceof ActionEvent)) {
            throw new IllegalArgumentException();
        }

        // OPEN QUESTION: should we consider a navigation to the same view as a
        // no-op navigation?

        // only proceed if the response has not been marked complete and
        // navigation to another view has not occurred
        if (!context.getResponseComplete() && (context.getViewRoot() == getViewRootOf(event))) {
            ActionListener listener = context.getApplication().getActionListener();
            if (listener != null) {
                UIViewRoot viewRootBefore = context.getViewRoot();
                InstrumentedFacesContext instrumentedContext = new InstrumentedFacesContext(context);
                // defer the call to renderResponse() that happens in
                // ActionListener#processAction(ActionEvent)
                instrumentedContext.disableRenderResponseControl().set();
                listener.processAction((ActionEvent) event);
                instrumentedContext.restore();
                // if the response is marked complete, the story is over
                if (!context.getResponseComplete()) {
                    UIViewRoot viewRootAfter = context.getViewRoot();
                    // if the view id changed as a result of navigation, then execute
                    // the JSF lifecycle for the new view id
                    if (viewRootBefore != viewRootAfter) {
                        /*
                         * // execute the JSF lifecycle by dispatching a forward request // this approach is problematic because
                         * it throws a wrench in the event broadcasting try { context.getExternalContext
                         * ().dispatch(context.getApplication() .getViewHandler().getActionURL(context,
                         * viewRootAfter.getViewId()) .substring(context.getExternalContext
                         * ().getRequestContextPath().length())); // kill this lifecycle execution context.responseComplete(); }
                         * catch (IOException e) { throw new FacesException("Dispatch to viewId failed: " +
                         * viewRootAfter.getViewId(), e); }
                         */

                        // manually execute the JSF lifecycle on the new view id
                        // certain tweaks have to be made to the FacesContext to allow
                        // us to reset the lifecycle
                        Lifecycle lifecycle = getLifecycle(context);
                        instrumentedContext = new InstrumentedFacesContext(context);
                        instrumentedContext.pushViewIntoRequestMap().clearViewRoot().clearPostback().set();
                        lifecycle.execute(instrumentedContext);
                        instrumentedContext.restore();

                        /*
                         * Another approach would be to register a result listener in the decode() method for the result in which
                         * the action is set to invoke. The result listener would performs a servlet forward if a non-redirect
                         * navigation occurs after the result.
                         */
                    } else {
                        // apply the deferred call (relevant when immediate is true)
                        context.renderResponse();
                    }
                }
            }
        }
    }

    /**
     * <p class="changed_added_2_2">Override behavior from the
     * superclass to queue an {@link ActionEvent} that may result in the
     * invocation of the <code>action</code> or any
     * <code>actionListener</code>s that may be associated with this
     * instance.</p>

     * <div class="changed_added_2_2">

     * <p>Take no action if any of the following conditions are true:</p>

     * <ul>

     * 	  <li><p>The current request is a postback and the instance has
     * been configured to not operate on postback. See {@link #isOnPostback}.</p></li>

     * 	  <li><p>The condition stated in the <code>if</code> property
     * evaluates to <code>false</code>.  See {@link #isIf}</p>.</li>

     * </ul>

     * <p>Instantiate an {@link ActionEvent}, passing this component
     * instance as the source.  Set the <code>phaseId</code> property of
     * the <code>ActionEvent</code> as follows.</p>

     * <ul>

     * <li><p>If this component instance has been configured with a
     * specific lifecycle phase with a call to {@link #setPhase} use
     * that as the <code>phaseId</code></p></li>

     * <li><p>If the value of the <code>immediate</code> property is
     * true, use {@link PhaseId#APPLY_REQUEST_VALUES}.</p></li>

     * <li><p>Otherwise, use {@link PhaseId#INVOKE_APPLICATION}.
     * </p></li>

     * </ul>

     * <p>Queue the event with a call to {@link #queueEvent}.</p>

     * </div>
     * 
     * @since 2.2

     */
    @Override
    public void decode(final FacesContext context) {
        if (context == null) {
            throw new NullPointerException();
        }

        if ((context.isPostback() && !isOnPostback()) || !isIf()) {
            return;
        }

        ActionEvent e = new ActionEvent(this);
        PhaseId phaseId = getPhaseId();
        if (phaseId != null) {
            e.setPhaseId(phaseId);
        } else if (isImmediate()) {
            e.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
        } else {
            e.setPhaseId(PhaseId.INVOKE_APPLICATION);
        }

        queueEvent(e);
    }

    private UIViewRoot getViewRootOf(final FacesEvent e) {
        UIComponent c = e.getComponent();
        do {
            if (c instanceof UIViewRoot) {
                return (UIViewRoot) c;
            }
            c = c.getParent();
        } while (c != null);
        return null;
    }

    private Lifecycle getLifecycle(final FacesContext context) {
        LifecycleFactory lifecycleFactory = (LifecycleFactory) FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
        String lifecycleId = context.getExternalContext().getInitParameter(FacesServlet.LIFECYCLE_ID_ATTR);
        if (lifecycleId == null) {
            lifecycleId = LifecycleFactory.DEFAULT_LIFECYCLE;
        }
        return lifecycleFactory.getLifecycle(lifecycleId);
    }

    /**
     * A FacesContext delegator that gives us the necessary controls over the FacesContext to allow the execution of the
     * lifecycle to accomodate the UIViewAction sequence.
     */
    private class InstrumentedFacesContext extends FacesContextWrapper {

        private final FacesContext wrapped;
        private boolean viewRootCleared = false;
        private boolean renderedResponseControlDisabled = false;
        private Boolean postback = null;

        public InstrumentedFacesContext(final FacesContext wrapped) {
            this.wrapped = wrapped;
        }

        @Override
        public FacesContext getWrapped() {
            return wrapped;
        }

        @Override
        public UIViewRoot getViewRoot() {
            if (viewRootCleared) {
                return null;
            }

            return wrapped.getViewRoot();
        }

        @Override
        public void setViewRoot(final UIViewRoot viewRoot) {
            viewRootCleared = false;
            wrapped.setViewRoot(viewRoot);
        }

        @Override
        public boolean isPostback() {
            return postback == null ? wrapped.isPostback() : postback;
        }

        @Override
        public void renderResponse() {
            if (!renderedResponseControlDisabled) {
                wrapped.renderResponse();
            }
        }

        /**
         * Make it look like we have dispatched a request using the include method.
         */
        public InstrumentedFacesContext pushViewIntoRequestMap() {
            getExternalContext().getRequestMap().put("javax.servlet.include.servlet_path", wrapped.getViewRoot().getViewId());
            return this;
        }

        public InstrumentedFacesContext clearPostback() {
            postback = false;
            return this;
        }

        public InstrumentedFacesContext clearViewRoot() {
            viewRootCleared = true;
            return this;
        }

        public InstrumentedFacesContext disableRenderResponseControl() {
            renderedResponseControlDisabled = true;
            return this;
        }

        public void set() {
            setCurrentInstance(this);
        }

        public void restore() {
            setCurrentInstance(wrapped);
        }
    }
}
