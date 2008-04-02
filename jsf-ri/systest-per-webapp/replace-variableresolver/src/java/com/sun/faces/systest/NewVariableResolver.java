/*
 * NewVariableResolver.java
 *
 * Created on April 29, 2006, 1:50 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.sun.faces.systest;

import com.sun.faces.config.ConfigureListener;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.VariableResolver;

/**
 *
 * @author edburns
 */
public class NewVariableResolver extends VariableResolver {
    
    private VariableResolver original = null;
    
    /** Creates a new instance of NewVariableResolver */
    public NewVariableResolver(VariableResolver original) {
        this.original = original;
        
        ConfigureListener.getExternalContextDuringInitialize().getApplicationMap().put("newVR", this);
    }

    public Object resolveVariable(FacesContext context, String name) throws EvaluationException {
        Object result = null;
        
        if (name.equals("custom")) {
            result = "custom";
        }
        else {
            result = original.resolveVariable(context, name);
        }
        
        return result;
    }
    
}
