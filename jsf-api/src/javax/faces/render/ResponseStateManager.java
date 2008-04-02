/*
 * $Id: ResponseStateManager.java,v 1.28 2006/09/01 01:22:24 tony_robertson Exp $
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

package javax.faces.render;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.application.StateManager;
import javax.faces.application.StateManager.SerializedView;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;




/**
 * <p><strong>ResponseStateManager</strong> is the helper class to
 * {@link javax.faces.application.StateManager} that knows the specific
 * rendering technology being used to generate the response.  It is a
 * singleton abstract class, vended by the {@link RenderKit}.  This
 * class knows the mechanics of saving state, whether it be in hidden
 * fields, session, or some combination of the two.</p>
 */

public abstract class ResponseStateManager {
    
    private static Logger log = Logger.getLogger("javax.faces.render");

    /**
     * <p>The name of the request parameter used by the default
     * implementation of {@link
     * javax.faces.application.ViewHandler#calculateRenderKitId} to
     * derive a RenderKit ID.</p>
     */
    public static final String RENDER_KIT_ID_PARAM =
            "javax.faces.RenderKitId";
            
    /**
     * <p>Implementations must use this value as the name and id of the client
     * parameter in which to save the state between requests.</p>
     *
     * @since 1.2
     */

    public static final String VIEW_STATE_PARAM = "javax.faces.ViewState";

    /*       
     * <p>Take the argument <code>state</code> and write it into the
     * output using the current {@link ResponseWriter}, which must be
     * correctly positioned already.</p>
     *
     * <p>If the state is to be written out to hidden fields, the
     * implementation must take care to make all necessary character
     * replacements to make the Strings suitable for inclusion as an
     * HTTP request paramater.</p>
     *
     * <p>If the state saving method for this application is {@link
     * javax.faces.application.StateManager#STATE_SAVING_METHOD_CLIENT},
     * the implementation may encrypt the state to be saved to the
     * client.  We recommend that the state be unreadable by the client,
     * and also be tamper evident.  The reference implementation follows
     * these recommendations.  </p>
     *
     * <p>Write out the render kit identifier associated with this 
     * <code>ResponseStateManager</code> implementation with the name
     * as the value of the <code>String</code> constant 
     * <code>ResponseStateManager.RENDER_KIT_ID_PARAM</code>.  The  
     * render kit identifier must not be written if:</p>
     * <ul>
     * <li>it is the default render kit identifier as returned by 
     * {@link Application#getDefaultRenderKitId()} or</li>
     * <li>the render kit identfier is the value of 
     * <code>RenderKitFactory.HTML_BASIC_RENDER_KIT</code> and 
     * {@link Application.getDefaultRenderKitId()} returns <code>null</code>.
     * </li>
     * </ul> 
     *
     * <p>For backwards compatability with existing
     * <code>ResponseStateManager</code> implementations, the default
     * implementation of this method checks if the argument is an
     * instance of <code>SerializedView</code>.  If so, it calls through
     * to {@link
     * #writeState(javax.faces.context.FacesContext,javax.faces.application.StateManager.SerializedView}.
     * If not, it expects the state to be a two element Object array.  It creates 
     * an instance of <code>SerializedView</code> and
     * stores the state as the treeStructure, and passes it to {@link
     * #writeState(javax.faces.context.FacesContext,javax.faces.application.StateManager.SerializedView}.</p>
     *
     *
     * @since 1.2
     *
     * @param context The {@link FacesContext} instance for the current request
     * @param state The serialized state information previously saved
     * @throws IOException if the state argument is not an array of length 2.
     *
     */
    public void writeState(FacesContext context,
        Object state) throws IOException {
        
	SerializedView view = null;
	if (state instanceof SerializedView) {
	    view = (SerializedView) state;
	}
	else {
            if (state instanceof Object[]) {
                Object[] stateArray = (Object[])state;
                if (2 == stateArray.length) {
	            StateManager stateManager = 
		        context.getApplication().getStateManager();
	            view = stateManager.new SerializedView(stateArray[0], 
                        stateArray[1]);
                } else {
                    //PENDING - I18N
                    if (log.isLoggable(Level.SEVERE)) {
                        log.log(Level.SEVERE, "State is not an expected array of length 2.");
                    }
                    throw new IOException("State is not an expected array of length 2.");
                }
            } else {
                //PENDING - I18N
                if (log.isLoggable(Level.SEVERE)) {
                    log.log(Level.SEVERE, "State is not an expected array of length 2.");
                }
                throw new IOException("State is not an expected array of length 2.");
            }
	}
	writeState(context, view);
    }

