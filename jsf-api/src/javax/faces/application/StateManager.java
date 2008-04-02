/*
 * $Id: StateManager.java,v 1.5 2003/08/02 05:11:31 eburns Exp $
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

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.io.Serializable;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;



/**
 * <p><strong>StateManager</strong> directs the process of saving and
 * restoring tree structure and tree state.  StateManager is retrieved
 * from the {@link ViewHandler}, and therefore, it knows the kind of
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
 * <p>The state of a tree is broken into two pieces: 1. tree structure
 * and 2. component state.  The former is described in the documentation
 * for {@link #getTreeStructureToSave} and the latter in {@link
 * #getTreeStateToSave}.</p>
 *
 * <p>The separation between tree structure and tree state has been
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
 * javax.faces.component.UIPage} and {@link
 * javax.faces.component.UIForm} tags.  In non-JSP applications, the
 * methods in this class may be called directly from the {@link
 * ViewHandler}.  The relevant public methods for state saving are:</p>
 *
 * 	<ul>
 *
 *	  <li>{@link #writeStateMarker}
 *	  </li>
 *
 *	  <li>{@link #getSerializedTree}
 *	  </li>
 *
 *	  <li>{@link #saveTree}
 *	  </li>
 *
 *	</ul>
 *
 *
 * <p>For state restoring, the {@link #restoreTree} method in this class
 * must be called from the <em>Restore Component Tree</em> phase of the
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
     * @see #saveTree
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
     * collected, stored, and returned in a {@link SerializedTree}
     * Object.</p>
     *
     * <p>Implementations may override this method to provide their own
     * state management mechanism.  The default implementation simply
     * calls through to {@link #getTreeStructureToSave} and {@link
     * #getTreeStateToSave} to obtain the tree structure and tree state
     * objects, which it wraps in a {@link SerializedTree} Object to return.
     * </p>
     *
     * <p>In JSP applications, this method must be called from the
     * <code>doAfterBody()</code> method of the tag handler for the
     * {@link javax.faces.component.UIPage} tag.</p>
     *
     * @return a SerializedTree instance which encapsulates the state of this
     * tree, or null if no state needs to be written to the response.
     *
     */

    public SerializedTree getSerializedTree(FacesContext context) {
	SerializedTree result = null;

	result = new SerializedTree(getTreeStructureToSave(context),
				    getTreeStateToSave(context));
	return result;
    }


    /**
     *
     * <p>The implementation must save the tree structure, from the root
     * of the component tree, to a <code>Serializable</code> Object.
     * Tree structure is comprised of parent child relationships,
     * including Facets.</p>
     *
     * <p>Note that associations to {@link
     * javax.faces.validator.Validator}s, {@link
     * javax.faces.convert.Converter}s, {@link
     * javax.faces.event.FacesListener}s, and other types of things
     * attached to components is considered component state, not
     * structure.</p>
     *
     * <p>Components can opt-out of being included in the tree structure
     * <strong>and</strong> state by setting their
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
    * javax.faces.component.UIComponent#processGetState} on the root of
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
    * @return A serialzable Object representation of the state of the
    * tree.  

    */

    protected abstract Object getTreeStateToSave(FacesContext context);

    // ------- Methods that can be used in writing the state to the response

    /**
     *
     * <p>This method may be called during the <em>Render Response
     * Phase</em> phase of the request processing lifecycle to cause a
     * marker to be written to the output so that the state of multiple
     * forms in a single page can be saved.</p>
     *
     * <p>In JSP applications, this method must be called during the
     * <code>doEndTag()</code> method of the tag handler for {@link
     * javax.faces.component.UIForm} <strong>before</strong> the {@link
     * javax.faces.component.UIComponent#encodeEnd} method for the form
     * component is called.  The implementation of this method may call
     * {@link javax.faces.render.ResponseStateManager#writeStateMarker}
     * to actually cause the marker to be written to the {@link
     * javax.faces.context.ResponseWriter} for this request.</p>
     *
     */

     public abstract void writeStateMarker(FacesContext context) throws IOException;

    /**
     *
     * <p>This method causes the state that has been obtained from
     * {@link StateManager#getSerializedTree} to be actually saved in an
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
     * can be retrieved using only the <code>treeId</code>.  This is
     * necessary for the {@link #restoreTree} method to work.</p>
     *
     * <p>In JSP applications, this method must be called from the
     * <code>doAfterBody()</code> method of the tag handler for the
     * {@link javax.faces.component.UIPage} component.</p>
     *
     * @param context the {@link FacesContext} for this tree.  This is
     * used to obtain the {@link javax.faces.context.ResponseWriter} to
     * which to write the response, and the <code>treeId</code> of this
     * tree.
     *
     * @param content the rendered content of this component tree.  
     *
     * @param state the state of the rendered tree, obtained from
     * {@link #getSerializedTree}.
     *
     */

    public abstract void saveTree(FacesContext context, Reader content, 
				  SerializedTree state);

    // ---------------------- Methods used in restoring the component tree

    /**
     *
     * <p>This method causes the tree structure and the component state
     * of the tree for this <code>treeId</code> to be restored.  In the
     * initial request case, this method causes the tree to be created.
     * When this method exits, the {@link
     * javax.faces.component.UIComponent} tree is ready to be used for
     * the remainder of the request processing lifecycle.</p>
     *
     * <p>This method must be called from the <em>Restore Component
     * Tree</em> phase of the request processing lifecycle.</p>
     *
     * <p> The implementation must consult the
     * <code>ServletContext</code> init parameter named as the value of
     * the constant {@link #STATE_SAVING_METHOD_PARAM_NAME} to determine
     * whether state was saved in the response or on the server.</p>
     *
     * <p>If the state was saved on the server, it is accessible using
     * the <code>treeId</code> as a key.</p>
     *
     * <p>If the state was saved in the client, this method calls
     * through to {@link #restoreTreeStructure} and, if necessary {@link
     * #restoreTreeState}</p>
     */

    public abstract void restoreTree(FacesContext context, String treeId) throws IOException;

    /**
     *
     * <p>The implementation must cause the {@link
     * javax.faces.component.UIPage} root of the component tree for this
     * request to be set as the root of this {@link FacesContext}.  The
     * implementation may call {@link
     * javax.faces.render.ResponseStateManager#getTreeStructureToRestore}
     * to get back the Object which it returned from {@link
     * #getTreeStructureToSave}.  It must then turn that Object into the
     * component tree with {@link javax.faces.component.UIPage} root,
     * which it stores as the root in the FacesContext.  If {@link
     * javax.faces.render.ResponseStateManager#getTreeStructureToRestore}
     * returns <code>null</code>, create a {@link
     * javax.faces.component.UIPage} instance, set its
     * <code>treeId</code> property using the argument, and store that
     * as the root node in the <code>FacesContext</code>.</p>
     *
     * @return true if there was a tree structure Object, false if we
     * had to create a {@link javax.faces.component.UIPage} from
     * scratch.
     */

    protected abstract boolean restoreTreeStructure(FacesContext context, 
						 String treeId);

    /**
     *
     * <p>This method should only be called if {@link
     * #restoreTreeStructure} returned true, indicating there actually
     * is some state to restore.  The implementation must take the
     * argument <code>FacesContext</code> and pull out its {@link
     * javax.faces.component.UIPage} root and populate it with state
     * information.  The implementation may call {@link
     * javax.faces.render.ResponseStateManager#getTreeStateToRestore} to
     * get back the Object which it returned from {@link
     * #getTreeStateToSave}, then it must call {@link
     * javax.faces.component.UIComponent#processRestoreState} on the
     * {@link javax.faces.component.UIPage} root of the tree.</p>
     */
    
    protected abstract void restoreTreeState(FacesContext context) throws IOException;

    /**
     *
     * <p>Convenience struct for encapsulating tree structure and tree
     * state.  This is necessary to allow the API to be flexible enough
     * to work in JSP and non-JSP environments.</p>
     *
     */

    public class SerializedTree extends Object {
	protected Object structure = null;
	protected Object state = null;

	public SerializedTree(Object newStructure, Object newState) {
	    structure = newStructure;
	    state = newState;
	}
    }

    // --------- methods used by UIComponents to save their attached Objects.

    /**
     *
     * <p>This method is called by {@link
     * javax.faces.component.UIComponent} subclasses that have attached
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
     * composed of the concatenation of the <code>treeId</code> of the
     * current tree, the <code>clientId</code> of the argument
     * component, and the argument distinguishingKey, and returns the
     * key.  Otherwise, we proceed as follows.</p>
     *
     * <p>If argument attachedObjects is null return.</p>
     *
     * <p>Store the state of the attachedObjects as an opaque Object array,
     * interpreted only by our corresponding {@link
     * #restoreAttachedObjectState} method.  Each element in the Object array
     * is either null, or is itself an array of {@link StateHolderSaver}
     * instances.</p>
     *
     * <p>For each element in the argument attachedObjects array:</p>
     *
     * <p>If the current element is null, assign the corresponding
     * element in the Object array to null.</p>
     *
     * <p>If the current element is non-null, create an inner {@link
     * StateHolderSaver} array and iterate over the elements in the
     * <code>List</code> contained in the current element in the
     * argument attachedObjects array.  For each element, save the
     * attached object using the {@link StateHolderSaver} helper
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
		key = context.getRoot().getTreeId() + key + distinguishingKey;
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
		    innerList[j] = 
			new StateHolderSaver(context, curAttachedObject);
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
     * composed of the <code>treeId</code> of the current tree
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
     * <p>If the current element is non-null, it must be of type {@link
     * StateHolderSaver} array.  Create an ArrayList to store the contents
     * of the inner {@link StateHolderSaver} array.  For each element in
     * the inner <code>StateHolderSaver</code> array:</p>
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
					      Object stateObj) throws IOException {
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
		result[i] = curList = new ArrayList();
		// create the attachedObjects for this List
		for (j = 0; j < innerLen; j++) {
		    curAttachedObject = innerArray[j].restore(context);
		    if (null != curAttachedObject) {
			curList.add(curAttachedObject);
		    }
		}
	    }
	}
	return result;
    }

    /**
     * <p>Helper class for saving and restoring attached objects.</p>
     *
     */
    protected class StateHolderSaver extends Object implements Serializable {
	protected String className = null;
	protected Object savedState = null;

	StateHolderSaver(FacesContext context, Object toSave) {
	    className = toSave.getClass().getName();

	    if (toSave instanceof StateHolder) {
		if (!((StateHolder)toSave).isTransient()) {
		    savedState = ((StateHolder)toSave).getState(context);
		}
	    }
	}

	/**
	 *
	 * @return the restored {@link StateHolder} instance.
	 */

	Object restore(FacesContext context) throws IOException {
	    Object result = null;
	    Class toRestoreClass = null;
	    try {
		toRestoreClass = 
		    StateManager.loadClass(className, this);
	    }
	    catch (ClassNotFoundException e) {
		toRestoreClass = null;
	    }
	    
	    if (null != toRestoreClass) {
		try {
		    result = toRestoreClass.newInstance();
		}
		catch (InstantiationException e) {
		    throw new IOException(e.getMessage());
		}
		catch (IllegalAccessException a) {
		    throw new IOException(a.getMessage());
		}
	    }
	    
	    if (null != result && null != savedState && 
		result instanceof StateHolder) {
		// don't need to check transient, since that was done on
		// the saving side.
		((StateHolder)result).restoreState(context, savedState);
	    }
	    return result;
	}
    }

    private static Class loadClass(String name, 
				   Object fallbackClass) throws ClassNotFoundException {
	ClassLoader loader =
	    Thread.currentThread().getContextClassLoader();
	if (loader == null) {
	    loader = fallbackClass.getClass().getClassLoader();
	}
	return loader.loadClass(name);
    }


}
