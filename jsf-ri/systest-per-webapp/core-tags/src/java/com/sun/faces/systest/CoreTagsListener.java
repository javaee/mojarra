/*
 * $Id: CoreTagsListener.java,v 1.4 2006/03/29 23:04:25 rlubke Exp $
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

package com.sun.faces.systest;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * <p>Right now, just a minimal listener that sets the java.beans.Beans.DesignTime
 *    property to "true" to test the bypass of the TLV and allowance of not
 *    specifying non required attributes. </p> 
 * <p/>
 */
public class CoreTagsListener implements ServletContextListener {


    // -------------------------------------------------------- Static Variables

    /**
     * <p>The <code>Log</code> instance for this class.</p>
     */
    private static Log log = LogFactory.getLog(CoreTagsListener.class);


    // ------------------------------------------ ServletContextListener Methods
    public void contextInitialized(ServletContextEvent sce) {
        java.beans.Beans.setDesignTime(true);
    }


    public void contextDestroyed(ServletContextEvent sce) {

    }


    // --------------------------------------------------------- Private Methods

} 

