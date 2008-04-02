/*
 * $Id: StateManager.java,v 1.39 2005/12/05 16:42:42 edburns Exp $
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

import javax.faces.component.NamingContainer;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.faces.render.ResponseStateManager;

import java.io.IOException;



/**
 * <p><strong>StateManager</strong> directs the process of saving and
 * restoring the view between requests.  The {@link StateManager}
 * instance for an application is retrieved from the {@link Application}
 * instance, and thus cannot know any details of the markup language
 * created by the {@link RenderKit} being used to render a view.  The
 * {@link StateManager} utilizes a helper object ({@link
 * ResponseStateManager}), that is provided by the {@link RenderKit}
 * implementation and is therefore aware of the markup language
 * details.</p>
 */

public abstract class StateManager {


    // ------------------------------------------------------ Manifest Constants


    /**
     * <p>The <code>ServletContext</code> init parameter consulted by
     * the <code>StateManager</code> to tell where the state should be
     * saved.  Valid values are given as the values of the constants:
     * {@link #STATE_SAVING_METHOD_CLIENT} or {@link
     * #STATE_SAVING_METHOD_SERVER}.</p>
     *
     * <p>If this parameter is not specified, the default value is the
     * value of the constant {@link #STATE_SAVING_METHOD_CLIENT}. </p>
     */
    public static final String STATE_SAVING_METHOD_PARAM_NAME = 
	"javax.faces.STATE_SAVING_METHOD";


    /**
     * <p>Constant value for the initialization parameter named by
     * the <code>STATE_SAVING_METHOD_PARAM_NAME</code> that indicates
     * state saving should take place on the client.</p>
     */
    public static final String STATE_SAVING_METHOD_CLIENT = "client";


    /**
     * <p>Constant value for the initialization parameter named by
     * the <code>STATE_SAVING_METHOD_PARAM_NAME</code> that indicates
     * state saving should take place on the server.</p>
     */
    public static final String STATE_SAVING_METHOD_SERVER = "server";


    // ---------------------------------------------------- State Saving Methods


    /**
     * <p>Return the tree structure and component state information for the
     * view contained in the specified {@link FacesContext} instance as an
     * object of type <code>StateManager.SerializedView</code>.  If there
     * is no state information to be saved, return <code>null</code>
     * instead.</p>
     *
     * <p>Components may opt out of being included in the serialized view
     * by setting their <code>transient</code> property to <code>true</code>.
     * This must cause the component itself, as well as all of that component's
     * children and facets, to be omitted from the saved  tree structure
     * and component state information.</p>
     *
     * <p>This method must also enforce the rule that, for components with
     * non-null <code>id</code>s, all components that are descendants of the
     * same nearest {@link NamingContainer} must have unique identifiers.</p>
     *
     * @deprecated this has been replaced by {@link #saveView}.  The
     * default implementation returns <code>null</code>.
     *
     * @param context {@link FacesContext} for the current request
     *
     * @throws IllegalStateException if more than one component or
     * facet within the same {@link NamingContainer} in this view has
     * the same non-<code>null</code> component id
     */
    public SerializedView saveSerializedView(FacesContext context) {
	return null;
    }

    /**
     * <p>Return an opaque <code>Object</code> containing sufficient
     * information for this same instance to restore the state of the
     * current {@link UIViewRoot} on a subsequent request.  The returned
     * object must implement <code>java.io.Serializable</code>. If there
     * is no state information to be saved, return <code>null</code>
     * instead.</p>
     *
     * <p>Components may opt out of being included in the serialized view
     * by setting their <code>transient</code> property to <code>true</code>.
     * This must cause the component itself, as well as all of that component's
     * children and facets, to be omitted from the saved  tree structure
     * and component state information.</p>
     *
     * <p>This method must also enforce the rule that, for components with
     * non-null <code>id</code>s, all components that are descendants of the
     * same nearest {@link NamingContainer} must have unique identifiers.</p>
     *
     * <p>For backwards compatability with existing
     * <code>StateManager</code> implementations, the default
     * implementation of this method calls {@link #saveSerializedView}
     * and creates and returns a two element <code>Object</code> array
     * with element zero containing the <code>structure</code> property
     * and element one containing the <code>state</code> property of the
     * <code>SerializedView</code>.</p>
     *
     * @since 1.2
     *
     * @param context {@link FacesContext} for the current request
     *
     * @throws IllegalStateException if more than one component or
     * facet within the same {@link NamingContainer} in this view has
     * the same non-<code>null</code> component id
     */
    public Object saveView(FacesContext context) {
	SerializedView view = saveSerializedView(context);
	Object stateArray[] = { view.getStructure(),
				view.getState() };
	return stateArray;
    }


