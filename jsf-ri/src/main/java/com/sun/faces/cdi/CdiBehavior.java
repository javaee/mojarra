/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2015 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.faces.cdi;

import javax.faces.component.StateHolder;
import javax.faces.component.behavior.Behavior;
import javax.faces.context.FacesContext;
import javax.faces.event.BehaviorEvent;

/**
 * A delegate to the CDI managed behavior.
 */
class CdiBehavior implements Behavior, StateHolder {

    /**
     * Stores the behavior id.
     */
    private String behaviorId;

    /**
     * Stores the transient delegate.
     */
    private transient Behavior delegate;

    /**
     * Constructor.
     *
     * @param behaviorId the behavior id.
     * @param delegate the delegate.
     */
    public CdiBehavior(String behaviorId, Behavior delegate) {
        this.behaviorId = behaviorId;
        this.delegate = delegate;
    }

    /**
     * Broadcast the event.
     *
     * @param event the event.
     */
    @Override
    public void broadcast(BehaviorEvent event) {
        getDelegate(event.getFacesContext()).broadcast(event);
    }

    /**
     * Get the delegate.
     *
     * @param facesContext the Faces context.
     * @return the delegate.
     */
    private Behavior getDelegate(FacesContext facesContext) {
        if (delegate == null) {
            delegate = facesContext.getApplication().createBehavior(behaviorId);
        }
        return delegate;
    }

    /**
     * Are we transient?
     *
     * @return false.
     */
    @Override
    public boolean isTransient() {
        return false;
    }

    /**
     * Restore the state.
     *
     * @param facesContext the Faces context.
     * @param state the state.
     */
    @Override
    public void restoreState(FacesContext facesContext, Object state) {
        Object[] stateArray = (Object[]) state;
        behaviorId = (String) stateArray[0];
    }

    /**
     * Save the state.
     *
     * @param facesContext the Faces context.
     * @return the state.
     */
    @Override
    public Object saveState(FacesContext facesContext) {
        return new Object[]{behaviorId};
    }

    /**
     * Set the transient flag.
     *
     * <p>
     * Since our proxy is required to be non-transient we ignore any calls here.
     * </p>
     *
     * @param transientValue the transient value.
     */
    @Override
    public void setTransient(boolean transientValue) {
    }
}
