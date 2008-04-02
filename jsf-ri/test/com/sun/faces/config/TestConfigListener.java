/*
 * $Id: TestConfigListener.java,v 1.8 2005/10/19 19:51:30 edburns Exp $
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

package com.sun.faces.config;

import com.sun.faces.cactus.ServletFacesTestCase;
import org.apache.cactus.ServletTestCase;

import javax.servlet.ServletContextEvent;

/**
 * <p>Unit tests for Configuration File processing.</p>
 */

public class TestConfigListener extends ServletFacesTestCase {


    // ----------------------------------------------------- Instance Variables


    // ----------------------------------------------------------- Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public TestConfigListener(String name) {

        super(name);

    }


    // --------------------------------------------------- Overall Test Methods




    // ------------------------------------------------ Individual Test Methods

    // this method manually invokes the ContextListener contextInitialized method
    // multiple times to ensure the parsing logic only gets executed once
    // (for the same webapp).
    //
    public void testContextInitialized() {
        ConfigureListener cl = new ConfigureListener();
        ServletContextEvent e = new ServletContextEvent(
            getConfig().getServletContext());
        cl.contextInitialized(e);
        cl.contextInitialized(e);
    }
}
