/*
 * $Id: ResponseStateManager.java,v 1.22 2005/05/02 19:27:07 edburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.render;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.application.StateManager;
import javax.faces.application.StateManager.SerializedView;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;



/**
 * <p><strong>ResponseStateManager</strong> is the helper class to
 * {@link javax.faces.application.StateManager} that knows the specific
 * rendering technology being used to generate the response.  It is a
 * singleton abstract class, vended by the {@link RenderKit}.  This
 * class knows the mechanics of saving state, whether it be in hidden
 * fields, session, or some combination of the two.</p>
 */

public abstract class ResponseStateManager {

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
     * If not, it creates an instance of <code>SerializedView</code> and
     * stores the state as the treeStructure, and passes it to {@link
     * #writeState(javax.faces.context.FacesContext,javax.faces.application.StateManager.SerializedView}.</p>
     *
     *
     * @since 1.2
     *
     * @param context The {@link FacesContext} instance for the current request
     * @param state The serialized state information previously saved
     *
     */
    public void writeState(FacesContext context,
			   Object state) throws IOException {
	SerializedView view = null;
	if (state instanceof SerializedView) {
	    view = (SerializedView) state;
	}
	else {
	    StateManager stateManager = 
		context.getApplication().getStateManager();
	    view = stateManager.new SerializedView(state, null);
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
     */

    public abstract boolean isPostback(FacesContext context);


}
