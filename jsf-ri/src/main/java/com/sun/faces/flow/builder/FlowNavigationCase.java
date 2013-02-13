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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.NavigationCase;
import javax.faces.context.FacesContext;

public class FlowNavigationCase extends NavigationCase {
    
    private String fromOutcome;
    private String condition;
    private String fromViewId;
    private String toViewId;
    private ValueExpression conditionExpr;
    
    private String toFlowDocumentId;
    private ValueExpression toFlowDocumentIdExpr;
    
    public FlowNavigationCase() {
        super(null, null, null, null, null, null, false, false);
        fromOutcome = null;
    }

    public FlowNavigationCase(String fromViewId, String fromAction, String fromOutcome, String condition, String toViewId, Map<String, List<String>> parameters, boolean redirect, boolean includeViewParams) {
        super(fromViewId, fromAction, fromOutcome, condition, toViewId, parameters, redirect, includeViewParams);
        this.fromOutcome = fromOutcome;
    }

    public ValueExpression getConditionExpression () {
        return conditionExpr;
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

    @Override
    public String getFromOutcome() {
        return fromOutcome;
    }

    @Override
    public String getToViewId(FacesContext context) {
        return toViewId;
    }

    @Override
    public String getToFlowDocumentId(FacesContext context) {
        if (toFlowDocumentIdExpr == null) {
            ExpressionFactory factory =
                  context.getApplication().getExpressionFactory();
            toFlowDocumentIdExpr = factory.createValueExpression(context.getELContext(),
                                                         toFlowDocumentId,
                                                         String.class);
        }
        String result = (String) toFlowDocumentIdExpr.getValue(context.getELContext());

        return result;
        
    }
    
    public void setToFlowDocumentId(String toFlowDocumentId) {
        this.toFlowDocumentId = toFlowDocumentId;
    }
    
    @Override
    public boolean hasCondition() {
        return (condition != null);
    }

    public void setActionURL(FacesContext context, URL actionURL) throws MalformedURLException {
    }

    
    public void setBookmarkableURL(FacesContext context, URL bookmarkableURL) throws MalformedURLException {
    }

    
    public void setCondition(String condition) {
        this.condition = condition;
    }

    public void setConditionExpression(ValueExpression conditionExpression) {
        this.conditionExpr = conditionExpression;
    }
    
    public void setFromAction(String a) {
    }

    
    public void setFromOutcome(String fromOutcome) {
        this.fromOutcome = fromOutcome;
    }

    
    public void setFromViewId(String fromViewId) {
        this.fromViewId = fromViewId;
    }

    @Override
    public String getFromViewId() {
        return fromViewId;
    }
    
    public void setParameters(Map<String, List<String>> a) {
    }

    
    public void setRedirectURL(FacesContext context, URL redirectURL) throws MalformedURLException {
    }

    
    public void setResourceURL(FacesContext context, URL resourceURL) throws MalformedURLException {
    }

    
    public void setToViewId(String toViewId) {
        this.toViewId = toViewId;
    }
    
    public void setIncludeViewParams(boolean a) {
    }

    
    public void setRedirect(boolean a) {
    }
    
    
    
}
