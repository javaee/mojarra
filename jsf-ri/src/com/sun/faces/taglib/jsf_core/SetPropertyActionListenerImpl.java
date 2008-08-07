/*
 * $Id: SetPropertyActionListenerImpl.java,v 1.10 2007/07/19 15:50:55 rlubke Exp $
 */
/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
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

public class SetPropertyActionListenerImpl implements ActionListener, StateHolder {
    
    private ValueExpression target;
    private ValueExpression source;


    // ------------------------------------------------------------ Constructors
    
    public SetPropertyActionListenerImpl() {}


    public SetPropertyActionListenerImpl(ValueExpression target, ValueExpression value) {

        this.target = target;
        this.source = value;

    }


    // --------------------------------------------- Methods from ActionListener


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


    // ------------------------------------------------ Methods from StateHolder

    
    public void setTransient(boolean trans) {
    }

    
    public boolean isTransient() {

        return false;

    }

    
    public Object saveState(FacesContext context) {

        Object [] state = new Object[2];
        state[0] = target;
        state[1] = source;
        return state;

    }
    
    public void restoreState(FacesContext context, Object state) {

        Object [] stateArray = (Object []) state;
        target = (ValueExpression) stateArray[0];
        source = (ValueExpression) stateArray[1];

    }
    
}
