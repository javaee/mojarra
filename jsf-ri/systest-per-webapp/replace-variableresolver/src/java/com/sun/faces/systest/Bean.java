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
