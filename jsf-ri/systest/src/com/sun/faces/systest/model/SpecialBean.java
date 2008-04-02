/*
 * $Id: SpecialBean.java,v 1.2 2006/03/29 22:38:53 rlubke Exp $
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
 * Copyright 2006 Sun Microsystems Inc. All Rights Reserved
 */

package com.sun.faces.systest.model;

public class SpecialBean {


    private final String special;

    // ------------------------------------------------------------ Constructors


    public SpecialBean(String specialValue) {

        special = specialValue;

    }

    // ---------------------------------------------------------- Public Methods


    public boolean equals(Object target) {

        if (!(target instanceof SpecialBean)) {
            return false;
        } else {
            return (special.equals(((SpecialBean) target).getString()));
        }

    }


    public String getString() {

        return special;

    }

}

