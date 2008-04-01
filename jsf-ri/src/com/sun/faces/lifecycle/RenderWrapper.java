/*
 * $Id: RenderWrapper.java,v 1.2 2002/03/15 23:29:48 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// RenderWrapper.java

package com.sun.faces.lifecycle;

import java.io.IOException;

import javax.servlet.ServletException;

import javax.faces.FacesContext;
import javax.faces.TreeNavigator;

/**
 *
 * An implementation of this class knows what to do to start rendering
 * the Faces page

 * @version $Id: RenderWrapper.java,v 1.2 2002/03/15 23:29:48 eburns Exp $
 * 
 * @see	com.sun.faces.Page#service
 *
 */

public interface RenderWrapper
{

public void commenceRendering(FacesContext facesContext, 
			      TreeNavigator treeNav) throws ServletException, IOException;

} // end of interface RenderWrapper
