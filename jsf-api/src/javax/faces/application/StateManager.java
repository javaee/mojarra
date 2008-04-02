/*
 * $Id: StateManager.java,v 1.17 2003/09/15 23:18:49 jvisvanathan Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.application;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;

import java.io.IOException;
import java.io.Writer;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;



/**
 * <p><strong>StateManager</strong> directs the process of saving and
 * restoring the view between requests.  StateManager is retrieved from
 * the {@link ViewHandler}, and therefore, it knows the kind of
 * rendering technology being used, for example JSPs or XML, or Java,
 * etc.  But it shouldn't know whether JSPs are being used to to render
 * HTML or XHTML or WML, etc.  Thus it can't know the mechanism of how
 * to save state (hidden fields, etc.).  The StateManager uses a helper
 * interface for that decision, the {@link
 * javax.faces.render.ResponseStateManager}.
 * <code>ResponseStateManager</code> is retrieved from the {@link
 * javax.faces.render.RenderKit}, since the RenderKit
 * <strong>does</strong> know the markup language or other rendering
 * mechanism used.</p>
 *
 * <p>The state of a component tree is broken into two pieces: 1. tree
 * structure and 2. component state.  The former is described in the
 * documentation for {@link #getTreeStructureToSave} and the latter in
 * {@link #getComponentStateToSave}.</p>
 *
 * <p>The separation between tree structure and component state has been
 * explicitly called out to make it clear that implementations can use a
 * different mechanism for persisting the structure than is used to
 * persist the state.  For example, in a system where the tree structure
 * is stored statically, as an XML file, for example, the system could
 * keep a DOM representation of the trees representing the webapp UI in
 * memory, to be used by all requests to the application.</p>
 *
 * <p><strong>Usage Contract for this Class</strong></p>
 *
 * <p>For state saving, the user of this class depends on the type of
 * rendering technology being used.  For JSP-based applications, the
 * methods in this class are called indirectly by the {@link
 * ViewHandler} via the tag handlers for the {@link
 * UIViewRoot} and {@link
 * javax.faces.component.UIForm} tags.  In non-JSP applications, the
 * methods in this class may be called directly from the {@link
 * ViewHandler}.  The relevant public methods for state saving are:</p>
 *
 * 	<ul>
 *
 *	  <li>{@link #saveSerializedView}
 *	  </li>
 *
 *	  <li>{@link #writeState}
 *	  </li>
 *
 *	</ul>
 *
 *
 * <p>For state restoring, the {@link #restoreView} method in this class
 * must be called from the <em>Restore View</em> phase of the
 * request processing lifecycle.</p>
 *
 */

public abstract class StateManager {

    /**
     *
     * <p>The <code>ServletContext</code> init parameter consulted by
     * the <code>StateManager</code> to tell where the state should be
     * saved.  Valid values are given as the values of the constants:
     * {@link #STATE_SAVING_METHOD_CLIENT} or {@link
     * #STATE_SAVING_METHOD_SERVER}.</p>
     *
     * <p>If this parameter is not specified, the default value is the
     * value of the constant {@link #STATE_SAVING_METHOD_CLIENT}. </p>
     *
     * @see #writeState
     *
     */

    public static final String STATE_SAVING_METHOD_PARAM_NAME = 
	"javax.faces.STATE_SAVING_METHOD";

    public static final String STATE_SAVING_METHOD_CLIENT = "client";

    public static final String STATE_SAVING_METHOD_SERVER = "server";

    // --------------------  Methods used in marshalling the state to be saved

    /**
     * <p>This method causes the structure and state of the component
     * tree linked from the argument {@link FacesContext} to be
     * collected, stored, and possibly returned in a {@link
     * SerializedView} Object.</p>
     *
     * <p>The implementation must consult the
     * <code>ServletContext</code> init parameter named as the value of
     * the constant {@link #STATE_SAVING_METHOD_PARAM_NAME} to determine
     * whether it should save the state in the response or on the
     * server.</p>
     *
     * <p>In JSP applications, this method must be called from the
     * <code>doAfterBody()</code> method of the tag handler for the
     * {@link UIViewRoot} tag.</p>
     *
     * @return a SerializedView instance which encapsulates the state of this
     * view, or null if no state needs to be written to the response.
     *
     */

    public abstract SerializedView saveSerializedView(FacesContext context);


