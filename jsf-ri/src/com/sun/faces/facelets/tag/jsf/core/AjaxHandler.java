/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2008 Sun Microsystems, Inc. All rights reserved.
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
 *
 * This file incorporates work covered by the following copyright and
 * permission notice:
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sun.faces.facelets.tag.jsf.core;

import java.io.IOException;
import java.util.*;

import javax.el.ELException;
import javax.faces.FacesException;
import javax.faces.component.ActionSource;
import javax.faces.component.AjaxBehavior;
import javax.faces.component.AjaxBehaviors;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.webapp.pdl.facelets.FaceletContext;
import javax.faces.webapp.pdl.facelets.FaceletException;

import javax.faces.webapp.pdl.facelets.tag.TagAttribute;
import javax.faces.webapp.pdl.facelets.tag.TagAttributeException;
import javax.faces.webapp.pdl.facelets.tag.TagConfig;
import com.sun.faces.facelets.tag.jsf.ComponentSupport;
import com.sun.faces.RIConstants;
import com.sun.faces.facelets.tag.TagHandlerImpl;

/**
 * <p class="changed_added_2_0">Enable one or more components in the view
 * to perform Ajax operations.  This tag handler must create an instance
 * of {@link javax.faces.component.AjaxBehavior} using the tag attribute 
 * values.  If this tag is nested within a single 
 * {@link javax.faces.component.ActionSource} component, and the
 * <code>events</code> attribute value is not specified or is
 * one of the following: 
 * <ul>
 * <li>{@link javax.faces.component.AjaxBehavior#AJAX_ACTION}</li>
 * <li>{@link javax.faces.component.AjaxBehavior#AJAX_VALUE_CHANGE_ACTION}</li>
 * </ul>
 * put the {@link javax.faces.component.AjaxBehavior} instance in the parent 
 * component's attribute <code>Map</code> under the key 
 * {@link javax.faces.component.AjaxBehavior#AJAX_BEHAVIOR}.  If this tag is nested within
 * a single {@link javax.faces.component.EditableValueHolder} component,
 * and the <code>events</code> attribute value is not specified or is
 * one of the following:
 * <ul>
 * <li>{@link javax.faces.component.AjaxBehavior#AJAX_VALUE_CHANGE}</li>
 * <li>{@link javax.faces.component.AjaxBehavior#AJAX_VALUE_CHANGE_ACTION}</li>
 * </ul>
 * put the {@link javax.faces.component.AjaxBehavior} instance in the parent 
 * component's attribute <code>Map</code> under the key
 * {@link javax.faces.component.AjaxBehavior#AJAX_BEHAVIOR}.
 * Throw an <code>exception</code> if the <code>events</code> attribute value
 * does not match the component type.
 * <br/><br/>
 * If this tag is nested within a component other than an 
 * {@link javax.faces.component.ActionSource} or 
 * {@link javax.faces.component.EditableValueHolder} type, 
 * make this tag's parent component subscribe to {@link javax.faces.event.AfterAddToParentEvent}
 * events.  Retrieve an {@link javax.faces.component.AjaxBehaviors} instance from 
 * the current {@link javax.faces.context.FacesContext} attributes <code>Map</code>
 * using the key {@link javax.faces.component.AjaxBehaviors#AJAX_BEHAVIORS}.  If an instance does not exist,
 * create it.  Call {@link javax.faces.component.AjaxBehaviors#pushBehavior} passing the
 * {@link javax.faces.component.AjaxBehavior} instance and the parent component instance.
 * Put the {@link javax.faces.component.AjaxBehaviors} instance into the 
 * {@link javax.faces.context.FacesContext} attributes <code>Map</code>.
 * <br/><br/>
 * Check for the existence of the Ajax resource by calling 
 * <code>UIViewRoot.getComponentResources()</code>.  If
 * the Ajax resource does not exist, create a <code>UIOutput</code> component
 * instance and set the renderer type to <code>javax.faces.resource.Script</code>.
 * Set the the following attributes in the component's attribute <code>Map</code>:
 * <code>library</code> with the value <code>javax.faces</code> and 
 * <code>name</code> with the value <code>jsf.js</code>.  Install the component
 * resource using <code>UIViewRoot.addComponentResource()</code> and specifying
 * <code>head</code> as the <code>target</code> argument.</p> 
 *
 * @version $Id: AjaxHandler.java 5369 2008-09-08 19:53:45Z rogerk $
 */