    /**
     * <p>Convenience method, which must be called by
     * <code>saveSerializedView()</code>, to construct and return a
     * <code>Serializable</code> object that represents the structure
     * of the entire component tree (including children and facets)
     * of this view.</p>
     *
     * <p>Components may opt-out of being included in the tree structure
     * by setting their <code>transient</code> property to <code>true</code>.
     * This must cause the component itself, as well as all of that component's
     * children and facets, to be omitted from the saved  tree structure
     * information.</p>
     *
     * @deprecated the distinction between tree structure and component
     * state is now an implementation detail.  The default
     * implementation returns <code>null</code>.
     *
     * @param context {@link FacesContext} for the current request
     */
    protected Object getTreeStructureToSave(FacesContext context) {
	return null;
    }


    /**
     * <p>Convenience method, which must be called by
     * <code>saveSerializedView()</code>, to construct and return a
     * <code>Serializable</code> object that represents the state of
     * all component properties, attributes, and attached objects, for
     * the entire component tree (including children and facets)
     * of this view.</p>
     *
     * <p>Components may opt-out of being included in the component state
     * by setting their <code>transient</code> property to <code>true</code>.
     * This must cause the component itself, as well as all of that component's
     * children and facets, to be omitted from the saved component state
     * information.</p>
     *
     * @deprecated the distinction between tree structure and component
     * state is now an implementation detail.  The default
     * implementation returns <code>null</code>.
     *
     * @param context {@link FacesContext} for the current request
     */
    protected Object getComponentStateToSave(FacesContext context) {
	return null;
    }

    /**
     * <p>Save the state represented in the specified state
     * <code>Object</code> instance, in an implementation dependent
     * manner.</p>
     *
     * <p>This method will typically simply delegate the actual
     * writing to the <code>writeState()</code> method of the
     * {@link ResponseStateManager} instance provided by the
     * {@link RenderKit} being used to render this view.  This
     * method assumes that the caller has positioned the
     * {@link ResponseWriter} at the correct position for the
     * saved state to be written.</p>
     *
     * <p>For backwards compatability with existing
     * <code>StateManager</code> implementations, the default
     * implementation of this method checks if the argument is an
     * instance of <code>Object []</code> of length greater than or
     * equal to two.  If so, it creates a <code>SerializedView</code>
     * instance with the tree structure coming from element zero and
     * the component state coming from element one and calls through to
     * {@link
     * #writeState(javax.faces.context.FacesContext,javax.faces.application.StateManager.SerializedView)}.
     * If not, does nothing.</p>
     *
     * @since 1.2
     *
     * @param context {@link FacesContext} for the current request
     * @param state the Serializable state to be written,
     *   as returned by {@link #saveSerializedView}
     */
    public void writeState(FacesContext context, Object state) throws IOException {
	if (null != state && state.getClass().isArray() && 
	    state.getClass().getComponentType().equals(Object.class)) {
	    Object stateArray [] = (Object []) state;
	    if (2 == stateArray.length) {
		SerializedView view = new SerializedView(stateArray[0], 
							 stateArray[1]);
		writeState(context, view);
	    }
	}
    }

    /**
     *
     * <p>Save the state represented in the specified
     * <code>SerializedView</code> isntance, in an implementation
     * dependent manner.</p>
     *
     * <p>This method must consult the context initialization parameter
     * named by the symbolic constant
     * <code>StateManager.STATE_SAVING_METHOD_PARAMETER_NAME</code>
     * to determine whether state should be saved on the client or the
     * server.  If not present, client side state saving is assumed.</p>
     *
     * <p>If the init parameter indicates that client side state
     * saving should be used, this method must delegate the actual
     * writing to the <code>writeState()</code> method of the
     * {@link ResponseStateManager} instance provided by the
     * {@link RenderKit} being used to render this view.  This
     * method assumes that the caller has positioned the
     * {@link ResponseWriter} at the correct position for the
     * saved state to be written.</p>
     *
     * @deprecated This method has been replaced by {@link
     * #writeState(javax.faces.context.FacesContext,java.lang.Object)}.
     * The default implementation of this method does nothing.
     *
     * @param context {@link FacesContext} for the current request
     * @param state the serialized state to be written
     */
    public void writeState(FacesContext context,
			   SerializedView state) throws IOException {
    }


    // ------------------------------------------------- State Restoring Methods


