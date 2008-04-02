/*
 * $Id: TestExceptionBean.java,v 1.4 2006/03/29 23:04:37 rlubke Exp $
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

package com.sun.faces;
/**
 * <p>JavaBean represented the data for an individual customer.</p>
 */

public class TestExceptionBean implements java.io.Serializable {


    public TestExceptionBean() throws InstantiationException{
        throw new InstantiationException("TestConstructorException Passed");
    }


    private String name = null;

    public String getName() {
        return (this.name);
    }


    public void setName(String name) {
        this.name = name;
    }

}