    /**
     *
     * <p>The implementation must save the tree structure, from the root
     * of the component tree, to a <code>Serializable</code> Object.
     * Tree structure is comprised of parent child relationships,
     * including Facets, as well as the <code>id</code> for each
     * component.  It is necessary to save the <code>id</code> of each
     * component to allow the naming containers to be correctly
     * populated as the tree is constructed.</p>
     *
     * <p>Note that associations to {@link
     * javax.faces.validator.Validator}s, {@link
     * javax.faces.convert.Converter}s, {@link
     * javax.faces.event.FacesListener}s, and other types of things
     * attached to components is considered component state, not
     * structure.</p>
     *
     * <p>Components can opt-out of being included in the tree structure
     * <strong>and</strong> component state by setting their
     * <code>transient</code> property to <code>true</code>. The default
     * value of this property is <code>false</code>.  Children of
     * <code>transient</code> components are not saved. During the
     * traversal of the tree structure, if a component has its
     * <code>transient</code> property set to true, that component (and
     * its children) should not be included in the tree structure.</p>
     *
     * @return an Serilizable Object representing the structure of the
     * tree.  For example, this could be an ASCII encoding of the actual
     * tree, or it could be a key to some in-memory data structure.
     *
     *
     */

    protected abstract Object getTreeStructureToSave(FacesContext context);


    /**

    * <p>The implementation must get the state of the component tree as
    * a <code>Serializable</code> Object.  This may be done by calling {@link
    * UIComponent#processRestoreState} on the root of
    * the component tree.  This state includes the following kinds of
    * information.</p>
    *
    * <p>
    *
    * <ul><li><p>the relationship of components to their <code>{@link
    * javax.faces.validator.Validator}s, {@link
    * javax.faces.convert.Converter}s, {@link
    * javax.faces.event.FacesListener}s, {@link
    * javax.faces.render.Renderer}s</code> and other objects
    * associated with a component.</p>
    *
    * <p>Each component is responsible for saving its attached objects.
    * The {@link javax.faces.component.StateHolder} interface is
    * provided for this use.</p>
    *
    * </li>
    *
    * <li><p>attributes and properties for all components and attached
    * things</p></li>
    *
    * </ul>
    *
    * </p>
    *
    * @return A Serialzable Object representation of the state of the
    * tree.  

    */

    protected abstract Object getComponentStateToSave(FacesContext context);

    // ------- Methods that can be used in writing the state to the response

    /**
     *
     * <p>This method causes the state that has been obtained from
     * {@link StateManager#saveSerializedView} to be actually saved in an
     * implementation dependent manner.</p>
     *
     * <p>The implementation must consult the
     * <code>ServletContext</code> init parameter named as the value of
     * the constant {@link #STATE_SAVING_METHOD_PARAM_NAME} to determine
     * whether it should save the state in the response or on the
     * server.</p>
     *
     * <p>If the <code>ServletContext</code> init parameter directs the
     * state to be saved to the response, the implementation of this
     * method must call {@link
     * javax.faces.render.ResponseStateManager#writeState} to cause the
     * state to be saved to the response.</p>
     *
     * <p>If the <code>ServletContext</code> init parameter directs the
     * state to be saved on the server, it must be done so such that it
     * can be retrieved using only the <code>viewId</code>.  This is
     * necessary for the {@link #restoreView} method to work.</p>
     *
     * <p>This method assumes the caller has positioned the {@link
     * javax.faces.context.ResponseWriter} at the correct place for the
     * saved state to be written.</p>
     *
     * <p>In JSP applications, this method must be called from the
     * <code>doAfterBody()</code> method of the tag handler for the
     * {@link UIViewRoot} component.  The <code>doAfterBody()</code>
     * method is responsible for causing the between, but not including,
     * each state marker to be written to the
     * <code>ResponseWriter</code>, calling this method at the right
     * time so that the state can be written out appropriately.</p>
     *
     * @param context the {@link FacesContext} for this view.  This is
     * used to obtain the <code>ResponseWriter</code> to which to write
     * the response, and the <code>viewId</code> of this view.
     *
     * @param content the rendered content of this view.  
     *
     * @param state the state of the rendered view, obtained from
     * {@link #saveSerializedView}.
     * @return Object response to be sent to client.
     *
     */
    public abstract void writeState(FacesContext context,
				    SerializedView state) throws IOException;

    // ---------------------- Methods used in restoring the view

    /**
     *
     * <p>This method causes the tree structure and the component state
     * of the view for this <code>viewId</code> to be restored.  In the
     * initial request case, this method returns <code>null</code>.</p>
     *
     * <p> The implementation must consult the
     * <code>ServletContext</code> init parameter named as the value of
     * the constant {@link #STATE_SAVING_METHOD_PARAM_NAME} to determine
     * whether state was saved in the response or on the server.</p>
     *
     * <p>If the state was saved on the server, it is accessible using
     * the <code>viewId</code> as a key.</p>
     *
     * <p>If the state was saved in the client, this method calls
     * through to {@link #restoreTreeStructure} and, if necessary {@link
     * #restoreComponentState}</p>
     *
     * @return the {@link UIViewRoot} that matches this
     * <code>viewId</code>, if there was a view to restore,
     * <code>null</code> otherwise.
     */

    public abstract UIViewRoot restoreView(FacesContext context, String viewId);

