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

package javax.faces.application;

import javax.faces.context.FacesContext;
import javax.faces.component.UIViewRoot;

import java.io.IOException;
import javax.faces.FacesWrapper;

/**
 * <p>Provides a simple implementation of {@link StateManager} that can
 * be subclassed by developers wishing to provide specialized behavior
 * to an existing {@link StateManager} instance.  The default
 * implementation of all methods is to call through to the wrapped
 * {@link StateManager}.</p>
 *
 * <p>Usage: extend this class and override {@link #getWrapped} to
 * return the instance we are wrapping.</p>
 *
 * @since 1.2
 */
public abstract class StateManagerWrapper extends StateManager implements FacesWrapper<StateManager> {

    /**
     * @return the instance that we are wrapping.
     */ 
    @Override
    public abstract StateManager getWrapped();

    // ----------------------- Methods from javax.faces.application.StateManager


    /**
     * <p>The default behavior of this method is to
     * call {@link StateManager#saveSerializedView(javax.faces.context.FacesContext)}
     * on the wrapped {@link StateManager} object.</p>
     *
     * @see StateManager#saveSerializedView(javax.faces.context.FacesContext)
     * @since 1.2
     */
    @Override
    public SerializedView saveSerializedView(FacesContext context) {

        return getWrapped().saveSerializedView(context);

    }


    /**
     * <p>The default behavior of this method is to
     * call {@link StateManager#saveView(javax.faces.context.FacesContext)}
     * on the wrapped {@link StateManager} object.</p>
     *
     * @see StateManager#saveView(javax.faces.context.FacesContext)
     * @since 1.2
     */
    @Override
    public Object saveView(FacesContext context) {
        return getWrapped().saveView(context);
    }


    /**
     * <p>The default behavior of this method is to
     * call {@link StateManager#getTreeStructureToSave(javax.faces.context.FacesContext)}
     * on the wrapped {@link StateManager} object.</p>
     *
     * @see StateManager#getTreeStructureToSave(javax.faces.context.FacesContext)
     * @since 1.2
     */
    @Override
    protected Object getTreeStructureToSave(FacesContext context) {

        return getWrapped().getTreeStructureToSave(context);

    }


    /**
     * <p>The default behavior of this method is to
     * call {@link StateManager#getComponentStateToSave(javax.faces.context.FacesContext)}
     * on the wrapped {@link StateManager} object.</p>
     *
     * @see StateManager#getComponentStateToSave(javax.faces.context.FacesContext)
     * @since 1.2
     */
    @Override
    protected Object getComponentStateToSave(FacesContext context) {

        return getWrapped().getComponentStateToSave(context);

    }

    /**
     * <p>The default behavior of this method is to call {@link
     * StateManager#writeState(javax.faces.context.FacesContext,
     * java.lang.Object)} on the wrapped {@link StateManager}
     * object.</p>
     *
     * @see StateManager#writeState(javax.faces.context.FacesContext,
     * java.lang.Object)
     * @since 1.2
     */
    @Override
    public void writeState(FacesContext context,
                           Object state)
    throws IOException {

        getWrapped().writeState(context, state);

    }




    /**
     * <p>The default behavior of this method is to
     * call {@link StateManager#writeState(javax.faces.context.FacesContext, javax.faces.application.StateManager.SerializedView)}
     * on the wrapped {@link StateManager} object.</p>
     *
     * @see StateManager#writeState(javax.faces.context.FacesContext, javax.faces.application.StateManager.SerializedView)
     * @since 1.2
     */
    @Override
    public void writeState(FacesContext context,
                           SerializedView state)
    throws IOException {

        getWrapped().writeState(context, state);

    }


    /**
     * <p>The default behavior of this method is to
     * call {@link StateManager#restoreView(javax.faces.context.FacesContext, String, String)}
     * on the wrapped {@link StateManager} object.</p>
     *
     * @see StateManager#restoreView(javax.faces.context.FacesContext, String, String)
     * @since 1.2
     */
    @Override
    public UIViewRoot restoreView(FacesContext context, String viewId,
                                  String renderKitId) {

        return getWrapped().restoreView(context, viewId, renderKitId);

    }


    /**
     * <p>The default behavior of this method is to
     * call {@link StateManager#restoreTreeStructure(javax.faces.context.FacesContext, String, String)}
     * on the wrapped {@link StateManager} object.</p>
     *
     * @see StateManager#restoreTreeStructure(javax.faces.context.FacesContext, String, String)
     * @since 1.2
     */
    @Override
    protected UIViewRoot restoreTreeStructure(FacesContext context,
                                              String viewId,
                                              String renderKitId) {

        return getWrapped().restoreTreeStructure(context, viewId, renderKitId);

    }


    /**
     * <p>The default behavior of this method is to
     * call {@link StateManager#restoreComponentState(javax.faces.context.FacesContext, javax.faces.component.UIViewRoot, String)}
     * on the wrapped {@link StateManager} object.</p>
     *
     * @see StateManager#restoreComponentState(javax.faces.context.FacesContext, javax.faces.component.UIViewRoot, String)
     * @since 1.2
     */
    @Override
    protected void restoreComponentState(FacesContext context,
                                         UIViewRoot viewRoot,
                                         String renderKitId) {

        getWrapped().restoreComponentState(context, viewRoot, renderKitId);

    }


    /**
     * <p>The default behavior of this method is to
     * call {@link StateManager#isSavingStateInClient(javax.faces.context.FacesContext)}
     * on the wrapped {@link StateManager} object.</p>
     *
     * @see StateManager#isSavingStateInClient(javax.faces.context.FacesContext) 
     * @since 1.2
     */
    @Override
    public boolean isSavingStateInClient(FacesContext context) {

        return getWrapped().isSavingStateInClient(context);

    }


    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link StateManager#getViewState(javax.faces.context.FacesContext)} on the
     * wrapped {@link StateManager} object.</p>
     *
     * @since 2.0
     */
    @Override
    public String getViewState(FacesContext context) {

        return getWrapped().getViewState(context);

    }
}
