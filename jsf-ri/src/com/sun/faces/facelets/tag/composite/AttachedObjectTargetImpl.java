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

import java.util.ArrayList;
import java.util.List;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.webapp.pdl.AttachedObjectTarget;

import com.sun.faces.util.Util;
import java.util.Iterator;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.event.AbortProcessingException;


public class AttachedObjectTargetImpl implements AttachedObjectTarget {

    private String name = null;
    public String getName() {
        return name;
    }
    
    void setName(String name) {
        this.name = name;
    }

    public List<UIComponent> getTargets(UIComponent topLevelComponent) {
        assert(null != name);
        
        List<UIComponent> result;
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (null != targetsList) {
            String targetsListStr = (String) targetsList.getValue(ctx.getELContext());
            String[] targetArray = Util.split(targetsListStr, " ");
            result = new ArrayList<UIComponent>(targetArray.length);
            for (int i = 0, len = targetArray.length; i < len; i++) {
                UIComponent comp = findWithTreeTraversal(ctx, topLevelComponent, targetArray[i]);
                if (null != comp) {
                    result.add(comp);
                }
            }
        }
        else {
            result = new ArrayList<UIComponent>(1);
            UIComponent comp = findWithTreeTraversal(ctx, topLevelComponent, name);
            if (null != comp) {
                result.add(comp);
            }
        }
        return result;
        
    }
    
    private UIComponent findWithTreeTraversal(FacesContext context,
            UIComponent topLevelComponent,
            final String componentId) {
        final UIComponent result[] = new UIComponent[1];
        result[0] = null;
        
        Iterator<UIComponent> iter = topLevelComponent.getFacetsAndChildren();
        while (null == result[0] && iter.hasNext()) {
            try {
                iter.next().visitTree(VisitContext.createVisitContext(context),
                        new VisitCallback() {

                            public VisitResult visit(VisitContext context,
                                    UIComponent target) {
                                VisitResult visitResult = VisitResult.ACCEPT;
                                String targetId = target.getId();
                                if (null != targetId &&
                                    targetId.equals(componentId)) {
                                    visitResult = VisitResult.COMPLETE;
                                    result[0] = target;
                                }

                                return visitResult;
                            }

                        });
            } catch (AbortProcessingException e) {
            }
        }
        return result[0];
    }
    
    private ValueExpression targetsList;
    
    void setTargetsList(ValueExpression targetsList) {
        this.targetsList = targetsList;
    }


}
