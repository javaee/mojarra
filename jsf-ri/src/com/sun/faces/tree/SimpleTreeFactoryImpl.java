/*
 * $Id: SimpleTreeFactoryImpl.java,v 1.2 2002/10/07 20:39:52 jvisvanathan Exp $
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
import javax.faces.context.FacesContext;

/**
 *
 *  <B>SimpleTreeFactoryImpl</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: SimpleTreeFactoryImpl.java,v 1.2 2002/10/07 20:39:52 jvisvanathan Exp $
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
    public Tree getTree(FacesContext facesContext,
                        String treeId) throws FacesException {
        if ((facesContext == null) || (treeId == null)) {
            throw new NullPointerException();
        }
        return (new SimpleTreeImpl(facesContext, treeId));
    }

} // end of class SimpleTreeFactoryImpl
