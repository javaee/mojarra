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

/*
 * Bean.java
 *
 * Created on April 29, 2006, 1:57 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.sun.faces.systest;

import com.sun.faces.systest.model.TestBean;
import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.PropertyResolver;
import javax.faces.el.VariableResolver;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletContext;
import javax.servlet.jsp.JspApplicationContext;
import javax.servlet.jsp.JspFactory;

/**
 *
 * @author edburns
 */
public class Bean {
    
    /** Creates a new instance of Bean */
    public Bean() {
    }
    
    public String callMethodsOnVariableResolver(FacesContext context, 
            VariableResolver vr) throws EvaluationException {
        Object result = null;
        
        result = vr.resolveVariable(context, "noneBean");
        
        if (!(result instanceof TestBean)) {
            throw new IllegalStateException("Bean not of correct type");
        }
        
        result = vr.resolveVariable(context, "custom");
        
        if (!result.equals("custom")) {
            throw new IllegalStateException("Bean not of correct type");
        }
        
        return "success";
    }

    public void verifyELResolverChainIsCorrectlyConfigured(ActionEvent e) {
        String result = null;
        final FacesContext context = FacesContext.getCurrentInstance();
        StringBuilder message = Bean.getBuilder(context);

        message.append("<br /><br />\n\n  <h1>FacesELResolverForFaces</h1><br /><br />\n\n  ");

        // FacesELResolver Chain
        context.getApplication().getELResolver().getValue(context.getELContext(), null,
                new Object() {

            @Override
            public String toString() {
                Bean.captureStackTrace(context);
                return "traceResolution";
            }

        });

        message.append("<br /><br />\n\n  <h1>FacesELResolverForJsp</h1><br /><br />\n\n  ");

        JspFactory factory = JspFactory.getDefaultFactory();
        JspApplicationContext jspContext = factory.
                getJspApplicationContext((ServletContext)
                context.getExternalContext().getContext());
        ExpressionFactory elFactory = jspContext.getExpressionFactory();
        ValueExpression ve = elFactory.createValueExpression(context.getELContext(), "#{traceResolution}",
                Object.class);
        ve.getValue(context.getELContext());
    }

    public static StringBuilder getBuilder(FacesContext context) {
        StringBuilder result = (StringBuilder) context.getExternalContext().getRequestMap().get("message");
        if (null == result) {
            result = new StringBuilder();
            context.getExternalContext().getRequestMap().put("message", result);
        }
        return result;
    }

    public static void captureStackTrace(FacesContext context) {
        StringBuilder message = getBuilder(context);
        message.append("<h2>toString() invocation</h2>");

        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        String stackTraceElement;
        for (int i = 0; i < 4; i++) {
            StackTraceElement cur = stackTrace[i];
            stackTraceElement = cur.toString();
            if (!(stackTraceElement.contains("Thread") ||
                  stackTraceElement.contains("com.sun.faces.systest.Bean"))) {
                message.append("<p>").append(stackTraceElement).append("</p>");
                break;
            }
        }
    }
    
    public String getInvokeVariableResolverThruChain() throws EvaluationException {
        FacesContext context = FacesContext.getCurrentInstance();
        VariableResolver vr = context.getApplication().getVariableResolver();
        return callMethodsOnVariableResolver(context, vr);
    }
    
    public String getInvokeVariableResolverDirectly() throws EvaluationException {
        FacesContext context = FacesContext.getCurrentInstance();
        VariableResolver vr = (VariableResolver) context.getExternalContext().getApplicationMap().get("newVR");
        return callMethodsOnVariableResolver(context, vr);
    }
    
    public String getInvokeELResolverThruChain() throws EvaluationException {
        FacesContext context = FacesContext.getCurrentInstance();
        ELResolver er = context.getApplication().getELResolver();
        boolean isReadOnly = er.isReadOnly(context.getELContext(), "newERThruChain", null);

        return Boolean.valueOf(isReadOnly).toString();
    }

    public String getInvokeELResolverDirectly() throws EvaluationException {
        FacesContext context = FacesContext.getCurrentInstance();
        ELResolver er = (ELResolver) context.getExternalContext().getApplicationMap().get("newER");
        boolean isReadOnly = er.isReadOnly(context.getELContext(), "newERDirect", null);

        return Boolean.valueOf(isReadOnly).toString();
    }

    public String getInvokeVariableResolverThruChain1() throws EvaluationException {
        FacesContext context = FacesContext.getCurrentInstance();
        VariableResolver vr = context.getApplication().getVariableResolver();
        Object result = vr.resolveVariable(context, "nonmanaged");
        if (!(result instanceof TestBean)) {
            throw new IllegalStateException("Bean not of correct type");
        }
        return "success";
    }
    
    public String getInvokeVariableResolverDirectly1() throws EvaluationException {
        FacesContext context = FacesContext.getCurrentInstance();
        VariableResolver vr = (VariableResolver) context.getExternalContext().getApplicationMap().get("newVR");
        Object result = vr.resolveVariable(context, "nonmanaged");
        if (!(result instanceof TestBean)) {
            throw new IllegalStateException("Bean not of correct type");
        }
        return "success";
    }
}
