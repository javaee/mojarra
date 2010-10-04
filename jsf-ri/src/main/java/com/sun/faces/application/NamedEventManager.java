/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.faces.application;

import com.sun.faces.util.Util;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.faces.FacesException;
import javax.faces.event.PostAddToViewEvent;
import javax.faces.event.PostValidateEvent;
import javax.faces.event.PreValidateEvent;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.PreRenderComponentEvent;
import javax.faces.event.PreRenderViewEvent;
import javax.faces.event.SystemEvent;

/**
 * Note: New, relevant spec'd ComponentSystemEvents must be added to the constructor
 */
public class NamedEventManager {

    private Map<String, Class<? extends SystemEvent>> namedEvents =
            new ConcurrentHashMap<String, Class<? extends SystemEvent>>();
    private Map<String, Set<Class<? extends SystemEvent>>> duplicateNames =
             new ConcurrentHashMap<String, Set<Class<? extends SystemEvent>>>();

    public NamedEventManager() {
        namedEvents.put("javax.faces.event.PreRenderComponent", PreRenderComponentEvent.class);
        namedEvents.put("javax.faces.event.PreRenderView", PreRenderViewEvent.class);
        namedEvents.put("javax.faces.event.PostAddToView", PostAddToViewEvent.class);
        namedEvents.put("javax.faces.event.PreValidate", PreValidateEvent.class);
        namedEvents.put("javax.faces.event.PostValidate", PostValidateEvent.class);
        namedEvents.put("preRenderComponent", PreRenderComponentEvent.class);
        namedEvents.put("preRenderView", PreRenderViewEvent.class);
        namedEvents.put("postAddToView", PostAddToViewEvent.class);
        namedEvents.put("preValidate", PreValidateEvent.class);
        namedEvents.put("postValidate", PostValidateEvent.class);
    }

    public void addNamedEvent(String name, Class<? extends SystemEvent> event) {
        namedEvents.put(name, event);
    }

    public Class<? extends SystemEvent> getNamedEvent(String name) {
        Class<? extends SystemEvent> namedEvent = namedEvents.get(name);
        if (namedEvent == null) {
            try {
                namedEvent = Util.loadClass(name, this);
            } catch (ClassNotFoundException ex) {
                throw new FacesException ("An unknown event type was specified:  " + name, ex);
            }
        }
        if (!ComponentSystemEvent.class.isAssignableFrom(namedEvent)) {
                throw new ClassCastException();
        }

        return namedEvent;
    }

    public void addDuplicateName(String name, Class<? extends SystemEvent> event) {
        Class<? extends SystemEvent> registeredEvent = namedEvents.remove(name);
        Set<Class<? extends SystemEvent>> events = duplicateNames.get(name);
        if (events == null) {
            events = new HashSet<Class<? extends SystemEvent>>();
            duplicateNames.put(name, events);
        }
        events.add(event);
        if (registeredEvent != null) {
            events.add(registeredEvent);
        }
    }

    public boolean isDuplicateNamedEvent(String name) {
        return (namedEvents.get(name) != null) || (duplicateNames.get(name) != null);
    }
}
