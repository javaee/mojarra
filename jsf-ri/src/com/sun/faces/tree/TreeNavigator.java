/*
 * $Id: TreeNavigator.java,v 1.1 2002/06/01 00:58:22 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TreeNavigator.java

package com.sun.faces.tree;

import javax.faces.component.UIComponent;


/**
 *

 *  </P><B>TreeNavigator</B>'s methods are called from the custom tag
 *  lib to obtain the UIComponent instance that corresponds to a tag
 *  instance. </P>
 *  

 * <P>PRECONDITION: These methods are only called from the custom tag
 * handler.  They are only called for tags that have a corresponding
 * UIComponent instance.  This implementation relies on the
 * getNextStart() and getNextEnd() methods being called from the doStart
 * and doEnd of the custom tags.  Calling from another place will break
 * this implementation.</P>

 * <B>Lifetime And Scope</B> <P>

 * There is one instance of TreeNavigator per request.  TreeNavigator
 * owns the tree.  </P>

 * The same TreeNavigator instance may be used to traverse the tree
 * multiple times by calling reset between traversals.

 *
 * @version $Id: TreeNavigator.java,v 1.1 2002/06/01 00:58:22 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public interface TreeNavigator
{

public UIComponent getRoot();

public UIComponent getNextStart();
public UIComponent getNextEnd();

public void reset();

public UIComponent findComponentForId(String id);

} // end of interface TreeNavigator
