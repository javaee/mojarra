/*
 * $Id: TreeBuilder.java,v 1.6 2002/03/19 19:25:02 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TreeBuilder.java

package com.sun.faces.treebuilder;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import org.apache.jasper_hacked.compiler.JspParseListener;
import javax.faces.UIComponent;
import javax.faces.UIPage;
import javax.faces.RenderContext;

import javax.servlet.jsp.tagext.TagInfo;
import javax.servlet.jsp.tagext.TagLibraryInfo;
import javax.servlet.ServletRequest;

import org.xml.sax.Attributes;

import java.util.Stack;
import java.util.Iterator;

import java.io.PrintStream;

/**

 *  This gets called for each tag.  We use it to build the component
 *  tree, using the BuildComponentFromTag instance, provided on
 *  construction.  This implementation is optimized only to be fast to
 *  implement.  <P>

 * <B>Lifetime And Scope</B> <P>

 * There is one instance of TreeBuilder for each time you want to parse
 * a tree.  Instances are not intended to be reused. </P>

 *
 *
 * @version $Id: TreeBuilder.java,v 1.6 2002/03/19 19:25:02 eburns Exp $
 * 
 * @see	com.sun.faces.treebuilder.TreeEngine#getTreeForURI
 *
 */

public class TreeBuilder extends Object implements JspParseListener
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

private RenderContext renderContext;
private String requestURI;
private UIPage root = null;

protected BuildComponentFromTag componentBuilder = null;

private Stack tagStack;

private int curDepth = 0;


//
// Constructors and Initializers    
//

public TreeBuilder(BuildComponentFromTag newComponentBuilder,
		   RenderContext newRenderContext, UIPage newRoot, 
		   String newRequestURI)
{
    ParameterCheck.nonNull(newComponentBuilder);
    ParameterCheck.nonNull(newRenderContext);
    ParameterCheck.nonNull(newRoot);
    ParameterCheck.nonNull(newRequestURI);
    componentBuilder = newComponentBuilder;
    renderContext = newRenderContext;
    requestURI = newRequestURI;
    root = newRoot;
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
	Iterator it = ((javax.faces.UISelectOne)root).getItems(renderContext);
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
	out.print(" value=" + root.getValue(renderContext) + "\n");
    }
    curDepth++;
    Iterator it = root.getChildren(renderContext);
    while (it.hasNext()) {
	printTree((UIComponent) it.next(), out);
    }
    curDepth--;
}

//
// Methods from JspParseListener
//

public void handleTagBegin(Attributes attrs, String prefix,
			   String shortTagName, TagLibraryInfo tli,
			   TagInfo ti, boolean hasBody, boolean isXml)
{
    UIComponent child = null, parent = null;
    String attrName, attrValue;

    if (!tagStack.empty()) {
	parent = (UIComponent) tagStack.peek();
    }

    if (null == (child=componentBuilder.createComponentForTag(shortTagName))) {
	// This isn't a tag that has a component.  See if it is a tag
	// that has a nested relationship to a component, such as
	// SelectOne
	componentBuilder.handleNestedComponentTag(renderContext, parent, 
						  shortTagName, attrs);
	return;
    }

    componentBuilder.applyAttributesToComponentInstance(renderContext, 
							child, attrs);
    
    if (componentBuilder.tagHasComponent(shortTagName)) {
	if (null != parent) {
	    parent.add(child);
	}
	
	tagStack.push(child);
    }
}

public void handleTagEnd(Attributes attrs, String prefix,
			 String shortTagName, TagLibraryInfo tli,
			 TagInfo ti, boolean hasBody)
{
    if (!componentBuilder.tagHasComponent(shortTagName)) {
	// This isn't a tag that has a component
	return;
    }
    tagStack.pop();
}

// The testcase for this class is TestTreebuilder.java 


} // end of class TreeBuilder
