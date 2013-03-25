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
 */

package com.sun.faces.flow.builder;

import java.util.Collections;
import javax.faces.application.*;
import java.util.Map;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import javax.el.ValueExpression;
import javax.el.ExpressionFactory;
import javax.faces.context.FacesContext;

/**
 * <p class="changed_added_2_0"><strong class="changed_modified_2_2">
 * NavigationCase</strong>
 * represents a <code>&lt;navigation-case&gt;</code> in the navigation
 * rule base, as well as the <span class="changed_modified_2_2"><code>&lt;from-view-id&gt;</code> with
 * which this <code>&lt;navigation-case&gt;</code> is a sibling</span>.</p>
 *
 * @since 2.0
 */
public class MutableNavigationCase extends NavigationCase {

    private String fromViewId;
    private String fromAction;
    private String fromOutcome;
    private String condition;
    private String toViewId;
    private String toFlowDocumentId;
    private Map<String,List<String>> parameters;
    private boolean redirect;
    private boolean includeViewParams;

    private ValueExpression toViewIdExpr;
    private ValueExpression conditionExpr;
    private String toString;
    private int hashCode;


    // ------------------------------------------------------------ Constructors

    public MutableNavigationCase() {
        this(null, null, null, null, null, null, null, false, false);
        parameters = new ConcurrentHashMap<String, List<String>>();
    }
    
    public MutableNavigationCase(String fromViewId,
                          String fromAction,
                          String fromOutcome,
                          String condition,
                          String toViewId,
                          String toFlowDocumentId,
                          Map<String,List<String>> parameters,
                          boolean redirect,
                          boolean includeViewParams) {
        super(fromViewId, fromAction, fromOutcome, condition, toViewId, toFlowDocumentId, parameters, redirect, includeViewParams);

        this.fromViewId = fromViewId;
        this.fromAction = fromAction;
        this.fromOutcome = fromOutcome;
        this.condition = condition;
        this.toViewId = toViewId;
        this.toFlowDocumentId = toFlowDocumentId;
        this.parameters = (null != parameters) ? parameters : new ConcurrentHashMap<String, List<String>>();
        this.redirect = redirect;
        this.includeViewParams = includeViewParams;

    }

    public MutableNavigationCase(String fromViewId, 
                        String fromAction, String fromOutcome, String condition, String toViewId, 
                        String toFlowDocumentId, boolean redirect, boolean includeViewParams) {
        super(fromViewId, fromAction, fromOutcome, condition, toViewId, toFlowDocumentId, Collections.EMPTY_MAP, redirect, includeViewParams);

        this.fromViewId = fromViewId;
        this.fromAction = fromAction;
        this.fromOutcome = fromOutcome;
        this.condition = condition;
        this.toViewId = toViewId;
        this.toFlowDocumentId = toFlowDocumentId;
        this.parameters = Collections.emptyMap();
        this.redirect = redirect;
        this.includeViewParams = includeViewParams;
        
    }

    // ---------------------------------------------------------- Public Methods

    @Override
    public String getFromViewId() {

        return fromViewId;

    }
    
    public void setFromViewId(String fromViewId) {
        this.fromViewId = fromViewId;
    }


    @Override
    public String getFromAction() {

        return fromAction;

    }
    
    public void setFromAction(String fromAction) {
        this.fromAction = fromAction;
    }


    @Override
    public String getFromOutcome() {

        return fromOutcome;

    }
    
    public void setFromOutcome(String fromOutcome) {
        this.fromOutcome = fromOutcome;
    }

    @Override
    public String getToViewId(FacesContext context) {

        if (toViewIdExpr == null) {
            ExpressionFactory factory =
                  context.getApplication().getExpressionFactory();
            toViewIdExpr = factory.createValueExpression(context.getELContext(),
                                                         toViewId,
                                                         String.class);
        }
        String result = (String) toViewIdExpr.getValue(context.getELContext());
        if (result.charAt(0) != '/') {
            result = '/' + result;
        }

        return result;

    }
    
    public void setToViewId(String toViewId) {
        this.toViewId = toViewId;
        toViewIdExpr = null;
    }

    @Override
    public String getToFlowDocumentId() {

        return toFlowDocumentId;

    }
    
