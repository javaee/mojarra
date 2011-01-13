/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2010 Sun Microsystems, Inc. All rights reserved.
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

package com.sun.faces.systest.model;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpSession;

import javax.faces.component.UIInput;
import javax.faces.el.MethodBinding;

import com.sun.faces.systest.TestValueChangeListener;

public class MethodRef extends Object {

    public MethodRef() {
    }


    protected String buttonPressedOutcome = null;


    public String getButtonPressedOutcome() {
        return buttonPressedOutcome;
    }


    public void setButtonPressedOutcome(String newButtonPressedOutcome) {
        buttonPressedOutcome = newButtonPressedOutcome;
    }


    public String button1Pressed() {
        setButtonPressedOutcome("button1 was pressed");
        return null;
    }


    public String invalidateSession() {
        FacesContext fContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession)
            fContext.getExternalContext().getSession(true);
        session.invalidate();
        return null;
    }


    public String button2Pressed() {
        setButtonPressedOutcome("button2 was pressed");
        return null;
    }


    public void button3Pressed(ActionEvent event) {
        setButtonPressedOutcome(event.getComponent().getId() +
                                " was pressed");
    }


    protected String validateOutcome;


    public String getValidateOutcome() {
        return validateOutcome;
    }


    public void setValidateOutcome(String newValidateOutcome) {
        validateOutcome = newValidateOutcome;
    }


    public void validateInput(FacesContext context, UIComponent toValidate, Object valueObj) {
        String value = (String) valueObj;
        if (!value.equals("batman")) {
            throw new ValidatorException(new FacesMessage(
                "You didn't enter batman",
                "You must enter batman"));
        }

    }


    protected String changeOutcome;


    public String getChangeOutcome() {
        return changeOutcome;
    }


    public void setChangeOutcome(String newChangeOutcome) {
        changeOutcome = newChangeOutcome;
    }


    public void valueChange(ValueChangeEvent vce) {
        vce.getComponent().getAttributes().put("onblur",
                                               vce.getNewValue().toString());
        setChangeOutcome(vce.getNewValue().toString());
    }
    
    public void inputFieldValueChange(ValueChangeEvent vce) {
        vce.getComponent().getAttributes().put("onblur",
                                               vce.getNewValue().toString());
    }
    
    protected UIInput inputField = null;
    public void setInputField(UIInput input){
        this.inputField = input;
    }
    
    public UIInput getInputField() {
        if (inputField == null) {
            inputField = new UIInput();
            inputField.addValueChangeListener(new TestValueChangeListener());
            Class args[] = { ValueChangeEvent.class };
            MethodBinding mb = 
            FacesContext.getCurrentInstance().getApplication().
            createMethodBinding("#{methodRef.inputFieldValueChange}", args);
            inputField.setValueChangeListener(mb);
        }
        return inputField;
    }


}


