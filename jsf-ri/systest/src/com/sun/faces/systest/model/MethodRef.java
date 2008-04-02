/*
 * $Id: MethodRef.java,v 1.1 2003/10/31 18:42:25 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.systest.model;


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


};
