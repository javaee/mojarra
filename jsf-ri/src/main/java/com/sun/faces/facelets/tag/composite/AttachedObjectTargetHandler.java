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
 *
 *
 * This file incorporates work covered by the following copyright and
 * permission notice:
 *
 * Copyright 2005-2007 The Apache Software Foundation
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

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.view.AttachedObjectTarget;
import javax.faces.view.facelets.*;
import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.io.IOException;
import java.util.List;


public abstract class AttachedObjectTargetHandler extends TagHandlerImpl {
    
    private TagAttribute name = null;
    private TagAttribute targets = null;

    public AttachedObjectTargetHandler(TagConfig config) {
        super(config);
        this.name = this.getRequiredAttribute("name");
        this.targets = this.getAttribute("targets");
    }
    
    abstract AttachedObjectTargetImpl newAttachedObjectTargetImpl();
    
    public void apply(FaceletContext ctx, UIComponent parent)
    throws IOException {

        assert(ctx.getFacesContext().getAttributes().containsKey(FaceletViewHandlingStrategy.IS_BUILDING_METADATA));
        
        // only process if it's been created
        if (null == parent || 
            (null == (parent = parent.getParent())) ||
            !(ComponentHandler.isNew(parent))) {
            return;
        }

        BeanInfo componentBeanInfo = (BeanInfo)
                parent.getAttributes().get(UIComponent.BEANINFO_KEY);
        if (null == componentBeanInfo) {
            throw new TagException(this.tag, "Error: I have an EditableValueHolder tag, but no enclosing composite component");
        }
        BeanDescriptor componentDescriptor = componentBeanInfo.getBeanDescriptor();
        if (null == componentDescriptor) {
            throw new TagException(this.tag, "Error: I have an EditableValueHolder tag, but no enclosing composite component");
        }

        List<AttachedObjectTarget> targetList = (List<AttachedObjectTarget>)
                componentDescriptor.getValue(AttachedObjectTarget.ATTACHED_OBJECT_TARGETS_KEY);
        AttachedObjectTargetImpl target = newAttachedObjectTargetImpl();
        targetList.add(target);
        
        ValueExpression ve = name.getValueExpression(ctx, String.class);
        String strValue = (String) ve.getValue(ctx);
        if (null != strValue) {
            target.setName(strValue);
        }

        if (null != targets) {
            ve = targets.getValueExpression(ctx, String.class);
            target.setTargetsList(ve);
        }
        
        this.nextHandler.apply(ctx, parent);
        
    }

}
