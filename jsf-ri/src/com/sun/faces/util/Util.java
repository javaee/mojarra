/*
 * $Id: Util.java,v 1.10 2002/06/04 00:11:25 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// Util.java

package com.sun.faces.util;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.servlet.ServletContext;
import javax.faces.FacesException;
import javax.faces.FactoryFinder;

import javax.faces.render.RenderKitFactory;
import javax.faces.render.RenderKit;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.tree.TreeFactory;
import javax.faces.context.FacesContextFactory;

import com.sun.faces.RIConstants;

/**
 *
 *  <B>Util</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: Util.java,v 1.10 2002/06/04 00:11:25 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class Util extends Object
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
private static long id = 0;

// Attribute Instance Variables

// Relationship Instance Variables

//
// Constructors and Initializers    
//

private Util()
{
    throw new IllegalStateException();
}

//
// Class methods
//
    public static Class loadClass(String name) throws ClassNotFoundException {
	ClassLoader loader =
	    Thread.currentThread().getContextClassLoader();
	if (loader == null) {
	    return Class.forName(name);
	}
	else {
	    return loader.loadClass(name);
	}
    }

    /**
     * Generate a new identifier currently used to uniquely identify
     * components.
     */
    public static synchronized String generateId() {
        if (id == Long.MAX_VALUE) {
            id = 0;
        } else { 
            id++;
        }
        return Long.toHexString(id);
    }

    /**

    * Init all the factories needed by faces. <P>

    * PRECONDITION: The ServletContext attr set has not been initialized
    * with the required factories, and singleton instances.<P>

    * POSTCONDITION: The ServletContext attr set has been initialized to
    * contain the factories under the keys given as constants in
    * FactoryFinder. <P>

    * @see javax.faces.FactoryFinder

    */

    public static void initServletContextForFaces(ServletContext context) throws FacesException {
	RenderKitFactory renderKitFactory = null;
	LifecycleFactory lifecycleFactory = null;
	TreeFactory treeFactory = null;
	FacesContextFactory facesContextFactory = null;
	RenderKit defaultRenderKit = null;

	Assert.assert_it(null == 
                       context.getAttribute(FactoryFinder.RENDER_KIT_FACTORY));
	renderKitFactory = (RenderKitFactory)
	    FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
	Assert.assert_it(null != renderKitFactory);
	context.setAttribute(FactoryFinder.RENDER_KIT_FACTORY, 
			     renderKitFactory);

	Assert.assert_it(null == 
                       context.getAttribute(FactoryFinder.LIFECYCLE_FACTORY));
	lifecycleFactory = (LifecycleFactory)
	    FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
	Assert.assert_it(null != lifecycleFactory);
	context.setAttribute(FactoryFinder.LIFECYCLE_FACTORY, 
			     lifecycleFactory);

	Assert.assert_it(null == 
                       context.getAttribute(FactoryFinder.TREE_FACTORY));
	treeFactory = (TreeFactory)
	    FactoryFinder.getFactory(FactoryFinder.TREE_FACTORY);
	Assert.assert_it(null != treeFactory);
	context.setAttribute(FactoryFinder.TREE_FACTORY, 
			     treeFactory);

	Assert.assert_it(null == 
                    context.getAttribute(FactoryFinder.FACES_CONTEXT_FACTORY));
	facesContextFactory = (FacesContextFactory)
	    FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
	Assert.assert_it(null != facesContextFactory);
	context.setAttribute(FactoryFinder.FACES_CONTEXT_FACTORY, 
			     facesContextFactory);

	Assert.assert_it(null == 
		 context.getAttribute(RIConstants.DEFAULT_RENDER_KIT));
	defaultRenderKit = 
         renderKitFactory.createRenderKit(RIConstants.DEFAULT_RENDER_KIT);
	Assert.assert_it(null != defaultRenderKit);
	context.setAttribute(RIConstants.DEFAULT_RENDER_KIT, 
			     defaultRenderKit);
    }

    /** 

    * Release the factories initialized in the initServletContextForFaces method. <P>

    * PRECONDITION: the POSTCONDITION of initServletContextForFaces is satisfied <P>

    * POSTCONDITION: The ServletContext attr set has no entries for the
    * factories from initServletContextForFaces().

    */

    public static void releaseServletContextFromFaces(ServletContext context) throws FacesException {
	Assert.assert_it(null != 
		 context.getAttribute(FactoryFinder.RENDER_KIT_FACTORY));
	context.removeAttribute(FactoryFinder.RENDER_KIT_FACTORY);

	Assert.assert_it(null != 
		 context.getAttribute(FactoryFinder.LIFECYCLE_FACTORY));
	context.removeAttribute(FactoryFinder.LIFECYCLE_FACTORY);

	Assert.assert_it(null != 
		 context.getAttribute(FactoryFinder.TREE_FACTORY));
	context.removeAttribute(FactoryFinder.TREE_FACTORY);

	Assert.assert_it(null != 
		 context.getAttribute(FactoryFinder.FACES_CONTEXT_FACTORY));
	context.removeAttribute(FactoryFinder.FACES_CONTEXT_FACTORY);

	Assert.assert_it(null != 
		 context.getAttribute(RIConstants.DEFAULT_RENDER_KIT));
	context.removeAttribute(RIConstants.DEFAULT_RENDER_KIT);
    }
			 



//
// General Methods
//

} // end of class Util