    /**
     * <p>Restore the tree structure and the component state of the view
     * for the specified <code>viewId</code>, in an implementation dependent
     * manner, and return the restored {@link UIViewRoot}.  If there is no
     * saved state information available for this <code>viewId</code>,
     * return <code>null</code> instead.</p>
     *
     * <p>This method must consult the context initialization parameter
     * named by the symbolic constant
     * <code>StateManager.STATE_SAVING_METHOD_PARAMETER_NAME</code>
     * to determine whether state should be saved on the client or the
     * server.  If not present, client side state saving is assumed.</p>
     *
     * <p>If the init parameter indicates that client side state
     * saving should be used, this method must call the
     * <code>getTreeStructureToRestore()</code> and (if the previous method
     * call returned a non-null value) <code>getComponentStateToRestore()</code>
     * methods of the {@link ResponseStateManager} instance provided by the
     * {@link RenderKit} responsible for this view.</p>
     *
     * @param context {@link FacesContext} for the current request
     * @param viewId View identifier of the view to be restored
     * @param renderKitId the renderKitId used to render this response.
     * Must not be <code>null</code>.
     *
     * @throws IllegalArgumentException if <code>renderKitId</code>
     * is <code>null</code>.
     */
    public abstract UIViewRoot restoreView(FacesContext context, String viewId,
					   String renderKitId);


    /**
     * <p>Convenience method, which must be called by
     * <code>restoreView()</code>, to construct and return a {@link UIViewRoot}
     * instance (populated with children and facets) representing the
     * tree structure of the component tree being restored.  If no saved
     * state information is available, return <code>null</code> instead.</p>
     *
     * @param context {@link FacesContext} for the current request
     * @param viewId View identifier of the view to be restored
     * @param renderKitId the renderKitId used to render this response.
     * Must not be <code>null</code>.
     *
     * @deprecated the distinction between tree structure and component
     * state is now an implementation detail.  The default
     * implementation returns <code>null</code>.
     *
     * @throws IllegalArgumentException if <code>renderKitId</code>
     * is <code>null</code>.
     */
    protected UIViewRoot restoreTreeStructure(FacesContext context, 
					      String viewId, 
					      String renderKitId) {
	return null;
    }


    /**
     * <p>Convenience method, which must be called by
     * <code>restoreView()</code>, to restore the attributes, properties,
     * and attached objects of all components in the restored component tree.
     * </p>    
     *
     * @param context {@link FacesContext} for the current request
     * @param viewRoot {@link UIViewRoot} returned by a previous call
     *  to <code>restoreTreeStructure()</code>
     * @param renderKitId the renderKitId used to render this response.
     * Must not be <code>null</code>.
     *
     * @deprecated the distinction between tree structure and component
     * state is now an implementation detail.  The default
     * implementation does nothing.
     *
     * @throws IllegalArgumentException if <code>renderKitId</code>
     * is <code>null</code>.
     */
    protected void restoreComponentState(FacesContext context, 
					 UIViewRoot viewRoot, 
					 String renderKitId) {
    }








    private Boolean savingStateInClient = null;

    /**
     * @return <code>true</code> if and only if the value of the
     * <code>ServletContext</code> init parameter named by the value of
     * the constant {@link #STATE_SAVING_METHOD_PARAM_NAME} is equal to
     * the value of the constant {@link #STATE_SAVING_METHOD_CLIENT}.
     * <code>false</code> otherwise.
     *
     * @throws NullPointerException if <code>context</code> is
     * <code>null</code>.
     */

    public boolean isSavingStateInClient(FacesContext context) {
	if (null != savingStateInClient) {
	    return savingStateInClient.booleanValue();
	}
	savingStateInClient = Boolean.FALSE;

        String saveStateParam = context.getExternalContext().
            getInitParameter(STATE_SAVING_METHOD_PARAM_NAME);
        if (saveStateParam != null && 
           saveStateParam.equalsIgnoreCase(STATE_SAVING_METHOD_CLIENT)){
	    savingStateInClient = Boolean.TRUE;
        }
	return savingStateInClient.booleanValue();
    }

    /**
     *
     * <p>Convenience struct for encapsulating tree structure and
     * component state.  This is necessary to allow the API to be
     * flexible enough to work in JSP and non-JSP environments.</p>
     *
     * @deprecated This class was not marked <code>Serializable</code>
     * in the 1.0 version of the spec.  It was also not a static inner
     * class, so it can't be made to be <code>Serializable</code>.
     * Therefore, it is being deprecated in version 1.2 of the spec.
     * The replacement is to use an implementation dependent
     * <code>Object</code>.
     *
     */

    public class SerializedView extends Object {
	private Object structure = null;
	private Object state = null;

	public SerializedView(Object newStructure, Object newState) {
	    structure = newStructure;
	    state = newState;
	}

	public Object getStructure() {
	    return structure;
	}

	public Object getState() {
	    return state;
	}
    }

}
