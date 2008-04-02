/*
 * $Id: LifecycleFactoryImpl.java,v 1.24 2005/06/23 20:29:33 jayashri Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// LifecycleFactoryImpl.java

package com.sun.faces.lifecycle;

import java.util.HashMap;
import java.util.Iterator;

import javax.faces.FacesException;
import javax.faces.event.PhaseId;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;

import com.sun.faces.util.Util;

import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * <B>LifecycleFactoryImpl</B> is the stock implementation of Lifecycle
 * in the JSF RI. <P>
 *
 * @version $Id: LifecycleFactoryImpl.java,v 1.24 2005/06/23 20:29:33 jayashri Exp $
 * @see	javax.faces.lifecycle.LifecycleFactory
 */

public class LifecycleFactoryImpl extends LifecycleFactory {

    //
    // Protected Constants
    //
    static final int FIRST_PHASE = PhaseId.RESTORE_VIEW.getOrdinal();
    static final int LAST_PHASE = PhaseId.RENDER_RESPONSE.getOrdinal();

    // Log instance for this class
    private static Logger logger = Util.getLogger(Util.FACES_LOGGER 
            + Util.LIFECYCLE_LOGGER);

    //
    // Class Variables
    //

    //
    // Instance Variables
    //

    // Attribute Instance Variables

    // Relationship Instance Variables

    protected HashMap lifecycleMap = null;
    protected Object lock = null;

    //
    // Constructors and Initializers    
    //

    public LifecycleFactoryImpl() {
        super();
        lifecycleMap = new HashMap();

        // We must have an implementation under this key.
        lifecycleMap.put(LifecycleFactory.DEFAULT_LIFECYCLE,
                         new LifecycleWrapper(new LifecycleImpl(),
                                              false));
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("Created Default Lifecycle");
        }
        lock = new Object();
    }

//
// Class methods
//

//
// General Methods
//

    /**
     * @return true iff lifecycleId was already created
     */

    boolean alreadyCreated(String lifecycleId) {
        LifecycleWrapper wrapper = (LifecycleWrapper) lifecycleMap.get(
            lifecycleId);
        return (null != wrapper && wrapper.created);
    }


    /**
     * POSTCONDITION: If no exceptions are thrown, it is safe to proceed with
     * register*().;
     */

    Lifecycle verifyRegisterArgs(String lifecycleId,
                                 int phaseId, Phase phase) {
        String message = null;
        LifecycleWrapper wrapper = null;
        Lifecycle result = null;
        Object[] params = {lifecycleId};
        if (null == lifecycleId || null == phase) {
            throw new NullPointerException(
                Util.getExceptionMessageString(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

        if (null ==
            (wrapper = (LifecycleWrapper) lifecycleMap.get(lifecycleId))) {
            message = Util.getExceptionMessageString(
                Util.LIFECYCLE_ID_NOT_FOUND_ERROR_MESSAGE_ID,
                params);
            if (logger.isLoggable(Level.WARNING)) {
                logger.warning(message);
            }
            throw new IllegalArgumentException(message);
        }
        result = wrapper.instance;
        assert (null != result);

        if (alreadyCreated(lifecycleId)) {
            message = Util.getExceptionMessageString(
                Util.LIFECYCLE_ID_ALREADY_ADDED_ID,
                params);
            if (logger.isLoggable(Level.WARNING)) {
                logger.warning(message);
            }
            throw new IllegalStateException(message);
        }

        if (!((FIRST_PHASE <= phaseId) &&
            (phaseId <= LAST_PHASE))) {
            params = new Object[]{Integer.toString(phaseId)};
            message = Util.getExceptionMessageString(
                Util.PHASE_ID_OUT_OF_BOUNDS_ERROR_MESSAGE_ID,
                params);
            if (logger.isLoggable(Level.WARNING)) {
                logger.warning(message);
            }
            throw new IllegalArgumentException(message);
        }
        return result;
    }

//
// Methods from LifecycleFactory
//

    public void addLifecycle(String lifecycleId, Lifecycle lifecycle) {
        if (lifecycleId == null || lifecycle == null) {
            throw new NullPointerException(
                Util.getExceptionMessageString(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        if (null != lifecycleMap.get(lifecycleId)) {
            Object params[] = {lifecycleId};
            String message =
                Util.getExceptionMessageString(Util.LIFECYCLE_ID_ALREADY_ADDED_ID,
                                         params);
            if (logger.isLoggable(Level.WARNING)) {
                logger.warning(Util.getExceptionMessageString(
                        Util.LIFECYCLE_ID_ALREADY_ADDED_ID,params));
            }
            throw new IllegalArgumentException(message);
        }
        lifecycleMap.put(lifecycleId, new LifecycleWrapper(lifecycle, false));
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("addedLifecycle: " + lifecycleId + " " + lifecycle);
        }
    }


    public Lifecycle getLifecycle(String lifecycleId) throws FacesException {
        Lifecycle result = null;
        LifecycleWrapper wrapper = null;

        if (null == lifecycleId) {
            throw new NullPointerException(
                Util.getExceptionMessageString(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

        if (null == lifecycleMap.get(lifecycleId)) {
            Object[] params = {lifecycleId};
            String message =
                Util.getExceptionMessageString(
                    Util.CANT_CREATE_LIFECYCLE_ERROR_MESSAGE_ID,
                    params);
            if (logger.isLoggable(Level.WARNING)) {
                logger.warning("LifecycleId " + lifecycleId + " does not exist");
            }
            throw new IllegalArgumentException(message);
        }

        wrapper = (LifecycleWrapper) lifecycleMap.get(lifecycleId);
        result = wrapper.instance;
        wrapper.created = true;

        if (logger.isLoggable(Level.FINE)) {
            logger.fine("getLifecycle: " + lifecycleId + " " + result);
        }
        return result;
    }


    public Iterator getLifecycleIds() {
        return lifecycleMap.keySet().iterator();
    }

//
// Helper classes
//

    static class LifecycleWrapper extends Object {


        Lifecycle instance = null;
        boolean created = false;


        LifecycleWrapper(Lifecycle newInstance, boolean newCreated) {
            instance = newInstance;
            created = newCreated;
        }

    } // end of class LifecycleWrapper


// The testcase for this class is TestLifecycleFactoryImpl.java 


} // end of class LifecycleFactoryImpl
