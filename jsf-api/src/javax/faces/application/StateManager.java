/*
 * $Id: StateManager.java,v 1.33 2004/02/26 20:30:25 eburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.application;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.component.NamingContainer;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.render.RenderKit;
import javax.faces.render.ResponseStateManager;

import java.io.IOException;
import java.io.Writer;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;



/**
 * <p><strong>StateManager</strong> directs the process of saving and
 * restoring the view between requests.  The {@link StateManager}
 * instance for an application is retrieved from the {@link Application}
 * instance, and therefore cannot know any details of the markup language
 * created by the {@link RenderKit} being used to render a view.
 * Therefore, the {@link StateManager} utilizes a helper object
 * ({@link ResponseStateManager}), that is provided by the {@link RenderKit}
 * implementation and is therefore aware of the markup language details.</p>
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
     * @param context {@link FacesContext} for the current request
     *
     * @exception IllegalStateException if more than one component or
     * facet within the same {@link NamingContainer} in this view has
     * the same non-<code>null</code> component id
     */
    public abstract SerializedView saveSerializedView(FacesContext context);


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
     * <p>PENDING(craigmcc) - Does this method need to be in the
     * public APIs?</p>
     *
     * @param context {@link FacesContext} for the current request
     */
    protected abstract Object getTreeStructureToSave(FacesContext context);


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
     * <p>PENDING(craigmcc) - Does this method need to be in the
     * public APIs?</p>
     *
     * @param context {@link FacesContext} for the current request
     */
    protected abstract Object getComponentStateToSave(FacesContext context);


    /**
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
     * <p>If the init parameter indicates that server side state
     * saving should be used, this method must save the state in
     * such a manner that it may be retrieved using only the
     * <code>viewId</code>.</p>
     *
     * @param context {@link FacesContext} for the current request
     * @param state the serialized state to be written
     */
    public abstract void writeState(FacesContext context,
				    SerializedView state) throws IOException;


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
     * @exception IllegalArgumentException if <code>renderKitId</code>
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
     * <p>PENDING(craigmcc) - Does this method need to be in the
     * public APIs?</p>
     *
     * @param context {@link FacesContext} for the current request
     * @param viewId View identifier of the view to be restored
     * @param renderKitId the renderKitId used to render this response.
     * Must not be <code>null</code>.
     *
     * @exception IllegalArgumentException if <code>renderKitId</code>
     * is <code>null</code>.
     */
    protected abstract UIViewRoot restoreTreeStructure
	(FacesContext context, String viewId, String renderKitId);


    /**
     * <p>Convenience method, which must be called by
     * <code>restoreView()</code>, to restore the attributes, properties,
     * and attached objects of all components in the restored component tree.
     * </p>
     *
     * <p>PENDING(craigmcc) - Does this method need to be in the
     * public APIs?</p>
     *
     * @param context {@link FacesContext} for the current request
     * @param viewRoot {@link UIViewRoot} returned by a previous call
     *  to <code>restoreTreeStructure()</code>
     * @param renderKitId the renderKitId used to render this response.
     * Must not be <code>null</code>.
     *
     * @exception IllegalArgumentException if <code>renderKitId</code>
     * is <code>null</code>.
     */
    protected abstract void restoreComponentState
	(FacesContext context, UIViewRoot viewRoot, String renderKitId);








    private Boolean savingStateInClient = null;

    /**
     * @return <code>true</code> if and only if the value of the
     * <code>ServletContext</code> init parameter named by the value of
     * the constant {@link #STATE_SAVING_METHOD_PARAM_NAME} is equal to
     * the value of the constant {@link #STATE_SAVING_METHOD_CLIENT}.
     * <code>false</code> otherwise.
     *
     * @exception NullPointerException if <code>context</code> is
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