    public void setToFlowDocumentId(String toFlowDocumentId) {
        this.toFlowDocumentId = toFlowDocumentId;
    }
    
    @Override
    public boolean hasCondition() {

        return (condition != null);

    }

    @Override
    public Boolean getCondition(FacesContext context) {

        if (conditionExpr == null && condition != null) {
            ExpressionFactory factory =
                  context.getApplication().getExpressionFactory();
            conditionExpr = factory.createValueExpression(context.getELContext(),
                                                          condition,
                                                          Boolean.class);
        }

        return ((conditionExpr != null)
                ? (Boolean) conditionExpr.getValue(context.getELContext())
                : null);

    }
    
    public void setCondition(String condition) {
        this.condition = condition;
        this.conditionExpr = null;
    }
    
    public void setConditionExpression(ValueExpression conditionExpression) {
        this.conditionExpr = conditionExpression;
    }

    @Override
    public Map<String, List<String>> getParameters() {

        return parameters;

    }
    
    @Override
    public boolean isRedirect() {

        return redirect;

    }
    
    public void setRedirect(boolean redirect) {
        this.redirect = redirect;
    }
    


    @Override
    public boolean isIncludeViewParams() {

        return includeViewParams;

    }
    
    public void setIncludeViewParams(boolean includeViewParams) {
        this.includeViewParams = includeViewParams;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MutableNavigationCase other = (MutableNavigationCase) obj;
        if ((this.fromViewId == null) ? (other.fromViewId != null) : !this.fromViewId.equals(other.fromViewId)) {
            return false;
        }
        if ((this.fromAction == null) ? (other.fromAction != null) : !this.fromAction.equals(other.fromAction)) {
            return false;
        }
        if ((this.fromOutcome == null) ? (other.fromOutcome != null) : !this.fromOutcome.equals(other.fromOutcome)) {
            return false;
        }
        if ((this.condition == null) ? (other.condition != null) : !this.condition.equals(other.condition)) {
            return false;
        }
        if ((this.toViewId == null) ? (other.toViewId != null) : !this.toViewId.equals(other.toViewId)) {
            return false;
        }
        if ((this.toFlowDocumentId == null) ? (other.toFlowDocumentId != null) : !this.toFlowDocumentId.equals(other.toFlowDocumentId)) {
            return false;
        }
        if (this.parameters != other.parameters && (this.parameters == null || !this.parameters.equals(other.parameters))) {
            return false;
        }
        if (this.redirect != other.redirect) {
            return false;
        }
        if (this.includeViewParams != other.includeViewParams) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + (this.fromViewId != null ? this.fromViewId.hashCode() : 0);
        hash = 29 * hash + (this.fromAction != null ? this.fromAction.hashCode() : 0);
        hash = 29 * hash + (this.fromOutcome != null ? this.fromOutcome.hashCode() : 0);
        hash = 29 * hash + (this.condition != null ? this.condition.hashCode() : 0);
        hash = 29 * hash + (this.toViewId != null ? this.toViewId.hashCode() : 0);
        hash = 29 * hash + (this.toFlowDocumentId != null ? this.toFlowDocumentId.hashCode() : 0);
        hash = 29 * hash + (this.parameters != null ? this.parameters.hashCode() : 0);
        hash = 29 * hash + (this.redirect ? 1 : 0);
        hash = 29 * hash + (this.includeViewParams ? 1 : 0);
        return hash;
    }
    


    @Override
    public String toString() {

        if (toString == null) {
            StringBuilder sb = new StringBuilder(64);
            sb.append("NavigationCase{");
            sb.append("fromViewId='").append(fromViewId).append('\'');
            sb.append(", fromAction='").append(fromAction).append('\'');
            sb.append(", fromOutcome='").append(fromOutcome).append('\'');
            sb.append(", if='").append(condition).append('\'');
            sb.append(", toViewId='").append(toViewId).append('\'');
            sb.append(", faces-redirect=").append(redirect);
            sb.append(", includeViewParams=").append(includeViewParams).append('\'');
            sb.append(", parameters=").append(((parameters != null) ? parameters.toString() : ""));
            sb.append('}');
            toString = sb.toString();
        }
        return toString;

    }
 
}
