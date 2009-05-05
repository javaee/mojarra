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

import com.sun.faces.facelets.Facelet;
import com.sun.faces.facelets.FaceletFactory;
import com.sun.faces.facelets.el.VariableMapperWrapper;
import com.sun.faces.facelets.tag.jsf.ComponentTagHandlerDelegateImpl.CreateComponentDelegate;
import com.sun.faces.util.RequestStateManager;

import javax.el.*;
import javax.faces.FacesException;
import javax.faces.application.Resource;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ComponentSystemEventListener;
import javax.faces.event.PostAddToViewEvent;
import javax.faces.view.AttachedObjectHandler;
import javax.faces.view.facelets.*;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import javax.faces.FactoryFinder;
import javax.faces.view.ViewDeclarationLanguage;
import javax.faces.view.ViewDeclarationLanguageFactory;

/**
 * RELEASE_PENDING (rlubke,driscoll) document
 */
public class CompositeComponentTagHandler extends ComponentHandler implements CreateComponentDelegate {

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
            EXCLUDED_COPY_ATTRIBUTES = propNames;
        } catch (Exception e) {
            throw new FacesException(e);
        }
    }
    
    CompositeComponentTagHandler(Resource ccResource,
            ComponentConfig config) {
        super(config);
        this.ccResource = ccResource;
        ((ComponentTagHandlerDelegateImpl)this.getTagHandlerDelegate()).setCreateComponentDelegate(this);
    }
    
    private void copyTagAttributesIntoComponentAttributes(FaceletContext ctx,
                                                          UIComponent cc) {
        
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
                    Map<String, Object> map = cc
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
    
    private Resource ccResource;
    
    

    public UIComponent createComponent(FaceletContext ctx) {
        FacesContext context = ctx.getFacesContext();
        UIComponent result = context.getApplication().createComponent(context, ccResource);
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
            String viewId = context.getViewRoot().getViewId();
            // PENDING(rlubke): performance
            ViewDeclarationLanguageFactory factory = (ViewDeclarationLanguageFactory)
                    FactoryFinder.getFactory(FactoryFinder.VIEW_DECLARATION_LANGUAGE_FACTORY);

            ViewDeclarationLanguage vdl = factory.getViewDeclarationLanguage(viewId);
            vdl.retargetAttachedObjects(context, c,
                    getAttachedObjectHandlers(c, false));
            viewHandler.retargetMethodExpressions(context, c);

            // RELEASE_PENDING This is *ugly*.  See my comments in
            // ComponentTagHandlerDelegateImpl at the end of the apply()
            // method
            if (Boolean.TRUE.equals(ctx.getFacesContext().getAttributes().get("partialStateSaving"))) {
                markInitialState(c);
            }

        }

    }

    private boolean isNameValid(String name) {

        return (name != null
                && name.length() > 0
                && (Arrays.binarySearch(EXCLUDED_COPY_ATTRIBUTES, name) < 0));
        
    }
    
    private void applyCompositeComponent(FaceletContext ctx, UIComponent c)
    throws IOException {

        FacesContext facesContext = ctx.getFacesContext();
        FaceletFactory factory = (FaceletFactory)
              RequestStateManager.get(facesContext, RequestStateManager.FACELET_FACTORY);
        VariableMapper orig = ctx.getVariableMapper();
        
        UIPanel facetComponent;
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
            Facelet f = factory.getFacelet(ccResource.getURL());
            copyTagAttributesIntoComponentAttributes(ctx, c);
            VariableMapper wrapper = new VariableMapperWrapper(orig) {

                @Override
                public ValueExpression resolveVariable(String variable) {
                    return super.resolveVariable(variable);
                }
                
            };
            ctx.setVariableMapper(wrapper);
            f.apply(facesContext, facetComponent);
        } finally {
            ctx.setVariableMapper(orig);
        }

    }

    public static List<AttachedObjectHandler> getAttachedObjectHandlers(UIComponent component) {
        return getAttachedObjectHandlers(component, true);
    }
    
    @SuppressWarnings({"unchecked"})
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


    private void markInitialState(UIComponent c) {
        c.markInitialState();
        for (Iterator<UIComponent> i = c.getFacetsAndChildren(); i.hasNext(); ) {
            markInitialState(i.next());
        }
    }


    // ---------------------------------------------------------- Nested Classes


     public static class CompositeAttributesCopyListener implements
           ComponentSystemEventListener, Serializable {



        // --------------------------- Methods from ComponentSystemEventListener


         public void processEvent(ComponentSystemEvent event)
               throws AbortProcessingException {

             UIComponent cc = event.getComponent();
             UIComponent compositeParent =
                   UIComponent.getCompositeComponentParent(cc);
             if (compositeParent != null) {
                 for (Map.Entry<String, Object> entry : cc
                       .getAttributes().entrySet()) {
                     if (entry.getValue() instanceof Expression) {
                         Expression expr = (Expression) entry.getValue();
                         if (!expr.isLiteralText()) {
                             String exprString = expr
                                   .getExpressionString();
                             if (exprString.startsWith(
                                   "#{cc.attrs.")) {
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
                                         cc.getAttributes()
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
