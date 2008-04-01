/*
 * $Id: FacesContextFactory.java,v 1.1 2002/04/05 19:40:16 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// FacesContextFactory.java

package javax.faces;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletContext;

/**
 *

 *  <B>FacesContextFactory</B> Creates FacesContext instances using
 *  the pluggable implementation scheme defined in the spec. <P>

 *
 * <B>Lifetime And Scope</B> <P>

 * There should be one instance of FacesContextFactory per app. <P></P>

 * It is created and used like this: <P></P>

<CODE><PRE>
    RenderKit kit = null;
    FacesContext context;
    FacesContextFactory factory;
    Renderer renderer;
    
    try {
	factory = FacesContextFactory.newInstance();
	context = factory.newFacesContext(servletRequest);
	kit = context.getRenderKit();
    }
    catch (Exception e) {
	System.out.println("Exception getting factory!!! " + e.getMessage());
    }

</PRE></CODE>

 *
 * @version $Id: FacesContextFactory.java,v 1.1 2002/04/05 19:40:16 jvisvanathan Exp $
 * 
 * @see	javax.faces.FacesContext
 *
 */

public abstract class FacesContextFactory extends Object
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

protected FacesContextFactory()
{
    super();
}

//
// Class methods
//

public static FacesContextFactory newInstance() throws FactoryConfigurationError  {
    try {
	return (FacesContextFactory) FactoryFinder.find(
	   /* The default property name according to the JSFaces spec */
	   "javax.faces.FacesContextFactory",
	   /* The fallback implementation class name */
	   "com.sun.faces.renderkit.html_basic.HtmlBasicFacesContextFactory");
    } catch (FactoryFinder.ConfigurationError e) {
	throw new FactoryConfigurationError(e.getException(),
					    e.getMessage());
    }
}


//
// Abstract Methods 
//

/**
  * Internal constructor which initializes a <code>FacesContext</code>,
  * the <code>ClientCapabilities</code>, <code>Locale</code> based on
  * information in the request.
  * @throws FacesException if any of these objects could not be
  *         created.
  */

public abstract FacesContext newFacesContext(ServletRequest request, 
        ServletResponse response, ServletContext sc ) throws FacesException;

} // end of class FacesContextFactory
