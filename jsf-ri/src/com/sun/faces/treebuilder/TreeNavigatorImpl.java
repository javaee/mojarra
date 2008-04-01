/*
 * $Id: TreeNavigatorImpl.java,v 1.2 2002/03/08 22:16:09 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TreeNavigatorImpl.java

package com.sun.faces.treebuilder;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.UIComponent;

import java.util.Iterator;
import java.util.Stack;

import javax.faces.TreeNavigator;

/**

 * @version $Id: TreeNavigatorImpl.java,v 1.2 2002/03/08 22:16:09 eburns Exp $
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

    // if there is nothing on the stack, that means we're at the root

    if (startStack.empty()) {
	cur = root;
	iter = cur.getChildren(null);
	if (iter.hasNext()) {
	    startStack.push(iter);
	}
    }
    else {
	// else the we are processing the children of the root
	iter = (Iterator)startStack.peek();
	if (null != iter && iter.hasNext()) {
	    // if there are children left
	    cur = (UIComponent) iter.next();
	    // return this child
	    childIter = cur.getChildren(null);
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
	}
	else if (null != iter) {
	    // we are done with this level.
	    startStack.pop();
	}
	    
    }
    Assert.assert_it(null != cur);
    // make it so getNextEnd returns this.
    endStack.push(cur);

    return cur;
}

public UIComponent getNextEnd() {
    return (UIComponent) endStack.pop();
}

public void reset() {
    startStack.clear();
    endStack.clear();
}

public UIComponent findComponentForId(String id) {
    ParameterCheck.nonNull(id);

    return findComponentForId(getRoot(), id);
}

/**

* This can be WAY optimized.

*/

protected UIComponent findComponentForId(UIComponent root, String id) {
    UIComponent result = null;
    if (null == root) {
	return null;
    }
    if (id.equals(root.getId())) {
	result = root;
	return result;
    }

    Iterator it = root.getChildren(null);
    while (it.hasNext()&& null == result) {
	result = findComponentForId((UIComponent) it.next(), id);
    }
    return result;
}

// The testcase for this class is TestTreebuilder.java


} // end of class TreeNavigatorImpl
