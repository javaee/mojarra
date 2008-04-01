/*
 * $Id: RenderContextFactory.java,v 1.2 2001/12/20 22:25:45 ofung Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// RenderContextFactory.java

package javax.faces;

import javax.servlet.ServletRequest;

/**
 *

 *  <B>RenderContextFactory</B> Creates RenderContext instances using
 *  the pluggable implementation scheme defined in the spec. <P>

 *
 * <B>Lifetime And Scope</B> <P>

 * There should be one instance of RenderContextFactory per app. <P></P>

 * It is created and used like this: <P></P>

<CODE><PRE>
    RenderKit kit = null;
    RenderContext context;
    RenderContextFactory factory;
    Renderer renderer;
    
    try {
	factory = RenderContextFactory.newInstance();
	context = factory.newRenderContext(servletRequest);
	kit = context.getRenderKit();
    }
    catch (Exception e) {
	System.out.println("Exception getting factory!!! " + e.getMessage());
    }

</PRE></CODE>

 *
 * @version $Id: RenderContextFactory.java,v 1.2 2001/12/20 22:25:45 ofung Exp $
 * 
 * @see	javax.faces.RenderContext
 *
 */

public abstract class RenderContextFactory extends Object
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

// Relationship Instance Variables

//
// Constructors and Initializers    
//

protected RenderContextFactory()
{
    super();
}

//
// Class methods
//

public static RenderContextFactory newInstance() throws FactoryConfigurationError  {
    try {
	return (RenderContextFactory) FactoryFinder.find(
	   /* The default property name according to the JSFaces spec */
	   "javax.faces.RenderContextFactory",
	   /* The fallback implementation class name */
	   "com.sun.faces.renderkit.html_basic.HtmlBasicRenderContextFactory");
    } catch (FactoryFinder.ConfigurationError e) {
	throw new FactoryConfigurationError(e.getException(),
					    e.getMessage());
    }
}


//
// Abstract Methods 
//

/**
  * Internal constructor which initializes a <code>RenderContext</code>,
  * the <code>ClientCapabilities</code>, <code>Locale</code> based on
  * information in the request.
  * @throws FacesException if any of these objects could not be
  *         created.
  */

public abstract RenderContext newRenderContext(ServletRequest request) throws FacesException;

} // end of class RenderContextFactory
