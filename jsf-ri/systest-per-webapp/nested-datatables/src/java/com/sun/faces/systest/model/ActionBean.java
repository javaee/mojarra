/*
 * $Id: ActionBean.java,v 1.1 2004/06/17 20:13:34 eburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.systest.model;

public class ActionBean extends Object {


    public ActionBean() {
    }

    public String outerAction() {
	outerActionCallCount++;
	return null;
    }

    public String innerAction() {
	innerActionCallCount++;
	return null;
    }

    protected int outerActionCallCount = 0;
    public int getOuterActionCallCount() {
	return outerActionCallCount;
    }

    public void setOuterActionCallCount(int newOuterActionCallCount) {
	outerActionCallCount = newOuterActionCallCount;
    }

    protected int innerActionCallCount = 0;
    public int getInnerActionCallCount() {
	return innerActionCallCount;
    }

    public void setInnerActionCallCount(int newInnerActionCallCount) {
	innerActionCallCount = newInnerActionCallCount;
    }





}
