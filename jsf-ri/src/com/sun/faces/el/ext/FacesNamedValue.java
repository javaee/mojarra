/*
 * $Id: FacesNamedValue.java,v 1.1 2003/08/13 18:10:46 rlubke Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.el.ext;

import com.sun.faces.el.impl.NamedValue;
import com.sun.faces.el.impl.ExpressionInfo;
import com.sun.faces.el.impl.ElException;

import javax.faces.el.VariableResolver;
import javax.faces.context.ExternalContext;
import java.util.Map;

public class FacesNamedValue extends NamedValue {

    //
    // private/protected constants
    //    
    
    //
    // Constructors and Initializers    
    //

    public FacesNamedValue(String pName) {
        super(pName);
    }
    
    //
    // General Methods
    //

    /**
     *
     * Evaluates by looking up the name in the VariableResolver
     **/
    public Object evaluate(ExpressionInfo exprInfo) throws ElException {
        
        FacesExpressionInfo facesExprInfo = (FacesExpressionInfo) exprInfo;        
        VariableResolver resolver = facesExprInfo.getFacesVariableResolver();        
        Object rValue = facesExprInfo.getRValue();
        
        if (resolver == null) {
            return null;
        } else {
            String name = getName();            
            if (rValue != null) {
                replaceOrAddAttributeInValidScopes(
                    facesExprInfo.getFacesContext().getExternalContext(),
                    name,
                    rValue);
                return null;
            }
            return resolver.resolveVariable(facesExprInfo.getFacesContext(), name);
        }
    }          
    
    // Reference section 5.1.2.3
    // if the identifier matches an attribute in
    // the request, session, or applications scopes
    // the new value replaces the existing value.
    // Otherwise a new request scoped attribute
    // will be created using the identifier
    // as the name, and the new value.
    private void replaceOrAddAttributeInValidScopes(ExternalContext context, String name, Object value) {               
        Map sessionMap = context.getSessionMap();
        if (sessionMap.containsKey(name)) {
            sessionMap.put(name, value);          
        }               
        
        Map applicationMap = context.getApplicationMap();
        if (applicationMap.containsKey(name)) {
            applicationMap.put(name, value);      
        }
              
        Map requestMap = context.getRequestMap();
        requestMap.put(name, value);
    }

} // end of class FacesNamedValue
