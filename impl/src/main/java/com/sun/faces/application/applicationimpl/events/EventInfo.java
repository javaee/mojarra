/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2017 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.java.net/public/CDDL+GPL_1_1.html
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
package com.sun.faces.application.applicationimpl.events;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.FacesException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

import com.sun.faces.util.FacesLogger;

/**
 * Represent a logical association between a SystemEvent and a Source. This call will contain the
 * Listeners specific to this association as well as provide a method to construct new SystemEvents
 * as required.
 */
public class EventInfo {

    private static final Logger LOGGER = FacesLogger.APPLICATION.getLogger();

    private Class<? extends SystemEvent> systemEvent;
    private Class<?> sourceClass;
    private Set<SystemEventListener> listeners;
    private Constructor eventConstructor;
    private Map<Class<?>, Constructor> constructorMap;

    // -------------------------------------------------------- Constructors

    public EventInfo(Class<? extends SystemEvent> systemEvent, Class<?> sourceClass) {

        this.systemEvent = systemEvent;
        this.sourceClass = sourceClass;
        this.listeners = new CopyOnWriteArraySet<>();
        this.constructorMap = new HashMap<>();
        if (!sourceClass.equals(Void.class)) {
            eventConstructor = getEventConstructor(sourceClass);
        }

    }

    // ------------------------------------------------------ Public Methods

    public Set<SystemEventListener> getListeners() {

        return listeners;

    }

    public SystemEvent createSystemEvent(Object source) {

        Constructor toInvoke = getCachedConstructor(source.getClass());
        if (toInvoke != null) {
            try {
                return (SystemEvent) toInvoke.newInstance(source);
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                throw new FacesException(e);
            }
        }
        return null;

    }

    // ----------------------------------------------------- Private Methods

    private Constructor getCachedConstructor(Class<?> source) {

        if (eventConstructor != null) {
            return eventConstructor;
        } else {
            Constructor c = constructorMap.get(source);
            if (c == null) {
                c = getEventConstructor(source);
                if (c != null) {
                    constructorMap.put(source, c);
                }
            }
            return c;
        }

    }

    private Constructor getEventConstructor(Class<?> source) {

        Constructor ctor = null;
        try {
            return systemEvent.getDeclaredConstructor(source);
        } catch (NoSuchMethodException ignored) {
            Constructor[] ctors = systemEvent.getConstructors();
            if (ctors != null) {
                for (Constructor c : ctors) {
                    Class<?>[] params = c.getParameterTypes();
                    if (params.length != 1) {
                        continue;
                    }
                    if (params[0].isAssignableFrom(source)) {
                        return c;
                    }
                }
            }
            if (eventConstructor == null && LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "Unable to find Constructor within {0} that accepts {1} instances.",
                        new Object[] { systemEvent.getName(), sourceClass.getName() });
            }
        }
        return ctor;

    }

}
