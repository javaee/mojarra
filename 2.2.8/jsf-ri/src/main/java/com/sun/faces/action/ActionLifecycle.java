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
package com.sun.faces.action;

import com.sun.faces.lifecycle.Phase;
import com.sun.faces.lifecycle.RenderResponsePhase;
import java.util.Arrays;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseListener;
import javax.faces.lifecycle.Lifecycle;

public class ActionLifecycle extends Lifecycle {
    
    public static final String ACTION_LIFECYCLE = "com.sun.faces.action.ActionLifecycle";
    
    private final RenderResponsePhase renderResponsePhase;
    private Phase actionPhase;
    
    public ActionLifecycle() {
        renderResponsePhase = new RenderResponsePhase();
        try {
            Class phaseClass = Class.forName("com.sun.faces.action.ActionPhase");
            actionPhase = (Phase) phaseClass.newInstance();
        } catch(Throwable throwable) {  
            actionPhase = null;
        }
    }

    @Override
    public void addPhaseListener(PhaseListener listener) {
    }

    @Override
    public void execute(FacesContext context) throws FacesException {
        if (actionPhase != null) {
            actionPhase.doPhase(context, this, Arrays.asList(getPhaseListeners()).listIterator());
        } else {
            throw new FacesException("Unable to handle action");
        }
    }

    @Override
    public PhaseListener[] getPhaseListeners() {
        return new PhaseListener[0];
    }

    @Override
    public void removePhaseListener(PhaseListener listener) {
    }

    @Override
    public void render(FacesContext context) throws FacesException {
        renderResponsePhase.doPhase(context, this, Arrays.asList(getPhaseListeners()).listIterator());
    }
}
