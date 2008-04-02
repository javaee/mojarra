/*
 * $Id: ApplicationFactoryImpl.java,v 1.7 2004/05/10 19:55:59 jvisvanathan Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.application;

import com.sun.faces.util.Util;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;

/**
 * <p><strong>ApplicationFactory</strong> is a factory object that creates
 * (if needed) and returns {@link Application} instances.</p>
 * <p/>
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
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message + " Application " + application;
            throw new NullPointerException(message);
        }

        this.application = application;
        if (log.isDebugEnabled()) {
            log.debug("set Application Instance to " + application);
        }
    }
}