public final class AjaxHandler extends TagHandlerImpl {

    private final TagAttribute events;
    private final TagAttribute execute;
    private final TagAttribute render;
    private final TagAttribute onevent;
    private final TagAttribute onerror;
    private final TagAttribute disabled;

    /**
     * @param config
     */
    public AjaxHandler(TagConfig config) {
        super(config);
        this.events = this.getAttribute("events");
        this.execute = this.getAttribute("execute");
        this.render = this.getAttribute("render");
        this.onevent = this.getAttribute("onevent");
        this.onerror = this.getAttribute("onerror");
        this.disabled = this.getAttribute("disabled");
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sun.facelets.FaceletHandler#apply(com.sun.facelets.FaceletContext,
     *      javax.faces.component.UIComponent)
     */
    @SuppressWarnings("unchecked")
    public void apply(FaceletContext ctx, UIComponent parent)
          throws IOException, FacesException, FaceletException, ELException {
        if (null == parent || !(ComponentSupport.isNew(parent))) {
            return;
        }

        String events = null;
        Collection<String> execute = null;
        Collection<String> render = null;
        String onevent = null;
        String onerror = null;
        Boolean disabled = false;

        if (this.events != null) {
            events = this.events.getValue(ctx);
        }
        if (this.execute != null) {
            Object tempAttr = this.execute.getObject(ctx, Object.class);
            if (tempAttr instanceof String) {
                // split into separate strings, add these into a new Collection
                execute = new LinkedHashSet<String>(Arrays.asList(((String)tempAttr).split(" ")));
            } else if (tempAttr instanceof Collection) {
                execute = (Collection<String>)tempAttr;
            } else {
                // RELEASE_PENDING  i18n
                throw new TagAttributeException(this.execute,"'execute' attribute value must be either a String or a Collection");
            }
        }
        if (this.render != null) {
            Object tempAttr = this.render.getObject(ctx, Object.class);
            if (tempAttr instanceof String) {
                // split into separate strings, add these into a new Collection
                render = new LinkedHashSet<String>(Arrays.asList(((String)tempAttr).split(" ")));
            } else if (tempAttr instanceof Collection) {
                render = (Collection<String>)tempAttr;
            } else {
                // RELEASE_PENDING  i18n
                throw new TagAttributeException(this.render,"'render' attribute value must be either a String or a Collection");
            }
        }

        if (this.onevent != null) {
            onevent = this.onevent.getValue(ctx);
        }
        if (this.onerror != null) {
            onerror = this.onerror.getValue(ctx);
        }

        if (this.disabled != null) {
            disabled = this.disabled.getBoolean(ctx);
        }

        AjaxBehavior ajaxBehavior = new AjaxBehavior(events, onevent, onerror, execute, render, disabled);

        //
        // If we are nested within an EditableValueHolder or ActionSource component..
        //
        if (parent instanceof ActionSource) {
            if (null == events || events.equals(AjaxBehavior.AJAX_VALUE_CHANGE_ACTION) ||
                events.equals(AjaxBehavior.AJAX_ACTION)) {
                parent.getAttributes().put(AjaxBehavior.AJAX_BEHAVIOR, ajaxBehavior);
                installAjaxResourceIfNecessary();
                return;
            } else {
                // RELEASE_PENDING 118N
                throw new TagAttributeException(this.events, "'events' attribute value must be 'action' for 'ActionSource' components");
            }
        } else if (parent instanceof EditableValueHolder) {
            if (null == events || events.equals(AjaxBehavior.AJAX_VALUE_CHANGE_ACTION) ||
                events.equals(AjaxBehavior.AJAX_VALUE_CHANGE)) {
                parent.getAttributes().put(AjaxBehavior.AJAX_BEHAVIOR, ajaxBehavior);
                installAjaxResourceIfNecessary();
                return;
            } else {
                // RELEASE_PENDING 118N
                throw new TagAttributeException(this.events, "'events' attribute value must be 'valueChange' for 'EditableValueHolder' components");
            }
        }
            
        AjaxBehaviors ajaxBehaviors = (AjaxBehaviors)ctx.getFacesContext().getAttributes().
            get(AjaxBehaviors.AJAX_BEHAVIORS);
        if (ajaxBehaviors == null) {
            ajaxBehaviors = new AjaxBehaviors();
        }
        ajaxBehaviors.pushBehavior(ajaxBehavior); 
        ctx.getFacesContext().getAttributes().put(AjaxBehaviors.AJAX_BEHAVIORS, ajaxBehaviors);
        installAjaxResourceIfNecessary();

        nextHandler.apply(ctx, parent);

        // At this point, we have reached the closing </f:ajax> tag..

        FacesContext context = ctx.getFacesContext();
        ajaxBehaviors = (AjaxBehaviors)context.getAttributes().get(AjaxBehaviors.AJAX_BEHAVIORS);
        if (ajaxBehaviors != null) {
            ajaxBehaviors.popBehavior();
        }
    }

