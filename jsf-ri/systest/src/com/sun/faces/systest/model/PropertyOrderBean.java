/*
 * $Id: PropertyOrderBean.java,v 1.4 2006/03/29 23:04:01 rlubke Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
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
