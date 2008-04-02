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

package com.sun.faces.systest;

import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;

public class NewApplicationFactory extends ApplicationFactory {


    private ApplicationFactory oldFactory = null;

    private NewApplication newApp = null;

    // ------------------------------------------------------------ Constructors


    public NewApplicationFactory(ApplicationFactory yourOldFactory) {

        oldFactory = yourOldFactory;

    }

    // ---------------------------------------------------------- Public Methods


    public Application getApplication() {

        if (null == newApp) {
            newApp = new NewApplication(oldFactory.getApplication());
        }
        return newApp;

    }


    public void setApplication(Application application) {

        newApp = (NewApplication) application;

    }


    public String toString() {

        return "NewApplicationFactory";

    }

}
