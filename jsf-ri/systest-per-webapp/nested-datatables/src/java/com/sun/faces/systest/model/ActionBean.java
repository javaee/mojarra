/*
 * $Id: ActionBean.java,v 1.3 2006/03/29 22:39:18 rlubke Exp $
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

public class ActionBean extends Object {


    protected int innerActionCallCount = 0;

    protected int outerActionCallCount = 0;

    // ------------------------------------------------------------ Constructors


    public ActionBean() {
    }

    // ---------------------------------------------------------- Public Methods


    public int getInnerActionCallCount() {

        return innerActionCallCount;

    }


    public void setInnerActionCallCount(int newInnerActionCallCount) {

        innerActionCallCount = newInnerActionCallCount;

    }


    public int getOuterActionCallCount() {

        return outerActionCallCount;

    }


    public void setOuterActionCallCount(int newOuterActionCallCount) {

        outerActionCallCount = newOuterActionCallCount;

    }


    public String innerAction() {

        innerActionCallCount++;
        return null;

    }


    public String outerAction() {

        outerActionCallCount++;
        return null;

    }

}
