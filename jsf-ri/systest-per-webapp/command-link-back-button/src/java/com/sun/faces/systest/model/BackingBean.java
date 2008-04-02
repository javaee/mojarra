/*
 * $Id: BackingBean.java,v 1.1 2004/10/21 21:25:10 edburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package com.sun.faces.systest.model;

public class BackingBean extends Object {

    public String linkPressed() {
	result.append("linkPressed");
	return null;
    }

    public String buttonPressed() {
	result.append("buttonPressed");
	return null;
    }

    StringBuffer result = new StringBuffer();

    public String getResult() {
	return result.toString();
    }

}
