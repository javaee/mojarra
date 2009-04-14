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

package com.sun.faces.facelets.tag.composite;

import com.sun.faces.application.view.FaceletViewHandlingStrategy;
import com.sun.faces.facelets.tag.TagHandlerImpl;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.FaceletException;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagConfig;
import java.beans.BeanDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.view.AttachedObjectTarget;
import javax.faces.application.Resource;
import javax.faces.application.ProjectStage;
import javax.faces.context.FacesContext;
import javax.faces.view.facelets.ComponentHandler;

public class InterfaceHandler extends TagHandlerImpl {

    public final static String Name = "interface";

    
    public InterfaceHandler(TagConfig config) {
        super(config);
    }
    
    public void apply(FaceletContext ctx, UIComponent parent) throws IOException, FacesException, FaceletException, ELException {
        FacesContext context = ctx.getFacesContext();
        // only process if it's been created
        // Do not process if we're simply building metadata
        if (context.getAttributes().containsKey(FaceletViewHandlingStrategy.IS_BUILDING_METADATA)) {
            imbueComponentWithMetadata(ctx, parent);
            this.nextHandler.apply(ctx, parent);
        }
    }
    
    private void imbueComponentWithMetadata(FaceletContext ctx, UIComponent parent) {
        // only process if it's been created
        if (null == parent || 
            (null == (parent = parent.getParent())) ||
            !(ComponentHandler.isNew(parent))) {
            return;
        }
        
        // the real implementation will check if there is a cached beaninfo somewhere first
	Map<String, Object> attrs = parent.getAttributes();

        CompositeComponentBeanInfo componentBeanInfo = null;
        
        if (null == (componentBeanInfo = (CompositeComponentBeanInfo)
                     attrs.get(UIComponent.BEANINFO_KEY))) {
            componentBeanInfo = new CompositeComponentBeanInfo();
            attrs.put(UIComponent.BEANINFO_KEY, componentBeanInfo);
            BeanDescriptor componentDescriptor = new BeanDescriptor(parent.getClass());
            // PENDING(edburns): Make sure attributeNames() returns the right content
            // per the javadocs for ViewDeclarationLanguage.getComponentMetadata()
            componentBeanInfo.setBeanDescriptor(componentDescriptor);
            TagAttribute attr = null;
            ValueExpression ve = null;
            String strValue = null;
            boolean booleanValue = false;

            if (ctx.getFacesContext().getApplication().getProjectStage() == ProjectStage.Development) {

                if (null != (attr = this.getAttribute("displayName"))) {
                    ve = attr.getValueExpression(ctx, String.class);
                    strValue = (String) ve.getValue(ctx);
                    if (null != strValue) {
                        componentDescriptor.setDisplayName(strValue);
                    }
                }
                if (null != (attr = this.getAttribute("expert"))) {
                    ve = attr.getValueExpression(ctx, Boolean.class);
                    booleanValue = ((Boolean) ve.getValue(ctx)).booleanValue();
                    componentDescriptor.setExpert(booleanValue);
                }
                if (null != (attr = this.getAttribute("hidden"))) {
                    ve = attr.getValueExpression(ctx, Boolean.class);
                    booleanValue = ((Boolean) ve.getValue(ctx)).booleanValue();
                    componentDescriptor.setHidden(booleanValue);
                }
                if (null != (attr = this.getAttribute("name"))) {
                    ve = attr.getValueExpression(ctx, String.class);
                    strValue = (String) ve.getValue(ctx);
                    if (null != strValue) {
                        componentDescriptor.setName(strValue);
                    }
                }
                if (null != (attr = this.getAttribute("preferred"))) {
                    ve = attr.getValueExpression(ctx, Boolean.class);
                    booleanValue = ((Boolean) ve.getValue(ctx)).booleanValue();
                    componentDescriptor.setPreferred(booleanValue);
                }
                if (null != (attr = this.getAttribute("shortDescription"))) {
                    ve = attr.getValueExpression(ctx, String.class);
                    strValue = (String) ve.getValue(ctx);
                    if (null != strValue) {
                        componentDescriptor.setShortDescription(strValue);
                    }
                }
            }
            if (null != (attr = this.getAttribute("componentType"))) {
                ve = attr.getValueExpression(ctx, String.class);
                componentDescriptor.setValue(UIComponent.COMPOSITE_COMPONENT_TYPE_KEY, ve);
            }

            List<AttachedObjectTarget> targetList = (List<AttachedObjectTarget>)
              componentDescriptor.getValue(AttachedObjectTarget.ATTACHED_OBJECT_TARGETS_KEY);
            if (null == targetList) {
                targetList = new ArrayList<AttachedObjectTarget>();
                componentDescriptor.setValue(AttachedObjectTarget.ATTACHED_OBJECT_TARGETS_KEY,
                        targetList);
            }

            Resource componentResource =
                    (Resource) attrs.get(Resource.COMPONENT_RESOURCE_KEY);
            if (null == componentResource) {
                throw new NullPointerException("Unable to find Resource for composite component");
            }
        }
    }

}
