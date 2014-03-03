/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2014 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
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

import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.MessageUtils;
import javax.faces.FacesException;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;

/**
 * <B>LifecycleFactoryImpl</B> is the stock implementation of Lifecycle
 * in the JSF RI. <P>
 *
 * @see	javax.faces.lifecycle.LifecycleFactory
 */

public class LifecycleFactoryImpl extends LifecycleFactory {


    // Log instance for this class
    private static Logger LOGGER = FacesLogger.LIFECYCLE.getLogger();

    protected ConcurrentHashMap<String,Lifecycle> lifecycleMap = null;


    // ------------------------------------------------------------ Constructors


    public LifecycleFactoryImpl() {
        super();
        lifecycleMap = new ConcurrentHashMap<String,Lifecycle>();

        // We must have an implementation under this key.
        lifecycleMap.put(LifecycleFactory.DEFAULT_LIFECYCLE,
                         new LifecycleImpl(FacesContext.getCurrentInstance()));
//        lifecycleMap.put(ActionLifecycle.ACTION_LIFECYCLE,
//                         new ActionLifecycle());
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Created Default Lifecycle");
        }
    }


    // -------------------------------------------------- Methods from Lifecycle


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
        lifecycleMap.put(lifecycleId, lifecycle);
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("addedLifecycle: " + lifecycleId + " " + lifecycle);
        }
    }


    public Lifecycle getLifecycle(String lifecycleId) throws FacesException {

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
            throw new IllegalArgumentException(message);
        }

        Lifecycle result = lifecycleMap.get(lifecycleId);

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("getLifecycle: " + lifecycleId + " " + result);
        }
        return result;
    }


    public Iterator<String> getLifecycleIds() {
        return lifecycleMap.keySet().iterator();
    }



// The testcase for this class is TestLifecycleFactoryImpl.java 


} // end of class LifecycleFactoryImpl
