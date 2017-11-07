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

package com.sun.faces.application.applicationimpl.events;

import javax.faces.event.SystemEvent;

import com.sun.faces.util.Cache;
import com.sun.faces.util.Cache.Factory;

/**
 * Utility class for dealing with {@link javax.faces.component.UIComponent} events.
 */
public class ComponentSystemEventHelper {

    private Cache<Class<?>, Cache<Class<? extends SystemEvent>, EventInfo>> sourceCache;

    // -------------------------------------------------------- Constructors

    public ComponentSystemEventHelper() {

        // Initialize the 'sources' cache for, ahem, readability...
        // ~generics++
        Factory<Class<?>, Cache<Class<? extends SystemEvent>, EventInfo>> eventCacheFactory = new Factory<Class<?>, Cache<Class<? extends SystemEvent>, EventInfo>>() {
            @Override
            public Cache<Class<? extends SystemEvent>, EventInfo> newInstance(final Class<?> sourceClass) throws InterruptedException {
                Factory<Class<? extends SystemEvent>, EventInfo> eventInfoFactory = new Factory<Class<? extends SystemEvent>, EventInfo>() {
                    @Override
                    public EventInfo newInstance(final Class<? extends SystemEvent> systemEventClass) throws InterruptedException {
                        return new EventInfo(systemEventClass, sourceClass);
                    }
                };
                return new Cache<>(eventInfoFactory);
            }
        };
        sourceCache = new Cache<>(eventCacheFactory);

    }

    // ------------------------------------------------------ Public Methods

    public EventInfo getEventInfo(Class<? extends SystemEvent> systemEvent, Class<?> sourceClass) {

        Cache<Class<? extends SystemEvent>, EventInfo> eventsCache = sourceCache.get(sourceClass);
        return eventsCache.get(systemEvent);

    }

}
