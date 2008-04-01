/*
 * $Id: TreeEngine.java,v 1.4 2002/03/19 19:25:02 eburns Exp $
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
import javax.faces.UIPage;

/**
 *

 *  <B>TreeEngine</B> is the entrypoint into the system that can turn a
 *  jsp page into a tree of UIComponent widgets. <P>

 *
 * <B>Lifetime And Scope</B> <P>

 * There is one instance of this class per webapp.  The TreeEngine
 * instance has a BuildComponentFromTag instance.</P>
 *
 * @version $Id: TreeEngine.java,v 1.4 2002/03/19 19:25:02 eburns Exp $
 * 
 * @see	com.sun.faces.servlet.FacesFilter#init
 * @see	com.sun.faces.treebuilder.TreeBuilder
 * @see	com.sun.faces.treebuilder.BuildComponentFromTag

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

public TreeNavigator getTreeForURI(RenderContext rc, UIPage root, 
				   String requestURI);

} // end of interface TreeEngine
