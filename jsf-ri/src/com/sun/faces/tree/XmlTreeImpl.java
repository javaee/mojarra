/*
 * $Id: XmlTreeImpl.java,v 1.1 2002/05/30 01:42:08 eburns Exp $
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

/**
 *
 *  <B>XmlTreeImpl</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: XmlTreeImpl.java,v 1.1 2002/05/30 01:42:08 eburns Exp $
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

public XmlTreeImpl(UIComponent newRoot, String newTreeId)
{
    super();
    setRoot(newRoot);
    setTreeId(newTreeId);
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
