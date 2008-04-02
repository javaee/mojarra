/*
 * $Id: StateManagerWrapper.java,v 1.6 2005/08/22 22:07:51 ofung Exp $
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

package javax.faces.application;

import javax.faces.context.FacesContext;
import javax.faces.component.UIViewRoot;

import java.io.IOException;

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
public abstract class StateManagerWrapper extends StateManager {

    /**
     * @return the instance that we are wrapping.
     */ 

    abstract protected StateManager getWrapped();

    // ----------------------- Methods from javax.faces.application.StateManager


    /**
     * <p>The default behavior of this method is to
     * call {@link StateManager#saveSerializedView(javax.faces.context.FacesContext)}
     * on the wrapped {@link StateManager} object.</p>
     *
     * @see StateManager#saveSerializedView(javax.faces.context.FacesContext)
     * @since 1.2
     */
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
    public boolean isSavingStateInClient(FacesContext context) {

        return getWrapped().isSavingStateInClient(context);

    }
}
