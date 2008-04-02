/*
 * $Id: MethodRef.java,v 1.7 2004/01/27 21:04:56 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.systest.model;

import javax.servlet.http.HttpSession;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.validator.ValidatorException;
import javax.faces.application.FacesMessage;


public class MethodRef extends Object {

    public MethodRef() {}

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
          throw new ValidatorException(
			       new FacesMessage("You didn't enter batman",
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
    

};
