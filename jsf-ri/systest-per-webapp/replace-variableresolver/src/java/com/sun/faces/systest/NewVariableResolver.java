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
import com.sun.faces.util.Util;
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
        
        // This expects a plain old bean that is not configured as a Faces
        // managed bean.  However, an additional check is done to make sure
        // that is not configured as a managed bean.  So, we've resolved 
        // the name as a "non" managed bean, but want to do some additional
        // checks to make sure that name does not also resolve to a 
        // managed bean. 
        //  
        if (name.equals("nonmanaged")) {
            Object bean = null;
            Object managedBean = null;             
            try {
                Class clazz = Util.loadClass("com.sun.faces.systest.model.TestBean", context);
                bean = clazz.newInstance();
            } catch (Exception e) {
            } 
            managedBean = original.resolveVariable(context, name); 
            if (bean == null) {
                if (managedBean==null) {
                    return null;
                } else {
                    result = managedBean;
                }
            } else {
                result = bean;
            }
            return result;
        }

        if (name.equals("custom")) {
            result = "custom";
        }
        else {
            result = original.resolveVariable(context, name);
        }
        
        return result;
    }
    
}
