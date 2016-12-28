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
 * https://glassfish.java.net/public/CDDL+GPL_1_1.html
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

import com.sun.faces.util.Util;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.view.AttachedObjectTarget;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.faces.component.ContextCallback;
import javax.faces.component.search.SearchExpressionContext;
import javax.faces.component.search.SearchExpressionHint;


public class AttachedObjectTargetImpl implements AttachedObjectTarget {

    private String name = null;
    
    @Override
    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    private static final Set<SearchExpressionHint> EXPRESSION_HINTS =
            EnumSet.of(SearchExpressionHint.SKIP_VIRTUAL_COMPONENTS);
    
    @Override
    public List<UIComponent> getTargets(UIComponent topLevelComponent) {
        assert(null != name);

        List<UIComponent> result;
        FacesContext ctx = FacesContext.getCurrentInstance();

        if (null != targetsList) {
            String targetsListStr = (String) targetsList.getValue(ctx.getELContext());
            Map<String, Object> appMap = FacesContext.getCurrentInstance().getExternalContext().getApplicationMap();
            
            String[] targetArray = Util.split(appMap, targetsListStr, " ");
            result = new ArrayList<>(targetArray.length);
            
            if (targetArray.length > 0) {

                SearchExpressionContext searchContext = SearchExpressionContext.createSearchExpressionContext(
                                    ctx, topLevelComponent, EXPRESSION_HINTS, null);
                
                for (int i = 0, len = targetArray.length; i < len; i++) {
                    UIComponent comp = topLevelComponent.findComponent(
                          augmentSearchId(ctx, topLevelComponent, targetArray[i]));
                    if (null != comp) {
                        result.add(comp);
                    }
                }
            }
        }
        else {
            result = new ArrayList<>(1);
            UIComponent comp = topLevelComponent.findComponent(
                  augmentSearchId(ctx, topLevelComponent, name));
            if (null != comp) {
                result.add(comp);
            }
        }
        return result;

    }

    private ValueExpression targetsList;

    void setTargetsList(ValueExpression targetsList) {
        this.targetsList = targetsList;
    }


    // if the current composite component ID is the same as the target ID,
    // we'll need to make the ID passed to findComponent be a combination
    // of the two so we find the correct component.  If we don't do this,
    // we end with a StackOverFlowException as 'c' will be what is found
    // and not the child of 'c'.
    private String augmentSearchId(FacesContext ctx,
                                   UIComponent c,
                                   String targetId) {

        if (targetId.equals(c.getId())) {
            return targetId + UINamingContainer.getSeparatorChar(ctx) + targetId;
        }
        return targetId;

    }

    private static class CollectComponentListCallback implements ContextCallback {
        private List<UIComponent> list = null;

        public CollectComponentListCallback(int size) {
            list = new ArrayList<UIComponent>(size);
        }

        @Override
        public void invokeContextCallback(FacesContext context, UIComponent target) {
            getList().add(target);
        }

        public List<UIComponent> getList() {
            return list;
        }
    }
}
