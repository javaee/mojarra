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
package com.sun.faces.facelets.tag.jsf;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.beans.PropertyDescriptor;
import java.beans.Introspector;

import javax.el.ELException;
import javax.el.Expression;
import javax.el.ValueExpression;
import javax.el.VariableMapper;
import javax.faces.FacesException;
import javax.faces.event.ComponentSystemEventListener;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PostAddToViewEvent;
import javax.faces.application.Resource;
import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.component.UIInput;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.webapp.pdl.AttachedObjectHandler;

import com.sun.faces.facelets.Facelet;
import javax.faces.webapp.pdl.facelets.FaceletContext;
import com.sun.faces.facelets.FaceletFactory;
import com.sun.faces.facelets.el.VariableMapperWrapper;
import com.sun.faces.facelets.tag.jsf.ComponentTagHandlerDelegateImpl.CreateComponentDelegate;
import javax.faces.webapp.pdl.facelets.tag.TagAttribute;
import javax.faces.webapp.pdl.facelets.tag.TagAttributes;
import com.sun.faces.util.RequestStateManager;
import com.sun.faces.util.FacesLogger;

import javax.el.MethodExpression;
import javax.faces.application.ViewHandler;
import javax.faces.webapp.pdl.facelets.tag.ComponentConfig;
import javax.faces.webapp.pdl.facelets.tag.ComponentHandler;

/**
 * RELEASE_PENDING (rlubke,driscoll) document
 */
public class CompositeComponentTagHandler extends ComponentHandler implements CreateComponentDelegate {

    private static final Logger LOGGER = FacesLogger.FACELETS_COMPONENT.getLogger();

     private static String[] EXCLUDED_COPY_ATTRIBUTES;

    // setup the list of properties that will not be exposed as composite
    // component attributes
    static {
        try {
            PropertyDescriptor[] properties =
                  Introspector.getBeanInfo(UINamingContainer.class).getPropertyDescriptors();
            Set<String> props = new HashSet<String>();
            for (PropertyDescriptor pd : properties) {
                if (pd.getWriteMethod() != null) {
                    props.add(pd.getName());
                }
            }
            // add attributes that aren't exposed as standard properties
            props.add("binding");
            String[] propNames = props.toArray(new String[props.size()]);
            Arrays.sort(propNames);
            System.out.println("PROPERTIES" + Arrays.toString(propNames));
            EXCLUDED_COPY_ATTRIBUTES = propNames;
        } catch (Exception e) {
            throw new FacesException(e);
        }
    }
    
    CompositeComponentTagHandler(Resource compositeComponentResource,
            ComponentConfig config) {
        super(config);
        this.compositeComponentResource = compositeComponentResource;
        ((ComponentTagHandlerDelegateImpl)this.getTagHandlerHelper()).setCreateComponentDelegate(this);
    }
    
    private void copyTagAttributesIntoComponentAttributes(FaceletContext ctx,
                                                          UIComponent compositeComponent) {
        
        TagAttributes tagAttributes = this.tag.getAttributes();
        TagAttribute attrs[] = tagAttributes.getAll();

        for (TagAttribute attr : attrs) {
            String name = attr.getLocalName();
            if (isNameValid(name)) {
                String value = attr.getValue();
                if (null != value && 0 < value.length()) {

                    Expression expression = attr.getValueExpression(ctx, Object.class);
                    // PENDING: I don't think copyTagAttributesIntoComponentAttributes
                    // should be getting called 
                    // on postback, yet it is.  In lieu of a real fix, I'll
                    // make sure I'm not overwriting a MethodExpression with a 
                    // ValueExpression.
                    Map<String, Object> map = compositeComponent
                          .getAttributes();
                    boolean doPut = true;
                    if (map.containsKey(name)) {
                        Object curVal = map.get(name);
                        if (curVal instanceof MethodExpression) {
                            doPut = false;
                        }
                    }
                    if (doPut) {
                        map.put(name, expression);
                    }
                }
            }
        }
        
    }
    
    private Resource compositeComponentResource;
    
    

    public UIComponent createComponent(FaceletContext ctx) {
        UIComponent result = null;
        FacesContext context = ctx.getFacesContext();
        result = context.getApplication().createComponent(context, compositeComponentResource);
        result.subscribeToEvent(PostAddToViewEvent.class,
                                new CompositeAttributesCopyListener());



        return result;
    }
    
