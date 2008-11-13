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
import java.io.Serializable;
import java.util.Iterator;
import java.util.ListIterator;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.ActionSource;
import javax.faces.component.AjaxBehavior;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.AfterAddToParentEvent;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ComponentSystemEventListener;
import javax.faces.webapp.pdl.facelets.FaceletContext;
import javax.faces.webapp.pdl.facelets.FaceletException;

import com.sun.faces.facelets.tag.TagAttribute;
import com.sun.faces.facelets.tag.TagAttributeException;
import com.sun.faces.facelets.tag.TagConfig;
import com.sun.faces.facelets.tag.TagException;
import com.sun.faces.facelets.tag.TagHandler;
import com.sun.faces.facelets.tag.jsf.CompositeComponentTagHandler;
import com.sun.faces.facelets.tag.jsf.ComponentSupport;
import com.sun.faces.facelets.util.ReflectionUtil;

import javax.faces.application.Resource;

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
 * <li>{@link javax.faces.component.AjaxBehaviorx#AJAX_ACTION_VALUE_CHANGE}</li>
 * </ul>
 * put the {@link javax.faces.component.AjaxBehavior} instance in the parent 
 * component's attribute <code>Map</code>.  If this tag is nested within
 * a single {@link javax.faces.component.EditableValueHolder} component,
 * and the <code>events</code> attribute value is not specified or is 
 * one of the following:
 * <ul>
 * <li>{@link javax.faces.component.AjaxBehavior#AJAX_VALUE_CHANGE}</li>
 * <li>{@link javax.faces.component.AjaxBehaviorx#AJAX_ACTION_VALUE_CHANGE}</li>
 * </ul>
 * put the {@link javax.faces.component.AjaxBehavior} instance in the parent 
 * component's attribute <code>Map</code>.  
 * <br/><br/>
 * If this tag is nested within a component other than an 
 * {@link javax.faces.component.ActionSource} or 
 * {@link javax.faces.component.EditableValueHolder} type, put the 
 * {@link javax.faces.component.AjaxBehavior} instance on the attribute
 * <code>Map</code> of all {@link javax.faces.component.ActionSource} and/or
 * {@link javax.faces.component.EditableValueHolder} components following
 * the <code>events</code> matching rules above.  Throw an <code>exception</code>
 * if the <code>events</code> attribute value does not match the component type.
 * Once a  {@link javax.faces.component.AjaxBehavior} instance has been added to the
 * component's attribute <code>Map</code>, check for the existence of the Ajax 
 * resource by calling <code>UIViewRoot.getComponentResources()</code>.  If
 * the Ajax resource does not exist, create a <code>UIOutput</code> component
 * instance and set the renderer type to <code>javax.faces.resource.Script</code>.
 * Set the the following attributes in the component's attribute <code>Map</code>:
 * <code>library</code> with the value <code>javax.faces</code> and 
 * <code>name</code> with the value <code>ajax.js</code>.  Install the component
 * resource using <code>UIViewRoot.addComponentResource()</code> and specifying
 * <code>head</code> as the <code>target</code> argument.</p> 
 *
 * @version $Id: AjaxHandler.java 5369 2008-09-08 19:53:45Z rogerk $
 */
public final class AjaxHandler extends TagHandler {

    private final TagAttribute events;
    private final TagAttribute execute;
    private final TagAttribute render;

    private AddToParentEventListener addToParentEventListener = null;

    //
    // Listens for when components are added to the parent component of this tag.
    // If the component that is added is an ActionSource or EditableValueHolder,
    // it is "ajaxified" by adding an instance of Ajaxrequest to the component's
    // attribute Map.
    // 
    private final static class AddToParentEventListener 
          implements ComponentSystemEventListener, Serializable {

        private Object previousAjaxBehavior = null;
        private AjaxBehavior ajaxBehavior = null;
        private AjaxHandler ajaxHandler = null;

        public AddToParentEventListener(AjaxBehavior ajaxBehavior, AjaxHandler ajaxHandler) {
            this.ajaxBehavior = ajaxBehavior;
            this.ajaxHandler = ajaxHandler;
        }

        public void processEvent(ComponentSystemEvent event) 
            throws AbortProcessingException {
            UIComponent source = event.getComponent();
            ajaxifyComponents(source);     
        }

        //
        // Helper method to ajaxify applicable components under 'component' argument.
        // It does this by adding an instance of AjaxBehavior to the component's
        // attribute Map.
        //
        private void ajaxifyComponents(UIComponent component) {
            String events = ajaxBehavior.getEvents();
            if (null == component.getAttributes().get(AjaxBehavior.AJAX_BEHAVIOR)) {
                if (component instanceof ActionSource) {
                    if (null == events || events.equals(AjaxBehavior.AJAX_VALUE_CHANGE_ACTION) ||
                        events.equals(AjaxBehavior.AJAX_ACTION)) {
                        component.getAttributes().put(AjaxBehavior.AJAX_BEHAVIOR, ajaxBehavior);
                        ajaxHandler.installAjaxResourceIfNecessary();
                    } else {
                        throw new AbortProcessingException("'Events' attribute value must be 'action'" +
                            "for 'ActionSource' components");
                    }
                } else if (component instanceof EditableValueHolder) {
                    if (null == events || events.equals(AjaxBehavior.AJAX_VALUE_CHANGE_ACTION) ||
                        events.equals(AjaxBehavior.AJAX_VALUE_CHANGE)) {
                        component.getAttributes().put(AjaxBehavior.AJAX_BEHAVIOR, ajaxBehavior);
                        ajaxHandler.installAjaxResourceIfNecessary();
                    } else {
                        throw new AbortProcessingException("'Events' attribute value must be 'valueChange' " +
                            "for 'EditableValueHolder' components");
                    }
                }
            }

            Iterator kids = component.getChildren().iterator();
            UIComponent kid;
            while (kids.hasNext()) {
                kid = (UIComponent)kids.next();
                ajaxifyComponents(kid);
            }
        }
    }

    /**
     * @param config
     */
    public AjaxHandler(TagConfig config) {
        super(config);
        this.events = this.getAttribute("events");
        this.execute = this.getAttribute("execute");
        this.render = this.getAttribute("render");
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sun.facelets.FaceletHandler#apply(com.sun.facelets.FaceletContext,
     *      javax.faces.component.UIComponent)
     */
    public void apply(FaceletContext ctx, UIComponent parent)
          throws IOException, FacesException, FaceletException, ELException {
        if (null == parent || !(ComponentSupport.isNew(parent))) {
            return;
        }

        String events = null;
        String execute = null;
        String render = null;

        if (null != this.events) {
            events = this.events.getValue(ctx);
        }
        if (null != this.execute) {
            execute = this.execute.getValue(ctx).replace(' ',',');
        }
        if (null != this.render) {
            render = this.render.getValue(ctx).replace(' ',',');
        }
        
        AjaxBehavior ajaxBehavior = new AjaxBehavior(events, execute, render);

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
                throw new TagAttributeException(this.events, "'Events' attribute value must be 'action' for 'ActionSource' components"); 
            }
        } else if (parent instanceof EditableValueHolder) {
            if (null == events || events.equals(AjaxBehavior.AJAX_VALUE_CHANGE_ACTION) || 
                events.equals(AjaxBehavior.AJAX_VALUE_CHANGE)) { 
                parent.getAttributes().put(AjaxBehavior.AJAX_BEHAVIOR, ajaxBehavior);
                installAjaxResourceIfNecessary();
                return;
            } else {
                throw new TagAttributeException(this.events, "'Events' attribute value must be 'valueChange' for 'EditableValueHolder' components"); 
            }
        }
            
        //
        // We are nested within some other component.  Attach a listener that will listen for when components
        // are added to the parent component.  The listener will add the 'AjaxBehavior' instance to the added
        // component if it is an EditableValueHolder or ActionSource component.
        //
        addToParentEventListener = new AddToParentEventListener(ajaxBehavior, this);

        parent.subscribeToEvent(AfterAddToParentEvent.class, addToParentEventListener);
    }

    // Only install the Ajax resource if it doesn't exist.
    // The resource component will be installed with the target "head".
    //
    public void installAjaxResourceIfNecessary() {
        FacesContext context = FacesContext.getCurrentInstance();
        UIViewRoot viewRoot = context.getViewRoot();
        ListIterator iter = (viewRoot.getComponentResources(context, "head")).listIterator();
        while (iter.hasNext()) {
            UIComponent resource = (UIComponent)iter.next();
            String name = (String)resource.getAttributes().get("name");
            String library = (String)resource.getAttributes().get("library");

            if (name != null && library != null &&
                    name.equals("ajax.js") && library.equals("javax.faces")) {
                return;
            }
        }


        iter = (viewRoot.getComponentResources(context, "body")).listIterator();
        while (iter.hasNext()) {
            UIComponent resource = (UIComponent)iter.next();
            String name = (String)resource.getAttributes().get("name");
            String library = (String)resource.getAttributes().get("library");

            // RELEASE_PENDING driscoll - is this really the best way to determine if
            // the ajax library is loaded already?
            if (name != null && library != null &&
                    name.equals("ajax.js") && library.equals("javax.faces")) {
                return;
            }
        }
        iter = (viewRoot.getComponentResources(context, "form")).listIterator();
        while (iter.hasNext()) {
            UIComponent resource = (UIComponent)iter.next();
            String name = (String)resource.getAttributes().get("name");
            String library = (String)resource.getAttributes().get("library");
            if (name != null && library != null &&
                    name.equals("ajax.js") && library.equals("javax.faces")) {
                return;
            }
        }
        
        UIOutput output = new UIOutput();
        output.setRendererType("javax.faces.resource.Script");
        output.getAttributes().put("name", "ajax.js");
        output.getAttributes().put("library", "javax.faces");
        viewRoot.addComponentResource(context, output, "head");
    }
}
