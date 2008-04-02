/*
 * $Id: LifecycleCallback.java,v 1.2 2003/02/20 22:48:47 ofung Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// LifecycleCallback.java

package com.sun.faces.lifecycle;

import javax.faces.component.UIComponent;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;

/**
 *
 *  <B>LifecycleCallback</B> defines an interface that allows plugging
 *  into the GenericPhase.traverseTreeInvokingMethod() system.
 *
 * @version $Id: LifecycleCallback.java,v 1.2 2003/02/20 22:48:47 ofung Exp $
 * 
 * @see	com.sun.faces.lifecycle.GenericPhaseImpl
 *
 */

public interface LifecycleCallback
{

/**

* Take some action on the component.

* return one of the Phase values, like Phase.GOTO_NEXT;

*/

public int takeActionOnComponent(FacesContext context,
				 UIComponent component) throws FacesException;

} // end of interface LifecycleCallback