    @Override
    public void applyNextHandler(FaceletContext ctx, UIComponent c) throws IOException, FacesException, ELException {
        // Allow any nested elements that reside inside the markup element
        // for this tag to get applied
        super.applyNextHandler(ctx, c);
        // Apply the facelet for this composite component
        applyCompositeComponent(ctx, c);
        // Allow any PDL declared attached objects to be retargeted
        if (ComponentHandler.isNew(c)) {
            FacesContext context = ctx.getFacesContext();
            ViewHandler viewHandler = context.getApplication().getViewHandler();
            viewHandler.retargetAttachedObjects(context, c,
                    getAttachedObjectHandlers(c, false));
            viewHandler.retargetMethodExpressions(context, c);
        }

    }

    private boolean isNameValid(String name) {

        return (name != null
                && name.length() > 0
                && (Arrays.binarySearch(EXCLUDED_COPY_ATTRIBUTES, name) < 0));
        
    }
    
    private void applyCompositeComponent(FaceletContext ctx, UIComponent c) {
        Facelet f = null;
        FacesContext facesContext = ctx.getFacesContext();
        FaceletFactory factory = (FaceletFactory)
              RequestStateManager.get(facesContext, RequestStateManager.FACELET_FACTORY);
        VariableMapper orig = ctx.getVariableMapper();
        
        UIPanel facetComponent = null;
        if (ComponentHandler.isNew(c)) {
            facetComponent = (UIPanel)
             facesContext.getApplication().createComponent("javax.faces.Panel");
            facetComponent.setRendererType("javax.faces.Group");
            c.getFacets().put(UIComponent.COMPOSITE_FACET_NAME, facetComponent);
        }
        else {
            facetComponent = (UIPanel) 
                    c.getFacets().get(UIComponent.COMPOSITE_FACET_NAME);
        }
        assert(null != facetComponent);
        
        try {
            f = factory.getFacelet(compositeComponentResource.getURL());
            copyTagAttributesIntoComponentAttributes(ctx, c);
            VariableMapper wrapper = new VariableMapperWrapper(orig) {

                @Override
                public ValueExpression resolveVariable(String variable) {
                    return super.resolveVariable(variable);
                }
                
            };
            ctx.setVariableMapper(wrapper);
            f.apply(facesContext, facetComponent);
        } catch (Exception e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, e.toString(), e);
            }
        } finally {
            ctx.setVariableMapper(orig);
        }

    }

    public static List<AttachedObjectHandler> getAttachedObjectHandlers(UIComponent component) {
        return getAttachedObjectHandlers(component, true);
    }
    
    public static List<AttachedObjectHandler> getAttachedObjectHandlers(UIComponent component,
            boolean create) {
        Map<String, Object> attrs = component.getAttributes();
        List<AttachedObjectHandler> result = (List<AttachedObjectHandler>)
                attrs.get("javax.faces.RetargetableHandlers");
        
        if (null == result) {
            if (create) {
                result = new ArrayList<AttachedObjectHandler>();
                attrs.put("javax.faces.RetargetableHandlers", result);
            }
            else {
                result = Collections.EMPTY_LIST;
            }
        }
        return result;
    }


    // ---------------------------------------------------------- Nested Classes


     public static class CompositeAttributesCopyListener implements
           ComponentSystemEventListener, Serializable {



        // --------------------------- Methods from ComponentSystemEventListener


         public void processEvent(ComponentSystemEvent event)
               throws AbortProcessingException {

             UIComponent compositeComponent = event.getComponent();
             UIComponent compositeParent =
                   UIComponent.getCompositeComponentParent(compositeComponent);
             if (compositeParent != null) {
                 for (Map.Entry<String, Object> entry : compositeComponent
                       .getAttributes().entrySet()) {
                     if (entry.getValue() instanceof Expression) {
                         Expression expr = (Expression) entry.getValue();
                         if (!expr.isLiteralText()) {
                             String exprString = expr
                                   .getExpressionString();
                             if (exprString.startsWith(
                                   "#{compositeComponent.attrs.")) {
                                 int lastDot = exprString
                                       .lastIndexOf('.');
                                 if (lastDot != -1) {
                                     String attrName = exprString
                                           .substring((lastDot + 1),
                                                      (exprString.length()
                                                       - 1));

                                     // see if our parent composite component
                                     // has an Expression in its attribute
                                     // map for this attribute name, if so,
                                     // use that as the expression for this
                                     // composite component
                                     Object parentExpr = compositeParent
                                           .getAttributes()
                                           .get(attrName);
                                     if (parentExpr instanceof Expression) {
                                         compositeComponent.getAttributes()
                                               .put(entry.getKey(), parentExpr);
                                     }

                                 }
                             }
                         }
                     }
                 }
             }


         }

    }
    
    
    
}
