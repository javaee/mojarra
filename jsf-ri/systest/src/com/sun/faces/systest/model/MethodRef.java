/*
 * $Id: MethodRef.java,v 1.2 2003/10/31 21:40:21 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.systest.model;

import javax.faces.event.ActionEvent;
import javax.faces.context.FacesContext;
import javax.faces.component.UIInput;
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


    public void validateInput(FacesContext context, UIInput toValidate) {
	String value = (String) toValidate.getValue();
	if (!value.equals("batman")) {
	    toValidate.setValid(false);
	    context.addMessage(toValidate.getClientId(context),
			       new FacesMessage("You didn't enter batman",
						"You must enter batman"));
	}
	else {
	    toValidate.setValid(true);
	}
    }
    

};
