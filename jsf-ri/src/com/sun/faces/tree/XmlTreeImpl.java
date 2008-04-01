/*
 * $Id: XmlTreeImpl.java,v 1.2 2002/06/01 00:58:22 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// XmlTreeImpl.java

package com.sun.faces.tree;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.component.UIComponent;
import javax.faces.render.RenderKit;
import javax.faces.tree.Tree;
import javax.faces.FactoryFinder;

import javax.servlet.ServletContext;

import com.sun.faces.RIConstants;

/**
 *
 *  <B>XmlTreeImpl</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: XmlTreeImpl.java,v 1.2 2002/06/01 00:58:22 eburns Exp $
 * 
 * @see	javax.faces.tree.Tree
 *
 */

public class XmlTreeImpl extends Tree
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

protected String treeId = null;

// Relationship Instance Variables

protected RenderKit renderKit = null;
protected UIComponent root = null;

//
// Constructors and Initializers    
//

/**

* PRECONDITION: the ServletContext has been initialized with all the
* required factories.

*/ 

public XmlTreeImpl(ServletContext context, UIComponent newRoot, 
		   String newTreeId)
{
    super();
    ParameterCheck.nonNull(context);

    setRoot(newRoot);
    setTreeId(newTreeId);

    renderKit = (RenderKit) 
	context.getAttribute(RIConstants.DEFAULT_RENDER_KIT);
    Assert.assert_it(null != renderKit);
    
}

//
// Class methods
//

//
// General Methods
//

void setRoot(UIComponent newRoot)
{
    ParameterCheck.nonNull(newRoot);
    root = newRoot;
}

void setTreeId(String newTreeId)
{
   ParameterCheck.nonNull(newTreeId);
   treeId = newTreeId;
}

//
// Methods from Tree
//

public RenderKit getRenderKit()
{
    return renderKit;
}

public void setRenderKit(RenderKit newRenderKit)
{
    ParameterCheck.nonNull(newRenderKit);

    renderKit = newRenderKit;
}

public UIComponent getRoot()
{
    return root;
}
 
public String getTreeId()
{
    return treeId;
}

public void release()
{
    root = null;
    treeId = null;
    renderKit = null;
}

// The testcase for this class is TestXmlTreeImpl.java 


} // end of class XmlTreeImpl
