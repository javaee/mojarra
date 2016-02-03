/*
 * $Id: PropertyOrderBean.java,v 1.5 2007/04/27 22:01:13 ofung Exp $
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
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
