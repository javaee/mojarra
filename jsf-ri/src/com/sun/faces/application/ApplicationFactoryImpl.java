/*
 * $Id: ApplicationFactoryImpl.java,v 1.2 2003/12/17 15:13:22 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.application;

import javax.faces.application.ApplicationFactory;
import javax.faces.application.Application;

import com.sun.faces.util.Util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p><strong>ApplicationFactory</strong> is a factory object that creates
 * (if needed) and returns {@link Application} instances.</p>
 *
 * <p>There must be one {@link ApplicationFactory} instance per web
 * application that is utilizing JavaServer Faces.  This instance can be
 * acquired, in a portable manner, by calling:</p>
 * <pre>
 *   ApplicationFactory factory = (ApplicationFactory)
 *    FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
 * </pre>
 */
public class ApplicationFactoryImpl extends ApplicationFactory {

// Log instance for this class
protected static Log log = LogFactory.getLog(ApplicationFactoryImpl.class);

//
// Protected Constants
//

//
// Class Variables
//

// Attribute Instance Variables

    private Application application;

// Relationship Instance Variables

//
// Constructors and Initializers
//


    /*
     * Constructor
     */
    public ApplicationFactoryImpl() {
        super();
        application = null;
        if (log.isDebugEnabled()) {
            log.debug("Created ApplicationFactory ");
        }
    }

    /**
     * <p>Create (if needed) and return an {@link Application} instance
     * for this web application.</p>
     */
    public Application getApplication() {

        if (application == null) {
            application = new ApplicationImpl();
            if (log.isDebugEnabled()) {
                log.debug("Created Application instance " + application);
            }
        }
        return application;
    }

    /**
     * <p>Replace the {@link Application} instance that will be
     * returned for this web application.</p>
     *
     * @param application The replacement {@link Application} instance
     */
    public void setApplication(Application application) {
        if (application == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

        this.application = application;
        if (log.isDebugEnabled()) {
            log.debug("set Application Instance to " + application);
        }
    }
}
