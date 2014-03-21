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

package com.sun.faces.renderkit;

import java.io.IOException;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.render.ResponseStateManager;
import com.sun.faces.util.RequestStateManager;


/**
 * <p>A <code>ResonseStateManager</code> implementation
 * for the default HTML render kit.
 */
public class ResponseStateManagerImpl extends ResponseStateManager {

    private StateHelper helper;

    public ResponseStateManagerImpl() {
    }


    // --------------------------------------- Methods from ResponseStateManager


    /**
     * @see ResponseStateManager#isPostback(javax.faces.context.FacesContext) 
     */
    @Override
    public boolean isPostback(FacesContext context) {

        return context.getExternalContext().getRequestParameterMap().
              containsKey(ResponseStateManager.VIEW_STATE_PARAM);

    }

    @Override
    public String getCryptographicallyStrongTokenFromSession(FacesContext context) {
        return getStateHelper(context).getCryptographicallyStrongTokenFromSession(context);
    }

    /**
     * @see ResponseStateManager#getState(javax.faces.context.FacesContext, java.lang.String) 
     */
    @Override
    public Object getState(FacesContext context, String viewId) {

        Object state =
              RequestStateManager.get(context, RequestStateManager.FACES_VIEW_STATE);
        if (state == null) {
            try {
                state = getStateHelper(context).getState(context, viewId);
                if (state != null) {
                    RequestStateManager.set(context,
                                            RequestStateManager.FACES_VIEW_STATE,
                                            state);
                }
            } catch (IOException e) {
                throw new FacesException(e);
            }
        }
        return state;

    }


    /**
     * @see ResponseStateManager#writeState(javax.faces.context.FacesContext, java.lang.Object) 
     */
    @Override
    public void writeState(FacesContext context, Object state) 
            throws IOException {

        getStateHelper(context).writeState(context, state, null);
    }


    /**
     * @see ResponseStateManager#getViewState(javax.faces.context.FacesContext, java.lang.Object) 
     */
    @Override
    public String getViewState(FacesContext context, Object state) {

        StringBuilder sb = new StringBuilder(32);        
        try {
            getStateHelper(context).writeState(context, state, sb);
        } catch (IOException e) {
            throw new FacesException(e);
        }
        return sb.toString();

    }


    @SuppressWarnings({"deprecation"})
    @Override
    public Object getTreeStructureToRestore(FacesContext context, String viewId) {

        Object[] state = (Object[]) getState(context, viewId);
        if (state != null) {
            return state[0];
        }
        return null;

    }

    /**
     * @param facesContext the Faces context.
     * @param viewId the view id.
     * @return true if "stateless" was found, false otherwise.
     * @throws IllegalStateException when the request is not a postback.
     */
    @Override
    public boolean isStateless(FacesContext facesContext, String viewId) {
        return getStateHelper(facesContext).isStateless(facesContext, viewId);
    }
    
    /**
     * Get the state helper.
     * 
     * @param context the Faces context.
     * @return the state helper.
     */
    private StateHelper getStateHelper(FacesContext context) {
        if (helper == null) {
            boolean clientStateSaving = context.getApplication().
                getStateManager().isSavingStateInClient(context);

            helper = clientStateSaving
               ? new ClientSideStateHelper()
               : new ServerSideStateHelper();
        }
        return helper;
    }
}
