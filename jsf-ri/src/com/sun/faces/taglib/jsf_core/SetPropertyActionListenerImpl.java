/*
 * $Id: SetPropertyActionListenerImpl.java,v 1.7 2006/09/15 01:45:58 rlubke Exp $
 */
/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

package com.sun.faces.taglib.jsf_core;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ValueExpression;
import javax.el.ExpressionFactory;
import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

public class SetPropertyActionListenerImpl
      implements ActionListener, StateHolder {


    private ValueExpression target;
    private ValueExpression source;

    // ------------------------------------------------------------ Constructors


    public SetPropertyActionListenerImpl() {
    }


    public SetPropertyActionListenerImpl(ValueExpression target,
                                         ValueExpression source) {

        this.target = target;
        this.source = source;

    }

    // --------------------------------------------- Methods From ActionListener


    public void processAction(ActionEvent e) throws AbortProcessingException {

        FacesContext facesContext = FacesContext.getCurrentInstance();
        ELContext elContext = facesContext.getELContext();        
               
        try {
            Object value = source.getValue(elContext);
            if (value != null) {
                ExpressionFactory factory =
                  facesContext.getApplication().getExpressionFactory();
                value = factory.coerceToType(value, target.getType(elContext));
            }
            target.setValue(elContext, value);
        } catch (ELException ele) {
            throw new AbortProcessingException(ele);
        }
               
    }

    // ------------------------------------------------ Methods From StateHolder

    private Object[] state;
    public Object saveState(FacesContext context) {
        
        if (state == null) {
             state = new Object[2];
        }
       
        state[0] = target;
        state[1] = source;
        return state;

    }


    public void restoreState(FacesContext context, Object state) {

        this.state = (Object[]) state;
        target = (ValueExpression) this.state[0];
        source = (ValueExpression) this.state[1];

    }


    public boolean isTransient() {
        return false;
    }


    public void setTransient(boolean newTransientValue) {
    }    

}
