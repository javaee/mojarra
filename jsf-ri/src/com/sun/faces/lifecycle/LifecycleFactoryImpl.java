/*
 * $Id: LifecycleFactoryImpl.java,v 1.31 2007/04/25 04:07:01 rlubke Exp $
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

// LifecycleFactoryImpl.java

package com.sun.faces.lifecycle;

import java.util.HashMap;
import java.util.Iterator;

import javax.faces.FacesException;
import javax.faces.event.PhaseId;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;

import com.sun.faces.util.MessageUtils;
import com.sun.faces.util.FacesLogger;

import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * <B>LifecycleFactoryImpl</B> is the stock implementation of Lifecycle
 * in the JSF RI. <P>
 *
 * @version $Id: LifecycleFactoryImpl.java,v 1.31 2007/04/25 04:07:01 rlubke Exp $
 * @see	javax.faces.lifecycle.LifecycleFactory
 */

public class LifecycleFactoryImpl extends LifecycleFactory {

    //
    // Protected Constants
    //
    static final int FIRST_PHASE = PhaseId.RESTORE_VIEW.getOrdinal();
    static final int LAST_PHASE = PhaseId.RENDER_RESPONSE.getOrdinal();

    // Log instance for this class
    private static Logger LOGGER = FacesLogger.LIFECYCLE.getLogger();

    //
    // Class Variables
    //

    //
    // Instance Variables
    //

    // Attribute Instance Variables

    // Relationship Instance Variables

    protected HashMap<String,LifecycleWrapper> lifecycleMap = null;
    protected Object lock = null;

    //
    // Constructors and Initializers    
    //

    public LifecycleFactoryImpl() {
        super();
        lifecycleMap = new HashMap<String, LifecycleWrapper>();

        // We must have an implementation under this key.
        lifecycleMap.put(LifecycleFactory.DEFAULT_LIFECYCLE,
                         new LifecycleWrapper(new LifecycleImpl(),
                                              false));
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Created Default Lifecycle");
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
        LifecycleWrapper wrapper = lifecycleMap.get(
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
        if (null == phase) {
            throw new NullPointerException(
                MessageUtils.getExceptionMessageString(MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "lifecycleId"));
        }
        if (null == phase) {
            throw new NullPointerException(
                MessageUtils.getExceptionMessageString(MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "phace"));
        }

        if (null ==
            (wrapper = lifecycleMap.get(lifecycleId))) {
            message = MessageUtils.getExceptionMessageString(
                MessageUtils.LIFECYCLE_ID_NOT_FOUND_ERROR_MESSAGE_ID,
                params);
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning(message);
            }
            throw new IllegalArgumentException(message);
        }
        result = wrapper.instance;
        assert (null != result);

        if (alreadyCreated(lifecycleId)) {
            message = MessageUtils.getExceptionMessageString(
                MessageUtils.LIFECYCLE_ID_ALREADY_ADDED_ID,
                params);
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning(message);
            }
            throw new IllegalStateException(message);
        }

        if (!((FIRST_PHASE <= phaseId) &&
            (phaseId <= LAST_PHASE))) {
            params = new Object[]{Integer.toString(phaseId)};
            message = MessageUtils.getExceptionMessageString(
                MessageUtils.PHASE_ID_OUT_OF_BOUNDS_ERROR_MESSAGE_ID,
                params);
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning(message);
            }
            throw new IllegalArgumentException(message);
        }
        return result;
    }

//
// Methods from LifecycleFactory
//

    public void addLifecycle(String lifecycleId, Lifecycle lifecycle) {
        if (lifecycleId == null) {
            throw new NullPointerException(
                MessageUtils.getExceptionMessageString(MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "lifecycleId"));
        }
        if (lifecycle == null) {
            throw new NullPointerException(
                MessageUtils.getExceptionMessageString(MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "lifecycle"));
        }
        if (null != lifecycleMap.get(lifecycleId)) {
            Object params[] = {lifecycleId};
            String message =
                MessageUtils.getExceptionMessageString(MessageUtils.LIFECYCLE_ID_ALREADY_ADDED_ID,
                                         params);
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning(MessageUtils.getExceptionMessageString(
                        MessageUtils.LIFECYCLE_ID_ALREADY_ADDED_ID,params));
            }
            throw new IllegalArgumentException(message);
        }
        lifecycleMap.put(lifecycleId, new LifecycleWrapper(lifecycle, false));
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("addedLifecycle: " + lifecycleId + " " + lifecycle);
        }
    }


    public Lifecycle getLifecycle(String lifecycleId) throws FacesException {
        Lifecycle result = null;
        LifecycleWrapper wrapper = null;

        if (null == lifecycleId) {
            throw new NullPointerException(
                MessageUtils.getExceptionMessageString(MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "lifecycleId"));
        }

        if (null == lifecycleMap.get(lifecycleId)) {
            Object[] params = {lifecycleId};
            String message =
                MessageUtils.getExceptionMessageString(
                    MessageUtils.CANT_CREATE_LIFECYCLE_ERROR_MESSAGE_ID,
                    params);
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning("LifecycleId " + lifecycleId + " does not exist");
            }
            throw new IllegalArgumentException(message);
        }

        wrapper = lifecycleMap.get(lifecycleId);
        result = wrapper.instance;
        wrapper.created = true;

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("getLifecycle: " + lifecycleId + " " + result);
        }
        return result;
    }


    public Iterator<String> getLifecycleIds() {
        return lifecycleMap.keySet().iterator();
    }

//
// Helper classes
//

    static class LifecycleWrapper {


        Lifecycle instance = null;
        boolean created = false;


        LifecycleWrapper(Lifecycle newInstance, boolean newCreated) {
            instance = newInstance;
            created = newCreated;
        }

    } // end of class LifecycleWrapper


// The testcase for this class is TestLifecycleFactoryImpl.java 


} // end of class LifecycleFactoryImpl
