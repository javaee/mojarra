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
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.VariableResolver;

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
