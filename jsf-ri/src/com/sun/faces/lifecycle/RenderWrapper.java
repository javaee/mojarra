/*
 * $Id: RenderWrapper.java,v 1.1 2002/03/13 18:04:23 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// RenderWrapper.java

package com.sun.faces.lifecycle;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletException;

/**
 *
 * An implementation of this class knows what to do to start rendering
 * the Faces page

 * @version $Id: RenderWrapper.java,v 1.1 2002/03/13 18:04:23 eburns Exp $
 * 
 * @see	com.sun.faces.Page#service
 *
 */

public interface RenderWrapper
{

public void commenceRendering(ServletRequest req, ServletResponse res) throws ServletException, IOException;

} // end of interface RenderWrapper
