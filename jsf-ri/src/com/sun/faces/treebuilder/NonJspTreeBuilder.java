/*
 * $Id: NonJspTreeBuilder.java,v 1.1 2002/04/25 22:42:21 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// NonJspTreeBuilder.java

package com.sun.faces.treebuilder;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.UIComponent;
import javax.faces.UIPage;
import javax.faces.FacesContext;

import javax.servlet.ServletRequest;

import java.util.Stack;
import java.util.Iterator;

import java.io.PrintStream;
import java.io.InputStream;

/**

 *  This class is the entry point for building the tree from a non-JSP
 *  file.  This implementation is optimized only to be fast to
 *  implement.<P>

 * <B>Lifetime And Scope</B> <P>

 * There is one instance of NonJspTreeBuilder for each time you want to parse
 * a tree.  Instances are not intended to be reused. </P>

 *
 *
 * @version $Id: NonJspTreeBuilder.java,v 1.1 2002/04/25 22:42:21 eburns Exp $
 * 
 * @see	com.sun.faces.treebuilder.TreeEngine#getTreeForURI
 *
 */

public class NonJspTreeBuilder extends Object
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

// Relationship Instance Variables

private FacesContext facesContext;
private String requestURI;
private UIPage root = null;

protected BuildComponentFromTag componentBuilder = null;

private Stack tagStack;

private int curDepth = 0;

private InputStream treeInput = null;


//
// Constructors and Initializers    
//

public NonJspTreeBuilder(BuildComponentFromTag newComponentBuilder,
			 FacesContext newFacesContext, UIPage newRoot, 
			 InputStream newTreeInput)
{
    ParameterCheck.nonNull(newComponentBuilder);
    ParameterCheck.nonNull(newFacesContext);
    ParameterCheck.nonNull(newRoot);
    ParameterCheck.nonNull(newTreeInput);
    componentBuilder = newComponentBuilder;
    facesContext = newFacesContext;
    root = newRoot;
    treeInput = newTreeInput;
    curDepth = 0;
    tagStack = new Stack();
    // push the root
    tagStack.push(root);
}

//
// Class methods
//

//
// General Methods
//

public UIComponent getRoot() {
    return root;
}

public void printTree(UIComponent root, PrintStream out) {
    if (null == root) {
	return;
    }
    int i = 0;

    // handle indentation
    for (i = 0; i < curDepth; i++) {
	out.print("  ");
    }
    out.print(root.getId());
    if (root instanceof javax.faces.UISelectOne) {
	Iterator it = ((javax.faces.UISelectOne)root).getItems(facesContext);
	out.print(" {\n");
	while (it.hasNext()) {
	    for (i = 0; i < curDepth; i++) {
		out.print("  ");
	    }
	    
	    javax.faces.UISelectOne.Item curItem = (javax.faces.UISelectOne.Item) it.next();
	    out.print("\t value=" + 
		      curItem.getValue() + 
		      " label=" + 
		      curItem.getLabel() +
		      " description=" + 
		      curItem.getDescription() + "\n");
	}
	for (i = 0; i < curDepth; i++) {
	    out.print("  ");
	}
	out.print(" }\n");
    }
    else {
	out.print(" value=" + root.getValue(facesContext) + "\n");
    }
    curDepth++;
    Iterator it = root.getChildren(facesContext);
    while (it.hasNext()) {
	printTree((UIComponent) it.next(), out);
    }
    curDepth--;
}

// The testcase for this class is TestNonJspTreebuilder.java 


} // end of class NonJspTreeBuilder
