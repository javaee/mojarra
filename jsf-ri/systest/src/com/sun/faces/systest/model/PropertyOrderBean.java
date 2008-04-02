/*
 * $Id: PropertyOrderBean.java,v 1.1 2004/04/28 01:41:26 eburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.systest.model;

import java.util.ArrayList;

public class PropertyOrderBean extends Object {

    protected String one;
    public String getOne() {
	return one;
    }

    public void setOne(String newOne) {
	one = newOne;
	order = order + " " + one;
    }

    protected String two;
    public String getTwo() {
	return two;
    }

    public void setTwo(String newTwo) {
	two = newTwo;
	order = order + " " + two;
    }

    protected String three;
    public String getThree() {
	return three;
    }

    public void setThree(String newThree) {
	three = newThree;
	order = order + " " + three;
    }

    protected String four;
    public String getFour() {
	return four;
    }

    public void setFour(String newFour) {
	four = newFour;
	order = order + " " + four;
    }

    
    protected String order = "";
    public String getOrder() {
	return order;
    }

    protected ArrayList listProperty = new ArrayList();

    public ArrayList getListProperty() {
	return listProperty;
    }

    public void setListProperty(ArrayList newListProperty) {
	listProperty = newListProperty;
    }



}
