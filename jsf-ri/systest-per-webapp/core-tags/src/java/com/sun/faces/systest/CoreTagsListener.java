/*
 * $Id: CoreTagsListener.java,v 1.1 2004/12/20 21:26:36 rogerk Exp $
 */
/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
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

