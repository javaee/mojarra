/*
 * $Id: TreeNavigatorImpl.java,v 1.3 2003/02/20 22:49:38 ofung Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TreeNavigatorImpl.java

package com.sun.faces.tree;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.component.UIComponent;

import java.util.Iterator;
import java.util.Stack;

/**

 * @version $Id: TreeNavigatorImpl.java,v 1.3 2003/02/20 22:49:38 ofung Exp $
 * 
 * @see	javax.faces.TreeNavigator

 */

public class TreeNavigatorImpl extends Object implements TreeNavigator
{
//
// Protected Constants
//

//
// Class Variables
//

//
// Instance Variables
//

// Attribute Instance Variables

// Relationship Instance Variables

private UIComponent root;
private Stack startStack;
private Stack endStack;
private boolean startTraversalDone = false;

//
// Constructors and Initializers    
//

public TreeNavigatorImpl(UIComponent newRoot)
{
    super();
    ParameterCheck.nonNull(newRoot);
    root = newRoot;
    startStack = new Stack();
    endStack = new Stack();
}

//
// Class methods
//

//
// General Methods
//

public void replaceRoot(TreeNavigator newRoot) {
    ParameterCheck.nonNull(newRoot);
    this.reset();
    root = newRoot.getRoot();
}

// 
// Methods from TreeNavigator
//

public UIComponent getRoot() {
    return root;
}

public UIComponent getNextStart() {
    Iterator iter;
    Iterator childIter;
    UIComponent cur = null;

    if (startTraversalDone) {
	return cur;
    }

    // if there is nothing on the stack, that means we're at the root
    if (startStack.empty()) {
	cur = root;
	iter = cur.getChildren();
	if (iter.hasNext()) {
	    startStack.push(iter);
	}
    }
    else {
	while (!startStack.empty()) {
	    // else the we are processing the children of the root
	    iter = (Iterator)startStack.peek();
	    if (null != iter && iter.hasNext()) {
		// if there are children left
		cur = (UIComponent) iter.next();
		// return this child
		childIter = cur.getChildren();
		// see if this child has children
		if (null != childIter && childIter.hasNext()) {
		    // if so, push them on the stack
		    startStack.push(childIter);
		}
		else {
		    // If this is the last child of this parent
		    if (!iter.hasNext()) {
			// pop up a level
			startStack.pop();
		    }
		}
		break;
	    }
	    else if (null != iter) {
		// we are done with this level.
		startStack.pop();
	    }
	}
    }
    // done with traversal
    if (startStack.empty()) {
	startTraversalDone = true;
    }
    if (null != cur) {
	// make it so getNextEnd returns this.
	endStack.push(cur);
    }

    return cur;
}

public UIComponent getNextEnd() {
    return (UIComponent) endStack.pop();
}

public void reset() {
    startStack.clear();
    endStack.clear();
    startTraversalDone = false;
}

public UIComponent findComponentForId(String id) {
    ParameterCheck.nonNull(id);

    return findComponentForId(getRoot(), id);
}

/**

* This can be WAY optimized.

*/

protected UIComponent findComponentForId(UIComponent root, String componentId){
    UIComponent result = null;
    if (null == root) {
	return null;
    }
    if (componentId.equals(root.getComponentId())) {
	result = root;
	return result;
    }

    Iterator it = root.getChildren();
    while (it.hasNext()&& null == result) {
	result = findComponentForId((UIComponent) it.next(), componentId);
    }
    return result;
}

// The testcase for this class is TestGenericPhaseImpl.java


} // end of class TreeNavigatorImpl
