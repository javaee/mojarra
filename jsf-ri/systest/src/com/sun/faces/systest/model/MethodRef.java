/*
 * $Id: MethodRef.java,v 1.11 2004/04/26 16:37:41 jvisvanathan Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
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


