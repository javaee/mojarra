/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2011 Oracle and/or its affiliates. All rights reserved.
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

import java.util.Collection;
import java.util.Iterator;
import javax.faces.application.Application;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
import javax.faces.event.PostValidateEvent;
import javax.faces.event.PreValidateEvent;


/**
 * <p><strong class="changed_modified_2_1">UIForm</strong> is a {@link
 * UIComponent} that represents an input form to be presented to the
 * user, and whose child components represent (among other things) the
 * input fields to be included when the form is submitted.</p>

 * <p>By default, the <code>rendererType</code> property must be set to
 * "<code>javax.faces.Form</code>".  This value can be changed by calling the
 * <code>setRendererType()</code> method.</p>
 */

public class UIForm extends UIComponentBase implements NamingContainer, UniqueIdVendor {


    // ------------------------------------------------------ Manifest Constants


    /**
     * <p>The standard component type for this component.</p>
     */
    public static final String COMPONENT_TYPE = "javax.faces.Form";


    /**
     * <p>The standard component family for this component.</p>
     */
    public static final String COMPONENT_FAMILY = "javax.faces.Form";


    /**
     * Properties that are tracked by state saving.
     */
    enum PropertyKeys {

        /**
         * <p>The prependId flag.</p>
         */
        prependId,

        /**
         * <p>Last id vended by {@link UIForm#createUniqueId(javax.faces.context.FacesContext, String)}.</p>
         */
        lastId,
        
        submitted,
    }


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Create a new {@link UIForm} instance with default property
     * values.</p>
     */
    public UIForm() {

        super();
        setRendererType("javax.faces.Form");

    }


    // ------------------------------------------------------ Instance Variables

    //private int lastId = 0;

    // -------------------------------------------------------------- Properties


    public String getFamily() {

        return (COMPONENT_FAMILY);

    }


    /**
     * <p>The form submitted flag for this {@link UIForm}.</p>
     */
    //private boolean submitted = false;


    /**
     * <p><span class="changed_modified_2_1">Returns</span> the current value
     * of the <code>submitted</code> property.  The default value is
     * <code>false</code>.  See {@link #setSubmitted} for details.</p>

     * <p class="changed_modified_2_1">This property must be kept as a
     * transient property using the {@link
     * UIComponent#getTransientStateHelper}.</p>
     */
    public boolean isSubmitted() {

        //return (this.submitted);
        return (Boolean) getTransientStateHelper().getTransient(PropertyKeys.submitted, false);
    }


    /**
     * <p><span class="changed_modified_2_1">If</span>
     * <strong>this</strong> <code>UIForm</code> instance (as opposed to
     * other forms in the page) is experiencing a submit during this
     * request processing lifecycle, this method must be called, with
     * <code>true</code> as the argument, during the {@link
     * UIComponent#decode} for this <code>UIForm</code> instance.  If
     * <strong>this</strong> <code>UIForm</code> instance is
     * <strong>not</strong> experiencing a submit, this method must be
     * called, with <code>false</code> as the argument, during the
     * {@link UIComponent#decode} for this <code>UIForm</code>
     * instance.</p> <p/> <p>The value of a <code>UIForm</code>'s
     * submitted property must not be saved as part of its state.</p>

     * <p class="changed_modified_2_1">This property must be kept as a
     * transient property using the {@link
     * UIComponent#getTransientStateHelper}.</p>
     */
    public void setSubmitted(boolean submitted) {

        //this.submitted = submitted;
        getTransientStateHelper().putTransient(PropertyKeys.submitted, submitted);
    }

    /**
     * <p>The prependId flag.</p>
     */
    //private Boolean prependId;


    public boolean isPrependId() {

        return (Boolean) getStateHelper().eval(PropertyKeys.prependId, true);

    }


    public void setPrependId(boolean prependId) {

        getStateHelper().put(PropertyKeys.prependId, prependId);

    }

    // ----------------------------------------------------- UIComponent Methods


    /**
     * <p>Override {@link UIComponent#processDecodes} to ensure that the
     * form is decoded <strong>before</strong> its children.  This is
     * necessary to allow the <code>submitted</code> property to be
     * correctly set.</p>
     *
     * @throws NullPointerException {@inheritDoc}
     */
    public void processDecodes(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }

        // Process this component itself
        decode(context);

        // if we're not the submitted form, don't process children.
        if (!isSubmitted()) {
            return;
        }

