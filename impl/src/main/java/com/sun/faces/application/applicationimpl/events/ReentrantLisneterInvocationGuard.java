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

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import java.util.HashMap;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.event.SystemEvent;

public class ReentrantLisneterInvocationGuard {

    public boolean isGuardSet(FacesContext ctx, Class<? extends SystemEvent> systemEventClass) {
        Boolean result;
        Map<Class<? extends SystemEvent>, Boolean> data = getDataStructure(ctx);
        result = data.get(systemEventClass);

        return null == result ? false : result;
    }

    public void setGuard(FacesContext ctx, Class<? extends SystemEvent> systemEventClass) {
        Map<Class<? extends SystemEvent>, Boolean> data = getDataStructure(ctx);
        data.put(systemEventClass, TRUE);

    }

    public void clearGuard(FacesContext ctx, Class<? extends SystemEvent> systemEventClass) {
        Map<Class<? extends SystemEvent>, Boolean> data = getDataStructure(ctx);
        data.put(systemEventClass, FALSE);

    }

    private Map<Class<? extends SystemEvent>, Boolean> getDataStructure(FacesContext ctx) {
        Map<Class<? extends SystemEvent>, Boolean> result = null;
        Map<Object, Object> ctxMap = ctx.getAttributes();
        final String IS_PROCESSING_LISTENERS_KEY = "com.sun.faces.application.ApplicationImpl.IS_PROCESSING_LISTENERS";

        if (null == (result = (Map<Class<? extends SystemEvent>, Boolean>) ctxMap.get(IS_PROCESSING_LISTENERS_KEY))) {
            result = new HashMap<>(12);
            ctxMap.put(IS_PROCESSING_LISTENERS_KEY, result);
        }

        return result;
    }

}