/*
 * $Id: SimpleTreeFactoryImpl.java,v 1.1 2002/07/11 20:33:22 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// SimpleTreeFactoryImpl.java

package com.sun.faces.tree;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.FacesException;
import javax.faces.tree.Tree;
import javax.faces.tree.TreeFactory;
import javax.servlet.ServletContext;

/**
 *
 *  <B>SimpleTreeFactoryImpl</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: SimpleTreeFactoryImpl.java,v 1.1 2002/07/11 20:33:22 jvisvanathan Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class SimpleTreeFactoryImpl extends TreeFactory
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

    public SimpleTreeFactoryImpl() {
        super();
    }

    //
    // Class methods
    //

    //
    // General Methods
    //

    //
    // Methods from TreeFactory
    //
    public Tree getTree(ServletContext servletContext,
                        String treeId) throws FacesException {
        if ((servletContext == null) || (treeId == null)) {
            throw new NullPointerException();
        }
        return (new SimpleTreeImpl(servletContext, treeId));
    }

} // end of class SimpleTreeFactoryImpl