        // Process all facets and children of this component
        Iterator kids = getFacetsAndChildren();
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            kid.processDecodes(context);
        }

    }


    /**
     * <p>Override {@link UIComponent#processValidators} to ensure that
     * the children of this <code>UIForm</code> instance are only
     * processed if {@link #isSubmitted} returns <code>true</code>.</p>
     *
     * @throws NullPointerException {@inheritDoc}
     */
    public void processValidators(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }
        if (!isSubmitted()) {
            return;
        }
        pushComponentToEL(context, this);
        Application app = context.getApplication();
        app.publishEvent(context, PreValidateEvent.class, this);
        // Process all the facets and children of this component
        Iterator kids = getFacetsAndChildren();
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            kid.processValidators(context);
        }
        app.publishEvent(context, PostValidateEvent.class, this);
        popComponentFromEL(context);

    }


    /**
     * <p>Override {@link UIComponent#processUpdates} to ensure that the
     * children of this <code>UIForm</code> instance are only processed
     * if {@link #isSubmitted} returns <code>true</code>.</p>
     *
     * @throws NullPointerException {@inheritDoc}
     */
    public void processUpdates(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }
        if (!isSubmitted()) {
            return;
        }

        // Process all facets and children of this component
        Iterator kids = getFacetsAndChildren();
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            kid.processUpdates(context);
        }

    }

    /**<p class="changed_modified_2_2">Generate an identifier for a component. The identifier
     * will be prefixed with UNIQUE_ID_PREFIX, and will be unique
     * within this component-container. Optionally, a unique seed value can
     * be supplied by component creators which should be
     * included in the generated unique id.</p>
     * <p class="changed_added_2_2">
     * If the <code>prependId</code> property has the value <code>false</code>,
     * this method must call <code>createUniqueId</code> on the next ancestor
     * <code>UniqueIdVendor</code>.
     * </p>
     *
     * @param context FacesContext
     * @param seed an optional seed value - e.g. based on the position of the component in the VDL-template
     * @return a unique-id in this component-container
     */
    public String createUniqueId(FacesContext context, String seed) {
        if (isPrependId()) {
            Integer i = (Integer) getStateHelper().get(PropertyKeys.lastId);
            int lastId = ((i != null) ? i : 0);
            getStateHelper().put(PropertyKeys.lastId,  ++lastId);
            return UIViewRoot.UNIQUE_ID_PREFIX + (seed == null ? lastId : seed);
        } else {
            UIComponent ancestorNamingContainer = (getParent() == null) ? null : getParent().getNamingContainer();
            String uid = null;
            if (null != ancestorNamingContainer &&
                    ancestorNamingContainer instanceof UniqueIdVendor) {
                uid = ((UniqueIdVendor) ancestorNamingContainer).createUniqueId(context, seed);
            } else {
                uid = context.getViewRoot().createUniqueId(context, seed);
            }
            return uid;
        }
    }
    
    /**
     * <p>Override the {@link UIComponent#getContainerClientId} to allow
     * users to disable this form from prepending its <code>clientId</code> to
     * its descendent's <code>clientIds</code> depending on the value of
     * this form's {@link #isPrependId} property.</p>
     */
    public String getContainerClientId(FacesContext context) {
        if (this.isPrependId()) {
            return super.getContainerClientId(context);
        } else {
            UIComponent parent = this.getParent();
            while (parent != null) {
                if (parent instanceof NamingContainer) {
                    return parent.getContainerClientId(context);
                }
                parent = parent.getParent();
            }
        }
        return null;
    }

   
    /**
     * @see UIComponent#visitTree
     */
    @Override
    public boolean visitTree(VisitContext context,
                             VisitCallback callback) {

        // NamingContainers can optimize partial tree visits by taking advantage
        // of the fact that it is possible to detect whether any ids to visit
        // exist underneath the NamingContainer.  If no such ids exist, there
        // is no need to visit the subtree under the NamingContainer.

        // UIForm is a bit different from other NamingContainers.  It only acts
        // as a NamingContainer when prependId is true.  Note that if it 
        // weren't for this, we could push this implementation up in to
        // UIComponent and share it across all NamingContainers.  Instead,
        // we currently duplicate this implementation in UIForm and 
        // UINamingContainer, so that we can check isPrependId() here.

        if (!this.isPrependId()) {
            return super.visitTree(context, callback);
        }

        Collection<String> idsToVisit = context.getSubtreeIdsToVisit(this);
        assert (idsToVisit != null);

        // If we have ids to visit, let the superclass implementation
        // handle the visit
        if (!idsToVisit.isEmpty()) {
            return super.visitTree(context, callback);
        }

        // If we have no child ids to visit, just visit ourselves, if
        // we are visitable.
        if (isVisitable(context)) {
            FacesContext facesContext = context.getFacesContext();
            pushComponentToEL(facesContext, null);

            try {
                VisitResult result = context.invokeVisitCallback(this, callback);
                return (result == VisitResult.COMPLETE);
            }
            finally {
                popComponentFromEL(facesContext);
            }
        }

        // Done visiting this subtree.  Return false to allow 
        // visit to continue.
        return false;
    }
}

