/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2012 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.faces.test.agnostic.composite;

import java.beans.BeanInfo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;

/**
 * @author Manfred Riem (manfred.riem@oracle.com)
 */
@ManagedBean(name = "issue2176Bean")
@SessionScoped
public class Issue2176Bean implements Serializable {

    /**
     * Stores the previos count.
     */
    private int previousCount = -1;

    /**
     * Get the status.
     */
    public String getStatus() {
        String result = "SUCCESS";
        final List<Integer> seenCount = new ArrayList<Integer>();
        seenCount.add(0);
        VisitContext visitContext = VisitContext.createVisitContext(FacesContext.getCurrentInstance());
        FacesContext.getCurrentInstance().getViewRoot().visitTree(visitContext, new VisitCallback() {

            public VisitResult visit(VisitContext context, UIComponent target) {
                if ("javax.faces.Composite".equals(target.getRendererType())) {
                    BeanInfo beanInfo = (BeanInfo) target.getAttributes().get(UIComponent.BEANINFO_KEY);
                    Collection<String> ids = (Collection<String>) beanInfo.getBeanDescriptor().getValue(UIComponent.ATTRS_WITH_DECLARED_DEFAULT_VALUES);

                    int count = 0;
                    if (ids != null) {
                        for (String id : ids) {
                            count++;
                        }
                    }
                    
                    seenCount.set(0, Integer.valueOf(seenCount.get(0).intValue() + count));
                }
                return VisitResult.ACCEPT;
            }
        });
        int observedCount = (Integer) seenCount.get(0);
        if (previousCount != -1 && observedCount > previousCount) {
            result = "FAILED";
        }
        previousCount = observedCount;
        return result;
    }
}
