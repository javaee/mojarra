/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2008 Sun Microsystems, Inc. All rights reserved.
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


package model;

import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.event.ValueChangeEvent;
import javax.faces.event.ValueChangeListener;

public class Bean implements ActionListener, ValueChangeListener {

    public Bean() {
    }

    private String requestKey = "Hello World!";

    public void setRequestKey(String message) {
        this.requestKey = message;
    }
    
    public String getRequestKey() {
        return requestKey;
    }

    @NotEmpty
    private String fieldValue;

    public void setFieldValue(String message) {
        this.fieldValue = message;
    }
    
    public String getFieldValue() {
        return fieldValue;
    }


    private String actionListenerKey = "actionListenerCalled";

    public void setActionListenerKey(String message) {
        this.actionListenerKey = message;
    }
    
    public String getActionListenerKey() {
        return actionListenerKey;
    }



    
    public String loginAction() {
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().
	    put("loginActionCalled", Boolean.TRUE);
        
        return "login";
    }

    public String backAction() {
        return "back";
    }

    public ActionListener getLoginEventListener() {
	Bean result = new Bean();
	result.setRequestKey("Login Event Listener");
	result.setActionListenerKey("loginEventCalled");
	return result;
    }

    public ActionListener getCancelEventListener() {
	Bean result = new Bean();
	result.setRequestKey("Cancel Event Listener");
	result.setActionListenerKey("cancelEventCalled");
	return result;
    }

    public ActionListener getAllEventsListener() {
	Bean result = new Bean();
	result.setRequestKey("All Events Listener");
	result.setActionListenerKey("allEventsCalled");
	return result;
    }
    
    public ValueChangeListener getUseridValueChangeListener() {
        Bean other = new Bean();
        other.setRequestKey("useridValueChangeListener");
        return ((ValueChangeListener) other);
    }

    public ValueChangeListener getPasswordValueChangeListener() {
        Bean other = new Bean();
        other.setRequestKey("passwordValueChangeListener");
        return ((ValueChangeListener) other);
    }
    
    // PENDING(edburns): This ends up being installed twice each time
    // the page says install it once, not correct.
    
    public void processValueChange(ValueChangeEvent arg0) throws AbortProcessingException {
        String newValue = (String) arg0.getNewValue();
        if (null != newValue && !(0 == newValue.length())) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().
                    put(getRequestKey(), newValue);
        }
    }
    
    

    public void processAction(ActionEvent event) {
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().
	    put(getActionListenerKey(), getRequestKey());
        
    }

}
