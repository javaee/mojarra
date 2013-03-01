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
package com.sun.faces.flow.builder;

import com.sun.faces.util.Util;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.el.ValueExpression;
import javax.faces.application.NavigationCase;
import javax.faces.flow.builder.NavigationCaseBuilder;

public class NavigationCaseBuilderImpl extends NavigationCaseBuilder {
    
    private FlowBuilderImpl root;
    private MutableNavigationCase navCase;

    public NavigationCaseBuilderImpl(FlowBuilderImpl root) {
        navCase = new MutableNavigationCase();
        this.root = root;
    }
    
    @Override
    public NavigationCaseBuilder toFlowDocumentId(String toFlowDocumentId) {
        Util.notNull("toFlowDocumentId", toFlowDocumentId);
        navCase.setToFlowDocumentId(toFlowDocumentId);
        return this;
    }

    @Override
    public NavigationCaseBuilder fromAction(String fromAction) {
        Util.notNull("fromAction", fromAction);
        navCase.setFromAction(fromAction);
        return this;
    }

    @Override
    public NavigationCaseBuilder fromOutcome(String fromOutcome) {
        Util.notNull("fromOutcome", fromOutcome);
        navCase.setFromOutcome(fromOutcome);
        return this;
    }

    @Override
    public NavigationCaseBuilder fromViewId(String fromViewId) {
        Util.notNull("fromViewId", fromViewId);
        navCase.setFromViewId(fromViewId);
        Map<String,Set<NavigationCase>> rules = root._getFlow()._getNavigationCases();
        Set<NavigationCase> cases = rules.get(fromViewId);
        if (null == cases) {
            cases = new CopyOnWriteArraySet<NavigationCase>();
            rules.put(fromViewId, cases);
        }
        cases.add(navCase);
        return this;
    }

    @Override
    public NavigationCaseBuilder toViewId(String toViewId) {
        Util.notNull("toViewId", toViewId);
        navCase.setToViewId(toViewId);
        return this;
    }

    @Override
    public NavigationCaseBuilder condition(String condition) {
        Util.notNull("condition", condition);
        navCase.setCondition(condition);
        return this;
    }

    @Override
    public NavigationCaseBuilder condition(ValueExpression condition) {
        Util.notNull("condition", condition);
        navCase.setConditionExpression(condition);
        return this;
    }

    @Override
    public RedirectBuilder redirect() {
        navCase.setRedirect(true);
        return new RedirectBuilderImpl();
    }

    private class RedirectBuilderImpl extends NavigationCaseBuilder.RedirectBuilder {

        public RedirectBuilderImpl() {
        }

        @Override
        public RedirectBuilder parameter(String name, String value) {
            Util.notNull("name", name);
            Util.notNull("value", value);
            Map<String, List<String>> redirectParams = NavigationCaseBuilderImpl.this.navCase.getParameters();
            List<String> values = redirectParams.get(name);
            if (null == values) {
                values = new CopyOnWriteArrayList<String>();
                redirectParams.put(name, values);
            }
            values.add(value);
            return this;
        }

        @Override
        public RedirectBuilder includeViewParams() {
            NavigationCaseBuilderImpl.this.navCase.isIncludeViewParams();
            return this;
        }
    
        
        
        
    }
    
    
}