    /**
     * <p>Take the argument <code>state</code> and write it into
     * the output using the current {@link ResponseWriter}, which
     * must be correctly positioned already.</p>
     *
     * <p>If the {@link
     * javax.faces.application.StateManager.SerializedView} is to be
     * written out to hidden fields, the implementation must take care
     * to make all necessary character replacements to make the Strings
     * suitable for inclusion as an HTTP request paramater.</p>
     *
     * <p>If the state saving method for this application is {@link
     * javax.faces.application.StateManager#STATE_SAVING_METHOD_CLIENT},
     * the implementation may encrypt the state to be saved to the
     * client.  We recommend that the state be unreadable by the client,
     * and also be tamper evident.  The reference implementation follows
     * these recommendations.  </p>
     *
     * @deprecated This method has been replaced by {@link
     * #writeState(javax.faces.context.FacesContext,java.lang.Object)}.
     * The default implementation of this method does nothing.
     * 
     * @param context The {@link FacesContext} instance for the current request
     * @param state The serialized state information previously saved
     *
     */
    public void writeState(FacesContext context,
			   SerializedView state) throws IOException {
    }

    /**
     * <p>The implementation must inspect the current request and return
     * an Object representing the tree structure and component state
     * passed in to a previous invocation of {@link
     * #writeState(javax.faces.context.FacesContext,java.lang.Object)}.</p>
     *
     * <p>For backwards compatability with existing
     * <code>ResponseStateManager</code> implementations, the default
     * implementation of this method calls {@link
     * #getTreeStructureToRestore} and {@link
     * #getComponentStateToRestore} and creates and returns a two
     * element <code>Object</code> array with element zero containing
     * the <code>structure</code> property and element one containing
     * the <code>state</code> property of the
     * <code>SerializedView</code>.</p>
     *
     * @since 1.2
     *
     * @param context The {@link FacesContext} instance for the current request
     * @param viewId View identifier of the view to be restored
     *
     * @return the tree structure and component state Object passed in
     * to <code>writeState</code>.  If this is an initial request, this
     * method returns <code>null</code>.
     */
 
    public Object getState(FacesContext context, String viewId) {
	Object stateArray[] = { getTreeStructureToRestore(context, viewId),
				getComponentStateToRestore(context) };
	return stateArray;
    }


    /**
     * <p>The implementation must inspect the current request and return
     * the tree structure Object passed to it on a previous invocation of
     * <code>writeState()</code>.</p>
     *
     * @deprecated This method has been replaced by {@link #getState}.
     * The default implementation returns <code>null</code>.
     *
     * @param context The {@link FacesContext} instance for the current request
     * @param viewId View identifier of the view to be restored
     *
     */
    public Object getTreeStructureToRestore(FacesContext context, 
					    String viewId) {
	return null;
    }


    /**
     * <p>The implementation must inspect the current request and return
     * the component state Object passed to it on a previous invocation
     * of <code>writeState()</code>.</p>
     *
     * @deprecated This method has been replaced by {@link #getState}.
     * The default implementation returns <code>null</code>.
     *
     * @param context The {@link FacesContext} instance for the current request
     *
     */
    public Object getComponentStateToRestore(FacesContext context) {
	return null;
    }

    /**
     * <p>Return true if the current request is a postback.  This method
     * is leveraged from the <i>Restore View Phase</i> to determine if
     * {@link javax.faces.application.ViewHandler#restoreView} or {@link
     * javax.faces.application.ViewHandler#createView} should be called.
     * The default implementation must return <code>true</code> if this
     * <code>ResponseStateManager</code> instance wrote out state on a
     * previous request to which this request is a postback,
     * <code>false</code> otherwise.</p>
     *
     * <p>The implementation if this method for the Standard HTML
     * RenderKit must consult the {@link
     * javax.faces.context.ExternalContext}'s
     * <code>requestParameterMap</code> and return <code>true</code> if
     * and only if there is a key equal to the value of the symbolic
     * constant {@link #VIEW_STATE_PARAM}.</p>
     *
     * <p>For backwards compatability with implementations of
     * <code>ResponseStateManager</code> prior to JSF 1.2, a default
     * implementation is provided that consults the {@link
     * javax.faces.context.ExternalContext}'s <code>requestParameterMap</code> and return
     * <code>true</code> if its size is greater than 0.</p>
     *
     * @since 1.2
     */

    public boolean isPostback(FacesContext context) {
        return (0 < context.getExternalContext().getRequestParameterMap().size());
    }


}
