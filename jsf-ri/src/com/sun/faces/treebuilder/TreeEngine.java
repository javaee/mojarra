/*
 * $Id: TreeEngine.java,v 1.2 2002/03/08 22:16:09 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TreeEngine.java

package com.sun.faces.treebuilder;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.UIComponent;
import javax.faces.RenderContext;
import javax.faces.TreeNavigator;

/**
 *

 *  <B>TreeEngine</B> is the entrypoint into the system that can turn a
 *  jsp page into a tree of UIComponent widgets. <P>

 *
 * <B>Lifetime And Scope</B> <P>

 * There is one instance of this class per webapp. </P>
 *
 * @version $Id: TreeEngine.java,v 1.2 2002/03/08 22:16:09 eburns Exp $
 * 
 * @see	com.sun.faces.servlet.FacesFilter#init
 * @see	com.sun.faces.treebuilder.TreeBuilder

 *
 */

public interface TreeEngine 
{
//
// Protected Constants
//

//
// Class Variables
//

//
// General Methods
//

/**

* @return the UIComponent root of the component tree embedded in the
* page addressed by the requestURI.

*/

public TreeNavigator getTreeForURI(RenderContext rc, String requestURI);

} // end of interface TreeEngine
