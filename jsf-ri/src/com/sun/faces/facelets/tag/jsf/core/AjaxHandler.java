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
import java.util.*;

import javax.el.ELException;
import javax.el.ELContext;
import javax.el.MethodExpression;
import javax.el.MethodNotFoundException;
import javax.faces.FacesException;
import javax.faces.component.ActionSource;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.component.behavior.AjaxBehavior;
import javax.faces.component.behavior.AjaxBehaviors;
import javax.faces.component.behavior.BehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.AjaxBehaviorListener;
import javax.faces.webapp.pdl.facelets.FaceletContext;
import javax.faces.webapp.pdl.facelets.FaceletException;

import javax.faces.webapp.pdl.facelets.tag.TagAttribute;
import javax.faces.webapp.pdl.facelets.tag.TagAttributeException;
import javax.faces.webapp.pdl.facelets.tag.TagConfig;
import javax.faces.webapp.pdl.facelets.tag.TagException;
import javax.faces.webapp.pdl.facelets.tag.TagHandler;
import com.sun.faces.facelets.tag.jsf.ComponentHandler;
import com.sun.faces.facelets.tag.jsf.ComponentSupport;
import com.sun.faces.RIConstants;
import com.sun.faces.facelets.tag.TagHandlerImpl;

/**
 * <p class="changed_added_2_0">Enable one or more components in the view
 * to perform Ajax operations.  This tag handler must create an instance
 * of {@link javax.faces.component.AjaxBehavior} using the tag attribute 
 * values.  If this tag is nested within a single 
 * {@link BehaviorHolder} component:
 * <ul>
 * <li>If the <code>events</code> attribute value is not specified, 
 * obtain the default event name by calling {@link BehaviorHolder#getDefaultEventName}.
 * If that returns <code>null</code> throw an <code>exception</code>.</li>
 * <li>If the <code>events</code> attribute value is specified, ensure it
 * that it exists in the <code>Collection</code> returned from a call to
 * {@link BehaviorHolder#getEventNames} and throw an <code>exception</code> if
 * it doesn't exist.</li>
 * <li>Add the {@link AjaxBehavior} instance to the {@link BehaviorHolder}
 * component by calling {@link BehaviorHolder#addBehavior} passing <code>event</code>
 * and the {@link AjaxBehavior} instance.</li> 
 * </ul>
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
 * If this tag has component children, add the {@link AjaxBehavior} to 
 * {@link AjaxBehaviors} by calling {@link AjaxBehaviors#pushBehavior}. As 
 * subsequent child components that implement the {@link BehaviorHolder} interface 
 * are evaluated this {@link AjaxBehavior} instance must be added as a behavior to
 * the component.
 * </p>
 * @version $Id: AjaxHandler.java 5369 2008-09-08 19:53:45Z rogerk $
 */
public final class AjaxHandler extends TagHandlerImpl {

    private final TagAttribute event;
    private final TagAttribute execute;
    private final TagAttribute render;
    private final TagAttribute onevent;
    private final TagAttribute onerror;
    private final TagAttribute disabled;
    private final TagAttribute listener;

    /**
     * @param config
     */
    public AjaxHandler(TagConfig config) {
        super(config);
        this.event = this.getAttribute("event");
        this.execute = this.getAttribute("execute");
        this.render = this.getAttribute("render");
        this.onevent = this.getAttribute("onevent");
        this.onerror = this.getAttribute("onerror");
        this.disabled = this.getAttribute("disabled");
        this.listener = this.getAttribute("listener");
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

        // Construct our AjaxBehavior from tag parameters..

        String event = (this.event != null) ? this.event.getValue() : null;

        AjaxBehavior ajaxBehavior = new AjaxBehavior(event,
            ((this.onevent != null) ? this.onevent.getValueExpression(ctx, String.class) : null),
            ((this.onerror != null) ? this.onerror.getValueExpression(ctx, String.class) : null),
            ((this.execute != null) ? this.execute.getValueExpression(ctx, Object.class) : null),
            ((this.render != null) ? this.render.getValueExpression(ctx, Object.class) : null),
            ((this.disabled != null) ? this.disabled.getValueExpression(ctx, Boolean.class) : null));

        if (null != listener) {
            ajaxBehavior.addAjaxBehaviorListener(new AjaxBehaviorListenerImpl(
                this.listener.getMethodExpression(ctx, Object.class, new Class[] { AjaxBehaviorEvent.class }),
                this.listener.getMethodExpression(ctx, Object.class, new Class[] { })));
        }

        // See if we have any components nested below us..

        Iterator iter = TagHandlerImpl.findNextByType(this.nextHandler, TagHandler.class);
        
        // If we don't have any components nested below us..
        // then we handle the case of existing as nested within a BehaviorHolder component..

        if (!iter.hasNext()) {
            //
            // If we are nested within a BehaviorHolder
            //
            if (parent instanceof BehaviorHolder) {
                BehaviorHolder bHolder = (BehaviorHolder)parent;
                if (null == event) {
                    event = bHolder.getDefaultEventName();
                    if (null == event) {
                        throw new TagException(this.tag,
                            "Event attribute could not be determined: " + event);
                    }
                } else {
                    if (!bHolder.getEventNames().contains(event)) {
                        throw new TagException(this.tag,
                            "Event attribute could not be determined: " + event);
                    }               
                }
                bHolder.addBehavior(event, ajaxBehavior);
                installAjaxResourceIfNecessary();
            }
            return;
        }
            
        // We have component children nested below us..

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

class AjaxBehaviorListenerImpl implements AjaxBehaviorListener, Serializable {
    private static final long serialVersionUID = -6056525197409773897L;

    private MethodExpression oneArgListener;
    private MethodExpression noArgListener;

    // Necessary for state saving
    public AjaxBehaviorListenerImpl() {}

    public AjaxBehaviorListenerImpl(MethodExpression oneArg, MethodExpression noArg) {
        this.oneArgListener = oneArg;
        this.noArgListener = noArg;
    }

    public void processAjaxBehavior(AjaxBehaviorEvent event) throws AbortProcessingException {
        final ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        try{
            noArgListener.invoke(elContext, new Object[]{});
        } catch (MethodNotFoundException mnfe) {
            // Attempt to call public void method(AjaxBehaviorEvent event)
            oneArgListener.invoke(elContext, new Object[]{event});
        }
    }
}