    /**
     *
     * <p>The implementation must obtain the view with {@link
     * UIViewRoot} root that corresponds to the argument
     * <code>viewId</code>.  The implementation may call {@link
     * javax.faces.render.ResponseStateManager#getTreeStructureToRestore}
     * to get back the Object which it returned from {@link
     * #getTreeStructureToSave}.  It must then turn that Object into the
     * component tree with {@link UIViewRoot} root, which returns.  If
     * {@link
     * javax.faces.render.ResponseStateManager#getTreeStructureToRestore}
     * returns <code>null</code>, just return null.</p>
     *
     * <p><strong>NOTE:</strong> the return from this method is just the
     * tree structure.  Component state must still be applied to this
     * tree structure by calling {@link #restoreComponentState}.</p>
     *
     * @return the {@link UIViewRoot} rooted tree structure, or null if
     * no tree structure has been stored.
     */

    protected abstract UIViewRoot restoreTreeStructure(FacesContext context, 
						       String viewId);

    /**
     *
     * <p>This method should only be called if {@link
     * #restoreTreeStructure} returned non-<code>null</code>, indicating
     * there actually is some state to restore.  The implementation must
     * take the argument {@link UIViewRoot} root and populate it with
     * state information.  The implementation may call {@link
     * javax.faces.render.ResponseStateManager#getComponentStateToRestore}
     * to get back the Object which it returned from {@link
     * #getComponentStateToSave}, then it must call {@link
     * UIComponent#processRestoreState} on the {@link UIViewRoot} root
     * of the tree.</p>
     *
     * @param context the FacesContext for this request
     *
     * @param root what was returned from {@link #restoreTreeStructure}
     *
     * @exception NullPointerException if any of the arguments are
     * <code>null</code>.
     */
    
    protected abstract void restoreComponentState(FacesContext context, UIViewRoot root) throws IOException;

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
	protected Object structure = null;
	protected Object state = null;

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

    // --------- methods used by UIComponents to save their attached Objects.

    /**
     *
     * <p>This method is called by {@link
     * UIComponent} subclasses that have attached
     * Objects.  It is a convenience method that does the work of saving
     * attached objects that may or may not implement the {@link
     * StateHolder} interface.</p>
     *
     * <p>Algorithm:</p>
     *
     * <p>Consult the {@link #STATE_SAVING_METHOD_PARAM_NAME}
     * <code>ServletContext</code> init parameter.  If the value of the
     * parameter is equal to the value of {@link
     * #STATE_SAVING_METHOD_SERVER}, this implementation saves the
     * argument attachedObjects in the {@link
     * javax.faces.context.ExternalContext}'s session under a key
     * composed of the concatenation of the <code>viewId</code> of the
     * current tree, the <code>clientId</code> of the argument
     * component, and the argument distinguishingKey, and returns the
     * key.  Otherwise, we proceed as follows.</p>
     *
     * <p>If argument attachedObjects is null return.</p>
     *
     * <p>Store the state of the attachedObjects as an opaque Object array,
     * interpreted only by our corresponding {@link
     * #restoreAttachedObjectState} method.  Each element in the Object array
     * is either null, or is itself an array of <code>StateHolderSaver</code>
     * instances.</p>
     *
     * <p>For each element in the argument attachedObjects array:</p>
     *
     * <p>If the current element is null, assign the corresponding
     * element in the Object array to null.</p>
     *
     * <p>If the current element is non-null, create an inner
     * <code>StateHolderSaver</code> array and iterate over the elements
     * in the <code>List</code> contained in the current element in the
     * argument attachedObjects array.  For each element, save the
     * attached object using the <code>StateHolderSaver</code> helper
     * class</p>.
     *
     * <p>Save the inner <code>StateHolderSaver</code> array to the
     * corresponding element in the result Object array</p>
     *
     * @param context the {@link FacesContext} for this request.
     *
     * @param attachee the {@link UIComponent} to which the
     * attachedObjects are attached.
     *
     * @param distinguishingKey an opaque string that is mixed in to the
     * key used when saving on the server.  May be null.  This parameter
     * is used when one {@link UIComponent} class has multiple different
     * kinds of attached objects.  For example, {@link
     * javax.faces.component.UIInput} has both validators and listeners.
     *
     * @param attachedObjects the objects, which may implement {@link
     * StateHolder}, that are attached to argument attachee.
     *
     * @exception NullPointerException if the context or attachee
     * arguments are null.
     *
     */