    // Only install the Ajax resource if it doesn't exist.
    // The resource component will be installed with the target "head".
    //
    private void installAjaxResourceIfNecessary() {

        FacesContext context = FacesContext.getCurrentInstance();
        if (context.getAttributes().get(RIConstants.SCRIPT_STATE) != null) {
            // Already included, return
            return;
        }

        final String name = "jsf.js";
        final String library = "javax.faces";
        UIViewRoot viewRoot = context.getViewRoot();
        ListIterator iter = (viewRoot.getComponentResources(context, "head")).listIterator();
        while (iter.hasNext()) {
            UIComponent resource = (UIComponent)iter.next();
            String rname = (String)resource.getAttributes().get("name");
            String rlibrary = (String)resource.getAttributes().get("library");
            if (name.equals(rname) && library.equals(rlibrary)) {
                // Set the context to record script as included
                context.getAttributes().put(RIConstants.SCRIPT_STATE, Boolean.TRUE);
                return;
            }
        }


        iter = (viewRoot.getComponentResources(context, "body")).listIterator();
        while (iter.hasNext()) {
            UIComponent resource = (UIComponent)iter.next();
            String rname = (String)resource.getAttributes().get("name");
            String rlibrary = (String)resource.getAttributes().get("library");
            if (name.equals(rname) && library.equals(rlibrary)) {
                // Set the context to record script as included
                context.getAttributes().put(RIConstants.SCRIPT_STATE, Boolean.TRUE);
                return;
            }
        }
        iter = (viewRoot.getComponentResources(context, "form")).listIterator();
        while (iter.hasNext()) {
            UIComponent resource = (UIComponent)iter.next();
            String rname = (String)resource.getAttributes().get("name");
            String rlibrary = (String)resource.getAttributes().get("library");
            if (name.equals(rname) && library.equals(rlibrary)) {
                // Set the context to record script as included
                context.getAttributes().put(RIConstants.SCRIPT_STATE, Boolean.TRUE);
                return;
            }
        }
        
        UIOutput output = new UIOutput();
        output.setRendererType("javax.faces.resource.Script");
        output.getAttributes().put("name", name);
        output.getAttributes().put("library", library);
        viewRoot.addComponentResource(context, output, "head");

        // Set the context to record script as included
        context.getAttributes().put(RIConstants.SCRIPT_STATE, Boolean.TRUE);

    }
}
