/*
 * $Id: LifecycleFactoryImpl.java,v 1.21.30.2 2007/04/27 21:27:43 ofung Exp $
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

// LifecycleFactoryImpl.java

package com.sun.faces.lifecycle;

import com.sun.faces.util.Util;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FacesException;
import javax.faces.event.PhaseId;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;

import java.util.HashMap;
import java.util.Iterator;

/**
 * <B>LifecycleFactoryImpl</B> is the stock implementation of Lifecycle
 * in the JSF RI. <P>
 *
 * @version $Id: LifecycleFactoryImpl.java,v 1.21.30.2 2007/04/27 21:27:43 ofung Exp $
 * @see	javax.faces.lifecycle.LifecycleFactory
 */

public class LifecycleFactoryImpl extends LifecycleFactory {

//
// Protected Constants
//
    static final int FIRST_PHASE = PhaseId.RESTORE_VIEW.getOrdinal();
    static final int LAST_PHASE = PhaseId.RENDER_RESPONSE.getOrdinal();

// Log instance for this class
    protected static Log log = LogFactory.getLog(LifecycleFactoryImpl.class);

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
        if (log.isDebugEnabled()) {
            log.debug("Created Default Lifecycle");
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
            if (log.isErrorEnabled()) {
                log.error("Error: " + message);
            }
            throw new IllegalArgumentException(message);
        }
        result = wrapper.instance;
        Util.doAssert(null != result);

        if (alreadyCreated(lifecycleId)) {
            message = Util.getExceptionMessageString(
                Util.LIFECYCLE_ID_ALREADY_ADDED_ID,
                params);
            if (log.isErrorEnabled()) {
                log.error("Error: " + message);
            }
            throw new IllegalStateException(message);
        }

        if (!((FIRST_PHASE <= phaseId) &&
            (phaseId <= LAST_PHASE))) {
            params = new Object[]{Integer.toString(phaseId)};
            message = Util.getExceptionMessageString(
                Util.PHASE_ID_OUT_OF_BOUNDS_ERROR_MESSAGE_ID,
                params);
            if (log.isErrorEnabled()) {
                log.error("Error: " + message);
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
            if (log.isErrorEnabled()) {
                log.error("addLifecycle: " + message);
            }
            throw new IllegalArgumentException(message);
        }
        lifecycleMap.put(lifecycleId, new LifecycleWrapper(lifecycle, false));
        if (log.isDebugEnabled()) {
            log.debug("addedLifecycle: " + lifecycleId + " " + lifecycle);
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
            if (log.isErrorEnabled()) {
                log.error("LifecycleId " + lifecycleId + " does not exist");
            }
            throw new IllegalArgumentException(message);
        }

        wrapper = (LifecycleWrapper) lifecycleMap.get(lifecycleId);
        result = wrapper.instance;
        wrapper.created = true;

        if (log.isTraceEnabled()) {
            log.trace("getLifecycle: " + lifecycleId + " " + result);
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