    public Object getAttachedObjectState(FacesContext context,
					 UIComponent attachee,
					 String distinguishingKey,
					 List attachedObjects[]) {
	if (null == attachedObjects) {
	    return null;
	}
	if (null == context || null == attachee) {
	    throw new NullPointerException();
	}
	String 
	    key = null,
	    paramVal = context.getExternalContext().getInitParameter(STATE_SAVING_METHOD_PARAM_NAME);
	if (null != paramVal && paramVal.equals(STATE_SAVING_METHOD_SERVER)) {
	    if (null != (key = attachee.getClientId(context))) {
		key = context.getViewRoot().getViewId() + key + distinguishingKey;
		context.getExternalContext().getSessionMap().put(key,
								 attachedObjects);
		return key;
	    }
	    // else we can't get a key, so we have to save the
	    // attachedObjects
	}
	
	int 
	    i, attachedObjectsLength = attachedObjects.length,
	    j, innerListLength;
	Object [] result = new Object[attachedObjectsLength];
	StateHolderSaver [] innerList = null;
	Object curAttachedObject = null;
	Iterator iter = null;
	
	// For each List in the List array.
	for (i = 0; i < attachedObjectsLength; i++) {
	    // if there is no List
	    if (null == attachedObjects[i]) {
		result[i] = null;
	    }
	    else {
		// There is a List, therefore we have some attachedObjects
		innerListLength = attachedObjects[i].size();
		innerList = new StateHolderSaver[innerListLength];
		iter = attachedObjects[i].iterator();
		j = 0;
		// Iteratate over the attachedObjects
		while (iter.hasNext()) {
		    curAttachedObject = iter.next();
		    if (null != curAttachedObject) {
			innerList[j] = 
			    new StateHolderSaver(context, curAttachedObject);
		    }
		    j++;
		}
		// at this point, innerList has the state of all the
		// attachedObjects for this element in the argument attachedObjects
		// array.
		result[i] = innerList;
	    }
	}
	return result;
    }
    
    /**
     *
     * <p>This method is tightly coupled with {@link #getAttachedObjectState}.</p>
     *
     * <p>Algorithm:</p>
     *
     * <p>Consult the {@link #STATE_SAVING_METHOD_PARAM_NAME}
     * <code>ServletContext</code> init parameter.  If the value of the
     * parameter is equal to the value of {@link
     * #STATE_SAVING_METHOD_SERVER}, this implementation looks in the
     * {@link javax.faces.context.ExternalContext}'s session for a key
     * composed of the <code>viewId</code> of the current tree
     * concatenated with the <code>clientId</code> of the argument
     * component.  If found, this is returned.  Otherwise, we proceed as
     * follows.</p>
     *
     * <p>Interpret the argument Object as an Object array, which it
     * must be because that's what {@link #getAttachedObjectState} has
     * produced.  This method creates a new <code>List []</code> to
     * store the restored attachedObjects.  For each element in the Object
     * array:</p>
     *
     * <p>If the current element is non-null, it must be of type
     * <code>StateHolderSaver</code> array.  Create an ArrayList to
     * store the contents of the inner <code>StateHolderSaver</code> array.
     * For each element in the inner <code>StateHolderSaver</code>
     * array:</p>
     *
     * <ul>
     *
     * <p>Interpret the element as the fully qualified Java class name
     * and create an instance of that class, storing it in the
     * ArrayList.</p>
     *
     * </ul>
     *
     * @exception NullPointerException if context is null.
     *
     */
    
    public List [] restoreAttachedObjectState(FacesContext context,
            Object stateObj, List[] currentList) throws IOException {
	if (null == stateObj) {
	    return null;
	}
	if (null == context) {
	    throw new NullPointerException();
	}
	String 
	    key = null,
	    paramVal = context.getExternalContext().getInitParameter(STATE_SAVING_METHOD_PARAM_NAME);
	
	if (null != paramVal && paramVal.equals(STATE_SAVING_METHOD_SERVER)) {
	    key = (String) stateObj;
	    return (List []) context.getExternalContext().getSessionMap().get(key);
	}

	
	Object [] state = (Object []) stateObj;
	StateHolderSaver [] innerArray = null;
	int 
	    i, j, innerLen, outerLen = state.length;
	List [] result = null;
	ArrayList curList = null;
	Object curAttachedObject = null;
	
	for (i = 0; i < outerLen; i++) {
            if (null != state[i]) {
                if (null == result) {
                    result = new List[outerLen];
                }
                innerArray = (StateHolderSaver []) state[i];
                innerLen = innerArray.length;
                // if there were some attached objects registered prior
                // to this method being invoked, merge them with the list
                // to be restored.
                if ( currentList == null || currentList[i] == null) {
                    result[i] = curList = new ArrayList();
                } else {
                    curList = (ArrayList) currentList[i];
                    result[i] = curList;
                }
                // create the attachedObjects for this List
                for (j = 0; j < innerLen; j++) {
                    if (null != innerArray[j]) {
                        curAttachedObject = innerArray[j].restore(context);
                        if (null != curAttachedObject) {
                            curList.add(curAttachedObject);
                        }
                    }
                }
            }
        }
	return result;
    }

}
